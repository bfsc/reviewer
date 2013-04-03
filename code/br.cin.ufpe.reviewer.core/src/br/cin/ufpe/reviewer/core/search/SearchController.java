package br.cin.ufpe.reviewer.core.search;

import java.io.FileWriter;

import br.cin.ufpe.reviewer.core.exceptions.CoreException;
import br.cin.ufpe.reviewer.search.provider.acm.AcmSearchProvider;
import br.cin.ufpe.reviewer.search.provider.engineeringvillage.EngineeringVillageSearchProvider;
import br.cin.ufpe.reviewer.search.provider.ieee.IeeeSearchProvider;
import br.cin.ufpe.reviewer.search.provider.sciencedirect.ScienceDirectSearchProvider;
import br.cin.ufpe.reviewer.search.provider.scopus.ScopusSearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.springerlink.SpringerLinkSearchProvider;

public class SearchController {

	public SearchResult search(String searchString, SearchFilter filter) {
		SearchResult result = new SearchResult();
		result.setSearchString(searchString);
		
		try {
			for (String searchProviderKey : filter.getSearchProvidersKeys()) {
				switch (searchProviderKey) {
				case "ACM":
					AcmSearchProvider acmSearchProvider = new AcmSearchProvider();
					result.addStudies(acmSearchProvider.getKey(), acmSearchProvider.search(searchString).getStudies());
					break;
				case "IEEE":
					IeeeSearchProvider ieeeSearchProvider = new IeeeSearchProvider();
					result.addStudies(ieeeSearchProvider.getKey(), ieeeSearchProvider.search(searchString).getStudies());
					break;
				case "SCOPUS":
					ScopusSearchProvider scopusSearchProvider = new ScopusSearchProvider();
					result.addStudies(scopusSearchProvider.getKey(), scopusSearchProvider.search(searchString).getStudies());
					break;
				case "SPRINGER_LINK":
					SpringerLinkSearchProvider springerLinkSearchProvider = new SpringerLinkSearchProvider();
					result.addStudies(springerLinkSearchProvider.getKey(), springerLinkSearchProvider.search(searchString).getStudies());
					break;	
				case "SCIENCE_DIRECT":
					ScienceDirectSearchProvider scienceDirectSearchProvider = new ScienceDirectSearchProvider();
					result.addStudies(scienceDirectSearchProvider.getKey(), scienceDirectSearchProvider.search(searchString).getStudies());
					break;
				case "ENGINEERING_VILLAGE":
					EngineeringVillageSearchProvider engineeringVillageSearchProvider = new EngineeringVillageSearchProvider();
					result.addStudies(engineeringVillageSearchProvider.getKey(), engineeringVillageSearchProvider.search(searchString).getStudies());
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
	
	public static void main(String[] args) {
		try {
			SearchFilter filter = new SearchFilter();
			filter.addSearchProviderKey("ACM");
			filter.addSearchProviderKey("IEEE");
			filter.addSearchProviderKey("SCIENCE_DIRECT");
			filter.addSearchProviderKey("SCOPUS");

			SearchController searchController = new SearchController();
			SearchResult result = searchController.search("\"systematic mapping study\"", filter);
//			SearchResult result = searchController.search("\"software engineering\"", filter);
			
			StringBuilder buffer = new StringBuilder();
			for (String searchProviderKey : result.getAllStudies().keySet()) {
				for (Study study : result.getAllStudies().get(searchProviderKey)) {
					buffer.append(searchProviderKey + ": " + study.getTitle() + "\r\n");
				}
			}
			
			FileWriter writer = new FileWriter("C:/Documents and Settings/Bruno Cartaxo/Desktop/search.result.txt");
			writer.write(buffer.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
