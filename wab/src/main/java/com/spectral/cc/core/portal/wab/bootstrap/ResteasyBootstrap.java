/**
 * Portal Web App Bundle
 * RESTEasy Bootstrap
 * Copyright (C) 2014 Mathilde Ffrench
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

package com.spectral.cc.core.portal.wab.bootstrap;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ListenerBootstrap;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ResteasyBootstrap implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(ResteasyBootstrap.class);
    private static final String RESTEASY_BOOTSTRAP_NAME = "CC Portal RESTEasy Bootsrap";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();

        ListenerBootstrap config = new ListenerBootstrap(servletContext);
        ResteasyDeployment deployment = config.createDeployment();
        deployment.start();

        servletContext.setAttribute(ListenerBootstrap.class.getName(), config);
        servletContext.setAttribute(ResteasyDeployment.class.getName(), deployment);
        servletContext.setAttribute(ResteasyProviderFactory.class.getName(), deployment.getProviderFactory());
        servletContext.setAttribute(Dispatcher.class.getName(), deployment.getDispatcher());
        servletContext.setAttribute(Registry.class.getName(), deployment.getRegistry());
        log.info("{} servlet context is initialized", RESTEASY_BOOTSTRAP_NAME);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        Object deployment = servletContext.getAttribute(ResteasyDeployment.class.getName());
        if (deployment!=null && deployment instanceof ResteasyDeployment) {
            ((ResteasyDeployment)deployment).stop();
            log.info("{} servlet context is destroyed", RESTEASY_BOOTSTRAP_NAME);
        } else {
            log.error("RESTEasy deployment is not or badly registered in servlet context attributes !");
        }
    }
}