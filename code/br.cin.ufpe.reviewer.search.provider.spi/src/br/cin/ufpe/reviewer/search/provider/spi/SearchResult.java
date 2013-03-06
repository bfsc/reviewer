package br.cin.ufpe.reviewer.search.provider.spi;

import java.util.ArrayList;
import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

public class SearchResult {

	private List<Study> studies;

	public SearchResult() {
		this.studies = new ArrayList<Study>();
	}

	public SearchResult(List<Study> studies) {
		this.studies = studies;
	}

	public List<Study> getStudies() {
		return studies;
	}
	
}
