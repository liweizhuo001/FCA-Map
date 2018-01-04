/*******************************************************************************
 * Copyright 2012 by the Department of Computer Science (University of Oxford)
 * 
 *    This file is part of LogMap.
 * 
 *    LogMap is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 * 
 *    LogMap is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 * 
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with LogMap.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.amss.fca.Tools;


import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ibm.icu.text.MessagePattern.Part;


public class OAEIAlignmentOutputMultiple {
	
	File alignmentFile;
	FileWriter fw;
	//String URI1="";
	//String URI2="";
	ArrayList<Map<String, String>> ontoIRIMaps=new ArrayList<Map<String, String>>();
	
	/**
	 * Same format than OAEIRDFAlignmentFormat, but with different ouput.
	 * SEALS requires the creation of a temporal file and returning its URL
	 * @param name
	 * @throws Exception 
	 */
	public OAEIAlignmentOutputMultiple(String pathname, ArrayList<Map<String, String>> ontoIRIMaps) throws Exception
	{	
		setOutput(pathname);
		printHeader();
		this.ontoIRIMaps=ontoIRIMaps;
	}
	

	protected void setOutput(String pathname) throws Exception {
		alignmentFile=new File(pathname+".rdf");
		fw = new FileWriter(alignmentFile);
	}

	private void printHeader() throws Exception{
		fw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		
		fw.write("<rdf:RDF xmlns=\"http://knowledgeweb.semanticweb.org/heterogeneity/alignment\"\n"); 
		fw.write("\txmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"); 
		fw.write("\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\">\n");
		
		fw.write("\n");
		
		fw.write("\t<Alignment>\n");
		fw.write("\t<xml>yes</xml>\n");
		fw.write("\t<level>0</level>\n");
		fw.write("\t<type>??</type>\n");	
	}
	
	private void printTail() throws Exception
	{		
		fw.write("\t</Alignment>\n");
		fw.write("</rdf:RDF>\n");	
	}
	
	
	public void addMapping2Output(String iri_strs[],String in_str3) throws Exception
	{	
		fw.write("\t<map>\n");
		fw.write("\t\t<Cell>\n");
		for(int i=0;i<iri_strs.length;i++)
		{
			String iri=ontoIRIMaps.get(i).get(iri_strs[i].substring(2)); //这里把前缀给去掉了。
			//fw.write("\t\t\t<entity1 rdf:resource=\""+iri+"\""+"/>\n");
			fw.write("\t\t\t<entity"+(i+1)+" rdf:resource=\""+iri+"\""+"/>\n");
		}
		/*String iri1=IRIMap1.get(iri_str1);
		String iri2=IRIMap2.get(iri_str2);
		fw.write("\t\t\t<entity1 rdf:resource=\""+iri1+"\""+"/>\n");
		fw.write("\t\t\t<entity2 rdf:resource=\""+iri2+"\""+"/>\n");*/
			
		fw.write("\t\t\t<measure rdf:datatype=\"xsd:float\">" + Double.parseDouble(in_str3) + "</measure>\n");
		
		fw.write("\t\t\t<relation>=</relation>\n");
			
		fw.write("\t\t</Cell>\n");
		fw.write("\t</map>\n");
	}

	
	public void saveOutputFile() throws Exception{		
		printTail();
		fw.flush();
		fw.close();
		
	}
	
	public URL returnAlignmentFile() throws Exception{
		return alignmentFile.toURI().toURL();
	}
}
