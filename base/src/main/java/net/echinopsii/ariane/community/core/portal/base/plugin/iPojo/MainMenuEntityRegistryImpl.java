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
 * This registry contains all Ariane main menu entity. <br/>
 * This is used by any Ariane components which needs to register its main menu entities and by the main menu controller which reads the registry and forward it to the main menu view.<br/><br/>
 *
 * This is the iPojo implementation of {@link MainMenuEntityRegistry}. The component is instantiated at commons-services bundle startup. It provides the {@link MainMenuEntityRegistry} service.
 */
@Component
@Provides
@Instantiate
public class MainMenuEntityRegistryImpl implements MainMenuEntityRegistry {

    private static final String MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME = "Ariane Portal Main Menu Item Registry";
    private static final Logger log = LoggerFactory.getLogger(MainMenuEntityRegistryImpl.class);

    private TreeSet<MainMenuEntity> registry = new TreeSet<MainMenuEntity>();

    @Validate
    public void validate() throws Exception {
        log.info("{} is started", new Object[]{MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        registry.clear();
        log.info("{} is stopped", new Object[]{MAIN_MENU_ITEM_REGISTRY_SERVICE_NAME});
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