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

import java.io.PrintWriter;

import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NodeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PTMarkingHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PnObjectHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.RefPlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.RefTransitionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.ToolInfoHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;

/**
 * Place/Transition Net processor.
 * @author lom
 */
public class PTProcessor extends Processor {
	/**
	 * Generated label name.
	 */
	public static final String LAB_NAME = "GeneratedLabel";

	/**
	 * Processor of P/T nets.
	 */
	public PTProcessor() {
		super();
	}

	/**
	 * Processes top-level class in the Petri net document. This top-level class
	 * should be a PetriNetDoc(HLAPI).
	 * @param rcl the Petri net document root class
	 * @param fr the writer (the destination is a file).
	 */
	@Override
	public final void process(HLAPIRootClass rcl, PrintWriter fr) {
		fwloc = fr;
		final PetriNetDocHLAPI root = (PetriNetDocHLAPI) rcl;
		System.out.println(root.getNets().size());
		for (PetriNetHLAPI iterableElement : root.getNetsHLAPI()) {
			processNets(iterableElement);
		}
		fr.close();
	}

	/**
	 * Processes a Petri net model.
	 * @param ptn the top-level class of a Petri net model.
	 */
	private void processNets(PetriNetHLAPI ptn) {
		print("strict digraph \"" + ptn.getId() + "\" {");
		print("graph [pad=\".75\", ranksep=\"0.25\", nodesep=\"1\"];");
		print("overlap=scale;");
		print("splines=true;");
		print("node[fixedsize=true];");
		print("edge[];");

		incrementDecalage();
		for (PageHLAPI iterableElement : ptn.getPagesHLAPI()) {
			processPages(iterableElement);
		}
		decrementdecalage();
		print("}");
	}

	/**
	 * Processes a Page. Labels are stored as comment to improve the resulting
	 * graph presentation.
	 * @param pth the Page to process
	 */
	private void processPages(PageHLAPI pth) {
		print("subgraph \"cluster" + pth.getId() + "\" {");
		incrementDecalage();
		print("ordering=out;");
		print("comment=\"" + workOnName(pth) + "\";");
		print("color=none;");
		for (PageHLAPI iterableElement : pth.getObjects_PageHLAPI()) {
			processPages(iterableElement);
		}
		for (PlaceHLAPI iterableElement : pth.getObjects_PlaceHLAPI()) {
			processPlace(iterableElement);
		}
		for (TransitionHLAPI iterableElement : pth.getObjects_TransitionHLAPI()) {
			processTransition(iterableElement);
		}
		for (RefPlaceHLAPI iterableElement : pth.getObjects_RefPlaceHLAPI()) {
			processRefPlace(iterableElement);
		}
		for (RefTransitionHLAPI iterableElement : pth.getObjects_RefTransitionHLAPI()) {
			processRefTransition(iterableElement);
		}
		for (ArcHLAPI iterableElement : pth.getObjects_ArcHLAPI()) {
			processArc(iterableElement);
		}
		decrementdecalage();
		print("}");
	}

	/**
	 * Processes a Node.
	 * @param nhp the node to process
	 * @param shape the shape it should be given in dot
	 * @param sb a buffer containing the output dot graph
	 */
	private void processNode(NodeHLAPI nhp, String shape, StringBuffer sb) {
		sb.append("\"" + nhp.getId() + "\" [");
		if (nhp.getNodegraphicsHLAPI() != null && nhp.getNodegraphics().getPosition() != null) {
			sb.append("pos=\"" + nhp.getNodegraphics().getPosition().getX() + "," + nhp.getNodegraphics().getPosition().getY() + "!\",");
		}
		sb.append("shape=" + shape + ",");
		if (dotNodesSize.get(shape) != null) {
			sb.append(dotNodesSize.get(shape) + ",");
		}
		// Names are stored in comments.
		sb.append("comment=\"" + workOnName(nhp));
		sb.append("\"];\n");
	}

	/**
	 * Processes a Place.
	 * @param pla the place to process.
	 */
	private void processPlace(PlaceHLAPI pla) {
		final StringBuffer sb = new StringBuffer();
		processNode(pla, "circle", sb);
		print(sb.toString());
	}

