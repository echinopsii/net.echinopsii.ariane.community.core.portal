/**
 * Portal Commons Services bundle
 * User Preference Section
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
package com.spectral.cc.core.portal.commons.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;

public class UserPreferenceSection implements Comparable<UserPreferenceSection> {

    private static final Logger log = LoggerFactory.getLogger(UserPreferenceSection.class);

    private String name ;
    private String description ;
    private int    type ;

    private TreeSet<UserPreferenceEntity> entityRegistry = new TreeSet<UserPreferenceEntity>();

    public UserPreferenceSection(String name, String description, int type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public TreeSet<UserPreferenceEntity> getEntityRegistry() {
        return entityRegistry;
    }

    public UserPreferenceSection addEntity(UserPreferenceEntity entity) {
        log.debug("Add entity {} to user preference section {}", new Object[]{entity.getFieldName(),name});
        this.entityRegistry.add(entity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserPreferenceSection that = (UserPreferenceSection) o;

        if (!name.equals(that.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "UserPreferenceSection{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }

    @Override
    public int compareTo(UserPreferenceSection that) {
        return this.name.compareTo(that.getName());
    }
}