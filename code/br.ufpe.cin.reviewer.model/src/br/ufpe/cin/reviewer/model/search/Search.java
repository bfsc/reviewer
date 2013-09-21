package br.ufpe.cin.reviewer.model.search;

import java.util.LinkedList;
import java.util.List;
import br.ufpe.cin.reviewer.model.study.Study;

public class Search {

	private List<Study> studies = new LinkedList<Study>();
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}

}
