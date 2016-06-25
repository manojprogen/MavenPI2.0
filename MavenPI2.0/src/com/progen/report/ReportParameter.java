/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author arun
 */
public class ReportParameter extends Observable implements Serializable {

    Set<ReportParameterValue> repParamValues;
    Set<String> rowViewBys;
    Set<String> colViewBys;
    private static final long serialVersionUID = 752647115562291L;

    public ReportParameter() {
        repParamValues = new LinkedHashSet<ReportParameterValue>();
        rowViewBys = new HashSet<String>();
        colViewBys = new HashSet<String>();
    }

    public void setParameter(String elementId, List<String> value, boolean excluded) {
        Iterable<ReportParameterValue> repParamIterable = Iterables.filter(repParamValues, ReportParameterValue.getReportParameterPredicate(elementId));
        Iterator<ReportParameterValue> paramIter = repParamIterable.iterator();
        if (paramIter.hasNext()) {
            ReportParameterValue param = paramIter.next();
            param.updateParamterValue(value);
            param.updateParamterAsExcluded(excluded);
        } else {
            ReportParameterValue param = new ReportParameterValue(elementId);
            param.updateParamterValue(value);
            param.updateParamterAsExcluded(excluded);
            this.repParamValues.add(param);
        }
    }

    public void setViewBys(ArrayList<String> rowViewBys, ArrayList<String> colViewBys) {
        this.rowViewBys.clear();
        this.colViewBys.clear();
        for (String rowViewBy : rowViewBys) {
            this.rowViewBys.add(rowViewBy);
        }
        for (String colViewBy : colViewBys) {
            this.colViewBys.add(colViewBy);
        }
    }

    public void setReportParametersSV(HashMap paramValues) {
        Set<String> paramKeys = paramValues.keySet();
        for (String elementId : paramKeys) {
            String valueString = paramValues.get(elementId).toString();
            List<String> value = new LinkedList<String>();
            value.add(valueString);
            this.setParameter(elementId, value, false);
        }
    }
    //added by Dinanath for colorgroup

