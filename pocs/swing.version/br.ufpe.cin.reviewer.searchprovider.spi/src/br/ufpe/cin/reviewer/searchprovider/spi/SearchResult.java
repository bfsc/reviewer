package br.ufpe.cin.reviewer.searchprovider.spi;

import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;

public class SearchResult {

	private int totalResults;
	private int fetchedResults;
	private List<Study> studies = new LinkedList<Study>();

	public SearchResult() {
		
	}
	
	public SearchResult(int totalResults, int fetchedResults) {
		this.totalResults = totalResults;
		this.fetchedResults = fetchedResults;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public int getTotalResults() {
		return totalResults;
	}
	
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public int getFetchedResults() {
		return fetchedResults;
	}
	
	public void setFetchedResults(int fetchedResults) {
		this.fetchedResults = fetchedResults;
	}
}
