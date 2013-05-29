package br.ufpe.cin.reviewer.searchprovider.ieee;

import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

public class IeeeSearchProvider implements SearchProvider {

	public static final String SEARCH_PROVIDER_NAME = "IEEE";

	private static final String URL_ENCODE_UTF_8 = "UTF-8";
	
	private static final String URL_DL_IEEE_SEARCH = "http://ieeexplore.ieee.org/gateway/ipsSearch.jsp?";

	private int count = 1;
	private int numeroDeEstudos = 1000;
	
	private AtomicBoolean die;
	
	public IeeeSearchProvider() {
		this.die = new AtomicBoolean(false);
	}
	
	public SearchProviderResult search(String searchString) throws SearchProviderException {
		SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
		int totalFound;
		
		try {
			WebClient browser = new WebClient();
			
			String query = URLEncoder.encode(searchString, URL_ENCODE_UTF_8).toString();
			String searchUrl = mountSearchUrl(query);
			
			XmlPage page = browser.getPage(searchUrl);
			DomElement totalFoundElement = page.getFirstByXPath("/root/totalfound");
			totalFound = Integer.parseInt(totalFoundElement.getTextContent());
			result.setTotalFound(totalFound);
			
			while(count < totalFound){
				searchUrl = mountSearchUrl(searchString);
				page = browser.getPage(searchUrl);
					
				List<?> documents = page.getByXPath("//document");
				for (Object object : documents) {
					
					if (die.get()) {
						return result;
					}
					
					Study study = new Study();
					study.setSource(SEARCH_PROVIDER_NAME);
					study.setStatus(Study.StudyStatus.NOT_EVALUATED);
					
					DomElement element = (DomElement) object;
					DomNodeList<DomNode> childNodes = element.getChildNodes();
					for (DomNode domNode : childNodes) {
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("title")) {
							study.setTitle(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("abstract")) {
							study.setAbstract(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("mdurl")) {
							study.setUrl(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("authors")) {
							//ageitar ainda pois ta pegando como uma string só e tem que pegar como lista
							study.addAuthor(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("py")) {
							study.setYear(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("affiliations")) {
							String nodeText = domNode.getTextContent();
							
							if (nodeText.contains(",")) {
								study.addCountry(nodeText.substring(nodeText.lastIndexOf(",") + 1).trim());
								study.addInstitution(nodeText.substring(0, nodeText.lastIndexOf(",")).replaceAll(",", "-").trim());
							} else {
								study.addInstitution(nodeText.trim());
							}
						}
					}
					
					result.getStudies().add(study);
				}
				
				count = count + 1000;
				if(count - 1 + numeroDeEstudos > totalFound){
					numeroDeEstudos = totalFound - (count - 1);
				}
			}
			
			browser.closeAllWindows();
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return result;
	}
	
	public void die() {
		this.die.set(true);
	}
	
	private String mountSearchUrl(String searchString) {
		String query = "";
		query = "md=" + searchString + "&hc="+ numeroDeEstudos + "&rs=" + count;
		
		return URL_DL_IEEE_SEARCH + query;
	}

}
