<%-- 
    Document   : pbStickyNote
    Created on : Aug 29, 2009, 3:59:10 PM
    Author     : Administrator
--%>
<%

%>
<%@include file="pbGetStickyNotes.jsp" %>
<%@page import="java.util.HashMap"%>

<%
            String repId = (String) request.getAttribute("REPORTID");
            HashMap stickListHashMap = new HashMap();
            if(session.getAttribute("stickListHashMap")!=null){
            stickListHashMap =  (HashMap) session.getAttribute("stickListHashMap");
            }
            //String TimeLevel = (String) request.getAttribute("timelevel");
//////////////////////////////////////////////////////////////////////////////////////////////////.println.println("repId in pbStickyNote is "+repId);
%>

<SPAN id="MyStick">
    <%
            int divLeft = 0;
            int divTop = 0;
            String styleStr = null;
            String divId = null;
            String sNote = null;
            String stickId = null;
            String closeFn = null;
            String tAreaSave = null;
            String tAreaBlur = null;
            String moveFun = null;
            String tAreaFoc = null;
            String createdDate = null;
            for (int i = 0; i < stickyNoteCount; i++) {
                if (listcount.contains(i)) {
                    divLeft = stickyRetObj.getFieldValueInt(i, "POS_LEFT");
                    divTop = stickyRetObj.getFieldValueInt(i, "POS_TOP");
                    stickId = stickyRetObj.getFieldValueString(i, "STICKY_NOTES_ID");
                    sNote = stickyRetObj.getFieldValueString(i, "S_NOTE");
                    createdDate = stickyRetObj.getFieldValueString(i, "CREATED_ON");
                    sNote = sNote.trim();
                    styleStr = "left:" + divLeft + "px;top:" + divTop + "px;";
                    divId = "NOTE_" + (i + 1);
                    closeFn = "Close('" + divId + "');return false;";
                    tAreaSave = "javascript:saveNote('txt"+divId+"','" + divId + "','')";
                    tAreaBlur = "javascript:updateNote(this,'" + divId + "','')";
                    tAreaFoc = "javascript:tAreaFocus(this)";
                    moveFun = "javascript:funUp('" + divId + "')";
                    String styleType = "";
                    if(stickListHashMap!=null){
                        styleType = (String) stickListHashMap.get(divId);
                        }
    %>
    <DIV id='<%=divId%>' style='display:<%=styleType%>;z-index:1000;position:absolute;width:150px;<%=styleStr%>;'>
        <Table border='0' width='100px' class='mycls' cellspacing='0' cellpadding='0' style="height:auto">
            <Tr  onmousemove="divMove('<%=divId%>')" class="navtitle1">
                <!--<Tr style='background-color:#ffff00'>-->
                <Td id='titleBar' onMouseUp="<%=moveFun%>" style='cursor:move' width='70%'>
            <ilayer width='100%' onSelectStart='return false'>
                <layer id='xyz' width='100%' onMouseover='isHot=true;if (isN4) ddN4(theLayer)' onMouseout='isHot=false'>
                    <font><%=stickyRetObj.getFieldValueString(i, "STICKY_LABEL")%></font>
                </layer>
            </ilayer>
            </Td>
            <Td style='cursor:hand' valign='top' width="10%" title="Save Note">
                <a href='javascript:void(0)' onclick="<%=tAreaSave%>" class="ui-icon ui-icon-disk"></a>
            </Td>
            <Td style='cursor:hand' valign='top' width="10%" title="Hide Note">
                <a href='javascript:void(0)' onclick="hideStickText('<%=divId%>','<%=stickyRetObj.getFieldValueString(i, "STICKY_LABEL")%>')" class="ui-icon ui-icon-gear"></a>
            </Td>
            <Td style='cursor:hand' valign='top' width="10%" title="Delete Note">
                <a href='javascript:void(0)' onClick="<%=closeFn%>" class="ui-icon ui-icon-trash"></a>
            </Td>
            </Tr>
            <Tr id="stickTextTr">
                <Td width='100%' valign='top' bgcolor='#d7faff' colspan='4'>
                    <textarea name="sticknote" cols="18" rows="8" class="mycls" style="overflow:auto;font-size:10px;font-family:verdana" id="txt<%=divId%>" lang="<%=stickId%>"
                              onfocus="<%=tAreaFoc%>" onblur="<%=tAreaBlur%>"><%=sNote%></textarea>
                </Td>
            </Tr>
        </Table>
    </DIV>
    <%}
            }
    %>
</SPAN>
<IFRAME NAME="updateFrame" ID="updateFrame" STYLE="width:0px;height:0px;visibility:hidden;" SRC="pbUpdateNotes.jsp?REPORTID=<%=repId%>"></IFRAME>

