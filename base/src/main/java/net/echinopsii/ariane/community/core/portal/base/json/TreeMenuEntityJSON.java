/**
 * Copyright (C) 08/05/15 echinopsii
 *
 * Portal Base
 * TreeMenuEntity JSON
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
import com.fasterxml.jackson.core.JsonParseException;
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
        for (String permission : entity.getDisplayPermissions())
            jgenerator.writeString(permission);
        jgenerator.writeEndArray();
        jgenerator.writeEndObject();
    }

    public final static String treeMenuEntity2JSON(TreeMenuEntity entity) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        JsonGenerator jgenerator = jFactory.createGenerator(outStream, JsonEncoding.UTF8);
        treeMenuEntity2JSON(entity, jgenerator);
        return ToolBox.getOuputStreamContent(outStream, "UTF-8");
    }

    public final static String manyTreeMenuEntity2JSON(Set<TreeMenuEntity> treeMenuEntitySet) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        JsonGenerator jgenerator = jFactory.createGenerator(outStream, JsonEncoding.UTF8);
        jgenerator.writeStartObject();
        jgenerator.writeArrayFieldStart("treeMenuEntities");
        for (TreeMenuEntity treeMenuEntity : treeMenuEntitySet)
            treeMenuEntity2JSON(treeMenuEntity, jgenerator);
        jgenerator.writeEndArray();
        jgenerator.writeEndObject();
        return ToolBox.getOuputStreamContent(outStream, "UTF-8");
    }

    public final static TreeMenuEntity JSON2TreeMenuEntity(String payload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TreeMenuEntity entity = mapper.readValue(payload, TreeMenuEntity.class);
        return entity;
    }
}
