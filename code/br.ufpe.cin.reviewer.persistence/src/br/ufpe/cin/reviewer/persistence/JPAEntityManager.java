package br.ufpe.cin.reviewer.persistence;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;

public class JPAEntityManager {
	
	public static final EntityManager ENTITY_MANAGER;
	
	static {
		try {
			HashMap<String, Object> props = new HashMap<String, Object>();
			props.put(PersistenceUnitProperties.CLASSLOADER, JPAEntityManager.class.getClassLoader());
			EntityManagerFactory entityManagerFactory = new PersistenceProvider().createEntityManagerFactory("br.ufpe.cin.reviewer",props);
			ENTITY_MANAGER = entityManagerFactory.createEntityManager(props);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to create JPA entity manager", e);
		}
	}
	
}
