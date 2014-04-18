/**
 * Portal base bundle
 * Tree Menu Entity
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
import java.util.TreeSet;

/**
 * A Tree Menu Entity represent a tree menu entry with its childs and parent.
 */
public class TreeMenuEntity implements Comparable<TreeMenuEntity> {

    private String id                       = null;
    private String value                    = null;
    private int    type                     = 0;
    private String contextAddress           = "";
    private String description              = "";
    private String icon                     = "";
    private List<String> displayRoles       = new ArrayList<String>();
    private List<String> displayPermissions = new ArrayList<String>();

    private TreeMenuEntity parent = null;
    private TreeSet<TreeMenuEntity> childs = new TreeSet<TreeMenuEntity>();

    /**
     * Get tree menu entity id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the tree menu entity id.<br/><br/>
     *
     * @param id the tree menu entity
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Set the tree menu entity value (display of the entity in primefaces menu rendering). The value must be unique.
     *
     * @param value
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Get the tree menu entity value (display of the entity in primefaces menu rendering)
     *
     * @return the tree menu entity
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the type of the tree menu entity.
     *
     * @return tree menu entity type
     */
    public int getType() {
        return type;
    }

    /**
     * Set the type of the tree menu entity.
     *
     * @param type ot the tree menu entity. Supported type are defined here in {@link MenuEntityType}.
     *
     * @return this tree menu entity type
     *
     * @throws Exception if provided type is not supported.
     */
    public TreeMenuEntity setType(int type) throws Exception {
        if (type == MenuEntityType.TYPE_MENU_ITEM || type == MenuEntityType.TYPE_MENU_SUBMENU || type == MenuEntityType.TYPE_MENU_SEPARATOR)
            this.type = type;
        else
            throw new Exception("Not supported tree entity type : " + type);
        return this;
    }

    /**
     * Get the tree menu entity target context address
     *
     * @return the context address
     */
    public String getContextAddress() {
        return contextAddress;
    }

    /**
     * Set the tree menu entity context address
     *
     * @param contextAddress
     *
     * @return this tree menu entity type
     */
    public TreeMenuEntity setContextAddress(String contextAddress) {
        this.contextAddress = contextAddress;
        return this;
    }

    /**
     * Set the tree menu entity description
     *
     * @return the tree menu entity description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the tree menu entity description (displayed in the tree dashboard)
     *
     * @param description
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get icon of this tree menu entity
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Set icon of this tree menu entity (could be jquery-ui icon or font awesome icon)
     *
     * @param icon
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Get the display roles for this tree entity
     *
     * @return the display roles list
     */
    public List<String> getDisplayRoles() {
        return displayRoles;
    }

    /**
     * Get the display permissions for this tree entity
     *
     * @return the display permissions list
     */
    public List<String> getDisplayPermissions() {
        return displayPermissions;
    }

    /**
     * add the provided display role if not already in the display roles list
     *
     * @param role
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity addDisplayRole(String role) {
        if (!displayRoles.contains(role))
            displayRoles.add(role);
        return this;
    }

    /**
     * add the provided display permission if not already in the display permissions list
     *
     * @param permission
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity addDisplayPermission(String permission) {
        if (!displayPermissions.contains(permission))
            displayPermissions.add(permission);
        return this;
    }

    /**
     * remove provided display role from display roles list
     *
     * @param role
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity removeDisplayRole(String role) {
        displayRoles.remove(role);
        return this;
    }

    /**
     * remove provided display permission from display permissions list
     *
     * @param permission
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity removeDisplayPermission(String permission) {
        displayPermissions.remove(permission);
        return this;
    }

    /**
     * Set the tree menu entity parent
     *
     * @param parent
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity setParentTreeMenuEntity(TreeMenuEntity parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Get the tree menu entity parent
     *
     * @return the tree menu entity parent
     */
    public TreeMenuEntity getParentTreeMenuEntity() {
        return parent;
    }

    /**
     * Add the tree menu entity child
     *
     * @param child
     *
     * @return this tree menu entity
     */
    public TreeMenuEntity addChildTreeMenuEntity(TreeMenuEntity child) {
        this.childs.add(child);
        return this;
    }

    /**
     * Get the tree menu entity childs
     *
     * @return the tree menu entity childs
     */
    public TreeSet<TreeMenuEntity> getChildTreeMenuEntities() {
        return this.childs;
    }

    /**
     * Find and return tree menu entity from value
     *
     * @param value_
     *
     * @return the found tree menu entity or null if no entity has been found
     */
    public TreeMenuEntity findTreeMenuEntityFromValue(String value_) {
        TreeMenuEntity ret = null;
        if (this.value.equals(value_)) {
            ret = this;
        } else {
            for (TreeMenuEntity entity : childs) {
                ret = entity.findTreeMenuEntityFromValue(value_);
                if (ret!=null) break;
            }
        }
        return ret;
    }

    /**
     * Find and return tree menu entity from id
     *
     * @param id_
     *
     * @return the found tree menu entity or null if no entity has been found
     */
    public TreeMenuEntity findTreeMenuEntityFromID(String id_) {
        TreeMenuEntity ret = null;
        if (this.id.equals(id_)) {
            ret = this;
        } else {
            for (TreeMenuEntity entity : childs) {
                ret = entity.findTreeMenuEntityFromID(id_);
                if (ret!=null) break;
            }
        }
        return ret;
    }

    /**
     * Find and return tree menu entity from context address
     *
     * @param contextAddress
     *
     * @return the found tree menu entity or null if no entity has been found
     */
    public TreeMenuEntity findTreeMenuEntityFromContextAddress(String contextAddress) {
        TreeMenuEntity ret = null;
        if (this.contextAddress!=null && this.contextAddress.equals(contextAddress)) {
            ret = this;
        } else {
            for (TreeMenuEntity entity : childs) {
                ret = entity.findTreeMenuEntityFromContextAddress(contextAddress);
                if (ret!=null) break;
            }
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TreeMenuEntity that = (TreeMenuEntity) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!value.equals(that.value)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TreeMenuEntity{" +
                       "id='" + id + '\'' +
                       ", value='" + value + '\'' +
                       '}';
    }

    @Override
    public int compareTo(TreeMenuEntity that) {
        return this.value.compareTo(that.getValue());
    }
}