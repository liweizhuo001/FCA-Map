/*
   Copyright 2009 Jean-R�my Falleri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package fr.labri.galatea.tests;

import java.io.IOException;

import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.algo.Algorithm;
import fr.labri.galatea.algo.SimpleGSH;
import fr.labri.galatea.io.GenerateCOD;
import fr.labri.galatea.io.ParseCOD;
import fr.labri.galatea.io.ParseCSVContext;
import fr.labri.galatea.io.GenerateDot;
import fr.labri.galatea.io.GenerateHTML;

public class TestAllParsers {

	public static void main(String[] args) throws IOException {
		String path = "target/test-classes/gsh.csv";
		
		ParseCSVContext p = new ParseCSVContext(path);
		p.parse();
		Context c = p.getContext();
		
		System.out.println(c);
		
		GenerateHTML g1 = new GenerateHTML(c);
		g1.generateCode();
		g1.toFile("tmp/test.html");
		
		Algorithm a1 = new SimpleGSH(c);
		a1.compute();
		
		ConceptOrder o = a1.getConceptOrder();
		
		GenerateCOD cod = new GenerateCOD(o);
		cod.generateCode();
		cod.toFile("tmp/cod.xml");
		
		ParseCOD codp = new ParseCOD("tmp/cod.xml");
		codp.parse();
		
		GenerateDot g2 = new GenerateDot(o);
		g2.generateCode();
		g2.toFile("tmp/test.dot");
		
		GenerateDot g3 = new GenerateDot(codp.getConceptOrder());
		g3.generateCode();
		g3.toFile("tmp/cod.dot");
	}
	
}
