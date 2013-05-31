package br.ufpe.cin.reviewer.ui.rcp.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import br.ufpe.cin.reviewer.core.literaturereview.LiteratureReviewController;
import br.ufpe.cin.reviewer.core.search.SearchController;
import br.ufpe.cin.reviewer.core.search.SearchFilter;
import br.ufpe.cin.reviewer.core.search.SearchResult;
import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource.SourceType;
import br.ufpe.cin.reviewer.searchprovider.extensions.SearchProviderExtensionsRegistry;
import br.ufpe.cin.reviewer.searchprovider.spi.SearchProviderResult;
import br.ufpe.cin.reviewer.searchprovider.spi.exceptions.SearchProviderError;
import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewPerspective;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewView;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class SearchView extends BaseView {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.search.SearchView";
	
	String searchString;
	private SearchComposite searchComposite;
	private ResultComposite resultComposite;
	
	// CONSTRUCTORS ============================================================
	
	public SearchView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	// PUBLIC METHODS ==========================================================	
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createSearchWidgets(parent);
		createResultWidgets();
	}

	public void setFocus() {
		
	}
	
	// PRIVATE METHODS =========================================================
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Search studies");
		super.form.getBody().setLayout(new GridLayout(1, false));
	}

	private void createSearchWidgets(Composite parent) {
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
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
		
		private static final String SEARCH_TEXT_DEFAULT_VALUE = "Type your query string here...";
		
		private Button searchStopButton;
		private boolean stop = false;
		
		private Text searchText;
		private Composite checkBoxesComposite;
		
		private List<Button> searchProvidersCheckBoxes = new ArrayList<Button>();
		
		private br.ufpe.cin.reviewer.ui.rcp.search.SearchView.SearchComposite.SearchButtonHandler.AsyncSearchJob searchJob;
		
		public SearchComposite(Composite parent, int style) {
			super(parent, style);
			
			TableWrapData td = new TableWrapData();
			
			searchText = toolkit.createText(this, SEARCH_TEXT_DEFAULT_VALUE, SWT.WRAP);
			searchText.addFocusListener(new SearchTextHandler());
			td = new TableWrapData(TableWrapData.FILL_GRAB);
			td.heightHint = 80;
			searchText.setLayoutData(td);
			
			td = new TableWrapData();
			this.searchStopButton = toolkit.createButton(this, "Search", SWT.PUSH);
			td.heightHint = 40;
			this.searchStopButton.setLayoutData(td);
			this.searchStopButton.addSelectionListener(new SearchButtonHandler());
			
			List<IConfigurationElement> configs = SearchProviderExtensionsRegistry.getConfigElements();
			Collections.sort(configs, new SearchProviderConfiguratorElementComparator());
			checkBoxesComposite = toolkit.createComposite(this);
			checkBoxesComposite.setLayout(new GridLayout(configs.size(), false));
			td = new TableWrapData();
			checkBoxesComposite.setLayoutData(td);
			
			for (IConfigurationElement config : configs) {
				Button checkBox = toolkit.createButton(checkBoxesComposite, config.getAttribute("friendly.name"), SWT.CHECK);
				checkBox.setData(config.getAttribute("key"));
				searchProvidersCheckBoxes.add(checkBox);
			}
		}
		
		private class SearchButtonHandler implements SelectionListener {

			private IProgressMonitor progressMonitor;
			
			public void widgetSelected(SelectionEvent e) {
				if (stop) {
					searchJob.cancelJob();
					searchStopButton.setText("Search");
					stop = false;
					WidgetsUtil.refreshComposite(form.getBody());					
				} else {
					searchStopButton.setText("Cancel");
					stop = true;
					
					if (searchText.getText().equals(SEARCH_TEXT_DEFAULT_VALUE)) {
						searchText.setText("");
					}
					
					searchString = searchText.getText().trim();
					
					boolean anySearchProviderSelected = false;
					for (Button checkBoxes : searchProvidersCheckBoxes) {
						anySearchProviderSelected |= checkBoxes.getSelection();
					}
					
					if (searchString.trim().isEmpty()) {
						SearchView.super.form.setMessage("You must inform a query string to search.", IMessageProvider.ERROR);
					} else if (!anySearchProviderSelected) {
						SearchView.super.form.setMessage("You must select at least one source to search.", IMessageProvider.ERROR);
					} else {
						SearchView.super.form.setMessage("", IMessageProvider.NONE);
						
						SearchFilter searchFilter = new SearchFilter();
						
						for (Button checkBox : searchProvidersCheckBoxes) {
							if (checkBox.getSelection()) {
								searchFilter.addSearchProviderKey((String) checkBox.getData());
							}
						}
						
						IActionBars bars = SearchView.this.getViewSite().getActionBars();
						IStatusLineManager statusLine = bars.getStatusLineManager();
						this.progressMonitor = statusLine.getProgressMonitor();
						this.progressMonitor.beginTask("Searching... This may take several minutes.", IProgressMonitor.UNKNOWN);
						
						searchJob = new AsyncSearchJob(searchString, searchFilter);
						searchJob.schedule();
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
			private class AsyncSearchJob extends Job {

				private SearchController searchController = new SearchController();
				
				private String searchString;
				private SearchFilter searchFilter;
				
				private SearchResult searchResult;
				
				public AsyncSearchJob(String searchString, SearchFilter searchFilter) {
					super("AsyncSearchJob");
					this.searchString = searchString;
					this.searchFilter = searchFilter;
				}

				protected IStatus run(IProgressMonitor monitor) {
					this.searchResult = this.searchController.search(searchString, searchFilter, true);
					
					Display.getDefault().asyncExec(new Runnable() {
						
						public void run() {
							SearchView.this.resultComposite.setSearchResult(searchJob.getSearchResult());
							SearchView.this.resultComposite.getTable().setFocus();
							progressMonitor.done();
							
							stop = false;
							searchStopButton.setText("Search");
							WidgetsUtil.refreshComposite(form.getBody());
						}
					});
					
					return Status.OK_STATUS;
				}
				
				public void cancelJob() {
					progressMonitor.done();
					this.searchController.stopSearch();
					this.getThread().interrupt();
				}

				public SearchResult getSearchResult() {
					return searchResult;
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
		
		private class SearchProviderConfiguratorElementComparator implements Comparator<IConfigurationElement> {

			public int compare(IConfigurationElement config1, IConfigurationElement config2) {
				return config1.getAttribute("friendly.name").compareTo(config2.getAttribute("friendly.name"));
			}
			
		}
		
	}

	private class ResultComposite extends Composite {

		private SearchResult searchResult;
		
		private Table table;
		private Composite resultCompositeLabels;
		private Label labelTotalFound;
		private Label labelTotalFetched;
		private Hyperlink errorsLink;
		
		public ResultComposite(Composite parent, int style) {
			super(parent, style);
			
			//Composite for labels
			resultCompositeLabels = toolkit.createComposite(this);
			resultCompositeLabels.setLayout(new GridLayout(2, true));
			resultCompositeLabels.setLayoutData(new GridData());

			labelTotalFound = toolkit.createLabel(resultCompositeLabels, "Total Found:");
			GridData totalFoundLayout = new GridData();
			totalFoundLayout.horizontalSpan = 1;
			labelTotalFound.setLayoutData(totalFoundLayout);
			
			labelTotalFetched = toolkit.createLabel(resultCompositeLabels, "Total Found:");
			GridData totalFetchedLayout = new GridData();
			totalFetchedLayout.horizontalSpan = 1;
			labelTotalFetched.setLayoutData(totalFetchedLayout);
			
			table = toolkit.createTable(this, SWT.BORDER | SWT.FULL_SELECTION);
			table.setLinesVisible (true);
			table.setHeaderVisible (true);
			GridData tableLayoutData = new GridData(GridData.FILL_BOTH);
			table.setLayoutData(tableLayoutData);
	
			Composite c = toolkit.createComposite(this);
			c.setLayout(new GridLayout(2, false));
			GridData td = new GridData(GridData.HORIZONTAL_ALIGN_END);
			c.setLayoutData(td);
			errorsLink = toolkit.createHyperlink(c, "Some errors occurred at search. See the details here...", SWT.NONE);			
			errorsLink.addHyperlinkListener(new ShowErrorsLinkHandler()); 
			Hyperlink studyLink = toolkit.createHyperlink(c, "Create new literature review from these results...", SWT.NONE);			
			studyLink.addHyperlinkListener(new CreateLiteratureReviewLinkHandler());
		}
		
		public Table getTable(){
			return this.table;
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
				
				if (study.getSource() != null) {
					item.setText(1, study.getSource());
				}
				
				if (study.getTitle() != null) {
					item.setText(2, study.getTitle());
				}
				
				String authors = "";
				for (String author : study.getAuthors()) {
					authors += author + ",";
				}
				
				item.setText(3, authors);

				if (study.getYear() != null) {
					item.setText(4, study.getYear());
				}
			}
			
			labelTotalFound.setText("Total Found: " + searchResult.getTotalFound());
			labelTotalFetched.setText("Total Fetched: " + searchResult.getTotalFetched());
			
			WidgetsUtil.refreshComposite(resultCompositeLabels);
			
			for (int i=0; i<titles.length; i++) {
				table.getColumn (i).pack ();
			}
			
			WidgetsUtil.refreshComposite(form.getBody());
			
			if (!this.hasSearchProviderErrors()) {
				errorsLink.setVisible(false);
			} else {
				errorsLink.setVisible(true);
			}
			
			resultComposite.setVisible(true);
		}
		
		private boolean hasSearchProviderErrors() {
			boolean ret = false;
			for (SearchProviderResult result : searchResult.getSearchProviderResults()) {
				if (result.getRaisedErrors().size() > 0) {
					ret = true;
					break;
				}
			}
			return ret;
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
					if(searchString != null) {
						literatureReview.setQueryString(searchString);
					}
					
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
					
					LiteratureReviewView literatureReviewView = (LiteratureReviewView) ReviewerViewRegister.getView(LiteratureReviewView.ID);
					if (literatureReviewView != null) {
						literatureReviewView.setSelectedLiteratureReview(literatureReview);
						literatureReviewView.refreshView();
					}
				}
			}
		}
		
		private class ShowErrorsLinkHandler implements IHyperlinkListener {

			public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				
			}

			public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				
			}

			public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				ListDialog dialog = new ListDialog(shell);
				dialog.setContentProvider(new ArrayContentProvider());
				dialog.setTitle("Search Providers Erros");
				dialog.setMessage("Search providers erros:");
				dialog.setLabelProvider(new ArrayLabelProvider());
				dialog.setAddCancelButton(false);
				List<String[]> input = new ArrayList<String[]>();
				
				for (SearchProviderResult result : searchResult.getSearchProviderResults()) {

					for (SearchProviderError error:  result.getRaisedErrors()) {
						switch (error) {
						case SEARCH_PROVIDER_COMMON_ERROR:
							input.add(new String[]{result.getSearchProviderName()+ " - Search provider common error."});
							break;
						case SEARCH_PROVIDER_ERROR_LOGIN_REQUIRED:
							input.add(new String[]{result.getSearchProviderName()+ " - Login required to this provider."});
							break;
						case SEARCH_PROVIDER_ERROR_FORBIDDEN:
							input.add(new String[]{result.getSearchProviderName()+ " - 403 Forbidden."});
							break;
						default:
							break;
						}
					}
				}		
				
				dialog.setInput(input);
				dialog.setWidthInChars(70);
				dialog.open();

			}

			private class ArrayLabelProvider extends LabelProvider implements ITableLabelProvider{
				public String getText(Object element) {
					return ((String[]) element)[0].toString();
				}

				@Override
				public Image getColumnImage(Object element, int columnIndex) {
					return null;
				}

				@Override
				public String getColumnText(Object element, int columnIndex) {
					String[] ss = (String[]) element;
					return ss[columnIndex];
				}

			}
		}
		
	}
	
}
