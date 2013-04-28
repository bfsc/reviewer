package br.ufpe.cin.reviewer.core.aspects;

import javax.persistence.EntityTransaction;

import br.ufpe.cin.reviewer.core.ITransactionalController;
import br.ufpe.cin.reviewer.persistence.JPAEntityManager;

public aspect JPATransactionManagement extends TransactionManagement {
	
	public pointcut transactionalMethods() :
		execution(public * ITransactionalController+.*(..)) &&
		!execution(public * ITransactionalController+.set*(..)) &&
		!execution(public * ITransactionalController+.get*(..)) &&
		!execution(static * ITransactionalController+.*(..));

	protected void beginTransaction() {
		EntityTransaction transaction = JPAEntityManager.ENTITY_MANAGER.getTransaction();
		transaction.begin();
	}

	protected void commit() {
		EntityTransaction transaction = JPAEntityManager.ENTITY_MANAGER.getTransaction();
		transaction.commit();
	}

	protected void rollback() {
		EntityTransaction transaction = JPAEntityManager.ENTITY_MANAGER.getTransaction();
		transaction.rollback();
	}
	
}
