/*
  Program name: codeWordSet.java. Is a class used to store code words, and then add them to the tree structure.

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

  AI Agents, hereby disclaims all copyright interest in the program `codeWordSet.java' written
  by Tommy Petersen.
*/

package SuffixTree.Java;

import java.util.*;

/**
   The codeword set consists of a context set and a symbol 'a', which is the symbol that has just
   been added to the symbol list. Every context in the context set stop being a context when
   extended with the 'a', and hence becomes a codeword.
   When the appropriate number of contexts have been added to the context set, the 'a'-extensions
   can be moved as codewords to the suffix tree by a method call.

   @author Tommy Petersen
 */
public class codeWordSet{

    private ArrayList<Context> set;
    private int a;

    /**
       Creates an empty codeword set.

       @param a The symbol that has been added to the symbol
                list. This is used to extend given contexts to
		codewords
     */
    codeWordSet(int a){
	set = new ArrayList<Context>();
	this.a = a;
    }

    /**
       Adds the 'a'-extension of each of some previously given contexts
       to the suffix tree.
       <p>
       There are two possibilities for a context; either it ends at its
       base vertex or it ends somewhere on an edge originating from its
       base vertex.<br />
       In the first case, a child is inserted at the base vertex representing
       the new codeword in the suffix tree.<br />
       In the second case, an intermediate vertex is inserted on the edge
       containing the context's end, and at the position which is on the
       context's end. A child is then inserted at this intermediate vertex
       representing the new codeword in the suffix tree.
       </p>
     */
    protected void moveSetToTree(){
	Context tmpContext = null;
	Vertex baseVertex = null;
	int direction = -1;
	int offset = -1;
	int indexFrom = -1;
	int indexTo = -1;
	//iterate from large contexts to small:
	for (int i=set.size()-1; i>=0; i--){
	    tmpContext = (Context)set.remove(i);
	    ArrayList L = tmpContext.getL();
	    baseVertex = tmpContext.getBaseVertex();
	    direction = tmpContext.getDirection();
	    offset = tmpContext.getOffset();

	    if (direction == -1){  //context is given by baseVertex
		indexFrom = indexTo = L.size() - 1;
		Vertex newChild = new Vertex(Integer.MAX_VALUE, baseVertex.getK(), baseVertex, a);
		baseVertex.setChild(newChild, indexFrom, a);
	    } else{  //context is given by edge from baseVertex
		//make the intermediate vertex:
		indexFrom = baseVertex.getIndexFrom(direction);
		int b = ((Integer)L.get(indexFrom + offset + 1)).intValue();
		Vertex intermediateVertex = new Vertex(indexFrom + offset, baseVertex.getK(), baseVertex, direction);
		Vertex oldChild = baseVertex.getChild(direction);
		oldChild.setParentVertex(intermediateVertex);
		oldChild.setParentDirection(b);
		intermediateVertex.setChild(oldChild, indexFrom + offset + 1, b);
		baseVertex.setChild(intermediateVertex, indexFrom, direction); 
		//intermediateVertex is now between baseVertex and oldChild in direction "direction"
		//as seen from baseVertex. A new child, in direction a, is now added to
		//intermediateVertex:
		indexFrom = indexTo = L.size() - 1;
		Vertex newChild = new Vertex(Integer.MAX_VALUE, intermediateVertex.getK(), intermediateVertex, a);
		intermediateVertex.setChild(newChild, indexFrom, a);
	    }
	}
    }

    /**
       Adds a context whos extension with the symbol 'a' is a codeword.

       @param C A context whos extension with the symbol 'a' is a codeword
     */
    protected void add(Context C){
	set.add(C);
    }

    /**
       Gets the number of added contexts.

       @return The number of added contexts
     */
    protected int getSize(){
	return set.size();
    }
}
