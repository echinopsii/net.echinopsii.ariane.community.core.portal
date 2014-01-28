/**
 * Portal Commons JSF bundle
 * UserProfile Registry consumer singleton
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

import com.spectral.cc.core.portal.commons.registry.UserProfileRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class UserProfileRegistryConsumer {
    private static final Logger log = LoggerFactory.getLogger(UserProfileRegistryConsumer.class);
    private static UserProfileRegistryConsumer INSTANCE;

    @Requires
    private UserProfileRegistry userProfileRegistry;

    @Bind
    public void bindMainMenuEntityRegistry(UserProfileRegistry r) {
        log.info("Consumer bound to user registry...");
        userProfileRegistry = r;
    }

    @Unbind
    public void unbindMainMenuEntityRegistry() {
        log.info("Consumer unbound from user registry...");
        userProfileRegistry = null;
    }

    public UserProfileRegistry getUserProfileRegistry() {
        return userProfileRegistry;
    }

    public static UserProfileRegistryConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserProfileRegistryConsumer();
        }
        return INSTANCE;
    }
}
