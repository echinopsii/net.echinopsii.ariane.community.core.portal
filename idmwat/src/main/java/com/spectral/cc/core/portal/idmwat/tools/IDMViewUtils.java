/**
 * Portal IDM wat bundle
 * IDM View Utils
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

package com.spectral.cc.core.portal.idmwat.tools;

import com.spectral.cc.core.portal.idmwat.plugin.IDMJPAProviderConsumer;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for working with Java Server Faces views. Taglib defined in ccidm.taglib.xml.
 */
public final class IDMViewUtils {
    private static final Logger log = LoggerFactory.getLogger(IDMViewUtils.class);

    public static <T> List<T> asList(Object object, String collectionName) throws InvocationTargetException, IllegalAccessException {
        if (collectionName == null && collectionName.equals(""))
            return null;

        String methodName = "get"+collectionName.substring(0, 1).toUpperCase() + collectionName.substring(1);
        Method getter = null;
        Method id = null;
        try {
            getter = object.getClass().getDeclaredMethod(methodName);
            id = object.getClass().getDeclaredMethod("getId");
        } catch (NoSuchMethodException e) {
            log.warn("No such method : {} or getId", methodName);
            return null;
        }

        EntityManager entityManager = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        object = entityManager.find(object.getClass(), id.invoke(object));
        ArrayList<T> ret = new ArrayList<T>((Collection<? extends T>) getter.invoke(object));
        entityManager.close();
        return ret;
    }

    public static boolean isAuthenticated() {
        log.debug("principal : {} ; authenticated : {} ", new Object[]{SecurityUtils.getSubject().getPrincipal(),SecurityUtils.getSubject().isAuthenticated()});
        return SecurityUtils.getSubject().isAuthenticated();
    }

    public static boolean hasPermission(String permission) {
        log.debug("principal : {} ; permission : {} - {}", new Object[]{SecurityUtils.getSubject().getPrincipal(), permission, SecurityUtils.getSubject().isPermitted(permission)});
        return SecurityUtils.getSubject().isPermitted(permission);
    }

    public static boolean hasRole(String role) {
        log.debug("principal : {} ; role : {} - {}", new Object[]{SecurityUtils.getSubject().getPrincipal(), role, SecurityUtils.getSubject().hasRole(role)});
        return SecurityUtils.getSubject().hasRole(role);
    }

    private IDMViewUtils() {
        // Can never be called
    }
}
