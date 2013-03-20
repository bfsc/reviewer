package br.cin.ufpe.reviewer.search.provider.acm;

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
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AcmSearchProvider implements SearchProvider {

	private static final String SEARCH_PROVIDER_KEY_ACM = "ACM";
	
	private static final String DOMAIN_DL_ACM = "http://dl.acm.org/";
	private static final String URL_DL_ACM_SEARCH = "http://dl.acm.org/results.cfm?query=";

	private static final String URL_ENCODE_UTF_8 = "UTF-8";
	private static final String URL_ENCODE_ISO_8859_1 = "ISO-8859-1";

	private static final String NEXT_PAGE_ANCHOR_TEXT = "next";
	
	private static final String STUDY_ABSTRACT_END_MARKER = "...";
	
	private static final String XPATH_STUDY_TITLE_AND_URL = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//a[@class='medium-text']";
	private static final String XPATH_STUDY_ABSTRACT = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//div[@class='abstract2']";
	private static final String X_PATH_NEXT_PAGE = "//td[@colspan='2' and @align='right']/a";
	
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
	
	public String getKey() {
		return SEARCH_PROVIDER_KEY_ACM;
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
		
		return URL_DL_ACM_SEARCH + query;
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
				study.setUrl(DOMAIN_DL_ACM + anchor.getHrefAttribute().trim());
				
				// Extracting study abstract.
				List<?> studyDivsAbstracts = page.getByXPath(XPATH_STUDY_ABSTRACT);
				if (studyDivsAbstracts.size() >= i) {
					HtmlDivision div = (HtmlDivision) studyDivsAbstracts.get(i);
					
					// Extracting study abstract div content.
					String divContent = div.getTextContent().trim();
					
					// Removing non-abstract informations from div content.
					int abstractEndMarker = divContent.indexOf(STUDY_ABSTRACT_END_MARKER);
					if (abstractEndMarker >= 0) {
						study.setAbstract(divContent.substring(0, divContent.indexOf(STUDY_ABSTRACT_END_MARKER) + STUDY_ABSTRACT_END_MARKER.length()));
					} else {
						study.setAbstract("");
					}
				}
				
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

	private String extractNextPageUrl(HtmlPage page) {
		String toReturn = null;
		
		List<?> nextPageTags = page.getByXPath(X_PATH_NEXT_PAGE);
		for (Object object : nextPageTags) {
			HtmlAnchor nextPageAnchor = (HtmlAnchor)object;
			String nextPageString = nextPageAnchor.getTextContent();
			
			if (nextPageString.trim().equalsIgnoreCase(NEXT_PAGE_ANCHOR_TEXT)) {
				toReturn = DOMAIN_DL_ACM + nextPageAnchor.getHrefAttribute().trim();
			}
		}
		
		return toReturn;
	}
	
	// TODO METODO DE TESTE
	public static void main(String[] args) {
		try {
			SearchProvider searchProvider = new AcmSearchProvider();
			
//			List<Study> studies = searchProvider.search("\"systematic mapping study\" AND \"software engineering\"");
			SearchResult result = searchProvider.search("Software AND cloud OR computing");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
