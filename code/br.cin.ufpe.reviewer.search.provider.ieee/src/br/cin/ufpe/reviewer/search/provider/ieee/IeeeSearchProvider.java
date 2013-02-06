package br.cin.ufpe.reviewer.search.provider.ieee;

import java.util.ArrayList;
import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

public class IeeeSearchProvider implements SearchProvider {
	
	public List<Study> search(String searchString) throws SearchProviderException {
		List<Study> toReturn = new ArrayList<Study>();
		
		try {
			WebClient browser = new WebClient();
			XmlPage page = browser.getPage(searchString);
			List<?> documents = page.getByXPath("//document");
			for (Object object : documents) {
				Study study = new Study();
				
				DomElement element = (DomElement) object;
				DomNodeList<DomNode> childNodes = element.getChildNodes();
				for (DomNode domNode : childNodes) {
					if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("title")) {
						study.setTitle(domNode.getTextContent());
					}
				}
				
				toReturn.add(study);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	public static void main(String[] args) {
		try{
			SearchProvider searchProvider = new IeeeSearchProvider();
			List<Study> studies = searchProvider.search("http://ieeexplore.ieee.org/gateway/ipsSearch.jsp?ti=software&hc=1000&rs=1001");
			
			for (Study study : studies) {
				System.out.println(study.getTitle());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
