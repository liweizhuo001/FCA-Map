package edu.amss.fca.Tools;

import java.util.LinkedList;


class Node {
    char content; // the character in the node
    boolean isEnd; // whether the end of the words
    int count;  // the number of words sharing this character
    LinkedList<Node> childList; // the child list
  
    public Node(char c){
        childList = new LinkedList<Node>();
        isEnd = false;
        content = c;
        count = 0;
    }
  
    public Node subNode(char c){
        if(childList != null){
	        for(Node eachChild : childList){
	            if(eachChild.content == c){
	                 return eachChild;
	            }
        	}
        }
        return null;
   }
}