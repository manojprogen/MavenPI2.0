/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.xml;

/**
 *
 * @author db2admin
 */
import java.io.ByteArrayInputStream;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ReadingxmlusingJdom {

    public static void main(String[] args) throws Exception {
        String data =
                "<root>"
                + "<Companyname>"
                + "<Employee name=\"Girish\" Age=\"25\">Developer</Employee> Amit111"
                + "</Companyname>"
                + "<Companyname>"
                + "<Employee name=\"Komal\" Age=\"25\">Administrator</Employee>"
                + "</Companyname>"
                + "<Companyname1>"
                + "<Employee name=\"Girish\" Age=\"25\">Developer1</Employee>"
                + "</Companyname1>"
                + "<Companyname1>"
                + "<Employee name=\"Komal\" Age=\"25\">Administrator1</Employee>"
                + "</Companyname1>"
                + "<Companyname2>"
                + "<Employee> <Name></Name><Title>Head- Product Development</Title></Employee>"
                + "<Employee> <Name>Amit1</Name><Title>Head- Product Development111</Title></Employee>"
                + "</Companyname2>"
                + "</root>";
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new ByteArrayInputStream(data.getBytes()));
        Element root = document.getRootElement();
        List row = root.getChildren("Companyname");
        for (int i = 0; i < row.size(); i++) {
            Element Companyname = (Element) row.get(i);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Companyname"+Companyname.getText());

            List column = Companyname.getChildren("Employee");
            for (int j = 0; j < column.size(); j++) {
                Element Employee = (Element) column.get(j);
                String name = Employee.getAttribute("name").getValue();
                String value = Employee.getText();
                int length = Employee.getAttribute("Age").getIntValue();

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Name = " + name);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Profile = " + value);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Age = " + length);
            }
        }

        ////////////////////////

        row = root.getChildren("Companyname1");
        for (int i = 0; i < row.size(); i++) {
            Element Companyname = (Element) row.get(i);


            List column = Companyname.getChildren("Employee");
            for (int j = 0; j < column.size(); j++) {
                Element Employee = (Element) column.get(j);
                String name = Employee.getAttribute("name").getValue();
                String value = Employee.getText();
                int length = Employee.getAttribute("Age").getIntValue();

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Name 1= " + name);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Profile1 = " + value);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Age1 = " + length);
            }
        }

        /////New Testing //////////
        row = root.getChildren("Companyname2");
        for (int i = 0; i < row.size(); i++) {
            Element Companyname = (Element) row.get(i);


            List column = Companyname.getChildren("Employee");
            for (int j = 0; j < column.size(); j++) {
                Element Employee = (Element) column.get(j);



                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Name 2= " + ((Element)((List)Employee.getChildren("Name")).get(0)).getText());


                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Profile2 = " + ((Element)((List)Employee.getChildren("Title")).get(0)).getText());






            }
        }




    }
}
