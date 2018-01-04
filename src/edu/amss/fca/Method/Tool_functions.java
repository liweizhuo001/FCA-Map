package edu.amss.fca.Method;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.amss.fca.Tools.OAEIAlignmentOutput;
import edu.amss.fca.Tools.OAEIAlignmentOutputMultiple;
import edu.amss.fca.Tools.OWLAPI_tools;
import fr.labri.galatea.Attribute;
import fr.labri.galatea.BinaryAttribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;


public class Tool_functions {

	
	/** 将概念按两个本体分开，然后组成pre_alignment **/
	public ArrayList<ArrayList<String>> Separate(Set<Entity> Concepts_Set) {
		ArrayList<String> A_concepts = new ArrayList<>();
		ArrayList<String> B_concepts = new ArrayList<>();
		for (Iterator<Entity> iter = Concepts_Set.iterator(); iter.hasNext();) {
			String tem = iter.next().toString();
			if (tem.substring(0, 2).contains("A_")) {
				A_concepts.add(tem);

			} else if (tem.substring(0, 2).contains("B_")) {
				B_concepts.add(tem);
			}
		}
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		result.add(A_concepts);
		result.add(B_concepts);
		return result;
	}

	public Set<String> Combine(ArrayList<ArrayList<String>> A_B_Concept) {
		Set<String> alignment = new HashSet<>();
		ArrayList<String> A_concepts = A_B_Concept.get(0);
		ArrayList<String> B_concepts = A_B_Concept.get(1);

		if ((A_concepts.size() != 0) && (B_concepts.size() != 0)) {
			for (int i = 0; i < A_concepts.size(); i++) {
				for (int j = 0; j < B_concepts.size(); j++) {

					alignment.add(A_concepts.get(i) + " = " + B_concepts.get(j));
				}
			}
		}
		return alignment;
	}

	public ArrayList<ArrayList<String>> Separate_generator(Set<Entity> Concepts_Set,
			Map<String, String> label_origin_map) {

		ArrayList<String> A_concepts = new ArrayList<>();
		ArrayList<String> B_concepts = new ArrayList<>();
		for (Iterator<Entity> iter = Concepts_Set.iterator(); iter.hasNext();) {

			String tem = iter.next().toString();
			if (tem.substring(0, 2).contains("A_")) {
				String origin = label_origin_map.get(tem);
				A_concepts.add(origin);

			} else if (tem.substring(0, 2).contains("B_")) {
				String origin = label_origin_map.get(tem);
				B_concepts.add(origin);

			}
		}
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		result.add(A_concepts);
		result.add(B_concepts);
		return result;
	}

	public Set<String> Separate_Combine(Set<Entity> Concepts_Set, Map<String, String> label_origin_map) {
		Set<String> alignment = new HashSet<>();
		Set<String> A_concepts = new HashSet<>();
		Set<String> B_concepts = new HashSet<>();
		for (Iterator<Entity> iter = Concepts_Set.iterator(); iter.hasNext();) {

			String tem = iter.next().toString();
			if (tem.substring(0, 2).contains("A_")) {
				String origin = label_origin_map.get(tem);
				A_concepts.add(origin);

			} else if (tem.substring(0, 2).contains("B_")) {
				String origin = label_origin_map.get(tem);
				B_concepts.add(origin);

			}
		}
		if ((A_concepts.size() != 0) && (B_concepts.size() != 0)) {
			for (Iterator<String> iter1 = A_concepts.iterator(); iter1.hasNext();) {
				String aString = iter1.next();
				for (Iterator<String> iter2 = B_concepts.iterator(); iter2.hasNext();) {
					alignment.add(aString + " = " + iter2.next());
				}
			}
		}
//		System.out.println(alignment.toString());
		return alignment;
	}

