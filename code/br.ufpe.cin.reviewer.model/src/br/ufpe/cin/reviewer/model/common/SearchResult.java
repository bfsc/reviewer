package br.ufpe.cin.reviewer.model.common;

import java.util.LinkedList;
import java.util.List;

public class SearchResult {

	private int totalResults;
	private int fetchedResults;
	
	private List<Study> studies = new LinkedList<Study>();

	public int getTotalResults() {
		return totalResults;
	}

	public int getFetchedResults() {
		return fetchedResults;
	}

	public void addStudy(Study study) {
		this.studies.add(study);
	}
	
	public List<Study> getAllStudies() {
		return studies;
	}
	
}
