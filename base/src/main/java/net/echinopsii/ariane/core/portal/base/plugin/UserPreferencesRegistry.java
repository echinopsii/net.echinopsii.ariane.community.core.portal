/**
 * Portal base bundle
 * UserProfile Preference Registry Interface
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
package net.echinopsii.ariane.core.portal.base.plugin;

import net.echinopsii.ariane.core.portal.base.model.UserPreferenceSection;

import java.util.TreeSet;

/**
 * This registry contains all Ariane user preferences. <br/>
 * This is used by any Ariane component which needs to register its user preference sections (and so its user preference entities) and by the user home controller which reads the registry and forward it to the user home view.
 */
public interface UserPreferencesRegistry {
    /**
     * Register a user preference section to the user preference registry
     *
     * @param section the section to register
     *
     * @return the registered section
     */
    public UserPreferenceSection registerUserPreferenceSection(UserPreferenceSection section);

    /**
     * Unregister a user preference section from the user preference registry
     *
     * @param section the section to unregister
     *
     * @return the unregistered section
     */
    public UserPreferenceSection unregisterUserPreferenceSection(UserPreferenceSection section);

    /**
     * Get the sorted user preference registry
     *
     * @return the sorted user preference registry
     */
    public TreeSet<UserPreferenceSection> getUserPreferenceSections();
}
