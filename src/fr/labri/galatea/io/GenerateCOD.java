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

public class GenerateCOD extends GenerateCode {

	private ConceptOrder conceptOrder;
	
	private Map<Concept,Integer> idMap;
	
	public GenerateCOD(ConceptOrder conceptOrder) {
		this.conceptOrder = conceptOrder;
		this.idMap = new HashMap<Concept, Integer>();
		initIdMap();
	}
	
	public static void toFile(ConceptOrder conceptOrder,String path) {
		GenerateCOD cod = new  GenerateCOD(conceptOrder);
		cod.generateCode();
		try {
			cod.toFile(path);
		} catch (IOException e) {}
	}
	
	public void initIdMap() {
		int id = 1;
		for(Concept c: conceptOrder ) {
			idMap.put(c,id);
			id++;
		}
	}
	
	@Override
	public void generateCode() {
		appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		appendLine("<concept_order>");
		for( Concept concept: conceptOrder ) {
			appendLine("<concept id=\"" + idMap.get(concept) + "\">");			
			appendLine("<parents>");
			for(Concept parent: concept.getParents() )
				appendLine("<parent id=\"" + idMap.get(parent) + "\"/>");
			appendLine("</parents>");
			appendLine("<extent>");
			for(Entity e: concept.getExtent())
				appendLine("<entity desc=\"" + e.toString() + "\"/>");
			appendLine("</extent>");
			appendLine("<intent>");
			for(Attribute a: concept.getIntent())
				appendLine("<attribute desc=\"" + a.toString() + "\"/>");
			appendLine("</intent>");
			appendLine("</concept>");
		}
		appendLine("</concept_order>");
	}

}
