/*
  Program name: Context.java. Is a class representing a context.

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

  AI Agents, hereby disclaims all copyright interest in the program `Context.java' written
  by Tommy Petersen.
*/

package SuffixTree.Java;

import java.util.*;

/**
   This class represents a context by a base vertex, a direction and and an offset.

   <table border="1" width="100%" cellpadding="3" cellspacing="0" summary="">
   <tr><td><font size="-1">Base vertex</font></td><td>The end of the context is represented
       relative to the base vertex</td></tr>
   <tr><td><font size="-1">Direction</font></td><td>If the end of the context is not the base
       vertex, then it is on some edge originating from the base vertex, and direction tells
       which edge that is</td></tr>
   <tr><td><font size="-1">Offset</font></td><td>When direction is used, offset tells how many
       symbols to go along the edge given by direction in order to get the context's last
       symbol</td></tr>
   </table>

   @author Tommy Petersen
 */
public class Context{

    private Vertex baseVertex;
    private int direction, offset;
    private ArrayList L;

    /**
       Creates a new context.

       @param baseVertex The context's base vertex
       @param direction  The direction from the base vertex toward the end of the
                         context
       @param offset     The offset from the base vertex in the direction given by
                         parameter "direction"
       @param L          The symbol sequence
     */
    protected Context(Vertex baseVertex, int direction, int offset, ArrayList L){
	this.baseVertex = baseVertex;
	this.direction  = direction;
	this.offset     = offset;
	this.L          = L;
    }

    /**
       Returns a new context which extends this one with one symbol.

       @param a The symbol extending this context
       @return  A new context which extends this one with one symbol
       @throws  ContextException If there is an inconsistency in data
     */
    public Context newContext(int a) throws ContextException{
	Vertex baseVertex = null;
	int direction = -1;
	int offset = -1;
	Vertex child = null;
	int indexTo = -1;
	int indexFrom = -1;

	if (this.direction == -1){  //context is given by this.baseVertex
	    child = this.baseVertex.getChild(a);
	    indexTo = child.getIndexTo();
	    indexFrom = this.baseVertex.getIndexFrom(a);

	    if (indexTo > indexFrom){
		baseVertex = this.baseVertex;
		direction = a;
		offset = 0;
	    } else if (indexTo == indexFrom){
		baseVertex = child;
		direction = -1;
		offset = -1;
	    } else{
		throw new ContextException("indexTo < indexFrom");
	    }
	} else{  //context is given by edge from this.baseVertex
	    child = this.baseVertex.getChild(this.direction);
	    indexTo = child.getIndexTo();
	    indexFrom = this.baseVertex.getIndexFrom(this.direction);

	    if (indexTo > indexFrom + this.offset + 1){
		baseVertex = this.baseVertex;
		direction = this.direction;
		offset = this.offset + 1;
	    } else if (indexTo == indexFrom + this.offset + 1){
		baseVertex = child;
		direction = -1;
		offset = -1;
	    } else{
		throw new ContextException("indexTo < indexFrom + this.offset + 1");
	    }
	}
	return new Context(baseVertex, direction, offset, L);
    }

    /**
       Returns a boolean value which is true if the extension of this context
       with the argument is a context and which is false otherwise.

       @param a The symbol with which this context is extended
       @return  A boolean value which is true if the extension of this context
                with the argument is a context and which is false otherwise.
     */
    protected boolean isContext(int a){
	if (direction == -1){  //context is given by baseVertex
	    return baseVertex.getChild(a) != null;
	} else{  //context is given by edge from baseVertex
	    return ((Integer)L.get(baseVertex.getIndexFrom(direction) + offset + 1)).intValue() == a;
	}
    }

    /**
       Returns a boolean value which is true if this context has decoded a suffix
       and which is false otherwise.

       @return A boolean value which is true if this context has decoded a suffix
               and which is false otherwise
     */
    public boolean hasDecodedSuffix(){
	if (direction < 0) return false;

	return baseVertex.getChild(direction).isLeaf();
    }

    /**
       Returns a boolean value which is true if this context is an extension
       of the argument context and which is false otherwise.

       @return A boolean value which is true if this context is an extension
               of the argument context and which is false otherwise.
     */
    public boolean isExtensionOf(Context C){
	ArrayList<Integer> symbols1 = this.getSymbols();
	ArrayList<Integer> symbols2 = C.getSymbols();

	if (symbols1.size() != symbols2.size() + 1) return false;
	
	for (int i=0; i<symbols2.size(); i++){
	    if (((Integer)symbols1.get(i)).intValue() != ((Integer)symbols2.get(i)).intValue())
		return false;
	}
	return true;
    }

    /**
       Returns the base vertex of this context.

       @return The base vertex of this context
     */
    public Vertex getBaseVertex(){
	return baseVertex;
    }

    /**
       Returns the direction of this context.

       @return The direction of this context
     */
    public int getDirection(){
	return direction;
    }

    /**
       Returns the offset of this context.

       @return The offset of this context
     */
    public int getOffset(){
	return offset;
    }

    /**
       Returns the symbol list used to create the
       entire suffix tree

       @return The symbol list used to create the
               entire suffix tree
     */
    public ArrayList getL(){
	return L;
    }

