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

package fr.labri.galatea.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class Ceres extends Algorithm {

	private ConceptOrder gsh;

	private Map<Concept, Set<Entity>> simplifies;
	
	private Concept top;

	public Ceres(Context context) {
		super(context);
	}
	
	public void init() {
		this.gsh = new ConceptOrder();
		this.simplifies = new HashMap<Concept, Set<Entity>>();
	}

	@Override
	public void compute() {
		init();

		top = new Concept();
		top.getExtent().addAll(context.getEntities());

		List<Attribute> attributes = new ArrayList<Attribute>(context.getAttributes());
		Collections.sort(attributes,new AttributeComparator(context) );
		
		Set<Concept> PLN;
		Set<Entity> SOO = new HashSet<Entity>();
		Set<Attribute> SOP = new HashSet<Attribute>();

		int index = 0;

		for (int i = context.getEntities().size(); i > -1; i--) {

			PLN = new HashSet<Concept>();

			for (int j = index; j < context.getAttributes().size(); j++) {

				Attribute attribute = attributes.get(j);

				if ( context.getEntities(attribute).size() != i)
					break;

				boolean found = false;
				
				for(Concept c: PLN) {
					if (c.getExtent().equals(context.getEntities(attribute)) ) {
						c.getIntent().add(attribute);
						found = true;
						break;
					}
				}
				if ( !found ) {
					Concept concept = new Concept();
					concept.getIntent().add(attribute);
					concept.getExtent().addAll(context.getEntities(attribute));
					PLN.add(concept);
				}
				index++;
			}
			for (Concept concept : PLN) {
				classify(concept, true);
				SOP.addAll(concept.getSimplifiedIntent());
				simplifies.put(concept, new HashSet<Entity>());
				for (Entity ent : concept.getExtent()) {
					if ( context.getAttributes(ent).size() == concept.getIntent().size()) {
						simplifies.get(concept).add(ent);
						SOO.add(ent);
					}
				}
				workOnLeftPart(concept, SOP, SOO);
			}
		}
		
		if ( top.getSimplifiedExtent().size() == 0 && top.getSimplifiedIntent().size() == 0) {
			for( Concept c: gsh.getConcepts() ) {
				c.getChildren().remove(top);
				c.getParents().remove(top);
			}
			gsh.getConcepts().remove(top);
		}
	
	}

	private void classify(Concept concept,boolean addIntent) {
		Map<Concept,Integer> levels = new HashMap<Concept, Integer>();

		for( Concept c: gsh.getConcepts() )
			levels.put(c,-1);

		List<Concept> q = new LinkedList<Concept>();		
		Set<Concept> dsc = new HashSet<Concept>();
		
		q.add(top);
		Concept csc = null;
		
		while (!q.isEmpty()) {
			csc = q.remove(0);
			dsc.add(csc);
			dsc.removeAll(csc.getParents());

			if (addIntent)
				concept.getIntent().addAll(csc.getSimplifiedIntent());

			for (Concept child : csc.getChildren()) {
				if ( levels.get(child) == -1)
					levels.put(child,child.getParents().size());

				levels.put(child,levels.get(child) - 1);

				if ( levels.get(child) == 0)
					if (child.getExtent().containsAll(concept.getExtent()))
						q.add(child);
			}

		}

		gsh.getConcepts().add(concept);

		for (Concept parent : dsc)
			concept.addParent(parent);
	}

	private void workOnLeftPart(Concept concept, Set<Attribute> SOP,Set<Entity> SOO) {
		List<Entity> CC = new LinkedList<Entity>();

		List<Entity> extents = new ArrayList<Entity>(concept.getExtent());
		Collections.sort(extents, new EntityComparator(context));
		CC.addAll(extents);
		CC.removeAll(simplifies.get(concept));

		for (int i = 0; i < CC.size(); i++) {
			Entity e = CC.get(i);
			
			if ( SOP.containsAll(context.getAttributes(e)) ) {

				Concept newConcept = new Concept();
				newConcept.getIntent().addAll(context.getAttributes(e));
				newConcept.getExtent().add(e);
				SOO.add(e);
				
				for (int j = i + 1; j < CC.size(); j++) {
					Entity cc = CC.get(j);
					if (context.getAttributes(cc).size() == context.getAttributes(e).size()) {
						if (context.getAttributes(cc).equals(context.getAttributes(e))) {
							newConcept.getExtent().add(cc);
							CC.remove(cc);
							SOO.add(cc);
							j--;
						}
					} 
					else {
						if (context.getAttributes(cc).containsAll(context.getAttributes(e))) {
							newConcept.getExtent().add(cc);
							SOO.add(cc);
						}
					}
				}
				classify(newConcept, false);
			}
		}
	}

	@Override
	public ConceptOrder getConceptOrder() {
		return gsh;
	}

}
