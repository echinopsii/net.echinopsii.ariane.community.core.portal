/**
 * IDM Commons JSF bundle
 * Roles RUD Controller
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

package com.spectral.cc.core.portal.idm.controller.role;

import com.spectral.cc.core.portal.idm.ccplugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.portal.idm.controller.group.GroupsListController;
import com.spectral.cc.core.portal.idm.controller.permission.PermissionsListController;
import com.spectral.cc.core.portal.idm.controller.user.UsersListController;
import com.spectral.cc.core.idm.commons.model.jpa.Group;
import com.spectral.cc.core.idm.commons.model.jpa.Permission;
import com.spectral.cc.core.idm.commons.model.jpa.Role;
import com.spectral.cc.core.idm.commons.model.jpa.User;
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

public class RolesListController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(RolesListController.class);

    private LazyDataModel<Role> lazyModel = new RoleLazyModel();
    private Role[]              selectedRoleList ;

    private HashMap<Long,String>           addedPermission    = new HashMap<Long, String>();
    private HashMap<Long,List<Permission>> removedPermissions = new HashMap<Long, List<Permission>>();

    private HashMap<Long,String>      addedUser    = new HashMap<Long, String>();
    private HashMap<Long,List<User>>  removedUsers = new HashMap<Long, List<User>>();

    private HashMap<Long,String>      addedGroup    = new HashMap<Long, String>();
    private HashMap<Long,List<Group>> removedGroups = new HashMap<Long, List<Group>>();

    public LazyDataModel<Role> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Role> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Role[] getSelectedRoleList() {
        return selectedRoleList;
    }

    public void setSelectedRoleList(Role[] selectedRoleList) {
        this.selectedRoleList = selectedRoleList;
    }

    public HashMap<Long, String> getAddedPermission() {
        return addedPermission;
    }

    public void setAddedPermission(HashMap<Long, String> addedPermission) {
        this.addedPermission = addedPermission;
    }

    public void syncAddedPermission(Role role) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (Permission permission : PermissionsListController.getAll()) {
                if (permission.getName().equals(this.addedPermission.get(role.getId()))) {
                    em.getTransaction().begin();
                    role = em.find(role.getClass(), role.getId());
                    permission = em.find(permission.getClass(),permission.getId());
                    role.getPermissions().add(permission);
                    permission.getRoles().add(role);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "Role updated successfully !",
                                                               "Role name : " + role.getName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating role " + role.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, List<Permission>> getRemovedPermissions() {
        return removedPermissions;
    }

    public void setRemovedPermissions(HashMap<Long, List<Permission>> removedPermissions) {
        this.removedPermissions = removedPermissions;
    }

    public void syncRemovedPermissions(Role role) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            List<Permission> permissions2beRM = this.removedPermissions.get(role.getId());
            em.getTransaction().begin();
            role = em.find(role.getClass(), role.getId());
            for (Permission permission2beRM : permissions2beRM) {
                permission2beRM = em.find(permission2beRM.getClass(), permission2beRM.getId());
                role.getPermissions().remove(permission2beRM);
                permission2beRM.getRoles().remove(role);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Role updated successfully !",
                                                       "Role name : " + role.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating role " + role.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, String> getAddedUser() {
        return addedUser;
    }

    public void setAddedUser(HashMap<Long, String> addedUser) {
        this.addedUser = addedUser;
    }

    public void syncAddedUser(Role role) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (User user : UsersListController.getAll()) {
                if (user.getUserName().equals(this.addedUser.get(role.getId()))) {
                    em.getTransaction().begin();
                    role = em.find(role.getClass(), role.getId());
                    user = em.find(user.getClass(), user.getId());
                    role.getUsers().add(user);
                    user.getRoles().add(role);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "Role updated successfully !",
                                                               "Role name : " + role.getName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating role " + role.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, List<User>> getRemovedUsers() {
        return removedUsers;
    }

    public void setRemovedUsers(HashMap<Long, List<User>> removedUsers) {
        this.removedUsers = removedUsers;
    }

    public void syncRemovedUsers(Role role) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            List<User> users2beRM = this.removedUsers.get(role.getId());
            em.getTransaction().begin();
            role = em.find(role.getClass(), role.getId());
            for (User user2beRM : users2beRM) {
                user2beRM = em.find(user2beRM.getClass(), user2beRM.getId());
                role.getUsers().remove(user2beRM);
                user2beRM.getRoles().remove(role);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Role updated successfully !",
                                                       "Role name : " + role.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating role " + role.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, String> getAddedGroup() {
        return addedGroup;
    }

    public void setAddedGroup(HashMap<Long, String> addedGroup) {
        this.addedGroup = addedGroup;
    }

    public void syncAddedGroup(Role role) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (Group group : GroupsListController.getAll()) {
                if (group.getName().equals(this.addedGroup.get(role.getId()))) {
                    em.getTransaction().begin();
                    role = em.find(role.getClass(), role.getId());
                    group = em.find(group.getClass(), group.getId());
                    role.getGroups().add(group);
                    group.getRoles().add(role);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "Role updated successfully !",
                                                               "Role name : " + role.getName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating role " + role.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, List<Group>> getRemovedGroups() {
        return removedGroups;
    }

    public void setRemovedGroups(HashMap<Long, List<Group>> removedGroups) {
        this.removedGroups = removedGroups;
    }

    public void syncRemovedGroups(Role role) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            role = em.find(role.getClass(), role.getId());
            List<Group> groups2beRM = this.removedGroups.get(role.getId());
            for (Group group2beRM : groups2beRM) {
                group2beRM = em.find(group2beRM.getClass(), group2beRM.getId());
                role.getGroups().remove(group2beRM);
                group2beRM.getRoles().remove(role);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Role updated successfully !",
                                                       "Role name : " + role.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating role " + role.getName() + " !",
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
        Role eventRole = ((Role) event.getData());
        if (event.getVisibility().toString().equals("HIDDEN")) {
            addedPermission.remove(eventRole.getId());
            removedPermissions.remove(eventRole.getId());
            addedGroup.remove(eventRole.getId());
            removedGroups.remove(eventRole.getId());
            addedUser.remove(eventRole.getId());
            removedUsers.remove(eventRole.getId());
        } else {
            addedPermission.put(eventRole.getId(), "");
            removedPermissions.put(eventRole.getId(), new ArrayList<Permission>());
            addedGroup.put(eventRole.getId(), "");
            removedGroups.put(eventRole.getId(), new ArrayList<Group>());
            addedUser.put(eventRole.getId(), "");
            removedUsers.put(eventRole.getId(), new ArrayList<User>());
        }
    }

    public void update(Role role) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            role = em.find(role.getClass(), role.getId()).setNameR(role.getName()).setDescriptionR(role.getDescription());
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Role updated successfully !",
                                                       "Role name : " + role.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating role " + role.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /*
     * Role delete tool
     */
    public void delete() {
        log.debug("Remove selected Role !");
        for (Role role2BeRemoved: selectedRoleList) {
            EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
            try {
                em.getTransaction().begin();
                role2BeRemoved = em.find(role2BeRemoved.getClass(), role2BeRemoved.getId());
                for (Group group : role2BeRemoved.getGroups())
                    group.getRoles().remove(role2BeRemoved);
                for (User user : role2BeRemoved.getUsers())
                    user.getRoles().remove(role2BeRemoved);
                for (Permission permission : role2BeRemoved.getPermissions())
                    permission.getRoles().remove(role2BeRemoved);
                em.remove(role2BeRemoved);
                em.flush();
                em.getTransaction().commit();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                           "Role deleted successfully !",
                                                           "Role name : " + role2BeRemoved.getName());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Throwable t) {
                log.debug("Throwable catched !");
                t.printStackTrace();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                           "Throwable raised while creating role " + role2BeRemoved.getName() + " !",
                                                           "Throwable message : " + t.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            } finally {
                em.close();
            }
        }
        selectedRoleList=null;
    }

    /*
     * User join tool
     */
    public static List<Role> getAll() throws SystemException, NotSupportedException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        log.debug("Get all roles from : \n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}",
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
        CriteriaQuery<Role> criteria = builder.createQuery(Role.class);
        Root<Role> root = criteria.from(Role.class);
        criteria.select(root).orderBy(builder.asc(root.get("name")));

        List<Role> ret = em.createQuery(criteria).getResultList();
        em.close();
        return ret ;
    }
}