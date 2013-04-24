package tela_busca.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;


public class Tela_busca extends ViewPart {
	private FormToolkit toolkit;
	private ScrolledForm form;

	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createScrolledForm(parent);
		form.setText("Reviewer");
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
		layout.numColumns = 7;

		
		TableWrapData td = new TableWrapData();
		Button MyStudies = toolkit.createButton(form.getBody(), "MyStudies", SWT.PUSH);
		td.colspan = 1;
		MyStudies.setLayoutData(td);
		td = new TableWrapData();
		Button searchTop = toolkit.createButton(form.getBody(), "Search", SWT.PUSH);
		td.colspan = 5;
		searchTop.setLayoutData(td);
		Hyperlink link = toolkit.createHyperlink(form.getBody(), "How to search", SWT.WRAP);
		link.addHyperlinkListener(new HyperlinkAdapter(){
			public void linkActivated(HyperlinkEvent e){
				
			}
		});
		final Text text = toolkit.createText(form.getBody(), "Type your text here...");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		text.addMouseListener(new MouseAdapter(){
			public void mouseActivated(MouseEvent e){
				
			}
		});

		
		td.colspan = 6;
		text.setLayoutData(td);
		Button search = toolkit.createButton(form.getBody(), "Search", SWT.PUSH);
		Button button = toolkit.createButton(form.getBody(),"ACM", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button.setLayoutData(td);
		Button button2 = toolkit.createButton(form.getBody(),"IEEE", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button2.setLayoutData(td);
		Button button3 = toolkit.createButton(form.getBody(),"Science Direct", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button3.setLayoutData(td);
		Button button4 = toolkit.createButton(form.getBody(),"Scopus", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button4.setLayoutData(td);
		Button button5 = toolkit.createButton(form.getBody(),"Springer Link", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		button5.setLayoutData(td);
		Button button6 = toolkit.createButton(form.getBody(),"Engineering Village", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 2;
		button6.setLayoutData(td);


		

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
		
		section.setClient(sectionClient);
		
		Hyperlink studyLink = toolkit.createHyperlink(form.getBody(), "Create new study from these results...", SWT.WRAP);
		studyLink.addHyperlinkListener(new HyperlinkAdapter(){
			public void linkActivated(HyperlinkEvent e){
				
			}
		});
		td = new TableWrapData();
		td.colspan = 7;
		studyLink.setLayoutData(td);
				
//			section2.setClient(sectionClient2);
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