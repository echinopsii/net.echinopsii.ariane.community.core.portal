/**
 * Portal base bundle
 * Main Menu Entity Registry iPojo impl test
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

package net.echinopsii.ariane.community.core.portal.base.registry;

import net.echinopsii.ariane.community.core.portal.base.model.MainMenuEntity;
import net.echinopsii.ariane.community.core.portal.base.model.MenuEntityType;
import net.echinopsii.ariane.community.core.portal.base.plugin.MainMenuEntityRegistry;
import net.echinopsii.ariane.community.core.portal.base.plugin.iPojo.MainMenuEntityRegistryImpl;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainMenuEntityRegistryTest extends TestCase {

    private static MainMenuEntityRegistry mainMenuEntityRegistry = new MainMenuEntityRegistryImpl();

    @BeforeClass
    public static void testSetup() {
    }

    @AfterClass
    public static void testCleanup() {
        ((MainMenuEntityRegistryImpl)mainMenuEntityRegistry).invalidate();
    }

    @Test
    public void testRegisteringUnregisteringEntity() throws Exception {
        MainMenuEntity mmEntity = new MainMenuEntity("dashboardMItem", "Dashboard", "#", MenuEntityType.TYPE_MENU_ITEM, 1, "icon-dashboard icon-large");
        mainMenuEntityRegistry.registerMainMenuEntity(mmEntity);
        assertTrue(mainMenuEntityRegistry.getMainMenuEntities().contains(mmEntity));
        mainMenuEntityRegistry.unregisterMainMenuEntity(mmEntity);
        assertFalse(mainMenuEntityRegistry.getMainMenuEntities().contains(mmEntity));
    }

    @Test
    public void testGetEntityFromParent() throws Exception {
        MainMenuEntity mmParentEntity = new MainMenuEntity("helpSButton", "Help", null, MenuEntityType.TYPE_MENU_SUBMENU, 6, "icon-question-sign icon-large");
        mainMenuEntityRegistry.registerMainMenuEntity(mmParentEntity);
        MainMenuEntity mmChildEntity = new MainMenuEntity("bookMItem", "Help book", "#", MenuEntityType.TYPE_MENU_ITEM, 61, "icon-beer icon-large").setParent(mmParentEntity);
        mainMenuEntityRegistry.registerMainMenuEntity(mmChildEntity);
        assertTrue(mainMenuEntityRegistry.getMainMenuEntitiesFromParent(mmParentEntity).contains(mmChildEntity));
    }
}