package br.ufpe.cin.reviewer.logger;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.internal.workbench.WorkbenchLogger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

@SuppressWarnings("restriction")
public class ReviewerLogger {

	@Inject 	
	private static Logger log;
	
	static {
		Bundle bundle = FrameworkUtil.getBundle(ReviewerLogger.class);
		BundleContext bundleContext = bundle.getBundleContext();
		IEclipseContext eclipseCtx =   
				EclipseContextFactory.getServiceContext(bundleContext);

		// Create instance of class
		log = ContextInjectionFactory.make(WorkbenchLogger.class, eclipseCtx);
	}
	
	public static void error(String message) {
		log.error(message);
	}
	
	public static void warn(String message) {
		log.warn(message);
	}
	
	public static void debug(String message) {
		log.debug(message);
	}
	
	public static void info(String message) {
		log.info(message);
	}
	
}
