package br.cin.ufpe.reviewer.search.provider.spi.entities;

public class Study {

	private String title;
	private String studyAbstract;
	private String url;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
}
