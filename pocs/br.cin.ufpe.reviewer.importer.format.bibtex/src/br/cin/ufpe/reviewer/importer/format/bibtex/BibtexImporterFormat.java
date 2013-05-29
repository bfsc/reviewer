package br.cin.ufpe.reviewer.importer.format.bibtex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bibtex.dom.BibtexAbstractEntry;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;
import br.cin.ufpe.reviewer.importer.format.spi.ImporterFormat;
import br.cin.ufpe.reviewer.model.study.Author;
import br.cin.ufpe.reviewer.model.study.PrimaryStudy;

public class BibtexImporterFormat implements ImporterFormat {

	@Override
	public List<PrimaryStudy> importStudiesFromFile(String path) throws FileNotFoundException, IOException {
		try {
			BibtexParser parser = new BibtexParser(true);
			BibtexFile bibFile = new BibtexFile();
			parser.parse(bibFile, new BufferedReader(new FileReader(path)));
			
			List entries = bibFile.getEntries();
			
			BibtexAbstractEntry entry = null;
	        PrimaryStudy item = null;
	        ArrayList<PrimaryStudy> studies = new ArrayList<PrimaryStudy>();
	        
	        Iterator<BibtexAbstractEntry> it = entries.iterator();
	        while (it.hasNext()){
	            entry = (BibtexAbstractEntry)it.next();
	            if (entry instanceof BibtexEntry){
	                item = new PrimaryStudy();
	                item.setAuthors(parseAuthors(((BibtexEntry)entry).getFieldValue("author").toString()));
	                item.setTitle(removeCurlyBrackets(((BibtexEntry)entry).getFieldValue("title").toString()));
	                item.setYear(Integer.parseInt(((BibtexEntry)entry).getFieldValue("year").toString()));
	                
	                if(((BibtexEntry)entry).getFieldValue("abstract") != null) {
	                	item.setPaperAbstract(removeCurlyBrackets(((BibtexEntry)entry).getFieldValue("abstract").toString()));
	                } else {
	                	item.setPaperAbstract("");
	                }
	                
	                if( ((BibtexEntry)entry).getFieldValue("url") != null ) {
	                	item.setUrl(removeCurlyBrackets(((BibtexEntry)entry).getFieldValue("url").toString()));
	                } else {
	                	item.setUrl("");
	                }
	                item.setEvaluated(false);
	                
	                if(((BibtexEntry)entry).getFieldValue("issn") != null) {
	                	item.setIssn(removeCurlyBrackets(((BibtexEntry)entry).getFieldValue("issn").toString()));
	                } else {
	                	item.setIssn("");
	                }
	                
	                studies.add(item);
	            }
	        }
	        
	        return studies;
		} catch (ParseException e) {
			
		}
		return null;
	}
	
	private List<Author> parseAuthors(String authors){
        List<Author> list = new ArrayList<Author>();
        Author author;
        String[] names = authors.trim().replace("{", "").replace("}", "").split(" and ");
        for (int i=0; i < names.length; i++){
            author = new Author();
            author.setName(names[i]);
            list.add(author);
        }
        return list;
    }
    
    private String removeCurlyBrackets(String title){
        return title.trim().replace("{", "").replace("}", "");
    }

}
