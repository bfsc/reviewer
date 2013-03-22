package br.cin.ufpe.reviewer.core.aspects;

public abstract aspect TransactionManagement {

	private static int methodCallQuantity = 0;
	
	public abstract pointcut transactionalMethods();
	
	before() : transactionalMethods() {
		if (methodCallQuantity == 0) {
			beginTransaction();
		}
		
		methodCallQuantity++;
	}

	after() returning : transactionalMethods() {
		methodCallQuantity--;
		
		if (methodCallQuantity == 0) {
			commit();
		}
	}

	after() throwing() : transactionalMethods()  {
		rollback();
		methodCallQuantity = 0;
	}

	protected abstract void beginTransaction();

	protected abstract void commit();

	protected abstract void rollback();
	
}
