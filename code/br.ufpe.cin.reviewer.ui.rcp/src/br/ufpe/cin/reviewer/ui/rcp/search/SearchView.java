package br.ufpe.cin.reviewer.ui.rcp.search;

import java.util.List;

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

import br.cin.ufpe.reviewer.core.search.SearchController;
import br.cin.ufpe.reviewer.core.search.SearchFilter;
import br.cin.ufpe.reviewer.core.search.SearchResult;
import br.cin.ufpe.reviewer.search.provider.spi.entities.Study;

public class SearchView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.search.SearchView";
	
	private FormToolkit toolkit;
	private ScrolledForm form;
	
	private Text searchText;
	private Button acmCheckBox;
	private Button ieeeCheckBox;
	private Button scienceDirectCheckBox;
	private Button scopusCheckBox;
	private Button springerLinkCheckBox;
	private Button engineeringVillageCheckBox;

	public void createPartControl(Composite parent) {
		configureView(parent);
		createSearchComponents(parent);
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
		
		searchText = toolkit.createText(searchComposite, "Type your text here...");
		td = new TableWrapData(TableWrapData.FILL_GRAB);

		td.heightHint = 80;
		td.colspan = 6;
		searchText.setLayoutData(td);
		td = new TableWrapData();
		
		Button search = toolkit.createButton(searchComposite, "Search", SWT.PUSH);
		td.colspan = 1;
		search.setLayoutData(td);
		search.addSelectionListener(new SearchButtonHandler());
		
		acmCheckBox = toolkit.createButton(searchComposite,"ACM", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		acmCheckBox.setLayoutData(td);
		
		ieeeCheckBox = toolkit.createButton(searchComposite,"IEEE", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		ieeeCheckBox.setLayoutData(td);
		
		scienceDirectCheckBox = toolkit.createButton(searchComposite,"Science Direct", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		scienceDirectCheckBox.setLayoutData(td);
		
		scopusCheckBox = toolkit.createButton(searchComposite,"Scopus", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		scopusCheckBox.setLayoutData(td);
		
		springerLinkCheckBox = toolkit.createButton(searchComposite,"Springer Link", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 1;
		springerLinkCheckBox.setLayoutData(td);
		
		engineeringVillageCheckBox = toolkit.createButton(searchComposite,"Engineering Village", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 2;
		engineeringVillageCheckBox.setLayoutData(td);
		
		section.setClient(searchComposite);
	}
	
	private void createResultComponents(SearchResult searchResult){
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
		table.setLayoutData(data);
		
		String[] titles = {"Title", "Source", "Authors", "Year"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		
		for (String searchProviderKey : searchResult.getAllStudies().keySet()) {
			List<Study> studies = searchResult.getAllStudies().get(searchProviderKey);
			for (Study study : studies) {
				TableItem item = new TableItem (table, SWT.NONE);
				item.setText (0, study.getTitle());
				item.setText (1, searchProviderKey);
				item.setText (2, "");
				item.setText (3, "");
			}
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
			SearchController searchController = new SearchController();
			
			SearchFilter searchFilter = new SearchFilter();
			if (acmCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("ACM");
			}
			
			if (ieeeCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("IEEE");
			}
			
			if (scienceDirectCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("SCIENCE_DIRECT");
			}
			
			if (springerLinkCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("SPRINGER_LINK");
			}
			
			if (scopusCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("SCOPUS");
			}

			if (engineeringVillageCheckBox.getSelection()) {
				searchFilter.addSearchProviderKey("ENGINEERING_VILLAGE");
			}
			
			SearchResult searchResult = searchController.search(searchText.getText(), searchFilter);

			createResultComponents(searchResult);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
}
