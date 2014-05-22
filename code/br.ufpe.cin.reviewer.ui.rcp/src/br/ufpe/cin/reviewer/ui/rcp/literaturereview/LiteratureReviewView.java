package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.framework.Bundle;

import br.ufpe.cin.reviewer.core.literaturereview.LiteratureReviewController;
import br.ufpe.cin.reviewer.model.literaturereview.Criteria;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.model.search.AutomatedSearch;
import br.ufpe.cin.reviewer.model.search.QueryInfo;
import br.ufpe.cin.reviewer.model.search.Search;
import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.common.UIConstants;
import br.ufpe.cin.reviewer.ui.rcp.search.SearchPerspective;
//import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReviewSource;

public class LiteratureReviewView extends BaseView {
	
	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewView";

	private java.util.List<LiteratureReview> literatureReviews;
	private LiteratureReview selectedLiteratureReview;
	
	private Criteria selectedCriteria;
	
	private SashForm sash;
	private Composite listComposite;
	private Section sectionList;
	private List list;
	private ToolBar toolbarList;
	
	private Section sectionInfo;
	private Composite reviewInfoComposite;
	
	private Label titleLabel;
	private Label titleInfoLabel;
	private Composite criteriaListComposite;
	
	private Section sectionCriteria;
	private List criteriaList;
	private ToolBar toolbarCriteria;
	
	private Section sectionStudies;
	private Composite studiesComposite;
	private ToolBar toolbarStudies;

	private Section sectionManual;
	private Composite manualComposite;
	private Table infoTable;
	private ToolBar toolbarManual;
	
	private Section sectionAutomatic;
	private Composite automaticComposite;
	private ToolBar toolbarAutomatic;
	private Label QueryStringLabel;
	private Label QueryLabel;
	private Label SourceLabel;
	private Table sourceTable;
	
	private Composite buttonInfoComposite;
	private Button evaluateButton;
	private Button exportButton;
	
