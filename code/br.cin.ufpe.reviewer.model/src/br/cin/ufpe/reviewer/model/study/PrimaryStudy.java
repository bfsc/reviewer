package br.cin.ufpe.reviewer.model.study;

import java.util.List;

public class PrimaryStudy {
	
	private String bibtexString;
    
    private String title;
    
    private int year;
    
    private String paperAbstract;
    
    private String issn;
    
    private List<Author> authors;
    
    private boolean evaluated;
    
    private String url;

	public String getBibtexString() {
		return bibtexString;
	}

	public void setBibtexString(String bibtexString) {
		this.bibtexString = bibtexString;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPaperAbstract() {
		return paperAbstract;
	}

	public void setPaperAbstract(String paperAbstract) {
		this.paperAbstract = paperAbstract;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
}
