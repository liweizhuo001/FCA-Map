package edu.amss.fca.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.ibm.icu.math.BigDecimal;


public class MappingInfoTemporary {
	private ArrayList<String> mappings; 
	private ArrayList<ArrayList<String>> MinimcalConflictSet; 
	OntModel ontology = ModelFactory.createOntologyModel();
	
	public MappingInfoTemporary(String MappingPath) throws IOException 
	{
		if(MappingPath.contains(".rdf"))
		{
			mappings=getReference(MappingPath);
		}
		else
		{
			BufferedReader Alignment = new BufferedReader(new FileReader(new File(MappingPath)));
			mappings = new ArrayList<String>();
			String lineTxt = null;
			while ((lineTxt = Alignment.readLine()) != null) {
				String line = lineTxt.trim(); // 鍘绘帀瀛楃涓查浣嶇殑绌烘牸锛岄伩鍏嶅叾绌烘牸閫犳垚鐨勯敊璇�
				// line=line.toLowerCase();//鍏ㄩ儴鍙樻垚灏忓啓
				mappings.add(line);
			}
		}
		if(mappings.size()==0)
		{
			mappings=getReference2(MappingPath);
		}
	}
	

	
	public ArrayList<String> getMappings()
	{
		return mappings;
	}
	
	public ArrayList<String> getReference(String alignmentFile)
	{
		  ArrayList<String> references=new ArrayList<String>();  
		    Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open( alignmentFile );
	        if (in == null) {
	        	System.out.println("File: " + alignmentFile + " not found!");
	            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
	        }
	        model.read(in,"");
			//model.read(in,null);
	        //瑙ｆ瀽鏂瑰紡1(閽堝YAM++)
			/*Property alignmententity1, alignmententity2;
			alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1");
			alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2");
			Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#measure");
			Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#relation");*/
			//OntClass temp;
	        //瑙ｉ噴鏂瑰紡2(閽堝AML++,reference alignments)
		    Property alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1");
			Property alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2");//alignment
			Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentmeasure");
			Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentrelation");

	        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	//涓よ�呮柟娉曟槸涓�鏍风殑 
		    Resource temp;
			while(resstmt.hasNext()){
				temp = resstmt.next();//temp鏄笁鍏冪粍
				String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();//鑾峰彇鏈綋1鐨勬湰浣�
				String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();//鑾峰彇鏈綋2鐨勬湰浣�
				String Confidence="1";
				if(temp.getProperty(value)!=null)
				 Confidence=temp.getProperty(value).getObject().asLiteral().getString();//鑾峰彇淇″康鍊�
				String Relation="=";
				if(temp.getProperty(relation)!=null)
				  Relation=temp.getProperty(relation).getObject().toString();//鑾峰彇鍖归厤鐨勫叧绯�
				
				//姣旇緝绗ㄧ殑鏂规硶
				/*String Confidence=temp.getProperty(value).getLiteral().toString();
				Confidence=Confidence.replace("^^xsd:float", "").replace("^^http://www.w3.org/2001/XMLSchema#float", "");
				System.out.println(Confidence);*/
				
				
			//	System.out.println(Relation);
			//	System.out.println(entity1+" "+entity2);
			//	System.out.println(entity1+" "+entity2+" "+Relation+" "+Confidence);
				
				//杈撳嚭鎵�鏈夌殑涓夊厓缁�
		/*		StmtIterator stmt = model.listStatements();
				while(stmt.hasNext()){
					System.out.println(stmt.next());
				}*/
				//entity1=entity1.replace("-", "_");//涓轰簡鐢诲浘鏂逛究锛屽皢'-'鏇挎崲鎴�'_'		
				//entity2=entity2.replace("-", "_");//涓轰簡鐢诲浘鏂逛究锛屽皢'-'鏇挎崲鎴�'_'	
				BigDecimal   b   =   new   BigDecimal(Double.parseDouble(Confidence));  //鍥涜垗浜斿叆鐨勬柟寮�
				Double confidence =   b.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();  
				
			/*	if(Relation.equals("?"))
					continue;*/
			/*	if(!references.contains(entity1+","+entity2+","+Relation+","+confidence))
					references.add(entity1+","+entity2+","+Relation+","+confidence);//缁熶竴杞寲鎴愬皬鍐�
*/				
				if(Relation.equals("&gt;"))
					continue;
				references.add(entity2+","+entity1+","+Relation);//缁熶竴杞寲鎴愬皬鍐�
				//references.add(entity1+" = "+entity2);//缁熶竴杞寲鎴愬皬鍐�
			}
			return references;
	}
	
