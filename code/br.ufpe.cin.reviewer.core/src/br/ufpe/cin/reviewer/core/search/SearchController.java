package br.ufpe.cin.reviewer.core.search;

import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.searchprovider.acm.AcmSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.engineeringvillage.EngineeringVillageSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.ieee.IeeeSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.sciencedirect.ScienceDirectSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.scopus.ScopusSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.springerlink.SpringerLinkSearchProvider;

public class SearchController {

	public SearchResult search(String searchString, SearchFilter filter) {
		SearchResult result = new SearchResult();
		
		try {
			for (String searchProviderKey : filter.getSearchProvidersKeys()) {
				switch (searchProviderKey) {
				case "ACM":
					AcmSearchProvider acmSearchProvider = new AcmSearchProvider();
					result.addSearchProviderResult(acmSearchProvider.search(searchString));
					break;
				case "IEEE":
					IeeeSearchProvider ieeeSearchProvider = new IeeeSearchProvider();
					result.addSearchProviderResult(ieeeSearchProvider.search(searchString));
					break;
				case "SCOPUS":
					ScopusSearchProvider scopusSearchProvider = new ScopusSearchProvider();
					result.addSearchProviderResult(scopusSearchProvider.search(searchString));
					break;
				case "SPRINGER_LINK":
					SpringerLinkSearchProvider springerLinkSearchProvider = new SpringerLinkSearchProvider();
					result.addSearchProviderResult(springerLinkSearchProvider.search(searchString));
					break;	
				case "SCIENCE_DIRECT":
					ScienceDirectSearchProvider scienceDirectSearchProvider = new ScienceDirectSearchProvider();
					result.addSearchProviderResult(scienceDirectSearchProvider.search(searchString));
					break;
				case "ENGINEERING_VILLAGE":
					EngineeringVillageSearchProvider engineeringVillageSearchProvider = new EngineeringVillageSearchProvider();
					result.addSearchProviderResult(engineeringVillageSearchProvider.search(searchString));
					break;
				default:
					throw new CoreException("Invalid search provider key: " + searchProviderKey);
				}
			}
		} catch (Exception e) {
			throw new CoreException("Error trying to perform the search for: " + searchString, e);
		}
		
		return result;
	}
	
}