    public void setReportParameters1(HashMap paramValues) {
        // Get a set of the entries
        Set set = paramValues.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
            String key = (String) me.getKey();
            ArrayList<String> lst = new ArrayList<String>();
            lst.add(me.getValue().toString());
            this.setParameter(key, lst, false);
        }
//        Set<String> paramKeys = paramValues.keySet();
//        for (String elementId : paramKeys) {
//
//            List<String> value = (List<String>) paramValues.get(elementId);
//            //for ( String value : valueLst )
//           this.setParameter(elementId, value,false);
//        }
    }

    public void setReportParameters(HashMap paramValues) {
        Set<String> paramKeys = paramValues.keySet();
        for (String elementId : paramKeys) {

            List<String> value = (List<String>) paramValues.get(elementId);
            //for ( String value : valueLst )
            this.setParameter(elementId, value, false);
        }
    }

    public void setReportParametersWithIncludeSelect(HashMap paramValues) {
        Set<String> paramKeys = paramValues.keySet();
        for (String elementId : paramKeys) {
            List<String> value = (List<String>) paramValues.get(elementId);
            //for ( String value : valueLst )
            this.setParameter(elementId, value, false);
        }
    }

    public void setReportParametersWithExcludeSelect(HashMap paramValues) {
        Set<String> paramKeys = paramValues.keySet();
        for (String elementId : paramKeys) {
            List<String> value = (List<String>) paramValues.get(elementId);
            //for ( String value : valueLst )
            this.setParameter(elementId, value, true);
        }
    }

    public void notifyObserversOfUpdate(Integer noOfDays) {
        this.setChanged();
        this.notifyObservers(noOfDays);
        this.clearChanged();
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;

        boolean paramValueChk = true;

        if (o != null && o instanceof ReportParameter) {
            if (repParamValues.size() == (((ReportParameter) o).getReportParameterValues().size())) {
                for (ReportParameterValue srcParamValue : repParamValues) {
                    if (!paramValueChk) {
                        return false;
                    }
                    paramValueChk = false;
                    for (ReportParameterValue cmpParamValue : ((ReportParameter) o).getReportParameterValues()) {
                        if (srcParamValue.equals(cmpParamValue)) {
                            paramValueChk = true;
                            break;
                        }
                    }
                }
                if (!paramValueChk) {
                    return false;
                }

                for (String rowElement : this.rowViewBys) {
                    if (!paramValueChk) {
                        return false;
                    }
                    paramValueChk = false;
                    for (String compRowViewBy : ((ReportParameter) o).getRowViewByForParameter()) {
                        if (rowElement.equals(compRowViewBy)) {
                            paramValueChk = true;
                            break;
                        }
                    }
                }
                if (!paramValueChk) {
                    return false;
                }

                for (String colElement : this.colViewBys) {
                    if (!paramValueChk) {
                        return false;
                    }
                    paramValueChk = false;
                    for (String compColViewBy : ((ReportParameter) o).getColViewByForParameter()) {
                        if (colElement.equals(compColViewBy)) {
                            paramValueChk = true;
                            break;
                        }
                    }
                }
                if (!paramValueChk) {
                    return false;
                }

                isEqual = true;
            }
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.repParamValues != null ? this.repParamValues.hashCode() : 0);
        hash = 11 * hash + (this.rowViewBys != null ? this.rowViewBys.hashCode() : 0);
        hash = 11 * hash + (this.colViewBys != null ? this.colViewBys.hashCode() : 0);
        return hash;
    }

    @Override
    public Object clone() {
        ReportParameter clone = new ReportParameter();
        Set<ReportParameterValue> parameterClone = new HashSet<ReportParameterValue>();
        ArrayList<String> rowViewBysClone = new ArrayList<String>();
        ArrayList<String> colViewBysClone = new ArrayList<String>();
        for (ReportParameterValue paramterValue : this.repParamValues) {
            parameterClone.add((ReportParameterValue) paramterValue.clone());
        }
        for (String rowViewBy : this.rowViewBys) {
            rowViewBysClone.add(rowViewBy);
        }
        for (String colViewBy : this.colViewBys) {
            colViewBysClone.add(colViewBy);
        }
        clone.setViewBys(rowViewBysClone, colViewBysClone);
        clone.setReportParameterValues(parameterClone);
        return clone;
    }

    @Override
    public String toString() {
        String toString = "ReportParameter";
        for (ReportParameterValue parameterValue : this.repParamValues) {
            toString = toString + "\n Report Parameter Value: " + parameterValue.toString();
        }
        return toString;
    }

    public Set<ReportParameterValue> getReportParameterValues() {
        return this.repParamValues;
    }

    public void setReportParameterValues(Set<ReportParameterValue> paramValues) {
        this.repParamValues = paramValues;
    }

    public Set<String> getRowViewByForParameter() {
        return this.rowViewBys;
    }

    public Set<String> getColViewByForParameter() {
        return this.colViewBys;
    }

    public StringBuilder toXml() {
        StringBuilder repXml = new StringBuilder("");
        repXml.append("<ReportParameters>");
        for (ReportParameterValue repValue : repParamValues) {
            repXml.append(repValue.toXml());
        }
        repXml.append("</ReportParameters>");
        return repXml;
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
    }

    public Set<String> getParameterElementIds() {
        Set<String> paramElementSet = new HashSet<String>();
        for (ReportParameterValue paramValue : repParamValues) {
            paramElementSet.add(paramValue.getElementId());
        }
        return paramElementSet;
    }

    public List<String> getParameterElementValues(String elementId) {
        Iterable<ReportParameterValue> parameters = Iterables.filter(repParamValues, ReportParameterValue.getReportParameterPredicate(elementId));
        List<String> values = new ArrayList();

        for (ReportParameterValue parameter : parameters) {
            values = parameter.getParameterValues();
        }
        if (values != null && values.isEmpty()) {
            values.add("All");
        }
        return values;
    }
}
