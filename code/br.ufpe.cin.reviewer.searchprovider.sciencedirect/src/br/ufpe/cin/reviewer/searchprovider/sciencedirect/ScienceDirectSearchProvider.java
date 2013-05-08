package br.ufpe.cin.reviewer.searchprovider.sciencedirect;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProvider;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
 
public class ScienceDirectSearchProvider implements SearchProvider {
       
    public static final String SEARCH_PROVIDER_NAME = "SCIENCE_DIRECT";

	private static final String DOMAIN_DL_SCIENCE_DIRECT = "http://www.sciencedirect.com/";
   
    private static final String URL_ENCODE_UTF_8 = "UTF-8";
    private static final String URL_ENCODE_ISO_8859_1 = "ISO-8859-1";
   
    private static final String SEARCH_URL_SCIENCE_DIRECT_1 = "http://www.sciencedirect.com/science?_ob=QuickSearchURL&_method=submitForm&_acct=C000228598&_origin=home&_zone=qSearch&md5=61ce8901b141d527683913a240486ac4&qs_all=";
    private static final String SEARCH_URL_SCIENCE_DIRECT_2 = "&qs_author=&qs_title=&qs_vol=&qs_issue=&qs_pages=&sdSearch=Search";

    private static final String XPATH_STUDY_TITLE_AND_URL = "//a[@class='cLink']";
    
    //private static final String XPATH_EXPORT_BIBTEX = "//input[@id='exportIcon_sci_dir']";

    private static final String X_PATH_NEXT_PAGE = "//form[@action='/science' and @style='padding:0px; margin:0px;']";
    
    private String NEXT_PAGE_link;
   
    //private static final String NEXT_PAGE_ANCHOR_TEXT = "Next &gt;";
       
    public SearchProviderResult search(String searchString) throws SearchProviderException {
    	SearchProviderResult result = new SearchProviderResult(SEARCH_PROVIDER_NAME);
           
            try {
                    // Create the web browser
                WebClient browser = new WebClient();
               
                // Throwing an exception if the search string is invalid.
                if (searchString == null || searchString.trim().equalsIgnoreCase("")) {
                        throw new RuntimeException("Invalid search string");
                }
               
                // Assemble search URL
                String searchUrl = assembleSearchUrl(searchString);
               
                // Extract studies data
                System.out.println(searchUrl);
                System.out.println("para aqui!");
                result.getStudies().addAll(extractStudiesData(browser, searchUrl));
        } catch (Exception e) {
                throw new SearchProviderException("An error occurred trying to search the following query string:" + searchString, e);
                }
               
                return  result;
               
               
 
        }
       
    private String assembleSearchUrl(String searchString) {
            String query = "";
           
            try {
                    query = URLEncoder.encode(searchString, URL_ENCODE_ISO_8859_1).toString();
            } catch (UnsupportedEncodingException e) {
                    try {
                            query = URLEncoder.encode(searchString, URL_ENCODE_UTF_8).toString();
                    } catch (UnsupportedEncodingException e2) {
                            throw new RuntimeException("An error occurred tryinf to encode the url.", e2);
                    }
            }
           
            return SEARCH_URL_SCIENCE_DIRECT_1 + query + SEARCH_URL_SCIENCE_DIRECT_2;
    }
   
