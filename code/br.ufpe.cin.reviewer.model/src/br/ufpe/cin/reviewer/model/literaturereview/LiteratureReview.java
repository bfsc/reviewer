package br.ufpe.cin.reviewer.model.literaturereview;

import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;

public class LiteratureReview {

	private int id;
	
	private String title;
	private String queryString;
	private List<LiteratureReviewSource> sources = new LinkedList<LiteratureReviewSource>();
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
	
	public String getQueryString() {
		return queryString;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public List<LiteratureReviewSource> getSources() {
		return sources;
	}

	public void setSources(List<LiteratureReviewSource> sources) {
		this.sources = sources;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(List<Study> studies) {
		this.studies = studies;
	}
	
	public int getTotalFound(){
		int totalFound = 0;
		for (LiteratureReviewSource literatureReviewSource : this.sources) {
			totalFound += literatureReviewSource.getTotalFound();
		}
		return totalFound;
	}
	
	public int getTotalFetched(){
		int totalFetched = 0;
		for (LiteratureReviewSource literatureReviewSource : this.sources) {
			totalFetched += literatureReviewSource.getTotalFetched();
		}
		return totalFetched;
	}
	
	public void addSource(LiteratureReviewSource source) {
		this.sources.add(source);
	}
	
	public void removeSource(LiteratureReviewSource source) {
		this.sources.remove(source);
	}
	
	public void addStudy(Study study) {
		this.studies.add(study);
	}
	
	public void removeStudy(Study study) {
		this.studies.remove(study);
	}
	
}
