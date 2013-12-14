/**
 * Portal Main
 * User Home Controller
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
package com.spectral.cc.core.portal.main.controller;

import com.spectral.cc.core.portal.commons.consumer.UserPreferencesRegistryConsumer;
import com.spectral.cc.core.portal.commons.model.UserPreferenceEntity;
import com.spectral.cc.core.portal.commons.model.UserPreferenceSection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@RequestScoped
public class UserHomeController implements Serializable {

    private List<UserPreferenceSection> preferencesSectionOnLeft;
    private String activePreferencesSectionOnLeft;
    private List<UserPreferenceSection> preferencesSectionOnRight;
    private String activePreferencesSectionOnRight;

    @PostConstruct
    public void init(){
        preferencesSectionOnLeft = new ArrayList<UserPreferenceSection>();
        preferencesSectionOnRight = new ArrayList<UserPreferenceSection>();
        boolean isOdd = true;
        for(UserPreferenceSection section : UserPreferencesRegistryConsumer.getInstance().getUserPreferencesRegistry().getUserPreferenceSections()) {
            if (isOdd) {
                preferencesSectionOnLeft.add(section);
                isOdd=false;
            } else {
                preferencesSectionOnRight.add(section);
                isOdd=true;
            }
        }

        activePreferencesSectionOnLeft="0";
        activePreferencesSectionOnRight="0";
        isOdd = true;
        int sectionLeftIdx = 0 ;
        int sectionRightIdx = 0 ;
        for (UserPreferenceSection section : UserPreferencesRegistryConsumer.getInstance().getUserPreferencesRegistry().getUserPreferenceSections()) {
            if (isOdd) {
                if (sectionLeftIdx!=0)
                    activePreferencesSectionOnLeft += ","+sectionLeftIdx;
                sectionLeftIdx++;
                isOdd = false;
            } else {
                if (sectionRightIdx!=0)
                    activePreferencesSectionOnRight += ","+sectionRightIdx;
                sectionRightIdx++;
                isOdd = true;
            }
        }
    }

    public List<UserPreferenceSection> getPreferencesSectionsOnLeft() {
        return this.preferencesSectionOnLeft;
    }

    public void setPreferencesSectionOnLeft(List<UserPreferenceSection> preferencesSectionOnLeft) {
        this.preferencesSectionOnLeft = preferencesSectionOnLeft;
    }

    public String getActivePreferencesSectionsOnLeft() {
        return this.activePreferencesSectionOnLeft;
    }

    public void setActivePreferencesSectionOnLeft(String activePreferencesSectionOnLeft) {
        this.activePreferencesSectionOnLeft = activePreferencesSectionOnLeft;
    }

    public List<UserPreferenceSection> getPreferencesSectionsOnRight() {
        return this.preferencesSectionOnRight;
    }

    public void setPreferencesSectionOnRight(List<UserPreferenceSection> preferencesSectionOnRight) {
        this.preferencesSectionOnRight = preferencesSectionOnRight;
    }

    public String getActivePreferencesSectionsOnRight() {
        return this.activePreferencesSectionOnRight;
    }

    public void setActivePreferencesSectionOnRight(String activePreferencesSectionOnRight) {
        this.activePreferencesSectionOnRight = activePreferencesSectionOnRight;
    }

    public List<UserPreferenceEntity> getPreferencesEntitiesFromSection(UserPreferenceSection section) {
        ArrayList<UserPreferenceEntity> ret = new ArrayList<UserPreferenceEntity>();
        if (section !=null) {
            for(UserPreferenceEntity entity : section.getEntityRegistry()) {
                ret.add(entity);
            }
        }
        return ret;
    }

    public List<String> getSelectValuesFromEntity(UserPreferenceEntity entity) {
        ArrayList<String> ret = new ArrayList<String>();
        if (entity!=null) {
            for(String value : entity.getSelectSet()) {
                ret.add(value);
            }
        }
        return ret ;
    }
}
