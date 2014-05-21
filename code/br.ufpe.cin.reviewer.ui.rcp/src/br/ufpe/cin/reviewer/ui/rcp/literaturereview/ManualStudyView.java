package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.common.UIConstants;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyAnalysisView.BackButtonHandler;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyAnalysisView.ExcludeButtonHandler;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyAnalysisView.SkipButtonHandler;

public class ManualStudyView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyAnalysisView";

	public ManualStudyView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createStudyWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Study analysis");
		
		GridLayout layoutData = new GridLayout(5, false);
		layoutData.marginTop = 10;
		layoutData.marginLeft = 10;
		layoutData.marginRight = 20;
		layoutData.marginBottom = 10;
		
		form.getBody().setLayout(layoutData);
	}
	
	private void createStudyWidgets(Composite parent) {
		this.form.getBody().setLayoutData(new GridData(GridData.GRAB_VERTICAL, GridData.GRAB_HORIZONTAL));

		GridData layoutData;
		
		sash = new SashForm(form.getBody(),SWT.HORIZONTAL);
		sash.setLayout(new GridLayout(2, false));
		GridData sashLayout = new GridData(GridData.FILL_BOTH);
		sashLayout.horizontalSpan = 5;
		sashLayout.grabExcessHorizontalSpace = true;
		sashLayout.grabExcessVerticalSpace = true;
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

		infoComposite = toolkit.createComposite(sectionInfo, SWT.BORDER);
		infoComposite.setLayout(new GridLayout(2, false));
		infoComposite.setLayoutData(new GridData());

		abstractComposite = toolkit.createComposite(sectionAbstract, SWT.BORDER);
		abstractComposite.setLayout(new GridLayout(2, false));
		abstractComposite.setLayoutData(new GridData());
		
		// Code widgets
		Label codeLabel = toolkit.createLabel(infoComposite, "CODE: ");
		codeLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, UIConstants.SYSTEM_FONT_HEIGHT, SWT.BOLD));
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		codeLabel.setLayoutData(layoutData);
		this.codeStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		this.codeStyledText.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, UIConstants.SYSTEM_FONT_HEIGHT, SWT.BOLD));
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.codeStyledText.setText("000");
		this.codeStyledText.setLayoutData(layoutData);
		this.codeStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.codeStyledText, true, true);
		
		// Title widgets
		Label titleLabel = toolkit.createLabel(infoComposite, "TITLE: ");
		titleLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, UIConstants.SYSTEM_FONT_HEIGHT, SWT.BOLD));
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		titleLabel.setLayoutData(layoutData);
		this.titleStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		this.titleStyledText.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, UIConstants.SYSTEM_FONT_HEIGHT, SWT.BOLD));
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.titleStyledText.setText("000");
		this.titleStyledText.setLayoutData(layoutData);
		this.titleStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.titleStyledText, true, true);
		
		// Status widgets
		Label statusLabel = toolkit.createLabel(infoComposite, "STATUS: ");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		statusLabel.setLayoutData(layoutData);
		this.statusStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.statusStyledText.setText("000");
		this.statusStyledText.setLayoutData(layoutData);
		this.statusStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.statusStyledText, true, true);
		
		// Source widgets
		Label sourceLabel = toolkit.createLabel(infoComposite, "SOURCE: ");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		sourceLabel.setLayoutData(layoutData);
		this.sourceStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.sourceStyledText.setText("000");
		this.sourceStyledText.setLayoutData(layoutData);
		this.sourceStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.sourceStyledText, true, true);
		
		// Authors widgets
		Label authorsLabel = toolkit.createLabel(infoComposite, "AUTHORS: ");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		authorsLabel.setLayoutData(layoutData);
		this.authorsStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.authorsStyledText.setText("000");
		this.authorsStyledText.setLayoutData(layoutData);
		this.authorsStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.authorsStyledText, true, true);
		
		// Institutions widgets
		Label institutionsLabel = toolkit.createLabel(infoComposite, "INSTITUTIONS: ");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		institutionsLabel.setLayoutData(layoutData);
		this.institutionsStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.institutionsStyledText.setText("000");
		this.institutionsStyledText.setLayoutData(layoutData);
		this.institutionsStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.institutionsStyledText, true, true);
		
		// Countries widgets
		Label countriesLabel = toolkit.createLabel(infoComposite, "COUNTRIES: ");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		countriesLabel.setLayoutData(layoutData);
		this.countriesStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.countriesStyledText.setText("000");
		this.countriesStyledText.setLayoutData(layoutData);
		this.countriesStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.countriesStyledText, true, true);
		
		// Year widgets
		Label yearLabel = toolkit.createLabel(infoComposite, "YEAR: ");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		yearLabel.setLayoutData(layoutData);
		this.yearStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.yearStyledText.setText("000");
		this.yearStyledText.setLayoutData(layoutData);
		this.yearStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.yearStyledText, true, true);
		
		// URL widgets
		Label urlLabel = toolkit.createLabel(infoComposite, "URL: ");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		urlLabel.setLayoutData(layoutData);
		this.urlStyledText = new StyledText(infoComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 1;
		this.urlStyledText.setText("000");
		this.urlStyledText.setLayoutData(layoutData);
		this.urlStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.urlStyledText, true, true);
		
		//Groups Table
		groupTable = toolkit.createTable(infoComposite, SWT.BORDER | SWT.FULL_SELECTION);
		groupTable.setLinesVisible (true);
		groupTable.setHeaderVisible (true);
		GridData groupTableLayoutData = new GridData(GridData.FILL_BOTH);
		groupTableLayoutData.horizontalSpan = 2;
		groupTableLayoutData.grabExcessHorizontalSpace = true;
		groupTableLayoutData.grabExcessVerticalSpace = true;
		groupTable.setLayoutData(groupTableLayoutData);
		//criteriaTable.addMouseListener(new StudyMouseHandler());
		
		//insert columns and set their names
		String[] titlesGroup = {"GROUP", "CRITERIA"};
		for (int i=0; i<titlesGroup.length; i++) {
			TableColumn column = new TableColumn (groupTable, SWT.CENTER);
			column.setText (titlesGroup [i]);
		}
		for (int i=0; i<titlesGroup.length; i++) {
			groupTable.getColumn (i).pack ();
		}
		
		// Abstract widgets
		Label abstractLabel = toolkit.createLabel(abstractComposite, "ABSTRACT:");
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 1;
		abstractLabel.setLayoutData(layoutData);
		this.abstractStyledText = new StyledText(abstractComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 4;
		this.abstractStyledText.setText("000");
		this.abstractStyledText.setLayoutData(layoutData);
		this.abstractStyledText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.abstractStyledText, true, true);

		//Criteria Table
		criteriaTable = toolkit.createTable(form.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		criteriaTable.setLinesVisible (true);
		criteriaTable.setHeaderVisible (true);
		GridData criteriaTableLayoutData = new GridData(GridData.FILL_BOTH);
		criteriaTableLayoutData.horizontalSpan = 5;
		criteriaTableLayoutData.grabExcessVerticalSpace = true;
		criteriaTable.setLayoutData(criteriaTableLayoutData);
		//criteriaTable.addMouseListener(new StudyMouseHandler());
		
		//insert columns and set their names
		String[] titlesCriteria = {"CRITERIA"};
		for (int i=0; i<titlesCriteria.length; i++) {
			TableColumn column = new TableColumn (criteriaTable, SWT.CENTER);
			column.setText (titlesCriteria [i]);
		}
		for (int i=0; i<titlesCriteria.length; i++) {
			criteriaTable.getColumn (i).pack ();
		}
		
		// View all studies link
		Button studyLink = toolkit.createButton(form.getBody(), "View all studies", SWT.PUSH);
		//Hyperlink studyLink = toolkit.createHyperlink(form.getBody(), "View all studies", SWT.WRAP);
		GridData studyLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END);
		layoutData.horizontalSpan = 1;
		studyLinkLayout.grabExcessVerticalSpace = true;
		studyLinkLayout.grabExcessHorizontalSpace = true;
		studyLink.setLayoutData(studyLinkLayout);
		//studyLink.addHyperlinkListener(new LiteratureReviewStudiesLinkHandler());
		
		// Back button
		layoutData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Button back = toolkit.createButton(form.getBody(), "Back", SWT.PUSH);
		layoutData.horizontalSpan = 1;
		layoutData.grabExcessVerticalSpace = true;
		//layoutData.grabExcessHorizontalSpace = true;
		layoutData.verticalAlignment = SWT.END;
		layoutData.horizontalAlignment = SWT.RIGHT;
		back.setLayoutData(layoutData);
		back.addSelectionListener(new BackButtonHandler());
		
		// Include button
		Button include = toolkit.createButton(form.getBody(), "Include", SWT.PUSH);
		layoutData.horizontalSpan = 1;
		layoutData.grabExcessVerticalSpace = true;
		//layoutData.grabExcessHorizontalSpace = true;
		layoutData.verticalAlignment = SWT.END;
		layoutData.horizontalAlignment = SWT.RIGHT;
		include.setLayoutData(layoutData);
		layoutData = new GridData();
		include.addSelectionListener(new IncludeButtonHandler());
		
		// Exclude button
		Button exclude = toolkit.createButton(form.getBody(), "Exclude", SWT.PUSH);
		layoutData.horizontalSpan = 1;
		layoutData.grabExcessVerticalSpace = true;
		//layoutData.grabExcessHorizontalSpace = true;
		layoutData.verticalAlignment = SWT.END;
		layoutData.horizontalAlignment = SWT.RIGHT;
		exclude.setLayoutData(layoutData);
		layoutData = new GridData();
		exclude.addSelectionListener(new ExcludeButtonHandler());
		
		// Skip button
		Button skip = toolkit.createButton(form.getBody(), "Skip", SWT.PUSH);
		layoutData.horizontalSpan = 1;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.verticalAlignment = SWT.END;
		layoutData.horizontalAlignment = SWT.RIGHT;
		skip.setLayoutData(layoutData);
		skip.addSelectionListener(new SkipButtonHandler());
		
		sash.setWeights(new int[] {1, 3});
		
		sectionInfo.setClient(infoComposite);
		sectionAbstract.setClient(abstractComposite);
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
