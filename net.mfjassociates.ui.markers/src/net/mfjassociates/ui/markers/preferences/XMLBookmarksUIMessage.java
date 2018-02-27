package net.mfjassociates.ui.markers.preferences;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class XMLBookmarksUIMessage extends NLS {
	private static final String BUNDLE_NAME = "net.mfjassociates.ui.markers.preferences.XMLBookmarksUIPluginResources";//$NON-NLS-1$
	private static ResourceBundle fResourceBundle;

	public static String create_bookmarks_file_label;
	public static String preference_title;
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, XMLBookmarksUIMessage.class);
	}

	private XMLBookmarksUIMessage() {
		// cannot create new instance
	}

	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null) {
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
			}
		}
		catch (MissingResourceException x) {
			fResourceBundle = null;
		}
		return fResourceBundle;
	}
}
