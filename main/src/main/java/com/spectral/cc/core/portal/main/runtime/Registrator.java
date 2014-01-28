/**
 * Portal Main
 * Registrator
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

package com.spectral.cc.core.portal.main.runtime;

import com.spectral.cc.core.portal.commons.consumer.MainMenuRegistryConsumer;
import com.spectral.cc.core.portal.commons.model.MainMenuEntity;
import com.spectral.cc.core.portal.commons.model.MenuEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Registrator implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Registrator.class);

    private static String MAIN_MENU_PORTAL_CONTEXT = "/CCmain/";
    private static int    MAIN_MENU_DASH_RANK = 1;
    private static int    MAIN_MENU_SPREAD_RANK = 5;
    private static int    MAIN_MENU_HELP_RANK = 6;
    private static int    MAIN_MENU_ADMIN_RANK = 7;
    private static int    MAIN_MENU_HOME_RANK = 8;

    @Override
    public void run() {
        int submenuCount;
        MainMenuEntity entity;

        //TODO : remove this uuugly sleep
        //TODO : check a better way to start war after OSGI layer

        while(MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry()==null)
            try {
                log.info("Portal main menu registry is missing to load {}. Sleep some times...", OsgiActivator.PORTAL_MAIN_MENU_REGISTRATOR_SERVICE_NAME);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        try {
            entity = new MainMenuEntity("dashboardMItem", "Dashboard", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_DASH_RANK, "icon-dashboard icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("spreadsheetMItem", "Spreadsheet", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_SPREAD_RANK, "icon-table icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            submenuCount = 0;
            MainMenuEntity helpSB = new MainMenuEntity("helpSButton", "Help", null, MenuEntityType.TYPE_MENU_SUBMENU, MAIN_MENU_HELP_RANK, "icon-question-sign icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(helpSB);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(helpSB);

            entity = new MainMenuEntity("bookMItem", "Help book", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HELP_RANK * 10 + submenuCount++, "icon-beer icon-large").setParent(helpSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("bookSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, MAIN_MENU_HELP_RANK * 10 + submenuCount++, null).setParent(helpSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("aboutMItem", "About", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HELP_RANK * 10 + submenuCount++, "icon-beer icon-large").setParent(helpSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("contactMItem", "Contact", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HELP_RANK * 10 + submenuCount++, "icon-envelope icon-large").setParent(helpSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("bugMItem", "Report problem", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HELP_RANK * 10 + submenuCount++, "icon-bug icon-large").setParent(helpSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);



            submenuCount = 0;
            MainMenuEntity adminSB = new MainMenuEntity("administrationSButton", "Administration", null, MenuEntityType.TYPE_MENU_SUBMENU, MAIN_MENU_ADMIN_RANK, "icon-cog icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(adminSB);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(adminSB);

            entity = new MainMenuEntity("udirectoryMItem", "User directory", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-beer icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("usersMItem", "Users", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-user icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("groupsMItem", "Groups", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-group icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("rolesMItem", "Roles", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-beer icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("aasSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, null).setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("marketplaceMItem", "Marketplace", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-shopping-cart icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("updateMItem", "Update", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-refresh").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("installMItem", "Install new software...", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-circle-arrow-down icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);



            submenuCount = 0;
            MainMenuEntity homeEntity = new MainMenuEntity("homeSButton", "Home", null, MenuEntityType.TYPE_MENU_SUBMENU, MAIN_MENU_HOME_RANK, "icon-home icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(homeEntity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(homeEntity);

            entity = new MainMenuEntity(
                                                              "accountMItem", "Manage account",
                                                              MAIN_MENU_PORTAL_CONTEXT + "views/userHome.jsf",
                                                              MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HOME_RANK * 10 + submenuCount++,
                                                              "icon-user icon-large").setParent(homeEntity);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity(
                                               "accountSeparator", null, null,
                                               MenuEntityType.TYPE_MENU_SEPARATOR, MAIN_MENU_HOME_RANK * 10 + submenuCount++,
                                               null).setParent(homeEntity);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity(
                                               "logoutMItem", "Logout",
                                               MAIN_MENU_PORTAL_CONTEXT,
                                               MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HOME_RANK * 10 + submenuCount++,
                                               "icon-beer icon-large").setParent(homeEntity).setActionListener("#{loginController.logout}");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            log.debug("{} has registered its main menu items", new Object[]{OsgiActivator.PORTAL_MAIN_MENU_REGISTRATOR_SERVICE_NAME});

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
