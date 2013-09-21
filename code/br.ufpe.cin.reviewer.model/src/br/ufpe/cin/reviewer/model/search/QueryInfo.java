package br.ufpe.cin.reviewer.model.search;

public class QueryInfo {

	private int totalFount;
	private int totalFetched;
	private String source;
	
	public QueryInfo(){
		
	}

	public int getTotalFount() {
		return totalFount;
	}

	public void setTotalFount(int totalFount) {
		this.totalFount = totalFount;
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
