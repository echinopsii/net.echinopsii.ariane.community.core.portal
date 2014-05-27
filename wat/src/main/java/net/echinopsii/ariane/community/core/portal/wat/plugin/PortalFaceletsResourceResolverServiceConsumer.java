/**
 * Portal wat bundle
 * Portal Facelets Resource Resolver Service consumer singleton
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

package net.echinopsii.ariane.community.core.portal.wat.plugin;

import net.echinopsii.ariane.community.core.portal.base.plugin.FaceletsResourceResolverService;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iPojo singleton which consume the facelets resource resolver service with filter targetArianeComponent=Portal.
 * Instantiated during portal commons-jsf bundle startup. FactoryMethod : getInstance
 */
@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class PortalFaceletsResourceResolverServiceConsumer {
    private static final Logger log = LoggerFactory.getLogger(PortalFaceletsResourceResolverServiceConsumer.class);
    private static PortalFaceletsResourceResolverServiceConsumer INSTANCE;

    @Requires(filter="(targetArianeComponent=Portal)")
    private FaceletsResourceResolverService[] faceletsResolverList;

    /**
     * Get all facelets resource resolver service binded to this consumer...
     *
     * @return all facelets resource resolver service binded on this consumer. If null no registry has been binded ...
     */
    public FaceletsResourceResolverService[] getFaceletsResourceResolverServices() {
        log.debug("{} FaceletsResourceResolverService are bound to this consumer...", (faceletsResolverList!=null) ? faceletsResolverList.length : "0");
        return faceletsResolverList;
    }

    /**
     * Factory method for this singleton.
     *
     * @return instantiated directory facelets resource resolver consumer
     */
    public static PortalFaceletsResourceResolverServiceConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PortalFaceletsResourceResolverServiceConsumer();
        }
        return INSTANCE;
    }
}