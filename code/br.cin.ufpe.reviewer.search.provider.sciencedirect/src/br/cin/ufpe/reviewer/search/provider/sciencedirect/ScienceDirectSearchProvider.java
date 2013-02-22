package br.cin.ufpe.reviewer.search.provider.sciencedirect;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
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
	
	//private static final String XPATH_ADVANCED_SEARCH = "//td[@align='right' and @nowrap='nowrap' and @widht='90%' and @valign='middle']//a[@style='vertical-align:bottom;font-size:0.92em;']";
	
	//private static final String XPATH_EXPERT_SEARCH = "//div[@class='advExpertLink' and @style='float:right;']";
	
	

	

	
	
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
			System.out.println(searchUrl);
			
			// Extract studies data
			//toReturn.addAll(extractStudiesData(browser, searchUrl));
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return  null;
		
		
		
		
//		try {
//			WebClient browser = new WebClient();
//			HtmlPage page = browser.getPage(DOMAIN_DL_SCIENCE_DIRECT);
//			HtmlPage advancedSearchPage = null;
//			HtmlPage expertSearchPage = null;
//			
//			HtmlTableDataCell Advanced_Search_link = (HtmlTableDataCell) page.getFirstByXPath("//td[a='Advanced search']");
//			
//			DomNodeList<DomNode> advancedChildNodes = Advanced_Search_link.getChildNodes();
//			for (DomNode domNode : advancedChildNodes) {
//				if (domNode instanceof HtmlAnchor && domNode.getTextContent().equalsIgnoreCase("advanced search")) {
//					HtmlAnchor anchor = (HtmlAnchor) domNode;
//					String advancedSearchLink = anchor.getHrefAttribute();
//					advancedSearchPage = browser.getPage(advancedSearchLink);
//				}
//			}
//			
//			HtmlTableDataCell Expert_Search_link = (HtmlTableDataCell) advancedSearchPage.getFirstByXPath("//div[a='Expert search']");
//			
//			DomNodeList<DomNode> expertChildNodes = Expert_Search_link.getChildNodes();
//			for (DomNode domNode : expertChildNodes) {
//				if (domNode instanceof HtmlAnchor && domNode.getTextContent().equalsIgnoreCase("expert search")){
//					HtmlAnchor anchor = (HtmlAnchor) domNode;
//					String expertSearchLink = anchor.getHrefAttribute();
//					expertSearchPage = browser.getPage(DOMAIN_DL_SCIENCE_DIRECT + expertSearchLink);
//				}
//			}
//			
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
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

	public static void main(String[] args) {
		try{
			SearchProvider searchProvider = new ScienceDirectSearchProvider();
			searchProvider.search("\"Software Engineering\"");
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
