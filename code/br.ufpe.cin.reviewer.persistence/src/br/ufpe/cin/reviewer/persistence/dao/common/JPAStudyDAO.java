package br.ufpe.cin.reviewer.persistence.dao.common;

import br.ufpe.cin.reviewer.model.study.Study;
import br.ufpe.cin.reviewer.persistence.dao.JPADAO;

public class JPAStudyDAO extends JPADAO<Study, Integer> implements IStudyDAO {

	private static JPAStudyDAO instance = new JPAStudyDAO();
	
	private JPAStudyDAO() {
		super(Study.class);
	}

	public static JPAStudyDAO getInstance(){
		return instance;
	}
}
