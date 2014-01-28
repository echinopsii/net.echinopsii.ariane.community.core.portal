/**
 * Portal Commons Services bundle
 * Web Security Manager Proxy iPojo impl
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
package com.spectral.cc.core.portal.commons.idm.iPojo;

import com.spectral.cc.core.portal.commons.idm.WebSecurityManagerProxy;
import org.apache.felix.ipojo.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.config.Ini;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.config.WebIniSecurityManagerFactory;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * This service target is the Shiro system sharing accross CC components.<br/><br/>
 *
 * This is the iPojo implementation of {@link WebSecurityManagerProxy}. The component is instantiated at commons-services bundle startup.
 * It provides the {@link WebSecurityManagerProxy} service.
 */
@Component
@Provides
@Instantiate
public class WebSecurityManagerProxyImpl implements WebSecurityManagerProxy {

    private static final String SECURITY_MANAGER_PROXY_SERVICE_NAME = "Security Manager Proxy Service";
    private static final String SHIRO_BASE_INI_FILE_PATH = "shiro.ini";
    private static final Logger log = LoggerFactory.getLogger(WebSecurityManagerProxyImpl.class);

    private WebIniSecurityManagerFactory factory;
    private WebSecurityManager securityManager;

    private static Ini convertPathToIni() {
        Ini ini = null;
        if (StringUtils.hasText(SHIRO_BASE_INI_FILE_PATH)) {
            InputStream is = new WebSecurityManagerProxyImpl().getClass().getResourceAsStream("/" + SHIRO_BASE_INI_FILE_PATH);
            if (is != null) {
                ini = new Ini();
                ini.load(is);
            } else {
                throw new ConfigurationException("Unable to load resource path '" + SHIRO_BASE_INI_FILE_PATH + "'");
            }
        }
        return ini;
    }

    @Validate
    public void validate(){
        log.info("{} is starting...", new Object[]{SECURITY_MANAGER_PROXY_SERVICE_NAME});
        factory = new WebIniSecurityManagerFactory(convertPathToIni());
        securityManager = (WebSecurityManager)factory.getInstance();
        /*
         * TODO: investigate...
         * shiro core class loading raise Class Not Found Exception with shiro.ini configuration :
         * sessionManager = org.apache.shiro.web.session.mgt.DefaultWebSessionManager
         * securityManager.sessionManager = $sessionManager
         */
        ((DefaultWebSecurityManager)securityManager).setSessionManager(new DefaultWebSessionManager());
        /*
         * TODO: investigate...
         * same classloader problem with :
         * cookie = org.apache.shiro.web.servlet.SimpleCookie
         * cookie.name = SSOcookie
         * cookie.path = /
         * securityManager.sessionManager.sessionIdCookie = $cookie
         */
        Cookie cookie = new SimpleCookie();
        cookie.setHttpOnly(true);
        cookie.setName("CC_SSO");
        //cookie.setDomain("CC");
        cookie.setPath("/");
        // TODO: problems with rememberMe... to be investigating
        //CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        //rememberMeManager.setCookie(cookie);
        //((DefaultWebSecurityManager) securityManager).setRememberMeManager(rememberMeManager);
        ((DefaultWebSessionManager)((DefaultWebSecurityManager)securityManager).getSessionManager()).setSessionIdCookie(cookie);
        //((DefaultWebSessionManager)((DefaultWebSecurityManager)securityManager).getSessionManager()).setSessionIdCookie(rememberMeManager.getCookie());
        ((DefaultWebSessionManager)((DefaultWebSecurityManager)securityManager).getSessionManager()).setSessionIdCookieEnabled(true);
        SecurityUtils.setSecurityManager(securityManager);
        log.info("{} is started...", new Object[]{SECURITY_MANAGER_PROXY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        //log.debug("Stopping {} ...", new Object[]{SECURITY_MANAGER_PROXY_SERVICE_NAME});
        log.info("{} is stopped...", new Object[]{SECURITY_MANAGER_PROXY_SERVICE_NAME});
    }

    @Override
    public WebSecurityManager getWebSecurityManager() {
        return securityManager;
    }

    @Override
    public WebIniSecurityManagerFactory getWebIniSecurityManagerFactory() {
        return factory;
    }
}