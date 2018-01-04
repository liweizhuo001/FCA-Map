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

import fr.labri.galatea.BinaryAttribute;

public class Facet extends BinaryAttribute {

	protected String type;
	
	public Facet(String type,String value) {
		super(value);
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isValid() {
		if ( this.type == null )
			return false;
		else
			return true;
	}
	
	public boolean isCompatible(Facet f) {
		if ( this.type.equals(f.type) )
			return true;
		else
			return false;
	}

	public boolean equals(Object o) {
		if ( !(o instanceof Facet) )
			return false;
		else {
			Facet f = (Facet) o;
			return ( f.type.equals(this.type) && f.value.equals(this.value) );
		}
	}
	
	public int hashCode() {
		return value.hashCode() + type.hashCode();
	}
	
	public String toString() {
		return type + "," + value;
	}
	
}
