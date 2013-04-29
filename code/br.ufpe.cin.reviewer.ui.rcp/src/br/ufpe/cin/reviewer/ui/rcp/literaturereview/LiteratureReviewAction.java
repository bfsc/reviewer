package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import br.ufpe.cin.reviewer.ui.rcp.search.SearchPerspective;

public class LiteratureReviewAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.literatureReview";
	
	private IWorkbenchWindow window;
	
	@Override
	public void run(IAction action) {
		IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		IWorkbenchPage activePage = window.getActivePage();
		activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewPerspective.ID));
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
