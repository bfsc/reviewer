package br.cin.ufpe.reviewer.search.provider.spi;

import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

public interface SearchProvider {

	public List<Study> search(String searchString) throws SearchProviderException;
	
}
