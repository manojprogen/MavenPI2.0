<%@page import="utils.db.ProgenConnection"%>
<%@page import="java.sql.*"%>
<%@page  import="utils.db.*" %>
<%@page  import="prg.measure.param.*" %>
<%@page  import="prg.measure.client.*" %>
<%@page  import="prg.reporttable.params.*" %>
<%@page  import="prg.db.*" %>


<html>
    <!--Connection to database//-->
    <head>
        <%
        try {
            Session sess=new Session();
            String reportId=request.getParameter("reportId");
            String lastSeq=request.getParameter("lastSeq");
            String path=request.getParameter("path");
          
            //path=path.replace(';','&');

            PbMeasuresParams params = new PbMeasuresParams();
            PbMeasuresManager client = new PbMeasuresManager();
            PbReportTableParams repParams = new PbReportTableParams();
            repParams.setReportId(reportId);
            sess.setObject(repParams);
            PbReturnObject qrRetObj=client.getMeasureQueryId(sess);

            params.setQueryId(qrRetObj.getFieldValueInt(0,"QUERY_ID"));
            //params.setQueryId(505);
            sess.setObject(params);
            PbReturnObject pbro =client.getMeasures(sess);
        %>
        <link href="css/myStyles.css" rel="stylesheet" type="text/css">
        <style>
            input.myButton {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 12px;
                font-weight: bold;
                padding: 2px;
                height: 25px;
                width: 25px;
            }

        </style>

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

            function addValue(str,str2,classType){//storing external formula
                if(document.forms.f1.txt2.value==''){
                    if(classType=='Operators' || classType=='CloseOper' || classType=='SpecOper'){
                        alert('Please select a Query Column or a Numeric')
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

                else{
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
                            alert('Please select Query column or a Numeric')
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
                            alert('Please select Query column or a Numeric')
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
        </script>
    </head>
    <body>
       
        <form id="f1">
            <br>
                <br>
            <input type="hidden" name="queryId" id="queryId" value="<%=qrRetObj.getFieldValueInt(0,"QUERY_ID")%>">
            <input type="hidden" name="tableId" id="tableId" value="<%=qrRetObj.getFieldValueInt(0,"TABLE_ID")%>">
            <input type="hidden" name="path" id="path" value="<%=path%>">
            <table class="Table1" align="center"  border="1" cellpadding="0" cellspacing="0" id="T1" width="30%" border-style="dashed" border-color="#0099CC" >
                <tr>
                    <td align="left"><font size="2"><strong> New Column : </strong></font><input type="text" id="txt1" name="txt1" class="myTextbox5" size="30" value=" " onblur="Editname()" > </td>
                </tr>
                <tr>
                    <td align="middle"><input type="button" class="btn" id="savecolumn" value="Save Measure" onclick="val_fun('<%=reportId%>','<%=lastSeq%>','<%=path%>')">
                        <input type="button" class="btn" id="Close" value="Close Window" onclick="window.close();"></td>
                    <input type="hidden" id="h" name="h">
                </tr>
                <tr>
                    <td ><font size="2"><strong>Formula : &nbsp;&nbsp;</strong></font><textarea rows="1" cols="40"  name="txt2" id="txt2" class="myTextbox5" ></textarea></td>
                </tr>

                <tr>
                    <td align="center">
                        <input type="button" id="Sub" value="Clear" class="btn" onclick="clearboxes()">
                        <input type="button" id="Undo" value="Undo" class="btn" onclick="undoFun()">
                        <input type="button" id="Redo" value="Redo" class="btn" onclick="redoFun()" >
                    </td>
                </tr>
                <tr>
                <tr>
                    <td>
                        <table border="1" cellpadding="0" cellspacing="0" id="T1" width="100%" border-style="dashed" border-color="#0099CC" >
                            <tr>
                                <td width="33%" align="center"><font size="2"><strong>Query</strong></font></td>
                                <td width="33%" align="center"><font size="2"><strong>Numerics</strong></font></td>
                            </tr>
                            <tr>
                                <td>
                                    <table border-style="dashed" border-color="#0099CC">
                                        <%
                                        String colNames[]=pbro.getColumnNames();
                                        for(int i=0;i<pbro.getRowCount();i++) {
                                        %>

                                        <tr>
                                            <td>
                                                <font size="2"><a href="javascript:void(0)" id="<%=pbro.getFieldValueString(i,colNames[1])%>" style="color:blue"  onclick="addValue('<%=pbro.getFieldValueString(i,colNames[0])%>','<%=pbro.getFieldValueString(i,colNames[1])%>','Query')"><%=pbro.getFieldValueString(i,colNames[0])%></a></font>
                                            </td>
                                        </tr>

                                        <%}
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        %>
                                    </table>
                                </td>
                                <td>
                                    <table border="0"  id="T4" width="100%" style="border-style:dashed;border-color:#0099CC">
                                        <tr>
                                            <td width="50%" align="center" valign="top">
                                            <input type="text" onkeyup="test()" id="Numer" name="Numer" class="myTextbox15"/></td>

                                            <td width="50%" align="center" valign="top">
                                            <input type="button" id="Button1" value="Enter" class="btn" onclick="addNumeric('Numerics')"></td>
                                        </tr>
                                        <tr>
                                            <br>
                                        </tr>
                                    </table>

                                    <table border="1" cellpadding="0" cellspacing="0" id="T4" width="100%" border-style="dashed" border-color="#0099CC">
                                        <tr>
                                            <td align="center"><font size="2"><strong>Operators</strong></font></td>
                                        </tr>
                                    </table>
                                    <br>
                                    <table border="0" cellpadding="0" cellspacing="0" id="T5" width="100%" border-style="dashed" border-color="#0099CC">
                                        <tr>
                                            <td width="25%" align="center"> <input type="button" class="btnsmall" id="+" value="+" onclick="addValue('+','+','Operators')"></td>

                                            <td width="25%" align="center"> <input type="button" class="btnsmall" id="-" value="-" onclick="addValue('-','-','Operators')"></td>

                                            <td width="25%" align="center"> <input type="button" class="btnsmall" id="*" value="*" onclick="addValue('*','*','Operators')"></td>

                                            <td width="25%" align="center"> <input type="button" class="btnsmall" id="/" value="/" onclick="addValue('/','/','Operators')"></td>
                                        </tr>
                                    </table>
                                    <br>
                                    <table border="0" cellpadding="0" cellspacing="0" id="T5" width="100%" border-style="dashed" border-color="#0099CC">
                                        <tr>
                                            <td width="25%" align="center"> <input type="button" class="btnsmall" id="=" value="=" onclick="addValue('=','=','Operators')"></td>

                                            <td width="25%" align="center"> <input type="button" class="btnsmall" id="( )" value="( )" onclick="addValue('( )','( )','SpecOper')"></td>

                                            <td width="25%" align="center"> <input type="button" class="btnsmall" id="(" value="(" onclick="addValue('( ','(','OpenOper')"></td>

                                            <td width="25%" align="center"> <input type="button" class="btnsmall"  id=")" value=")" onclick="addValue(' )',')','CloseOper')"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <textarea id="tArea" name="tArea" style="display:none"> </textarea>
        </form>
         
    </body>
</html>
