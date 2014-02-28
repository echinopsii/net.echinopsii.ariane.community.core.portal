/**
 * IDM JSF Commons
 * Group PrimeFaces Lazy Model
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

package com.spectral.cc.core.portal.idmwat.controller.group;

import com.spectral.cc.core.portal.idmwat.ccplugin.IDMJPAProviderConsumer;
import com.spectral.cc.core.idm.base.model.jpa.Group;
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

public class GroupLazyModel extends LazyDataModel<Group> {
    private static final Logger log = LoggerFactory.getLogger(GroupLazyModel.class);

    private int         rowCount ;
    private List<Group> pageItems ;

    private Predicate[] getSearchPredicates(EntityManager em, Root<Group> root, Map<String,String> filters) {
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

    private void paginate(int first, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        EntityManager em = IDMJPAProviderConsumer.getInstance().getIdmJpaProvider().createEM();
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Populate this.count
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Group> root = countCriteria.from(Group.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(em, root,filters));
        this.rowCount = (int) (long) em.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems
        CriteriaQuery<Group> criteria = builder.createQuery(Group.class);
        root = criteria.from(Group.class);
        criteria.select(root).where(getSearchPredicates(em, root,filters));
        if (sortOrder!=null && sortField!=null)
            criteria.orderBy(sortOrder.toString().equals("DESCENDING") ? builder.desc(root.get(sortField)) : builder.asc(root.get(sortField)));
        TypedQuery<Group> query = em.createQuery(criteria);
        query.setFirstResult(first).setMaxResults(getPageSize());
        log.debug("Query: {}", new Object[]{query.toString()});
        this.pageItems = query.getResultList();

        em.close();
    }

    @Override
    public Group getRowData(String rowKey) {
        for(Group group : pageItems) {
            if(group.getId().equals(rowKey))
                return group;
        }
        return null;
    }

    @Override
    public Object getRowKey(Group group) {
        return group.getId();
    }

    @Override
    public List<Group> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        this.setPageSize(pageSize);
        paginate(first,sortField,sortOrder,filters);
        this.setRowCount(rowCount);
        return pageItems;
    }
}