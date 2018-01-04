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

package fr.labri.galatea;

import java.util.*;

public class Concept2 {

	private Set<Entity> extent;

//	private Set<Entity> extent_ori;

	private Set<Attribute> intent;

	private Set<Concept2> parents;

	private Set<Concept2> children;

	public Concept2() {
		this.parents = new HashSet<Concept2>();
		this.children = new HashSet<Concept2>();
		this.intent = new HashSet<Attribute>();
		this.extent = new HashSet<Entity>();
//		this.extent_ori = new HashSet<Entity>();
	}

	public Set<Concept2> getParents() {
		return parents;
	}

	public Set<Concept2> getChildren() {
		return children;
	}

	public void addParent(Concept2 c) {
		this.parents.add(c);
		c.children.add(this);
	}

	public void removeParent(Concept2 c) {
		this.parents.remove(c);
		c.children.remove(this);
	}

	public void addChild(Concept2 c) {
		this.children.add(c);
		c.parents.add(this);
	}

	public void removeChild(Concept2 c) {
		this.children.remove(c);
		System.out.println(this.children.remove(c));
		c.parents.remove(this);
	}

	public Set<Entity> getExtent() {
		return extent;
	}

//	public Set<Entity> getExtent_Ori() {
//		return extent_ori;
//	}
	
//	public void setExtent_Ori(Set<Entity> entity_ori) {
//		this.extent_ori.addAll(entity_ori);
//	}

	public Set<Attribute> getIntent() {
		return intent;
	}

	public Set<Entity> getSimplifiedExtent() {
		Set<Entity> simplifiedExtent = new HashSet<Entity>();
		for (Entity entity : this.getExtent()) {
			boolean leafEntity = true;
			for (Concept2 child : this.children)
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
			for (Concept2 parent : this.parents)
				if (parent.getIntent().contains(c))
					leafAttribute = false;
			if (leafAttribute)
				simplifiedIntent.add(c);
		}
		return simplifiedIntent;
	}

	public Set<Concept2> getAllChildren() {
		Set<Concept2> result = new HashSet<Concept2>();
		Set<Concept2> temp = new HashSet<Concept2>();
		temp.add(this);
		while (temp.size() > 0) {
			Concept2 concept = pickOne(temp);
			if (!result.contains(concept)) {
				result.add(concept);
				temp.addAll(concept.getChildren());
			}
		}
		return result;
	}

	public Set<Concept2> getAllParents() {
		Set<Concept2> result = new HashSet<Concept2>();
		Set<Concept2> temp = new HashSet<Concept2>();
		temp.add(this);
		while (temp.size() > 0) {
			Concept2 concept = pickOne(temp);
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

	public boolean isGreaterThan(Concept2 c) {
		return (this.extent.containsAll(c.extent));
	}

	public boolean isSmallerThan(Concept2 c) {
		return (c.extent.containsAll(this.extent));
	}

	public String toString() {
		List<String> intent = new ArrayList<>();
		for (Attribute a : getIntent())
			intent.add(a.toString());
		Collections.sort(intent);

		List<String> extent = new ArrayList<>();
		for (Entity e : getExtent())
			extent.add(e.toString());
		Collections.sort(extent);

//		List<String> extent_ori = new ArrayList<>();
//		for (Entity e : getExtent_Ori())
//			extent_ori.add(e.toString());
//		Collections.sort(extent_ori);

		return "(" + intent.toString() + "," + extent.toString() +  ")";
	}

	public int hashCode() {
		return this.intent.hashCode() + this.extent.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof Concept2))
			return false;
		else {
			Concept2 c = (Concept2) o;
			boolean extEq = this.extent.equals(c.extent);
			boolean intEq = this.intent.equals(c.intent);
			return extEq && intEq;
		}
	}

	public static Concept2 pickOne(Set<Concept2> concepts) {
		if (concepts.isEmpty())
			return null;

		Iterator<Concept2> cIt = concepts.iterator();
		Concept2 tmp = cIt.next();
		cIt.remove();
		return tmp;
	}

}
