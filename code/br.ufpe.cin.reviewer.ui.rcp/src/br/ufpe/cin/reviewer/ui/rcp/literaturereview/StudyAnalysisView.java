package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import br.ufpe.cin.reviewer.core.common.StudyController;
import br.ufpe.cin.reviewer.model.common.Study;
import br.ufpe.cin.reviewer.model.literaturereview.LiteratureReview;
import br.ufpe.cin.reviewer.ui.rcp.ReviewerViewRegister;
import br.ufpe.cin.reviewer.ui.rcp.util.WidgetsUtil;

public class StudyAnalysisView extends ViewPart {

	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyAnalysisView";
	
	private Study study;
	private LiteratureReview literatureReview;
	
	private FormToolkit toolkit;
	private Form form;
	
	private Label label_Id_conteudo;
	private Label label_Title_conteudo;
	private Label label_Status_conteudo;
	private Label label_Source_conteudo;
	private Label label_Authors_conteudo;
	private Label label_Institution_conteudo;
	private Label label_Country_conteudo;
	private Label label_Link_conteudo;
	private Label label_Abstract_conteudo;
	
	public StudyAnalysisView() {
		ReviewerViewRegister.putView(ID, this);
	}

	public void setStudy(Study study) {
		this.study = study;
		
		label_Id_conteudo.setText(study.getCode());
		label_Title_conteudo.setText(study.getTitle());
		label_Status_conteudo.setText(study.getStatus().toString());
		label_Source_conteudo.setText(study.getSource());
		String authors = "";
		for (String author : study.getAuthors()) {
			authors += author + ",";
		}
		label_Authors_conteudo.setText(authors);
		String institutions = "";
		for (String institution : study.getInstitutions()) {
			institutions += institution + ",";
		}
		label_Institution_conteudo.setText(institutions);
		String countries = "";
		for (String country : study.getCountries()) {
			countries += country + ",";
		}
		label_Country_conteudo.setText(countries);
		label_Link_conteudo.setText(study.getUrl());
		label_Abstract_conteudo.setText(study.getAbstract());
		
		WidgetsUtil.refreshComposite(form.getBody());
	}
	
	public void setLiteratureReview(LiteratureReview literatureReview){
		this.literatureReview = literatureReview;
	}
	
	public void createPartControl(Composite parent) {
		configureView(parent);
		createStudyWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.setText("Reviewer");
		form.getBody().setLayout(new GridLayout(4, false));
	}

	private void createStudyWidgets(Composite parent) {
		GridData layout = new GridData(GridData.GRAB_VERTICAL, GridData.GRAB_HORIZONTAL);
		form.getBody().setLayoutData(layout);

		GridData td;
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Id = toolkit.createLabel(form.getBody(), "Id:");
		td.horizontalSpan = 1;
		label_Id.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Id_conteudo = toolkit.createLabel(form.getBody(), "", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Id_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Title = toolkit.createLabel(form.getBody(), "Title:");
		td.horizontalSpan = 1;
		label_Title.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Title_conteudo = toolkit.createLabel(form.getBody(), "", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Title_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Status = toolkit.createLabel(form.getBody(), "Status:");
		td.horizontalSpan = 1;
		label_Status.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Status_conteudo = toolkit.createLabel(form.getBody(), "", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Status_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Source = toolkit.createLabel(form.getBody(), "Source:");
		td.horizontalSpan = 1;
		label_Source.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Source_conteudo = toolkit.createLabel(form.getBody(), "", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Source_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Authors = toolkit.createLabel(form.getBody(), "Authors:");
		td.horizontalSpan = 1;
		label_Authors.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Authors_conteudo = toolkit.createLabel(form.getBody(), "Victor Basili", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Authors_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Institution = toolkit.createLabel(form.getBody(), "Institution(s):");
		td.horizontalSpan = 1;
		label_Institution.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Institution_conteudo = toolkit.createLabel(form.getBody(), "Universidade Federal de Pernambuco", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Institution_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Country = toolkit.createLabel(form.getBody(), "Country:");
		td.horizontalSpan = 1;
		label_Country.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Country_conteudo = toolkit.createLabel(form.getBody(), "BRA", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Country_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Link = toolkit.createLabel(form.getBody(), "Link:");
		td.horizontalSpan = 1;
		label_Link.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Link_conteudo = toolkit.createLabel(form.getBody(), "http://aehbdfi.com", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Link_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Label label_Abstract = toolkit.createLabel(form.getBody(), "Abstract:");
		td.horizontalSpan = 1;
		label_Abstract.setLayoutData(td);
		td = new GridData(GridData.GRAB_HORIZONTAL);
		label_Abstract_conteudo = toolkit.createLabel(form.getBody(), "", SWT.WRAP);
		td.horizontalSpan = 3;
		label_Abstract_conteudo.setLayoutData(td);
		
		td = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Button include = toolkit.createButton(form.getBody(), "Include", SWT.PUSH);
		td.horizontalSpan = 1;
		td.grabExcessVerticalSpace = true;
		td.verticalAlignment = SWT.END;
		include.setLayoutData(td);
		td = new GridData();
		include.addSelectionListener(new IncludeButtonHandler());
		
		Button exclude = toolkit.createButton(form.getBody(), "Exclude", SWT.PUSH);
		td.horizontalSpan = 1;
		td.grabExcessVerticalSpace = true;
		td.verticalAlignment = SWT.END;
		exclude.setLayoutData(td);
		td = new GridData();
		exclude.addSelectionListener(new ExcludeButtonHandler());
		
		Button skip = toolkit.createButton(form.getBody(), "Skip", SWT.PUSH);
		td.horizontalSpan = 2;
		td.grabExcessVerticalSpace = true;
		td.verticalAlignment = SWT.END;
		skip.setLayoutData(td);
		skip.addSelectionListener(new SkipButtonHandler());
	}
	
	public void skip(){
		
		int index = 0;
		Study nextStudy;
		
		for (Study study : literatureReview.getStudies()) {
			if(study.getId() == StudyAnalysisView.this.study.getId()){
				index = literatureReview.getStudies().indexOf(study);
				break;
			}
		}
		
		if(literatureReview.getStudies().size() > (index + 1)){
			nextStudy = literatureReview.getStudies().get(index + 1);
			this.setStudy(nextStudy);
		}
		else{
			IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			activePage.setPerspective(perspectiveRegistry.findPerspectiveWithId(LiteratureReviewStudiesPerspective.ID));
			
			LiteratureReviewStudiesView literatureReviewStudiesView = (LiteratureReviewStudiesView) ReviewerViewRegister.getView(LiteratureReviewStudiesView.ID);
			literatureReviewStudiesView.setLiteratureReview(literatureReview);
		}
	}

	public void setFocus() {

	}
	
	private class IncludeButtonHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			study.setStatus(Study.StudyStatus.INCLUDED);
			StudyController studyController = new StudyController();
			studyController.updateStudy(study);
			StudyAnalysisView.this.skip();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	public class ExcludeButtonHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			study.setStatus(Study.StudyStatus.EXCLUDED);
			StudyController studyController = new StudyController();
			studyController.updateStudy(study);
			StudyAnalysisView.this.skip();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}
	
	public class SkipButtonHandler implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			
			StudyAnalysisView.this.skip();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
	}

}
