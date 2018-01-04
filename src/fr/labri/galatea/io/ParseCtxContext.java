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

package fr.labri.galatea.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class ParseCtxContext extends ParseContext {

	public ParseCtxContext(String path) {
		super(path);
	}

	@Override
	public void parse() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(path));
		context = new Context();
		int currentRow = 0;
		List<Attribute> attrs = new ArrayList<Attribute>();
		String line;
		while (( line = input.readLine()) != null) {

			line = line.trim();

			if ( !"".equals(line) && ( !line.startsWith("|") || !line.endsWith("|")) ) {
				input.close();
				return;
			}

			String[] tokens = line.split("\\|");
			int len = tokens.length;
			if ( currentRow == 0 ) {
				for(int i = 2 ; i < len ; i++ ) {
					String attrDesc = tokens[i].trim();
					Attribute attr = FcaFactory.createAttribute(attrDesc);
					attrs.add(i-2,attr);
					context.getAttributes().add(attr);
				}
			}
			else {
				if (  !"".equals(line) ) {
					String name = tokens[1].trim();
					Entity ent = new Entity(name);
					context.getEntities().add(ent);
					for(int i = 2 ; i < len ; i++ ) {
						String cell = tokens[i].trim().toLowerCase();
						if ( "x".equals(cell) ) {
							Attribute attr = attrs.get(i-2);
							context.addPair(ent,attr);
						}
					}
				}
			}
			currentRow++;
		}
		input.close();
	}

}
