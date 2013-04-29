package br.ufpe.cin.reviewer.ui.rcp.search;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import br.ufpe.cin.reviewer.core.literaturereview.LiteratureReviewController;
import br.ufpe.cin.reviewer.core.search.SearchController;
import br.ufpe.cin.reviewer.core.search.SearchFilter;
import br.ufpe.cin.reviewer.core.search.SearchResult;
import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class SearchView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.search.SearchView";
	
	private FormToolkit toolkit;
	private Form form;
	
	//Results Widgets
	private Table table;
	private Composite resultCompositeLabels;
	private Composite resultCompositeTable;
	private Label labelTotalFound;
	private Label labelTotalFetched;

	//Search Widgets
	private Text searchText;
	private Button acmCheckBox;
	private Button ieeeCheckBox;
	private Button scienceDirectCheckBox;
	private Button scopusCheckBox;
	private Button springerLinkCheckBox;
	private Button engineeringVillageCheckBox;
	
	private int totalFound;
	
	private SearchResult searchResult;
	
	public void createPartControl(Composite parent) {
		configureView(parent);
		createSearchWidgets(parent);
		createResultWidgets();
	}

	private void configureView(Composite parent) {
		this.toolkit = new FormToolkit(parent.getDisplay());
//		this.form = toolkit.createScrolledForm(parent);
		this.form = toolkit.createForm(parent);
		this.toolkit.decorateFormHeading(this.form);
		this.form.setText("Reviewer");
		this.form.getBody().setLayout(new GridLayout(1, false));
	}

	private void createSearchWidgets(Composite parent) {
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
//	    GridData sectionLayoutData = new GridData(GridData.FILL_HORIZONTAL);
//	    sectionLayoutData.horizontalIndent = 20;
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Composite searchComposite = this.toolkit.createComposite(section);
		
		TableWrapLayout layout = new TableWrapLayout();
		TableWrapData td = new TableWrapData();
		searchComposite.setLayout(layout);
		layout.numColumns = 7;
		
		searchText = toolkit.createText(searchComposite, "Type your text here...");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = 80;
		td.colspan = 6;
		searchText.setLayoutData(td);
		
		td = new TableWrapData();
		Button search = toolkit.createButton(searchComposite, "Search", SWT.PUSH);
		td.heightHint = 40;
		td.colspan = 1;
		search.setLayoutData(td);
		search.addSelectionListener(new SearchButtonHandler());
		
		acmCheckBox = toolkit.createButton(searchComposite,"ACM", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		acmCheckBox.setLayoutData(td);
		
		ieeeCheckBox = toolkit.createButton(searchComposite,"IEEE", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		ieeeCheckBox.setLayoutData(td);
		
		scienceDirectCheckBox = toolkit.createButton(searchComposite,"Science Direct", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		scienceDirectCheckBox.setLayoutData(td);
		
		scopusCheckBox = toolkit.createButton(searchComposite,"Scopus", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		scopusCheckBox.setLayoutData(td);
		
		springerLinkCheckBox = toolkit.createButton(searchComposite,"Springer Link", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		springerLinkCheckBox.setLayoutData(td);
		
		engineeringVillageCheckBox = toolkit.createButton(searchComposite,"Engineering Village", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 2;
		engineeringVillageCheckBox.setLayoutData(td);
		
		section.setClient(searchComposite);
	}
	
	private void createResultWidgets(){
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
//	    GridData sectionLayoutData = new GridData(GridData.FILL_BOTH);
//	    sectionLayoutData.horizontalIndent = 20;
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//Composite for Table and Link
		resultCompositeTable = toolkit.createComposite(section);
		resultCompositeTable.setLayout(new GridLayout(1, true));
		resultCompositeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		resultCompositeTable.setVisible(false);
		
		//Composite for labels
		resultCompositeLabels = toolkit.createComposite(resultCompositeTable);
		resultCompositeLabels.setLayout(new GridLayout(2, true));
		resultCompositeLabels.setLayoutData(new GridData());
		resultCompositeLabels.setVisible(false);

		labelTotalFound = toolkit.createLabel(resultCompositeLabels, "Total Found:" + totalFound);
		GridData totalFoundLayout = new GridData();
		totalFoundLayout.horizontalSpan = 1;
		labelTotalFound.setLayoutData(totalFoundLayout);
		
		labelTotalFetched = toolkit.createLabel(resultCompositeLabels, "Total Found:" + totalFound);
		GridData totalFetchedLayout = new GridData();
		totalFetchedLayout.horizontalSpan = 1;
		labelTotalFetched.setLayoutData(totalFetchedLayout);
		
		table = toolkit.createTable(resultCompositeTable, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData tableLayoutData = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(tableLayoutData);
		
		Hyperlink studyLink = toolkit.createHyperlink(resultCompositeTable, "Create new study from these results...", SWT.WRAP);
		GridData studyLinkLayout = new GridData(GridData.HORIZONTAL_ALIGN_END);
		studyLink.setLayoutData(studyLinkLayout);
		studyLink.addHyperlinkListener(new CreateLiteratureReviewLinkHandler());

		section.setClient(resultCompositeTable);
	}
	
	public void addResults(SearchResult searchResult){

		table.removeAll();
		
		String[] titles = {"N#", "Title", "Source", "Authors", "Year"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}

		int currentStudyNumber = 0;
		for (String searchProviderKey : searchResult.getAllStudies().keySet()) {
			List<Study> studies = searchResult.getAllStudies().get(searchProviderKey);
			for (Study study : studies) {
				currentStudyNumber++;
				
				TableItem item = new TableItem (table, SWT.NONE);
				item.setText (0, String.valueOf(currentStudyNumber));
				item.setText (1, study.getTitle());
				item.setText (2, searchProviderKey);
				item.setText (3, "");
				item.setText (4, "");
			}
		}
		totalFound = currentStudyNumber;
		labelTotalFound.setText("Total Found: " + totalFound);
		labelTotalFetched.setText("Total Fetched: " + totalFound);
		
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		
		WidgetsUtil.refreshComposite(this.form.getBody());
		resultCompositeLabels.setVisible(true);
		resultCompositeTable.setVisible(true);
		
	}

	public void setFocus() {
		
	}
	
	private class SearchButtonHandler implements SelectionListener{

		public void widgetSelected(SelectionEvent e) {
			SearchController searchController = new SearchController();
			
			SearchFilter searchFilter = new SearchFilter();
			if (acmCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("ACM");
			}
			
			if (ieeeCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("IEEE");
			}
			
			if (scienceDirectCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("SCIENCE_DIRECT");
			}
			
			if (springerLinkCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("SPRINGER_LINK");
			}
			
			if (scopusCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("SCOPUS");
			}

			if (engineeringVillageCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("ENGINEERING_VILLAGE");
			}
			
			SearchView.this.searchResult = searchController.search(searchText.getText(), searchFilter);
			addResults(searchResult);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}

	private class CreateLiteratureReviewLinkHandler implements IHyperlinkListener {

		public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			LiteratureReview literatureReview = new LiteratureReview();
			literatureReview.setTitle("");
			for (String searchProviderKey : searchResult.getAllStudies().keySet()) {
				List<Study> studies = searchResult.getAllStudies().get(searchProviderKey);
				for (Study study : studies) {
					study.setSource(searchProviderKey);
					literatureReview.addStudy(study);
				}
			}
			
			LiteratureReviewController literatureReviewController = new LiteratureReviewController();
			literatureReviewController.createLiteratureReview(literatureReview);
		}
	}
}
