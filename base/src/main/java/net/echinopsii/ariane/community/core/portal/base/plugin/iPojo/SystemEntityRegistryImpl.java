/**
 * Portal base bundle
 * System Registry iPojo impl
 * Copyright (C) 2015 Mathilde Ffrench
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

import net.echinopsii.ariane.community.core.portal.base.model.UIInsertEntity;
import net.echinopsii.ariane.community.core.portal.base.plugin.SystemEntityRegistry;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
@Provides
@Instantiate
public class SystemEntityRegistryImpl implements SystemEntityRegistry {

    private static final String SYS_TAB_ITEM_REGISTRY_SERVICE_NAME = "Ariane Portal System Item Registry";
    private static final Logger log = LoggerFactory.getLogger(SystemEntityRegistryImpl.class);

    private ArrayList<UIInsertEntity> tabMenuEntityRegistry = new ArrayList<>();
    private ArrayList<UIInsertEntity> dialogEntityRegistry = new ArrayList<>();

    @Validate
    public void validate() throws Exception {
        log.info("{} is started", new Object[]{SYS_TAB_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){
        tabMenuEntityRegistry.clear();
        dialogEntityRegistry.clear();
        log.info("{} is stopped", new Object[]{SYS_TAB_ITEM_REGISTRY_SERVICE_NAME});
    }

    @Override
    public UIInsertEntity registerTabMenuEntity(UIInsertEntity entity) {
        tabMenuEntityRegistry.add(entity);
        return entity;
    }

    @Override
    public UIInsertEntity registerDialogEntity(UIInsertEntity entity) {
        dialogEntityRegistry.add(entity);
        return entity;
    }

    @Override
    public List<UIInsertEntity> getTabMenuEntities() {
        return tabMenuEntityRegistry;
    }

    @Override
    public List<UIInsertEntity> getDialogEntities() {
        return dialogEntityRegistry;
    }

}
