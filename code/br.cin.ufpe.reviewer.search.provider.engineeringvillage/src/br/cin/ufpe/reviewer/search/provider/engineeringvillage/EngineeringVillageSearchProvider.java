package br.cin.ufpe.reviewer.search.provider.engineeringvillage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

public class EngineeringVillageSearchProvider implements SearchProvider {

	private static final String SEARCH_PROVIDER_KEY_ENGINEERING_VILLAGE = "ENGINEERING_VILLAGE";

    private static final String EXPERT_SEARCH_LINK = "http://www.engineeringvillage.com/controller/servlet/Controller?CID=expertSearch";

    private static final String X_PATH_TEXT_AREA = "//textarea[@name='searchWord1' and @id='srchWrd1']";

    private static final String X_PATH_SEARCH_INPUT = "//input[@type='submit' and @value='Search']";

    private static final String X_PATH_STUDY_TITLE = "/*/p[@class='resulttitle']";

    private static final String X_PATH_STUDY_ABSTRACT = "/*/*/a[@class='externallink' and @title='Abstract']";

    private static final String X_PATH_STUDY_LINK = "/*/*/a[@title='Full-text']";

    private static final String X_PATH_NEXT_PAGE = "//a[@title='Go to next page']";
    
    private static final String X_PATH_STUDY_ABSTRACT_TEXT = "//td[@style='*padding-top:3px']//p";
    
    private static final String X_PATH_STUDY_LIST = "//div[@class='result' or @class='result odd']";

	public SearchResult search(String searchString) throws SearchProviderException {
    	SearchResult result = new SearchResult();
        
        try {
	            // Create the web browser
	            WebClient browser = new WebClient();
	           
	            // Throwing an exception if the search string is invalid.
	            if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
	                    throw new RuntimeException("Invalid search string");
	            }
	           
	            // Assemble search URL
	            String searchUrl = EXPERT_SEARCH_LINK;
	           
	            // Extract studies data
                HtmlPage page = browser.getPage(searchUrl);
           
                // Extracting studies data.
                HtmlTextArea string_field = (HtmlTextArea) page.getFirstByXPath(X_PATH_TEXT_AREA);
                HtmlInput input_search = (HtmlInput) page.getFirstByXPath(X_PATH_SEARCH_INPUT);

                if (string_field != null) {
                    string_field.setText(searchString);                    		
                }
                HtmlPage search_result_page = input_search.click(); 
                    
	            result.getStudies().addAll(extractStudiesData(browser, search_result_page));
        	} catch (Exception e) {
        		throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
            }
           
