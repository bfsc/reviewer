package br.ufpe.cin.reviewer.searchprovider.ieee;

import java.net.URLEncoder;
import java.util.ArrayList;
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
			
			XmlPage page = browser.getPage(searchUrl);
			DomElement totalFoundElement = page.getFirstByXPath("/root/totalfound");
			totalFound = Integer.parseInt(totalFoundElement.getTextContent());
			
			while(count < totalFound){
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
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("authors")) {
							//ageitar ainda pois ta pegando como uma string só e tem que pegar como lista
							study.addAuthor(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("py")) {
							study.setYear(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("affiliations")) {
							//ajeitar ainda pois ta pegando como uma string só e tem que pegar como lista
							String string = domNode.getTextContent();
							List<String> stringList = new ArrayList<String>();
							int lastIndex = 0;
							for(int i = 0;i < string.length();i++){
								String substring = null;
								if(string.charAt(i) == ','){
									substring = string.substring(lastIndex, i);
									lastIndex = i + 2;
									stringList.add(substring);
								}
								if(i+1 == string.length()){
									substring = string.substring(lastIndex);
									stringList.add(substring);
								}
							}
							List<String> institutions = new ArrayList<String>();
							List<String> countries = new ArrayList<String>();
							if(stringList.size() >= 3 && stringList.size() != 1){
								institutions = stringList.subList(0, stringList.size() - 1);
								countries = stringList.subList(stringList.size() - 1, stringList.size());
							}
							else if(stringList.size() == 1){
								institutions = stringList.subList(0, stringList.size());
								countries = null;
							}
							else{
								institutions = stringList.subList(0, stringList.size() - 1);
								countries = stringList.subList(stringList.size() - 1, stringList.size());
							}
							study.setInstitutions(institutions);
							study.setCountries(countries);
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
	
	private String mountSearchUrl(String searchString) {
		String query = "";
		query = "querytext=" + searchString + "&hc="+ numeroDeEstudos + "&rs=" + count;
		
		return URL_DL_IEEE_SEARCH + query;
	}

}
