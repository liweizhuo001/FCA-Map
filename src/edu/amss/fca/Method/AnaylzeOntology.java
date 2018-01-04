package edu.amss.fca.Method;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import edu.amss.fca.Tools.OWLAPI_tools;

public class AnaylzeOntology {
	
	public static void main(String[] args) throws Exception
	{
		String uri1="Ontologies/oaei_FMA_whole_ontology.owl";	
		String uri2="Ontologies/oaei_NCI_whole_ontology.owl";
		String uri3="Ontologies/oaei_SNOMED_extended_overlapping_fma_nci.owl";
		
		OWLAPI_tools Onto1 = new OWLAPI_tools();
		OWLAPI_tools Onto2 = new OWLAPI_tools();		
		OWLAPI_tools Onto3 = new OWLAPI_tools();
		
		Onto1.readOnto(uri1);
		Onto2.readOnto(uri2);
		Onto3.readOnto(uri3);
		
		
		int SynNum=0;
		
		ArrayList<String>	concepts1= Onto1.getConcepts();
		Map<String, Set<String>> Concept_AnnoationsSet_Map1 = Onto1.getConcept_AllAnnoationsSet();
		System.out.println("FMA本体概念的个数为："+ concepts1.size());
		
		for(String concept:Concept_AnnoationsSet_Map1.keySet())
		{
			SynNum=SynNum+Concept_AnnoationsSet_Map1.get(concept).size();		
		}
		System.out.println("FMA本体的同义词个数："+ SynNum);
		
		
		ArrayList<String>	concepts2= Onto2.getConcepts();
		Map<String, Set<String>> Concept_AnnoationsSet_Map2 = Onto2.getConcept_AllAnnoationsSet();
		System.out.println("NCI本体概念的个数为："+ concepts2.size());
		SynNum=0;
		for(String concept:Concept_AnnoationsSet_Map2.keySet())
		{
			SynNum=SynNum+Concept_AnnoationsSet_Map2.get(concept).size();		
		}
		System.out.println("NCI本体的同义词个数为："+ SynNum);
		
		ArrayList<String>	concepts3= Onto3.getConcepts();
		Map<String, Set<String>> Concept_AnnoationsSet_Map3 = Onto3.getConcept_AllAnnoationsSet();
		System.out.println("SNOMED本体概念的个数为："+ concepts3.size());
		SynNum=0;
		for(String concept:Concept_AnnoationsSet_Map3.keySet())
		{
			SynNum=SynNum+Concept_AnnoationsSet_Map3.get(concept).size();		
		}
		System.out.println("SNOMED本体的同义词个数为："+ SynNum);
	}

}
