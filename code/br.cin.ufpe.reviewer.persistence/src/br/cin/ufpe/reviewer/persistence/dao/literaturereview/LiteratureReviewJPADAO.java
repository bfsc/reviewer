package br.cin.ufpe.reviewer.persistence.dao.literaturereview;

import javax.persistence.EntityManager;

import br.cin.ufpe.reviewer.model.literaturereview.LiteratureReview;
import br.cin.ufpe.reviewer.persistence.dao.JPADAO;

public class LiteratureReviewJPADAO extends JPADAO<LiteratureReview, Long> implements ILiteratureReviewDAO {

	public LiteratureReviewJPADAO() {
		super(LiteratureReview.class);
	}

	public LiteratureReviewJPADAO(EntityManager entityManager) {
		super(entityManager, LiteratureReview.class);
	}
	
	

}
