/**
 * Portal Commons Service bundle
 * Portal Faces Managed Bean Registry interface
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

package com.spectral.cc.core.portal.base.registry;

import javax.servlet.ServletContext;
import java.net.URL;

/**
 * Provide registry tooling for external plugin faces-config.xml : register / unregister faces config URL and add / remove managed bean from this faces config.
 */
public interface FacesMBeanRegistry {
    /**
     * register the provided faces config
     *
     * @param facesConfig faces config URL to register
     */
    public void registerPluginFacesMBeanConfig(URL facesConfig);

    /**
     * unregister the provided faces config
     *
     * @param facesConfig faces config URL to unregister
     *
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    public void unregisterPluginFacesMBeanConfig(URL facesConfig) throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    /**
     * register the target servlet context.
     *
     * @param sc the target servlet context
     */
    public void registerServletContext(ServletContext sc);

    /**
     * unregister the target servlet context
     *
     * @return the unregistered servlet context
     */
    public ServletContext getRegisteredServletContext();

    /**
     * add faces managed bean registered previously to the target registered servlet context
     */
    public void addPluginFacesMBeanConfigsToServletContext();

    /**
     * delete faces managed bean unregistered previously from the target registered servlet context
     */
    public void delPluginFacesMBeanConfigsFromServletContext();
}