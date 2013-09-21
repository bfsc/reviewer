package br.ufpe.cin.reviewer.model.phase;

import java.util.LinkedList;
import java.util.List;
import br.ufpe.cin.reviewer.model.study.StudyAnalysis;


public class Phase {

	private int id;

	private List<StudyAnalysis> studyAnalyses = new LinkedList<StudyAnalysis>();
	private List<Group> groups = new LinkedList<Group>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<StudyAnalysis> getStudyAnalysis() {
		return studyAnalyses;
	}

	public void setStudyAnalysis(List<StudyAnalysis> studyAnalyses) {
		this.studyAnalyses = studyAnalyses;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
