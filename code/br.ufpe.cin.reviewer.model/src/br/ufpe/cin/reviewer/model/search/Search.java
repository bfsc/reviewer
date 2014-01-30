package br.ufpe.cin.reviewer.model.search;

import java.util.LinkedList;
import java.util.List;
import br.ufpe.cin.reviewer.model.study.Study;

public class Search {

	private int id;
	private List<Study> studies = new LinkedList<Study>();

	public int getId() {
		return id;
	}

	public void setId(int searchId) {
		this.id = searchId;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}

}
