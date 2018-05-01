/**
 * IDM Web Application Tooling
 * IDM REST Bootstrap
 *
 * Copyright (C) 2018 echinopsii
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
package net.echinopsii.ariane.community.core.portal.idmwat;

import net.echinopsii.ariane.community.core.portal.base.plugin.RestResourceRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Provides(properties= {@StaticServiceProperty(name="targetArianeComponent", type="java.lang.String", value="Portal")})
@Instantiate
public class IDMWatBootstrap {
    private static final Logger log = LoggerFactory.getLogger(IDMWatBootstrap.class);
    private static final String IDMWAT_COMPONENT = "Ariane IDM WAT Component";

    private static final String basePath = "/META-INF";
    private static final String REST_EP_FILE_PATH = basePath + "/rest.endpoints";

    @Requires
    private RestResourceRegistry restResourceRegistry = null;

    @Bind
    public void bindRestResourceRegistry(RestResourceRegistry r) {
        log.debug("Bound to rest resource registry...");
        restResourceRegistry = r;
    }

    @Unbind
    public void unbindRestResourceRegistry() {
        log.debug("Unbound from rest resource registry...");
        restResourceRegistry = null;
    }

    @Validate
    public void validate() throws Exception {
        restResourceRegistry.registerPluginRestEndpoints(IDMWatBootstrap.class.getResource(REST_EP_FILE_PATH));
        log.info("{} is started", IDMWAT_COMPONENT);
    }

    @Invalidate
    public void invalidate() throws Exception {
        log.info("{} is stopped", IDMWAT_COMPONENT);
    }
}