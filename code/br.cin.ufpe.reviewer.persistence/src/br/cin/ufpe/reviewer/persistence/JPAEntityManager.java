package br.cin.ufpe.reviewer.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAEntityManager {
	
	public static final EntityManager ENTITY_MANAGER;
	
	static {
		try {
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.cin.ufpe.reviewer");
			ENTITY_MANAGER = entityManagerFactory.createEntityManager();
		} catch (Exception e) {
			throw new RuntimeException("Error trying to create JPA entity manager", e);
		}
	}

}