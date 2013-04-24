package tela_busca.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;


public class Tela_analise extends ViewPart {
	private FormToolkit toolkit;
	private ScrolledForm form;

	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());

		form = toolkit.createScrolledForm(parent);
		form.setText("Analisys");
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
		layout.numColumns = 7;

		
		TableWrapData td = new TableWrapData();
		Button MyStudies = toolkit.createButton(form.getBody(), "MyStudies", SWT.PUSH);
		td.colspan = 1;
		MyStudies.setLayoutData(td);
		td = new TableWrapData();
		Button searchTop = toolkit.createButton(form.getBody(), "Search", SWT.PUSH);
		td.colspan = 6;
		searchTop.setLayoutData(td);

		td = new TableWrapData(TableWrapData.RIGHT);
		Label label_Id = toolkit.createLabel(form.getBody(), "Id:");
		td.colspan = 1;
		label_Id.setLayoutData(td);
		td = new TableWrapData();
		Label label_Id_conteudo = toolkit.createLabel(form.getBody(), "PS01");
		td.colspan = 6;
		label_Id_conteudo.setLayoutData(td);
		
		td = new TableWrapData(TableWrapData.RIGHT);
		Label label_Title = toolkit.createLabel(form.getBody(), "Title:");
		td.colspan = 1;
		label_Title.setLayoutData(td);
		td = new TableWrapData();
		Label label_Title_conteudo = toolkit.createLabel(form.getBody(), "Experimentation in Software Engineering");
		td.colspan = 6;
		label_Title_conteudo.setLayoutData(td);
		
		td = new TableWrapData(TableWrapData.RIGHT);
		Label label_Authors = toolkit.createLabel(form.getBody(), "Authors:");
		td.colspan = 1;
		label_Authors.setLayoutData(td);
		td = new TableWrapData();
		Label label_Authors_conteudo = toolkit.createLabel(form.getBody(), "Victor Basili");
		td.colspan = 6;
		label_Authors_conteudo.setLayoutData(td);
		
		td = new TableWrapData(TableWrapData.RIGHT);
		Label label_Institution = toolkit.createLabel(form.getBody(), "Institution(s):");
		td.colspan = 1;
		label_Institution.setLayoutData(td);
		td = new TableWrapData();
		Label label_Institution_conteudo = toolkit.createLabel(form.getBody(), "Universidade Federal de Pernambuco");
		td.colspan = 6;
		label_Institution_conteudo.setLayoutData(td);
		
		td = new TableWrapData(TableWrapData.RIGHT);
		Label label_Country = toolkit.createLabel(form.getBody(), "Country:");
		td.colspan = 1;
		label_Country.setLayoutData(td);
		td = new TableWrapData();
		Label label_Country_conteudo = toolkit.createLabel(form.getBody(), "BRA");
		td.colspan = 6;
		label_Country_conteudo.setLayoutData(td);
		
		td = new TableWrapData(TableWrapData.RIGHT);
		Label label_Link = toolkit.createLabel(form.getBody(), "Link:");
		td.colspan = 1;
		label_Link.setLayoutData(td);
		td = new TableWrapData();
		Label label_Link_conteudo = toolkit.createLabel(form.getBody(), "http://aehbdfi.com");
		td.colspan = 6;
		label_Link_conteudo.setLayoutData(td);
		
		td = new TableWrapData(TableWrapData.RIGHT);
		Label label_Abstract = toolkit.createLabel(form.getBody(), "Abstract:");
		td.colspan = 1;
		label_Abstract.setLayoutData(td);
		td = new TableWrapData();
		Label label_Abstract_conteudo = toolkit.createLabel(form.getBody(), "BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA\n BLA BLA BLA BLA BLA BLA\n BLA BLA BLA BLA BLA BLA");
		td.colspan = 6;
		label_Abstract_conteudo.setLayoutData(td);
		
		td = new TableWrapData(TableWrapData.RIGHT);
		Button Include = toolkit.createButton(form.getBody(), "Include", SWT.PUSH);
		td.colspan = 2;
		Include.setLayoutData(td);
		td = new TableWrapData();
		Button Exclude = toolkit.createButton(form.getBody(), "Exclude", SWT.PUSH);
		td.colspan = 1;
		Exclude.setLayoutData(td);
		td = new TableWrapData();
		Button Skip = toolkit.createButton(form.getBody(), "Skip", SWT.PUSH);
		td.colspan = 4;
		Skip.setLayoutData(td);
		
	}

	@Override
	public void setFocus() {
		form.setFocus();
		
	}
	
	public void dispose(){
		form.dispose();
		super.dispose();
	}

}