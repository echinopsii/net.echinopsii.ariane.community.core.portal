/**
 * Portal Commons JSF bundle
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
package com.spectral.cc.core.portal.commons.controller;

import com.spectral.cc.core.portal.commons.consumer.UserProfileRegistryConsumer;
import com.spectral.cc.core.portal.commons.model.UserProfile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Get logged user profile from user profile registry. Used by user home view and other components view for which specific preferences has been registered.
 * This is a session managed bean.
 */
public class UserProfileController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private Subject subject;

    private String theme = "rocket"; //default

    private String firstname ;
    private String lastname ;
    private String email ;
    private String phone ;

    private String username ;

    private HashMap<String, String> preferences;
    private HashMap<String, String> sessionPreferences;

    /**
     * get logged user profile theme
     *
     * @return logged user profile theme
     */
    public String getTheme() {
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                theme = userProfile.getTheme();
            }
        }
        log.debug("Theme : {}", new Object[]{theme});
        return theme;
    }

    /**
     * set logged user theme
     *
     * @param theme primefaces theme
     */
    public void setTheme(String theme) {
        this.theme = theme;
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                userProfile.setTheme(this.theme);
            }
        }
    }

    /**
     * get logged user name
     *
     * @return logged user name
     */
    public String getUsername() {
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated())
            username = subject.getPrincipal().toString();
        else
            username = "Guest";
        return username;
    }

    /**
     * get logged user first name
     *
     * @return logged first name
     */
    public String getFirstname() {
        firstname = "";
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                firstname = userProfile.getFirstname();
            }
        }
        return firstname;
    }

    /**
     * set logged user first name
     *
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                userProfile.setFirstname(this.firstname);
            }
        }
    }

    /**
     * get logged user first name
     *
     * @return logged user first name
     */
    public String getLastname() {
        lastname = "";
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                lastname = userProfile.getLastname();
            }
        }
        return lastname;
    }

    /**
     * set logged user last name
     *
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                userProfile.setLastname(this.lastname);
            }
        }
    }

    /**
     * get logged user email
     *
     * @return logged user email
     */
    public String getEmail() {
        email = "";
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                email = userProfile.getEmail();
            }
        }
        return email;
    }

    /**
     * set logged user email
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                userProfile.setEmail(this.email);
            }
        }
    }

    /**
     * get logged user phone
     *
     * @return logged user phone
     */
    public String getPhone() {
        phone = "";
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                phone = userProfile.getPhone();
            }
        }
        return phone;
    }

    /**
     * set logged user phone
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if (userProfile !=null) {
                userProfile.setPhone(this.phone);
            }
        }
    }

    /**
     * get logged user preferences
     *
     * @return logged user preferences
     */
    public HashMap<String, String> getPreferences() {
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                preferences = userProfile.getPreferences();
                log.debug("Synchronize from db preferences. {}", new Object[]{this.preferences.toString()});
            }
        }
        return preferences;
    }

    /**
     * set logged user preferences
     *
     * @param preferences
     */
    public void setPreferences(HashMap<String, String> preferences) {
        this.preferences = preferences;
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                userProfile.setPreferences(this.preferences);
            }
        }
    }

    public void syncPreferences() {
        subject = SecurityUtils.getSubject();
        if (subject!=null && subject.isAuthenticated()) {
            UserProfile userProfile = UserProfileRegistryConsumer.getInstance().getUserProfileRegistry().getUserFromPrincipal(subject.getPrincipal().toString());
            if(userProfile !=null) {
                log.debug("Synchronize preferences to DB. {}", new Object[]{preferences.toString()});
                userProfile.setPreferences(this.preferences);
            }
        }
    }

    public HashMap<String, String> getSessionPreferences() {
        getPreferences();
        sessionPreferences = new HashMap<String,String>(preferences);
        return sessionPreferences;
    }

    public void setSessionPreferences(HashMap<String, String> sessionPreferences) {
        this.sessionPreferences = sessionPreferences;
    }
}