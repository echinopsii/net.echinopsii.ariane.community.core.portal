/**
 * Portal Commons JSF bundle
 * Facelets Resource Resolver
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import javax.faces.view.facelets.ResourceResolver;

public class FaceletsResourceResolver extends ResourceResolver {

    private static final Logger log = LoggerFactory.getLogger(FaceletsResourceResolver.class);
    private ResourceResolver parent;
    private static String basePath;

    public FaceletsResourceResolver(ResourceResolver parent) {
        this.parent = parent;
        this.basePath = "/META-INF";
    }

    @Override
    public URL resolveUrl(String path) {
        log.debug("Resolve {} from portal main...", new Object[]{path});
        URL url = parent.resolveUrl(path);
        if (url == null)
            url = resolveUrlFromThisJar(path);
        return url;
    }

    public static URL resolveUrlFromThisJar(String path) {
        log.debug("Resolve {} from portal commons-jsf...", new Object[]{path});
        return FaceletsResourceResolver.class.getResource(basePath + path);
    }
}
