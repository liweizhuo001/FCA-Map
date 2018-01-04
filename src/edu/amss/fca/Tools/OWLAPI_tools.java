package edu.amss.fca.Tools;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.semanticweb.HermiT.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import gov.nih.nlm.nls.lvg.Api.NormApi;
import gov.nih.nlm.nls.lvg.Lib.LexItem;
import uk.ac.manchester.cs.owl.owlapi.OWLAnonymousIndividualImpl;

public class OWLAPI_tools {
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	OWLOntology onto;
	String OntoID;
	OWLReasoner hermit;
	OWLDataFactory fac = manager.getOWLDataFactory();
	Map<String, String> Name_IRI_Map=new HashMap<>();//用来存name和其IRI
	// String IndividualID="";
	// String IndividualID="http://xmlns.com/foaf/0.1";
	public void readOnto(String path) {
		try {
			File file = new File(path);
			manager.setSilentMissingImportsHandling(true);
			onto = manager.loadOntologyFromOntologyDocument(file);
			hermit = new Reasoner.ReasonerFactory().createReasoner(onto);// 调用hermit推理机，但要导入org.semanticweb.HermiT.*;
//						      hermit=new Reasoner(onto);

			OWLOntologyID ontologyIRI = onto.getOntologyID();
			OntoID = ontologyIRI.getOntologyIRI().toString();
			System.out.println("Load ontology sucessfully!");
			IRI documentIRI = manager.getOntologyDocumentIRI(onto);
			System.out.println("The path comes from " + documentIRI);// 获取文件的相对路径
			System.out.println("The OntoID is " + OntoID);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.out.println("cuowu");
			e.printStackTrace();
		}
	}

	public OWLOntology readOnto_wrap(String path) {
		try {
			File file = new File(path);
			manager.setSilentMissingImportsHandling(true);
			onto = manager.loadOntologyFromOntologyDocument(file);
			hermit = new Reasoner.ReasonerFactory().createReasoner(onto);// 调用hermit推理机，但要导入org.semanticweb.HermiT.*;
			//			      hermit=new Reasoner(onto);
			OWLOntologyID ontologyIRI = onto.getOntologyID();
			OntoID = ontologyIRI.getOntologyIRI().toString();
			System.out.println("Load ontology sucessfully!");
			IRI documentIRI = manager.getOntologyDocumentIRI(onto);
			System.out.println("The path comes from " + documentIRI);// 获取文件的相对路径
			System.out.println("The OntoID is " + OntoID);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.out.println("cuowu");
			e.printStackTrace();
		}
		return onto;
	}

	public void readOnto(URL url) throws URISyntaxException {
		try {
			manager.setSilentMissingImportsHandling(true);
			// OntoID="file:Datasets/Instance_ontologys/sourceIn.ttl";
			onto = manager.loadOntology(IRI.create(url));
			hermit = new Reasoner.ReasonerFactory().createReasoner(onto);
//			OWLOntologyID ontologyIRI = onto.getOntologyID();
//			if(ontologyIRI!=null)
//				OntoID = ontologyIRI.getOntologyIRI().toString();
			System.out.println("Load ontology sucessfully!");
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.out.println("cuowu");
			e.printStackTrace();
		}
	}

//	public String getURI() {
//		return OntoID.replace("#", "");
//	}
	
	public Map<String, String> getIRIMap() {
		return Name_IRI_Map;
	}
	
	public void isConsistent() // 嵌入推理机来判断是否一致
	{
		// hermit.isConsistent();
		System.out.println("The ontology is " + hermit.isConsistent());
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 * reasoner.precomputeInferences(); boolean consistent =
		 * reasoner.isConsistent(); System.out.println("Consistent: " +
		 * consistent);
		 */
		System.out.println("\n");
	}

