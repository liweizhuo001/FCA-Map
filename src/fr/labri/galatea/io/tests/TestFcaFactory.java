/*
   Copyright 2009 Jean-Rémy Falleri

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

package fr.labri.galatea.io.tests;


import org.junit.Test;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.BinaryAttribute;
import fr.labri.galatea.composite.CompositeAttribute;
import fr.labri.galatea.composite.Facet;
import fr.labri.galatea.io.FcaFactory;

import static junit.framework.Assert.*;

public class TestFcaFactory {
	
	@Test
	public void testBinaryAttribute() {
		Attribute a1 = FcaFactory.createAttribute("test");
		assertTrue(a1 instanceof BinaryAttribute);
		BinaryAttribute ba1 = (BinaryAttribute) a1;
		assertTrue(ba1.getValue().equals("test"));
		
		Attribute a2 = FcaFactory.createAttribute("");
		assertTrue(a2 instanceof BinaryAttribute);
		BinaryAttribute ba2 = (BinaryAttribute) a2;
		assertTrue(ba2.getValue().equals(""));
	}
	
	@Test
	public void testFacet() {
		Attribute a1 = FcaFactory.createAttribute("test,1");
		assertTrue(a1 instanceof Facet);
		Facet f1 = (Facet) a1;
		assertTrue(f1.getType().equals("test"));
		assertTrue(f1.getValue().equals("1"));
		
		Attribute a2 = FcaFactory.createAttribute(",test");
		assertTrue(a2 instanceof Facet);
		Facet f2 = (Facet) a2;
		assertTrue(f2.getType().equals(""));
		assertTrue(f2.getValue().equals("test"));
	}

	@Test
	public void testCompositeAttribute() {
		Attribute a1 = FcaFactory.createAttribute("()");
		assertTrue(a1 instanceof CompositeAttribute);
		CompositeAttribute c1 = (CompositeAttribute) a1;
		assertTrue(c1.size() == 0);
		
		Attribute a2 = FcaFactory.createAttribute("((test,1),(test,2))");
		assertTrue(a2 instanceof CompositeAttribute);
		CompositeAttribute c2 = (CompositeAttribute) a2;
		assertTrue(c2.size() == 2);
		Facet c2f1 = c2.getFacet(0);
		assertTrue(c2f1.getType().equals("test"));
		assertTrue(c2f1.getValue().equals("1"));
	}
	
}
