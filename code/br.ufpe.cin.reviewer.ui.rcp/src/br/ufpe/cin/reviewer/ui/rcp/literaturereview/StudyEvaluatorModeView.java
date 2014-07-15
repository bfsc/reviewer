package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Section;

import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;

public class StudyEvaluatorModeView extends BaseView {
	
	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyEvaluatorModeView";

	private Section section;

	private Composite sectionComposite;

	private Text titleText;
	private Table studiesTable;
	private Button saveButton;
	
	public StudyEvaluatorModeView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createStudyWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Study Evaluator Mode");
		form.getBody().setLayout(new GridLayout(1, false));
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void createStudyWidgets(Composite parent) {
		section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout(1, false));
	    GridData sectionLayout = new GridData(GridData.FILL_BOTH);
	    sectionLayout.grabExcessVerticalSpace = true;
	    sectionLayout.horizontalSpan = 1;
	    section.setLayoutData(sectionLayout);
	    
		sectionComposite = toolkit.createComposite(section);
		sectionComposite.setLayout(new GridLayout(2, false));
		sectionComposite.setLayoutData(new GridData());

		Label titleLabel = toolkit.createLabel(sectionComposite, "Title: ");
		Label titleNameLabel = toolkit.createLabel(sectionComposite, "Titulo");

		//Info Table
		studiesTable = toolkit.createTable(sectionComposite, SWT.BORDER | SWT.FULL_SELECTION);
		studiesTable.setLinesVisible (true);
		studiesTable.setHeaderVisible (true);
		GridData studiesTableLayoutData = new GridData(GridData.FILL_BOTH);
		studiesTableLayoutData.horizontalSpan = 2;
		studiesTable.setLayoutData(studiesTableLayoutData);
		studiesTable.addMouseListener(new StudyMouseHandler());
		
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

		saveButton = toolkit.createButton(sectionComposite, "Save", SWT.PUSH);
		GridData importButtonLayoutData = new GridData();
		importButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		importButtonLayoutData.horizontalSpan = 2;
		saveButton.setLayoutData(importButtonLayoutData);

		section.setClient(sectionComposite);
	}

	private class StudyMouseHandler implements MouseListener {

		public void mouseDoubleClick(MouseEvent e) {
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(StudyInfoEvaluatorPerspective.ID));
			
			StudyInfoEvaluatorView studyInfoEvaluatorView = (StudyInfoEvaluatorView) ReviewerViewRegister.getView(StudyInfoEvaluatorView.ID);
		}

		public void mouseDown(MouseEvent e) {
			
		}

		public void mouseUp(MouseEvent e) {
			
		}
		
	}

}
