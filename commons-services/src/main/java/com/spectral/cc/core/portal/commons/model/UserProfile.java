/**
 * Portal Commons Services bundle
 * UserProfile
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

package com.spectral.cc.core.portal.commons.model;

import java.util.HashMap;

/**
 * Define the user profile of a connected user used to populate the user home page. <br/>
 * When a user connect to CC then if user profile doesn't exist the login controller register
 * a new user profile in the CC Portal user profile registry with user definition
 *  (TODO: coming from security module)
 *  and default preferences
 */
public class UserProfile implements Comparable<UserProfile>{

    private String firstname ;
    private String lastname ;
    private String email ;
    private String phone ;
    private String theme = "rocket";
    private String principal ; //shiro principal

    private HashMap<String, String> preferences = new HashMap<String, String>();

    /**
     * Define a new user profile from the Shiro principal
     * @param principal the login principal coming from Shiro
     * @throws Exception
     */
    public UserProfile(String principal) throws Exception {
        if (principal!=null && !principal.equals(""))
            this.principal = principal;
        else
            throw new Exception();
    }

    /**
     * Return the user first name (TODO: coming from security module)
     *
     * @return the user first name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Return the user last name (TODO: coming from security module)
     *
     * @return the user last name
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Return the user email (TODO: coming from security module)
     *
     * @return the user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Return the user phone (TODO: coming from security module)
     *
     * @return the user phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Return the user theme defined by default (TODO: or coming from user profile persistence)
     *
     * @return the user theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Return the logged shiro user principal
     *
     * @return the logged shiro user principal
     */
    public String getPrincipal() {
        return principal;
    }

    /**
     * Return the user modules preferences defined by default (TODO: or coming from user profile persistence)
     *
     * @return the user profile preferences
     */
    public HashMap<String, String> getPreferences() {
        return preferences;
    }

    /**
     * Set the user first name (TODO: persist the change into the security module)
     *
     * @param firstname the user first name
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Set the user last name (TODO: persist the change into the security module)
     *
     * @param lastname the user last name
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Set the user email (TODO: persist the change into the security module)
     *
     * @param email the user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the user phone (TODO: persist the change into the security module)
     *
     * @param phone the user phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Set the user theme (TODO: persist the change in the user profile persistence)
     *
     * @param theme the user theme
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Set the user CC modules preferences (TODO: persist the change in the user profile persistence)
     *
     * @param preferences the user CC modules preferences
     */
    public void setPreferences(HashMap<String, String> preferences) {
        this.preferences = preferences;
    }

    @Override
    public int compareTo(UserProfile that) {
        return this.principal.compareTo(that.getPrincipal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserProfile userProfile = (UserProfile) o;

        if (email != null ? !email.equals(userProfile.email) : userProfile.email != null) {
            return false;
        }
        if (firstname != null ? !firstname.equals(userProfile.firstname) : userProfile.firstname != null) {
            return false;
        }
        if (lastname != null ? !lastname.equals(userProfile.lastname) : userProfile.lastname != null) {
            return false;
        }
        if (preferences != null ? !preferences.equals(userProfile.preferences) : userProfile.preferences != null) {
            return false;
        }
        if (!principal.equals(userProfile.principal)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstname != null ? firstname.hashCode() : 0;
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + principal.hashCode();
        result = 31 * result + (preferences != null ? preferences.hashCode() : 0);
        return result;
    }
}