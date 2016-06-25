/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import java.util.*;

/**
 *
 * @author arun
 */
public class ColumnGroup {

    Object value;
    Set<ColumnGroup> childGroups;

    public ColumnGroup(Object value) {
        this.value = value;
        childGroups = new LinkedHashSet<ColumnGroup>();
    }

    public void addChildGroups(Set<ColumnGroup> childGroups) {
        this.childGroups.addAll(childGroups);
    }

    public Set<ColumnGroup> getChildGroups() {
        return this.childGroups;
    }

    public int getGrandChildrenCount() {
        int count = 0;
        if (childGroups.isEmpty()) {
            return 1;
        } else {
            for (ColumnGroup group : childGroups) {
                count = count + group.getGrandChildrenCount();
            }
        }
        return count;
    }

    //TODO handle multi chains
    public ArrayList<LinkedList<Object>> getMyChains() {
        ArrayList<LinkedList<Object>> myChain = new ArrayList<LinkedList<Object>>();
        LinkedList<Object> chainElement;

        if (childGroups.isEmpty()) {
            chainElement = new LinkedList<Object>();
            chainElement.add(value);
            myChain.add(chainElement);
        } else {
            chainElement = new LinkedList<Object>();
            addToChain(myChain, chainElement);
        }

        return myChain;
    }

    private void addToChain(ArrayList<LinkedList<Object>> myChain, LinkedList<Object> chainElement) {
        if (childGroups.isEmpty()) {
            chainElement.add(value);
            myChain.add(chainElement);
        } else {
            for (ColumnGroup group : childGroups) {
                LinkedList<Object> childChainElements = new LinkedList<Object>();
                childChainElements.addAll(chainElement);
                childChainElements.add(value);
                group.addToChain(myChain, childChainElements);
            }
        }
    }

    public int getChildCount() {
        if (childGroups.isEmpty()) {
            return 0;
        } else {
            return childGroups.size();
        }
    }

    public List<ArrayList<Object>> makeChainElements(int depth) {
        List<ArrayList<Object>> chainElements = new ArrayList<ArrayList<Object>>();


        if (depth == 0) {
            ArrayList<Object> chain = new ArrayList<Object>();
            chain.add(value);
            chainElements.add(chain);
        } else {
            chainElements = makeChain(childGroups, chainElements, depth - 1);
            for (ArrayList<Object> chain : chainElements) {
                chain.add(0, value);
            }
        }

        return chainElements;
    }

    private List<ArrayList<Object>> makeChain(Set<ColumnGroup> children, List<ArrayList<Object>> chainElements, int depth) {
        if (depth == 0) {
            for (ColumnGroup group : children) {
                ArrayList<Object> chain = new ArrayList<Object>();
                chain.add(0, group.value);
                chainElements.add(chain);
            }
        } else {
            for (ColumnGroup group : children) {
                chainElements = makeChain(group.childGroups, chainElements, depth - 1);
                for (ArrayList<Object> chain : chainElements) {
                    if (chain.size() == depth) {
                        chain.add(0, group.value);
                    }
                }
            }
        }

        return chainElements;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColumnGroup other = (ColumnGroup) obj;
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
