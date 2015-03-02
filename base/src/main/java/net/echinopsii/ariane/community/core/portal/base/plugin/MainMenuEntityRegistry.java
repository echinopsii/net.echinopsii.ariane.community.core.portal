/**
 * Portal base bundle
 * Main Menu Entity Registry Interface
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
package net.echinopsii.ariane.community.core.portal.base.plugin;

import net.echinopsii.ariane.community.core.portal.base.model.MainMenuEntity;

import java.util.TreeSet;

/**
 * This registry contains all Arian main menu entity. <br/>
 * This is used by any Ariane component which needs to register its main menu entities and by the main menu controller which reads the registry and forward it to the main menu view.
 */
public interface MainMenuEntityRegistry {

    /**
     * Register a left menu entity to the main menu registry
     *
     * @param mainMenuEntity the menu entity to register
     * @return the registered menu entity
     * @throws Exception if the menu entity is not valid
     */
    public MainMenuEntity registerMainLeftMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;

    /**
     * Unregister a left menu entity from the main menu registry
     *
     * @param mainMenuEntity the menu entity to unregister
     * @return the unregistered menu entity
     * @throws Exception if the menu entity is not valid
     */
    public MainMenuEntity unregisterMainLeftMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;

    /**
     * Get sorted main left menu entities
     *
     * @return sorted main left menu entities
     */
    public TreeSet<MainMenuEntity> getMainLeftMenuEntities();

    /**
     * Get sorted main left menu entities from a parent
     *
     * @param parent the parent entity start point
     *
     * @return sorte main left menu entities from the parent
     */
    public TreeSet<MainMenuEntity> getMainLeftMenuEntitiesFromParent(MainMenuEntity parent);

    /**
     * Register a right menu entity to the main menu registry
     *
     * @param mainMenuEntity the menu entity to register
     * @return the registered menu entity
     * @throws Exception if the menu entity is not valid
     */
    public MainMenuEntity registerMainRightMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;

    /**
     * Unregister a right menu entity from the main menu registry
     *
     * @param mainMenuEntity the menu entity to unregister
     * @return the unregistered menu entity
     * @throws Exception if the menu entity is not valid
     */
    public MainMenuEntity unregisterMainRightMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;

    /**
     * Get sorted main right menu entities
     *
     * @return sorted main right menu entities
     */
    public TreeSet<MainMenuEntity> getMainRightMenuEntities();

    /**
     * Get sorted main leftt menu entities from a parent
     *
     * @param parent the parent entity start point
     *
     * @return sorte main leftt menu entities from the parent
     */
    public TreeSet<MainMenuEntity> getMainRightMenuEntitiesFromParent(MainMenuEntity parent);
}