package edu.amss.fca.Method;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import edu.amss.fca.Tools.EvaluationLargeBio;
import edu.amss.fca.Tools.MappingInfo;
import edu.amss.fca.Tools.Evaluation;
import edu.amss.fca.Tools.MappingInfoTemporary;

public class StatisticResultForAllMatchers {
	public static void main(String args[]) throws IOException
	{
		/*String FCA_Map = "alignment/anatomy/FCA_Map.rdf";
		
		String Alin = "alignment/anatomy/Alin.rdf";
		String AML = "alignment/anatomy/AML.rdf";
		String CroMatcher = "alignment/anatomy/CroMatcher.rdf";
		String DKP_AOM_Lite = "alignment/anatomy/DKP-AOM-Lite.rdf";
		String DKP_AOM = "alignment/anatomy/DKP-AOM.rdf";		
		String Lily = "alignment/anatomy/Lily.rdf";
		String LogMap = "alignment/anatomy/LogMap.rdf";
		String LogMapBio = "alignment/anatomy/LogMapBio.rdf";
		String LogMapLite = "alignment/anatomy/LogMapLite.rdf";
		String LPHOM = "alignment/anatomy/LPHOM.rdf";
		String LYAM = "alignment/anatomy/LYAM.rdf";
		String XMap = "alignment/anatomy/XMap.rdf";
		String referencePath = "ReferenceAlignment/reference_2015.rdf";*/
		
	/*	String FCA_Map = "alignment/FMA-NCI-small/FCA_Map-largebio-fma_nci_small_2016.rdf";
		
		String Alin = "alignment/FMA-NCI-small/Alin-largebio-fma_nci_small_2016.rdf";
		String AML = "alignment/FMA-NCI-small/AML-largebio-fma_nci_small_2016.rdf";
		String DKP_AOM_Lite = "alignment/FMA-NCI-small/DKP-AOM-largebio-fma_nci_small_2016.rdf";
		String DKP_AOM = "alignment/FMA-NCI-small/DKP-AOM-Lite-largebio-fma_nci_small_2016.rdf";		
		String Lily = "alignment/FMA-NCI-small/Lily-largebio-fma_nci_small_2016.rdf";
		String LogMap = "alignment/FMA-NCI-small/LogMap-largebio-fma_nci_small_2016.rdf";
		String LogMapBio = "alignment/FMA-NCI-small/LogMapBio-largebio-fma_nci_small_2016.rdf";
		String LogMapLite = "alignment/FMA-NCI-small/LogMapLite-largebio-fma_nci_small_2016.rdf";
		String LYAM = "alignment/FMA-NCI-small/LYAM-largebio-fma_nci_small_2016.rdf";
		String XMap = "alignment/FMA-NCI-small/XMap-largebio-fma_nci_small_2016.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*String FCA_Map = "alignment/FMA-NCI-whole/FCA_Map-FMA-NCI.rdf";
		
		String AML = "alignment/FMA-NCI-whole/AML-largebio-fma_nci_whole_2016.rdf";
		String LogMap = "alignment/FMA-NCI-whole/LogMap-largebio-fma_nci_whole_2016.rdf";
		String LogMapBio = "alignment/FMA-NCI-whole/LogMapBio-largebio-fma_nci_whole_2016.rdf";
		String LogMapLite = "alignment/FMA-NCI-whole/LogMapLite-largebio-fma_nci_whole_2016.rdf";
		String XMap = "alignment/FMA-NCI-whole/XMap-largebio-fma_nci_whole_2016.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2NCI_UMLS_mappings_with_flagged_repairs.rdf"*/;
		
/*		String FCA_Map = "alignment/FMA-SNOMED-small/FCA_Map-largebio-fma_snomed_small_2016.rdf";
		
		String AML = "alignment/FMA-SNOMED-small/AML-largebio-fma_snomed_small_2016.rdf";
		String LogMap = "alignment/FMA-SNOMED-small/LogMap-largebio-fma_snomed_small_2016.rdf";
		String LogMapBio = "alignment/FMA-SNOMED-small/LogMapBio-largebio-fma_snomed_small_2016.rdf";
		String LogMapLite = "alignment/FMA-SNOMED-small/LogMapLite-largebio-fma_snomed_small_2016.rdf";
		String XMap = "alignment/FMA-SNOMED-small/XMap-largebio-fma_snomed_small_2016.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2SNOMED_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		/*String FCA_Map = "alignment/FMA-SNOMED-whole/FCA_Map-FMA-SNOMED.rdf";
		
		String AML = "alignment/FMA-SNOMED-whole/AML-largebio-fma_snomed_whole_2016.rdf";
		String LogMap = "alignment/FMA-SNOMED-whole/LogMap-largebio-fma_snomed_whole_2016.rdf";
		String LogMapBio = "alignment/FMA-SNOMED-whole/LogMapBio-largebio-fma_snomed_whole_2016.rdf";
		String LogMapLite = "alignment/FMA-SNOMED-whole/LogMapLite-largebio-fma_snomed_whole_2016.rdf";
		String XMap = "alignment/FMA-SNOMED-whole/XMap-largebio-fma_snomed_whole_2016.rdf";
		String referencePath = "ReferenceAlignment/oaei_FMA2SNOMED_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		String FCA_Map = "alignment/SNOMED-NCI-small/FCA_Map-SNOMED_small-NCI_small-step3_additional.rdf";
		//注意，这里后续的alignment 的匹配情况是NCI-SNOMED，需要在读入本体时，用MappingInfoTemporary进行调整
		String AML = "alignment/SNOMED-NCI-small/AML-largebio-snomed_nci_small_2016.rdf";
		String LogMap = "alignment/SNOMED-NCI-small/LogMap-largebio-snomed_nci_small_2016.rdf";
		String LogMapBio = "alignment/SNOMED-NCI-small/LogMapBio-largebio-snomed_nci_small_2016.rdf";
		String LogMapLite = "alignment/SNOMED-NCI-small/LogMapLite-largebio-snomed_nci_small_2016.rdf";
		String XMap = "alignment/SNOMED-NCI-small/XMap-largebio-snomed_nci_small_2016.rdf";
		String referencePath = "ReferenceAlignment/oaei_SNOMED2NCI_UMLS_mappings_with_flagged_repairs.rdf";
		
/*		String FCA_Map = "alignment/SNOMED-NCI-whole/FCA_Map-SNOMED-NCI.rdf";
		
		String AML = "alignment/SNOMED-NCI-whole/AML-largebio-snomed_nci_whole_2016.rdf";
		String LogMap = "alignment/SNOMED-NCI-whole/LogMap-largebio-snomed_nci_whole_2016.rdf";
		String LogMapBio = "alignment/SNOMED-NCI-whole/LogMapBio-largebio-snomed_nci_whole_2016.rdf";
		String LogMapLite = "alignment/SNOMED-NCI-whole/LogMapLite-largebio-snomed_nci_whole_2016.rdf";
		//String XMap = "alignment/SNOMED-NCI-whole/XMap-largebio-snomed_nci_whole_2016.rdf";
		String referencePath = "ReferenceAlignment/oaei_SNOMED2NCI_UMLS_mappings_with_flagged_repairs.rdf";*/
		
		MappingInfo MappingInformation=new MappingInfo(FCA_Map);	
		ArrayList<String> mappings=MappingInformation.getMappings();
		System.out.println(mappings.size());
		
		/*MappingInfo MappingInformation1=new MappingInfo(Alin);	
		ArrayList<String> mappings1=MappingInformation1.getMappings();
		System.out.println("1	:"+mappings1.size());*/
		
		MappingInfo MappingInformation2=new MappingInfo(AML);	
		ArrayList<String> mappings2=MappingInformation2.getMappings();
		System.out.println("2	:"+mappings2.size());
		
		/*MappingInfo MappingInformation3=new MappingInfo(CroMatcher);	
		ArrayList<String> mappings3=MappingInformation3.getMappings();
		System.out.println("3	:"+mappings3.size());
		
		MappingInfo MappingInformation4=new MappingInfo(DKP_AOM_Lite);	
		ArrayList<String> mappings4=MappingInformation4.getMappings();
		System.out.println("4	:"+mappings4.size());
		
		MappingInfo MappingInformation5=new MappingInfo(DKP_AOM);	
		ArrayList<String> mappings5=MappingInformation5.getMappings();
		System.out.println("5	:"+mappings5.size());
		
		MappingInfo MappingInformation6=new MappingInfo(Lily);	
		ArrayList<String> mappings6=MappingInformation6.getMappings();
		System.out.println("6	:"+mappings6.size());*/
		
		MappingInfo MappingInformation7=new MappingInfo(LogMap);	
		ArrayList<String> mappings7=MappingInformation7.getMappings();
		System.out.println("7	:"+mappings7.size());
		
		MappingInfo MappingInformation8=new MappingInfo(LogMapBio);	
		ArrayList<String> mappings8=MappingInformation8.getMappings();
		System.out.println("8	:"+mappings8.size());
		
		MappingInfo MappingInformation9=new MappingInfo(LogMapLite);	
		ArrayList<String> mappings9=MappingInformation9.getMappings();
		System.out.println("9	:"+mappings9.size());
		
		/*MappingInfo MappingInformation10=new MappingInfo(LPHOM);	
		ArrayList<String> mappings10=MappingInformation10.getMappings();
		System.out.println("10	:"+mappings10.size());

		MappingInfo MappingInformation11=new MappingInfo(LYAM);	
		ArrayList<String> mappings11=MappingInformation11.getMappings();
		System.out.println("11	:"+mappings11.size());*/
		
		MappingInfo MappingInformation12=new MappingInfo(XMap);	
		ArrayList<String> mappings12=MappingInformation12.getMappings();
		System.out.println("12	:"+mappings12.size());
		
		ArrayList<String> referenceMappings= new ArrayList<String>();
		MappingInfo ReferenceInformation=new MappingInfo(referencePath);
		referenceMappings=ReferenceInformation.getMappings();	
		System.out.println(referenceMappings.size());

		
		System.out.println("--------------------------------------------------------");	
		//mappings.removeAll(mappings1);
		mappings.removeAll(mappings2);
		//mappings.removeAll(mappings3);
		//mappings.removeAll(mappings4);
		//mappings.removeAll(mappings5);
		//mappings.removeAll(mappings6);
		//mappings2.removeAll(mappings);
		mappings2.removeAll(mappings7);
		mappings2.removeAll(mappings8);
		mappings2.removeAll(mappings9);
		//mappings.removeAll(mappings10);
		//mappings.removeAll(mappings11);
		mappings2.removeAll(mappings12);
		
		System.out.println("The number of new generated mappings is " + mappings.size());
		//Evaluation  DBefore = new Evaluation(mappings2, referenceMappings);		
		EvaluationLargeBio cBefore = new EvaluationLargeBio(mappings, referenceMappings);
		System.out.println("before debugging (pre, rec, f): " + cBefore.toShortDesc());	
		System.out.println("The number of correct mappings in new generated mappings is " + cBefore.getCorrectAlignment());
		
	}
}
