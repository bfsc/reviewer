package br.ufpe.cin.reviewer.model.literaturereview;

import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;

public class LiteratureReview {

	private int id;
	private String title;
	private List<Study> studies = new LinkedList<Study>();
	
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

	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}
	
	public void addStudy(Study study) {
		this.studies.add(study);
	}
	
	public void removeStudy(Study study) {
		this.studies.remove(study);
	}
	
}
