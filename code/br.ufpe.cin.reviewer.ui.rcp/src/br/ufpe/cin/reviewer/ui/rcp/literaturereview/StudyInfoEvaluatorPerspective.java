package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class StudyInfoEvaluatorPerspective implements IPerspectiveFactory {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.StudyInfoEvaluatorPerspective";
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	}

}
