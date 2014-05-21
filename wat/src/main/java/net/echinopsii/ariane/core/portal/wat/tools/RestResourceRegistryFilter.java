/**
 * Portal wat bundle
 * REST resource Registry Filter
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

package net.echinopsii.ariane.core.portal.wat.tools;

import net.echinopsii.ariane.core.portal.wat.plugin.PortalRestResourceRegistryConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This servlet filter is an helper to add new REST endpoint coming from Ariane plugin to Ariane servlet context thanks the cc portal plugin faces mbean registry consumer.<br/>
 * It must be configured properly in the web.xml file :<br/><br/>
 * <pre>
 *  <!-- Portal Rest Resource Registry Filter -->
 *  <filter>
 *      <filter-name>ArianePortalRestResourceRegistryFilter</filter-name>
 *      <filter-class>net.echinopsii.ariane.core.portal.wat.tools.RestResourceRegistryFilter</filter-class>
 *  </filter>
 *  <filter-mapping>
 *    <filter-name>ArianePortalRestResourceRegistryFilter</filter-name>
 *    <url-pattern>/rest/*</url-pattern>
 *  </filter-mapping>
 * </pre>
 */
public class RestResourceRegistryFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RestResourceRegistryFilter.class);

    /**
     * The filter configuration object we are associated with. If this value is null, this filter instance is not currently
     * configured.
     */
    protected FilterConfig filterConfig = null;

    /**
     * Register the servlet context into the portal rest resource registry
     *
     * @param filterConfig the filter config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        while(PortalRestResourceRegistryConsumer.getInstance().getPortalRestResourceRegistry()==null)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        PortalRestResourceRegistryConsumer.getInstance().getPortalRestResourceRegistry().registerServletContext(filterConfig.getServletContext());
    }

    /**
     * Ask the rest resource registry to add registered rest endpoint to the portal servlet context,
     * and then pass control to the next filter
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            PortalRestResourceRegistryConsumer.getInstance().getPortalRestResourceRegistry().addPluginRestEndpointsToServletContext();
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}