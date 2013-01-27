package br.cin.ufpe.reviewer.search.provider.acm;

import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

public class ConcurrentAcmSearchProvider implements SearchProvider {

	public List<Study> search(String searchString) throws SearchProviderException {
		//TODO IMPLEMENTAR
		return null;
	}

}
