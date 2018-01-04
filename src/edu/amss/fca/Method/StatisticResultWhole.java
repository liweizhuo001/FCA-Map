package edu.amss.fca.Method;


import java.io.IOException;
import java.util.ArrayList;

import edu.amss.fca.Tools.EvaluationLargeBio;
import edu.amss.fca.Tools.MappingInfo;
import edu.amss.fca.Tools.Evaluation;

public class StatisticResultWhole {
	public static void main(String args[]) throws IOException
	{	
		
		
		String mappingsPath = "alignment/FCA_Map-mouse-human.rdf";
		//String mappingsPath = "alignment/FCA_Map-Test0.rdf";
		String referencePath = "ReferenceAlignment/reference_2015.rdf";
		
		/*String mappingsPath = "alignment/FCA_Map-FMA_small-NCI_small.rdf";
		//String mappingsPath = "alignment/FCA_Map-Test1.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*String mappingsPath = "alignment/FCA_Map-FMA_small-SNOMED_small.rdf";
		//String mappingsPath = "alignment/FCA_Map-Test2.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2SNOMED_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*String mappingsPath = "alignment/FCA_Map-SNOMED_small-NCI_small.rdf";
		//String mappingsPath = "alignment/FCA_Map-Test3.rdf";
		String referencePath = "ReferenceAlignment/oaei_SNOMED2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
			
		/*String mappingsPath = "alignment/FCA_Map-FMA-NCI.rdf";
		//String mappingsPath = "alignment/FCA_Map-whole-test1.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*String mappingsPath = "alignment/FCA_Map-FMA-SNOMED.rdf";
		//String mappingsPath = "alignment/FCA_Map-whole-Test2.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2SNOMED_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*//String mappingsPath = "alignment/FCA_Map-SNOMED-NCI.rdf";
		String mappingsPath = "alignment/FCA_Map-whole-Test3.rdf";
		String referencePath = "ReferenceAlignment/oaei_SNOMED2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		
		
		MappingInfo MappingInformation=new MappingInfo(mappingsPath);	
		ArrayList<String> mappings= new ArrayList<String>();
		mappings=MappingInformation.getMappings();
		System.out.println(mappings.size());
		
		
		ArrayList<String> referenceMappings= new ArrayList<String>();
		MappingInfo ReferenceInformation=new MappingInfo(referencePath);
		referenceMappings=ReferenceInformation.getMappings();
		
		System.out.println(referenceMappings.size());
		EvaluationLargeBio cBefore = new EvaluationLargeBio(mappings, referenceMappings);
		
		System.out.println("--------------------------------------------------------");
		System.out.println("before debugging (pre, rec, f): " + cBefore.toShortDesc());
		System.out.println("The number of total correct mappings in alignment:  " + cBefore.getCorrectAlignment());
		System.out.println("The number of total unknow mappings in alignment:  " +cBefore.getUnknownAlignment());
		System.out.println("The number of total incorrect mappings in alignment:  " + cBefore.getInCorrectAlignment());		
		System.out.println("The number of total mappings in alignment:  " + cBefore.getMatcherAlignment());
		
		
		
	}
}
