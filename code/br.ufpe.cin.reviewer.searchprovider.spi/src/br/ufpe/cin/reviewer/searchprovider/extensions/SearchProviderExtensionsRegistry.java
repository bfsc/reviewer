package br.ufpe.cin.reviewer.searchprovider.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class SearchProviderExtensionsRegistry {

	public static List<IConfigurationElement> getConfigElements() {
		List<IConfigurationElement> toReturn = new ArrayList<IConfigurationElement>();
		
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement[] configArray = extensionRegistry.getConfigurationElementsFor("br.ufpe.cin.reviewer.searchprovider");
		
		for (IConfigurationElement config : configArray) {
			toReturn.add(config);
		}
		
		return toReturn;
	}
	
}
