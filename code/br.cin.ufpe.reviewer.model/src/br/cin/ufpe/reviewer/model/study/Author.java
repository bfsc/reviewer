package br.cin.ufpe.reviewer.model.study;

public class Author {

	private String name;

	Author(String name) {
		this.name = name;
	}

	public Author() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
