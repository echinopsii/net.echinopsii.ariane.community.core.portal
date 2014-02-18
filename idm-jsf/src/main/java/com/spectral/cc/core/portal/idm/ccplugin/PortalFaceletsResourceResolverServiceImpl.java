/**
 * Tibco rv addon directory bundle
 * Directory Facelets Resource Resolver Service iPojo impl
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

package com.spectral.cc.core.portal.idm.ccplugin;

import com.spectral.cc.core.portal.commons.facesplugin.FaceletsResourceResolverService;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

@Component
@Provides(properties= {@StaticServiceProperty(name="targetCCcomponent", type="java.lang.String", value="Portal")})
@Instantiate
public class PortalFaceletsResourceResolverServiceImpl implements FaceletsResourceResolverService {

    private static final String FACELETS_RESOURCE_RESOLVER_SERVICE_NAME = "IDM Facelets Resource Resolver Service For Portal";
    private static final Logger log = LoggerFactory.getLogger(PortalFaceletsResourceResolverServiceImpl.class);
    private static final String basePath = "/META-INF";

    @Validate
    public void validate() throws Exception {
        log.info("{} is started.", new Object[]{FACELETS_RESOURCE_RESOLVER_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        log.info("{} is stopped.", new Object[]{FACELETS_RESOURCE_RESOLVER_SERVICE_NAME});
    }

    @Override
    public URL resolveURL(String path) {
        log.debug("Resolve {} from idm commons-jsf...", new Object[]{path});
        return PortalFaceletsResourceResolverServiceImpl.class.getResource(basePath + path);
    }
}