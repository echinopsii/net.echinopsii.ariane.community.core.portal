/**
 * Portal Web App Bundle
 * UserProfile Preference Entity Type Controller
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
package com.spectral.cc.core.portal.wab.controller;

import com.spectral.cc.core.portal.base.model.UserPreferenceEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

/**
 * Provide helper methods to get user preference entity type enumeration fields.<br/>
 * This is a request managed bean.
 */
@ManagedBean
@RequestScoped
public class UserPreferenceEntityTypeController implements Serializable{
    private static final Logger log = LoggerFactory.getLogger(UserPreferenceEntityTypeController.class);

    public int getUserPreferenceEntityInplaceString() {
        return UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_INPLACE_STRING;
    }

    public int getUserPreferenceEntityInplaceSelect() {
        return UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_INPLACE_SELECT;
    }

    public int getUserPreferenceEntityOneButtonSelect() {
        return UserPreferenceEntityType.TYPE_USR_PREF_ENTITY_ONEBUTTON_SELECT;
    }
}
