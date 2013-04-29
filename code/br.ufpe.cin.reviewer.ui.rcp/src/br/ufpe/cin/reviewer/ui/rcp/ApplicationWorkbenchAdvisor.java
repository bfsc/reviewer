package br.ufpe.cin.reviewer.ui.rcp;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import br.ufpe.cin.reviewer.ui.rcp.search.SearchPerspective;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewPerspective;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.LiteratureReviewStudiesPerspective;
import br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyAnalysisPerspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return StudyAnalysisPerspective.ID;
	}
}
