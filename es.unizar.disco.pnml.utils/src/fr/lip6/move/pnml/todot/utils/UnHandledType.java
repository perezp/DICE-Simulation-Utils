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

package fr.lip6.move.pnml.todot.utils;

/**
 * 
 * @author Guillaume Giffo
 * 
 */
public class UnHandledType extends Exception {
	/**
	 * Serialization stuff.
	 */
	private static final long serialVersionUID = -544239706092600793L;

	/**
	 * Default constructor.
	 */
	public UnHandledType() {
		super();
	}

	/**
	 * Constructor with message.
	 * 
	 * @param mssg
	 *            the message.
	 */
	public UnHandledType(String mssg) {
		super(mssg);
	}

	/**
	 * Constructor with message and cause of the exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public UnHandledType(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with the cause of the exception.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public UnHandledType(Throwable cause) {
		super(cause);
	}
}
