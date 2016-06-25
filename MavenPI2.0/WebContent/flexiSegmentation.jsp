<%--
    Document   : flexiSegmentation
    Created on : 2 Mar, 2012, 3:38:40 PM
    Author     : ramesh janakuttu
--%>

<%@page import="prg.db.PbDb,prg.db.PbReturnObject" contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            PbReturnObject returnObject=new PbReturnObject();
             PbDb pbDb = new PbDb();
             String conquery="SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
             String conid=null;
             returnObject=pbDb.execSelectSQL(conquery);
             for(int i=0;i<returnObject.getRowCount();i++)
             {
             conid=returnObject.getFieldValueString(i,0);
             }
String contextPath=request.getContextPath();
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
     <!--   <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />-->
          <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
          <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <title></title>
        <style type="text/css">
            .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: black;
                padding-left:12px;
                border:0px;
            }
            #ui-datepicker-div
            {
                z-index: 9999999;
            }
        </style>
        <script type="text/javascript">
          var grpmember="";
          var grpvalues="";
          $(document).ready(function(){
              $('#startdate1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat:'yy-mm-dd'

                });
                $('#enddate1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat:'yy-mm-dd'
                });
              var conid=<%=conid%>
              $.get('<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=getFlexiNames&conid='+conid,function(data)
               {    var length=data.length
                     $("#allModifyMeasures").val(length);
                    var jsonVar=eval('('+data+')')
                    var tablenames=jsonVar.tablenames
                    var groupname=jsonVar.groupnames
                    var tableid=jsonVar.flexitableid
                     grpmember=jsonVar.grpmember
                     grpvalues=jsonVar.grpvalues
                     var htmlVar=""
                    for(var i=0;i<tablenames.length;i++)
                        {
                          htmlVar+="<tr><td><input type='text' name='flexitablenames' value='"+tablenames[i]+"' id='flexitableid"+i+"'/><input type='hidden' name='tableid' value='"+tableid[i]+"' id='tableid"+i+"'/></td>\n\
                                        <td><input type='text' name='groupnames' value='"+groupname[i]+"' id='groupname"+i+"'/></td>\n\
                                         <td><img border='0' align='middle' title='edit' onclick='editsegvalues("+i+")' alt='' src='/pi/images/editList.gif'></td></tr>"
                        }
                     $("#flexiTabBody").html(htmlVar)
                      $("#flexitableId ").tablesorter( {headers : {0:{sorter:false}}} ).tablesorterPager({container: $("#flexipager") });
               });


              if ($.browser.msie == true){
                  $("#flexigroupid").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'justify',
                        modal: true

                    });
                    $("#groupdiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'justify',
                        modal: true

                    });
                    $("#membrvaldivid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                     $("#flexidivid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                   $("#crategroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#editgroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#deletegroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                     $("#deletetypesid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#deleterecordsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#deletetablesid").dialog({
                        autoOpen: false,
                        height: 800,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                     $("#deleteOptions").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#createdatedivid").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });

                    $("#nextDivId").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });

              }
              else
                  {
                   $("#flexigroupid").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#groupdiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#membrvaldivid").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#flexidivid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#crategroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#editgroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#deletegroupsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });

                    $("#deletetypesid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#deleterecordsid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#deletetablesid").dialog({
                        autoOpen: false,
                        height: 800,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#deleteOptions").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#createdatedivid").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#nextDivId").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                  }
          });
         </script>
         <script type="text/javascript">
              var conId="";
              var id;
              var rowCount="";
              var tablename="";
              var noid="";
              var groupid="";
              var tablename2="";
              var tempId="";
              var flexi_id="";
              var tabname="";
              var groupname="";
              var groupmembers="";
              var flextableid="";
              var countArray=new Array();
              var temparray=new Array();
              var temparray1=new Array();
              var temparray2=new Array();
              var grpnameArray=new Array();
              var tabnameArray=new Array();
              var typnameArray=new Array();
              var tableid="";
               function creategroups()
               {

                 var x= $("#crategroupsid").html();
                 $("#data").html(x);
               }
               function editgroups()
               {
                  var x= $("#editgroupsid").html();
                 $("#data").html(x);
               }
               function deleteoption()
               {
                   $("#deleteOptions").dialog('open');

               }
               function deletetypes(){
                   $("#deleteOptions").dialog('close');
                   var connid=<%=conid%>
                   $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getGroupnames&connId='+connid,
                    success: function(data){
                   
                             var jsonVar=eval('('+data+')')
                             var groupname=jsonVar.grplilst
                              var htmlVar="<option value=0000>--Select--</option>"
                             for(var i=0;i<groupname.length;i++)
                                 {
                                     htmlVar+="<option value="+groupname[i].replace(" ", "_", "gi")+">"+groupname[i]+"</option>"
                                  }

                             $("#grpnames").html(htmlVar);
                               }
                   });
                                var x= $("#deletetypesid").html();
                 $("#data").html(x);
                    
               }

               function getRelatedRecords1()
               {
                 var connid=<%=conid%>
                    var typesname=$("#typnames1").val()
                    typesname = typesname.replace("_", " ", "gi");
                   
                   $.post('targetmeasuresaction.do?targetMeasures=getRecords&connId='+connid+"&typesname="+typesname,
                        function(data)
                               {
                             var jsonVar=eval('('+data+')')

                                tablenames=jsonVar.tablenames;
                                groupnames=jsonVar.groupnames;
                                typenames=jsonVar.typenames;
                                flexiName=jsonVar.flexiName;
                                 flexiIdtext=jsonVar.flexiIdtext;
                                 flexiIdnum=jsonVar.flexiIdnum;
                                 flexitableid=jsonVar.flexitableid;
                              var htmlVar="<tr><td class='navtitle-hover'>Table Name</td><td class='navtitle-hover'>Group Name</td><td class='navtitle-hover'>Type Name</td><td class='navtitle-hover'>Flexi Name</td><td class='navtitle-hover'>Flexi Id Text</td><td class='navtitle-hover'>Flexi Id Num</td><td class='navtitle-hover'>Flexi Table Id</td><td class='navtitle-hover'>Delete</td></tr>";
                             for(var i=0;i<tablenames.length;i++)
                                 {
                                    
                                     htmlVar+="<tr><td><input type='text' name='flexitablenames' value='"+tablenames[i]+"' id='flexitablename"+i+"'/></td>\n\
                                        <td><input type='text' name='groupnames' value='"+groupnames[i]+"' id='groupnames"+i+"'/></td>\n\
                                          <td><input type='text' name='typenames' value='"+typenames[i]+"' id='typenames"+i+"'/></td>\n\
                                          <td><input type='text' name='flexiName' value='"+flexiName[i]+"' id='flexiName"+i+"'/></td>\n\
                                          <td><input type='text' name='flexiIdtext' value='"+flexiIdtext[i]+"' id='flexiIdtext"+i+"'/></td>\n\
                                          <td><input type='text' name='flexiIdnum' value='"+flexiIdnum[i]+"' id='flexiIdnum"+i+"'/></td>\n\
                                          <td><input type='text' name='flexitableid' value='"+flexitableid[i]+"' id='flexitableid"+i+"'/></td>\n\
                                          <td align='center' valign='top'><a class='ui-icon ui-icon-trash' title='Delete Record' onclick=\"deleteRec("+i+")\" href='javascript:void(0)'></a></td></tr>"
                                    <%--","+grpname+","+typname+","+flxname+","+flxtext+","+flxnum+","+flxtable+
                                    ,groupname,typename,flexiname,flexitext,flexinum,flexitable--%>
                                }
                              $("#deleteflexirecordid").html(htmlVar);
                               });
               }
               function deleteRec(id)
               {
                  
                                     var tabname11=$("#flexitablename"+id).val();
                                     var grpname11=$("#groupnames"+id).val();
                                     var typname11=$("#typenames"+id).val();
                                     var flxname11=$("#flexiName"+id).val();
                                     var flxtext11=$("#flexiIdtext"+id).val();
                                     var flxnum11=$("#flexiIdnum"+id).val();
                                     var flxtable11=$("#flexitableid"+id).val();
                                     
                    var connId=<%=conid%>
                    var confirmText= confirm("All records having similar values will be delete");
                            if(confirmText==true){
                   $.post('targetmeasuresaction.do?targetMeasures=deleteRecord&connId='+connId+'&tablename='+tabname11+'&groupname='+grpname11+'&typename='+typname11+'&flexiname='+flxname11+'&flexitext='+flxtext11+'&flexinum='+flxnum11+'&flexitable='+flxtable11,
                        function(data)
                               { alert("Records deleted")
                                   getRelatedRecords1();
                               });
                            }
               }
               function getRelatedTypes1()
               {
                 var connid=<%=conid%>
                    var groupsname=$("#grpnames1").val()
                    groupsname = groupsname.replace("_", " ", "gi");
                   $.post('targetmeasuresaction.do?targetMeasures=getTypenames&connId='+connid+"&groupsname="+groupsname,
                        function(data)
                               {
                             var jsonVar=eval('('+data+')')
                             var typename=jsonVar.typlist
                              var htmlVar="<option value=0000>--Select--</option>"
                             for(var i=0;i<typename.length;i++)
                                 {   htmlVar+="<option value="+typename[i].replace(" ", "_", "gi")+">"+typename[i]+"</option>"
                                  }

                             $("#typnames1").html(htmlVar);
                               });

               }
               function getRelatedGroups1()
               {
                  var connid=<%=conid%>
                  var tabname=$("#tabnames1").val();
                  
                  
                   $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getGroupnames1&connId='+connid+"&tabname="+tabname,
                    success: function(data){

                             var jsonVar=eval('('+data+')')
                             var groupname=jsonVar.grplilst
                              var htmlVar="<option value=0000>--Select--</option>"
                             for(var i=0;i<groupname.length;i++)
                                 {
                                     htmlVar+="<option value="+groupname[i].replace(" ", "_", "gi")+">"+groupname[i]+"</option>"
                                  }

                             $("#grpnames1").html(htmlVar);
                               }
                   });
                                var x= $("#deleterecordsid").html();
                 <%--$("#data").html(x);--%>
               }
               function deletegroups()
               {
                   $("#deleteOptions").dialog('close');
                   var connid=<%=conid%>

                   $.ajax({
                       url:'targetmeasuresaction.do?targetMeasures=getGroupnames&connId='+connid,
                       success: function(data)
                               {
                             var jsonVar=eval('('+data+')')
                             var groupname=jsonVar.grplilst
                              var htmlVar=""
                             for(var i=0;i<groupname.length;i++)
                                 {
                                  htmlVar+="<tr><td><input type='text' name='groupname"+i+"' value='"+groupname[i]+"' id='groupname"+i+"' readonly/></td>\n\
                                                <td><input type='checkbox' name='groupnamechk' id='groupnamechk"+i+"' value='groupnamechk"+i+"' onclick=checkgrp("+i+") /></tr>"
                                 }
                              $("#deleteflexigroupid").html(htmlVar);
                               }
                               });
                    var x= $("#deletegroupsid").html();
                 $("#data").html(x);
               }
               function getRelatedTypes()
               {
                    var connid=<%=conid%>
                    var groupsname=$("#grpnames").val()
                    groupsname = groupsname.replace("_", " ", "gi");
                   $.post('targetmeasuresaction.do?targetMeasures=getTypenames&connId='+connid+"&groupsname="+groupsname,
                        function(data)
                               {
                             var jsonVar=eval('('+data+')')
                             var typename=jsonVar.typlist
                              var htmlVar=""
                             for(var i=0;i<typename.length;i++)
                                 {
                                  htmlVar+="<tr><td><input type='text' name='typename"+i+"' value='"+typename[i]+"' id='typename"+i+"' readonly/></td>\n\
                                                <td><input type='checkbox' name='typenamechk' id='typenamechk"+i+"' value='typenamechk"+i+"' onclick=checktyp("+i+") /></tr>"
                                 }
                              $("#deleteflexitypeid").html(htmlVar);
                               });
                    var x= $("#gettypesid").html();
                 <%--$("#data").html(x);--%>
               }
               function checktyp(id)
               {
                   if(document.getElementById("typenamechk"+id).checked==true)
                             { 
                                temparray2.push(id);
               
                             }
               }
               function deleterecords()
               {
                  $("#deleteOptions").dialog('close');
                   var connid=<%=conid%>
                    $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getTablenames&connId='+connid,
                         success: function(data){

                             var jsonVar=eval('('+data+')')
                             var tablename=jsonVar.tablilst
                              var htmlVar="<option value=0000>--Select--</option>"
                             for(var i=0;i<tablename.length;i++)
                                 {
                                       htmlVar+="<option value="+tablename[i]+">"+tablename[i]+"</option>"
                                  }

                             $("#tabnames1").html(htmlVar);
                               }
                              });
                                var x= $("#deleterecordsid").html();
                             $("#data").html(x);
                   }
               function deletetables()
               {
                   $("#deleteOptions").dialog('close');
                   var connid=<%=conid%>
                    $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getTablenames&connId='+connid,
                    success: function(data){
                  
                             var jsonVar=eval('('+data+')')
                             var tablename=jsonVar.tablilst
                              var htmlVar=""
                             for(var i=0;i<tablename.length;i++)
                                 {
                                  htmlVar+="<tr><td><input type='text' name='tablename"+i+"' value='"+tablename[i]+"' id='tablename"+i+"' readonly/></td>\n\
                                                <td><input type='checkbox' name='tablenamechk' id='tablenamechk"+i+"' value='tablenamechk"+i+"' onclick=checktab("+i+") /></tr>"
                                 }
                              $("#deleteflexitableid").html(htmlVar);
                               }
                    });
                    var x= $("#deletetablesid").html();
                 $("#data").html(x);
               }

               function checktab(id)
               {
                   if(document.getElementById("tablenamechk"+id).checked==true)
                             { 
                                temparray1.push(id);

                             }
               }
               function checkgrp(id)
               {
                  
                 
                   if(document.getElementById("groupnamechk"+id).checked==true)
                             {
                                
                           
                                temparray.push(id);
                           <%--     alert('checkArray--'+temparray)
                                  for(var i=0;i<temparray.length;i++)
                                      {
                                          var groupid=temparray[i];
                                          var grpname=$("#groupname"+groupid).val()
                                          alert('grpname'+grpname)
                                         grpnameArray.push(grpname)
                                         alert('grpnameArray'+grpnameArray)
                                      }--%>
                             }
               }
               function deletegrps()
               {
                   for(var i=0;i<temparray.length;i++)
                                      {
                                          var groupid=temparray[i];
                                          var grpname=$("#groupname"+groupid).val()
                                         grpnameArray.push(grpname)
                                      }

                   var connId=<%=conid%>
                   $.post('targetmeasuresaction.do?targetMeasures=deleteGroupNames&connId='+connId+'&grpnameurl='+grpnameArray, $("#GroupForm").serialize(),
                        function(data)
                               { deletegroups();
                               });
               }

               function deletetyps()
               {
                 var confirmText= confirm("Are you sure you want to delete types");
                            if(confirmText==true){

                   for(var i=0;i<temparray2.length;i++)
                                      {
                                          var typeid=temparray2[i];
                                          var typname=$("#typename"+typeid).val()

                                         typnameArray.push(typname)
                                      }

                   var connId=<%=conid%>
                   $.post('targetmeasuresaction.do?targetMeasures=deleteTypeNames&connId='+connId+'&typnameurl='+typnameArray,
                        function(data)
                               {alert("Types Deleted successfully");
                                  getRelatedTypes();
                               });

                          <%-- window.location.href = window.location.href;--%>
                            }
               }
                function deletetabs()
               {
                   var confirmText= confirm("Are you sure you want to delete tables");
                            if(confirmText==true){

                   for(var i=0;i<temparray1.length;i++)
                                      {
                                          var tableid=temparray1[i];
                                          var tabname=$("#tablename"+tableid).val()

                                         tabnameArray.push(tabname)
                                      }

                   var connId=<%=conid%>
                   $.post('targetmeasuresaction.do?targetMeasures=deleteTableNames&connId='+connId+'&tabnameurl='+tabnameArray, $("#TableForm").serialize(),
                        function(data)
                               {alert("Tables Deleted successfully");
                                   $("#data").html(htmlVar);
                               });
                               
                          <%-- window.location.href = window.location.href;--%>
                            }
               }

             function addRow()
              {
              rowCount=$('#segmntTableId tr').length
              countArray.push(rowCount);
            $("#segmntTableId").append("<tr><td><input type='text' name='membergrp'  style='width:120px' id='membergrpid"+rowCount+"'/></td>\n\
           <td><select id='selectid"+rowCount+"' class='grpMembrsid' name='selectedroles' onchange='changeImg(this.id)'><option value='IN'>IN</option><option value='LIKE'>LIKE</option></select></td>\n\
           <td><input type='text' name='membervalues' maxlength='255' style='width: 120px;' onclick='segmntMemCols(this.id)'id='membervalueid"+rowCount+"'><input type='hidden' name='hiddenmemberid' id='hiddenmemberid"+rowCount+"' value=''> </td>\n\
           <td><input type='text' name='textid'  style='width:120px;display: none' id='likeId1'/> </td>\n\
           <td><img id='imageid"+rowCount+"' border='0' align='middle' onclick='getLikeValues(this.id)' src='/pi/icons pinvoke/plus-circle.png'></td></tr>")
            $("#grpMembrsid"+rowCount).html($("#grpMembrsid1").html())
             $("#membergrpid"+rowCount).val('');
            }
             function deleteParentRow(tableID)
             {
                try {
                    var table =tableID ;
                    var rowCount = table.rows.length;
                    if(rowCount > 2)
                    {
                        var membergrpid=$("#membergrpid"+(rowCount-1)).val()
                        var membervalueid=$("#membervalueid"+(rowCount-1)).val()
                        var flexinumid=$("#flexiidnum"+(rowCount-1)).val()
                        $.post('targetmeasuresaction.do?targetMeasures=deleteFlexiValues&membergrpid='+membergrpid+'&membervalueid='+membervalueid+'&flexiidnum='+flexinumid, $("#columnForm").serialize(),
                        function(data)
                               {
                               });
                        table.deleteRow(rowCount - 1);
                    }
                    }catch(e)
                    {
                    alert(e);
                    }
            }
     function getGroupValues(id)
      {
          noid=id
          tablename=$("#flexitabid"+noid).val();
          flextableid=$("#flexitableid"+noid).val();
          $("#groupid").val('');
          flexi_id=$("#flexi_id"+noid).val();
          $("#flexigroupid").dialog('open');
      }
      function getgroupnames()
      {
          $("#membergrpid1").val('')
          $("#membervalueid1").val('')
          $("#segmntTableId").find("tr:gt(1)").remove();
       var grpname = $("#groupid").val();
       if(grpname!=''){
        $("#groupdiv").dialog('open');
                $("#flexigroupid").dialog('close');
       }else{
           alert("Please enter the Group Name")
       }
      }

      function saveGrpMems()
            {

                var valListUl=document.getElementById("sortable");
                var valListUlIds=valListUl.getElementsByTagName("li");
                var valListUlnames=[]
                 var valList=[];
                var tableObject=""
                var trobject=""
                var tdObject=""
                for(var i=0;i<valListUlIds.length;i++){
                    tableObject= valListUlIds[i].getElementsByTagName("table")
                    trobject=tableObject[0].getElementsByTagName("tr")
                    tdObject=trobject[0].getElementsByTagName("td")
            valList.push(valListUlIds[i].id.split("_li")[0]+"~"+tdObject[1].innerHTML)
            valListUlnames.push(tdObject[1].innerHTML)
                }
                <%--alert("valList\t"+valList)
                alert("valListUlnames\t"+valListUlnames)--%>
//                        alert("id==="+id+"======")
                $("#"+id).val(valListUlnames);
                <%--alert("id\t"+id)--%>
//                    alert("valList\t"+valList)
//                    alert("-----"+id.toString().replace("membervalueid","hiddenmembervalueid","gi")+"---")
                $("#"+id.toString().replace("membervalueid","hiddenmembervalueid","gi")).val(valList);

                 $("#hiddenmemberid"+rowCount).val(valList);

                $("#membrvaldivid").dialog('close')
            }
            function closebutt(){
                $("#groupdiv").dialog('close');
            }
      function segmntMemCols(sid)
      {
           var conId=<%=conid%>

           id=sid;
           var tempID=sid.toString().replace("membervalueid","selectid","gi")
          var TableName=tablename
          var Flextableid=flextableid
         <%-- alert(Flextableid);--%>
         if(TableName!="")
         {
         if($("#"+tempID).val()!="LIKE"){
          $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getFlexiColumns&tablename='+TableName+'&flextableid='+Flextableid+'&conId='+conId,
                    success: function(data){

                        var jsonVar=eval('('+data+')')

                        $("#htmsdrgndrpid").html("")
                        $("#htmsdrgndrpid").html(jsonVar.htmlStr);
                        $("#membrvaldivid").dialog('open');
                        isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                        $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        })
                        $(".myDragTabs").draggable({

                            helper:"clone",
                            effect:["", "fade"]
                        });
                        $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
                    );
                        //                        grpColArray=jsonVar.memberValues
                        $(".sortable").sortable();
                    }
                })
      }
         }
         else
         {
         var tabName=tabname
         if($("#"+tempID).val()!="LIKE"){
          $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getFlexiColumns&tablename='+tabName+'&flextableid='+Flextableid+'&conId='+conId,
                    success: function(data){
                        var jsonVar=eval('('+data+')')

                        $("#htmsdrgndrpid").html("")
                        $("#htmsdrgndrpid").html(jsonVar.htmlStr);
                        $("#membrvaldivid").dialog('open');
                        isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                        $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        })
                        $(".myDragTabs").draggable({

                            helper:"clone",
                            effect:["", "fade"]
                        });
                        $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
                    );
                        //                        grpColArray=jsonVar.memberValues
                        $(".sortable").sortable();
                    }
         });
      }
         }
      }

      function saveGrpMemVals()
      {

        var Tablename=tablename

        var count=rowCount

        var membergrpid=$("#membergrpid"+count).val()
        var  conId= $("#connnames").val();
        var grpName = $("#groupid").val();
        var flexiid=flexi_id
        var flexi_table_id=flextableid
        var membergroupidArray=new Array();
        var membervalueArray=new Array();
        var hiddenmemArray=new Array();
    if(Tablename!="")
        {
   $.post('targetmeasuresaction.do?targetMeasures=saveFlexiSegmentationTableNames&conId='+conId+'&tablename='+Tablename+'&grpName='+grpName+'&flexiid='+flexiid+'&flexitableid='+flexi_table_id, $("#FlexiForm").serialize(),
                        function(data)
                               {
                                });
            $("#groupdiv").dialog('close');
          $("#flexigroupid").dialog('close');

        }
        else
            {

                for(var i=0;i<countArray.length;i++)
                    {
                        var countid=countArray[i];
                        var membergrp=$("#membergrpid"+countid).val()

                        var membervalue=$("#membervalueid"+countid).val()
                        var hiddenmemid=$("#hiddenmemberid"+countid).val()

                        membergroupidArray.push(membergrp);
                        membervalueArray.push(membervalue);
                        hiddenmemArray.push(hiddenmemid);

                    }
                var hiddenmembervalueid=$("#hiddenmembervalueid"+count).val()
                var conId=<%=conid%>
                var tabName=tabname
                 var grpname= groupname
                 var groupmembers1=groupmembers
                 var tabid=tableid


               $.post('targetmeasuresaction.do?targetMeasures=saveFlexiSegmentationTableNames&conId='+conId+'&tablename='+tabName+'&grpName='+grpname+'&flexiid='+flexiid+'&groupmembers1='+groupmembers1+'&membergrouptotalpath='+membergroupidArray+'&membervaluepath='+membervalueArray+'&hiddenmempath='+hiddenmemArray+'&tabid='+tabid, $("#FlexiForm").serialize(),
                        function(data)
                               {
                                });

                                $("#groupdiv").dialog('close');
          $("#flexigroupid").dialog('close');
            }


           }
      function getTables()
      {
        conId= $("#connnames").val()

        $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getFlexiSegmentationTableNames&conId='+conId,
                    success: function(data){
                           var jsonVar=eval('('+data+')')
                    var flexitablename=jsonVar.flexitablename
                    var flexiid=jsonVar.flexiids
                    var flexioriginalid=jsonVar.flexioriginal
                    var fleximapid=jsonVar.fleximapid
                    var flexi_id=jsonVar.flexiid
                    var static_table=jsonVar.statictable
                    var static_view=jsonVar.staticview

                    var htmlVar=""
                    var jsonvaleuse=""
                    for(var i=0;i<flexitablename.length;i++)
                        {
                             htmlVar+="<tr><td><input type='text' name='flexitableids' value='"+flexiid[i]+"' id='flexitableid"+i+"'/>\n\
                 <td><input type='text' name='flexitablenames' value='"+flexitablename[i]+"' id='flexitabid"+i+"'/>\n\
                        <td><input type='text' name='fleximapid' value='"+fleximapid[i]+"' id='fleximapid"+i+"'/>\n\
                                               <input type='hidden' name='flexiid' value='"+flexiid[i]+"' id='flexiid"+i+"'/><input type='hidden' name='flexioriginalid' value='"+flexioriginalid[i]+"' id='flexioriginalid"+i+"'/><input type='hidden' name='flexi_id' value='"+flexi_id[i]+"' id='flexi_id"+i+"'/></td>\n\
                                           <td><input type='text' name='groupnames'  id='groupnames"+i+"'value=''/><input type='hidden' name='fleximapid'  id='fileximapid"+i+"'value='"+fleximapid[i]+"'/><td>\n\
                                           <td><img border='0' align='middle'  src='/pi/icons pinvoke/plus-circle.png' onclick='getGroupValues("+i+")'>"
                                           if(static_table[i]!="" && static_view[i]!="") {
                                            htmlVar+="<td><input type='button' value='Move Data' name='Move Data' onclick=\"MoveData('"+static_table[i]+"','"+static_view[i]+"')\" class='navtitle-hover'/></td>";
                                           }
                                           htmlVar+="</tr>";
                        }
                        $("#flexisegmentationid").html(htmlVar);
                    }
        });
      }
      function MoveData(statictable,staticview)
      { 
          $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=moveFlexiSegmentationTableData&conId='+conId+"&statictable="+statictable+"&staticview="+staticview,
                    success: function(data){
                        alert("data moved successfully");
                        getTables();
                    }

        });
      }
      function changeImg(id)
      {
         var selectedvalue=$("#"+id).val();
          var length=id.substring(8, id.length);
            if(selectedvalue=="IN")
              {
                  $("#imageid"+length).hide();
              }
            else
            {
             $("#imageid"+length).show();
         }
          if(selectedvalue=="select")
              {
               $("#imageId").hide();
              }
      }
      function getLikeValues(id)
      {
        tempId=id
        var rowno=noid
        var flexioriginalid=$("#flexioriginalid"+rowno).val()
        var fileximapid=$("#fileximapid"+rowno).val()
        tablename2=$("#flexitabid"+rowno).val()
        var connId=$("#connnames").val()
        groupid=$("#groupid").val()
     $.post('targetmeasuresaction.do?targetMeasures=getFlexiSegmentationTableColumns&conId='+connId+'&tablename='+tablename2+'&flexioriginalid='+flexioriginalid+'&fileximapid='+fileximapid, $("#FlexiForm").serialize(),
                        function(data)
                               {
                                    var jsonVar=eval('('+data+')')
                                    var name=jsonVar.name
                                    var idlist=jsonVar.idlist
                                      var htmlVar=""
                                    for(var i=0;i<name.length;i++)
                                        {
                                             htmlVar+="<tr><td><input type='text' name='flexicolumnvalue"+i+"' value='"+name[i]+"' id='flexicolumnvalue"+i+"'/></td>\n\
                                                           <td><input type='text' name='idlist"+i+"' value='"+idlist[i]+"' id='idlist"+i+"'/></td>\n\
                                                           <td><input type='checkbox' name='flexiname' id='flexiname"+i+"' value='flexiname"+i+"'></td></tr>"
                                        }
                                       $("#flexisegid").html(htmlVar);
                                });
                                     $("#flexidivid").dialog('open');
      }
      function saveValues()
      {
      var memberid=tempId
      var length=memberid.substring(7, memberid.length);
      var grpname=$("#membergrpid"+length).val()
      var groupId=groupid
      var tablename=tablename2
      var connId=$("#connnames").val()
      $.post('targetmeasuresaction.do?targetMeasures=saveFlexiSegmentationTableColumnNames&grpname='+grpname+'&groupId='+groupId+'&tablename='+tablename+'&connId='+connId, $("#flexiForm1").serialize(),
                        function(data)
                               {
                               });
      }
      function createDate()
      {
            $.post("<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=getConnectionNames",function(data){
                    var jsonVar=eval('('+data+')')
                    var connids=jsonVar.connids
                    var connames=jsonVar.connnames
                    var htmlVar=""
                    var jsonvaleuse=""
                    htmlVar+="<option  value=''>--SELECT--</option>"
                    for(var i=0;i<connids.length;i++)
                    {
                        jsonvaleuse=connames[i]

                        htmlVar+="<option  value='"+connids[i]+"'>"+jsonvaleuse+"</option>"
                    }
                    $("#connnamesid").html(htmlVar)

                });
           var x= $("#createdatedivid").html();
                 $("#data").html(x);
        }
      function editsegvalues(id)
      {
         groupname=$("#groupname"+id).val()
         tabname=$("#flexitableid"+id).val()
         tableid=$("#tableid"+id).val()

          $.post('targetmeasuresaction.do?targetMeasures=getFlexiMemberNames&groupname='+groupname, $("#flexiForm1").serialize(),
                        function(data)
                               {
                                    var jsonVar=eval('('+data+')')

                                     groupmembers=jsonVar.grpmember
                                    var groupvalues=jsonVar.grpvalues
                                    var flexiidnum=jsonVar.fliexidnum
                                     $("#segmntTableId").find("tr:gt(0)").remove();
                                    for(var i=0;i<groupmembers.length;i++)
                                    {
                    $("#segmntTableId").append("<tr><td><input type='text' name='membergrp'  style='width:120px' id='membergrpid"+(i+1)+"' value='"+groupmembers[i]+"'/></td>\n\
                    <td><select id='selectid"+(i+1)+"' class='grpMembrsid' name='selectedroles'><option value='IN'>IN</option><option value='LIKE'>LIKE</option></select></td>\n\
                    <td><input type='text' name='membervalues'  style='width:120px' id='membervalueid"+(i+1)+"' value='"+groupvalues[i]+"' onclick='segmntMemCols(this.id)'/><input type='hidden' name='flexiidnum'  style='width:120px' id='flexiidnum"+(i+1)+"' value='"+flexiidnum[i]+"'/><input type='hidden'  id='hiddenmembervalueid"+(i+1)+"' name='hiddenmembervalueid'  value='"+(i+1)+"' ></td></tr>")
                                   }
                                });

                    $("#groupdiv").dialog('open');
      }
      function getRelatedTables()
      {
            var conId=$("#connnamesid").val();
                $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getDisplayTableNames&conId='+conId,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        var tabNames=jsonVar.tablenames
                        var htmlVar=""
                        var jsonvaleuse=""
                        htmlVar+="<option  value=''>--SELECT--</option>"
                        for(var i=0;i<tabNames.length;i++)
                        {
                            jsonvaleuse=tabNames[i]
                            htmlVar+="<option  value='"+tabNames[i]+"'>"+jsonvaleuse+"</option>"
                        }
                        $("#tableid1").html(htmlVar);
                    }
                });
      }
      function getColumnNames()
      {
          var connectionID=$("#connnamesid").val()
                var tabname=$("#tableid1").val();
                $.ajax({
                    url: 'targetmeasuresaction.do?targetMeasures=getTableColumns&tabname='+tabname+'&connectionID='+connectionID,
                    success: function(data){
                        var jsonVar=eval('('+data+')')
                        var colNames=jsonVar.columnnames
                        var htmlVar=""
                        var jsonvaleuse=""
                        htmlVar+="<option  value=''>--SELECT--</option>"
                        for(var i=0;i<colNames.length;i++)
                        {
                            jsonvaleuse=colNames[i]
                            htmlVar+="<option  value='"+colNames[i]+"'>"+jsonvaleuse+"</option>"
                        }
                        $("#columnsid1").html(htmlVar);
                        $("#basecolumnid1").html(htmlVar);
                    }
                });
      }
      function segColnext()
      {
          $("#nextDivId").dialog('open');
      }
      function ExtraRow()
      {
          var Count=$('#FlexisegmntTableId1 tr').length

                         $("#FlexisegmntTableId1").append("<tr><td><input type='text' name='Seasonvalues'  style='width:120px' id='Seasonvalue"+Count+"'/></td>\n\
           <td><input type='text' name='startdate' maxlength='255' style='width: 120px;' id='startdate"+Count+"' onclick='getDatePicker(this.id)' /></td>\n\
           <td><input type='text' name='enddate' maxlength='255' style='width: 120px;' id='enddate"+Count+"' onclick='getDatePicker(this.id)' /></td></tr>")
      }
      function deleteRow(tableID)
      {
           var table =tableID ;
                    var rowCount = table.rows.length;
                    if(rowCount > 2) {
                             table.deleteRow(rowCount - 1);
                    }
      }
      function closeDiv()
      {
          $("#nextDivId").dialog('close');
      }
      function saveDate()
      {
                         var connId=$("#connnamesid").val()
                         var tablename=$("#tableid1").val()
                         var dependentcolumn=$("#columnsid1").val()
                         var basecolumn=$("#basecolumnid1").val()
                          $.post('targetmeasuresaction.do?targetMeasures=saveFlexiDate&connId='+connId+'&tablename='+tablename+'&dependentcolumn='+dependentcolumn+'&basecolumn='+basecolumn, $("#DateForm").serialize(),
                        function(data)
                               {

                               });

      }
      function getDatePicker(id)
      {
         $("#"+id).datepicker({
                             changeMonth: true,
                             changeYear: true,
                             dateFormat:'yy-mm-dd'
                         });
      }
      function selectAll()
      {

           var flexinameObj=document.getElementsByName("flexiname");
                for(var i=0;i<flexinameObj.length;i++){
                    if(flexinameObj[i].checked){

                        flexinameObj[i].checked=false;
                    }
                    else{
                        flexinameObj[i].checked=true;
                    }
                }
      }
      $(document).ready(function()
                    {
                      $.get('<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=getConnectionNames',function(data){
                    var jsonVar=eval('('+data+')')
                    var connids=jsonVar.connids
                    var connames=jsonVar.connnames
                    var htmlVar=""
                    var jsonvaleuse=""
                    htmlVar+="<option  value=''>--SELECT--</option>"
                    for(var i=0;i<connids.length;i++)
                    {
                        jsonvaleuse=connames[i]
                        htmlVar+="<option  value='"+connids[i]+"'>"+jsonvaleuse+"</option>"
                    }
                    $("#connnames").html(htmlVar)
                });

           <%--$.get('<%= request.getContextPath()%>/targetmeasuresaction.do?targetMeasures=getFlexiSegmentationTableNames&conId='+conId,function(data)
           {


                    var jsonVar=eval('('+data+')')
                    var flexitablename=jsonVar.flexitablename
                    var groupnames=jsonVar.flexigroupnames
                    var htmlVar=""
                    var jsonvaleuse=""
                    for(var i=0;i<flexitablename.length;i++)
                        {
                             htmlVar+="<tr><td><input type='text' name='flexitablenames' value='"+flexitablename[i]+"' id='flexitableid"+i+"'</td>\n\
                                           <td><input type='text' name='flexitablenames'  id='flexitableid"+i+"'</td> \n\
                                           <td><img border='0' align='middle'  src='/pi/icons pinvoke/plus-circle.png' onclick='getGroupValues()'></tr>"
                        }
                        $("#flexisegmentationid").html(htmlVar);

           });--%>



});
         </script>

    </head>

    <body><div><div id ="" title="" style=" min-height: 400px;max-height:auto; border-width: 4px; border-style: groove; border-color: skyblue; margin-top: auto; margin-left: 100px; margin-right: 100px; margin-bottom: 100px; margin: 80px 10px 10px 10px; ">
             <table border="1" width="100%" height="100%"><tr><td width="10%" valign="top"> <table>
                <tr>
                    <td><input type="button" class="navtitle-hover" name="creategroup" value="Create Group"  onclick="creategroups()" style="height: 20px; width: 100px"/></td>
                </tr>
                <tr>
                    <td><input type="button" class="navtitle-hover" name="editgroup" value="Edit Group" onclick="editgroups()" style="height: 20px; width: 100px"/></td>
                </tr>
                <tr>
                    <td><input type="button" class="navtitle-hover" name="DeleteGroup" value="Delete" onclick="deleteoption()" style="height: 20px; width: 100px"/></td>
                </tr>
                <tr>
                    <td><input type="button" class="navtitle-hover" name="createdate" onclick="createDate()" value="Create Date"  style="height: 20px; width: 100px"/></td>
                </tr>

                         </table></td><td width="90%" id="data" ALIGN="center" valign="top" height="auto">

                             </td></tr></table></div>



            <div id ="deleteOptions" title="Delete Options" style=" height: 400px; border-width: 4px; border-style: groove; border-color: skyblue; margin-top: auto; margin-left: 100px; margin-right: 100px; margin-bottom: 100px; margin: 80px 10px 10px 10px; ">
             <table border="1" width="100%" height="100%"><tr><td width="10%" valign="top" align="center"> <table>
               <tr>
                    <td><input type="button" class="navtitle-hover" name="DeleteGroup" value="Delete specific Group" onclick="deletegroups()" style="height: 20px; width: 200px"/></td>
                </tr>
                <tr>
                    <td><input type="button" class="navtitle-hover" name="DeleteAll" value="Delete all Group of Table" onclick="deletetables()" style="height: 20px; width: 200px"/></td>
                </tr>
                <tr>
                    <td><input type="button" class="navtitle-hover" name="DeleteType" value="Delete all Group of Type" onclick="deletetypes()" style="height: 20px; width: 200px"/></td>
                </tr>
                 <tr>
                    <td><input type="button" class="navtitle-hover" name="DeleteRecord" value="Delete all Record of Type" onclick="deleterecords()" style="height: 20px; width: 200px"/></td>
                </tr>

                         </table></td></tr></table></div>
         <div id="crategroupsid">

             <table align="center">
                <tr>
                        <td class="navtitle-hover"><b> Connection Name </b>&nbsp; </td>
                        <td><select id="connnames" name="connnames" style="width:auto" onchange="getTables()">
                                <option>---SELECT---</option>
                            </select></td>
                    </tr>
            </table>
                <br><br><br>
                <table  border="1px solid" cellpadding="0" cellspacing="1" align="center">
                <thead>
                    <tr><th class="navtitle-hover" width="100px">Table Id</th>
                        <th class="navtitle-hover" width="100px">Table Name</th>
                        <th class="navtitle-hover" width="100px">Table Map Id</th>
                            <th class="navtitle-hover" width="100px">Groups</th>
                        </tr>
                  </thead>
                  <tbody id="flexisegmentationid">
                  </tbody>
                </table>
     </div>
         <div id="editgroupsid">
                            <div id="flexipager" class="pager" align="center" >
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize">
                                <option selected value="5">5</option>
                                <option value="10">10</option>
                               <option id="allModifyMeasures" value="">All</option>
                            </select>

        <table id="flexitableId" class="tablesorter"  border="1px solid" cellpadding="0" cellspacing="1" align="bottom">
                <thead>
                    <tr>
                         <th class="navtitle-hover">TableName</th>
                         <th class="navtitle-hover">Group Name</th>
                    </tr>
                </thead>
                <tbody id="flexiTabBody">
                </tbody>
        </table></div></div>
            <div id="flexigroupid" title="Flexigroups">
                  <table id="flexigroupid">
                        <tr>
                            <td class="navtitle-hover"><b> Group Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><input type="text" name="groupname" id="groupid"></td></tr>
                    </table>
                    <center><input type="submit" value="Next" onclick="getgroupnames()" class="navtitle-hover"/></center>
            </div>
        </div>
        <div id="groupdiv" style="display: none">
            <form id="FlexiForm">
                <table id="segmntTableId" width="">
                <tr>
                    <th  class="navtitle-hover"> Group Members</th>
                    <th>         </th>
                    <th class="navtitle-hover" width="">  Member Values</th>
                </tr>
                <tr>
                    <td>
                        <input type="text"  id="membergrpid1" name="membergrp" class="myTextbox" style="width:120px" maxlength="255" value="" />
                    </td>
                    <td><select id="selectid1" name="selectedroles" onchange="changeImg(this.id)">
                            <option selected value="select">select</option>
                            <option value="IN">IN</option>
                            <option value="LIKE">LIKE</option>
                        </select></td>
                        <td><input type="text"  id="membervalueid1" name="membervalues" class="myTextbox" style="width:120px" maxlength="255" value="" onclick="segmntMemCols(this.id)">
                            <input type="hidden"  id="hiddenmembervalueid1" name="hiddenmembervalueid"  value="1" >
                        </td>&nbsp;&nbsp;&nbsp;&nbsp;
                        <td id="imageId">
                         <img id="imageid1"border="0" align="middle" onclick="getLikeValues(this.id)" src="/pi/icons pinvoke/plus-circle.png">
                        </td>
                </tr>
            </table>
            <br/><br/>
            <input type="button" id="buttonSaveId" value="Save" name="Save" onclick="saveGrpMemVals()" class="navtitle-hover">
              <input type="button" value="Add Row" id="addButtonId" onclick="addRow()"  name="" class="navtitle-hover" />
              <input type="button" value="Delete Row" id="deleteButtonId" onclick="deleteParentRow(segmntTableId)"  name="" class="navtitle-hover" />
              <input type="button" id="buttonId" value="Close" onclick="closebutt()" class="navtitle-hover">
            </form>
            </div>
            <div id="membrvaldivid" style="display: none">
            <table width="100%">
                <tr>
                    <td id="htmsdrgndrpid"></td>
                </tr>
                <tr>
                    <td align="center"> <input type="button" value="Save" name="Save" onclick="saveGrpMems(this.id)" class="navtitle-hover"/>
                        <input type="button" value="Cancel" name="Cancel" onclick="cancelGrpMems()" class="navtitle-hover"/>
                    </td>
                </tr>
            </table>
           </div>
          <div id="flexidivid">
              <form id="flexiForm1">
                  <table  border="1px solid" cellpadding="0" cellspacing="1" align="center">
                  <thead>
                      <tr>
                          <th class="navtitle-hover">Name</th>
                          <th class="navtitle-hover">List</th>
                          <th class="navtitle-hover"><input type="checkbox" onclick="selectAll()" name="flexiname1"></th>
                      </tr>
                  </thead>
                  <tbody id="flexisegid">
                 </tbody>
              </table>
              <center><input type="submit" value="Save" onclick="saveValues()" class="navtitle-hover" id="savesegid1"/></center>
              </form>
            </div>
           <div id="deletegroupsid">
                <form id="GroupForm">
               <table  border="1px solid" cellpadding="0" cellspacing="1" align="center">
                   <thead>
                      <tr>
                          <th class="navtitle-hover">GroupName</th>
                          <th class="navtitle-hover">Delete</th>
                      </tr>
                  </thead>
                  <tbody id="deleteflexigroupid">

                  </tbody>
               </table>
                    <center><input type="submit" value="Delete" onclick="deletegrps()" class="navtitle-hover" /></center>
                </form>
          </div>

          <div id="deletetablesid">
                <form id="TableForm">
               <table  border="1px solid" cellpadding="0" cellspacing="1" align="center">
                   <thead>
                      <tr>
                          <th class="navtitle-hover">TableName</th>
                          <th class="navtitle-hover">Delete</th>
                      </tr>
                  </thead>
                  <tbody id="deleteflexitableid">

                  </tbody>
               </table>
                    <center><input type="submit" value="Delete" onclick="deletetabs()" class="navtitle-hover" /></center>
                </form>
          </div>

           <div id="deletetypesid">
                <form id="TypeForm">
                   <table height:400px width:400px align="center" id="getgroupsid">
                        <tr>
                            <td class="navtitle-hover"><b> Group Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="grpnames" name="grpnames" style="width:auto" onchange="getRelatedTypes()">
                                    
                           </select></td>
                        </tr>
                       <%--<tr>
                            <td class="navtitle-hover"><b>Type Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="typeid1" name="typeid" style="width:auto" onchange="getColumnNames()">
                                </select>
                            </td>
                        </tr>--%>
                    </table>
                   
               <table  border="1px solid" cellpadding="0" cellspacing="1" align="center" id="gettypesid" >
                   <thead>
                      <tr>
                          <th class="navtitle-hover">TypeName</th>
                          <th class="navtitle-hover">Delete</th>
                      </tr>
                  </thead>
                  <tbody id="deleteflexitypeid">

                  </tbody>
               </table>
                    <center><input type="submit" value="Delete" onclick="deletetyps()" class="navtitle-hover" /></center>
                </form>
          </div>
           <div id="deleterecordsid" style="overflow:auto">
                <form id="RecordForm">
                   <table height:400px width:400px align="center" id="getgroupsid1">
                        <tr>
                            <td class="navtitle-hover"><b> Table Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="tabnames1" name="tabnames1" style="width:auto" onchange="getRelatedGroups1()">

                           </select></td>
                        </tr>
                       <tr>
                            <td class="navtitle-hover"><b> Group Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="grpnames1" name="grpnames1" style="width:auto" onchange="getRelatedTypes1()">

                           </select></td>
                        </tr>
                       <tr>
                            <td class="navtitle-hover"><b> Type Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
                            <td><select id="typnames1" name="typnames1" style="width:auto" onchange="getRelatedRecords1()">

                           </select></td>
                        </tr>
                    </table>

               <table  border="1px solid" cellpadding="0" cellspacing="1" align="center" id="gettypesid" >
                   
                  <tbody id="deleteflexirecordid">

                  </tbody>
               </table>
                    
                </form>
          </div>
   <div id="createdatedivid">
         <table height:400px width:400px align="center">
        <tr>
            <td class="navtitle-hover"><b> Connection Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="connnamesid" name="connnames" style="width:auto" onchange="getRelatedTables()">
                    <option>---SELECT---</option>
           </select></td>
        </tr>
       <tr>
            <td class="navtitle-hover"><b>Table Name </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="tableid1" name="tableid" style="width:auto" onchange="getColumnNames()">
                </select>
            </td>
        </tr>
       <tr>
            <td class="navtitle-hover"><b>Depend Column </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="columnsid1"  style="width:auto"><option>---SELECT---</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="navtitle-hover"><b>Base Column </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
            <td><select id="basecolumnid1" style="width:auto"><option>---SELECT---</option>
                </select>
            </td>
        </tr><br/>
        <tr>
        <center> <td>
                <input type="button" onclick="segColnext()"  value="Next" class="navtitle-hover"/>
            </td></center>
        </tr>
    </table>
   </div>
            <div id="nextDivId">
                <form name="columnForm" id="DateForm" method="post" action="">
        <table id="FlexisegmntTableId1" width="">
            <thead>
                <tr>
                    <th class="navtitle-hover">Season</th>
                    <th class="navtitle-hover">Start Date</th>
                    <th class="navtitle-hover">End Date</th>
                </tr>
            </thead>
            <tbody id="DateTabBody">
                <tr>
                    <td><input type="text"  id="Seasonvalue1" name="Seasonvalues" class="myTextbox" style="width:120px" maxlength="255" value="" /></td>
                    <td><input type="text"  id="startdate1" name="startdate" class="myTextbox" style="width:120px" maxlength="255" value=""/></td>
                    <td><input type="text"  id="enddate1" name="enddate" class="myTextbox" style="width:120px" maxlength="255" value=""/></td>
                </tr>
            </tbody>
        </table>
        <br/><br/>
        <input type="submit" id="buttonSaveId" value="Save" name="Save" onclick="saveDate()" class="navtitle-hover">
        <input type="button" value="Add Row" id="addButtonId" onclick="ExtraRow()"  name="" class="navtitle-hover" />
        <input type="button" value="Delete Row" id="deleteButtonId" onclick="deleteRow(FlexisegmntTableId1)"  name="" class="navtitle-hover" />

        <input type="button" id="buttonId" value="Close" onclick="closeDiv()" class="navtitle-hover">
    </form>
            </div>
    </body>

</html>