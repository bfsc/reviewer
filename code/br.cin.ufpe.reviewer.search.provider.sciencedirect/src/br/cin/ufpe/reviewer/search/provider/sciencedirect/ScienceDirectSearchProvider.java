package br.cin.ufpe.reviewer.search.provider.sciencedirect;
 
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
 
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
 
import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;
 
public class ScienceDirectSearchProvider implements SearchProvider {
       
        private static final String DOMAIN_DL_SCIENCE_DIRECT = "http://www.sciencedirect.com/";
       
        private static final String URL_ENCODE_UTF_8 = "UTF-8";
        private static final String URL_ENCODE_ISO_8859_1 = "ISO-8859-1";
       
        private static final String SEARCH_URL_SCIENCE_DIRECT_1 = "http://www.sciencedirect.com/science?_ob=QuickSearchURL&_method=submitForm&_acct=C000228598&_origin=home&_zone=qSearch&md5=61ce8901b141d527683913a240486ac4&qs_all=";
        private static final String SEARCH_URL_SCIENCE_DIRECT_2 = "&qs_author=&qs_title=&qs_vol=&qs_issue=&qs_pages=&sdSearch=Search";
       
        private static final String XPATH_STUDY_TITLE_AND_URL = "//a[@class='cLink']";
       
        private static final String X_PATH_NEXT_PAGE = "//input[@name='topNext']";
       
        private static final String NEXT_PAGE_ANCHOR_TEXT = "Next &gt;";
       
 
       
       
        public List<Study> search(String searchString) throws SearchProviderException {
                List<Study> toReturn = new LinkedList<Study>();
               
                try {
                        // Create the web browser
                        WebClient browser = new WebClient();
                       
                        // Throwing an exception if the search string is invalid.
                        if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
                                throw new RuntimeException("Invalid search string");
                        }
                       
                        // Assemble search URL
                        String searchUrl = assembleSearchUrl(searchString);
                       
                        // Extract studies data
                        toReturn.addAll(extractStudiesData(browser, searchUrl));
                } catch (Exception e) {
                        throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
                }
               
                return  toReturn;
               
               
 
        }
       
        private String assembleSearchUrl(String searchString) {
                String query = "";
               
                try {
                        query = URLEncoder.encode(searchString, URL_ENCODE_ISO_8859_1).toString();
                } catch (UnsupportedEncodingException e) {
                        try {
                                query = URLEncoder.encode(searchString, URL_ENCODE_UTF_8).toString();
                        } catch (UnsupportedEncodingException e2) {
                                throw new RuntimeException("An error occurred tryinf to encode the url.", e2);
                        }
                }
               
                return SEARCH_URL_SCIENCE_DIRECT_1 + query + SEARCH_URL_SCIENCE_DIRECT_2;
        }
       
        private List<Study> extractStudiesData(WebClient browser, String searchUrl) {
                List<Study> toReturn = new LinkedList<Study>();
               
                try {
                        HtmlPage page = browser.getPage(searchUrl);
               
                        // Extracting studies data.
                        List<?> studyTablesAnchors = page.getByXPath(XPATH_STUDY_TITLE_AND_URL);
                        for (int i = 0; i < studyTablesAnchors.size(); i++) {
                                Study study = new Study();
                               
                                HtmlAnchor anchor = (HtmlAnchor) studyTablesAnchors.get(i);
                               
                                // Extracting study title and URL
                                study.setTitle(anchor.getTextContent().trim());
                                study.setUrl(DOMAIN_DL_SCIENCE_DIRECT + anchor.getHrefAttribute().trim());
                            
                                toReturn.add(study);
                        }
                       
                        // Extracting the URL of next page.
                        String nextPageUrl = extractNextPageUrl(page);
                       
                        if (nextPageUrl != null) {
                                toReturn.addAll(extractStudiesData(browser, nextPageUrl));
                        }
                } catch (Exception e) {
                        //TRATAR EXCECAO
                        e.printStackTrace();
                }
               
                return toReturn;
        }
 
        private String extractNextPageUrl(HtmlPage page) throws IOException {
                String toReturn = null;
               
                List<?> nextPageTags = page.getByXPath(X_PATH_NEXT_PAGE);
                for (Object object : nextPageTags) {
            			System.out.println("chegou aqui");
                		HtmlInput nextPageInput = (HtmlInput)object;
                		HtmlPage nextPageString = (HtmlPage) nextPageInput.mouseDown();
                		System.out.println(nextPageString);
                       
//                        if (nextPageString.trim().equalsIgnoreCase(NEXT_PAGE_ANCHOR_TEXT)) {
//                                toReturn = DOMAIN_DL_SCIENCE_DIRECT + nextPageInput.getHrefAttribute().trim();
//                        }
                }
               
                return toReturn;
        }
       
       
 
        public static void main(String[] args) {
                try{
                        SearchProvider searchProvider = new ScienceDirectSearchProvider();
                        List<Study> studies = searchProvider.search("\"Software Engineering\"");
                        
                        StringBuilder buffer = new StringBuilder();
            			
            			for (Study study : studies) {
            				buffer.append(study.getTitle() + "\r\n");
//            				buffer.append(study.getAbstract() + "\n");
            				buffer.append(study.getUrl() + "\r\n\r\n");
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