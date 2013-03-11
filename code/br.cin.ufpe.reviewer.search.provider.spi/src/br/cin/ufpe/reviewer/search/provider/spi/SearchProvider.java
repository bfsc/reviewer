package br.cin.ufpe.reviewer.search.provider.spi;

import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;

public interface SearchProvider {

	public SearchResult search(String searchString) throws SearchProviderException;
	
	public String getKey();
	
}
