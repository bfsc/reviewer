package br.ufpe.cin.reviewer.searchprovider.springerlink;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.logger.ReviewerLogger;
import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;

public class SpringerLinkSearchProvider implements SearchProvider {
	
	public static final String SEARCH_PROVIDER_NAME = "SPRINGER_LINK";
	
	private static final String DOMAIN_DL_SPRINGER_LINK = "http://link.springer.com";
	private static final String URL_DL_SPRINGER_LINK_SEARCH = "http://link.springer.com/search?query=";
	private static final String URL_DL_SPRINGER_LINK_ARTICLE_SEARCH = "&facet-content-type=%22Article%22";

	private static final String URL_ENCODE_UTF_8 = "UTF-8";
	private static final String URL_ENCODE_ISO_8859_1 = "ISO-8859-1";
	
	private static final String XPATH_STUDY_TITLE_AND_URL = "//a[@class='title']";
	private static final String XPATH_STUDY_ABSTRACT = "//p[@class='snippet']";
	private static final String XPATH_STUDY_NUMBER_OF_PAGES = "//span[@class='number-of-pages']";
	private static final String STUDY_ABSTRACT_END_MARKER = "...";
	private static final String XPATH_STRONG_TOTAL_FOUND = "//strong";
	
	private int NEXT_PAGE_COUNTER = 2;
	private String SEARCH_STRING = "";
	
	public SearchProviderResult search(String searchString) throws SearchProviderException {
		SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
		
		try {
			// Create the web browser
			WebClient browser = new WebClient();
			browser.getOptions().setJavaScriptEnabled(false);
			browser.getOptions().setCssEnabled(false);
			
			// Throwing an exception if the search string is invalid.
			if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
				throw new RuntimeException("Invalid search string");
			}
			
			// Assemble search URL
			String searchUrl = assembleSearchUrl(searchString);
			
			// Extract studies data
			result.getStudies().addAll(extractStudiesData(browser, searchUrl, result));
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
	
	private List<Study> extractStudiesData(WebClient browser, String searchUrl, SearchProviderResult result) {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			HtmlPage page = browser.getPage(searchUrl);
			HtmlStrong totalFound = (HtmlStrong) page.getByXPath(XPATH_STRONG_TOTAL_FOUND).get(0);
			result.setTotalFound(Integer.parseInt(totalFound.getTextContent().replaceAll(",", "").trim()));
			
			String numberOfPages;
			try {
				List<?> studyNumberSpan = page.getByXPath(XPATH_STUDY_NUMBER_OF_PAGES);
				HtmlSpan s = (HtmlSpan) studyNumberSpan.get(0);
				numberOfPages = s.getTextContent().trim();
			} catch (Exception e) {
				numberOfPages = "1";
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
				study.setUrl(DOMAIN_DL_SPRINGER_LINK + anchor.getHrefAttribute().trim());
				
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
			String nextPageUrl = extractNextPageUrl(numberOfPages);
			
			if (nextPageUrl != null) {
				toReturn.addAll(extractStudiesData(browser, nextPageUrl, result));
			}
		} catch (Exception e) {
			ReviewerLogger.info(e.getMessage());
		}
		
		return toReturn;
	}

	private String extractNextPageUrl(String numberOfPages) {
		String toReturn = null;
		String numberEdited = numberOfPages.replaceAll(",","");
		int number = Integer.parseInt(numberEdited);
		if(number > 999){
			if(NEXT_PAGE_COUNTER < 1000){
				toReturn = DOMAIN_DL_SPRINGER_LINK + "/search/page/" + NEXT_PAGE_COUNTER + "?query=" + SEARCH_STRING + URL_DL_SPRINGER_LINK_ARTICLE_SEARCH;
				NEXT_PAGE_COUNTER++;
				//System.out.println(toReturn);
			}			
		}else{
			if(NEXT_PAGE_COUNTER <= number){
				toReturn = DOMAIN_DL_SPRINGER_LINK + "/search/page/" + NEXT_PAGE_COUNTER + "?query=" + SEARCH_STRING + URL_DL_SPRINGER_LINK_ARTICLE_SEARCH;
				NEXT_PAGE_COUNTER++;
				//System.out.println(toReturn);
			}			
		}
		return toReturn;
	}

}
