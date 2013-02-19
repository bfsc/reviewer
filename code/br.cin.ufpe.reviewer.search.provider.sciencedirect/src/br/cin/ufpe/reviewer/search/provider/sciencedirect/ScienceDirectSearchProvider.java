package br.cin.ufpe.reviewer.search.provider.sciencedirect;

import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

public class ScienceDirectSearchProvider implements SearchProvider {
	
	private static final String DOMAIN_DL_SCIENCE_DIRECT = "http://www.sciencedirect.com/";
	
	private static final String XPATH_ADVANCED_SEARCH = "//td[@align='right' and @nowrap='nowrap' and @widht='90%' and @valign='middle']//a[@style='vertical-align:bottom;font-size:0.92em;']";
	
	private static final String XPATH_EXPERT_SEARCH = "//div[@class='advExpertLink' and @style='float:right;']";
	
	

	
	
	
	
	public List<Study> search(String searchString) throws SearchProviderException {
		return null;
	}

}
