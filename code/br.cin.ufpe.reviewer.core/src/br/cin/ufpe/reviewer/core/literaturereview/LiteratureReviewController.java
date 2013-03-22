package br.cin.ufpe.reviewer.core.literaturereview;

import br.cin.ufpe.reviewer.core.ITransactionalController;
import br.cin.ufpe.reviewer.core.exceptions.CoreException;
import br.cin.ufpe.reviewer.core.search.SearchResult;
import br.cin.ufpe.reviewer.model.literaturereview.LiteratureReview;
import br.cin.ufpe.reviewer.model.literaturereview.Protocol;
import br.cin.ufpe.reviewer.persistence.dao.literaturereview.ILiteratureReviewDAO;
import br.cin.ufpe.reviewer.persistence.dao.literaturereview.JPALiteratureReviewDAO;
import br.cin.ufpe.reviewer.persistence.exceptions.PersistenceException;
import br.cin.ufpe.reviewer.persistence.util.HSQLUtil;

public class LiteratureReviewController implements ITransactionalController {

	private ILiteratureReviewDAO dao = new JPALiteratureReviewDAO();
	
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
	
	public void defineLiteratureReviewProtocol(LiteratureReview literatureReview, Protocol protocol) {
		
	}
	
	public static void main(String[] args) {
		HSQLUtil.initDatabase();
		
		LiteratureReviewController controller = new LiteratureReviewController();
		LiteratureReview literatureReview = new LiteratureReview();
		literatureReview.setTitle("TITLE 1");
		controller.createLiteratureReview(literatureReview);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
