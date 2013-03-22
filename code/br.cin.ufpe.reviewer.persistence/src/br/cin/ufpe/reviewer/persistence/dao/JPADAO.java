package br.cin.ufpe.reviewer.persistence.dao;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Query;

import br.cin.ufpe.reviewer.persistence.JPAEntityManager;
import br.cin.ufpe.reviewer.persistence.PersistenceConstants;
import br.cin.ufpe.reviewer.persistence.exceptions.PersistenceException;

public class JPADAO<E,K> implements IDAO<E, K> {
	
	private static Properties queries;
	private Class<E> entityClass;
	
	static {
		try {
			queries = new Properties();
			queries.load(ClassLoader.getSystemResourceAsStream(PersistenceConstants.QUERIES_FILE_NAME));
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
	public Set<E> retrieveAll() throws PersistenceException {
		Set<E> entities = new HashSet<>();
		
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
			JPAEntityManager.ENTITY_MANAGER.remove(entity);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to delete an entity.",e);
		}
	}
	
	protected Query createQuery(String key, Map<String, String> queryParameters) {
		Query query = JPAEntityManager.ENTITY_MANAGER.createQuery(queries.getProperty(getClass() + "." + key));
		
		if (queryParameters != null) {
			for (String parameterName : queryParameters.keySet()) {
				query.setParameter(parameterName, queryParameters.get(parameterName));
			}
		}
		
		return query;
	}
	
}
