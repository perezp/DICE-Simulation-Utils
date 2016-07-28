package es.unizar.disco.pnml.utils.testers;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import fr.lip6.move.pnml.framework.general.OfficialPNMLFileType;
import fr.lip6.move.pnml.framework.general.PNMLFileType;

public class PnmlXmiPropertyTester extends PropertyTester {

	public PnmlXmiPropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		boolean isPnmlXmi = false;
		if (receiver instanceof IFile) {
			IFile file = (IFile) receiver;
			Resource resource = new XMIResourceImpl(URI.createPlatformResourceURI(file.getFullPath().toString(), true));
			try {
				resource.load(Collections.emptyMap());
				if (!resource.getContents().isEmpty()) {
					EObject firstRootEObject = resource.getContents().get(0);
					final String classname = firstRootEObject.eClass().getInstanceTypeName();
					PNMLFileType type = OfficialPNMLFileType.getByNativeClassName(classname);
					isPnmlXmi = (type != null);
				}
			} catch (IOException e) {
				isPnmlXmi = false;
			} finally {
				if (resource != null) {
					resource.unload();
				}
			}
		}
		return isPnmlXmi;
	}

}
