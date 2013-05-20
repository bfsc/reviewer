package br.ufpe.cin.reviewer.ui.rcp.common;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class UIConstants {

	public static final Display APP_DISPLAY = PlatformUI.getWorkbench().getDisplay();
	
	public static final String SYSTEM_FONT_NAME = APP_DISPLAY.getSystemFont().getFontData()[0].getName();
	
	public static final int SYSTEM_FONT_HEIGHT = APP_DISPLAY.getSystemFont().getFontData()[0].getHeight();
	
	public static final String SYSTEM_LINE_BREAK = System.getProperty("line.separator");
	
	public static final String TOOLBAR_SEARCH_ACTION_IMAGE_ID = "br.ufpe.cin.reviewer.ui.rcp.toolbar.search.action.image";
	
	public static final String TOOLBAR_LITERATURE_REVIEW_ACTION_IMAGE_ID = "br.ufpe.cin.reviewer.ui.rcp.toolbar.literaturereview.action.image";
	
	
}
