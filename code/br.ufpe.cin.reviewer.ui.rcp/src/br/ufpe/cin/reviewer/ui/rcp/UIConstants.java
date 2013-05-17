package br.ufpe.cin.reviewer.ui.rcp;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class UIConstants {

	public static final Display APP_DISPLAY = PlatformUI.getWorkbench().getDisplay();
	
	public static final String SYSTEM_FONT_NAME = APP_DISPLAY.getSystemFont().getFontData()[0].getName();
	
	public static final String SYSTEM_LINE_BREAK = System.getProperty("line.separator");
	
	
}
