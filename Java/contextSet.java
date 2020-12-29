/*
  Program name: contextSet.java. Is a class representing a context set.

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

  AI Agents, hereby disclaims all copyright interest in the program `contextSet.java' written
  by Tommy Petersen.
*/

package SuffixTree.Java;

import java.util.*;

/**
   Contains the set of contexts.

   @author Tommy Petersen
 */
public class contextSet{

    private ArrayList<Context> set;

    /**
       Creates a new context set containing only the empty context lambda.

       @param lambda The empty context
     */
    protected contextSet(Context lambda){
	set = new ArrayList<Context>();
	set.add(lambda);
    }

    /**
       Gets the i'th context.

       @param i Index into the context set
       @return  The i'th context
     */
    public Context get(int i){
	return (Context)set.get(i);
    }

    /**
       Adds a context to the context set.

       @param C The context to be added
     */
    protected void add(Context C){
	set.add(C);
    }

    /**
       Adds the codewords to the suffix tree and returns the context set resulting
       from adding the argument symbol to the contexts in this context set.
       <p>
       This is done by dividing this context set into two parts with respect to the
       argument symbol 'a'; an upper part (larger indexes) and a lower part (smaller
       indexes). Each context in the upper part is not a context when it is extended
       with the symbol 'a', while each context in the lower part is a context when
       it is extended with the symbol 'a'.
       </p>
       <p>
       The upper part and the symbol 'a' is used to create a codeword set which is
       moved to the suffix tree, while the lower part and the symbol 'a' is used
       to create a new context set, which is returned.
       </p>

       @param a The symbol that has been added to the symbol list
       @return  The context set that is the result of adding the symbol 'a'
                to this context set
       @throws ContextException If an error occurs when creating a new context
                                by extending an existing one with the symbol 'a'
     */
    protected contextSet process(int a) throws ContextException{
	contextSet CS = new contextSet((Context)set.get(0));
	int size = set.size();
	Context tmpContext = null;
	int j = size;
	int i = 0;

	//Find index j dividing the context set into the upper and lower parts:
	while (i<size){
	    tmpContext = (Context)set.get(i);
	    if (!tmpContext.isContext(a)){
		j = i;
		break;
	    }
	    i++;
	}

	//Use the upper part and the symbol 'a' to create a codeword set:
	codeWordSet CWS = new codeWordSet(a);
	while (i<size){
	    CWS.add((Context)set.get(i));
	    i++;
	}
	//Move the codeword set to the suffix tree:
	CWS.moveSetToTree();
	
	//Use the lower part and the symbol 'a' to create a new context set:
	for (int n=0; n<j; n++){
	    tmpContext = (Context)set.get(n);
	    CS.add(tmpContext.newContext(a));
	}

	//Return the new context set resulting from the extension by 'a':
	return CS;
    }

    /**
       Returns the number of contexts in this context set including the empty context.

       @return The number of contexts in this context set including the empty context
     */
    public int size(){
	return set.size();
    }

    /**
       Produces a simple ascii print of all the contexts in this context set.
     */
    public void print(String indent){
	Context C = null;
	if (set.size() == 1) ((Context)set.get(0)).print(indent);
	else{
	    for (int i=0; i<set.size()-1; i++){
		((Context)set.get(i)).print(indent);
	    }
	    ((Context)set.get(set.size()-1)).print(indent);
	}
    }
}
