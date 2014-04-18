/**
 * Portal IDM wat bundle
 * Group RUD Controller
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

package com.spectral.cc.core.portal.idmwat.controller.group;

import com.spectral.cc.core.portal.idmwat.plugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.portal.idmwat.controller.role.RolesListController;
import com.spectral.cc.core.portal.idmwat.controller.user.UsersListController;
import com.spectral.cc.core.idm.base.model.jpa.Group;
import com.spectral.cc.core.idm.base.model.jpa.Role;
import com.spectral.cc.core.idm.base.model.jpa.User;
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

/**
 * This class provide stuff to display a groups list in a PrimeFaces data table, display groups, update a group and remove groups
 */
public class GroupsListController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(GroupsListController.class);

    private LazyDataModel<Group> lazyModel = new GroupLazyModel();
    private Group[]              selectedGroupList ;

    private HashMap<Long,String>     addedRole    = new HashMap<Long, String>();
    private HashMap<Long,List<Role>> removedRoles = new HashMap<Long, List<Role>>();

    private HashMap<Long,String>     addedUser    = new HashMap<Long, String>();
    private HashMap<Long,List<User>> removedUsers = new HashMap<Long, List<User>>();

    public LazyDataModel<Group> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Group> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Group[] getSelectedGroupList() {
        return selectedGroupList;
    }

    public void setSelectedGroupList(Group[] selectedGroupList) {
        this.selectedGroupList = selectedGroupList;
    }

    public HashMap<Long, String> getAddedRole() {
        return addedRole;
    }

    public void setAddedRole(HashMap<Long, String> addedRole) {
        this.addedRole = addedRole;
    }

    /**
     * Synchronize added role into a group to database
     *
     * @param group the group the UI is working on
     * @throws NotSupportedException
     * @throws SystemException
     */
    public void syncAddedRole(Group group) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (Role role : RolesListController.getAll()) {
                if (role.getName().equals(this.addedRole.get(group.getId()))) {
                    em.getTransaction().begin();
                    group = em.find(group.getClass(), group.getId());
                    role = em.find(role.getClass(), role.getId());
                    group.getRoles().add(role);
                    role.getGroups().add(group);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "Group updated successfully !",
                                                               "Group name : " + group.getName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating group " + group.getName() + " !",
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

    /**
     * Synchronize removed roles from a group to database
     *
     * @param group the group the UI is working on
     * @throws NotSupportedException
     * @throws SystemException
     */
    public void syncRemovedRoles(Group group) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            group = em.find(group.getClass(), group.getId());
            List<Role> roles2beRM = this.removedRoles.get(group.getId());
            for (Role role2beRM : roles2beRM) {
                role2beRM = em.find(role2beRM.getClass(), role2beRM.getId());
                group.getRoles().remove(role2beRM);
                role2beRM.getGroups().remove(group);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Group updated successfully !",
                                                       "Group name : " + group.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating group " + group.getName() + " !",
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

    /**
     * Synchronize added user into a group to database
     *
     * @param group the group the UI is working on
     * @throws NotSupportedException
     * @throws SystemException
     */
    public void syncAddedUser(Group group) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (User user : UsersListController.getAll()) {
                if (user.getUserName().equals(this.addedUser.get(group.getId()))) {
                    em.getTransaction().begin();
                    group = em.find(group.getClass(), group.getId());
                    user = em.find(user.getClass(), user.getId());
                    group.getUsers().add(user);
                    user.getGroups().add(group);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "Group updated successfully !",
                                                               "Group name : " + group.getName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating group " + group.getName() + " !",
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

    /**
     * Synchronize removed users from a group to database
     *
     * @param group the group the UI is working on
     * @throws NotSupportedException
     * @throws SystemException
     */
    public void syncRemovedUsers(Group group) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            group = em.find(group.getClass(), group.getId());
            List<User> users2beRM = this.removedUsers.get(group.getId());
            for (User user2beRM : users2beRM) {
                user2beRM = em.find(user2beRM.getClass(), user2beRM.getId());
                group.getUsers().remove(user2beRM);
                user2beRM.getGroups().remove(group);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Group updated successfully !",
                                                       "Group name : " + group.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating group " + group.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * When a PrimeFaces data table row is toogled init reference into the addedUser, removedUsers, addedRole and removedRoles list with the correct group id
     * When a PrimeFaces data table row is untoogled remove reference from the addedUser, removedUsers, addedRole and removedRoles list with the correct group id
     *
     * @param event provided by the UI through PrimeFaces on a row toggle
     * @throws CloneNotSupportedException
     */
    public void onRowToggle(ToggleEvent event) throws CloneNotSupportedException {
        log.debug("Row Toogled : {}", new Object[]{event.getVisibility().toString()});
        Group eventGroup = ((Group) event.getData());
        if (event.getVisibility().toString().equals("HIDDEN")) {
            addedUser.remove(eventGroup.getId());
            removedUsers.remove(eventGroup.getId());
            addedRole.remove(eventGroup.getId());
            removedRoles.remove(eventGroup.getId());
        } else {
            addedUser.put(eventGroup.getId(), "");
            removedUsers.put(eventGroup.getId(), new ArrayList<User>());
            addedRole.put(eventGroup.getId(), "");
            removedRoles.put(eventGroup.getId(), new ArrayList<Role>());
        }
    }

    /**
     * When UI actions an update merge the corresponding group bean with the correct group instance in the DB and save this instance
     *
     * @param group group bean UI is working on
     * @throws SystemException
     * @throws NotSupportedException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     */
    public void update(Group group) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            group = em.find(group.getClass(), group.getId()).setNameR(group.getName()).setDescriptionR(group.getDescription());
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Group updated successfully !",
                                                       "Group name : " + group.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating group " + group.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }

    /**
     * Remove selected groups
     */
    public void delete() {
        log.debug("Remove selected Group !");
        for (Group group2BeRemoved: selectedGroupList) {
            EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
            try {
                em.getTransaction().begin();
                group2BeRemoved = em.find(group2BeRemoved.getClass(), group2BeRemoved.getId());
                for (Role role : group2BeRemoved.getRoles())
                    role.getGroups().remove(group2BeRemoved);
                for (User user : group2BeRemoved.getUsers())
                    user.getGroups().remove(group2BeRemoved);
                em.remove(group2BeRemoved);
                em.flush();
                em.getTransaction().commit();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                           "Group deleted successfully !",
                                                           "Group name : " + group2BeRemoved.getName());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Throwable t) {
                log.debug("Throwable catched !");
                t.printStackTrace();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                           "Throwable raised while creating group " + group2BeRemoved.getName() + " !",
                                                           "Throwable message : " + t.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            } finally {
                em.close();
            }
        }
        selectedGroupList=null;
    }


    /**
     * Get all groups from the db
     *
     * @return all groups from the db
     * @throws SystemException
     * @throws NotSupportedException
     */
    public static List<Group> getAll() throws SystemException, NotSupportedException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        log.debug("Get all groups from : \n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}",
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
        CriteriaQuery<Group> criteria = builder.createQuery(Group.class);
        Root<Group> root = criteria.from(Group.class);
        criteria.select(root).orderBy(builder.asc(root.get("name")));

        List<Group> ret = em.createQuery(criteria).getResultList();
        em.close();
        return ret ;
    }
}