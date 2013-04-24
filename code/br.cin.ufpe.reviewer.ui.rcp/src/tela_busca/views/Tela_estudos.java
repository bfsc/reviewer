package tela_busca.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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


public class Tela_estudos extends ViewPart {
	private FormToolkit toolkit;
	private ScrolledForm form;

	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createScrolledForm(parent);
		form.setText("Analisys");
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
		layout.numColumns = 7;

		
		TableWrapData td = new TableWrapData();
		Button MyStudies = toolkit.createButton(form.getBody(), "MyStudies", SWT.PUSH);
		td.colspan = 1;
		MyStudies.setLayoutData(td);
		td = new TableWrapData();
		Button searchTop = toolkit.createButton(form.getBody(), "Search", SWT.PUSH);
		td.colspan = 6;
		searchTop.setLayoutData(td);
		td = new TableWrapData();
		Label label = toolkit.createLabel(form.getBody(), "Study name:");
		td.colspan = 1;
		label.setLayoutData(td);
		final Text text = toolkit.createText(form.getBody(), "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		text.addMouseListener(new MouseAdapter(){
			public void mouseActivated(MouseEvent e){
				
			}
		});
		td.colspan = 6;
		text.setLayoutData(td);

	    Section section = toolkit.createSection(form.getBody(), Section.TITLE_BAR);
		td = new TableWrapData(TableWrapData.FILL);
		td.colspan = 7;
		section.setLayoutData(td);
//	    section.setText("Section 1 for demonstration");
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayoutData(new GridData(GridData.FILL_VERTICAL, GridData.FILL_HORIZONTAL));
		sectionClient.setLayout(new GridLayout(1, true));
		Table table = toolkit.createTable(sectionClient, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {"ID", "Status", "Title", "Year"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		int count = 5;
		for (int i=0; i<count; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, "" + (i+1));
			item.setText (1, "Fazer botão ainda!");
			item.setText (2, "Experimentation in Software Engineering");
			item.setText (3, " " + (1980 + i));
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		
		section.setClient(sectionClient);
	}

	@Override
	public void setFocus() {
		form.setFocus();
		
	}
	
	public void dispose(){
		form.dispose();
		super.dispose();
	}

}