    private List<Study> extractStudiesData(WebClient browser, String searchUrl) {
            List<Study> toReturn = new LinkedList<Study>();
           
            try {
                    HtmlPage page = browser.getPage(searchUrl);

                    // Extracting studies data.
                    List<?> studyTablesAnchors = page.getByXPath(XPATH_STUDY_TITLE_AND_URL);
                    String string = extractNextPageUrl(page);

                    System.out.println("tamanho da lista eh" + studyTablesAnchors.size());
                    for (int i = 0; i < studyTablesAnchors.size(); i++) {
                        System.out.println("passou aqui dentro do for!");
                            Study study = new Study();
                            study.setSource(SEARCH_PROVIDER_NAME);
                           
                            HtmlAnchor anchor = (HtmlAnchor) studyTablesAnchors.get(i);
                           
                            // Extracting study title and URL
                            study.setTitle(anchor.getTextContent().trim());
                            study.setUrl(anchor.getHrefAttribute().trim());
                            System.out.println(study.getTitle());
                            System.out.println("passou aqui!");
                        
                            toReturn.add(study);
                    }
                   
                    // Extracting the URL of next page.
                    String nextPageUrl = extractNextPageUrl(page);
                   
                    if (nextPageUrl != null) {
                            toReturn.addAll(extractStudiesData(browser, nextPageUrl));
                    }
            } 
            catch (Exception e) {
                    //TRATAR EXCECAO
                        e.printStackTrace();
            }
            return toReturn;
        }
 
