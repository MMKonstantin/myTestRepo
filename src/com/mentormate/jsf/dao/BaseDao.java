package com.mentormate.jsf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.mentormate.jsf.database.JPAContext;

/**
 * Base DAO Provides methods for CRUD operations
 */

public abstract class BaseDao<T> extends JPAContext {

	/**
	 * Saves a new entity in the DB
	 *
	 * @param entity
	 */
	public void save(T entity) {
		getEntityManager().persist(entity);
		getEntityManager().flush();
	}

	/**
	 * Updates an already persisted entity
	 *
	 * @param entity
	 */
	public void merge(T entity) {
		getEntityManager().merge(entity);
		getEntityManager().persist(entity);
		getEntityManager().flush();
	}

	/**
	 * Saves new entities in the DB
	 *
	 * @param list
	 *            of entities
	 */
	public void save(List<T> entities) {
		for (T baseEntity : entities) {
			getEntityManager().persist(baseEntity);
		}

		getEntityManager().flush();
	}

	/**
	 * Updates an entity
	 *
	 * @param entity
	 */
	public void update(T entity) {
		getEntityManager().merge(entity);
		getEntityManager().flush();
	}

	/**
	 * Updates an entity
	 *
	 * @param list
	 *            of entities
	 */
	public void update(List<T> entities) {
		for (T baseEntity : entities) {
			getEntityManager().persist(baseEntity);
		}
		getEntityManager().flush();
	}

	/**
	 * Updates an entity
	 *
	 * @param entity
	 */
	public T updateEntity(T entity) {
		entity = getEntityManager().merge(entity);
		getEntityManager().flush();
		return entity;
	}

	/**
	 * Deletes an entity
	 *
	 * @param entity
	 */
	public void delete(T entity) {
		if (!getEntityManager().contains(entity)) {
			entity = getEntityManager().merge(entity);
		}
		getEntityManager().remove(entity);
		getEntityManager().flush();
	}

	/**
	 * Deletes an entity
	 *
	 * @param list
	 *            of entities
	 */
	public void delete(List<T> entities) {
		for (T baseEntity : entities) {
			if (!getEntityManager().contains(baseEntity)) {
				baseEntity = getEntityManager().merge(baseEntity);
			}
			getEntityManager().remove(baseEntity);
		}
		getEntityManager().flush();
	}

	/**
	 * Get en entity by id
	 *
	 * @param id
	 * @return - entity
	 */
	public T getById(int id, Class<T> clazz) {
		return getEntityManager().find(clazz, id);
	}

	/**
	 * @see Dao#getById(String, Class)
	 */
	public T getById(String id, Class<T> clazz) {
		return getEntityManager().find(clazz, id);
	}

	/**
	 * Get all entities of a specified type
	 *
	 * @return - list of entities
	 */
	public List<T> getAll(Class<T> clazz) {
		CriteriaQuery<T> criteria = getEntityManager().getCriteriaBuilder().createQuery(clazz);
		criteria.from(clazz);

		return getEntityManager().createQuery(criteria).getResultList();
	}

	/**
	 * Get all entities of a specified type sorted
	 *
	 * @return - list of entities
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> getAllSorted(Class<T> clazz, String sortField, boolean descend) {

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = builder.createQuery();
		Root<T> from = cq.from(clazz);
		cq.select(from);

		if (sortField != null && !sortField.isEmpty()) {
			setOrderBy(cq, builder, from, sortField, descend);
		}
		Query q = getEntityManager().createQuery(cq);
		return q.getResultList();
	}

	/**
	 * Find objects by criteria
	 *
	 * @param query
	 *            query name
	 * @param parameters
	 *            query parameters
	 * @return - list of entities
	 */
	public List<T> findByCriteria(String name, Object[] parameters) {
		return findByCriteria(name, parameters, FlushModeType.AUTO);
	}

