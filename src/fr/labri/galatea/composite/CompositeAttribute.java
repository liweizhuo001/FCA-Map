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

package fr.labri.galatea.composite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.labri.galatea.Attribute;

public class CompositeAttribute extends Attribute implements Iterable<Facet> {

	private List<Facet> facets;

	public CompositeAttribute() {
		facets = new ArrayList<Facet>();	
	}

	public void addFacet(Facet f) {
		this.facets.add(f);
	}
	
	@Override
	public Iterator<Facet> iterator() {
		return facets.iterator();
	}

	public int size() {
		return facets.size();
	}

	public Facet getFacet(int i) {
		return this.facets.get(i);
	}
	
	public boolean isValid() {
		for(Facet f: this)
			if ( !f.isValid() )
				return false;
		return true;
	}

	public boolean isCompatible(CompositeAttribute c) {
		if ( this.size() != c.size() )
			return false;
		else {
			for ( int i = 0 ; i < this.size() ; i++ ) 
				if ( !this.getFacet(i).isCompatible(c.getFacet(i)) )
					return false;

			return true;
		}
	}

	public int hashCode() {
		return facets.hashCode();
	}

	public boolean equals(Object o) {
		if ( !(o instanceof CompositeAttribute) )
			return false;
		else {
			CompositeAttribute c = (CompositeAttribute) o;
			if ( this.size() != c.size() )
				return false;
			else {
				for (int i = 0 ; i < this.size() ; i++ ) 
					if ( !( this.getFacet(i).equals(c.getFacet(i)) ) )
						return false;

				return true;
			}
		}
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("(");
		Iterator<Facet> fIt = this.iterator();
		while ( fIt.hasNext() ) {
			Facet f  = fIt.next();
			b.append("(");
			if ( fIt.hasNext() )
				b.append(f.toString() + "),");
			else
				b.append(f.toString() + ")");
		}
		b.append(")");
		return b.toString();
	}

}
