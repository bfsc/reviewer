package br.cin.ufpe.reviewer.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cin.ufpe.reviewer.persistence.exceptions.PersistenceException;

public class JPADAO<E,K> implements IDAO<E, K> {
	
	private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("br.cin.ufpe.reviewer");
	
	private EntityManager entityManager;
	private Class<E> entityClass;
		
	public JPADAO(Class<E> entityClass) {
		this.entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
		this.entityClass = entityClass;
	}
	
	public JPADAO(EntityManager entityManager, Class<E> entityClass) {
		this.entityManager = entityManager;
		this.entityClass = entityClass;
	}

	public void create(E entity) throws PersistenceException {
		try {
			this.entityManager.persist(entity);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to create an entity." ,e);
		}
	}

	public E retrieve(K key) throws PersistenceException {
		E entity = null;
		
		try {
			this.entityManager.find(entityClass, key);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to retriece an entity with key: " + key ,e);
		}
		
		return entity;
	}
	
	public void update(E entity) throws PersistenceException {
		try {
			this.entityManager.merge(entity);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to update an entity.",e);
		}
	}

	public void delete(E entity) throws PersistenceException {
		try {
			this.entityManager.remove(entity);
		} catch (Exception e) {
			throw new PersistenceException("An error occurred trying to delete an entity.",e);
		}
	}
	
}
