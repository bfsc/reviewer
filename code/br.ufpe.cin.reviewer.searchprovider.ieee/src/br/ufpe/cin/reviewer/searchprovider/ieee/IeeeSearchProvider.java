package br.ufpe.cin.reviewer.searchprovider.ieee;

import java.io.FileWriter;
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
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("authors")) {
							//ageitar ainda pois ta pegando como uma string só e tem que pegar como lista
							study.addAuthor(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("py")) {
							study.setYear(domNode.getTextContent());
						}
						if (domNode.getLocalName() != null && domNode.getLocalName().equalsIgnoreCase("affiliations")) {
							//ageitar ainda pois ta pegando como uma string só e tem que pegar como lista
							String string = domNode.getTextContent();
							List<String> stringList = new ArrayList<String>();
							int lastIndex = 0;
							for(int i = 0;i < string.length();i++){
								String substring = null;
								if(string.charAt(i) == ','){
									substring = string.substring(lastIndex, i);
									lastIndex = i + 2;
									System.out.println(substring + "  ->" + i);
									stringList.add(substring);
								}
								if(i+1 == string.length()){
									substring = string.substring(lastIndex);
									System.out.println(substring + "<chegou na ultima string>" + i);
									stringList.add(substring);
								}
							}
							List<String> institutions = new ArrayList<String>();
							List<String> countries = new ArrayList<String>();
							if(stringList.size() >= 3){
								institutions = stringList.subList(0, stringList.size() - 2);
								countries = stringList.subList(stringList.size() - 2, stringList.size());
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
			SearchResult result = searchProvider.search("\"systematic literature review\"");
			int count = 1;

			StringBuilder buffer = new StringBuilder();
			
			for (Study study : result.getStudies()) {
				buffer.append(count + ": " + study.getTitle() + "\r\n");
        		buffer.append(study.getAbstract() + "\r\n");
				buffer.append(study.getUrl() + "\r\n");
        		buffer.append(study.getAuthors() + "\r\n");
				buffer.append(study.getYear() + "\r\n");
				buffer.append(study.getInstitutions() + "\r\n");
				buffer.append(study.getCountries() + "\r\n\r\n");
				count++;
			}
			
			FileWriter writer = new FileWriter("C:/Arthur/Iniciação cientifica/search.result.txt");
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
