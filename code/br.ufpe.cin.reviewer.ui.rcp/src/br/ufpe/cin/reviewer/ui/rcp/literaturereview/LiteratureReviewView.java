package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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

public class LiteratureReviewView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewView";

	private static java.util.List<LiteratureReview> literatureReviews;
	private static LiteratureReview selectedLiteratureReview;
	
	private static FormToolkit toolkit;
	private static Form form;
	
	private static Section sectionList;
	private static Composite listComposite;
	private static List list;
	
	private static Section sectionInfo;
	private static Composite reviewInfoComposite;
	private static Composite searchTitleComposite;
	private static Label titleLabel;
	private static Text titleText;
	private static Label totalFoundLabel;
	private static Label totalFetchedLabel;
	
	public LiteratureReviewView() {

	}
	
	public static void refreshView() {
		LiteratureReviewController literatureReviewController = new LiteratureReviewController();
		literatureReviews = literatureReviewController.findAllLiteratureReview();
		
		list.removeAll();
		for (LiteratureReview literatureReview : literatureReviews) {
			list.add(literatureReview.getTitle());
		}
	}
	
	public static void setSelecredLiteratureReview() {
		
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
		
		listComposite = toolkit.createComposite(sectionList);
		listComposite.setLayout(new GridLayout(2, false));
		listComposite.setLayoutData(new GridData());
		
		list = new List (listComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
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
		reviewInfoComposite.setLayout(new GridLayout(2, false));
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
		
		totalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Total Found: " + "valor");
		GridData totalFoundLayout = new GridData();
		totalFoundLayout.horizontalSpan = 1;
		totalFoundLabel.setLayoutData(totalFoundLayout);
		
		totalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Total Fetched: " + "valor");
		GridData totalFetchedLayout = new GridData();
		totalFetchedLayout.horizontalIndent = 30;
		totalFetchedLayout.horizontalSpan = 1;
		totalFetchedLabel.setLayoutData(totalFetchedLayout);
		
//		//IEEE
//		Label ieeeTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Ieee total Found: " + "valor");
//		GridData ieeetotalFoundLayout = new GridData();
//		ieeetotalFoundLayout.horizontalSpan = 1;
//		ieeeTotalFoundLabel.setLayoutData(ieeetotalFoundLayout);
//		
//		Label ieeetotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Ieee total Fetched: " + "valor");
//		GridData ieeetotalFetchedLayout = new GridData();
//		ieeetotalFetchedLayout.horizontalIndent = 30;
//		ieeetotalFetchedLayout.horizontalSpan = 1;
//		ieeetotalFetchedLabel.setLayoutData(ieeetotalFetchedLayout);
//		
//		//ACM
//		Label acmTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "ACM total Found: " + "valor");
//		GridData acmtotalFoundLayout = new GridData();
//		acmtotalFoundLayout.horizontalSpan = 1;
//		acmTotalFoundLabel.setLayoutData(acmtotalFoundLayout);
//		
//		Label acmtotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "ACM total Fetched: " + "valor");
//		GridData acmtotalFetchedLayout = new GridData();
//		acmtotalFetchedLayout.horizontalIndent = 30;
//		acmtotalFetchedLayout.horizontalSpan = 1;
//		acmtotalFetchedLabel.setLayoutData(acmtotalFetchedLayout);
//		
//		//Engineering village
//		Label engineeringTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Engineering village total Found: " + "valor");
//		GridData engineeringtotalFoundLayout = new GridData();
//		engineeringtotalFoundLayout.horizontalSpan = 1;
//		engineeringTotalFoundLabel.setLayoutData(engineeringtotalFoundLayout);
//		
//		Label engineeringtotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Engineering village total Fetched: " + "valor");
//		GridData engineeringtotalFetchedLayout = new GridData();
//		engineeringtotalFetchedLayout.horizontalIndent = 30;
//		engineeringtotalFetchedLayout.horizontalSpan = 1;
//		engineeringtotalFetchedLabel.setLayoutData(engineeringtotalFetchedLayout);
//		
//		//Science Direct
//		Label scienceTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Science direct total Found: " + "valor");
//		GridData scienceTotalFoundLayout = new GridData();
//		scienceTotalFoundLayout.horizontalSpan = 1;
//		scienceTotalFoundLabel.setLayoutData(scienceTotalFoundLayout);
//		
//		Label scienceTotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Science direct total Fetched: " + "valor");
//		GridData scienceTotalFetchedLayout = new GridData();
//		scienceTotalFetchedLayout.horizontalIndent = 30;
//		scienceTotalFetchedLayout.horizontalSpan = 1;
//		scienceTotalFetchedLabel.setLayoutData(scienceTotalFetchedLayout);
//		
//		//Scopus
//		Label scopusTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Scopus total Found: " + "valor");
//		GridData scopusTotalFoundLayout = new GridData();
//		scopusTotalFoundLayout.horizontalSpan = 1;
//		scopusTotalFoundLabel.setLayoutData(scopusTotalFoundLayout);
//		
//		Label scopusTotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Scopus total Fetched: " + "valor");
//		GridData scopusTotalFetchedLayout = new GridData();
//		scopusTotalFetchedLayout.horizontalIndent = 30;
//		scopusTotalFetchedLayout.horizontalSpan = 1;
//		scopusTotalFetchedLabel.setLayoutData(scopusTotalFetchedLayout);
//		
//		//Springer link
//		Label springerTotalFoundLabel = toolkit.createLabel(reviewInfoComposite, "Springer link total Found: " + "valor");
//		GridData springerTotalFoundLayout = new GridData();
//		springerTotalFoundLayout.horizontalSpan = 1;
//		springerTotalFoundLabel.setLayoutData(springerTotalFoundLayout);
//		
//		Label springerTotalFetchedLabel = toolkit.createLabel(reviewInfoComposite, "Springer link total Fetched: " + "valor");
//		GridData springerTotalFetchedLayout = new GridData();
//		springerTotalFetchedLayout.horizontalIndent = 30;
//		springerTotalFetchedLayout.horizontalSpan = 1;
//		springerTotalFetchedLabel.setLayoutData(springerTotalFetchedLayout);
		
		Hyperlink studyLink = toolkit.createHyperlink(reviewInfoComposite, "View studies", SWT.WRAP);
		GridData studyLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END);
		studyLinkLayout.horizontalSpan = 2;
		studyLinkLayout.grabExcessVerticalSpace = true;
		studyLink.setLayoutData(studyLinkLayout);
		studyLink.addHyperlinkListener(new LiteratureReviewStudiesLinkHandler());

		sectionList.setClient(listComposite);
		sectionInfo.setClient(reviewInfoComposite);
	}

	private class LiteratureReviewsListHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = list.getSelectionIndex();
			if (selectionIndex >= 0) {
				selectedLiteratureReview = literatureReviews.get(selectionIndex);
				titleText.setText(selectedLiteratureReview.getTitle());
				sectionInfo.setVisible(true);
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
			
			LiteratureReviewStudiesView.setLiteratureReview(selectedLiteratureReview);
		}
		
	}
	
}
