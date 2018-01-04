package edu.amss.fca.Method;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.amss.fca.Tools.OWLAPI_tools;
import fr.labri.galatea.Attribute;
import fr.labri.galatea.BinaryAttribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;
import fr.labri.galatea.algo.Algorithm;
import fr.labri.galatea.algo.SimpleGSH;
import fr.labri.galatea.algo.SimpleGSH2;


public class Structural_based_lattice_finder {

	public Structural_based_lattice_finder() {
		// TODO Auto-generated constructor stub
	}

	public static Set<String> get_Improved_Alignment(ArrayList<OWLAPI_tools> Ontologies,Map<String, Map<String, Set<String>>> structure_properity,Set<String> repaired_alignment) throws Exception {
		
		long tic=System.currentTimeMillis();
		Context c = new Context();
		int pre_num = repaired_alignment.size();

		ArrayList<String> Correspondences_initial = new ArrayList<>();
		Correspondences_initial.addAll(repaired_alignment);
		
		/** 将属性添加到context c 中!!!!!! **/
		ArrayList<Attribute> Attr_SubclassOf = new ArrayList<>();

		ArrayList<Attribute> Attr_SiblingWith = new ArrayList<>();
		ArrayList<Attribute> Attr_PartOf = new ArrayList<>();
		ArrayList<Attribute> Attr_SupclassOf = new ArrayList<>();
		ArrayList<Attribute> Attr_HasPart = new ArrayList<>();
		for (int i = 0; i < pre_num; i++) {
			Attribute a1 = new BinaryAttribute("(ISA)" + Correspondences_initial.get(i));
			Attribute a2 = new BinaryAttribute("(Sib)" + Correspondences_initial.get(i));
			Attribute a3 = new BinaryAttribute("(PAT)" + Correspondences_initial.get(i));
			Attribute a4 = new BinaryAttribute("(SUP)" + Correspondences_initial.get(i));
			Attribute a5 = new BinaryAttribute("(HSP)" + Correspondences_initial.get(i));
			Attr_SubclassOf.add(a1);
			Attr_SiblingWith.add(a2);
			Attr_PartOf.add(a3);
			Attr_SupclassOf.add(a4);
			Attr_HasPart.add(a5);
			c.addAttribute(a1);
			c.addAttribute(a2);
			c.addAttribute(a3);
			c.addAttribute(a4);
			c.addAttribute(a5);
		}

		/** 将得到的类按A_和B_分开 **/
		String[] temp = null;
		ArrayList<String> A_Concepts = new ArrayList<>();
		ArrayList<String> B_Concepts = new ArrayList<>();
		Set<String> All_concepts = new HashSet<>();
		Map<String, ArrayList<String>> corres_map = new HashMap<>();//key:概念C; value:与C匹配的所有概念
		for (int i = 0; i < pre_num; i++) {
			temp = Correspondences_initial.get(i).split(" = ");
			String aString = temp[0];
			String bString = temp[1];
			All_concepts.add(aString);
			All_concepts.add(bString);
			if (corres_map.keySet().contains(aString)) 
			{
				ArrayList<String> as = corres_map.get(aString);
				as.add(bString);
			} 
			else 
			{
				ArrayList<String> as = new ArrayList<>();
				as.add(bString);
				corres_map.put(aString, as);
			}
			if (corres_map.keySet().contains(bString)) 
			{
				ArrayList<String> as = corres_map.get(bString);
				as.add(aString);
			} 
			else 
			{
				ArrayList<String> as = new ArrayList<>();
				as.add(aString);
				corres_map.put(bString, as);
			}
			if (temp[0].substring(0, 2).contains("A_")) 
			{
				A_Concepts.add(temp[0].substring(2));
				B_Concepts.add(temp[1].substring(2));
			} 
			else 
			{
				A_Concepts.add(temp[1].substring(2));
				B_Concepts.add(temp[0].substring(2));
			}
		}
		ArrayList<ArrayList<String>> initial_match = new ArrayList<>();
		initial_match.add(A_Concepts);
		initial_match.add(B_Concepts);

		String[] pre = { "A", "B" };

		for (int i = 0; i < Ontologies.size(); i++) 
		{
			OWLAPI_tools Onto = Ontologies.get(i);
			ArrayList<String> concepts = Onto.getConcepts();
			ArrayList<String> pre_concepts = new ArrayList<>();
			ArrayList<Entity> entities = new ArrayList<Entity>();
			for (String concept : concepts) {
				String pre_concept = pre[i] +"_"+ concept;
				pre_concepts.add(pre_concept);
				Entity e = new Entity(pre_concept);
				entities.add(e);
				c.addEntity(e);
			}
			
			Map<String, Set<String>> Subclass_map = structure_properity.get("sub"+pre[i]);
			Map<String, Set<String>> Sib_map = structure_properity.get("sib"+pre[i]);
			Map<String, Set<String>> Partof_map = structure_properity.get("pat"+pre[i]);

			for (int n = 0; n < concepts.size(); n++) {
				String concept = concepts.get(n);
				for (int m = 0; m < pre_num; m++) {
					String con = initial_match.get(i).get(m);
					
					/** 添加SubClassOf对 **/
					Set<String> subclass = Subclass_map.get(con);			
					if (subclass!=null &&subclass.contains(concept)) {
						c.addPair(entities.get(n), Attr_SubclassOf.get(m));
					}

					/** 添加SuperClassOf对 **/
					Set<String> subclass_Object = Subclass_map.get(concept);
					if (subclass_Object!=null&&subclass_Object.contains(con)) {
						c.addPair(entities.get(n), Attr_SupclassOf.get(m));
					}

					/** 添加SiblingWith对 **/
					Set<String> sibclasses = Sib_map.get(con);
					if (sibclasses!=null&&sibclasses.contains(concept) && sibclasses.size() < 100) {
						c.addPair(entities.get(n), Attr_SiblingWith.get(m));
					}
					/** 添加PartOf对 **/
					Set<String> partclasses = Partof_map.get(concepts.get(n));
					if (partclasses!=null&&partclasses.contains(con)) {
						c.addPair(entities.get(n), Attr_PartOf.get(m));
					}
					
					/** 添加HasPart对 **/
					Set<String> haspartclass_Object = Partof_map.get(con);
					if (haspartclass_Object!=null && haspartclass_Object.contains(concept)) {
						c.addPair(entities.get(n), Attr_HasPart.get(m));
					}
				}
			}

		}

		/**过滤属性和对象，删除行为空的对象和列为空的属性**/
		Set<Entity> Entities = new HashSet<>();
		Entities.addAll(c.getEntities());
		for (Entity e : Entities) {
			Set<Attribute> attributes = c.getAttributes(e);
			if (attributes.size() == 0) {
				c.removeEntity(e);
			}
		}

		Set<Attribute> Attributes = new HashSet<>();
		Attributes.addAll(c.getAttributes());
		for (Attribute a : Attributes) {
			Set<Entity> entities = c.getEntities(a);
			if (entities.size() == 0) {
				c.removeAttribute(a);
			}
		}


		c.Clarify();
		long toc=System.currentTimeMillis();
		System.out.println("The time of constructing lattice is "+ (toc-tic)/1000+"  s");
		
		Set<String> discovery = new HashSet<>();//存储所有新找的匹配
		Tool_functions tool = new Tool_functions();
		Set<Entity> entities = c.getEntities();
		String t[] = null;
		ArrayList<Set<String>> A_B = new ArrayList<>();
		Set<String> A = new HashSet<>();
		Set<String> B = new HashSet<>();
		for (Entity e : entities) {
			String string = e.toString().replace("[", "").replace("]", "").trim();
			t = string.split(",");
			if (t.length > 1) {
				A_B = tool.check_A_B(t);
				A = A_B.get(0);
				B = A_B.get(1);
				if (A.size() > 0 && B.size() > 0) {
					if (t.length == 2) {
						String string2 = A.toString().replace("[", "").replace("]", "") + " = "
								+ B.toString().replace("[", "").replace("]", "");
						discovery.add(string2);
					} 
				}
			}
		}
	

		/** 构建格 **/
		//Algorithm a = new SimpleGSH(c);
		tic=System.currentTimeMillis();
		Algorithm a = new SimpleGSH2(c);
		a.compute();
		ConceptOrder o = a.getConceptOrder();
		toc=System.currentTimeMillis();
		System.out.println("The time of computing context is "+ (toc-tic)/1000+"  s");

		Set<Concept> Set_S = new HashSet<>();
		Set<Concept> wanted_A = new HashSet<>();
		Set<Concept> wanted_B = new HashSet<>();
		
		/*StringBuilder ext=new StringBuilder();
		StringBuilder inte=new  StringBuilder();*/

		for (Concept cc : o) {
			Set<Entity> extents = cc.getExtent();
			Set<Attribute> intents = cc.getIntent();
			Set<Entity> generators = cc.getSimplifiedExtent();
			
			//测试是否有效？	(不行，结果会改变)			
			/*for(Entity ex: extents)
				ext.append(ex.toString());			
			for(Attribute in: intents)
				inte.append(in.toString());
			String extent=ext.toString();
			String intent=inte.toString();
			ext.setLength(0);
			inte.setLength(0);*/
			
			//原始代码	
			String extent = extents.toString().replace("[", "").replace("]", "").trim();
			String intent = intents.toString().replace("[", "").replace("]", "").trim();
	
			if (intents.size() > 0 && extents.size() > 0) {
				ArrayList<ArrayList<String>> A_B_ = tool.Separate(extent);
				ArrayList<String> A_Class = A_B_.get(0);
				ArrayList<String> B_Class = A_B_.get(1);
				if (A_Class.size() > 0 && B_Class.size() > 0) {
					Set_S.add(cc);
					if (intent.contains("(ISA)") || (intent.contains("(PAT)") || intent.contains("(SUP)") || intent.contains("(HSP)") )) {
						Analysis_Extent(extents, generators, discovery, tool, intent);
					}
				}
				if (generators.size() == 1) {
					String pre_f = generators.toString().replace("[", "").substring(0, 2);
					if (pre_f.contains("A_")) {
						wanted_A.add(cc);
					} else if (pre_f.contains("B_")) {
						wanted_B.add(cc);
					}
				}
			}
		}
		
		//感觉没啥用，而且占内存
		/*Set<String> Correspondences_initial_set_copy = new HashSet<>();
		Correspondences_initial_set_copy.addAll(repaired_alignment);*/

		String temp__[] = null;
		String aString = null;
		String bString = null;
		Set<String> discovery_copy = new HashSet<>();
		discovery_copy.addAll(discovery);
		for (String string : discovery_copy) {
			temp__ = string.split(" = ");
			aString = temp__[0];
			bString = temp__[1];
			ArrayList<String> A_mathcers = corres_map.get(aString);
			ArrayList<String> B_mathcers = corres_map.get(bString);
			if (A_mathcers != null || B_mathcers!=null) {
				discovery.remove(string);
			}
		}
		repaired_alignment.addAll(discovery);
		//discovery.removeAll(Correspondences_initial_set_copy);
		return repaired_alignment;
	}

	public static void Analysis_Extent(Set<Entity> extents, Set<Entity> generators, Set<String> discovery,
			Tool_functions tool, String intent) throws IOException {
		String extent = extents.toString().replace("[", "").replace("]", "").trim();
		String array[] = extent.split(", ");
		int count = array.length;
		if (extents.size() == 2) {
			if (count == 2) {
				Set<String> new_ = tool.Combine(tool.Separate(extent));
				discovery.addAll(new_);
			}
		}
	}

}