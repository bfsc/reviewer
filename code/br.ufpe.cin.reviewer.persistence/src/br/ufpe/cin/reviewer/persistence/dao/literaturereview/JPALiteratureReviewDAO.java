package br.ufpe.cin.reviewer.persistence.dao.literaturereview;

import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.persistence.dao.JPADAO;

public class JPALiteratureReviewDAO extends JPADAO<LiteratureReview, Integer> implements ILiteratureReviewDAO {

	private static JPALiteratureReviewDAO instance = new JPALiteratureReviewDAO();
	
	private JPALiteratureReviewDAO() {
		super(LiteratureReview.class);
	}

	public static JPALiteratureReviewDAO getInstance(){
		return instance;
	}
	
}
