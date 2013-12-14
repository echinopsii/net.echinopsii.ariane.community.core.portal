/**
 * Portal Commons Services bundle
 * User
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

public class User implements Comparable<User>{

    private String firstname ;
    private String lastname ;
    private String email ;
    private String phone ;
    private String theme = "rocket";
    private String principal ; //shiro principal

    private HashMap<String, String> preferences = new HashMap<String, String>();

    public User(String principal) throws Exception {
        if (principal!=null && !principal.equals(""))
            this.principal = principal;
        else
            throw new Exception();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getTheme() {
        return theme;
    }

    public String getPrincipal() {
        return principal;
    }

    public HashMap<String, String> getPreferences() {
        return preferences;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public void setPreferences(HashMap<String, String> preferences) {
        this.preferences = preferences;
    }

    @Override
    public int compareTo(User that) {
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

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) {
            return false;
        }
        if (firstname != null ? !firstname.equals(user.firstname) : user.firstname != null) {
            return false;
        }
        if (lastname != null ? !lastname.equals(user.lastname) : user.lastname != null) {
            return false;
        }
        if (preferences != null ? !preferences.equals(user.preferences) : user.preferences != null) {
            return false;
        }
        if (!principal.equals(user.principal)) {
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