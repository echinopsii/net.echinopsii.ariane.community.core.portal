/**
 * Portal base bundle
 * Tree Menu Roots Registry Interface
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
package net.echinopsii.ariane.core.portal.base.plugin;

import net.echinopsii.ariane.core.portal.base.model.MainMenuEntity;
import net.echinopsii.ariane.core.portal.base.model.TreeMenuEntity;

import java.util.TreeSet;

/**
 * The tree menu roots registry store the root tree menu entity. This is used by any Ariane component which need to define tree menu and so expose tree menu roots registry service.
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

    /**
     * get the main menu entity linked to this tree menu roots registry
     *
     * @return linked main menu entity
     */
    public MainMenuEntity getLinkedMainMenuEntity();

    /**
     * set the provided main menu entity linked to this tree menu roots registry
     *
     * @param mainMenuEntity
     */
    public void setLinkedMainMenuEntity(MainMenuEntity mainMenuEntity);
}