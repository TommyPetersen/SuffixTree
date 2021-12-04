/*
  Program name: suffixTree.java. Is a class representing a suffix tree.

  Copyright (C) 2005 Tommy Petersen, AI Agents, e-mail: tp@ai-agents.com.

  This program is free software; you can redistribute it and/or modify it under the terms of
  the GNU General Public License as published by the Free Software Foundation; either version
  2 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program;
  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
  MA 02111-1307 USA  

  AI Agents, hereby disclaims all copyright interest in the program `suffixTree.java' written
  by Tommy Petersen.
*/

package SuffixTree.Java;

import java.util.*;

/**
   This class represents the suffix tree. It also contains the symbol list, from which the
   suffix tree is created.

   @author Tommy Petersen
 */
public class suffixTree{

    private ArrayList<Integer> L;
    private int K;
    private Vertex root;
    private contextSet CS;

    /**
       Creates a new suffix tree containing only the root vertex and an empty list
       of integers.

       @param K The number of symbols in the alphabet
     */
    public suffixTree(int K){  //K is the number of symbols in the alphabet
	this.K = (K < 1) ? 1 : K;
	L = new ArrayList<Integer>();
	root = new Vertex(-1, this.K, null, -1);
	Context lambda = new Context(root, -1, -1, L);
	CS = new contextSet(lambda);
    }

    /**
       Adds a new symbol to the symbol list and updates the suffix tree
       accordingly. The new context set is returned.

       @param  a The symbol to be added to the symbol list
       @return   The new context set for the extended symbol list
       @throws   java.lang.OutOfMemoryError If there is not enough
                 store in order to add the new symbol
     */
    public contextSet add(int a) throws java.lang.OutOfMemoryError{
	if (a < 0) a = -a;
	if (a >= K) a = a % K;

	L.add(Integer.valueOf(a));
	try{
	    CS = CS.process(a);
	} catch (ContextException CE){
	    System.out.println("! ERROR IN INTERNAL DATA !");
	    System.out.println(CE);
	    System.exit(1);
	}
	return CS;
    }

    /**
       Adds the symbols in a given string to the symbol list
       and updates the suffix tree accordingly.
       The new context set is returned.

       @param  s The symbols to be added to the symbol list. The
                 symbols are separated by white spaces.
       @return   The new context set for the extended symbol list
       @throws   java.lang.OutOfMemoryError If there is not enough
                 store in order to add the new symbols
     */
    
    public contextSet add(String s) throws java.lang.OutOfMemoryError{

	if ((s == null) || (s.trim().isEmpty())) return CS;

	try{
	    String[] numberStrings = s.split("\\s+");

	    for (String ss : numberStrings){
		CS = this.add(Integer.parseInt(ss));
	    }
	} catch (NumberFormatException NFE){
	    System.out.println("! ERROR IN INPUT DATA !");
	    System.out.println(NFE);
	    System.exit(1);
	}

	return CS;
    }

    /**
       Returns the root vertex of the suffix tree.

       @return   The root vertex of the suffix tree
     */
    public Vertex getRoot(){
	return root;
    }

    /**
       Returns the symbol list from which the suffix tree is created.

       @return   The symbol list from which the suffix tree is created
     */
    public ArrayList getL(){
	return L;
    }

    public int getSizeL(){
	return L.size();
    }

    /**
       Produces a simple ascii print of the code tree on sysout. This
       is done by traversing the suffix tree in preorder printing all
       the symbols at the indexes as given in the suffix tree.
     */
    public void printCodeTree(){
	Vertex child = null;
	int indexFrom = -1;
	int indexTo = -1;
	int t = 0;  //t is the tabulator giving the left indent

	for (int k=0; k<K; k++){
	    child = root.getChild(k);
	    if (child != null){
		indexFrom = root.getIndexFrom(k);
		indexTo = child.isLeaf() ? indexFrom : child.getIndexTo();
		printSequence(t, indexFrom, indexTo);

		if (!child.isLeaf()) recursivePrintCodeTree(t + 1 + 2 * (indexTo - indexFrom), child);
	    }
	}
    }

    /*
       Recursive method used by method printCodeTree.

       Parameter Usage
       --------- ------------------------------------------------------
       t         Tabulator giving the left indent
       v         Vertex which is the origin of zero or more edges whose
                 code fragments are to be printed from left to right
     */
    private void recursivePrintCodeTree(int t, Vertex v){
	Vertex child = null;
	int indexFrom = -1;
	int indexTo = -1;

	for (int k=0; k<K; k++){
	    child = v.getChild(k);
	    if (child != null){
		indexFrom = v.getIndexFrom(k);
		indexTo = child.isLeaf() ? indexFrom : child.getIndexTo();
		printSequence(t, indexFrom, indexTo);

		if (!child.isLeaf()) recursivePrintCodeTree(t + 1 + 2 * (indexTo - indexFrom), child);
	    }
	}
    }


    /*
       Method used in order to print a symbol sequence.

       Parameter Usage
       --------- -------------------------------------
       t         Tabulator giving the left indent
       indexFrom Smallest index of the symbol sequence
       indexTo   Greatest index of the symbol sequence
     */
    private void printSequence(int t, int indexFrom, int indexTo){
	for (int i=0; i<t; i++) System.out.print(" ");
	if (indexTo == indexFrom) System.out.println(L.get(indexFrom));
	else{
	    for (int j=indexFrom; j<indexTo; j++) System.out.print(L.get(j) + " ");
	    System.out.println(L.get(indexTo));
	}
    }

    /**
       Produces a simple ascii print of the index tree on sysout. This
       is done by traversing the suffix tree in preorder printing all
       the indexes as given in the suffix tree.
     */
    public void printIndexTree(){
	System.out.println("Printing suffixtree:");
	System.out.println("[-1, -1]");
	int t = 1;
	Vertex child = null;
	for (int k=0; k<K; k++){
	    child = root.getChild(k);
	    if (child != null){
		for (int i=0; i<t; i++) System.out.print(" ");
		System.out.println("k = " + k + ":");
		for (int i=0; i<t; i++) System.out.print(" ");
		System.out.print("[" + root.getIndexFrom(k) + ", ");
		if (child.isLeaf()){
		    System.out.println(child.getIndexTo() + "]");
		} else{
		    recursivePrintIndexTree(t + 1, child);
		}
	    }
	}
    }

    /*
       Recursive method used by method printIndexTree.

       Parameter Usage
       --------- ------------------------------------------------------
       t         Tabulator giving the left indent
       v         Vertex which is the origin of zero or more edges whose
                 corresponding indexes (indexFrom and indexTo) are to
		 be printed from left to right
     */
    private void recursivePrintIndexTree(int t, Vertex v){
	System.out.println(v.getIndexTo() + "]");
	if (v.isLeaf()) return;
	Vertex child = null;
	for (int k=0; k<K; k++){
	    child = v.getChild(k);
	    if (child != null){
		for (int i=0; i<t; i++) System.out.print(" ");
		System.out.println("k = " + k + ":");
		for (int i=0; i<t; i++) System.out.print(" ");
		System.out.print("[" + v.getIndexFrom(k) + ", ");
		if (child.isLeaf()){
		    System.out.println(child.getIndexTo() + "]");
		} else{
		    recursivePrintIndexTree(t + 1, child);
		}
	    }
	}
    }
}
