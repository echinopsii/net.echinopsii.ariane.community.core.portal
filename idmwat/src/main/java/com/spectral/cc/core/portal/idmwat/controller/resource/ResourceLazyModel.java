/**
 * Portal IDM wat bundle
 * Resource PrimeFaces Lazy Model
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

package com.spectral.cc.core.portal.idmwat.controller.resource;

import com.spectral.cc.core.portal.idmwat.plugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.idm.base.model.jpa.Resource;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provide lazy loading stuff for our Resource PrimeFaces datatable implementation
 */
public class ResourceLazyModel extends LazyDataModel<Resource> {
    private static final Logger log = LoggerFactory.getLogger(ResourceLazyModel.class);

    private int            rowCount  ;
    private List<Resource> pageItems ;

    /**
     * Add search predicate to the JPA query
     *
     * @param em the current JPA entity manager in use
     * @param root the current JPA root of the query
     * @param filters the provided filters
     * @return the generated JPA predicate
     */
    private Predicate[] getSearchPredicates(EntityManager em, Root<Resource> root, Map<String,String> filters) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = filters.get(filterProperty);
            log.debug("Filter : { {}, {} }", new Object[]{filterProperty, filterValue});
            predicatesList.add(builder.like(root.<String> get(filterProperty), '%' + filterValue + '%'));
        }

        Predicate[] ret = predicatesList.toArray(new Predicate[predicatesList.size()]);
        log.debug("Return predicates list: {}", new Object[]{ret.toString()});
        return ret;
    }

    /**
     * Generate a JPA query and push the result into pageItems
     *
     * @param first first result of the query (the group id)
     * @param sortField the sort field of the query
     * @param sortOrder the sort order of the query
     * @param filters the provided filters
     */
    private void paginate(int first, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        EntityManager entityManager = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        entityManager.getTransaction().begin();
        CriteriaBuilder builder = entityManager.getEntityManagerFactory().getCriteriaBuilder();

        // Populate this.count from DB
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Resource> root = countCriteria.from(Resource.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(entityManager, root,filters));
        TypedQuery<Long> queryC = entityManager.createQuery(countCriteria);
        this.rowCount = (int) (long) queryC.getSingleResult();
        log.debug("row count: {}", this.rowCount);

        // Populate tempPageItems from DB
        CriteriaQuery<Resource> criteria = builder.createQuery(Resource.class);
        root = criteria.from(Resource.class);
        criteria.select(root).where(getSearchPredicates(entityManager, root,filters));
        if (sortOrder!=null && sortField!=null)
            criteria.orderBy(sortOrder.toString().equals("DESCENDING") ? builder.desc(root.get(sortField)) : builder.asc(root.get(sortField)));
        TypedQuery<Resource> query = entityManager.createQuery(criteria);
        query.setFirstResult(first).setMaxResults(getPageSize());
        query.setHint("org.hibernate.cacheable", true);
        this.pageItems = query.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * Return the resource assigned to a table row
     *
     * @param rowKey the row resource id
     * @return resource object according to provided resource id
     */
    @Override
    public Resource getRowData(String rowKey) {
        for(Resource resource : pageItems) {
            if(resource.getId().equals(rowKey))
                return resource;
        }
        return null;
    }

    /**
     * Return the resource id assigned to a table row
     *
     * @param resource the row resource
     * @return the resource id
     */
    @Override
    public Object getRowKey(Resource resource) {
        return resource.getId();
    }

    /**
     * Return the resources list for the PrimeFaces table
     *
     * @param first first result of the query (the group id)
     * @param pageSize the page size
     * @param sortField the sort field of the query
     * @param sortOrder the sort order of the query
     * @param filters the provided filters
     *
     * @return queried resources list
     */
    @Override
    public List<Resource> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        this.setPageSize(pageSize);
        paginate(first,sortField,sortOrder,filters);
        this.setRowCount(rowCount);
        return pageItems;
    }
}