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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Attr;

public class Context {

	protected Set<Attribute> attributes;

	protected Set<Entity> entities;

	protected Map<Entity, Set<Attribute>> relation;

	protected Map<Attribute, Set<Entity>> reverseRelation;

	public Context() {
		this.attributes = new LinkedHashSet<Attribute>();
		this.entities = new LinkedHashSet<Entity>();
		this.relation = new LinkedHashMap<Entity, Set<Attribute>>();
		this.reverseRelation = new LinkedHashMap<Attribute, Set<Entity>>();
	}

	public void addPair(Entity e, Attribute a) {
		if (!relation.containsKey(e))
			relation.put(e, new HashSet<Attribute>());

		if (!reverseRelation.containsKey(a))
			reverseRelation.put(a, new HashSet<Entity>());

		relation.get(e).add(a);
		reverseRelation.get(a).add(e);
	}

	public void removePair(Entity e, Attribute a) {
		if (relation.get(e).contains(a) && reverseRelation.get(a).contains(e)) {
			relation.get(e).remove(a);
			reverseRelation.get(a).remove(e);
		}
	}

//	public void removePair(Entity e,Attribute a) {
//		if ( !relation.containsKey(e) )
//			relation.put(e,new HashSet<Attribute>());
//		
//		if ( !reverseRelation.containsKey(a) )
//			reverseRelation.put(a,new HashSet<Entity>());
//		
//		relation.get(e).add(a);
//		reverseRelation.get(a).add(e);
//	}

	public boolean hasPair(Entity e, Attribute a) {
		return this.getAttributes(e).contains(a);
	}

	public int getPairNb() {
		int p = 0;
		for (Set<Attribute> attrs : relation.values())
			p += attrs.size();
		return p;
	}

	public void addAttribute(Attribute a) {
		this.attributes.add(a);
	}

	public void addAllAttribute(Set<Attribute> alist) {
		for (Attribute a : alist) {
			this.attributes.add(a);
		}
	}

	/**remove attribute**/
	public void removeAttribute(Attribute a) {

		Set<Entity> temp = this.getEntities(a);
		int size = temp.size();
		ArrayList<Entity> temp1 = new ArrayList<>();

		if (size != 0) {
			temp1.addAll(temp);
			for (int i = 0; i < temp1.size(); i++) {
				Entity e = temp1.get(i);
				this.removePair(e, a);
			}
		}

		this.attributes.remove(a);
	}

	public Set<Attribute> getAttributes(Entity e) {
		if (relation.containsKey(e))
			return relation.get(e);
		else
			return new HashSet<Attribute>();
	}

	//对对象集合求导
	public Set<Attribute> Entity_derivation(Set<Entity> entities) {
		Set<Attribute> attributes = new HashSet<>();
		for (Entity e : entities) {
			Set<Attribute> attr = getAttributes(e);
			if (attributes.size() == 0) {
				attributes.addAll(attr);
			} else {
				attributes.retainAll(attr);
			}
		}
		return attributes;
	}
    
	//对属性集合求导
	public Set<Entity> Attribute_derivation(Set<Attribute> attributes) {
		Set<Entity> entities = new HashSet<>();
		for (Attribute a : attributes) {
			Set<Entity> ent = getEntities(a);
			if (entities.size() == 0) {
				entities.addAll(ent);
			} else {
				entities.retainAll(ent);
			}
		}
		return entities;
	}

	public Set<Attribute> getAttributes() {
		return attributes;
	}

	public int getAttributeNb() {
		return attributes.size();
	}

	public void addEntity(Entity e) {
		this.entities.add(e);
	}

	public void addAllEntity(Set<Entity> elist) {
		for (Entity e : elist) {
			this.entities.add(e);
		}
	}

	/**remove entity**/
	public void removeEntity(Entity e) {
		Set<Attribute> temp = this.getAttributes(e);
		int size = temp.size();
		ArrayList<Attribute> temp1 = new ArrayList<>();

		if (size != 0) {
			temp1.addAll(temp);
			for (int i = 0; i < temp1.size(); i++) {
				Attribute a = temp1.get(i);
				this.removePair(e, a);
			}
		}

		this.entities.remove(e);
	}

	public Set<Entity> getEntities(Attribute a) {
		if (reverseRelation.containsKey(a))
			return reverseRelation.get(a);
		else
			return new HashSet<Entity>();
	}

	public Set<Entity> getEntities() {
		return entities;
	}

	public int getEntityNb() {
		return entities.size();
	}

