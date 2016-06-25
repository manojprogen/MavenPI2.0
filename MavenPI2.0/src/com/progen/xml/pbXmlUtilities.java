/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.xml;

import java.util.List;
import org.jdom.Element;

/**
 *
 * @author db2admin
 */
public class pbXmlUtilities {

    public pbXmlUtilities() {
    }

    public String getXmlTagValue(Element parentElement, String childName) {
        return (((Element) ((List) parentElement.getChildren(childName)).get(0)).getText());
    }
}
