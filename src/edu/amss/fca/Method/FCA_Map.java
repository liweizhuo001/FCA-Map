package edu.amss.fca.Method;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;






import edu.amss.fca.Tools.OWLAPI_tools;

public class FCA_Map {
	public URL returnAlignmentFile(URL source, URL target) throws Exception {
		// TODO Auto-generated method stub	
		OWLAPI_tools Onto1 = new OWLAPI_tools();
		OWLAPI_tools Onto2 = new OWLAPI_tools();
		Onto1.readOnto(source);
		Onto2.readOnto(target);

		Set<String> lexical_anchors = Token_based_lattice_anchors.get_Lexical_Anchors(Onto1, Onto2);
		Set<String> string_equal_anchors = Token_based_lattice_anchors.string_equal_anchros;
	
		Set<String> type1_anchors = new HashSet<>(); //type I anchors(貌似后面没有用到)
		type1_anchors.addAll(string_equal_anchors);

		Set<String> type2_anchors = new HashSet<>(); //type II anchors(貌似后面没有用到)
		type2_anchors.addAll(lexical_anchors);
		type2_anchors.removeAll(string_equal_anchors);
	
		
		ArrayList<OWLAPI_tools> Ontologies = new ArrayList<>();
		Ontologies.add(Onto1);
		Ontologies.add(Onto2);

		Map<String, Map<String, Set<String>>> structure_properity = get_structure_properity(Ontologies);

		Set<String> repaired_alignment = Structural_based_lattice_screen.get_Repaired_Anchors(Ontologies,
				structure_properity, lexical_anchors, string_equal_anchors);
		

		Set<String> improved_alignment = Structural_based_lattice_finder.get_Improved_Alignment(Ontologies,
				structure_properity, repaired_alignment);
		
		//URL result = Tool_functions.get_AlignmentFile(improved_alignment, Onto1, Onto2); //存储匹配的结果
		URL result = Tool_functions.get_AlignmentFile(improved_alignment, Onto1, Onto2); //存储匹配的结果		
		return result;
	}
	
	public URL returnAlignmentFile(String source, String target) throws Exception {
		// TODO Auto-generated method stub	
		OWLAPI_tools Onto1 = new OWLAPI_tools();
		OWLAPI_tools Onto2 = new OWLAPI_tools();
		Onto1.readOnto(source);
		Onto2.readOnto(target);

		Set<String> lexical_anchors = Token_based_lattice_anchors.get_Lexical_Anchors(Onto1, Onto2);
		Set<String> string_equal_anchors = Token_based_lattice_anchors.string_equal_anchros;
	
		Set<String> type1_anchors = new HashSet<>(); //type I anchors(貌似后面没有用到)
		type1_anchors.addAll(string_equal_anchors);

		Set<String> type2_anchors = new HashSet<>(); //type II anchors(貌似后面没有用到)
		type2_anchors.addAll(lexical_anchors);
		type2_anchors.removeAll(string_equal_anchors);
	
		
		ArrayList<OWLAPI_tools> Ontologies = new ArrayList<>();
		Ontologies.add(Onto1);
		Ontologies.add(Onto2);

		Map<String, Map<String, Set<String>>> structure_properity = get_structure_properity(Ontologies);

		Set<String> repaired_alignment = Structural_based_lattice_screen.get_Repaired_Anchors(Ontologies,
				structure_properity, lexical_anchors, string_equal_anchors);
		

		Set<String> improved_alignment = Structural_based_lattice_finder.get_Improved_Alignment(Ontologies,
				structure_properity, repaired_alignment);
		
		//URL result = Tool_functions.get_AlignmentFile(improved_alignment, Onto1, Onto2); //存储匹配的结果
		URL result = Tool_functions.get_AlignmentFile(improved_alignment, Onto1, Onto2); //存储匹配的结果

		return result;
	}
	
