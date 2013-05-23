package br.ufpe.cin.reviewer.searchprovider.sciencedirect;
 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
 
public class ScienceDirectSearchProvider implements SearchProvider {

	public static final String SEARCH_PROVIDER_NAME = "SCIENCE_DIRECT";
	
	private static final String URL_MAIN_SITE = "http://www.sciencedirect.com";
	
	private static final String XPATH_BUTTON_EXPORT = "//input[@type='submit' and @value='Export' and @name='Export' and @alt='Export' and @class='button']";
	private static final String XPATH_INPUT_EXPORT_FORMAT = "//input[@type='radio' and @id='BIBTEX' and @name='citation-type' and @value='BIBTEX' and @class='artRadio expRadio']";
	private static final String XPATH_INPUT_CONTENT_FORMAT = "//input[@type='radio' and @id='cite-abs' and @name='format' and @value='cite-abs' and @class='artRadio expRadio']";
	private static final String XPATH_INPUT_EXPORT = "//input[@id='exportIcon_sci_dir' and @value='Export citations' and @name='export' and @class='listAction' and @type='submit']";
	private static final String XPATH_STRONG_TOTAL_FOUND = "//div[@class='iconLinks']//strong";
	private static final String XPATH_BUTTON_SEARCH = "//input[@type='submit' and @class='button' and @value='Search' and @name='RegularSearch' and @alt='Search']";
	private static final String XPATH_TEXTAREA_SEARCH_STRING = "//textarea[@name='SearchText' and @wrap='virtual' and @rows='5' and @cols='60']";
	private static final String XPATH_ANCHOR_EXPERT_SEARCH = "//div[@class='advExpertLink' and @style='float:right;']//a";
	private static final String XPATH_ANCHOR_ADVANCED_SEARCH = "//a[@style='vertical-align:bottom;font-size:0.92em;']";
	
	public SearchProviderResult search(String searchString) throws SearchProviderException {
		SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
		
		try {
			// Create the web browser
			WebClient browser = new WebClient();
			browser.getOptions().setThrowExceptionOnScriptError(false);
			browser.getOptions().setJavaScriptEnabled(false);
			browser.getOptions().setCssEnabled(false);
			
			// Throwing an exception if the search string is invalid.
			if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
				throw new RuntimeException("Invalid search string");
			}

			// Accessing the main site page
			HtmlPage mainSitePage = browser.getPage(URL_MAIN_SITE);
			HtmlAnchor advancedSearchAnchor = mainSitePage.getFirstByXPath(XPATH_ANCHOR_ADVANCED_SEARCH);
			
			// Accessing the advanced search page
			HtmlPage advancedSearchPage = advancedSearchAnchor.click();
			HtmlAnchor expertSearchAnchor = advancedSearchPage.getFirstByXPath(XPATH_ANCHOR_EXPERT_SEARCH);

			// Performing the search in the expert search page
			HtmlPage expertSearchPage = expertSearchAnchor.click();
			HtmlTextArea searchStringTextArea = expertSearchPage.getFirstByXPath(XPATH_TEXTAREA_SEARCH_STRING);
			searchStringTextArea.setTextContent(searchString);
			HtmlInput searchButton = expertSearchPage.getFirstByXPath(XPATH_BUTTON_SEARCH);
			HtmlPage resultsPage = searchButton.click();
			
			// Extraction total found studies
			HtmlStrong totalFoundStrong = resultsPage.getFirstByXPath(XPATH_STRONG_TOTAL_FOUND);
			result.setTotalFound(Integer.parseInt(totalFoundStrong.getTextContent().replaceAll(",", "").trim()));
			
			// Calling the export page
			HtmlInput exportInput = resultsPage.getFirstByXPath(XPATH_INPUT_EXPORT);
			HtmlPage exportPage = exportInput.click();
			
			// Selecting the export format (BIB) as well as the content format (with abstract)
			HtmlInput contentFormatInput = exportPage.getFirstByXPath(XPATH_INPUT_CONTENT_FORMAT);
			contentFormatInput.setChecked(true);
			HtmlInput exportFormatInput = exportPage.getFirstByXPath(XPATH_INPUT_EXPORT_FORMAT);
			exportFormatInput.setChecked(true);
			
			// Exporting the results according to the selections above
			HtmlSubmitInput exportButton = exportPage.getFirstByXPath(XPATH_BUTTON_EXPORT);
			Page exportedStudiesPage = exportButton.click();
			
			// Extract studies data
			result.getStudies().addAll(extractStudiesData(exportedStudiesPage.getWebResponse().getContentAsStream()));
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return  result;
	}
	
	private List<Study> extractStudiesData(InputStream inputStream) throws SearchProviderException {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
			
			while ((line = reader.readLine()) != null && (line.startsWith("[@\\s]") || line.equals(""))){
				// Consuming the initial white spaces from the input stream and the first bibtex entry
			}

			// Parsing studies from input stream
			Study study = null;
			while ((line = reader.readLine()) != null){
				if (study == null) {
					study = new Study();
					study.setStatus(Study.StudyStatus.NOT_EVALUATED);
					study.setSource(SEARCH_PROVIDER_NAME);
				}
				
				if (line.startsWith("title = \"")) {
					study.setTitle(line.substring(0, line.length() - 2).substring(9).trim());
				}
				
				if (line.startsWith("abstract = \"")) {
					study.setAbstract(line.substring(0, line.length() - 2).substring(12).trim());
				}
				
				if (line.startsWith("url = \"")) {
					study.setUrl(line.substring(0, line.length() - 2).substring(7).trim());
				}
				
				if (line.startsWith("year = \"")) {
					study.setYear(line.substring(0, line.length() - 2).substring(8).trim());
				}
				
				if (line.startsWith("author = \"")) {
					for (String author : line.substring(0, line.length() - 2).substring(10).trim().split(" and ")) {
						study.addAuthor(author);
					}
				}
				
				if (line.indexOf("@") == 0) {
					toReturn.add(study);
					study = null;
				}
			}
			
			//Adding the last study
			toReturn.add(study);
		} catch (Exception e) {
			throw new SearchProviderException("Error trying to parse bibtex input stream.", e);
		}
		
		return toReturn;
	}
    
}
