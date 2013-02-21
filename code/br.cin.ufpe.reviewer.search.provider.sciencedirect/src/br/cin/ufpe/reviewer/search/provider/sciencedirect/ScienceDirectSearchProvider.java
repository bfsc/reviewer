package br.cin.ufpe.reviewer.search.provider.sciencedirect;

import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

public class ScienceDirectSearchProvider implements SearchProvider {
	
	private static final String DOMAIN_DL_SCIENCE_DIRECT = "http://www.sciencedirect.com/";
	
	//private static final String XPATH_ADVANCED_SEARCH = "//td[@align='right' and @nowrap='nowrap' and @widht='90%' and @valign='middle']//a[@style='vertical-align:bottom;font-size:0.92em;']";
	
	//private static final String XPATH_EXPERT_SEARCH = "//div[@class='advExpertLink' and @style='float:right;']";
	
	
	public List<Study> search(String searchString) throws SearchProviderException {
		try {
			WebClient browser = new WebClient();
			HtmlPage page = browser.getPage(DOMAIN_DL_SCIENCE_DIRECT);
			
			HtmlTableDataCell Advanced_Search_link = (HtmlTableDataCell) page.getFirstByXPath("//td[a='Advanced search']");
			
			DomNodeList<DomNode> childNodes = Advanced_Search_link.getChildNodes();
			for (DomNode domNode : childNodes) {
				if (domNode instanceof HtmlAnchor && domNode.getTextContent().equalsIgnoreCase("advanced search")) {
					HtmlAnchor anchor = (HtmlAnchor) domNode;
					String advancedSearchLink = anchor.getHrefAttribute();
					HtmlPage advancedSearchPage = browser.getPage(advancedSearchLink);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		try{
			SearchProvider searchProvider = new ScienceDirectSearchProvider();
			searchProvider.search("teste");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
