package br.cin.ufpe.reviewer.persistence.dao.literaturereview;

import br.cin.ufpe.reviewer.model.literaturereview.LiteratureReview;
import br.cin.ufpe.reviewer.persistence.dao.JPADAO;

public class JPALiteratureReviewDAO extends JPADAO<LiteratureReview, Integer> implements ILiteratureReviewDAO {

	public JPALiteratureReviewDAO() {
		super(LiteratureReview.class);
	}

}
