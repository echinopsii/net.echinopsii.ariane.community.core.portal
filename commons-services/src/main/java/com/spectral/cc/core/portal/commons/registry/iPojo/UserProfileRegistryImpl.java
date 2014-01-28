/**
 * Portal Commons Services bundle
 * UserProfile Registry iPojo impl
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

import com.spectral.cc.core.portal.commons.model.UserProfile;
import com.spectral.cc.core.portal.commons.registry.UserProfileRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * This registry contains all CC user profiles. <br/>
 * This is used by the CC login to controller to register new default use profile on login and by the user home controller which reads the registry according to
 * the Shiro logged user principal and forward it to the user home view.
 *
 * This is the iPojo implementation of {@link UserProfileRegistry}. The component is instantiated at commons-services bundle startup. It provides the {@link UserProfileRegistry} service.
 */
@Component
@Provides
@Instantiate
public class UserProfileRegistryImpl implements UserProfileRegistry {

    private static final String USER_REGISTRY_SERVICE_NAME = "Portal User Registry Service";
    private static final Logger log = LoggerFactory.getLogger(UserProfileRegistryImpl.class);

    private HashMap<String,UserProfile> registry = new HashMap<String,UserProfile>();

    @Validate
    public void validate() throws Exception {
        log.info("{} is started...", new Object[]{USER_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate() {
        log.info("{} is stopped...", new Object[]{USER_REGISTRY_SERVICE_NAME});
    }

    @Override
    public UserProfile registerUser(UserProfile userProfile) {
        registry.put(userProfile.getPrincipal(), userProfile);
        return userProfile;
    }

    @Override
    public UserProfile unregisterUser(UserProfile userProfile) {
        registry.remove(userProfile.getPrincipal());
        return userProfile;
    }

    @Override
    public UserProfile getUserFromPrincipal(String principal) {
        return registry.get(principal);
    }
}