package br.cin.ufpe.reviewer.model.study;

public class PrimaryStudySource {

	private String name;

	private String link;

	public PrimaryStudySource(String name, String link) {

	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		return name.equalsIgnoreCase(((PrimaryStudySource) o).name);
	}

}
