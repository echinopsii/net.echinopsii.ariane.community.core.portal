/**
 * Portal Web App Bundle
 * OSGI activator
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

package com.spectral.cc.core.portal.wab.bootstrap;

import com.spectral.cc.core.portal.wat.plugin.MainMenuRegistryConsumer;
import com.spectral.cc.core.portal.base.model.MainMenuEntity;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * This is the Portal WAB OsgiActivor :
 * when starting it registers common portal main menu entity into main menu entity registry
 * when stopping it unregisters the previously registered main menu entity
 */
public class OsgiActivator implements BundleActivator {

    protected static final String PORTAL_MAIN_MENU_REGISTRATOR_SERVICE_NAME = "CC Portal WAB";
    private static final Logger log = LoggerFactory.getLogger(OsgiActivator.class);

    protected static ArrayList<MainMenuEntity> mainPortalMainMenuEntityList = new ArrayList<MainMenuEntity>() ;

    @Override
    public void start(BundleContext context) {
       new Thread(new Registrator()).start();
       log.info("{} is started", new Object[]{PORTAL_MAIN_MENU_REGISTRATOR_SERVICE_NAME});
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry()!=null) {
            for(MainMenuEntity entity : mainPortalMainMenuEntityList) {
                MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().unregisterMainMenuEntity(entity);
            }
        }
        mainPortalMainMenuEntityList.clear();
        log.info("{} is stopped", new Object[]{PORTAL_MAIN_MENU_REGISTRATOR_SERVICE_NAME});
    }
}