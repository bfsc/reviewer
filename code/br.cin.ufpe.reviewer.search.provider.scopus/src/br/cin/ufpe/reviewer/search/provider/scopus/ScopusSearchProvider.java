package br.cin.ufpe.reviewer.search.provider.scopus;

import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.util.WebClientUtils;

public class ScopusSearchProvider implements SearchProvider {

	private static final String URL_SCOPUS_1 = "http://www.scopus.com/results/results.url?sort=plf-f&src=s&sid=";
	private static final String URL_SCOPUS_2 = "&sot=a&sdt=a&sl=30&s=";
	private static final String URL_SCOPUS_3 = "&origin=searchadvanced&txGid=";

	private static final String URL_ENCODE_UTF_8 = "UTF-8";
	private static final String URL_ENCODE_ISO_8859_1 = "ISO-8859-1";

	public List<Study> search(String searchString) throws SearchProviderException {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			// Create the web browser
			WebClient browser = new WebClient();
			browser.getOptions().setThrowExceptionOnScriptError(false);
			
			// Throwing an exception if the search string is invalid.
			if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
				throw new RuntimeException("Invalid search string");
			}

			// Obtaining the TXGID in order to compose the search URL 
			HtmlPage advancedSearchPage = browser.getPage("http://www.scopus.com/search/form.url?display=advanced");
			HtmlInput txGidInput = (HtmlInput) advancedSearchPage.getFirstByXPath("//input[@type='hidden' and @id='txGid']");
			String txGid = txGidInput.getValueAttribute();
			
			HtmlInput input = (HtmlInput) advancedSearchPage.getFirstByXPath("//input[@type='hidden' and @id='txGid']");
			
			// Assemble search URL
			String searchUrl = assembleSearchUrl(searchString, txGid);
			
			// Extract studies data
			toReturn.addAll(extractStudiesData(browser, searchUrl));
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return  toReturn;
	}
	
	private String assembleSearchUrl(String searchString, String txGid) {
		String encodedSearchString = "";
		String encodedTxGid= "";
		
		try {
			encodedSearchString = URLEncoder.encode(searchString, URL_ENCODE_ISO_8859_1).toString();
		} catch (UnsupportedEncodingException e) {
			try {
				encodedSearchString = URLEncoder.encode(searchString, URL_ENCODE_UTF_8).toString();
			} catch (UnsupportedEncodingException e2) {
				throw new RuntimeException("An error occurred trying to encode the url.", e2);
			}
		}
		
		try {
			encodedTxGid = URLEncoder.encode(txGid, URL_ENCODE_ISO_8859_1).toString();
		} catch (UnsupportedEncodingException e) {
			try {
				encodedTxGid = URLEncoder.encode(txGid, URL_ENCODE_UTF_8).toString();
			} catch (UnsupportedEncodingException e2) {
				throw new RuntimeException("An error occurred trying to encode the url.", e2);
			}
		}
		
		return URL_SCOPUS_1 + encodedTxGid + URL_SCOPUS_2 + encodedSearchString + URL_SCOPUS_3 + encodedTxGid;
	}
	
	private List<Study> extractStudiesData(WebClient browser, String searchUrl) {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			HtmlPage page = browser.getPage(searchUrl);
			
			HtmlCheckBoxInput checkboxInput = page.getFirstByXPath("//input[@type='checkbox' and @name='selectAllCheckBox']");
			checkboxInput.click();
			
			HtmlAnchor exportAnchor = page.getFirstByXPath("//a[@class='jsEnabled icon export']");
			exportAnchor.setAttribute("onclick", "");
			HtmlPage exportPage = exportAnchor.click();
			
			HtmlSelect exportFormatSelect = exportPage.getFirstByXPath("//select[@name='exportFormat' and @id='exportFormat']");
			for (HtmlOption option : exportFormatSelect.getOptions()) {
				if (option.getValueAttribute().equalsIgnoreCase("BIB")) {
					option.setSelected(true);
				} else {
					option.setSelected(false);
				}
			}
			
			HtmlSelect outputSelect = exportPage.getFirstByXPath("//select[@name='view' and @onchange='javascript:changeHelpSection(this);']");
			for (HtmlOption option : outputSelect.getOptions()) {
				if (option.getValueAttribute().equalsIgnoreCase("CiteAbsKeyws")) {
					option.setSelected(true);
				} else {
					option.setSelected(false);
				}
			}
			
			HtmlSubmitInput exportButton = exportPage.getFirstByXPath("//input[@type='submit' and @value='Export' and @class='jsEnabled Bold']");
			Page exportedStudies = exportButton.click();
			
			FileWriter writer = new FileWriter("C:/Documents and Settings/Bruno Cartaxo/Desktop/page.html");
			writer.write(exportedStudies.getWebResponse().getContentAsString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			//TRATAR EXCECAO
			e.printStackTrace();
		}
		
		return toReturn;
	}

	// TODO METODO DE TESTE
	public static void main(String[] args) {
		try {
			SearchProvider searchProvider = new ScopusSearchProvider();
			
//			List<Study> studies = searchProvider.search("\"systematic mapping study\" AND \"software engineering\"");
//			List<Study> studies = searchProvider.search("security AND \"cloud computing\"");
			List<Study> studies = searchProvider.search("\"systematic mapping study\"");
			
			StringBuilder buffer = new StringBuilder();
			
			for (Study study : studies) {
				buffer.append(study.getTitle() + "\n");
//				buffer.append(study.getAbstract() + "\n");
//				buffer.append(study.getUrl() + "\n\n");
			}
			
//			FileWriter writer = new FileWriter("C:/Documents and Settings/Bruno Cartaxo/Desktop/search.result.txt");
//			writer.write(buffer.toString());
//			writer.flush();
//			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
