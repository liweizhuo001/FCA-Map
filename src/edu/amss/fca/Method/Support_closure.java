package edu.amss.fca.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Support_closure {
		
	public static Set<String> delete(Map<String, Set<String>> negative_unsupport_map,
		Map<String, Set<String>> positive_support_map) throws IOException {
		Set<String> delete = new HashSet<>();
		Map<String, Integer> positvie_degree_map = get_degree(positive_support_map);
		//当不存在矛盾时，或者矛盾没有变化时
		while (negative_unsupport_map.size() > 0) {
			String minial_negative = get_minimal_negative(negative_unsupport_map);//找矛盾最少的匹配
			ArrayList<String> negatives = new ArrayList<>();
			negatives.addAll(negative_unsupport_map.get(minial_negative));//其矛盾集合
			Set<String> positive = positive_support_map.keySet();
			Set<String> delete_this_time = new HashSet<>();

			for (String negative : negatives) {
				Set<String> negative_nes = negative_unsupport_map.get(negative);
				if(negative_nes==null) //可能存在空指针
					continue;
				if (negative_nes.size() > negatives.size()) { //如果它的矛盾S，还有其他匹配与S矛盾，则就把S删掉
					delete.add(negative);//删除 刷新
					updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map, negative);
					delete_this_time.add(negative);
				} else if (negative_nes.size() == negatives.size()) { //如果两个的矛盾集互为彼此，则看支持度
					if (positive.contains(minial_negative) && positive.contains(negative)) {
						double support1 = positvie_degree_map.get(minial_negative);
						double support2 = positvie_degree_map.get(negative);
						if (support1 > support2) {
							delete.add(negative);//删除 刷新
							updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
									negative);
							delete_this_time.add(negative);
						} else if (support1 < support2) {
							delete.add(minial_negative);//删除 刷新
							updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
									minial_negative);
							delete_this_time.add(minial_negative);
						}
						else if(support1 == support2){
							updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
									minial_negative);
							updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
									negative);
						}
					} else if (positive.contains(minial_negative) && !positive.contains(negative)) {
						delete.add(negative);//删除 刷新
						updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
								negative);
						delete_this_time.add(negative);
					} else if (!positive.contains(minial_negative) && positive.contains(negative)) {
						delete.add(minial_negative);//删除 刷新
						updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
								minial_negative);
						delete_this_time.add(minial_negative);
					} else {
						updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
								minial_negative);
						updata_after_delete(negative_unsupport_map, positive_support_map, positvie_degree_map,
								negative);
					}
				}
			}
		}
	
		return delete;

	}

	public static void updata_after_delete(Map<String, Set<String>> negative_unsupport_map,
			Map<String, Set<String>> positive_support_map, Map<String, Integer> positvie_degree_map, String delete)
					throws IOException {
		updata_map(negative_unsupport_map, delete);
		updata_map(positive_support_map, delete);
		
		positvie_degree_map = get_degree(positive_support_map);
	}

	public static void updata_map(Map<String, Set<String>> map, String delete) {
		Set<String> set = map.get(delete); //所有与C矛盾的匹配
		if (set != null) {
			for (String string : set) { //找到每个与C矛盾的匹配的矛盾集合，将C删掉
				Set<String> temp = map.get(string);
				temp.remove(delete);
				if (temp.size() == 0) { //如果删掉C之后，该匹配的矛盾集合为空，则从map中删掉该匹配
					map.remove(string);
				}
			}
			map.remove(delete);
		}
	}

	public static String get_minimal_negative(Map<String, Set<String>> negative_unsupport_map) {
		String minimal_negative = "";
		int count = Integer.MAX_VALUE;
		for (String key : negative_unsupport_map.keySet()) {
			if (negative_unsupport_map.get(key).size() < count) {
				minimal_negative = key;
				count = negative_unsupport_map.get(key).size();
			}
		}
		return minimal_negative;
	}


	//算每个anchor的支持度或不支持度
	public static Map<String, Integer> get_degree(Map<String, Set<String>> support_map) throws IOException {
		Map<String, Integer> degree_map = new HashMap<>();
		for (String string : support_map.keySet()) {
			Set<String> supporters = support_map.get(string);
			int degree = supporters.size();
			degree_map.put(string, degree);
		}
		return degree_map;
	}

	
	public static Map<String, Double> get_support_idf(Map<String, Set<String>> support_map) throws IOException {

		Set<String> support = new HashSet<>();

		support.addAll(support_map.keySet());

		Map<String, Double> positive_idf_map = new HashMap<>();
		//idf
		int total = support.size();
		for (String string : support) {
			double idf = 0;
			Set<String> supporters = support_map.get(string);
			int c = supporters.size();
			idf = Math.log(total / c);
			positive_idf_map.put(string, idf);
		}
		return positive_idf_map;
	}

	
	public static Set<String> split(String string) {
		Set<String> set = new HashSet<>();
		String Temp[] = string.replace("[", "").replace("]", "").split(", ");
		for (int i = 0; i < Temp.length; i++) {
			set.add(Temp[i].trim());
		}

		return set;

	}

	
	
}
