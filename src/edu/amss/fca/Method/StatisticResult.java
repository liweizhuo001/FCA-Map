package edu.amss.fca.Method;


import java.io.IOException;
import java.util.ArrayList;

import edu.amss.fca.Tools.EvaluationLargeBio;
import edu.amss.fca.Tools.MappingInfo;
import edu.amss.fca.Tools.Evaluation;

public class StatisticResult {
	public static void main(String args[]) throws IOException
	{
		/*String mappingsPath = "alignment/FCA_Map-HP-MP-step2_validated.rdf";
		String mappingsPath2 = "alignment/FCA_Map-HP-MP-step2_validated.rdf";
		//String mappingsPath2 = "alignment/FCA_Map-HP-MP-step1_lexical.rdf";
		String referencePath = "ReferenceAlignment/HP_MP_baseline.rdf";*/
		
		/*String mappingsPath = "alignment/FCA_Map-hp-mp-2017-step3_additional.rdf";
		String mappingsPath2 = "alignment/FCA_Map-hp-mp-2017-step2_validated.rdf";
		//String mappingsPath2 = "alignment/FCA_Map-hp-mp-2017-step1_lexical.rdf";
		String referencePath = "ReferenceAlignment/HP_MP-2017.rdf";*/
				
		/*String mappingsPath = "alignment/FCA_Map-DOID-ORDO-step2_validated.rdf";
		//String mappingsPath2 = "alignment/FCA_Map-DOID-ORDO-step2_validated.rdf";
		String mappingsPath2 = "alignment/FCA_Map-DOID-ORDO.rdf";
		String referencePath = "ReferenceAlignment/DOID_ORDO_baseline.rdf";*/
		
		/*String mappingsPath = "alignment/FCA_Map-diod-ordo-2017-step1_lexical.rdf";
		String mappingsPath2 = "alignment/FCA_Map-diod-ordo-2017-step2_validated.rdf";
		//String mappingsPath2 = "alignment/FCA_Map-diod-ordo-2017.rdf";
		String referencePath = "ReferenceAlignment/DOID_ORDO-2017.rdf";*/
		
		String mappingsPath = "alignment/FCA_Map-mouse-human-step3_additional.rdf";
		String mappingsPath2 = "alignment/FCA_Map-mouse-human-step2_validated.rdf";
		String referencePath = "ReferenceAlignment/reference_2015.rdf";
		
	/*	String mappingsPath = "alignment/FCA_Map-mouse-human-step2_validated.rdf";
		String mappingsPath2 = "alignment/FCA_Map-mouse-human-step3_additional.rdf";
		String referencePath = "ReferenceAlignment/reference_2015.rdf";*/
		
	/*	String mappingsPath = "alignment/FCA_Map-FMA_small-NCI_small-step1_lexical.rdf";
		String mappingsPath2 = "alignment/FCA_Map-FMA_small-NCI_small-step2_validated.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*String mappingsPath = "alignment/FCA_Map-FMA_small-SNOMED_small-step3_additional.rdf";
		String mappingsPath2 = "alignment/FCA_Map-FMA_small-SNOMED_small-step2_validated.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2SNOMED_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*String mappingsPath = "alignment/FCA_Map-SNOMED_small-NCI_small-step3_additional.rdf";
		String mappingsPath2 = "alignment/FCA_Map-SNOMED_small-NCI_small-step2_validated.rdf";
		String referencePath = "ReferenceAlignment/oaei_SNOMED2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		MappingInfo MappingInformation=new MappingInfo(mappingsPath);	
		ArrayList<String> mappings= new ArrayList<String>();
		mappings=MappingInformation.getMappings();
		System.out.println(mappings.size());
		
		MappingInfo MappingInformation2=new MappingInfo(mappingsPath2);	
		ArrayList<String> mappings2= new ArrayList<String>();
		mappings2=MappingInformation2.getMappings();
		System.out.println(mappings2.size());
		
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
		
		System.out.println("--------------------------------------------------------");	
		mappings.removeAll(mappings2);
		System.out.println("The number of new generated mappings is " + mappings.size());
		EvaluationLargeBio cBefore2 = new EvaluationLargeBio(mappings, referenceMappings);
		System.out.println("The number of correct mappings in new generated mappings is " + cBefore2.getCorrectAlignment());
		
	}
}
