package br.ufpe.cin.reviewer.model.search;

import java.util.LinkedList;
import java.util.List;

public class AutomatedSearch {

	private int id;
	private String queryString;
	private List<QueryInfo> queryInfos = new LinkedList<QueryInfo>();


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List<QueryInfo> getQueryInfos() {
		return queryInfos;
	}

	public void setQueryInfos(List<QueryInfo> queryInfos) {
		this.queryInfos = queryInfos;
	}

}
