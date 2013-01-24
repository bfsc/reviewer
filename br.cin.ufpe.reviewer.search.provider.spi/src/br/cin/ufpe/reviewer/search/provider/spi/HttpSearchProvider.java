package br.cin.ufpe.reviewer.search.provider.spi;

import java.util.HashSet;
import java.util.Set;

import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

public abstract class HttpSearchProvider implements SearchProvider {

	public Set<Study> search(String searchString) {
		Set<Study> studies = new HashSet<>();
		
		// Perform search
		String html = performSearch(searchString);
		
		while (html != null) {
			// Extract studies data
			studies.addAll(extractStudiesData(html));
			
			// Navigate through search pages
			html = navigateThroughSearchPages(html);
		}
		
		return studies;
	}

	protected abstract String performSearch(String searchString);
	
	protected abstract Set<Study> extractStudiesData(String html);

	protected abstract String navigateThroughSearchPages(String html);	
	
}
