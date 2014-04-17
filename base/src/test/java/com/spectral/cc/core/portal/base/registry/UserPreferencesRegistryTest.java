/**
 * Portal Commons Services bundle
 * User Preference Registry iPojo Impl test
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

package com.spectral.cc.core.portal.base.registry;

import com.spectral.cc.core.portal.base.model.UserPreferenceEntity;
import com.spectral.cc.core.portal.base.model.UserPreferenceEntityType;
import com.spectral.cc.core.portal.base.model.UserPreferenceSection;
import com.spectral.cc.core.portal.base.model.UserPreferenceSectionType;
import com.spectral.cc.core.portal.base.plugin.UserPreferencesRegistry;
import com.spectral.cc.core.portal.base.plugin.iPojo.UserPreferencesRegistryImpl;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserPreferencesRegistryTest extends TestCase {

    private static UserPreferencesRegistry userPreferencesRegistry = new UserPreferencesRegistryImpl();
    private UserPreferenceSection mappingDisplay = new UserPreferenceSection("mappingDisplay", "Define your mapping preferences", UserPreferenceSectionType.TYPE_USR_PREF_SECTION_MAP).
                                                                            addEntity(new UserPreferenceEntity("mappingDisplayLayout",
                                                                                                               UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_ONEBUTTON_SELECT,
                                                                                                               "Define your prefered layout").
                                                                                      addSelectValue("Tree").addSelectValue("Network").addSelectValue("Random").setFieldDefault("Tree")).
                                                                            addEntity(new UserPreferenceEntity("mappingDisplayView",
                                                                                                               UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_ONEBUTTON_SELECT,
                                                                                                               "Define your prefered view").addSelectValue("Infrastructure").
                                                                                      addSelectValue("Component").addSelectValue("Application").setFieldDefault("Infrastructure"));

    @BeforeClass
    public static void testSetup() {
    }

    @AfterClass
    public static void testCleanup() {
        ((UserPreferencesRegistryImpl)userPreferencesRegistry).invalidate();
    }

    @Test
    public void testRegisteringUnregisteringSectionPreferences() {
        userPreferencesRegistry.registerUserPreferenceSection(mappingDisplay);
        assertTrue(userPreferencesRegistry.getUserPreferenceSections().contains(mappingDisplay));
        userPreferencesRegistry.unregisterUserPreferenceSection(mappingDisplay);
        assertFalse(userPreferencesRegistry.getUserPreferenceSections().contains(mappingDisplay));
    }

    @Test
    public void testRegisteringUnregisteringPreferenceInExistingSection() {
        userPreferencesRegistry.registerUserPreferenceSection(mappingDisplay);
        UserPreferenceEntity mappingEntity1 = new UserPreferenceEntity("mappingDisplayLayout2", UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_ONEBUTTON_SELECT, "Define your prefered layout").
                                                                       addSelectValue("Tree").addSelectValue("Network").addSelectValue("Random").setFieldDefault("Tree");
        userPreferencesRegistry.getUserPreferenceSections().first().getEntityRegistry().add(mappingEntity1);
        assertTrue(userPreferencesRegistry.getUserPreferenceSections().first().getEntityRegistry().contains(mappingEntity1));
        userPreferencesRegistry.getUserPreferenceSections().first().getEntityRegistry().remove(mappingEntity1);
        assertFalse(userPreferencesRegistry.getUserPreferenceSections().first().getEntityRegistry().contains(mappingEntity1));
    }
}