package br.cin.ufpe.reviewer.persistence.dao;

import java.util.Set;

import br.cin.ufpe.reviewer.persistence.exceptions.PersistenceException;

public interface IDAO<E,K> {

	public void create(E entity) throws PersistenceException;
	
	public E retrieve(K key) throws PersistenceException;
	
	public Set<E> retrieveAll() throws PersistenceException;
	
	public void update(E entity) throws PersistenceException;
	
	public void delete(E entity) throws PersistenceException;
	
}
