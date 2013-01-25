package br.cin.ufpe.reviewer.search.provider.spi;

import java.util.Set;

import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

public interface SearchProvider {

	public Set<Study> search(String searchString);
	
}
