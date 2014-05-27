/**
 * Portal base bundle
 * Rest Resource Registry interface
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

package net.echinopsii.ariane.community.core.portal.base.plugin;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Provide registry tooling for external plugin rest.endpoints config : register / unregister rest.endpoint config URL and add / remove rest resources from these configs.
 */
public interface RestResourceRegistry {
    /**
     * register the provided rest.endpoints config
     *
     * @param restEndpointsConfig
     */
    public void registerPluginRestEndpoints(URL restEndpointsConfig);

    /**
     * unregister the provided rest.endpoints config
     *
     * @param restEndpointsConfig
     */
    public void unregisterPluginRestEndpoints(URL restEndpointsConfig);

    /**
     * register the target servlet context
     *
     * @param sc
     */
    public void registerServletContext(ServletContext sc);

    /**
     * unregister the target servlet context
     *
     * @return
     */
    public ServletContext getRegisteredServletContext();

    /**
     * add rest endpoints registered previously to the target registered servlet context
     */
    public void addPluginRestEndpointsToServletContext() throws URISyntaxException, IOException;

    /**
     * delete rest endpoints unregistered previously from the target registered servlet context
     */
    public void delPluginRestEndpointsFromServletContext() throws URISyntaxException, IOException;
}