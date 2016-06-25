<!doctype html>
<%@page import="java.util.Iterator,java.util.Collection,java.util.Set,java.util.HashMap,prg.business.group.BusinessTablePaths"%>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html lang="en">
<head>
	
	
		
</head>
<body>
<%
BusinessTablePaths paths=new BusinessTablePaths();
String bussTableId="12";
HashMap hpaths=new HashMap();
hpaths=paths.getpathFromBusinessTable(bussTableId);


if(hpaths!=null){
Set s=hpaths.keySet();
Collection c=hpaths.values();

Iterator i=s.iterator();
String keyarr="";
 while(i.hasNext()){
     keyarr+="~"+i.next();
 }

if(keyarr.indexOf("~")>=0){
    keyarr=keyarr.substring(1);
    }


Iterator j=c.iterator();
String valarr="";
 while(j.hasNext()){
     valarr+="~"+j.next();
 }

if(valarr.indexOf("~")>=0){
    valarr=valarr.substring(1);
    }

String keys[]=keyarr.split("~");
String values[]=valarr.split("~");
String totalPaths[]=new String[values.length];

for(int k=0;k<keys.length;k++){
    totalPaths[k]=keys[k];
    if(!(values[k].equals(""))){
    
       totalPaths[k]+=","+values[k];
       }
  
    }
paths.AddPathMaps(bussTableId, keys,totalPaths);
}
%>
</body>
</html>
