/**
 * Portal wat bundle
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

package net.echinopsii.ariane.core.portal.wat.plugin;

import net.echinopsii.ariane.core.portal.base.plugin.FaceletsResourceResolverService;
import net.echinopsii.ariane.core.portal.wat.tools.FaceletsResourceResolver;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Provide access to portal facelets resource from web application which are using their own facelets resource resolver (IE : not the portal one).<br/><br/>
 *
 * This is the iPojo implementation of {@link FaceletsResourceResolverService}. The component is instantiated at commons-jsf bundle startup.
 * It provides the {@link FaceletsResourceResolverService} service for Ariane directory.
 */
@Component
@Provides
@Instantiate
public class FaceletsResourceResolverServiceImpl implements FaceletsResourceResolverService{

    private static final String FACELETS_RESOURCE_RESOLVER_SERVICE_NAME = "Ariane Portal Facelets Resource Resolver";
    private static final Logger log = LoggerFactory.getLogger(FaceletsResourceResolverServiceImpl.class);

    @Validate
    public void validate() throws Exception {
        log.info("{} is started", new Object[]{FACELETS_RESOURCE_RESOLVER_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        log.info("{} is stopped", new Object[]{FACELETS_RESOURCE_RESOLVER_SERVICE_NAME});
    }

    @Override
    public URL resolveURL(String path) {
        return FaceletsResourceResolver.resolveUrlFromThisJar(path);
    }


}