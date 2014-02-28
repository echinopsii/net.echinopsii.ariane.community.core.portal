/**
 * Portal Commons Services bundle
 * UserProfile Preference Entity Type
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
package com.spectral.cc.core.portal.base.model;

/**
 * The user preference entity type class is an enumeration where your can find supported PrimeFaces user preference entity type.
 */
public enum UserPreferenceEntityType {
    ;
    public final static int TYPE_USR_PREF_ENTITY_INPLACE_STRING = 1;
    public final static int TYPE_USR_PREF_ENTITY_INPLACE_SELECT = 2;
    public final static int TYPE_USR_PREF_ENTITY_ONEBUTTON_SELECT = 3;
}
