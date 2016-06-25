/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.graph.info;

/**
 *
 * @author progen
 */
public class GraphTypeInfo {

    private int graphTypeId;
    private String graphTypeName;
    private int graphClassId;
    private String graphClassName;

    public int getGraphClassId() {
        return graphClassId;
    }

    public void setGraphClassId(int graphClassId) {
        this.graphClassId = graphClassId;
    }

    public String getGraphClassName() {
        return graphClassName;
    }

    public void setGraphClassName(String graphClassName) {
        this.graphClassName = graphClassName;
    }

    public int getGraphTypeId() {
        return graphTypeId;
    }

    public void setGraphTypeId(int graphTypeId) {
        this.graphTypeId = graphTypeId;
    }

    public String getGraphTypeName() {
        return graphTypeName;
    }

    public void setGraphTypeName(String graphTypeName) {
        this.graphTypeName = graphTypeName;
    }
}
