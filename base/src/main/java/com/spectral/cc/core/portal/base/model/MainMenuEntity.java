/**
 * Portal base bundle
 * Main Menu Entity
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

import java.util.ArrayList;
import java.util.List;

/**
 * A main menu entity is an object which aims to be registered into the CC Portal Main Menu Registry.<br/>
 * The fields definitions of this entity are closely linked to the PrimeFaces MenuItem class.<br/><br/>
 *
 * This class implements Comparable in order to help the CC Portal Main Menu registry to sort the registered main menu entity.
 *
 * @see <a href="http://www.primefaces.org/docs/api/3.5/org/primefaces/component/menuitem/MenuItem.html">PrimeFaces 3.5 MenuItem</a>
 *
 */
public class MainMenuEntity implements Comparable<MainMenuEntity> {

    private String id;
    private String value;
    private String contextAddress;
    private int type;
    private int rank = 0; //LEFT to RIGHT. Must be > 0.
    private MainMenuEntity parent;
    private String icon;
    private String actionListener;
    private List<String> displayRoles = new ArrayList<String>();
    private List<String> displayPermissions = new ArrayList<String>();
    //onRight not working on primefaces 3.5
    //TODO : check with primefaces communauty
    //private boolean onRight = false;

    /**
     * MainMenuEntity constructor
     *
     * @param id Identify the main menu entity in the CC Portal JSF rendering.<br/>
     *           MUST BE UNIQUE
     * @param value Define the main menu entity value which will be displayed in the final CC main menu.<br/>
     *              Used in compareTo implementation.
     * @param contextAddress Define the target link part after scheme, server name and port of the CC main menu entity.
     * @param type Type of this menu item. Menu item types are defined in Menu entity type class.<br/>
     * @param rank The display from left to right rank of this menu item.<br/>
     *             Used in compareTo implementation.
     * @param icon The icon which will be displayed with this main menu entity.<br/>
     *             This is icon could be a fontawesome or jquery-ui icon.
     */
    public MainMenuEntity(String id, String value, String contextAddress, int type, int rank, String icon) {
        this.id = id;
        this.value = value;
        this.contextAddress = contextAddress;
        this.type = type;
        this.rank = rank;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getContextAddress() {
        return contextAddress;
    }

    public int getType() {
        return type;
    }

    public int getRank() {
        return rank;
    }

    /**
     * Return the parent of this item. <br/>
     * If this item is contained into a submenu then parent is equal to the submenu main menu item.<br/>
     * Else this field is null.<br/>
     * Used in the compareTo implementation.
     *
     * @return parent MainMenuEntity
     */
    public MainMenuEntity getParent() {
        return parent;
    }

    /**
     * Set the parent of this item.<br/>
     * Used in the compareTo implementation.
     *
     * @param parent
     * @return this Main Menu Entity
     */
    public MainMenuEntity setParent(MainMenuEntity parent) {
        this.parent = parent;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    /**
     * Return the action listener primefaces will call when this item will be clicked.<br/>
     * (a good example is the logout main menu item which will call the backend logout method on click)
     *
     * @return the action listener definition
     */
    public String getActionListener() {
        return actionListener;
    }

    /**
     * Set the action listener primefaces will call when this item will be clicked.<br/>
     *
     * @param actionListener Correspond to the action listener primefaces will call when this item will be clicked.<br/>
     *                       (a good example is the logout main menu item which will call the backend logout method on click)
     * @return this Main Menu Entity
     */
    public MainMenuEntity setActionListener(String actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    /**
     * Get the display permissions of this main menu entity
     *
     * @return the display permissions list
     */
    public List<String> getDisplayPermissions() {
        return displayPermissions;
    }

    /**
     * Get the display roles of this main menu entity
     *
     * @return the display roles list
     */
    public List<String> getDisplayRoles() {
        return displayRoles;
    }

    public boolean isValid() {
        switch(this.type) {
            case MenuEntityType.TYPE_MENU_ITEM:
                return (this.id!=null && !this.id.equals("") && this.value!=null && !this.value.equals("") &&
                                this.contextAddress!=null && !this.contextAddress.equals("") && this.rank!=0 &&
                                this.icon!=null && !this.icon.equals(""));
            case MenuEntityType.TYPE_MENU_SUBMENU:
                return (this.id!=null && !this.id.equals("") && this.value!=null && !this.value.equals("")
                        && this.rank!=0 && this.icon!=null && !this.icon.equals(""));
            case MenuEntityType.TYPE_MENU_SEPARATOR:
                return (this.id!=null && !this.id.equals("") && this.parent!=null);
            default:
                return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MainMenuEntity that = (MainMenuEntity) o;
        if (!id.equals(that.id)) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        //if (onRight != that.onRight) {
        //    return false;
        //}
        if (type!= MenuEntityType.TYPE_MENU_SEPARATOR && !value.equals(that.value)) {
            return false;
        }
        if (type!= MenuEntityType.TYPE_MENU_SEPARATOR && !contextAddress.equals(that.contextAddress)) {
            return false;
        }
        if (type!= MenuEntityType.TYPE_MENU_SEPARATOR && !icon.equals(that.icon)) {
            return false;
        }
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + contextAddress.hashCode();
        result = 31 * result + type;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + icon.hashCode();
        //result = 31 * result + (onRight ? 1 : 0);
        return result;
    }

    //TODO
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "{ id:" + this.id +
                       ", value:" + this.value +
                       ", contextAddress:" + this.contextAddress +
                       ", icon:" + this.icon +
                       ", type:" + this.type +
                       ", rank:" + this.rank +
                       ", parent: " + ((this.parent!=null)?this.parent.getId():"null") + " }";    //To change body of overridden methods use File | Settings | File Templates.
    }

    //TODO
    @Override
    protected void finalize() throws Throwable {
        super.finalize();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int compareTo(MainMenuEntity that) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == that)
            return EQUAL;

        //else if ((this.isOnRight() && that.isOnRight()) || (!this.isOnRight() && !this.isOnRight())) {
            if (this.getParent()==null && that.getParent()==null) {
                if (this.getRank() > that.getRank())
                    return AFTER;
                else if (this.getRank() < that.getRank())
                    return BEFORE;
                else
                    return ((this.getValue().compareTo(that.getValue())>=0) ? AFTER : BEFORE);

            } else if (this.getParent()==null && that.getParent()!=null) {
                return BEFORE;

            } else if (this.getParent()!=null && that.getParent()==null) {
                return AFTER;

            } else {
                if (this.getParent().equals(that.getParent())) {
                    if (this.getRank() > that.getRank())
                        return AFTER;
                    else if (this.getRank() < that.getRank())
                        return BEFORE;
                    else
                        return ((this.getValue().compareTo(that.getValue())>=0) ? AFTER : BEFORE);
                } else {
                    return this.getParent().compareTo(that.getParent());
                }
            }
        //}
        /*
        else if (this.isOnRight())
            return AFTER;

        else
            return BEFORE;
        */
    }
}