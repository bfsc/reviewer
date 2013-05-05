package br.ufpe.cin.reviewer.core.literaturereview;

import br.ufpe.cin.reviewer.core.ITransactionalController;
import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.persistence.dao.DAOFactory;
import br.ufpe.cin.reviewer.persistence.dao.literaturereview.ILiteratureReviewDAO;
import br.ufpe.cin.reviewer.persistence.exceptions.PersistenceException;

public class LiteratureReviewController implements ITransactionalController {

	private ILiteratureReviewDAO dao = (ILiteratureReviewDAO) DAOFactory.getInstance().getDAO(ILiteratureReviewDAO.class);

	public LiteratureReviewController(){

	}
	
	public void createLiteratureReview(LiteratureReview literatureReview) {
		try {
			this.dao.create(literatureReview);
		} catch (PersistenceException e) {
			throw new CoreException("An error occurred trying to create an literature review.", e);
		}
	}
	
}
