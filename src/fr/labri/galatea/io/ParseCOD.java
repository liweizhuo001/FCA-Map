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

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Entity;

public class ParseCOD  implements ContentHandler {

	private String path;
	
	private ConceptOrder conceptOrder;

	private Map<String,Entity> entMap;

	private Map<String,Attribute> attrMap;

	private Map<String,Concept> cptMap;
	
	private Concept current;
	
	public ParseCOD(String path) {
		this.path = path;
	}
	
	public ConceptOrder getConceptOrder() {
		return conceptOrder;
	}

	public void parse() {
		conceptOrder = new ConceptOrder();
		
		entMap = new HashMap<String, Entity>();
		attrMap = new HashMap<String, Attribute>();
		cptMap = new HashMap<String, Concept>();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);

		try {
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.parse(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if ( qName.equals("concept"))
			current = cpt(atts.getValue("id"));
		else if ( qName.equals("parent"))
			current.getParents().add(cpt(atts.getValue("id")));
		else if ( qName.equals("entity"))
			current.getExtent().add(ent(atts.getValue("desc")));
		else if ( qName.equals("attribute"))
			current.getIntent().add(attr(atts.getValue("desc")));
		
	}
	
	@Override
	public void endDocument() throws SAXException {
		for( Concept c: conceptOrder ) 
			for( Concept p: c.getParents() )
				p.getChildren().add(c);
	}
	
	private Concept cpt(String id) {
		if ( cptMap.containsKey(id) )
			return cptMap.get(id);
		else {
			Concept c = new Concept();
			cptMap.put(id,c);
			conceptOrder.getConcepts().add(c);
			return c;
		}
	}
	
	private Attribute attr(String desc) {
		if ( attrMap.containsKey(desc) )
			return attrMap.get(desc);
		else {
			Attribute a = FcaFactory.createAttribute(desc);
			attrMap.put(desc,a);
			return a;
		}
	}
	
	private Entity ent(String desc) {
		if ( entMap.containsKey(desc) )
			return entMap.get(desc);
		else {
			Entity e = new Entity(desc);
			entMap.put(desc,e);
			return e;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {}

	@Override
	public void setDocumentLocator(Locator locator) {}

	@Override
	public void skippedEntity(String name) throws SAXException {}

	@Override
	public void startDocument() throws SAXException {}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	
}
