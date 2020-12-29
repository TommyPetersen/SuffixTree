/*
  Program name: Vertex.java. Is a class representing a vertex.

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

  AI Agents, hereby disclaims all copyright interest in the program `Vertex.java' written
  by Tommy Petersen.
*/

package SuffixTree.Java;

/**
   This class represents a vertex as used in the suffix tree. It contains the following
   information:
   <table border="1" width="100%" cellpadding="3" cellspacing="0" summary="">
   <tr><td><font size="-1">Parent vertex</font></td><td>This vertex's parent vertex, which
       is null if this vertex is the root vertex of the suffix tree</td></tr>
   <tr><td><font size="-1">Parent direction</font></td><td>The direction (corresponding to
       an index) from the parent vertex to this vertex</td></tr>
   <tr><td><font size="-1">indexTo</font></td><td>An index into the symbol list. This index
       gives the last symbol in the subsequence beginning at index indexFrom[direction]
       in the parent vertex
   </td></tr>
   <tr><td><font size="-1">K</font></td><td>The maximum number of children, which is also
       the number of symbols in the alphabet</td></tr>
   <tr><td><font size="-1">Children[]</font></td><td>An array of child vertices</td></tr>
   <tr><td><font size="-1">indexFrom[]</font></td><td>An array of indexes of the
       beginnings of subsequences. Each index in this array is used together with the
       corresponding child in the array containing this vertex's children.
</td></tr>
   </table>

   @author Tommy Petersen
 */
public class Vertex{

    private Vertex parentVertex;
    private int parentDirection;
    private int indexTo;
    private int K;  //maximum number of children
    private Vertex children[];
    private int indexFrom[];

    /**
       Creates a new vertex containing an index into the symbol sequence, the number of
       symbols in the alphabet and information about the parent vertex.

       @param indexTo          Index into the symbol sequence giving the last symbol
                               of a subsequence "ending" at this vertex
       @param K                The number of symbols in the alphabet
       @param parentVertex     The parentVertex
       @param parentDirection  The branching direction from the parent vertex to this
                               vertex
     */
    protected Vertex(int indexTo, int K, Vertex parentVertex, int parentDirection){
	this.indexTo         = indexTo;
	this.K               = K;
	this.parentVertex    = parentVertex;
	this.parentDirection = parentDirection;
	children             = new Vertex[K];
	indexFrom            = new int[K];
	for (int k=0; k<K; k++){
	    children[k] = null;
	    indexFrom[k] = -1;
	}
    }

    /**
       Sets this vertex's child in direction k.

       @param v         The child to be set
       @param indexFrom Index into the symbol sequence giving the first symbol
                        of a subsequence "beginning" at this vertex
       @param k         The direction from this vertex to the child
     */
    protected void setChild(Vertex v, int indexFrom, int k){
	this.indexFrom[k] = indexFrom;
	children[k]       = v;
    }

    /**
       Gets this vertex's parent vertex.

       @return This vertex's parent vertex
     */
    protected Vertex getParentVertex(){
	return parentVertex;
    }

    /**
       Sets this vertex's parent vertex.

       @param parentVertex This vertex's parent vertex
     */
    protected void setParentVertex(Vertex parentVertex){
	this.parentVertex = parentVertex;
    }

    /**
       Gets the direction from this vertex's parent to
       this vertex.

       @return The direction from this vertex's parent to
               this vertex
     */
    protected int getParentDirection(){
	return parentDirection;
    }

    /**
       Sets the direction from this vertex's parent to
       this vertex.

       @param parentDirection The direction from this
                              vertex's parent to this vertex
     */
    protected void setParentDirection(int parentDirection){
	this.parentDirection = parentDirection;
    }

    /**
       Gets this vertex's child in direction k.

       @param k The child's direction
       @return This vertex's child in direction k
     */
    public Vertex getChild(int k){
	return children[k];
    }

    /**
       Gets an index into the symbol sequence giving the last symbol
       of a subsequence "ending" at this vertex

       @return Index into the symbol sequence giving the last symbol
               of a subsequence "ending" at this vertex
     */
    public int getIndexTo(){
	return indexTo;
    }

    /**
       Gets an index into the symbol sequence giving the first symbol
       of a subsequence "begining" at this vertex

       @param  k The direction of the returned index
       @return   Index into the symbol sequence giving the first symbol
                 of a subsequence "begining" at this vertex
     */
    public int getIndexFrom(int k){
	return indexFrom[k];
    }

    /**
       Gets the maximum number of children.

       @return The maximum number of children
     */
    protected int getK(){
	return K;
    }

    /**
       Returns a boolean informing if this vertex is a leaf or not.

       @return True if this vertex is a leaf, false if not
     */
    public boolean isLeaf(){
	for (int k=0; k<K; k++){
	    if (children[k] != null) return false;
	}
	return true;
    }

    protected boolean isRoot(){
	return parentVertex == null;
    }
}
