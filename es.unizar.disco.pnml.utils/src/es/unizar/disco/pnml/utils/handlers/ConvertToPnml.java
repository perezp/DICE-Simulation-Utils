package es.unizar.disco.pnml.utils.handlers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import es.unizar.disco.pnml.utils.PnmlUtilsPlugin;
import fr.lip6.move.pnml.framework.utils.PNMLUtils;
import fr.lip6.move.pnml.framework.utils.exception.BadFileFormatException;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.InvocationFailedException;
import fr.lip6.move.pnml.framework.utils.exception.OCLValidationFailed;
import fr.lip6.move.pnml.framework.utils.exception.OtherException;
import fr.lip6.move.pnml.framework.utils.exception.UnhandledNetType;
import fr.lip6.move.pnml.framework.utils.exception.ValidationFailedException;

public class ConvertToPnml extends AbstractHandler {

	public static final String ID = "es.unizar.disco.pnml.utils.popup.handlers.ConvertToPnml";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Job job = new Job(ID) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					if (monitor == null) monitor = new NullProgressMonitor();
					try {
						monitor.beginTask("Transforming to PNML", IProgressMonitor.UNKNOWN);
						for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {
							// TODO: Handle selection on multiple files for the return status (i.e. use MultiStatus when needed)
							Object obj = iterator.next();
							if (obj instanceof IFile) {
								IFile file = (IFile) obj;
								Resource resource = new XMIResourceImpl(URI.createPlatformResourceURI(file.getFullPath().toString(), true));
								java.net.URI uri = file.getLocationURI();
								try {
									resource.load(Collections.emptyMap());
									if (!resource.getContents().isEmpty()) {
										if (resource.getContents().size() == 1) {
											EObject rootEObject = resource.getContents().get(0);
											PNMLUtils.exportPetriNetDocToPNML(rootEObject,
													new Path(URIUtil.toFile(uri).getAbsolutePath()).removeFileExtension().addFileExtension("pnml").toOSString());
										} else {
											return new Status(IStatus.ERROR, PnmlUtilsPlugin.PLUGIN_ID, 
													MessageFormat.format(
															"Unexpected file format for a PNML XMI file: resource ''{0}'' has more than one root EObject",
															resource.getURI().toString()));
										}
									}
								} catch (IOException | UnhandledNetType | OCLValidationFailed | ValidationFailedException
										| BadFileFormatException | OtherException | InvalidIDException
										| InvocationFailedException e) {
									return new Status(IStatus.ERROR, PnmlUtilsPlugin.PLUGIN_ID, e.getLocalizedMessage(), e);
								} finally {
									if (resource != null) {
										resource.unload();
									}
									// Refresh workspace
									IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(uri);
									for (IFile locatedFile : files) {
										try {
											if (locatedFile.getParent() != null) { 
												locatedFile.getParent().refreshLocal(IResource.DEPTH_ONE, new SubProgressMonitor(monitor, 0));
											}
										} catch (CoreException e) {
											// Do nothing: refresh failed 
										}
									}
								}
							}
						}
					} finally {
						monitor.done();
					}
					return Status.OK_STATUS;
				}
			};
			job.schedule();
			
		}
		return null;
	}
}
