package br.ufpe.cin.reviewer.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAEntityManager {
	
	public static final EntityManager ENTITY_MANAGER;
	
	static {
		try {
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.ufpe.cin.reviewer");
			ENTITY_MANAGER = entityManagerFactory.createEntityManager();
		} catch (Exception e) {
			throw new RuntimeException("Error trying to create JPA entity manager", e);
		}
	}
	
}
