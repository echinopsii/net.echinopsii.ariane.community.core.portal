/**
 * Portal Commons JSF bundle
 * Faces Managed Bean Portal Registry implementation
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

package com.spectral.cc.core.portal.commons.facesplugin.iPojo;

import com.spectral.cc.core.portal.commons.facesplugin.PluginFacesMBeanRegistry;
import com.spectral.cc.core.portal.commons.tools.PluginFacesMBeanConfigTools;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Provide registry tooling for external plugin faces-config.xml : register / unregister faces config URL and add / remove managed bean from this faces config.<br/><br/>
 *
 *  This is the iPojo implementation of {@link PluginFacesMBeanRegistry} for CC portal. The component is instantiated at commons-jsf bundle startup.
 *  It provides the {@link PluginFacesMBeanRegistry} service for CC portal.
 */
@Component
@Provides
@Instantiate(name="PortalPluginFacesMBeanRegistryImpl")
public class PortalPluginFacesMBeanRegistryImpl implements PluginFacesMBeanRegistry {
    private static final Logger log = LoggerFactory.getLogger(PortalPluginFacesMBeanRegistryImpl.class);
    private static final String PLUGINS_FACES_MBEAN_DIRECTORY_REGISTRY_SERVICE_NAME = "Plugin faces managed bean portal registry";

    private Set<URL> pluginFacesConfigToAdd = new HashSet<>();
    private Set<URL> pluginFacesConfigToDel = new HashSet<>();
    private ServletContext servletContext = null;

    @Validate
    public void validate() throws Exception {
        log.info("{} is started.", new Object[]{PLUGINS_FACES_MBEAN_DIRECTORY_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        pluginFacesConfigToAdd.clear();
        pluginFacesConfigToDel.clear();
        log.info("{} is stopped.", new Object[]{PLUGINS_FACES_MBEAN_DIRECTORY_REGISTRY_SERVICE_NAME});
    }

    @Override
    public void registerPluginFacesMBeanConfig(URL facesConfig) {
        pluginFacesConfigToAdd.add(facesConfig);
    }

    @Override
    public void unregisterPluginFacesMBeanConfig(URL facesConfig) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        if (pluginFacesConfigToAdd.contains(facesConfig)) {
            pluginFacesConfigToAdd.remove(facesConfig);
        } else {
            pluginFacesConfigToDel.add(facesConfig);
        }
    }

    @Override
    public void registerServletContext(ServletContext sc) {
        log.debug("Register servlet context {} into {}", new Object[]{sc.getContextPath(),PLUGINS_FACES_MBEAN_DIRECTORY_REGISTRY_SERVICE_NAME});
        servletContext = sc;
    }

    @Override
    public ServletContext getRegisteredServletContext() {
        return servletContext;
    }

    @Override
    public void addPluginFacesMBeanConfigsToServletContext() {
        if (servletContext!=null) {
            for (URL url : pluginFacesConfigToAdd) {
                PluginFacesMBeanConfigTools.registerFromDocument(PluginFacesMBeanConfigTools.parseXML(url), servletContext);
                pluginFacesConfigToAdd.remove(url);
            }
        } else {
            log.error("No servlet context registered in {}", PLUGINS_FACES_MBEAN_DIRECTORY_REGISTRY_SERVICE_NAME);
        }
    }

    @Override
    public void delPluginFacesMBeanConfigsFromServletContext() {
        if (servletContext != null) {
            for (URL url : pluginFacesConfigToDel) {
                try {
                    PluginFacesMBeanConfigTools.unregisterFromDocument(PluginFacesMBeanConfigTools.parseXML(url), servletContext);
                    pluginFacesConfigToDel.remove(url);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstantiationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } else {
            log.error("No servlet context registered in {}", PLUGINS_FACES_MBEAN_DIRECTORY_REGISTRY_SERVICE_NAME);
        }
    }
}