	/** 检查extent中的概念是否同时包含A和B，即是否包含来自两个本体的概念 **/
	public boolean Check_A_B(Set<Entity> Concepts_Set) {
		int A_num = 0;
		int B_num = 0;
		boolean flag = false;
		for (Iterator<Entity> iter = Concepts_Set.iterator(); iter.hasNext();) {

			String tem = iter.next().toString();
			if (tem.substring(0, 2).contains("A_")) {
				A_num++;
			} else if (tem.substring(0, 2).contains("B_")) {
				B_num++;
			}
		}
		if (A_num != 0 && B_num != 0) {
			flag = true;
		}
		return flag;
	}

	public ArrayList<Set<String>> check_A_B(String string[]) {

		Set<String> A = new HashSet<>();
		Set<String> B = new HashSet<>();
		ArrayList<Set<String>> result = new ArrayList<>();
		for (int i = 0; i < string.length; i++) {
			String tem = string[i].trim();
			if (tem.substring(0, 2).contains("A_")) {
				A.add(tem);
			} else if (tem.substring(0, 2).contains("B_")) {
				B.add(tem);
			}
		}
		result.add(A);
		result.add(B);
		return result;
	}

	

	/** 将map根据key进行排序 **/
	public Map<Concept, Integer> sortMap(Map<Concept, Integer> oldMap) {
		ArrayList<Map.Entry<Concept, Integer>> list = new ArrayList<Map.Entry<Concept, Integer>>(oldMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Concept, Integer>>() {

			@Override
			public int compare(Entry<Concept, Integer> arg0, Entry<Concept, Integer> arg1) {
				return arg0.getValue() - arg1.getValue();
			}
		});
		Map<Concept, Integer> newMap = new LinkedHashMap<Concept, Integer>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return newMap;
	}

