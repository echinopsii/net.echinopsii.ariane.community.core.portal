/**
 * Portal IDM wat bundle
 * Resource RUD Controller
 * Copyright (C) 2014 Mathilde Ffrench
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
package com.spectral.cc.core.portal.idmwat.controller.resource;

import com.spectral.cc.core.portal.idmwat.plugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.portal.idmwat.controller.permission.PermissionsListController;
import com.spectral.cc.core.idm.base.model.jpa.*;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provide stuff to display a resources list in a PrimeFaces data table, display resources, update a resource and remove resources
 */
public class ResourcesListController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ResourcesListController.class);

    private LazyDataModel<Resource> lazyModel = new ResourceLazyModel();
    private Resource[]              selectedResourceList ;

    private HashMap<Long,String>           addedPermission    = new HashMap<Long, String>();
    private HashMap<Long,List<Permission>> removedPermissions = new HashMap<Long, List<Permission>>();

    public LazyDataModel<Resource> getLazyModel() {
        return lazyModel;
    }

    public Resource[] getSelectedResourceList() {
        return selectedResourceList;
    }

    public void setSelectedResourceList(Resource[] selectedResourceList) {
        this.selectedResourceList = selectedResourceList;
    }

    public HashMap<Long, String> getAddedPermission() {
        return addedPermission;
    }

    public void setAddedPermission(HashMap<Long, String> addedPermission) {
        this.addedPermission = addedPermission;
    }

    /**
     * Synchronize added permission into a resource to database
     *
     * @param resource the resource the UI is working on
     * @throws NotSupportedException
     * @throws SystemException
     */
    public void syncAddedPermission(Resource resource) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            for (Permission permission : PermissionsListController.getAll()) {
                if (permission.getName().equals(this.addedPermission.get(resource.getId()))) {
                    resource = em.find(resource.getClass(), resource.getId());
                    permission = em.find(permission.getClass(), permission.getId());
                    Resource bindedPermissionResource = permission.getResource();
                    if (bindedPermissionResource!=null) {
                        bindedPermissionResource.getPermissions().remove(permission);
                        permission.setResource(null);
                        em.flush();
                    }
                    resource.getPermissions().add(permission);
                    permission.setResource(resource);
                    em.flush();
                    em.getTransaction().commit();
                    break;
                }
            }
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Resource updated successfully !",
                                                       "Resource name : " + resource.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating resource " + resource.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public HashMap<Long, List<Permission>> getRemovedPermissions() {
        return removedPermissions;
    }

    public void setRemovedPermissions(HashMap<Long, List<Permission>> removedPermissions) {
        this.removedPermissions = removedPermissions;
    }

    /**
     * Synchronize deleted permission from a resource to database
     *
     * @param resource the resource the UI is working on
     * @throws NotSupportedException
     * @throws SystemException
     */
    public void syncRemovedPermissions(Resource resource) throws NotSupportedException, SystemException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            List<Permission> permissions2beRM = this.removedPermissions.get(resource.getId());
            for (Permission permission2beRM : permissions2beRM) {
                permission2beRM = em.find(permission2beRM.getClass(), permission2beRM.getId());
                resource = em.find(resource.getClass(), resource.getId());
                resource.getPermissions().remove(permission2beRM);
                em.remove(permission2beRM);
            }
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Resource updated successfully !",
                                                       "Resource name : " + resource.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating resource " + resource.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * When a PrimeFaces data table row is toogled init reference into the addedPermission, removedPermissions lists with the correct resource id
     * When a PrimeFaces data table row is untoogled remove reference from the addedPermission, removedPermissions lists with the correct resource id
     *
     * @param event provided by the UI through PrimeFaces on a row toggle
     * @throws CloneNotSupportedException
     */
    public void onRowToggle(ToggleEvent event) throws CloneNotSupportedException {
        log.debug("Row Toogled : {}", new Object[]{event.getVisibility().toString()});
        Resource eventResource = ((Resource) event.getData());
        if (event.getVisibility().toString().equals("HIDDEN")) {
            addedPermission.remove(eventResource.getId());
            removedPermissions.remove(eventResource.getId());
        } else {
            addedPermission.put(eventResource.getId(), "");
            removedPermissions.put(eventResource.getId(), new ArrayList<Permission>());
        }
    }

    /**
     * When UI actions an update merge the corresponding resource bean with the correct resource instance in the DB and save this instance
     *
     * @param resource the resource the UI is working on
     * @throws SystemException
     * @throws NotSupportedException
     * @throws HeuristicRollbackException
     * @throws HeuristicMixedException
     * @throws RollbackException
     */
    public void update(Resource resource) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        try {
            em.getTransaction().begin();
            Resource resource2update = em.find(resource.getClass(), resource.getId()).setNameR(resource.getName()).setDescriptionR(resource.getDescription());
            //em.merge(resource2update);
            em.flush();
            em.getTransaction().commit();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Resource updated successfully !",
                                                       "Resource name : " + resource.getName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Throwable t) {
            log.debug("Throwable catched !");
            t.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Throwable raised while updating resource " + resource.getName() + " !",
                                                       "Throwable message : " + t.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * Remove selected resources
     */
    public void delete() {
        log.debug("Remove selected Resource !");
        for (Resource resource2BeRemoved: selectedResourceList) {
            EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
            try {
                em.getTransaction().begin();
                resource2BeRemoved = em.find(resource2BeRemoved.getClass(), resource2BeRemoved.getId());
                for (Permission permission : resource2BeRemoved.getPermissions()) {
                    resource2BeRemoved.getPermissions().remove(permission);
                    em.remove(permission);
                }
                em.remove(resource2BeRemoved);
                em.flush();
                em.getTransaction().commit();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                           "Resource deleted successfully !",
                                                           "Resource name : " + resource2BeRemoved.getName());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Throwable t) {
                log.debug("Throwable catched !");
                t.printStackTrace();
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                           "Throwable raised while creating resource " + resource2BeRemoved.getName() + " !",
                                                           "Throwable message : " + t.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            } finally {
                em.close();
            }
        }
        selectedResourceList=null;
    }

    /**
     * Get all resources from the db
     *
     * @return all resources from the db
     * @throws SystemException
     * @throws NotSupportedException
     */
    public static List<Resource> getAll() throws SystemException, NotSupportedException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        log.debug("Get all resources from : \n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}",
                         new Object[]{
                                             (Thread.currentThread().getStackTrace().length>0) ? Thread.currentThread().getStackTrace()[0].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>1) ? Thread.currentThread().getStackTrace()[1].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>2) ? Thread.currentThread().getStackTrace()[2].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>3) ? Thread.currentThread().getStackTrace()[3].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>4) ? Thread.currentThread().getStackTrace()[4].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>5) ? Thread.currentThread().getStackTrace()[5].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>6) ? Thread.currentThread().getStackTrace()[6].getClassName() : ""
                         });
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Resource> criteria = builder.createQuery(Resource.class);
        Root<Resource> root = criteria.from(Resource.class);
        criteria.select(root).orderBy(builder.asc(root.get("name")));

        List<Resource> ret = em.createQuery(criteria).getResultList();
        em.close();
        return ret ;
    }

    /**
     * Get all resources from the db + "Select resource" for UI selector
     *
     * @return all resources from the db
     * @throws SystemException
     * @throws NotSupportedException
     */
    public static List<Resource> getAllForSelector() throws SystemException, NotSupportedException {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        log.debug("Get all resources from : \n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}\n\t{}",
                         new Object[]{
                                             (Thread.currentThread().getStackTrace().length>0) ? Thread.currentThread().getStackTrace()[0].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>1) ? Thread.currentThread().getStackTrace()[1].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>2) ? Thread.currentThread().getStackTrace()[2].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>3) ? Thread.currentThread().getStackTrace()[3].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>4) ? Thread.currentThread().getStackTrace()[4].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>5) ? Thread.currentThread().getStackTrace()[5].getClassName() : "",
                                             (Thread.currentThread().getStackTrace().length>6) ? Thread.currentThread().getStackTrace()[6].getClassName() : ""
                         });
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Resource> criteria = builder.createQuery(Resource.class);
        Root<Resource> root = criteria.from(Resource.class);
        criteria.select(root).orderBy(builder.asc(root.get("name")));

        List<Resource> ret = em.createQuery(criteria).getResultList();
        ret.add(0, new Resource().setNameR("Select resource"));
        em.close();
        return ret ;
    }
}