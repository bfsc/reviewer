package br.cin.ufpe.reviewer.search.provider.springerlink;

import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

public class SpringerLinkSearchProvider implements SearchProvider {

	public List<Study> search(String searchString) throws SearchProviderException {
		return null;
	}

}
