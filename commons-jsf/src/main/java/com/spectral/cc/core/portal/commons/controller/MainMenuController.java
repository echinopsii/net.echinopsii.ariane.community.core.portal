/**
 * Portal Commons JSF bundle
 * Main Menu Controller
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
package com.spectral.cc.core.portal.commons.controller;

import com.spectral.cc.core.portal.commons.consumer.MainMenuRegistryConsumer;
import com.spectral.cc.core.portal.commons.model.MainMenuEntity;
import com.spectral.cc.core.portal.commons.model.MenuEntityType;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.component.separator.Separator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.faces.context.FacesContext;
import java.io.Serializable;

public class MainMenuController implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MainMenuController.class);

    private MenuModel model = new DefaultMenuModel();

    private static MenuItem createMenuItemFromEntity(MainMenuEntity entity) {
        FacesContext context = FacesContext.getCurrentInstance();
        MenuItem item = new MenuItem();
        item.setId(entity.getId());
        if (entity.getActionListener()==null || entity.getActionListener().equals("")) {
            item.setUrl(context.getExternalContext().getRequestScheme() + "://" +
                        context.getExternalContext().getRequestServerName() + ":" +
                        context.getExternalContext().getRequestServerPort() +
                        entity.getContextAddress());
        }
        item.setIcon(entity.getIcon());
        item.setValue(entity.getValue());
        item.setStyleClass("menuItem");
        if (entity.getActionListener()!=null && !entity.getActionListener().equals("")) {
            item.setActionExpression(context.getApplication().getExpressionFactory().createMethodExpression(
                                                                                                                   context.getELContext(),entity.getActionListener(),
                                                                                                                   null,new Class[]{}
            ));
        }
        return item;
    }

    private static Submenu createSubMenuFromEntity(MainMenuEntity entity) {
        Submenu submenu = new Submenu();
        submenu.setId(entity.getId());
        submenu.setStyleClass("menuItem");
        submenu.setLabel(entity.getValue());
        submenu.setIcon(entity.getIcon());
        for (MainMenuEntity subEntity : MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().getMainMenuEntitiesFromParent(entity)) {
            switch(subEntity.getType()) {
                case MenuEntityType.TYPE_MENU_ITEM:
                    MenuItem item = createMenuItemFromEntity(subEntity);
                    submenu.getChildren().add(item);
                    break;
                case MenuEntityType.TYPE_MENU_SEPARATOR:
                    Separator separator = new Separator();
                    separator.setId(subEntity.getId());
                    submenu.getChildren().add(separator);
                    break;
                default:
                    break;
            }
        }
        return submenu;
    }

    public MenuModel getModel() {
        log.debug("Get Menu Model...");
        if (MainMenuRegistryConsumer.getInstance()!=null) {
            for (MainMenuEntity entity : MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().getMainMenuEntities()) {
                if (entity.getParent()==null) {
                    switch (entity.getType()) {
                        case MenuEntityType.TYPE_MENU_ITEM:
                            MenuItem item = createMenuItemFromEntity(entity);
                            model.addMenuItem(item);
                            break;
                        case MenuEntityType.TYPE_MENU_SUBMENU:
                            Submenu submenu = createSubMenuFromEntity(entity);
                            model.addSubmenu(submenu);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return model;
    }
}