package br.ufpe.cin.reviewer.ui.rcp.search;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class SearchPerspective implements IPerspectiveFactory {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.SearchPerspective";
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	}

}
