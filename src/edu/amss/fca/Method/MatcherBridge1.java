package edu.amss.fca.Method;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.semanticweb.owlapi.model.IRI;

import eu.sealsproject.platform.res.domain.omt.IOntologyMatchingToolBridge;
import eu.sealsproject.platform.res.tool.api.ToolBridgeException;
import eu.sealsproject.platform.res.tool.api.ToolException;
import eu.sealsproject.platform.res.tool.api.ToolType;
import eu.sealsproject.platform.res.tool.impl.AbstractPlugin;



public class MatcherBridge1 extends AbstractPlugin implements IOntologyMatchingToolBridge {
	static String pathString="";
	
	public URL align(URL source, URL target) throws ToolBridgeException, ToolException {
		FCA_Map fca_Map;
		URL url_alignment;

		try {

			fca_Map = new FCA_Map();
			System.out.println("**************************");
			System.out.println(source);
			System.out.println(target);
			System.out.println("**************************");

			/*String path1="Ontologies/cmt.owl";
			String path2="Ontologies/confOf.owl";*/
			
			//url_alignment = fca_Map.returnAlignmentFile(source, target);

			url_alignment = fca_Map.returnAlignmentFile(source, target,pathString);
			
			return url_alignment;

		}

		catch (IOException e) {
			throw new ToolBridgeException("Cannot create file for resulting alignment", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ToolException("Error extracting/cleaning/storing mappings with FCA_Map: ");

		}
	}

	
	public URL align(URL source, URL target, URL inputAlignment) throws ToolBridgeException, ToolException {
		throw new ToolException("functionality of called method is not supported");
	}

	
	public boolean canExecute() {
		return true;
	}

	
	public ToolType getType() {
		return ToolType.OntologyMatchingTool;
	}

	public static void main(String[] args) {
		MatcherBridge1 m = new MatcherBridge1();

		//Parameters.print_output=true;

//		String uri1 = "http://repositories.seals-project.eu/tdrs/testdata/persistent/2015benchmarks-biblio/2015benchmarks-biblio-r1/suite/201/component/source/";
//		String uri2 = "http://repositories.seals-project.eu/tdrs/testdata/persistent/2015benchmarks-biblio/2015benchmarks-biblio-r1/suite/201/component/target/";

		/*String uri1="http://repositories.seals-project.eu/tdrs/testdata/persistent/2015benchmarks-ifc/2015benchmarks-ifc-r1/suite/101/component/source/";
		String uri2="http://repositories.seals-project.eu/tdrs/testdata/persistent/2015benchmarks-ifc/2015benchmarks-ifc-r1/suite/101/component/target/";*/

//		String uri1="http://repositories.seals-project.eu/tdrs/testdata/persistent/conference/conference-v1/suite/cmt-ekaw/component/source/";
//		String uri2="http://repositories.seals-project.eu/tdrs/testdata/persistent/conference/conference-v1/suite/cmt-ekaw/component/target/";

//		String uri1="http://repositories.seals-project.eu/tdrs/testdata/persistent/anatomy_track/anatomy_track-anatomy_2015/suite/mouse-human-suite/component/source/";
//		String uri2="http://repositories.seals-project.eu/tdrs/testdata/persistent/anatomy_track/anatomy_track-anatomy_2015/suite/mouse-human-suite/component/target/";

		/*String uri1="http://repositories.seals-project.eu/tdrs/testdata/persistent/largebio/largebio-fma_nci_small_2015/suite/small-fma-nci/component/source/";
		String uri2="http://repositories.seals-project.eu/tdrs/testdata/persistent/largebio/largebio-fma_nci_small_2015/suite/small-fma-nci/component/target/";*/

		/*String uri1 = "file:Ontologies/101onto.rdf";
		String uri2 = "file:Ontologies/201-2onto.rdf";*/

		/*String uri1 = "file:Ontologies/cmt.owl";
		String uri2 = "file:Ontologies/confOf.owl";*/
		
		long tic=System.currentTimeMillis();
		
		/*String []Ontology1={"cmt","Conference","confOf","edas","ekaw","iasted","sigkdd"};
		String []Ontology2={"cmt","Conference","confOf","edas","ekaw","iasted","sigkdd"};
		String []Ontology1={"cmt"};
		String []Ontology2={"cmt","Conference","confOf"};
		for (int x=0;x<Ontology1.length;x++)
		{
			String ontologyName1 = Ontology1[x];
			for(int y=x+1;y<Ontology2.length;y++)
			{		
				String ontologyName2 = Ontology2[y];	
				String ontologyName1="confOf";
				String ontologyName2="edas";*/
				
				/*String ontologyName1="mouse";			
				String ontologyName2="human";
				pathString="alignment/FCA_Map-"+ontologyName1+"-"+ontologyName2;*/
				
				
				//String ontologyName1="oaei_FMA_whole_ontology";
				/*String ontologyName1="oaei_FMA_small_overlapping_nci";			
				String ontologyName2="oaei_NCI_small_overlapping_fma";
				pathString="alignment/FCA_Map-"+"FMA_small"+"-"+"NCI_small";*/
				
			/*	String ontologyName1="oaei_FMA_small_overlapping_snomed";			
				String ontologyName2="oaei_SNOMED_small_overlapping_fma";
				pathString="alignment/FCA_Map-"+"FMA_small"+"-"+"SNOMED_small";*/
				
				/*String ontologyName1="oaei_SNOMED_small_overlapping_nci";			
				String ontologyName2="oaei_NCI_small_overlapping_snomed";
				pathString="alignment/FCA_Map-"+"SNOMED_small"+"-"+"NCI_small";*/
				

				/*String uri1="file:Ontologies/"+ontologyName1+".owl";
				String uri2="file:Ontologies/"+ontologyName2+".owl";*/
				
				//pathString="alignment/FCA_Map-"+ontologyName1+"-"+ontologyName2;
				
		
		/*	String ontologyName1="iasted";
			String ontologyName2="sigkdd";
			
			String ontologyName1="oaei2014_FMA_small_overlapping_nci";
			String ontologyName2="oaei2014_NCI_small_overlapping_fma";
			
			String uri1="file:Ontologies/"+ontologyName1+".owl";
			String uri2="file:Ontologies/"+ontologyName2+".owl";
		
			pathString="alignment/FCA_Map-"+ontologyName1+"-"+ontologyName2;
			//pathString="alignment/FCA_Map-"+ontologyName1+"-"+ontologyName2;
*/			
			/*String uri1 = "file:Ontologies/cmt.owl";
			String uri2 = "file:Ontologies/confOf.owl";*/
		
		/*String uri1 = "file:Ontologies/HP.rdf";
		String uri2 = "file:Ontologies/MP.rdf";
		pathString="alignment/FCA_Map-HP-MP";*/
		
		/*String uri1 = "file:Ontologies/DOID.rdf";
		String uri2 = "file:Ontologies/ORDO.rdf";
		pathString="alignment/FCA_Map-DOID-ORDO";*/
		
		/*String uri1 = "file:Ontologies/hp.rdf";
		String uri2 = "file:Ontologies/mp.rdf";
		pathString="alignment/FCA_Map-hp-mp-2017";*/
		
		/*String uri1 = "file:Ontologies/doid.owl";
		String uri2 = "file:Ontologies/ordo.owl";
		pathString="alignment/FCA_Map-diod-ordo-2017";*/
			
		/*String uri1 = "file:Ontologies/mouse.owl";
		String uri2 = "file:Ontologies/human.owl";
		pathString="alignment/FCA_Map-Test00";*/
		
		String uri1 = "file:Ontologies/oaei_FMA_small_overlapping_nci.owl";
		String uri2 = "file:Ontologies/oaei_NCI_small_overlapping_fma.owl";
		pathString="alignment/FCA_Map-Test11";
				
		/*String uri1 = "file:Ontologies/oaei_FMA_small_overlapping_snomed.owl";
		String uri2 = "file:Ontologies/oaei_SNOMED_small_overlapping_fma.owl";
		pathString="alignment/FCA_Map-Test22";*/
		
		/*String uri1 = "file:Ontologies/oaei_SNOMED_small_overlapping_nci.owl";
		String uri2 = "file:Ontologies/oaei_NCI_small_overlapping_snomed.owl";
		pathString="alignment/FCA_Map-Test33";*/
			
		/*String uri1="file:Ontologies/oaei_FMA_whole_ontology.owl";			
		String uri2="file:Ontologies/oaei_NCI_whole_ontology.owl";
		pathString="alignment/FCA_Map-whole-Test11";*/
			
		/*String uri1="file:Ontologies/oaei_FMA_whole_ontology.owl";			
		String uri2="file:Ontologies/oaei_SNOMED_extended_overlapping_fma_nci.owl";
		pathString="alignment/FCA_Map-whole-Test22";*/
		
		/*String uri1="file:Ontologies/oaei_SNOMED_extended_overlapping_fma_nci.owl";			
		String uri2="file:Ontologies/oaei_NCI_whole_ontology.owl";
		pathString="alignment/FCA_Map-whole-Test33";*/
		


		
//		String uri1 = args[0];
//		String uri2 = args[1];

				try {
					//long tic=System.currentTimeMillis();
					m.align(IRI.create(uri1).toURI().toURL(), IRI.create(uri2)
							.toURI().toURL());
					
					/*long toc=System.currentTimeMillis();
					System.out.println((toc-tic)/1000+"s");*/
				} 
				catch (ToolException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (ToolBridgeException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (MalformedURLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//}
		//}
		long toc=System.currentTimeMillis();
		
		System.out.println("The time of whole repair is "+ (toc-tic)/1000+"  s");
	}

}
