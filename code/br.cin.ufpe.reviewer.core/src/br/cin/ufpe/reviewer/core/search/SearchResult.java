package br.cin.ufpe.reviewer.core.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

public class SearchResult {

	private Map<String, List<Study>> allStudies = new HashMap<String, List<Study>>();
	
	public SearchResult() {
		
	}

	public Map<String, List<Study>> getAllStudies() {
		return allStudies;
	}
	
	public void addStudies(String searchProviderKey, List<Study> studies) {
		this.allStudies.put(searchProviderKey, studies);
	}
	
	public void removeStudies(String searchProviderKey) {
		this.allStudies.remove(searchProviderKey);
	}
	
}
