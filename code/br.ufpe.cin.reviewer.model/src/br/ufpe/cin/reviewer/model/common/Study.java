package br.ufpe.cin.reviewer.model.common;

import java.util.List;

public class Study {

	private int id;
	
	private String code;
	private String title;
	private String source;
	private String studyAbstract;
	private String year;
	private String url;
	private List<StudyAuthor> studyAuthors;
	private List<StudyInstitution> studyInstitutions;
	private List<StudyCountry> studyCountries;
	
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
	
	public String toString() {
		return title;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public List<StudyAuthor> getStudyAuthors() {
		return studyAuthors;
	}

	public void setStudyAuthors(List<StudyAuthor> studyAuthors) {
		this.studyAuthors = studyAuthors;
	}

	public List<StudyInstitution> getStudyInstitutions() {
		return studyInstitutions;
	}

	public void setStudyInstitutions(List<StudyInstitution> studyInstitutions) {
		this.studyInstitutions = studyInstitutions;
	}

	public List<StudyCountry> getStudyCountries() {
		return studyCountries;
	}

	public void setStudyCountries(List<StudyCountry> studyCountries) {
		this.studyCountries = studyCountries;
	}
	
	public void addStudyAuthor(StudyAuthor studyAuthor) {
		this.studyAuthors.add(studyAuthor);
	}
	
	public void removeStudyAuthor(StudyAuthor studyAuthor) {
		this.studyAuthors.remove(studyAuthor);
	}
	
	public void addStudyInstitution(StudyInstitution studyInstitution) {
		this.studyInstitutions.add(studyInstitution);
	}
	
	public void removeStudyInstitution(StudyInstitution studyInstitution) {
		this.studyInstitutions.remove(studyInstitution);
	}
	
	public void addStudyCountry(StudyCountry studyCountry) {
		this.studyCountries.add(studyCountry);
	}
	
	public void removeStudyCountry(StudyCountry studyCountry) {
		this.studyCountries.remove(studyCountry);
	}
	
}
