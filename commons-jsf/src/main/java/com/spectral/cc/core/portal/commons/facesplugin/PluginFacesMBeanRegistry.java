/**
 * Directory Commons JSF bundle
 * Faces Managed Bean Directory Registry interface
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

package com.spectral.cc.core.portal.commons.facesplugin;

import javax.servlet.ServletContext;
import java.net.URL;

public interface PluginFacesMBeanRegistry {
    public void registerPluginFacesMBeanConfig(URL facesConfig);
    public void unregisterPluginFacesMBeanConfig(URL facesConfig) throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    public void registerServletContext(ServletContext sc);
    public ServletContext getRegisteredServletContext();

    public void addPluginFacesMBeanConfigsToServletContext();
    public void delPluginFacesMBeanConfigsFromServletContext();
}