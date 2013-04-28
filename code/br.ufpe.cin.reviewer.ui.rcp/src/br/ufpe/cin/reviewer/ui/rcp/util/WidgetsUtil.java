package br.ufpe.cin.reviewer.ui.rcp.util;

import org.eclipse.swt.widgets.Composite;

public class WidgetsUtil {
	
	public static void refreshComposite(Composite composite) { 
		for (Composite parent = composite.getParent(); parent != null; parent = parent.getParent()) {
			parent.layout();
		}
	}
}
