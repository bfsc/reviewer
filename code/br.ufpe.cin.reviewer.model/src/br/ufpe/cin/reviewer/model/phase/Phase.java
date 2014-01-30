package br.ufpe.cin.reviewer.model.phase;

import java.util.LinkedList;
import java.util.List;
import br.ufpe.cin.reviewer.model.study.StudyAnalysis;


public class Phase {

	private int id;
	private List<StudyAnalysis> studyAnalisys = new LinkedList<StudyAnalysis>();
	private List<Group> groups = new LinkedList<Group>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public List<StudyAnalysis> getStudyAnalisys() {
		return studyAnalisys;
	}

	public void setStudyAnalisys(List<StudyAnalysis> studyAnalisys) {
		this.studyAnalisys = studyAnalisys;
	}

}