	public LiteratureReviewView() {
		ReviewerViewRegister.putView(ID, this);
	}
	

	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createLiteratureWidgets(parent);
	}
	
	public void setFocus() {

	}
	
	public void refreshLiteratureView() {
		LiteratureReviewController literatureReviewController = new LiteratureReviewController();
		literatureReviews = literatureReviewController.findAllLiteratureReview();
		
		list.removeAll();
		for (LiteratureReview literatureReview : literatureReviews) {
			list.add(literatureReview.getTitle());
		}
	}	
	
	public void refreshCriteriaList() {
		if (selectedLiteratureReview != null) {
			java.util.List<Criteria> cr = selectedLiteratureReview
					.getCritireon();

			criteriaList.removeAll();
			for (Criteria c : cr) {
				criteriaList.add(c.getName());
			}
		}
	}
	
	public LiteratureReview getSelectedLiteratureReview() {
		return this.selectedLiteratureReview;
	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - My literature reviews");
		super.form.getBody().setLayout(new GridLayout(1, false));
	}

	private void createLiteratureWidgets(Composite parent) {
		//criando imagens
	    String PLUGIN_ID = "br.ufpe.cin.reviewer.ui.rcp";
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
        Image addIcon = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/Add-Green-Button-icon.png"), null)).createImage();
        Image minusIcon = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/Minus-Green-Button-icon.png"), null)).createImage();
        Image AutomaticIcon = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/A-Green-Button-icon.png"), null)).createImage();
        Image ManualIcon = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/M-Green-Button-icon.png"), null)).createImage();
        Image DeleteIcon = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/D-Green-Button-icon.png"), null)).createImage();
		
		sash = new SashForm(form.getBody(),SWT.HORIZONTAL);
		sash.setLayout(new GridLayout(4, false));
		GridData sashLayout = new GridData(GridData.FILL_BOTH);
		sashLayout.grabExcessHorizontalSpace = true;
		sashLayout.grabExcessVerticalSpace = true;
		sash.setLayoutData(sashLayout);
		sash.getMaximizedControl();
		
		//Section for List of reviews
	    sectionList = toolkit.createSection(sash, Section.SHORT_TITLE_BAR);
	    sectionList.setText("REVIEWS");	    
	    sectionList.setLayout(new GridLayout(1, false));
	    GridData sectionListLayout = new GridData(GridData.FILL_VERTICAL);
	    sectionListLayout.horizontalSpan = 1;
		sectionList.setLayoutData(sectionListLayout);
		
	    toolbarList = new ToolBar (sectionList, SWT.NONE);
	    ToolItem itemAddReview = new ToolItem(toolbarList, SWT.BUTTON1);
	    itemAddReview.setImage(addIcon);
	    ToolItem itemDeleteReview = new ToolItem(toolbarList, SWT.BUTTON1);
	    itemDeleteReview.setImage(minusIcon);
	    sectionList.setTextClient(toolbarList);
	    
		listComposite = toolkit.createComposite(sectionList, SWT.BORDER);
		listComposite.setLayout(new GridLayout(2, false));
		listComposite.setLayoutData(new GridData());
		
		list = new List (listComposite, SWT.V_SCROLL);
		GridData listLayoutData = new GridData(GridData.FILL_BOTH);
		listLayoutData.horizontalSpan = 1;
		list.setLayoutData(listLayoutData);

		list.addSelectionListener(new LiteratureReviewsListHandler());
		refreshLiteratureView();
		
		itemAddReview.addSelectionListener(new LiteratureReviewAddReviewHandler());
		itemDeleteReview.addSelectionListener(new LiteratureReviewRemoveReviewHandler());
		
		//Section for review information
	    sectionInfo = toolkit.createSection(sash, Section.SHORT_TITLE_BAR);
	    sectionInfo.setText("REVIEW INFO");
	    sectionInfo.setLayout(new GridLayout(1, false));
		sectionInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
		

		reviewInfoComposite = toolkit.createComposite(sectionInfo, SWT.BORDER);
		GridData reviewCompositeData = new GridData(GridData.FILL_BOTH);
		reviewCompositeData.horizontalSpan = 1;
		reviewInfoComposite.setLayoutData(reviewCompositeData);
		reviewInfoComposite.setLayout(new GridLayout(3, false));
		reviewInfoComposite.setVisible(true);

		//Review Title
		titleLabel = toolkit.createLabel(reviewInfoComposite, "TITLE: ");
		titleLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		titleLabel.setLayoutData(new GridData());

		//Review Title
		titleInfoLabel = toolkit.createLabel(reviewInfoComposite, "Pesquisa 1");
		titleInfoLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.NONE));
		GridData titleInfoLabelData = new GridData(GridData.FILL_HORIZONTAL);
		titleInfoLabelData.horizontalSpan = 2;
		titleInfoLabel.setLayoutData(titleInfoLabelData);

		//Criteria List
	    sectionCriteria = toolkit.createSection(reviewInfoComposite, Section.SHORT_TITLE_BAR);
	    sectionCriteria.setText("CRITERIONS");
	    sectionCriteria.setLayout(new GridLayout(2, false));
	    GridData sectionCriteriaLayout = new GridData(GridData.FILL_HORIZONTAL);
	    sectionCriteriaLayout.horizontalSpan = 3;
	    sectionCriteria.setLayoutData(sectionCriteriaLayout);
		
	    toolbarCriteria = new ToolBar (sectionCriteria, SWT.NONE);
	    ToolItem itemAddCriteria = new ToolItem(toolbarCriteria, SWT.BUTTON1);

	    itemAddCriteria.setImage(addIcon);    
	    ToolItem itemDeleteCriteria = new ToolItem(toolbarCriteria, SWT.BUTTON1);
	    itemDeleteCriteria.setImage(minusIcon);
	    sectionCriteria.setTextClient(toolbarCriteria);
		
		criteriaListComposite = toolkit.createComposite(sectionCriteria, SWT.BORDER);
		criteriaListComposite.setLayout(new GridLayout(1, false));
		criteriaListComposite.setLayoutData(new GridData());
		
		criteriaList = new List (criteriaListComposite, SWT.V_SCROLL);
		GridData criterialistLayout = new GridData(GridData.FILL_BOTH);
		criterialistLayout.horizontalSpan = 1;
		criteriaList.setLayoutData(criterialistLayout);
		
		criteriaList.addSelectionListener(new CriteriaListHandler());
		refreshCriteriaList();
		itemAddCriteria.addSelectionListener(new LiteratureReviewAddCriteriaHandler());
		itemDeleteCriteria.addSelectionListener(new LiteratureReviewRemoveCriteriaHandler());
		
		//Studies section
		sectionStudies = toolkit.createSection(reviewInfoComposite, Section.SHORT_TITLE_BAR);
		sectionStudies.setText("STUDIES");
		sectionStudies.setLayout(new GridLayout(2, false));
	    GridData sectionStudiesLayout = new GridData(GridData.FILL_BOTH);
	    sectionStudiesLayout.horizontalSpan = 3;
	    sectionStudies.setLayoutData(sectionStudiesLayout);
		
	    toolbarStudies = new ToolBar (sectionStudies, SWT.NONE);
	    final ToolItem itemAutomatic = new ToolItem(toolbarStudies, SWT.DROP_DOWN);
	    itemAutomatic.setImage(AutomaticIcon);
	    itemAutomatic.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                //super.widgetSelected(e);
                Menu menu = new Menu( form.getShell(), SWT.POP_UP);

                MenuItem item1 = new MenuItem(menu, SWT.PUSH);
                item1.setText("New search");
                item1.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
						IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(SearchPerspective.ID));
						
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
                MenuItem item2 = new MenuItem(menu, SWT.PUSH);
                item2.setText("import BibText");

                Point loc = itemAutomatic.getParent().getLocation();
                Rectangle rect = itemAutomatic.getBounds();

                Point mLoc = new Point(loc.x-1, loc.y+rect.height);

                menu.setLocation(form.getShell().getDisplay().map(itemAutomatic.getParent().getParent(), null, mLoc));

                menu.setVisible(true);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	    final ToolItem itemManual = new ToolItem(toolbarStudies, SWT.DROP_DOWN);
	    itemManual.setImage(ManualIcon);
	    itemManual.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
                //super.widgetSelected(e);
                Menu menu = new Menu( form.getShell(), SWT.POP_UP);

                MenuItem item1 = new MenuItem(menu, SWT.PUSH);
                item1.setText("Add Study");
                item1.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
						IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(ManualStudyPerspective.ID));
								
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
                MenuItem item2 = new MenuItem(menu, SWT.PUSH);
                item2.setText("Import BibText");

                Point loc = itemManual.getParent().getLocation();
                Rectangle rect = itemManual.getBounds();

                Point mLoc = new Point(loc.x+38, loc.y+rect.height);

                menu.setLocation(form.getShell().getDisplay().map(itemManual.getParent().getParent(), null, mLoc));

                menu.setVisible(true);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	    final ToolItem itemDeleteStudies = new ToolItem(toolbarStudies, SWT.DROP_DOWN);
	    itemDeleteStudies.setImage(DeleteIcon);
	    itemDeleteStudies.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
                //super.widgetSelected(e);
                Menu menu = new Menu( form.getShell(), SWT.POP_UP);

                MenuItem item1 = new MenuItem(menu, SWT.PUSH);
                item1.setText("Remove by title");
                MenuItem item2 = new MenuItem(menu, SWT.PUSH);
                item2.setText("Remove by abstract");

                Point loc = itemDeleteStudies.getParent().getLocation();
                Rectangle rect = itemDeleteStudies.getBounds();

                Point mLoc = new Point(loc.x+76, loc.y+rect.height);

                menu.setLocation(form.getShell().getDisplay().map(itemDeleteStudies.getParent().getParent(), null, mLoc));

                menu.setVisible(true);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	    sectionStudies.setTextClient(toolbarStudies);
		
		studiesComposite = toolkit.createComposite(sectionStudies, SWT.BORDER);
		studiesComposite.setLayout(new GridLayout(1, false));
		studiesComposite.setLayoutData(new GridData());
		
		//Manual Studies section
		sectionManual = toolkit.createSection(studiesComposite, Section.SHORT_TITLE_BAR);
		sectionManual.setText("MANUAL STUDIES");
		sectionManual.setLayout(new GridLayout(2, false));
	    GridData sectionManualLayout = new GridData(GridData.FILL_BOTH);
	    sectionManualLayout.horizontalSpan = 2;
	    sectionManual.setLayoutData(sectionManualLayout);
		
	    toolbarManual = new ToolBar (sectionManual, SWT.NONE);
	    ToolItem itemDeleteManual = new ToolItem(toolbarManual, SWT.BUTTON1);
	    itemDeleteManual.setImage(minusIcon);
	    sectionManual.setTextClient(toolbarManual);
		
		manualComposite = toolkit.createComposite(sectionManual, SWT.BORDER);
		manualComposite.setLayout(new GridLayout(1, false));
		manualComposite.setLayoutData(new GridData());

		//Info Table
		infoTable = toolkit.createTable(manualComposite, SWT.BORDER | SWT.FULL_SELECTION);
		infoTable.setLinesVisible (true);
		infoTable.setHeaderVisible (true);
		GridData infoTableLayoutData = new GridData(GridData.FILL_BOTH);
		infoTable.setLayoutData(infoTableLayoutData);
		//insert columns and set their names
		String[] titles = {"info 1", "info 2", "info 3", "info 4"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (infoTable, SWT.NONE);
			column.setText (titles [i]);
		}
		for (int i=0; i<titles.length; i++) {
			infoTable.getColumn (i).pack ();
		}
		
		//Automatic Studies section
		sectionAutomatic = toolkit.createSection(studiesComposite, Section.SHORT_TITLE_BAR);
		sectionAutomatic.setText("AUTOMATIC STUDIES");
		sectionAutomatic.setLayout(new GridLayout(2, false));
	    GridData sectionAutomaticLayout = new GridData(GridData.FILL_BOTH);
	    sectionAutomaticLayout.horizontalSpan = 2;
	    sectionAutomatic.setLayoutData(sectionAutomaticLayout);
		
	    toolbarAutomatic = new ToolBar (sectionAutomatic, SWT.NONE);
	    ToolItem itemDeleteAutomatic = new ToolItem(toolbarAutomatic, SWT.BUTTON1);
	    itemDeleteAutomatic.setImage(minusIcon);
	    sectionAutomatic.setTextClient(toolbarAutomatic);
		
		automaticComposite = toolkit.createComposite(sectionAutomatic, SWT.BORDER);
		automaticComposite.setLayout(new GridLayout(2, false));
		automaticComposite.setLayoutData(new GridData());

		//Query String Label
		QueryStringLabel = toolkit.createLabel(automaticComposite, "QUERY STRING: ");
		QueryStringLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		QueryStringLabel.setLayoutData(new GridData());

		//Query String Label
		QueryLabel = toolkit.createLabel(automaticComposite, "");
		QueryLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.NONE));
		QueryLabel.setLayoutData(new GridData());

		//Source Label
		SourceLabel = toolkit.createLabel(automaticComposite, "SOURCE");
		SourceLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		GridData sourceLabelData = new GridData();
		sourceLabelData.horizontalSpan = 2;
		SourceLabel.setLayoutData(sourceLabelData);
		
		//Source Table
		sourceTable = toolkit.createTable(automaticComposite, SWT.BORDER | SWT.FULL_SELECTION);
		sourceTable.setLinesVisible (true);
		sourceTable.setHeaderVisible (true);
		GridData sourceTableLayoutData = new GridData(GridData.FILL_BOTH);
		sourceTableLayoutData.horizontalSpan = 2;
		sourceTable.setLayoutData(sourceTableLayoutData);
		//insert columns and set their names
		String[] titles2 = {"Source", "Total Founded", "Total Fetched"};
		for (int i=0; i<titles2.length; i++) {
			TableColumn column = new TableColumn (sourceTable, SWT.NONE);
			column.setText (titles2 [i]);
		}
		for (int i=0; i<titles2.length; i++) {
			sourceTable.getColumn (i).pack ();
		}
		
		buttonInfoComposite = toolkit.createComposite(reviewInfoComposite);
		buttonInfoComposite.setLayout(new GridLayout(2, false));
		GridData buttonInfoData = new GridData(GridData.FILL_HORIZONTAL);
		buttonInfoData.horizontalSpan = 3;
		buttonInfoData.grabExcessHorizontalSpace = true;
		buttonInfoComposite.setLayoutData(buttonInfoData);
		
		exportButton = toolkit.createButton(buttonInfoComposite, "Export review", SWT.PUSH);
		GridData exportButtonLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		exportButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		//exportButtonLayoutData.grabExcessHorizontalSpace = true;
		exportButtonLayoutData.horizontalSpan = 1;
		exportButton.setLayoutData(exportButtonLayoutData);
		
		evaluateButton = toolkit.createButton(buttonInfoComposite, "evaluate studies", SWT.PUSH);
		GridData evaluateButtonLayoutData = new GridData();
		evaluateButtonLayoutData.horizontalAlignment = SWT.RIGHT;
		//evaluateButtonLayoutData.grabExcessHorizontalSpace = true;
		evaluateButtonLayoutData.horizontalSpan = 1;
		evaluateButton.setLayoutData(evaluateButtonLayoutData);
		evaluateButton.addSelectionListener(new LiteratureReviewPhasesButtonHandler());
		
		sash.setWeights(new int[] {1, 3});

		sectionList.setClient(listComposite);
		sectionInfo.setClient(reviewInfoComposite);
		sectionCriteria.setClient(criteriaListComposite);
		sectionStudies.setClient(studiesComposite);
		sectionManual.setClient(manualComposite);
		sectionAutomatic.setClient(automaticComposite);
	
	}
	
	private class LiteratureReviewPhasesButtonHandler implements SelectionListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewPhasesPerspective.ID));
			
			LiteratureReviewPhasesView literatureReviewStudiesView = (LiteratureReviewPhasesView) ReviewerViewRegister.getView(LiteratureReviewPhasesView.ID);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public void populateReviewInfo() {
		
		refreshCriteriaList();		
		refreshAutomaticSearchTable();		
	}
	
	
	private void refreshAutomaticSearchTable() {
		
		sourceTable.removeAll();
		
		for (Search s: selectedLiteratureReview.getSearches()) {
			
			if (s instanceof AutomatedSearch) {
				AutomatedSearch as = (AutomatedSearch)s;
				this.QueryLabel.setText(as.getQueryString());				
				
				/*TableItem generalItem = new TableItem (sourceTable, SWT.NONE);
				generalItem.setText(0, "ALL");
				generalItem.setText(1, String.valueOf(selectedLiteratureReview.getTotalFound()));
				generalItem.setText(2, String.valueOf(selectedLiteratureReview.getTotalFetched()));*/
				
				for (QueryInfo qi : as.getQueryInfos()) {
					TableItem item = new TableItem (sourceTable, SWT.NONE);
					item.setText(0, qi.getSource());
					item.setText(1, String.valueOf(qi.getTotalFound()));
					item.setText(2, String.valueOf(qi.getTotalFound()));
				}
				
				for (int i=0; i < sourceTable.getColumnCount(); i++) {
					sourceTable.getColumn(i).pack();
				}		
			}
		}
	}

	private class LiteratureReviewsListHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = list.getSelectionIndex();
			
			if (selectionIndex >= 0) {
				selectedLiteratureReview = literatureReviews.get(selectionIndex);
				populateReviewInfo();
				reviewInfoComposite.setVisible(true);				
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	private class LiteratureReviewAddReviewHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			InputDialog dialog = new InputDialog(shell, "Create literature review", "Literature review title", null, null);
			dialog.open();
			
			if (dialog.getReturnCode() == InputDialog.OK) {
				LiteratureReview literatureReview = new LiteratureReview();
				
				literatureReview.setTitle(dialog.getValue());
				
				LiteratureReviewController literatureReviewController = new LiteratureReviewController();
				literatureReviewController.createLiteratureReview(literatureReview);
				refreshLiteratureView();
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	private class LiteratureReviewRemoveReviewHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {

			MessageBox dialog =  new MessageBox(form.getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText("Reviewer");
			dialog.setMessage("Do you really want to delete this literature review?");
			
			int returnCode = dialog.open();
			
			if (returnCode == SWT.OK) {
				LiteratureReviewController literatureReviewController = new LiteratureReviewController();
				literatureReviewController.deleteLiteratureReview(selectedLiteratureReview);
				
				list.remove(selectedLiteratureReview.getTitle());
				literatureReviews.remove(selectedLiteratureReview);
				
				selectedLiteratureReview = null;
				reviewInfoComposite.setVisible(false);
	
				refreshLiteratureView();
			}
			
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class LiteratureReviewAddCriteriaHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			InputDialog dialog = new InputDialog(shell, "Create Criteria", "Criteria Description", null, null);
			dialog.open();
			
			if (dialog.getReturnCode() == InputDialog.OK) {
				Criteria c = new Criteria();
				c.setName(dialog.getValue());
				selectedLiteratureReview.getCritireon().add(c);
				LiteratureReviewController literatureReviewController = new LiteratureReviewController();
				literatureReviewController.updateLiteratureReview(selectedLiteratureReview);
				refreshCriteriaList();
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	private class LiteratureReviewRemoveCriteriaHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {

			MessageBox dialog =  new MessageBox(form.getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText("Reviewer");
			dialog.setMessage("Do you really want to delete this literature review?");
			
			int returnCode = dialog.open();
			
			if (returnCode == SWT.OK) {
				selectedLiteratureReview.getCritireon().remove(criteriaList.getSelectionIndex());			
				
				LiteratureReviewController literatureReviewController = new LiteratureReviewController();
				literatureReviewController.updateLiteratureReview(selectedLiteratureReview);
				refreshCriteriaList();
			}
			
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class CriteriaListHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = criteriaList.getSelectionIndex();
			
			if (selectionIndex >= 0) {
				selectedCriteria = selectedLiteratureReview.getCritireon().get(selectionIndex);
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	/*

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewView";

	private java.util.List<LiteratureReview> literatureReviews;
	private LiteratureReview selectedLiteratureReview;
	
	private SashForm sash;
	private Section sectionList;
	private Composite listComposite;
	private List list;
	
	private Section sectionInfo;
	private Composite reviewInfoComposite;
	
	private Composite searchHeaderComposite;
	private Composite reviewInfoBodyComposite;
	private Composite reviewInfoFooterComposite;

	private Label titleLabel;
	private StyledText titleText;
	
	private StyledText queryStringText;
	
	private Table sourcesTable;
	
	public LiteratureReviewView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	public void refreshView() {
		LiteratureReviewController literatureReviewController = new LiteratureReviewController();
		literatureReviews = literatureReviewController.findAllLiteratureReview();
		
		list.removeAll();
		for (LiteratureReview literatureReview : literatureReviews) {
			list.add(literatureReview.getTitle());
		}
	}
	
	public void setSelectedLiteratureReview(LiteratureReview literatureReview) {
		selectedLiteratureReview = literatureReview;
		populateReviewInfo();
		sectionInfo.setVisible(true);
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createLiteratureWidgets(parent);
	}
	
	public void setFocus() {

	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - My literature reviews");
		super.form.getBody().setLayout(new GridLayout(1, false));
	}

	private void createLiteratureWidgets(Composite parent) {
		
		sash = new SashForm(form.getBody(),SWT.HORIZONTAL);
		sash.setLayout(new GridLayout(4, false));
		GridData sashLayout = new GridData(GridData.FILL_BOTH);
		sashLayout.grabExcessHorizontalSpace = true;
		sashLayout.grabExcessVerticalSpace = true;
		sash.setLayoutData(sashLayout);
		sash.getMaximizedControl();
		
		//Section for List
	    sectionList = toolkit.createSection(sash, Section.NO_TITLE);
	    sectionList.setLayout(new GridLayout(1, false));
	    GridData sectionListLayout = new GridData(GridData.FILL_VERTICAL);
	    sectionListLayout.horizontalSpan = 1;
		sectionList.setLayoutData(sectionListLayout);
		
		listComposite = toolkit.createComposite(sectionList, SWT.BORDER);
		listComposite.setLayout(new GridLayout(2, false));
		listComposite.setLayoutData(new GridData());
		
		list = new List (listComposite, SWT.V_SCROLL);
		GridData listLayoutData = new GridData(GridData.FILL_BOTH);
		listLayoutData.horizontalSpan = 1;
		list.setLayoutData(listLayoutData);
		list.addSelectionListener(new LiteratureReviewsListHandler());
		refreshView();
		
		//Section for information
	    sectionInfo = toolkit.createSection(sash, Section.NO_TITLE);
	    sectionInfo.setLayout(new GridLayout(1, false));
		sectionInfo.setLayoutData(new GridData(GridData.FILL_BOTH));

		reviewInfoComposite = toolkit.createComposite(sectionInfo, SWT.BORDER);
		GridData reviewCompositeData = new GridData(GridData.FILL_BOTH);
		reviewCompositeData.horizontalSpan = 1;
		reviewInfoComposite.setLayoutData(reviewCompositeData);
		reviewInfoComposite.setLayout(new GridLayout(1, false));
		reviewInfoComposite.setVisible(false);
		
		sash.setWeights(new int[] {1, 3});
		
		// Header composite
		searchHeaderComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		GridData searchTitleCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		searchTitleCompositeData.horizontalSpan = 2;
		searchHeaderComposite.setLayoutData(searchTitleCompositeData);
		searchHeaderComposite.setLayout(new GridLayout(2, false));

		titleLabel = toolkit.createLabel(searchHeaderComposite, "TITLE: ");
		titleLabel.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		titleLabel.setLayoutData(new GridData());
		
		titleText = new StyledText(searchHeaderComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		this.titleText.setFont(new Font(UIConstants.APP_DISPLAY, UIConstants.SYSTEM_FONT_NAME, 10, SWT.BOLD));
		this.titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.titleText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.titleText, true, true);
		
		// Body composite
		reviewInfoBodyComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		reviewInfoBodyComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		reviewInfoBodyComposite.setLayout(new GridLayout(2, false));
		
		// Query string
//		Composite queryStringComposite = toolkit.createComposite(reviewInfoBodyComposite);
//		queryStringComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		queryStringComposite.setLayout(new GridLayout(2, false));
		
		Label queryStringLabel = toolkit.createLabel(reviewInfoBodyComposite, "QUERY STRING:");
		queryStringLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		this.queryStringText = new StyledText(reviewInfoBodyComposite, SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		this.queryStringText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.queryStringText.addFocusListener(new StyleTextFocusHandler());
		this.toolkit.adapt(this.queryStringText, true, true);
		
		// Span composite
		Composite spanComposite = toolkit.createComposite(reviewInfoBodyComposite);
		GridData sgc = new GridData(GridData.FILL_HORIZONTAL);
		sgc.heightHint = 10;
		sgc.horizontalSpan = 2;
		spanComposite.setLayoutData(sgc);
		
		// Sources
//		Composite sourcesComposite = toolkit.createComposite(reviewInfoBodyComposite);
//		sourcesComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		sourcesComposite.setLayout(new GridLayout(2, false));
		
		Label sourcesLabel = toolkit.createLabel(reviewInfoBodyComposite, "SOURCES:");
		sourcesLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		this.sourcesTable = toolkit.createTable(reviewInfoBodyComposite, SWT.BORDER | SWT.FULL_SELECTION);
		this.sourcesTable.setLinesVisible (true);
		this.sourcesTable.setHeaderVisible (true);
		this.sourcesTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		String[] titles = {"Source", "Total Found", "Total Fetched"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (this.sourcesTable, SWT.NONE);
			column.setText(titles [i]);
		}

		// Footer composite
		reviewInfoFooterComposite = toolkit.createComposite(reviewInfoComposite, SWT.NONE);
		reviewInfoFooterComposite.setLayout(new GridLayout(2, false));
		GridData reviewFooterCompositeData = new GridData(GridData.FILL_BOTH);
		reviewFooterCompositeData.horizontalSpan = 1;
		reviewInfoFooterComposite.setLayoutData(reviewFooterCompositeData);
		
		Hyperlink studyLink = toolkit.createHyperlink(reviewInfoFooterComposite, "View studies", SWT.WRAP);
		GridData studyLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END | GridData.HORIZONTAL_ALIGN_END | GridData.GRAB_HORIZONTAL);
		studyLinkLayout.grabExcessVerticalSpace = true;
		studyLink.setLayoutData(studyLinkLayout);
		studyLink.addHyperlinkListener(new LiteratureReviewStudiesLinkHandler());
		
		Hyperlink deleteLink = toolkit.createHyperlink(reviewInfoFooterComposite, "Delete literature review", SWT.WRAP);
		GridData deleteLinkLayout = new GridData(GridData.VERTICAL_ALIGN_END | GridData.HORIZONTAL_ALIGN_END);
		deleteLinkLayout.grabExcessVerticalSpace = true;
		deleteLink.setLayoutData(deleteLinkLayout);
		deleteLink.addHyperlinkListener(new DeleteLiteratureReviewLinkHandler());

		sectionList.setClient(listComposite);
		sectionInfo.setClient(reviewInfoComposite);
	}

	private void populateReviewInfo() {
		titleText.setText(selectedLiteratureReview.getTitle());

		this.queryStringText.setText(selectedLiteratureReview.getQueryString());
		this.queryStringText.setLineJustify(0, this.queryStringText.getLineCount(), true);
		
		sourcesTable.removeAll();
		
		TableItem generalItem = new TableItem (sourcesTable, SWT.NONE);
		generalItem.setText(0, "ALL");
		generalItem.setText(1, String.valueOf(selectedLiteratureReview.getTotalFound()));
		generalItem.setText(2, String.valueOf(selectedLiteratureReview.getTotalFetched()));
		
		for (LiteratureReviewSource source : selectedLiteratureReview.getSources()) {
			TableItem item = new TableItem (sourcesTable, SWT.NONE);
			item.setText(0, source.getName());
			item.setText(1, String.valueOf(source.getTotalFound()));
			item.setText(2, String.valueOf(source.getTotalFetched()));
		}
		
		for (int i=0; i < sourcesTable.getColumnCount(); i++) {
			sourcesTable.getColumn(i).pack();
		}
		
		WidgetsUtil.refreshComposite(form.getBody());
		WidgetsUtil.refreshComposite(reviewInfoBodyComposite);
	}
	
	private class LiteratureReviewsListHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = list.getSelectionIndex();
			
			if (selectionIndex >= 0) {
				selectedLiteratureReview = literatureReviews.get(selectionIndex);
				populateReviewInfo();
				reviewInfoComposite.setVisible(true);				
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	private class LiteratureReviewStudiesLinkHandler implements IHyperlinkListener {

		public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewStudiesPerspective.ID));
			
			LiteratureReviewStudiesView literatureReviewStudiesView = (LiteratureReviewStudiesView) ReviewerViewRegister.getView(LiteratureReviewStudiesView.ID);
			literatureReviewStudiesView.setLiteratureReview(selectedLiteratureReview);
		}
		
	}
	
	private class DeleteLiteratureReviewLinkHandler implements IHyperlinkListener {

		public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
			
		}

		public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {

			MessageBox dialog =  new MessageBox(form.getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText("Reviewer");
			dialog.setMessage("Do you really want to delete this literature review?");
			
			int returnCode = dialog.open();
			
			if (returnCode == SWT.OK) {
				LiteratureReviewController literatureReviewController = new LiteratureReviewController();
				literatureReviewController.deleteLiteratureReview(selectedLiteratureReview);
				
				list.remove(selectedLiteratureReview.getTitle());
				literatureReviews.remove(selectedLiteratureReview);
				
				selectedLiteratureReview = null;
				reviewInfoComposite.setVisible(false);
	
				WidgetsUtil.refreshComposite(listComposite);
				WidgetsUtil.refreshComposite(reviewInfoComposite);
			}
			
		}
		
	}
	
	private class StyleTextFocusHandler implements FocusListener {

		public void focusGained(FocusEvent e) {
			
		}

		public void focusLost(FocusEvent e) {
			StyledText sourceWidget = (StyledText) e.getSource();
			sourceWidget.setSelection(0);
		}
		
	}
	
	
	*/
}
