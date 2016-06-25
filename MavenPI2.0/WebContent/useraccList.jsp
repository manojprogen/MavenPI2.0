<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,java.util.ArrayList,utils.db.ProgenConnection"%>

<%
            PbDb pbdb = new PbDb();
            PbReturnObject list = null;
            String sortValue = "org_st_date";
            String sortOptionvalue = "";
            PbReturnObject retObject = null;
            String dbType = "";
            if (session.getAttribute("MetadataDbType") != null) {
                dbType = (String) session.getAttribute("MetadataDbType");
            }
            if (session.getAttribute("userlistaccprgr") != null) {
                retObject = (PbReturnObject) session.getAttribute("userlistaccprgr");

            }
            if (session.getAttribute("selectedoption") != null) {
                sortValue = (String) session.getAttribute("selectedVal");

            }
            if (session.getAttribute("selectedVal") != null) {
                sortOptionvalue = (String) session.getAttribute("selectedoption");

            }
            if (session.getAttribute("userlistaccprgr") == null) {
                //Added by Srikanth.
     /*  Please change the Query in ResourceBundle ,if U want to change the below Query  */
                String query = "";
                if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    query = "select pom.org_id,pom.ORGANISATION_NAME,pom.ORGANISATION_DESC,pom.ORG_ST_DATE, COALESCE(pom.org_end_date,'') ,case when getdate()- COALESCE(pom.org_end_date,getdate()) <= 0 then 'Active' else 'Expired' end as  status from prg_org_master pom   ";
                } else {
                    query = "select * from (select pom.org_id,pom.ORGANISATION_NAME,pom.ORGANISATION_DESC,pom.ORG_ST_DATE, nvl(pom.org_end_date,'') ,case when sysdate- nvl(pom.org_end_date,sysdate) <= 0 then 'Active' else 'Expired' end as  status from prg_org_master pom  order by org_st_date desc) order by status ";
                }
                list = pbdb.execSelectSQL(query);
                list.writeString();
                sortValue = "org_st_date";
                sortOptionvalue = "ASC";

            } else {
                list = retObject;
                list.writeString();

            }

            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
        
    }
            String contextPath1=request.getContextPath();

            // String query = "select pom.*,puf.folder_name from prg_org_master pom,prg_user_folder puf where puf.folder_id(+)= pom.buss_role";
            // PbReturnObject list = pbdb.execSelectSQL(query);
            ////////////////////////////////////////////////.println.println("list is " + list);
            ////////////////////////////////////////////////.println.println(" list.getRowCount() is " + list.getRowCount());
%>

