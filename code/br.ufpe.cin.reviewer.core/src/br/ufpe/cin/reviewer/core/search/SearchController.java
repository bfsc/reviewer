package br.ufpe.cin.reviewer.core.search;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.searchprovider.acm.AcmSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.engineeringvillage.EngineeringVillageSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.ieee.IeeeSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.sciencedirect.ScienceDirectSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.scopus.ScopusSearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;
import br.ufpe.cin.reviewer.searchprovider.springerlink.SpringerLinkSearchProvider;

public class SearchController {

	public SearchResult search(String searchString, SearchFilter filter) {
		SearchResult result = new SearchResult();
		
		// TODO REMOVER INSTRUMENTACAO PARA MEDIR PERFORMANCE
		long start = System.currentTimeMillis();
		
		Map<String, SearchProviderThread> searchProviderThreads = new HashMap<String, SearchController.SearchProviderThread>();
		
		try {
			for (String searchProviderKey : filter.getSearchProvidersKeys()) {
				switch (searchProviderKey) {
				case "ACM":
					SearchProviderThread acmThread = new SearchProviderThread(new AcmSearchProvider(), searchString);
					searchProviderThreads.put(searchProviderKey, acmThread);
					acmThread.run();
					break;
				case "IEEE":
					SearchProviderThread ieeeThread = new SearchProviderThread(new IeeeSearchProvider(), searchString);
					searchProviderThreads.put(searchProviderKey, ieeeThread);
					ieeeThread.run();
					break;
				case "SCOPUS":
					SearchProviderThread scopusThread = new SearchProviderThread(new ScopusSearchProvider(), searchString);
					searchProviderThreads.put(searchProviderKey, scopusThread);
					scopusThread.run();
					break;
				case "SPRINGER_LINK":
					SearchProviderThread springerLinkThread = new SearchProviderThread(new SpringerLinkSearchProvider(), searchString);
					searchProviderThreads.put(searchProviderKey, springerLinkThread);
					springerLinkThread.run();
					break;	
				case "SCIENCE_DIRECT":
					SearchProviderThread scienceDirectThread = new SearchProviderThread(new ScienceDirectSearchProvider(), searchString);
					searchProviderThreads.put(searchProviderKey, scienceDirectThread);
					scienceDirectThread.run();
					break;
				case "ENGINEERING_VILLAGE":
					SearchProviderThread engineeringVillageThread = new SearchProviderThread(new EngineeringVillageSearchProvider(), searchString);
					searchProviderThreads.put(searchProviderKey, engineeringVillageThread);
					engineeringVillageThread.run();
				default:
					throw new CoreException("Invalid search provider key: " + searchProviderKey);
				}
			}
			
			// Blocking the current thread until all search providers finish
			for (SearchProviderThread thread : searchProviderThreads.values()) {
				thread.join();
			}
			
			// Adding the search provider results to search result 
			for (SearchProviderThread thread : searchProviderThreads.values()) {
				result.addSearchProviderResult(thread.getResult());
			}
			
			// TODO REMOVER INSTRUMENTACAO PARA MEDIR PERFORMANCE
			System.out.println(((System.currentTimeMillis() - start) / 1000) + "s");
		} catch (Exception e) {
			throw new CoreException("Error trying to perform the search for: " + searchString, e);
		}
		
		return result;
	}
	
	private class SearchProviderThread extends Thread {
		
		private SearchProvider provider;
		private String searchString;
		
		private SearchProviderResult result;
		
		public SearchProviderThread(SearchProvider provider, String searchString) {
			this.provider = provider;
			this.searchString = searchString;
		}
		
		public void run() {
			try {
				this.result = this.provider.search(this.searchString);
			} catch (SearchProviderException e) {
				throw new RuntimeException("Error trying to execute a search to search provider " + provider.getClass().getSimpleName());
			}
		}

		public SearchProviderResult getResult() {
			return result;
		}
		
	}
	
}
