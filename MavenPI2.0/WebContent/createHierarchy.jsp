

<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.*,java.sql.*,prg.db.PbDb,prg.db.PbReturnObject"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String flag= request.getParameter("flag");
            String check= request.getParameter("check");
            String contextPath=request.getContextPath();

%>
<html>
    <head>

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/treeview/jquery.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

<!--        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>-->

        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>


        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="confirm.css" rel="stylesheet" type="text/css" />
        <link href="jquery.contextMenu.css" rel="stylesheet" type="text/css" />

        <script src="jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="jquery.contextMenu.js" type="text/javascript"></script>
        <link href="myStyles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style>
            *{
                font:           12px verdana;
            }
            .myHead
            {
                font-family: Verdana;
                font-size: 8pt;
                font-weight: bold;
                color: black;
                padding-left:12px;
                width:50%;
                background-color:#b4d9ee;
                border:0px;
            }
        </style>
               <%

            Connection con = null;
           // Statement st, st1 = null;
           // Statement st2 = null;
           // ResultSet rs, rs1,rs2 = null;
          //  ResultSet rs3 = null;
            PbReturnObject  pbRet1=new PbReturnObject();
            PbDb pbDb=new PbDb();
            try{

            //con =  utils.db.ProgenConnection.getInstance().getConnection();

            String dimId = request.getParameter("dimId");

            String sql = "SELECT MEMBER_ID, MEMBER_NAME FROM PRG_QRY_DIM_MEMBER WHERE DIM_ID=" + dimId +" ORDER BY MEMBER_ID";
            //.println("sql"+sql);
           pbRet1 = pbDb.execSelectSQL(sql);

           prg.db.PbReturnObject pbro=pbRet1;
           //out.println("pbro=="+pbro.getRowCount());
           int count=pbro.getRowCount();
           //for find out same hierachy exist or not
           String sql1 = "SELECT REL_ID, REL_NAME FROM PRG_QRY_DIM_REL where dim_id=" + dimId ;


           PbReturnObject pbro1=new PbReturnObject();
             pbro1= pbDb.execSelectSQL(sql1);
           String lev="";
           String namecheck="";
               for(int i=0;i<pbro1.getRowCount();i++){

              lev+="~";
              namecheck+="~"+pbro1.getFieldValueString(i,1);
           String sql2 = "SELECT REL_ID, MEM_ID, REL_LEVEL FROM PRG_QRY_DIM_REL_DETAILS where REL_ID="+pbro1.getFieldValueInt(i,0)+" order by  MEM_ID";
             PbReturnObject pbro2=new PbReturnObject();
            pbro2= pbDb.execSelectSQL(sql2);


           for(int j=0;j<pbro2.getRowCount();j++){
               lev+=pbro2.getFieldValueInt(j,2);

               }

                   }
           if(pbro1.getRowCount()>0){

               lev=lev.substring(1);
               namecheck=namecheck.substring(1);
               }


          PbReturnObject pbReturnObject=new PbReturnObject();
           pbReturnObject = pbDb.execSelectSQL("select REL_ID from PRG_QRY_DIM_REL where DIM_ID="+dimId);
%>
        

    </head>
    <body>



           <%
           if(pbro.getRowCount()>0){
        %>
        <center>
            <form name="myForm" method="post">
                <br>
                <table style="width:40%;border:0px solid black">
                    <%

                    if(check!=null){
                    %>
                     <tr style="display:none">
                    <%
                    } else
                        {
                    %>
                    <tr>
                        <%}%>
                        <td class="myHead" style="width:50%" align="left">
                            <b> Name</b>
                        </td>
                        <td style="width:50%">
                            <input type="text" name="hieName" class="myTextbox5" style="width:150px" maxlength="255" onkeyup="tabmsg()" >
                        </td>
                    </tr>
                    <%

                    if(flag!=null||check!=null){
                    %>
                    <tr style="display:none">
                    <%
                    } else
                        {
                    %>
                    <tr>
                        <%}%>
                        <td class="myHead" style="width:50%" align="left">
                            <b>   Members </b>
                        </td>
                        <td style="width:50%">
                            &nbsp;&nbsp;<b style="font-weight:bold"> Levels</b>
                        </td>
                    </tr>
                    <%
                    for(int i=0;i<count;i++){
                    if(flag!=null||check!=null){
                    %>
                    <tr style="display:none">
                    <%
                    } else
                        {
                    %>
                    <tr>
                        <%}%>
                        <td  class="myHead" style="width:50%">
                            <input type="hidden" name="memv<%=i+1%>" readonly value="<%=pbro.getFieldValueInt(i,0)%>">
                            <input type="text" style="border:0px;background-color:#b4d9ee" name="mem<%=i+1%>" readonly value="<%=pbro.getFieldValueString(i,1)%>">
                        </td>
                        <td style="width:50%">
                            <select id="val<%=i+1%>" name="val<%=i+1%>" class="myTextbox5" style="width:150px;backgroubackground-color:#f0f0f0;">
                                <% for(int j=i;j<count;j++){
                                %>
                                <option value="<%=j+1%>">Level<%=j+1%></option>
                                <%}%>
                                <% for(int j=1;j<=i;j++){
                                %>
                                <option value="<%=j%>">Level<%=j%></option>
                                <%}%>

                            </select>

                        </td>
                    </tr>

                    <%}
                    if(check!=null){
                    %>
                     <tr style="display:none">
                    <%
                    } else
                        {
                    %>
                    <tr>
                    <%}%>
                        <td class="myHead" style="width:50%" align="left">
                            <b>Description</b>
                        </td>
                        <td style="width:50%">
                            <input type="text" class="myTextbox5" style="width:150px" name="hieDesc" maxlength="255">
                        </td>
                    </tr>
                    <%

                    if(flag!=null||check!=null){
                    %>
                     <tr style="display:none">
                    <%
                    } else
                        {
                    %>
                    <tr>
                        <%}%>
                        <td class="myHead" style="width:50%" align="left">
                            <b>Make This Default Hierarchy</b>
                        </td>
                        <td style="width:50%">
                            <%
                if(pbReturnObject.getRowCount()>0){%>
                            <input type="checkbox" class="myTextbox5" style="width:150px" name="chk" maxlength="255">
                            <%}else{%>
                            <input type="checkbox" class="myTextbox5" style="width:150px" name="chk" value="Y"checked readonly>
                            <%}%>
                        </td>
                    </tr>
                </table>
                <table >
                    <%
                    if(check!=null){
                    %>
                    <tr>
                        <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveHierarchy1('<%=count%>')"></td>
                        <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelHierarchy()"></td>
                    </tr>
                    <%
                    } else
                        {
                    %>
                    <tr>

                        <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveHierarchy('<%=count%>')"></td>
                        <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelHierarchy()"></td>
                    </tr>
                    <%}%>
                </table>
                <input type="hidden" name="rowcount"  value="<%=count%>">
                <input type="hidden" name="dimId"  value="<%=dimId%>">
                <input type="hidden" name="lev" id="lev" value="<%=lev%>">
                <input type="hidden" name="namecheck" id="namecheck" value="<%=namecheck%>">
            </form>
        </center>
        <%}else{%>
        <center>
            <h4> Add Table Detalis Before Creating Hierachy</h4>
            <table >
                <tr>
                    <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelHierarchy()"></td>
                </tr>
            </table>
            <%}
       
                   }catch(Exception e){
                   e.printStackTrace();
                   }
            %>
        </center>
        <script>
            function saveHierarchy(count)
            { //alert(count);
                var vals;
                var lev1='';
                var count1=0;
                var llen=0;
                var lname=0;
                //for(var i=0;i<count;i++){
                // alert(document.getElementById('val'+(i+1)).value)
                // vals[i]=document.getElementById('val'+(i+1)).value;
                // alert(vals[i]);
               
                //  }
                var lev=document.getElementById('lev').value;
                var name=document.getElementById('namecheck').value;
                var namearr=name.split('~');
                var levarr=lev.split('~');
       
                // alert('lev===>'+lev+"name-->"+name);
                for(var j=0;j<count;j++){
                    lev1+=document.getElementById('val'+(j+1)).value;
                    //  alert('lev1===>'+lev1);
                    for(var k=j+1;k<count;k++){
                
                        if(document.getElementById('val'+(j+1)).value==document.getElementById('val'+(k+1)).value){
                            count1=1;
                        }

                    }
                }
          
                if(lev!=''){
             
                    for(var l=0;l<levarr.length;l++){

                        if(lev1==levarr[l]){
                            llen=1;
                            break;
                        }
                    }
                    /*
                  if(llen==1){
                      alert('Hierachy exist with this relationship Please Create another hierarchy');
                  }
                     */
              
                }

                if(name!=''){

                    for(var l=0;l<namearr.length;l++){

                        if(document.myForm.hieName.value==namearr[l]){
                            lname=1;
                            break;
                        }
                    }
                    /*  if(lname==1){
                      alert('Hierachy name exist Please give another name');
                  }

                     */
                }

                if(count1==1){
                    alert('No two members have the same level ');
                }
                else if(document.myForm.hieName.value==""){
                    alert('Please enter Hierarchy name ');
                }
                else if(lname==1){
                    /*
                   for(var l=0;l<namearr.length;l++){

                      if(document.myForm.hieName.value==namearr[l]){
                          lname=1;
                          break;
                      }
                  }
                  if(lname==1){
                      alert('Hierachy name exist Please give another name');
                  }
             
                     */
                    alert('Hierachy name exist Please give another name');

                }
                else if(llen==1){
                    /*
                  for(var l=0;l<levarr.length;l++){

                      if(lev1==levarr[l]){
                          llen=1;
                          break;
                      }
                  }
                  if(llen==1){
                      alert('Hierachy exist with this relationship Please Create another hierarchy');
                  }
                     */
                    alert('Hierachy exist with this relationship Please Create another hierarchy');
                }
                else{
                    document.myForm.action="saveHierarchy.jsp";
                    document.myForm.submit();
                }

        

            }
            function saveHierarchy1(count)
            {
                var vals;
                var lev1='';
                var count1=0;
                var llen=0;
                var lname=0;

                var lev=document.getElementById('lev').value;
                var name=document.getElementById('namecheck').value;
                var namearr=name.split('~');
                var levarr=lev.split('~');
                 for(var j=0;j<count;j++){
                    lev1+=document.getElementById('val'+(j+1)).value;

                    for(var k=j+1;k<count;k++){

                        if(document.getElementById('val'+(j+1)).value==document.getElementById('val'+(k+1)).value){
                            count1=1;
            }
           
            }
                }

                if(lev!=''){

                    for(var l=0;l<levarr.length;l++){
       
                        if(lev1==levarr[l]){
                            llen=1;
                            break;
                        }
                    }
      
                }
       
                if(name!=''){
      
                    for(var l=0;l<namearr.length;l++){
      
                        if(document.myForm.hieName.value==namearr[l]){
                            document.myForm.hieName.value=namearr[l];
                            break;
                        }
                    }
      
                }

                    document.myForm.action="saveHierarchy.jsp?check='true'";
                    document.myForm.submit();
     
     

   
                   }
            function cancelHierarchy()
            {
                parent.refreshparenthie();
            }
            function tabmsg(){

                // alert(document.myForm.hieName.value)
                var name=document.myForm.hieName.value;
                // alert(name);
                document.myForm.hieDesc.value=name;
               }
        </script>
    </body>
</html>