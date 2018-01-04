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
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class SimpleGSH extends Algorithm {

	private Set<Concept> concepts;

	private ConceptOrder gsh;
	
	public SimpleGSH(Context context) {
		super(context);
	}

	@Override
	public void compute() {
		gsh = new ConceptOrder();
		concepts = new HashSet<Concept>();
		
		for( Entity e : context.getEntities() )
			concepts.add(u(e));

		for( Attribute a : context.getAttributes() )
			concepts.add(v(a));

		HashSet<Concept> addedConcepts = new HashSet<Concept>();

		for( Concept c: concepts )
			add(c,addedConcepts);

		for( Concept c: concepts ) 
			gsh.getConcepts().add(c);
	}

	private void add(Concept c,Set<Concept> addedConcepts) {
		if ( addedConcepts.contains(c) )
			return;

		Set<Concept> allParents = allGreaters(c);
		for( Concept p: allParents) 
			if ( !addedConcepts.contains(p) )
				add(p,addedConcepts);

		addedConcepts.add(c);
		Set<Concept> smallestParents = selectSmallest(allParents);

		for ( Concept parent: smallestParents )
			c.addParent(parent);
	}

	private Set<Concept> allGreaters(Concept c) {
		HashSet<Concept> allGreaters = new HashSet<Concept>();
		for( Concept candidate: concepts)
			if ( c != candidate) 
				if ( candidate.isGreaterThan(c) )
					allGreaters.add(candidate);		

		return allGreaters;
	}

	private Set<Concept> selectSmallest(Set<Concept> concepts) {
		Set<Concept> smallests = new HashSet<Concept>();
		for ( Concept c: concepts )
			push(c,smallests);

		return smallests;
	}

	private void push(Concept c,Set<Concept> smallests) {
		Set<Concept> swap = new HashSet<Concept>();
		for ( Concept current: smallests )
			if ( current.isSmallerThan(c) )
				return;
			else if ( c.isSmallerThan(current) )
				swap.add(current);
		if ( swap.size() > 0 )
			smallests.removeAll(swap);
			
		smallests.add(c);

	}

	private Concept u(Entity e) {
		Concept c = new Concept();
		c.getIntent().addAll(context.getAttributes(e));
		
		/*Set<Entity> extent = new HashSet<Entity>();
		extent.addAll(context.getEntities());*/
		
		/*Set<Attribute> intend=new HashSet<Attribute>(context.getAttributes()); 
		c.initIntent(intend);*/	
		
		Set<Entity> extent = new HashSet<Entity>(context.getEntities()); //效果是否是一样的?会不会更消耗内存
		
		for( Attribute a: context.getAttributes(e) )
			extent.retainAll(context.getEntities(a));
		
		c.getExtent().addAll(extent);
		//c.initExtent(extent);  //是不是可以节约少量操作?(但似乎较前者内存在增加)
		return c;
	}

	private Concept v(Attribute a) {
		Concept c = new Concept();
		c.getExtent().addAll(context.getEntities(a));
		
		/*Set<Attribute> intent = new HashSet<Attribute>();
		intent.addAll(context.getAttributes());*/
		
		/*Set<Entity> extent = new HashSet<Entity>(context.getEntities()); //效果是否是一样的?会不会更消耗内存
		c.initExtent(extent);	*/	
		
		Set<Attribute> intent = new HashSet<Attribute>(context.getAttributes()); 
		
		for( Entity e: context.getEntities(a) )
			intent.retainAll(context.getAttributes(e));
		
		c.getIntent().addAll(intent);
		//c.initIntent(intent);//是不是可以节约少量操作?(但似乎较前者内存在增加)
		return c;
	}

	@Override
	public ConceptOrder getConceptOrder() {
		return gsh;
	}

}
