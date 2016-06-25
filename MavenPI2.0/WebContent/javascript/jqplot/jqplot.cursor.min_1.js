/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.0r1095
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects 
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL 
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can 
 * choose the license that best suits your project and use it accordingly. 
 *
 * Although not required, the author would appreciate an email letting him 
 * know of any substantial use of jqPlot.  You can reach the author at: 
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 * included jsDate library by Chris Leonello:
 *
 * Copyright (c) 2010-2011 Chris Leonello
 *
 * jsDate is currently available for use in all personal or commercial projects 
 * under both the MIT and GPL version 2.0 licenses. This means that you can 
 * choose the license that best suits your project and use it accordingly.
 *
 * jsDate borrows many concepts and ideas from the Date Instance 
 * Methods by Ken Snyder along with some parts of Ken's actual code.
 * 
 * Ken's origianl Date Instance Methods and copyright notice:
 * 
 * Ken Snyder (ken d snyder at gmail dot com)
 * 2008-09-10
 * version 2.0.2 (http://kendsnyder.com/sandbox/date/)     
 * Creative Commons Attribution License 3.0 (http://creativecommons.org/licenses/by/3.0/)
 *
 * jqplotToImage function based on Larry Siden's export-jqplot-to-png.js.
 * Larry has generously given permission to adapt his code for inclusion
 * into jqPlot.
 *
 * Larry's original code can be found here:
 *
 * https://github.com/lsiden/export-jqplot-to-png
 * 
 * 
 */
(function(j){
    j.jqplot.Cursor=function(q){
        this.style="crosshair";
        this.previousCursor="auto";
        this.show=j.jqplot.config.enablePlugins;
        this.showTooltip=true;
        this.followMouse=false;
        this.tooltipLocation="se";
        this.tooltipOffset=6;
        this.showTooltipGridPosition=false;
        this.showTooltipUnitPosition=true;
        this.showTooltipDataPosition=false;
        this.tooltipFormatString="%.4P, %.4P";
        this.useAxesFormatters=true;
        this.tooltipAxisGroups=[];
        this.zoom=false;
        this.zoomProxy=false;
        this.zoomTarget=false;
        this.looseZoom=true;
        this.clickReset=false;
        this.dblClickReset=true;
        this.showVerticalLine=false;
        this.showHorizontalLine=false;
        this.constrainZoomTo="none";
        this.shapeRenderer=new j.jqplot.ShapeRenderer();
        this._zoom={
            start:[],
            end:[],
            started:false,
            zooming:false,
            isZoomed:false,
            axes:{
                start:{},
                end:{}
        },
        gridpos:{},
        datapos:{}
};

this._tooltipElem;
this.zoomCanvas;
this.cursorCanvas;
this.intersectionThreshold=2;
this.showCursorLegend=false;
this.cursorLegendFormatString=j.jqplot.Cursor.cursorLegendFormatString;
this._oldHandlers={
    onselectstart:null,
    ondrag:null,
    onmousedown:null
};

this.constrainOutsideZoom=true;
this.showTooltipOutsideZoom=false;
this.onGrid=false;
j.extend(true,this,q)
    };

j.jqplot.Cursor.cursorLegendFormatString="%s x:%s, y:%s";
j.jqplot.Cursor.init=function(t,s,r){
    var q=r||{};

    this.plugins.cursor=new j.jqplot.Cursor(q.cursor);
    var u=this.plugins.cursor;
    if(u.show){
        j.jqplot.eventListenerHooks.push(["jqplotMouseEnter",b]);
        j.jqplot.eventListenerHooks.push(["jqplotMouseLeave",f]);
        j.jqplot.eventListenerHooks.push(["jqplotMouseMove",i]);
        if(u.showCursorLegend){
            r.legend=r.legend||{};

            r.legend.renderer=j.jqplot.CursorLegendRenderer;
            r.legend.formatString=this.plugins.cursor.cursorLegendFormatString;
            r.legend.show=true
            }
            if(u.zoom){
            j.jqplot.eventListenerHooks.push(["jqplotMouseDown",a]);
            if(u.clickReset){
                j.jqplot.eventListenerHooks.push(["jqplotClick",k])
                }
                if(u.dblClickReset){
                j.jqplot.eventListenerHooks.push(["jqplotDblClick",c])
                }
            }
        this.resetZoom=function(){
        var x=this.axes;
        if(!u.zoomProxy){
            for(var w in x){
                x[w].reset();
                x[w]._ticks=[];
                if(u._zoom.axes[w]!==undefined){
                    x[w]._autoFormatString=u._zoom.axes[w].tickFormatString
                    }
                }
            this.redraw()
        }else{
        var v=this.plugins.cursor.zoomCanvas._ctx;
        v.clearRect(0,0,v.canvas.width,v.canvas.height);
        v=null
        }
        this.plugins.cursor._zoom.isZoomed=false;
    this.target.trigger("jqplotResetZoom",[this,this.plugins.cursor])
    };

if(u.showTooltipDataPosition){
    u.showTooltipUnitPosition=false;
    u.showTooltipGridPosition=false;
    if(q.cursor.tooltipFormatString==undefined){
        u.tooltipFormatString=j.jqplot.Cursor.cursorLegendFormatString
        }
    }
}
};

j.jqplot.Cursor.postDraw=function(){
    var x=this.plugins.cursor;
    if(x.zoomCanvas){
        x.zoomCanvas.resetCanvas();
        x.zoomCanvas=null
        }
        if(x.cursorCanvas){
        x.cursorCanvas.resetCanvas();
        x.cursorCanvas=null
        }
        if(x._tooltipElem){
        x._tooltipElem.emptyForce();
        x._tooltipElem=null
        }
        if(x.zoom){
        x.zoomCanvas=new j.jqplot.GenericCanvas();
        this.eventCanvas._elem.before(x.zoomCanvas.createElement(this._gridPadding,"jqplot-zoom-canvas",this._plotDimensions,this));
        x.zoomCanvas.setContext()
        }
        var v=document.createElement("div");
    x._tooltipElem=j(v);
    v=null;
    x._tooltipElem.addClass("jqplot-cursor-tooltip");
    x._tooltipElem.css({
        position:"absolute",
        display:"none"
    });
    if(x.zoomCanvas){
        x.zoomCanvas._elem.before(x._tooltipElem)
        }else{
        this.eventCanvas._elem.before(x._tooltipElem)
        }
        if(x.showVerticalLine||x.showHorizontalLine){
        x.cursorCanvas=new j.jqplot.GenericCanvas();
        this.eventCanvas._elem.before(x.cursorCanvas.createElement(this._gridPadding,"jqplot-cursor-canvas",this._plotDimensions,this));
        x.cursorCanvas.setContext()
        }
        if(x.showTooltipUnitPosition){
        if(x.tooltipAxisGroups.length===0){
            var t=this.series;
            var u;
            var q=[];
            for(var r=0;r<t.length;r++){
                u=t[r];
                var w=u.xaxis+","+u.yaxis;
                if(j.inArray(w,q)==-1){
                    q.push(w)
                    }
                }
            for(var r=0;r<q.length;r++){
            x.tooltipAxisGroups.push(q[r].split(","))
            }
        }
    }
};

j.jqplot.Cursor.zoomProxy=function(v,r){
    var q=v.plugins.cursor;
    var u=r.plugins.cursor;
    q.zoomTarget=true;
    q.zoom=true;
    q.style="auto";
    q.dblClickReset=false;
    u.zoom=true;
    u.zoomProxy=true;
    r.target.bind("jqplotZoom",t);
    r.target.bind("jqplotResetZoom",s);
    function t(x,w,z,y,A){
        q.doZoom(w,z,v,A)
        }
        function s(w,x,y){
        v.resetZoom()
        }
    };

j.jqplot.Cursor.prototype.resetZoom=function(u,v){
    var t=u.axes;
    var s=v._zoom.axes;
    if(!u.plugins.cursor.zoomProxy&&v._zoom.isZoomed){
        for(var r in t){
            t[r].reset();
            t[r]._ticks=[];
            t[r]._autoFormatString=s[r].tickFormatString
            }
            u.redraw();
        v._zoom.isZoomed=false
        }else{
        var q=v.zoomCanvas._ctx;
        q.clearRect(0,0,q.canvas.width,q.canvas.height);
        q=null
        }
        u.target.trigger("jqplotResetZoom",[u,v])
    };

j.jqplot.Cursor.resetZoom=function(q){
    q.resetZoom()
    };

j.jqplot.Cursor.prototype.doZoom=function(G,t,C,u){
    var I=u;
    var F=C.axes;
    var r=I._zoom.axes;
    var w=r.start;
    var s=r.end;
    var B,E,z,D,v,x,q,H,J;
    var A=C.plugins.cursor.zoomCanvas._ctx;
    if((I.constrainZoomTo=="none"&&Math.abs(G.x-I._zoom.start[0])>6&&Math.abs(G.y-I._zoom.start[1])>6)||(I.constrainZoomTo=="x"&&Math.abs(G.x-I._zoom.start[0])>6)||(I.constrainZoomTo=="y"&&Math.abs(G.y-I._zoom.start[1])>6)){
        if(!C.plugins.cursor.zoomProxy){
            for(var y in t){
                alert("t22222222222"+t[y])
                if(I._zoom.axes[y]==undefined){
                    I._zoom.axes[y]={};

                    I._zoom.axes[y].numberTicks=F[y].numberTicks;
                    I._zoom.axes[y].tickInterval=F[y].tickInterval;
                    I._zoom.axes[y].daTickInterval=F[y].daTickInterval;
                    I._zoom.axes[y].min=F[y].min;
                    I._zoom.axes[y].max=F[y].max;
                    I._zoom.axes[y].tickFormatString=(F[y].tickOptions!=null)?F[y].tickOptions.formatString:""
                    }
                    if((I.constrainZoomTo=="none")||(I.constrainZoomTo=="x"&&y.charAt(0)=="x")||(I.constrainZoomTo=="y"&&y.charAt(0)=="y")){
                    z=t[y];
                    if(z!=null){
                        if(z>w[y]){
                            v=w[y];
                            x=z
                            }else{
                            D=w[y]-z;
                            v=z;
                            x=w[y]
                            }
                            q=F[y];
                        H=null;
                        if(q.alignTicks){
                            if(q.name==="x2axis"&&C.axes.xaxis.show){
                                H=C.axes.xaxis.numberTicks
                                }else{
                                if(q.name.charAt(0)==="y"&&q.name!=="yaxis"&&q.name!=="yMidAxis"&&C.axes.yaxis.show){
                                    H=C.axes.yaxis.numberTicks
                                    }
                                }
                        }
                    if(this.looseZoom&&(F[y].renderer.constructor===j.jqplot.LinearAxisRenderer||F[y].renderer.constructor===j.jqplot.LogAxisRenderer)){
                    J=j.jqplot.LinearTickGenerator(v,x,q._scalefact,H);
                    if(F[y].tickInset&&J[0]<F[y].min+F[y].tickInset*F[y].tickInterval){
                        J[0]+=J[4];
                        J[2]-=1
                        }
                        if(F[y].tickInset&&J[1]>F[y].max-F[y].tickInset*F[y].tickInterval){
                        J[1]-=J[4];
                        J[2]-=1
                        }
                        if(F[y].renderer.constructor===j.jqplot.LogAxisRenderer&&J[0]<F[y].min){
                        J[0]+=J[4];
                        J[2]-=1
                        }
                        F[y].min=J[0];
                    F[y].max=J[1];
                    F[y]._autoFormatString=J[3];
                    F[y].numberTicks=J[2];
                    F[y].tickInterval=J[4];
                    F[y].daTickInterval=[J[4]/1000,"seconds"]
                    }else{
                    F[y].min=v;
                    F[y].max=x;
                    F[y].tickInterval=null;
                    F[y].numberTicks=null;
                    F[y].daTickInterval=null
                    }
                    F[y]._ticks=[]
                }
            }
            }
        A.clearRect(0,0,A.canvas.width,A.canvas.height);
C.redraw();
I._zoom.isZoomed=true;
A=null
}
C.target.trigger("jqplotZoom",[G,t,C,u])
}
};

j.jqplot.preInitHooks.push(j.jqplot.Cursor.init);
j.jqplot.postDrawHooks.push(j.jqplot.Cursor.postDraw);
function e(G,r,C){
    var J=C.plugins.cursor;
    var w="";
    var N=false;
    if(J.showTooltipGridPosition){
        w=G.x+", "+G.y;
        N=true
        }
        if(J.showTooltipUnitPosition){
        var F;
        for(var E=0;E<J.tooltipAxisGroups.length;E++){
            F=J.tooltipAxisGroups[E];
            if(N){
                w+="<br />"
                }
                if(J.useAxesFormatters){
                for(var D=0;D<F.length;D++){
                    if(D){
                        w+=", "
                        }
                        var H=C.axes[F[D]]._ticks[0].formatter;
                    var B=C.axes[F[D]]._ticks[0].formatString;
                    w+=H(B,r[F[D]])
                    }
                }else{
            w+=j.jqplot.sprintf(J.tooltipFormatString,r[F[0]],r[F[1]])
            }
            N=true
        }
        }
    if(J.showTooltipDataPosition){
    var u=C.series;
    var M=d(C,G.x,G.y);
    var N=false;
    for(var E=0;E<u.length;E++){
        if(u[E].show){
            var y=u[E].index;
            var t=u[E].label.toString();
            var I=j.inArray(y,M.indices);
            var z=undefined;
            var x=undefined;
            if(I!=-1){
                var L=M.data[I].data;
                if(J.useAxesFormatters){
                    var A=u[E]._xaxis._ticks[0].formatter;
                    var q=u[E]._yaxis._ticks[0].formatter;
                    var K=u[E]._xaxis._ticks[0].formatString;
                    var v=u[E]._yaxis._ticks[0].formatString;
                    z=A(K,L[0]);
                    x=q(v,L[1])
                    }else{
                    z=L[0];
                    x=L[1]
                    }
                    if(N){
                    w+="<br />"
                    }
                    w+=j.jqplot.sprintf(J.tooltipFormatString,t,z,x);
                N=true
                }
            }
    }
}
J._tooltipElem.html(w)
}
function g(C,A){
    var E=A.plugins.cursor;
    var z=E.cursorCanvas._ctx;
    z.clearRect(0,0,z.canvas.width,z.canvas.height);
    if(E.showVerticalLine){
        E.shapeRenderer.draw(z,[[C.x,0],[C.x,z.canvas.height]])
        }
        if(E.showHorizontalLine){
        E.shapeRenderer.draw(z,[[0,C.y],[z.canvas.width,C.y]])
        }
        var G=d(A,C.x,C.y);
    if(E.showCursorLegend){
        var r=j(A.targetId+" td.jqplot-cursor-legend-label");
        for(var B=0;B<r.length;B++){
            var v=j(r[B]).data("seriesIndex");
            var t=A.series[v];
            var s=t.label.toString();
            var D=j.inArray(v,G.indices);
            var x=undefined;
            var w=undefined;
            if(D!=-1){
                var H=G.data[D].data;
                if(E.useAxesFormatters){
                    var y=t._xaxis._ticks[0].formatter;
                    var q=t._yaxis._ticks[0].formatter;
                    var F=t._xaxis._ticks[0].formatString;
                    var u=t._yaxis._ticks[0].formatString;
                    x=y(F,H[0]);
                    w=q(u,H[1])
                    }else{
                    x=H[0];
                    w=H[1]
                    }
                }
            if(A.legend.escapeHtml){
            j(r[B]).text(j.jqplot.sprintf(E.cursorLegendFormatString,s,x,w))
            }else{
            j(r[B]).html(j.jqplot.sprintf(E.cursorLegendFormatString,s,x,w))
            }
        }
    }
z=null
}
function d(A,F,E){
    var B={
        indices:[],
        data:[]
    };

    var G,w,u,C,v,q,t;
    var z;
    var D=A.plugins.cursor;
    for(var w=0;w<A.series.length;w++){
        G=A.series[w];
        q=G.renderer;
        if(G.show){
            z=D.intersectionThreshold;
            if(G.showMarker){
                z+=G.markerRenderer.size/2
                }
                for(var v=0;v<G.gridData.length;v++){
                t=G.gridData[v];
                if(D.showVerticalLine){
                    if(Math.abs(F-t[0])<=z){
                        B.indices.push(w);
                        B.data.push({
                            seriesIndex:w,
                            pointIndex:v,
                            gridData:t,
                            data:G.data[v]
                            })
                        }
                    }
            }
        }
    }
return B
}
function n(r,t){
    var v=t.plugins.cursor;
    var s=v._tooltipElem;
    switch(v.tooltipLocation){
        case"nw":
            var q=r.x+t._gridPadding.left-s.outerWidth(true)-v.tooltipOffset;
            var u=r.y+t._gridPadding.top-v.tooltipOffset-s.outerHeight(true);
            break;
        case"n":
            var q=r.x+t._gridPadding.left-s.outerWidth(true)/2;
            var u=r.y+t._gridPadding.top-v.tooltipOffset-s.outerHeight(true);
            break;
        case"ne":
            var q=r.x+t._gridPadding.left+v.tooltipOffset;
            var u=r.y+t._gridPadding.top-v.tooltipOffset-s.outerHeight(true);
            break;
        case"e":
            var q=r.x+t._gridPadding.left+v.tooltipOffset;
            var u=r.y+t._gridPadding.top-s.outerHeight(true)/2;
            break;
        case"se":
            var q=r.x+t._gridPadding.left+v.tooltipOffset;
            var u=r.y+t._gridPadding.top+v.tooltipOffset;
            break;
        case"s":
            var q=r.x+t._gridPadding.left-s.outerWidth(true)/2;
            var u=r.y+t._gridPadding.top+v.tooltipOffset;
            break;
        case"sw":
            var q=r.x+t._gridPadding.left-s.outerWidth(true)-v.tooltipOffset;
            var u=r.y+t._gridPadding.top+v.tooltipOffset;
            break;
        case"w":
            var q=r.x+t._gridPadding.left-s.outerWidth(true)-v.tooltipOffset;
            var u=r.y+t._gridPadding.top-s.outerHeight(true)/2;
            break;
        default:
            var q=r.x+t._gridPadding.left+v.tooltipOffset;
            var u=r.y+t._gridPadding.top+v.tooltipOffset;
            break
            }
            s.css("left",q);
    s.css("top",u);
    s=null
    }
    function m(u){
    var s=u._gridPadding;
    var v=u.plugins.cursor;
    var t=v._tooltipElem;
    switch(v.tooltipLocation){
        case"nw":
            var r=s.left+v.tooltipOffset;
            var q=s.top+v.tooltipOffset;
            t.css("left",r);
            t.css("top",q);
            break;
        case"n":
            var r=(s.left+(u._plotDimensions.width-s.right))/2-t.outerWidth(true)/2;
            var q=s.top+v.tooltipOffset;
            t.css("left",r);
            t.css("top",q);
            break;
        case"ne":
            var r=s.right+v.tooltipOffset;
            var q=s.top+v.tooltipOffset;
            t.css({
            right:r,
            top:q
        });
        break;
        case"e":
            var r=s.right+v.tooltipOffset;
            var q=(s.top+(u._plotDimensions.height-s.bottom))/2-t.outerHeight(true)/2;
            t.css({
            right:r,
            top:q
        });
        break;
        case"se":
            var r=s.right+v.tooltipOffset;
            var q=s.bottom+v.tooltipOffset;
            t.css({
            right:r,
            bottom:q
        });
        break;
        case"s":
            var r=(s.left+(u._plotDimensions.width-s.right))/2-t.outerWidth(true)/2;
            var q=s.bottom+v.tooltipOffset;
            t.css({
            left:r,
            bottom:q
        });
        break;
        case"sw":
            var r=s.left+v.tooltipOffset;
            var q=s.bottom+v.tooltipOffset;
            t.css({
            left:r,
            bottom:q
        });
        break;
        case"w":
            var r=s.left+v.tooltipOffset;
            var q=(s.top+(u._plotDimensions.height-s.bottom))/2-t.outerHeight(true)/2;
            t.css({
            left:r,
            top:q
        });
        break;
        default:
            var r=s.right-v.tooltipOffset;
            var q=s.bottom+v.tooltipOffset;
            t.css({
            right:r,
            bottom:q
        });
        break
        }
        t=null
    }
    function k(r,q,v,u,t){
    r.preventDefault();
    r.stopImmediatePropagation();
    var w=t.plugins.cursor;
    if(w.clickReset){
        w.resetZoom(t,w)
        }
        var s=window.getSelection;
    if(document.selection&&document.selection.empty){
        document.selection.empty()
        }else{
        if(s&&!s().isCollapsed){
            s().collapse()
            }
        }
    return false
}
function c(r,q,v,u,t){
    r.preventDefault();
    r.stopImmediatePropagation();
    var w=t.plugins.cursor;
    if(w.dblClickReset){
        w.resetZoom(t,w)
        }
        var s=window.getSelection;
    if(document.selection&&document.selection.empty){
        document.selection.empty()
        }else{
        if(s&&!s().isCollapsed){
            s().collapse()
            }
        }
    return false
}
function f(w,t,q,z,u){
    var v=u.plugins.cursor;
    v.onGrid=false;
    if(v.show){
        j(w.target).css("cursor",v.previousCursor);
        if(v.showTooltip&&!(v._zoom.zooming&&v.showTooltipOutsideZoom&&!v.constrainOutsideZoom)){
            v._tooltipElem.empty();
            v._tooltipElem.hide()
            }
            if(v.zoom){
            v._zoom.gridpos=t;
            v._zoom.datapos=q
            }
            if(v.showVerticalLine||v.showHorizontalLine){
            var B=v.cursorCanvas._ctx;
            B.clearRect(0,0,B.canvas.width,B.canvas.height);
            B=null
            }
            if(v.showCursorLegend){
            var A=j(u.targetId+" td.jqplot-cursor-legend-label");
            for(var s=0;s<A.length;s++){
                var y=j(A[s]).data("seriesIndex");
                var r=u.series[y];
                var x=r.label.toString();
                if(u.legend.escapeHtml){
                    j(A[s]).text(j.jqplot.sprintf(v.cursorLegendFormatString,x,undefined,undefined))
                    }else{
                    j(A[s]).html(j.jqplot.sprintf(v.cursorLegendFormatString,x,undefined,undefined))
                    }
                }
            }
    }
}
function b(r,q,u,t,s){
    var v=s.plugins.cursor;
    v.onGrid=true;
    if(v.show){
        v.previousCursor=r.target.style.cursor;
        r.target.style.cursor=v.style;
        if(v.showTooltip){
            e(q,u,s);
            if(v.followMouse){
                n(q,s)
                }else{
                m(s)
                }
                v._tooltipElem.show()
            }
            if(v.showVerticalLine||v.showHorizontalLine){
            g(q,s)
            }
        }
}
function i(r,q,u,t,s){
    var v=s.plugins.cursor;
    if(v.show){
        if(v.showTooltip){
            e(q,u,s);
            if(v.followMouse){
                n(q,s)
                }
            }
        if(v.showVerticalLine||v.showHorizontalLine){
        g(q,s)
        }
    }
}
function o(y){
    var x=y.data.plot;
    var t=x.eventCanvas._elem.offset();
    var w={
        x:y.pageX-t.left,
        y:y.pageY-t.top
        };

    var u={
        xaxis:null,
        yaxis:null,
        x2axis:null,
        y2axis:null,
        y3axis:null,
        y4axis:null,
        y5axis:null,
        y6axis:null,
        y7axis:null,
        y8axis:null,
        y9axis:null,
        yMidAxis:null
    };

    var v=["xaxis","yaxis","x2axis","y2axis","y3axis","y4axis","y5axis","y6axis","y7axis","y8axis","y9axis","yMidAxis"];
    var q=x.axes;
    var r,s;
    for(r=11;r>0;r--){
        s=v[r-1];
        if(q[s].show){
            u[s]=q[s].series_p2u(w[s.charAt(0)])
            }
        }
    return{
    offsets:t,
    gridPos:w,
    dataPos:u
}
}
function h(z){
    var x=z.data.plot;
    var y=x.plugins.cursor;
    if(y.show&&y.zoom&&y._zoom.started&&!y.zoomTarget){
        z.preventDefault();
        var B=y.zoomCanvas._ctx;
        var v=o(z);
        var w=v.gridPos;
        var t=v.dataPos;
        y._zoom.gridpos=w;
        y._zoom.datapos=t;
        y._zoom.zooming=true;
        var u=w.x;
        var s=w.y;
        var A=B.canvas.height;
        var q=B.canvas.width;
        if(y.showTooltip&&!y.onGrid&&y.showTooltipOutsideZoom){
            e(w,t,x);
            if(y.followMouse){
                n(w,x)
                }
            }
        if(y.constrainZoomTo=="x"){
        y._zoom.end=[u,A]
        }else{
        if(y.constrainZoomTo=="y"){
            y._zoom.end=[q,s]
            }else{
            y._zoom.end=[u,s]
            }
        }
    var r=window.getSelection;
if(document.selection&&document.selection.empty){
    document.selection.empty()
    }else{
    if(r&&!r().isCollapsed){
        r().collapse()
        }
    }
l.call(y);
B=null
}
}
function a(w,s,r,x,t){
    var v=t.plugins.cursor;
    if(t.plugins.mobile){
        j(document).one("vmouseup.jqplot_cursor",{
            plot:t
        },p)
        }else{
        j(document).one("mouseup.jqplot_cursor",{
            plot:t
        },p)
        }
        var u=t.axes;
    if(document.onselectstart!=undefined){
        v._oldHandlers.onselectstart=document.onselectstart;
        document.onselectstart=function(){
            return false
            }
        }
    if(document.ondrag!=undefined){
    v._oldHandlers.ondrag=document.ondrag;
    document.ondrag=function(){
        return false
        }
    }
if(document.onmousedown!=undefined){
    v._oldHandlers.onmousedown=document.onmousedown;
    document.onmousedown=function(){
        return false
        }
    }
if(v.zoom){
    if(!v.zoomProxy){
        var y=v.zoomCanvas._ctx;
        y.clearRect(0,0,y.canvas.width,y.canvas.height);
        y=null
        }
        if(v.constrainZoomTo=="x"){
        v._zoom.start=[s.x,0]
        }else{
        if(v.constrainZoomTo=="y"){
            v._zoom.start=[0,s.y]
            }else{
            v._zoom.start=[s.x,s.y]
            }
        }
    v._zoom.started=true;
for(var q in r){
    v._zoom.axes.start[q]=r[q]
    }
    if(t.plugins.mobile){
    j(document).bind("vmousemove.jqplotCursor",{
        plot:t
    },h)
    }else{
    j(document).bind("mousemove.jqplotCursor",{
        plot:t
    },h)
    }
}
}
function p(y){
    var v=y.data.plot;
    var x=v.plugins.cursor;
    if(x.zoom&&x._zoom.zooming&&!x.zoomTarget){
        var u=x._zoom.gridpos.x;
        var r=x._zoom.gridpos.y;
        var t=x._zoom.datapos;
        var z=x.zoomCanvas._ctx.canvas.height;
        var q=x.zoomCanvas._ctx.canvas.width;
        var w=v.axes;
        if(x.constrainOutsideZoom&&!x.onGrid){
            if(u<0){
                u=0
                }else{
                if(u>q){
                    u=q
                    }
                }
            if(r<0){
            r=0
            }else{
            if(r>z){
                r=z
                }
            }
        for(var s in t){
        if(t[s]){
            if(s.charAt(0)=="x"){
                t[s]=w[s].series_p2u(u)
                }else{
                t[s]=w[s].series_p2u(r)
                }
            }
    }
    }
if(x.constrainZoomTo=="x"){
    r=z
    }else{
    if(x.constrainZoomTo=="y"){
        u=q
        }
    }
x._zoom.end=[u,r];
x._zoom.gridpos={
    x:u,
    y:r
};

x.doZoom(x._zoom.gridpos,t,v,x)
}
x._zoom.started=false;
x._zoom.zooming=false;
j(document).unbind("mousemove.jqplotCursor",h);
if(document.onselectstart!=undefined&&x._oldHandlers.onselectstart!=null){
    document.onselectstart=x._oldHandlers.onselectstart;
    x._oldHandlers.onselectstart=null
    }
    if(document.ondrag!=undefined&&x._oldHandlers.ondrag!=null){
    document.ondrag=x._oldHandlers.ondrag;
    x._oldHandlers.ondrag=null
    }
    if(document.onmousedown!=undefined&&x._oldHandlers.onmousedown!=null){
    document.onmousedown=x._oldHandlers.onmousedown;
    x._oldHandlers.onmousedown=null
    }
}
function l(){
    var y=this._zoom.start;
    var u=this._zoom.end;
    var s=this.zoomCanvas._ctx;
    var r,v,x,q;
    if(u[0]>y[0]){
        r=y[0];
        q=u[0]-y[0]
        }else{
        r=u[0];
        q=y[0]-u[0]
        }
        if(u[1]>y[1]){
        v=y[1];
        x=u[1]-y[1]
        }else{
        v=u[1];
        x=y[1]-u[1]
        }
        s.fillStyle="rgba(0,0,0,0.2)";
    s.strokeStyle="#999999";
    s.lineWidth=1;
    s.clearRect(0,0,s.canvas.width,s.canvas.height);
    s.fillRect(0,0,s.canvas.width,s.canvas.height);
    s.clearRect(r,v,q,x);
    s.strokeRect(r,v,q,x);
    s=null
    }
    j.jqplot.CursorLegendRenderer=function(q){
    j.jqplot.TableLegendRenderer.call(this,q);
    this.formatString="%s"
    };

j.jqplot.CursorLegendRenderer.prototype=new j.jqplot.TableLegendRenderer();
j.jqplot.CursorLegendRenderer.prototype.constructor=j.jqplot.CursorLegendRenderer;
j.jqplot.CursorLegendRenderer.prototype.draw=function(){
    if(this._elem){
        this._elem.emptyForce();
        this._elem=null
        }
        if(this.show){
        var w=this._series,A;
        var r=document.createElement("div");
        this._elem=j(r);
        r=null;
        this._elem.addClass("jqplot-legend jqplot-cursor-legend");
        this._elem.css("position","absolute");
        var q=false;
        for(var x=0;x<w.length;x++){
            A=w[x];
            if(A.show&&A.showLabel){
                var v=j.jqplot.sprintf(this.formatString,A.label.toString());
                if(v){
                    var t=A.color;
                    if(A._stack&&!A.fill){
                        t=""
                        }
                        z.call(this,v,t,q,x);
                    q=true
                    }
                    for(var u=0;u<j.jqplot.addLegendRowHooks.length;u++){
                    var y=j.jqplot.addLegendRowHooks[u].call(this,A);
                    if(y){
                        z.call(this,y.label,y.color,q);
                        q=true
                        }
                    }
                }
        }
    w=A=null;
delete w;
delete A
}
function z(D,C,F,s){
    var B=(F)?this.rowSpacing:"0";
    var E=j('<tr class="jqplot-legend jqplot-cursor-legend"></tr>').appendTo(this._elem);
    E.data("seriesIndex",s);
    j('<td class="jqplot-legend jqplot-cursor-legend-swatch" style="padding-top:'+B+';"><div style="border:1px solid #cccccc;padding:0.2em;"><div class="jqplot-cursor-legend-swatch" style="background-color:'+C+';"></div></div></td>').appendTo(E);
    var G=j('<td class="jqplot-legend jqplot-cursor-legend-label" style="vertical-align:middle;padding-top:'+B+';"></td>');
    G.appendTo(E);
    G.data("seriesIndex",s);
    if(this.escapeHtml){
        G.text(D)
        }else{
        G.html(D)
        }
        E=null;
    G=null
    }
    return this._elem
}
})(jQuery);