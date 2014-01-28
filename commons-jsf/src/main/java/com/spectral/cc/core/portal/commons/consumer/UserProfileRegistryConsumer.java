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

/**
 * iPojo singleton which consume the portal user profile registry service. Instantiated during portal commons-jsf bundle startup. FactoryMethod : getInstance.
 */
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

    /**
     * Get user preference registry binded to this consumer...
     *
     * @return user profile registry binded by this consumer. If null the registry is still not binded or has been unbinded...
     */
    public UserProfileRegistry getUserProfileRegistry() {
        return userProfileRegistry;
    }

    /**
     * Factory method for this singleton.
     *
     * @return instantiated user profile registry consumer
     */
    public static UserProfileRegistryConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserProfileRegistryConsumer();
        }
        return INSTANCE;
    }
}
