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

import java.util.*;

public class Concept {

	private Set<Entity> extent;

	private Set<Entity> extent_ori;

	private Set<Attribute> intent;

	private Set<Concept> parents;

	private Set<Concept> children;

	public Concept() {
		this.parents = new HashSet<Concept>();
		this.children = new HashSet<Concept>();
		this.intent = new HashSet<Attribute>();
		this.extent = new HashSet<Entity>();
		this.extent_ori = new HashSet<Entity>();
	}

	public Set<Concept> getParents() {
		return parents;
	}

	public Set<Concept> getChildren() {
		return children;
	}

	public void addParent(Concept c) {
		this.parents.add(c);
		c.children.add(this);
	}

	public void removeParent(Concept c) {
		this.parents.remove(c);
		c.children.remove(this);
	}
	
	public void removeChild(Concept c) {
		this.children.remove(c);
		c.parents.remove(this);
	}

	public void addChild(Concept c) {
		this.children.add(c);
		c.parents.add(this);
	}

	public void removeChild_token_lattice(Concept c) {
//		System.out.println("孩子：" + c);
//		System.out.println("删之前先看有没有孩子："+this.children.toString());
//		System.out.println("有没有"+this.children);
//		this.children.remove(c);
//		System.out.println("删除没"+this.children.remove(c));
//		c.parents.remove(this);
		Set<Concept> chi = new HashSet<>();
		Set<Concept> chi1 = new HashSet<>();
		chi.addAll(this.children);
		chi1.addAll(this.children);
		for (Iterator<Concept> iter = chi.iterator(); iter.hasNext();) {
			Concept temp = iter.next();
			Set<Entity> entities = new HashSet<>();
			entities.addAll(temp.getExtent());
			entities.removeAll(c.getExtent());  //可能耗时的地方
			Set<Attribute> attributes = new HashSet<>();
			attributes.addAll(temp.getIntent());
			attributes.remove(c.getIntent());
			if (entities.size() == 0 && attributes.size() == 0) {
//				System.out.println("找到了！！！！");
				chi1.remove(temp);
			} else {
				break;
			}
		}
		this.children = chi1;
	}

	public Set<Entity> getExtent() {
		return extent;
	}

	public void addExtent(Set<Entity> entities) {
		this.extent.addAll(entities);
	}
	
	public void initExtent(Set<Entity> entities) {
		this.extent=entities;
	}

	public Set<Entity> getExtent_Ori() {
		return extent_ori;
	}

	public void setExtent_Ori(Set<Entity> entity_ori) {
		this.extent_ori.addAll(entity_ori);
	}

	public Set<Attribute> getIntent() {
		return intent;
	}

	public void addIntent(Set<Attribute> attributes) {
		this.intent.addAll(attributes);
	}
	
	public void initIntent(Set<Attribute> attributes) {
		this.intent=attributes;
	}

	public Set<Entity> getSimplifiedExtent() {
		Set<Entity> simplifiedExtent = new HashSet<Entity>();
		for (Entity entity : this.getExtent()) {
			boolean leafEntity = true;
			for (Concept child : this.children)
				if (child.getExtent().contains(entity))
					leafEntity = false;
			if (leafEntity)
				simplifiedExtent.add(entity);
		}
		return simplifiedExtent;
	}

	public Set<Attribute> getSimplifiedIntent() {
		Set<Attribute> simplifiedIntent = new HashSet<Attribute>();
		for (Attribute c : this.getIntent()) {
			boolean leafAttribute = true;
			for (Concept parent : this.parents)
				if (parent.getIntent().contains(c))
					leafAttribute = false;
			if (leafAttribute)
				simplifiedIntent.add(c);
		}
		return simplifiedIntent;
	}

	public Set<Concept> getAllChildren() {
		Set<Concept> result = new HashSet<Concept>();
		Set<Concept> temp = new HashSet<Concept>();
		temp.add(this);
		while (temp.size() > 0) {
			Concept concept = pickOne(temp);
			if (!result.contains(concept)) {
				result.add(concept);
				temp.addAll(concept.getChildren());
			}
		}
		return result;
	}

	public Set<Concept> getAllParents() {
		Set<Concept> result = new HashSet<Concept>();
		Set<Concept> temp = new HashSet<Concept>();
		temp.add(this);
		while (temp.size() > 0) {
			Concept concept = pickOne(temp);
			if (!result.contains(concept)) {
				result.add(concept);
				temp.addAll(concept.getParents());
			}
		}
		return result;
	}

	public boolean isEntity() {
		return getSimplifiedExtent().size() == 1;
	}

	public boolean isEntityFusion() {
		return getSimplifiedExtent().size() > 1;
	}

	public boolean isNewEntity() {
		return getSimplifiedExtent().size() < 1;
	}

	public boolean isGreaterThan(Concept c) //非常耗时的地方
	{
		if(this.extent.size()<c.extent.size())
			return false;
		return (this.extent.containsAll(c.extent));
	}

	public boolean isSmallerThan(Concept c) 
	{
		if(c.extent.size()<this.extent.size())
			return false;
		return (c.extent.containsAll(this.extent));
	}

//	public Concept getInfimum(Set<Concept> concepts, Context context) {//用定理可以写，比较简单
//		Set<Entity> entities = new HashSet<>();
//		Set<Attribute> attributes = new HashSet<>();
//		for (Concept concept : concepts) {
//			Set<Entity> ent = concept.getExtent();
//			Set<Attribute> attr = concept.getIntent();
//			if (entities.size() == 0) {
//				entities.addAll(ent);
//			} else {
//				entities.retainAll(entities);
//			}
//			attributes.addAll(attr);
//		}
//		Concept infimum = new Concept();
//		if (entities.size() > 0) { //说明下确界不是最小的formal concept
//			infimum.extent = entities;
//			Set<Entity> derivation1 = context.Attribute_derivation(attributes);
//			Set<Attribute> derivation2 = context.Entity_derivation(derivation1);
//			infimum.intent = derivation2;
//		}
//		return infimum;
//	}

	public String toString() {
		List<String> intent = new ArrayList<>();
		for (Attribute a : getIntent())
			intent.add(a.toString());
		Collections.sort(intent);

		List<String> extent = new ArrayList<>();
		for (Entity e : getExtent())
			extent.add(e.toString());
		Collections.sort(extent);

		List<String> extent_ori = new ArrayList<>();
		for (Entity e : getExtent_Ori())
			extent_ori.add(e.toString());
		Collections.sort(extent_ori);

		return "(" + intent.toString() + "," + extent.toString() + "," + extent_ori.toString() + ")";
	}

	public int hashCode() {
		return this.intent.hashCode() + this.extent.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof Concept))
			return false;
		else {
			Concept c = (Concept) o;
			boolean extEq = this.extent.equals(c.extent);
			boolean intEq = this.intent.equals(c.intent);
			return extEq && intEq;
		}
	}

	public static Concept pickOne(Set<Concept> concepts) {
		if (concepts.isEmpty())
			return null;

		Iterator<Concept> cIt = concepts.iterator();
		Concept tmp = cIt.next();
		cIt.remove();
		return tmp;
	}

}