	public ArrayList<String> getConcepts() {
		ArrayList<String> classes = new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			// System.out.println(c.getIRI().getFragment());
			String concept = c.getIRI().getFragment();
			if (concept != null) {
				if (concept.charAt(0) == '_')
					concept = concept.replaceFirst("_", "");
				if (concept.equals("Thing") || concept.equals("Nothing"))// Thing的情况就不考虑了
					continue;
				else if (concept.equals("DbXref") || concept.equals("Subset") || concept.equals("Synonym")
						|| concept.equals("ObsoleteClass") || concept.equals("SynonymType")
						|| concept.equals("Definition"))
					continue;
				else if (concept.equals("decimal") || concept.equals("nonNegativeInteger"))
					continue;
				else if (!classes.contains(concept))
					classes.add(concept);
			}
		}
		return classes;
	}

	public ArrayList<String> getConceptAnnoations() {
		ArrayList<String> Annoations = new ArrayList<String>();
		// OWLDataFactory fac = manager.getOWLDataFactory();
		for (OWLClass c : onto.getClassesInSignature()) {
			String a = c.getIRI().getFragment();
			if (a != null) {
				if (a.charAt(0) == '_')
					a = a.replaceFirst("_", "");
				if (a.equals("Nothing") || a.equals("Thing"))// 常规本体
					continue;
				else if (a.equals("DbXref") || a.equals("Subset") || a.equals("Synonym") || a.equals("ObsoleteClass")
						|| a.equals("SynonymType") || a.equals("Definition"))
					continue;
				String label = null;
				for (OWLAnnotation anno : c.getAnnotations(onto, fac.getRDFSLabel())) {
					if (anno.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) anno.getValue();
						// if(val.hasLang("pt"))
						label = val.getLiteral();
						// System.out.println(c+" labelled "+val.getLiteral());
					}
				}
				String comment = null;
				for (OWLAnnotation comm : c.getAnnotations(onto, fac.getRDFSComment())) {
					if (comm.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) comm.getValue();
						// if(val.hasLang("pt"))
						comment = val.getLiteral();
						// System.out.println(c+" comment is  "+val.getLiteral());
					}
				}

				if (label != null && !label.equals(a))// 存在自己等于自己的label
					Annoations.add(a + "--" + label);
				else if (label != null && comment != null && !comment.equals("")) {
					Annoations.add(a + "--" + comment.trim());
				}
			}
		}
		return Annoations;
	}

	//获得map:概念名-lable
	public Map<String, String> getConcept_Annoations() {
		Map<String, String> Con_Lab = new HashMap<>();

		// OWLDataFactory fac = manager.getOWLDataFactory();
		for (OWLClass c : onto.getClassesInSignature()) {
			String a = c.getIRI().getFragment();
			if (a != null) {
				if (a.charAt(0) == '_')
					a = a.replaceFirst("_", "");
				if (a.equals("Nothing") || a.equals("Thing"))// 常规本体
					continue;
				else if (a.equals("DbXref") || a.equals("Subset") || a.equals("Synonym") || a.equals("ObsoleteClass")
						|| a.equals("SynonymType") || a.equals("Definition"))
					continue;
				ArrayList<String> labels = new ArrayList<>();
				String label = null;
				for (OWLAnnotation anno : c.getAnnotations(onto, fac.getRDFSLabel())) {
					if (anno.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) anno.getValue();
						// if(val.hasLang("pt"))
						label = val.getLiteral();
						if (label != null && !label.equals(a)) {
							labels.add(label);
						}
						// System.out.println(c+" labelled "+val.getLiteral());
					}
				}
//				String comment = null;
//				for (OWLAnnotation comm : c.getAnnotations(onto, fac.getRDFSComment())) {
//					if (comm.getValue() instanceof OWLLiteral) {
//						OWLLiteral val = (OWLLiteral) comm.getValue();
//						// if(val.hasLang("pt"))
//						comment = val.getLiteral();
//						// System.out.println(c+" comment is  "+val.getLiteral());
//					}
//				}
//				if (label != null && !label.equals(a))// 存在自己等于自己的label
//				{
//					Con_Lab.put(a, label);
//				} else if (label != null && comment != null && !comment.equals("")) {
//					Con_Lab.put(a, comment.trim());
//				}
//				if(labels.size()>0){
				Con_Lab.put(a, labels.toString().replace("[", "").replace("]", ""));
//				}
			}
		}
		return Con_Lab;
	}

	/**获得标准化的label，如果label标准化之后的token集合和概念名称的token集合一样的话，就不要这个label了，即token的顺序不一样，但是内容一样就不予考虑**/
	public Map<String, Set<String>> getConcept_AnnoationsSet() throws Exception {
		Map<String, Set<String>> Con_Lab = new HashMap<>();
		NormApi norm = new NormApi();
		// OWLDataFactory fac = manager.getOWLDataFactory();
		for (OWLClass c : onto.getClassesInSignature()) {
			String a = c.getIRI().getFragment();
			LexItem items = new LexItem(tokeningWord(a));
			Vector<LexItem> listStr = norm.Mutate(items);
			if (a != null) {
				if (a.charAt(0) == '_')
					a = a.replaceFirst("_", "");
				if (a.equals("Nothing") || a.equals("Thing"))// 常规本体
					continue;
				else if (a.equals("DbXref") || a.equals("Subset") || a.equals("Synonym") || a.equals("ObsoleteClass")
						|| a.equals("SynonymType") || a.equals("Definition"))
					continue;
				Set<String> labels = new HashSet<>();
				String label = null;
				for (OWLAnnotation anno : c.getAnnotations(onto, fac.getRDFSLabel())) {
					if (anno.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) anno.getValue();
						// if(val.hasLang("pt"))
						label = val.getLiteral().trim().replace("_", " ").replace("-", " ");
						LexItem items1 = new LexItem(tokeningWord(label));
						Vector<LexItem> listStr1 = norm.Mutate(items1);
						if (label != null && !label.equals(a) && listStr1.size() != 0 && listStr.size() != 0
								&& !listStr1.get(listStr1.size() - 1).GetTargetTerm()
										.equals(listStr.get(listStr.size() - 1).GetTargetTerm())) {
							labels.add(label);
						} else if (label != null && !label.equals(a) && listStr.size() == 0) {
							labels.add(label);
						}
//						if (label != null && !label.equals(a)) {
//							labels.add(label);
//						}
						// System.out.println(c+" labelled "+val.getLiteral());
					}
				}
				String comment = null;
				for (OWLAnnotation comm : c.getAnnotations(onto, fac.getRDFSComment())) {
					if (comm.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) comm.getValue();
						// if(val.hasLang("pt"))
						LexItem items1 = new LexItem(tokeningWord(label));
						Vector<LexItem> listStr1 = norm.Mutate(items1);
						comment = val.getLiteral();
						// System.out.println(c+" comment is  "+val.getLiteral());
						if (comment != null && !comment.equals("") && listStr1.size() != 0 && listStr.size() != 0
								&& !listStr1.get(listStr1.size() - 1).GetTargetTerm()
										.equals(listStr.get(listStr.size() - 1).GetTargetTerm())) {
							labels.add(comment.trim());
						} else if (comment != null && !comment.equals("") && listStr.size() != 0) {
							labels.add(comment.trim());
						}
					}
				}
//				if (label != null && !label.equals(a))// 存在自己等于自己的label
//				{
//					Con_Lab.put(a, label);
//				} else if (label != null && comment != null && !comment.equals("")) {
//					Con_Lab.put(a, comment.trim());
//				}
//				if(labels.size()>0){
				Con_Lab.put(a, labels);
//				}
			}
		}
		return Con_Lab;
	}

	/**获得每个概念的label和syns**/
	public Map<String, Set<String>> getConcept_AllAnnoationsSet() throws Exception {
		Map<String, Set<String>> Con_Lab = new HashMap<>();
		String lvgConfigFile = "Lexical_Tools/data/config/lvg.properties";
		NormApi norm = new NormApi(lvgConfigFile);
		for (OWLClass c : onto.getClassesInSignature()) {
//			System.out.println("== "+c.getIRI());
			IRI iri = c.getIRI();
			String a = getIriName(iri);
			
			if (a != null) {
				
//				System.out.println(a+"--" +iri.toString());
				LexItem items = new LexItem(tokeningWord(a));
				Vector<LexItem> listStr = norm.Mutate(items);
				if (listStr.size()==0)
					continue;
//				System.out.println(listStr.get(0).GetTargetTerm());
				String class_name = listStr.get(listStr.size() - 1).GetTargetTerm();
//				String class_name = listStr.get(0).GetTargetTerm();
				if (a.charAt(0) == '_')
					a = a.replaceFirst("_", "");
				if (a.equals("Nothing") || a.equals("Thing"))// 常规本体
					continue;
				else if (a.equals("DbXref") || a.equals("Subset") || a.equals("Synonym") || a.equals("ObsoleteClass")
						|| a.equals("SynonymType") || a.equals("Definition"))
					continue;
				
				Name_IRI_Map.put(a, iri.toString());
				
				Set<String> labels = new HashSet<>();
				String label = null;
				
//				System.out.println("== " + a);
				//将norm之后的token存起来，防止之后的label或者syn跟之前的重复
				Set<String> appeared = new HashSet<>();
				appeared.add(class_name);
				for (OWLAnnotationAssertionAxiom annotations : c.getAnnotationAssertionAxioms(onto)) //oboInOwl:hasRelatedSynonym的解析，也包含label的抽取。
				{

					if (annotations.getValue() instanceof OWLAnonymousIndividualImpl) //找到http://human.owl#genid4450 对应的具体Value
					{
//						String type = annotations.getProperty().getIRI().getFragment();
						String type = getIriName(annotations.getProperty().getIRI());
						if (type.contains("hasRelatedSynonym")) {
							OWLAnonymousIndividualImpl am = (OWLAnonymousIndividualImpl) annotations.getValue();
							// am.getDataPropertyValues(onto);
							// OWLAnonymousIndividual an = am.asOWLAnonymousIndividual();
							for (OWLAnnotationAssertionAxiom axiom : onto.getAnnotationAssertionAxioms(am)) {
//							System.out.println(a.getValue()); //获取同义词的值
								OWLLiteral syn = (OWLLiteral) axiom.getValue();
								label = syn.getLiteral();
							}
						}
					} else {
//						String type = annotations.getProperty().getIRI().getFragment();
						String type = getIriName(annotations.getProperty().getIRI());
//						System.out.println(type);
						if (type.contains("label") || type.contains("hasExactSynonym")
								|| type.contains("hasBroadSynonym") || type.contains("hasNarrowSynonym")
								|| type.contains("hasRelatedSynonym") || type.contains("alternative_term")) {
							OWLLiteral val = (OWLLiteral) annotations.getValue(); //获取label的值
							label = val.getLiteral();
						}
					}

					//判断label或syn是不是和class name一样
					if (label != null && label.length() != 0) {
//						System.out.println("*" + label + "*");
						LexItem items1 = new LexItem(tokeningWord(label));
						Vector<LexItem> listStr1 = norm.Mutate(items1);
						if (listStr1.size() > 0) {
							String label_or_syn = listStr1.get(listStr1.size() - 1).GetTargetTerm();
							if (label != null && !appeared.contains(label_or_syn)) {
								labels.add(label);
								appeared.add(label);
							}
						} else if (label != null && listStr1.size() == 0) {
							labels.add(label);
						}
//						labels.add(label);
						label = null;
					}
				}

				Con_Lab.put(a, labels);
			}
		}
		norm.CleanUp();
		return Con_Lab;
	}

	public String getIriName(IRI iri) {
		String result = iri.getFragment();
		if(result==null){
			String prefix = iri.getStart();
			result = iri.toString().replace(prefix, "");
		}
		return result;
	}
	
	public Map<String, Set<String>> getConcept_concept() throws Exception {
		Map<String, Set<String>> Con_Lab = new HashMap<>();
		NormApi norm = new NormApi();
		// OWLDataFactory fac = manager.getOWLDataFactory();
		for (OWLClass c : onto.getClassesInSignature()) {
			String a = c.getIRI().getFragment();
			LexItem items = new LexItem(tokeningWord(a));
			Vector<LexItem> listStr = norm.Mutate(items);
			if (a != null) {
				if (a.charAt(0) == '_')
					a = a.replaceFirst("_", "");
				if (a.equals("Nothing") || a.equals("Thing"))// 常规本体
					continue;
				else if (a.equals("DbXref") || a.equals("Subset") || a.equals("Synonym") || a.equals("ObsoleteClass")
						|| a.equals("SynonymType") || a.equals("Definition"))
					continue;
				Set<String> labels = new HashSet<>();
				labels.add(a);
				Con_Lab.put(a, labels);
			}
		}
		return Con_Lab;
	}

	public Map<String, Set<String>> getConcept_AnnoationsSet_origianl() throws Exception {
		Map<String, Set<String>> Con_Lab = new HashMap<>();
		NormApi norm = new NormApi();
		// OWLDataFactory fac = manager.getOWLDataFactory();
		for (OWLClass c : onto.getClassesInSignature()) {
			String a = c.getIRI().getFragment();
			LexItem items = new LexItem(tokeningWord(a));
			Vector<LexItem> listStr = norm.Mutate(items);
			if (a != null) {
				if (a.charAt(0) == '_')
					a = a.replaceFirst("_", "");
				if (a.equals("Nothing") || a.equals("Thing"))// 常规本体
					continue;
				else if (a.equals("DbXref") || a.equals("Subset") || a.equals("Synonym") || a.equals("ObsoleteClass")
						|| a.equals("SynonymType") || a.equals("Definition"))
					continue;
				Set<String> labels = new HashSet<>();
				String label = null;
				for (OWLAnnotation anno : c.getAnnotations(onto, fac.getRDFSLabel())) {
					if (anno.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) anno.getValue();
						// if(val.hasLang("pt"))
						label = val.getLiteral().trim().replace("_", " ").replace("-", " ");
						if (label != null && !label.equals(a)) {
							labels.add(label);
						} else if (label != null && !label.equals(a) && listStr.size() == 0) {
							labels.add(label);
						}
//						if (label != null && !label.equals(a)) {
//							labels.add(label);
//						}
						// System.out.println(c+" labelled "+val.getLiteral());
					}
				}
				String comment = null;
				for (OWLAnnotation comm : c.getAnnotations(onto, fac.getRDFSComment())) {
					if (comm.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) comm.getValue();
						// if(val.hasLang("pt"))
						comment = val.getLiteral();
						// System.out.println(c+" comment is  "+val.getLiteral());
						if (comment != null && !comment.equals("")) {
							labels.add(comment.trim());
						} else if (comment != null && !comment.equals("") && listStr.size() != 0) {
							labels.add(comment.trim());
						}
					}
				}
//				if (label != null && !label.equals(a))// 存在自己等于自己的label
//				{
//					Con_Lab.put(a, label);
//				} else if (label != null && comment != null && !comment.equals("")) {
//					Con_Lab.put(a, comment.trim());
//				}
//				if(labels.size()>0){
				Con_Lab.put(a, labels);
//				}
			}
		}
		return Con_Lab;
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

	public ArrayList<String> getObjectProperties() {
		ArrayList<String> Properties = new ArrayList<String>();
		for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) {
			// System.out.println(op.getIRI().getFragment());
			String property = op.getIRI().getFragment();
			if (property.equals("topObjectProperty") || property.equals("bottomObjectProperty"))// 常规本体
				continue;
			// 医学本体
			// else
			// if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else if (property.equals("ObsoleteProperty") || property.equals("UNDEFINED"))
				continue;
			else if (!Properties.contains(property))
				Properties.add(property);
		}
		return Properties;
	}

	public ArrayList<String> getObjectPropertyAnnoations() {
		ArrayList<String> Annoations = new ArrayList<String>();
		// OWLDataFactory fac = manager.getOWLDataFactory();
		for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) {
			String property = op.getIRI().getFragment();
			if (property.equals("topObjectProperty") || property.equals("bottomObjectProperty"))// 常规本体
				continue;
			// 医学本体
			// else
			// if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else if (property.equals("ObsoleteProperty") || property.equals("UNDEFINED"))
				continue;
			String label = null;
			for (OWLAnnotation anno : op.getAnnotations(onto, fac.getRDFSLabel())) {
				if (anno.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) anno.getValue();
					// if(val.hasLang("pt"))
					label = val.getLiteral();
					// System.out.println(op+" labelled "+val.getLiteral());
				}
			}
			String comment = null;
			for (OWLAnnotation comm : op.getAnnotations(onto, fac.getRDFSComment())) {
				if (comm.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) comm.getValue();
					// if(val.hasLang("pt"))
					comment = val.getLiteral();
					// System.out.println(op+" comment is  "+val.getLiteral());
				}
			}

			if (label != null && !label.equals(property))// 存在自己等于自己的label
				Annoations.add(property + "--" + label);
			else if (label != null && comment != null && !comment.equals("")) {
				Annoations.add(property + "--" + comment.trim());
			}
		}
		return Annoations;
	}

	public ArrayList<String> getPropertyAndInverse() {
		ArrayList<String> propertiesAndInverse = new ArrayList<String>();
		for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) {
			String property = op.getIRI().getFragment();
			if (property != null) {
				Set<OWLObjectPropertyExpression> inverse = op.getInverses(onto);
				for (OWLObjectPropertyExpression a : inverse) {
					// a.asOWLObjectProperty().getIRI().getFragment();
					// System.out.println(property+"--"+a.asOWLObjectProperty().getIRI().getFragment());
					propertiesAndInverse.add(property + "--" + a.asOWLObjectProperty().getIRI().getFragment());
				}
			}
		}
		return propertiesAndInverse;
	}

	public ArrayList<String> getDataProperties() {
		ArrayList<String> Properties = new ArrayList<String>();
		for (OWLDataProperty dp : onto.getDataPropertiesInSignature()) {
			// System.out.println(op.getIRI().getFragment());
			String property = dp.getIRI().getFragment();
			if (property != null) {
				if (property.equals("topDataProperty") || property.equals("bottomDataProperty"))
					continue;
				else if (!Properties.contains(property))
					Properties.add(property);
			}

		}
		return Properties;
	}

	public ArrayList<String> getDataPropertyAnnoations() {
		ArrayList<String> Annoations = new ArrayList<String>();
		// OWLDataFactory fac = manager.getOWLDataFactory();
		for (OWLDataProperty op : onto.getDataPropertiesInSignature()) {
			String property = op.getIRI().getFragment();
			if (property != null) {
				if (property.equals("topDataProperty") || property.equals("bottomDataProperty"))// 常规本体
					continue;
				// 医学本体
				// else
				// if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
				else if (property.equals("ObsoleteProperty") || property.equals("UNDEFINED"))
					continue;
				String label = null;
				for (OWLAnnotation anno : op.getAnnotations(onto, fac.getRDFSLabel())) {
					if (anno.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) anno.getValue();
						// if(val.hasLang("pt"))
						label = val.getLiteral();
						// System.out.println(op+" labelled "+val.getLiteral());
					}
				}
				String comment = null;
				for (OWLAnnotation comm : op.getAnnotations(onto, fac.getRDFSComment())) {
					if (comm.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) comm.getValue();
						// if(val.hasLang("pt"))
						comment = val.getLiteral();
						// System.out.println(op+" comment is  "+val.getLiteral());
					}
				}

				if (label != null && !label.equals(property))// 存在自己等于自己的label
					Annoations.add(property + "--" + label);
				else if (label != null && comment != null && !comment.equals("")) {
					Annoations.add(property + "--" + comment.trim());
				}
			}
		}
		return Annoations;
	}

	public ArrayList<String> getConceptSubClasses(String concept) {
		ArrayList<String> conceptSubClasses = new ArrayList<String>();
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * //OWLReasonerFactory reasonerFactory = new
		 * Reasoner.ReasonerFactory(); ConsoleProgressMonitor progressMonitor =
		 * new ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */

		// OWLDataFactory fac = manager.getOWLDataFactory();
		String concept_url = OntoID + "#" + concept;
		// System.out.println( concept_url);
		OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
		NodeSet<OWLClass> Children = hermit.getSubClasses(concept_name, false);
		// NodeSet<OWLClass> Children = reasoner.getSubClasses(concept_name,
		// false);//false是使用推理机的情况
		Set<OWLClass> children = Children.getFlattened();
		for (OWLClass c : children) {
			String child = c.getIRI().getFragment();
			if (!child.equals("Nothing"))// Thing的情况就不考
				conceptSubClasses.add(child);
			// System.out.println(child);
		}
		return conceptSubClasses;
	}

	public ArrayList<String> getConceptDirectSubClasses(String concept) {
		ArrayList<String> conceptDirectSubClasses = new ArrayList<String>();
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */

		// OWLDataFactory fac = manager.getOWLDataFactory();
		String concept_url = OntoID + "#" + concept;
		// System.out.println( concept_url);
		OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
		NodeSet<OWLClass> Children = hermit.getSubClasses(concept_name, true);// false是使用推理机的情况
		Set<OWLClass> children = Children.getFlattened();
		for (OWLClass c : children) {
			String child = c.getIRI().getFragment();
			if (!child.equals("Nothing"))// Thing的情况就不考
				conceptDirectSubClasses.add(child);
			// System.out.println(child);
		}
		return conceptDirectSubClasses;
	}

	public Map<String, Set<String>> GetSubclass_Map() {
		Map<String, Set<String>> map = new HashMap<>();
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Children = hermit.getSubClasses(c, false);// false是使用了推理机，返回的是子孙节点
			Set<OWLClass> children = Children.getFlattened();
			Set<String> Objects = new HashSet<>();

			for (OWLClass sub : children) {
				String child = sub.getIRI().getFragment();
				if (child.equals("Nothing"))
					continue;
				else
					Objects.add(child);
			}
			map.put(concept, Objects);
		}
		return map;
	}

	public ArrayList<String> getSubClasses() {
		ArrayList<String> Subclasses = new ArrayList<String>();
		int i = 0;
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			// a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Children = hermit.getSubClasses(c, false);// false是使用了推理机，返回的是子孙节点
			Set<OWLClass> children = Children.getFlattened();
			String sub_information = concept + "--";
			for (OWLClass sub : children) {
				String child = sub.getIRI().getFragment();
				sub_information = sub_information + "," + child;
			}
			if (sub_information.replace(concept, "").equals("--,Nothing"))
				continue;
			else if (sub_information.replace(concept, "").equals("--")) {
				continue;
			} else {
				sub_information = sub_information.replace(",Nothing", "");
				sub_information = sub_information.replace("--,", "--"); // 考虑可能出先Nothing的情况
				Subclasses.add(sub_information);
			}
			System.out.println(i++);
		}
		return Subclasses;
	}

	public ArrayList<String> getDirectSubClasses() {
		ArrayList<String> Subclasses = new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			// a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Children = hermit.getSubClasses(c, true);// true表示没有使用了推理机，返回的是儿子
			Set<OWLClass> children = Children.getFlattened();
			String sub_information = concept + "--";
			for (OWLClass sub : children) {
				String child = sub.getIRI().getFragment();
				sub_information = sub_information + "," + child;
			}
			if (sub_information.replace(concept, "").equals("--,Nothing"))
				continue;
			else if (sub_information.replace(concept, "").equals("--")) {
				continue;
			} else {
				sub_information = sub_information.replace(",Nothing", "");
				sub_information = sub_information.replace("--,", "--"); // 考虑可能出先Nothing的情况
				Subclasses.add(sub_information);
			}
		}
		return Subclasses;
	}

	public Map<String, Integer> GetConcept_Path() {
		// ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		Map<String, Integer> map = new HashMap<>();
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			// a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Ancestors = hermit.getSuperClasses(c, false);// true表示没有使用了推理机，返回的是儿子
			Set<OWLClass> ancestors = Ancestors.getFlattened();
			int depth = 0;
			for (OWLClass sup : ancestors) {
				String anc = sup.getIRI().getFragment();
				if (anc == null) {
					continue;
				}

				if (anc.equals("Thing") || anc.equals("Resource"))// 表明其父类用Resource或者Thing表达出现
				{
					continue;
				} else // 遇到其他的父亲概念深度加1
				{
					depth++;
				}
			}
			depth = depth + 1;
			map.put(concept, depth);
		}
		return map;
	}

	public Map<String, Set<String>> GetSupclass_Map() {
		Map<String, Set<String>> map = new HashMap<>();
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Parents = hermit.getSuperClasses(c, false);// false是使用了推理机，返回的是子孙节点
			Set<OWLClass> parents = Parents.getFlattened();
			Set<String> Objects = new HashSet<>();

			for (OWLClass sup : parents) {
				String parent = sup.getIRI().getFragment();
				if (parent.equals("Thing"))
					continue;
				else
					Objects.add(parent);
			}
			map.put(concept, Objects);
		}
		return map;
	}

	public ArrayList<String> getSuperClasses() {
		ArrayList<String> SuperClasses = new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			// a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Father = hermit.getSuperClasses(c, false);// false表示使用了推理机
			 
			Set<OWLClass> fathers = Father.getFlattened();
			String super_information = concept + "--";
			for (OWLClass sup : fathers) {
				String father = sup.getIRI().getFragment();
				super_information = super_information + "," + father;
			}
			if (super_information.replace(concept, "").equals("--,Thing"))
				continue;
			else if (super_information.replace(concept, "").equals("--")) {
				continue;
			} else {
				super_information = super_information.replace(",Thing", "");
				super_information = super_information.replace("--,", "--"); // 考虑可能出先Nothing的情况
				SuperClasses.add(super_information);
			}
		}
		return SuperClasses;
	}

	public ArrayList<String> getDirectSuperClasses() {
		ArrayList<String> SuperClasses = new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			// a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Father = hermit.getSuperClasses(c, true);// false表示没有使用了推理机
			Set<OWLClass> fathers = Father.getFlattened();
			String super_information = concept + "--";
			for (OWLClass sup : fathers) {
				String father = sup.getIRI().getFragment();
				super_information = super_information + "," + father;
			}
			if (super_information.replace(concept, "").equals("--,Thing"))
				continue;
			else if (super_information.replace(concept, "").equals("--")) {
				continue;
			} else {
				super_information = super_information.replace(",Thing", "");
				super_information = super_information.replace("--,", "--"); // 考虑可能出先Nothing的情况
				SuperClasses.add(super_information);
			}
		}
		return SuperClasses;
	}

	public Map<String, Set<String>> getSibling_Map() {
		Map<String, Set<String>> map = new HashMap<>();

		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			NodeSet<OWLClass> Parents = hermit.getSuperClasses(c, true);
			Set<OWLClass> parents = Parents.getFlattened();
			Set<String> Objects = new HashSet<>();

			for (OWLClass sup : parents) {
				NodeSet<OWLClass> siblings = hermit.getSubClasses(sup, true);
				Set<OWLClass> Siblings = siblings.getFlattened();
				for (OWLClass sib : Siblings) {
					String Sib = sib.getIRI().getFragment();
					if (Sib.equals(concept)) {
						continue;
					} else {
						Objects.add(Sib);
					}
				}
			}
			map.put(concept, Objects);
		}
		return map;
	}

	public ArrayList<String> getSibling(ArrayList<String> Subclasses_Direct) {
		ArrayList<String> Sibling = new ArrayList<String>();
		// System.out.println("***********************");
		for (int i = 0; i < Subclasses_Direct.size(); i++) {
			String father_children[] = Subclasses_Direct.get(i).split("--");
			if (father_children[0].equals("Thing"))
				continue;
			else {
				// System.out.println(father_children[0]);
				String children[] = father_children[1].split(",");
				if (children.length > 1) {
					String sibling_information = "";
					for (int j = 0; j < children.length; j++) {
						// 考虑添加的位置可能是最后一位，也可以是第一位
						String others = father_children[1].replace(children[j] + ",", "").replace("," + children[j],
								"");
						sibling_information = children[j].toString() + "--" + others;
						/*
						 * if(!Sibling.contains(sibling_information))
						 * Sibling.add(sibling_information); for(int
						 * k=0;k<children.length;k++) { if(k!=j) {
						 * sibling_information
						 * =sibling_information+","+children[k].toString(); } }
						 */
						/*
						 * if(!Sibling.contains(sibling_information))
						 * Sibling.add(sibling_information);
						 */
					}
				}
			}
		}
		return Sibling;
	}

	public Map<String, Set<String>> getDisjointwith_Map() {
//		ArrayList<String> Disjointion = new ArrayList<String>();
//		Map<String, ArrayList<String>> map = new HashMap<>();
		Map<String, Set<String>> map1 = new HashMap<>();
		// ArrayList<String> SuperClasses=new ArrayList<String>();
		int i = 0;
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			// a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			Set<OWLClassExpression> disjoints = c.getDisjointClasses(onto);
//			NodeSet<OWLClass> Disijoint = hermit.getDisjointClasses(c);
//			System.out.println(i++);
//			Set<OWLClass> disjoints = Disijoint.getFlattened();

//			String super_information = concept + "--";
			Set<String> disjointions = new HashSet<>();
			for (OWLClassExpression dis : disjoints) {
//				String disjoint = dis.getIRI().getFragment();
				String disjoint = dis.asOWLClass().getIRI().getFragment();
//				super_information = super_information + "," + disjoint;
				if (disjoint.equals("nothing")) {
					continue;
				} else {
					for (String sub : getConceptSubClasses(disjoint)) {
						disjointions.add(sub);
					}
					disjointions.add(disjoint);
				}
			}

			if (map1.keySet().contains(concept)) {
				Set<String> diStrings = map1.get(concept);
				diStrings.addAll(disjointions);
			} else {
				map1.put(concept, disjointions);
			}

			ArrayList<String> A_children = getConceptSubClasses(concept);
			for (String s : disjointions) {
				Set<String> dis_list = new HashSet<>();

				if (map1.keySet().contains(s)) {
					dis_list = map1.get(s);
					dis_list.add(concept);
				} else {
					dis_list.add(concept);
					map1.put(s, dis_list);
				}

				ArrayList<String> B_children = getConceptSubClasses(s);
				for (String child1 : A_children) {
					for (String child2 : B_children) {
						if (map1.keySet().contains(child1)) {
							Set<String> dis = map1.get(child1);
							dis.add(child2);
						} else {
							Set<String> dis = new HashSet<>();
							dis.add(child2);
							map1.put(child1, dis);
						}
						if (map1.keySet().contains(child2)) {
							Set<String> dis = map1.get(child2);
							dis.add(child1);
						} else {
							Set<String> dis = new HashSet<>();
							dis.add(child1);
							map1.put(child2, dis);
						}
					}
				}
			}

//			for (String s : map1.keySet()) {
//				ArrayList<String> v = new ArrayList<>();
//				v.addAll(map1.get(s));
//				map.put(s, v);
//			}
//			ArrayList<String> children = getConceptSubClasses(concept);
//			for (String child : children) {
//				if (map1.keySet().contains(child)) {
//					Set<String> dis= map1.get(child);
//					dis.add(concept);
//				} else {
//					Set<String> dis = new HashSet<>();
//					dis.add(concept);
//					map1.put(child, dis);
//				}
//			}
		}
		return map1;
	}

	public ArrayList<String> getDisjointwith() {
		ArrayList<String> Disjointion = new ArrayList<String>();
		// ArrayList<String> SuperClasses=new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			// a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if (concept.equals("Thing") || concept.equals("Nothing"))
				continue;
			Set<OWLClassExpression> disjoints = c.getDisjointClasses(onto);
			String super_information = concept + "--";
			ArrayList<String> disjointions = new ArrayList<String>();
			for (OWLClassExpression dis : disjoints) {
				String disjoint = dis.asOWLClass().getIRI().getFragment();
				super_information = super_information + "," + disjoint;
				for (String sub : getConceptSubClasses(disjoint))
					disjointions.add(sub);
				// disjointions.add(disjoint);
				// getConceptSubClasses
			}
			for (String a : disjointions) {
				super_information = super_information + "," + a;
			}

			/*
			 * NodeSet<OWLClass> DisjointList =
			 * hermit.getDisjointClasses(c);//false表示没有使用了推理机
			 * System.out.println(DisjointList.isEmpty()); Set<OWLClass>
			 * disjoints = DisjointList.getFlattened(); String
			 * super_information=concept+"--"; for (OWLClass dis : disjoints) {
			 * String disjoint=dis.getIRI().getFragment();
			 * super_information=super_information+","+disjoint; }
			 */
			if (super_information.replace(concept, "").equals("--,Nothing"))
				continue;
			else if (super_information.replace(concept, "").equals("--")) {
				continue;
			} else {
				super_information = super_information.replace(",Nothing", "");
				super_information = super_information.replace("--,", "--"); // 考虑可能出先Nothing的情况
				Disjointion.add(super_information);
				ArrayList<String> children = getConceptSubClasses(concept);
				for (String child : children) {
					String inferred_information = super_information.replace(concept, child);
					Disjointion.add(inferred_information);
				}
			}

		}
		return Disjointion;
	}

	public void isConceptInstance(String concept, String instance)// 判断保证原始的"-"不变
	{
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */

		// OWLDataFactory fac = manager.getOWLDataFactory();
		String concept_url = OntoID + "#" + concept;
		OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
		NodeSet<OWLNamedIndividual> individualsNodeSet = hermit.getInstances(concept_name, false);// false是使用推理机的情况
		Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
		// System.out.println(concept+"实例的个数为:"+individuals.size());
		boolean flag = false;
		for (OWLNamedIndividual i : individuals) {
			if (i.getIRI().getFragment().equals(instance)
					|| i.getIRI().toString().replace(OntoID + "#", "").equals(instance)) {
				flag = true;
				break;
			}
		}
		System.out.println(flag);
	}

	public Map<String, ArrayList<String>> getConceptInstances_Map() {
		Map<String, ArrayList<String>> Concept_Instances = new HashMap<>();

		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);
		String information = "";
		for (OWLClass c : onto.getClassesInSignature()) {
			// System.out.println(c.getIRI().getFragment());//可以直接将不带URL的概念名进行输出
			NodeSet<OWLNamedIndividual> individualsNodeSet_c = reasoner.getInstances(c, true);// false是使用推理机的情况
			Set<OWLNamedIndividual> individuals_c = individualsNodeSet_c.getFlattened();
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			concept = c.getIRI().getFragment().replace("-", "_");// 考虑下划线问题

			ArrayList<String> instances = new ArrayList<>();
			if (!concept.equals("Thing") && individuals_c.size() > 0) {
				information = concept + "--";
				// System.out.println("****************************************");
				// System.out.println("the number of instances of "+c.getIRI().getFragment()+
				// " is "+individuals_c.size());
				for (OWLNamedIndividual i : individuals_c) {
					String individual = "";
					// if(i.getIRI().getNamespace().contains(OntoID))
					if (i.getIRI().getFragment().contains(OntoID)) {
						individual = i.getIRI().getFragment();
						// individual=i.getIRI().toString().replace(OntoID+"#",
						// "").replace("-", "_");
					} else {
						individual = i.getIRI().getFragment().replace("-", "_");
					}
					// System.out.println(individual + "\t is an instance of\t"
					// + concept);
					information = information + "," + individual;
					instances.add(individual);
				}
				// System.out.println(information);
				Concept_Instances.put(concept, instances);
				//		        	information="";
			}

		}
		return Concept_Instances;
	}

	public ArrayList<String> getConceptInstances() {
		ArrayList<String> Concept_Instances = new ArrayList<String>();
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */
		String information = "";
		for (OWLClass c : onto.getClassesInSignature()) {
			// System.out.println(c.getIRI().getFragment());//可以直接将不带URL的概念名进行输出
			NodeSet<OWLNamedIndividual> individualsNodeSet_c = hermit.getInstances(c, false);// false是使用推理机的情况
			Set<OWLNamedIndividual> individuals_c = individualsNodeSet_c.getFlattened();
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			concept = c.getIRI().getFragment().replace("-", "_");// 考虑下划线问题

			if (!concept.equals("Thing") && individuals_c.size() > 0) {
				information = concept + "--";
				// System.out.println("****************************************");
				// System.out.println("the number of instances of "+c.getIRI().getFragment()+
				// " is "+individuals_c.size());
				for (OWLNamedIndividual i : individuals_c) {
					String individual = "";
					// if(i.getIRI().getNamespace().contains(OntoID))
					if (i.getIRI().getFragment().contains(OntoID)) {
						individual = i.getIRI().getFragment();
						// individual=i.getIRI().toString().replace(OntoID+"#",
						// "").replace("-", "_");
					} else {
						individual = i.getIRI().getFragment().replace("-", "_");
					}
					// System.out.println(individual + "\t is an instance of\t"
					// + concept);
					information = information + "," + individual;
				}
				// System.out.println(information);
				Concept_Instances.add(information.replace("--,", "--"));
				information = "";
			}

		}
		return Concept_Instances;
	}

	public ArrayList<String> getConceptInstances(String concept) {
		ArrayList<String> Instances = new ArrayList<String>();
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */

		// OWLDataFactory fac = manager.getOWLDataFactory();
		String concept_url = OntoID + "#" + concept;
		System.out.println(concept_url);
		OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
		NodeSet<OWLNamedIndividual> individualsNodeSet = hermit.getInstances(concept_name, false);// false是使用推理机的情况
		Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
		System.out.println(concept + "实例的个数为:" + individuals.size());

		String information = concept.replace("-", "_") + "--";
		for (OWLNamedIndividual i : individuals) {

			// String instance=i.getIRI().getFragment().replace("-",
			// "_");//考虑下划线问题;
			// IRI instance=i.getIRI();//考虑下划线问题;
			// String IndividualID=find_Instances_url();
			String instance = "";
			// if(i.getIRI().getNamespace().contains(OntoID))
			if (i.getIRI().getFragment().contains(OntoID)) {
				instance = i.getIRI().toString().replace(OntoID + "#", "");
				System.out.println(instance + "\tinstance of\t" + concept.replace("-", "_")); // 考虑下划线问题;
			} else {
				instance = i.getIRI().getFragment();
				System.out.println(instance + "\tinstance of\t" + concept.replace("-", "_")); // 考虑下划线问题;
			}
			information = information + "," + instance.replace("-", "_");

		}
		Instances.add(information.replace("--,", "--"));
		return Instances;
	}

	public ArrayList<String> getRelationInstances() {
		ArrayList<String> Relations = new ArrayList<String>();

		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */

		for (OWLClass c : onto.getClassesInSignature()) {
			NodeSet<OWLNamedIndividual> instances = hermit.getInstances(c, false);
			for (OWLNamedIndividual i : instances.getFlattened()) {
				String instance = i.getIRI().getFragment();
				for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) {
					String objectpropety = op.getIRI().getFragment();
					NodeSet<OWLNamedIndividual> petValuesNodeSet = hermit.getObjectPropertyValues(i, op);
					for (OWLNamedIndividual value : petValuesNodeSet.getFlattened()) {
						String instance_value = value.getIRI().getFragment();
						System.out.println(instance + "\t" + objectpropety + "\t" + instance_value);
						if (instance != null && instance_value != null) // 可能有关系为空的情况
							Relations.add(instance.replace("-", "_") + "," + objectpropety.replace("-", "_") + ","
									+ instance_value.replace("-", "_"));
					}
				}
			}
		}
		return Relations;
	}

	/*
	 * public ArrayList<String> getDataProperyInstances() //并没有实例 {
	 * ArrayList<String> Relations=new ArrayList<String>();
	 * 
	 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
	 * ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
	 * OWLReasonerConfiguration config = new
	 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
	 * reasonerFactory.createReasoner(onto, config);
	 * 
	 * for (OWLClass c : onto.getClassesInSignature()) {
	 * NodeSet<OWLNamedIndividual> instances = hermit.getInstances(c, false);
	 * for (OWLNamedIndividual i : instances.getFlattened()) { String
	 * instance=i.getIRI().getFragment(); for (OWLDataProperty dp :
	 * onto.getDataPropertiesInSignature()) { String
	 * objectpropety=dp.getIRI().getFragment(); Set<OWLLiteral> petValuesNodeSet
	 * = hermit.getDataPropertyValues(i, dp); for (OWLLiteral value :
	 * petValuesNodeSet) { String instance_value= value.getLiteral();
	 * System.out.println(instance + "\t" + objectpropety + "\t"+
	 * instance_value); if(instance!=null&&instance_value!=null) //可能有关系为空的情况
	 * Relations.add(instance.replace("-", "_") + "," +
	 * objectpropety.replace("-", "_")+ ","+ instance_value.replace("-", "_"));
	 * } } } } return Relations; }
	 */

	public ArrayList<String> getRelationInstances(String individual, String property) {
		ArrayList<String> Relations = new ArrayList<String>();
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */

		// OWLDataFactory fac = manager.getOWLDataFactory();

		String individual_url = findInstancesUrl() + individual;// url不一定是本体的，可能是"http://xmlns.com/foaf/0.1#"
		String property_url = OntoID + "#" + property;

		OWLNamedIndividual Individual = fac.getOWLNamedIndividual(IRI.create(individual_url));
		OWLObjectProperty Property = fac.getOWLObjectProperty(IRI.create(property_url));

		NodeSet<OWLNamedIndividual> petValuesNodeSet = hermit.getObjectPropertyValues(Individual, Property);
		Set<OWLNamedIndividual> values = petValuesNodeSet.getFlattened();
		for (OWLNamedIndividual ind : values) {
			String value = ind.getIRI().getFragment();
			// System.out.println(individual+","+property+","+value);
			Relations.add(individual + "," + property + "," + value);
		}
		return Relations;
	}

	public String findInstancesUrl() {
		String IndividualID = OntoID;
		/*
		 * OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		 * ConsoleProgressMonitor progressMonitor = new
		 * ConsoleProgressMonitor(); OWLReasonerConfiguration config = new
		 * SimpleConfiguration(progressMonitor); OWLReasoner reasoner =
		 * reasonerFactory.createReasoner(onto, config);
		 */

		for (OWLClass c : onto.getClassesInSignature()) {
			NodeSet<OWLNamedIndividual> individualsNodeSet_c = hermit.getInstances(c, true);// false是使用推理机的情况
			Set<OWLNamedIndividual> individuals_c = individualsNodeSet_c.getFlattened();
			if (individuals_c.size() > 0) {
				for (OWLNamedIndividual i : individuals_c) {
					// String URL=i.getIRI().getNamespace();//考虑下划线问题;
					String URL = i.getIRI().getFragment();// 考虑下划线问题;

					// 这里看看getScheme()是什么？
					if (!URL.contains(OntoID)) {
						IndividualID = URL;
						// System.out.println(IndividualID);
						return IndividualID;
					}
				}
			}
		}
		return IndividualID;
	}

	public ArrayList<String> getObjectRelations() {
		ArrayList<String> Relations = new ArrayList<String>();
		for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) {
			String property = op.getIRI().getFragment();
			// System.out.println(property);
			if (property.equals("topObjectProperty") || property.equals("bottomObjectProperty"))// 常规本体
				continue;
			Set<OWLClassExpression> Domain = op.getDomains(onto);
			Set<OWLClassExpression> Range = op.getRanges(onto);
			String domain = "";
			String range = "";
			Set<OWLClassExpression> domains = null;
			Set<OWLClassExpression> ranges = null;
			for (OWLClassExpression d : Domain) {
				if (d == null)
					continue;
				if (!d.isAnonymous())
					domain = d.asOWLClass().getIRI().getFragment();
				else
					domains = d.asDisjunctSet();
			}
			for (OWLClassExpression r : Range) {
				if (r == null)
					continue;
				if (!r.isAnonymous())
					range = r.asOWLClass().getIRI().getFragment();
				else
					ranges = r.asDisjunctSet();
			}
			// 目前采用保守态势
			if (domain == null || range == null)
				continue;
			if (!domain.equals("") && !range.equals("")) {
				// System.out.println(domain+" "+property+" "+range);
				String Triple = "";
				ArrayList<String> subjects = getConceptSubClasses(domain);
				subjects.add(domain);
				for (String s : subjects) {
					// System.out.println(s+","+property+","+range);
					Triple = s + "," + property + "," + range;
					if (!Relations.contains(Triple))
						Relations.add(Triple);
				}
				ArrayList<String> objects = getConceptSubClasses(range);
				objects.add(range);
				for (String o : objects) {
					// System.out.println(s+","+property+","+range);
					Triple = domain + "," + property + "," + o;
					if (!Relations.contains(Triple))
						Relations.add(Triple);
				}
			}

			else if (!domain.equals("") && ranges != null) {
				// System.out.println("********************");
				for (OWLClassExpression a : ranges) {
					if (a == null)
						continue;
					String Triple = "";
					// 值域考虑得比较详细,因此只对定义域做扩张
					ArrayList<String> subjects = getConceptSubClasses(domain);
					subjects.add(domain);
					for (String s : subjects) {
						// System.out.println(s+","+property+","+range);
						Triple = s + "," + property + "," + a.asOWLClass().getIRI().getFragment();
						if (!Relations.contains(Triple))
							Relations.add(Triple);
					}
					// System.out.println(domain+" "+property+" "+a);
				}
				// System.out.println("********************");

			} else if (domains != null && !range.equals("")) {
				// System.out.println("********************");
				for (OWLClassExpression a : domains) {
					if (a == null)
						continue;
					String Triple = "";
					// 定义域域考虑得比较详细,因此只对值域做扩张
					ArrayList<String> objects = getConceptSubClasses(range);
					objects.add(range);
					for (String o : objects) {
						// System.out.println(s+","+property+","+range);
						Triple = a.asOWLClass().getIRI().getFragment() + "," + property + "," + o;
						if (!Relations.contains(Triple))
							Relations.add(Triple);
					}
					// System.out.println(a.asOWLClass().getIRI().getFragment()+" "+property+" "+range);
				}
				// System.out.println("********************");
			}

			else if (domains != null && ranges != null) {
				// System.out.println("********************");
				String Triple = "";
				for (OWLClassExpression a : domains) {
					if (a == null)
						continue;
					for (OWLClassExpression b : ranges) {
						if (b == null)
							continue;
						// System.out.println(a.asOWLClass().getIRI().getFragment()+" "+property+" "+b.asOWLClass().getIRI().getFragment());
						Triple = a.asOWLClass().getIRI().getFragment() + "," + property + ","
								+ b.asOWLClass().getIRI().getFragment();
						if (!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}
				// System.out.println("********************");
			}
		}

		return Relations;
	}

	public ArrayList<String> getDataPropertyRelations() {
		ArrayList<String> Relations = new ArrayList<String>();
		for (OWLDataProperty dp : onto.getDataPropertiesInSignature()) {
			String property = dp.getIRI().getFragment();
			// System.out.println(property);
			if (property == null)
				continue;
			if (property.equals("topDataProperty") || property.equals("bottomDataProperty"))// 常规本体
				continue;
			Set<OWLClassExpression> Domain = dp.getDomains(onto);
			Set<OWLDataRange> Range = dp.getRanges(onto);
			String domain = "";
			String range = "";
			Set<OWLClassExpression> domains = null;
			// Set<OWLClassExpression> ranges = null;
			for (OWLClassExpression d : Domain) {
				if (d == null)
					continue;
				if (!d.isAnonymous())
					domain = d.asOWLClass().getIRI().getFragment();
				else
					domains = d.asDisjunctSet();
			}
			for (OWLDataRange r : Range) {
				if (r == null)
					continue;
				// if(!r.isDatatype())
				// range=r.asOWLDatatype().toString();
				if (r.isDatatype())
					range = r.asOWLDatatype().getIRI().getFragment();
			}
			// String Triple="";
			if (!domain.equals("") && !range.equals("")) {
				ArrayList<String> subjects = getConceptSubClasses(domain);
				subjects.add(domain);
				String Triple = "";
				for (String s : subjects) {
					// System.out.println(s+","+property+","+range);
					Triple = s + "," + property + "," + range;
					if (!Relations.contains(Triple))
						Relations.add(Triple);
				}
			}

			else if (domains != null && !range.equals("")) {
				// System.out.println("********************");
				for (OWLClassExpression a : domains) {
					if (a == null)
						continue;
					domain = a.asOWLClass().getIRI().getFragment();
					ArrayList<String> subjects = getConceptSubClasses(domain);
					subjects.add(domain);
					String Triple = "";
					for (String s : subjects) {
						// System.out.println(s+","+property+","+range);
						Triple = s + "," + property + "," + range;
						if (!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}
				// System.out.println("********************");
			}
		}

		return Relations;
	}

	public ArrayList<String> getEquivalentClass() {
		ArrayList<String> EquivalentClass = new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			// System.out.println(c.getIRI().getFragment());
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (!concept.equals("Thing") || concept.equals("Nothing"))// Thing的情况就不考虑了
			{

				/*
				 * Set<OWLClassExpression> equivalents
				 * =c.getEquivalentClasses(onto); for(OWLClassExpression equ:
				 * equivalents)
				 */
				Node<OWLClass> equivalents = hermit.getEquivalentClasses(c);
				for (OWLClass equ : equivalents) {
					if (!equ.isAnonymous()) {
						String equivalentConcept = equ.asOWLClass().getIRI().getFragment();
						if (!equivalentConcept.equals(concept) && !equivalentConcept.equals("Nothing")) {
							EquivalentClass.add(concept + "," + equivalentConcept + "," + "Equal");
							// System.out.println(concept+","+equivalentConcept+","+"Equal");
						}
					}
				}

			}
		}
		return EquivalentClass;
	}

	public ArrayList<String> getEquivalentObjectProperty() {
		ArrayList<String> EquivalentProperties = new ArrayList<String>();
		for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) {
			// System.out.println(op.getIRI().getFragment());
			String property = op.getIRI().getFragment();
			if (property == null)
				continue;
			if (property.equals("topObjectProperty") || property.equals("bottomObjectProperty"))// 常规本体
				continue;
			// 医学本体
			// else
			// if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else if (property.equals("ObsoleteProperty") || property.equals("UNDEFINED"))
				continue;
			else {
				Node<OWLObjectPropertyExpression> equivalents = hermit.getEquivalentObjectProperties(op);
				for (OWLObjectPropertyExpression equ : equivalents) {
					if (!equ.isAnonymous()) {
						String equivalentProperty = equ.asOWLObjectProperty().getIRI().getFragment();
						if (!equivalentProperty.equals(property) && !equivalentProperty.equals("Nothing")) {
							EquivalentProperties.add(property + "," + equivalentProperty + "," + "Equal");
							// System.out.println(property+","+equivalentProperty+","+"Equal");
						}
					}
				}
			}
		}
		return EquivalentProperties;
	}

	public ArrayList<String> getEquivalenDataProperty() {
		ArrayList<String> EquivalentProperties = new ArrayList<String>();
		for (OWLDataProperty dp : onto.getDataPropertiesInSignature()) {
			// System.out.println(op.getIRI().getFragment());
			String property = dp.getIRI().getFragment();
			if (property == null)
				continue;
			if (property.equals("topDataProperty") || property.equals("bottomDataProperty"))// 常规本体
				continue;
			// 医学本体
			// else
			// if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else {
				Node<OWLDataProperty> equivalents = hermit.getEquivalentDataProperties(dp);
				for (OWLDataProperty equ : equivalents) {
					if (!equ.isAnonymous()) {
						String equivalentProperty = equ.getIRI().getFragment();
						if (!equivalentProperty.equals(property) && !equivalentProperty.equals("Nothing")) {
							EquivalentProperties.add(property + "," + equivalentProperty + "," + "Equal");
							// System.out.println(property+","+equivalentProperty+","+"Equal");
						}
					}
				}
			}
		}
		return EquivalentProperties;
	}

	public ArrayList<String> getSomeRestrictions()// 找到某个本体所有概念的领域限制
	{
		ArrayList<String> someRestrictions = new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			// System.out.println(c.getIRI().getFragment());
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (!concept.equals("Thing") || concept.equals("Nothing"))// Thing的情况就不考虑了
			{
				NodeSet<OWLClass> Father = hermit.getSuperClasses(c, false);
				Set<OWLClass> fathers = Father.getFlattened();
				fathers.add(c);
				for (OWLClass sup : fathers) {
					RestrictionVisitor restrictionVisitor = new RestrictionVisitor(sup, onto);
					HashMap<OWLObjectPropertyExpression, Set<OWLClass>> someValue = restrictionVisitor
							.getRestrictedProperties();
					if (someValue.size() != 0) {
						// System.out.println(concept);
						// System.out.println("Restricted properties for " +
						// concept + ": " + someValue.size());
						for (OWLObjectPropertyExpression prop : someValue.keySet()) {
							// System.out.println("    " + prop);
							Set<OWLClass> objects = someValue.get(prop);
							for (OWLClass o : objects) {
								String relation = prop.asOWLObjectProperty().getIRI().getFragment();
								String object = o.getIRI().getFragment();
								String constraint = concept + "," + relation + "," + "some" + "," + object;
								// System.out.println(constraint);
								someRestrictions.add(constraint);
							}
						}
					}
				}
			}
		}
		return someRestrictions;
	}

	public ArrayList<String> getLocalSomeRestrictions()// 找到某个本体所有概念的领域限制
	{
		ArrayList<String> someRestrictions = new ArrayList<String>();
		for (OWLClass c : onto.getClassesInSignature()) {
			// System.out.println(c.getIRI().getFragment());
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (!concept.equals("Thing") || concept.equals("Nothing"))// Thing的情况就不考虑了
			{
				RestrictionVisitor restrictionVisitor = new RestrictionVisitor(c, onto);
				HashMap<OWLObjectPropertyExpression, Set<OWLClass>> someValue = restrictionVisitor
						.getRestrictedProperties();
				if (someValue.size() != 0) {
//					System.out.println(concept);
//					System.out.println(someValue.toString());
//					 System.out.println("Restricted properties for " + concept
//					 + ": " + someValue.size());
					for (OWLObjectPropertyExpression prop : someValue.keySet()) {
						// System.out.println("    " + prop);
						Set<OWLClass> objects = someValue.get(prop);
						for (OWLClass o : objects) {
							String relation = prop.asOWLObjectProperty().getIRI().getFragment();
							String object = o.getIRI().getFragment();
							String constraint = concept + "," + relation + "," + "some" + "," + object;
							// System.out.println(constraint);
							someRestrictions.add(constraint);
						}
					}
				}
			}
		}
		return someRestrictions;
	}

	//获得part-of信息
	public Map<String, Set<String>> getPartof_Map() {
		Map<String, Set<String>> results = new HashMap<>();
		String partofproperty1 = "Anatomic_Structure_Is_Physical_Part_Of";
		String partofproperty2 = "Gene_Product_Is_Physical_Part_Of";
		String partofproperty3 = "UNDEFINED_part_of";
		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (!concept.equals("Thing") || concept.equals("Nothing"))// Thing的情况就不考虑了
			{
				RestrictionVisitor restrictionVisitor = new RestrictionVisitor(c, onto);
				HashMap<OWLObjectPropertyExpression, Set<OWLClass>> someValue = restrictionVisitor
						.getRestrictedProperties();
				Set<String> partof = new HashSet<>();
				if (someValue.size() != 0) {
					for (OWLObjectPropertyExpression prop : someValue.keySet()) {
						Set<OWLClass> objects = someValue.get(prop);
						for (OWLClass o : objects) {
							String relation = prop.asOWLObjectProperty().getIRI().getFragment();
							if (relation.equals(partofproperty1) || relation.equals(partofproperty2)
									|| relation.equals(partofproperty3)) {
								String object = o.getIRI().getFragment();
								partof.add(object);
							}
						}
					}
				}
				results.put(concept, partof);
			}
		}

		return results;
	}

	public Map<String, Set<String>> get_MA_NCI_PartOf() {
		Map<String, Set<String>> results = new HashMap<>();
		String partofproperty = "UNDEFINED_part_of";

		for (OWLClass c : onto.getClassesInSignature()) {
			String concept = c.getIRI().getFragment();
			if (concept == null)
				continue;
			if (concept.charAt(0) == '_')
				concept = concept.replaceFirst("_", "");
			if (!concept.equals("Thing") || concept.equals("Nothing"))// Thing的情况就不考虑了
			{
				RestrictionVisitor restrictionVisitor = new RestrictionVisitor(c, onto);
				HashMap<OWLObjectPropertyExpression, Set<OWLClass>> someValue = restrictionVisitor
						.getRestrictedProperties();
				Set<String> partof = new HashSet<>();
				if (someValue.size() != 0) {
					for (OWLObjectPropertyExpression prop : someValue.keySet()) {
						Set<OWLClass> objects = someValue.get(prop);
						for (OWLClass o : objects) {
							String relation = prop.asOWLObjectProperty().getIRI().getFragment();
							if (relation.equals(partofproperty)) {
								String object = o.getIRI().getFragment();
								partof.add(object);
							}
						}
					}
				}
				results.put(concept, partof);
			}
		}

		return results;
	}

	private static class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {

		private boolean processInherited = false;

		private Set<OWLClass> processedClasses;

		private HashMap<OWLObjectPropertyExpression, Set<OWLClass>> restrictedProperties;

		private OWLOntology ontology;

		public RestrictionVisitor(OWLClass restrictedClass, OWLOntology onts) {
			restrictedProperties = new HashMap<OWLObjectPropertyExpression, Set<OWLClass>>();
			processedClasses = new HashSet<OWLClass>();
			ontology = onts;
			processRestrictions(restrictedClass);
		}

		public void setProcessInherited(boolean processInherited) {
			this.processInherited = processInherited;
		}

		public HashMap<OWLObjectPropertyExpression, Set<OWLClass>> getRestrictedProperties() {
			return restrictedProperties;
		}

		public void visit(OWLClass desc) {
			if (processInherited && !processedClasses.contains(desc)) {
				// If we are processing inherited restrictions then
				// we recursively visit named supers. Note that we
				// need to keep track of the classes that we have processed
				// so that we don't get caught out by cycles in the taxonomy
				processedClasses.add(desc);
				for (OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(desc)) {
					ax.getSuperClass().accept(this);
				}

			}
		}

		public void reset() {
			processedClasses.clear();
			restrictedProperties.clear();
		}

		public void visit(OWLObjectSomeValuesFrom desc) {
			// This method gets called when a class expression is an
			// existential (someValuesFrom) restriction and it asks us to visit
			// it
			OWLObjectPropertyExpression property = desc.getProperty();
			OWLClassExpression filler = desc.getFiller();
			Set<OWLClass> classes = filler.getClassesInSignature();
			addProperty(property, classes);
			// recurse
			filler.accept(this);
		}

		private void addProperty(OWLObjectPropertyExpression property, Set<OWLClass> classes) {
			Set<OWLClass> existingClasses = restrictedProperties.get(property);
			if (existingClasses != null) {
				classes.addAll(existingClasses);
			}
			restrictedProperties.put(property, classes);
		}

		private void processRestrictions(OWLClass restrictedClass) {

			for (OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(restrictedClass)) {
				OWLClassExpression cls = ax.getSuperClass();
				if (cls.isAnonymous()) {
					// System.out.println(restrictedClass.getIRI().getFragment());
					// System.out.println("[Inferred] Superclass: " + cls);
					cls.accept(this);
				}
				// Ask our superclass to accept a visit from the
				// RestrictionVisitor - if it is an
				// existential restriction then our restriction visitor will
				// answer it - if not our
				// visitor will ignore it

			}

			for (OWLEquivalentClassesAxiom ax : ontology.getEquivalentClassesAxioms(restrictedClass)) {
				for (OWLClassExpression cls : ax.getClassExpressions()) {
					if (cls.isAnonymous()) {
						// System.out.println(restrictedClass.getIRI().getFragment());
						// System.out.println("Equiv class: " + cls);
						cls.accept(this);
					}
					// Ask our class to accept a visit from the
					// RestrictionVisitor - if it is an
					// existential restriction then our restriction visitor will
					// answer it - if not our
					// visitor will ignore it

				}
			}
			// System.out.println("%%%%%%%%%%%%%%%%%%%%%%");

//			if (this.getRestrictedProperties().size() != 0) {
//				System.out.println(restrictedClass.getIRI().getFragment());
//				System.out.println(
//						"Restricted properties for " + restrictedClass + ": " + this.getRestrictedProperties().size());
//				for (OWLObjectPropertyExpression prop : this.getRestrictedProperties().keySet()) {
//					//System.out.println("    " + prop); Set<OWLClass>
//					Set<OWLClass> objects = getRestrictedProperties().get(prop);
//					for (OWLClass o : objects) {
//						System.out.println(prop);
//						System.out.println(o);
//					}
//				}
//			}

		}
	}

	public ArrayList<String> enhancedRelation(ArrayList<String> ObjectRelations, ArrayList<String> EquivalentClass) {
		// System.out.println("*********************************");
		int m = 0, n = 0;
		for (int i = 0; i < EquivalentClass.size(); i++) {
			String pairs[] = EquivalentClass.get(i).split(",");
			if (pairs.length == 4 && pairs[2].equals("some")) // parts[0]
																// ObjectProperty
																// some parts[1]
			{
				ArrayList<String> triple = new ArrayList<String>();
				triple = findIndexOfRelation(ObjectRelations, pairs[1]);// 定位属性,且三元组可以不止一个，但只有定义域为null或者不为null两种情况
				// boolean flag1=false;//用来标记三元组的定义域为空的情况
				boolean flag2 = false;// 用来标记三元组的定义域不为空，但不相等的情况
				for (int j = 0; j < triple.size(); j++) {
					String parts1[] = triple.get(j).split(",");
					/*
					 * for(String a:parts1) { System.out.println(a+" "); }
					 * System.out.println();
					 */

					int index = Integer.parseInt(parts1[3]);
					String domain = parts1[0];
					if (domain.equals("null"))// 定义域为空的三元组关系
					{
						String orginalRelation[] = ObjectRelations.get(index).split(",");
						String newRelation = pairs[0] + "," + orginalRelation[1] + "," + orginalRelation[2];
						ObjectRelations.remove(index);
						ObjectRelations.add(newRelation);
						if (!ObjectRelations.contains(newRelation)) {
							// System.out.println("修改的三元组为: "+newRelation);
							m++;
						}
						// flag1=true;//确实有为空的情况
					} else if (!domain.equals("null") && pairs[0].equals(domain)) {
						flag2 = true;// 存在相等的情况
						break;
					}

				}
				if (flag2 == false)// 确实不相等且domain不为0
				{
					triple = findIndexOfRelation(ObjectRelations, pairs[1]);// 重新检索一次
					for (int j = 0; j < triple.size(); j++) {
						String parts2[] = triple.get(j).split(",");
						// int index=Integer.parseInt(parts2[3]);
						// String range=parts2[2];
						// String
						// orginalRelation[]=ObjectRelations.get(index).split(",");
						String newRelation = pairs[0] + "," + parts2[1] + "," + parts2[2];
						if (!ObjectRelations.contains(newRelation)) {
							// System.out.println("添加的三元组为: "+newRelation);
							ObjectRelations.add(newRelation);
							n++;
						}
					}
				}
				flag2 = false;
			}

		}
		// System.out.println("更改关系的总数为: "+m);
		// System.out.println("添加关系的总数为: "+n);
		return ObjectRelations;
	}

	public ArrayList<String> findIndexOfRelation(ArrayList<String> Relations, String index) {
		ArrayList<String> Triples = new ArrayList<String>();
		for (int i = 0; i < Relations.size(); i++) {
			String parts[] = Relations.get(i).split(",");
			if (index.equals(parts[1])) {
				Triples.add(Relations.get(i) + "," + i);// 同时记录索引的值
			}
		}
		return Triples;
	}

	public Set<String> getConceptSyn(String concept) {
		String concept_url = OntoID + "#" + concept;
		OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
		String label = null;
		Set<String> results = new HashSet<>();
		for (OWLAnnotationAssertionAxiom annotations : concept_name.getAnnotationAssertionAxioms(onto)) //oboInOwl:hasRelatedSynonym的解析，也包含label的抽取。
		{
			if (annotations.getValue() instanceof OWLAnonymousIndividualImpl) //找到http://human.owl#genid4450 对应的具体Value
			{
				OWLAnonymousIndividualImpl am = (OWLAnonymousIndividualImpl) annotations.getValue();
				// am.getDataPropertyValues(onto);
				// OWLAnonymousIndividual an = am.asOWLAnonymousIndividual();
				for (OWLAnnotationAssertionAxiom a : onto.getAnnotationAssertionAxioms(am)) {
//					System.out.println(a.getValue()); //获取同义词的值
					OWLLiteral syn = (OWLLiteral) a.getValue();
					label = syn.getLiteral();
//					System.out.println(label);
				}
			} else {
				OWLLiteral val = (OWLLiteral) annotations.getValue(); //获取label的值
				label = val.getLiteral();
//				System.out.println(label);
			}
			results.add(label);
		}
		return results;
//		System.out.println("**************");
	}

	public Set<String> getSynonyms(OWLClass concept) {
		String label = null;
		Set<String> results = new HashSet<>();
		for (OWLAnnotationAssertionAxiom annotations : concept.getAnnotationAssertionAxioms(onto)) //oboInOwl:hasRelatedSynonym的解析，也包含label的抽取。
		{
			if (annotations.getValue() instanceof OWLAnonymousIndividualImpl) //找到http://human.owl#genid4450 对应的具体Value
			{
				OWLAnonymousIndividualImpl am = (OWLAnonymousIndividualImpl) annotations.getValue();
				// am.getDataPropertyValues(onto);
				// OWLAnonymousIndividual an = am.asOWLAnonymousIndividual();
				for (OWLAnnotationAssertionAxiom a : onto.getAnnotationAssertionAxioms(am)) {
//					System.out.println(a.getValue()); //获取同义词的值
					OWLLiteral syn = (OWLLiteral) a.getValue();
					label = syn.getLiteral();
//					System.out.println(label);
				}
			} else {
				OWLLiteral val = (OWLLiteral) annotations.getValue(); //获取label的值
				label = val.getLiteral();
//				System.out.println(label);
			}
			results.add(label);
		}
		return results;
//		System.out.println("**************");
	}

}
