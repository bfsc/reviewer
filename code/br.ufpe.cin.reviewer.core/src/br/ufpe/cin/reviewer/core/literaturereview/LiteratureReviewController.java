package br.ufpe.cin.reviewer.core.literaturereview;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import br.ufpe.cin.reviewer.core.ITransactionalController;
import br.ufpe.cin.reviewer.core.exceptions.CoreException;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.study.Study;
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
	
	public void exportSudies(LiteratureReview literatureReview, String filePath) {
		try {
			InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream("export.studies.template.xls");
			Workbook workbook = WorkbookFactory.create(templateInputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			// TODO DEFINIR NOME DA SHEET
			CellStyle templateCellStyle = sheet.getRow(1).getCell(0).getCellStyle();
			
			//TODO REFAZER
			List<Study> studies = literatureReview.getSearches().get(1).getStudies();
			
			for (int i = 0; i < studies.size(); i++) {
				Study study = studies.get(i);
				
				Row row = sheet.getRow(i + 1);
				row = (row == null) ? sheet.createRow(i + 1) : row;
				
				// Filling code cell
				Cell codeCell = row.getCell(0);
				codeCell = codeCell == null ? row.createCell(0) : codeCell;
				codeCell.setCellValue(study.getCode());
				codeCell.setCellStyle(templateCellStyle);
				
				// Filling status cell
				Cell statusCell = row.getCell(1);
				statusCell = statusCell == null ? row.createCell(1) : statusCell;
				statusCell.setCellValue(study.getStatus().name());
				statusCell.setCellStyle(templateCellStyle);
				
				// Filling source cell
				Cell sourceCell = row.getCell(2);
				sourceCell = sourceCell == null ? row.createCell(2) : sourceCell;
				sourceCell.setCellValue(study.getSource());
				sourceCell.setCellStyle(templateCellStyle);
				
				// Filling title cell
				Cell titleCell = row.getCell(3);
				titleCell = titleCell == null ? row.createCell(3) : titleCell;
				titleCell.setCellValue(study.getTitle());
				titleCell.setCellStyle(templateCellStyle);
				
				// Filling authors cell
				Cell authorsCell = row.getCell(4);
				authorsCell = authorsCell == null ? row.createCell(4) : authorsCell;
				
				String authors = "";
				if(!study.getAuthors().isEmpty()) {
					for (int j = 0; j < (study.getAuthors().size() - 1); j++) {
						authors += study.getAuthors().get(j) + ", ";
					}
					
					authors += study.getAuthors().get(study.getAuthors().size() - 1);
				}
				
				authorsCell.setCellValue(authors);
				authorsCell.setCellStyle(templateCellStyle);
				
				// Filling abstract cell
				Cell abstractCell = row.getCell(5);
				abstractCell = abstractCell == null ? row.createCell(5) : abstractCell;
				abstractCell.setCellValue(study.getAbstract());
				abstractCell.setCellStyle(templateCellStyle);
				
				// Filling url cell
				Cell urlCell = row.getCell(6);
				urlCell = urlCell == null ? row.createCell(6) : urlCell;
				urlCell.setCellValue(study.getUrl());
				urlCell.setCellStyle(templateCellStyle);
			}
			
			OutputStream exportOutputStream = new FileOutputStream(filePath);
			workbook.write(exportOutputStream);
			
			exportOutputStream.close();
			templateInputStream.close();
		} catch (Exception e) {
			// TODO TRATAR EXCECAO
			e.printStackTrace();
		}
	}
	
	public void updateLiteratureReview(LiteratureReview lr){
		try {
			dao.update(lr);
		} catch (PersistenceException e) {
			throw new CoreException("An error occurred trying to update a literature review.", e);
		}
	}
	
}
