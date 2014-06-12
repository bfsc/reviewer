package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

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
	private Text typeText;
	private Text sourceText;
	private Text authorsText;
	private Text institutionsText;
	private Text countriesText;
	private Text yearText;

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
		
		Label typeLabel = toolkit.createLabel(infoComposite, "Type: ");
		typeText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		typeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label sourceLabel = toolkit.createLabel(infoComposite, "Source: ");
		sourceText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		sourceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label authorsLabel = toolkit.createLabel(infoComposite, "Authors: ");
		authorsText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		authorsText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label institutionsLabel = toolkit.createLabel(infoComposite, "Institutions: ");
		institutionsText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		institutionsText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label countriesLabel = toolkit.createLabel(infoComposite, "Countries: ");
		countriesText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		countriesText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label yearLabel = toolkit.createLabel(infoComposite, "Year: ");
		yearText = toolkit.createText(infoComposite, "", SWT.WRAP | SWT.BORDER);
		yearText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//Abstract
		Label abstractLabel = toolkit.createLabel(abstractComposite, "Abstract: ");
		abstractText = toolkit.createText(abstractComposite, "", SWT.WRAP | SWT.BORDER);
		abstractText.setLayoutData(new GridData(GridData.FILL_BOTH));
		//abstractText.addFocusListener(new SearchTextHandler());
		//TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		//abstractText.setLayoutData(td);
	    
		buttonsComposite = toolkit.createComposite(sectionComposite);
		buttonsComposite.setLayout(new GridLayout(2, false));
	    GridData buttonCompositeLayout = new GridData(GridData.FILL_HORIZONTAL);
	    buttonsComposite.setLayoutData(buttonCompositeLayout);
		
		cancelButton = toolkit.createButton(buttonsComposite, "Cancel", SWT.PUSH);
		GridData cancelButtonLayoutData = new GridData();
		cancelButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		cancelButtonLayoutData.grabExcessHorizontalSpace = true;
		cancelButton.setLayoutData(cancelButtonLayoutData);
		
		saveButton = toolkit.createButton(buttonsComposite, "Save", SWT.PUSH);
		GridData saveButtonLayoutData = new GridData();
		saveButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		saveButton.setLayoutData(cancelButtonLayoutData);
		
		section.setClient(sectionComposite);
		sectionInfo.setClient(infoComposite);
		sectionAbstract.setClient(abstractComposite);
	}

	public void setFocus() {
		
	}

}
