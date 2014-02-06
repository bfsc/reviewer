package br.ufpe.cin.reviewer.ui.rcp.evaluator;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class EvaluatorPerspective implements IPerspectiveFactory{

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.EvaluatorPerspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	}

}
