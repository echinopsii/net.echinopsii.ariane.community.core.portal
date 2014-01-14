/**
 * Portal Commons Services bundle
 * User Registry iPojo impl
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
package com.spectral.cc.core.portal.commons.registry.iPojo;

import com.spectral.cc.core.portal.commons.model.User;
import com.spectral.cc.core.portal.commons.registry.UserRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Component
@Provides
@Instantiate
public class UserRegistryImpl implements UserRegistry {

    private static final String USER_REGISTRY_SERVICE_NAME = "Portal User Registry Service";
    private static final Logger log = LoggerFactory.getLogger(UserRegistryImpl.class);

    private HashMap<String,User> registry = new HashMap<String,User>();

    @Validate
    public void validate() throws Exception {
        log.info("{} is started...", new Object[]{USER_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate() {
        log.info("{} is stopped...", new Object[]{USER_REGISTRY_SERVICE_NAME});
    }

    @Override
    public User registerUser(User user) {
        registry.put(user.getPrincipal(),user);
        return user;
    }

    @Override
    public User unregisterUser(User user) {
        registry.remove(user.getPrincipal());
        return user;
    }

    @Override
    public User getUserFromPrincipal(String principal) {
        return registry.get(principal);
    }
}