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
import fr.labri.galatea.io.ParseCSVContext;
import fr.labri.galatea.io.GenerateDot;
import fr.labri.galatea.io.GenerateLatex;

public class TestDotGenerator {

	public static void main(String[] args) throws IOException {
		String path ="target/test-classes/usability.csv";
		
		ParseCSVContext p = new ParseCSVContext(path);
		p.parse();
		Context c = p.getContext();
		
		//Algorithm a1 = new AddExtent();
		Algorithm a1 = new SimpleGSH(c);
		a1.compute();
		
		ConceptOrder o = a1.getConceptOrder();
		
		GenerateLatex lg = new GenerateLatex(c);
		lg.generateCode();
		lg.toFile("tmp/latex");
		
		GenerateDot dg = new GenerateDot(o);
		dg.setUseSimplifiedExtent(true);
		dg.setUseSimplifiedIntent(true);
		dg.generateCode();
		dg.toFile("tmp/lattice.dot");
	}
	
}
