

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,prg.db.PbDb,prg.db.PbReturnObject"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>


        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="tabs/css/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <!-- <script src="javascript/treeview/jquery.js" type="text/javascript"></script>-->
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>

        <script type="text/javascript" src="javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
<!--        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>-->
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />

        <!--<link href="StyleSheet.css" rel="stylesheet" type="text/css" />-->
        <link href="confirm.css" rel="stylesheet" type="text/css" />
        <link href="jquery.contextMenu.css" rel="stylesheet" type="text/css" />

        <script src="jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="tabs/js/ui.dialog.js"></script>
        <script type="text/javascript" src="tabs/js/external/bgiframe/jquery.bgiframe.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />



      
        <title>JSP Page</title>
        <style type="text/css">
            a:link {color:blue;}
            a:visited {color: #660066;}
            a:hover {text-decoration: none; color: #ff9900; font-weight:bold;}
            a:active {color: #ff0000;text-decoration: none}

            .input {
            border: 1px solid #006;
            background: #ffffcc;
            }
            .input:hover {
            border: 1px solid #f00;
            background: #ffffff;
            }
            .button {
            border: 1px solid #006;
            background: #ccf;

            }
            .button:hover {
             border: 1px solid #f00;
             background: #eef;
            }

              .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                overflow:auto;
            }
            .white_content {
                display:none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 200px;
                height:150px;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
             .white_content1 {
                display:none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 200px;
                height:200px;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
             .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
        </style>
    </head>
    <body>
        <center>
            <%

        //int table_id = 161;
            int table_id = Integer.parseInt(request.getParameter("bussTableId"));
        String query = "select column_name from prg_grp_buss_table_details where buss_table_id=" + table_id;
    
       
         

       PbDb pbDb =new PbDb();
        PbReturnObject rsPbReturnObject = pbDb.execSelectSQL(query);
            %>
            <form id="f1" name="myForm">
                <input type="hidden" name="tableId" id="tableId" value="<%=table_id%>">

                 
                    <Table ALIGN="right">
                  <Tr>
                      <Td>
                      <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveFormula()">
                     </Td>
                  </Tr>
                      </Table>
                    <Br/><Br/>

                    <center>
                <table align="center" width="100%" border="1" >
                    <tr style="width:100%">
                        <td style="width:100%">
                            <table style="width:100%">
                                <tr style="width:100%" align="center">
                                    <td width="20%" >
                                        <label class="label"><b>Name</b></label>
                        </td>
                                    <td width="80%">
                                        <input type="text" name="colName" size="50">
                        </td>
                    </tr>
                            </table>
                        </td>
                                    </tr>
                    <tr style="width:100%">
                        <td style="width:100%">
                            <table style="width:100%" border="0px solid">
                                <tr style="width:100%">
                                    <td width="50%" style="height:150px" valign="top">
                                        <center><label class="label"><b>Display</b></label ><hr></center>
                                        <textarea style="width:99%;height:150px;overflow:auto;background-color:#e6e6e6;color:#369;border:0;" id="txt2" name="txt2" cols="60" ></textarea>
                        </td>
                                    <td width="50%" valign="top">
                                        <center><label class="label"><b>Columns</b></label ><hr></center>
                                        <div style="height:200px;overflow-y:auto;overflow-x:hidden">

                            <%
       for(int i=0;i<rsPbReturnObject.getRowCount();i++) {
                            %>

                            &nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" id="<%=rsPbReturnObject.getFieldValueString(i,0)%>" style="font-family:verdana;font-size:12px;text-decoration:none;color:#369" onclick="addValue('<%=rsPbReturnObject.getFieldValueString(i,0)%>','<%=rsPbReturnObject.getFieldValueString(i,0)%>','Query')"><%=rsPbReturnObject.getFieldValueString(i,0)%></a><br>

                            <%}%>
                                        </div>
                                    </td>

                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <table style="width:100%" border="1px solid" align="center">
                <tr style="width:100%" align="center">
                    <td width="100%" align="center" valign="top">
                        <table border="0" width="100%" align="center" cellspacing="5">
                            <tr style="width:100%" align="center">
                                <td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="+"  id="+" onclick="addValue('+','+','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="-"  id="-" onclick="addValue('-','-','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="*"  id="*" onclick="addValue('*','*','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="/"  id="/" onclick="addValue('/','/','Operators')"></td></tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="("  id="(" onclick="addValue('( ','(','OpenOper')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value=")"   id=")" onclick="addValue(' )',')','CloseOper')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="()"  id="( )" onclick="addValue('( )','( )','SpecOper')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="="  id="=" onclick="addValue('=','=','Operators')"></td></tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="," onclick="addValue(',',',','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="case" onclick="addCase()"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Undo" value="Undo"  onclick="undoFun()"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Redo" value="Redo"  onclick="redoFun()" ></td></tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="nvl"  id="nvlb" onclick="addNvl()"></td></tr>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="sum"  id="-" onclick="addValue('sum','sum','Operators')"></td></tr>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="avg"  id="avg" onclick="addValue('avg(','avg(','operands')"></td></tr>
                                        <tr><td valign="top"><input type="button" class="navtitle-hover" style="width:auto" value="in"  id="inb" onclick="addIn()"></td></tr>
                                    </table>
                                </td>
                    </tr>
                </table>
                    </td>
                </tr>
            </table>
            </center>
                <br>
                    <table align="center" width="100%" border="0">
                <tr align="center">
                    <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveFormula()" >&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelFilter()"></td>
                </tr>
            </table>

                <textarea id="tArea" name="tArea" style="display:none"> </textarea>
            </form>
            <div id="fadeCase" class="black_overlay"></div>
            <div id="caseDialog" class="white_content" title="Cases"><center>
                    <form name="caseForm">
                        When &nbsp; <input type="text" id="when" name="when" onfocus="focusedElement('when')"><br>
                        Then &nbsp; <input type="text" id="then" name="then" onfocus="focusedElement('then')"><br>
                        Else &nbsp; <input type="text" id="else" name="else" onfocus="focusedElement('else')"><br><br>
                <input type="button" class="navtitle-hover" style="width:auto" value="ok" onclick="saveCase()"></form></center>
            </div>

            <div id="nvlDialog" class="white_content1" title="NVL">
               Column Name : <textarea id="nvlCN" onfocus="focusedElement('nvlCN')"></textarea><br><br>
               Replace With: <textarea id="nvlRW" onfocus="focusedElement('nvlRW')"></textarea><br>
                <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveNVL()">
            </div>

            <div id="anyDialog" class="white_content" title="ANY">
                <textarea id="any" onfocus="focusedElement('any')"></textarea><br>
                <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveANY()">
            </div>

            <div id="inDialog" class="white_content" title="IN">
                <textarea id="in" onfocus="focusedElement('in')"></textarea><br>
                <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveIN()">

            </div>

        </center>
                                          <script type="text/javascript">
            var formArray=new Array();// used for external formula
            var formArray1=new Array();// used for internal formula
            var prevClass=new Array();//used for storing prevClassType

            var formula='';
            var formula1='';

            var arrIndex=0;
            var arrIndex1=0;
            var prevIndex=0;
            var Flag=1;

            var arrIndexLength=0;
            var arrIndex1Length=0;
            var prevIndexlength=0;
            var prevClassType=null;
            var prevStr=null;
            var curStr=null;

            var caseStr = 'case when {        }'+"\n"+'then {       }'+"\n"+'else  {       }'+"\n"+'end';
            var caseWindowStatus = '0';
            var nvlWindowStatus = '0';
            var anyWindowStatus = '0';
            var inWindowStatus = '0';
            var focussed='';
            var output='';
            var activeDialog='';
            function focusedElement(element)
            {
                focussed = element;
                //alert('focussed is '+element)
            }

            function addValue(str,str2,classType)
            {
                if(caseWindowStatus == '1')
                {

                    document.getElementById(focussed).value = document.getElementById(focussed).value+str2;
                    return;
                }
                else if(nvlWindowStatus == '1')
                {

                    document.getElementById(focussed).value = document.getElementById(focussed).value+str2;
                    return;
                }
                else if(anyWindowStatus == '1')
                {

                    document.getElementById(focussed).value = document.getElementById(focussed).value+str2;
                    return;
                }
                else if(inWindowStatus == '1')
                {

                    document.getElementById(focussed).value = document.getElementById(focussed).value+str2;
                    return;
                }
                // alert(document.forms.f1.txt2.value=='')//storing external formula
                if(document.forms.f1.txt2.value=='')
                {
                    // alert('in if')
                    if(classType=='Operators' || classType=='CloseOper' || classType=='SpecOper')
                    {
                        alert('Please select a column')
                        prevStr=null;
                        prevClassType=null;
                        return false;
                    }
                    else{
                        if(checkExpectedClassType(prevClassType,classType)){
                            formula=document.forms.f1.txt2.value;
                            if(str=='( )'){
                                formArray[arrIndex]=formula;
                                formula='('+formula+')'
                                document.forms.f1.txt2.value =formula;
                                arrIndexLength=formArray.length;
                                arrIndex++;
                                addTextArea(str2);
                                prevStr=str;
                                prevClassType=classType;
                                prevClass[prevIndex]=prevClassType;
                                prevIndex++;

                            }
                            else{
                                formArray[arrIndex]=formula;
                                formula=formula+str;
                                document.forms.f1.txt2.value = formula;
                                arrIndexLength=formArray.length;
                                arrIndex++;
                                addTextArea(str2);
                                prevStr=str;
                                //alert("prevStr-----"+prevStr)
                                prevClassType=classType;
                                //alert("prevClassType----"+prevClassType)
                                prevClass[prevIndex]=prevClassType;
                                //alert("prevClass[prevIndex]---------"+prevClass[prevIndex])
                                prevIndex++;
                            }
                            return true;
                        }
                    }
                }

                else if(!document.forms.f1.txt2.value==''){
                    if(checkExpectedClassType(prevClassType,classType)){
                        // alert("checking")
                        formula=document.forms.f1.txt2.value;
                        //alert("formula"+formula)
                        if(str=='( )'){
                            formArray[arrIndex]=formula;
                            formula='('+formula+')'
                            document.forms.f1.txt2.value =formula;
                            arrIndexLength=formArray.length;
                            arrIndex++;
                            addTextArea(str2);
                            prevStr=str;
                            prevClassType=classType;
                            prevClass[prevIndex]=prevClassType;
                            prevIndex++;
                        }
                        else{
                            formArray[arrIndex]=formula;
                            formula=formula+str;
                            document.forms.f1.txt2.value = formula;
                            arrIndexLength=formArray.length;
                            arrIndex++;
                            addTextArea(str2);
                            prevStr=str;
                            //alert("prevStr-----"+prevStr)
                            prevClassType=classType;
                            // alert("prevClassType----"+prevClassType)
                            prevClass[prevIndex]=prevClassType;
                            //alert("prevClass[prevIndex]----"+prevClass[prevIndex])
                            prevIndex++;
                        }
                        return true;
                    }
                }

            }
            function addTextArea(str2){//storing internal formula
                formula1=document.forms.f1.tArea.value;

                if(str2=='( )'){
                    formArray1[arrIndex1]=formula1;
                    formula1='('+formula1+')'
                    document.forms.f1.tArea.value =formula1;
                    arrIndex1Length=formArray1.length;

                    arrIndex1++;
                }
                else{
                    formArray1[arrIndex1]=formula1;
                    formula1=formula1+str2;
                    document.forms.f1.tArea.value = formula1;
                    arrIndex1Length=formArray1.length;

                    arrIndex1++;
                }
                return true;
            }
            function undoFun(){
                if(arrIndexLength>0){
                    arrIndexLength--;
                    arrIndex1Length--;
                    // alert('inside arrIndexLength is '+arrIndexLength)
                    // alert(formArray[parseInt(arrIndexLength)]);
                    //alert(formArray1[parseInt(arrIndex1Length)]);
                    document.forms.f1.txt2.value =formArray[parseInt(arrIndexLength)];
                    document.forms.f1.tArea.value =formArray1[parseInt(arrIndex1Length)];
                }
                else{
                    alert('nothing to undo')
                }
                if(prevIndex>0){
                    prevIndex--;
                    prevClassType=prevClass[parseInt(prevIndex-1)];
                }
                return (Flag);
            }
            function redoFun(){

                if(arrIndexLength<(formArray.length-1)){
                    arrIndexLength++;
                    arrIndex1Length++;
                    document.forms.f1.txt2.value =formArray[parseInt(arrIndexLength)];
                    document.forms.f1.tArea.value =formArray1[parseInt(arrIndex1Length)];

                }
                else{
                    alert('nothing to redo')
                }
            }

            function Editname(){
                var Ename = document.forms.f1.txt1.value;
                Ename = Ename.replace(/[^a-zA-Z0-9 ]+/g,'');
                Ename = Ename.replace(/\s/g, "");
                document.getElementById('h').value=Ename;
                return Ename;
            }

            function addNumeric(classType3){
                if(checkExpectedClassType(prevClassType,classType3)){
                    var r=document.forms.f1.Numer.value;
                    if(Flag==1)
                    {
                        formula=document.forms.f1.txt2.value;
                        formula1=document.forms.f1.tArea.value;
                    }

                    formArray1[arrIndex1]=formula1;
                    formArray[arrIndex]=formula;

                    formula=formula+r;
                    formula1=formula1+r;

                    document.forms.f1.txt2.value=formula;
                    document.forms.f1.tArea.value=formula1;
                    arrIndexLength=formArray.length;
                    arrIndex1Length=formArray1.length;
                    arrIndex1++;
                    arrIndex++;
                    prevIndex++;

                    prevClassType=classType3;
                }

            }
            function clearboxes(){
                document.getElementById('txt2').value='';
                document.getElementById('tArea').value='';
                document.getElementById('Numer').value='';
                prevClassType=null;
            }
            function checksave(jspname){
                document.forms.f1.action=jspname;
                document.forms.f1.submit();
            }
            function val_fun(reportId,lastSeq,path){
                if(document.forms.f1.txt1.value==" ")
                {
                    alert("Enter NewColumn Name")
                    return false;
                }
                if(document.forms.f1.txt2.value==""){
                    alert("Enter Formula")
                    return false;
                }
                else
                {
                    //alert('pbFormula_save.jsp?reportId='+reportId+"&lastSeq="+lastSeq+"&path="+path);
                    checksave('pbFormula_save.jsp?reportId='+reportId+"&lastSeq="+lastSeq+"&path="+path);
                }
            }

            function checkExpectedClassType(prevClassType1,classType1){
                //alert('prevClassType1 is '+prevClassType1+',classType1 is'+classType1)
                if(prevClassType!=null){
                    if(prevClassType1=='Query'){
                        if(classType1=='Operators'|| classType1=='OpenOper'|| classType1=='CloseOper' || classType1=='SpecOper')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }

                    }
                    if(prevClassType1=='Operators'){

                        if(classType1=='Query' || classType1=='Numerics' || classType1=='OpenOper')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }
                    if(prevClassType1=='Numerics'){
                        if(classType1=='Operators'|| classType1=='OpenOper' || classType1=='CloseOper' || classType1=='SpecOper')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                    if(prevClassType1=='OpenOper'){
                        if(classType1=='Query' || classType1=='Numerics'|| classType1=='OpenOper')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }
                    if(prevClassType1=='CloseOper'){
                        if(classType1=='Operators' || classType1=='SpecOper' || classType1=='CloseOper')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                    if(prevClassType1=='SpecOper'){
                        if(classType1=='Operators')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                }
                else{
                    return true;
                }
            }
            function test(){
                var x=document.getElementById("Numer").value;
                if(x.match(/\D/g)!=null){
                    alert("Please Enter Only Digits From 0-9");
                    x=x.replace(/\D/g,"");
                    document.getElementById("Numer").value=x;
                }
            }

            function saveFormula()
            {
                var query = document.getElementById('txt2').value;
                var tableId = document.getElementById('tableId').value;
                var url = "checkFilter?query="+query+"&tableId="+tableId;
                // alert('query is '+query);
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }

                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);
                // if(output=='Incorrect')
                // alert('output is '+output)

                //  document.myForm.action = "addColumnFormula.do";
                //  document.myForm.submit();
                // alert(document.getElementById('tArea').value);
            }

            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }

            function stateChanged()
            {
                if (xmlHttp.readyState==4)
                {

                    var output1=xmlHttp.responseText;
                    // alert("next of output "+output1+"continue");
                    if(output1=="Correct")
                    {
                        // alert("in if "+output1);
                        document.myForm.action = "addFilter.do";
                        document.myForm.submit();
                        parent.cancelFilter();
                    }
                    else
                    {
                        alert(output1);
                    }

                }
            }

            function cancelFilter()
            {
               
                parent.cancelFilter();
            }

            function addCase()
            {

                     document.getElementById("caseDialog").style.display='block';
                document.getElementById("fadeCase").style.display='block';
                $('#caseDialog').dialog('open');
                caseWindowStatus = 1;
            }

            function addAny()
            {
              document.getElementById("anyDialog").style.display='block';
                document.getElementById("fadeCase").style.display='block';
              //  $('#anyDialog').dialog('open');
                anyWindowStatus = 1;
            }

            function addIn()
            {
              document.getElementById("inDialog").style.display='block';
                document.getElementById("fadeCase").style.display='block';
               // $('#inDialog').dialog('open');
                inWindowStatus = 1;
            }

            function addNvl()

            {  document.getElementById("nvlDialog").style.display='block';
                document.getElementById("fadeCase").style.display='block';

                // alert('addnvl');
               // $('#nvlDialog').dialog('open');
                nvlWindowStatus = 1;
            }



            $.ui.dialog.defaults.bgiframe = true;

            $(function() {
                $("#caseDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 200,
                    position: 'top',
                    close: function() {
                   caseWindowStatus = '0';
                   
                   
                   
                }

                });   
                ////////////////////

                $("#nvlDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 200,
                    position: 'top',
                    close: function() {
                    nvlWindowStatus = '0';
                    }
                });

                ////////////////////////////

                $("#anyDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 200,
                    position: 'top',
                    close: function() {
                    anyWindowStatus = '0';
                    }

                });

                //////////////////////////////

                $("#inDialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 200,
                    position: 'top',
                    close: function() {
                    inWindowStatus = '0';
                    }
                });
            });



            function saveCase()
            {

                var when = ' case when{'+document.getElementById('when').value+'}'+"\n";
                var then = 'then{'+document.getElementById('then').value+'}'+"\n";
                var elsee = 'else{'+document.getElementById('else').value+'}'+"\n"+'end'+"\n";
                //alert(when+then+elsee);
                document.getElementById('txt2').value = document.getElementById('txt2').value+when+then+elsee;
                document.getElementById('when').value=document.getElementById('then').value=document.getElementById('else').value='';
                caseWindowStatus = '0';
                //$('#caseDialog').dialog('close');
                document.getElementById("caseDialog").style.display='none';
                document.getElementById("fadeCase").style.display='none';
            }

            function saveNVL()
            {
                var cName = ' nvl ('+document.getElementById('nvlCN').value+','+document.getElementById('nvlCN').value+')';
                document.getElementById('txt2').value = document.getElementById('txt2').value+cName;
                nvlWindowStatus = '0';
                  document.getElementById("nvlDialog").style.display='none';
                document.getElementById("fadeCase").style.display='none';
               // $('#nvlDialog').dialog('close');
            }

            function saveANY()
            {
                var anyValue = ' any ('+document.getElementById('any').value+')';
                document.getElementById('txt2').value = document.getElementById('txt2').value+anyValue;
                anyWindowStatus = '0';
                document.getElementById("anyDialog").style.display='none';
                document.getElementById("fadeCase").style.display='none';
                //$('#anyDialog').dialog('close');
            }

            function saveIN()
            {
                var inValue = ' in ('+document.getElementById('in').value+')';
                document.getElementById('txt2').value = document.getElementById('txt2').value+inValue;
                inWindowStatus = '0';
                document.getElementById("inDialog").style.display='none';
                document.getElementById("fadeCase").style.display='none';
               // $('#inDialog').dialog('close');
            }


        </script>
    </body>
</html>
