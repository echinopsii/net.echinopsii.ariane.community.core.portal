/**
 * Portal Commons JSF bundle
 * Main Menu Registry consumer singleton
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
package com.spectral.cc.core.portal.commons.consumer;

import com.spectral.cc.core.portal.commons.registry.MainMenuEntityRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class MainMenuRegistryConsumer {
    private static final Logger log = LoggerFactory.getLogger(MainMenuRegistryConsumer.class);
    private static MainMenuRegistryConsumer INSTANCE;

    @Requires
    private MainMenuEntityRegistry mainMenuEntityRegistry = null;

    @Bind
    public void bindMainMenuEntityRegistry(MainMenuEntityRegistry r) {
        log.info("Consumer bound to main menu item registry...");
        mainMenuEntityRegistry = r;
    }

    @Unbind
    public void unbindMainMenuEntityRegistry() {
        log.info("Consumer unbound from main menu item registry...");
        mainMenuEntityRegistry = null;
    }

    public MainMenuEntityRegistry getMainMenuEntityRegistry() {
        return mainMenuEntityRegistry;
    }

    public synchronized static MainMenuRegistryConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainMenuRegistryConsumer();
        }
        return INSTANCE;
    }
}