	/**
	 * Find objects by criteria
	 *
	 * @param name
	 * @param parameters
	 * @param flushMode
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(String name, Object[] parameters, FlushModeType flushMode) {
		Query criteria = getEntityManager().createNamedQuery(name);
		criteria.setFlushMode(flushMode);

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				criteria.setParameter(i + 1, parameters[i]);
			}
		}

		return criteria.getResultList();
	}

	/**
	 * Find objects by columns
	 *
	 * @param clazz
	 * @param columnName
	 * @param columnValue
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Class<T> clazz, String columnName, Object columnValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		cq.select(from);
		cq.where(builder.equal(from.<String>get(columnName), columnValue));

		Query q = getEntityManager().createQuery(cq);
		return q.getResultList();
	}

	/**
	 * Find objects by columns
	 *
	 * @param clazz
	 * @param Map<columnName,
	 *            columnValue>
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Class<T> clazz, Map<String, Object> params) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		cq.select(from);
		if (params != null && !params.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				predicates.add(builder.equal(from.<String>get(entry.getKey()), entry.getValue()));
			}
			cq.where(predicates.toArray(new Predicate[predicates.size()]));
		}
		Query q = getEntityManager().createQuery(cq);
		return q.getResultList();
	}

	/**
	 * Find objects by columns
	 *
	 * @param clazz
	 * @param columnName
	 * @param columnValue
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriteriaSorted(Class<T> clazz, String columnName, Object columnValue, String sortField,
			boolean descend) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		cq.select(from);
		cq.where(builder.equal(from.<String>get(columnName), columnValue));

		if (sortField != null && !sortField.isEmpty()) {
			setOrderBy(cq, builder, from, sortField, descend);
		}

		Query q = getEntityManager().createQuery(cq);
		return q.getResultList();
	}

	/**
	 * Get an entity manager
	 *
	 * @return - entity manager
	 */
	@Override
	public EntityManager getEntityManager() {
		return super.getEntityManager();
	}

	/**
	 * Finds a single object by criteria
	 *
	 * @param name
	 * @param parameters
	 * @return a persisted object
	 */
	public Object findSingleResultByCriteria(String name, Object[] parameters) {
		return findSingleResultByCriteria(name, parameters, FlushModeType.AUTO);
	}

	/**
	 * Finds a single object by criteria
	 *
	 * @param name
	 * @param parameters
	 * @param flushMode
	 * @return
	 */
	public Object findSingleResultByCriteria(String name, Object[] parameters, FlushModeType flushMode) {
		try {
			Query criteria = getEntityManager().createNamedQuery(name);
			criteria.setFlushMode(flushMode);

			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					criteria.setParameter(i + 1, parameters[i]);
				}
			}

			return criteria.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/*
	 * Find object by columns
	 *
	 * @param clazz
	 * 
	 * @param columnName
	 * 
	 * @param columnValue
	 * 
	 * @return list of entities
	 */
	public Object findSingleResultByCriteria(Class<T> clazz, String columnName, Object columnValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		cq.select(from);
		cq.where(builder.equal(from.<String>get(columnName), columnValue));

		Query q = getEntityManager().createQuery(cq);
		return q.getSingleResult();
	}

	/**
	 * Saves and refreshes after the save an object in the DB
	 *
	 * @param entity
	 */
	public void saveAndRefresh(T entity) {
		save(entity);
		getEntityManager().refresh(entity);
	}

	/**
	 * Updates an object in the DB and refreshes the reference to it
	 * updateAndRefresh method throws "Can not refresh not managed object"
	 *
	 * @param entity
	 */
	public void updateAndRefresh(T entity) {
		entity = updateEntity(entity);
		getEntityManager().refresh(entity);
	}

