package SuffixTree.Java;

import java.util.*;

public class partitionedIntList{

    private int L[];
    private int capacity,u,p,split;
    private boolean capacityReached;

    partitionedIntList(int _capacity){
	capacity = (_capacity < 0) ? 0 : _capacity;
	L = new int[capacity];
	capacityReached = capacity == 0;
	u = capacity - 1;  //the upperbound
	p = -1;  //the index of the last inserted element
	split = (int) Math.floor(((double)capacity) / 2.0);
    }

    protected boolean add(int a){
	if (!capacityReached) {p++; L[p] = a;}
	else return true;
	if (p == u){
	    capacityReached = true;
	}
	return false;
    }

    public int get(int i){
	return L[i % capacity];
    }

    public int getP(){
	return p;
    }

    protected int getCapacity(){
	return capacity;
    }

    protected int getSplit(){
	return split;
    }

    public void print(){
	System.out.print("[");
	for (int i=0; i<p; i++){
	    System.out.print(L[i] + " ");
	}
	if (p >= 0) System.out.println(L[p] + "]");
	else System.out.println("]");
    }
}
