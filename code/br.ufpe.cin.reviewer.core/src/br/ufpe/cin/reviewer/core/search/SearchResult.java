package br.ufpe.cin.reviewer.core.search;

import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.study.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;

public class SearchResult {
	
	private List<SearchProviderResult> searchProviderResults = new LinkedList<SearchProviderResult>();

	public int getTotalFound() {
		int totalfound = 0;
		
		for (SearchProviderResult result : searchProviderResults) {
			totalfound += result.getTotalFound();
		}
		
		return totalfound;
	}

	public int getTotalFetched() {
		int totalFetched = 0;
		
		for (SearchProviderResult result : searchProviderResults) {
			totalFetched += result.getTotalFetched();
		}
		
		return totalFetched;
	}

	public List<SearchProviderResult> getSearchProviderResults() {
		return searchProviderResults;
	}

	public void addSearchProviderResult(SearchProviderResult searchProviderResult) {
		this.searchProviderResults.add(searchProviderResult);
	}
	
	public List<Study> getAllStudies() {
		List<Study> studies = new LinkedList<Study>();
		
		for (SearchProviderResult searchProviderResult : searchProviderResults) {
			studies.addAll(searchProviderResult.getStudies());
		}
		
		return studies;
	}
	
}
