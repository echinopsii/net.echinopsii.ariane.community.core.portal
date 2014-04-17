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

package com.spectral.cc.core.portal.base.plugin.iPojo;

import com.spectral.cc.core.portal.base.plugin.FacesMBeanRegistry;
import com.spectral.cc.core.portal.base.tools.FacesMBeanConfig;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Provide registry tooling for external plugin faces-config.xml : register / unregister faces config URL and add / remove managed bean from this faces config.<br/><br/>
 *
 *  This is the iPojo implementation of {@link com.spectral.cc.core.portal.base.plugin.FacesMBeanRegistry} for CC portal. The component is instantiated at commons-jsf bundle startup.
 *  It provides the {@link com.spectral.cc.core.portal.base.plugin.FacesMBeanRegistry} service for CC portal.
 */
@Component
@Provides
@Instantiate(name="CCPortalFacesMBeanRegistry")
public class FacesMBeanRegistryImpl implements FacesMBeanRegistry {
    private static final Logger log = LoggerFactory.getLogger(FacesMBeanRegistryImpl.class);
    private static final String PORTAL_FACES_MBEAN_REGISTRY_SERVICE_NAME = "CC Portal Faces Managed Bean Registry";

    private static volatile CopyOnWriteArrayList<URL> facesConfigToAdd = new CopyOnWriteArrayList<>();
    private static volatile CopyOnWriteArrayList<URL> facesConfigToDel = new CopyOnWriteArrayList<>();
    private static volatile ServletContext servletContext = null;

    private static Object lockSet = new Object();
    private static Object lockServletContext = new Object();

    @Validate
    public void validate() {
        log.info("{} is started", new Object[]{PORTAL_FACES_MBEAN_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        synchronized (lockSet) {
            facesConfigToAdd.clear();
            facesConfigToDel.clear();
        }
        log.info("{} is stopped", new Object[]{PORTAL_FACES_MBEAN_REGISTRY_SERVICE_NAME});
    }

    @Override
    public void registerPluginFacesMBeanConfig(URL facesConfig) {
        synchronized (lockSet) {
            facesConfigToAdd.add(facesConfig);
        }
    }

    @Override
    public void unregisterPluginFacesMBeanConfig(URL facesConfig) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        synchronized (lockSet) {
            if (facesConfigToAdd.contains(facesConfig)) {
                facesConfigToAdd.remove(facesConfig);
            } else {
                facesConfigToDel.add(facesConfig);
            }
        }
    }

    @Override
    public void registerServletContext(ServletContext sc) {
        log.debug("Register servlet context {} into {}", new Object[]{sc.getContextPath(), PORTAL_FACES_MBEAN_REGISTRY_SERVICE_NAME});
        synchronized (lockServletContext) {
            servletContext = sc;
        }
    }

    @Override
    public ServletContext getRegisteredServletContext() {
        synchronized (lockServletContext) {
            return servletContext;
        }
    }

    @Override
    public void addPluginFacesMBeanConfigsToServletContext() {
        if (servletContext!=null) {
            synchronized (lockSet) {
                for (URL url : facesConfigToAdd) {
                    synchronized (lockServletContext) {
                        FacesMBeanConfig.register(url, servletContext);
                    }
                    facesConfigToAdd.remove(url);
                }
            }
        } else {
            log.error("No servlet context registered in {}", PORTAL_FACES_MBEAN_REGISTRY_SERVICE_NAME);
        }
    }

    @Override
    public void delPluginFacesMBeanConfigsFromServletContext() {
        if (servletContext != null) {
            synchronized (lockSet) {
                for (URL url : facesConfigToDel) {
                    try {
                        synchronized (lockServletContext) {
                            FacesMBeanConfig.unregister(url, servletContext);
                        }
                        facesConfigToDel.remove(url);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InstantiationException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        } else {
            log.error("No servlet context registered in {}", PORTAL_FACES_MBEAN_REGISTRY_SERVICE_NAME);
        }
    }
}