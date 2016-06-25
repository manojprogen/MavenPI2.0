
<%@page import="prg.business.group.TargetMeasureParametersDAO,prg.db.PbReturnObject,prg.business.group.BusinessGroupListDAO"%>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<Head>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
         <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/BusinessGroup.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

    <script type="text/javascript">

        function CheckAll(chk)
        {
            if(document.myForm.checkCtr.checked==true)
            {
                if(isNaN(chk.length))
                {
                    chk.checked=true;
                }
                else
                {
                    for (i = 0; i < chk.length; i++)
                    chk[i].checked = true ;
                }
            }
            else
            {
                if(isNaN(chk.length))
                {
                    chk.checked=false;
                }
                else
                {
                    for (i = 0; i < chk.length; i++)
                    chk[i].checked = false ;
                }
            }
        }
        function CheckTop2(ctr)
        {
            var all = document.myForm.paramName.length;
            var m=0;
            var n=0;
            for(j=0;j<all;j++)
            {
                if(document.myForm.paramName[j].checked==false)
                {
                    m++;
                }
                if(document.myForm.paramName[j].checked==true)
                {
                    n++
                }
            }
            if(m>=1)
            {
                ctr.checked=false;
            }
            if(n==all)
            {
                ctr.checked=true;
            }
        }

        function cancelShowParameters()
        {
            parent.cancelDiv();            
        }

        //jsp added by susheela
        function saveMeasureParametrs(){
        //document.forms.myForm.action="saveMeasureParameters.jsp";
        //document.forms.myForm.submit();
        var allValues="";
        var checkB = document.getElementsByName("paramName");
        var AllSelectedTabCols1 = document.getElementById("AllSelectedTabCols");
        var busGroup1 = document.getElementById("busGroup");
        var AllSelectedTabCols =AllSelectedTabCols1.value;
        var busGroup =busGroup1.value;
        var eleIds="";
       // alert(AllSelectedTabCols.value);
        for(var p=0;p<checkB.length;p++){
            if(checkB[p].checked){
           // alert(checkB[p].value);
            var e = checkB[p].value.split(":");

            allValues = allValues+","+e[0];
            eleIds = eleIds+","+e[1];
            }
        }
       // alert('eleIds  '+eleIds);
      //  alert('AllSelectedTabCols1 --- '+AllSelectedTabCols1.value)
        $.ajax({
                        url: 'targetmeasuresaction.do?targetMeasures=saveTargetFactsParameters&allValues='+allValues+'&busGroup='+busGroup+'&AllSelectedTabCols='+AllSelectedTabCols+"&eleIds="+eleIds,
                        success: function(data) {
                            if(data=="false")
                                alert("Parameters Saved Successfully")
                            else{
                                  alert("Parameters Saved Successfully.")
                            }

                        }
                    });
                    var frameObj=parent.document.getElementById("targetParameters");
                    var divObj=parent.document.getElementById("targetParametersDiv");
                    frameObj.style.display='none';
                    divObj.style.display='none';
                    parent.document.getElementById('fade').style.display='none';
                    parent.refreshPage();
        }
        
</script>
<script type="text/javascript" src="javascript/BusinessGroup.js"></script>
<link href="stylesheets/myStyles.css" rel="stylesheet" type="text/css" />
</Head>
<%
TargetMeasureParametersDAO bgList=new TargetMeasureParametersDAO();
String AllSelectedTabCols = "";
if(request.getAttribute("AllSelectedTabCols")!=null)
AllSelectedTabCols = (String)request.getAttribute("AllSelectedTabCols");
else
AllSelectedTabCols = request.getParameter("AllSelectedTabCols");
String busGroup = "";
if(request.getAttribute("busGroup")!=null)
busGroup = (String)request.getAttribute("busGroup");
else
    busGroup = request.getParameter("busGroup");
//////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in show Params busGroup "+busGroup+" AllSelectedTabCols "+AllSelectedTabCols);
PbReturnObject pbro = bgList.getMeasureTargetParametersGroup(busGroup);//bgList.getMeasureTargetParameters(AllSelectedTabCols);
//pbro.writeString();

%>
<Br/>
<center><font size="3px" color="black"><%="Select Parameters To Save"%></font><br/><Br/>
<Form name="myForm" id="myForm">
    <Table>
        <tr>
            <td><input checked type="checkbox" name="checkCtr" value="yes" onclick="CheckAll(document.myForm.paramName)"></td>
            <td style="font-weight:bold">Parameter Name</td>
        </tr>
        <%for(int y=0;y<pbro.getRowCount();y++){%>
        <Tr>
            <Td>
                <Input CHECKED type="checkbox" name="paramName" id="paramName"<%=y%> value="<%=pbro.getFieldValueString(y,"MEMBER_ID")%>:<%=pbro.getFieldValueString(y,"ELEMENT_ID")%>" onclick="CheckTop2(document.myForm.checkCtr)">
                </Td>
           <Td><%=pbro.getFieldValueString(y,"MEMBER_NAME")%></Td>
       </Tr>
        <%}%>
    </Table><br/>
     <Input type="hidden" name="busGroup" id="busGroup"  value="<%=busGroup%>">
                 <Input type="hidden" name="AllSelectedTabCols" id="AllSelectedTabCols"  value="<%=AllSelectedTabCols%>">
           
    <Table>
         <Tr><Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Save" ONCLICK="saveMeasureParametrs()"></Td>
         <Td align="center"><Input TYPE="button" class="navtitle-hover" style="width:auto" value="Cancel" ONCLICK="cancelShowParameters()"></Td>


         </Tr>
    </Table>

</Form>
</center>