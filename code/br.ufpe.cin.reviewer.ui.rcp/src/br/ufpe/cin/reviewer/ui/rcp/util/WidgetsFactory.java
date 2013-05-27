package br.ufpe.cin.reviewer.ui.rcp.util;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;

public class WidgetsFactory {

	public static StyledText createStyledText(Composite parent, int style) {
		StyledText styledText = new StyledText(parent, style);
		
		styledText.addFocusListener(new StyledTextLostSelectionFocusListenner());
		
		return styledText;
	}
	
	private static class StyledTextLostSelectionFocusListenner implements FocusListener {

		public void focusGained(FocusEvent e) {
			
		}

		public void focusLost(FocusEvent e) {
			if (e.getSource() instanceof StyledText) {
				StyledText sourceWidget = (StyledText) e.getSource();
				sourceWidget.setSelection(0);
			}
		}
		
	}
	
}
