/**
 * Directory Commons Services bundle
 * Root Directory Registry Interface
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
package com.spectral.cc.core.portal.commons.registry;

import com.spectral.cc.core.portal.commons.model.TreeMenuEntity;

import java.util.TreeSet;

/**
 * The tree menu roots registry store the root tree menu entity.
 */
public interface TreeMenuRootsRegistry {
    /**
     * Add a new tree menu root entity to registry
     *
     * @param treeMenuEntity
     *
     * @return registered tree menu root entity
     */
    public TreeMenuEntity registerTreeMenuRootEntity(TreeMenuEntity treeMenuEntity);

    /**
     * Remove the tree menu root entity from registry
     *
     * @param treeMenuEntity
     *
     * @return unregistered tree menu root entity
     */
    public TreeMenuEntity unregisterTreeMenuRootEntity(TreeMenuEntity treeMenuEntity);

    /**
     * Get the registry
     *
     * @return the registry
     */
    public TreeSet<TreeMenuEntity> getTreeMenuRootsEntities();

    /**
     * Get the tree menu entity from value
     *
     * @param value
     *
     * @return if found the tree menu root entity else null
     */
    public TreeMenuEntity getTreeMenuEntityFromValue(String value);

    /**
     * Get the tree menu entity from id
     *
     * @param id
     *
     * @return if found the tree menu entity else null
     */
    public TreeMenuEntity getTreeMenuEntityFromID(String id);

    /**
     * Get the tree menu root entity from context address
     *
     * @param contextAddress
     *
     * @return if found the tree menu entity else null
     */
    public TreeMenuEntity getTreeMenuEntityFromContextAddress(String contextAddress);
}