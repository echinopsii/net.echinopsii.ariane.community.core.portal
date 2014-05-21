/**
 * Portal IDM wat bundle
 * IDM JPA Provider Consumer
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

package net.echinopsii.ariane.core.portal.idmwat.plugin;

import net.echinopsii.ariane.core.idm.base.proxy.IDMJPAProvider;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iPojo singleton which consume the IDM JPA provider.
 * Instantiated during IDM commons-jsf bundle startup. FactoryMethod : getInstance
 */
@Component(publicFactory = false, factoryMethod = "getInstance")
@Instantiate
public class IDMJPAProviderConsumer {
    private static final Logger log = LoggerFactory.getLogger(IDMJPAProviderConsumer.class);
    private static IDMJPAProviderConsumer INSTANCE;

    @Requires
    private IDMJPAProvider idmJpaProvider = null;

    @Bind
    public void bindJPAProvider(IDMJPAProvider r) {
        log.debug("Bound to IDM JPA provider...");
        idmJpaProvider = r;
    }

    @Unbind
    public void unbindJPAProvider() {
        log.debug("Unbound from IDM JPA provider...");
        idmJpaProvider = null;
    }

    /**
     * Get IDM JPA provider binded to this consumer...
     *
     * @return the binded IDM JPA provider. null if unbinded.
     */
    public IDMJPAProvider getIdmJpaProvider() {
        return idmJpaProvider;
    }

    /**
     * Factory method for this singleton...
     *
     * @return instantiated IDM JPA provider consumer
     */
    public static IDMJPAProviderConsumer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IDMJPAProviderConsumer();
        }
        return INSTANCE;
    }
}