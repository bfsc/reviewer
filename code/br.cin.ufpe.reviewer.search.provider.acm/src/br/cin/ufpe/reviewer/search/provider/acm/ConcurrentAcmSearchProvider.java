package br.cin.ufpe.reviewer.search.provider.acm;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;

public class ConcurrentAcmSearchProvider implements SearchProvider {

	private static final String SEARCH_PROVIDER_KEY_CONCURRENT_ACM = "CONCURRENT_ACM";

	public SearchResult search(String searchString) throws SearchProviderException {
		//TODO IMPLEMENTAR
		return null;
	}

	public String getKey() {
		return SEARCH_PROVIDER_KEY_CONCURRENT_ACM;
	}

}
