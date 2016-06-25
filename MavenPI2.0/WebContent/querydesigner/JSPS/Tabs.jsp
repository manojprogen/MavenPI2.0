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
       // DisplayFrame disp = new DisplayFrame("", "");
      //  disp.setCtxPath(request.getContextPath());
      //  out.println(disp.DisplayPreHead());

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

            function getdatabase()
            {
                // alert(document.getElementById('dbname').value);
                if(document.getElementById('dbname').value=="oracle")
                {
                    document.getElementById('oraclediv').style.display='';
                    document.getElementById('exceldiv').style.display='none';
                }

                if(document.getElementById('dbname').value=="excel")
                {
                    document.getElementById('oraclediv').style.display='none';
                    document.getElementById('exceldiv').style.display='';
                }
                if(document.getElementById('dbname').value=="none")
                    {
                        document.getElementById('oraclediv').style.display='none';
                        document.getElementById('exceldiv').style.display='none';
                    }

            }

            function getconnection()
            {
                document.myForm.action="pbCheckConnection.jsp";
                document.myForm.submit();
            }


        </script>
        <%
       // out.println(disp.DisplayPostHead());
        %>
    </head>
    <body>
        <%
       // out.println(disp.DisplayPreBody());
       // out.println(disp.DisplayPreRow());
        %>

        <div class="demo">

            <div id="tabs">
                <ul>
                    <li><a href="#tabs-1">Database</a></li>
                    <li><a href="#tabs-2">Project</a></li>
                    <li><a href="#tabs-3">Query</a></li>
                    <li><a href="#tabs-4">Report</a></li>
                </ul>
                <div id="tabs-1">
                    <div class="demo-1">

                       <!-- <div id="dialog" title="Create new user">
                            <p id="validateTips">All form fields are required.</p>

                            <form>
                                <fieldset>
                                    <label for="name">Name*</label>
                                    <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
                                    <label for="email">Email*</label>
                                    <input type="text" name="email" id="email" value="" class="text ui-widget-content ui-corner-all" />
                                    <label for="password">Password*</label>
                                    <input type="password" name="password" id="password" value=""  class="text ui-widget-content ui-corner-all" />
                                </fieldset>
                            </form>
                        </div>-->

                        <!--Connections div-->

                        <div id="dialog" style="background-color:#66CCFF;width:700px" class="demo-1">
            <form name="myForm" method="post">
                <br><br><br><br><br>
                <center>

                    <table style="width:37%">
                        <tr>
                            <td class="myHead" style="width:58%">
                                Connection Name
                            </td>
                            <td  style="width:58%">
                                <input type="text" name="connectionname">
                            </td>
                        </tr>
                        <tr>
                            <td class="myHead" style="width:58%">
                                Database
                            </td>
                            <td style="width:58%">
                                <select id="dbname" onchange="getdatabase()" style="width:146px">
                                    <option value="none">----select----</option>
                                    <option value="oracle">Oracle</option>
                                    <option value="excel">Excel</option>
                                </select>
                    </td></tr></table>
                    <div id="oraclediv" style="display:none;width:700px;border-width:thick">
                        <table style="width:37%">
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Username
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="username">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Password
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="password">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Server
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="server" value="localhost">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    ServiceId
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="Serviceid">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Port
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="Port" value="1521">
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div id="exceldiv" style="display:none;width:700px">
                        <table style="width:37%">  <tr>
                                <td class="myHead" style="width:58%">
                                    UserName
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="excelUserName">
                                </td>
                            </tr>
                            <tr>
                                <td class="myHead" style="width:58%">
                                    Password
                                </td>
                                <td style="width:58%">
                                    <input type="text" name="excelPassword">
                                </td>
                        </tr></table>
                    </div>

                    <table>
                        <tr>
                            <td><input type="button" class="btn" value="connect" onclick="getconnection()"></td>
                        </tr>
                    </table>


                </center>
            </form>
        </div>



                        <button id="create-user" class="ui-button ui-state-default ui-corner-all">Create Connection</button>

                    </div><!-- End demo -->

                </div>
                <div id="tabs-2">
                    <div id="users-contain" class="ui-widget">
                        <table id="users" class="ui-widget ui-widget-content">
                            <thead>
                                <tr class="ui-widget-header ">
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Password</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>John Doe</td>
                                    <td>john.doe@example.com</td>
                                    <td>johndoe1</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="tabs-3">
                    <p><strong>Click this tab again to close the content pane.</strong></p>
                    <p>Duis cursus. Maecenas ligula eros, blandit nec, pharetra at, semper at, magna. Nullam ac lacus. Nulla facilisi. Praesent viverra justo vitae neque. Praesent blandit adipiscing velit. Suspendisse potenti. Donec mattis, pede vel pharetra blandit, magna ligula faucibus eros, id euismod lacus dolor eget odio. Nam scelerisque. Donec non libero sed nulla mattis commodo. Ut sagittis. Donec nisi lectus, feugiat porttitor, tempor ac, tempor vitae, pede. Aenean vehicula velit eu tellus interdum rutrum. Maecenas commodo. Pellentesque nec elit. Fusce in lacus. Vivamus a libero vitae lectus hendrerit hendrerit.</p>
                </div>

                <div id="tabs-4"><!--sortable-->
                    <table>
                        <tr>
                            <td>
                                <div class="column" style="width:500px">

                                    <div class="portlet" >
                                        <div class="portlet-header">Parameters Region1</div>
                                        <table width="450px" height="250px" ><tr><td valign="Top">
                                                    <BR><BR>
                                                    Proin elit arcu, rutrum commodo, vehicula tempus, commodo a, risus. Curabitur nec arcu. Donec sollicitudin mi sit amet mauris. Nam elementum quam ullamcorper ante. Etiam aliquet massa et lorem. Mauris dapibus lacus auctor risus. Aenean tempor ullamcorper leo. Vivamus sed magna quis ligula eleifend adipiscing. Duis orci. Aliquam sodales tortor vitae ipsum. Aliquam nulla.

                                            </td></tr>
                                        </table>
                                </div>
                                 <div class="portlet" >
                                        <div class="portlet-header">Parameters Region2</div>
                                        <table width="450px" height="250px" ><tr><td valign="Top">
                                                    <BR><BR>
                                                    Proin elit arcu, rutrum commodo, vehicula tempus, commodo a, risus. Curabitur nec arcu. Donec sollicitudin mi sit amet mauris. Nam elementum quam ullamcorper ante. Etiam aliquet massa et lorem. Mauris dapibus lacus auctor risus. Aenean tempor ullamcorper leo. Vivamus sed magna quis ligula eleifend adipiscing. Duis orci. Aliquam sodales tortor vitae ipsum. Aliquam nulla.

                                            </td></tr>
                                        </table>
                                </div></div>
                                <div class="column" style="width:500px;">
                                    <div class="portlet" >
                                        <div class="portlet-header">Parameters Region3</div>
                                        <table width="450px" height="250px"><tr><td valign="Top">
                                                    <BR><BR>
                                                    Proin elit arcu, rutrum commodo, vehicula tempus, commodo a, risus. Curabitur nec arcu. Donec sollicitudin mi sit amet mauris. Nam elementum quam ullamcorper ante. Etiam aliquet massa et lorem. Mauris dapibus lacus auctor risus. Aenean tempor ullamcorper leo. Vivamus sed magna quis ligula eleifend adipiscing. Duis orci. Aliquam sodales tortor vitae ipsum. Aliquam nulla.
                                            </td></tr>
                                        </table>
                                    </div>

                                    <div class="portlet" >
                                        <div class="portlet-header">Parameters Region4</div>
                                        <table width="450px" height="250px"><tr><td valign="Top">
                                                    <BR><BR>
                                                    Proin elit arcu, rutrum commodo, vehicula tempus, commodo a, risus. Curabitur nec arcu. Donec sollicitudin mi sit amet mauris. Nam elementum quam ullamcorper ante. Etiam aliquet massa et lorem. Mauris dapibus lacus auctor risus. Aenean tempor ullamcorper leo. Vivamus sed magna quis ligula eleifend adipiscing. Duis orci. Aliquam sodales tortor vitae ipsum. Aliquam nulla.
                                            </td></tr>
                                        </table>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>

            </div>

        </div><!-- End demo -->


         
        <%


       // out.println(disp.DisplayPostRow());
       // out.println(disp.DisplayPostBody());
        %>
    </body>
</html>

