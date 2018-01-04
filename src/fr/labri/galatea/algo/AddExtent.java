/*
   Copyright 2009 Jean-R茅my Falleri

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

import java.util.HashSet;
import java.util.Set;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptLattice;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class AddExtent extends Algorithm {
	
	private ConceptLattice lattice;
	
	public AddExtent(Context context) {
		super(context);
	}
	
	@Override
	public ConceptOrder getConceptOrder() {
		return lattice;
	}
	
	@Override
	public void compute() {
		lattice = new ConceptLattice();
		
		Concept top = new Concept();
		top.getExtent().addAll(context.getEntities());
		lattice.getConcepts().add(top);
		lattice.setTop(top);
		
		for(Attribute attribute: context.getAttributes()) {
//			System.out.println("================属性===="+attribute.toString()+"==================");
			Concept concept = addExtent(context.getEntities(attribute),top,lattice);
//			System.out.println(concept.toString());
			for(Concept child: concept.getAllChildren() ){
//				System.out.println("原始的child："+child.toString());
				child.getIntent().add(attribute);
//				System.out.println("添加属性"+attribute.toString()+"后的child："+child.toString());
				}
//			System.out.println("当前的lattice："+lattice.getConcepts().toString());
		}
		
	}
	
	private Concept addExtent(Set<Entity> extent, Concept generator, ConceptLattice lattice) {		
		generator = getMaximalConcept(extent, generator, lattice);
		if ( extent.equals(generator.getExtent()) )
			return generator;

		Set<Concept> newChildren = new HashSet<Concept>();

		for( Concept candidate: generator.getChildren() ) {
			if ( ! candidate.getExtent().containsAll(extent) ) {
				Set<Entity> intersection = new HashSet<Entity>(candidate.getExtent());
				intersection.retainAll(extent);
				candidate = addExtent(intersection, candidate,lattice);
			}
			boolean addChild = true;
			Set<Concept> conceptsToDelete = new HashSet<Concept>();

			for(Concept child: newChildren ) {
				if ( child.getExtent().containsAll(candidate.getExtent()) ) {
					addChild = false;
					break;
				}
				else if ( candidate.getExtent().containsAll(child.getExtent()) )
					conceptsToDelete.add(child);
			}
			for( Concept concept: conceptsToDelete )
				newChildren.remove(concept);
			if ( addChild )
				newChildren.add(candidate);
		}

		Concept newConcept = new Concept();
		newConcept.getIntent().addAll(generator.getIntent());
		newConcept.getExtent().addAll(extent);
		lattice.getConcepts().add(newConcept);

		for ( Concept child: newChildren ) {
			generator.removeChild(child);
			newConcept.addChild(child);
		}

		generator.addChild(newConcept);
	
		return newConcept;
	}

	private Concept getMaximalConcept(Set<Entity> extent, Concept generator, ConceptLattice lattice) {
		boolean isMaximal = true;
		while ( isMaximal ) {
			isMaximal = false;
			for ( Concept child: generator.getChildren()) {
				if ( child.getExtent().containsAll(extent) ) {
					generator = child;
					isMaximal = true;
					break;
				}
			}
		}
		return generator;
	}

}
