package br.ufpe.cin.reviewer.core.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;

import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.searchprovider.extensions.SearchProviderExtensionsRegistry;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

public class SearchController {

	public SearchResult search(String searchString, SearchFilter filter) {
		SearchResult result = new SearchResult();
		
		// TODO REMOVER INSTRUMENTACAO PARA MEDIR PERFORMANCE
		long start = System.currentTimeMillis();
		
		Map<String, SearchProviderThread> searchProviderThreads = new HashMap<String, SearchController.SearchProviderThread>();
		
		try {
			// Performing the search
			for (String searchProviderKey : filter.getSearchProvidersKeys()) {
				List<IConfigurationElement> configs = SearchProviderExtensionsRegistry.getConfigElements();
				
				for (IConfigurationElement config : configs) {
					if (config.getAttribute("key").equals(searchProviderKey)) {
						SearchProvider searchProvider = (SearchProvider) config.createExecutableExtension("class");
						SearchProviderThread thread = new SearchProviderThread(searchProvider, searchString);
						searchProviderThreads.put(searchProviderKey, thread);
						thread.run();
						break;
					}
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
