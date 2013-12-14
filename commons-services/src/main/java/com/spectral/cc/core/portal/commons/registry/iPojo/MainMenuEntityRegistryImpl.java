/**
 * Portal Commons Services bundle
 * Main Menu Entity Registry iPojo impl
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
package com.spectral.cc.core.portal.commons.registry.iPojo;

import com.spectral.cc.core.portal.commons.model.MainMenuEntity;
import com.spectral.cc.core.portal.commons.registry.MainMenuEntityRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;

@Component
@Provides
@Instantiate
public class MainMenuEntityRegistryImpl implements MainMenuEntityRegistry {

    private static final String MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME = "Portal Main Menu Item Registry Service";
    private static final Logger log = LoggerFactory.getLogger(MainMenuEntityRegistryImpl.class);

    private TreeSet<MainMenuEntity> registry = new TreeSet<MainMenuEntity>();

    @Validate
    public void validate() throws Exception {
        //TODO : move bellow code on JUNIT test
        //this.registerMainMenuEntity(new MainMenuEntity("dashboardMItem", "Dashboard", "#", MenuEntityType.TYPE_MENU_ITEM, 1, "icon-dashboard icon-large"));
        //this.registerMainMenuEntity(new MainMenuEntity("directoriesMItem", "Directories", "#", MenuEntityType.TYPE_MENU_ITEM, 2, "icon-book icon-large"));
        //this.registerMainMenuEntity(new MainMenuEntity("injectorsMItem", "Injectors", "#", MenuEntityType.TYPE_MENU_ITEM, 3, "icon-filter icon-large"));
        //this.registerMainMenuEntity(new MainMenuEntity("mappingMItem", "Mapping", "/CCmapping/views/mapping.jsf", MenuEntityType.TYPE_MENU_ITEM, 4, "icon-sitemap icon-large"));
        //this.registerMainMenuEntity(new MainMenuEntity("spreadsheetMItem", "Spreadsheet", "#", MenuEntityType.TYPE_MENU_ITEM, 5, "icon-table icon-large"));

        /*
        MainMenuEntity helpSB = new MainMenuEntity("helpSButton", "Help", null, MenuEntityType.TYPE_MENU_SUBMENU, 6, "icon-question-sign icon-large");
        this.registerMainMenuEntity(helpSB);
        this.registerMainMenuEntity(new MainMenuEntity("bookMItem", "Help book", "#", MenuEntityType.TYPE_MENU_ITEM, 61, "icon-beer icon-large").setParent(helpSB));
        this.registerMainMenuEntity(new MainMenuEntity("bookSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, 62, null).setParent(helpSB));
        this.registerMainMenuEntity(new MainMenuEntity("aboutMItem", "About", "#", MenuEntityType.TYPE_MENU_ITEM, 63, "icon-beer icon-large").setParent(helpSB));
        this.registerMainMenuEntity(new MainMenuEntity("contactMItem", "Contact", "#", MenuEntityType.TYPE_MENU_ITEM, 64, "icon-envelope icon-large").setParent(helpSB));
        this.registerMainMenuEntity(new MainMenuEntity("bugMItem", "Report problem", "#", MenuEntityType.TYPE_MENU_ITEM, 65, "icon-bug icon-large").setParent(helpSB));

        MainMenuEntity adminSB = new MainMenuEntity("administrationSButton", "Administration", null, MenuEntityType.TYPE_MENU_SUBMENU, 8, "icon-cog icon-large");
        this.registerMainMenuEntity(adminSB);
        this.registerMainMenuEntity(new MainMenuEntity("udirectoryMItem", "User directory", "#", MenuEntityType.TYPE_MENU_ITEM, 71, "icon-beer icon-large").setParent(adminSB));
        this.registerMainMenuEntity(new MainMenuEntity("usersMItem", "Users", "#", MenuEntityType.TYPE_MENU_ITEM, 72, "icon-user icon-large").setParent(adminSB));
        this.registerMainMenuEntity(new MainMenuEntity("groupsMItem", "Groups", "#", MenuEntityType.TYPE_MENU_ITEM, 73, "icon-group icon-large").setParent(adminSB));
        this.registerMainMenuEntity(new MainMenuEntity("rolesMItem", "Roles", "#", MenuEntityType.TYPE_MENU_ITEM, 74, "icon-beer icon-large").setParent(adminSB));
        this.registerMainMenuEntity(new MainMenuEntity("aasSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, 75, null).setParent(adminSB));
        this.registerMainMenuEntity(new MainMenuEntity("marketplaceMItem", "Marketplace", "#", MenuEntityType.TYPE_MENU_ITEM, 76, "icon-shopping-cart icon-large").setParent(adminSB));
        this.registerMainMenuEntity(new MainMenuEntity("updateMItem", "Update", "#", MenuEntityType.TYPE_MENU_ITEM, 77, "icon-refresh").setParent(adminSB));
        this.registerMainMenuEntity(new MainMenuEntity("installMItem", "Install new software...", "#", MenuEntityType.TYPE_MENU_ITEM, 78, "icon-circle-arrow-down icon-large").setParent(adminSB));

        MainMenuEntity homeSB = new MainMenuEntity("homeSButton", "Home", null, MenuEntityType.TYPE_MENU_SUBMENU, 8, "icon-home icon-large");
        this.registerMainMenuEntity(homeSB);
        this.registerMainMenuEntity(new MainMenuEntity("accountMItem", "Manage account", "/CCmainl/views/userHome.jsf", MenuEntityType.TYPE_MENU_ITEM, 81, "icon-user icon-large").setParent(homeSB));
        this.registerMainMenuEntity(new MainMenuEntity("accountSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, 82, null).setParent(homeSB));
        this.registerMainMenuEntity(
             new MainMenuEntity(
                                       "logoutMItem", "Logout",
                                       "/CCmain/",
                                       MenuEntityType.TYPE_MENU_ITEM, 83,
                                       "icon-beer icon-large").setParent(homeSB).setActionListener("#{loginController.logout}"));
        */
        log.debug("{} is started.", new Object[]{MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        log.debug("Stopping {}...", new Object[]{MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME});
        registry.clear();
        log.debug("{} is stopped.", new Object[]{MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Override
    public MainMenuEntity registerMainMenuEntity(MainMenuEntity mainMenuEntity) throws Exception {
        if (mainMenuEntity!=null) {
            if (mainMenuEntity.isValid()) {
                registry.add(mainMenuEntity);
            } else {
                throw new Exception("Invalid main menu entity : { id:" + mainMenuEntity.getId() + ", value:" + mainMenuEntity.getValue() + ", contextAddress:" + mainMenuEntity.getContextAddress() +
                                            ", icon:" + mainMenuEntity.getIcon() + ", type:" + mainMenuEntity.getType() + " }");
            }
        } else {
            throw new NullPointerException();
        }
        return mainMenuEntity;
    }

    @Override
    public MainMenuEntity unregisterMainMenuEntity(MainMenuEntity mainMenuEntity) throws Exception {
        if (mainMenuEntity!=null) {
            if (mainMenuEntity.isValid()) {
                registry.remove(mainMenuEntity);
            } else {
                throw new Exception("Invalid main menu entity : { id:" + mainMenuEntity.getId() + ", value:" + mainMenuEntity.getValue() + ", contextAddress:" + mainMenuEntity.getContextAddress() +
                                            ", icon:" + mainMenuEntity.getIcon() + ", type:" + mainMenuEntity.getType() + " }");
            }
        } else {
            throw new NullPointerException();
        }
        return mainMenuEntity;
    }

    @Override
    public TreeSet<MainMenuEntity> getMainMenuEntities() {
        return this.registry;
    }

    @Override
    public TreeSet<MainMenuEntity> getMainMenuEntitiesFromParent(MainMenuEntity parent) {
        TreeSet<MainMenuEntity> ret = new TreeSet<MainMenuEntity>();
        for (MainMenuEntity entity : registry) {
            if (entity.getParent()!=null && entity.getParent().equals(parent))
                ret.add(entity);
        }
        return ret;
    }
}