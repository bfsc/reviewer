package br.ufpe.cin.reviewer.persistence.dao;

import br.ufpe.cin.reviewer.persistence.dao.literaturereview.ILiteratureReviewDAO;
import br.ufpe.cin.reviewer.persistence.dao.literaturereview.JPALiteratureReviewDAO;

public class DAOFactory {

	private static DAOFactory instance = new DAOFactory();
	
	private DAOFactory() {
		
	}
	
	public static DAOFactory getInstance() {
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public IDAO getDAO(Class<? extends IDAO> clazz) {
		IDAO dao = null;
		
		if (clazz.equals(ILiteratureReviewDAO.class)) {
			dao = JPALiteratureReviewDAO.getInstance();
		}
		
		return dao;
	}
	
}
