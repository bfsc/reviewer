package br.ufpe.cin.reviewer.ui.rcp.evaluator;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Section;

import br.ufpe.cin.reviewer.model.study.Study;
import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class EvaluatorView extends BaseView {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.evaluator.EvaluatorView";
	
	private StudiesComposite studiesComposite;
	
	public EvaluatorView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	@Override
	public void createPartControlImpl(Composite parent) {
		// TODO Auto-generated method stub
		configureView(parent);
		createStudiesWidgets();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	// PRIVATE METHODS
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Evaluate studies");
		super.form.getBody().setLayout(new GridLayout(1, false));
	}
	
	private void createStudiesWidgets(){
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		studiesComposite = new StudiesComposite(section, SWT.NONE);
		studiesComposite.setLayout(new GridLayout(1, true));
		studiesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		studiesComposite.setVisible(false);

		section.setClient(studiesComposite);
		studiesComposite.setStudies(null);
	}
	
	private class StudiesComposite extends Composite {
		
		
		private Table table;
		private Composite studiesCompositeLabels;
		private Label labelTitle;
		private Button saveButton;
		
		public StudiesComposite(Composite parent, int style) {
			super(parent, style);
			
			studiesCompositeLabels = toolkit.createComposite(this);
			studiesCompositeLabels.setLayout(new GridLayout(2, false));

			labelTitle = toolkit.createLabel(studiesCompositeLabels, "Title:");
			labelTitle.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
			
			table = toolkit.createTable(this, SWT.BORDER | SWT.FULL_SELECTION);
			table.setLinesVisible (true);
			table.setHeaderVisible (true);
			GridData tableLayoutData = new GridData(GridData.FILL_BOTH);
			table.setLayoutData(tableLayoutData);
			
			saveButton = toolkit.createButton(this, "save studies", SWT.PUSH);
			GridData exportButtonLayoutData = new GridData();
			exportButtonLayoutData.horizontalAlignment = SWT.RIGHT;
			saveButton.setLayoutData(exportButtonLayoutData);
			saveButton.addSelectionListener(new SaveButtonHandler());			
					
		}
		
		public Table getTable(){
			return this.table;
		}
		
		public void setStudies(LinkedList<Study> studies) {
			
			table.removeAll();
			
			String[] titles = {"N#", "Source", "Title", "Authors", "Year"};
			for (int i=0; i<titles.length; i++) {
				TableColumn column = new TableColumn (table, SWT.NONE);
				column.setText (titles [i]);
			}
			
			labelTitle.setText("Title: TESTE");
			
			for (int i=0; i<titles.length; i++) {
				table.getColumn (i).pack ();
			}
			
			this.getTable().setFocus();
			
			WidgetsUtil.refreshComposite(studiesComposite);
			WidgetsUtil.refreshComposite(form.getBody());
			
			studiesComposite.setVisible(true);
		}
		
		private class SaveButtonHandler implements SelectionListener {
			
			
			public void widgetSelected(SelectionEvent e) {
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		}
	}

}
