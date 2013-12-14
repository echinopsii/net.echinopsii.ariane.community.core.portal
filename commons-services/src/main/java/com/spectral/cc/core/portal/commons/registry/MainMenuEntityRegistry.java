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

public interface MainMenuEntityRegistry {
    public MainMenuEntity registerMainMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;
    public MainMenuEntity unregisterMainMenuEntity(MainMenuEntity mainMenuEntity) throws Exception;

    public TreeSet<MainMenuEntity> getMainMenuEntities();
    public TreeSet<MainMenuEntity> getMainMenuEntitiesFromParent(MainMenuEntity parent);
}