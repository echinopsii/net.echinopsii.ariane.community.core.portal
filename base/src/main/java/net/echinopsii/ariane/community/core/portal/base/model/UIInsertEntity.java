/**
 * Portal base bundle
 * System Dialog Helper Entity
 * Copyright (C) 2015 Mathilde Ffrench
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
package net.echinopsii.ariane.community.core.portal.base.model;

public class UIInsertEntity {
    private String uiInsertName;
    private String uiIncludeSrc;

    public String getUiInsertName() {
        return uiInsertName;
    }

    public void setUiInsertName(String uiInsertName) {
        this.uiInsertName = uiInsertName;
    }

    public UIInsertEntity setUiInsertNameR(String uiInsertName) { this.uiInsertName = uiInsertName; return this; }

    public String getUiIncludeSrc() {
        return uiIncludeSrc;
    }

    public void setUiIncludeSrc(String uiIncludeSrc) {
        this.uiIncludeSrc = uiIncludeSrc;
    }

    public UIInsertEntity setUiIncludeSrcR(String uiIncludeSrc) { this.uiIncludeSrc = uiIncludeSrc; return this; }
}
