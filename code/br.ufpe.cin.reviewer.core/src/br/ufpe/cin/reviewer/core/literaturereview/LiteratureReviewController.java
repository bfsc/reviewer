package br.ufpe.cin.reviewer.core.literaturereview;

import javax.naming.directory.SearchResult;

import br.ufpe.cin.reviewer.core.ITransactionalController;
import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.persistence.dao.DAOFactory;
import br.ufpe.cin.reviewer.persistence.dao.literaturereview.ILiteratureReviewDAO;
import br.ufpe.cin.reviewer.persistence.exceptions.PersistenceException;

public class LiteratureReviewController implements ITransactionalController {

	private ILiteratureReviewDAO dao = (ILiteratureReviewDAO) DAOFactory.getInstance().getDAO(ILiteratureReviewDAO.class);
	
	public void createLiteratureReview(LiteratureReview literatureReview) {
		try {
			this.dao.create(literatureReview);
		} catch (PersistenceException e) {
			throw new CoreException("An error occurred truing to create an literature review.", e);
		}
	}
	
	public void createLiteratureReview(LiteratureReview literatureReview, SearchResult searchResult) {
		
	}
	
	public void associateSearchToLiteratureReview(LiteratureReview literatureReview, SearchResult searchResult) {
		
	}
	
//	public void defineLiteratureReviewProtocol(LiteratureReview literatureReview, Protocol protocol) {
//		
//	}
	
//	public static void main(String[] args) {
//		HSQLUtil.initDatabase();
//		
//		LiteratureReviewController controller = new LiteratureReviewController();
//		LiteratureReview literatureReview = new LiteratureReview();
//		literatureReview.setTitle("TITLE 1");
//		controller.createLiteratureReview(literatureReview);
//
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	
}
