/**
 * Directory Commons JSF bundle
 * Directories View Utils
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

package com.spectral.cc.core.portal.idm.tools;

import com.spectral.cc.core.portal.idm.ccplugin.IDMJPAProviderConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for working with Java Server Faces views.
 */
public final class IDMViewUtils {
    private static final Logger log = LoggerFactory.getLogger(IDMViewUtils.class);

    public static <T> List<T> asList(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        return new ArrayList<T>(collection);
    }

    public static <T> List<T> asList2(Object object, String collectionName) throws InvocationTargetException, IllegalAccessException {
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

    public static String display(Object object) {

        if (object == null) {
            return null;
        }

        try {
            // Invoke toString if declared in the class. If not found, the NoSuchMethodException is caught and handled
            object.getClass().getDeclaredMethod("toString");
            return object.toString();
        } catch (NoSuchMethodException noMethodEx) {
            try {
                for (Field field : object.getClass().getDeclaredFields()) {
                    // Find the primary key field and display it
                    if (field.getAnnotation(Id.class) != null) {
                        // Find a matching getter and invoke it to display the key
                        for (Method method : object.getClass().getDeclaredMethods()) {
                            if (method.equals(new PropertyDescriptor(field.getName(), object.getClass()).getReadMethod())) {
                                return method.invoke(object).toString();
                            }
                        }
                    }
                }
                for (Method method : object.getClass().getDeclaredMethods()) {
                    // Find the primary key as a property instead of a field, and display it
                    if (method.getAnnotation(Id.class) != null) {
                        return method.invoke(object).toString();
                    }
                }
            }
            catch (Exception ex) {
                // Unlikely, but abort and stop view generation if any exception is thrown
                throw new RuntimeException(ex);
            }
        }

        return null;
    }

    private IDMViewUtils() {
        // Can never be called
    }
}
