/**
 * Portal wat bundle
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
package net.echinopsii.ariane.community.core.portal.wat.controller;

import net.echinopsii.ariane.community.core.portal.wat.plugin.MainMenuRegistryConsumer;
import net.echinopsii.ariane.community.core.portal.base.model.MainMenuEntity;
import net.echinopsii.ariane.community.core.portal.base.model.MenuEntityType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.component.separator.Separator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Generate primefaces menu model from Main menu entity registry. Used by main layout view.</br>
 * This is a request managed bean.
 */
public class MainMenuController implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MainMenuController.class);

    private MenuModel model = new DefaultMenuModel();

    /**
     * check if subject is authorized to display the main menu entity
     *
     * @param subject the shiro subject
     * @param entity the main menu entity to check
     *
     * @return if authorized true else false
     */
    private static boolean isAuthorized(Subject subject, MainMenuEntity entity) {
        boolean ret = false;
        if (subject.hasRole("Jedi") || subject.isPermitted("universe:zeone") || entity.getDisplayRoles().size()==0) {
            ret = true;
        } else {
            for (String role : entity.getDisplayRoles())
                if (subject.hasRole(role)) {
                    ret = true;
                    break;
                }
            if (!ret) {
                for (String perm : entity.getDisplayPermissions())
                    if (subject.isPermitted(perm)) {
                        ret = true;
                        break;
                    }
            }
        }
        return ret;
    }

    /**
     * Create PrimeFaces menu item from main menu entity
     *
     * @param entity the main menu entity
     *
     * @return the generated PrimeFaces menu item
     */
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

    /**
     * Create PrimeFaces submenu from main menu entity
     *
     * @param subject the shiro subject
     * @param entity the main menu entity
     *
     * @return the generated PrimeFaces sub menu
     */
    private static Submenu createSubMenuFromEntity(Subject subject, MainMenuEntity entity) {
        Submenu submenu = new Submenu();
        submenu.setId(entity.getId());
        submenu.setStyleClass("menuItem");
        submenu.setLabel(entity.getValue());
        submenu.setIcon(entity.getIcon());
        for (MainMenuEntity subEntity : MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().getMainMenuEntitiesFromParent(entity)) {
            switch(subEntity.getType()) {
                case MenuEntityType.TYPE_MENU_ITEM:
                    if (isAuthorized(subject, subEntity)) {
                        MenuItem item = createMenuItemFromEntity(subEntity);
                        submenu.getChildren().add(item);
                    }
                    break;
                case MenuEntityType.TYPE_MENU_SEPARATOR:
                    if (isAuthorized(subject, subEntity)) {
                        Separator separator = new Separator();
                        separator.setId(subEntity.getId());
                        submenu.getChildren().add(separator);
                    }
                    break;
                default:
                    break;
            }
        }
        return submenu;
    }

    /**
     * Generate PrimeFaces MenuModel from Main Menu Entity Registry service and return it
     *
     * @return generated PrimeFaces MenuModel
     */
    public MenuModel getModel() {
        log.debug("Get Menu Model...");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() && MainMenuRegistryConsumer.getInstance()!=null) {
            for (MainMenuEntity entity : MainMenuRegistryConsumer.getInstance().getMainMenuEntityRegistry().getMainMenuEntities()) {
                if (entity.getParent()==null) {
                    switch (entity.getType()) {
                        case MenuEntityType.TYPE_MENU_ITEM:
                            if (isAuthorized(subject, entity)) {
                                MenuItem item = createMenuItemFromEntity(entity);
                                model.addMenuItem(item);
                            }
                            break;
                        case MenuEntityType.TYPE_MENU_SUBMENU:
                            if (isAuthorized(subject, entity)) {
                                Submenu submenu = createSubMenuFromEntity(subject, entity);
                                model.addSubmenu(submenu);
                            }
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