/**
 * Portal base bundle
 * Rest Resource Registry Impl
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

package net.echinopsii.ariane.community.core.portal.base.plugin.iPojo;

import net.echinopsii.ariane.community.core.portal.base.plugin.RestResourceRegistry;
import net.echinopsii.ariane.community.core.portal.base.tools.RestEndpointsConfig;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Provides
@Instantiate(name="ArianePortalRestResourceRegistry")
public class RestResourceRegistryImpl implements RestResourceRegistry {
    private static final Logger log = LoggerFactory.getLogger(RestResourceRegistryImpl.class);
    private static final String REST_RESOURCE_REGISTRY_SERVICE_NAME = "Ariane Portal REST Resource Registry";

    private static volatile CopyOnWriteArrayList<URL> restEPConfigToAdd = new CopyOnWriteArrayList<>();
    private static volatile CopyOnWriteArrayList<URL> restEPConfigToDel = new CopyOnWriteArrayList<>();
    private static volatile ServletContext            servletContext    = null;

    private static Object lockSet            = new Object();
    private static Object lockServletContext = new Object();

    @Validate
    public void validate() {
        log.info("{} is started", new Object[]{REST_RESOURCE_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate() {
        synchronized (lockSet) {
            restEPConfigToAdd.clear();
            restEPConfigToDel.clear();
        }
        log.info("{} is stopped", new Object[]{REST_RESOURCE_REGISTRY_SERVICE_NAME});
    }

    @Override
    public void registerPluginRestEndpoints(URL restEndpointsConfig) {
        synchronized (lockSet) {
            log.debug("Add new rest enddpoints config URL: {}", restEndpointsConfig);
            restEPConfigToAdd.add(restEndpointsConfig);
        }
    }

    @Override
    public void unregisterPluginRestEndpoints(URL restEndpointsConfig) {
        synchronized (lockSet) {
            if (restEPConfigToAdd.contains(restEndpointsConfig)) {
                restEPConfigToAdd.remove(restEndpointsConfig);
            } else {
                restEPConfigToDel.add(restEndpointsConfig);
            }
        }
    }

    @Override
    public void registerServletContext(ServletContext sc) {
        servletContext = sc;
    }

    @Override
    public ServletContext getRegisteredServletContext() {
        return servletContext;
    }

    @Override
    public void addPluginRestEndpointsToServletContext() throws URISyntaxException, IOException {
        log.debug("Add rest endpoints to servlet context if needed...");
        if (servletContext!=null) {
            synchronized (lockSet) {
                for (URL url : restEPConfigToAdd) {
                    synchronized (lockServletContext) {
                        RestEndpointsConfig.register(url, servletContext);
                    }
                    restEPConfigToAdd.remove(url);
                }
            }
        } else {
            log.error("No servlet context registered in {}", REST_RESOURCE_REGISTRY_SERVICE_NAME);
        }
    }

    @Override
    public void delPluginRestEndpointsFromServletContext() throws URISyntaxException, IOException {
        if (servletContext != null) {
            synchronized (lockSet) {
                synchronized (lockServletContext) {
                    RestEndpointsConfig.unregister(restEPConfigToAdd, servletContext);
                }
                restEPConfigToDel.clear();
            }
        } else {
            log.error("No servlet context registered in {}", REST_RESOURCE_REGISTRY_SERVICE_NAME);
        }
    }

}