	public String toString() {
		return this.getClass().getSimpleName() + " with " + getEntityNb() + " entities, " + getAttributeNb()
				+ " attributes and " + getPairNb() + " pairs in the binary relation.";
	}

	/**背景净化：先净化个数多的维度**/
	/*public void Clarify() {
		int attr_num = attributes.size();
		int ent_num = entities.size();
		if (attr_num < ent_num || attr_num == ent_num) {
			Clarify_Attribute(this);
			Clarify_Entity(this);
		} else if (attr_num < ent_num) {
			Clarify_Entity(this);
			Clarify_Attribute(this);
		}
	}*/

	public void Clarify_Attribute(Context context) {
		ArrayList<Attribute> attrs = new ArrayList<>();
		attrs.addAll(context.getAttributes());
		ArrayList<Entity> entis = new ArrayList<>();
		entis.addAll(context.getEntities());
		int attr_num = attrs.size();
		int ent_num = entis.size();
		Set<String> rows = new HashSet<>();
		Map<String, ArrayList<Attribute>> row_list_map = new HashMap<>();
		for (int i = 0; i < attr_num; i++) 
		{
			Attribute a = attrs.get(i);
		
			Set<Entity> es = context.getEntities(a);
			Set<Entity> es_copy = new HashSet<>();
			es_copy.addAll(es);
			/*String row = "";
			//ArrayList<Integer> indexs = new ArrayList<>();
			LinkedList<Integer> indexs = new LinkedList<>();
			for (Entity e : es) 
			{
				indexs.add(entis.indexOf(e));
			}

			for (int j = 0; j < ent_num; j++) 
			{
				if (indexs.contains(j)) {
					row = row + "1";
				} else {
					row = row + "0";
				}
			}*/
			
			StringBuilder row = new StringBuilder();
			HashSet<Integer> indexs = new HashSet<>();
			for (Entity e : es) 
			{
				indexs.add(entis.indexOf(e));
			}

			for (int j = 0; j < ent_num; j++) 
			{
				if (indexs.contains(j)) 
				{
					row.append("1");
					//row = row + "1";
				} else {
					row.append("0");
					//row = row + "0";
				}
			}
			
//			System.out.println(row);
			//如果list的size>1,则该list的第一个属性是没有删除的，需要将它删掉，并添加pair(e,属性list)
			if (rows.add(row.toString())) 
			{
				ArrayList<Attribute> temp = new ArrayList<>();
				temp.add(a);
				row_list_map.put(row.toString(), temp);
			} 
			else 
			{
				//感觉没什么用
				/*ArrayList<Attribute> temp = row_list_map.get(row);
				temp.add(a);*/

				for (Entity e : es_copy) 
				{
					context.removePair(e, a);
				}
				context.getAttributes().remove(a);
			}

		} //for attrs ends
		for (ArrayList<Attribute> list : row_list_map.values()) 
		{
			if (list.size() > 1) 
			{
				Attribute a = list.get(0);
				String attrs_string = list.toString().replace("[", "").replace("]", "");
				Attribute new_a = new BinaryAttribute(attrs_string);
				context.addAttribute(new_a);
				Set<Entity> eSet = context.getEntities(a);
				Set<Entity> eSet_copy = new HashSet<>();
				eSet_copy.addAll(eSet);
				for (Entity e : eSet_copy) 
				{
					context.removePair(e, a);
					context.addPair(e, new_a);
				}
				context.getAttributes().remove(a);
			} 
			else
				continue;
		}
	}

