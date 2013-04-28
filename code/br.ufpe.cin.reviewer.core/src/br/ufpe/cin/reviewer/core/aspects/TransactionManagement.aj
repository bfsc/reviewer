package br.ufpe.cin.reviewer.core.aspects;

public abstract aspect TransactionManagement {

	private static int methodCallCount = 0;
	
	public abstract pointcut transactionalMethods();
	
	before() : transactionalMethods() {
		if (methodCallCount == 0) {
			beginTransaction();
		}
		
		methodCallCount++;
	}

	after() returning : transactionalMethods() {
		methodCallCount--;
		
		if (methodCallCount == 0) {
			commit();
		}
	}

	after() throwing() : transactionalMethods()  {
		rollback();
		methodCallCount = 0;
	}

	protected abstract void beginTransaction();

	protected abstract void commit();

	protected abstract void rollback();
	
}
