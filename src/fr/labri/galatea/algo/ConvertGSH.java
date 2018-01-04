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

import java.awt.event.MouseWheelEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

/**删除一部分attribute，在原有GSH上更改，使其变为删掉attribute之后的context对应的新lattice**/
public class ConvertGSH extends Algorithm {

	private ConceptOrder gsh;

	public ConvertGSH(Context context) {
		super(context);
	}

	public void init_gsh(ConceptOrder conceptOrder) {
		gsh = conceptOrder;
	}

	public void compute(Set<Attribute> delete_attr, Map<Attribute, Concept> attr_attrconcept) {
		Set<Concept> AllChildren = new HashSet<>(); //得到所有delete属性的属性概念的所有children
		Set<Concept> delete_attrconcept = new HashSet<>();
		for (Attribute attribute : delete_attr) {
			Concept attrconcept = attr_attrconcept.get(attribute);//得到该属性的属性概念
			Set<Concept> children = attrconcept.getAllChildren();//得到该属性概念的所有children
			Set<Attribute> key_intent = attrconcept.getSimplifiedIntent();
			if (key_intent.size() == 1) { //如果要删除的这个属性概念的key intent只有这一个属性，则直接删除
				gsh.removeConcept(attrconcept);
				AllChildren.addAll(children);
				delete_attrconcept.add(attrconcept);
			} else { //如果要删除的这个属性的属性概念的key intent还有其他属性				
				if (key_intent.size() == 0) {
					gsh.removeConcept(attrconcept);
					AllChildren.addAll(children);
					delete_attrconcept.add(attr_attrconcept.get(attribute));
				} else {
					AllChildren.addAll(children);
				}
			}
		}
		/**得到不包含delete属性概念的children集合，可能一个delete属性概念是另一个delete属性概念的孩子**/
		AllChildren.removeAll(delete_attrconcept);
		for (Concept child : AllChildren) {
			repalceConcept(child, delete_attr);
		}
	}

	public void repalceConcept(Concept old_concept, Set<Attribute> delete_attr) {
		
		Concept new_concept = new Concept();
		new_concept.addExtent(old_concept.getExtent());
		Set<Attribute> attributes = new HashSet<>();
		attributes.addAll(old_concept.getIntent());
		attributes.removeAll(delete_attr);
		new_concept.addIntent(attributes);

		/**例如，对于概念A:([a, b, c, g, h],[3])，“c”的属性概念B:([a,c],[3,4,6,7,8])是A的parent，
		 * 删去B之后A的key_intent就为c了(为什么看getSimplifiedIntent()函数)，但实际上应该是空的，
		 * 但是又不能改变old的内容**/
		Set<Attribute> key_intent = new HashSet<>();
		key_intent.addAll(old_concept.getSimplifiedIntent());
		key_intent.removeAll(delete_attr);//要删去key_intent中的删除属性，然后看是否大于0
		if (key_intent.size() > 0) {
			gsh.getConcepts().add(new_concept);
			gsh.delete_add_children_parents(old_concept, new_concept);
		} else {
			Set<Entity> entities_calculate = context.Attribute_derivation(attributes);
			Set<Entity> entities_ori = new_concept.getExtent();
			if (entities_calculate.equals(entities_ori)) {
				gsh.getConcepts().add(new_concept);
				gsh.delete_add_children_parents(old_concept, new_concept);
			} 
		}
		gsh.removeConcept(old_concept);
	}

	

	@Override
	public ConceptOrder getConceptOrder() {
		return gsh;
	}

	@Override
	public void compute() {
		// TODO Auto-generated method stub

	}

}
