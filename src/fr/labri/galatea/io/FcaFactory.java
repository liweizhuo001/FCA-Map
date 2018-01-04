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

import fr.labri.galatea.Attribute;
import fr.labri.galatea.BinaryAttribute;
import fr.labri.galatea.composite.CompositeAttribute;
import fr.labri.galatea.composite.Facet;

public class FcaFactory {

	public static Attribute createAttribute(String desc) {
		if ( !desc.startsWith("(") ) {
			if ( !desc.contains(",") )
				return createBinaryAttribute(desc);
			else
				return createFacet(desc);
		}
		else
			return createCompositeAttribute(desc);
	}

	public static BinaryAttribute createBinaryAttribute(String desc) {
		return new BinaryAttribute(desc);
	}

	public static Facet createFacet(String desc) {
		String[] tokens = desc.split(",");
		return new Facet(tokens[0],tokens[1]);
	}

	public static CompositeAttribute createCompositeAttribute(String desc) {
		if ( "()".equals(desc) )
			return new CompositeAttribute();

		CompositeAttribute c = new CompositeAttribute();
		String cdesc = desc.substring(2,desc.length()-2);
		String[] tokens = cdesc.split("(\\),\\()");

		for(String token: tokens)
			c.addFacet(createFacet(token));

		return c;
	}

}
