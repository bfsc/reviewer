package br.ufpe.cin.reviewer.persistence.dao;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.ufpe.cin.reviewer.persistence.JPAEntityManager;
import br.ufpe.cin.reviewer.persistence.PersistenceConstants;
import br.ufpe.cin.reviewer.persistence.exceptions.PersistenceException;

public class JPADAO<E,K> implements IDAO<E, K> {
	
	private static Properties queries;
	private Class<E> entityClass;
	
	static {
		try {
			queries = new Properties();
			queries.load(JPADAO.class.getClassLoader().getResourceAsStream(PersistenceConstants.QUERIES_FILE_NAME));
		} catch (IOException e) {
			throw new RuntimeException("Error trying to load queries file.", e);
		}
	}
		
	public JPADAO(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void create(E entity) throws PersistenceException {
		try {
			JPAEntityManager.ENTITY_MANAGER.persist(entity);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to create an entity." ,e);
		}
	}

	public E retrieve(K key) throws PersistenceException {
		E entity = null;
		
		try {
			JPAEntityManager.ENTITY_MANAGER.find(entityClass, key);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to retrieve an entity with key: " + key ,e);
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<E> retrieveAll() throws PersistenceException {
		List<E> entities = new LinkedList<E>();
		
		try {
			Query query = createQuery("1", null);
			
			List<Object> resultList = query.getResultList();
			
			for (Object object : resultList) {
				entities.add((E) object);
			}
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to retrieve all entities",e);
		}
		
		return entities;
	}
	
	public void update(E entity) throws PersistenceException {
		try {
			JPAEntityManager.ENTITY_MANAGER.merge(entity);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to update an entity.",e);
		}
	}

	public void delete(E entity) throws PersistenceException {
		try {
			EntityManager em  = JPAEntityManager.ENTITY_MANAGER;
			em.remove(em.contains(entity) ? entity : em.merge(entity));
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to delete an entity.",e);
		}
	}
	
	protected Query createQuery(String key, Map<String, String> queryParameters) throws PersistenceException {
		String queryStringKey = getClass().getName() + "." + key;
		String queryString = queries.getProperty(queryStringKey);
		
		if (queryString == null) {
			throw new PersistenceException("Non existent query key: " + queryStringKey);
		}
		
		Query query = JPAEntityManager.ENTITY_MANAGER.createQuery(queryString);
		
		if (queryParameters != null) {
			for (String parameterName : queryParameters.keySet()) {
				query.setParameter(parameterName, queryParameters.get(parameterName));
			}
		}
		
		return query;
	}
	
}
