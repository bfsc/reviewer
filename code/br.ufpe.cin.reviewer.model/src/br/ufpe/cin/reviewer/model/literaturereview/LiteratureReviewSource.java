package br.ufpe.cin.reviewer.model.literaturereview;

public class LiteratureReviewSource {

	public enum SourceType {
		AUTOMATIC, MANUAL
	}
	
	private String name;
	private SourceType type;
	private int totalFound;
	private int totalFetched;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public SourceType getType() {
		return type;
	}
	
	public void setType(SourceType type) {
		this.type = type;
	}
	
	public int getTotalFound() {
		return totalFound;
	}
	
	public void setTotalFound(int totalFound) {
		this.totalFound = totalFound;
	}
	
	public int getTotalFetched() {
		return totalFetched;
	}
	
	public void setTotalFetched(int totalFetched) {
		this.totalFetched = totalFetched;
	}

	public boolean equals(Object anotherObject) {
		boolean equals = false;
		
		if (anotherObject instanceof LiteratureReviewSource) {
			LiteratureReviewSource anotherSource = (LiteratureReviewSource) anotherObject;
			equals = this.name.equals(anotherSource.name);
		}
		
		return equals;
	}
	
	public int hashCode() {
		return (this.getClass().getName() + this.name).hashCode();
	}
	
}
