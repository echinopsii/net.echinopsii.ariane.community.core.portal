/**
 * Portal wat bundle
 * Plugin Faces Managed Bean Filter
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
package net.echinopsii.ariane.community.core.portal.wat.tools;

import net.echinopsii.ariane.community.core.portal.wat.plugin.PortalFacesMBeanRegistryConsumer;

import javax.servlet.*;
import java.io.IOException;

/**
 * This servlet filter is an helper to add new Managed Bean coming from Ariane plugin to Ariane servlet context thanks the cc portal plugin faces mbean registry consumer.<br/>
 * It must be configured properly in the web.xml file :<br/><br/>
 * <pre>
 *         <!-- Ariane Plugin Faces Managed Bean Registry Filter -->
 *         <filter>
 *              <filter-name>ArianePluginFacesMBeanRegistryFilter</filter-name>
 *              <filter-class>net.echinopsii.ariane.core.portal.commons.tools.FacesMBeanRegistryFilter</filter-class>
 *         </filter>
 *         <filter-mapping>
 *              <filter-name>ArianePluginFacesMBeanRegistryFilter</filter-name>
 *              <url-pattern>*.jsf</url-pattern>
 *         </filter-mapping>
 * </pre>
 */
public class FacesMBeanRegistryFilter implements Filter {

    /**
     * The filter configuration object we are associated with. If this value is null, this filter instance is not currently
     * configured.
     */
    protected FilterConfig filterConfig = null;

    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Ask the faces managed bean registry to add registers faces managed bean to the portal servlet context,
     * and then pass control to the next filter
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     *
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        PortalFacesMBeanRegistryConsumer.getInstance().getPortalPluginFacesMBeanRegistry().addPluginFacesMBeanConfigsToServletContext();
        // pass control on to the next filter
        chain.doFilter(request, response);
    }

    /**
     * Register the servlet context into the portal faces managed bean registry
     *
     * @param filterConfig the filter config
     *
     * @throws javax.servlet.ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        while(PortalFacesMBeanRegistryConsumer.getInstance().getPortalPluginFacesMBeanRegistry()==null)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        PortalFacesMBeanRegistryConsumer.getInstance().getPortalPluginFacesMBeanRegistry().registerServletContext(filterConfig.getServletContext());
    }
}