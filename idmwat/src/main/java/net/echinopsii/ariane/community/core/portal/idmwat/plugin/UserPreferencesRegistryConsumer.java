/**
 * Portal IDM wat bundle
 * UserProfile Preference Registry consumer singleton
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
package net.echinopsii.ariane.community.core.portal.idmwat.plugin;

import net.echinopsii.ariane.community.core.portal.base.plugin.UserPreferencesRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iPojo singleton which consume the portal user preference registry service. Instantiated during portal commons-jsf bundle startup. FactoryMethod : getInstance.
 */
@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class UserPreferencesRegistryConsumer {
    private static final Logger log = LoggerFactory.getLogger(UserPreferencesRegistryConsumer.class);
    private static UserPreferencesRegistryConsumer INSTANCE;

    @Requires
    private UserPreferencesRegistry userPreferencesRegistry;

    @Bind
    public void bindMainMenuEntityRegistry(UserPreferencesRegistry r) {
        log.debug("Bound to user preferences registry...");
        userPreferencesRegistry = r;
    }

    @Unbind
    public void unbindMainMenuEntityRegistry() {
        log.debug("Unbound from user preferences registry...");
        userPreferencesRegistry = null;
    }

    /**
     * Get user preference registry binded to this consumer...
     *
     * @return user preference registry binded by this consumer. If null the registry is still not binded or has been unbinded...
     */
    public UserPreferencesRegistry getUserPreferencesRegistry() {
        return userPreferencesRegistry;
    }

    /**
     * Factory method for this singleton.
     *
     * @return instantiated user preference registry consumer
     */
    public static UserPreferencesRegistryConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserPreferencesRegistryConsumer();
        }
        return INSTANCE;
    }
}
