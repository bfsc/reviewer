package br.ufpe.cin.reviewer.core.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;

import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.searchprovider.extensions.SearchProviderExtensionsRegistry;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;

public class SearchController {

	private static Set<SearchProviderThread> searchProviderThreads = new HashSet<SearchProviderThread>();
	
	public SearchResult search(String searchString, SearchFilter filter, boolean concurrent) {
		SearchResult result = null;
		
		if (concurrent) {
			result = concurrentSearch(searchString, filter);
		} else {
			result = sequentialSearch(searchString, filter);
		}
			
		return result;
	}

	public void stopSearch() {
		try {
			// Stopping all running threads
			for (SearchProviderThread thread : searchProviderThreads) {
				thread.die();
			}
			
			// Removing all used threads
			searchProviderThreads.clear();
		} catch (Throwable t) {
			throw new CoreException("Error trying to stop a search", t);
		}
	}
	
	private SearchResult sequentialSearch(String searchString, SearchFilter filter) {
		SearchResult result = new SearchResult();

		try {
			// TODO REMOVER INSTRUMENTACAO PARA MEDIR PERFORMANCE
			long start = System.currentTimeMillis();
			
			List<IConfigurationElement> configs = SearchProviderExtensionsRegistry.getConfigElements();
			
			for (String searchProviderKey : filter.getSearchProvidersKeys()) {
				for (IConfigurationElement config : configs) {
					if (config.getAttribute("key").equals(searchProviderKey)) {
						SearchProvider searchProvider = (SearchProvider) config.createExecutableExtension("class");
						result.addSearchProviderResult(searchProvider.search(searchString));
						break;
					}
				}
			}
			
			// TODO REMOVER INSTRUMENTACAO PARA MEDIR PERFORMANCE
			System.out.println(((System.currentTimeMillis() - start) / 1000) + "s");
		} catch (Exception e) {
			throw new CoreException("Error trying to perform the search for: " + searchString, e);
		}

		return result;
	}
	
	private SearchResult concurrentSearch(String searchString, SearchFilter filter) {
		SearchResult result = new SearchResult();

		try {
			// TODO REMOVER INSTRUMENTACAO PARA MEDIR PERFORMANCE
			long start = System.currentTimeMillis();
			
			// Performing the search
			for (String searchProviderKey : filter.getSearchProvidersKeys()) {
				List<IConfigurationElement> configs = SearchProviderExtensionsRegistry.getConfigElements();

				for (IConfigurationElement config : configs) {
					if (config.getAttribute("key").equals(searchProviderKey)) {
						SearchProvider searchProvider = (SearchProvider) config.createExecutableExtension("class");
						
						SearchProviderThread thread = new SearchProviderThread(searchProviderKey, searchProvider, searchString);
						searchProviderThreads.add(thread);
						thread.start();
						break;
					}
				}
			}

			// Blocking the current thread until all search providers finish
			for (SearchProviderThread thread : searchProviderThreads) {
				thread.join();
			}

			// Adding the search provider results to search result 
			for (SearchProviderThread thread : searchProviderThreads) {
				if (thread.getResult() != null) {
					result.addSearchProviderResult(thread.getResult());
				}
			}
			
			// Removing all used threads
			searchProviderThreads.clear();

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
		
		public SearchProviderThread(String threadName, SearchProvider provider, String searchString) {
			super(threadName);
			this.provider = provider;
			this.searchString = searchString;
		}

		public void run() {
			try {
				this.result = this.provider.search(this.searchString);
			} catch (Throwable t) {
				throw new RuntimeException("Error trying to execute a search to search provider " + provider.getClass().getSimpleName(), t);
			}
		}

		public SearchProviderResult getResult() {
			return result;
		}
		
		public void die() {
			this.provider.die();
		}
	}
}
