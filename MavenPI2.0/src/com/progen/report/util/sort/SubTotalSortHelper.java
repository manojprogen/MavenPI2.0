/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.util.sort;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This is a helper Class to Sort Table Based On subTotatls
 *
 * @author srikanth.p
 */
public class SubTotalSortHelper {

    private int level;
    private boolean root;
    private boolean leaf;
    private LinkedList nodeSet = new LinkedList(); /*
     * Nodes will be added by comparing with the previos Node on Inserting
     */

    private HashMap subTotalMap = new HashMap();
    private HashMap parentMap = new HashMap(); /*
     * It Contains index of the node set Element index as as Key and Its
     * Respected Parent Value's index in the parentNodeSet
     */

    private HashMap<String, ArrayList> sequenceMap = new HashMap<String, ArrayList>();
    private LinkedList<Integer> subViewSequence = new LinkedList<Integer>();
    private LinkedList<Double> subTotalList = new LinkedList<Double>();
    private LinkedList<LinkedList> viewSequence = new LinkedList<LinkedList>();
    private ArrayList<ArrayList<Double>> dataList = new ArrayList<ArrayList<Double>>(); /*
     * This will be used only for two row view by
     */

    private LinkedList parentList = new LinkedList();
    private char sortType;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
    /*
     * adds Distinct elements to NodeList
     */

    public int addToNodeSet(Object node) {
        int insertIndex = this.nodeSet.size();
        /*
         * This code is commented by amar to add all Nodes
         */
        /*
         * if(this.nodeSet.size() >0){
         * if(!this.nodeSet.getLast().toString().equalsIgnoreCase(node.toString())){
         * this.nodeSet.add(node); }
        }else{
         */
        this.nodeSet.add(node);
        //}
        return insertIndex;
    }
//    public void addNodesToTree(Object child,Object parent){
//        int childPoss=0;
//        if(this.nodeSet.size() >0){
//            if(!this.nodeSet.getLast().toString().equalsIgnoreCase(child.toString())){
//                this.nodeSet.add(child);
//            }
//        }else{
//            this.nodeSet.add(child);
//        }
//        childPoss=this.nodeSet.size()-1;
//        parentMap.put(childPoss,parent);
//    }

    public LinkedList getNodeSet() {
        return this.nodeSet;
    }
    /*
     * sets parent element to each node in the Node List
     */

    public void setParentNode(Object key, Object val) {
        parentMap.put(key, val);
    }

    public HashMap getParentMap() {
        return this.parentMap;
    }

    public LinkedList<Integer> getSubViewSequence() {
        return subViewSequence;
    }

    public void addParent(Object parentNode) {
        this.parentList.add(parentNode);
    }

    public void addSubTotal(Double subTot) {
        this.subTotalList.add(subTot);
    }

    public LinkedList<Double> getSubTotalList() {
        return this.subTotalList;
    }

    public void addViewSequence(LinkedList viewSeq) {
        this.viewSequence.add(viewSeq);
    }

    public LinkedList getViewSequence() {
        return this.viewSequence;
    }

    public void addDataList(ArrayList dataList) {
        this.dataList.add(dataList);
    }

    public ArrayList getDataList() {
        return this.dataList;
    }

    /*
     * It Sorts The Leaf Level Data
     */
    public void sortLeafLevel() {
    }
    /*
     * It sorts the inner levels based on the next level to respected level This
     * Sorting will be Done from the leaf level to Inner Levels
     */

    public void sortInnerLevel() {
    }
    /*
     * This Midthod Sorts The Each Level subtotals and Updates The Respected
     * subTotalViewBySequence wrt ViewBySequence
     */

    public void sortThisLevel() {
        Ordering<Double> ordering = Ordering.natural().nullsLast().onResultOf(new Function<Double, Double>() {

            public Double apply(Double data) {
                return data;
            }
        });
        ArrayList<Double> sortedList = null;
        if (sortType == 'A') {
            sortedList = (ArrayList<Double>) ordering.nullsLast().sortedCopy(this.subTotalList);
        } else {
            sortedList = (ArrayList<Double>) ordering.reverse().nullsLast().sortedCopy(this.subTotalList);
        }


        Object tempObject = this.subTotalList.clone();
        LinkedList tempSubTotal = (LinkedList) tempObject;
        for (Double value : sortedList) {
            int foundIndex = tempSubTotal.indexOf(value);
            tempSubTotal.add(foundIndex, null);
            tempSubTotal.remove(foundIndex + 1);
            this.subViewSequence.add(foundIndex);
        }
    }

    /**
     * @return the sortType
     */
    public char getSortType() {
        return sortType;
    }

    /**
     * @param sortType the sortType to set
     */
    public void setSortType(char sortType) {
        this.sortType = sortType;
    }
}