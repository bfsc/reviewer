package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class LiteratureReviewStudiesView extends ViewPart {

	private FormToolkit toolkit;
	private Form form;
	
	public LiteratureReviewStudiesView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		configureView(parent);
		createLiteratureStudiesWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		this.toolkit = new FormToolkit(parent.getDisplay());
		this.form = toolkit.createForm(parent);
		this.toolkit.decorateFormHeading(this.form);
		this.form.setText("Reviewer");
		this.form.getBody().setLayout(new GridLayout(2, false));
	}

	private void createLiteratureStudiesWidgets(Composite parent) {
	    Section section = this.toolkit.createSection(this.form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout(1, false));
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite studiesComposite = this.toolkit.createComposite(section);
		studiesComposite.setLayout(new GridLayout(1, false));
		studiesComposite.setLayoutData(new GridData());
		
		Label titleLabel = this.toolkit.createLabel(studiesComposite, "Title: NOME");
		titleLabel.setLayoutData(new GridData());

		Table table = toolkit.createTable(studiesComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData tableLayoutData = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(tableLayoutData);

		String[] titles = {"ID", "Status", "Title", "Year"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}

		section.setClient(studiesComposite);
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