	public void Clarify_Entity(Context context) {
		ArrayList<Attribute> attrs = new ArrayList<>();
		attrs.addAll(context.getAttributes());
		ArrayList<Entity> entis = new ArrayList<>();
		entis.addAll(context.getEntities());
		int attr_num = attrs.size();
		int ent_num = entis.size();
		Set<String> columns = new HashSet<>();
		Map<String, ArrayList<Entity>> column_list_map = new HashMap<>();
		for (int i = 0; i < ent_num; i++) {
			Entity e = entis.get(i);
			
			Set<Attribute> as = context.getAttributes(e);
			Set<Attribute> as_copy = new HashSet<>();
			as_copy.addAll(as);
			//ArrayList<Integer> indexs = new ArrayList<>();
			/*LinkedList<Integer> indexs = new LinkedList<>();
			String column = ""; 
			for (Attribute a : as) {
				indexs.add(attrs.indexOf(a));
			}

			for (int j = 0; j < attr_num; j++) {
				if (indexs.contains(j)) {
					column = column + "1";
				} else {
					column = column + "0";
				}
			}*/
			StringBuilder column=new StringBuilder();
			HashSet<Integer> indexs = new HashSet<>();
			for (Attribute a : as) {
				indexs.add(attrs.indexOf(a));
			}

			for (int j = 0; j < attr_num; j++) {
				if (indexs.contains(j)) {		
					//column = column + "1";
					column.append("1");
				} else {
					//column = column + "0";
					column.append("0");
				}
			}
//			System.out.println(column);
			//如果list的size>1,则该list的第一个属性是没有删除的，需要将它删掉，并添加pair(e,属性list)
			//if (columns.add(column)) 
			if (columns.add(column.toString())) 	
			{
				ArrayList<Entity> temp = new ArrayList<>();
				temp.add(e);
				column_list_map.put(column.toString(), temp);
			} 
			else 
			{
				//感觉没什么
				/*ArrayList<Entity> temp = column_list_map.get(column);
				temp.add(e);*/

				for (Attribute a : as_copy) {
					context.removePair(e, a);
				}
				context.getEntities().remove(e);
			}

		} //for attrs ends
		for (ArrayList<Entity> list : column_list_map.values()) {
			if (list.size() > 1) {
				Entity e = list.get(0);
				String entis_string = list.toString().replace("[", "").replace("]", "");
				Entity new_e = new Entity(entis_string);
				context.addEntity(new_e);
				Set<Attribute> aSet = context.getAttributes(e);
				Set<Attribute> aSet_copy = new HashSet<>();
				aSet_copy.addAll(aSet);
				for (Attribute a : aSet_copy) {
					context.removePair(e, a);
					context.addPair(new_e, a);
				}
				context.getEntities().remove(e);
			} else
				continue;
		}

	}
	
	public void Clarify() {
		int attr_num = this.attributes.size();
		int ent_num = this.entities.size();
		if (attr_num >= ent_num && attr_num != ent_num) {
			this.Clarify_ent(this);
			this.Clarify_attr(this);
		} else {
			this.Clarify_attr(this);
			this.Clarify_ent(this);
		}

	}

	public void Clarify_attr(Context context) {
		Set<Attribute> attrs = context.getAttributes();
		Set<Entity> entis = context.getEntities();
		HashMap entity_index_map = new HashMap();
		int i = 0;
		Iterator<Entity> attr_indexlist = entis.iterator();

		while (attr_indexlist.hasNext()) {
			Entity attr_hashcode_map = (Entity) attr_indexlist.next();
			entity_index_map.put(attr_hashcode_map, Integer.valueOf(i++));
		}

		HashMap arg22 = new HashMap();
		HashMap arg23 = new HashMap();
		Iterator arg8 = attrs.iterator();

		Set attributes;
		Iterator arg12;
		while (arg8.hasNext()) {
			Attribute index = (Attribute) arg8.next();
			attributes = context.getEntities(index);
			ArrayList copy = new ArrayList();
			arg12 = attributes.iterator();

			while (arg12.hasNext()) {
				Entity attribute = (Entity) arg12.next();
				int list1 = ((Integer) entity_index_map.get(attribute)).intValue();
				copy.add(Integer.valueOf(list1));
			}

			arg23.put(index, copy);
			this.update_map1(arg22, copy.hashCode(), index);
		}

		arg8 = arg22.keySet().iterator();

		label66 : while (arg8.hasNext()) {
			int arg24 = ((Integer) arg8.next()).intValue();
			attributes = (Set) arg22.get(Integer.valueOf(arg24));
			HashSet arg25 = new HashSet();
			arg25.addAll(attributes);
			arg12 = attributes.iterator();

			while (true) {
				ArrayList same_attr;
				Attribute arg26;
				do {
					do {
						if (!arg12.hasNext()) {
							continue label66;
						}

						arg26 = (Attribute) arg12.next();
					} while (!arg25.contains(arg26));

					ArrayList arg27 = (ArrayList) arg23.get(arg26);
					same_attr = new ArrayList();
					same_attr.add(arg26);
					arg25.remove(arg26);
					Iterator new_a = attributes.iterator();

					while (new_a.hasNext()) {
						Attribute attrs_string = (Attribute) new_a.next();
						if (arg25.contains(attrs_string)) {
							ArrayList entities = (ArrayList) arg23.get(attrs_string);
							if (arg27.equals(entities)) {
								same_attr.add(attrs_string);
								arg25.remove(attrs_string);
							}
						}
					}
				} while (same_attr.size() == 1);

				String arg28 = same_attr.toString().replace("[", "").replace("]", "");
				BinaryAttribute arg29 = new BinaryAttribute(arg28);
				context.addAttribute(arg29);
				HashSet arg30 = new HashSet();
				arg30.addAll(context.getEntities(arg26));
				Iterator arg19 = arg30.iterator();

				while (arg19.hasNext()) {
					Entity e = (Entity) arg19.next();
					context.addPair(e, arg29);
					Iterator arg21 = same_attr.iterator();

					while (arg21.hasNext()) {
						Attribute attr = (Attribute) arg21.next();
						context.removePair(e, attr);
						context.getAttributes().remove(attr);
					}
				}
			}
		}

	}

