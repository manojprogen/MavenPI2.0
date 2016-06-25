$(document).ready(function() {

                $("#addNewCustomerInstructionsImg").click(function(ev) {
                    toggleAddCustomerInstructions();
                });

                $("#addNewCustomerInstructionsLink").click(function(ev) {
                    ev.preventDefault();
                    toggleAddCustomerInstructions();
                });

                $("#addNewCustomerInstructionsClose").click(function(ev) {
                    ev.preventDefault();
                    toggleAddCustomerInstructions();
                });

               // $(".customerRow").contextMenu({ menu: 'myMenu' }, function(action, el, pos) { contextMenuWork(action, el, pos); });

                $(".openmenu").contextMenu({ menu: 'connListMenu', leftButton: true }, function(action, el, pos) {

                    contextMenuWork(action, el.parent("tr"), pos); });


                $(".connMenu").contextMenu({ menu: 'tabListMenu', leftButton: true }, function(action, el, pos) {
                    $(el).attr('id')
                    contextMenuWork1(action, el.parent("li"), pos);
                });


                  $("#myList").treeview({
                    animated:"slow",
                    persist: "cookie"
                });


            });

            function confirm(message) {
                $("#confirm").modal({
                    close: true,
                    overlayId: 'confirmModalOverlay',
                    containerId: 'confirmModalContainer',
                    onClose: modalOnClose,
                    onShow: function modalShow(dialog) {
                        dialog.overlay.fadeIn('slow', function() {
                            dialog.container.fadeIn('fast', function() {
                                dialog.data.hide().slideDown('slow');
                            });
                        });

                        dialog.data.find(".confirmmessage").append(message);

                        // Yes button clicked
                        dialog.data.find("#ButtonYes").click(function(ev) {
                            ev.preventDefault();
                            $.modal.close();
                            alert('The customer with id ' + $("#HiddenFieldRowId").val() + ' would of been deleted.');
                            //$("#ButtonDeleteCustomer").click();
                        });
                    }
                })
            }

            function modalOpenAddCustomer(dialog) {
                dialog.overlay.fadeIn('fast', function() {
                    dialog.container.fadeIn('fast', function() {
                        dialog.data.hide().slideDown('slow');
                    });
                });

                dialog.data.find(".modalheader span").html("Add New Customer");

                // if the user clicks "yes"
                dialog.data.find("#ButtonAddCustomer").click(function(ev) {
                    ev.preventDefault();

                    //Perfom validation
                    if (Page_ClientValidate("addCustomer")) {
                        $.modal.close();
                        $("#ButtonHiddenAddCustomer").click();
                    }

                });
            }

            function toggleAddCustomerInstructions() {
                $("#addNewCustomerFields").toggle();
                $("#addNewCustomerInstructions").toggle()
            }

            function modalOnClose(dialog) {
                dialog.data.fadeOut('slow', function() {
                    dialog.container.slideUp('slow', function() {
                        dialog.overlay.fadeOut('slow', function() {
                            $.modal.close(); // must call this to have SimpleModal
                            // re-insert the data correctly and
                            // clean up the dialog elements
                        });
                    });
                });
            }

            function contextMenuWork(action, el, pos) {

                switch (action) {
                    case "delete":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "insert":
                        {
                            /*  $("#TextBoxContactName").val("");
                        $("#TextBoxContactTitle").val("");
                        $("#TextBoxCountry").val("");
                        $("#TextBoxPhone").val("");

                        $("#addNewCustomer").modal({
                            close: true,
                            onOpen: modalOpenAddCustomer,
                            onClose: modalOnClose,
                            persist: true,
                            containerCss: ({ width: "500px", height: "275px", marginLeft: "-250px" })
                        });*/

                            createConnection();
                            break;
                        }

                    case "edit":
                        {
                            alert(
                            'Action: ' + action + '\n\n' +
                                'Element ID: ' + $(el).attr('id') + '\n\n' +
                                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                        );
                        }
                }
            }

            function contextMenuWork1(action, el, pos) {

                switch (action) {
                    case "delete":
                        {
                            var msg = "Delete " + $(el).find("#contactname").text() + "?";
                            $("#HiddenFieldRowId").val($(el).find("#customerid").text());
                            confirm(msg);
                            break;
                        }
                    case "addTables":
                        {

                            //   alert($(el).attr('id'));

                            /*  $("#TextBoxContactName").val("");
                        $("#TextBoxContactTitle").val("");
                        $("#TextBoxCountry").val("");
                        $("#TextBoxPhone").val("");

                        $("#addNewCustomer").modal({
                            close: true,
                            onOpen: modalOpenAddCustomer,
                            onClose: modalOnClose,
                            persist: true,
                            containerCss: ({ width: "500px", height: "275px", marginLeft: "-250px" })
                        });*/

                            tableList($(el).attr('id'));
                            break;
                        }

                    case "edit":
                        {
                            alert(
                            'Action: ' + action + '\n\n' +
                                'Element ID: ' + $(el).attr('id') + '\n\n' +
                                'X: ' + pos.x + '  Y: ' + pos.y + ' (relative to element)\n\n' +
                                'X: ' + pos.docX + '  Y: ' + pos.docY + ' (relative to document)'
                        );
                        }
                }
            }