    /**
       Returns the symbol list representation of this context.

       @return The symbol list representation of this context
     */
    public ArrayList<Integer> getSymbols(){
	Vertex parent = baseVertex.getParentVertex();

	if (direction < 0){
	    if (parent == null){  //then baseVertex is the root
		return new ArrayList<Integer>();
	    } else{
		return getSymbolsRecursively(parent, baseVertex.getParentDirection());
	    }
	} else{
	    int indexFrom = baseVertex.getIndexFrom(direction);
	    int indexTo = indexFrom + offset;

	    ArrayList<Integer> B = new ArrayList<Integer>();

	    for (int i=indexFrom; i<=indexTo; i++){
		B.add((Integer)L.get(i));
	    }

	    if (parent == null){  //then baseVertex is the root
		return B;
	    } else{
		ArrayList<Integer> A = getSymbolsRecursively(parent, baseVertex.getParentDirection());
		A.addAll(B);
		return A;
	    }
	}
    }

    /*
      This recursive method is called by the method "getSymbols" in order to get a part
      of the symbol list representing this context.

      Parameter  Usage
      ---------- ------------------------------------------------------------
      v          A vertex from which this part of the symbol list begins
      vDirection The direction to go from 'v' in order to get the symbol list
     */
    private ArrayList<Integer> getSymbolsRecursively(Vertex v, int vDirection){
	int indexFrom = v.getIndexFrom(vDirection);
	int indexTo = v.getChild(vDirection).getIndexTo();
	ArrayList<Integer> B = new ArrayList<Integer>();

	for (int i=indexFrom; i<=indexTo; i++){
	    B.add((Integer)L.get(i));
	}

	Vertex parent = v.getParentVertex();

	if (parent == null){  //then v is the root
	    return B;
	} else{
	    ArrayList<Integer> A = getSymbolsRecursively(parent, v.getParentDirection());
	    A.addAll(B);
	    return A;
	}
    }

    /* Finds the instance endpoint for the zeroth instance. */
    public int getZerothInstanceEndPoint() throws ContextException{
	ArrayList<Integer> instanceEndPoints = getInstanceEndPoints();
	if (instanceEndPoints.size() > 0) {
	    return ((Integer) getInstanceEndPoints().get(0)).intValue();
	} else {
	    throw new ContextException("There are no context instances");
	}
    }

    /* Finds the instance endpoints for zero, one or more instances. */
    public ArrayList<Integer> getInstanceEndPoints(){
    	return getInstanceEndPointsRecur(0);
    }
    
    /*
      To find the instance endpoints for the context, first find the
      decoding points and then subtract the length of the decoding
      sequence (accumulated in l).
    */
    private ArrayList<Integer> getInstanceEndPointsRecur(int l){
	ArrayList<Integer> A = new ArrayList<Integer>();
	if (direction < 0){
	    Context C = null;
	    int K = baseVertex.getK();
	    for (int k=0; k<K; k++){
		if (baseVertex.getChild(k) != null){
		    try{
			C = newContext( ((Integer) L.get(baseVertex.getIndexFrom(k))).intValue()  );
			A.addAll(C.getInstanceEndPointsRecur(l + 1));
		    } catch(ContextException CE){
			System.out.println("ContextException : " + CE);
			System.exit(1);
		    }
		}
	    }
	} else{
	    if (hasDecodedSuffix()){
		A.add(Integer.valueOf(baseVertex.getIndexFrom(direction) + offset - l));
	    } else{
		try{
		    Context C = newContext( ((Integer) L.get(baseVertex.getIndexFrom(direction) + offset + 1)).intValue() );
		    A = C.getInstanceEndPointsRecur(l + 1);
		} catch(ContextException CE){
		    System.out.println("ContextException : " + CE);
		    System.exit(1);
		}
	    }
	}
	return new ArrayList<Integer>(new TreeSet<Integer>(A)); //TreeSet sorts A
    }

    public int getSize(){
	int tailSize = 0;

	if (direction >= 0){
	    tailSize = offset + 1;
	}

	Vertex parent = baseVertex.getParentVertex();
	if (parent == null){  //then baseVertex is the root
	    return tailSize;
	} else{
	    return getSizeRecursively(parent, baseVertex.getParentDirection()) + tailSize;
	}
    }

    private int getSizeRecursively(Vertex v, int vDirection){
	int tailSize = v.getChild(vDirection).getIndexTo() - v.getIndexFrom(vDirection) + 1;

	Vertex parent = v.getParentVertex();
	if (parent == null){  //then v is the root
	    return tailSize;
	} else{
	    return getSizeRecursively(parent, v.getParentDirection()) + tailSize;
	}
    }

    /**
       Produces a simple ascii print of the symbol list representing this context
       on sysout.
     */
    public void print(String indent){
	ArrayList symbols = this.getSymbols();
	System.out.print(indent + "[");
	if (symbols.size() > 0){
	    for (int j=0; j<symbols.size()-1; j++) System.out.print(((Integer)symbols.get(j)).intValue() + " ");
	    System.out.print(((Integer)symbols.get(symbols.size()-1)).intValue() + "]");
	} else System.out.print("]");
    }
}
