/**
 *  Copyright 2009 University Pierre & Marie Curie - UMR CNRS 7606 (LIP6/MoVe)
 *  All rights reserved.   This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Initial contributor:
 *    Lom Messan Hillah - <lom-messan.hillah@lip6.fr>
 *
 *  Mailing list:
 *    pnmlframework@lip6.fr
 */
/**
 *  (C) 2009 University Pierre & Marie Curie - UMR CNRS 7606 (LIP6/MoVe)                                              
 *  All rights reserved.   This program and the accompanying materials                                           
 *  are made available under the terms of the Eclipse Public License v1.0                                        
 *  which accompanies this distribution, and is available at                                                     
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Initial contributor:
 *    Lom Messan Hillah - <lom-messan.hillah@lip6.fr>
 *  
 *  Mailing list:
 *    pnmlframework@lip6.fr
 */

package fr.lip6.move.pnml.todot.processors;

import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;
import fr.lip6.move.pnml.todot.utils.UnHandledType;

/**
 * Dispatches the transformation to the right PNML type processor.
 * 
 * @author lom
 * 
 */
public final class MainProcessor {
	/**
	 * Hidden constructor.
	 */
	private MainProcessor() {
		super();
	}

	/**
	 * Determines which PNML type processor to call to perform the actual
	 * transformation.
	 * 
	 * @param theclass
	 *            HLAPI root class
	 * @return the PNML type processor which is going to perform the actual
	 *         transformation.
	 * @throws UnHandledType
	 *             Unhandled net type.
	 */
	public static Processor getProcessor(HLAPIRootClass theclass)
			throws UnHandledType {
		Processor p = null;
		if (theclass.getClass().equals(
				fr.lip6.move.pnml.pnmlcoremodel.hlapi.PetriNetDocHLAPI.class)) {
			p = new CoreProcessor();
		}

		if (theclass
				.getClass()
				.equals(
						fr.lip6.move.pnml.symmetricnet.hlcorestructure.hlapi.PetriNetDocHLAPI.class)) {
			p = new SNProcessor();
		}

		if (theclass.getClass().equals(
				fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI.class)) {
			p = new PTProcessor();
		}

		if (p == null) {
			throw new UnHandledType(
					"this PNML type is not supported by this version",
					new Throwable(
							"this PNML type is not supported by this version"));
		}
		// TODO : support HLPN and PT-HLPN.
		return p;
	}
}
