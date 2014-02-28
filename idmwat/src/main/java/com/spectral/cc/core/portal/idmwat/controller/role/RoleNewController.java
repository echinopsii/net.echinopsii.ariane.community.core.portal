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

package com.spectral.cc.core.portal.idmwat.controller.role;

import com.spectral.cc.core.portal.idmwat.ccplugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.portal.idmwat.controller.permission.PermissionsListController;
import com.spectral.cc.core.idm.base.model.jpa.Permission;
import com.spectral.cc.core.idm.base.model.jpa.Role;
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

public class RoleNewController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(RoleNewController.class);

    private EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();

    private String name;
    private String description;

    private List<String> permissionsToBind = new ArrayList<String>();
    private Set<Permission> permissions = new HashSet<Permission>();

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

    public List<String> getPermissionsToBind() {
        return permissionsToBind;
    }

    public void setPermissionsToBind(List<String> permissionsToBind) {
        this.permissionsToBind = permissionsToBind;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    private void bindSelectedPermissions() throws NotSupportedException, SystemException {
        for (Permission permission: PermissionsListController.getAll()) {
            for (String permissionToBind : permissionsToBind)
                if (permission.getName().equals(permissionToBind)) {
                    permission = em.find(permission.getClass(), permission.getId());
                    this.permissions.add(permission);
                    log.debug("Synced permission : {} {}", new Object[]{permission.getId(), permission.getName()});
                }
        }
    }

    public void save() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        try {
            bindSelectedPermissions();
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Exception raise while creating role " + name + " !",
                                                       "Exception message : " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setPermissions(permissions);

        try {
            em.getTransaction().begin();
            em.persist(role);
            for (Permission permission : role.getPermissions()) {
                permission.getRoles().add(role);
                em.merge(permission);
            }
            em.flush();
            em.getTransaction().commit();
            log.debug("Save new Role {} !", new Object[]{name});
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Role created successfully !",
                                                       "Role name : " + role.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while creating role " + role.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }
}