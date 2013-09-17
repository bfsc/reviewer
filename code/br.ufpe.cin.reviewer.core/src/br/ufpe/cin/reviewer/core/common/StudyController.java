package br.ufpe.cin.reviewer.core.common;

import br.ufpe.cin.reviewer.core.ITransactionalController;
import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.model.study.Study;
import br.ufpe.cin.reviewer.persistence.dao.common.IStudyDAO;
import br.ufpe.cin.reviewer.persistence.dao.common.JPAStudyDAO;
import br.ufpe.cin.reviewer.persistence.exceptions.PersistenceException;

public class StudyController  implements ITransactionalController {
	
	private IStudyDAO dao = JPAStudyDAO.getInstance();
	
	public void updateStudy(Study study){
		try {
			dao.update(study);
		} catch (PersistenceException e) {
			throw new CoreException("An error occurred trying to update a study.", e);
		}
	}
}
