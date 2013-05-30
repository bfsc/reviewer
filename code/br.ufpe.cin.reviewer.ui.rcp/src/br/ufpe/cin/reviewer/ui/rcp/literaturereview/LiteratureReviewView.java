package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

import br.ufpe.cin.reviewer.core.literaturereview.LiteratureReviewController;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource;
import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.common.UIConstants;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class LiteratureReviewView extends BaseView {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewView";

	private java.util.List<LiteratureReview> literatureReviews;
	private LiteratureReview selectedLiteratureReview;
	
	private SashForm sash;
	private Section sectionList;
	private Composite listComposite;
	private List list;
	
	private Section sectionInfo;
	private Composite reviewInfoComposite;
	
	private Composite searchHeaderComposite;
	private Composite reviewInfoBodyComposite;
	private Composite reviewInfoFooterComposite;

	private Label titleLabel;
	private StyledText titleText;
	
	private StyledText queryStringText;
	
	private Table sourcesTable;
	
	public LiteratureReviewView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	public void refreshView() {
		LiteratureReviewController literatureReviewController = new LiteratureReviewController();
		literatureReviews = literatureReviewController.findAllLiteratureReview();
		
		list.removeAll();
		for (LiteratureReview literatureReview : literatureReviews) {
			list.add(literatureReview.getTitle());
		}
	}
	
	public void setSelectedLiteratureReview(LiteratureReview literatureReview) {
		selectedLiteratureReview = literatureReview;
		populateReviewInfo();
		sectionInfo.setVisible(true);
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createLiteratureWidgets(parent);
	}
	
	public void setFocus() {

	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - My literature reviews");
		super.form.getBody().setLayout(new GridLayout(1, false));
	}

	private void createLiteratureWidgets(Composite parent) {
		
		sash = new SashForm(form.getBody(),SWT.HORIZONTAL);
		sash.setLayout(new GridLayout(4, false));
		GridData sashLayout = new GridData(GridData.FILL_VERTICAL, GridData.FILL_HORIZONTAL);
		sashLayout.grabExcessHorizontalSpace = true;
		sashLayout.grabExcessVerticalSpace = true;
		sash.setLayoutData(sashLayout);
		sash.getMaximizedControl();
		
		//Section for List
	    sectionList = toolkit.createSection(sash, Section.NO_TITLE);
	    sectionList.setLayout(new GridLayout(1, false));
	    GridData sectionListLayout = new GridData(GridData.FILL_VERTICAL);
	    sectionListLayout.horizontalSpan = 1;
		sectionList.setLayoutData(sectionListLayout);
		
		listComposite = toolkit.createComposite(sectionList, SWT.BORDER);
		listComposite.setLayout(new GridLayout(2, false));
		listComposite.setLayoutData(new GridData());
		
		list = new List (listComposite, SWT.V_SCROLL);
		GridData listLayoutData = new GridData(GridData.FILL_VERTICAL);
		listLayoutData.horizontalSpan = 1;
		list.setLayoutData(listLayoutData);
		list.addSelectionListener(new LiteratureReviewsListHandler());
		refreshView();
		
		//Section for information
	    sectionInfo = toolkit.createSection(sash, Section.NO_TITLE);
	    sectionInfo.setLayout(new GridLayout(1, false));
		sectionInfo.setLayoutData(new GridData(GridData.FILL_BOTH));

		reviewInfoComposite = toolkit.createComposite(sectionInfo, SWT.BORDER);
		GridData reviewCompositeData = new GridData(GridData.FILL_BOTH);
		reviewCompositeData.horizontalSpan = 1;
		reviewInfoComposite.setLayoutData(reviewCompositeData);
		reviewInfoComposite.setLayout(new GridLayout(1, false));
		reviewInfoComposite.setVisible(false);
		
		
		sash.setWeights(new int[] { 1, 3});
		
		// Header composite
		searchHeaderComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		GridData searchTitleCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		searchTitleCompositeData.horizontalSpan = 2;
		searchHeaderComposite.setLayoutData(searchTitleCompositeData);
		searchHeaderComposite.setLayout(new GridLayout(2, false));

		titleLabel = toolkit.createLabel(searchHeaderComposite, "TITLE: ");
		titleLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		titleLabel.setLayoutData(new GridData());
		
		titleText = new StyledText(searchHeaderComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		this.titleText.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		this.titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.titleText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.titleText, true, true);
		
		// Body composite
		reviewInfoBodyComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		reviewInfoBodyComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		reviewInfoBodyComposite.setLayout(new GridLayout(1, false));
		
		// Query string
		Composite queryStringComposite = toolkit.createComposite(reviewInfoBodyComposite);
		queryStringComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		queryStringComposite.setLayout(new GridLayout(2, false));
		
		Label queryStringLabel = toolkit.createLabel(queryStringComposite, "QUERY STRING:");
		queryStringLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		this.queryStringText = new StyledText(queryStringComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		this.queryStringText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.queryStringText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.queryStringText, true, true);
		
		// Sources
		Composite sourcesComposite = toolkit.createComposite(reviewInfoBodyComposite);
		sourcesComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sourcesComposite.setLayout(new GridLayout(2, false));
		
		Label sourcesLabel = toolkit.createLabel(sourcesComposite, "SOURCES INFORMATIONS:");
		sourcesLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		this.sourcesTable = toolkit.createTable(sourcesComposite, SWT.BORDER | SWT.FULL_SELECTION);
		this.sourcesTable.setLinesVisible (true);
		this.sourcesTable.setHeaderVisible (true);
		this.sourcesTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		String[] titles = {"Source", "Total Found", "Total Fetched"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (this.sourcesTable, SWT.NONE);
			column.setText(titles [i]);
		}

		// Footer composite
		reviewInfoFooterComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		reviewInfoFooterComposite.setLayout(new GridLayout(2, false));
		GridData reviewFooterCompositeData = new GridData(GridData.FILL_BOTH);
		reviewFooterCompositeData.horizontalSpan = 1;
		reviewInfoFooterComposite.setLayoutData(reviewFooterCompositeData);
		
		Hyperlink studyLink = toolkit.createHyperlink(reviewInfoFooterComposite, "View studies", SWT.WRAP);
		GridData studyLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END | GridData.HORIZONTAL_ALIGN_END | GridData.GRAB_HORIZONTAL);
		studyLinkLayout.grabExcessVerticalSpace = true;
		studyLink.setLayoutData(studyLinkLayout);
		studyLink.addHyperlinkListener(new LiteratureReviewStudiesLinkHandler());
		
		Hyperlink deleteLink = toolkit.createHyperlink(reviewInfoFooterComposite, "Delete literature review", SWT.WRAP);
		GridData deleteLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END | GridData.HORIZONTAL_ALIGN_END);
		deleteLinkLayout.grabExcessVerticalSpace = true;
		deleteLink.setLayoutData(deleteLinkLayout);
		deleteLink.addHyperlinkListener(new DeleteLiteratureReviewLinkHandler());

		sectionList.setClient(listComposite);
		sectionInfo.setClient(reviewInfoComposite);
	}

	private void populateReviewInfo() {
		titleText.setText(selectedLiteratureReview.getTitle());

		queryStringText.setText(selectedLiteratureReview.getQueryString());
		
		sourcesTable.removeAll();
		
		TableItem generalItem = new TableItem (sourcesTable, SWT.NONE);
		generalItem.setText(0, "ALL");
		generalItem.setText(1, String.valueOf(selectedLiteratureReview.getTotalFound()));
		generalItem.setText(2, String.valueOf(selectedLiteratureReview.getTotalFetched()));
		
		for (LiteratureReviewSource source : selectedLiteratureReview.getSources()) {
			TableItem item = new TableItem (sourcesTable, SWT.NONE);
			item.setText(0, source.getName());
			item.setText(1, String.valueOf(source.getTotalFound()));
			item.setText(2, String.valueOf(source.getTotalFetched()));
		}
		
		for (int i=0; i < sourcesTable.getColumnCount(); i++) {
			sourcesTable.getColumn(i).pack();
		}
		
		WidgetsUtil.refreshComposite(form.getBody());
		WidgetsUtil.refreshComposite(reviewInfoBodyComposite);
	}
	
	private class LiteratureReviewsListHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = list.getSelectionIndex();
			
			if (selectionIndex >= 0) {
				selectedLiteratureReview = literatureReviews.get(selectionIndex);
				populateReviewInfo();
				reviewInfoComposite.setVisible(true);				
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	private class LiteratureReviewStudiesLinkHandler implements IHyperlinkListener {

		public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewStudiesPerspective.ID));
			
			LiteratureReviewStudiesView literatureReviewStudiesView = (LiteratureReviewStudiesView) ReviewerViewRegister.getView(LiteratureReviewStudiesView.ID);
			literatureReviewStudiesView.setLiteratureReview(selectedLiteratureReview);
		}
		
	}
	
	private class DeleteLiteratureReviewLinkHandler implements IHyperlinkListener {

		public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {

			MessageBox dialog =  new MessageBox(form.getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText("Reviewer");
			dialog.setMessage("Do you really want to delete this literature review?");
			
			int returnCode = dialog.open();
			
			if (returnCode == SWT.OK) {
				LiteratureReviewController literatureReviewController = new LiteratureReviewController();
				literatureReviewController.deleteLiteratureReview(selectedLiteratureReview);
				
				list.remove(selectedLiteratureReview.getTitle());
				literatureReviews.remove(selectedLiteratureReview);
				
				selectedLiteratureReview = null;
				sectionInfo.setVisible(false);
	
				WidgetsUtil.refreshComposite(listComposite);
				WidgetsUtil.refreshComposite(reviewInfoComposite);
			}
			
		}
		
	}
	
	private class StyleTextFocusHandler implements FocusListener {

		public void focusGained(FocusEvent e) {
			
		}

		public void focusLost(FocusEvent e) {
			StyledText sourceWidget = (StyledText) e.getSource();
			sourceWidget.setSelection(0);
		}
		
	}
	
}
