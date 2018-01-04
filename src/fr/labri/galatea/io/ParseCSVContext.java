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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class ParseCSVContext  extends ParseContext {

	private Character separator;
	
	public ParseCSVContext(String path, Character separator) {
		super(path);
		this.separator = separator;
	}
	
	public ParseCSVContext(String path) {
		this(path,';');
	}
	
	public void parse() throws IOException {
		CSVReader reader = new CSVReader(new FileReader(path),separator);
		List<String[]> entries = reader.readAll();

		context = new Context();

		int currentRow = 0;

		List<Attribute> attrs = new ArrayList<Attribute>();

		for(String[] entry: entries ) {
			int len = entry.length;

			if ( currentRow == 0 ) {
				for(int i = 1 ; i < len ; i++ ) {
					String attrDesc = entry[i].trim().replaceAll("\\\"","");
					Attribute attr = FcaFactory.createAttribute(attrDesc);
					context.getAttributes().add(attr);
					attrs.add(i-1,attr);
				}
			}
			else {
				String name = entry[0].trim().replaceAll("\\\"","");;
				Entity ent = new Entity(name);
				context.addEntity(ent);
				for(int i = 1 ; i < len ; i++ ) {
					String cell = entry[i].trim().replaceAll("\\\"","").toLowerCase();
					if ( "x".equals(cell) ) {
						Attribute attr = attrs.get(i-1);
						context.addPair(ent,attr);
					}
				}
			}
			currentRow++;
		}
		
		reader.close();
	}

}
