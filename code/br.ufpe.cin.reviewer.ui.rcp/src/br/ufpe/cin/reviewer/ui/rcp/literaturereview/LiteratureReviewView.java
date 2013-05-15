package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.core.runtime.IConfigurationElement;
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
import br.ufpe.cin.reviewer.searchprovider.extensions.SearchProviderExtensionsRegistry;
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

	private java.util.List<Label> searchProvidersTotalFoundLabels = new ArrayList<Label>();
	private java.util.List<Label> searchProvidersTotalFetchedLabels = new ArrayList<Label>();
	
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
		reviewInfoFooterComposite.setLayout(new GridLayout(3, false));
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
		
		Hyperlink deleteLink = toolkit.createHyperlink(reviewInfoFooterComposite, "Delete Literature Review", SWT.WRAP);
		GridData deleteLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END);
		deleteLinkLayout.grabExcessVerticalSpace = true;
		deleteLink.setLayoutData(deleteLinkLayout);
		deleteLink.addHyperlinkListener(new DeleteLiteratureReviewLinkHandler());

		sectionList.setClient(listComposite);
		sectionInfo.setClient(reviewInfoComposite);
	}

	private class LiteratureReviewsListHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = list.getSelectionIndex();

			for (Label label : searchProvidersTotalFoundLabels) {
				label.dispose();
			}
			for (Label label : searchProvidersTotalFetchedLabels) {
				label.dispose();
			}
			
			searchProvidersTotalFoundLabels.clear();
			searchProvidersTotalFetchedLabels.clear();
			
			if (selectionIndex >= 0) {
				selectedLiteratureReview = literatureReviews.get(selectionIndex);
				titleText.setText(selectedLiteratureReview.getTitle());
				totalFoundLabel.setText("Total Found: " + selectedLiteratureReview.getTotalFound());
				totalFetchedLabel.setText("Total Fetched: " + selectedLiteratureReview.getTotalFetched());

				java.util.List<IConfigurationElement> configs = SearchProviderExtensionsRegistry.getConfigElements();
				Collections.sort(configs, new SearchProviderConfiguratorElementComparator());
				
				for (LiteratureReviewSource source : selectedLiteratureReview.getSources()) {
					
					for (IConfigurationElement config : configs) {
						if(source.getName().equals( config.getAttribute("key") )){
							Label totalFoundLabel = toolkit.createLabel(reviewInfoBodyComposite, config.getAttribute("friendly.name") + " total Found: " + source.getTotalFound());
							totalFoundLabel.setData(config.getAttribute("key"));
							GridData sourceTotalFoundLayout = new GridData();
							sourceTotalFoundLayout.horizontalSpan = 1;
							totalFoundLabel.setLayoutData(sourceTotalFoundLayout);
							searchProvidersTotalFoundLabels.add(totalFoundLabel);
	
							Label totalFetchedLabel = toolkit.createLabel(reviewInfoBodyComposite, config.getAttribute("friendly.name") + " total Fetched: " + source.getTotalFetched());
							totalFetchedLabel.setData(config.getAttribute("key"));
							GridData sourceTotalFetchedLayout = new GridData();
							sourceTotalFetchedLayout.horizontalIndent = 30;
							sourceTotalFetchedLayout.horizontalSpan = 1;
							totalFetchedLabel.setLayoutData(sourceTotalFetchedLayout);
							searchProvidersTotalFetchedLabels.add(totalFetchedLabel);
						}
					}
				}
				System.out.println(searchProvidersTotalFoundLabels.size());
				WidgetsUtil.refreshComposite(reviewInfoComposite);
				WidgetsUtil.refreshComposite(reviewInfoBodyComposite);
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
	
	private class DeleteLiteratureReviewLinkHandler implements IHyperlinkListener {

		public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {

			LiteratureReviewController literatureReviewController = new LiteratureReviewController();
			literatureReviewController.deleteLiteratureReview(selectedLiteratureReview);
			
			list.remove(selectedLiteratureReview.getTitle());
			literatureReviews.remove(selectedLiteratureReview);
			
			if(!literatureReviews.isEmpty()) {
				if(literatureReviews.get(0) != null) {
					selectedLiteratureReview = literatureReviews.get(0);
				}
				else {
					selectedLiteratureReview = null;
					sectionInfo.setVisible(false);
				}
			}
			else {
				selectedLiteratureReview = null;
				sectionInfo.setVisible(false);
			}

			WidgetsUtil.refreshComposite(listComposite);
			WidgetsUtil.refreshComposite(reviewInfoComposite);
			
		}
		
	}
	
	private class SearchProviderConfiguratorElementComparator implements Comparator<IConfigurationElement> {

		public int compare(IConfigurationElement config1, IConfigurationElement config2) {
			return config1.getAttribute("friendly.name").compareTo(config2.getAttribute("friendly.name"));
		}
		
	}
	
}
