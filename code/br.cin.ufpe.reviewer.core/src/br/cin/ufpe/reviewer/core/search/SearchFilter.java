package br.cin.ufpe.reviewer.core.search;

import java.util.LinkedList;
import java.util.List;

public class SearchFilter {

	private List<String> searchProvidersKeys = new LinkedList<String>();

	public SearchFilter() {
	
	}
	
	public List<String> getSearchProvidersKeys() {
		return this.searchProvidersKeys;
	}
	
	public void addSearchProviderKey(String searchProviderKey) {
		this.searchProvidersKeys.add(searchProviderKey);
	}
	
	public void removeSearchProviderKey(String searchProviderKey) {
		this.searchProvidersKeys.remove(searchProviderKey);
	}
	
}
