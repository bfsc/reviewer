package br.cin.ufpe.reviewer.search.provider.springerlink;

import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;


public class SpringerLinkSearchProvider implements SearchProvider {
	
	private static final String SEARCH_PROVIDER_KEY_SPRINGER_LINK = "SPRINGER_LINK";

	private static final String DOMAIN_DL_SPRINGER_LINK = "http://link.springer.com";
	private static final String URL_DL_SPRINGER_LINK_SEARCH = "http://link.springer.com/search?query=";
	private static final String URL_DL_SPRINGER_LINK_ARTICLE_SEARCH = "&facet-content-type=%22Article%22";

	private static final String URL_ENCODE_UTF_8 = "UTF-8";
	private static final String URL_ENCODE_ISO_8859_1 = "ISO-8859-1";
	
	private static final String XPATH_STUDY_TITLE_AND_URL = "//a[@class='title']";
	private static final String XPATH_STUDY_ABSTRACT = "//p[@class='snippet']";
	private static final String STUDY_ABSTRACT_END_MARKER = "...";
	
	private int NEXT_PAGE_COUNTER = 2;
	private String SEARCH_STRING = "";
	
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
			String searchUrl = assembleSearchUrl(searchString);
			
			
			// Extract studies data
			result.getStudies().addAll(extractStudiesData(browser, searchUrl));
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return result;
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
		SEARCH_STRING = query;
		
		return URL_DL_SPRINGER_LINK_SEARCH + query + URL_DL_SPRINGER_LINK_ARTICLE_SEARCH;
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
				study.setTitle(anchor.getTextContent().trim() + "\n");
				study.setUrl(DOMAIN_DL_SPRINGER_LINK + anchor.getHrefAttribute().trim() + "\n");
				
				// Extracting study abstract.
				List<?> studyPAbstracts = page.getByXPath(XPATH_STUDY_ABSTRACT);
				//System.out.println(studyPAbstracts);
				if (studyPAbstracts.size() >= i) {
					HtmlParagraph p = (HtmlParagraph) studyPAbstracts.get(i);
					
					// Extracting study abstract p content.
					String Content = p.getTextContent().trim();
					
					// Removing non-abstract informations from p content.
					int abstractEndMarker = Content.indexOf(STUDY_ABSTRACT_END_MARKER);
					if (abstractEndMarker >= 0) {
						study.setAbstract(Content.substring(0, Content.indexOf(STUDY_ABSTRACT_END_MARKER) + STUDY_ABSTRACT_END_MARKER.length()));
					} else {
						study.setAbstract("");
					}
				}
				
				toReturn.add(study);
			}
			
			// Extracting the URL of next page.
			String nextPageUrl = extractNextPageUrl();
			
			if (nextPageUrl != null) {
				toReturn.addAll(extractStudiesData(browser, nextPageUrl));
			}
		} catch (Exception e) {
			//TRATAR EXCECAO
			e.printStackTrace();
		}
		
		return toReturn;
	}

	private String extractNextPageUrl() {
		String toReturn = null;
		if(NEXT_PAGE_COUNTER < 24){
			toReturn = DOMAIN_DL_SPRINGER_LINK + "/search/page/" + NEXT_PAGE_COUNTER + "?query=" + SEARCH_STRING + URL_DL_SPRINGER_LINK_ARTICLE_SEARCH;
			NEXT_PAGE_COUNTER++;
			//System.out.println(toReturn);
			}
		return toReturn;
	}
	public String getKey() {
		return SEARCH_PROVIDER_KEY_SPRINGER_LINK;
	}

	
	// TODO METODO DE TESTE
	public static void main(String[] args) {
		try {
			SearchProvider searchProvider = new SpringerLinkSearchProvider();
			
//			List<Study> studies = searchProvider.search("\"systematic mapping study\" AND \"software engineering\"");
			SearchResult result = searchProvider.search("\"Software Engineering\"");
			
			int count = 1;
            
            StringBuilder buffer = new StringBuilder();
			
			for (Study study : result.getStudies()) {
				buffer.append(count + ": " + study.getTitle() + "\r\n");
        		buffer.append(study.getAbstract() + "\n");
				buffer.append(study.getUrl() + "\r\n\r\n");
				count++;
			}
			
			FileWriter writer = new FileWriter("C:/Users/Pedro/Desktop/search.result.txt");
			writer.write(buffer.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
