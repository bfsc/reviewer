package br.ufpe.cin.reviewer.model.phase;

import java.util.LinkedList;
import java.util.List;

public class Group {

	private int id;
	private List<Evaluator> evaluators = new LinkedList<Evaluator>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Evaluator> getEvaluators() {
		return evaluators;
	}

	public void setEvaluators(List<Evaluator> evaluators) {
		this.evaluators = evaluators;
	}

}