    private String extractNextPageUrl(HtmlPage page) throws IOException {
        String toReturn = null;

        System.out.println(page.getUrl());
        HtmlForm nextPageForm = (HtmlForm) page.getFirstByXPath(X_PATH_NEXT_PAGE);
        
        if (nextPageForm != null) {
        	//recolhe todos os parametros para montar a url da proxima pagina
    		//HtmlForm nextPageForm = (HtmlForm)object;
    		
    		HtmlInput _ob = (HtmlInput) nextPageForm.getInputByName("_ob");
    		String value__ob = "_ob=" + _ob.getAttribute("value") + "&";
    		
    		HtmlInput _method = (HtmlInput) nextPageForm.getInputByName("_method");
    		String value__method = "_method=" + _method.getAttribute("value") + "&";

    		
    		HtmlInput searchtype = (HtmlInput) nextPageForm.getInputByName("searchtype");
    		String value_searchtype = "searchtype=" + searchtype.getAttribute("value") + "&";

    		
    		HtmlInput refSource = (HtmlInput) nextPageForm.getInputByName("refSource");
    		String value_refSource = "refSource=" + refSource.getAttribute("value") + "&";
    		
    		
    		//HtmlInput pdfDownloadSort = (HtmlInput) nextPageForm.getInputByName("pdfDownloadSort");
    		//String value_pdfDownloadSort = "pdfDownloadSort=" + pdfDownloadSort.getAttribute("value") + "&";
    		
    		
    		//HtmlInput PDF_DDM_MAX = (HtmlInput) nextPageForm.getInputByName("PDF_DDM_MAX");
    		//String value_PDF_DDM_MAX = "PDF_DDM_MAX=" + PDF_DDM_MAX.getAttribute("value") + "&";
    		
    		HtmlInput _st = (HtmlInput) nextPageForm.getInputByName("_st");
    		String value__st = "_st=" + _st.getAttribute("value") + "&";
    		
    		HtmlInput count = (HtmlInput) nextPageForm.getInputByName("count");
    		String value_count = "count=" + count.getAttribute("value") + "&";
    		
    		HtmlInput sort = (HtmlInput) nextPageForm.getInputByName("sort");
    		String value_sort = "sort=" + sort.getAttribute("value") + "&";
    		
    		HtmlInput _chunk = (HtmlInput) nextPageForm.getInputByName("_chunk");
    		String value__chunk = "_chunk=" + _chunk.getAttribute("value") + "&";
    		
    		HtmlInput NEXT_LIST;
    		try{
    			NEXT_LIST = (HtmlInput) nextPageForm.getInputByName("NEXT_LIST");
    		}catch(ElementNotFoundException e){
    			return null;
    		}
    		String value_NEXT_LIST = "NEXT_LIST=" + NEXT_LIST.getAttribute("value") + "&";

    		HtmlInput view = (HtmlInput) nextPageForm.getInputByName("view");
    		String value_view = "view=" + view.getAttribute("value") + "&";
    		
    		HtmlInput md5 = (HtmlInput) nextPageForm.getInputByName("md5");
    		String value_md5 = "md5=" + md5.getAttribute("value") + "&";
    		
    		HtmlInput _ArticleListID = (HtmlInput) nextPageForm.getInputByName("_ArticleListID");
    		String value__ArticleListID = "_ArticleListID=" + _ArticleListID.getAttribute("value") + "&";
    		
    		HtmlInput sisr_search = (HtmlInput) nextPageForm.getInputByName("sisr_search");
    		String value_sisr_search = "sisr_search=" + sisr_search.getAttribute("value") + "&";
    		
    		HtmlInput TOTAL_PAGES = (HtmlInput) nextPageForm.getInputByName("TOTAL_PAGES");
    		String value_TOTAL_PAGES = "TOTAL_PAGES=" + TOTAL_PAGES.getAttribute("value") + "&";
    		
    		HtmlInput topPaginationBoxChanged = (HtmlInput) nextPageForm.getInputByName("topPaginationBoxChanged");
    		String value_topPaginationBoxChanged = "topPaginationBoxChanged=" + topPaginationBoxChanged.getAttribute("value") + "&";
    		
    		HtmlInput pageNumberTop = (HtmlInput) nextPageForm.getInputByName("pageNumberTop");
    		String value_pageNumberTop = "pageNumberTop=" + pageNumberTop.getAttribute("value") + "&";
    		
    		HtmlInput topNext = (HtmlInput) nextPageForm.getInputByName("topNext");
    		String value_topNext = "topNext=" + URLEncoder.encode(topNext.getAttribute("value"), URL_ENCODE_UTF_8).toString() + "&";
    		
    		HtmlInput sisrterm = (HtmlInput) nextPageForm.getInputByName("sisrterm");
    		String value_sisrterm = "sisrterm=" + sisrterm.getAttribute("value") + "&";
    		
    		//HtmlInput pdfDownload = (HtmlInput) nextPageForm.getInputByName("pdfDownload");
    		//String value_pdfDownload = "pdfDownload=" + pdfDownload.getAttribute("value") + "&";
    		
    		HtmlInput bottomPaginationBoxChanged = (HtmlInput) nextPageForm.getInputByName("bottomPaginationBoxChanged");
    		String value_bottomPaginationBoxChanged = "bottomPaginationBoxChanged=" + bottomPaginationBoxChanged.getAttribute("value") + "&";
    		
    		HtmlInput pageNumberBottom = (HtmlInput) nextPageForm.getInputByName("pageNumberBottom");
    		String value_pageNumberBottom = "pageNumberBottom=" + pageNumberBottom.getAttribute("value") + "&";
    		
    		HtmlInput displayPerPageFlag = (HtmlInput) nextPageForm.getInputByName("displayPerPageFlag");
    		String value_displayPerPageFlag = "displayPerPageFlag=" + displayPerPageFlag.getAttribute("value") + "&";
    		
    		//HtmlInput resultsPerPage = (HtmlInput) nextPageForm.getInputByName("resultsPerPage");
    		//String value_resultsPerPage = "resultsPerPage=" + resultsPerPage.getAttribute("value");
    		
    		//monta a url da proxima pagina de busca
    		
    		NEXT_PAGE_link = DOMAIN_DL_SCIENCE_DIRECT + "science?" + value__ob + value__method + value_searchtype + value_refSource + value__st + value_count + value_sort + value__chunk + value_NEXT_LIST + value_view + value_md5 + value__ArticleListID + value_sisr_search + value_TOTAL_PAGES + value_topPaginationBoxChanged + value_pageNumberTop + value_topNext + value_sisrterm + value_bottomPaginationBoxChanged + value_pageNumberBottom + value_displayPerPageFlag + "resultsPerPage=25";

    		System.out.println("este eh o link para a proxima pagina");
    		System.out.println(NEXT_PAGE_link);
        		
        		toReturn = NEXT_PAGE_link;
        }
       
        return toReturn;
    }
       
}
