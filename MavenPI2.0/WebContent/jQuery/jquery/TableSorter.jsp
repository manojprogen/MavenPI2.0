<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-us">
    <head>
        <title>jQuery plugin: Tablesorter 2.0 - Pager plugin</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/tablesorter/jquery.columnfilters.js"></script>
        


        <script type="text/javascript">
            
            $(document).ready(function() {
                $('#tablesorter').columnFilters();
                $("#tablesorter")
                .tablesorter({widthFixed: true, widgets: ['zebra']})
                .tablesorterPager({container: $("#pager")});
            });
        </script>


        
    </head>
    <body>
        <br>
            <div id="pager"  align="right" style="width:50%">
            <form>
                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                <input type="text" class="pagedisplay"/>
                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                <select class="pagesize">
                    <option value="5">5</option>
                    <option selected value="10" >10</option>
                </select>
            </form>
        </div>

        <div class="drag" id="main" style="width:25%;">


            <table cellspacing="1" class="tablesorter" id="tablesorter" width="100px">
                <thead>
                    <tr id="tabrow">
                        <th id="fisCol">Last Name</th>
                        <th id="secCol">First Name</th>
                       
                    </tr>
                </thead>
                <tfoot >


                </tfoot>
                <tbody>
                    <tr id="tabrow">
                        <td id="fisCol">Smith</td>
                        <td id="secCol">John</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">Bach</td>
                        <td id="secCol">Frank</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">HSmith</td>
                        <td id="secCol">John</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">DBach</td>
                        <td id="secCol">Frank</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">NSmith</td>
                        <td id="secCol">John</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">PBach</td>
                        <td id="secCol">Frank</td>
                       
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">Smith</td>
                        <td id="secCol">John</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">Bach</td>
                        <td id="secCol">Frank</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">HSmith</td>
                        <td id="secCol">John</td>
                       
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">DBach</td>
                        <td id="secCol">Frank</td>
                        
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">NSmith</td>
                        <td id="secCol">John</td>
                       
                    </tr>
                    <tr id="tabrow">
                        <td id="fisCol">PBach</td>
                        <td id="secCol">Frank</td>
                        
                    </tr>


                </tbody>
            </table>


        </div>
        




    </body>
</html>

