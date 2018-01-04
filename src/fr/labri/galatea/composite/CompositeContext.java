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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class CompositeContext extends Context {
	
	protected FacetLatticeFactory facetLatticeFactory;
	
	public CompositeContext(FacetLatticeFactory facetLatticeFactory) {
		super();
		this.facetLatticeFactory = facetLatticeFactory;
	}
	
	public void prepare() {
		expand();
		complete();
	}
	
	private void expand() {
		List<Attribute> attrs = new ArrayList<Attribute>(attributes);

		for( int i = 0 ; i < attrs.size() ; i++ ) {
			CompositeAttribute c1 = (CompositeAttribute) attrs.get(i);
			for( int j = i + 1 ; j < attrs.size() ; j++ ) {
				CompositeAttribute c2 =  (CompositeAttribute) attrs.get(j);
				if ( c1.isCompatible(c2) ) {
					CompositeAttribute newComp = new CompositeAttribute();
					for( int k = 0 ; k < c1.size() ; k++ ) {
						String type = c1.getFacet(k).getType();
						String value1 = c1.getFacet(k).getValue();
						String value2 = c2.getFacet(k).getValue();
						
						String newValue = facetLatticeFactory.supremum(type, value1, value2);
						
						if ( newValue == null ) {
							newComp = null;
							break;
						}
						
						Facet f = new Facet(type,newValue);
						newComp.addFacet(f);
					}
					if ( newComp != null )
						attributes.add(newComp);
				}
			}
		}
	}
	
	private boolean isLesser(CompositeAttribute c1,CompositeAttribute c2) {
		if ( !c1.isCompatible(c2) )
			return false;

		for( int i = 0 ; i < c1.size() ; i++ ) {

			Facet f1 = c1.getFacet(i);
			Facet f2 = c2.getFacet(i);

			if ( !facetLatticeFactory.isLesser(f1.getType(),f1.getValue(),f2.getValue()) )
				return false;
		}

		return true;
	}
	
	private void complete() {
		for( Entity e: entities ) {
			Set<Attribute> a1set = new HashSet<Attribute>(getAttributes(e));
			for( Attribute a1: a1set ) {
				CompositeAttribute c1 = (CompositeAttribute) a1;
				for( Attribute a2: this.attributes ) {
					CompositeAttribute c2 = (CompositeAttribute) a2;
					if ( c1 != c2 )
						if ( c1.isCompatible(c2) )
							if ( isLesser(c2,c1) ) 
								addPair(e,c2);
				}
			}
		}
	}
	

}
