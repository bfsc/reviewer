package br.cin.ufpe.reviewer.search.provider.scopus;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.Value;

import br.cin.ufpe.reviewer.search.provider.spi.SearchProvider;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;
import br.cin.ufpe.reviewer.search.provider.spi.expetions.SearchProviderException;

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

			// Performing the search in the advanced search page
			HtmlPage advancedSearchPage = browser.getPage("http://www.scopus.com/search/form.url?display=advanced");
			HtmlDivision searchDiv = advancedSearchPage.getFirstByXPath("//div[@id='searchfield']");
			searchDiv.setTextContent(searchString);
			HtmlSubmitInput searchButton = advancedSearchPage.getFirstByXPath("//input[@type='submit' and @value='Search']");
			HtmlPage resultsPage = searchButton.click();
			
			// Selecting all results in order export then
			HtmlCheckBoxInput checkboxInput = resultsPage.getFirstByXPath("//input[@type='checkbox' and @name='selectAllCheckBox']");
			checkboxInput.click();
			
			// Calling the export page
			HtmlAnchor exportAnchor = resultsPage.getFirstByXPath("//a[@class='jsEnabled icon export']");
			exportAnchor.setAttribute("onclick", "");
			HtmlPage exportPage = exportAnchor.click();
			
			// Selecting the export format (BIB) as well as the output informations (with abstract)
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
			
			// Exporting the results according to the selecionts above
			HtmlSubmitInput exportButton = exportPage.getFirstByXPath("//input[@type='submit' and @value='Export' and @class='jsEnabled Bold']");
			Page exportedStudiesPage = exportButton.click();
			
			// Extract studies data
			toReturn.addAll(extractStudiesData(exportedStudiesPage.getWebResponse().getContentAsStream()));
		} catch (Exception e) {
			throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
		}
		
		return  toReturn;
	}
	
	private List<Study> extractStudiesData(InputStream inputStream) {
		List<Study> toReturn = new LinkedList<Study>();
		
		try {
			BibTeXParser parser = new BibTeXParser();
			
			BibTeXDatabase bibtex = parser.parse(new InputStreamReader(inputStream));
			for (BibTeXEntry bibTeXEntry : bibtex.getEntries().values()) {
				Study study = new Study();
				
				Value titleField = bibTeXEntry.getField(new Key("title"));
				study.setTitle(titleField.toUserString());
				Value abstractField = bibTeXEntry.getField(new Key("abstract"));
				study.setAbstract(abstractField.toUserString());
				Value urlField = bibTeXEntry.getField(new Key("url"));
				study.setUrl(urlField.toUserString());
				
				toReturn.add(study);
			}
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
				buffer.append(study.getAbstract() + "\n");
				buffer.append(study.getUrl() + "\n\n");
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
