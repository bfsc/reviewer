package br.ufpe.cin.reviewer.searchprovider.scopus;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class ScopusSearchProvider implements SearchProvider {

	private static final String SEARCH_PROVIDER_KEY_SCOPUS = "SCOPUS";
	
	private static final String EXPORT_FORMAT_BIBTEX = "BIB";
	private static final String OUTPUT_FORMAT_WITH_ABSTRACT = "CiteAbsKeyws";
	
	private static final String XPATH_EXPORT_BUTTON = "//input[@type='submit' and @value='Export' and @class='jsEnabled Bold']";
	private static final String XPATH_SELECT_OUTPUT_FORMAT = "//select[@name='view' and @onchange='javascript:changeHelpSection(this);']";
	private static final String XPATH_SELECT_EXPORT_FORMAT = "//select[@name='exportFormat' and @id='exportFormat']";
	private static final String XPATH_ANCHOR_EXPORT_RESULTS = "//a[@class='jsEnabled icon export']";
	private static final String XPATH_SELECT_ALL_CHECKBOX = "//input[@type='checkbox' and @name='selectAllCheckBox']";
	private static final String XPATH_SEARCH_BUTTON = "//input[@type='submit' and @value='Search']";
	private static final String XPATH_DIV_SEARCH_FIELD = "//div[@id='searchfield']";
	
	private static final String URL_SCOPUS_ADVANCED_SEARCH = "http://www.scopus.com/search/form.url?display=advanced";

	public SearchResult search(String searchString) throws SearchProviderException {
		SearchResult result = new SearchResult();
		
		try {
			// Create the web browser
			WebClient browser = new WebClient();
			browser.getOptions().setThrowExceptionOnScriptError(false);
			
			// Throwing an exception if the search string is invalid.
			if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
				throw new RuntimeException("Invalid search string");
			}

			// Performing the search in the advanced search page
			HtmlPage advancedSearchPage = browser.getPage(URL_SCOPUS_ADVANCED_SEARCH);
			HtmlDivision searchDiv = advancedSearchPage.getFirstByXPath(XPATH_DIV_SEARCH_FIELD);
			searchDiv.setTextContent(searchString);
			HtmlSubmitInput searchButton = advancedSearchPage.getFirstByXPath(XPATH_SEARCH_BUTTON);
			HtmlPage resultsPage = searchButton.click();
			
			// Selecting all results in order export then
			HtmlCheckBoxInput checkboxInput = resultsPage.getFirstByXPath(XPATH_SELECT_ALL_CHECKBOX);
			checkboxInput.click();
			
			// Calling the export page
			HtmlAnchor exportAnchor = resultsPage.getFirstByXPath(XPATH_ANCHOR_EXPORT_RESULTS);
			exportAnchor.setAttribute("onclick", "");
			HtmlPage exportPage = exportAnchor.click();
			
			// Selecting the export format (BIB) as well as the output informations (with abstract)
			HtmlSelect exportFormatSelect = exportPage.getFirstByXPath(XPATH_SELECT_EXPORT_FORMAT);
			for (HtmlOption option : exportFormatSelect.getOptions()) {
				if (option.getValueAttribute().equalsIgnoreCase(EXPORT_FORMAT_BIBTEX)) {
					option.setSelected(true);
				} else {
					option.setSelected(false);
				}
			}
			
			HtmlSelect outputSelect = exportPage.getFirstByXPath(XPATH_SELECT_OUTPUT_FORMAT);
			for (HtmlOption option : outputSelect.getOptions()) {
				if (option.getValueAttribute().equalsIgnoreCase(OUTPUT_FORMAT_WITH_ABSTRACT)) {
					option.setSelected(true);
				} else {
					option.setSelected(false);
				}
			}
			
			// Exporting the results according to the selecionts above
			HtmlSubmitInput exportButton = exportPage.getFirstByXPath(XPATH_EXPORT_BUTTON);
			Page exportedStudiesPage = exportButton.click();
			
			// Extract studies data
			result.getStudies().addAll(extractStudiesData(exportedStudiesPage.getWebResponse().getContentAsStream()));
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return  result;
	}
	
	public String getKey() {
		return SEARCH_PROVIDER_KEY_SCOPUS;
	}
	
	private List<Study> extractStudiesData(InputStream inputStream) {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
			
			while ((line = reader.readLine()) != null && (line.startsWith("[@\\s]") || line.equals(""))){
				// Consuming the initial white spaces from the input stream and the first bibtex entry
			}
			
			Study study = null;
			while ((line = reader.readLine()) != null){
				if (study == null) {
					study = new Study();
				}
				
				if (line.startsWith("title={")) {
					study.setTitle(line.substring(7).replaceAll("},", ""));
				}
				
				if (line.startsWith("abstract={")) {
					study.setAbstract(line.substring(10).replaceAll("},", ""));
				}
				
				if (line.startsWith("url={")) {
					study.setUrl(line.substring(5).replaceAll("},", ""));
				}
				
				if (line.indexOf("@") == 0) {
					toReturn.add(study);
					study = null;
				}
			}
			
			//Adding the last study
			toReturn.add(study);
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
//			SearchResult result = searchProvider.search("\"systematic mapping study\"");
			SearchResult result = searchProvider.search("\"software engineering\"");
			
			StringBuilder buffer = new StringBuilder();
			
			for (Study study : result.getStudies()) {
				buffer.append(study.getTitle() + "\n");
//				buffer.append(study.getAbstract() + "\n");
//				buffer.append(study.getUrl() + "\n\n");
			}
			
			FileWriter writer = new FileWriter("C:/Documents and Settings/Bruno Cartaxo/Desktop/search.result.txt");
			writer.write(buffer.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
