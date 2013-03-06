package br.cin.ufpe.reviewer.search.provider.spi;

public class SearchFilter {

	private int startYear;
	private int endYear;
	private String author;
	private String country;

	
	public SearchFilter() {
		
	}

	public SearchFilter(int startYear, int endYear, String author,
			String country) {
		super();
		this.startYear = startYear;
		this.endYear = endYear;
		this.author = author;
		this.country = country;
	}

	public int getStartYear() {
		return startYear;
	}
	
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	
	public int getEndYear() {
		return endYear;
	}
	
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
}
