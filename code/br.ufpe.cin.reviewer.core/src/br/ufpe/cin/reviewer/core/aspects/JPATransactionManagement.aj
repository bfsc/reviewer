package br.ufpe.cin.reviewer.core.aspects;

import javax.persistence.EntityTransaction;

import br.ufpe.cin.reviewer.core.ITransactionalController;
import br.ufpe.cin.reviewer.persistence.JPAEntityManager;

public aspect JPATransactionManagement extends TransactionManagement {
	
	private static EntityTransaction transaction;
	
	public pointcut transactionalMethods() :
		execution(public * ITransactionalController+.*(..)) &&
		!execution(public * ITransactionalController+.set*(..)) &&
		!execution(public * ITransactionalController+.get*(..)) &&
		!execution(static * ITransactionalController+.*(..));

	protected void beginTransaction() {
		transaction = JPAEntityManager.ENTITY_MANAGER.getTransaction();
		transaction.begin();
	}

	protected void commit() {
		if (transaction!= null && transaction.isActive()) {
			transaction.commit();
		}
	}

	protected void rollback() {
		if (transaction!= null && transaction.isActive()) {
			transaction.rollback();
		}
	}
	
}
