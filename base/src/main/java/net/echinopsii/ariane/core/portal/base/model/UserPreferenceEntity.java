/**
 * Portal base bundle
 * UserProfile Preference Entity
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
package net.echinopsii.ariane.core.portal.base.model;

import java.util.TreeSet;

/**
 * Define a user preference entity which are registered by Ariane components into Ariane Portal user preference section. <br/>
 * These entities are typed with definitions provided by user preference entity type enum. <br/>
 * The user preference entity are displayed in the portal user home page. <br/><br/>
 *
 * This class implements Comparable in order to help the Ariane Portal user preference section to sort the registered user preference entity.
 */
public class UserPreferenceEntity implements Comparable<UserPreferenceEntity> {

    private String fieldName ;
    private int    fieldType ;
    private String fieldDescription;

    private TreeSet<String> selectSet = new TreeSet<String>();

    private String fieldDefault;

    /**
     * Constructor
     *
     * @param fieldName name of the preference field
     * @param fieldType type of the preference field (check {@link UserPreferenceEntityType} for supported type)
     * @param fieldDescription description of the preference field
     */
    public UserPreferenceEntity(String fieldName, int fieldType, String fieldDescription) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldDescription = fieldDescription;
    }

    /**
     * Get the name of the preference field
     *
     * @return the name of the preference field
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the type of the preference field
     *
     * @return the type of the preference field (check {@link UserPreferenceEntityType} for supported type)
     */
    public int getFieldType() {
        return fieldType;
    }

    /**
     * Get select values (for any user preference entity of select kind type)
     *
     * @return selected values
     */
    public TreeSet<String> getSelectSet() {
        return selectSet;
    }

    /**
     * Add a value to select (for any user preference entity of select kind type)
     *
     * @param value value to select
     *
     * @return this UserPreferenceEntity
     */
    public UserPreferenceEntity addSelectValue(String value) {
        this.selectSet.add(value);
        return this;
    }

    /**
     * Get the description of the preference field
     *
     * @return the description of the preference field
     */
    public String getFieldDescription() {
        return fieldDescription;
    }

    /**
     * Set the description of the preference field
     *
     * @param fieldDescription the description of the preference field
     *
     * @return this UserPreferenceEntity
     */
    public UserPreferenceEntity setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
        return this;
    }

    /**
     * Get the default value of the preference field
     *
     * @return the default value of the preference field
     */
    public String getFieldDefault() {
        return fieldDefault;
    }

    /**
     * Set the default value of the preference field
     *
     * @param fieldDefault the default value of the preference field
     *
     * @return this UserPreferenceEntity
     */
    public UserPreferenceEntity setFieldDefault(String fieldDefault) {
        this.fieldDefault = fieldDefault;
        return this;
    }

    @Override
    public int compareTo(UserPreferenceEntity that) {
        return this.fieldName.compareTo(that.getFieldName());
    }
}