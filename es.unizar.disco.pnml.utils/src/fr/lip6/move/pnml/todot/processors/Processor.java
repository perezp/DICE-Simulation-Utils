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

/**
 * 
 */
package fr.lip6.move.pnml.todot.processors;

import java.io.PrintWriter;
import java.util.HashMap;

import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;

/**
 * Every Petri net type processor must implement this class.
 * @author ggiffo
 */
public abstract class Processor {
	/**
	 * Writer.
	 */
	protected PrintWriter fwloc;
	/**
	 * For Indentation.
	 */
	protected String decalage = "";
	/**
	 * Number of indentation spaces.
	 */
	protected int decalagevalue;
	/**
	 * Stores the desired values for place and transition dimensions.
	 */
	protected HashMap<String, String> dotNodesSize = new HashMap<String, String>();
	{
	}

	/**
	 * Each Petri net type processor must implement this method.
	 * @param rcl the Petri net document top-level class.
	 * @param fr a writer
	 */
	public abstract void process(HLAPIRootClass rcl, PrintWriter fr);

	/**
	 * Increment indentation by 1.
	 */
	protected final void incrementDecalage() {
		setdecalage(decalagevalue + 1);

	}

	/**
	 * Decrement indentation by 1.
	 */
	protected final void decrementdecalage() {
		setdecalage(decalagevalue - 1);
	}

	/**
	 * Sets the indentation by value.
	 * @param decal the indentation value.
	 */
	private void setdecalage(int decal) {
		decalagevalue = decal;
		decalage = "";
		for (int i = 0; i < decalagevalue; i++) {
			decalage += "    ";
		}
	}

	/**
	 * Print some string.
	 * @param s the string to print.
	 */
	protected final void print(String s) {
		fwloc.println(decalage + s);
	}

}
