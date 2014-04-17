/**
 * Portal Commons JSF bundle
 * Facelets Resource Resolver Service Interface
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

package com.spectral.cc.core.portal.base.plugin;

import java.net.URL;

/**
 * Provide access to portal facelets resource from web application which are using their own facelets resource resolver (IE : not the portal one).
 */
public interface FaceletsResourceResolverService {
    /**
     * resolve facelets resource from portal main war or portal commons-jsf jar
     *
     * @param path path of the resource
     *
     * @return the resource URL (null if not found)
     */
    public URL resolveURL(String path);
}