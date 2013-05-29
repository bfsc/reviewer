package br.ufpe.cin.reviewer.searchprovider.acm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

public class AcmSearchProvider implements SearchProvider {

	public static final String SEARCH_PROVIDER_NAME = "ACM";
	
	private static final int YEAR_STRING_LENGTH = 4;
	private static final String DOMAIN_DL_ACM = "http://dl.acm.org/";
	private static final String URL_DL_ACM_SEARCH = "http://dl.acm.org/results.cfm?query=";

	private static final String URL_ENCODE_UTF_8 = "UTF-8";
	private static final String URL_ENCODE_ISO_8859_1 = "ISO-8859-1";

	private static final String NEXT_PAGE_ANCHOR_TEXT = "next";
	
	private static final String STUDY_ABSTRACT_END_MARKER = "...";
	
	private static final String XPATH_STUDY_TITLE_AND_URL = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//a[@class='medium-text']";
	private static final String XPATH_STUDY_ABSTRACT = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//div[@class='abstract2']";
	private static final String XPATH_STUDY_AUTHORS = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//div[@class='authors']";
	private static final String XPATH_STUDY_YEAR = "//table[@style='padding: 5px; 5px; 5px; 5px;' and @border='0']//td[@class='small-text' and @nowrap and (not(@colspan='3'))]";

	private static final String XPATH_TOTAL_FOUND = "//table[@border='0' and @width='100%' and @align='left' and @class='small-text']//tr[@valign='top']//td";
	private static final String X_PATH_NEXT_PAGE = "//td[@colspan='2' and @align='right']/a";
	
	private AtomicBoolean die;
	
	public AcmSearchProvider() {
		this.die = new AtomicBoolean(false);
	}
	
	public SearchProviderResult search(String searchString) throws SearchProviderException {
		SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
		
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
			result.getStudies().addAll(extractStudiesData(browser, searchUrl, result));
			
			browser.closeAllWindows();
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return result;
	}
	
	public void die() {
		this.die.set(true);
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
	
	private List<Study> extractStudiesData(WebClient browser, String searchUrl, SearchProviderResult result) {
		List<Study> toReturn = new LinkedList<Study>();
		
		if (die.get()) {
			return toReturn;
		}
		
		try {
			HtmlPage page = browser.getPage(searchUrl);
		
			// Extracting total found studies
			List<?> totalFoundElements = page.getByXPath(XPATH_TOTAL_FOUND);
			for (Object object : totalFoundElements) {
				HtmlTableDataCell cell = (HtmlTableDataCell) object;
				String cellContent = cell.getTextContent();
				if (cellContent.trim().startsWith("Results 1")) {
					String totalFoundString = cellContent.substring(cellContent.indexOf("of") + 2);
					result.setTotalFound(Integer.parseInt(totalFoundString.replaceAll(",", "").trim()));
				}
			}
			
			// Extracting studies data.
			List<?> studyTablesAnchors = page.getByXPath(XPATH_STUDY_TITLE_AND_URL);
			for (int i = 0; i < studyTablesAnchors.size(); i++) {
				Study study = new Study();
				study.setStatus(Study.StudyStatus.NOT_EVALUATED);
				study.setSource(SEARCH_PROVIDER_NAME);
				
				HtmlAnchor anchor = (HtmlAnchor) studyTablesAnchors.get(i);
				
				// Extracting study title and URL
				study.setTitle(anchor.getTextContent().trim());
				study.setUrl(DOMAIN_DL_ACM + anchor.getHrefAttribute().trim());
				
				// Extracting study authors.
				List<?> studyDivsAuthors = page.getByXPath(XPATH_STUDY_AUTHORS);
				if (studyDivsAuthors.size() > i) {
					HtmlDivision div = (HtmlDivision) studyDivsAuthors.get(i);
					
					// Extracting study abstract div content.
					String divContent = div.getTextContent().trim();
					
					// Removing non-abstract informations from div content.
					if (divContent.trim().length() >= 0) {
						divContent = divContent.replaceAll("[\t\n]", "");
						study.addAuthor(divContent);
						
					} else {
//						study.addStudyAuthor(new StudyAuthor(""));
					}
				}
				
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
				
				// Extracting study year.
				List<?> studyTdYear = page.getByXPath(XPATH_STUDY_YEAR);
				if (studyTdYear.size() >= i) {
					HtmlTableDataCell div = (HtmlTableDataCell) studyTdYear.get(i);
					
					// Extracting study abstract div content.
					String divContent = div.getTextContent().trim();
					
					// Removing non-abstract informations from div content.
					if (divContent.trim().length() >= 0) {
						study.setYear(divContent.substring(divContent.length() - YEAR_STRING_LENGTH));
						
					} else {
						study.setYear("");
					}
				}
				
				toReturn.add(study);
			}
			
			// Extracting the URL of next page.
			String nextPageUrl = extractNextPageUrl(page);
			
			if (nextPageUrl != null) {
				toReturn.addAll(extractStudiesData(browser, nextPageUrl, result));
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

}
