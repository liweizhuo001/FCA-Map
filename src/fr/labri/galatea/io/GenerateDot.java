/*
   Copyright 2009 Jean-Rémy Falleri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package fr.labri.galatea.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Entity;

/**
 * Generates the dot code corresponding to a concept lattice.
 * @author Jean-R�my Falleri
 */
public class GenerateDot extends GenerateCode {
	
	private ConceptOrder conceptOrder;
	
	private String newConceptColor = "lightblue";
	
	private String fusionConceptColor = "orange";
	
	private String normalConceptColor = "yellow";
	
	private String conceptNamePrefix = "C";
	
	private boolean displayConceptNumber = false;

	private boolean useSimplifiedIntent = true;

	private boolean useSimplifiedExtent = true;
	
	private boolean displayIntent = true;
	
	private boolean displayExtent = true;

	private boolean useColor = true;
	
	private Map<Concept,Integer> idMap;

	public GenerateDot(ConceptOrder lattice) {
		this.conceptOrder = lattice;
		this.idMap = new HashMap<Concept, Integer>();
		initIdMap();
	}
	
	public String getNewConceptColor() {
		return newConceptColor;
	}

	public void setNewConceptColor(String newConceptColor) {
		this.newConceptColor = newConceptColor;
	}

	public String getFusionConceptColor() {
		return fusionConceptColor;
	}

	public void setFusionConceptColor(String fusionConceptColor) {
		this.fusionConceptColor = fusionConceptColor;
	}

	public String getNormalConceptColor() {
		return normalConceptColor;
	}

	public void setNormalConceptColor(String normalConceptColor) {
		this.normalConceptColor = normalConceptColor;
	}

	public String getConceptNamePrefix() {
		return conceptNamePrefix;
	}

	public void setConceptNamePrefix(String conceptNamePrefix) {
		this.conceptNamePrefix = conceptNamePrefix;
	}

	public boolean isDisplayConceptNumber() {
		return displayConceptNumber;
	}

	public void setDisplayConceptNumber(boolean displayConceptNumber) {
		this.displayConceptNumber = displayConceptNumber;
	}

	public boolean isUseSimplifiedIntent() {
		return useSimplifiedIntent;
	}

	public void setUseSimplifiedIntent(boolean useSimplifiedIntent) {
		this.useSimplifiedIntent = useSimplifiedIntent;
	}

	public boolean isUseSimplifiedExtent() {
		return useSimplifiedExtent;
	}

	public void setUseSimplifiedExtent(boolean useSimplifiedExtent) {
		this.useSimplifiedExtent = useSimplifiedExtent;
	}

	public boolean isDisplayIntent() {
		return displayIntent;
	}

	public void setDisplayIntent(boolean displayIntent) {
		this.displayIntent = displayIntent;
	}

	public boolean isDisplayExtent() {
		return displayExtent;
	}

	public void setDisplayExtent(boolean displayExtent) {
		this.displayExtent = displayExtent;
	}

	public boolean isUseColor() {
		return useColor;
	}

	public void setUseColor(boolean useColor) {
		this.useColor = useColor;
	}

	public static void toFile(ConceptOrder conceptOrder,String path) throws IOException {
		GenerateDot g = new GenerateDot(conceptOrder);
		g.generateCode();
		g.toFile(path);
	}
	
	public void initIdMap() {
		int id = 1;
		for( Concept c: conceptOrder ) {
			idMap.put(c,id);
			id++;
		}
	}

	/**
	 * Generates the dot code corresponding to the concept lattice.
	 */
	public void generateCode() {
		appendHeader();
		generateDot();
		appendFooter();
	}
	
	private void generateDot() {
		for(Concept c: conceptOrder.getConcepts() ) {
			append(idMap.get(c) + " ");
			append("[shape=record,style=filled");
			
			if ( useColor ) {
				if ( c.isNewEntity() )
					append(",fillcolor=" + newConceptColor );
				else if ( c.isEntityFusion() )
					append(",fillcolor=" + fusionConceptColor );
				else
					append(",fillcolor=" + normalConceptColor );
			}
						
			append(",label=\"{");
			
			if ( displayConceptNumber )
				append( conceptNamePrefix + idMap.get(c) + "|");
			
			if ( displayIntent ) {
				if ( useSimplifiedIntent )
					for( Attribute attr: c.getSimplifiedIntent() )
						append( attr.toString() + "\\n" );
				else
					for( Attribute attr: c.getIntent() )
						append( attr.toString() + "\\n" );
			}

			if ( displayExtent ) {
				append("|");
				if ( useSimplifiedExtent ) 
					for(Entity ent: c.getSimplifiedExtent() ) 
						append(ent.getName() + "\\n");
				else
					for(Entity ent: c.getExtent())
						append(ent.getName() + "\\n");
			}

			append("}\"];\n");
		}

		for( Concept c: conceptOrder.getConcepts() )
			for( Concept child: c.getParents() )
				appendLine("\t" + idMap.get(c) + " -> " + idMap.get(child) );
	}


	private void appendHeader() {
		appendLine("digraph G { ");
		appendLine("\trankdir=BT;");
	}

	private void appendFooter() {
		append("}");
	}

}
