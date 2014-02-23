/**
 * IDM JSF Commons
 * Resource Create Controller
 * Copyright (C) 2014 Mathilde Ffrench
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.spectral.cc.core.portal.idm.controller.resource;

import com.spectral.cc.core.portal.idm.ccplugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.idm.commons.model.jpa.Permission;
import com.spectral.cc.core.idm.commons.model.jpa.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.transaction.*;

public class ResourceNewController {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ResourceNewController.class);

    private EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();

    private String name;
    private String description;

    @PreDestroy
    public void clean() {
        log.debug("Close entity manager");
        em.close();
    }

    public EntityManager getEm() {
        return em;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void save() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        Resource resource = new Resource();
        resource.setName(name);
        resource.setDescription(description);

        try {
            em.getTransaction().begin();
            em.persist(resource);
            for (Permission permission : resource.getPermissions()) {
                permission.setResource(resource);
                em.merge(permission);
            }
            em.flush();
            em.getTransaction().commit();
            log.debug("Save new Resource {} !", new Object[]{name});
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Resource created successfully !",
                                                       "Resource name : " + resource.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while creating resource " + resource.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }
}