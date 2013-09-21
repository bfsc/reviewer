package br.ufpe.cin.reviewer.model.study;

import java.util.LinkedList;
import java.util.List;
import br.ufpe.cin.reviewer.model.phase.EvaluationAnalysis;

public class StudyAnalysis {

	private int id;
	private List<EvaluationAnalysis> evaluationAnalysis = new LinkedList<EvaluationAnalysis>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<EvaluationAnalysis> getEvaluationAnalysis() {
		return evaluationAnalysis;
	}

	public void setEvaluationAnalysis(
			List<EvaluationAnalysis> evaluationAnalysis) {
		this.evaluationAnalysis = evaluationAnalysis;
	}

}
