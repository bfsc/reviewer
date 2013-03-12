package br.cin.ufpe.reviewer.search.provider.engineeringvillage;

import java.net.URLEncoder;
import java.util.List;
import java.util.LinkedList;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

public class EngineeringVillageSearchProvider implements SearchProvider {

	private static final String SEARCH_PROVIDER_KEY_ENGINEERING_VILLAGE = "ENGINEERING_VILLAGE";

    private static final String EXPERT_SEARCH_LINK = "http://www.engineeringvillage.com/controller/servlet/Controller?CID=expertSearch";

    private static final String X_PATH_TEXT_AREA = "//textarea[@name='searchWord1' and @id='srchWrd1']";
    
    private static final String X_PATH_SEARCH_INPUT = "//input[@type='submit' and @value='Search']";

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
	            result.getStudies().addAll(extractStudiesData(browser, searchUrl, searchString));
        	} catch (Exception e) {
        		throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
            }
           
            return  result;
	}
   
    private List<Study> extractStudiesData(WebClient browser, String searchUrl, String searchString) {
            List<Study> toReturn = new LinkedList<Study>();
           
            try {
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
                    
                    System.out.println(search_result_page.getUrl());
                    
                    /*
                    for (int i = 0; i < studyTablesAnchors.size(); i++) {
                            Study study = new Study();
                           
                            HtmlAnchor anchor = (HtmlAnchor) studyTablesAnchors.get(i);
                           
                            // Extracting study title and URL
                            study.setTitle(anchor.getTextContent().trim());
                            study.setUrl(anchor.getHrefAttribute().trim());
                        
                            toReturn.add(study);
                    }
                   
                    // Extracting the URL of next page.
                    String nextPageUrl = extractNextPageUrl(page);
                   
                    if (nextPageUrl != null) {
                            toReturn.addAll(extractStudiesData(browser, nextPageUrl));
                    }
                    */
            } catch (Exception e) {
                    //TRATAR EXCECAO
                        e.printStackTrace();
                }
               
                return toReturn;
        }

	public String getKey() {
		return SEARCH_PROVIDER_KEY_ENGINEERING_VILLAGE;
	}
    
	 public static void main(String[] args) {
	         try{	
	                 SearchProvider searchProvider = new EngineeringVillageSearchProvider();
	                 SearchResult result = searchProvider.search("Software");
	         }
	         catch (Exception e) {
	                 e.printStackTrace();
	         }
	     }

}
