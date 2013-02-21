package br.cin.ufpe.reviewer.search.provider.sciencedirect;

import java.util.List;

import br.cin.ufpe.reviewer.search.provider.sciencedirect.ScienceDirectSearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ScienceDirectSearchProvider implements SearchProvider {
	
	private static final String DOMAIN_DL_SCIENCE_DIRECT = "http://www.sciencedirect.com/";
	
	//private static final String XPATH_ADVANCED_SEARCH = "//td[@align='right' and @nowrap='nowrap' and @widht='90%' and @valign='middle']//a[@style='vertical-align:bottom;font-size:0.92em;']";
	
	//private static final String XPATH_EXPERT_SEARCH = "//div[@class='advExpertLink' and @style='float:right;']";
	
	
	public List<Study> search(String searchString) throws SearchProviderException {
		try {
			WebClient browser = new WebClient();
			HtmlPage page = browser.getPage(DOMAIN_DL_SCIENCE_DIRECT);
			
			DomElement Advanced_Search_link = page.getFirstByXPath("/td[@a='Advanced search']");
			String link = Advanced_Search_link.getAttribute("href");
			System.out.print(link);
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
