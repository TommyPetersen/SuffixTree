/*
  Program name: ContextException.java. Is a class representing an exception.

  Copyright (C) 2006 Tommy Petersen, AI Agents, e-mail: tp@ai-agents.com.

  This program is free software; you can redistribute it and/or modify it under the terms of
  the GNU General Public License as published by the Free Software Foundation; either version
  2 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program;
  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
  MA 02111-1307 USA  

  AI Agents, hereby disclaims all copyright interest in the program `ContextException.java' written
  by Tommy Petersen.
*/

package SuffixTree.Java;

/**
   This exception is thrown if an error occurs within a context method.

   @author Tommy Petersen
 */
public class ContextException extends Exception{

    private String cause;

    /**
       Creates a new context exception containing information
       about its cause.

       @param cause Describes the cause of this exception
     */
    public ContextException(String cause){
	this.cause = cause;
    }

    /**
       Returns a string identifying the type of exception and describing
       its cause.

       @return A string identifying the type of exception and describing
               its cause
     */
    public String toString(){
	return "ContextException: " + cause;
    }
}
