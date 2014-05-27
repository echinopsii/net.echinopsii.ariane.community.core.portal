/**
 * Portal base bundle
 * Theme
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
package net.echinopsii.ariane.community.core.portal.base.model;

import java.io.Serializable;

/**
 * The theme class is mainly used by the ThemeSwitcherController. <br/>
 * This is the PrimeFaces theme definition. Closely dependent of theme contained in org.primefaces.themes:all-themes.
 */
public class Theme implements Serializable {

    private String name;
    private String image;

    /**
     * PrimeFaces theme definition. Closely dependent of theme contained in org.primefaces.themes:all-themes
     * @param name name of the theme
     * @param image image file of the theme
     */
    public Theme(String name, String image) {
        this.name = name;
        this.image = image;
    }

    /**
     *
     * @return image file of this theme
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image image file of this theme
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return name of this theme
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name of this theme
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
