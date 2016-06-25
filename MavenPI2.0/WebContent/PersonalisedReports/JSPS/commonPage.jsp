<%--
/* commonPage.jsp *
 * Created on May 7, 2009, 6:56 PM
 *@author : K.Santhosh Kumar
 * 
 */
--%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/extremecomponents.tld" prefix="ec" %>
<%@ page import="java.util.*" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>  
<%@ page import="prg.util.TableUtils"%>
       

        <%  //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
             TableUtils TableUtilsRef=null;  
            ArrayList currentDisplayedRecords=null;
            String[] columnsVisibility;
            String[] columnsNames;
            String[] columnURLS;
            String[] columnTypes;
            String[] tableColumnsNames;
            int[] columnWidths; 
          
            boolean[] columnSortable;
            boolean[] columnFilterble;
             //  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----TableUtilsRef---"+TableUtilsRef);
            TableUtilsRef=(TableUtils)session.getAttribute("TableUtilsBean");
           
          //  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----TableUtilsRef---"+TableUtilsRef);
            
            tableColumnsNames=TableUtilsRef.getTableColumnsNames();
            columnsNames=TableUtilsRef.getColumnNames();
            columnsVisibility=TableUtilsRef.getColumnsVisibility(); 
          //  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----columnsVisibility.length------"+columnsVisibility.length);
            columnWidths=TableUtilsRef.getColumnWidths();          
            columnSortable=TableUtilsRef.getColumnSortable();
            columnFilterble=TableUtilsRef.getColumnFilterble();
            columnURLS=TableUtilsRef.getColumnURLS();
            columnTypes=TableUtilsRef.getColumnTypes();
            
            if(request.getParameter(columnsNames[0])!=null){
              columnsVisibility=new String[TableUtilsRef.getColumnCount()];
              for(int colNum=0;colNum<columnsVisibility.length;colNum++){
                        columnsVisibility[colNum]=request.getParameter(columnsNames[colNum]);
                    }   
                }  
            TableUtilsRef.setColumnsVisibility(columnsVisibility);          
            pageContext.setAttribute(TableUtilsRef.getItems(),TableUtilsRef.getTotalRecords());    
             TableUtilsRef.setImagePath(request.getContextPath()+"/images/extreme/*.gif");
            ArrayList alist=(ArrayList)pageContext.getAttribute("Records");
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----TableUtilsRef.getItems()---- "+TableUtilsRef.getItems()+" ------TableUtilsRef.getTotalRecords()------ "+TableUtilsRef.getTotalRecords());
           // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----alist in common page--------"+alist);
            HashMap hmap;
           // TableUtilsRef.setAction("./Sample2.jsp");
           //TableUtilsRef.setPageContext(pageContext);  
       // out.println(pageContext.getAttribute(TableUtilsRef.getItems()));
           // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------TableUtilsRef.getImagePath()----------"+TableUtilsRef.getImagePath());
            %>    
         
         <ec:table border="1" action='<%=TableUtilsRef.getAction()+""%>'  var='<%=TableUtilsRef.getVar()+""%>'  items='<%=TableUtilsRef.getItems()+""%>' title='<%=TableUtilsRef.getTitle()+""%>' sortable='<%=TableUtilsRef.getTableSortable()+""%>'
                    showPagination='<%=TableUtilsRef.getShowPagination()+""%>' showExports='<%=TableUtilsRef.getShowExports()+""%>' showTitle='<%=TableUtilsRef.getShowTitle()+""%>'
                    maxRowsDisplayed='<%=TableUtilsRef.getTotalRecordsSize()+""%>' showStatusBar='<%=TableUtilsRef.getShowStatusBar()+""%>' showTooltips='<%=TableUtilsRef.getShowTooltips()+""%>'
                    imagePath='<%=TableUtilsRef.getImagePath()+""%>' filterRowsDisplayed='<%=TableUtilsRef.getFilterRowsDisplayed()+""%>' medianRowsDisplayed='<%=TableUtilsRef.getMedianRowsDisplayed()+""%>'
                    filterable='<%=TableUtilsRef.getFilterable()+""%>' width='<%=TableUtilsRef.getWidth()+"%"+""%>' rowsDisplayed='<%=TableUtilsRef.getRowsDisplayed()+""%>' >
            <% if(TableUtilsRef.getShowExports()){%>
             <ec:exportXls fileName='<%=TableUtilsRef.getFileName()+".xls"%>'  />
             <ec:exportPdf fileName='<%=TableUtilsRef.getFileName()+".pdf"%>' headerTitle="Title" headerColor="white"/>
             <%-- <ec:exportCsv  fileName="<%=TableUtilsRef.getFileName()+".doc"%>"/> --%>
             <%}            
            %>
             <ec:row highlightRow="true" >                  
             <%  // if(counter!=0){
                  //hmap=(HashMap)alist.get(counter-1);
                  //}else{
                  //hmap=new HashMap();
                  //}                 
                 //pageContext.setAttribute("counter",counter);
                  for(int index=0;index<TableUtilsRef.getTableColumnsNames().length;index++){
                  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----index----- "+index);
                  
                  if(columnsVisibility[index].equalsIgnoreCase("show")){
                     if(columnTypes[index].equalsIgnoreCase("checkbox")){
                         pageContext.setAttribute("columnsNames",columnsNames[index]);
                        // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(columnsNames[index]);%> 
                         <ec:column    property="<%=tableColumnsNames[index]%>" cell="date" width='<%=columnWidths[index]+""%>' format="d MMM,yyyy"   title='<%=columnsNames[index].replace("_"," ")+""%>' filterable='<%=columnFilterble[index]+""%>'  sortable='<%=columnSortable[index]+""%>'>
                          <%--<input type="checkbox" name="chk1" value="<%=hmap.get(columnsNames[index])%>">  --%>                                        
                         </ec:column>                   
                 <%}else if(columnTypes[index].equalsIgnoreCase("url")){
                         //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Hyper Link");%>
                         <ec:column  property="<%=tableColumnsNames[index]%>"  cell="date" width='<%=columnWidths[index]+""%>' format="d MMM,yyyy"  title='<%=columnsNames[index].replace("_"," ")+""%>' filterable='<%=columnFilterble[index]+""%>'  sortable='<%=columnSortable[index]+""%>'>
                          <%--<a href="<%=columnURLS[index]%><%=hmap.get(columnsNames[index])%>"><%=hmap.get(columnsNames[index])%></a>--%>
                         </ec:column>     
                 <%}else if(columnTypes[index].equalsIgnoreCase("radio")){
                       //  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("radio Button");%>
                        <ec:column property="<%=tableColumnsNames[index]%>"  cell="date" width='<%=columnWidths[index]+""%>' format="d MMM,yyyy"  title='<%=columnsNames[index].replace("_"," ")+""%>' filterable='<%=columnFilterble[index]+""%>'  sortable='<%=columnSortable[index]+""%>'>
                        <%--<input type="radio" name="rad1" value="<%=hmap.get(columnsNames[index])%>">--%>
                        </ec:column>                           
                 <%}else {
                        // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Normal text");%>
                        <ec:column  property="<%=tableColumnsNames[index]%>" cell="date" width='<%=columnWidths[index]+""%>' format="###,###,##0.00" title='<%=columnsNames[index].replace("_"," ")+""%>' filterable='<%=columnFilterble[index]+""%>'  sortable='<%=columnSortable[index]+""%>'>
                        </ec:column>  
                 <%}
                    }
                  }                   
                  //pageContext.setAttribute("counter",counter);%>                   
             </ec:row>
           
             <% //counter++; 
              if(currentDisplayedRecords==null){
                 currentDisplayedRecords=new ArrayList();
                 }
                 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------------- "+TableUtilsRef.getPageContext().getAttribute(TableUtilsRef.getVar()));
                // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---TableUtilsRef.getPageContext()----- "+TableUtilsRef.getPageContext());
                 //currentDisplayedRecords.add(TableUtilsRef.getPageContext().getAttribute(TableUtilsRef.getVar()));
                 currentDisplayedRecords.add(pageContext.getAttribute(TableUtilsRef.getVar()));
             //currentDisplayedRecords.add(TableUtilsRef.getPageContext().getAttribute(TableUtilsRef.getVar()));%>
        </ec:table> 
             <% 
                 currentDisplayedRecords.remove(0);
                 TableUtilsRef.setCurrentDisplayedRecords(currentDisplayedRecords); 
                 if(session.getAttribute("TableUtilsBean")!=null){
                     session.removeAttribute("TableUtilsBean");
                     session.setAttribute("TableUtilsBean",TableUtilsRef); 
                 }
                 else{
                     session.setAttribute("TableUtilsBean",TableUtilsRef); 
                 }
                 
                 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----currentDisplayedRecords---- "+currentDisplayedRecords);
             %>
            <!--  <form action="hideShowColumns.jsp" method="post">
                  <input type="Submit" name="btnHide" class="btn" id="btnHide" value="Hide/Show Columns"/>
             </form> -->
                         
   

