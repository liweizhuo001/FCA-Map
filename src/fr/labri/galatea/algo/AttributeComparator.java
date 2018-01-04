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

package fr.labri.galatea.algo;

import java.util.Comparator;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Context;


public class AttributeComparator implements Comparator<Attribute> {

	private Context context;
	
	public AttributeComparator(Context context) {
		this.context = context;
	}
	
	@Override
	public int compare(Attribute a1, Attribute a2) {
		return context.getEntities(a2).size() - context.getEntities(a1).size(); 
	}

}
