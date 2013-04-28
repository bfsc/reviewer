package br.ufpe.cin.reviewer.model.common;

public class StudyCountry {

	private int id;
	private String country;
	
	public StudyCountry() {
	
	}

	public StudyCountry(String country) {
		this.country = country;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
