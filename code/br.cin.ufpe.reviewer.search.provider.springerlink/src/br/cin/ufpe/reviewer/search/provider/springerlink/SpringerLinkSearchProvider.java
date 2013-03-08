package br.cin.ufpe.reviewer.search.provider.springerlink;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;

public class SpringerLinkSearchProvider implements SearchProvider {

	private static final String SEARCH_PROVIDER_KEY_SPRINGER_LINK = "SPRINGER_LINK";

	public SearchResult search(String searchString) throws SearchProviderException {
		return null;
	}

	public String getKey() {
		return SEARCH_PROVIDER_KEY_SPRINGER_LINK;
	}

}
