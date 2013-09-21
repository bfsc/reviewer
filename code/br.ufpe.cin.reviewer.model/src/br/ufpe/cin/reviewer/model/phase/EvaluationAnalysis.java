package br.ufpe.cin.reviewer.model.phase;

import br.ufpe.cin.reviewer.model.literaturereview.Criteria;

public class EvaluationAnalysis {

	private int id;
	private Evaluator evaluator;
	private Criteria criteria;
	private Status status;

	public enum Status {
		ACCEPTED, REJECT, NOT_EVALUATED;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Evaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


}
