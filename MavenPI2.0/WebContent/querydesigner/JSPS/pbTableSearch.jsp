<%@page import="utils.db.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/Tr/html4/loose.dtd">
<html>
    <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.draggable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.resizable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.tabs.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.sortable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.dialog.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/effects.core.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/effects.highlight.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/external/bgiframe/jquery.bgiframe.js"></script>
    <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
    <style type="text/css">
        body { font-size: 62.5%; }
        label, input { display:block; }
        input.text { margin-bottom:12px; width:95%; padding: .4em; }
        fieldset { padding:0; border:0; margin-top:25px; }
        h1 { font-size: 1.2em; margin: .6em 0; }
        div#users-contain {  width: 350px; margin: 20px 0; }
        div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
        div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
        .ui-button { outline: 0; margin:0; padding: .4em 1em .5em; text-decoration:none; cursor:pointer; position: relative; text-align: center; }
        .ui-dialog .ui-state-highlight, .ui-dialog .ui-state-error { padding: .3em;  }


    </style>
    <head>
        <%
        DisplayFrame disp = new DisplayFrame("", "");
        disp.setCtxPath(request.getContextPath());
        out.println(disp.DisplayPreHead());

        %>
        <title>jQuery UI Dialog - Modal form</title>
        <link type="text/css" href="themes/base/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="ui/ui.core.js"></script>
        <script type="text/javascript" src="ui/ui.draggable.js"></script>
        <script type="text/javascript" src="ui/ui.resizable.js"></script>
        <script type="text/javascript" src="ui/ui.tabs.js"></script>
        <script type="text/javascript" src="ui/ui.dialog.js"></script>
        <script type="text/javascript" src="ui/ui.sortable.js"></script>
        <script type="text/javascript" src="ui/effects.core.js"></script>
        <script type="text/javascript" src="ui/effects.highlight.js"></script>
        <script type="text/javascript" src="external/bgiframe/jquery.bgiframe.js"></script>
        <link type="text/css" href="demos.css" rel="stylesheet" />
        <style type="text/css">
            body { font-size: 62.5%; }
            label, input { display:inline; }
            input.text { margin-bottom:12px; width:95%; padding: .4em; }
            fieldset { padding:0; border:0; margin-top:25px; }
            h1 { font-size: 1.2em; margin: .6em 0; }
            div#users-contain {  width: 350px; margin: 20px 0; }
            div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
            div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
            .ui-button { outline: 0; margin:0; padding: .4em 1em .5em; text-decoration:none;   cursor:pointer; position: relative; text-align: center; }
            .ui-dialog .ui-state-highlight, .ui-dialog .ui-state-error { padding: .3em;  }
        </style>
        <style type="text/css">
            .column { width: 170px; float: left; padding-bottom: 100px; }
            .portlet { margin: 0 1em 1em 0; }
            .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em; }
            .portlet-header .ui-icon { float: right; }
            .portlet-content { padding: 0.4em; }
            .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important; }
            .ui-sortable-placeholder * { visibility: hidden; }
        </style>

        <script type="text/javascript">
            $(function() {
                $("#tabs").tabs({
                    collapsible: true
                });
            });

            $(function() {

                var name = $("#name"),
                email = $("#email"),
                password = $("#password"),
                allFields = $([]).add(name).add(email).add(password),
                tips = $("#validateTips");

                function updateTips(t) {
                    tips.text(t).effect("highlight",{},1500);
                }

                function checkLength(o,n,min,max) {

                    if ( o.val().length > max || o.val().length < min ) {
                        o.addClass('ui-state-error');
                        updateTips("Length of " + n + " must be between "+min+" and "+max+".");
                        return false;
                    } else {
                        return true;
                    }

                }

                function checkRegexp(o,regexp,n) {

                    if ( !( regexp.test( o.val() ) ) ) {
                        o.addClass('ui-state-error');
                        updateTips(n);
                        return false;
                    } else {
                        return true;
                    }

                }

                $("#dialog").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    modal: true,
                    buttons: {
                        'Create an account': function() {
                            var bValid = true;
                            allFields.removeClass('ui-state-error');

                            bValid = bValid && checkLength(name,"username",3,16);
                            bValid = bValid && checkLength(email,"email",6,80);
                            bValid = bValid && checkLength(password,"password",5,16);

                            bValid = bValid && checkRegexp(name,/^[a-z]([0-9a-z_])+$/i,"Username may consist of a-z, 0-9, underscores, begin with a letter.");
                            // From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
                            bValid = bValid && checkRegexp(email,/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,"eg. ui@jquery.com");
                            bValid = bValid && checkRegexp(password,/^([0-9a-zA-Z])+$/,"Password field only allow : a-z 0-9");

                            if (bValid) {
                                $('#users tbody').append('<tr>' +
                                    '<td>' + name.val() + '</td>' +
                                    '<td>' + email.val() + '</td>' +
                                    '<td>' + password.val() + '</td>' +
                                    '</tr>');
                                $(this).dialog('close');
                            }
                        },
                        Cancel: function() {
                            $(this).dialog('close');
                        }
                    },
                    close: function() {
                        allFields.val('').removeClass('ui-state-error');
                    }
                });



                $('#create-user').click(function() {
                    $('#dialog').dialog('open');
                })
                .hover(
                function(){
                    $(this).addClass("ui-state-hover");
                },
                function(){
                    $(this).removeClass("ui-state-hover");
                }
            ).mousedown(function(){
                    $(this).addClass("ui-state-active");
                })
                .mouseup(function(){
                    $(this).removeClass("ui-state-active");
                });

            });
            $(function() {
                $(".column").sortable({
                    connectWith: '.column'
                });

                $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                .find(".portlet-header")
                .addClass("ui-widget-header ui-corner-all")
                .end()
                .find(".portlet-content");

                $(".portlet-header .ui-icon").click(function() {
                    $(this).toggleClass("ui-icon-minusthick");
                    $(this).parents(".portlet:first").find(".portlet-content").toggle();
                });

                $(".column").disableSelection();
            });

            function getTableSet()
            {
                 alert(document.getElementById('type').value);
                if(document.getElementById('type').value=="Tables")
                {
                    document.getElementById('tableList').style.display='';
                    document.getElementById('viewList').style.display='none';
                }

                if(document.getElementById('type').value=="Views")
                {
                    document.getElementById('tableList').style.display='none';
                    document.getElementById('viewList').style.display='';
                }
                if(document.getElementById('type').value=="none")
                    {
                        document.getElementById('tableList').style.display='none';
                        document.getElementById('viewList').style.display='none';
                    }

            }

            function getconnection()
            {
                document.myForm.action="pbSaveTables.jsp";
                document.myForm.submit();
            }


            function testconnection()
            {
                
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


        </script>
        <%
        out.println(disp.DisplayPostHead());
        %>
    </head>
    <body>
        <%
        out.println(disp.DisplayPreBody());
        out.println(disp.DisplayPreRow());
        %>





                    <div class="demo-1">
                        <div id="dialog" style="background-color:#66CCFF;width:700px" class="demo-1">
            <form name="myForm" method="post">
                <br><br><br><br><br>
                <center>

                    <table style="width:37%">

                        <tr>
                            <td class="myHead" style="width:58%">
                                Type
                            </td>
                            <td style="width:58%">
                                <select id="type" onchange="getTableSet()" style="width:146px">
                                    <option value="none">----select----</option>
                                    <option value="Tables">Tables</option>
                                    <option value="Views">Views</option>
                                </select>
                    </td></tr>
                    <tr>
                        <td>
                            Search
                        </td>
                        <td>
                            <input type="text" id="search" name="search">
                        </td>
                    </tr>
                    </table>
                    <div id="tableList" style="display:none;width:700px;border-width:thick">
                        <table style="width:37%">
                            <tr>
                                <td class="myHead" style="width:58%">
                                 
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div id="viewList" style="display:none;width:700px">
                        <table style="width:37%">  <tr>
                                <td class="myHead" style="width:58%">
                                    
                                </td>
                                
                        </tr></table>
                    </div>

                    <table>
                        <tr>
                            <td><input type="button" class="btn" value="Save" onclick="SaveTables()"></td>
                        </tr>
                    </table>


                </center>
            </form>
        </div>



                   





        <%


        out.println(disp.DisplayPostRow());
        out.println(disp.DisplayPostBody());
        %>
    </body>
</html>

