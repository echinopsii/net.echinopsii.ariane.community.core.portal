/**
 * [DEFINE YOUR PROJECT NAME/MODULE HERE]
 * [DEFINE YOUR PROJECT DESCRIPTION HERE] 
 * Copyright (C) 26/02/14 echinopsii
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

package com.spectral.cc.core.portal.base.tools;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ListenerBootstrap;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RestEndpointsConfig {
    private static final Logger log = LoggerFactory.getLogger(RestEndpointsConfig.class);

    public static void register(URL restEPConfig, ServletContext servletContext) throws URISyntaxException, IOException {
        Object deployment = servletContext.getAttribute(ResteasyDeployment.class.getName());
        if (deployment!=null && deployment instanceof ResteasyDeployment) {
            for(String className : RestEndpointsConfig.parseRestEndpointsConfig(restEPConfig)) {
                Class clazz = null;
                try {
                    clazz = Thread.currentThread().getContextClassLoader().loadClass(className.trim());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                log.debug("Add {} to RESTEasy resources classes and registry", clazz.getName());
                ((ResteasyDeployment)deployment).getResourceClasses().add(className);
                ((ResteasyDeployment)deployment).getRegistry().addPerRequestResource(clazz);
            }
            servletContext.setAttribute(ResteasyDeployment.class.getName(), deployment);
            servletContext.setAttribute(Registry.class.getName(), ((ResteasyDeployment)deployment).getRegistry());
        } else {
            log.error("RESTEasy deployment is not or badly registered in servlet context attributes !");
        }
    }

    public static void unregister(CopyOnWriteArrayList<URL> restEPConfigs, ServletContext servletContext) throws URISyntaxException, IOException {
        Object deployment = servletContext.getAttribute(ResteasyDeployment.class.getName());
        if (deployment!=null && deployment instanceof ResteasyDeployment) {
            Object config = servletContext.getAttribute(ListenerBootstrap.class.getName());
            if (config!=null && config instanceof ListenerBootstrap) {
                ArrayList<String> resourcesClasses = new ArrayList<String>(((ResteasyDeployment)deployment).getResourceClasses());

                ((ResteasyDeployment)deployment).stop();
                for (URL restEPConfig: restEPConfigs) {
                    for(String className : RestEndpointsConfig.parseRestEndpointsConfig(restEPConfig)) {
                        resourcesClasses.remove(className);
                    }
                }

                ResteasyDeployment newDeployment = ((ListenerBootstrap)config).createDeployment();
                for(String className : resourcesClasses) { newDeployment.getResourceClasses().add(className); }
                newDeployment.start();

                servletContext.setAttribute(ListenerBootstrap.class.getName(), config);
                servletContext.setAttribute(ResteasyDeployment.class.getName(), newDeployment);
                servletContext.setAttribute(ResteasyProviderFactory.class.getName(), newDeployment.getProviderFactory());
                servletContext.setAttribute(Dispatcher.class.getName(), newDeployment.getDispatcher());
                servletContext.setAttribute(Registry.class.getName(), newDeployment.getRegistry());
            }

        } else {
            log.error("RESTEasy deployment is not or badly registered in servlet context attributes !");
        }
    }

    private static List<String> parseRestEndpointsConfig(URL restEndpointsConfig) throws URISyntaxException, IOException {
        return Files.readAllLines(Paths.get(restEndpointsConfig.toURI()), StandardCharsets.UTF_8);
    }
}