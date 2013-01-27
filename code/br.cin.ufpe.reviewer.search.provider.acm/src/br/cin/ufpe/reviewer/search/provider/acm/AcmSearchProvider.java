package br.cin.ufpe.reviewer.search.provider.acm;

import java.util.LinkedList;
import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;

public class AcmSearchProvider implements SearchProvider {

	private static final String DOMAIN_DL_ACM = "http://dl.acm.org/";

	private static final String REGEX_POSITIVE_INTEGER_VALUE = "\\d+";

	private static final String STUDY_ABSTRACT_END_MARKER = "...";
	
	private static final String XPATH_STUDY_TITLE_AND_URL = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//a[@class='medium-text']";
	private static final String XPATH_STUDY_ABSTRACT = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//div[@class='abstract2']";
	
	private static final String X_PATH_CURRENT_PAGE = "//td[@colspan='2' and @align='right']/strong";
	private static final String X_PATH_PAGES = "//td[@colspan='2' and @align='right']/a";

	public List<Study> search(String searchString) throws SearchProviderException {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			// Create the web browser
			WebClient browser = new WebClient();
			
			// Assemble search URL
			String searchUrl = assembleSearchUrl(searchString);
			
			// Extract studies data
			toReturn.addAll(extractStudiesData(browser, searchUrl));
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search for the following query string:" + searchString, e);
		}
		
		return  toReturn;
	}
	
	private String assembleSearchUrl(String searchString) {
		String query = "";
		
		// Replacing double quotes
		query = searchString.replaceAll("\"", "%22");
		
		// Replacing white spaces
		query = searchString.replaceAll(" ", "%20"); 
		
		return "http://dl.acm.org/results.cfm?query=" + query;
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
				study.setUrl(anchor.getHrefAttribute().trim());
				
				// Extracting study abstract.
				List<?> studyDivsAbstracts = page.getByXPath(XPATH_STUDY_ABSTRACT);
				if (studyDivsAbstracts.size() <= i) {
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
		
		List<?> currentPageTags = page.getByXPath(X_PATH_CURRENT_PAGE);
		if (currentPageTags.size() > 0) {
			String currentPageString = ((HtmlStrong)currentPageTags.get(0)).getTextContent();
			
			if (currentPageString.trim().matches(REGEX_POSITIVE_INTEGER_VALUE)) {
				int currentPage = Integer.parseInt(currentPageString);
				
				List<?> pages = page.getByXPath(X_PATH_PAGES);
				for (Object object : pages) {
					HtmlAnchor anchor = (HtmlAnchor) object;
					
					if ((anchor.getTextContent().trim().matches(REGEX_POSITIVE_INTEGER_VALUE)) && (Integer.parseInt(anchor.getTextContent().trim()) == currentPage + 1)) {
						toReturn = DOMAIN_DL_ACM + anchor.getHrefAttribute().trim();
						break;
					}
				}
			}
		}
		return toReturn;
	}
	
	public static void main(String[] args) {
		try {
			SearchProvider searchProvider = new AcmSearchProvider();
			
			List<Study> studies = searchProvider.search("\"systematic mapping study\" AND \"software engineering\"");
			
			for (Study study : studies) {
				System.out.println(study);
			}
		} catch (SearchProviderException e) {
			e.printStackTrace();
		}
	}
	
}
