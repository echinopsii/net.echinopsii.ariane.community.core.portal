/**
 * Portal IDM wat bundle
 * Login controller
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
package com.spectral.cc.core.portal.idmwat.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

/**
 * Provide Shiro login to CC web applications. Called by login view.<br/>
 * This is a request managed bean
 */
public class LoginController implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private String username;
    private String password;
    private Subject subject;

    /**
     * get user name
     *
     * @return login user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * set user name
     *
     * @param username login user name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get password
     *
     * @return login password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * login action listener
     *
     * if successfully logged (TODO: retrieved persisted user profile or)
     * register a new user profile according to default preferences (TODO: and idm user data)
     *
     * @throws Exception
     */
    public void login() throws Exception {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;

        subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
        } catch (Exception e) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
        }

        log.debug("Is authenticated:{} ; Is remembered:{}", new Object[]{subject.isAuthenticated(), subject.isRemembered()});
        log.debug("Principal:{}", new Object[]{(subject.getPrincipal() != null) ? subject.getPrincipal().toString() : "guest"});

        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
        context.addCallbackParam("redirectTo", "/");
    }

    /**
     * logout action listener
     *
     * @throws IOException
     */
    public void logout() throws IOException {
        subject = SecurityUtils.getSubject();
        String principal = subject.getPrincipal().toString();
        if (subject!=null) {
            log.debug("{} logging out ... ({})", new Object[]{principal, subject.isAuthenticated()});
            subject.logout();
            log.debug("{} is out ... ({})", new Object[]{principal, subject.isAuthenticated()});
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/CC");
    }
}