	public void Clarify_ent(Context context) {
		Set attrs = context.getAttributes();
		Set entis = context.getEntities();
		HashMap attr_index_map = new HashMap();
		int i = 0;
		Iterator ent_indexlist = attrs.iterator();

		while (ent_indexlist.hasNext()) {
			Attribute enty_hashcode_map = (Attribute) ent_indexlist.next();
			attr_index_map.put(enty_hashcode_map, Integer.valueOf(i++));
		}

		HashMap arg22 = new HashMap();
		HashMap arg23 = new HashMap();
		Iterator arg8 = entis.iterator();

		Set entities;
		Iterator arg12;
		while (arg8.hasNext()) {
			Entity index = (Entity) arg8.next();
			entities = context.getAttributes(index);
			ArrayList copy = new ArrayList();
			arg12 = entities.iterator();

			while (arg12.hasNext()) {
				Attribute entity = (Attribute) arg12.next();
				int list1 = ((Integer) attr_index_map.get(entity)).intValue();
				copy.add(Integer.valueOf(list1));
			}

			arg23.put(index, copy);
			this.update_map2(arg22, copy.hashCode(), index);
		}

		arg8 = arg22.keySet().iterator();

		label66 : while (arg8.hasNext()) {
			int arg24 = ((Integer) arg8.next()).intValue();
			entities = (Set) arg22.get(Integer.valueOf(arg24));
			HashSet arg25 = new HashSet();
			arg25.addAll(entities);
			arg12 = entities.iterator();

			while (true) {
				ArrayList same_ent;
				Entity arg26;
				do {
					do {
						if (!arg12.hasNext()) {
							continue label66;
						}

						arg26 = (Entity) arg12.next();
					} while (!arg25.contains(arg26));

					ArrayList arg27 = (ArrayList) arg23.get(arg26);
					same_ent = new ArrayList();
					same_ent.add(arg26);
					arg25.remove(arg26);
					Iterator new_e = entities.iterator();

					while (new_e.hasNext()) {
						Entity ents_string = (Entity) new_e.next();
						if (arg25.contains(ents_string)) {
							ArrayList attributes = (ArrayList) arg23.get(ents_string);
							if (arg27.equals(attributes)) {
								same_ent.add(ents_string);
								arg25.remove(ents_string);
							}
						}
					}
				} while (same_ent.size() == 1);

				String arg28 = same_ent.toString().replace("[", "").replace("]", "");
				Entity arg29 = new Entity(arg28);
				context.addEntity(arg29);
				HashSet arg30 = new HashSet();
				arg30.addAll(context.getAttributes(arg26));
				Iterator arg19 = arg30.iterator();

				while (arg19.hasNext()) {
					Attribute a = (Attribute) arg19.next();
					context.addPair(arg29, a);
					Iterator arg21 = same_ent.iterator();

					while (arg21.hasNext()) {
						Entity ent = (Entity) arg21.next();
						context.removePair(ent, a);
						context.getEntities().remove(ent);
					}
				}
			}
		}

	}

	public void update_map1(Map<Integer, Set<Attribute>> attr_hashcode_map, int hashcode, Attribute attr) {
		if (attr_hashcode_map.containsKey(Integer.valueOf(hashcode))) {
			Set set = (Set) attr_hashcode_map.get(Integer.valueOf(hashcode));
			set.add(attr);
		} else {
			HashSet set1 = new HashSet();
			set1.add(attr);
			attr_hashcode_map.put(Integer.valueOf(hashcode), set1);
		}

	}

	public void update_map2(Map<Integer, Set<Entity>> enty_hashcode_map, int hashcode, Entity entity) {
		if (enty_hashcode_map.containsKey(Integer.valueOf(hashcode))) {
			Set set = (Set) enty_hashcode_map.get(Integer.valueOf(hashcode));
			set.add(entity);
		} else {
			HashSet set1 = new HashSet();
			set1.add(entity);
			enty_hashcode_map.put(Integer.valueOf(hashcode), set1);
		}

	}
}
