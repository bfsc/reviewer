package br.cin.ufpe.reviewer.search.provider.ieee;


import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


import java.util.List;

import br.cin.ufpe.reviewer.search.provider.ieee.IeeeSearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

public class IeeeSearchProvider implements SearchProvider {
	public List<Study> search(String searchString) throws SearchProviderException {
		try {
		    URL url = new URL(searchString);
		    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		    String line;

		    while ((line = reader.readLine()) != null) {
		    	//System.out.print(line);
		    	if(line.contains("<root>") == true){
			    	System.out.print("chegou aqui");
		    	}
		    }
		    reader.close();
		} 
		catch (MalformedURLException e) {
			//...
		}
		catch (IOException e) {
			//...
		}
		return null;
	}
	
	public static void main(String[] args) {
		try{
			SearchProvider searchProvider = new IeeeSearchProvider();
			List<Study> studies = searchProvider.search("http://ieeexplore.ieee.org/gateway/ipsSearch.jsp?ti=software&hc=15&rs=11");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
