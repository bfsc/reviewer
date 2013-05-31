package br.ufpe.cin.reviewer.searchprovider.scopus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderError;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class ScopusSearchProvider implements SearchProvider {

	public static final String SEARCH_PROVIDER_NAME = "SCOPUS";
	
	private static final String EXPORT_FORMAT_BIBTEX = "BIB";
	private static final String OUTPUT_FORMAT_WITH_ABSTRACT = "CiteAbsKeyws";
	
	private static final String XPATH_EXPORT_BUTTON = "//input[@type='submit' and @value='Export']";
	private static final String XPATH_SELECT_OUTPUT_FORMAT = "//select[@name='view' and @onchange='javascript:changeHelpSection(this);']";
	private static final String XPATH_SELECT_EXPORT_FORMAT = "//select[@name='exportFormat' and @id='exportFormat']";
	private static final String XPATH_ANCHOR_EXPORT_RESULTS = "//a[@class='jsEnabled icon export']";
	private static final String XPATH_SELECT_ALL_CHECKBOX = "//input[@type='checkbox' and @name='selectAllCheckBox']";
	private static final String XPATH_SEARCH_BUTTON = "//input[@type='submit' and @value='Search']";
	private static final String XPATH_DIV_SEARCH_FIELD = "//div[@id='searchfield']";
	
	private static final String URL_SCOPUS_ADVANCED_SEARCH = "http://www.scopus.com/search/form.url?display=advanced";
	
	private static final String XPATH_TOTAL_FOUND = "//span[@class='Bold txtLarge1']";

	private AtomicBoolean die;
	
	public ScopusSearchProvider() {
		this.die = new AtomicBoolean(false);
	}
	
	public SearchProviderResult search(String searchString) throws SearchProviderException {
		SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
		
		try {
			// Create the web browser
			WebClient browser = new WebClient();
			browser.getOptions().setThrowExceptionOnScriptError(false);
			browser.getOptions().setJavaScriptEnabled(true);
			browser.getOptions().setCssEnabled(false);
			browser.getOptions().setUseInsecureSSL(true);
			
			// Throwing an exception if the search string is invalid.
			if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
				throw new RuntimeException("Invalid search string");
			}

			// Performing the search in the advanced search page
			HtmlPage advancedSearchPage = browser.getPage(URL_SCOPUS_ADVANCED_SEARCH);
			HtmlDivision searchDiv = advancedSearchPage.getFirstByXPath(XPATH_DIV_SEARCH_FIELD);
			
			if(searchDiv == null) {
				result.addError(SearchProviderError.SEARCH_PROVIDER_SCOPUS_ERROR_LOGIN_REQUIRED);
				throw new RuntimeException("Can not access provider page.");
			}
			
			searchDiv.setTextContent("TITLE-ABS-KEY(" + searchString + ")");
			HtmlSubmitInput searchButton = advancedSearchPage.getFirstByXPath(XPATH_SEARCH_BUTTON);
			HtmlPage resultsPage = searchButton.click();
			
			// Extraction total found studies
			HtmlSpan totalFoundSspan = resultsPage.getFirstByXPath(XPATH_TOTAL_FOUND);
			result.setTotalFound(Integer.parseInt(totalFoundSspan.getTextContent().replaceAll(",", "").trim()));
			
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
			
			// Exporting the results according to the selections above
			HtmlSubmitInput exportButton = exportPage.getFirstByXPath(XPATH_EXPORT_BUTTON);
			exportButton.setAttribute("onclick", "");
			Page exportedStudiesPage = exportButton.click();
			
			// Extract studies data
			result.getStudies().addAll(extractStudiesData(exportedStudiesPage.getWebResponse().getContentAsStream(), browser));
		} catch (Exception e) {
			if (result.getRaisedErrors().isEmpty()) {
				result.addError(SearchProviderError.SEARCH_PROVIDER_COMMON_ERROR);
			}
			//throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return  result;
	}
	
	public void die() {
		this.die.set(true);
	}
	
	private List<Study> extractStudiesData(InputStream inputStream, WebClient browser) throws SearchProviderException {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
			
			while ((line = reader.readLine()) != null && (line.startsWith("[@\\s]") || line.equals(""))){
				// Consuming the initial white spaces from the input stream and the first bibtex entry
			}

			// Removing initial garbage from input stream
			boolean fisrtStudyFound = false;
			while (!fisrtStudyFound && (line = reader.readLine()) != null) {
				if (line.trim().startsWith("@")) {
					fisrtStudyFound = true;
					line = reader.readLine();
				} else {
					continue;
				}
			}
			
			// Parsing studies from input stream
			Study study = null;
			while ((line = reader.readLine()) != null){
				
				if (die.get()) {
					return toReturn;
				}
				
				if (study == null) {
					study = new Study();
					study.setStatus(Study.StudyStatus.NOT_EVALUATED);
					study.setSource(SEARCH_PROVIDER_NAME);
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
				
				if (line.startsWith("year={")) {
					study.setYear(line.substring(6).replaceAll("},", ""));
				}
				
				if (line.startsWith("author={")) {
					for (String author : line.substring(8).replaceAll("},", "").split(" and ")) {
						study.addAuthor(author);
					}
				}
				
				if (line.startsWith("affiliation={")) {
					for (String institutionCountry : line.substring(13).replaceAll("},", "").split("; ")) {
						String country = institutionCountry.substring(institutionCountry.lastIndexOf(",") + 1).trim();
						study.addCountry(country);
						
						String institution = institutionCountry.substring(0, institutionCountry.lastIndexOf(",") + 1).trim();
						study.addInstitution(institution);
						
					}
				}
				
				if (line.indexOf("@") == 0) {
					toReturn.add(study);
					study = null;
				}
			}
			
			//Adding the last study
			toReturn.add(study);
			
			browser.closeAllWindows();
		} catch (Exception e) {
			throw new SearchProviderException("Error trying to parse bibtex input stream.", e);
		}
		
		return toReturn;
	}

}