$(function() {
                $("#abcd").draggable();
            });

            $(document).ready(function() {
                $('table#filterTable2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});

            });
            $(document).ready(function() {
                $('table#filterview2').columnFilters({alternateRowClassNames:['rowa','rowb'], excludeColumns:[0]});

            });
           function tableList(span){
                //document.getElementById("activeConnection").value=(span.innerHTML);
                // document.getElementById('type').style.display='block';
                // document.getElementById('fade').style.display='block';
                //
                 var frameObj = document.getElementById('dataDisptab');
                //alert(document.getElementById("conList").innerHTML);
                //window.open("TableList.jsp?connection="+span, "window.optablelist", "status=1,width=350,height=375");
                 var source="querydesigner/JSPS/TableList.jsp?connection="+span;
                frameObj.src=source;
                document.getElementById('dataDisptab').style.display='block';
                //
                //
                //alert('list');
                //document.getElementById("type").style.display='';
            }

              function getData(tableIds){
                var frameObj=document.getElementById("dataDisp");
                var source="querydesigner/JSPS/pbViewTable.jsp?tableIds="+tableIds;
                frameObj.src=source;
            }
            function TableList1(span){
                document.getElementById("activeConnection").value=(span.innerHTML);
            }


            function createConnection(){

                document.getElementById('connection').style.display='block';
                document.getElementById('fade').style.display='block';
                //alert('list');
                //document.getElementById("type").style.display='';
            }

            function getTableSet()
            {
                // alert(document.getElementById('tvtype').value);
                if(document.getElementById('tvtype').value=="Tables")
                {
                    document.getElementById('tableList').style.display='';
                    document.getElementById('viewList').style.display='none';
                }

                if(document.getElementById('tvtype').value=="Views")
                {
                    document.getElementById('tableList').style.display='none';
                    document.getElementById('viewList').style.display='';
                }
                if(document.getElementById('tvtype').value=="none")
                {
                    document.getElementById('tableList').style.display='none';
                    document.getElementById('viewList').style.display='none';
                }

            }
             function saveTables(type)
            { // alert('save tables'+type);
                document.myForm.action="querydesigner/JSPS/pbSaveTables.jsp";
                //alert( document.myForm.action);
                document.myForm.submit();
            }
            function temp(){
                // alert('Hello');
            }

       
            function getconnection()
            {
                document.myForm1.action="querydesigner/JSPS/pbCheckConnection.jsp";
                document.myForm1.submit();
            }

            function getdatabase()
            {
                // alert(document.getElementById('dbname').value);
                if(document.getElementById('dbname').value=="oracle")
                {
                    document.getElementById('oraclediv').style.display='';
                    document.getElementById('exceldiv').style.display='none';
                    document.getElementById('dbcode').value = '1';
                    //  alert("dbcode-->"+document.getElementById('dbcode').value);
                }

                if(document.getElementById('dbname').value=="excel")
                {
                    document.getElementById('oraclediv').style.display='none';
                    document.getElementById('exceldiv').style.display='';
                    document.getElementById('dbcode').value = '2';
                    //  alert("dbcode-->"+document.getElementById('dbcode').value);
                }
                if(document.getElementById('dbname').value=="none")
                {
                    document.getElementById('oraclediv').style.display='none';
                    document.getElementById('exceldiv').style.display='none';
                }

            }

            function cancelFade()
            {
                document.getElementById('fade').style.display='none';
                document.getElementById('type').style.display='none';
                document.getElementById('connection').style.display='none';
            }


            function testconnection()
            {
                var un = document.getElementById("username").value;
                var pwd = document.getElementById("password").value;
                var s = document.getElementById("server").value;
                var sid = document.getElementById("Serviceid").value;
                var p = document.getElementById("Port").value;
                var dbname = document.getElementById("dbname").value;
                var dsn = document.getElementById("exceldsn").value;

                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                ctxPath=document.getElementById("h").value;
                if(dbname=='oracle'){
                    var url=ctxPath+"/TestConnection";
                    url=url+"?un="+un+"&pwd="+pwd+"&s="+s+"&sid="+sid+"&p="+p;
                }
                else if(dbname=='excel')
                {
                    var url=ctxPath+"/TestExcelConnection";
                    url=url+"?dsn="+dsn;

                }
                // alert(url)
                // var payload = "q="+str+"&id="+id;
                //alert(url);
                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);


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

                    var output1=xmlHttp.responseText
                    alert(output1);


                }
            }

            function saveNext()
            {
                document.getElementById('connection').style.display='none';
                document.getElementById('fade').style.display='none';
                var connectionname = document.getElementById('connectionname').value;
                var dbc = document.getElementById('dbcode').value;
                var un = document.getElementById('username').value;
                var pwd = document.getElementById('password').value;
                var ser = document.getElementById('server').value;
                var sid = document.getElementById('Serviceid').value;
                var port = document.getElementById('Port').value;
                window.open("querydesigner/JSPS/pbSaveNextCheck.jsp?dbcode="+dbc+"&username="+un+"&password="+pwd+"&server="+ser+"&Serviceid="+sid+"&Port="+port+"&connectionname="+connectionname, "window.optablelist", "status=1,width=350,height=375");
                //document.myForm1.action="<%=request.getContextPath()%>/pbSaveNextCheck.jsp";
                //document.myForm1.submit();
            }
            //added by bharu on 19/08/09
            function colDelete(tableId){
                alert(tableId);
                document.getElementById("tabledeleteId").value=tableId;
                var i=0;
                var obj=document.myForm2.chk2;
                // alert(obj.length)
                for(var j=0;j<obj.length;j++)
                {
                    if(document.myForm2.chk2[j].checked==true)
                    {

                        i++;
                    }
                }

                //alert(i)
                if(i>0)
                {

                    //alert('Unselected Columns Are Deleted');
                    document.myForm2.action="querydesigner/JSPS/pbDeleteTableColumns.jsp";
                    // alert("url==="+document.myForm2.action);
                    document.myForm2.submit();
                }
                else
                {

                    alert('Unselected Columns Are Deleted please select atleast one column');
                    //document.myForm2.action="pbDeleteTableColumns.jsp?tableId="+tableId;
                    // alert("url==="+document.myForm2.action);
                    // document.myForm2.submit();

                }
            }

             function refreshparent()
            {
                document.myForm2.action="getAllTables.do";
                document.myForm2.submit();
            }
