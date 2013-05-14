package br.ufpe.cin.reviewer.core.literaturereview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import br.ufpe.cin.reviewer.core.ITransactionalController;
import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource;
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
	
	public void deleteLiteratureReview(LiteratureReview literatureReview) {
		try {
			this.dao.delete(literatureReview);
		} catch (PersistenceException e) {
			throw new CoreException("An error occurred trying to delete an literature review.", e);
		}
	}
	
	public List<LiteratureReview> findAllLiteratureReview() {
		List<LiteratureReview> toReturn = new LinkedList<LiteratureReview>();
		
		try {
			toReturn = this.dao.retrieveAll();
		} catch (PersistenceException e) {
			throw new CoreException("An error occurred trying to fin all literature reviews", e);
		}
		
		return toReturn;
	}
	
	public void exportLiteratureReview(LiteratureReview literatureReview, String filePath, boolean overwrite) {
		FileOutputStream fos = null;
		
		try {
			// File validation
			if (filePath == null || filePath.trim().isEmpty()) {
				throw new CoreException("Invalid file path: " + filePath);
			}
			
			File file = new File(filePath);
			if (!overwrite && file.exists()) {
				throw new CoreException("The file already exists: " + filePath); 
			}
			
			// TODO ACRESCENTAR CODIGO PARA TESTAR PERMISSAO DE ESCRITA
//			try {
//				System.getSecurityManager().checkWrite(file.getParentFile().getAbsolutePath());
//			} catch (SecurityException e) {
//				throw new CoreException("You have no permission to write in: " + filePath);
//			}

			// Defining xml structure
			Document document = new Document();
			
			Element rootElement = new Element("reviewer").setAttribute("version", "0.0.1");
			document.setRootElement(rootElement);
			
			Element literatureReviewElement = new Element("literature-review");
			rootElement.addContent(literatureReviewElement);
			
			Element literatureReviewTitleElement = new Element("title");
			literatureReviewTitleElement.addContent(literatureReview.getTitle());
			literatureReviewElement.addContent(literatureReviewTitleElement);

			// Defining sources
			if (!literatureReview.getSources().isEmpty()) {
				Element literatureReviewSourcesElement = new Element("sources");
				literatureReviewElement.addContent(literatureReviewSourcesElement);
				
				for (LiteratureReviewSource source : literatureReview.getSources()) {
					Element literatureReviewSourceElement = new Element("source");
					literatureReviewSourcesElement.addContent(literatureReviewSourceElement);
					
					Element literatureReviewSourceNameElement = new Element("name");
					literatureReviewSourceNameElement.addContent(source.getName());
					literatureReviewSourceElement.addContent(literatureReviewSourceNameElement);
					
					Element literatureReviewSourceTypeElement = new Element("type");
					literatureReviewSourceTypeElement.addContent(source.getType().toString());
					literatureReviewSourceElement.addContent(literatureReviewSourceTypeElement);
					
					Element literatureReviewSourceTotalFoundElement = new Element("total-found");
					literatureReviewSourceTotalFoundElement.addContent(String.valueOf(source.getTotalFound()));
					literatureReviewSourceElement.addContent(literatureReviewSourceTotalFoundElement);
					
					Element literatureReviewSourceTotalFetchedElement = new Element("total-fetched");
					literatureReviewSourceTotalFetchedElement.addContent(String.valueOf(source.getTotalFetched()));
					literatureReviewSourceElement.addContent(literatureReviewSourceTotalFetchedElement);
				}
			}
			
			// Defining studies
			if (!literatureReview.getStudies().isEmpty()) {
				Element literatureReviewStudiesElement = new Element("studies");
				literatureReviewElement.addContent(literatureReviewStudiesElement);
				
				for (Study study : literatureReview.getStudies()) {
					Element literatureReviewStudyElement = new Element("study");
					literatureReviewStudiesElement.addContent(literatureReviewStudyElement);
					
					Element literatureReviewStudyCodeElement = new Element("code");
					literatureReviewStudyCodeElement.addContent(study.getCode());
					literatureReviewStudyElement.addContent(literatureReviewStudyCodeElement);
					
					Element literatureReviewStudyTitleElement = new Element("title");
					literatureReviewStudyTitleElement.addContent(study.getTitle());
					literatureReviewStudyElement.addContent(literatureReviewStudyTitleElement);
					
					Element literatureReviewStudyStatusElement = new Element("status");
					literatureReviewStudyStatusElement.addContent(study.getStatus().toString());
					literatureReviewStudyElement.addContent(literatureReviewStudyStatusElement);
					
					Element literatureReviewStudySourceElement = new Element("source");
					literatureReviewStudySourceElement.addContent(study.getSource());
					literatureReviewStudyElement.addContent(literatureReviewStudySourceElement);
					
					Element literatureReviewStudyAbstractElement = new Element("abstract");
					literatureReviewStudyAbstractElement.addContent(study.getAbstract());
					literatureReviewStudyElement.addContent(literatureReviewStudyAbstractElement);
					
					Element literatureReviewStudyYearElement = new Element("year");
					literatureReviewStudyYearElement.addContent(study.getYear());
					literatureReviewStudyElement.addContent(literatureReviewStudyYearElement);
					
					Element literatureReviewStudyUrlElement = new Element("url");
					literatureReviewStudyUrlElement.addContent(study.getUrl());
					literatureReviewStudyElement.addContent(literatureReviewStudyUrlElement);
					
					// Defining authors
					if (!study.getAuthors().isEmpty()) {
						Element literatureReviewStudyAuthorsElement = new Element("authors");
						literatureReviewStudyElement.addContent(literatureReviewStudyAuthorsElement);
						
						for (String author : study.getAuthors()) {
							Element literatureReviewStudyAuthorElement = new Element("author");
							literatureReviewStudyAuthorElement.addContent(author);
							literatureReviewStudyAuthorsElement.addContent(literatureReviewStudyAuthorElement);
						}
					}
					
					// Defining institutions
					if (!study.getInstitutions().isEmpty()) {
						Element literatureReviewStudyInstitutionsElement = new Element("institutions");
						literatureReviewStudyElement.addContent(literatureReviewStudyInstitutionsElement);
						
						for (String institution : study.getInstitutions()) {
							Element literatureReviewStudyInstitutionElement = new Element("institution");
							literatureReviewStudyInstitutionElement.addContent(institution);
							literatureReviewStudyInstitutionsElement.addContent(literatureReviewStudyInstitutionElement);
						}
					}
					
					// Defining countries
					if (!study.getCountries().isEmpty()) {
						Element literatureReviewStudyCountriesElement = new Element("countries");
						literatureReviewStudyElement.addContent(literatureReviewStudyCountriesElement);
						
						for (String country : study.getCountries()) {
							Element literatureReviewStudyCountryElement = new Element("country");
							literatureReviewStudyCountryElement.addContent(country);
							literatureReviewStudyCountriesElement.addContent(literatureReviewStudyCountryElement);
						}
					}
				}
			}
		
			fos = new FileOutputStream(file);
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(document, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new CoreException("Error trying to close the xml exported file: " + filePath);
				}
			}
		}
	}
	
}
