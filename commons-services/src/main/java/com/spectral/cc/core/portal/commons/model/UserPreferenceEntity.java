/**
 * Portal Commons Services bundle
 * User Preference Entity
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

import java.util.TreeSet;

public class UserPreferenceEntity implements Comparable<UserPreferenceEntity> {

    private String fieldName ;
    private int    fieldType ;
    private String fieldTitle;
    private String fieldDescription;

    private TreeSet<String> selectSet = new TreeSet<String>();

    private String fieldDefault;

    public UserPreferenceEntity(String fieldName, int fieldType, String fieldDescription) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldTitle = fieldDescription;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getFieldType() {
        return fieldType;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public TreeSet<String> getSelectSet() {
        return selectSet;
    }

    public UserPreferenceEntity addSelectValue(String value) {
        this.selectSet.add(value);
        return this;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public UserPreferenceEntity setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
        return this;
    }

    public String getFieldDefault() {
        return fieldDefault;
    }

    public UserPreferenceEntity setFieldDefault(String fieldDefault) {
        this.fieldDefault = fieldDefault;
        return this;
    }

    @Override
    public int compareTo(UserPreferenceEntity that) {
        return this.fieldName.compareTo(that.getFieldName());
    }
}