	public ArrayList<String> getReference2(String alignmentFile)
	{
		  ArrayList<String> references=new ArrayList<String>();  
		    Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open( alignmentFile );
	        if (in == null) {
	        	System.out.println("File: " + alignmentFile + " not found!");
	            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
	        }
	        model.read(in,"");
			//model.read(in,null);
	        //瑙ｆ瀽鏂瑰紡1(閽堝YAM++)
			Property alignmententity1, alignmententity2;
			alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1");
			alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2");
			Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#measure");
			Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#relation");
			//OntClass temp;
	        //瑙ｉ噴鏂瑰紡2(閽堝AML++,reference alignments)
		   /* Property alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1");
			Property alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2");//alignment
			Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentmeasure");
			Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentrelation");*/

	        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	//涓よ�呮柟娉曟槸涓�鏍风殑 
		    Resource temp;
			while(resstmt.hasNext()){
				temp = resstmt.next();//temp鏄笁鍏冪粍
				String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();//鑾峰彇鏈綋1鐨勬湰浣�
				String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();//鑾峰彇鏈綋2鐨勬湰浣�
				String Confidence="1";
				if(temp.getProperty(value)!=null)
					 Confidence=temp.getProperty(value).getObject().asLiteral().getString();//鑾峰彇淇″康鍊�
					String Relation="=";
				if(temp.getProperty(relation)!=null)
					  Relation=temp.getProperty(relation).getObject().toString();//鑾峰彇鍖归厤鐨勫叧绯�
				
				//姣旇緝绗ㄧ殑鏂规硶
				/*String Confidence=temp.getProperty(value).getLiteral().toString();
				Confidence=Confidence.replace("^^xsd:float", "").replace("^^http://www.w3.org/2001/XMLSchema#float", "");
				System.out.println(Confidence);*/
				
				
			//	System.out.println(Relation);
			//	System.out.println(entity1+" "+entity2);
			//	System.out.println(entity1+" "+entity2+" "+Relation+" "+Confidence);
				
				//杈撳嚭鎵�鏈夌殑涓夊厓缁�
		/*		StmtIterator stmt = model.listStatements();
				while(stmt.hasNext()){
					System.out.println(stmt.next());
				}*/
				//entity1=entity1.replace("-", "_");//涓轰簡鐢诲浘鏂逛究锛屽皢'-'鏇挎崲鎴�'_'		
				//entity2=entity2.replace("-", "_");//涓轰簡鐢诲浘鏂逛究锛屽皢'-'鏇挎崲鎴�'_'	
				BigDecimal   b   =   new   BigDecimal(Double.parseDouble(Confidence));  //鍥涜垗浜斿叆鐨勬柟寮�
				Double confidence =   b.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue();  
				
			/*	if(Relation.equals("?"))
					continue;*/
				/*if(!references.contains(entity1+","+entity2+","+Relation+","+confidence))
					references.add(entity1+","+entity2+","+Relation+","+confidence);*/
				if(Relation.equals("&gt;"))
					continue;
					references.add(entity2+","+entity1+","+Relation);
				//references.add(entity1+" = "+entity2);//缁熶竴杞寲鎴愬皬鍐�
			}
			return references;
	}
}
