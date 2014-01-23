package br.ufpe.cin.reviewer.ui.rcp;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import br.ufpe.cin.reviewer.persistence.JPAEntityManager;
import br.ufpe.cin.reviewer.persistence.util.HSQLUtil;
import br.ufpe.cin.reviewer.ui.rcp.common.UIConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "br.ufpe.cin.reviewer.ui.rcp"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		//HSQLUtil.initDatabase();
		//JPAEntityManager.ENTITY_MANAGER.isOpen();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	protected void initializeImageRegistry(ImageRegistry reg) {
//		super.initializeImageRegistry(reg);
		
        Bundle bundle = Platform.getBundle(PLUGIN_ID);

        ImageDescriptor toolbarSearchImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/toolbar.search.png"), null));
        reg.put(UIConstants.TOOLBAR_SEARCH_ACTION_IMAGE_ID, toolbarSearchImage);
        
        ImageDescriptor toolbarLiteratureReviewImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("images/toobar.literaturereview.png"), null));
        reg.put(UIConstants.TOOLBAR_LITERATURE_REVIEW_ACTION_IMAGE_ID, toolbarLiteratureReviewImage);
	}
}
