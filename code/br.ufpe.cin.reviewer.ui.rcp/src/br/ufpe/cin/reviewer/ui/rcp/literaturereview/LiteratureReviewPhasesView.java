package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.framework.Bundle;

import br.ufpe.cin.reviewer.core.literaturereview.LiteratureReviewController;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.study.Study;
import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.common.UIConstants;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsFactory;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class LiteratureReviewPhasesView extends BaseView {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewStudiesView";
	
	private Section section;
	private Composite phaseComposite;

	private int compositeSize;
	private int numPhases;
	
	private List<Label> phasesLabel;
	private Button addPhasesButton;
	private Section sectionGroups;
	private ToolBar toolbarGroups;
	private Composite groupsComposite;
	private Table groupsTable;
	
	private Section sectionStudies;
	private Composite studiesComposite;
	private Table studiesTable;

	private Composite buttonsComposite;
	private Button exportButton;
	private Button importButton;

	public LiteratureReviewPhasesView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createLiteratureStudiesWidgets(parent);
	}
	
	public void setFocus() {

	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Literature review studies");
		form.getBody().setLayout(new GridLayout(2, false));
	}
	
	private void createLiteratureStudiesWidgets(Composite parent) {
		//criando imagens
	    String PLUGIN_ID = "br.ufpe.cin.reviewer.ui.rcp";
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
        Image addIcon = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/Add-Green-Button-icon.png"), null)).createImage();
        Image minusIcon = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/Minus-Green-Button-icon.png"), null)).createImage();
		
		//inicializa o tamanho da section
		numPhases = 4;
		compositeSize = numPhases + 1;
		
	    section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout(1, false));
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		phaseComposite = toolkit.createComposite(section);
		phaseComposite.setLayout(new GridLayout(compositeSize, false));
		phaseComposite.setLayoutData(new GridData());
		
		//Phases Labels
		phasesLabel = new ArrayList<>();
		for(int i = 0; i < numPhases; i++){
			phasesLabel.add(toolkit.createLabel(phaseComposite, (i+1)+"# Phase >"));
		}
	    GridData phasesLabelLayout = new GridData();
	    phasesLabelLayout.horizontalAlignment = SWT.BEGINNING;
	    //phasesLabelLayout.grabExcessHorizontalSpace = true;
		for (int i = 0; i < phasesLabel.size(); i++) {
			phasesLabel.get(i).setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
			phasesLabel.get(i).setLayoutData(phasesLabelLayout);
		}
		
		addPhasesButton = toolkit.createButton(phaseComposite, "", SWT.PUSH);
		addPhasesButton.setImage(addIcon);
		GridData addPhasesLayoutData = new GridData();
		addPhasesLayoutData.horizontalAlignment = SWT.LEFT;
		addPhasesLayoutData.grabExcessHorizontalSpace = true;
		addPhasesButton.setLayoutData(addPhasesLayoutData);
		
		//Section for Groups
		sectionGroups = toolkit.createSection(phaseComposite, Section.SHORT_TITLE_BAR);
		sectionGroups.setText("PHASE EVALUATOR GROUPS");	    
		sectionGroups.setLayout(new GridLayout(1, false));
	    GridData sectionGroupsLayout = new GridData(GridData.FILL_BOTH);
	    sectionGroupsLayout.horizontalSpan = compositeSize;
	    sectionGroups.setLayoutData(sectionGroupsLayout);
		
	    toolbarGroups = new ToolBar (sectionGroups, SWT.NONE);
	    ToolItem itemAddReview = new ToolItem(toolbarGroups, SWT.BUTTON1);
	    itemAddReview.setImage(addIcon);
	    ToolItem itemDeleteReview = new ToolItem(toolbarGroups, SWT.BUTTON1);
	    itemDeleteReview.setImage(minusIcon);
	    sectionGroups.setTextClient(toolbarGroups);
		
	    groupsComposite = toolkit.createComposite(sectionGroups);
	    groupsComposite.setLayout(new GridLayout(2, false));
	    groupsComposite.setLayoutData(new GridData());

		//Info Table
		groupsTable = toolkit.createTable(groupsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		groupsTable.setLinesVisible (true);
		groupsTable.setHeaderVisible (true);
		GridData groupsTableLayoutData = new GridData(GridData.FILL_BOTH);
		groupsTable.setLayoutData(groupsTableLayoutData);

		//insert columns and set their names
		String[] titles = {"Groups", "Members"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (groupsTable, SWT.CENTER);
			column.setText (titles [i]);
		}
		for (int i=0; i<titles.length; i++) {
			groupsTable.getColumn (i).pack ();
		}
		
		//Section for Studies List
		sectionStudies = toolkit.createSection(phaseComposite, Section.SHORT_TITLE_BAR);
		sectionStudies.setText("PHASE STUDIES");	    
		sectionStudies.setLayout(new GridLayout(1, false));
	    GridData sectionStudiesLayout = new GridData(GridData.FILL_BOTH);
	    sectionStudiesLayout.horizontalSpan = compositeSize;
	    sectionStudies.setLayoutData(sectionStudiesLayout);
		
	    studiesComposite = toolkit.createComposite(sectionStudies);
	    studiesComposite.setLayout(new GridLayout(2, false));
	    studiesComposite.setLayoutData(new GridData());

		//Info Table
		studiesTable = toolkit.createTable(studiesComposite, SWT.BORDER | SWT.FULL_SELECTION);
		studiesTable.setLinesVisible (true);
		studiesTable.setHeaderVisible (true);
		GridData studiesTableLayoutData = new GridData(GridData.FILL_BOTH);
		studiesTable.setLayoutData(studiesTableLayoutData);
		
		//insert columns and set their names
		String[] titlesStudies = {"CODE v", "STATUS v", "GROUP v", "TITLE v", "YEAR v"};
		for (int i=0; i<titlesStudies.length; i++) {
			TableColumn column = new TableColumn (studiesTable, SWT.CENTER);
			column.setText (titlesStudies [i]);
		}
		for (int i=0; i<titlesStudies.length; i++) {
			studiesTable.getColumn (i).pack ();
		}
		
		TableItem item = new TableItem(studiesTable,SWT.NONE);
		item.setText("item1");
		item.setText(0, "code1");
		//aqui não está adicionando o listener
		//item.addListener(SWT.Selection, (Listener) new ItemLinkHandler());
		
		buttonsComposite = toolkit.createComposite(phaseComposite);
		buttonsComposite.setLayout(new GridLayout(2, false));
	    GridData buttonCompositeLayout = new GridData(GridData.FILL_HORIZONTAL);
	    buttonCompositeLayout.horizontalSpan = compositeSize;
	    buttonsComposite.setLayoutData(buttonCompositeLayout);
		
		exportButton = toolkit.createButton(buttonsComposite, "Export", SWT.PUSH);
		GridData exportButtonLayoutData = new GridData();
		exportButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		exportButtonLayoutData.grabExcessHorizontalSpace = true;
		exportButton.setLayoutData(exportButtonLayoutData);
		
		importButton = toolkit.createButton(buttonsComposite, "Import", SWT.PUSH);
		GridData importButtonLayoutData = new GridData();
		importButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		importButton.setLayoutData(importButtonLayoutData);
		
		section.setClient(phaseComposite);
		sectionGroups.setClient(groupsComposite);
		sectionStudies.setClient(studiesComposite);
	}

	private class ItemLinkHandler implements SelectionListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(StudyAnalysisPerspective.ID));
			
			StudyAnalysisView studyAnalysisView = (StudyAnalysisView) ReviewerViewRegister.getView(StudyAnalysisView.ID);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	/*
	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewStudiesView";
	
	private LiteratureReview literatureReview;
	
	private Section section;
	private Composite studiesComposite;
	private StyledText titleText;
	private Table table;
	
	public LiteratureReviewPhasesView() {
		ReviewerViewRegister.putView(ID, this);
	}

	public void setLiteratureReview(LiteratureReview literatureReview) {
		this.literatureReview = literatureReview;

		titleText.setText(this.literatureReview.getTitle());

		table.removeAll();
		
		for (Study study : this.literatureReview.getStudies()) {
			Color red = form.getDisplay().getSystemColor(SWT.COLOR_RED);
			Color green = form.getDisplay().getSystemColor(SWT.COLOR_GREEN);
			Color yellow = form.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
			TableItem item = new TableItem (table, SWT.NONE);
			
			item.setText (0, study.getCode());
			
			if(study.getStatus() == Study.StudyStatus.NOT_EVALUATED){
				item.setBackground(1,yellow);
			}
			else if(study.getStatus() == Study.StudyStatus.INCLUDED){
				item.setBackground(1,green);
			}
			else if(study.getStatus() == Study.StudyStatus.EXCLUDED){
				item.setBackground(1,red);
			}
			
			if (study.getTitle() != null) {
				item.setText(2, study.getTitle());
			}

			if (study.getYear() != null) {
				item.setText (3, study.getYear());
			}
		}
		
		for (int i=0; i < 4; i++) {
			table.getColumn(i).pack ();
		}
		
		this.section.setVisible(true);
		WidgetsUtil.refreshComposite(form.getBody());
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createLiteratureStudiesWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Literature review studies");
		form.getBody().setLayout(new GridLayout(2, false));
	}

	private void createLiteratureStudiesWidgets(Composite parent) {
	    section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout(1, false));
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		studiesComposite = toolkit.createComposite(section);
		studiesComposite.setLayout(new GridLayout(2, false));
		studiesComposite.setLayoutData(new GridData());
		
		Label titleLabel = toolkit.createLabel(studiesComposite, "TITLE: ");
		titleLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		titleLabel.setLayoutData(new GridData());
		
		this.titleText = WidgetsFactory.createStyledText(studiesComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		this.titleText.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		this.titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.toolkit.adapt(this.titleText, true, true);

		table = toolkit.createTable(studiesComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.horizontalSpan = 2;
		table.setLayoutData(tableGridData);
		table.addMouseListener(new StudyMouseHandler());

		String[] titles = {"Code", "Status", "Title", "Year"};
		for (int i=0; i< titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		
		Hyperlink exportLink = toolkit.createHyperlink(studiesComposite, "Export studies to spreedsheet...", SWT.NONE);
		exportLink.addHyperlinkListener(new ExportLinkHandler());
		GridData exportLinkGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		exportLinkGridData.horizontalSpan = 2;
		exportLink.setLayoutData(exportLinkGridData);
		
		section.setClient(studiesComposite);
	}
	
	public void setFocus() {
		this.table.setFocus();
	}

	private class StudyMouseHandler implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(StudyAnalysisPerspective.ID));
			
			
			TableItem item = ((Table)e.getSource()).getSelection()[0];
			
			for (Study study : literatureReview.getStudies()) {
				if (study.getCode().equals(item.getText())) {
					StudyAnalysisView view = (StudyAnalysisView) ReviewerViewRegister.getView(StudyAnalysisView.ID);
					view.setLiteratureReview(literatureReview);
					view.setStudy(study);
					break;
				}
			}
		}

		public void mouseDown(MouseEvent e) {
			
		}

		public void mouseUp(MouseEvent e) {
			
		}
		
	}
	
	private class ExportLinkHandler implements IHyperlinkListener {

		public void linkEntered(HyperlinkEvent e) {
			
		}

		public void linkExited(HyperlinkEvent e) {
			
		}

		public void linkActivated(HyperlinkEvent e) {
			FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
			fileDialog.setFilterExtensions(new String[] {"*.xls"});
			fileDialog.setOverwrite(true);
			fileDialog.open();

			String filePath = fileDialog.getFilterPath() + File.separator + fileDialog.getFileName();
			if (filePath != null && !filePath.trim().isEmpty()) {
				LiteratureReviewController controller = new LiteratureReviewController();
				controller.exportSudies(literatureReview, filePath);
			}
		}

	}

	*/
}
