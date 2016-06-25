<%-- 
    Document   : TestHTML
    Created on : Nov 12, 2009, 10:50:14 AM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTd HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <div class='portlet-header ui-widget-header ui-corner-all'>

            <Table style='height:10px'>
                <Tr valign='top' align='right'>
                    <Td valign='top' align='right' style='color:#369;font-weight:bold'>
                        <Table width='100%'>
                            <Tr>
                                <Td align='left' style='color:#369;font-weight:bold'></Td>
                                <Td align='right'>
                                    <Table border='0'>
                                        <Tr>
                                            <Td>&nbsp;&nbsp;</Td>
                                            <Td>
                                                <a href='javascript:void(0)' onclick='openREPPreviews('" + PortletId + "',document.getElementsByName('chkREP-" + PortletId + "'),document.getElementById('REP" + PortletId + "'),'" + perBy + "','" + graphType + "')'  style='text-decoration:none' class='calcTitle' title='View By'><font size='1px'><b> View By</b> </font></a> &nbsp;&nbsp;
                                                <div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='REP" + PortletId + "'>
                                                    <Table>

                                                        <Tr valign='top'>
                                                            <Td valign='top'>
                                                                <b><font size='1px'>" + reportParameters.values().toArray()[i].toSTring().split(",")[1] + "<font size='1px'></b>
                                                            </Td>
                                                        </Tr>
                                                    </Table>
                                                </div>
                                            </Td>
                                            <Td>
                                                <a href='javascript:void(0)' onclick='openCEPPreviews('" + PortletId + "',document.getElementsByName('chkCEP-" + PortletId + "'),document.getElementById('CEP" + PortletId + "'),'"+perBy+"','"+graphType+"')'  style='text-decoration:none' class='calcTitle' title='CEP'> <font size='1px'>CEP</font> </a>&nbsp;&nbsp;
                                                <div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='CEP" + PortletId + "'>
                                                    <Table>
                                                        <Tr valign='top'>
                                                            <Td valign='top'>
                                                                <b><font size='1px'>" + reportParameters.values().toArray()[i].toSTring().split(",")[1] + "<font size='1px'></b>
                                                            </Td>
                                                        </Tr>
                                                    </Table>//closing of inner Table
                                                </div>
                                            </Td>
                                            <Td>
                                                <a href='javascript:void(0)' onclick='opentopbottomDisplay(document.getElementById('TopBottom" + PortletId + "'))'  style='text-decoration:none' class='calcTitle' title='Sort'><font size='1px'><b> Sort </b></font></a> &nbsp;&nbsp;
                                                <div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='TopBottom" + PortletId + "'>
                                                    <Table>
                                                        <Tr>
                                                            <Td valign='top'>
                                                                sas

                                                            </Td>
                                                        </Tr>
                                                    </Table>
                                                </div>
                                            </Td>
                                            <Td>
                                                <a href='javascript:void(0)' onclick='openGrpTypeDisplay(document.getElementById('GrpType" + PortletId + "'))'  style='text-decoration:none' class='calcTitle' title='Sort'><font size='1px'><b>Graph Types</b> </font></a> &nbsp;&nbsp;
                                                <div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='GrpType" + PortletId + "'>
                                                    <Table>
                                                        <Tr valign='top'>
                                                            <Td valign='top'>
                                                                sasa
                                                            </Td>
                                                        </Tr>
                                                    </Table>
                                                </div>
                                            </Td>
                                        </Tr>
                                    </Table>
                                </Td>
                            </Tr>
                        </Table>
                    </Td>
                </Tr>
            </Table>
        </div>

        <div style='width:415px;min-height:100px;max-height:320px;overflow-y:auto;overflow-x:auto'>
            <Table width='100%'>
                <Tr valign='top'>
                    <Td valign='top' style='color:#369;font-weight:bold'>
                        TableBuffer.append(pcharts[0].charTdisplay);
                    </Td>
                    <Td align='right'> </Td>
                </Tr>
            </Table>
        </div>
    </body>
</html>
