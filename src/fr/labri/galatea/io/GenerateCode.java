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

import java.io.FileWriter;
import java.io.IOException;

public abstract class GenerateCode {
	
	protected static final String LINE_SEPARATOR = System.getProperty("line.separator");

	protected StringBuffer buffer;
	
	public GenerateCode() {
		initBuffer();
	}
	
	/**
	 * Generate the code.
	 */
	public abstract void generateCode();
	
	/**
	 * Returns the produced code.
	 * @return a string containing the produced code.
	 */
	public String getCode() {
		return this.buffer.toString();
	}
	
	/**
	 * Returns the code buffer.
	 * @return a string buffer.
	 */
	public StringBuffer getCodeBuffer() {
		return this.buffer;
	}
	
	/**
	 * Output the internal buffer to the file at the given path.
	 * @param path the file path.
	 * @throws IOException
	 */
	public void toFile(String path) throws IOException {
		FileWriter fw = new FileWriter(path);
		fw.append(buffer);
		fw.close();
	}
	
	/**
	 * Clears the internal string buffer. 
	 */
	protected void initBuffer() {
		buffer = new StringBuffer();
	}
	
	/**
	 * Appends the given string to the internal buffer.
	 * @param s a string.
	 */
	protected void append(String s) {
		buffer.append(s);
	}
	
	/**
	 * Appends the given string to a dedicated line in the internal buffer.
	 * @param s a string.
	 */
	protected void appendLine(String s) {
		append(s + LINE_SEPARATOR);
	}
	
	/**
	 * Appends an empty line in the internal buffer.
	 */
	protected void newLine() {
		append(LINE_SEPARATOR);
	}
	
	/**
	 * Appends the given string in a dedicated line in the internal buffer. This string is preceded
	 * by a given number of tabulations.
	 * @param s a string.
	 * @param tabsNb the number of tabulations.
	 */
	protected void appendWithTabs(String s,int tabsNb) {
		for(int i = 0 ; i < tabsNb ; i++ )
			append("\t");
		append(s);
	}
	
	/**
	 * Appends the given string preceded by a tab.
	 * @param s a string.
	 */
	protected void appendWithTab(String s) {
		appendWithTabs(s,1);
	}
	
	/**
	 * Appends the given string on a dedicated line preceded by a tab.
	 * @param s a string
	 */
	protected void appendLineWithTab(String s) {
		appendLineWithTabs(s,1);
	}
	
	/**
	 * Appends the given string on a dedicated line preceded by a given number of tabs.
	 * @param s a string
	 * @param tabsNb the number of tabs.
	 */
	protected void appendLineWithTabs(String s,int tabsNb) {
		appendWithTabs(s + LINE_SEPARATOR, tabsNb);
	}
	
}