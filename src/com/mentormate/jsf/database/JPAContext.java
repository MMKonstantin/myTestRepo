package com.mentormate.jsf.database;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * A context class to holds all JPA data sources access and some utility
 * methods. This class can be inherited or instantiated to give you reference to
 * all data entity managers.
 */
public class JPAContext {

	@PersistenceContext(unitName = "sqlServerDS")
	private EntityManager entityManager;

	/**
	 * Remove the given entity from the persistence context, causing a managed
	 * entity to become detached. Unflushed changes made to the entity if any
	 * (including removal of the entity), will not be synchronized to the
	 * database. Entities which previously referenced the detached entity will
	 * continue to reference it.
	 * 
	 * @param entity
	 *            the entity to be detached
	 */
	public void detach(Object entity) {
		getEntityManager().detach(entity);
	}

	/**
	 * Clears the provided entity manager cache for all instances of particular
	 * entity type.
	 *
	 * @param manager
	 *            the entity manager
	 * @param type
	 *            the entity type
	 */
	public void evict(Class<?> type) {
		getEntityManager().getEntityManagerFactory().getCache().evict(type);
	}

	/**
	 * Clears the provided entity manager cache for a particular instance of
	 * entity type specified by its primary key.
	 *
	 * @param manager
	 *            the entity manager
	 * @param type
	 *            the entity type
	 * @param key
	 *            the entity key
	 */
	public void evict(Class<?> type, Object key) {
		getEntityManager().getEntityManagerFactory().getCache().evict(type, key);
	}

	/**
	 * Clears the provided entity manager entire cache.
	 *
	 * @param manager
	 *            the entity manager
	 */
	public void evictAll() {
		getEntityManager().getEntityManagerFactory().getCache().evictAll();
	}

	/**
	 * Gets current entity manager specified by applied qualifier annotation.
	 * 
	 * @return
	 */
	public EntityManager getEntityManager(Class<?> type) {

		return entityManager;
	}

	/**
	 * Gets current entity manager specified by applied qualifier annotation.
	 * 
	 * @return
	 */
	public EntityManager getEntityManager() {
		Class<?> type = this.getClass();
		return getEntityManager(type);
	}

	/**
	 * Gets a Logger for current class.
	 * 
	 * @return
	 */
	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getName());
	}

	/**
	 * Helper method to log any exceptions using the instance of Logger per
	 * particular class.
	 * 
	 * @param ex
	 */
	public void log(Exception ex) {
		Logger.getLogger(this.getClass().getName()).warning(ex.toString());
	}

	// Fetch
	// --------------------------------------------------------------------------------------------

	/**
	 * Gets the first result of the query, if query is not empty, otherwise the
	 * default value provided.
	 * 
	 * @param query
	 *            the query
	 * @param defaultValue
	 *            the default value for an empty result
	 * @return first result of the query, if it's not empty, otherwise the
	 *         default value
	 */
	@SuppressWarnings("unchecked")
	public <T> T firstOrDefault(Query query, T defaultValue) {
		List<T> result = query.setMaxResults(1).getResultList();
		return !result.isEmpty() ? result.get(0) : defaultValue;
	}

	/**
	 *
	 * @param sql
	 * @param defaultValue
	 * @param prepare
	 * @return
	 */
	public <T> T firstOrDefault(String sql, T defaultValue, Consumer<Query> prepare) {

		Query query = getEntityManager().createQuery(sql);
		prepare.accept(query);
		return firstOrDefault(query, defaultValue);
	}

	/**
	 * Gets the first result of the query, if query is not empty, otherwise
	 * returns NULL
	 * 
	 * @param query
	 *            the query
	 * @return the first result of the query, if query is not empty, otherwise
	 *         returns NULL
	 */
	public <T> T firstOrNull(Query query) {
		return firstOrDefault(query, null);
	}

	/**
	 *
	 * @param sql
	 * @param prepare
	 * @return
	 */
	public <T> T firstOrNull(String sql, Consumer<Query> prepare) {

		Query query = getEntityManager().createQuery(sql);
		prepare.accept(query);
		return firstOrDefault(query, null);
	}
}