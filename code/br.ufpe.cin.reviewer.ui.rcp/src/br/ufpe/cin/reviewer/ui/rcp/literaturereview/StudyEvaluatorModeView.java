package br.ufpe.cin.reviewer.ui.rcp.literaturereview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

import br.ufpe.cin.reviewer.ui.rcp.common.BaseView;
import br.ufpe.cin.reviewer.ui.rcp.common.ReviewerViewRegister;

public class StudyEvaluatorModeView extends BaseView {
	
	public static final String ID = "br.ufpe.cin.reviewer.ui.rcp.literaturereview.StudyEvaluatorModeView";

	private Section section;

	private Composite sectionComposite;

	private Text titleText;
	
	public StudyEvaluatorModeView() {
		ReviewerViewRegister.putView(ID, this);
	}
	
	public void createPartControlImpl(Composite parent) {
		configureView(parent);
		createStudyWidgets(parent);
	}
	
	private void configureView(Composite parent) {
		super.form.setText(super.form.getText() + " - Study Evaluator Mode");
		form.getBody().setLayout(new GridLayout(1, false));
	}

	private void createStudyWidgets(Composite parent) {
		section = toolkit.createSection(form.getBody(), Section.NO_TITLE);
	    section.setLayout(new GridLayout(1, false));
	    GridData sectionLayout = new GridData(GridData.FILL_BOTH);
	    sectionLayout.grabExcessVerticalSpace = true;
	    sectionLayout.horizontalSpan = 1;
	    section.setLayoutData(sectionLayout);
	    
		sectionComposite = toolkit.createComposite(section);
		sectionComposite.setLayout(new GridLayout(2, false));
		sectionComposite.setLayoutData(new GridData());

		Label titleLabel = toolkit.createLabel(sectionComposite, "Title: ");
		Label titleNameLabel = toolkit.createLabel(sectionComposite, "Titulo");

		section.setClient(sectionComposite);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
