/**
 * Portal Commons Services bundle
 * User Preference Registry iPojo Impl
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

import com.spectral.cc.core.portal.commons.model.UserPreferenceSection;
import com.spectral.cc.core.portal.commons.registry.UserPreferencesRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;

@Component
@Provides
@Instantiate
public class UserPreferencesRegistryImpl implements UserPreferencesRegistry {

    private static final String USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME = "Portal User Preferences Registry Service";
    private static final Logger log = LoggerFactory.getLogger(UserPreferencesRegistryImpl.class);

    private TreeSet<UserPreferenceSection> sectionRegistry = new TreeSet<UserPreferenceSection>();

    @Validate
    public void validate() throws Exception {
        //log.debug("Starting {}...", new Object[]{USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME});
        //TODO : move the bellow lines into JUNIT test case
        /*
        UserPreferenceSection mappingDisplay = new UserPreferenceSection("mappingDisplay", "Define your mapping preferences", UserPreferenceSectionType.TYPE_USR_PREF_SECTION_MAP).
            addEntity(
                new UserPreferenceEntity(
                    "mappingDisplayLayout",
                    UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_ONEBUTTON_SELECT,
                    "Define your prefered layout").addSelectValue("Tree").addSelectValue("Network").addSelectValue("Random").setFieldDefault("Tree")).
            addEntity(
                new UserPreferenceEntity(
                    "mappingDisplayView",
                    UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_ONEBUTTON_SELECT,
                    "Define your prefered view").addSelectValue("Infrastructure").addSelectValue("Component").addSelectValue("Application").setFieldDefault("Infrastructure"));
        this.registerUserPreferenceSection(mappingDisplay);

        this.registerUserPreferenceSection(new UserPreferenceSection("bookmarkedDSL", "Manage your bookmarked DSL requests", UserPreferenceSectionType.TYPE_USR_PREF_SECTION_MAP));
        */
        log.debug("{} is started.", new Object[]{USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        log.debug("Stopping {}...", new Object[]{USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME});
        sectionRegistry.clear();
        log.debug("{} is stopped.", new Object[]{USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Override
    public UserPreferenceSection registerUserPreferenceSection(UserPreferenceSection section) {
        sectionRegistry.add(section);
        return section;
    }

    @Override
    public UserPreferenceSection unregisterUserPreferenceSection(UserPreferenceSection section) {
        sectionRegistry.remove(section);
        return section;
    }

    @Override
    public TreeSet<UserPreferenceSection> getUserPreferenceSections() {
        return sectionRegistry;
    }
}
