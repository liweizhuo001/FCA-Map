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

package fr.labri.galatea;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ConceptOrder implements Iterable<Concept> {

	protected Set<Concept> concepts;

	public ConceptOrder() {
		this.concepts = new LinkedHashSet<Concept>();
	}

	public Set<Concept> getConcepts() {
		return concepts;
	}

//	public void removeConcept(Concept concept) {
//		Set<Concept> parents = concept.getParents();
//		Set<Concept> parents_copy = new HashSet<>();
//		parents_copy.addAll(parents);
//		for (Concept parent : parents_copy) {
//			parent.removeChild(concept);
//			concept.removeParent(parent);
//		}
//
//		Set<Concept> children = concept.getChildren();
//		Set<Concept> Children_copy = new HashSet<>();
//		Children_copy.addAll(children);
//		for (Concept child : Children_copy) {
//			child.removeParent(concept);
//			concept.removeChild(child);
//		}
//		this.concepts.remove(concept);
//	}
	
	
	public void removeConcept(Concept concept) {
		Set<Concept> parents = concept.getParents();
		Set<Concept> parents_copy = new HashSet<>();
		parents_copy.addAll(parents);
		for (Concept parent : parents_copy) {
			parent.removeChild(concept);
		}

		Set<Concept> children = concept.getChildren();
		Set<Concept> Children_copy = new HashSet<>();
		Children_copy.addAll(children);
		/**需要将删去的concept的父和子连接起来**/
		for (Concept child : Children_copy) {
			child.removeParent(concept);
			Set<Concept> child_parents = new HashSet<>();
			child_parents.addAll(child.getAllParents());//得到要删结点的一个孩子的所有parents
			parents_copy.removeAll(child_parents);//移除该孩子已经有的parent，添加通过要删结点才能有的parent
			for(Concept parent:parents_copy){
				child.addParent(parent);
			}
		}
		this.concepts.remove(concept);
	}
	
	//根据将老的概念中的父子添加给新的概念
	public void delete_add_children_parents(Concept old_concept, Concept new_concept) {
		Set<Concept> parents = new HashSet<>();
		parents.addAll(old_concept.getParents());
		Iterator<Concept> iterator = parents.iterator();
		while (iterator.hasNext()) {
			Concept parent = iterator.next();
			parent.removeChild(old_concept);
			new_concept.addParent(parent);
		}

		Set<Concept> children = new HashSet<>();
		children.addAll(old_concept.getChildren());
		Iterator<Concept> iterator1 = children.iterator();
		while (iterator1.hasNext()) {
			Concept child = iterator1.next();
			child.removeParent(old_concept);
			new_concept.addChild(child);
		}
	}

	
	public void delete_children_parents(Concept old_concept) {
		Set<Concept> parents = new HashSet<>();
		parents.addAll(old_concept.getParents());
		Iterator<Concept> iterator = parents.iterator();
		while (iterator.hasNext()) {
			Concept parent = iterator.next();
			parent.removeChild(old_concept);
		}

		Set<Concept> children = new HashSet<>();
		children.addAll(old_concept.getChildren());
		Iterator<Concept> iterator1 = children.iterator();
		while (iterator1.hasNext()) {
			Concept child = iterator1.next();
			child.removeParent(old_concept);
		}
	}

	public boolean addConcept(Concept concept) {
		Boolean flag = false;
		if (this.concepts.add(concept)) {
			flag = true;
		}
		return flag;

	}

	public void removeConcept(Set<Concept> concepts) {
		for (Concept concept : concepts) {
			removeConcept(concept);
		}
	}

	public void repalceConcept(Concept old_concept, Attribute deleteAttr) {
		Concept new_concept = new Concept();
		new_concept.addExtent(old_concept.getExtent());
		Set<Attribute> attributes = old_concept.getIntent();
		attributes.remove(deleteAttr);
		new_concept.addIntent(attributes);
		this.concepts.add(new_concept);
		Set<Concept> parents = old_concept.getParents();
		for (Concept parent : parents) {
			parent.addChild(new_concept);
			new_concept.addParent(parent);
		}

		Set<Concept> children = old_concept.getChildren();
		for (Concept child : children) {
			child.addParent(new_concept);
			new_concept.addChild(child);
		}
		this.concepts.remove(old_concept);
	}

	public void repalceConcept(Concept old_concept, Set<Attribute> deleteAttrs) {
		Concept new_concept = new Concept();
		new_concept.addExtent(old_concept.getExtent());
		Set<Attribute> attributes = old_concept.getIntent();
		attributes.remove(deleteAttrs);
		new_concept.addIntent(attributes);
		this.concepts.add(new_concept);
		Set<Concept> parents = old_concept.getParents();
		for (Concept parent : parents) {
			parent.addChild(new_concept);
			new_concept.addParent(parent);
		}

		Set<Concept> children = old_concept.getChildren();
		for (Concept child : children) {
			child.addParent(new_concept);
			new_concept.addChild(child);
		}
		this.concepts.remove(old_concept);
	}

	@Override
	public Iterator<Concept> iterator() {
		return concepts.iterator();
	}

	public int newEntityNb() {
		int i = 0;
		for (Concept c : this)
			if (c.isNewEntity())
				i++;
		return i;
	}

	public int entityFusionNb() {
		int i = 0;
		for (Concept c : this)
			if (c.isEntityFusion())
				i++;
		return i;
	}

	public int entityNb() {
		int i = 0;
		for (Concept c : this)
			if (c.isEntity())
				i++;
		return i;
	}

	public int size() {
		return this.concepts.size();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " with " + size() + " concepts. " + entityNb() + " entity concepts, "
				+ entityFusionNb() + " entity fusion concepts and " + newEntityNb() + " new entity concepts.";
	}

	public Concept getInfimum(Set<Concept> concepts, Context context) {//用定理可以写，比较简单
		Set<Entity> entities = new HashSet<>();
		Set<Attribute> attributes = new HashSet<>();
		int count = 0;
		for (Concept concept : concepts) {
			Set<Entity> ent = concept.getExtent();
			Set<Attribute> attr = concept.getIntent();
			if (count == 0) {
				entities.addAll(ent);
			} else if (entities.size() == 0) {
				break;
			} else {
				entities.retainAll(ent);
			}
//			System.out.println(entities);
			attributes.addAll(attr);
			count++;
		}
		Concept infimum = new Concept();
//		System.out.println("attributes= " + attributes);
		if (entities.size() > 0) { //说明下确界不是最小的formal concept
			infimum.addExtent(entities);
			Set<Entity> derivation1 = context.Attribute_derivation(attributes);
//			System.out.println("1" + derivation1);
			Set<Attribute> derivation2 = context.Entity_derivation(derivation1);
			infimum.addIntent(derivation2);
//			System.out.println("2" + derivation2);
		}
		return infimum;
	}

	public Concept getSupremum(Set<Concept> concepts, Context context) {
		Set<Entity> entities = new HashSet<>();
		Set<Attribute> attributes = new HashSet<>();
		int count = 0;
		for (Concept concept : concepts) {
			Set<Entity> ent = concept.getExtent();
			Set<Attribute> attr = concept.getIntent();
			if (count == 0) {
				attributes.addAll(attr);
			} else if (attributes.size() == 0) {
				break;
			} else {
				attributes.retainAll(attr);
			}
//			System.out.println(attr);
			entities.addAll(ent);
			count++;
		}
		Concept supremum = new Concept();
//		System.out.println("entities= " + entities);
		if (attributes.size() > 0) { //说明上确界不是最大的formal concept
			supremum.addIntent(attributes);

			Set<Attribute> derivation1 = context.Entity_derivation(entities);
			Set<Entity> derivation2 = context.Attribute_derivation(derivation1);
			supremum.addExtent(derivation2);
//			System.out.println("1" + derivation1);
//			System.out.println("2" + derivation2);
		}
		return supremum;
	}

	

}