	/**
	 * Finds the first of many persisted objects by criteria
	 *
	 * @param name
	 * @param parameters
	 * @param clazz
	 * @return a persisted object
	 */
	@SuppressWarnings("unchecked")
	public T findFirstResultByCriteria(String name, Object[] parameters, Class<T> clazz) {
		Query criteria = getEntityManager().createNamedQuery(name);
		criteria.setFirstResult(0);
		criteria.setMaxResults(1);

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				criteria.setParameter(i + 1, parameters[i]);
			}
		}

		List<T> result = criteria.getResultList();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Function used to add order by to criteria query
	 *
	 * @param cq
	 *            - criteria query object
	 * @param builder
	 *            - criteria query builder
	 * @param from
	 *            - root object
	 * @param sortField
	 *            - field used to sort
	 * @param descend
	 *            - flag indicates sort direction
	 */
	protected void setOrderBy(CriteriaQuery<T> cq, CriteriaBuilder builder, Root<T> from, String sortField,
			boolean descend) {
		if (sortField != null) {
			sortField = sortField.replace("wrappedObject.", "");
			if (sortField.indexOf('.') != -1 || sortField.indexOf('[') != -1)
				return;

			cq.orderBy(descend ? builder.desc(from.get(sortField)) : builder.asc(from.get(sortField)));
		}
	}

	/**
	 * Function retrieves list of entities from database. It uses sorting,
	 * filtering and paging to do this.
	 *
	 * @param first
	 *            - first row number
	 * @param pageSize
	 *            - page size
	 * @param sortField
	 *            - field to sort by
	 * @param descend
	 *            - flag indicates sort direction
	 * @param filters
	 *            - map of filters
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findFilteredRange(int first, int pageSize, String sortField, boolean descend,
			Map<String, String> filters, Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		cq.select(from);

		if (filters != null && !filters.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				predicates.add(builder.like(from.<String>get(entry.getKey()), "%" + entry.getValue() + "%"));
			}
			cq.where(predicates.toArray(new Predicate[predicates.size()]));
		}

		if (sortField != null && !sortField.isEmpty())
			setOrderBy(cq, builder, from, sortField, descend);

		Query q = getEntityManager().createQuery(cq);
		if (pageSize >= 0) {
			q.setMaxResults(pageSize);
		}
		q.setFirstResult(first);
		return q.getResultList();
	}

	/**
	 * Function retrieves list of entities from database. It uses sorting,
	 * filtering and paging to do this.
	 *
	 * @param first
	 *            - first row number
	 * @param pageSize
	 *            - page size
	 * @param sortField
	 *            - field to sort by
	 * @param descend
	 *            - flag indicates sort direction
	 * @param searchColumns
	 *            - list of columns to search in
	 * @param search
	 *            - needle to search for
	 * @return list of entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findFilteredRange(int first, int pageSize, String sortField, boolean descend,
			List<String> searchColumns, String search, Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(clazz);
		Root<T> from = cq.from(clazz);
		cq.select(from);

		if (searchColumns != null && !searchColumns.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (String column : searchColumns) {
				predicates.add(builder.like(from.<String>get(column), "%" + search + "%"));
			}
			cq.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));
		}

		if (sortField != null && !sortField.isEmpty()) {
			setOrderBy(cq, builder, from, sortField, descend);
		}

		Query q = getEntityManager().createQuery(cq);
		if (pageSize >= 0) {
			q.setMaxResults(pageSize);
		}
		q.setFirstResult(first);
		return q.getResultList();
	}

	/**
	 * Finds entities. Result set was pre-filtered using map of filters
	 *
	 * @param filters
	 *            - map of filters
	 * @return list of entities
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> getFiltered(Map<String, String> filters, Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = builder.createQuery();
		Root<T> from = cq.from(clazz);
		cq.select(from);

		if (filters != null && !filters.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				predicates.add(builder.like(from.<String>get(entry.getKey()), "%" + entry.getValue() + "%"));
			}
			cq.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));
		}

		Query q = getEntityManager().createQuery(cq);
		return q.getResultList();
	}

	/**
	 * Finds entities. Result set was pre-filtered using map of filters
	 *
	 * @param filters
	 *            - map of filters
	 * @return list of entities
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> getFilteredSorted(Map<String, String> filters, Class<T> clazz, String sortField, boolean descend) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = builder.createQuery();
		Root<T> from = cq.from(clazz);
		cq.select(from);

		if (filters != null && !filters.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				predicates.add(builder.like(from.<String>get(entry.getKey()), "%" + entry.getValue() + "%"));
			}
			cq.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));
		}

		if (sortField != null && !sortField.isEmpty()) {
			setOrderBy(cq, builder, from, sortField, descend);
		}

		Query q = getEntityManager().createQuery(cq);
		return q.getResultList();
	}

	/**
	 * Function retrieves count of rows for entities. Result set was
	 * pre-filtered using map of filters
	 *
	 * @param filters
	 *            - map of filters
	 * @return count rows
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int countFiltered(Map<String, String> filters, Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = builder.createQuery();
		Root<T> from = cq.from(clazz);
		cq.select(builder.count(from));

		if (filters != null && !filters.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				predicates.add(builder.like(from.<String>get(entry.getKey()), "%" + entry.getValue() + "%"));
			}
			cq.where(predicates.toArray(new Predicate[predicates.size()]));
		}

		Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	/**
	 * Function retrieves count of rows for entities. Result set was
	 * pre-filtered using map of filters
	 *
	 * @param searchColumns
	 *            - list of columns to search in
	 * @param search
	 *            - needle to search for
	 * @return count rows
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int countFiltered(List<String> searchColumns, String search, Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = builder.createQuery();
		Root<T> from = cq.from(clazz);
		cq.select(builder.count(from));

		if (searchColumns != null && !searchColumns.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (String column : searchColumns) {
				predicates.add(builder.like(from.<String>get(column), "%" + search + "%"));
			}
			cq.where(builder.or(predicates.toArray(new Predicate[predicates.size()])));
		}

		Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getCountAll(Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery cq = builder.createQuery();
		Root<T> from = cq.from(clazz);
		cq.select(builder.count(from));

		Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}
}