<html>
    <head>
        <title></title>
        <%--        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
                <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>--%>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/scripts.js"></script>
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath1%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />

      
        <style type="text/css">
            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }  
        </style>
    </head>
    <body id="mainBody" onload="loadpage()">
        <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>
        <form name="myFormA"  method="post" action="">
            <script type="text/javascript">
                $(document).ready(function(){
                    $("#tablesorterUserAcc")
                    .tablesorter({headers : {0:{sorter:false}}})
                    .tablesorterPager({container: $('#pagerUserAcc')})
                });
            </script>
            <table id="sortBytable" width="100%">
                <tr>
                    <td align="left" width="50%">
                        <div id="pagerUserAcc" class="pager" align="left" >
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option value="5">5</option>
                                <option selected value="10">10</option>
                                <option value="<%=list.getRowCount()%>">All</option>
                            </select>
                        </div>
                    </td>
                    <td width="50%">
                        <table width="100%">
                            <tr>
                                <td class="wordStyle" align="right" width="20%" style="font-weight:bold;">Sort by :
                        <select id="sortBySel1" onchange="orderBYsel()">
                            <%if (!sortValue.equalsIgnoreCase("")) {%>
                            <script language="text/javascript">
                                var sVal=document.getElementById('<%=sortValue%>').text;
                                var optioObje=document.getElementById("dispItem");
                                optioObje.value='<%=sortValue%>';
                                optioObje.text=sVal;
                                document.getElementById('<%=sortValue%>').style.display='none';
                            </script>
                            <option id="dispItem" value=""> </option>
                            <option id="organisation_name" value="organisation_name" >Company Name</option>
                            <option id="organisation_desc" value="organisation_desc">Company Desc</option>
                            <%--<option id="buss_role" value="buss_role">Business Role</option>--%>
                            <option id="org_st_date" value="org_st_date">Created Date</option>
                            <option id="enddate" value="enddate">Expired Date</option>
                            <%} else {%>
                            <option id="organisation_name" value="organisation_name" >Company Name</option>
                            <option id="organisation_desc" value="organisation_desc">Company Desc</option>
                            <%--<option id="buss_role" value="buss_role">Business Role</option>--%>
                            <option id="org_st_date" value="org_st_date">Created Date</option>
                            <option id="enddate" value="enddate">Expired Date</option>



                            <%}%></select>
                    </td>
                    <td class="wordStyle" align="right" width="10%" style="font-weight:bold;" valign="top">
                        Sort Option :
                    </td>
                    <td align="right" width="1%" valign="top">
                        <%
                                    if (sortOptionvalue.equalsIgnoreCase("ASC")) {
                        %>
                        <input type="radio" id="asc1" name="sortOption" checked value="ASC" onchange="orderBYsel()">
                        <%} else {%>
                        <input type="radio" id="asc1" name="sortOption"  value="ASC" onchange="orderBYsel()">
                        <%}%>
                    </td>
                    <td class="wordStyle" align="right" width="4%" valign="top">Ascending</td>

                    <td align="right" width="1%" valign="top">
                        <%
                                    if (sortOptionvalue.equalsIgnoreCase("DESC")) {
                        %>
                        <input type="radio" id="desc1" name="sortOption" checked value="DESC" onchange="orderBYsel()">
                        <%} else {%>
                        <input type="radio" id="desc1" name="sortOption" value="DESC" onchange="orderBYsel()">
                        <%}%>
                    </td><td class="wordStyle" align="right" width="4%" valign="top">Descending</td>
                            </tr>
                        </table>
                    </td>
                    
                </tr>
            </table>
           
            <table align="center" id="tablesorterUserAcc" class="tablesorter"  style="width:98%;" cellpadding="0" cellspacing="1">
                <thead>
                    <tr><th>&nbsp;</th>
                        <th>Company Name</th>
                        <th>Company Desc</th>
                        <%--<th>Business Role</th>--%>
                        <th>Created Date</th>
                        <th>Expiry Date</th>
                        <th>Company Status</th>
                        <th>Assigned Roles/Reports</th>
                    </tr>
                </thead>
                <tbody>
                    <%for (int i = 0; i < list.getRowCount(); i++) {%>
                    <tr>
                        <td style="width:40px">
                            <input type="checkbox" id="chkAccount" name="chkAccount" value="<%=list.getFieldValueInt(i, 0)%>">
                        </td>
                        <td valign="top" >
                            <%=list.getFieldValueString(i, 1)%>
                        </td>

                        <td valign="top" >
                            <%=list.getFieldValueString(i, 2)%>
                        </td>
                        <%--<td valign="top" >
                                <%=list.getFieldValueString(i, 6)%>
                            </td>--%>
                        <td valign="top" >
                            <%=list.getFieldValueDateString(i, 3)%>
                        </td>
                        <td valign="top" >
                            <%if (list.getFieldValueString(i, 4).equalsIgnoreCase(null) || "".equalsIgnoreCase(list.getFieldValueString(i, 4))) {%>
                            <%=""%>
                            <%} else {%>
                            <%=list.getFieldValueDateString(i, 4)%>
                            <%}%>
                        </td>
                        <%
                            //String status="";
                            //if(list.getFieldValueInt(i,6)>=0){
                            // status="Expired";
                            //}else{
                            //    status="Active";
                            //}
                        %>
                        <td valign="top" >
                            <%=list.getFieldValueString(i, 5)%>
                        </td>
                        <td valign="top">
                            <a style="font-size:11px;" href="javascript:viewByCompany('<%=list.getFieldValueInt(i, 0)%>')">View</a>
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
            <br><br>
            <table align="center">
                <tr>
                    <Td></Td>
                    <%if (list.getRowCount() == 0) {%>                 
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Create Company" onclick="javascript:createAccount();"><Td>
                        <%} else {%>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Create Company" onclick="javascript:createAccount();"><Td>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Assign Report To Company" onclick="javascript:reportToAccount();"><Td>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Assign Role" onclick="javascript:assignRoleToAcc();"><Td>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Expire Company" onclick="javascript:expireUser();"><Td>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Reset ExpiryDate" onclick="javascript:ResetExpiryDate();"><Td>
                        <%}%>
                </tr>
            </table>
            <div id="accNewReport" title="Company Report Assignment" STYLE='display:none'>
                <iframe  id="selectAccountReportData" NAME='selectAccountReportData' frameborder="0" height="100%" width="100%" SRC='#'></iframe>
            </div>
            <div id="createAcc"  title="Create Company" STYLE='display:none'>
                <iframe  id="createAccData" NAME='createAccData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
            </div>
            <div id="accRole"  title="Assign Role" STYLE='display:none'>
                <iframe  id="accRoleData" NAME='accRoleData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
            </div>
            <div id="resetExpiry"  title="Reset ExpiryDate" STYLE='display:none;height:auto'>
                <iframe  id="resetExpiryData" NAME='resetExpiryData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
            </div>
        </form>

        <div id="fadestart" class="black_start"></div>
        <div id="userAssignRoles" title="Assign Roles To Company" STYLE='display:none'>

        </div>
          <script type="text/javascript">
            function refAccRole()
            {
                $("#accRole").dialog('close');
                //window.location.reload(true);
            }
            function refreshAccountParent()
            {
                $("#createAcc").dialog('close');
                window.location.reload(true);
            }
            function createAccount()
            {
                initialog();
                var frameObj2=document.getElementById("createAccData");
                var source2 = "createOrganisationNew.jsp?accountFolderId=";
                frameObj2.src=source2;
                $("#createAcc").dialog('open');

            }
            function cancelAccountReportsAssign()
            {
                $('#accNewReport').dialog('close');
            }
            function reportToAccount()
            {
                var orgId="";
                var i=0;
                var obj = document.forms.myFormA.chkAccount;
                if(isNaN(obj.length))
                {
                    if(document.forms.myFormA.chkAccount.checked)
                    {
                        initialog();
                        orgId=document.forms.myFormA.chkAccount.value;
                        frameobj = document.getElementById("selectAccountReportData");
                        frameobj.src= "newReportAccountAssignment.jsp?orgId="+orgId;
                        $("#userNewReport").dialog('open');
                    }
                    else
                    {
                        alert("Please select company to Assign Reports")
                    }
                }
                else{
                    for(var j=0;j<obj.length;j++){
                        if(document.forms.myFormA.chkAccount[j].checked==true){
                            i++;
                            orgId=document.forms.myFormA.chkAccount[j].value;
                        }
                    }
                    if(i>1){
                        alert("Please select only one company to Assign Report");
                    }
                    else if(i==0){
                        alert("Please select company to Assign Report.");
                    }
                    else{
                        initialog();
                        frameobj = document.getElementById("selectAccountReportData");
                        frameobj.src= "newReportAccountAssignment.jsp?orgId="+orgId;
                        $("#accNewReport").dialog('open');
                    }
                }

            }


            function assignRoleToAcc()
            {
                var orgId="";
                var i=0;
                var obj = document.forms.myFormA.chkAccount;
                var tdObj;
                var trObj;
                var bRole;
                if(isNaN(obj.length))
                {
                    if(document.forms.myFormA.chkAccount.checked)
                    {
                        tdObj = document.forms.myFormA.chkAccount.parentNode;
                        trObj = tdObj.parentNode;
                        bRole = trObj.getElementsByTagName("td")[3].innerHTML;
                        bRole=$.trim(bRole);
                        //   if(bRole=="")
                        // {
                        initialog();
                        orgId=document.forms.myFormA.chkAccount.value;
                        frameobj = document.getElementById("accRoleData");
                        frameobj.src= "RoleList.jsp?orgId="+orgId;
                        $("#accRole").dialog('open');
                        // }
                        //  else
                        // {
                        //      alert('Role already assigned.');
                        //  }
                    }
                    else
                    {
                        alert("Please select company to Assign Role")
                    }
                }
                else{
                    for(var j=0;j<obj.length;j++)
                    {
                        if(document.forms.myFormA.chkAccount[j].checked==true)
                        {
                            tdObj = document.forms.myFormA.chkAccount[j].parentNode;
                            trObj = tdObj.parentNode;
                            bRole = trObj.getElementsByTagName("td")[3].innerHTML;
                            bRole=$.trim(bRole);                          
                            i++;
                            orgId=document.forms.myFormA.chkAccount[j].value;                            
                        }
                    }

                    /* if(i>1)
                    {
                        alert("Please select only one company to Assign Role");
                    }*/
                    // else
                    if(i==0)
                    {
                        alert("Please select company to Assign Role.");
                    }
                    else
                    {
                        // alert('bRole '+bRole)
                        // if(bRole=="")
                        // {
                        initialog();
                        //alert('orgId '+orgId);
                        frameobj = document.getElementById("accRoleData");
                        frameobj.src= "RoleList.jsp?orgId="+orgId;
                        $("#accRole").dialog('open');
                        // }
                        // else
                        // {
                        //     alert('Role already assigned.');
                        //  }
                    }
                }
            }
            function orderBYsel(){
                // alert("in function");
                var selval=document.getElementById("sortBySel1").value;
                var seloption;
                var optionobj = document.forms.myFormA.sortOption;
                for(var i=0;i<optionobj.length;i++){
                    if(optionobj[i].checked){
                        seloption=optionobj[i].value
                    }
                }
                $.ajax({
                    url: "sortTabdetails.do?param=useracclistSel&selval1="+selval+"&seloption1="+seloption,
                    success: function(data){

                        // window.location.reload(true);
                    }
                });
                document.forms.myFormA.action="AdminTab.jsp#User_Accounts";
                document.forms.myFormA.submit();

            }


            function initialog(){
                if ($.browser.msie == true){
                    $("#accNewReport").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true

                    });
                    $("#accRole").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: true

                    });

                    $("#createAcc").dialog({
                        autoOpen: false,
                        height: 380,
                        width: 620,
                        position: 'justify',
                        modal: true
                    });
                     $("#resetExpiry").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 340,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $("#accNewReport").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });
                    $("#createAcc").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 620,
                        position: 'justify',
                        modal: true
                    });
                    $("#accRole").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: true

                    });
                    $("#resetExpiry").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 300,
                        width: 450,
                        position: 'justify',
                        modal: true
                    });
                }
            }
        </script>
        <script type="text/javascript">


            function expireUser(){
                var orgId="";
                var i=0;
                var obj = document.forms.myFormA.chkAccount;
                // alert(obj);

                for(var j=0;j<obj.length;j++){
                    if(document.forms.myFormA.chkAccount[j].checked==true){
                        i++;
                        orgId=document.forms.myFormA.chkAccount[j].value;
                    }
                }
                if(i>1){
                    alert("Please select only one company to Assign Report");
                }
                else if(i==0){
                    alert("Please select company to Assign Report.");
                }else{

                    $.ajax({
                        url: 'organisationDetails.do?param=verifyExpireOrg&orgId='+orgId,
                        success: function(data) {
                            // alert("data is"+data)
                            if(data=='ok'){
                                // alert('Account Expired.');
                                var r=confirm("Confirm Expire");
                                if (r==true){
                                    $.ajax({
                                        url: 'organisationDetails.do?param=expireOrg&orgId='+orgId,
                                        success: function(data) {
                                            if(data==1){
                                                // alert('Account Expired.');
                                                window.location.reload(true);
                                            }

                                        }
                                    });
                                }
                            }else{
                                alert(data);
                            }
                        }
                    });
                }
            }
            function viewByCompany(userId){
                parent.viewByCompanyParent(userId);

            }
            function ResetExpiryDate(){
                var i=0;
                var orgId;
                var chkorgobj = document.forms.myFormA.chkAccount;
                for(var j=0;j<chkorgobj.length;j++){
                    if(chkorgobj[j].checked){
                        orgId=chkorgobj[j].value;
                        i++;
                    }
                }
                if(i==0){
                    alert("Please Select a Company to Reset ExpiryDate");
                    return false;
                }
                else if(i>1){
                    alert("Please Select only one Company");
                    return false;
                }else{
                    initialog();
                    var frameobj = document.getElementById("resetExpiryData");
                    frameobj.src= "<%=request.getContextPath()%>/pbResetExpiryDate.jsp?userId="+orgId+"&userFlag=N";
                    $("#resetExpiry").dialog('open');
                }
            }
        </script>
    </body>
</html>