	public URL returnAlignmentFile(URL source, URL target,String savePath) throws Exception {
		// TODO Auto-generated method stub	
		OWLAPI_tools Onto1 = new OWLAPI_tools();
		OWLAPI_tools Onto2 = new OWLAPI_tools();
		Onto1.readOnto(source);
		Onto2.readOnto(target);

		Token_based_lattice_anchors.string_equal_anchros.clear();//要预先清除一下
		Token_based_lattice_anchors.label_origin_map.clear();//要预先清除一下
		long tic=System.currentTimeMillis();
		Set<String> lexical_anchors = Token_based_lattice_anchors.get_Lexical_Anchors(Onto1, Onto2);
		
		URL lexical_results = Tool_functions.get_AlignmentFile(lexical_anchors, Onto1, Onto2,savePath+"-step1_lexical");
		Set<String> string_equal_anchors = Token_based_lattice_anchors.string_equal_anchros;
		long toc=System.currentTimeMillis();
		
		System.out.println("The time of lexical anchor is "+ (toc-tic)/1000+"  s");
		
		Set<String> type1_anchors = new HashSet<>(); //type I anchors(貌似后面没有用到)
		type1_anchors.addAll(string_equal_anchors);

		Set<String> type2_anchors = new HashSet<>(); //type II anchors(貌似后面没有用到)
		type2_anchors.addAll(lexical_anchors);
		type2_anchors.removeAll(string_equal_anchors);
	
		
		tic=System.currentTimeMillis();
		ArrayList<OWLAPI_tools> Ontologies = new ArrayList<>();
		Ontologies.add(Onto1);
		Ontologies.add(Onto2);

		Map<String, Map<String, Set<String>>> structure_properity = get_structure_properity(Ontologies);

		Set<String> repaired_alignment = Structural_based_lattice_screen.get_Repaired_Anchors(Ontologies,
				structure_properity, lexical_anchors, string_equal_anchors);
		
		toc=System.currentTimeMillis();
		System.out.println("The time of validated anchor is "+ (toc-tic)/1000+"  s");
		URL validated_results = Tool_functions.get_AlignmentFile(repaired_alignment, Onto1, Onto2,savePath+"-step2_validated");
	    
			
		tic=System.currentTimeMillis();
		Set<String> improved_alignment = Structural_based_lattice_finder.get_Improved_Alignment(Ontologies,
				structure_properity, repaired_alignment);
		
		toc=System.currentTimeMillis();		
		System.out.println("The time of additional anchor is "+ (toc-tic)/1000+"  s");
		
		URL improved_result = Tool_functions.get_AlignmentFile(improved_alignment, Onto1, Onto2,savePath+"-step3_additional");
		
		//URL result = Tool_functions.get_AlignmentFile(improved_alignment, Onto1, Onto2); //存储匹配的结果
		URL result = Tool_functions.get_AlignmentFile(improved_alignment, Onto1, Onto2,savePath); //存储匹配的结果

		//return result;
		return null;
	}

	public static Map<String, Map<String, Set<String>>> get_structure_properity(ArrayList<OWLAPI_tools> Ontologies)
			throws Exception {
		Map<String, Map<String, Set<String>>> results = new HashMap<>();
		String pre[] = { "A", "B" };
		for (int i = 0; i < Ontologies.size(); i++) {
			OWLAPI_tools Onto = Ontologies.get(i);

			//subclass information
			Map<String, Set<String>> Subclass_map = Onto.GetSubclass_Map();
			results.put("sub" + pre[i], Subclass_map);

			//superclass information
			Map<String, Set<String>> Supclass_map = Onto.GetSupclass_Map();
			results.put("sup" + pre[i], Supclass_map);

			//siblingWith information **/
			Map<String, Set<String>> Sib_map = Onto.getSibling_Map();
			results.put("sib" + pre[i], Sib_map);

			//disjoint information
			Map<String, Set<String>> Disjoint_map = Onto.getDisjointwith_Map();
			results.put("dis" + pre[i], Disjoint_map);

			//part of information
			Map<String, Set<String>> Partof_map = Onto.getPartof_Map();
			Set<String> Pat_classe_key = Partof_map.keySet();
			for (String class_name : Pat_classe_key) {
				Set<String> new_part_of = new HashSet<>();
				Set<String> partof_class = Partof_map.get(class_name);
				for (String partof : partof_class) {
					if (Partof_map.get(partof) == null)
						continue;
					new_part_of.addAll(Partof_map.get(partof));
				}
				Partof_map.get(class_name).addAll(new_part_of);
			}
			for (String class_name : Pat_classe_key) {
				Set<String> new_part_of = new HashSet<>();
				Set<String> super_class = Supclass_map.get(class_name);
				for (String supers : super_class) {
					if (Partof_map.get(supers) == null)
						continue;
					new_part_of.addAll(Partof_map.get(supers));
				}
				Partof_map.get(class_name).addAll(new_part_of);
			}
			results.put("pat" + pre[i], Partof_map);
		}

		return results;
	}

}
