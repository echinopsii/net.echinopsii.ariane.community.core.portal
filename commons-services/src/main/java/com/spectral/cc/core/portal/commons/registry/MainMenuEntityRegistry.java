/**
 * Portal Commons Services bundle
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
package com.spectral.cc.core.portal.commons.registry;

import com.spectral.cc.core.portal.commons.model.MainMenuEntity;

import java.util.TreeSet;

/**
 * This registry contains all CC main menu entity. <br/>
 * This is used by any CC component which needs to register its main menu entities and by the main menu controller which reads the registry and forward it to the main menu view.
 */
public interface MainMenuEntityRegistry {

    /**
     * Register a menu entity to the main menu registry
     *
     * @param mainMenuEntity the menu entity to register
     * @return the registered menu entity
     * @throws Exception if the menu entity is not valid
     */
    public MainMenuEntity registerMainMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;

    /**
     * Unregister a menu entity from the main menu registry
     *
     * @param mainMenuEntity the menu entity to unregister
     * @return the unregistered menu entity
     * @throws Exception if the menu entity is not valid
     */
    public MainMenuEntity unregisterMainMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;

    /**
     * Get sorted main menu entities
     *
     * @return sorted main menu entities
     */
    public TreeSet<MainMenuEntity> getMainMenuEntities();

    /**
     * Get sorted main menu entities from a parent
     *
     * @param parent the parent entity start point
     *
     * @return sorte main menu entities from the parent
     */
    public TreeSet<MainMenuEntity> getMainMenuEntitiesFromParent(MainMenuEntity parent);
}