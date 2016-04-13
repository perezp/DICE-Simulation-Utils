package es.unizar.disco.pnml.utils.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import es.unizar.disco.pnml.utils.PnmlUtilsPlugin;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import fr.lip6.move.pnml.framework.utils.PNMLUtils;
import fr.lip6.move.pnml.framework.utils.exception.ImportException;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;

public class ValidatePnml extends AbstractHandler {

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
						monitor.beginTask("Validating PNML", IProgressMonitor.UNKNOWN);
						for (Iterator<?> iterator = structuredSelection.iterator(); iterator.hasNext();) {
							// TODO: Handle selection on multiple files for the return status (i.e. use MultiStatus when needed)
							Object obj = iterator.next();
							if (obj instanceof IFile) {
								IFile file = (IFile) obj;
								try {
									PNMLUtils.importPnmlDocument(file.getLocation().toFile(), false);
									ModelRepository.getInstance().destroyCurrentWorkspace();
								} catch (InvalidIDException | ImportException | VoidRepositoryException e) {
									return new Status(IStatus.ERROR, PnmlUtilsPlugin.PLUGIN_ID, e.getLocalizedMessage(), e);
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
