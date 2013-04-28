package br.ufpe.cin.reviewer.model.common;

public class StudyAuthor {
	
	private int id;
	private String author;
	
	public StudyAuthor() {
	
	}
	
	public StudyAuthor(String author) {
		this.author = author;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
