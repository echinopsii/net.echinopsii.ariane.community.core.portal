/**
 * IDM JSF Commons
 * Group Create Controller
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

package com.spectral.cc.core.portal.idm.controller.group;

import com.spectral.cc.core.portal.idm.ccplugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.portal.idm.controller.role.RolesListController;
import com.spectral.cc.core.idm.commons.model.jpa.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.transaction.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupNewController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(GroupNewController.class);

    private EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();

    private String name;
    private String description;

    private List<String> rolesToBind = new ArrayList<String>();
    private Set<Role> roles = new HashSet<Role>();

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
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

    public List<String> getRolesToBind() {
        return rolesToBind;
    }

    public void setRolesToBind(List<String> rolesToBind) {
        this.rolesToBind = rolesToBind;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private void bindSelectedRoles() throws NotSupportedException, SystemException {
        for (Role role: RolesListController.getAll()) {
            for (String roleToBind : rolesToBind)
                if (role.getName().equals(roleToBind)) {
                    this.roles.add(role);
                    log.debug("Synced role : {} {}", new Object[]{role.getId(), role.getName()});
                    break;
                }
        }
    }

    public void save() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        try {
            bindSelectedRoles();
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Exception raise while creating group " + name + " !",
                                                       "Exception message : " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setRoles(roles);

        try {
            em.getTransaction().begin();
            em.persist(group);
            for (Role role : group.getRoles()) {
                role = em.find(role.getClass(), role.getId());
                role.getGroups().add(group);
            }
            em.flush();
            em.getTransaction().commit();
            log.debug("Save new Group {} !", new Object[]{name});
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Group created successfully !",
                                                       "Group name : " + group.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while creating group " + group.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }
}