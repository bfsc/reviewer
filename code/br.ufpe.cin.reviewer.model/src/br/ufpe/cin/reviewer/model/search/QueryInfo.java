package br.ufpe.cin.reviewer.model.search;

public class QueryInfo {

	private int totalFound;
	private int totalFetched;
	private String source;
	
	public QueryInfo(){
		
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
}
