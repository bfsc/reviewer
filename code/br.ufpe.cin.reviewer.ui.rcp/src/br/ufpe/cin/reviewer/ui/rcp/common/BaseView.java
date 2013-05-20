package br.ufpe.cin.reviewer.ui.rcp.common;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import br.ufpe.cin.reviewer.ui.rcp.Activator;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewPerspective;
import br.ufpe.cin.reviewer.ui.rcp.search.SearchPerspective;

public abstract class BaseView extends ViewPart {

	protected FormToolkit toolkit;
	protected Form form;
	
	public final void createPartControl(Composite parent) {
		this.toolkit = new FormToolkit(parent.getDisplay());
		this.form = toolkit.createForm(parent);
		this.toolkit.decorateFormHeading(this.form);
		this.form.setText("REviewER");
		
		IToolBarManager toolBarManager = this.form.getToolBarManager();
		toolBarManager.add(new SearchPerspectiveAction("Search studies"));
		toolBarManager.add(new LiteratureReviewPerspectiveAction("My literature reviews"));
		toolBarManager.update(true);
		
		createPartControlImpl(parent);
	}
	
	public abstract void createPartControlImpl(Composite parent);

	private class SearchPerspectiveAction extends Action {
		
		public SearchPerspectiveAction(String text) {
			super(text);
		}

		public void run() {
			super.run();
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(SearchPerspective.ID));
		}
		
		public ImageDescriptor getImageDescriptor() {
			ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
			return imageRegistry.getDescriptor(UIConstants.TOOLBAR_SEARCH_ACTION_IMAGE_ID);
		}
		
		public String getToolTipText() {
			return "Search studies";
		}
		
	}
	
	private class LiteratureReviewPerspectiveAction extends Action {
		
		public LiteratureReviewPerspectiveAction(String text) {
			super(text);
		}

		public void run() {
			super.run();
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewPerspective.ID));
		}
		
		public ImageDescriptor getImageDescriptor() {
			ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
			return imageRegistry.getDescriptor(UIConstants.TOOLBAR_LITERATURE_REVIEW_ACTION_IMAGE_ID);
		}
		
		public String getToolTipText() {
			return "My literature reviews";
		}
		
	}
	
}