            return  result;
	}
   
    private List<Study> extractStudiesData(WebClient browser, HtmlPage search_result_page) {
            List<Study> toReturn = new LinkedList<Study>();
           
            try {
                    //taking the studys tags
                    List<?> studyTables = search_result_page.getByXPath(X_PATH_STUDY_LIST);
        			System.out.print(search_result_page.getUrl());	
                    
                    //taking the studys from the tags
                    for (int i = 0; i < studyTables .size(); i++) {
                            Study study = new Study();
                            String AbstractUrl = null;
                            HtmlPage AbstractPage = null;
                            boolean sair = false;
                            
                            //taking the tags of each part of the studys
                            HtmlDivision Division = (HtmlDivision) studyTables.get(i);
                            HtmlParagraph Paragraph = (HtmlParagraph)search_result_page.getFirstByXPath(Division.getCanonicalXPath() + X_PATH_STUDY_TITLE);
                            HtmlAnchor AbstractAnchor = (HtmlAnchor)search_result_page.getFirstByXPath(Division.getCanonicalXPath() + X_PATH_STUDY_ABSTRACT);
                            HtmlAnchor Link = (HtmlAnchor)search_result_page.getFirstByXPath(Division.getCanonicalXPath() + X_PATH_STUDY_LINK);
                            
                            //taking the link page of Abstract
                            if(AbstractAnchor != null)
                            	AbstractUrl = "http://www.engineeringvillage.com" + AbstractAnchor.getAttribute("href");
                            
                            //Acessing the abstract page
                            if(AbstractUrl != null)
                            	AbstractPage = browser.getPage(AbstractUrl);
                            HtmlParagraph ParagraphAbstract = null;
                            if(AbstractPage != null){
	                            List<?> studyTablesParagraph = AbstractPage.getByXPath(X_PATH_STUDY_ABSTRACT_TEXT);
	                            for(int j = 0; j < studyTablesParagraph .size() && sair != true; j++){
	                            	HtmlParagraph verifica = (HtmlParagraph)studyTablesParagraph.get(j);
	                            	if(verifica.getAttribute("class").equalsIgnoreCase("abstracttext sectionstart")){
	                            		sair = true;
	                            		ParagraphAbstract = (HtmlParagraph)studyTablesParagraph.get(j+1);
	                            	}
	                            }
                            }
                            
                            // Extracting study title abstract and URL
                            if(Paragraph != null){
                            	study.setTitle(Paragraph.asText());
                            }
                            if(ParagraphAbstract != null){
                            	study.setAbstract(ParagraphAbstract.getTextContent());
                            }
                            if(Link != null){
                            	int index1 = Link.getAttribute("href").indexOf("/controller");
                            	int index2 = Link.getAttribute("href").indexOf("','newwindow'");
                            	String Url = Link.getAttribute("href").substring(index1, index2);
                            	study.setUrl( "http://www.engineeringvillage.com" + Url);
                            }
                            toReturn.add(study);
                    }
                   
                    // Extracting the URL of next page.
                    HtmlPage next_result_page = extractNextPageUrl(browser, search_result_page);
                   
                    if (next_result_page != null) {
                            toReturn.addAll(extractStudiesData(browser, next_result_page));
                    }
                    
            } catch (Exception e) {
                    //TRATAR EXCECAO
                    e.printStackTrace();
                }
               
                return toReturn;
        }
 
    private HtmlPage extractNextPageUrl(WebClient browser, HtmlPage page) throws IOException {
    	HtmlPage toReturn = null;
    	String Url;
       
        HtmlAnchor nextPageAnchor = (HtmlAnchor) page.getFirstByXPath(X_PATH_NEXT_PAGE);

        if (nextPageAnchor != null) {
        	if(nextPageAnchor.getAttribute("href").equalsIgnoreCase("javascript:window.alert('Only the first 4000 records can be viewed.')")){
        		toReturn = null;
        	}
        	else{
        		Url = "http://www.engineeringvillage.com" + nextPageAnchor.getAttribute("href");
        		toReturn = browser.getPage(Url);
        	}
        }
        
        return toReturn;
    }

	public String getKey() {
		return SEARCH_PROVIDER_KEY_ENGINEERING_VILLAGE;
	}
    
	 public static void main(String[] args) {
	         try{	
	                 SearchProvider searchProvider = new EngineeringVillageSearchProvider();
	                 SearchResult result = searchProvider.search("systematic mapping studies");
	                 
	                 int count = 1;
	                 
	                 StringBuilder buffer = new StringBuilder();
	     			
	     			for (Study study : result.getStudies()) {
	     				buffer.append(count + ": " + study.getTitle() + "\r\n");
	             		buffer.append(study.getAbstract() + "\r\n");
	     				buffer.append(study.getUrl() + "\r\n\r\n");
	     				count++;
	     			}
	     			
	     			FileWriter writer = new FileWriter("C:/Users/Arthur/Desktop/search.result.txt");
	     			writer.write(buffer.toString());
	     			writer.flush();
	     			writer.close();
	     			
	         }
	         catch (Exception e) {
	                 e.printStackTrace();
	         }
	     }

}
//\"software engineering\" compute
//software \"engineering abstract\"