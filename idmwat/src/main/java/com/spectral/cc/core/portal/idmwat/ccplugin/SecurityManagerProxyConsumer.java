/**
 * Portal Commons JSF bundle
 * Security Manager Proxy consumer singleton
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
package com.spectral.cc.core.portal.idmwat.ccplugin;

import com.spectral.cc.core.idm.base.proxy.WebSecurityManagerProxy;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iPojo singleton which consume the web security manager proxy service. Instantiate during portal commons-jsf bundle startup. FactoryMethod : getInstance.
 */
@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class SecurityManagerProxyConsumer {
    private static final Logger log = LoggerFactory.getLogger(SecurityManagerProxyConsumer.class);
    private static SecurityManagerProxyConsumer INSTANCE;

    @Requires
    private WebSecurityManagerProxy webSecurityManagerProxy;

    @Bind
    public void bindSecurityManagerProxy(WebSecurityManagerProxy p) {
        log.debug("Bound to security manager proxy...");
        webSecurityManagerProxy = p;
    }

    @Unbind
    public void unbindSecurityManagerProxy() {
        log.debug("Unbound to security manager proxy...");
        webSecurityManagerProxy = null;
    }

    /**
     * Get web security manager proxy binded to this consumer...
     *
     * @return web security manager proxy binded by this consumer. If null the proxy is still not binded or has been unbinded...
     */
    public WebSecurityManagerProxy getWebSecurityManagerProxy() {
        return webSecurityManagerProxy;
    }

    /**
     * Factory method for this singleton.
     *
     * @return instantiated web security manager proxy consumer
     */
    public static SecurityManagerProxyConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SecurityManagerProxyConsumer();
        }
        return INSTANCE;
    }
}
