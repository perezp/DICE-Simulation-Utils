package es.unizar.disco.pnml.utils.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.acceleo.common.preference.AcceleoPreferences;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import es.unizar.disco.pnml.utils.PnmlUtilsPlugin;
import es.unizar.disco.pnml.utils.pnml2dot.Pnml2dot;

public class ConvertToDot extends AbstractHandler {

	public static final String ID = "es.unizar.disco.pnml.utils.popup.handlers.ConvertToPnml";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Job job = new Job(ID) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					if (monitor == null)
						monitor = new NullProgressMonitor();
					MultiStatus status = new MultiStatus(PnmlUtilsPlugin.PLUGIN_ID, 0, null, null);
					try {
						monitor.beginTask("Converting to DOT", IProgressMonitor.UNKNOWN);
						for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {
							// TODO: Handle selection on multiple files for the
							// return status (i.e. use MultiStatus when needed)
							Object obj = iterator.next();
							if (obj instanceof IFile) {
								IFile file = (IFile) obj;
								try {
									// @formatter:off
									Pnml2dot generator = new Pnml2dot(
											URI.createFileURI(file.getLocation().toString()),
											file.getLocation().removeLastSegments(1).toFile(),
											Arrays.asList(new Object[] { file.getFullPath().addFileExtension("dot").lastSegment().toString() } ));
									// @formatter:on
									AcceleoPreferences.switchForceDeactivationNotifications(true);
									generator.doGenerate(new BasicMonitor());
									file.getParent().refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 1));
								} catch (IOException | CoreException e) {
									status.add(new Status(IStatus.ERROR, PnmlUtilsPlugin.PLUGIN_ID, e.getLocalizedMessage(), e));
								}
							}
						}
					} finally {
						monitor.done();
					}
					return status;
				}
			};
			job.schedule();
		}
		return null;
	}
}
