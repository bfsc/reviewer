package br.ufpe.cin.reviewer.searchprovider.spi;

import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;

public class SearchProviderResult {

	private String searchProviderName;
	
	private int totalFound;
	private int totalFetched;
	
	private List<Study> studies = new LinkedList<Study>();

	public SearchProviderResult() {
		
	}
	
	public SearchProviderResult(String searchProviderName) {
		this.searchProviderName = searchProviderName;
	}

	public SearchProviderResult(int totalFound, int totalFetched) {
		this.totalFound = totalFound;
		this.totalFetched = totalFetched;
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
		return totalFetched;
	}
	
	public void setTotalFetched(int totalFetched) {
		this.totalFetched = totalFetched;
	}
}