	/** 将map根据key进行排序 **/
	public static Map<String, Integer> sortMap1(Map<String, Integer> oldMap) {
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
				return arg0.getValue() - arg1.getValue();
			}
		});
		Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return newMap;
	}

	public static Map<String, Double> sortMap2(Map<String, Double> oldMap) {
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(oldMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
				double result = arg0.getValue() - arg1.getValue();
				if (result > 0)
					return 1;
				else if (result == 0)
					return 0;
				else
					return -1;
			}

		});
		Map<String, Double> newMap = new LinkedHashMap<String, Double>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return newMap;
	}
	
	public static Map<String, Set<String>>  sort_map(Map<String, Set<String>> map) {
		Map<String, Integer> map_count = new HashMap<>();
		for (String key : map.keySet()) {
			map_count.put(key, map.get(key).size());
		}
		Map<String, Set<String>> sorted_map = new HashMap<>();
		Map<String, Integer> map_count1 = new HashMap<>();
		map_count1 = Tool_functions.sortMap1(map_count);
		for (String s : map_count1.keySet()) {
			Set<String> negative = map.get(s);
			sorted_map.put(s, negative);
		}
		return sorted_map;
	}

	/** 判断一个字符串中是否包含数字 **/
	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}

	public static String tokeningWord(String str) {
		String s1 = str;
		// s1="Registration_SIGMOD_Member";
		String ss = "";
		for (int i = 0; i < s1.length() - 1; i++) {
			char aa = s1.charAt(i + 1);
			char a = s1.charAt(i);
			if (Character.isUpperCase(a) && i == 0)// 如果首字母是大写则直接添加
			{
				ss = ss + String.valueOf(a);
			} else if (Character.isUpperCase(a) && Character.isLowerCase(aa) && i != 0)// 如果非字母是大写则需要插入分隔符
			{
				ss = ss + " " + String.valueOf(a);
			} else if ((a == '-' || a == '_') || a == '.' || a == ',')// 当出现字符"-","_"
			// 而且后面aa是大写，则不做操作
			{
				// continue;
				ss = ss + " ";// 等于间接将'_','-'进行了替换
			} else if (Character.isUpperCase(a) && Character.isUpperCase(aa)) {
				ss = ss + String.valueOf(a);
			} else if (Character.isLowerCase(a) && Character.isUpperCase(aa))// 前面小写后面接大写
			{
				ss = ss + String.valueOf(a) + " ";
			} else // 其实情况正常添加
			{
				ss = ss + String.valueOf(a);
			}
		}
		ss = ss + s1.charAt(s1.length() - 1);
		ss = ss.replace("  ", " ").trim();
		return ss.toLowerCase().replaceAll("_|-", "");
	}

	public ArrayList<ArrayList<String>> Separate(String string) {
		ArrayList<String> A_concepts = new ArrayList<>();
		ArrayList<String> B_concepts = new ArrayList<>();
		String temp[] = null;
		temp = string.split(", ");
		for (int i = 0; i < temp.length; i++) {
			String tem = temp[i];
			if (tem.substring(0, 2).contains("A_")) {
				A_concepts.add(tem);

			} else if (tem.substring(0, 2).contains("B_")) {
				B_concepts.add(tem);
			}
		}
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		result.add(A_concepts);
		result.add(B_concepts);
		return result;
	}

	//没有disjoint with信息的格
	public Map<String, Set<String>> add_sup_OR_unsup(String[] Intent_split, Map<String, Set<String>> map,
			String corr_) {
		Set<String> con_set = new HashSet<>();
		for (int x = 0; x < Intent_split.length; x++) {
			String string = Intent_split[x].substring(5);
			con_set.add(string);

			if (map.keySet().contains(string)) {
				Set<String> cStrings = map.get(string);
				cStrings.add(corr_);
			} else {
				Set<String> init = new HashSet<>();
				init.add(corr_);
				map.put(string, init);
			}
		}
		if (map.keySet().contains(corr_)) {
			Set<String> cStrings = map.get(corr_);
			cStrings.addAll(con_set);
		} else {
			map.put(corr_, con_set);
		}
		return map;
	}

	//有disjoint with信息的格
	public Map<String, Set<String>> add_sup_OR_unsup(Set<String> Intent_split, Map<String, Set<String>> map,
			String corr_) 
	{
		for (String string : Intent_split) 
		{
			if (map.keySet().contains(string))  //没看出来cStrings有什么用
			{
				Set<String> cStrings = map.get(string);
				cStrings.add(corr_);
			} 
			else 
			{
				Set<String> init = new HashSet<>();
				init.add(corr_);
				map.put(string, init);
			}
		}
		if (map.keySet().contains(corr_)) //没看出来cStrings有什么用
		{
			Set<String> cStrings = map.get(corr_);
			cStrings.addAll(Intent_split);
		} 
		else 
		{
			Set<String> init = new HashSet<>();
			init.addAll(Intent_split);
			map.put(corr_, init);
		}
		/*//修改版(可能会导致  Support_closure 中的delete函数死循环)
		for (String string : Intent_split) 
		{
			if (!map.keySet().contains(string))  
			{
				Set<String> init = new HashSet<>();
				init.add(corr_);
				map.put(string, init);
			}
		}
		if (!map.keySet().contains(corr_))  
		{
			Set<String> init = new HashSet<>();
			init.addAll(Intent_split);
			map.put(corr_, init);
		}*/
		
		return map;
	}

	public static Map<String, Set<String>> getFMA_partof(ArrayList<String> concepts, Map<String, Set<String>> Con_Lab,
			Map<String, ArrayList<String>> Subclass_map, String pathpre) throws IOException {
		BufferedReader All_partof = null;
		BufferedReader origianl_FMA_syns = null;
		BufferedWriter FMA_partof = null;
		BufferedWriter label_ = null;
		try {
			All_partof = new BufferedReader(new FileReader(pathpre + "FMA_original_partof.csv"));
			origianl_FMA_syns = new BufferedReader(new FileReader(pathpre + "FMA_syns.csv"));
			FMA_partof = new BufferedWriter(new FileWriter(pathpre + "FMA_partof.csv"));
			label_ = new BufferedWriter(new FileWriter(pathpre + "label.csv"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Map<String, ArrayList<String>> origianl_partof_map = new HashMap<>();
		String line = null;
		String key_value[] = null;
		String values[] = null;
		while ((line = All_partof.readLine()) != null) {
			key_value = line.split(" --- ");
			String key = key_value[0];
			ArrayList<String> temp = new ArrayList<>();
			if (key_value.length > 1) {
				values = key_value[1].split(", ");
				for (int i = 0; i < values.length; i++) {
					temp.add(values[i]);
				}
			}
			origianl_partof_map.put(key, temp); //都有"_"
		}

		Map<String, String> origianl_syns_label = new HashMap<>();//key:tokened的同义词; value:带"_"的label
		String line1 = null;
		String key_value1[] = null;
		while ((line1 = origianl_FMA_syns.readLine()) != null) {
			key_value1 = line1.split(" --- ");
			String key = key_value1[0];
			String value = key_value1[1];
			origianl_syns_label.put(key, value); //都有"_"
		}
		Map<String, String> oaei_ori = new HashMap<>();//找到oaei中的概念名和原始fma中的概念名的对应
		Map<String, String> ori_oaei = new HashMap<>();//找到fma中的概念名和原始oaei中的概念名的对应
		Set<String> ori = origianl_partof_map.keySet();
		Map<String, String> ori_map = new HashMap<>();//key:没有"_"的 概念名; value:有"_"的
		for (String string : ori) {
			ori_map.put(Tool_functions.tokeningWord(string), string);
		}

		Map<String, String> concept_tokened = new HashMap<>();//key:概念名; value:分词之后的概念名
		for (String con : concepts) {
			String tokened_con = tokeningWord(con);
			concept_tokened.put(tokened_con, con);
			if (ori_map.containsKey(tokened_con)) { //如果原始fma有oaei中的概念名con
				String ori_ = ori_map.get(tokened_con);
				oaei_ori.put(con, ori_); //都是有"_"的 概念名
				ori_oaei.put(ori_, con); //都是有"_"的 概念名
			} else {//如果原始fma没有oaei中的概念名con，则去掉con中的连字符，如果还没有则看label
				Set<String> labels = Con_Lab.get(con);
				for (String label : labels) {
					String tokened_label = tokeningWord(label);
					if (ori_map.containsKey(tokened_label)) {
						String ori_ = ori_map.get(tokened_label);
						oaei_ori.put(con, ori_);
						ori_oaei.put(ori_, con);
						break;
					}
				}
			}
		}

		Map<String, Set<String>> partof_map = new HashMap<>();

		for (String string : concepts) {
//			System.out.println(string);
			String tokened_string = tokeningWord(string);
			ArrayList<String> partof = origianl_partof_map.get(string);
			if (partof == null) {
				String concept = oaei_ori.get(string);
				partof = origianl_partof_map.get(concept);
				if (partof == null) {
					String concept1 = origianl_syns_label.get(tokened_string);
					partof = origianl_partof_map.get(concept1);
					if (partof == null) {
						Set<String> oaei_labels = Con_Lab.get(string);
//						if(string.equals("Subdivision_of_inferior_frontal_gyrus")){
//							System.out.println(oaei_labels.toString());
//							System.out.println("!!!");
//						}
						for (String string2 : oaei_labels) {
							String concept2 = origianl_syns_label.get(tokeningWord(string2));
							partof = origianl_partof_map.get(concept2);
//							break;
							if (partof != null)
								break;
						}
						if (partof == null) {
//							System.out.println("!!!!" + string);
							ArrayList<String> temp = new ArrayList<>();
							partof = temp;
						}
					}
				}
			}
			Set<String> partof_oaei = new HashSet<>();
			for (String string2 : partof) {
				if (concepts.contains(string2)) {
					partof_oaei.add(string2);
				} else if (oaei_ori.containsValue(string2)) {
					partof_oaei.add(ori_oaei.get(string2));
				}
			}
			ArrayList<String> tokened_set = new ArrayList<>();
			tokened_set.add(tokened_string);
			partof_map.put(string, partof_oaei);
			FMA_partof.write(string + " --- " + partof_map.get(string) + "\n");
		}

		for (String string : concepts) {
			String tokened_string = tokeningWord(string);
			if (tokened_string.contains("subdivision of")) {
				String part_tokened = tokened_string.replace("subdivision of", "").trim();
//				System.out.println(part_tokened);
				String part = concept_tokened.get(part_tokened);
//				System.out.println("1" + string + "--" + part);
				if (part != null) {
					ArrayList<String> subclass = Subclass_map.get(string);
					for (String string2 : subclass) {
						Set<String> partof1 = partof_map.get(string2);
						if (partof1 != null) {
							partof1.add(part);
//							System.out.println("1" + string2 + "--" + part);
						}
					}
				}
			} else {
				Set<String> labels = Con_Lab.get(string);
				for (String label : labels) {
					String tokened_label = tokeningWord(label);
					if (tokened_label.contains("subdivision of")) {
						String part_tokened = tokened_label.replace("subdivision of", "").trim();
//						System.out.println(part_tokened);
						String part = concept_tokened.get(part_tokened);
//						System.out.println("2" + string + "--" + part);
						if (part != null) {
							ArrayList<String> subclass = Subclass_map.get(string);
//							System.out.println("!!!"+string);
							for (String string2 : subclass) {
								Set<String> partof1 = partof_map.get(string2);
								if (partof1 != null) {
									partof1.add(string);
//									System.out.println("2" + string2 + "--" + string);
								}
							}
						}
					}
				}
			}
		}

		for (String string : partof_map.keySet()) {
			FMA_partof.write(string + " --- " + partof_map.get(string) + "\n");
		}
		FMA_partof.close();
		label_.close();
		All_partof.close();
		return partof_map;
	}

	//获得匹配闭包
	public Set<String> get_corre_closure(Map<String, ArrayList<String>> corres_map, String concept, Set<String> results,
			Set<String> All_concepts) {
		All_concepts.remove(concept);
//			System.out.println("concept=" + concept);
		ArrayList<String> corrs = new ArrayList<>();
		corrs.addAll(corres_map.get(concept));

		corrs.retainAll(All_concepts);
		if (corrs.size() == 0) {
//				System.out.println("为0！！");
//				new_results.addAll(results);
		} else {
			for (String corr : corrs) {
				if (All_concepts.contains(corr)) {
					if (concept.substring(0, 2).contains("A_")) {
						results.add(concept + " = " + corr);
					} else {
						results.add(corr + " = " + concept);
					}
//						System.out.println("results" + results.toString());
					All_concepts.remove(corr);
					results.addAll(get_corre_closure(corres_map, corr, results, All_concepts));
//						new_results.addAll(results);
				} else
					continue;
			}
		}
		return results;
	}

	public Map<String, Set<Concept>> getLeaf_Single(Set<Concept> Set_S, Tool_functions tool, Concept TopConcept) {
		/** 对集合S中的formal concept进行分类：leaf，single和top **/
		System.out.println("==Set_S的大小：" + Set_S.size());

		Set<Concept> Leaf_Set = new HashSet<>();// 叶子集合

		Set<Concept> Top_Set = new HashSet<>();// 顶点集合

		Set<Concept> Single_Set = new HashSet<>();// 单点集合

		for (Concept TEMP : Set_S) {
			int children_num = 0;
			int parents_num = 0;
			Set<Concept> children = TEMP.getChildren();
			Set<Concept> parents = TEMP.getParents();
			// 遍历所有的直接孩子，删掉不在集合S中的孩子
			for (Concept temp1 : children) {
				Set<Entity> temp_entity = temp1.getExtent();
				if (temp_entity.size() < 1) {
//						System.out.println("2=========="+temp1.toString());
					TEMP.removeChild(temp1);
					temp1.removeParent(TEMP);
				} else if (!tool.Check_A_B(temp_entity)) {
					TEMP.removeChild(temp1);
					temp1.removeParent(TEMP);
//						if (temp1.getIntent().toString().contains("[chin]")) {
//							System.out.println("child:" + temp1.toString());
//							System.out.println("parent:" + TEMP.toString());
//							System.out.println("获得的" + TEMP.getChildren().toString());
//						}

//						System.out.println("1=========="+temp1.toString());
				} else {
					children_num++;
				}
			}

			parents_num = TEMP.getParents().size();
			if (parents.contains(TopConcept)) {
				parents_num--;
//					TEMP.removeParent(TopConcept);
			}
			if (children_num == 0 && parents_num == 0) {
				Single_Set.add(TEMP);
			} else if (parents_num == 0) {
				Top_Set.add(TEMP);
			} else if (children_num == 0) {
				Leaf_Set.add(TEMP);
			}
		}
//			s_set.close();
		Map<String, Set<Concept>> results = new HashMap<>();
		System.out.println("==leaf:" + Leaf_Set.size());
		results.put("Leaf", Leaf_Set);
		System.out.println("==top:" + Top_Set.size());
		results.put("Top", Top_Set);
		System.out.println("==single:" + Single_Set.size());
		results.put("Single", Single_Set);
		return results;
	}

	public void get_In_close2(Context context, BufferedWriter In_close2) throws IOException {
		int new_a_n = context.getAttributeNb();
		int new_e_n = context.getEntityNb();
		In_close2.write("B" + "\n\n");
		In_close2.write(new_e_n + "\n");
		In_close2.write(new_a_n + "\n\n");

		Set<Entity> all_entity = context.getEntities();
		ArrayList<Entity> allEntity = new ArrayList<>();
		for (Entity e : all_entity) {
			allEntity.add(e);
			In_close2.write(e.getName() + "\n");
		}
		System.out.println(allEntity.size());

		Set<Attribute> all_attr = context.getAttributes();
		ArrayList<Attribute> allAttr = new ArrayList<>();
		for (Attribute a : all_attr) {
			allAttr.add(a);
			In_close2.write(a.toString() + "\n");
		}
		System.out.println(allAttr.size());

		for (int i = 0; i < new_e_n; i++) {
			Entity e = allEntity.get(i);
			for (int j = 0; j < new_a_n; j++) {
				Attribute a = allAttr.get(j);
				if (context.hasPair(e, a)) {
					In_close2.write("X");
				} else
					In_close2.write(".");
			}
			In_close2.write("\n");
		}
		In_close2.close();
	}

	public Context Transform_Context(Context c) {
		Context new_c = new Context();
		Set<Entity> all_entity = c.getEntities();
		Map<String, String> entity_map = new HashMap<>();//key:编号    value:概念名称
		Map<Entity, Entity> entity_map1 = new HashMap<>();//key:原对象    value:编号对象
		int index_e = 1;
		int i = 0;
		for (Entity e : all_entity) {
//				System.out.println(e.toString());
			i = index_e;
			String index = String.valueOf(i);
//				System.out.println(index);
			Entity entity = new Entity(index);
			entity_map.put(index, e.toString());
			entity_map1.put(e, entity);
			new_c.addEntity(entity);
			index_e++;
		}

		Set<Attribute> all_attr = c.getAttributes();

		int index_a = 1;
		int j = 0;
		Map<String, String> attribute_map = new HashMap<>();//key:编号    value:属性
		for (Attribute a : all_attr) {
			j = index_a;
			Attribute attribute = new BinaryAttribute(String.valueOf(j));
			attribute_map.put(String.valueOf(j), a.toString());
			new_c.addAttribute(attribute);
			Set<Entity> entities = c.getEntities(a);
			for (Entity e : entities) {
				new_c.addPair(entity_map1.get(e), attribute);
			}
			index_a++;
		}

		System.out.println("new context: ");
		System.out.println("Intent= " + new_c.getAttributeNb());
		System.out.println("Extent= " + new_c.getEntityNb());
		System.out.println("Pair= " + new_c.getPairNb());
		return new_c;
	}


	public static void get_results(ArrayList<ArrayList<String>> initial_match, Set<String> alignment,
			BufferedWriter Pre_Alignment_Left, BufferedWriter Common_All, BufferedWriter Reference_left)
					throws IOException {
		ArrayList<ArrayList<String>> initial_match1 = new ArrayList<>();
		initial_match1.addAll(initial_match);
		Set<String> alignment1 = new HashSet<>();
		alignment1.addAll(alignment);
		// 等价的
		ArrayList<String> reference_equivalent_string = initial_match1.get(0);
		// 不确定的
		ArrayList<String> reference_unknown_string = initial_match1.get(1);
		// 全部的
		ArrayList<String> reference_all = new ArrayList<>();
		reference_all.addAll(reference_equivalent_string);
		reference_all.addAll(reference_unknown_string);

		int equivalent_refer_num = reference_equivalent_string.size();

		int unknown_refer_num = reference_unknown_string.size();
		System.out.println("Reference 等价对数=" + equivalent_refer_num + "; 不确定对数=" + unknown_refer_num);
		int all_refer = reference_all.size();
		System.out.println("Reference 所有对数=" + all_refer);

		int Pre_align_num = alignment1.size();
		ArrayList<String> common_all_equivalent = new ArrayList<>();
		common_all_equivalent.addAll(reference_equivalent_string);
		common_all_equivalent.retainAll(alignment1);
		int common_num_equivalent = common_all_equivalent.size();
		float precise_equivalent = (float) common_num_equivalent / (float) Pre_align_num;
		float recall_equivalent = (float) common_num_equivalent / (float) reference_equivalent_string.size();
		System.out.println("等价的正确的对数=" + common_num_equivalent);
		System.out.println("等价匹配的P=" + precise_equivalent);
		System.out.println("等价匹配的C=" + recall_equivalent);

		ArrayList<String> common_all = new ArrayList<>();
		common_all.addAll(reference_all);
		common_all.retainAll(alignment1);
		int common_num1 = common_all.size();
		float common_precise1 = (float) common_num1 / (float) Pre_align_num;
		float recall = (float) common_num1 / (float) all_refer;
		float f = (2 * (float) common_precise1 * (float) recall) / ((float) common_precise1 + (float) recall);

		System.out.println("全部的正确的对数=" + common_num1);
		System.out.println("全部的P=" + common_precise1);

		System.out.println("全部的C=" + recall);
		System.out.println("f=" + f);

		System.out.println("Size of Pre_Alignment = " + Pre_align_num);
		System.out.println("Pre_Alignment中正确的对数=" + common_all.size());

		alignment1.removeAll(common_all);
		System.out.println("Pre_Alignment中错误的对数=" + alignment.size());
		for (String s : alignment1) {
//			System.out.println(s);
			Pre_Alignment_Left.write(s + "\n");
		}
		Pre_Alignment_Left.close();

		for (int i = 0; i < common_all.size(); i++) {
			String TEMP = common_all.get(i);
			Common_All.write(TEMP + "\n");
		}
		Common_All.close();

		reference_all.removeAll(common_all);
		System.out.println("没有找出来的对数=" + reference_all.size());
		for (int i = 0; i < reference_all.size(); i++) {
			String TEMP = reference_all.get(i);
			Reference_left.write(TEMP + "\n");
		}
		Reference_left.close();
	}

	public static URL get_AlignmentFile (Set<String> alignment, ArrayList<OWLAPI_tools> Ontologies,String index)throws Exception 
	{
		ArrayList<Map<String, String>> ontoIRIMaps=new ArrayList<Map<String, String>>();
		for(OWLAPI_tools onto:Ontologies)
		{
			ontoIRIMaps.add(onto.getIRIMap());
		}
		OAEIAlignmentOutputMultiple out = new OAEIAlignmentOutputMultiple("alignment/Mutiple/"+index, ontoIRIMaps);
		//OAEIAlignmentOutputMultiple out = new OAEIAlignmentOutputMultiple("alignment/alignment123", ontoIRIMaps);
//		OAEIAlignmentOutput out = new OAEIAlignmentOutput("alignment");
		for (String string : alignment) {
			String parts[] = string.split(" ");  //实体的分割符号
			//System.out.println(Alignments.get(i));
			//String A = parts[0].substring(2);
			//String B = parts[1].substring(2);
			out.addMapping2Output(parts, "1");
		}
		out.saveOutputFile();
		return out.returnAlignmentFile();
	}
	
	
	public static URL get_AlignmentFile(Set<String> alignment, OWLAPI_tools Onto1, OWLAPI_tools Onto2)
			throws Exception {
		/*String alignmentPath="OM_SPN/Results_rdf/"+ontologyName1+"-"+ontologyName2;
		OAEIAlignmentOutput out=new OAEIAlignmentOutput(alignmentPath,ontologyName1,ontologyName2);*/
		OAEIAlignmentOutput out = new OAEIAlignmentOutput("alignment/alignment123", Onto1.getIRIMap(), Onto2.getIRIMap());
//		OAEIAlignmentOutput out = new OAEIAlignmentOutput("alignment");
		for (String string : alignment) {
			String parts[] = string.split(" = ");
			//System.out.println(Alignments.get(i));
			String A = parts[0].substring(2);
			String B = parts[1].substring(2);
			out.addMapping2Output(A, B, "1");
		}
		out.saveOutputFile();
		return out.returnAlignmentFile();
	}
	
	public static URL get_AlignmentFile(Set<String> alignment, OWLAPI_tools Onto1, OWLAPI_tools Onto2,String savePath)
			throws Exception {
		/*String alignmentPath="OM_SPN/Results_rdf/"+ontologyName1+"-"+ontologyName2;
		OAEIAlignmentOutput out=new OAEIAlignmentOutput(alignmentPath,ontologyName1,ontologyName2);*/
		OAEIAlignmentOutput out = new OAEIAlignmentOutput(savePath, Onto1.getIRIMap(), Onto2.getIRIMap());
//		OAEIAlignmentOutput out = new OAEIAlignmentOutput("alignment");
		for (String string : alignment) {
			String parts[] = string.split(" = ");
			//System.out.println(Alignments.get(i));
			String A = parts[0].substring(2);
			String B = parts[1].substring(2);
			out.addMapping2Output(A, B, "1");
		}
		out.saveOutputFile();
		return out.returnAlignmentFile();
	}
	
	public static Map<String, ArrayList<String>> Transform(Map<String, ArrayList<String>> Con_list,
			Map<String, String> Con_lab) {
		Map<String, ArrayList<String>> Con_list_new = new HashMap<>();
		for (String key : Con_list.keySet()) {
			String new_key = Con_lab.get(key);
//			System.out.println(new_key);
			ArrayList<String> list = Con_list.get(key);
			ArrayList<String> new_list = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				new_list.add(Con_lab.get(list.get(i)));
			}
//			System.out.println(new_list.toString());
			Con_list_new.put(new_key, new_list);
		}
		return Con_list_new;
	}

	public static Map<String, Set<String>> Transform_set(Map<String, Set<String>> Con_list,
			Map<String, String> Con_lab) {
		Map<String, Set<String>> Con_list_new = new HashMap<>();
		for (String key : Con_list.keySet()) {
			String new_key = Con_lab.get(key);
//			System.out.println(new_key);
			Set<String> list = Con_list.get(key);
			Set<String> new_list = new HashSet<>();
			for (String string : list) {
				new_list.add(Con_lab.get(string));
			}
//			System.out.println(new_list.toString());
			Con_list_new.put(new_key, new_list);
		}
		return Con_list_new;
	}

}
