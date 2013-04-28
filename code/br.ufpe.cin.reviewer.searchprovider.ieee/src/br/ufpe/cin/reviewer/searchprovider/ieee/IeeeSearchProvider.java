package br.ufpe.cin.reviewer.searchprovider.ieee;

import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

public class IeeeSearchProvider implements SearchProvider {

	private static final String URL_ENCODE_UTF_8 = "UTF-8";
	
	private static final String SEARCH_PROVIDER_KEY_IEEE = "IEEE";

	private static final String URL_DL_IEEE_SEARCH = "http://ieeexplore.ieee.org/gateway/ipsSearch.jsp?";

	private int count = 1;
	private int numeroDeEstudos = 1000;
	
	public SearchResult search(String searchString) throws SearchProviderException {
		SearchResult result = new SearchResult();
		int totalFound;
		
		try {
			WebClient browser = new WebClient();
			
			String query = URLEncoder.encode(searchString, URL_ENCODE_UTF_8).toString();
			String searchUrl = mountSearchUrl(query);
			System.out.print(searchUrl + "\n" );			
			
			XmlPage page = browser.getPage(searchUrl);
			DomElement totalFoundElement = page.getFirstByXPath("/root/totalfound");
			totalFound = Integer.parseInt(totalFoundElement.getTextContent());
			
			while(count < totalFound){
				System.out.print("O valor de totalFound eh " + totalFound + "\n" );
				searchUrl = mountSearchUrl(searchString);
				page = browser.getPage(searchUrl);
					
				List<?> documents = page.getByXPath("//document");
				for (Object object : documents) {
					Study study = new Study();
					
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
					}
					result.getStudies().add(study);
				}
				count = count + 1000;
				if(count - 1 + numeroDeEstudos > totalFound){
					numeroDeEstudos = totalFound - count - 1;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getKey() {
		return SEARCH_PROVIDER_KEY_IEEE;
	}
	
	private String mountSearchUrl(String searchString) {
		String query = "";
		query = "querytext=" + searchString + "&hc="+ numeroDeEstudos + "&rs=" + count;
		
		return URL_DL_IEEE_SEARCH + query;
	}

	public static void main(String[] args) {
		try{
			SearchProvider searchProvider = new IeeeSearchProvider();
			SearchResult result = searchProvider.search("\"Software engineering testbeds\"");
			int count = 1;

			StringBuilder buffer = new StringBuilder();
			
			for (Study study : result.getStudies()) {
				buffer.append(count + ": " + study.getTitle() + "\r\n");
        		buffer.append(study.getAbstract() + "\n");
				buffer.append(study.getUrl() + "\r\n\r\n");
				count++;
			}
			
			FileWriter writer = new FileWriter("/Users/emanoel/Documents/workspace-rcp/reviewer/resultados/search.result.txt");
			writer.write(buffer.toString());
			writer.flush();
			writer.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//http://ieeexplore.ieee.org/gateway/ipsSearch.jsp?ti=software&hc=1000&rs=1001

}
