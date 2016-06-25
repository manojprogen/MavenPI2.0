<%-- 
    Document   : purchaseDetails
    Created on : 1 Feb, 2012, 12:07:59 PM
    Author     : progen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb"%>
<!DOCTYPE html>
<%
    String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
    PbDb pbdb = new PbDb();
    String qry = "";
    qry = "select * from prg_previlage_master";
    PbReturnObject list = pbdb.execSelectSQL(qry);
    String detailsqry = "";
    detailsqry = "select * from PRG_PREVILAGE_DETAILS";
    PbReturnObject detailslist = pbdb.execSelectSQL(detailsqry);
    String Contxpath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="<%=Contxpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=Contxpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=Contxpath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=Contxpath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=Contxpath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=Contxpath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=Contxpath%>/javascript/quicksearch.js"></script>
        <%--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" /> --%>
        <link rel="stylesheet" href="<%=Contxpath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=Contxpath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= Contxpath%>//dragAndDropTable.js"></script>
        <script type="text/javascript" src="<%=Contxpath%>/javascript/pi.js"></script>
        
    </head>
    <body>
        <form id="myFormAdmin">
            <table align="center" id="tablesorterUserList" class="tablesorter"  style="width:98%" cellpadding="0" cellspacing="1">
                <thead>
                    <tr>
                        <th> &nbsp; </th>
                        <th id="module1"> Module </th>
                        <th id="qtyPurchase"> Quantity Purchase </th>
                        <th id="qtyUsed"> Quantity Used </th>
                        <th id="qtyAvail"> Quantity Available </th>
                    </tr>
                </thead>
                <tbody>
                    <%int i = 0;
                        for (i = 0; i < list.getRowCount(); i++) {                                                                             
                    %>
                    <tr>                        
                        <td> 
                            <%=list.getFieldValueString(i, 0)%>
                        </td>
                        <td>
                            <a style="font-size:11px;" href="javascript:getSubModuleList('<%=list.getFieldValueString(i, 1)%>','<%=list.getFieldValueString(i, 0)%>')"><%=list.getFieldValueString(i, 1)%></a>
                        </td>
                        <td>
                            <input id="qpurchase_<%=i%>" type="textbox" value="0"  name="qpurchase_<%=i%>" readonly />
                        </td>
                        <td> 
                            <input id="qUsed_<%=i%>" type="textbox" value="0"  name="qUsed_<%=i%>" readonly />
                        </td>
                        <td>
                            <input id="qAvail_<%=i%>" type="textbox"  value="0"  name="qAvail_<%=i%>" readonly />
                        </td>
                        <% }%>
                    </tr>
                </tbody>
            </table>
            <table align="center">
                <tr>
                    <td><input id="hiddenEdit" class="navtitle-hover" style="width:auto" onclick="editFunction()" type="button" value="Edit"/></td>
                    <td><input id="hiddenDone" class="navtitle-hover" style="width:auto" type="button" value="Done"/></td>
                </tr>
            </table>
        </form>                    
        <div id="modifyPrivileges1" title="Privileges" >     
            <table align="right" id="buttonTab"></table>
            <table id="modifyPrivileges">
            </table>
        </div>  
        <div id="editPrivileges" title="editPrivileges" >     
            <table align="center" id="editTab" style="width:98%" cellpadding="0" cellspacing="1">
                <tbody>
                    <%
                        for (i = 0; i < list.getRowCount(); i++) {                                                                             
                    %>
                    <tr>                        
                        <td> 
                        
                        </td>
                        <td>
                            <%=list.getFieldValueString(i, 1)%>
                        </td>
                        <td>
                            <input id="module_<%=i%>" type="textbox" value="0"  name="module_<%=i%>"  />
                        </td> 
                        <%}%>                            
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr colspan="4" align="center">
                        <td colspan="4" align="center"><input type="button" value="done" onclick="doneAfterEdit()"  class="navtitle-hover" style="width:auto" /></td>
                    </tr>
                </tbody>
            </table>
        </div>
                    <script type="text/javascript">
            var isMemberUseInOtherLevel="false"
            var jsonvalues;
            var jsonVar;
            var value;
            var preViName
            $(document).ready(function(){
                $("#modifyPrivileges1").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 400,
                    position: 'justify',
                    modal: true
                });
                $("#editPrivileges").dialog({
                    autoOpen: false,
                    height: 500,
                    width: 600,
                    position: 'justify',
                    modal: true
                });
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=getXmlData&userId='+'<%=userId%>',
                    success: function(data){
                        var  jsonVariable=eval('('+data+')')
                        value = jsonVariable
                    } 
                });
            });
            function getSubModuleList(previlageName,previlageid)
            {   
                    preViName=previlageName                                       
                $("#modifyPrivileges1").dialog('open');
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=getPrivilageDetails&previlageName='+previlageName+'&previlageid='+previlageid,
                    success: function(data) { 
                        //                        alert("data="+data)
                        jsonVar=eval('('+data+')')                        
                        var keys = [];
                        for (var key in jsonVar) {
                            if (jsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }                       
                        var htmlVar=""
                        var branches="";
                        var id=0
                        jsonvalues = []; 
                        htmlVar += "<ul id='"+previlageid+"_mainModule'>"
                        for(var i=0;i<keys.length;i++)
                        {                            
                            jsonvalues = jsonVar[keys[i]]                                  
                            htmlVar += "<li class='closed' id='"+previlageid+"_"+keys[i].toString().replace(" ","_","gi").replace("/","_","gi")+"'><input id='"+keys[i].toString().replace(" ","_","gi").replace("/","_","gi")+"' name='parentLi' type='checkbox' value='"+keys[i]+"' onclick=parentCheck(this,"+previlageid+") ><span class='folder'>"+keys[i]+"</span><ul>"
                            for(var j=0;j<jsonvalues.length;j++){
                                htmlVar += "<li><span class='file'><input class="+keys[i].toString().replace(" ","_","gi").replace("/","_","gi")+"  id='"+keys[i].toString().replace(" ","_","gi")+"~"+jsonvalues[j].toString().replace(" ","_","gi")+"' name='childLi' type='checkbox'  value='"+jsonvalues[j]+"' onclick=childCheck(this,"+previlageid+")>"+jsonvalues[j]+"</span></li>"   
                            }
                            htmlVar += "</ul></li>"
                  
                        }
                        htmlVar += "</ul>";
//                        htmlVar+="<input type='button' id='modulesave' align='center' value='Done' onclick='subPrevilage(this,this,"+previlageid+")' class='navtitle-hover' />"
                         $("#buttonTab").html("<tr><td><input type=\"button\" name=\"Done\" class=\"navtitle-hover\" value=\"Done\" onclick=\"saveSubCompents("+previlageid+")\" ></td></tr>") 
                       $("#modifyPrivileges").html(htmlVar)
                        $("#modifyPrivileges").treeview({                              
                        });
                        //                        $('#'+previlageid+':selected').attr('liid', 'checked');
                        //                        $("#"+jsonvalues[j]).attr('checked',false);
                    }
                });               
            }            
            function parentCheck(objectVal,previlageid){          
                if ($('#'+objectVal.id).is(':checked')) {                   
                    $('.'+objectVal.id).each(function() {
//                         alert($(objectVal).val())
                        $('.'+objectVal.id).attr('checked',true);
                    });

                }else{
                    $('.'+objectVal.id).each(function() {
                        $('.'+objectVal.id).attr('checked',false);
                    });
                }
            }
         function saveSubCompents(moduleID){
                var keys = [];
                        for (var key in jsonVar) {
                            if (jsonVar.hasOwnProperty(key)) {
                                keys.push(key);
                            }
                        }       
            var previlagesDetails=[]
                            var tempArray
            for(var i=0;i<keys.length;i++){
                       tempArray=new Array
                  $('.'+jQuery.trim(keys[i]).toString().replace(" ","_","gi")).each(function(index) {
//                        alert( $(objectVal).val() )
                        if (this.checked){
                            tempArray.push($(this).val())
//                           alert($(this).val())
                        }                       
                    });
//                    alert("tempArray\t"+tempArray)
               previlagesDetails.push({id: keys[i], optionValue:tempArray})
                
            }
           
           
                 $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=saveSubCompents&moduleID='+moduleID+'&previlagesDetails='+previlagesDetails+'&preViName='+preViName,
                    type: "POST",
                      data: {details: JSON.stringify(previlagesDetails) },
                      dataType: "json",
                    success: function(data){
                        
                    }   
                    });
             
            }
            
               
                    
            function childCheck(objectVal,id){
                //         alert(objectVal.className)
                if(!$(objectVal).is(':checked')){
                    $('#'+objectVal.className).attr('checked',false);                                        
                }else{
                    //             alert($('.'+objectVal.className).length)
                    var status=true
                    $('.'+objectVal.className).each(function(index) {
//                        alert( $(objectVal).val() )
                        if (!this.checked){
                            status=false
                            return false
                        }                       
                    });
                    if(status) 
                        $('#'+objectVal.className).attr('checked',true);
                }                                                  
            }
            function editFunction(){
                 $("#editPrivileges").dialog('open');
                 $("#editTab").html();                 
            }
            function doneAfterEdit()
            {
                $("#editPrivileges").dialog('close');                               
            }
            function getQuality(id){
                $('#'+id).val(value)
            }
//            function subPrevilage(previlageDetailsId,previlageFunction,previlageid){
//                $.ajax({
//                    url: '/superAdminAction.do?superAdminParam=getSubPrivilageDetails&previlageDetailsId='+previlageDetailsId+'previlageFunction='+previlageFunction+'&previlageid='+previlageid,
//                    success: function(data){
//                    }   
//                    });
//            }
        </script>
    </body>
</html>
