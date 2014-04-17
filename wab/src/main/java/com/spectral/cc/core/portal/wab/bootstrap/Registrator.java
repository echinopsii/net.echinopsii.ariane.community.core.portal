/**
 * Portal Web App Bundle
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

package com.spectral.cc.core.portal.wab.bootstrap;

import com.spectral.cc.core.portal.wat.plugin.MainMenuRegistryConsumer;
import com.spectral.cc.core.portal.base.model.MainMenuEntity;
import com.spectral.cc.core.portal.base.model.MenuEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Registrator implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Registrator.class);

    private static String MAIN_MENU_PORTAL_CONTEXT = "/CC/";
    //private static int    MAIN_MENU_DASH_RANK = 1;
    //private static int    MAIN_MENU_SPREAD_RANK = 5;
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
                log.debug("CC Portal main menu registry is missing to load {}. Sleep some times...", OsgiActivator.PORTAL_MAIN_MENU_REGISTRATOR_SERVICE_NAME);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        try {
            /*
            entity = new MainMenuEntity("dashboardMItem", "Dashboard", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_DASH_RANK, "icon-dashboard icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("spreadsheetMItem", "Spreadsheet", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_SPREAD_RANK, "icon-table icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);
            */

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
            adminSB.getDisplayRoles().add("ccsecadmin");
            adminSB.getDisplayRoles().add("ccsecreviewer");
            adminSB.getDisplayPermissions().add("ccSecResource:display");
            adminSB.getDisplayPermissions().add("ccSecPermission:display");
            adminSB.getDisplayPermissions().add("ccSecRole:display");
            adminSB.getDisplayPermissions().add("ccSecGroup:display");
            adminSB.getDisplayPermissions().add("ccSecUser:display");
            OsgiActivator.mainPortalMainMenuEntityList.add(adminSB);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(adminSB);

            /*
            entity = new MainMenuEntity("configurationMItem", "Configuration", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-beer icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);
            */

            entity = new MainMenuEntity("securityMItem", "Security",  MAIN_MENU_PORTAL_CONTEXT + "views/admin/security.jsf", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-key icon-large").setParent(adminSB);
            entity.getDisplayRoles().add("ccsecadmin");
            entity.getDisplayRoles().add("ccsecreviewer");
            entity.getDisplayPermissions().add("ccSecResource:display");
            entity.getDisplayPermissions().add("ccSecPermission:display");
            entity.getDisplayPermissions().add("ccSecRole:display");
            entity.getDisplayPermissions().add("ccSecGroup:display");
            entity.getDisplayPermissions().add("ccSecUser:display");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("dbSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, null).setParent(adminSB);
            entity.getDisplayRoles().add("ccdbadmin");
            entity.getDisplayPermissions().add("ccSQLConsole:display");
            entity.getDisplayPermissions().add("ccNeo4JConsole:display");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("neo4JMItem", "Neo4J Console",  MAIN_MENU_PORTAL_CONTEXT + "views/admin/neo4j.jsf", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-terminal icon-large").setParent(adminSB);
            entity.getDisplayRoles().add("ccdbadmin");
            entity.getDisplayPermissions().add("ccNeo4JConsole:display");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("sqlMItem", "SQL Console",  MAIN_MENU_PORTAL_CONTEXT + "views/admin/sql.jsf", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-terminal icon-large").setParent(adminSB);
            entity.getDisplayRoles().add("ccdbadmin");
            entity.getDisplayPermissions().add("ccSQLConsole:display");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("virgoSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, null).setParent(adminSB);
            entity.getDisplayRoles().add("ccvirgoadmin");
            entity.getDisplayPermissions().add("ccVirgoConsole:display");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);
            entity = new MainMenuEntity("virgoMItem", "Virgo Console",  MAIN_MENU_PORTAL_CONTEXT + "views/admin/virgo.jsf", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-terminal icon-large").setParent(adminSB);

            entity.getDisplayRoles().add("ccvirgoadmin");
            entity.getDisplayPermissions().add("ccVirgoConsole:display");
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            /*
            entity = new MainMenuEntity("aasSeparator", null, null, MenuEntityType.TYPE_MENU_SEPARATOR, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, null).setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("marketplaceMItem", "Marketplace", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-shopping-cart icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("updateMItem", "Update", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-refresh").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            entity = new MainMenuEntity("installMItem", "Install new software", "#", MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_ADMIN_RANK * 10 + submenuCount++, "icon-circle-arrow-down icon-large").setParent(adminSB);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);
            */

            submenuCount = 0;
            MainMenuEntity homeEntity = new MainMenuEntity("homeSButton", "Home", null, MenuEntityType.TYPE_MENU_SUBMENU, MAIN_MENU_HOME_RANK, "icon-home icon-large");
            OsgiActivator.mainPortalMainMenuEntityList.add(homeEntity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(homeEntity);

            entity = new MainMenuEntity("accountMItem", "Manage account",
                                        MAIN_MENU_PORTAL_CONTEXT + "views/home/userProfile.jsf",
                                        MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HOME_RANK * 10 + submenuCount++,
                                        "icon-user icon-large").setParent(homeEntity);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);

            /*
            entity = new MainMenuEntity("wpMItem", "White pages",
                                        MAIN_MENU_PORTAL_CONTEXT + "#",
                                        MenuEntityType.TYPE_MENU_ITEM, MAIN_MENU_HOME_RANK * 10 + submenuCount++,
                                        "icon-group icon-large").setParent(homeEntity);
            OsgiActivator.mainPortalMainMenuEntityList.add(entity);
            MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().registerMainMenuEntity(entity);
            */

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
