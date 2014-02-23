/**
 * IDM JSF Commons
 * User Create Controller
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

package com.spectral.cc.core.portal.idm.controller.user;

import com.spectral.cc.core.portal.idm.ccplugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.portal.idm.controller.group.GroupsListController;
import com.spectral.cc.core.portal.idm.controller.role.RolesListController;
import com.spectral.cc.core.idm.commons.model.jpa.Group;
import com.spectral.cc.core.idm.commons.model.jpa.Role;
import com.spectral.cc.core.idm.commons.model.jpa.User;
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

public class UserNewController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UserNewController.class);

    private EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String pwd1 ;
    private String pwd2 ;

    private List<String> groupsToBind = new ArrayList<String>();
    private Set<Group> groups = new HashSet<Group>();

    private List<String> rolesToBind = new ArrayList<String>();
    private Set<Role> roles = new HashSet<Role>();

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd1() {
        return pwd1;
    }

    public void setPwd1(String pwd1) {
        this.pwd1 = pwd1;
    }

    public String getPwd2() {
        return pwd2;
    }

    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }

    public List<String> getGroupsToBind() {
        return groupsToBind;
    }

    public void setGroupsToBind(List<String> groupsToBind) {
        this.groupsToBind = groupsToBind;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
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

    private void bindSelectedGroups() throws NotSupportedException, SystemException {
        for (Group group: GroupsListController.getAll()) {
            for (String groupToBind : groupsToBind)
                if (group.getName().equals(groupToBind)) {
                    this.groups.add(group);
                    log.debug("Synced group : {} {}", new Object[]{group.getId(), group.getName()});
                    break;
                }
        }
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
            bindSelectedGroups();
            bindSelectedRoles();
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Exception raise while creating user " + userName + " !",
                                                       "Exception message : " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        User user = new User();
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(pwd2);
        user.setGroups(groups);
        user.setRoles(roles);

        try {
            em.getTransaction().begin();
            em.persist(user);
            for (Group group : user.getGroups()) {
                group = em.find(group.getClass(), group.getId());
                group.getUsers().add(user);
            }
            for (Role role : user.getRoles()) {
                role = em.find(role.getClass(), role.getId());
                role.getUsers().add(user);
            }
            em.flush();
            em.getTransaction().commit();
            log.debug("Save new User {} !", new Object[]{userName});
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "User created successfully !",
                                                       "User name : " + user.getUserName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while creating user " + user.getUserName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }
}