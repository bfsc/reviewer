package br.ufpe.cin.reviewer.ui.rcp.search;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
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
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource.SourceType;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.ui.rcp.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewStudiesPerspective;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewPerspective;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewStudiesView;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewView;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class SearchView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.search.SearchView";
	
	private FormToolkit toolkit;
	private Form form;
	
	private SearchComposite searchComposite;
	private ResultComposite resultComposite;
	
	private int totalFound;
	
	// CONSTRUCTORS ============================================================
	
	public SearchView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	// PUBLIC METHODS ==========================================================	
	
	public void createPartControl(Composite parent) {
		configureView(parent);
		createSearchWidgets(parent);
		createResultWidgets();
	}

	public void setFocus() {
		
	}
	
	// PRIVATE METHODS =========================================================
	
	private void configureView(Composite parent) {
		this.toolkit = new FormToolkit(parent.getDisplay());
		this.form = toolkit.createForm(parent);
		this.toolkit.decorateFormHeading(this.form);
		this.form.setText("Search Studies");
		this.form.getBody().setLayout(new GridLayout(1, false));
	}

	private void createSearchWidgets(Composite parent) {
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 7;
		searchComposite = new SearchComposite(section, SWT.NONE);
		searchComposite.setLayout(layout);
		
		section.setClient(searchComposite);
	}
	
	private void createResultWidgets(){
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		resultComposite = new ResultComposite(section, SWT.NONE);
		resultComposite.setLayout(new GridLayout(1, true));
		resultComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		resultComposite.setVisible(false);

		section.setClient(resultComposite);
	}

	// PRIVATE CLASSES =========================================================
	
	private class SearchComposite extends Composite {
		
		private static final String SEARCH_TEXT_DEFAULT_VALUE = "Type your text here...";
		
		private Text searchText;
		private Button acmCheckBox;
		private Button ieeeCheckBox;
		private Button scienceDirectCheckBox;
		private Button scopusCheckBox;
		private Button springerLinkCheckBox;
		private Button engineeringVillageCheckBox;
	
		public SearchComposite(Composite parent, int style) {
			super(parent, style);
			
			TableWrapData td = new TableWrapData();
			
			searchText = toolkit.createText(this, SEARCH_TEXT_DEFAULT_VALUE);
			searchText.addFocusListener(new SearchTextHandler());
			td = new TableWrapData(TableWrapData.FILL_GRAB);
			td.heightHint = 80;
			td.colspan = 6;
			searchText.setLayoutData(td);
			
			td = new TableWrapData();
			Button search = toolkit.createButton(this, "Search", SWT.PUSH);
			td.heightHint = 40;
			td.colspan = 1;
			search.setLayoutData(td);
			search.addSelectionListener(new SearchButtonHandler());
			
			acmCheckBox = toolkit.createButton(this,"ACM", SWT.CHECK);
			td = new TableWrapData();
			td.colspan = 1;
			acmCheckBox.setLayoutData(td);
			
			ieeeCheckBox = toolkit.createButton(this,"IEEE", SWT.CHECK);
			td = new TableWrapData();
			td.colspan = 1;
			ieeeCheckBox.setLayoutData(td);
			
			scienceDirectCheckBox = toolkit.createButton(this,"Science Direct", SWT.CHECK);
			td = new TableWrapData();
			td.colspan = 1;
			scienceDirectCheckBox.setLayoutData(td);
			
			scopusCheckBox = toolkit.createButton(this,"Scopus", SWT.CHECK);
			td = new TableWrapData();
			td.colspan = 1;
			scopusCheckBox.setLayoutData(td);
			
			springerLinkCheckBox = toolkit.createButton(this,"Springer Link", SWT.CHECK);
			td = new TableWrapData();
			td.colspan = 1;
			springerLinkCheckBox.setLayoutData(td);
			
			engineeringVillageCheckBox = toolkit.createButton(this,"Engineering Village", SWT.CHECK);
			td = new TableWrapData();
			td.colspan = 2;
			engineeringVillageCheckBox.setLayoutData(td);
		}
		
		private class SearchButtonHandler implements SelectionListener {

			public void widgetSelected(SelectionEvent e) {
				if (searchText.getText().equals(SEARCH_TEXT_DEFAULT_VALUE)) {
					searchText.setText("");
				}
				
				String searchString = searchText.getText();
			
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
				
				new AsyncSearchJob(searchString, searchFilter).schedule();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
			private class AsyncSearchJob extends Job {

				private String searchString;
				private SearchFilter searchFilter;
				
				private SearchResult searchResult;
				
				private IProgressMonitor progressMonitor;
				
				public AsyncSearchJob(String searchString, SearchFilter searchFilter) {
					super("AsyncSearchJob");
					this.searchString = searchString;
					this.searchFilter = searchFilter;
				}

				protected IStatus run(IProgressMonitor monitor) {
					Display.getDefault().asyncExec(new Runnable() {
						
						public void run() {
							IActionBars bars = SearchView.this.getViewSite().getActionBars();
							IStatusLineManager statusLine = bars.getStatusLineManager();
							AsyncSearchJob.this.progressMonitor = statusLine.getProgressMonitor();
							
							AsyncSearchJob.this.progressMonitor.beginTask("Searching... This may take several minutes.", IProgressMonitor.UNKNOWN);
						}
					});
					
					SearchController searchController = new SearchController();
					AsyncSearchJob.this.searchResult = searchController.search(searchString, searchFilter);
					
					Display.getDefault().asyncExec(new Runnable() {
						
						public void run() {
							SearchView.this.resultComposite.setSearchResult(AsyncSearchJob.this.searchResult);
							progressMonitor.done();
						}
					});
					
					return Status.OK_STATUS;
				}
				
			}
			
		}
		
		private class SearchTextHandler implements FocusListener {

			public void focusGained(FocusEvent e) {
				if (searchText.getText().equals(SEARCH_TEXT_DEFAULT_VALUE)) {
					searchText.setText("");
				}
			}
			public void focusLost(FocusEvent e) {
				
			}
			
		}
		
	}

	private class ResultComposite extends Composite {

		private SearchResult searchResult;
		
		private Table table;
		private Composite resultCompositeLabels;
		private Label labelTotalFound;
		private Label labelTotalFetched;
		
		public ResultComposite(Composite parent, int style) {
			super(parent, style);
			
			//Composite for labels
			resultCompositeLabels = toolkit.createComposite(this);
			resultCompositeLabels.setLayout(new GridLayout(2, true));
			resultCompositeLabels.setLayoutData(new GridData());

			labelTotalFound = toolkit.createLabel(resultCompositeLabels, "Total Found:" + totalFound);
			GridData totalFoundLayout = new GridData();
			totalFoundLayout.horizontalSpan = 1;
			labelTotalFound.setLayoutData(totalFoundLayout);
			
			labelTotalFetched = toolkit.createLabel(resultCompositeLabels, "Total Found:" + totalFound);
			GridData totalFetchedLayout = new GridData();
			totalFetchedLayout.horizontalSpan = 1;
			labelTotalFetched.setLayoutData(totalFetchedLayout);
			
			table = toolkit.createTable(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
			table.setLinesVisible (true);
			table.setHeaderVisible (true);
			GridData tableLayoutData = new GridData(GridData.FILL_BOTH);
			table.setLayoutData(tableLayoutData);
			
			Hyperlink studyLink = toolkit.createHyperlink(this, "Create new literature review from these results...", SWT.WRAP);
			GridData studyLinkLayout = new GridData(GridData.HORIZONTAL_ALIGN_END);
			studyLink.setLayoutData(studyLinkLayout);
			studyLink.addHyperlinkListener(new CreateLiteratureReviewLinkHandler());
		}
		
		public void setSearchResult(SearchResult searchResult) {
			this.searchResult = searchResult;
			
			table.removeAll();
			
			String[] titles = {"N#", "Source", "Title", "Authors", "Year"};
			for (int i=0; i<titles.length; i++) {
				TableColumn column = new TableColumn (table, SWT.NONE);
				column.setText (titles [i]);
			}

			int currentStudyNumber = 0;
			for (Study study : searchResult.getAllStudies()) {
				currentStudyNumber++;
				
				TableItem item = new TableItem (table, SWT.NONE);
				item.setText (0, String.valueOf(currentStudyNumber));
				item.setText (1, study.getSource());
				item.setText (2, study.getTitle());
				
				String authors = "";
				for (String author : study.getAuthors()) {
					authors += author + ",";
				}
				item.setText (3, authors);
				item.setText (4, study.getYear());
			}
			
			totalFound = currentStudyNumber;
			labelTotalFound.setText("Total Found: " + totalFound);
			labelTotalFetched.setText("Total Fetched: " + totalFound);
			
			WidgetsUtil.refreshComposite(resultCompositeLabels);
			
			for (int i=0; i<titles.length; i++) {
				table.getColumn (i).pack ();
			}
			
			WidgetsUtil.refreshComposite(form.getBody());
			
			resultComposite.setVisible(true);
		}
		
		private class CreateLiteratureReviewLinkHandler implements IHyperlinkListener {

			public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				
			}

			public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				
			}

			public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				InputDialog dialog = new InputDialog(shell, "Create literature review", "Literature review title", null, null);
				dialog.open();
				
				if (dialog.getReturnCode() == InputDialog.OK) {
					LiteratureReview literatureReview = new LiteratureReview();
					
					literatureReview.setTitle(dialog.getValue());
					
					// Adding studies to the literature review
					int studyCounter = 1;
					for (Study study : searchResult.getAllStudies()) {
						study.setCode("S" + studyCounter);
						literatureReview.addStudy(study);
						studyCounter++;
					}
					
					// Adding sources to the literature review
					for (SearchProviderResult result : searchResult.getSearchProviderResults()) {
						LiteratureReviewSource source = new LiteratureReviewSource();
						source.setName(result.getSearchProviderName());
						source.setTotalFound(result.getTotalFound());
						source.setTotalFetched(result.getTotalFetched());
						source.setType(SourceType.AUTOMATIC);
						literatureReview.addSource(source);
					}
					
					LiteratureReviewController literatureReviewController = new LiteratureReviewController();
					literatureReviewController.createLiteratureReview(literatureReview);
					
					IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
					IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewPerspective.ID));
					
//					LiteratureReviewStudiesView literatureReviewStudiesView = (LiteratureReviewStudiesView) ReviewerViewRegister.getView(LiteratureReviewStudiesView.ID);
//					literatureReviewStudiesView.setLiteratureReview(literatureReview);
					
					LiteratureReviewView literatureReviewView = (LiteratureReviewView) ReviewerViewRegister.getView(LiteratureReviewView.ID);
					literatureReviewView.setSelectedLiteratureReview(literatureReview);
					if (literatureReviewView != null) {
						literatureReviewView.refreshView();
					}
				}
			}
		}
		
	}
	
}
