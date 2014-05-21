/**
 * Portal wat bundle
 * UserProfile Home Controller
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
package net.echinopsii.ariane.core.portal.wat.controller;

import net.echinopsii.ariane.core.portal.wat.plugin.UserPreferencesRegistryConsumer;
import net.echinopsii.ariane.core.portal.base.model.UserPreferenceEntity;
import net.echinopsii.ariane.core.portal.base.model.UserPreferenceSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper for user home view. Define user preferences sections lists and provide helper methods to navigate in these sections. Used by user home view.<br/>
 * This is a request managed bean.
 */
public class UserHomeController implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(UserHomeController.class);

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
                log.debug("Add section {} on left", section.getName());
                preferencesSectionOnLeft.add(section);
                isOdd=false;
            } else {
                log.debug("Add section {} on right", section.getName());
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
            log.debug("Get entities from section {}", section.getName());
            for(UserPreferenceEntity entity : section.getEntityRegistry()) {
                log.debug("Entity ({},{}) from section {}", new Object[]{entity.getFieldName(),entity.getFieldType(),section.getName()});
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
