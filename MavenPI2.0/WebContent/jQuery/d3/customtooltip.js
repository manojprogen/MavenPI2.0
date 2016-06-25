function CustomTooltip(tooltipId, width){
//	var tooltipId = tooltipId;
        if(tooltipId==='my_tooltip1')
        {
            $("#Hchart1").append("<div class='tooltip' id='"+tooltipId+"' style='display:block'></div>");
        }
        else
        {
	$("body").append("<div class='tooltip' id='"+tooltipId+"' style='display:none; z-index:12;'></div>");
        }
	
	if(width){
            
              		$("#"+tooltipId).css("width", width);
	}
	
	hideTooltip();
	
	function showTooltip(content, event){
		$("#"+tooltipId).html(content);
		$("#"+tooltipId).show();
		updatePosition(event);
	}
	function showTooltip2(content, event){
		$("#"+tooltipId).html(content);
		$("#"+tooltipId).show();
		updatePosition2(event);
	}
	
	function hideTooltip(){
		$("#"+tooltipId).hide();
	}
	
	function updatePosition2(event){
                 var left;
                   var top;
                 if(document.getElementById("quickTrend")!=null){
                      var element=parent.document.getElementById("quickTrend").parentNode;

                left=element.offsetLeft - element.scrollLeft + element.clientLeft;
                top=element.offsetTop - element.scrollTop + element.clientTop;
                 }
//                alert(left+":"+top);
		var ttid = "#"+tooltipId;
                var xOffset = 0;
		var yOffset = 10;
		
		 var ttw = $(ttid).width();
		 var tth = $(ttid).height();
		 var wscrY = $(window).scrollTop();
		 var wscrX = $(window).scrollLeft();
		 var curX = (document.all) ? event.clientX + wscrX : event.screenX;
		 var curY = (document.all) ? event.clientY + wscrY : event.screenY;
		 var ttleft = ((curX - wscrX + xOffset*2 + ttw) > $(window).width()) ? curX - ttw - xOffset*2 : curX + xOffset;
		 if (ttleft < wscrX + xOffset){
		 	ttleft = wscrX + xOffset;
		 } 
		 var tttop = ((curY - wscrY + yOffset + tth) > $(window).height()) ? curY - tth - yOffset : curY + yOffset;
		 if (tttop < wscrY + yOffset){
		 	tttop = curY + yOffset;
		 } 
                  if(document.getElementById("quickTrend")!=null){
                 tttop=tttop-top;
                 ttleft=ttleft-left-100;
                   }
		 $(ttid).css('top', tttop + 'px').css('left', ttleft + 'px');
	}
	function updatePosition(event){
               
                 var left;
                   var top;
                 if(document.getElementById("quickTrend")!=null){
                      var element=parent.document.getElementById("quickTrend").parentNode;

                left=element.offsetLeft - element.scrollLeft + element.clientLeft;
                top=element.offsetTop - element.scrollTop + element.clientTop;
                 }
//                alert(left+":"+top);
		var ttid = "#"+tooltipId;
                var xOffset = 20;
		var yOffset = 10;
		
		 var ttw = $(ttid).width();
		 var tth = $(ttid).height();
		 var wscrY = $(window).scrollTop();
		 var wscrX = $(window).scrollLeft();
		 var curX = (document.all) ? event.clientX + wscrX : event.pageX;
		 var curY = (document.all) ? event.clientY + wscrY : event.pageY;
		 var ttleft = ((curX - wscrX + xOffset*2 + ttw) > $(window).width()) ? curX - ttw - xOffset*2 : curX + xOffset;
		 if (ttleft < wscrX + xOffset){
		 	ttleft = wscrX + xOffset;
		 } 
		 var tttop = ((curY - wscrY + yOffset*2 + tth) > $(window).height()) ? curY - tth - yOffset*2 : curY + yOffset;
		 if (tttop < wscrY + yOffset){
		 	tttop = curY + yOffset;
		 } 
                  if(document.getElementById("quickTrend")!=null){
                 tttop=tttop-top;
                 ttleft=ttleft-left-100;
                   }
		 $(ttid).css('top', tttop + 'px').css('left', ttleft + 'px');
	}
	
	return {
		showTooltip: showTooltip,
                showTooltip2:showTooltip2,
		hideTooltip: hideTooltip,
                updatePosition2:updatePosition2,
		updatePosition: updatePosition
	}
}

	function updatePositionKpi(event,divId){
		var ttid = "#"+divId;
                var xOffset = 20;
		var yOffset = 10;
		
		 var ttw = $(ttid).width();
		 var tth = $(ttid).height();
		 var wscrY = $(window).scrollTop();
		 var wscrX = $(window).scrollLeft();
		 var curX = (document.all) ? event.clientX + wscrX : event.pageX;
		 var curY = (document.all) ? event.clientY + wscrY : event.pageY;
		 var ttleft = ((curX - wscrX + xOffset*2 + ttw) > $(window).width()) ? curX - ttw - xOffset*2 : curX + xOffset;
		 if (ttleft < wscrX + xOffset){
		 	ttleft = wscrX + xOffset;
		 } 
		 var tttop = ((curY - wscrY + yOffset*2 + tth) > $(window).height()) ? curY - tth - yOffset*2 : curY + yOffset;
		 if (tttop < wscrY + yOffset){
		 	tttop = curY + yOffset;
		 } 
		 $(ttid).css('top', tttop + 'px').css('left', ttleft + 'px');
	}
