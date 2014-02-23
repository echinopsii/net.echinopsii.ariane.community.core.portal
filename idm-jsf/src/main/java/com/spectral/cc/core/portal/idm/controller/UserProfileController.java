/**
 * Portal IDM JSF bundle
 * UserProfile Profile Controller
 * Copyright (C) 2013 Mathilde Ffrench
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
package com.spectral.cc.core.portal.idm.controller;

import com.spectral.cc.core.idm.commons.model.jpa.Group;
import com.spectral.cc.core.idm.commons.model.jpa.Role;
import com.spectral.cc.core.idm.commons.model.jpa.User;
import com.spectral.cc.core.idm.commons.model.jpa.UserPreference;
import com.spectral.cc.core.portal.commons.consumer.UserPreferencesRegistryConsumer;
import com.spectral.cc.core.portal.commons.model.UserPreferenceEntity;
import com.spectral.cc.core.portal.commons.model.UserPreferenceSection;
import com.spectral.cc.core.portal.idm.ccplugin.IDMJPAProviderConsumer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Get logged user profile from IDM DB. Used by user home view and other components view for which specific preferences has been registered.
 * This is a session managed bean.
 */
public class UserProfileController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private Subject subject ;
    private User    user ;

    private String theme = "rocket"; //default

    private String firstname ;
    private String lastname ;
    private String email ;
    private String phone ;

    private String pwd2 ;
    private String pwd1 ;

    private String username ;

    private List<Group> groups ;
    private List<Role>  roles  ;

    private HashMap<String, String> preferences = new HashMap<>();
    private HashMap<String, String> sessionPreferences = new HashMap<>();

    public void init() {
        subject = SecurityUtils.getSubject();
        log.debug("INIT : {}, {}, {} ", new Object[]{subject, (subject != null ? subject.isAuthenticated() : "null"), (subject != null ? subject.getPrincipal() : "null")});
        if (subject!=null && subject.isAuthenticated()) {
            EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<User> cmpCriteria = builder.createQuery(User.class);
            Root<User> cmpRoot = cmpCriteria.from(User.class);
            cmpCriteria.select(cmpRoot).where(builder.equal(cmpRoot.<String>get("userName"), subject.getPrincipal().toString()));
            TypedQuery<User> cmpQuery = em.createQuery(cmpCriteria);
            try {
                user = cmpQuery.getSingleResult();
            } catch (Exception e) {
                throw e;
            }

            log.debug("begin user profile initialization");
            em.getTransaction().begin();
            username = user.getUserName();
            firstname = user.getFirstName();
            lastname = user.getLastName();
            email = user.getEmail();
            phone = user.getPhone();
            groups = new ArrayList<Group>(user.getGroups());
            roles = new ArrayList<Role>(user.getRoles());
            for (Group group : user.getGroups())
                for (Role role : group.getRoles())
                    if (!roles.contains(role))
                        roles.add(role);


            UserPreference themePreference = null;
            for (UserPreference preference : user.getPreferences()) {
                if (preference.getPkey().equals("cctheme")) {
                    themePreference = preference;
                    break;
                }
            }
            if (themePreference==null) {
                themePreference = new UserPreference().setUserR(user).setPkeyR("cctheme").setPvalueR("rocket");
                em.persist(themePreference);
                user.getPreferences().add(themePreference);
            }
            theme = themePreference.getPvalue();

            for (UserPreferenceSection section : UserPreferencesRegistryConsumer.getInstance().getUserPreferencesRegistry().getUserPreferenceSections()) {
                for (UserPreferenceEntity entity : section.getEntityRegistry()) {
                    UserPreference userPreference = null;
                    for (UserPreference preference : user.getPreferences()) {
                        if (preference.getPkey().equals(entity.getFieldName())) {
                            userPreference = preference;
                            break;
                        }
                    }
                    if (userPreference==null) {
                        userPreference = new UserPreference().setUserR(user).setPkeyR(entity.getFieldName()).setPvalueR(entity.getFieldDefault());
                        em.persist(userPreference);
                        user.getPreferences().add(userPreference);
                    }
                    preferences.put(userPreference.getPkey(), userPreference.getPvalue());
                    sessionPreferences.put(userPreference.getPkey(), userPreference.getPvalue());
                }
            }

            log.debug("End user profile initialization");
            em.flush();
            em.getTransaction().commit();
            em.close();
        }
    }

    /**
     * get logged user profile theme
     *
     * @return logged user profile theme
     */
    public String getTheme() {
        if (subject == null || !subject.isAuthenticated())
            init();
        log.debug("Theme : {}", new Object[]{theme});
        return theme;
    }

    /**
     * set logged user theme
     *
     * @param theme primefaces theme
     */
    public void setTheme(String theme) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.theme = theme;
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        em.getTransaction().begin();
        user = em.find(user.getClass(), user.getId());
        for (UserPreference userPreference : user.getPreferences()) {
            if (userPreference.getPkey().equals("cctheme")) {
                userPreference.setPvalue(theme);
                break;
            }
        }
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    /**
     * get logged user name
     *
     * @return logged user name
     */
    public String getUsername() {
        if (subject == null || !subject.isAuthenticated())
            init();
        return username;
    }

    /**
     * set logged user name
     *
     * @param username
     */
    public void setUsername(String username) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.username = username;
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        em.getTransaction().begin();
        user = em.find(user.getClass(), user.getId());
        user.setUserName(username);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    /**
     * get logged user first name
     *
     * @return logged first name
     */
    public String getFirstname() {
        if (subject == null || !subject.isAuthenticated())
            init();
        return firstname;
    }

    /**
     * set logged user first name
     *
     * @param firstname
     */
    public void setFirstname(String firstname) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.firstname = firstname;
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        em.getTransaction().begin();
        user = em.find(user.getClass(), user.getId());
        user.setFirstName(firstname);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    /**
     * get logged user first name
     *
     * @return logged user first name
     */
    public String getLastname() {
        if (subject == null || !subject.isAuthenticated())
            init();
        return lastname;
    }

    /**
     * set logged user last name
     *
     * @param lastname
     */
    public void setLastname(String lastname) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.lastname = lastname;
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        em.getTransaction().begin();
        user = em.find(user.getClass(), user.getId());
        user.setLastName(lastname);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    /**
     * get logged user email
     *
     * @return logged user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set logged user email
     *
     * @param email
     */
    public void setEmail(String email) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.email = email;
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        em.getTransaction().begin();
        user = em.find(user.getClass(), user.getId());
        user.setEmail(email);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    /**
     * get logged user phone
     *
     * @return logged user phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * set logged user phone
     *
     * @param phone
     */
    public void setPhone(String phone) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.phone = phone;
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        em.getTransaction().begin();
        user = em.find(user.getClass(), user.getId());
        user.setPhone(phone);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    /**
     * return first password reset field
     *
     * @return first password reset field
     */
    public String getPwd1() {
        return pwd1;
    }

    /**
     * set first password reset field
     *
     * @param pwd1 first password reset field
     */
    public void setPwd1(String pwd1) {
        this.pwd1 = pwd1;
    }

    /**
     * return second password reset field
     *
     * @return second password reset field
     */
    public String getPwd2() {
        return pwd2;
    }

    /**
     * set second password reset field
     *
     * @param pwd2  second password reset field
     */
    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }

    public void resetPassword() {
        if (subject == null || !subject.isAuthenticated())
            init();
        if (this.pwd1 != null && this.pwd1.equals(this.pwd2)) {
            EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
            em.getTransaction().begin();
            user = em.find(user.getClass(), user.getId());
            user.setPassword(this.pwd1);
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "User password updated successfully !","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            em.close();
            pwd1="";
            pwd2="";
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * get logged user preferences
     *
     * @return logged user preferences
     */
    public HashMap<String, String> getPreferences() {
        return preferences;
    }

    /**
     * set logged user preferences
     *
     * @param preferences
     */
    public void setPreferences(HashMap<String, String> preferences) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.preferences = preferences;
        this.sessionPreferences = preferences;
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        em.getTransaction().begin();
        user = em.find(user.getClass(), user.getId());
        for(String key : preferences.keySet()) {
            for (UserPreference userPreference : user.getPreferences()) {
                if (userPreference.getPkey().equals(key))
                    userPreference.setPvalue(preferences.get(key));
            }
        }
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void syncPreferences() {
        this.setPreferences(this.preferences);
    }

    public HashMap<String, String> getSessionPreferences() {
        if (subject == null || !subject.isAuthenticated())
            init();
        return sessionPreferences;
    }

    public void setSessionPreferences(HashMap<String, String> sessionPreferences) {
        if (subject == null || !subject.isAuthenticated())
            init();
        this.sessionPreferences = sessionPreferences;
    }
}