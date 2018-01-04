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

import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;


public class EntityComparator  implements Comparator<Entity>{

	private Context context;
	
	public EntityComparator(Context context) {
		this.context = context;
	}

	@Override
	public int compare(Entity e1, Entity e2) {
		return context.getAttributes(e2).size() - context.getAttributes(e1).size();
	}
	
}
