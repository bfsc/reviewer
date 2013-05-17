package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.ui.rcp.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.UIConstants;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class LiteratureReviewStudiesView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewStudiesView";
	
	private LiteratureReview literatureReview;
	
	private FormToolkit toolkit;
	private Form form;
	
	private Section section;
	private Composite studiesComposite;
	private Label titleLabel;
	private Table table;
	
	public LiteratureReviewStudiesView() {
		ReviewerViewRegister.putView(ID, this);
	}

	public void setLiteratureReview(LiteratureReview literatureReview) {
		this.literatureReview = literatureReview;

		titleLabel.setText("Title: " + this.literatureReview.getTitle());

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
	
	public void createPartControl(Composite parent) {
		configureView(parent);
		createLiteratureStudiesWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.setText("Literature Review Studies");
		form.getBody().setLayout(new GridLayout(2, false));
	}

	private void createLiteratureStudiesWidgets(Composite parent) {
	    section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setVisible(false);
	    section.setLayout(new GridLayout(1, false));
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		studiesComposite = toolkit.createComposite(section);
		studiesComposite.setLayout(new GridLayout(1, false));
		studiesComposite.setLayoutData(new GridData());
		
		titleLabel = toolkit.createLabel(studiesComposite, "TITLE: ");
		titleLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		titleLabel.setLayoutData(new GridData());

		table = toolkit.createTable(studiesComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.addSelectionListener(new StudyClickHandler());

		String[] titles = {"Code", "Status", "Title", "Year"};
		for (int i=0; i< titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}

		section.setClient(studiesComposite);
	}
	
	public void setFocus() {

	}

	private class StudyClickHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(StudyAnalysisPerspective.ID));
			
			
			TableItem item = (TableItem)e.item;
			
			for (Study study : literatureReview.getStudies()) {
				if (study.getCode().equals(item.getText())) {
					StudyAnalysisView view = (StudyAnalysisView) ReviewerViewRegister.getView(StudyAnalysisView.ID);
					view.setLiteratureReview(literatureReview);
					view.setStudy(study);
					break;
				}
			}
			
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
}
