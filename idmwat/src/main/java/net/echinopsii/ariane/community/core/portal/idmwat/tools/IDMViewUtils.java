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

package net.echinopsii.ariane.community.core.portal.idmwat.tools;

import net.echinopsii.ariane.community.core.idm.base.model.jpa.User;
import net.echinopsii.ariane.community.core.idm.base.model.jpa.UserPreference;
import net.echinopsii.ariane.community.core.portal.base.model.TreeMenuEntity;
import net.echinopsii.ariane.community.core.portal.idmwat.plugin.IDMJPAProviderConsumer;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for working with Java Server Faces views. Taglib defined in arianeidm.taglib.xml.
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
        boolean ret = SecurityUtils.getSubject().isAuthenticated();
        if (ret) {
            EntityManager entityManager = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> userCriteria = builder.createQuery(User.class);
            Root<User> userRoot = userCriteria.from(User.class);
            userCriteria.select(userRoot).where(builder.equal(userRoot.<String>get("userName"), SecurityUtils.getSubject().getPrincipal()));
            TypedQuery<User> userQuery = entityManager.createQuery(userCriteria);

            User user = userQuery.getSingleResult();
            if (user != null) {
                String theCurrentPage = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() +
                        FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
                entityManager.getTransaction().begin();
                UserPreference currentPage = null;
                for (UserPreference userPreference : user.getPreferences()) {
                    if (userPreference.getPkey().equals("cccurrentpage")) {
                        currentPage = userPreference;
                        userPreference.setPvalue(theCurrentPage);
                        break;
                    }
                }
                if (currentPage == null) {
                    currentPage = new UserPreference().setUserR(user).setPkeyR("cccurrentpage").setPvalueR(theCurrentPage);
                    entityManager.persist(currentPage);
                    user.getPreferences().add(currentPage);
                }
                entityManager.flush();
                entityManager.getTransaction().commit();
            }
            entityManager.close();
        }
        return ret;
    }

    public static String getPageAfterLogin() {
        String ret = "/ariane/views/login.jsf";
        if (SecurityUtils.getSubject()!=null) {
            EntityManager entityManager = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> userCriteria = builder.createQuery(User.class);
            Root<User> userRoot = userCriteria.from(User.class);
            userCriteria.select(userRoot).where(builder.equal(userRoot.<String>get("userName"), SecurityUtils.getSubject().getPrincipal()));
            TypedQuery<User> userQuery = entityManager.createQuery(userCriteria);

            User user = userQuery.getSingleResult();
            if (user != null) {
                ret = null;
                for (UserPreference userPreference : user.getPreferences()) {
                    if (userPreference.getPkey().equals("ccstartuppage")) {
                        ret = userPreference.getPvalue();
                        break;
                    }
                }
                if (ret == null || ret.equals(""))
                    for (UserPreference userPreference : user.getPreferences()) {
                        if (userPreference.getPkey().equals("cccurrentpage")) {
                            ret = userPreference.getPvalue();
                            break;
                        }
                    }
                if (ret == null || ret.equals(""))
                    ret = "/ariane/views/home/userProfile.jsf";
            }
        }
        return ret;
    }

    public static boolean hasPermission(String permission) {
        log.debug("principal : {} ; permission : {} - {}", new Object[]{SecurityUtils.getSubject().getPrincipal(), permission, SecurityUtils.getSubject().isPermitted(permission)});
        return SecurityUtils.getSubject().isPermitted(permission);
    }

    public static boolean hasRole(String role) {
        log.debug("principal : {} ; role : {} - {}", new Object[]{SecurityUtils.getSubject().getPrincipal(), role, SecurityUtils.getSubject().hasRole(role)});
        return SecurityUtils.getSubject().hasRole(role);
    }

    public static boolean canDisplayTreeMenuEntity(TreeMenuEntity entity) {
        for (String displayRole : entity.getDisplayRoles())
            if (SecurityUtils.getSubject().hasRole(displayRole)) return true;
        for (String displayPermission : entity.getDisplayPermissions())
            if (SecurityUtils.getSubject().isPermitted(displayPermission)) return true;
        return false;
    }

    public static boolean canActionOnTreeMenuEntity(TreeMenuEntity entity, String action) {
        List<String> actionRoles = entity.getOtherActionsRoles().get(action);
        List<String> actionPerms = entity.getOtherActionsPerms().get(action);
        if (actionRoles!=null)
            for (String displayRole : actionRoles)
                if (SecurityUtils.getSubject().hasRole(displayRole)) return true;
        if (actionPerms!=null)
            for (String displayPermission : actionPerms)
                if (SecurityUtils.getSubject().isPermitted(displayPermission)) return true;
        return false;
    }

    private IDMViewUtils() {
        // Can never be called
    }
}
