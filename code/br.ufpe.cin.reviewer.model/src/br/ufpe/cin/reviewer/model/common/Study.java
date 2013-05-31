package br.ufpe.cin.reviewer.model.common;

import java.util.LinkedList;
import java.util.List;

public class Study {

	public enum StudyStatus{INCLUDED,EXCLUDED,NOT_EVALUATED};
	private int id;
	
	private String code;
	private String title;
	private StudyStatus status;
	private String source;
	private String studyAbstract;
	private String year;
	private String url;
	private List<String> authors = new LinkedList<String>();
	private List<String> institutions = new LinkedList<String>();
	private List<String> countries = new LinkedList<String>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getAbstract() {
		return studyAbstract;
	}
	
	public void setAbstract(String studyAbstract) {
		this.studyAbstract = studyAbstract;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public List<String> getInstitutions() {
		return institutions;
	}

	public void setInstitutions(List<String> institutions) {
		this.institutions = institutions;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	
	public StudyStatus getStatus() {
		return status;
	}

	public void setStatus(StudyStatus status) {
		this.status = status;
	}

	public void addAuthor(String author) {
		this.authors.add(author);
	}
	
	public void removeAuthor(String author) {
		this.authors.remove(author);
	}
	
	public void addInstitution(String institution) {
		this.institutions.add(institution);
	}
	
	public void removeInstitution(String institution) {
		this.institutions.remove(institution);
	}
	
	public void addCountry(String country) {
		if (!this.countries.contains(country)) {
			this.countries.add(country);
		}		
	}
	
	public void removeCountry(String country) {
		this.countries.remove(country);
	}
	
	public String toString() {
		return title;
	}
	
}
