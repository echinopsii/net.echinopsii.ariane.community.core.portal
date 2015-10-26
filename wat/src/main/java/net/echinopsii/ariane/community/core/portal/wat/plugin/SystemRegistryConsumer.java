/**
 * Portal wat bundle
 * System Registry Consumer
 * Copyright (C) 2015 Mathilde Ffrench
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
package net.echinopsii.ariane.community.core.portal.wat.plugin;

import net.echinopsii.ariane.community.core.portal.base.plugin.SystemEntityRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class SystemRegistryConsumer {
    private static final Logger log = LoggerFactory.getLogger(MailServiceConsumer.class);
    private static SystemRegistryConsumer INSTANCE;

    @Requires
    private SystemEntityRegistry systemEntityRegistry = null;

    @Bind
    public void bindSystemEntityRegistry(SystemEntityRegistry r) {
        log.debug("Bound to system entity registry...");
        systemEntityRegistry = r;
    }

    @Unbind
    public void unbindSystemEntityRegistry() {
        log.debug("Unbound from main menu item registry...");
        systemEntityRegistry = null;
    }

    public SystemEntityRegistry getSystemEntityRegistry() {
        return systemEntityRegistry;
    }

    /**
     * Factory method for this singleton.
     *
     * @return instantiated mail service consumer
     */
    public synchronized static SystemRegistryConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SystemRegistryConsumer();
        }
        return INSTANCE;
    }
}
