package br.ufpe.cin.reviewer.ui.rcp.search;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

public class SearchView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.search.SearchView";
	
	private FormToolkit toolkit;
	private ScrolledForm form;

	public void createPartControl(Composite parent) {
		configureView(parent);
		createSearchComponents(parent);
		createResultComponents();
	}

	private void configureView(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createScrolledForm(parent);
		form.setText("Reviewer");
		GridLayout layout = new GridLayout(1, false);
		form.getBody().setLayout(layout);
	}

	private void createSearchComponents(Composite parent) {
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Composite searchComposite = this.toolkit.createComposite(section);
		
		TableWrapLayout layout = new TableWrapLayout();
		TableWrapData td = new TableWrapData();
		searchComposite.setLayout(layout);
		layout.numColumns = 7;
		
		Text text = toolkit.createText(searchComposite, "Type your text here...");
		td = new TableWrapData(TableWrapData.FILL_GRAB);

		td.heightHint = 80;
		td.colspan = 6;
		text.setLayoutData(td);
		td = new TableWrapData();
		
		Button search = toolkit.createButton(searchComposite, "Search", SWT.PUSH);
		td.colspan = 1;
		search.setLayoutData(td);
		search.addSelectionListener(new SearchButtonHandler());
		
		Button button = toolkit.createButton(searchComposite,"ACM", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button.setLayoutData(td);
		
		Button button2 = toolkit.createButton(searchComposite,"IEEE", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button2.setLayoutData(td);
		
		Button button3 = toolkit.createButton(searchComposite,"Science Direct", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button3.setLayoutData(td);
		Button button4 = toolkit.createButton(searchComposite,"Scopus", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button4.setLayoutData(td);
		Button button5 = toolkit.createButton(searchComposite,"Springer Link", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button5.setLayoutData(td);
		Button button6 = toolkit.createButton(searchComposite,"Engineering Village", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 2;
		button6.setLayoutData(td);
		
		section.setClient(searchComposite);
	}
	
	private void createResultComponents(){
	    Section section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Composite resultComposite = toolkit.createComposite(section);
		resultComposite.setLayout(new GridLayout(1, true));
		resultComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Table table = toolkit.createTable(resultComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(GridData.FILL_BOTH);
//		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {"Title", "Authors", "Year"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		int count = 5;
		for (int i=0; i<count; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, "Experimentation in Software Engineering");
			item.setText (1, "Victor Basili");
			item.setText (2, " " + (1980 + i));
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		
		section.setClient(resultComposite);
	}

	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	private class SearchButtonHandler implements SelectionListener{

		public void widgetSelected(SelectionEvent e) {
						
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
}
