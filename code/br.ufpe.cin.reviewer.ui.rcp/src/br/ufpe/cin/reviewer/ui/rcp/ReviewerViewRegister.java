package br.ufpe.cin.reviewer.ui.rcp;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.part.ViewPart;

public class ReviewerViewRegister {

	private static Map<String, ViewPart> views = new HashMap<String, ViewPart>();
	
	public static void putView(String id, ViewPart view) {
		views.put(id, view);
	}
	
	public static ViewPart getView(String id) {
		return views.get(id);
	}
	
}
