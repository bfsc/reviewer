package br.ufpe.cin.reviewer.model.common;

public class StudyInstitution {

	private int id;
	private String institution;
	
	public StudyInstitution() {
	
	}
	
	public StudyInstitution(String institution) {
		this.institution = institution;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
}
