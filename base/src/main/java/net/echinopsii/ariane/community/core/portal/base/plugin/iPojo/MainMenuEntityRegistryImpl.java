/**
 * Portal base bundle
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
package net.echinopsii.ariane.community.core.portal.base.plugin.iPojo;

import net.echinopsii.ariane.community.core.portal.base.model.MainMenuEntity;
import net.echinopsii.ariane.community.core.portal.base.plugin.MainMenuEntityRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeSet;

/**
 * This leftRegistry contains all Ariane main menu entity. <br/>
 * This is used by any Ariane components which needs to register its main menu entities and by the main menu controller which reads the leftRegistry and forward it to the main menu view.<br/><br/>
 *
 * This is the iPojo implementation of {@link MainMenuEntityRegistry}. The component is instantiated at commons-services bundle startup. It provides the {@link MainMenuEntityRegistry} service.
 */
@Component
@Provides
@Instantiate
public class MainMenuEntityRegistryImpl implements MainMenuEntityRegistry {

    private static final String MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME = "Ariane Portal Main Menu Item Registry";
    private static final Logger log = LoggerFactory.getLogger(MainMenuEntityRegistryImpl.class);

    private TreeSet<MainMenuEntity> leftRegistry = new TreeSet<MainMenuEntity>();
    private TreeSet<MainMenuEntity> rightRegistry = new TreeSet<MainMenuEntity>();

    @Validate
    public void validate() throws Exception {
        log.info("{} is started", new Object[]{MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        rightRegistry.clear();
        leftRegistry.clear();
        log.info("{} is stopped", new Object[]{MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Override
    public MainMenuEntity registerMainLeftMenuEntity(MainMenuEntity mainMenuEntity) throws Exception {
        if (mainMenuEntity!=null) {
            if (mainMenuEntity.isValid()) {
                leftRegistry.add(mainMenuEntity);
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
    public MainMenuEntity unregisterMainLeftMenuEntity(MainMenuEntity mainMenuEntity) throws Exception {
        if (mainMenuEntity!=null) {
            if (mainMenuEntity.isValid()) {
                leftRegistry.remove(mainMenuEntity);
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
    public TreeSet<MainMenuEntity> getMainLeftMenuEntities() {
        return this.leftRegistry;
    }

    @Override
    public TreeSet<MainMenuEntity> getMainLeftMenuEntitiesFromParent(MainMenuEntity parent) {
        TreeSet<MainMenuEntity> ret = new TreeSet<MainMenuEntity>();
        for (MainMenuEntity entity : leftRegistry) {
            if (entity.getParent()!=null && entity.getParent().equals(parent))
                ret.add(entity);
        }
        return ret;
    }

    @Override
    public MainMenuEntity registerMainRightMenuEntity(MainMenuEntity mainMenuEntity) throws Exception {
        if (mainMenuEntity!=null) {
            if (mainMenuEntity.isValid()) {
                rightRegistry.add(mainMenuEntity);
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
    public MainMenuEntity unregisterMainRightMenuEntity(MainMenuEntity mainMenuEntity) throws Exception {
        if (mainMenuEntity!=null) {
            if (mainMenuEntity.isValid()) {
                rightRegistry.remove(mainMenuEntity);
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
    public TreeSet<MainMenuEntity> getMainRightMenuEntities() {
        return rightRegistry;
    }

    @Override
    public TreeSet<MainMenuEntity> getMainRightMenuEntitiesFromParent(MainMenuEntity parent) {
        TreeSet<MainMenuEntity> ret = new TreeSet<MainMenuEntity>();
        for (MainMenuEntity entity : rightRegistry) {
            if (entity.getParent()!=null && entity.getParent().equals(parent))
                ret.add(entity);
        }
        return ret;
    }
}