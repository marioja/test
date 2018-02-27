package net.mfjassociates.ui.markers.builder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import net.mfjassociates.ui.markers.Activator;
import net.mfjassociates.ui.markers.preferences.PreferenceConstants;

public class XMLBookmarksBuilder extends IncrementalProjectBuilder {

	class XMLBookmarksDeltaVisitor implements IResourceDeltaVisitor {
		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				checkXML(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				checkXML(resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class XMLBookmarksResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			checkXML(resource);
			//return true to continue visiting children.
			return true;
		}
	}

	class XMLErrorHandler extends DefaultHandler {
		
		private IFile file;

		public XMLErrorHandler(IFile file) {
			this.file = file;
		}

		private void addMarker(SAXParseException e, int severity) {
			XMLBookmarksBuilder.this.addMarker(file, e.getMessage(), e
					.getLineNumber(), severity);
		}

		public void error(SAXParseException exception) throws SAXException {
			addMarker(exception, IMarker.SEVERITY_ERROR);
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			addMarker(exception, IMarker.SEVERITY_ERROR);
		}

		public void warning(SAXParseException exception) throws SAXException {
			addMarker(exception, IMarker.SEVERITY_WARNING);
		}
	}

	public static final String BUILDER_ID = "net.mfjassociates.ui.markers.xmlBookmarksBuilder";

	private static final String MARKER_TYPE = "net.mfjassociates.ui.markers.xmlProblem";

	private static final String XML_FILE_TYPE = ".xml";

	private static final String BOOKMAKRS_XML_FILE_TYPE = ".bookmarks.xml";

	private SAXParserFactory parserFactory;
	
	private static DocumentBuilder docBuilder=null;
	static {
		try {
			docBuilder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}

	}

	private void addMarker(IFile file, String message, int lineNumber,
			int severity) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
		}
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	protected void clean(IProgressMonitor monitor) throws CoreException {
		// delete markers set and files created
		getProject().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	void checkXML(IResource resource) {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			if (file.getName().endsWith(XML_FILE_TYPE) && !file.getName().endsWith(BOOKMAKRS_XML_FILE_TYPE)) {
				IFile bookmarksfile=bookmarksFile(file);
				long xmltime=file.getLocalTimeStamp();
				long bookmarkstime=bookmarksfile.getLocalTimeStamp();
				if (xmltime>bookmarkstime || bookmarksfile.getModificationStamp()==IResource.NULL_STAMP) { // xml file was changed, rewrite the bookmarks
					System.out.println("XML file updated, rewriting bookmarks file");
					exportMarkers(file, bookmarksfile, xmltime-1);
				}
				
			}
			if (file.getName().endsWith(BOOKMAKRS_XML_FILE_TYPE)) {
				IFile xmlfile=xmlFile(file);
				long xmltime=xmlfile.getLocalTimeStamp();
				long bookmarkstime=file.getLocalTimeStamp();
				if (bookmarkstime>xmltime && !(file.getModificationStamp()==IResource.NULL_STAMP)) { // bookmarks xml file was changed, update bookmarks
					deleteMarkers(xmlfile, IMarker.BOOKMARK);
					try {
						Document doc = docBuilder.parse(file.getContents());
						NodeList markers = doc.getElementsByTagName("marker");
						Bookmark bookmark=new Bookmark(xmlfile);
						IntStream.range(0, markers.getLength()).mapToObj(markers::item).map(bookmark::createMarker).count();
						
					} catch (SAXException | IOException | CoreException e) {
						e.printStackTrace();
					}
					System.out.println("Bookmarks file updated, updating bookmarks");
				}
			}
		}
		/*if (resource instanceof IFile && resource.getName().endsWith(".xml") && !resource.getName().endsWith("bookmarks.xml")) {
			IFile file = (IFile) resource;
			exportMarkers(file);
			deleteMarkers(file);
			XMLErrorHandler reporter = new XMLErrorHandler(file);
			try {
				getParser().parse(file.getContents(), reporter);
			} catch (Exception e1) {
			}
		}*/
	}
	class Bookmark {
		private IFile file;
		public Bookmark(IFile aFile) {
			this.file = aFile;
		}
		private IMarker createMarker(Node node) {
			IMarker retmarker=null;
			try {
				final IMarker marker = file.createMarker(IMarker.BOOKMARK);
				retmarker = marker;
				NodeList attributes=node.getChildNodes();
				IntStream.range(0,  attributes.getLength()).mapToObj(attributes::item).forEach(attribute -> fillMarker(marker, attribute));
			} catch (CoreException e) {
			}
			return retmarker;
		}
		private void fillMarker(final IMarker marker, Node attribute) {
			try {
				if (attribute.getNodeType()==Node.ELEMENT_NODE) {
					Object value=null;
					String attributeName=attribute.getNodeName();
					if (!"message".equals(attributeName)) {
						value=new Integer(attribute.getTextContent());
					} else {
						try {
							value=attribute.getTextContent();
						} catch (NumberFormatException e) {
						}
					}
					if (value!=null) marker.setAttribute(attributeName, value);
				}
			} catch (DOMException | CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private IFile xmlFile(IFile file) {
		String fn=file.getName();
		int l=fn.length();
		String fileName=fn.substring(0, l-BOOKMAKRS_XML_FILE_TYPE.length())+XML_FILE_TYPE;
		return file.getParent().getFile(new Path("/"+fileName));
	}

	private IFile bookmarksFile(IFile file) {
		String fn=file.getName();
		int l=fn.length();
		String ft="."+file.getFileExtension();
		String bookmarksFileName=fn.substring(0, l-ft.length())+BOOKMAKRS_XML_FILE_TYPE;
		return file.getParent().getFile(new Path("/"+bookmarksFileName));
	}

	private void deleteMarkers(IFile file, String markerType) {
		try {
			file.deleteMarkers(markerType, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new XMLBookmarksResourceVisitor());
		} catch (CoreException e) {
		}
	}

	private SAXParser getParser() throws ParserConfigurationException,
			SAXException {
		if (parserFactory == null) {
			parserFactory = SAXParserFactory.newInstance();
		}
		return parserFactory.newSAXParser();
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new XMLBookmarksDeltaVisitor());
	}
	private void exportMarkers(IFile file, IFile bookmarksfile, long previousStamp) {
		IMarker[] bookmarks =null;
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		PrintStream bookmarksfileps=new PrintStream(baos);
		int depth = IResource.DEPTH_INFINITE;
		try {
			bookmarksfileps.println("<markers>");
			bookmarks = file.findMarkers(IMarker.BOOKMARK, true, depth);
			for (IMarker marker : bookmarks) {
				bookmarksfileps.println("  <marker>");
				Map<String, Object> attrs = marker.getAttributes();
				Set<String> ks = attrs.keySet();
				for (String key : ks) {
					bookmarksfileps.println(MessageFormat.format("    <{0}>{1}</{0}>", key, attrs.get(key)));
				}
				bookmarksfileps.println("  </marker>");
			}
			bookmarksfileps.println("</markers>");
			bookmarksfileps.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			boolean createBookmarksFile=Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_CREATE_BOOKMARKS_FILE);
			if (bookmarksfile.exists()) bookmarksfile.setContents(bais, true, false, null);
			else {
				if (createBookmarksFile) bookmarksfile.create(bais, true, null);
			}
		} catch (CoreException e) {
			// ignore
		} finally {
			try {
				bookmarksfile.setLocalTimeStamp(previousStamp);
			} catch (CoreException e) {
			}
		}
	}
}
