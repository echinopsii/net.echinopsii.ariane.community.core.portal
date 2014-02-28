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

package com.spectral.cc.core.portal.wat.consumer;

import com.spectral.cc.core.portal.base.registry.RestResourceRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class PortalRestResourceRegistryConsumer {
    private static final Logger log = LoggerFactory.getLogger(PortalFacesMBeanRegistryConsumer.class);
    private static PortalRestResourceRegistryConsumer INSTANCE;

    @Requires(from="CCPortalRestResourceRegistry")
    private RestResourceRegistry portalRestResourceRegistry = null;

    @Bind
    public void bindPortalRestResourceRegistry(RestResourceRegistry r) {
        log.debug("Bound to portal rest resource registry...");
        portalRestResourceRegistry = r;
    }

    @Unbind
    public void unbindPortalRestResourceRegistry() {
        log.debug("Unbound from portal rest resource registry...");
        portalRestResourceRegistry = null;
    }

    /**
     * Get portal rest resource registry
     *
     * @return the binded rest resource registry. null if unbinded.
     */
    public RestResourceRegistry getPortalRestResourceRegistry() {
        return portalRestResourceRegistry;
    }

    /**
     * Factory method for this singleton...
     *
     * @return instantiated portal rest resource registry consumer
     */
    public static PortalRestResourceRegistryConsumer getInstance() {
        if (INSTANCE == null)
            INSTANCE = new PortalRestResourceRegistryConsumer();
        return INSTANCE;
    }
}