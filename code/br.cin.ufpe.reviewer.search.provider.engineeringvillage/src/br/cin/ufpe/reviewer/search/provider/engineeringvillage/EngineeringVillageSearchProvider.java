package br.cin.ufpe.reviewer.search.provider.engineeringvillage;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;

public class EngineeringVillageSearchProvider implements SearchProvider {

	private static final String SEARCH_PROVIDER_KEY_ENGINEERING_VILLAGE = "ENGINEERING_VILLAGE";

	public SearchResult search(String searchString) throws SearchProviderException {
		return null;
	}

	public String getKey() {
		return SEARCH_PROVIDER_KEY_ENGINEERING_VILLAGE;
	}

}
