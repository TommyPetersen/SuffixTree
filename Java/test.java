package SuffixTree.Java;

import java.util.*;

class test{

    private static suffixTree T1, T2;

    public static void main(String args[]) throws Exception{

	int K = 2;
	int capacity = 10;
	contextSet CS1, CS2 = null;

	T1 = new suffixTree(K);

	T1.add(0);
	T1.add(1);
	T1.add(1);
	T1.add(0);
	T1.add(1);
	T1.add(0);
	T1.add(0);
	T1.add(1);
	T1.add(1);
	T1.add(0);
	T1.add(1);
	T1.add(0);
	T1.add(1);
	T1.add(1);
	T1.add(0);
	CS1 = T1.add(1);

	Random rand = new Random();

	T2 = new suffixTree(K);
	for (int i=0; i<capacity; i++){
	    try{
		CS2 = T2.add(rand.nextInt(K));
		traverseCS(T2, CS2);
	    } catch(java.lang.OutOfMemoryError E){
		System.out.println("Caught: " + E);
		System.exit(1);
	    }
	}

	/*	print(T1, CS1);
		traverseCS(T1, CS1);*/
	System.out.println("Done!");
    }

    private static void print(suffixTree T, contextSet CS){
	T.printCodeTree();
	System.out.println("Number of contexts: " + CS.size());
	CS.print("--");
    }

    private static void traverseCS(suffixTree T, contextSet CS){
	System.out.println("\nL = " + T.getL());
	Context C = null;
	ArrayList symbols = null;
	int size = CS.size();
	System.out.println("contextSet.size = " + size + " (including the empty context).");
	for (int i=1; i<size; i++){
	    C = CS.get(i);
	    System.out.println("Has decoded suffix: " + C.hasDecodedSuffix());
	    System.out.print("Writing out context nr. " + i + ": ");
	    symbols = C.getSymbols();
	    if (symbols.size() > 0){
		for (int j=0; j<symbols.size()-1; j++) System.out.print(((Integer)symbols.get(j)).intValue() + " ");
		System.out.println(((Integer)symbols.get(symbols.size()-1)).intValue());
	    } else System.out.println();
	    System.out.println("size = " + C.getSize());
	    ArrayList A = C.getInstanceEndPoints();
	    System.out.println("A = " + A);
	    for (int j=0; j<A.size(); j++){
		System.out.println("(" + (((Integer) A.get(j)).intValue() - C.getSize() + 1) + ", " + A.get(j) + ")");
	    }
	}
    }
}
