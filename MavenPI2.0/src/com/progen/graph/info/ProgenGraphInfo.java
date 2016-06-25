/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.graph.info;

import java.util.*;

/**
 *
 * @author progen
 */
public class ProgenGraphInfo {

    private static Map<Integer, GraphTypeInfo> graphTypes = new HashMap<Integer, GraphTypeInfo>();
    private static Map<Integer, GraphSizeInfo> graphSizes = new HashMap<Integer, GraphSizeInfo>();
    private static Map<Integer, String> graphClasses = new HashMap<Integer, String>();
    private static boolean initialized;

    public static void addGraphType(GraphTypeInfo info) {
        graphTypes.put(info.getGraphTypeId(), info);
        graphClasses.put(info.getGraphClassId(), info.getGraphClassName());
    }

    public static void addGraphSize(GraphSizeInfo info) {
        graphSizes.put(info.getSizeId(), info);
    }

    public static List<Integer> getGraphDimensions(Integer graphSizeId) {
        GraphSizeInfo info = graphSizes.get(graphSizeId);
        List<Integer> dimList = new ArrayList<Integer>();
        dimList.add(info.getHeight());
        dimList.add(info.getWidth());
        return dimList;
    }

    public static List<Integer> getGraphDimensions(String graphSizeName) {
        Integer sizeId = getGraphSizeId(graphSizeName);
        return getGraphDimensions(sizeId);
    }

    public static String getGraphSizeName(Integer sizeId) {
        GraphSizeInfo info = graphSizes.get(sizeId);
        return info.getSizeName();
    }

    public static Integer getGraphSizeId(String graphSizeName) {
        Integer sizeId = null;
        Set<Integer> keySet = graphSizes.keySet();
        Iterator<Integer> iter = keySet.iterator();

        while (iter.hasNext()) {
            Integer tempId = iter.next();
            GraphSizeInfo info = graphSizes.get(tempId);
            if (info.getSizeName().equalsIgnoreCase(graphSizeName)) {
                sizeId = tempId;
                break;
            }
        }
        return sizeId;
    }

    public static String getGraphTypeName(Integer graphTypeId) {
        GraphTypeInfo typeInfo = graphTypes.get(graphTypeId);
        return typeInfo.getGraphTypeName();
    }

    public static String getGraphClassName(Integer graphTypeId) {
        GraphTypeInfo typeInfo = graphTypes.get(graphTypeId);
        return typeInfo.getGraphClassName();
    }

    public static Integer getGraphClassId(Integer graphTypeId) {
        GraphTypeInfo typeInfo = graphTypes.get(graphTypeId);
        return typeInfo.getGraphClassId();
    }

    public static Integer getGraphTypeId(String graphTypeName) {
        Integer typeId = null;
        Set<Integer> keySet = graphTypes.keySet();
        Iterator<Integer> iter = keySet.iterator();

        while (iter.hasNext()) {
            Integer tempId = iter.next();
            GraphTypeInfo info = graphTypes.get(tempId);
            if (info.getGraphTypeName().equalsIgnoreCase(graphTypeName)) {
                typeId = tempId;
                break;
            }
        }
        return typeId;
    }

    public static Integer getGraphClassId(String className) {
        Integer classId = null;
        Set<Integer> keySet = graphClasses.keySet();
        Iterator<Integer> iter = keySet.iterator();

        while (iter.hasNext()) {
            Integer tempId = iter.next();
            String tempClass = graphClasses.get(tempId);
            if (tempClass.equalsIgnoreCase(className)) {
                classId = tempId;
                break;
            }
        }
        return classId;
    }

    public static Map<Integer, String> getGraphClasses() {
        return graphClasses;
    }

    public static void setGraphClasses(Map<Integer, String> val) {
        graphClasses = val;
    }

    public static Map<Integer, GraphSizeInfo> getGraphSizes() {
        return graphSizes;
    }

    public static void setGraphSizes(Map<Integer, GraphSizeInfo> val) {
        graphSizes = val;
    }

    public static Map<Integer, GraphTypeInfo> getGraphTypes() {
        return graphTypes;
    }

    public static void setGraphTypes(Map<Integer, GraphTypeInfo> val) {
        graphTypes = val;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void setInitialized(boolean val) {
        initialized = val;
    }
}
