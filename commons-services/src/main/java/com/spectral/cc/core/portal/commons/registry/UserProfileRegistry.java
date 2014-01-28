/**
 * Portal Commons Services bundle
 * UserProfile Registry interface
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
package com.spectral.cc.core.portal.commons.registry;

import com.spectral.cc.core.portal.commons.model.UserProfile;

/**
 * This registry contains all CC user profiles. <br/>
 * This is used by the CC login to controller to register new default use profile on login and by the user home controller which reads the registry according to
 * the Shiro logged user principal and forward it to the user home view.
 */
public interface UserProfileRegistry {
    /**
     * Register the user profile to the user profile registry
     *
     * @param userProfile the user profile to register
     *
     * @return the registered user profile
     */
    public UserProfile registerUser(UserProfile userProfile);

    /**
     * Unregister the user profile from the user profile registry
     *
     * @param userProfile the user profile to unregister
     *
     * @return the unregistered user profile
     */
    public UserProfile unregisterUser(UserProfile userProfile);

    /**
     * Get the user profile identified by the Shiro principal from registry
     *
     * @param principal the Shiro user logged principal
     *
     * @return the user profile identified by the Shiro principal
     */
    public UserProfile getUserFromPrincipal(String principal);
}
