/**
 * Portal Commons JSF bundle
 * Facelets Resource Resolver Service iPojo impl
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

package com.spectral.cc.core.portal.commons.fresolver.iPojo;

import com.spectral.cc.core.portal.commons.fresolver.FaceletsResourceResolverService;
import com.spectral.cc.core.portal.commons.tools.FaceletsResourceResolver;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

@Component
@Provides
@Instantiate
public class FaceletsResourceResolverServiceImpl implements FaceletsResourceResolverService{

    private static final String FACELETS_RESOURCE_RESOLVER_SERVICE_NAME = "Portal Facelets Resource Resolver Service";
    private static final Logger log = LoggerFactory.getLogger(FaceletsResourceResolverServiceImpl.class);

    @Validate
    public void validate() throws Exception {
        log.debug("{} is started.", new Object[]{FACELETS_RESOURCE_RESOLVER_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        log.debug("{} is stopped.", new Object[]{FACELETS_RESOURCE_RESOLVER_SERVICE_NAME});
    }

    @Override
    public URL resolveURL(String path) {
        return FaceletsResourceResolver.resolveUrlFromThisJar(path);
    }


}