	/**
	 * Processes a Transition.
	 * @param tra the transition to process
	 */
	private void processTransition(TransitionHLAPI tra) {
		final StringBuffer sb = new StringBuffer();
		processNode(tra, "box", sb);
		print(sb.toString());
	}

	/**
	 * Processes a Reference Transition.
	 * @param tra the ref transition to process.
	 */
	private void processRefTransition(RefTransitionHLAPI tra) {
		final StringBuffer sb = new StringBuffer();
		processNode(tra, "box", sb);
		print(sb.toString());
		print(tra.getId() + " -> " + tra.getRef().getId() + "[style=dotted];");
	}

	/**
	 * Processes a Reference Place.
	 * @param pla the ref place to process.
	 */
	private void processRefPlace(RefPlaceHLAPI pla) {
		final StringBuffer sb = new StringBuffer();
		processNode(pla, "circle", sb);
		print(sb.toString());
		print(pla.getId() + " -> " + pla.getRef().getId() + "[style=dotted];");
	}

	/**
	 * Processes an Arc.
	 * @param arc the arc to process.
	 */
	private void processArc(ArcHLAPI arc) {
		final StringBuffer sb = new StringBuffer();
		sb.append("\"" + arc.getSource().getId() + "\" -> \"" + arc.getTarget().getId() + "\"");
		sb.append("[");
		sb.append("label=\"" + extractInscription(arc) + "\"");
		sb.append("]");
		sb.append(";");
		print(sb.toString());
	}

	/**
	 * Returns the name of an Petri net object.
	 * @param pnobj the Petri net object which name is returned.
	 * @return the name of the Petri net object
	 */
	private String workOnName(PnObjectHLAPI pnobj) {
		String retour;
		if (pnobj.getNameHLAPI() == null || pnobj.getNameHLAPI().getText() == null || pnobj.getNameHLAPI().getText().equals("")) {
			retour = pnobj.getId();
		} else {
			retour = pnobj.getNameHLAPI().getText();
		}
		return retour;
	}

	/**
	 * Extracts a place marking.
	 * @param pla the place
	 * @return its marking
	 */
	private String extractMarking(PlaceHLAPI pla) {
		String marking = null;
		if (pla.getInitialMarkingHLAPI() != null) {
			PTMarkingHLAPI initMark = pla.getInitialMarkingHLAPI();
			final Integer intMark = initMark.getText();
			marking = intMark != null && intMark > 0 ? intMark.toString() : "";
			/*
			 * Experimental code: if (initMark.getToolspecificsHLAPI() != null)
			 * { List<ToolInfoHLAPI> tses = initMark.getToolspecificsHLAPI();
			 * for (ToolInfoHLAPI ts : tses) { marking += "\n" +
			 * extractToolSpecificTool(ts) + extractToolSpecificVersion(ts);
			 * System.out.println(extractToolSpecificXMLBuffer(ts)); } }
			 */
		} else {
			marking = "";
		}
		return marking;
	}

	/**
	 * Extracts Tool Specific tool attribute value.
	 * @param ts the tool specific
	 * @return its tool attribute value
	 */
	private String extractToolSpecificTool(ToolInfoHLAPI ts) {
		return ts.getTool();
	}

	/**
	 * Extracts Tool Specific version attribute value.
	 * @param ts the tool specific
	 * @return its tool attribute value
	 */
	private String extractToolSpecificVersion(ToolInfoHLAPI ts) {
		return ts.getVersion();
	}

	/**
	 * Extracts Tool Specific XML buffer contents
	 * @param ts the tool specific object
	 * @return its XML contents as a String. At this point, we don't know what
	 *         it is. It up to the developer to parse it, or display it, etc.
	 */
	private String extractToolSpecificXMLBuffer(ToolInfoHLAPI ts) {
		StringBuffer xmlBuffer = ts.getFormattedXMLBuffer();
		return xmlBuffer != null ? xmlBuffer.toString() : "";
	}

	/**
	 * Extracts an arc inscription.
	 * @param arc the arc
	 * @return its inscription
	 */
	private String extractInscription(ArcHLAPI arc) {
		String inscription = null;
		if (arc.getInscriptionHLAPI() != null) {
			final Integer intInsc = arc.getInscriptionHLAPI().getText();
			inscription = intInsc != null && intInsc > 1 ? intInsc.toString() : "";
		} else {
			inscription = "";
		}
		return inscription;
	}

}
