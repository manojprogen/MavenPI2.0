<%@page import="utils.db.ProgenConnection"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.Random"%>


<html>
    <title>
        PROGEN STICKY NOTES
    </title>
    <head>
        <style>
            .test
            {
                width : 150px;
                height : 170px;
                border : 1px solid #ffff00;
                background-color: #ffff00;
            }
        </style>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/themes/base/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.draggable.js"></script>
        <script language="JavaScript1.2">

            // Script Source: CodeLifter.com
            // Copyright 2003
            // Do not remove this header

            isIE=document.all;
            isNN=!document.all&&document.getElementById;
            isN4=document.layers;
            isHot=false;

            $(document).ready(function(){
                $("#theLayer").draggable();
            });
            function hideMe(){
                if (isIE||isNN) whichDog.style.visibility="hidden";
                else if (isN4) document.theLayer.visibility="hide";
            }

            function showMe(){
                if (isIE||isNN) whichDog.style.visibility="visible";
                else if (isN4) document.theLayer.visibility="show";
            }

            document.onmousedown=ddInit;
            document.onmouseup=Function("ddEnabled=false");

            function updateNote(){

                var url = "updateStickyNote.jsp?txt="+document.getElementById('txt').value+"&user="+document.getElementById('user').value;

                document.forms.myForm.action="updateStickyNote.jsp";
                document.forms.myForm.submit();
            }

        </script>

    </head>

    <body>
        
        <!-- BEGIN FLOATING LAYER CODE //-->
        <div id="theLayer" style="position:absolute;width:150px;left:100;top:100;visibility:visible">
            <table border="0" width="120" bgcolor="#ffff00" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="100%">
                        <table border="0" width="100%" cellspacing="0" cellpadding="0" >
                            <tr>
                                <td id="titleBar" style="cursor:move" width="100%">
                                    <ilayer width="100%" onSelectStart="return false">

                                        <layer id="draggable" width="100%" >
                                            Notes
                                        </layer>
                                    </ilayer>
                                </td>
                                <td style="cursor:hand" valign="top">

                                </td>
                            </tr>
                            <tr>
                                <td width="100%" valign="top" bgcolor="#ffff00" colspan="2">
                                    <iframe class="test" src="sticknote.jsp" frameborder="0" scrolling="no" marginheight="0"  marginwidth="0"> </iframe>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
        </div>
        <!-- END FLOATING LAYER CODE //-->







    </body>

</html>
