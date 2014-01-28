/**
 * Portal Commons Services bundle
 * UserProfile Preference Registry iPojo Impl
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

/**
 * This registry contains all CC user preferences. <br/>
 * This is used by any CC component which needs to register its user preference sections (and so its user preference entities)
 * and by the user home controller which reads the registry and forward it to the user home view.<br/><br/>
 *
 * This is the iPojo implementation of {@link UserPreferencesRegistry}. The component is instantiated at commons-services bundle startup. It provides the {@link UserPreferencesRegistry} service.
 */
@Component
@Provides
@Instantiate
public class UserPreferencesRegistryImpl implements UserPreferencesRegistry {

    private static final String USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME = "Portal User Preferences Registry Service";
    private static final Logger log = LoggerFactory.getLogger(UserPreferencesRegistryImpl.class);

    private TreeSet<UserPreferenceSection> sectionRegistry = new TreeSet<UserPreferenceSection>();

    @Validate
    public void validate() throws Exception {
        log.info("{} is started.", new Object[]{USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        sectionRegistry.clear();
        log.info("{} is stopped.", new Object[]{USER_PREFERENCES_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Override
    public UserPreferenceSection registerUserPreferenceSection(UserPreferenceSection section) {
        log.debug("Register new user preference section {} from : \n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}",
                         new Object[]{
                                             section.getName(),
                                             (Thread.currentThread().getStackTrace().length>0) ? Thread.currentThread().getStackTrace()[0].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>1) ? Thread.currentThread().getStackTrace()[1].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>2) ? Thread.currentThread().getStackTrace()[2].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>3) ? Thread.currentThread().getStackTrace()[3].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>4) ? Thread.currentThread().getStackTrace()[4].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>5) ? Thread.currentThread().getStackTrace()[5].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>6) ? Thread.currentThread().getStackTrace()[6].getClassName() : ""
                         });
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
