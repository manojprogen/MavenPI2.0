function createCSVOfreport(ctxPath,reportId,userId)
{
    document.getElementById('loadingmetadata').style.display="";
    $.post(ctxPath+'/reportViewer.do?reportBy=getCSV&reportId='+reportId+'&userId='+userId,
        function(data){
    document.getElementById('loadingmetadata').style.display="none";
            alert("Enabled for XTend!");
        });
}