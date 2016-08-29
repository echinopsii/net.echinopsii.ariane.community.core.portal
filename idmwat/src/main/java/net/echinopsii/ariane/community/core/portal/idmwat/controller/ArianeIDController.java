/**
 * Portal IDM wat bundle
 * Ariane ID controller
 * Copyright (C) 2016 echinopsii
 *
 * Author : Mathilde Ffrench
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

package net.echinopsii.ariane.community.core.portal.idmwat.controller;

import net.echinopsii.ariane.community.core.portal.base.json.ArianeIDJSON;
import net.echinopsii.ariane.community.core.portal.base.model.ArianeDefinition;

public class ArianeIDController {
    private boolean isInitialized = false;
    private ArianeDefinition arianeDefinition;

    private String arianeIDDeliveryYear;
    private String arianeIDDeliveryDate;
    private String arianeIDVersion;

    public void init() {
        if (!isInitialized) {
            arianeDefinition = ArianeIDJSON.getArianeIDFromJSON();
            arianeIDDeliveryDate = arianeDefinition.getDeliveryDate();
            arianeIDDeliveryYear = arianeDefinition.getDeliveryYear();
            arianeIDVersion = arianeDefinition.getVersion();
            isInitialized = true;
        }
    }

    public String getArianeIDDeliveryYear() {
        init();
        return arianeIDDeliveryYear;
    }

    public String getArianeIDDeliveryDate() {
        init();
        return arianeIDDeliveryDate;
    }

    public String getArianeIDVersion() {
        init();
        return arianeIDVersion;
    }
}
