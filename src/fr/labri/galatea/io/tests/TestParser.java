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

package fr.labri.galatea.io.tests;

import java.io.IOException;

import org.junit.Test;

import fr.labri.galatea.BinaryAttribute;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;
import fr.labri.galatea.io.ParseCSVContext;

import static junit.framework.Assert.*;

public class TestParser {
	
	@Test
	public void testContextParser() throws IOException {
		ParseCSVContext p = new ParseCSVContext("target/test-classes/gsh.csv");
		p.parse();
		Context ctx = p.getContext();
		
		assertTrue(ctx.getEntityNb() == 6);
		assertTrue(ctx.getAttributeNb() == 8);
		assertTrue(ctx.getPairNb() == 20);
		
		assertTrue(ctx.getAttributes(new Entity("1")).size() == 4);
		assertTrue(ctx.getAttributes(new Entity("2")).size() == 5);
		assertTrue(ctx.getAttributes(new Entity("3")).size() == 5);
		assertTrue(ctx.getAttributes(new Entity("4")).size() == 2);
		assertTrue(ctx.getAttributes(new Entity("5")).size() == 2);
		assertTrue(ctx.getAttributes(new Entity("6")).size() == 2);
		
		assertTrue(ctx.getEntities(new BinaryAttribute("a")).size() == 3);
		assertTrue(ctx.getEntities(new BinaryAttribute("b")).size() == 3);
		assertTrue(ctx.getEntities(new BinaryAttribute("c")).size() == 3);
		assertTrue(ctx.getEntities(new BinaryAttribute("d")).size() == 3);
		assertTrue(ctx.getEntities(new BinaryAttribute("e")).size() == 2);
		assertTrue(ctx.getEntities(new BinaryAttribute("f")).size() == 1);
		assertTrue(ctx.getEntities(new BinaryAttribute("g")).size() == 2);
		assertTrue(ctx.getEntities(new BinaryAttribute("h")).size() == 3);
	}

}
