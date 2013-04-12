package plugin.views;




import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;



/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {
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
		Label label = toolkit.createLabel(form.getBody(),"Search bar");
		td.colspan = 6;
		label.setLayoutData(td);
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
		Section section = toolkit.createSection(form.getBody(), 
				  Section.DESCRIPTION|Section.TITLE_BAR|
				  Section.TWISTIE|Section.EXPANDED);
				 td = new TableWrapData(TableWrapData.FILL);
				 td.colspan = 4;
				 section.setLayoutData(td);
				 section.addExpansionListener(new ExpansionAdapter() {
				  public void expansionStateChanged(ExpansionEvent e) {
				   form.reflow(true);
				  }
				 });
				 section.setText("Running Studies");
				 Composite sectionClient = toolkit.createComposite(section);
				 sectionClient.setLayout(new GridLayout());
				 button = toolkit.createButton(sectionClient, "Radio 1", SWT.RADIO);
				 button = toolkit.createButton(sectionClient, "Radio 2", SWT.RADIO);
				 section.setClient(sectionClient);

		Section section2 = toolkit.createSection(form.getBody(), 
				  Section.DESCRIPTION|Section.NO_TITLE);
		td = new TableWrapData(TableWrapData.FILL);
		section2.setLayoutData(td);
		Composite sectionClient2 = toolkit.createComposite(section2);
		sectionClient2.setLayout(new GridLayout());

//			CTabFolder folder = new CTabFolder(sectionClient2, SWT.BORDER);
//			folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//			folder.setSimple(false);
//			folder.setUnselectedImageVisible(false);
//			folder.setUnselectedCloseVisible(false);
//			for (int i = 0; i < 8; i++) {
//				CTabItem item = new CTabItem(folder, SWT.CLOSE);
//				item.setText("Item "+i);
//				Text text8 = new Text(folder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
//				text.setText("Text for item "+i+"\n\none, two, three\n\nabcdefghijklmnop");
//				item.setControl(text);
//			}
			section2.setClient(sectionClient2);
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