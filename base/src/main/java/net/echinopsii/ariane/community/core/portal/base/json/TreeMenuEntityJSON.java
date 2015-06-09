/**
 * Portal Base
 * TreeMenuEntity JSON
 *
 * Copyright (C) 08/05/15 echinopsii
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
package net.echinopsii.ariane.community.core.portal.base.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.echinopsii.ariane.community.core.portal.base.model.TreeMenuEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class TreeMenuEntityJSON {

    public final static String TREE_MENU_ENTITY_ID = "id";
    public final static String TREE_MENU_ENTITY_VALUE = "value";
    public final static String TREE_MENU_ENTITY_TYPE  = "type";
    public final static String TREE_MENU_ENTITY_CONTEXT_ADDRESS = "contextAddress";
    public final static String TREE_MENU_ENTITY_DESCRIPTION = "description";
    public final static String TREE_MENU_ENTITY_ICON = "icon";
    public final static String TREE_MENU_ENTITY_DISPLAY_ROLES = "displayRoles";
    public final static String TREE_MENU_ENTITY_DISPLAY_PERMISSIONS = "displayPermissions";
    public final static String TREE_MENU_ENTITY_OTHER_ACTIONS_ROLES = "otherActionsRoles";
    public final static String TREE_MENU_ENTITY_OTHER_ACTIONS_PERMISSIONS = "otherActionsPermissions";
    public final static String TREE_MENU_ENTITY_REMOTE_INJECTOR_TREE_ENTITY_GEARS_CACHE_ID = "remoteInjectorTreeEntityGearsCacheId";
    public final static String TREE_MENU_ENTITY_REMOTE_INJECTOR_TREE_ENTITY_COMPONENTS_CACHE_ID = "remoteInjectorTreeEntityComponentsCacheId";
    public final static String TREE_MENU_ENTITY_PARENT_ENTITY_ID = "parentTreeMenuEntityID";
    public final static String TREE_MENU_ENTITY_CHILDS_ENTITY_IDS = "childsID";

    public final static void treeMenuEntity2JSON(TreeMenuEntity entity, JsonGenerator jgenerator) throws IOException {
        jgenerator.writeStartObject();
        jgenerator.writeStringField(TREE_MENU_ENTITY_ID, entity.getId());
        jgenerator.writeStringField(TREE_MENU_ENTITY_VALUE, entity.getValue());
        jgenerator.writeNumberField(TREE_MENU_ENTITY_TYPE, entity.getType());
        jgenerator.writeStringField(TREE_MENU_ENTITY_CONTEXT_ADDRESS, entity.getContextAddress());
        jgenerator.writeStringField(TREE_MENU_ENTITY_ICON, entity.getIcon());
        jgenerator.writeStringField(TREE_MENU_ENTITY_DESCRIPTION, entity.getDescription());

        jgenerator.writeArrayFieldStart(TREE_MENU_ENTITY_DISPLAY_ROLES);
        for (String role : entity.getDisplayRoles())
            jgenerator.writeString(role);
        jgenerator.writeEndArray();

        jgenerator.writeArrayFieldStart(TREE_MENU_ENTITY_DISPLAY_PERMISSIONS);
        for (String permission : entity.getDisplayPermissions())
            jgenerator.writeString(permission);
        jgenerator.writeEndArray();

        jgenerator.writeStringField(TREE_MENU_ENTITY_PARENT_ENTITY_ID, (entity.getParentTreeMenuEntity()!=null) ? entity.getParentTreeMenuEntity().getId() : "-1");
        jgenerator.writeArrayFieldStart(TREE_MENU_ENTITY_CHILDS_ENTITY_IDS);
        for (TreeMenuEntity child : entity.getChildTreeMenuEntities())
            jgenerator.writeString(child.getId());
        jgenerator.writeEndArray();

        if (entity.getOtherActionsRoles().size()>0) {
            jgenerator.writeObjectFieldStart(TREE_MENU_ENTITY_OTHER_ACTIONS_ROLES);
            for (String actionName : entity.getOtherActionsRoles().keySet())
                jgenerator.writeObjectField(actionName, entity.getOtherActionsRoles().get(actionName));
            jgenerator.writeEndObject();
        }

        if (entity.getOtherActionsPerms().size()>0) {
            jgenerator.writeObjectFieldStart(TREE_MENU_ENTITY_OTHER_ACTIONS_PERMISSIONS);
            for (String actionName : entity.getOtherActionsPerms().keySet())
                jgenerator.writeObjectField(actionName, entity.getOtherActionsPerms().get(actionName));
            jgenerator.writeEndObject();
        }

        if (entity.getRemoteInjectorTreeEntityGearsCacheId()!=null)
            jgenerator.writeStringField(TREE_MENU_ENTITY_REMOTE_INJECTOR_TREE_ENTITY_GEARS_CACHE_ID, entity.getRemoteInjectorTreeEntityGearsCacheId());

        if (entity.getRemoteInjectorTreeEntityComponentsCacheId()!=null)
            jgenerator.writeStringField(TREE_MENU_ENTITY_REMOTE_INJECTOR_TREE_ENTITY_COMPONENTS_CACHE_ID, entity.getRemoteInjectorTreeEntityComponentsCacheId());

        jgenerator.writeEndObject();
    }

    public final static String treeMenuEntity2JSON(TreeMenuEntity entity) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        JsonGenerator jgenerator = jFactory.createGenerator(outStream, JsonEncoding.UTF8);
        treeMenuEntity2JSON(entity, jgenerator);
        jgenerator.close();
        return ToolBox.getOuputStreamContent(outStream, "UTF-8");
    }

    public final static void manyTreeMenuEntity2JSON(Set<TreeMenuEntity> treeMenuEntitySet, JsonGenerator jgenerator) throws IOException {
        for (TreeMenuEntity treeMenuEntity : treeMenuEntitySet) {
            treeMenuEntity2JSON(treeMenuEntity, jgenerator);
            manyTreeMenuEntity2JSON(treeMenuEntity.getChildTreeMenuEntities(), jgenerator);
        }
    }

    public final static String manyTreeMenuEntity2JSON(Set<TreeMenuEntity> treeMenuEntitySet) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        JsonGenerator jgenerator = jFactory.createGenerator(outStream, JsonEncoding.UTF8);
        jgenerator.writeStartObject();
        jgenerator.writeArrayFieldStart("treeMenuEntities");
        manyTreeMenuEntity2JSON(treeMenuEntitySet, jgenerator);
        jgenerator.writeEndArray();
        jgenerator.writeEndObject();
        jgenerator.close();
        return ToolBox.getOuputStreamContent(outStream, "UTF-8");
    }

    public final static TreeMenuEntity JSON2TreeMenuEntity(String payload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TreeMenuEntity entity = mapper.readValue(payload, TreeMenuEntity.class);
        return entity;
    }
}
