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

import java.io.IOException;
import java.util.Iterator;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class GenerateLatex extends GenerateCode {

	private Context context;
	
	private boolean rotateAttributes;
	
	public GenerateLatex(Context context) {
		this(context,true);
	}
	
	public GenerateLatex(Context context,boolean rotateAttributes) {
		super();
		this.rotateAttributes = rotateAttributes;
		this.context = context;
	}

	public static void toFile(Context context,String path) {
		GenerateLatex latex = new GenerateLatex(context);
		latex.generateCode();
		try {
			latex.toFile(path);
		} catch (IOException e) {}
	}
	
	@Override
	public void generateCode() {
		append("\\begin{tabular}");
		
		append("{|l|");
		for(int i = 0;i < context.getAttributes().size(); i++)
			append("c|");
		append("}");
		
		newLine();
		
		appendLine("\\hline");
		
		append(" & ");

		Iterator<Attribute> attrIt = context.getAttributes().iterator();
		
		while( attrIt.hasNext() ) {
			Attribute attr = attrIt.next();
			
			String desc = "\\textbf{" + attr.toString() + "}"; 
			
			if ( rotateAttributes )
				desc = "\\begin{sideways}" + desc + "\\end{sideways}";
			
			if ( attrIt.hasNext() )
				append(desc + " & ");
			else
				append(desc + "\\\\");
		}
		
		newLine();
		
		for(Entity ent: context.getEntities() ) {
			appendLine("\\hline");
			append("\\textbf{" + ent.getName() + "}");
			
			for( Attribute attr: context.getAttributes() )
				if ( context.hasPair(ent,attr))
					append("& $\\times$ ");
				else
					append(" & ");
			
			append("\\\\");
			newLine();
		}
		appendLine("\\hline");
		appendLine("\\end{tabular}");
	}

}
