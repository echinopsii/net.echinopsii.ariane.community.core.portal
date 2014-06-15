/**
 * Portal IDM wat bundle
 * User RUD Controller
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

package net.echinopsii.ariane.community.core.portal.idmwat.controller.user;

import net.echinopsii.ariane.community.core.idm.base.model.jpa.UserPreference;
import net.echinopsii.ariane.community.core.portal.idmwat.plugin.IDMJPAProviderConsumer;
import net.echinopsii.ariane.community.core.portal.idmwat.controller.group.GroupsListController;
import net.echinopsii.ariane.community.core.portal.idmwat.controller.role.RolesListController;
import net.echinopsii.ariane.community.core.idm.base.model.jpa.Group;
import net.echinopsii.ariane.community.core.idm.base.model.jpa.Role;
import net.echinopsii.ariane.community.core.idm.base.model.jpa.User;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provide stuff to display a users list in a PrimeFaces data table, display users, update a user and remove users
 */
public class UsersListController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UsersListController.class);

    private LazyDataModel<User> lazyModel = new UserLazyModel();
    private User[]              selectedUserList ;

    private HashMap<Long,String>      addedGroup    = new HashMap<Long, String>();
    private HashMap<Long,List<Group>> removedGroups = new HashMap<Long, List<Group>>();

    private HashMap<Long,String>     addedRole    = new HashMap<Long, String>();
    private HashMap<Long,List<Role>> removedRoles = new HashMap<Long, List<Role>>();

    public LazyDataModel<User> getLazyModel() {
        return lazyModel;
    }

    public User[] getSelectedUserList() {
        return selectedUserList;
    }

    public void setSelectedUserList(User[] selectedUserList) {
        this.selectedUserList = selectedUserList;
    }

    public HashMap<Long, String> getAddedGroup() {
        return addedGroup;
    }

    public void setAddedGroup(HashMap<Long, String> addedGroup) {
        this.addedGroup = addedGroup;
    }

    /**
     * Synchronize added group into a user to database
     *
     * @param user the user the UI is working on
     */
    public void syncAddedGroup(User user) {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (Group group : GroupsListController.getAll()) {
                if (group.getName().equals(this.addedGroup.get(user.getId()))) {
                    em.getTransaction().begin();
                    user = em.find(user.getClass(), user.getId());
                    group = em.find(group.getClass(), group.getId());
                    user.getGroups().add(group);
                    group.getUsers().add(user);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "User updated successfully !",
                                                               "User name : " + user.getUserName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating user " + user.getUserName() + " !",
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

    /**
     * Synchronize removed group from a user to database
     *
     * @param user the user the UI is working on
     */
    public void syncRemovedGroups(User user) {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            user = em.find(user.getClass(), user.getId());
            List<Group> groups2beRM = this.removedGroups.get(user.getId());
            for (Group group2beRM : groups2beRM) {
                group2beRM = em.find(group2beRM.getClass(), group2beRM.getId());
                user.getGroups().remove(group2beRM);
                group2beRM.getUsers().remove(user);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "User updated successfully !",
                                                       "User name : " + user.getUserName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating user " + user.getUserName() + " !",
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

    /**
     * Synchronize added role into a user to database
     *
     * @param user the user the UI is working on
     */
    public void syncAddedRole(User user) {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            for (Role role : RolesListController.getAll()) {
                if (role.getName().equals(this.addedRole.get(user.getId()))) {
                    em.getTransaction().begin();
                    user = em.find(user.getClass(), user.getId());
                    role = em.find(role.getClass(), role.getId());
                    user.getRoles().add(role);
                    role.getUsers().add(user);
                    em.flush();
                    em.getTransaction().commit();
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                               "User updated successfully !",
                                                               "User name : " + user.getUserName());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating user " + user.getUserName() + " !",
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
     * Synchronize removed role from a user to database
     *
     * @param user the user the UI is working on
     */
    public void syncRemovedRoles(User user) {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            List<Role> roles2beRM = this.removedRoles.get(user.getId());
            em.getTransaction().begin();
            user = em.find(user.getClass(), user.getId());
            for (Role role2beRM : roles2beRM) {
                role2beRM = em.find(role2beRM.getClass(), role2beRM.getId());
                user.getRoles().remove(role2beRM);
                role2beRM.getUsers().remove(user);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "User updated successfully !",
                                                       "User name : " + user.getUserName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating user " + user.getUserName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * When a PrimeFaces data table row is toogled init reference into the addedGroup, removedGroups, addedRole, removedRoles lists
     * with the correct resource id
     * When a PrimeFaces data table row is untoogled remove reference from the addedGroup, removedGroups, addedRole, removedRoles lists
     * with the correct resource id
     *
     * @param event provided by the UI through PrimeFaces on a row toggle
     */
    public void onRowToggle(ToggleEvent event) {
        log.debug("Row Toogled : {}", new Object[]{event.getVisibility().toString()});
        User eventUser = ((User) event.getData());
        if (event.getVisibility().toString().equals("HIDDEN")) {
            addedGroup.remove(eventUser.getId());
            removedGroups.remove(eventUser.getId());
            addedRole.remove(eventUser.getId());
            removedRoles.remove(eventUser.getId());
        } else {
            addedGroup.put(eventUser.getId(), "");
            removedGroups.put(eventUser.getId(), new ArrayList<Group>());
            addedRole.put(eventUser.getId(), "");
            removedRoles.put(eventUser.getId(), new ArrayList<Role>());
        }
    }

    /**
     * When UI actions an update merge the corresponding user bean with the correct user instance in the DB and save this instance
     *
     * @param user the user the UI is working on
     */
    public void update(User user) {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            user = em.find(user.getClass(), user.getId()).setUserNameR(user.getUserName()).setFirstNameR(user.getFirstName()).setLastNameR(user.getLastName())
                                                         .setEmailR(user.getEmail()).setPhoneR(user.getPhone());
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "User updated successfully !",
                                                       "User name : " + user.getUserName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating user " + user.getUserName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * Remove selected users
     */
    public void delete() {
        log.debug("Remove selected User !");
        for (User user2BeRemoved: selectedUserList) {
            EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
            try {
                em.getTransaction().begin();
                user2BeRemoved = em.find(user2BeRemoved.getClass(), user2BeRemoved.getId());
                for (Group group : user2BeRemoved.getGroups()) {
                    group.getUsers().remove(user2BeRemoved);
                    if (group.getName().equals(user2BeRemoved.getUserName())) {
                        user2BeRemoved.getGroups().remove(group);
                        em.remove(group);
                    }
                }
                for (Role role : user2BeRemoved.getRoles())
                    role.getUsers().remove(user2BeRemoved);
                for (UserPreference pref : user2BeRemoved.getPreferences())
                    em.remove(pref);
                user2BeRemoved.getPreferences().clear();
                em.flush();
                em.remove(user2BeRemoved);
                em.getTransaction().commit();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                           "User deleted successfully !",
                                                           "User name : " + user2BeRemoved.getUserName());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Throwable t) {
                log.debug("Throwable catched !");
                t.printStackTrace();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                           "Throwable raised while creating user " + user2BeRemoved.getUserName() + " !",
                                                           "Throwable message : " + t.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            } finally {
                em.close();
            }
        }
        selectedUserList=null;
    }

    /**
     * Get all users from the db
     *
     * @return all users from the db
     */
    public static List<User> getAll() {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        log.debug("Get all users from : \n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}",
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
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root).orderBy(builder.asc(root.get("userName")));
        List<User> ret = em.createQuery(criteria).getResultList();
        em.close();
        return ret ;
    }
}