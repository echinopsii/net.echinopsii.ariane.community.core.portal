/**
 * IDM Commons JSF bundle
 * Permission RUD Controller
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

package com.spectral.cc.core.portal.idmwat.controller.permission;

import com.spectral.cc.core.portal.idmwat.plugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.portal.idmwat.controller.resource.ResourcesListController;
import com.spectral.cc.core.portal.idmwat.controller.role.RolesListController;
import com.spectral.cc.core.idm.base.model.jpa.*;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PermissionsListController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(PermissionsListController.class);

    private LazyDataModel<Permission> lazyModel = new PermissionLazyModel();
    private Permission[]              selectedPermissionList ;

    private HashMap<Long, String> changedResource      = new HashMap<Long, String>();

    private HashMap<Long,String>     addedRole    = new HashMap<Long, String>();
    private HashMap<Long,List<Role>> removedRoles = new HashMap<Long, List<Role>>();

    public LazyDataModel<Permission> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Permission> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Permission[] getSelectedPermissionList() {
        return selectedPermissionList;
    }

    public void setSelectedPermissionList(Permission[] selectedPermissionList) {
        this.selectedPermissionList = selectedPermissionList;
    }

    public HashMap<Long, String> getChangedResource() {
        return changedResource;
    }

    public void setChangedResource(HashMap<Long, String> changedResource) {
        this.changedResource = changedResource;
    }

    public void syncResource(Permission permission) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {

            for (Resource resource1 : ResourcesListController.getAll()) {
                if (resource1.getName().equals(changedResource.get(permission.getId()))) {
                    // reset resource on UI object
                    permission.setResource(resource1);
                    em.getTransaction().begin();
                    Resource resource = em.find(resource1.getClass(), resource1.getId());
                    Permission permission2up = em.find(permission.getClass(), permission.getId());
                    if (permission2up.getResource()!=null) {
                        permission2up.getResource().getPermissions().remove(permission2up);
                        permission2up.setResource(null);
                        em.flush();
                    }
                    permission2up.setResource(resource);
                    resource.getPermissions().add(permission2up);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "Permission updated successfully !",
                                                               "Permission name : " + permission.getName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                "Throwable raised while updating permission " + permission.getName() + " !",
                                                "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, String> getAddedRole() {
        return addedRole;
    }

    public void setAddedRole(HashMap<Long, String> addedRole) {
        this.addedRole = addedRole;
    }

    public void syncAddedRole(Permission permission) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (Role role : RolesListController.getAll()) {
                if (role.getName().equals(this.addedRole.get(permission.getId()))) {
                    em.getTransaction().begin();
                    Permission permission2up = em.find(permission.getClass(), permission.getId());
                    role = em.find(role.getClass(), role.getId());
                    permission2up.getRoles().add(role);
                    // reset roles on UI object :
                    permission.setRoles(permission2up.getRoles());
                    role.getPermissions().add(permission2up);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "Permission updated successfully !",
                                                               "Permission name : " + permission.getName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating permission " + permission.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, List<Role>> getRemovedRoles() {
        return removedRoles;
    }

    public void setRemovedRoles(HashMap<Long, List<Role>> removedRoles) {
        this.removedRoles = removedRoles;
    }

    public void syncRemovedRoles(Permission permission) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            permission = em.find(permission.getClass(), permission.getId());
            List<Role> roles2beRM = this.removedRoles.get(permission.getId());
            for (Role role2beRM : roles2beRM)
                permission.getRoles().remove(role2beRM);
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Permission updated successfully !",
                                                       "Permission name : " + permission.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating permission " + permission.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void onRowToggle(ToggleEvent event) throws CloneNotSupportedException {
        log.debug("Row Toogled : {}", new Object[]{event.getVisibility().toString()});
        Permission eventPermission = ((Permission) event.getData());
        if (event.getVisibility().toString().equals("HIDDEN")) {
            addedRole.remove(eventPermission.getId());
            removedRoles.remove(eventPermission.getId());
            changedResource.remove(eventPermission.getId());
        } else {
            addedRole.put(eventPermission.getId(), "");
            removedRoles.put(eventPermission.getId(), new ArrayList<Role>());
            changedResource.put(eventPermission.getId(), "");
        }
    }

    public void update(Permission permission) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            Permission permission2up = em.find(permission.getClass(), permission.getId()).setNameR(permission.getName()).setDescriptionR(permission.getDescription());
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Permission updated successfully !",
                                                       "Permission name : " + permission.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating permission " + permission.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /*
     * Permission delete tool
     */
    public void delete() {
        log.debug("Remove selected Permission !");
        for (Permission permission2BeRemoved: selectedPermissionList) {
            EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
            try {
                em.getTransaction().begin();
                permission2BeRemoved = em.find(permission2BeRemoved.getClass(), permission2BeRemoved.getId());
                if (permission2BeRemoved.getResource()!=null)
                    permission2BeRemoved.getResource().getPermissions().remove(permission2BeRemoved);
                for (Role role : permission2BeRemoved.getRoles())
                    role.getUsers().remove(permission2BeRemoved);
                em.remove(permission2BeRemoved);
                em.flush();
                em.getTransaction().commit();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                           "Permission deleted successfully !",
                                                           "Permission name : " + permission2BeRemoved.getName());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Throwable t) {
                log.debug("Throwable catched !");
                t.printStackTrace();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                           "Throwable raised while creating user " + permission2BeRemoved.getName() + " !",
                                                           "Throwable message : " + t.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            } finally {
                em.close();
            }
        }
        selectedPermissionList=null;
    }

    public static List<Permission> getAll() throws SystemException, NotSupportedException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        log.debug("Get all permissions from : \n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}",
                         new Object[]{
                                             (Thread.currentThread().getStackTrace().length>0) ? Thread.currentThread().getStackTrace()[0].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>1) ? Thread.currentThread().getStackTrace()[1].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>2) ? Thread.currentThread().getStackTrace()[2].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>3) ? Thread.currentThread().getStackTrace()[3].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>4) ? Thread.currentThread().getStackTrace()[4].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>5) ? Thread.currentThread().getStackTrace()[5].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>6) ? Thread.currentThread().getStackTrace()[6].getClassName() : ""
                         });
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Permission> criteria = builder.createQuery(Permission.class);
        Root<Permission> root = criteria.from(Permission.class);
        criteria.select(root).orderBy(builder.asc(root.get("name")));

        List<Permission> ret = em.createQuery(criteria).getResultList();
        em.close();
        return ret ;
    }
}