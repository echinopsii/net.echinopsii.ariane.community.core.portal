/**
 * Portal wat bundle
 * Mail Service Consumer
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

package net.echinopsii.ariane.community.core.portal.idmwat.plugin;

import net.echinopsii.ariane.community.core.portal.base.plugin.MailService;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iPojo singleton which consume the portal mail service. Instantiated during portal wat bundle startup. FactoryMethod : getInstance
 */
@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class MailServiceConsumer {
    private static final Logger log = LoggerFactory.getLogger(MailServiceConsumer.class);
    private static MailServiceConsumer INSTANCE;

    @Requires
    private MailService mailService = null;

    @Bind
    public void bindMainMenuEntityRegistry(MailService r) {
        log.debug("Bound to main menu item registry...");
        mailService = r;
    }

    @Unbind
    public void unbindMainMenuEntityRegistry() {
        log.debug("Unbound from main menu item registry...");
        mailService = null;
    }

    /**
     * Get mail service binded to this consumer...
     *
     * @return mail service binded by this consumer. If null the registry is still not binded or has been unbinded...
     */
    public MailService getMailService() {
        return mailService;
    }

    /**
     * Factory method for this singleton.
     *
     * @return instantiated mail service consumer
     */
    public synchronized static MailServiceConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MailServiceConsumer();
        }
        return INSTANCE;
    }
}