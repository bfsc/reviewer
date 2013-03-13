package br.cin.ufpe.reviewer.search.provider.engineeringvillage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.LinkedList;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
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

    private static final String X_PATH_STUDY_LINK = "/*/*/a[title='Full-text']";

    private static final String X_PATH_NEXT_PAGE = "//a[@title='Go to next page']";
    
    private static final String X_PATH_STUDY_ABSTRACT_TEXT = "//td/p[8]";
    
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
                	System.out.println(searchUrl);
                    HtmlPage page = browser.getPage(searchUrl);
           
                    // Extracting studies data.
                    HtmlTextArea string_field = (HtmlTextArea) page.getFirstByXPath(X_PATH_TEXT_AREA);
                    HtmlInput input_search = (HtmlInput) page.getFirstByXPath(X_PATH_SEARCH_INPUT);

                    if (string_field != null) {
                    		string_field.setText(searchString);                    		
                    }
                    if (input_search != null) {                   		
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
                    
                    //List<?> studyTablesParagraph = search_result_page.getByXPath(X_PATH_STUDY_TITLE);
                    //List<?> studyTablesAbstract = search_result_page.getByXPath(X_PATH_STUDY_ABSTRACT);
                    //List<?> studyTablesLink = search_result_page.getByXPath(X_PATH_STUDY_LINK);
                    List<?> studyTables = search_result_page.getByXPath(X_PATH_STUDY_LIST);
                    
                    
                    System.out.println(search_result_page.getUrl());
                    for (int i = 0; i < studyTables .size(); i++) {
                            Study study = new Study();
                            

                            HtmlDivision Division = (HtmlDivision) studyTables.get(i);
                            System.out.println(Division.getCanonicalXPath() + X_PATH_STUDY_TITLE);
                            HtmlParagraph Paragraph = (HtmlParagraph)search_result_page.getFirstByXPath(Division.getCanonicalXPath() + X_PATH_STUDY_TITLE);
                            HtmlAnchor AbstractAnchor = (HtmlAnchor)search_result_page.getFirstByXPath(Division.getCanonicalXPath() + X_PATH_STUDY_ABSTRACT);
                            HtmlAnchor Link = (HtmlAnchor)search_result_page.getFirstByXPath(Division.getCanonicalXPath() + X_PATH_STUDY_LINK);
                            
                            
                            //HtmlAnchor AbstractAnchor = (HtmlAnchor)Division.getFirstByXPath(X_PATH_STUDY_ABSTRACT);
                            //HtmlAnchor Link = (HtmlAnchor)Division.getFirstByXPath(X_PATH_STUDY_LINK);
                            
                            
                            System.out.println(AbstractAnchor.asXml());
                            //taking the link page of Abstract
                            //HtmlParagraph Paragraph = (HtmlParagraph) studyTablesParagraph.get(i);
                            //HtmlAnchor AbstractAnchor = (HtmlAnchor) studyTablesAbstract.get(i);
                            String AbstractUrl = "http://www.engineeringvillage.com" + AbstractAnchor.getAttribute("href");
                            
                            //Acessing the abstract page
                            System.out.println(AbstractUrl);
                            HtmlPage AbstractPage = browser.getPage(AbstractUrl);
                            HtmlParagraph ParagraphAbstract = (HtmlParagraph) AbstractPage.getFirstByXPath(X_PATH_STUDY_ABSTRACT_TEXT);
                            
                            // Extracting study title abstract and URL
                            study.setTitle(Paragraph.asText());
                            System.out.println(ParagraphAbstract.asXml());
                            study.setAbstract(ParagraphAbstract.getTextContent());
                            if(Link != null)
                            	study.setUrl(Link.getAttribute("href"));
                        
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
       
        HtmlAnchor nextPageAnchor = (HtmlAnchor) page.getFirstByXPath(X_PATH_NEXT_PAGE);

        if (nextPageAnchor != null) {
        	String Url = "http://www.engineeringvillage.com" + nextPageAnchor.getAttribute("href");
        	toReturn = browser.getPage(Url);
        }
        
        return toReturn;
    }

	public String getKey() {
		return SEARCH_PROVIDER_KEY_ENGINEERING_VILLAGE;
	}
    
	 public static void main(String[] args) {
	         try{	
	                 SearchProvider searchProvider = new EngineeringVillageSearchProvider();
	                 SearchResult result = searchProvider.search("software \"engineering abstract\"");
	                 
	                 int count = 1;
	                 
	                 StringBuilder buffer = new StringBuilder();
	     			
	     			for (Study study : result.getStudies()) {
	     				buffer.append(count + ": " + study.getTitle() + "\r\n");
	             		buffer.append(study.getAbstract() + "\n");
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