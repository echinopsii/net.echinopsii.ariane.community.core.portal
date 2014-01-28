/**
 * Portal Commons JSF bundle
 * Character Encoding Filter
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
package com.spectral.cc.core.portal.commons.tools;

import javax.servlet.*;
import java.io.IOException;

/**
 * JSF character encoding filter. Called during a war startup if properly configured.<br/><br/>
 *
 * Example of war configuration (web.xml) to use this filter : <br/><br/>
 *
 * <pre>
 * <!-- Character Encoding Filter -->
 * <filter>
 *  <filter-name>CharacterEncodingFilter</filter-name>
 *  <filter-class>com.spectral.cc.core.portal.commons.tools.CharacterEncodingFilter</filter-class>
 *  <init-param>
 *      <description>override any encodings from client</description>
 *      <param-name>ignore</param-name>
 *      <param-value>true</param-value>
 *  </init-param>
 *      <init-param>
 *      <description>the encoding to use</description>
 *      <param-name>encoding</param-name>
 *      <param-value>UTF-8</param-value>
 *  </init-param>
 * </filter>
 * <filter-mapping>
 *  <filter-name>CharacterEncodingFilter</filter-name>
 *      <url-pattern>*.jsf</url-pattern>
 *  </filter-mapping>
 * </pre>
 */
public class CharacterEncodingFilter implements Filter {

    /**
     * The default character encoding to set for requests that pass through this filter.
     */
    protected String encoding = null;

    /**
     * The filter configuration object we are associated with. If this value is null, this filter instance is not currently
     * configured.
     */
    protected FilterConfig filterConfig = null;

    /**
     * Should a character encoding specified by the client be ignored?
     */
    protected boolean ignore = true;

    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    /**
     * Set the client request encoding filter to encoding param value if it's not specified by the client or the ignore param has been defined to true.
     *
     * @param request the client request
     * @param response the servlet response
     * @param chain the filter chain to forward
     *
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        // Conditionally select and set the character encoding to be used
        if (ignore || (request.getCharacterEncoding() == null)) {
            if (this.encoding != null) {
                request.setCharacterEncoding(this.encoding);
            }
        }

        // Pass control on to the next filter
        chain.doFilter(request, response);
    }

    /**
     * Read the filter config and set encoding and ignore params
     *
     * @param filterConfig
     *
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");

        String value = filterConfig.getInitParameter("ignore");
        if (value == null) {
            this.ignore = true;
        } else if (value.equalsIgnoreCase("true")) {
            this.ignore = true;
        } else if (value.equalsIgnoreCase("yes")) {
            this.ignore = true;
        } else {
            this.ignore = false;
        }
    }
}
