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

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class GenerateHTML extends GenerateCode {

	private Context context;

	public GenerateHTML(Context context) {
		this.context = context;
	}
	
	public static void toFile(Context context,String path) throws IOException {
		GenerateHTML g = new GenerateHTML(context);
		g.generateCode();
		g.toFile(path);
	}

	@Override
	public void generateCode() {
		initBuffer();

		appendHeader();

		appendLine("<table><tr><td></td>");
		for( Attribute attr: context.getAttributes() )
			appendLine("<td>" + attr.toString() + "</td>");
		appendLine("</tr>");

		for( Entity ent: context.getEntities() ) {
			appendLine("<tr><td>" + ent.getName() + "</td>");
			for( Attribute attr: context.getAttributes() ) {
				if ( context.getAttributes(ent).contains(attr))
					appendLine("<td>X</td>");
				else
					appendLine("<td></td>");
			}
			appendLine("</tr>");
		}

		appendLine("</table>");



		appendFooter();
	}

	private void appendHeader() {
		appendLine("<html><head><title>Relational Context Family</title><style type=\"text/css\">" +
				"body { background-color: white; color: black; font-family:Verdana,Geneva,Arial,Helvetica,sans-serif; font-size:small; }" +
				"table { border: medium solid black; border-collapse: collapse; empty-cells:show; }" +
				"td, th { border: thin solid black; font-size:small; text-align: center; padding: 5px;}" +
		"</style></head><body>");
	}

	private void appendFooter() {
		appendLine("</body></html>");
	}

}
