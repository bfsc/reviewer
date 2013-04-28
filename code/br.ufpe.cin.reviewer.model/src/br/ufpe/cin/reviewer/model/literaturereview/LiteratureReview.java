package br.ufpe.cin.reviewer.model.literaturereview;

import br.ufpe.cin.reviewer.model.common.SearchResult;

public class LiteratureReview {

	private int id;
	private String title;
	private SearchResult searchResult;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public SearchResult getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(SearchResult searchResult) {
		this.searchResult = searchResult;
	}
	
}
