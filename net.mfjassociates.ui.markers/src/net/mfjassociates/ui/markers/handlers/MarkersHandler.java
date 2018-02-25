package net.mfjassociates.ui.markers.handlers;

import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class MarkersHandler extends AbstractHandler {

	private static final String IMPORT_COMMAND = "Markers.import.command.name";
	private static final String EXPORT_COMMAND = "Markers.export.command.name";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structured = (IStructuredSelection) service.getSelection();
		if (structured.getFirstElement() instanceof IFile) {
			IFile file = (IFile) structured.getFirstElement();
			IPath path = file.getLocation();
			System.out.println("file="+path.toPortableString());
			ResourceBundle bundle=ResourceBundle.getBundle("net.mfjassociates.ui.markers.plugin");
			try {
				exportMarkers(file, event.getCommand().getCategory().getName(), bundle.getString(EXPORT_COMMAND), bundle.getString(IMPORT_COMMAND));
			} catch (NotDefinedException e) {
			}
		}
		if (structured.getFirstElement() instanceof ICompilationUnit) {
			ICompilationUnit cu = (ICompilationUnit) structured.getFirstElement();
			System.out.println("compilation unit="+cu.getElementName());
		}
		MessageDialog.openInformation(
				window.getShell(),
				"Markers",
				"Hello, Eclipse world");
		return null;
	}

	private void exportMarkers(IFile file, String commandName, String exportCommandName, String importCommandName) {
		IMarker[] bookmarks =null;
		int depth = IResource.DEPTH_INFINITE;
		try {
			bookmarks = file.findMarkers(IMarker.BOOKMARK, true, depth);
			for (IMarker marker : bookmarks) {
				Map<String, Object> attrs = marker.getAttributes();
				Set<String> ks = attrs.keySet();
				for (String key : ks) {
					System.out.println(MessageFormat.format("<{0}>{1}</{)}>", key, attrs.get(key)));
				}
			}
		} catch (CoreException e) {
			
		}
	}
}
