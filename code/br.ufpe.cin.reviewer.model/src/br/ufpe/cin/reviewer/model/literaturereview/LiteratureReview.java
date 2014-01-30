package br.ufpe.cin.reviewer.model.literaturereview;

import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.phase.Phase;
import br.ufpe.cin.reviewer.model.search.Search;

public class LiteratureReview {

	private int id;
	private String title;
	

	private List<Search> searches = new LinkedList<Search>();
	private List<Criteria> critireon = new LinkedList<Criteria>();
	private List<Phase> phases = new LinkedList<Phase>();

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

	public List<Search> getSearches() {
		return searches;
	}

	public void setSearches(List<Search> searches) {
		this.searches = searches;
	}

	public List<Criteria> getCritireon() {
		return critireon;
	}

	public void setCritireon(List<Criteria> critireon) {
		this.critireon = critireon;
	}

	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

}
