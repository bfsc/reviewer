package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import br.ufpe.cin.reviewer.core.literaturereview.LiteratureReviewController;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource;
import br.ufpe.cin.reviewer.ui.rcp.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class LiteratureReviewView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewView";

	private java.util.List<LiteratureReview> literatureReviews;
	private LiteratureReview selectedLiteratureReview;
	
	private FormToolkit toolkit;
	private Form form;
	
	private Section sectionList;
	private Composite listComposite;
	private List list;
	
	private Section sectionInfo;
	private Composite reviewInfoComposite;
	private Composite reviewInfoBodyComposite;
	private Composite reviewInfoFooterComposite;
	private Composite searchTitleComposite;
	private Label titleLabel;
	private Text titleText;
	private Label totalFoundLabel;
	private Label totalFetchedLabel;
	
	private Label ieeeTotalFoundLabel;
	private Label ieeeTotalFetchedLabel;
	private Label acmTotalFoundLabel;
	private Label acmTotalFetchedLabel;
	private Label engineeringTotalFoundLabel;
	private Label engineeringtotalFetchedLabel;
	private Label scienceTotalFoundLabel;
	private Label scienceTotalFetchedLabel;
	private Label scopusTotalFoundLabel;
	private Label scopusTotalFetchedLabel;
	private Label springerTotalFoundLabel;
	private Label springerTotalFetchedLabel;
	
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
		titleText.setText(selectedLiteratureReview.getTitle());
		sectionInfo.setVisible(true);
	}
	
	public void createPartControl(Composite parent) {
		configureView(parent);
		createLiteratureWidgets(parent);
	}
	
	public void setFocus() {

	}
	
	private void configureView(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.setText("Literature Reviews");
		form.getBody().setLayout(new GridLayout(2, false));
	}

	private void createLiteratureWidgets(Composite parent) {
		//Section for List
	    sectionList = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    sectionList.setLayout(new GridLayout(1, false));
		sectionList.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		listComposite = toolkit.createComposite(sectionList, SWT.BORDER);
		listComposite.setLayout(new GridLayout(2, false));
		listComposite.setLayoutData(new GridData());
		
		list = new List (listComposite, SWT.MULTI | SWT.V_SCROLL);
		GridData listLayoutData = new GridData(GridData.FILL_VERTICAL);
		listLayoutData.horizontalSpan = 1;
		list.setLayoutData(listLayoutData);
		list.addSelectionListener(new LiteratureReviewsListHandler());
		refreshView();
		
		//Section for information
	    sectionInfo = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    sectionInfo.setLayout(new GridLayout(1, false));
		sectionInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
		sectionInfo.setVisible(false);

		reviewInfoComposite = toolkit.createComposite(sectionInfo, SWT.BORDER);
		reviewInfoComposite.setLayout(new GridLayout(1, false));
		GridData reviewCompositeData = new GridData(GridData.FILL_BOTH);
		reviewCompositeData.horizontalSpan = 1;
		reviewInfoComposite.setLayoutData(reviewCompositeData);
		
		//composite for title and text field
		searchTitleComposite = toolkit.createComposite(reviewInfoComposite);
		searchTitleComposite.setLayout(new GridLayout(2, false));
		GridData searchTitleCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		searchTitleCompositeData.horizontalSpan = 2;
		searchTitleComposite.setLayoutData(searchTitleCompositeData);

		titleLabel = toolkit.createLabel(searchTitleComposite, "Title ");
		titleLabel.setLayoutData(new GridData());
		
		titleText = toolkit.createText(searchTitleComposite, "");
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//composite for labels
		reviewInfoBodyComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		reviewInfoBodyComposite.setLayout(new GridLayout(2, false));
		GridData reviewBodyCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		reviewBodyCompositeData.horizontalSpan = 2;
		reviewInfoComposite.setLayoutData(reviewBodyCompositeData);
		
		totalFoundLabel = toolkit.createLabel(reviewInfoBodyComposite, "Total Found: ");
		GridData totalFoundLayout = new GridData();
		totalFoundLayout.horizontalSpan = 1;
		totalFoundLabel.setLayoutData(totalFoundLayout);
		
		totalFetchedLabel = toolkit.createLabel(reviewInfoBodyComposite, "Total Fetched: ");
		GridData totalFetchedLayout = new GridData();
		totalFetchedLayout.horizontalIndent = 30;
		totalFetchedLayout.horizontalSpan = 1;
		totalFetchedLabel.setLayoutData(totalFetchedLayout);

		//composite for links
		reviewInfoFooterComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		reviewInfoFooterComposite.setLayout(new GridLayout(2, false));
		GridData reviewFooterCompositeData = new GridData(GridData.FILL_BOTH);
		reviewFooterCompositeData.horizontalSpan = 1;
		reviewInfoFooterComposite.setLayoutData(reviewFooterCompositeData);
		
		Hyperlink exportLink = toolkit.createHyperlink(reviewInfoFooterComposite, "Export literature review", SWT.WRAP);
		GridData exportLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END);
		exportLinkLayout.grabExcessVerticalSpace = true;
		exportLink.setLayoutData(exportLinkLayout);
		exportLink.addHyperlinkListener(new ExportLiteratureReviewLinkHandler());
		
		Hyperlink studyLink = toolkit.createHyperlink(reviewInfoFooterComposite, "View studies", SWT.WRAP);
		GridData studyLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END);
		studyLinkLayout.grabExcessVerticalSpace = true;
		studyLink.setLayoutData(studyLinkLayout);
		studyLink.addHyperlinkListener(new LiteratureReviewStudiesLinkHandler());

		sectionList.setClient(listComposite);
		sectionInfo.setClient(reviewInfoComposite);
	}

	private class LiteratureReviewsListHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = list.getSelectionIndex();
			
			if(ieeeTotalFoundLabel != null)
				ieeeTotalFoundLabel.dispose();
			if(ieeeTotalFetchedLabel != null)
				ieeeTotalFetchedLabel.dispose();
			
			if(acmTotalFoundLabel != null)
				acmTotalFoundLabel.dispose();
			if(acmTotalFetchedLabel != null)
				acmTotalFetchedLabel.dispose();
			
			if(engineeringTotalFoundLabel != null)
				engineeringTotalFoundLabel.dispose();
			if(engineeringtotalFetchedLabel != null)
				engineeringtotalFetchedLabel.dispose();
			
			if(scienceTotalFoundLabel != null)
				scienceTotalFoundLabel.dispose();
			if(scienceTotalFetchedLabel != null)
				scienceTotalFetchedLabel.dispose();
			
			if(scopusTotalFoundLabel != null)
				scopusTotalFoundLabel.dispose();
			if(scopusTotalFetchedLabel != null)
				scopusTotalFetchedLabel.dispose();
			
			if(springerTotalFoundLabel != null)
				springerTotalFoundLabel.dispose();
			if(springerTotalFetchedLabel != null)
				springerTotalFetchedLabel.dispose();
			
			if (selectionIndex >= 0) {
				selectedLiteratureReview = literatureReviews.get(selectionIndex);
				titleText.setText(selectedLiteratureReview.getTitle());
				totalFoundLabel.setText("Total Found: " + selectedLiteratureReview.getTotalFound());
				totalFetchedLabel.setText("Total Fetched: " + selectedLiteratureReview.getTotalFetched());
				for (LiteratureReviewSource source : selectedLiteratureReview.getSources()) {
					if(source.getName().equals("IEEE")){
						ieeeTotalFoundLabel = toolkit.createLabel(reviewInfoBodyComposite, "Ieee total Found: " + source.getTotalFound());
						GridData ieeeTotalFoundLayout = new GridData();
						ieeeTotalFoundLayout.horizontalSpan = 1;
						ieeeTotalFoundLabel.setLayoutData(ieeeTotalFoundLayout);
						
						ieeeTotalFetchedLabel = toolkit.createLabel(reviewInfoBodyComposite, "Ieee total Fetched: " + source.getTotalFetched());
						GridData ieeeTotalFetchedLayout = new GridData();
						ieeeTotalFetchedLayout.horizontalIndent = 30;
						ieeeTotalFetchedLayout.horizontalSpan = 1;
						ieeeTotalFetchedLabel.setLayoutData(ieeeTotalFetchedLayout);
					}
					if(source.getName().equals("ACM")){
						acmTotalFoundLabel = toolkit.createLabel(reviewInfoBodyComposite, "ACM total Found: " + source.getTotalFound());
						GridData acmTotalFoundLayout = new GridData();
						acmTotalFoundLayout.horizontalSpan = 1;
						acmTotalFoundLabel.setLayoutData(acmTotalFoundLayout);
						
						acmTotalFetchedLabel = toolkit.createLabel(reviewInfoBodyComposite, "ACM total Fetched: " + source.getTotalFetched());
						GridData acmTotalFetchedLayout = new GridData();
						acmTotalFetchedLayout.horizontalIndent = 30;
						acmTotalFetchedLayout.horizontalSpan = 1;
						acmTotalFetchedLabel.setLayoutData(acmTotalFetchedLayout);
					}
					if(source.getName().equals("ENGINEERING_VILLAGE")){
						engineeringTotalFoundLabel = toolkit.createLabel(reviewInfoBodyComposite, "Engineering village total Found: " + source.getTotalFound());
						GridData engineeringtotalFoundLayout = new GridData();
						engineeringtotalFoundLayout.horizontalSpan = 1;
						engineeringTotalFoundLabel.setLayoutData(engineeringtotalFoundLayout);
						
						engineeringtotalFetchedLabel = toolkit.createLabel(reviewInfoBodyComposite, "Engineering village total Fetched: " + source.getTotalFetched());
						GridData engineeringtotalFetchedLayout = new GridData();
						engineeringtotalFetchedLayout.horizontalIndent = 30;
						engineeringtotalFetchedLayout.horizontalSpan = 1;
						engineeringtotalFetchedLabel.setLayoutData(engineeringtotalFetchedLayout);
					}
					if(source.getName().equals("SCIENCE_DIRECT")){
						scienceTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Science direct total Found: " + source.getTotalFound());
						GridData scienceTotalFoundLayout = new GridData();
						scienceTotalFoundLayout.horizontalSpan = 1;
						scienceTotalFoundLabel.setLayoutData(scienceTotalFoundLayout);
						
						scienceTotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Science direct total Fetched: " + source.getTotalFetched());
						GridData scienceTotalFetchedLayout = new GridData();
						scienceTotalFetchedLayout.horizontalIndent = 30;
						scienceTotalFetchedLayout.horizontalSpan = 1;
						scienceTotalFetchedLabel.setLayoutData(scienceTotalFetchedLayout);
					}
					if(source.getName().equals("SCOPUS")){
						scopusTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Scopus total Found: " + source.getTotalFound());
						GridData scopusTotalFoundLayout = new GridData();
						scopusTotalFoundLayout.horizontalSpan = 1;
						scopusTotalFoundLabel.setLayoutData(scopusTotalFoundLayout);
						
						scopusTotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Scopus total Fetched: " + source.getTotalFetched());
						GridData scopusTotalFetchedLayout = new GridData();
						scopusTotalFetchedLayout.horizontalIndent = 30;
						scopusTotalFetchedLayout.horizontalSpan = 1;
						scopusTotalFetchedLabel.setLayoutData(scopusTotalFetchedLayout);
					}
					if(source.getName().equals("SPRINGER_LINK")){
						springerTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Springer link total Found: " + source.getTotalFound());
						GridData springerTotalFoundLayout = new GridData();
						springerTotalFoundLayout.horizontalSpan = 1;
						springerTotalFoundLabel.setLayoutData(springerTotalFoundLayout);
						
						springerTotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Springer link total Fetched: " + source.getTotalFetched());
						GridData springerTotalFetchedLayout = new GridData();
						springerTotalFetchedLayout.horizontalIndent = 30;
						springerTotalFetchedLayout.horizontalSpan = 1;
						springerTotalFetchedLabel.setLayoutData(springerTotalFetchedLayout);
					}
				}
				WidgetsUtil.refreshComposite(reviewInfoComposite);
				sectionInfo.setVisible(true);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	private class ExportLiteratureReviewLinkHandler implements IHyperlinkListener {

		public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
			fileDialog.setFilterExtensions(new String[] {"*.xml"});
			fileDialog.setOverwrite(true);
			fileDialog.open();
			
			String filePath = fileDialog.getFilterPath() + File.separator + fileDialog.getFileName();
			if (filePath != null && !filePath.trim().isEmpty() && !filePath.equals("\\")) {
				LiteratureReviewController controller = new LiteratureReviewController();
				controller.exportLiteratureReview(selectedLiteratureReview,filePath, true);
			}
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
	
}
