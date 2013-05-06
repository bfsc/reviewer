package br.ufpe.cin.reviewer.persistence.dao;

import java.util.List;

import br.ufpe.cin.reviewer.persistence.exceptions.PersistenceException;

public interface IDAO<E,K> {

	public void create(E entity) throws PersistenceException;
	
	public E retrieve(K key) throws PersistenceException;
	
	public List<E> retrieveAll() throws PersistenceException;
	
	public void update(E entity) throws PersistenceException;
	
	public void delete(E entity) throws PersistenceException;
	
}
