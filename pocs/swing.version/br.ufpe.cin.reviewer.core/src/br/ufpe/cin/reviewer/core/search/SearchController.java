package br.ufpe.cin.reviewer.core.search;

import java.io.FileWriter;

import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.acm.AcmSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.engineeringvillage.EngineeringVillageSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.ieee.IeeeSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.sciencedirect.ScienceDirectSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.scopus.ScopusSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.springerlink.SpringerLinkSearchProvider;

public class SearchController {

	public SearchResult search(String searchString, SearchFilter filter) {
		SearchResult result = new SearchResult();
		result.setSearchString(searchString);
		
		try {
			for (String searchProviderKey : filter.getSearchProvidersKeys()) {
				switch (searchProviderKey) {
				case "ACM":
					AcmSearchProvider acmSearchProvider = new AcmSearchProvider();
					result.addStudies("ACM", acmSearchProvider.search(searchString).getStudies());
					break;
				case "IEEE":
					IeeeSearchProvider ieeeSearchProvider = new IeeeSearchProvider();
					result.addStudies("IEEE", ieeeSearchProvider.search(searchString).getStudies());
					break;
				case "SCOPUS":
					ScopusSearchProvider scopusSearchProvider = new ScopusSearchProvider();
					result.addStudies("SCOPUS", scopusSearchProvider.search(searchString).getStudies());
					break;
				case "SPRINGER_LINK":
					SpringerLinkSearchProvider springerLinkSearchProvider = new SpringerLinkSearchProvider();
					result.addStudies("SPRINGER_LINK", springerLinkSearchProvider.search(searchString).getStudies());
					break;	
				case "SCIENCE_DIRECT":
					ScienceDirectSearchProvider scienceDirectSearchProvider = new ScienceDirectSearchProvider();
					result.addStudies("SCIENCE_DIRECT", scienceDirectSearchProvider.search(searchString).getStudies());
					break;
				case "ENGINEERING_VILLAGE":
					EngineeringVillageSearchProvider engineeringVillageSearchProvider = new EngineeringVillageSearchProvider();
					result.addStudies("ENGINEERING_VILLAGE", engineeringVillageSearchProvider.search(searchString).getStudies());
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
