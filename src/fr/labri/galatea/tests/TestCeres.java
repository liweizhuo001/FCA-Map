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

import fr.labri.galatea.algo.AddExtent;
import fr.labri.galatea.algo.Algorithm;
import fr.labri.galatea.algo.SimpleGSH;
import fr.labri.galatea.io.ParseCSVContext;
import fr.labri.galatea.io.GenerateDot;
import fr.labri.galatea.io.GenerateHTML;

public class TestCeres {

	public static void main(String[] args) throws IOException {
		ParseCSVContext p = new ParseCSVContext("target/test-classes/gsh.csv");
		p.parse();
		GenerateHTML h = new GenerateHTML(p.getContext());
		h.generateCode();
		h.toFile("tmp/test.html");
		
		Algorithm a1 = new SimpleGSH(p.getContext());
		a1.compute();
		
		System.out.println(a1.getConceptOrder());
		
		GenerateDot d = new GenerateDot(a1.getConceptOrder());
		d.generateCode();
		d.toFile("tmp/test-gsh.dot");
		
		AddExtent a2 = new AddExtent(p.getContext());
		a2.compute();
		d = new GenerateDot(a2.getConceptOrder());
		d.generateCode();
		d.toFile("tmp/test-lat.dot");
		
		System.out.println(a2.getConceptOrder());
	}
	
}
