/**
 * [DEFINE YOUR PROJECT NAME/MODULE HERE]
 * [DEFINE YOUR PROJECT DESCRIPTION HERE] 
 * Copyright (C) 28/01/14 echinopsii
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
import com.spectral.cc.core.portal.commons.registry.iPojo.UserProfileRegistryImpl;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserProfileRegistryTest extends TestCase {

    private static UserProfileRegistry userProfileRegistry = new UserProfileRegistryImpl();

    @BeforeClass
    public static void testSetup() {
    }

    @AfterClass
    public static void testCleanup() {
    }

    @Test
    public void testRegisterUnregisterUserProfile() throws Exception {
        UserProfile userProfile = new UserProfile("shiroPrincipal");
        userProfile.setFirstname("Define your first name...");
        userProfile.setLastname("Define your last name...");
        userProfile.setEmail("Define your mail...");
        userProfile.setPhone("Define your phone number...");
        userProfile.getPreferences().put("Field name","Field value");
        userProfileRegistry.registerUser(userProfile);
        assertTrue(userProfileRegistry.getUserFromPrincipal("shiroPrincipal").equals(userProfile));
        userProfileRegistry.unregisterUser(userProfile);
        assertTrue(userProfileRegistry.getUserFromPrincipal("shiroPrincipal")==null);
    }
}