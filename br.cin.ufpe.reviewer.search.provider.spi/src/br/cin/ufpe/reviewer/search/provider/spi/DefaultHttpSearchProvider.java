package br.cin.ufpe.reviewer.search.provider.spi;

import java.io.InputStream;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

public abstract class DefaultHttpSearchProvider extends HttpSearchProvider {

	protected String performSearch(String searchString) {
		String toReturn = null;
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			
			HttpGet httpget = new HttpGet(asembleUrl(searchString));
			HttpResponse response = httpclient.execute(httpget);
			
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				StringBuilder builder = new StringBuilder();
			    InputStream instream = entity.getContent();

			    byte[] buffer = new byte[1024];
		        int bytesRead = -1;
		        
		        while ((bytesRead = instream.read(buffer)) != -1)
		        {
		            if (bytesRead == 1024) {
						builder.append(new String(buffer, "UTF-8"));
					} else {
						byte[] array = new byte[bytesRead];
						System.arraycopy(buffer, 0, array, 0, bytesRead);
						builder.append(new String(array, "UTF-8"));
					}
		        }
		        
		        toReturn = builder.toString();
			}
		} catch (Throwable t) {
			
		}
		
		return toReturn;
	}

	protected Set<Study> extractStudiesData(String html) {
		return null;
	}

	protected String navigateThroughSearchPages(String html) {
		return null;
	}

	private String asembleUrl(String searchString) {
		return null;
	}
	
}
