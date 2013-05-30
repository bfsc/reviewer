package br.ufpe.cin.reviewer.searchprovider.spi;

import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderError;

public class SearchProviderResult {

	private String searchProviderName;
	
	private int totalFound;
	
	private List<Study> studies = new LinkedList<Study>();
	
	private List<SearchProviderError> raisedErrors = new LinkedList<SearchProviderError>();

	public SearchProviderResult() {
		
	}
	
	public SearchProviderResult(String searchProviderName) {
		this.searchProviderName = searchProviderName;
	}

	public String getSearchProviderName() {
		return searchProviderName;
	}

	public void setSearchProviderName(String searchProviderName) {
		this.searchProviderName = searchProviderName;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public int getTotalFound() {
		return totalFound;
	}
	
	public void setTotalFound(int totalFound) {
		this.totalFound = totalFound;
	}

	public int getTotalFetched() {
		return studies.size();
	}
	
	public void addError(SearchProviderError error) {
		this.raisedErrors.add(error);
	}
	
	public List<SearchProviderError> getRaisedErrors() {
		return this.raisedErrors;
	}
	
	public void clearErrors() {
		this.raisedErrors.clear();
	}
}
