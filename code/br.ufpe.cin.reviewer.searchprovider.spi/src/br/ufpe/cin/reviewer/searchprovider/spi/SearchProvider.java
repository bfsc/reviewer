package br.ufpe.cin.reviewer.searchprovider.spi;

import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

public interface SearchProvider {

	public SearchProviderResult search(String searchString) throws SearchProviderException;
	
	public void die();
	
}
