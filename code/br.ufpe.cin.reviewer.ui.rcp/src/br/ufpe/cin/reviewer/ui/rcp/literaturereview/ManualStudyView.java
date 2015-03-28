package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Section;

import br.ufpe.cin.reviewer.core.literaturereview.LiteratureReviewController;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.search.ManualSearch;
import br.ufpe.cin.reviewer.model.study.Study;
import br.ufpe.cin.reviewer.model.study.Study.StudyStatus;
import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;

public class ManualStudyView extends BaseView {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.ManualStudyView";
	
	private SashForm sash;
	
	private Section section;
	private Section sectionInfo;
	private Section sectionAbstract;
	
	private Composite sectionComposite;
	private Composite infoComposite;
	private Composite abstractComposite;
	private Composite buttonsComposite;

	private Text abstractText;
	private Text titleText;
	private Text sourceText;
	private Text authorsText;
	private Text institutionsText;
	private Text countriesText;
	private Text yearText;
	private Text URLText;

	private Button cancelButton;
	private Button saveButton;
	
	public ManualStudyView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createStudyWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Manual Study");
		form.getBody().setLayout(new GridLayout(2, false));
	}
	
	private void createStudyWidgets(Composite parent) {
		
		section = toolkit.createSection(form.getBody(), Section.SHORT_TITLE_BAR);
	    section.setLayout(new GridLayout(1, false));
	    section.setText("INFO");
	    GridData sectionLayout = new GridData(GridData.FILL_BOTH);
	    sectionLayout.grabExcessVerticalSpace = true;
	    sectionLayout.horizontalSpan = 1;
	    section.setLayoutData(sectionLayout);
	    
		sectionComposite = toolkit.createComposite(section);
		sectionComposite.setLayout(new GridLayout(1, false));
		sectionComposite.setLayoutData(new GridData());

		sash = new SashForm(sectionComposite,SWT.HORIZONTAL);
		sash.setLayout(new GridLayout(2, false));
		GridData sashLayout = new GridData(GridData.FILL_BOTH);
		sashLayout.horizontalSpan = 1;
		sash.setLayoutData(sashLayout);
		sash.getMaximizedControl();

		sectionInfo = toolkit.createSection(sash, Section.NO_TITLE);
	    sectionInfo.setLayout(new GridLayout(1, false));
	    GridData sectionInfoLayout = new GridData(GridData.FILL_VERTICAL);
	    sectionInfoLayout.grabExcessVerticalSpace = true;
	    sectionInfoLayout.horizontalSpan = 1;
	    sectionInfo.setLayoutData(sectionInfoLayout);
		
		sectionAbstract = toolkit.createSection(sash, Section.NO_TITLE);
		sectionAbstract.setLayout(new GridLayout(1, false));
	    GridData sectionAbstractLayout = new GridData(GridData.FILL_VERTICAL);
	    sectionAbstractLayout.grabExcessVerticalSpace = true;
	    sectionAbstractLayout.horizontalSpan = 1;
	    sectionAbstract.setLayoutData(sectionAbstractLayout);

		infoComposite = toolkit.createComposite(sectionInfo);
		infoComposite.setLayout(new GridLayout(2, false));
		infoComposite.setLayoutData(new GridData());

		abstractComposite = toolkit.createComposite(sectionAbstract);
		abstractComposite.setLayout(new GridLayout(1, false));
		abstractComposite.setLayoutData(new GridData());
		
		//Info
		Label titleLabel = toolkit.createLabel(infoComposite, "Title: ");
		titleText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTabTraverseListener(titleText);
		
		Label sourceLabel = toolkit.createLabel(infoComposite, "Source: ");
		sourceText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		sourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTabTraverseListener(sourceText);
		
		Label authorsLabel = toolkit.createLabel(infoComposite, "Authors: ");
		authorsText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		authorsText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTabTraverseListener(authorsText);
		
		Label institutionsLabel = toolkit.createLabel(infoComposite, "Institutions: ");
		institutionsText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		institutionsText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTabTraverseListener(institutionsText);
		
		Label countriesLabel = toolkit.createLabel(infoComposite, "Countries: ");
		countriesText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		countriesText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTabTraverseListener(countriesText);
		
		Label yearLabel = toolkit.createLabel(infoComposite, "Year: ");
		yearText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		yearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTabTraverseListener(yearText);
		
		Label URLLabel = toolkit.createLabel(infoComposite, "URL: ");
		URLText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		URLText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTabTraverseListener(URLText);
		
		//Abstract
		Label abstractLabel = toolkit.createLabel(abstractComposite, "Abstract: ");
		abstractText = toolkit.createText(abstractComposite, "", SWT.WRAP | SWT.BORDER);
		abstractText.setLayoutData(new GridData(GridData.FILL_BOTH));
		createTabTraverseListener(abstractText);
	    
		buttonsComposite = toolkit.createComposite(sectionComposite);
		buttonsComposite.setLayout(new GridLayout(2, false));
	    GridData buttonCompositeLayout = new GridData(GridData.FILL_HORIZONTAL);
	    buttonsComposite.setLayoutData(buttonCompositeLayout);
		
		cancelButton = toolkit.createButton(buttonsComposite, "Cancel", SWT.PUSH);
		GridData cancelButtonLayoutData = new GridData();
		cancelButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		cancelButtonLayoutData.grabExcessHorizontalSpace = true;
		cancelButton.setLayoutData(cancelButtonLayoutData);
		
		cancelButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
				IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewPerspective.ID));			
						
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		saveButton = toolkit.createButton(buttonsComposite, "Save", SWT.PUSH);
		GridData saveButtonLayoutData = new GridData();
		saveButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		//saveButtonLayoutData.grabExcessHorizontalSpace = true;
		saveButton.setLayoutData(saveButtonLayoutData);
		
		saveButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				LiteratureReviewView lrView = (LiteratureReviewView) ReviewerViewRegister
						.getView(LiteratureReviewView.ID);
				LiteratureReview lr = lrView.getSelectedLiteratureReview();

				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				boolean confirm = MessageDialog.openConfirm(shell,
						"Save Manual Search?", "Save Manual Search to " + lr.getTitle()
								+ "?");
				if (confirm) {
					ManualSearch s = new ManualSearch();
					Study study = new Study();
					
					study.setAbstract(abstractText.getText());
					study.setTitle(titleText.getText());
					study.setSource(sourceText.getText());
					
					String authors = authorsText.getText();
					StringTokenizer token = new StringTokenizer(authors, ",");
					List<String> authorsList = new ArrayList<String>();
					while (token.hasMoreTokens()) {
						authorsList.add(token.nextToken().trim());
					}
					study.setAuthors(authorsList);
					
					
					String institutions = institutionsText.getText();
					token = new StringTokenizer(institutions, ",");
					List<String> institutionsList = new ArrayList<String>();
					while (token.hasMoreTokens()) {
						institutionsList.add(token.nextToken().trim());
					}
					study.setInstitutions(institutionsList);
					
					
					String countries = countriesText.getText();
					token = new StringTokenizer(countries, ",");
					List<String> countriesList = new ArrayList<String>();
					while (token.hasMoreTokens()) {
						countriesList.add(token.nextToken().trim());
					}
					study.setCountries(countriesList);					
					
					study.setYear(yearText.getText());
					study.setStatus(StudyStatus.NOT_EVALUATED);
					study.setUrl(URLText.getText());					
					
					s.getStudies().add(study);					
					lr.getSearches().add(s);

					LiteratureReviewController literatureReviewController = new LiteratureReviewController();
					literatureReviewController.updateLiteratureReview(lr);
					lrView.populateReviewInfo();

					IPerspectiveRegistry perspectiveRegistry = PlatformUI
							.getWorkbench().getPerspectiveRegistry();
					IWorkbenchPage activePage = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage();
					activePage.setPerspective(perspectiveRegistry
							.findPerspectiveWithId(LiteratureReviewPerspective.ID));
				}
						
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		section.setClient(sectionComposite);
		sectionInfo.setClient(infoComposite);
		sectionAbstract.setClient(abstractComposite);
	}

	private void createTabTraverseListener(Text textField) {
		textField.addTraverseListener(new TraverseListener() {
		    public void keyTraversed(TraverseEvent e) {
		        if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
		            e.doit = true;
		        }
		    }
		});
	}

	public void setFocus() {
		
	}

}
