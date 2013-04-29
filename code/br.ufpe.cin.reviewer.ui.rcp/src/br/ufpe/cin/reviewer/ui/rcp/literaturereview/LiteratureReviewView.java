package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.part.ViewPart;

public class LiteratureReviewView extends ViewPart {

	private FormToolkit toolkit;
	private Form form;
	
	public LiteratureReviewView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		configureView(parent);
		createLiteratureWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		this.toolkit = new FormToolkit(parent.getDisplay());
		this.form = toolkit.createForm(parent);
		this.toolkit.decorateFormHeading(this.form);
		this.form.setText("Reviewer");
		this.form.getBody().setLayout(new GridLayout(2, false));
	}

	private void createLiteratureWidgets(Composite parent) {
		//Section for List
	    Section sectionList = this.toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    sectionList.setLayout(new GridLayout(1, false));
		sectionList.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		Composite listComposite = this.toolkit.createComposite(sectionList);
		listComposite.setLayout(new GridLayout(2, false));
		listComposite.setLayoutData(new GridData());
		
		List list = new List (listComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		GridData listLayoutData = new GridData(GridData.FILL_VERTICAL);
		listLayoutData.horizontalSpan = 1;
		list.setLayoutData(listLayoutData);
		list.add ("teste");
		
		//Section for information
	    Section sectionInfo = this.toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    sectionInfo.setLayout(new GridLayout(1, false));
		sectionInfo.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite reviewInfoComposite = this.toolkit.createComposite(sectionInfo, SWT.BORDER);
		reviewInfoComposite.setLayout(new GridLayout(2, false));
		GridData reviewCompositeData = new GridData(GridData.FILL_BOTH);
		reviewCompositeData.horizontalSpan = 1;
		reviewInfoComposite.setLayoutData(reviewCompositeData);
		
		//composite for title and text field
		Composite searchTitleComposite = this.toolkit.createComposite(reviewInfoComposite);
		searchTitleComposite.setLayout(new GridLayout(2, false));
		GridData searchTitleCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		searchTitleCompositeData.horizontalSpan = 2;
		searchTitleComposite.setLayoutData(searchTitleCompositeData);

		Label titleLabel = this.toolkit.createLabel(searchTitleComposite, "Title ");
		titleLabel.setLayoutData(new GridData());
		
		Text searchTitle = toolkit.createText(searchTitleComposite, "");
		searchTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label totalFoundLabel = this.toolkit.createLabel(reviewInfoComposite, "Total Found: " + "valor");
		GridData totalFoundLayout = new GridData();
		totalFoundLayout.horizontalSpan = 1;
		totalFoundLabel.setLayoutData(totalFoundLayout);
		
		Label totalFetchedLabel = this.toolkit.createLabel(reviewInfoComposite, "Total Fetched: " + "valor");
		GridData totalFetchedLayout = new GridData();
		totalFetchedLayout.horizontalIndent = 30;
		totalFetchedLayout.horizontalSpan = 1;
		totalFetchedLabel.setLayoutData(totalFetchedLayout);
		
		//IEEE
		Label ieeeTotalFoundLabel = this.toolkit.createLabel(reviewInfoComposite, "Ieee total Found: " + "valor");
		GridData ieeetotalFoundLayout = new GridData();
		ieeetotalFoundLayout.horizontalSpan = 1;
		ieeeTotalFoundLabel.setLayoutData(ieeetotalFoundLayout);
		
		Label ieeetotalFetchedLabel = this.toolkit.createLabel(reviewInfoComposite, "Ieee total Fetched: " + "valor");
		GridData ieeetotalFetchedLayout = new GridData();
		ieeetotalFetchedLayout.horizontalIndent = 30;
		ieeetotalFetchedLayout.horizontalSpan = 1;
		ieeetotalFetchedLabel.setLayoutData(ieeetotalFetchedLayout);
		
		//ACM
		Label acmTotalFoundLabel = this.toolkit.createLabel(reviewInfoComposite, "ACM total Found: " + "valor");
		GridData acmtotalFoundLayout = new GridData();
		acmtotalFoundLayout.horizontalSpan = 1;
		acmTotalFoundLabel.setLayoutData(acmtotalFoundLayout);
		
		Label acmtotalFetchedLabel = this.toolkit.createLabel(reviewInfoComposite, "ACM total Fetched: " + "valor");
		GridData acmtotalFetchedLayout = new GridData();
		acmtotalFetchedLayout.horizontalIndent = 30;
		acmtotalFetchedLayout.horizontalSpan = 1;
		acmtotalFetchedLabel.setLayoutData(acmtotalFetchedLayout);
		
		//Engineering village
		Label engineeringTotalFoundLabel = this.toolkit.createLabel(reviewInfoComposite, "Engineering village total Found: " + "valor");
		GridData engineeringtotalFoundLayout = new GridData();
		engineeringtotalFoundLayout.horizontalSpan = 1;
		engineeringTotalFoundLabel.setLayoutData(engineeringtotalFoundLayout);
		
		Label engineeringtotalFetchedLabel = this.toolkit.createLabel(reviewInfoComposite, "Engineering village total Fetched: " + "valor");
		GridData engineeringtotalFetchedLayout = new GridData();
		engineeringtotalFetchedLayout.horizontalIndent = 30;
		engineeringtotalFetchedLayout.horizontalSpan = 1;
		engineeringtotalFetchedLabel.setLayoutData(engineeringtotalFetchedLayout);
		
		//Science Direct
		Label scienceTotalFoundLabel = this.toolkit.createLabel(reviewInfoComposite, "Science direct total Found: " + "valor");
		GridData scienceTotalFoundLayout = new GridData();
		scienceTotalFoundLayout.horizontalSpan = 1;
		scienceTotalFoundLabel.setLayoutData(scienceTotalFoundLayout);
		
		Label scienceTotalFetchedLabel = this.toolkit.createLabel(reviewInfoComposite, "Science direct total Fetched: " + "valor");
		GridData scienceTotalFetchedLayout = new GridData();
		scienceTotalFetchedLayout.horizontalIndent = 30;
		scienceTotalFetchedLayout.horizontalSpan = 1;
		scienceTotalFetchedLabel.setLayoutData(scienceTotalFetchedLayout);
		
		//Scopus
		Label scopusTotalFoundLabel = this.toolkit.createLabel(reviewInfoComposite, "Scopus total Found: " + "valor");
		GridData scopusTotalFoundLayout = new GridData();
		scopusTotalFoundLayout.horizontalSpan = 1;
		scopusTotalFoundLabel.setLayoutData(scopusTotalFoundLayout);
		
		Label scopusTotalFetchedLabel = this.toolkit.createLabel(reviewInfoComposite, "Scopus total Fetched: " + "valor");
		GridData scopusTotalFetchedLayout = new GridData();
		scopusTotalFetchedLayout.horizontalIndent = 30;
		scopusTotalFetchedLayout.horizontalSpan = 1;
		scopusTotalFetchedLabel.setLayoutData(scopusTotalFetchedLayout);
		
		//Springer link
		Label springerTotalFoundLabel = this.toolkit.createLabel(reviewInfoComposite, "Springer link total Found: " + "valor");
		GridData springerTotalFoundLayout = new GridData();
		springerTotalFoundLayout.horizontalSpan = 1;
		springerTotalFoundLabel.setLayoutData(springerTotalFoundLayout);
		
		Label springerTotalFetchedLabel = this.toolkit.createLabel(reviewInfoComposite, "Springer link total Fetched: " + "valor");
		GridData springerTotalFetchedLayout = new GridData();
		springerTotalFetchedLayout.horizontalIndent = 30;
		springerTotalFetchedLayout.horizontalSpan = 1;
		springerTotalFetchedLabel.setLayoutData(springerTotalFetchedLayout);
		
		Hyperlink studyLink = toolkit.createHyperlink(reviewInfoComposite, "View studies", SWT.WRAP);
		GridData studyLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END);
		studyLinkLayout.horizontalSpan = 2;
		studyLinkLayout.grabExcessVerticalSpace = true;
		studyLink.setLayoutData(studyLinkLayout);

		sectionList.setClient(listComposite);
		sectionInfo.setClient(reviewInfoComposite);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
