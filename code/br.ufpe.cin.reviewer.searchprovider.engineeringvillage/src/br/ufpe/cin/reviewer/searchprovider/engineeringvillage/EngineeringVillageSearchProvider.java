package br.ufpe.cin.reviewer.searchprovider.engineeringvillage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import br.ufpe.cin.reviewer.logger.ReviewerLogger;
import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderError;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

public class EngineeringVillageSearchProvider implements SearchProvider {

	public static final String SEARCH_PROVIDER_NAME = "ENGINEERING_VILLAGE";
	
	private static final String URL_MAIN_SITE = "http://www.engineeringvillage.com";
	
	private static final String XPATH_BUTTON_DOWNLOAD = "//input[@type='button' and @name='Save']";
	private static final String XPATH_INPUT_DOWNLOAD = "//input[@id='rdBib']";
	private static final String XPATH_ANCHOR_DOWNLOAD = "//a[@id='downloadlink' and @title='Download selections']";
	private static final String XPATH_LIST_ITEM_MAXIMUM = "//li[@id='Maximum' and @class='pageselect_action' and @action='maximum']";
	private static final String XPATH_IMG_SELECTION_OPTION = "//img[@id='pageselect_toggle' and @title='Selection options' and @class='pageselect_toggle closed']";
	private static final String XPATH_BOLD_TOTAL_FOUND = "//div[@id='querydisplay']//p//b";
	private static final String XPATH_BUTTON_SEARCH = "//input[@type='submit' and @value='Search' and @style='float:right;' and @class='button']";
	private static final String XPATH_TEXTAREA_SEARCH_STRING = "//textarea[@name='searchWord1']";
	private static final String XPATH_ANCHOR_EXPERT_SEARCH = "//a[@class='tablink' and @title='Expert Search']";
	
	private AtomicBoolean die;
	
	public EngineeringVillageSearchProvider() {
		this.die = new AtomicBoolean(false);
	}
	
	public SearchProviderResult search(String searchString) throws SearchProviderException {
		SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
		
		try {
			// Create the web browser
			WebClient browser = new WebClient(BrowserVersion.FIREFOX_24);
			browser.getOptions().setThrowExceptionOnScriptError(false);
			browser.getOptions().setJavaScriptEnabled(true);
			browser.getOptions().setCssEnabled(false);
			browser.setAjaxController(new NicelyResynchronizingAjaxController());
			
			// Throwing an exception if the search string is invalid.
			if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
				throw new RuntimeException("Invalid search string");
			}

			// Accessing the main site page
			HtmlPage mainSitePage = browser.getPage(URL_MAIN_SITE);
			HtmlAnchor expertSearchAnchor = mainSitePage.getFirstByXPath(XPATH_ANCHOR_EXPERT_SEARCH);
			
			if(expertSearchAnchor == null) {
				result.addError(SearchProviderError.SEARCH_PROVIDER_ERROR_LOGIN_REQUIRED);
				throw new RuntimeException("Can not access provider page.");
			}
			
			// Performing the search in the expert search page
			HtmlPage expertSearchPage = expertSearchAnchor.click();
			HtmlTextArea searchStringTextArea = expertSearchPage.getFirstByXPath(XPATH_TEXTAREA_SEARCH_STRING);
			searchStringTextArea.setTextContent(searchString);
			HtmlInput searchButton = expertSearchPage.getFirstByXPath(XPATH_BUTTON_SEARCH);
			HtmlPage resultsPage = searchButton.click();
			
			// Extraction total found studies
			HtmlBold totalFoundBold = (HtmlBold) resultsPage.getByXPath(XPATH_BOLD_TOTAL_FOUND).get(0);
			int totalFound = Integer.parseInt(totalFoundBold.getTextContent().replaceAll(",", "").trim());
			result.setTotalFound(totalFound);
			
			// Selecting the studies to be download
			HtmlImage selectionOptionImage = resultsPage.getFirstByXPath(XPATH_IMG_SELECTION_OPTION);
			resultsPage = (HtmlPage) selectionOptionImage.click();
			
			HtmlListItem maximumListItem = resultsPage.getFirstByXPath(XPATH_LIST_ITEM_MAXIMUM);
			resultsPage = maximumListItem.click();
			
			HtmlButton okButton = resultsPage.getFirstByXPath("//button[@type='button' and @class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' and @role='button' and @aria-disabled='false']");
			if (okButton != null) {
				okButton.click();
			}
			
			browser.waitForBackgroundJavaScriptStartingBefore(10000);

			// Calling the download page
			HtmlAnchor downloadAnchor = resultsPage.getFirstByXPath(XPATH_ANCHOR_DOWNLOAD);
			HtmlPage exportPage = downloadAnchor.click();
			
			browser.waitForBackgroundJavaScriptStartingBefore(10000);
			
			// Selecting the export format (BIB) as well as the content format (with abstract)
			HtmlInput downloadFormatInput = exportPage.getFirstByXPath(XPATH_INPUT_DOWNLOAD);
			downloadFormatInput.setChecked(true);
			
			// Downloading the results according to the selections above
			HtmlButtonInput downloadButton = exportPage.getFirstByXPath(XPATH_BUTTON_DOWNLOAD);
			Page exportedStudiesPage = downloadButton.click();
			
			browser.waitForBackgroundJavaScript(10000);
			
			// Extract studies data
			result.getStudies().addAll(extractStudiesData(exportedStudiesPage.getWebResponse().getContentAsStream()));
			
			browser.closeAllWindows();
		} catch (Exception e) {
			if (result.getRaisedErrors().isEmpty()) {
				result.addError(SearchProviderError.SEARCH_PROVIDER_COMMON_ERROR);
			}	
			ReviewerLogger.error(e.getMessage());
			//throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return  result;
	}
	
	public void die() {
		this.die.set(true);
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
				
				if (die.get()) {
					return toReturn;
				}
				
				if (study == null) {
					study = new Study();
					study.setStatus(Study.StudyStatus.NOT_EVALUATED);
					study.setSource(SEARCH_PROVIDER_NAME);
				}
				
				if (line.startsWith("title = {")) {
					study.setTitle(line.substring(9).replaceAll("},", ""));
				}
				
				if (line.startsWith("abstract = {")) {
					study.setAbstract(line.substring(12).replaceAll("},", ""));
				}
				
				if (line.startsWith("URL = {")) {
					study.setUrl(line.substring(7).replaceAll("},", ""));
				}
				
				if (line.startsWith("year = {")) {
					study.setYear(line.substring(8).replaceAll("},", ""));
				}
				
				if (line.startsWith("author = {")) {
					for (String author : line.substring(10).replaceAll("},", "").split(",")) {
						study.addAuthor(author);
					}
				}
				
				if (line.startsWith("address = {")) {
					String[] countryInfo = line.substring(11).replaceAll("},", "").split(",");
					study.addCountry(countryInfo[countryInfo.length - 1].trim());
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
