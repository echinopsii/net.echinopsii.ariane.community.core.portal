/**
 * Portal Base
 * MainMenuEntityJSON JSON
 *
 * Copyright (C) 2015 mffrench
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
import net.echinopsii.ariane.community.core.portal.base.model.MainMenuEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainMenuEntityJSON {

    public final static String MAIN_MENU_ENTITY_ID = "id";
    public final static String MAIN_MENU_ENTITY_VALUE = "value";
    public final static String MAIN_MENU_ENTITY_CONTEXT_ADDRESS = "contextAddress";
    public final static String MAIN_MENU_ENTITY_TARGET  = "target";
    public final static String MAIN_MENU_ENTITY_ISEXTERNAL = "isExternal";
    public final static String MAIN_MENU_ENTITY_TYPE  = "type";
    public final static String MAIN_MENU_ENTITY_RANK = "rank";
    public final static String MAIN_MENU_ENTITY_PARENT = "parentID";
    public final static String MAIN_MENU_ENTITY_ICON = "icon";
    public final static String MAIN_MENU_ENTITY_ALISTENER = "actionListener";
    public final static String MAIN_MENU_ENTITY_DISPLAY_ROLES = "displayRoles";
    public final static String MAIN_MENU_ENTITY_DISPLAY_PERMISSIONS = "displayPermissions";

    public final static void mainMenuEntity2JSON(MainMenuEntity entity, JsonGenerator jgenerator) throws IOException {
        jgenerator.writeStartObject();
        jgenerator.writeStringField(MAIN_MENU_ENTITY_ID, entity.getId());
        jgenerator.writeStringField(MAIN_MENU_ENTITY_VALUE, entity.getValue());
        jgenerator.writeStringField(MAIN_MENU_ENTITY_CONTEXT_ADDRESS, entity.getContextAddress());
        jgenerator.writeStringField(MAIN_MENU_ENTITY_TARGET, entity.getTarget());
        jgenerator.writeBooleanField(MAIN_MENU_ENTITY_ISEXTERNAL, entity.isExternal());
        jgenerator.writeNumberField(MAIN_MENU_ENTITY_TYPE, entity.getType());
        jgenerator.writeNumberField(MAIN_MENU_ENTITY_RANK, entity.getRank());
        jgenerator.writeStringField(MAIN_MENU_ENTITY_PARENT, entity.getParentID());
        jgenerator.writeStringField(MAIN_MENU_ENTITY_ICON, entity.getIcon());
        jgenerator.writeStringField(MAIN_MENU_ENTITY_ALISTENER, entity.getActionListener());
        jgenerator.writeArrayFieldStart(MAIN_MENU_ENTITY_DISPLAY_ROLES);
        for (String role : entity.getDisplayRoles())
            jgenerator.writeString(role);
        jgenerator.writeEndArray();
        jgenerator.writeArrayFieldStart(MAIN_MENU_ENTITY_DISPLAY_PERMISSIONS);
        for (String permission : entity.getDisplayPermissions())
            jgenerator.writeString(permission);
        jgenerator.writeEndArray();
        jgenerator.writeEndObject();
    }

    public final static String mainMenuEntity2JSON(MainMenuEntity entity) throws IOException {
        JsonFactory jFactory = new JsonFactory();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        JsonGenerator jgenerator = jFactory.createGenerator(outStream, JsonEncoding.UTF8);
        mainMenuEntity2JSON(entity, jgenerator);
        return ToolBox.getOuputStreamContent(outStream, "UTF-8");
    }


}
