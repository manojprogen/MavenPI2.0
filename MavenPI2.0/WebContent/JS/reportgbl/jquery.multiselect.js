/* jshint forin:true, noarg:true, noempty:true, eqeqeq:true, boss:true, undef:true, curly:true, browser:true, jquery:true */
/*
 * jQuery MultiSelect UI Widget 1.14pre
 * Copyright (c) 2012 Eric Hynds
 *
 * http://www.erichynds.com/jquery/jquery-ui-multiselect-widget/
 *
 * Depends:
 *   - jQuery 1.4.2+
 *   - jQuery UI 1.8 widget factory
 *
 * Optional:
 *   - jQuery UI effects
 *   - jQuery UI position utility
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 */
(function($, undefined) {

  var multiselectID = 0;
  var $doc = $(document);
 var selfgr;
 var self11;
 var elementid;
 var onclik='false';
 var loadingtimegr=false
 var valselect=false
   var  notinflag=false
var map = new Object();
var value1;
var value5;
var totalSelectOption=0;
  $.widget("ech.multiselect", {

    // default options
    options: {
      header: true,
      height: 175,
      minWidth: 225,
      classes: '',
      checkAllText: 'Check all',
      uncheckAllText: 'Uncheck all',
      noneSelectedText: 'Select filters',
      selectedText: '# selected',
      selectedList: 0,
      show: null,
      hide: null,
      autoOpen: false,
      multiple: true,
      position: {},
      appendTo: "body"
    },

    _create: function() {
      var el = this.element.hide();
      var o = this.options;
loadingtimegr=true
valselect=true
      this.speed = $.fx.speeds._default; // default speed for effects
      this._isOpen = false; // assume no

      // create a unique namespace for events that the widget
      // factory cannot unbind automatically. Use eventNamespace if on
      // jQuery UI 1.9+, and otherwise fallback to a custom string.
      this._namespaceID = this.eventNamespace || ('multiselect' + multiselectID);

  var el1 = this.element;

    var $this = $(this);
var id = el1.attr('id') || multiselectID++; // unique ID for the label & option tags
 id=id.split("__")[1]; // changed by sandeep for displaying filter box on top (id changing element name to  element id with elemnt name) tack id-->TA_R_47
     elementid = el1.attr('name')
     var namevalue=id.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
     var totalvalue=id+"__"+elementid+"__"+$this;
      var scrollid='grui-helper-reset' + elementid
     var idbuttin='multiselectt' + elementid; var lableclickg='lableclickg' + elementid; var arrowclickg='arrowclickg' + elementid
var checkall='ui-multiselect-all' + elementid
var uncheckall='ui-multiselect-close' + elementid
var ulid='listoffilter'+elementid
  map[idbuttin] = this;
   var ctxPath=parent.document.getElementById("h").value;
      var button = (this.button = $('<button id="'+idbuttin+'" title="'+namevalue+'" value="'+totalvalue+'" style="width:200px;height:27px; color:white;"  type="button"><span style="float:right;margin-right:17px"><i id="'+arrowclickg+'" class="fa fa-angle-down" style="color:#ccc;font-size:large"></i></span></button>'))
        .addClass('ui-multiselect ui-widget ui-state-default ui-corner-all')
        .addClass(o.classes)
        .attr({'title':el.attr('title'), 'aria-haspopup':true, 'tabIndex':el.attr('tabIndex')})
        .insertAfter(el),

        buttonlabel = (this.buttonlabel = $('<span id="'+lableclickg+'" class="gFontFamily gFontSize12" style="color: #555;"  />'))
          .html(o.noneSelectedText)
          .appendTo(button),

        menu = (this.menu = $('<div />'))
          .addClass('ui-multiselect-menu ui-widget ui-widget-content ui-corner-all')
          .addClass(o.classes)
          .appendTo($(o.appendTo)),

        header = (this.header = $('<div />'))
          .addClass('ui-widget-header ui-corner-all ui-multiselect-header ui-helper-clearfix')
          .appendTo(menu),

        headerLinkContainer = (this.headerLinkContainer = $('<ul />'))
          .addClass('ui-helper-reset')
          .html(function() {
            if(o.header === true) {
                              return '<li id="'+checkall+'" style="width:40%;" ><a class="ui-multiselect-all" href="#" style="color: white;"><span class="ui-icon ui-icon-check"></span><span style="color: black;"> Select All </span></a></li><li id="'+uncheckall+'" style="width:50%;"><a class="ui-multiselect-none" href="#" style="color: white;"><span class="ui-icon ui-icon-closethick"></span><span style="color: black;" >UnSelect All</span></a></li>';

//              return '<li id="'+checkall+'"><a class="ui-multiselect-all" href="#" style="color: white;"><span class="ui-icon ui-icon-check"></span><span>' + o.checkAllText + '</span></a></li><li id="'+uncheckall+'"><a class="ui-multiselect-none" href="#" style="color: white;"><span class="ui-icon ui-icon-closethick"></span><span>' + o.uncheckAllText + '</span></a></li>';
            } else if(typeof o.header === "string") {
              return '<li>' + o.header + '</li>';
            } else {
              return '';
            }
          })
          .append('<li class="ui-multiselect-close"><a href="#" class="ui-multiselect-close"><span class="ui-icon ui-icon-circle-close"></span></a></li>')
          .appendTo(header),

        checkboxContainer = (this.checkboxContainer = $('<ul id="'+scrollid+'" />'))
          .addClass('ui-multiselect-checkboxes ui-helper-reset')
          .appendTo(menu);
$("#"+scrollid).on('scroll',function(){loadingtimegr=false; valselect=true
     setTimeout(parent.onScrollDivtable(this),2000);
//     setTimeout(parent.onScrollDivreport(this),2000);
//      setTimeout($.proxy(self1.refresh, self1), 2000);
//var  scHeight=(this.scrollHeight);
//  var   scTop=(this.scrollTop);
//  var   clHgt=(this.clientHeight);
//  var selfgr=map[idbuttin];
//        if(scTop==(scHeight-clHgt)){
//loadingtimegr=false
//   var flag= parent.$("#multigblrefresh").val();
//   if(flag=="true"){
//       valselect=true
//      setTimeout($.proxy(selfgr.refresh, selfgr), 2000);
//   }
//        }
})
var oldobj=this;
$("#"+idbuttin).mouseover(function(){
    var idvalue= $("#"+idbuttin).val()
var id1=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
    var pos = button.offset();
    var left=pos.left
     var top=pos.top + button.outerHeight()
//    parent.showfilters(elementid,"graph","event",left,top)
})
$("#"+idbuttin).mouseout(function(){
//   parent.hidefilters()
})

$("#"+idbuttin).click(function(){
   var idvalue= $("#"+idbuttin).val()
var id1=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
self11=map[idbuttin];
loadingtimegr=false
valselect=true
 selfgr = self11;
   setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"graph","null"),2000);
//    setTimeout(parent.getlovfiltersreport(id1,elementid1,self11),2000);
//    onclik='true'

//         setTimeout($.proxy(self11.refresh, self11), 2000);
//    }


})
$("#"+lableclickg).click(function(){
   var idvalue= $("#"+idbuttin).val()
var id1=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
self11=map[idbuttin];
loadingtimegr=false
valselect=true
 selfgr = self11;
   setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"graph","null"),2000);
//    setTimeout(parent.getlovfiltersreport(id1,elementid1,self11),2000);
//    onclik='true'

//         setTimeout($.proxy(self11.refresh, self11), 2000);
//    }

})
$("#"+arrowclickg).click(function(){
   var idvalue= $("#"+idbuttin).val()
var id1=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
self11=map[idbuttin];
loadingtimegr=false
valselect=true
 selfgr = self11;
   setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"graph","null"),2000);

})
$("#"+uncheckall).click(function(){
var idvalue= $("#"+idbuttin).val()
var name=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
 setTimeout(parent.setallfilters(name,elementid,"uncheckall",""),2000);
 selfgr=map[idbuttin];
// var flag= parent.$("#multigblrefresh").val();
//   if(flag=="true"){
//      setTimeout($.proxy(self1.refresh, self1), 10);
//   }
})
$("#"+checkall).click(function(){
var idvalue= $("#"+idbuttin).val()
var name=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
loadingtimegr=false
 setTimeout(parent.setallfilters(name,elementid,"checkall",""),2000);
 selfgr=map[idbuttin];
// var flag= parent.$("#multigblrefresh").val();
//   if(flag=="true"){
//      setTimeout($.proxy(self1.refresh, self1), 10);
//   }
})
        // perform event bindings
        this._bindEvents();

        // build menu
        this.refresh(true);

        // some addl. logic for single selects
        if(!o.multiple) {
          menu.addClass('ui-multiselect-single');
        }

        // bump unique ID
        multiselectID++;
    },

    _init: function() {
      if(this.options.header === false) {
        this.header.hide();
      }
      if(!this.options.multiple) {
        this.headerLinkContainer.find('.ui-multiselect-all, .ui-multiselect-none').hide();
      }
      if(this.options.autoOpen) {
        this.open();
      }
      if(this.element.is(':disabled')) {
        this.disable();
      }
    },

    refresh: function(init) {
         var flag= parent.$("#multigblrefresh").val();
        if(flag=="reset"){
            valselect=true
           
        }else{
             if(flag=="search"){
                valselect=true
            loadingtimegr=false
            }
            else if(flag=="filter"){
            valselect=true
            loadingtimegr=true
        }
        }
      
        if(flag=="notin"){
              valselect=true
            loadingtimegr=true
              notinflag=true
        }
        if(valselect){
      var el = this.element;
      var o = this.options;
      var menu = this.menu;
      var checkboxContainer = this.checkboxContainer;
      var optgroups = [];
         var self = this;
//      self1 = self;
      var html = "";
     var id = el.attr('id') || multiselectID++; // unique ID for the label & option tags
     id=id.split("__")[1];
      // unique ID for the label & option tags
//     var idbuttin='multiselectt' + elementid;
//   var idvalue= $("#"+idbuttin).val();
   if(onclik!=""&& onclik=='true'){
el= self11.element;
o = self11.options;
 menu = self11.menu;
 checkboxContainer = self11.checkboxContainer;
   self = self11;
   }



      // build items
      el.find('option').each(function(i) {
        var $this = $(this);
        var parent = this.parentNode;
        var description = this.innerHTML;
        var title = this.title;
        var filterselect;
        var value = this.value;
        var index=value.split("_")[1];
        if(value.split("_")[3]=='selecttrue'){
           filterselect = value.split("_")[3];
            value=value.replace('_selecttrue', '');
        }
        var selectvalue=value.split("_")[0].toString();
        value=value.replace('2q2', '_');
        var finalvalue=value.split("_")[0];
        var inputID = 'ui-multiselectg-' + (this.id || id + '-option-' + i);
        var isDisabled = this.disabled;
        var isSelected = this.selected;
        var labelClasses = [ 'ui-corner-all' ];
        var liClasses = (isDisabled ? 'ui-multiselect-disabled ' : ' ') + this.className;
        var optLabel;

        // is this an optgroup?
        if(parent.tagName === 'OPTGROUP') {
          optLabel = parent.getAttribute('label');

          // has this optgroup been added already?
          if($.inArray(optLabel, optgroups) === -1) {
            html += '<li class="ui-multiselect-optgroup-label ' + parent.className + '"><a href="#">' + optLabel + '</a></li>';
            optgroups.push(optLabel);
          }
        }

        if(isDisabled) {
          labelClasses.push('ui-state-disabled');
        }

        // browsers automatically select the first option
        // by default with single selects
        if(isSelected && !o.multiple) {
          labelClasses.push('ui-state-active');
        }

        html += '<li class="' + liClasses + '">';
var idval=(this.id || id)+"_"+index;
var nameval=(this.id || id)+"*,"+value.split("_")[2];
        // create the label
        html += '<label style="width:95%;" for="' + inputID + '" title="' + title + '" class="' + labelClasses.join(' ') + '">';
//        html += '<input id="' + inputID + '" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" title="' + title + '"';
        html += '<input id="' + inputID + '" style="float:left;" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" onclick=parent.applyGlobalFilterinreport("'+idval+ '","' + nameval + '","'+inputID+'") title="' + title + '"';

        // pre-selected?
        if(isSelected) {
          html += ' checked="checked"';
          html += ' aria-selected="true"';
        }

        // disabled?
        if(isDisabled) {
          html += ' disabled="disabled"';
          html += ' aria-disabled="true"';
        }
if(filterselect=='selecttrue'){
    html += ' checked="checked"';
          html += ' aria-selected="true"';
}
        // add the title and close everything off
        html += ' /><span style="float:left;width:80%;">' + description + '</span></label></li>';
        totalSelectOption=i+1;
      });

      // insert into the DOM
      checkboxContainer.html(html);

      // cache some moar useful elements
      this.labels = menu.find('label');
      this.inputs = this.labels.children('input');

      // set widths
      this._setButtonWidth();
      this._setMenuWidth();

      // remember default value
      this.button[0].defaultValue = this.update();

      // broadcast refresh event; useful for widgets
      if(!init) {
        this._trigger('refresh');
      }
    }

    },

    // updates the button text. call refresh() to rebuild
    update: function() {
      var o = this.options;
      var $inputs = this.inputs;
         var el = this.element;
          elementid = el.attr('name')
       var idbuttin='multiselectt' + elementid;
      var  selectedlist=parent.filterMapgraphs[elementid];
selfgr=map[idbuttin];
$inputs = selfgr.inputs;
      var $checked = $inputs.filter(':checked');
      var numChecked = $checked.length;
      var value;
// var el = this.element;
   el.find('option').each(function(i) {
 totalSelectOption=i+1;
           });
      var countt=0;
      if(numChecked === 0) {
        value = o.noneSelectedText;
        var iddd3=o.noneSelectedText.replace(' ', '1q1')
       iddd3=iddd3.replace(' ', '1q1')
       iddd3=iddd3.replace(' ', '1q1')
        iddd3=elementid+"__"+iddd3;
//        if(loadingtimegr){
//                            loadingtimegr=false
 if(notinflag){
                               notinflag=false;
                               $("#multiselectt"+$('#'+iddd3).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
                            $("#multiselectt"+$('#'+iddd3).attr('name')).css({'background': '#fff','color':'white'});
                        }
//        }else{
//        $("#multiselectt"+$('#'+iddd3).attr('name')).css({'background': 'grey','color':'white'});
//        }
 var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
        value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
      } else {
        if($.isFunction(o.selectedText)) {
          value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
           if(numChecked==totalSelectOption){
  if(loadingtimegr){
//                            loadingtimegr=false
                             value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
                        }else{
                             var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
                if(numChecked==1 && selectedlist!=="" && selectedlist!==undefined && selectedlist.length>=1 && selectedlist[0]!="All"){
                     value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
                }else{
value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
                }
//                       value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
                        }

                    }else{
           if(value.length>10 || value.length>10){
    value=value.substring(0,10)+".."
}
                    }
                    var iddd=o.noneSelectedText.replace(' ', '1q1')
            iddd=iddd.replace(' ', '1q1')
            iddd=iddd.replace(' ', '1q1')
            iddd=elementid+"__"+iddd;
//              iddd="table"+iddd;
               if(numChecked==totalSelectOption){
                      if(loadingtimegr){
 if(notinflag){
                               notinflag=false;
                               $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
                             $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                         }
               }else{loadingtimegr=true
                   if(numChecked==1 && selectedlist!=="" && selectedlist!==undefined && selectedlist.length>=1 && selectedlist[0]!="All"){
                     value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
                        $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});

                }else{
   $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': '#fff','color':'white'});
               }
               }
                    }else{
         $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                    }

        } else if(/\d/.test(o.selectedList) && o.selectedList > 0 && numChecked <= o.selectedList) {
          value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
          value1 = $checked.map(function() {return $(this).next().html();}).get();

           if(numChecked==totalSelectOption){

         if(numChecked==1 && selectedlist!=="" && selectedlist!==undefined && selectedlist.length>=1 && selectedlist[0]!="All"){
                     value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
                }else{
value = o.selectedText.replace('of # selected',o.noneSelectedText+":All").replace('#', '');
                }
//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
                    }else{
          if(value.length>10 || value.length>10){
    value=value.substring(0,10)+".."
}
                    }
//          alert("ddd "+o.noneSelectedText)
          var iddd=o.noneSelectedText.replace(' ', '1q1')
          iddd=iddd.replace(' ', '1q1')
          iddd=iddd.replace(' ', '1q1')
          iddd=elementid+"__"+iddd;
//              iddd="table"+iddd;
           if(numChecked==totalSelectOption){
                    if(loadingtimegr){
//                            loadingtimegr=false
                             value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
                                if(value.length>10 || value.length>10){
    value=value.substring(0,10)+".."
}
 if(notinflag){
                               notinflag=false;
                               $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
                              $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                          }
 }else{
      var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
                 if(numChecked==1 && selectedlist!=="" && selectedlist!==undefined && selectedlist.length>=1 && selectedlist[0]!="All"){
                     value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
                      $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                }else{
     value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');

//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//                           if(value.length>15 || value.length>15){
//    value=value.substring(0,10)+".."
//}
                       $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': '#fff','color':'white'});
 }
 }


                    }else{
          $("#multiselectt"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                    }

//          alert("value1 : "+$checked.map(function() {return $(this).next().html();}))
//          alert("value2 : "+$checked.map(function() {return $(this).next().html();}).get())
//          alert("value3 : "+$checked.toString())

        } else {
//            alert("attribute "+this.getAttribute('aria-selected'));
//            alert("attribute "+this.attr('aria-selected'));
//                    alert("$inputs.length "+$inputs.length)
//                    alert("$checked.get() "+$checked.get().toString())
//                    alert("o.noneSelectedText "+o.noneSelectedText)
//                    alert($('#'+o.noneSelectedText).attr('name'))
                    var iddd1=o.noneSelectedText.replace(' ', '1q1')
                    iddd1=iddd1.replace(' ', '1q1')
                    iddd1=iddd1.replace(' ', '1q1')
                    iddd1=elementid+"__"+iddd1;
//                    alert("iddd1 "+iddd1)


//                    alert("o.selectedList "+o.selectedList)
//                    alert("o.noneSelectedText "+o.noneSelectedText)
//                    alert("numChecked "+numChecked)
//                    alert("$checked.html() "+$checked.html())

                    countt++;
//                    alert("cllllllllllaass"+$(this).hasClass('ui-multiselect-all'));
//                    alert("numChecked ooooo   "+numChecked)
//                    alert("idd   "+totalSelectOption);

                    if(numChecked==totalSelectOption){
//                       $("#multiselectt"+$('#'+iddd1).attr('name')).css({'background': '#1B3E70','color':'white'});
//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//

                         if(loadingtimegr){
//                            loadingtimegr=false
 if(notinflag){
                               notinflag=false;
                               $("#multiselectt"+$('#'+iddd1).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
                             $("#multiselectt"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});
                         }
                              var nvalues=value=$checked.map(function() {
                            return $(this).next().html();
                        }).get();

                               if(nvalues[0].length>10 || nvalues[1].length>10){
    value=nvalues[0].substring(0,10)+".."
}else{
                               if(nvalues[0].length<10 || nvalues[1].length<10){
          value=nvalues[0].substring(0,5).toString().trim()+","+nvalues[1].substring(0,5).toString().trim()+", More.."
     }else{
                                value=nvalues[0]+", More.."
}
}
                    }else{loadingtimegr=true

                       var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
                 if(numChecked==1 && selectedlist!=="" && selectedlist!==undefined && selectedlist.length>=1 && selectedlist[0]!="All"){
                     value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
                                              $("#multiselectt"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});

                }else{
//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
 $("#multiselectt"+$('#'+iddd1).attr('name')).css({'background': '#fff','color':'white'});
                        value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
                    }
                    }


                    }else{
                         $("#multiselectt"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});
//                        value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
//                            if(numChecked<3){
//                        value=$checked.map(function() {
//                            return $(this).next().html();
//                        }).get()+"...";
////                        value5=value1+"...";
//                            }else{

                                var nvalues=$checked.map(function() {
                            return $(this).next().html();
                        }).get();
if(nvalues[0].length>10 || nvalues[1].length>10){
    value=nvalues[0].substring(0,10)+".."
}else{
                               if(nvalues[0].length<10 || nvalues[1].length<10){
          value=nvalues[0].substring(0,5).toString().trim()+","+nvalues[1].substring(0,5).toString().trim()+", More.."
     }else{
                                value=nvalues[0]+", More.."
}
}

//                        alert("nval "+nvalues[1])
//                                }
//                            }
//                            value=value5;
        }
//                    alert("cllllllllllaass  iddd   "+$(this).hasClass('ui-multiselect-all').id);
//                    edited by manik
//                             value = o.selectedText.replace('#', numChecked).replace('#', $inputs.length);
//alert("valu555 : "+$checked.map(function() {return $(this).next().html();}).get())
//                        value=$checked.map(function() {return $(this).next().html();}).get()+"...";
//                        value=value1+"..."+numChecked+" More";
//                        value=value1+"...";
//                             value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
//                    if(countt==1){
//                        value = $checked.map(function() {
//                            return $(this).next();
//                        });
////                            value=o.selectedText.append("...More");
////                        alert("value11 :"+value)
//                        alert("o.selectedText  :"+o.selectedText)
//                        alert("map  :"+$checked.map(function() {return $(this).next().html();}))
////                        alert("o.noneSelectedText :"+o.noneSelectedText)
//                    }
//          alert("value :"+value)
      }
      }
value=value.replace('&amp;', '&').replace('&gt;', '>').replace('&lt;', '<')
      this._setButtonValue(value);
loadingtimegr=true
valselect=false
      return value;
    },

    // this exists as a separate method so that the developer
    // can easily override it.
    _setButtonValue: function(value) {
      this.buttonlabel.text(value);
    },

    // binds events
    _bindEvents: function() {
      var self = this;
      var button = this.button;

      function clickHandler() {
        self[ self._isOpen ? 'close' : 'open' ]();
        return false;
      }

      // webkit doesn't like it when you click on the span :(
      button
        .find('span')
        .bind('click.multiselect', clickHandler);

      // button events
      button.bind({
        click: clickHandler,
        keypress: function(e) {
          switch(e.which) {
            case 27: // esc
              case 38: // up
              case 37: // left
              self.close();
            break;
            case 39: // right
              case 40: // down
              self.open();
            break;
          }
        },
        mouseenter: function() {
          if(!button.hasClass('ui-state-disabled')) {
            $(this).addClass('ui-state-hover');
          }
        },
        mouseleave: function() {
          $(this).removeClass('ui-state-hover');
        },
        focus: function() {
          if(!button.hasClass('ui-state-disabled')) {
            $(this).addClass('ui-state-focus');
          }
        },
        blur: function() {
          $(this).removeClass('ui-state-focus');
        }
      });

      // header links
      this.header.delegate('a', 'click.multiselect', function(e) {
        // close link
        if($(this).hasClass('ui-multiselect-close')) {
          self.close();

          // check all / uncheck all
        } else {
          self[$(this).hasClass('ui-multiselect-all') ? 'checkAll' : 'uncheckAll']();
        }

        e.preventDefault();
      });

      // optgroup label toggle support
      this.menu.delegate('li.ui-multiselect-optgroup-label a', 'click.multiselect', function(e) {
        e.preventDefault();

        var $this = $(this);
        var $inputs = $this.parent().nextUntil('li.ui-multiselect-optgroup-label').find('input:visible:not(:disabled)');
        var nodes = $inputs.get();
        var label = $this.parent().text();

        // trigger event and bail if the return is false
        if(self._trigger('beforeoptgrouptoggle', e, {inputs:nodes, label:label}) === false) {
          return;
        }

        // toggle inputs
        self._toggleChecked(
          $inputs.filter(':checked').length !== $inputs.length,
          $inputs
        );

        self._trigger('optgrouptoggle', e, {
          inputs: nodes,
          label: label,
          checked: nodes[0].checked
        });
      })
      .delegate('label', 'mouseenter.multiselect', function() {
        if(!$(this).hasClass('ui-state-disabled')) {
          self.labels.removeClass('ui-state-hover');
          $(this).addClass('ui-state-hover').find('input').focus();
        }
      })
      .delegate('label', 'keydown.multiselect', function(e) {
        e.preventDefault();

        switch(e.which) {
          case 9: // tab
            case 27: // esc
            self.close();
          break;
          case 38: // up
            case 40: // down
            case 37: // left
            case 39: // right
            self._traverse(e.which, this);
          break;
          case 13: // enter
            $(this).find('input')[0].click();
          break;
        }
      })
      .delegate('input[type="checkbox"], input[type="radio"]', 'click.multiselect', function(e) {
        var $this = $(this);
        var val = this.value;
        var checked = this.checked;
        var tags = self.element.find('option');

        // bail if this input is disabled or the event is cancelled
        if(this.disabled || self._trigger('click', e, {value: val, text: this.title, checked: checked}) === false) {
          e.preventDefault();
          return;
        }

        // make sure the input has focus. otherwise, the esc key
        // won't close the menu after clicking an item.
        $this.focus();

        // toggle aria state
        $this.attr('aria-selected', checked);

        // change state on the original option tags
        tags.each(function() {
          if(this.value === val) {
            this.selected = checked;
          } else if(!self.options.multiple) {
            this.selected = false;
          }
        });

        // some additional single select-specific logic
        if(!self.options.multiple) {
          self.labels.removeClass('ui-state-active');
          $this.closest('label').toggleClass('ui-state-active', checked);

          // close menu
          self.close();
        }

        // fire change on the select box
        self.element.trigger("change");

        // setTimeout is to fix multiselect issue #14 and #47. caused by jQuery issue #3827
        // http://bugs.jquery.com/ticket/3827
        setTimeout($.proxy(self.update, self), 10);
      });

      // close each widget when clicking on any other element/anywhere else on the page
      $doc.bind('mousedown.' + this._namespaceID, function(event) {
        var target = event.target;

        if(self._isOpen
            && target !== self.button[0]
            && target !== self.menu[0]
            && !$.contains(self.menu[0], target)
            && !$.contains(self.button[0], target)
          ) {
          self.close();
        }
      });

      // deal with form resets.  the problem here is that buttons aren't
      // restored to their defaultValue prop on form reset, and the reset
      // handler fires before the form is actually reset.  delaying it a bit
      // gives the form inputs time to clear.
      $(this.element[0].form).bind('reset.multiselect', function() {
        setTimeout($.proxy(self.refresh, self), 10);
      });
    },

    // set button width
    _setButtonWidth: function() {
      var width = this.element.outerWidth();
      var o = this.options;

      if(/\d/.test(o.minWidth) && width < o.minWidth) {
        width = o.minWidth;
      }
width=193;
var size=parent.sizeoffiltersg;
if(size>=6){
    size=6;
}
var dvdvalue=size+".3";
width=parseInt(((($(window).width()*.89)))/dvdvalue);
      // set widths
      this.button.outerWidth(width);
    },

    // set menu width
    _setMenuWidth: function() {
      var m = this.menu;
      m.outerWidth(this.button.outerWidth());
    },

    // move up or down within the menu
    _traverse: function(which, start) {
      var $start = $(start);
      var moveToLast = which === 38 || which === 37;

      // select the first li that isn't an optgroup label / disabled
      var $next = $start.parent()[moveToLast ? 'prevAll' : 'nextAll']('li:not(.ui-multiselect-disabled, .ui-multiselect-optgroup-label)').first();

      // if at the first/last element
      if(!$next.length) {
        var $container = this.menu.find('ul').last();

        // move to the first/last
        this.menu.find('label')[ moveToLast ? 'last' : 'first' ]().trigger('mouseover');

        // set scroll position
        $container.scrollTop(moveToLast ? $container.height() : 0);

      } else {
        $next.find('label').trigger('mouseover');
      }
    },

    // This is an internal function to toggle the checked property and
    // other related attributes of a checkbox.
    //
    // The context of this function should be a checkbox; do not proxy it.
    _toggleState: function(prop, flag) {
      return function() {
        if(!this.disabled) {
          this[ prop ] = flag;
        }

        if(flag) {
          this.setAttribute('aria-selected', true);
        } else {
          this.removeAttribute('aria-selected');
        }
      };
    },

    _toggleChecked: function(flag, group) {
      var $inputs = (group && group.length) ?  group : this.inputs;
      var self = this;

      // toggle state on inputs
      $inputs.each(this._toggleState('checked', flag));

      // give the first input focus
      $inputs.eq(0).focus();

      // update button text
      this.update();

      // gather an array of the values that actually changed
      var values = $inputs.map(function() {
        return this.value;
      }).get();

      // toggle state on original option tags
      this.element
        .find('option')
        .each(function() {
          if(!this.disabled && $.inArray(this.value, values) > -1) {
            self._toggleState('selected', flag).call(this);
          }
        });

      // trigger the change event on the select
      if($inputs.length) {
        this.element.trigger("change");
      }
    },

    _toggleDisabled: function(flag) {
      this.button.attr({'disabled':flag, 'aria-disabled':flag})[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');

      var inputs = this.menu.find('input');
      var key = "ech-multiselect-disabled";

      if(flag) {
        // remember which elements this widget disabled (not pre-disabled)
        // elements, so that they can be restored if the widget is re-enabled.
        inputs = inputs.filter(':enabled').data(key, true)
      } else {
        inputs = inputs.filter(function() {
          return $.data(this, key) === true;
        }).removeData(key);
      }

      inputs
        .attr({'disabled':flag, 'arial-disabled':flag})
        .parent()[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');

      this.element.attr({
        'disabled':flag,
        'aria-disabled':flag
      });
    },

    // open the menu
    open: function(e) {
      var self = this;
      var button = this.button;
      var menu = this.menu;
      var speed = this.speed;
      var o = this.options;
      var args = [];

      // bail if the multiselectopen event returns false, this widget is disabled, or is already open
      if(this._trigger('beforeopen') === false || button.hasClass('ui-state-disabled') || this._isOpen) {
        return;
      }

      var $container = menu.find('ul').last();
      var effect = o.show;

      // figure out opening effects/speeds
      if($.isArray(o.show)) {
        effect = o.show[0];
        speed = o.show[1] || self.speed;
      }

      // if there's an effect, assume jQuery UI is in use
      // build the arguments to pass to show()
      if(effect) {
        args = [ effect, speed ];
      }

      // set the scroll of the checkbox container
      $container.scrollTop(0).height(o.height);

      // positon
      this.position();

      // show the menu, maybe with a speed/effect combo
      $.fn.show.apply(menu, args);

      // select the first not disabled option
      // triggering both mouseover and mouseover because 1.4.2+ has a bug where triggering mouseover
      // will actually trigger mouseenter.  the mouseenter trigger is there for when it's eventually fixed
      this.labels.filter(':not(.ui-state-disabled)').eq(0).trigger('mouseover').trigger('mouseenter').find('input').trigger('focus');

      button.addClass('ui-state-active');
      this._isOpen = true;
      this._trigger('open');
    },

    // close the menu
    close: function() {
      if(this._trigger('beforeclose') === false) {
        return;
      }

      var o = this.options;
      var effect = o.hide;
      var speed = this.speed;
      var args = [];

      // figure out opening effects/speeds
      if($.isArray(o.hide)) {
        effect = o.hide[0];
        speed = o.hide[1] || this.speed;
      }

      if(effect) {
        args = [ effect, speed ];
      }

      $.fn.hide.apply(this.menu, args);
      this.button.removeClass('ui-state-active').trigger('blur').trigger('mouseleave');
      this._isOpen = false;
      this._trigger('close');
    },

    enable: function() {
      this._toggleDisabled(false);
    },

    disable: function() {
      this._toggleDisabled(true);
    },

    checkAll: function(e) {
      this._toggleChecked(true);
      this._trigger('checkAll');
    },

    uncheckAll: function() {
      this._toggleChecked(false);
      this._trigger('uncheckAll');
    },

    getChecked: function() {
      return this.menu.find('input').filter(':checked');
    },

    destroy: function() {
      // remove classes + data
      $.Widget.prototype.destroy.call(this);

      // unbind events
      $doc.unbind(this._namespaceID);

      this.button.remove();
      this.menu.remove();
      this.element.show();

      return this;
    },

    isOpen: function() {
      return this._isOpen;
    },

    widget: function() {
      return this.menu;
    },

    getButton: function() {
      return this.button;
    },

    position: function() {
      var o = this.options;

      // use the position utility if it exists and options are specifified
      if($.ui.position && !$.isEmptyObject(o.position)) {
        o.position.of = o.position.of || this.button;

        this.menu
          .show()
          .position(o.position)
          .hide();

        // otherwise fallback to custom positioning
      } else {
        var pos = this.button.offset();

        this.menu.css({
          top: pos.top + this.button.outerHeight(),
          left: pos.left
        });
      }
    },

    // react to option changes after initialization
    _setOption: function(key, value) {
      var menu = this.menu;

      switch(key) {
        case 'header':
          menu.find('div.ui-multiselect-header')[value ? 'show' : 'hide']();
          break;
        case 'checkAllText':
          menu.find('a.ui-multiselect-all span').eq(-1).text(value);
          break;
        case 'uncheckAllText':
          menu.find('a.ui-multiselect-none span').eq(-1).text(value);
          break;
        case 'height':
          menu.find('ul').last().height(parseInt(value, 10));
          break;
        case 'minWidth':
          this.options[key] = parseInt(value, 10);
          this._setButtonWidth();
          this._setMenuWidth();
          break;
        case 'selectedText':
        case 'selectedList':
        case 'noneSelectedText':
          this.options[key] = value; // these all needs to update immediately for the update() call
          this.update();
          break;
        case 'classes':
          menu.add(this.button).removeClass(this.options.classes).addClass(value);
          break;
        case 'multiple':
          menu.toggleClass('ui-multiselect-single', !value);
          this.options.multiple = value;
          this.element[0].multiple = value;
          this.refresh();
          break;
        case 'position':
          this.position();
      }

      $.Widget.prototype._setOption.apply(this, arguments);
    }

  });
//  var multiselectID = 0;
//  var $doc = $(document);
// var self1;
// var self11;
// var elementid;
// var onclikad='false';
// var loadingtimead=false
//  var valselectad=false
//var mapad = new Object();
//var value1;
//var value5;
//var totalSelectOptionad=0;
//   $.widget("ech.multiselect1", {
//
//    // default options
//    options: {
//      header: true,
//      height: 175,
//      minWidth: 225,
//      classes: '',
//      checkAllText: 'Check all',
//      uncheckAllText: 'Uncheck all',
//      noneSelectedText: 'Select filters',
//      selectedText: '# selected',
//      selectedList: 0,
//      show: null,
//      hide: null,
//      autoOpen: false,
//      multiple: true,
//      position: {},
//      appendTo: "body"
//    },
//
//    _create: function() {
//      var el = this.element.hide();
//      var o = this.options;
//loadingtimead=true
//valselectad=true
//      this.speed = $.fx.speeds._default; // default speed for effects
//      this._isOpen = false; // assume no
//
//      // create a unique namespace for events that the widget
//      // factory cannot unbind automatically. Use eventNamespace if on
//      // jQuery UI 1.9+, and otherwise fallback to a custom string.
//      this._namespaceID = this.eventNamespace || ('multiselect' + multiselectID);
//
//  var el1 = this.element;
//
//    var $this = $(this);
//var id = el1.attr('id') || multiselectID++; // unique ID for the label & option tags
// id=id.split("__")[1];
//id=id.replace("ad", "");
// var namevalue=id.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
//     elementid = el1.attr('name');
//     var totalvalue=id+"__"+elementid+"__"+$this;
//     var adidbuttin='multiselecttad' + elementid
//     var scrollid='adui-helper-reset' + elementid
//var adcheckall='adui-multiselect-all' + elementid
//var aduncheckall='adui-multiselect-close' + elementid
//  mapad[adidbuttin] = this;
//   var ctxPath=parent.document.getElementById("h").value;
////      var button = (this.button = $('<button id="'+adidbuttin+'" title="'+namevalue+'" value="'+totalvalue+'" style="width:200px; background:#1B3E70; color:white;"  type="button"><span><img  style="width: 11px; height: 11px;float:right;" src="'+ctxPath+'/images/arrow_down.png"></span></button>'))
//      var button = (this.button = $('<button id="'+adidbuttin+'" title="'+namevalue+'" value="'+totalvalue+'" style="width:200px;height:27px; color:white;"  type="button"><span style="float:right;margin-right:17px"><i class="fa fa-angle-down" style="color:#ccc;font-size:large"></i></span></button>'))
//        .addClass('ui-multiselect ui-widget ui-state-default ui-corner-all')
//        .addClass(o.classes)
//        .attr({'title':el.attr('title'), 'aria-haspopup':true, 'tabIndex':el.attr('tabIndex')})
//        .insertAfter(el),
//
//        buttonlabel = (this.buttonlabel = $('<span class="gFontFamily gFontSize12" style="color: #555;" />'))
//          .html(o.noneSelectedText)
//          .appendTo(button),
//
//        menu = (this.menu = $('<div />'))
//          .addClass('ui-multiselect-menu ui-widget ui-widget-content ui-corner-all')
//          .addClass(o.classes)
//          .appendTo($(o.appendTo)),
//
//        header = (this.header = $('<div />'))
//          .addClass('ui-widget-header ui-corner-all ui-multiselect-header ui-helper-clearfix')
//          .appendTo(menu),
//
//        headerLinkContainer = (this.headerLinkContainer = $('<ul />'))
//          .addClass('ui-helper-reset')
//          .html(function() {
//            if(o.header === true) {
//              return '<li id="'+adcheckall+'" style="width:40%;"><a class="ui-multiselect-all" href="#" style="color: white;"><span class="ui-icon ui-icon-check"></span><span>' + o.checkAllText + '</span></a></li><li id="'+aduncheckall+'" style="width:50%;"><a class="ui-multiselect-none" href="#" style="color: white;"><span class="ui-icon ui-icon-closethick"></span><span>' + o.uncheckAllText + '</span></a></li>';
//            } else if(typeof o.header === "string") {
//              return '<li>' + o.header + '</li>';
//            } else {
//              return '';
//            }
//          })
//          .append('<li class="ui-multiselect-close"><a href="#" class="ui-multiselect-close"><span class="ui-icon ui-icon-circle-close"></span></a></li>')
//          .appendTo(header),
//
//        checkboxContainer = (this.checkboxContainer = $('<ul id="'+scrollid+'" />'))
//          .addClass('ui-multiselect-checkboxes ui-helper-reset')
//          .appendTo(menu);
//$("#"+scrollid).on('scroll',function(){
////     setTimeout(parent.onScrollDivreport(this),2000);
//     setTimeout(parent.onScrollDivtable(this),2000);
////      setTimeout($.proxy(self1.refresh, self1), 2000);
//var  scHeight=(this.scrollHeight);
//  var   scTop=(this.scrollTop);
//  var   clHgt=(this.clientHeight);
//
//        var selfgr=mapad[adidbuttin];
//        if(scTop==(scHeight-clHgt)){
//loadingtimead=false
//valselectad=true
//   var flag= parent.$("#multigblrefresh").val();
//   if(flag=="true"){
//
//      setTimeout($.proxy(selfgr.refresh, selfgr), 2000);
//   }
//        }
//})
//var oldobj=this;
//
//$("#"+adidbuttin).mouseover(function(){
//    var idvalue= $("#"+adidbuttin).val()
//var id1=idvalue.split("__")[0];
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
//    var pos = button.offset();
//    var left=pos.left
//     var top=pos.top + button.outerHeight()
//    parent.showfilters(elementid,"graph","event",left,top)
//})
//$("#"+adidbuttin).mouseout(function(){
//   parent.hidefilters()
//})
//$("#"+adidbuttin).click(function(){
//   var idvalue= $("#"+adidbuttin).val()
//var id1=idvalue.split("__")[0];
//id1="ad"+id1;
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
//self11=mapad[adidbuttin];
// self1 = self11;
// loadingtimead=false
// valselectad=true
//  setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"advance"),2000);
////    setTimeout(parent.getlovfiltersreport(id1,elementid1,self11),2000);
////    onclikad='true'
//
////         setTimeout($.proxy(self11.refresh, self11), 2000);
////    }
//
//
//})
//$("#"+aduncheckall).click(function(){
//var idvalue= $("#"+adidbuttin).val()
//var name=idvalue.split("__")[0];
//name=name.replace("ad", " ");
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
// setTimeout(parent.setallfilters(name,elementid,"uncheckall","advance"),2000);
//})
//$("#"+adcheckall).click(function(){
//var idvalue= $("#"+adidbuttin).val()
//var name=idvalue.split("__")[0];
//name=name.replace("ad", " ");
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
//loadingtimead=false
// setTimeout(parent.setallfilters(name,elementid,"checkall","advance"),2000);
//})
//        // perform event bindings
//        this._bindEvents();
//
//        // build menu
//        this.refresh(true);
//
//        // some addl. logic for single selects
//        if(!o.multiple) {
//          menu.addClass('ui-multiselect-single');
//        }
//
//        // bump unique ID
//        multiselectID++;
//    },
//
//    _init: function() {
//      if(this.options.header === false) {
//        this.header.hide();
//      }
//      if(!this.options.multiple) {
//        this.headerLinkContainer.find('.ui-multiselect-all, .ui-multiselect-none').hide();
//      }
//      if(this.options.autoOpen) {
//        this.open();
//      }
//      if(this.element.is(':disabled')) {
//        this.disable();
//      }
//    },
//
//    refresh: function(init) {
//         var flag= parent.$("#multigblrefresh").val();
//        if(flag=="reset"){
//            valselectad=true
//        }else{
//            if(flag=="search"){
//                valselectad=true
//            loadingtimead=false
//            }
//           else if(flag=="filter"){
//            valselectad=true
//            loadingtimead=true
//        }
//        }
//        if(valselectad){
//      var el = this.element;
//      var o = this.options;
//      var menu = this.menu;
//      var checkboxContainer = this.checkboxContainer;
//      var optgroups = [];
//         var self = this;
////      self1 = self;
//      var html = "";
//     var id = el.attr('id') || multiselectID++; // unique ID for the label & option tags
//    id=id.split("__")[1];
//      // unique ID for the label & option tags
////     var idbuttin='multiselectt' + elementid;
////   var idvalue= $("#"+idbuttin).val();
////id=id.replace("ad", "");
//   if(onclikad!=""&& onclikad=='true'){
//el= self11.element;
//o = self11.options;
// menu = self11.menu;
// checkboxContainer = self11.checkboxContainer;
//   self = self11;
//   }
//
//
//
//      // build items
//      el.find('option').each(function(i) {
//        var $this = $(this);
//        var parent = this.parentNode;
//        var description = this.innerHTML;
//        var title = this.title;
//        var filterselect;
//        var value = this.value;
//        var index=value.split("_")[1];
//        if(value.split("_")[3]=='selecttrue'){
//           filterselect = value.split("_")[3];
//            value=value.replace('_selecttrue', '');
//        }
//        value=value.replace('2q2', '_');
//        var inputID = 'ui-multiselect-' + (this.id|| id + '-option-' + index);
//        var isDisabled = this.disabled;
//        var isSelected = this.selected;
//        var labelClasses = [ 'ui-corner-all' ];
//        var liClasses = (isDisabled ? 'ui-multiselect-disabled ' : ' ') + this.className;
//        var optLabel;
//
//        // is this an optgroup?
//        if(parent.tagName === 'OPTGROUP') {
//          optLabel = parent.getAttribute('label');
//
//          // has this optgroup been added already?
//          if($.inArray(optLabel, optgroups) === -1) {
//            html += '<li class="ui-multiselect-optgroup-label ' + parent.className + '"><a href="#">' + optLabel + '</a></li>';
//            optgroups.push(optLabel);
//          }
//        }
//
//        if(isDisabled) {
//          labelClasses.push('ui-state-disabled');
//        }
//
//        // browsers automatically select the first option
//        // by default with single selects
//        if(isSelected && !o.multiple) {
//          labelClasses.push('ui-state-active');
//        }
//
//        html += '<li class="' + liClasses + '">';
//var idval=(this.id.replace("ad", "") || id.replace("ad", ""))+"_"+index;
//var nameval=(this.id.replace("ad", "") || id.replace("ad", ""))+"*,"+value.split("_")[2];
//        // create the label
//        html += '<label style="width:95%;" for="' + inputID + '" title="' + title + '" class="' + labelClasses.join(' ') + '">';
////        html += '<input id="' + inputID + '" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" title="' + title + '"';
//        html += '<input id="' + inputID + '" style="height:10px;float:left;" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" onclick=parent.applyGlobalFilterinreport("'+idval+ '","' + nameval + '","'+inputID+'") title="' + title + '"';
//
//        // pre-selected?
//        if(isSelected) {
//          html += ' checked="checked"';
//          html += ' aria-selected="true"';
//        }
//
//        // disabled?
//        if(isDisabled) {
//          html += ' disabled="disabled"';
//          html += ' aria-disabled="true"';
//        }
//if(filterselect=='selecttrue'){
//    html += ' checked="checked"';
//          html += ' aria-selected="true"';
//}
//        // add the title and close everything off
//        html += ' /><span style="float:left;width:80%;">' + description + '</span></label></li>';
////        totalSelectOptionad=i+1;
//      });
//
//      // insert into the DOM
//      checkboxContainer.html(html);
//
//      // cache some moar useful elements
//      this.labels = menu.find('label');
//      this.inputs = this.labels.children('input');
//
//      // set widths
//      this._setButtonWidth();
//      this._setMenuWidth();
//
//      // remember default value
//      this.button[0].defaultValue = this.update();
//
//      // broadcast refresh event; useful for widgets
//      if(!init) {
//        this._trigger('refresh');
//      }
//    }
//    },
//
//    // updates the button text. call refresh() to rebuild
//    update: function() {
//      var o = this.options;
//      var $inputs = this.inputs;
//      var el = this.element;
//          elementid = el.attr('name')
//       var adidbuttin='multiselecttad' + elementid
//self1=mapad[adidbuttin];
//$inputs = self1.inputs;
//      var $checked = $inputs.filter(':checked');
//      var numChecked = $checked.length;
//      var value;
//// var el = this.element;
//   el.find('option').each(function(i) {
// totalSelectOptionad=i+1;
//           });
//      var countt=0;
//      if(numChecked === 0) {
//        value = o.noneSelectedText;
//        var iddd3=o.noneSelectedText.replace(' ', '1q1');
//          iddd3=iddd3.replace(' ', '1q1')
//          iddd3=iddd3.replace(' ', '1q1')
//        iddd3="ad"+iddd3; iddd3=elementid+"__"+iddd3;
////        if(loadingtimead){
////                            loadingtimead=false
//                            $("#multiselecttad"+$('#'+iddd3).attr('name')).css({'background': '#fff','color':'white'});
////        }else{
////        $("#multiselecttad"+$('#'+iddd3).attr('name')).css({'background': 'grey','color':'white'});
////        }
//var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//        value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
//      } else {
//        if($.isFunction(o.selectedText)) {
//          value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
//          if(numChecked==totalSelectOptionad){
//
//                        if(loadingtimead){
////                            loadingtimead=false
//                             value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
//                        }else{
//                            var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
////                       value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//                        }
//                    }else{
//           if(value.length>20 || value.length>20){
//    value=value.substring(0,20)+".."
//}
//                    }
//                    var iddd=o.noneSelectedText.replace(' ', '1q1')
//            iddd=iddd.replace(' ', '1q1')
//            iddd=iddd.replace(' ', '1q1')
// iddd="ad"+iddd;iddd=elementid+"__"+iddd;
//               if(numChecked==totalSelectOptionad){
//                      if(loadingtimead){
////                            loadingtimead=false
//                             $("#multiselecttad"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
//               }else{ loadingtimead=false
//   $("#multiselecttad"+$('#'+iddd).attr('name')).css({'background': '#1B3E70','color':'white'});
//               }
//                    }else{
//         $("#multiselecttad"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
//                    }
//        } else if(/\d/.test(o.selectedList) && o.selectedList > 0 && numChecked <= o.selectedList) {
//          value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
//          value1 = $checked.map(function() {return $(this).next().html();}).get();
//         if(numChecked==totalSelectOptionad){
//             var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//      
//value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
////                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//                    }else{
//           if(value.length>20 || value.length>20){
//    value=value.substring(0,20)+".."
//}
//                    }
////          alert("ddd "+o.noneSelectedText)
//          var iddd=o.noneSelectedText.replace(' ', '1q1');
//            iddd=iddd.replace(' ', '1q1')
//            iddd=iddd.replace(' ', '1q1')
//           iddd="ad"+iddd;iddd=elementid+"__"+iddd;
//            if(numChecked==totalSelectOptionad){
//if(loadingtimead){
////                            loadingtimead=false
//                             value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
//                                if(value.length>20 || value.length>20){
//    value=value.substring(0,20)+".."
//}
//                              $("#multiselecttad"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
// }else{
//      var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//     value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
////                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
////                           if(value.length>15 || value.length>15){
////    value=value.substring(0,10)+".."
////}
//   $("#multiselecttad"+$('#'+iddd).attr('name')).css({'background': '#1B3E70','color':'white'});
// }
//                    }else{
//          $("#multiselecttad"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
//                    }
////          alert("value1 : "+$checked.map(function() {return $(this).next().html();}))
////          alert("value2 : "+$checked.map(function() {return $(this).next().html();}).get())
////          alert("value3 : "+$checked.toString())
//
//        } else {
////            alert("attribute "+this.getAttribute('aria-selected'));
////            alert("attribute "+this.attr('aria-selected'));
////                    alert("$inputs.length "+$inputs.length)
////                    alert("$checked.get() "+$checked.get().toString())
////                    alert("o.noneSelectedText "+o.noneSelectedText)
////                    alert($('#'+o.noneSelectedText).attr('name'))
//                    var iddd1=o.noneSelectedText.replace(' ', '1q1');
//                     iddd1=iddd1.replace(' ', '1q1')
//                     iddd1=iddd1.replace(' ', '1q1')
//                        iddd1="ad"+iddd1;iddd1=elementid+"__"+iddd1;
////                    alert("iddd1 "+iddd1)
////          $("#multiselecttad"+$('#'+iddd1).attr('name')).css({'background': '#B36666','color':'white'});
//
////                    alert("o.selectedList "+o.selectedList)
////                    alert("o.noneSelectedText "+o.noneSelectedText)
////                    alert("numChecked "+numChecked)
////                    alert("$checked.html() "+$checked.html())
//
//                    countt++;
////                    alert("cllllllllllaass"+$(this).hasClass('ui-multiselect-all'));
////                    alert("numChecked ooooo   "+numChecked)
////                    alert("idd   "+totalSelectOption);
//
//                    if(numChecked==totalSelectOptionad){
//
//                         if(loadingtimead){
////                            loadingtimead=false
//                             $("#multiselecttad"+$('#'+iddd1).attr('name')).css({'background': 'ligghtgray','color':'white'});
//                              var nvalues=value=$checked.map(function() {
//                            return $(this).next().html();
//                        }).get();
//
//                               if(nvalues[0].length>20 || nvalues[1].length>20){
//    value=nvalues[0].substring(0,20)+".."
//}else{
//                               if(nvalues[0].length<10 || nvalues[1].length<10){
//          value=nvalues[0].substring(0,10)+","+nvalues[1].substring(0,5)+", More.."
//     }else{
//                                value=nvalues[0]+", More.."
//}
//}
//                    }else{ loadingtimead=true
//  $("#multiselecttad"+$('#'+iddd1).attr('name')).css({'background': '#1B3E70','color':'white'});
////                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//                        value = o.selectedText.replace('of # selected',o.noneSelectedText+":All").replace('#', '');
//                    }
//                    }else{
//                          $("#multiselecttad"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});
////                        value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
////                            if(numChecked<3){
////                        value=$checked.map(function() {
////                            return $(this).next().html();
////                        }).get()+"...";
//////                        value5=value1+"...";
////                            }else{
    //
//                                var nvalues=value=$checked.map(function() {
//                            return $(this).next().html();
//                        }).get();
//
//                               if(nvalues[0].length>20 || nvalues[1].length>20){
//    value=nvalues[0].substring(0,20)+".."
//}else{
//                                if(nvalues[0].length<10 || nvalues[1].length<10){
//          value=nvalues[0].substring(0,10)+","+nvalues[1].substring(0,5)+", More.."
//     }else{
//                                value=nvalues[0]+", More.."
//}
//}
//
////                        alert("nval "+nvalues[1])
////                                }
////                            }
////                            value=value5;
//        }
////                    alert("cllllllllllaass  iddd   "+$(this).hasClass('ui-multiselect-all').id);
////                    edited by manik
////                             value = o.selectedText.replace('#', numChecked).replace('#', $inputs.length);
////alert("valu555 : "+$checked.map(function() {return $(this).next().html();}).get())
////                        value=$checked.map(function() {return $(this).next().html();}).get()+"...";
////                        value=value1+"..."+numChecked+" More";
////                        value=value1+"...";
////                             value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
////                    if(countt==1){
////                        value = $checked.map(function() {
////                            return $(this).next();
////                        });
//////                            value=o.selectedText.append("...More");
//////                        alert("value11 :"+value)
////                        alert("o.selectedText  :"+o.selectedText)
////                        alert("map  :"+$checked.map(function() {return $(this).next().html();}))
//////                        alert("o.noneSelectedText :"+o.noneSelectedText)
////                    }
////          alert("value :"+value)
//      }
//      }
//value=value.replace('&amp;', '&')
//      this._setButtonValue(value);
//valselectad=false
//      return value;
//    },
//
//    // this exists as a separate method so that the developer
//    // can easily override it.
//    _setButtonValue: function(value) {
//      this.buttonlabel.text(value);
//    },
//
//    // binds events
//    _bindEvents: function() {
//      var self = this;
//      var button = this.button;
//
//      function clickHandler() {
//        self[ self._isOpen ? 'close' : 'open' ]();
//        return false;
//      }
//
//      // webkit doesn't like it when you click on the span :(
//      button
//        .find('span')
//        .bind('click.multiselect1', clickHandler);
//
//      // button events
//      button.bind({
//        click: clickHandler,
//        keypress: function(e) {
//          switch(e.which) {
//            case 27: // esc
//              case 38: // up
//              case 37: // left
//              self.close();
//            break;
//            case 39: // right
//              case 40: // down
//              self.open();
//            break;
//          }
//        },
//        mouseenter: function() {
//          if(!button.hasClass('ui-state-disabled')) {
//            $(this).addClass('ui-state-hover');
//          }
//        },
//        mouseleave: function() {
//          $(this).removeClass('ui-state-hover');
//        },
//        focus: function() {
//          if(!button.hasClass('ui-state-disabled')) {
//            $(this).addClass('ui-state-focus');
//          }
//        },
//        blur: function() {
//          $(this).removeClass('ui-state-focus');
//        }
//      });
//
//      // header links
//      this.header.delegate('a', 'click.multiselect1', function(e) {
//        // close link
//        if($(this).hasClass('ui-multiselect-close')) {
//          self.close();
//
//          // check all / uncheck all
//        } else {
//          self[$(this).hasClass('ui-multiselect-all') ? 'checkAll' : 'uncheckAll']();
//        }
//
//        e.preventDefault();
//      });
//
//      // optgroup label toggle support
//      this.menu.delegate('li.ui-multiselect-optgroup-label a', 'click.multiselect', function(e) {
//        e.preventDefault();
//
//        var $this = $(this);
//        var $inputs = $this.parent().nextUntil('li.ui-multiselect-optgroup-label').find('input:visible:not(:disabled)');
//        var nodes = $inputs.get();
//        var label = $this.parent().text();
//
//        // trigger event and bail if the return is false
//        if(self._trigger('beforeoptgrouptoggle', e, {inputs:nodes, label:label}) === false) {
//          return;
//        }
//
//        // toggle inputs
//        self._toggleChecked(
//          $inputs.filter(':checked').length !== $inputs.length,
//          $inputs
//        );
//
//        self._trigger('optgrouptoggle', e, {
//          inputs: nodes,
//          label: label,
//          checked: nodes[0].checked
//        });
//      })
//      .delegate('label', 'mouseenter.multiselect1', function() {
//        if(!$(this).hasClass('ui-state-disabled')) {
//          self.labels.removeClass('ui-state-hover');
//          $(this).addClass('ui-state-hover').find('input').focus();
//        }
//      })
//      .delegate('label', 'keydown.multiselect1', function(e) {
//        e.preventDefault();
//
//        switch(e.which) {
//          case 9: // tab
//            case 27: // esc
//            self.close();
//          break;
//          case 38: // up
//            case 40: // down
//            case 37: // left
//            case 39: // right
//            self._traverse(e.which, this);
//          break;
//          case 13: // enter
//            $(this).find('input')[0].click();
//          break;
//        }
//      })
//      .delegate('input[type="checkbox"], input[type="radio"]', 'click.multiselect1', function(e) {
//        var $this = $(this);
//        var val = this.value;
//        var checked = this.checked;
//        var tags = self.element.find('option');
//
//        // bail if this input is disabled or the event is cancelled
//        if(this.disabled || self._trigger('click', e, {value: val, text: this.title, checked: checked}) === false) {
//          e.preventDefault();
//          return;
//        }
//
//        // make sure the input has focus. otherwise, the esc key
//        // won't close the menu after clicking an item.
//        $this.focus();
//
//        // toggle aria state
//        $this.attr('aria-selected', checked);
//
//        // change state on the original option tags
//        tags.each(function() {
//          if(this.value === val) {
//            this.selected = checked;
//          } else if(!self.options.multiple) {
//            this.selected = false;
//          }
//        });
//
//        // some additional single select-specific logic
//        if(!self.options.multiple) {
//          self.labels.removeClass('ui-state-active');
//          $this.closest('label').toggleClass('ui-state-active', checked);
//
//          // close menu
//          self.close();
//        }
//
//        // fire change on the select box
//        self.element.trigger("change");
//
//        // setTimeout is to fix multiselect issue #14 and #47. caused by jQuery issue #3827
//        // http://bugs.jquery.com/ticket/3827
//        setTimeout($.proxy(self.update, self), 10);
//      });
//
//      // close each widget when clicking on any other element/anywhere else on the page
//      $doc.bind('mousedown.' + this._namespaceID, function(event) {
//        var target = event.target;
//
//        if(self._isOpen
//            && target !== self.button[0]
//            && target !== self.menu[0]
//            && !$.contains(self.menu[0], target)
//            && !$.contains(self.button[0], target)
//          ) {
//          self.close();
//        }
//      });
//
//      // deal with form resets.  the problem here is that buttons aren't
//      // restored to their defaultValue prop on form reset, and the reset
//      // handler fires before the form is actually reset.  delaying it a bit
//      // gives the form inputs time to clear.
//      $(this.element[0].form).bind('reset.multiselect1', function() {
//        setTimeout($.proxy(self.refresh, self), 10);
//      });
//    },
//
//    // set button width
//    _setButtonWidth: function() {
//      var width = this.element.outerWidth();
//      var o = this.options;
//
//      if(/\d/.test(o.minWidth) && width < o.minWidth) {
//        width = o.minWidth;
//      }
//width=193;
//var size=parent.sizeoffiltersg;
//if(size>=6){
//    size=6;
//}
//var dvdvalue=size+".3";
//width=parseInt(((($(window).width()*.89)))/dvdvalue);
//      // set widths
//      this.button.outerWidth(width);
//    },
//
//    // set menu width
//    _setMenuWidth: function() {
//      var m = this.menu;
//      m.outerWidth(this.button.outerWidth());
//    },
//
//    // move up or down within the menu
//    _traverse: function(which, start) {
//      var $start = $(start);
//      var moveToLast = which === 38 || which === 37;
//
//      // select the first li that isn't an optgroup label / disabled
//      var $next = $start.parent()[moveToLast ? 'prevAll' : 'nextAll']('li:not(.ui-multiselect-disabled, .ui-multiselect-optgroup-label)').first();
//
//      // if at the first/last element
//      if(!$next.length) {
//        var $container = this.menu.find('ul').last();
//
//        // move to the first/last
//        this.menu.find('label')[ moveToLast ? 'last' : 'first' ]().trigger('mouseover');
//
//        // set scroll position
//        $container.scrollTop(moveToLast ? $container.height() : 0);
//
//      } else {
//        $next.find('label').trigger('mouseover');
//      }
//    },
//
//    // This is an internal function to toggle the checked property and
//    // other related attributes of a checkbox.
//    //
//    // The context of this function should be a checkbox; do not proxy it.
//    _toggleState: function(prop, flag) {
//      return function() {
//        if(!this.disabled) {
//          this[ prop ] = flag;
//        }
//
//        if(flag) {
//          this.setAttribute('aria-selected', true);
//        } else {
//          this.removeAttribute('aria-selected');
//        }
//      };
//    },
//
//    _toggleChecked: function(flag, group) {
//      var $inputs = (group && group.length) ?  group : this.inputs;
//      var self = this;
//
//      // toggle state on inputs
//      $inputs.each(this._toggleState('checked', flag));
//
//      // give the first input focus
//      $inputs.eq(0).focus();
//
//      // update button text
//      this.update();
//
//      // gather an array of the values that actually changed
//      var values = $inputs.map(function() {
//        return this.value;
//      }).get();
//
//      // toggle state on original option tags
//      this.element
//        .find('option')
//        .each(function() {
//          if(!this.disabled && $.inArray(this.value, values) > -1) {
//            self._toggleState('selected', flag).call(this);
//          }
//        });
//
//      // trigger the change event on the select
//      if($inputs.length) {
//        this.element.trigger("change");
//      }
//    },
//
//    _toggleDisabled: function(flag) {
//      this.button.attr({'disabled':flag, 'aria-disabled':flag})[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');
//
//      var inputs = this.menu.find('input');
//      var key = "ech-multiselect-disabled";
//
//      if(flag) {
//        // remember which elements this widget disabled (not pre-disabled)
//        // elements, so that they can be restored if the widget is re-enabled.
//        inputs = inputs.filter(':enabled').data(key, true)
//      } else {
//        inputs = inputs.filter(function() {
//          return $.data(this, key) === true;
//        }).removeData(key);
//      }
//
//      inputs
//        .attr({'disabled':flag, 'arial-disabled':flag})
//        .parent()[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');
//
//      this.element.attr({
//        'disabled':flag,
//        'aria-disabled':flag
//      });
//    },
//
//    // open the menu
//    open: function(e) {
//      var self = this;
//      var button = this.button;
//      var menu = this.menu;
//      var speed = this.speed;
//      var o = this.options;
//      var args = [];
//
//      // bail if the multiselectopen event returns false, this widget is disabled, or is already open
//      if(this._trigger('beforeopen') === false || button.hasClass('ui-state-disabled') || this._isOpen) {
//        return;
//      }
//
//      var $container = menu.find('ul').last();
//      var effect = o.show;
//
//      // figure out opening effects/speeds
//      if($.isArray(o.show)) {
//        effect = o.show[0];
//        speed = o.show[1] || self.speed;
//      }
//
//      // if there's an effect, assume jQuery UI is in use
//      // build the arguments to pass to show()
//      if(effect) {
//        args = [ effect, speed ];
//      }
//
//      // set the scroll of the checkbox container
//      $container.scrollTop(0).height(o.height);
//
//      // positon
//      this.position();
//
//      // show the menu, maybe with a speed/effect combo
//      $.fn.show.apply(menu, args);
//
//      // select the first not disabled option
//      // triggering both mouseover and mouseover because 1.4.2+ has a bug where triggering mouseover
//      // will actually trigger mouseenter.  the mouseenter trigger is there for when it's eventually fixed
//      this.labels.filter(':not(.ui-state-disabled)').eq(0).trigger('mouseover').trigger('mouseenter').find('input').trigger('focus');
//
//      button.addClass('ui-state-active');
//      this._isOpen = true;
//      this._trigger('open');
//    },
//
//    // close the menu
//    close: function() {
//      if(this._trigger('beforeclose') === false) {
//        return;
//      }
//
//      var o = this.options;
//      var effect = o.hide;
//      var speed = this.speed;
//      var args = [];
//
//      // figure out opening effects/speeds
//      if($.isArray(o.hide)) {
//        effect = o.hide[0];
//        speed = o.hide[1] || this.speed;
//      }
//
//      if(effect) {
//        args = [ effect, speed ];
//      }
//
//      $.fn.hide.apply(this.menu, args);
//      this.button.removeClass('ui-state-active').trigger('blur').trigger('mouseleave');
//      this._isOpen = false;
//      this._trigger('close');
//    },
//
//    enable: function() {
//      this._toggleDisabled(false);
//    },
//
//    disable: function() {
//      this._toggleDisabled(true);
//    },
//
//    checkAll: function(e) {
//      this._toggleChecked(true);
//      this._trigger('checkAll');
//    },
//
//    uncheckAll: function() {
//      this._toggleChecked(false);
//      this._trigger('uncheckAll');
//    },
//
//    getChecked: function() {
//      return this.menu.find('input').filter(':checked');
//    },
//
//    destroy: function() {
//      // remove classes + data
//      $.Widget.prototype.destroy.call(this);
//
//      // unbind events
//      $doc.unbind(this._namespaceID);
//
//      this.button.remove();
//      this.menu.remove();
//      this.element.show();
//
//      return this;
//    },
//
//    isOpen: function() {
//      return this._isOpen;
//    },
//
//    widget: function() {
//      return this.menu;
//    },
//
//    getButton: function() {
//      return this.button;
//    },
//
//    position: function() {
//      var o = this.options;
//
//      // use the position utility if it exists and options are specifified
//      if($.ui.position && !$.isEmptyObject(o.position)) {
//        o.position.of = o.position.of || this.button;
//
//        this.menu
//          .show()
//          .position(o.position)
//          .hide();
//
//        // otherwise fallback to custom positioning
//      } else {
//        var pos = this.button.offset();
//
//        this.menu.css({
//          top: pos.top + this.button.outerHeight(),
//          left: pos.left
//        });
//      }
//    },
//
//    // react to option changes after initialization
//    _setOption: function(key, value) {
//      var menu = this.menu;
//
//      switch(key) {
//        case 'header':
//          menu.find('div.ui-multiselect-header')[value ? 'show' : 'hide']();
//          break;
//        case 'checkAllText':
//          menu.find('a.ui-multiselect-all span').eq(-1).text(value);
//          break;
//        case 'uncheckAllText':
//          menu.find('a.ui-multiselect-none span').eq(-1).text(value);
//          break;
//        case 'height':
//          menu.find('ul').last().height(parseInt(value, 10));
//          break;
//        case 'minWidth':
//          this.options[key] = parseInt(value, 10);
//          this._setButtonWidth();
//          this._setMenuWidth();
//          break;
//        case 'selectedText':
//        case 'selectedList':
//        case 'noneSelectedText':
//          this.options[key] = value; // these all needs to update immediately for the update() call
//          this.update();
//          break;
//        case 'classes':
//          menu.add(this.button).removeClass(this.options.classes).addClass(value);
//          break;
//        case 'multiple':
//          menu.toggleClass('ui-multiselect-single', !value);
//          this.options.multiple = value;
//          this.element[0].multiple = value;
//          this.refresh();
//          break;
//        case 'position':
//          this.position();
//      }
//
//      $.Widget.prototype._setOption.apply(this, arguments);
//    }
//
//  });

//   var multiselectID = 0;
//  var $doc = $(document);
// var self1;
// var self11;
// var elementid;
// var oncliktr='false';
// var loadingtimetr=false
// var valselecttr=false
//var maptr = new Object();
//var value1;
//var value5;
//var totalSelectOptiontr=0;
//$.widget("ech.multiselectTrend", {
//
//    // default options
//    options: {
//      header: true,
//      height: 175,
//      minWidth: 225,
//      classes: '',
//      checkAllText: 'Check all',
//      uncheckAllText: 'Uncheck all',
//      noneSelectedText: 'Select filters',
//      selectedText: '# selected',
//      selectedList: 0,
//      show: null,
//      hide: null,
//      autoOpen: false,
//      multiple: true,
//      position: {},
//      appendTo: "body"
//    },
//
//    _create: function() {
//      var el = this.element.hide();
//      var o = this.options;
//loadingtimetr=true
//valselecttr=true
//      this.speed = $.fx.speeds._default; // default speed for effects
//      this._isOpen = false; // assume no
//
//      // create a unique namespace for events that the widget
//      // factory cannot unbind automatically. Use eventNamespace if on
//      // jQuery UI 1.9+, and otherwise fallback to a custom string.
//      this._namespaceID = this.eventNamespace || ('multiselect' + multiselectID);
//
//  var el1 = this.element;
//
//    var $this = $(this);
//var id = el1.attr('id') || multiselectID++; // unique ID for the label & option tags
// id=id.split("__")[1];
//id=id.replace("Tr", "");
//var namevalue=id.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
//     elementid = el1.attr('name')
//     var totalvalue=id+"__"+elementid+"__"+$this;
//     var tridbuttin='multiselecttTr' + elementid
//      var scrollid='Trui-helper-reset' + elementid
//var Trcheckall='Trui-multiselect-all' + elementid
//var Truncheckall='Trui-multiselect-close' + elementid
//  maptr[tridbuttin] = this;
//      var button = (this.button = $('<button id="'+tridbuttin+'" title="'+namevalue+'" value="'+totalvalue+'" style="width:200px; background:#1B3E70; color:white;"  type="button"><span class="ui-icon ui-icon-triangle-1-s"></span></button>'))
//        .addClass('ui-multiselect ui-widget ui-state-default ui-corner-all')
//        .addClass(o.classes)
//        .attr({'title':el.attr('title'), 'aria-haspopup':true, 'tabIndex':el.attr('tabIndex')})
//        .insertAfter(el),
//
//        buttonlabel = (this.buttonlabel = $('<span />'))
//          .html(o.noneSelectedText)
//          .appendTo(button),
//
//        menu = (this.menu = $('<div />'))
//          .addClass('ui-multiselect-menu ui-widget ui-widget-content ui-corner-all')
//          .addClass(o.classes)
//          .appendTo($(o.appendTo)),
//
//        header = (this.header = $('<div />'))
//          .addClass('ui-widget-header ui-corner-all ui-multiselect-header ui-helper-clearfix')
//          .appendTo(menu),
//
//        headerLinkContainer = (this.headerLinkContainer = $('<ul />'))
//          .addClass('ui-helper-reset')
//          .html(function() {
//            if(o.header === true) {
//              return '<li id="'+Trcheckall+'"><a class="ui-multiselect-all" href="#" style="color: white;"><span class="ui-icon ui-icon-check"></span><span>' + o.checkAllText + '</span></a></li><li id="'+Truncheckall+'"><a class="ui-multiselect-none" href="#" style="color: white;"><span class="ui-icon ui-icon-closethick"></span><span>' + o.uncheckAllText + '</span></a></li>';
//            } else if(typeof o.header === "string") {
//              return '<li>' + o.header + '</li>';
//            } else {
//              return '';
//            }
//          })
//          .append('<li class="ui-multiselect-close"><a href="#" class="ui-multiselect-close"><span class="ui-icon ui-icon-circle-close"></span></a></li>')
//          .appendTo(header),
//
//        checkboxContainer = (this.checkboxContainer = $('<ul id="'+scrollid+'" />'))
//          .addClass('ui-multiselect-checkboxes ui-helper-reset')
//          .appendTo(menu);
//$("#"+scrollid).on('scroll',function(){
////     setTimeout(parent.onScrollDivreport(this),2000);
//     setTimeout(parent.onScrollDivtable(this),2000);
////      setTimeout($.proxy(self1.refresh, self1), 2000);
//var  scHeight=(this.scrollHeight);
//  var   scTop=(this.scrollTop);
//  var   clHgt=(this.clientHeight);
//        if(scTop==(scHeight-clHgt)){
// loadingtimetr=false
// valselecttr=true
// var flag= parent.$("#multigblrefresh").val();
//   if(flag=="true"){
//      setTimeout($.proxy(self1.refresh, self1), 10);
//        }
//        }
//})
//var oldobj=this;
//
//$("#"+tridbuttin).mouseover(function(){
//    var idvalue= $("#"+tridbuttin).val()
//var id1=idvalue.split("__")[0];
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
//    var pos = button.offset();
//    var left=pos.left
//     var top=pos.top + button.outerHeight()
//    parent.showfilters(elementid,"graph","event",left,top)
//})
//$("#"+tridbuttin).mouseout(function(){
//   parent.hidefilters()
//})
//$("#"+tridbuttin).click(function(){
//   var idvalue= $("#"+tridbuttin).val()
//var id1=idvalue.split("__")[0];
//id1="Tr"+id1;
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
//self11=maptr[tridbuttin];
// self1 = self11;
// loadingtimetr=false
// valselecttr=true
//  setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"trend"),2000);
////    setTimeout(parent.getlovfiltersreport(id1,elementid1,self11),2000);
////    oncliktr='true'
//
////         setTimeout($.proxy(self11.refresh, self11), 2000);
////    }
//
//
//})
//$("#"+Truncheckall).click(function(){
//var idvalue= $("#"+tridbuttin).val()
//var name=idvalue.split("__")[0];
//name=name.replace("Tr", " ");
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
// setTimeout(parent.setallfilters(name,elementid,"uncheckall","trend"),2000);
//})
//$("#"+Trcheckall).click(function(){
//var idvalue= $("#"+tridbuttin).val()
//var name=idvalue.split("__")[0];
//name=name.replace("Tr", " ");
//var elementid1=idvalue.split("__")[1];
//elementid=elementid1;
//loadingtimetr=false
// setTimeout(parent.setallfilters(name,elementid,"checkall","trend"),2000);
//})
//        // perform event bindings
//        this._bindEvents();
//
//        // build menu
//        this.refresh(true);
//
//        // some addl. logic for single selects
//        if(!o.multiple) {
//          menu.addClass('ui-multiselect-single');
//        }
//
//        // bump unique ID
//        multiselectID++;
//    },
//
//    _init: function() {
//      if(this.options.header === false) {
//        this.header.hide();
//      }
//      if(!this.options.multiple) {
//        this.headerLinkContainer.find('.ui-multiselect-all, .ui-multiselect-none').hide();
//      }
//      if(this.options.autoOpen) {
//        this.open();
//      }
//      if(this.element.is(':disabled')) {
//        this.disable();
//      }
//    },
//
//    refresh: function(init) {
//         var flag= parent.$("#multigblrefresh").val();
//        if(flag=="reset"){
//            valselecttr=true
//        }else{
//            if(flag=="search"){
//                valselecttr=true
//            loadingtimetr=false
//            }
//            else if(flag=="filter"){
//            valselecttr=true
//            loadingtimetr=true
//        }
//        }
//        if(valselecttr){
//      var el = this.element;
//      var o = this.options;
//      var menu = this.menu;
//      var checkboxContainer = this.checkboxContainer;
//      var optgroups = [];
//         var self = this;
////      self1 = self;
//      var html = "";
//     var id = el.attr('id') || multiselectID++; // unique ID for the label & option tags
//      id=id.split("__")[1];
//      // unique ID for the label & option tags
////     var idbuttin='multiselectt' + elementid;
////   var idvalue= $("#"+idbuttin).val();
//   if(oncliktr!=""&& oncliktr=='true'){
//el= self11.element;
//o = self11.options;
// menu = self11.menu;
// checkboxContainer = self11.checkboxContainer;
//   self = self11;  var $inputs = self.inputs;
//   }
//
//
//
//      // build items
//      el.find('option').each(function(i) {
//        var $this = $(this);
//        var parent = this.parentNode;
//        var description = this.innerHTML;
//        var title = this.title;
//        var filterselect;
//        var value = this.value;
//           var index=value.split("_")[1];
//        if(value.split("_")[3]=='selecttrue'){
//           filterselect = value.split("_")[3];
//            value=value.replace('_selecttrue', '');
//        }
//        value=value.replace('2q2', '_');
//        var inputID = 'ui-multiselect-' + (this.id || id + '-option-' + index);
//        var isDisabled = this.disabled;
//        var isSelected = this.selected;
//        var labelClasses = [ 'ui-corner-all' ];
//        var liClasses = (isDisabled ? 'ui-multiselect-disabled ' : ' ') + this.className;
//        var optLabel;
//
//        // is this an optgroup?
//        if(parent.tagName === 'OPTGROUP') {
//          optLabel = parent.getAttribute('label');
//
//          // has this optgroup been added already?
//          if($.inArray(optLabel, optgroups) === -1) {
//            html += '<li class="ui-multiselect-optgroup-label ' + parent.className + '"><a href="#">' + optLabel + '</a></li>';
//            optgroups.push(optLabel);
//          }
//        }
//
//        if(isDisabled) {
//          labelClasses.push('ui-state-disabled');
//        }
//
//        // browsers automatically select the first option
//        // by default with single selects
//        if(isSelected && !o.multiple) {
//          labelClasses.push('ui-state-active');
//        }
//
//        html += '<li class="' + liClasses + '">';
//var idval=(this.id.replace("Tr", "") || id.replace("Tr", ""))+"_"+index;
//var nameval=(this.id.replace("Tr", "") || id.replace("Tr", ""))+"*,"+value.split("_")[2];
//        // create the label
//        html += '<label for="' + inputID + '" title="' + title + '" class="' + labelClasses.join(' ') + '">';
////        html += '<input id="' + inputID + '" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" title="' + title + '"';
//        html += '<input id="' + inputID + '" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" onclick=parent.applyGlobalFilterinreport("'+idval+ '","' + nameval + '","'+inputID+'") title="' + title + '"';
//
//        // pre-selected?
//        if(isSelected) {
//          html += ' checked="checked"';
//          html += ' aria-selected="true"';
//        }
//
//        // disabled?
//        if(isDisabled) {
//          html += ' disabled="disabled"';
//          html += ' aria-disabled="true"';
//        }
//if(filterselect=='selecttrue'){
//    html += ' checked="checked"';
//          html += ' aria-selected="true"';
//}
//        // add the title and close everything off
//        html += ' /><span>' + description + '</span></label></li>';
////        totalSelectOptiontr=i+1;
//      });
//totalSelectOptiontr=0;
//      // insert into the DOM
//      checkboxContainer.html(html);
//
//      // cache some moar useful elements
//      this.labels = menu.find('label');
//      this.inputs = this.labels.children('input');
//
//      // set widths
//      this._setButtonWidth();
//      this._setMenuWidth();
//
//      // remember default value
//      this.button[0].defaultValue = this.update();
//
//      // broadcast refresh event; useful for widgets
//      if(!init) {
//        this._trigger('refresh');
//      }
//    }
//    },
//
//    // updates the button text. call refresh() to rebuild
//    update: function() {
//      var o = this.options;
//      var $inputs = this.inputs;
//       var el = this.element;
//          elementid = el.attr('name')
//        var tridbuttin='multiselecttTr' + elementid
//self1=maptr[tridbuttin];
//      var $checked = $inputs.filter(':checked');
//      var numChecked = $checked.length;
//      var value;
//
//   el.find('option').each(function(i) {
// totalSelectOptiontr=i+1;
//           });
//      var countt=0;
//      if(numChecked === 0) {
//        value = o.noneSelectedText;
//        var iddd3=o.noneSelectedText.replace(' ', '1q1');
////          iddd3=iddd3.replace(' ', '1q1')
//        iddd3="Tr"+iddd3;
////          if(loadingtimetr){
////                            loadingtimetr=false
//                            $("#multiselecttTr"+$('#'+iddd3).attr('name')).css({'background': '#1B3E70','color':'white'});
////        }else{
////        $("#multiselecttTr"+$('#'+iddd3).attr('name')).css({'background': 'grey','color':'white'});
////        }
//var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//        value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
//      } else {
//        if($.isFunction(o.selectedText)) {
//          value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
//         if(numChecked==totalSelectOptiontr){
//if(loadingtimetr){
////                            loadingtimetr=false
//                             value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
//                        }else{
//                            var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
////                     value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//                        }
//
//                    }else{
//           if(value.length>15 || value.length>15){
//    value=value.substring(0,15)+".."
//}
//                    }
//                    var iddd=o.noneSelectedText.replace(' ', '1q1')
//            iddd=iddd.replace(' ', '1q1')
//            iddd=iddd.replace(' ', '1q1')
// iddd="Tr"+iddd;
//               if(numChecked==totalSelectOptiontr){
//                      if(loadingtimetr){
////                            loadingtimetr=false
//                             $("#multiselecttTr"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
//               }else{ loadingtimetr=true
//   $("#multiselecttTr"+$('#'+iddd).attr('name')).css({'background': '#1B3E70','color':'white'});
//               }
//                    }else{
//         $("#multiselecttTr"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
//                    }
//        } else if(/\d/.test(o.selectedList) && o.selectedList > 0 && numChecked <= o.selectedList) {
//          value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
//          value1 = $checked.map(function() {return $(this).next().html();}).get();
//         if(numChecked==totalSelectOptiontr){
//          var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
////                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//                    }else{
//           if(value.length>15 || value.length>15){
//    value=value.substring(0,15)+".."
//}
//                    }
////          alert("ddd "+o.noneSelectedText)
//          var iddd=o.noneSelectedText.replace(' ', '1q1')
//          iddd=iddd.replace(' ', '1q1')
//          iddd=iddd.replace(' ', '1q1')
//              iddd="Tr"+iddd;
//              if(numChecked==totalSelectOptiontr){
//if(loadingtimetr){
////                            loadingtimetr=false
//                             value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
//                                if(value.length>15 || value.length>15){
//    value=value.substring(0,15)+".."
//}
//                              $("#multiselecttTr"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
// }else{
//     var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
//     value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
////                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
////                           if(value.length>15 || value.length>15){
////    value=value.substring(0,10)+".."
////}
//  $("#multiselecttTr"+$('#'+iddd).attr('name')).css({'background': '#1B3E70','color':'white'});
// }
//
//                    }else{
//          $("#multiselecttTr"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
//                    }
////          alert("value1 : "+$checked.map(function() {return $(this).next().html();}))
////          alert("value2 : "+$checked.map(function() {return $(this).next().html();}).get())
////          alert("value3 : "+$checked.toString())
//
//        } else {
////            alert("attribute "+this.getAttribute('aria-selected'));
////            alert("attribute "+this.attr('aria-selected'));
////                    alert("$inputs.length "+$inputs.length)
////                    alert("$checked.get() "+$checked.get().toString())
////                    alert("o.noneSelectedText "+o.noneSelectedText)
////                    alert($('#'+o.noneSelectedText).attr('name'))
//                    var iddd1=o.noneSelectedText.replace(' ', '1q1')
//                     iddd1=iddd1.replace(' ', '1q1')
//                     iddd1=iddd1.replace(' ', '1q1')
//                      iddd1="Tr"+iddd1;
////                    alert("iddd1 "+iddd1)
////          $("#multiselecttTr"+$('#'+iddd1).attr('name')).css({'background': '#B36666','color':'white'});
//
////                    alert("o.selectedList "+o.selectedList)
////                    alert("o.noneSelectedText "+o.noneSelectedText)
////                    alert("numChecked "+numChecked)
////                    alert("$checked.html() "+$checked.html())
//
//                    countt++;
////                    alert("cllllllllllaass"+$(this).hasClass('ui-multiselect-all'));
////                    alert("numChecked ooooo   "+numChecked)
////                    alert("idd   "+totalSelectOption);
//
//                    if(numChecked==totalSelectOptiontr){
//if(loadingtimetr){
////                            loadingtimetr=false
//                             $("#multiselecttTr"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});
//                              var nvalues=value=$checked.map(function() {
//                            return $(this).next().html();
//                        }).get();
//
//                               if(nvalues[0].length>15 || nvalues[1].length>15){
//    value=nvalues[0].substring(0,15)+".."
//}else{
//                                value=nvalues[0]+","+nvalues[1].substring(0,4)+".."
//}
//                    }else{ loadingtimetr=true
// $("#multiselecttTr"+$('#'+iddd1).attr('name')).css({'background': '#1B3E70','color':'white'});
//  var filName1= o.noneSelectedText;
//  if(filName1.length>20){
//                    filName1=filName1.substring(0, 20)+"..";
//                }
////                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
//                        value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
//                    }
    //
//                    }else{
//                         $("#multiselecttTr"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});
////                        value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
////                            if(numChecked<3){
////                        value=$checked.map(function() {
////                            return $(this).next().html();
////                        }).get()+"...";
//////                        value5=value1+"...";
////                            }else{
//
//                                var nvalues=value=$checked.map(function() {
//                            return $(this).next().html();
//                        }).get();
//
//                               if(nvalues[0].length>15 || nvalues[1].length>15){
//    value=nvalues[0].substring(0,10)+".."
//}else{
//                                value=nvalues[0]+","+nvalues[1].substring(0,4)+".."
//}
//
////                        alert("nval "+nvalues[1])
////                                }
////                            }
////                            value=value5;
//        }
////                    alert("cllllllllllaass  iddd   "+$(this).hasClass('ui-multiselect-all').id);
////                    edited by manik
////                             value = o.selectedText.replace('#', numChecked).replace('#', $inputs.length);
////alert("valu555 : "+$checked.map(function() {return $(this).next().html();}).get())
////                        value=$checked.map(function() {return $(this).next().html();}).get()+"...";
////                        value=value1+"..."+numChecked+" More";
////                        value=value1+"...";
////                             value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
////                    if(countt==1){
////                        value = $checked.map(function() {
////                            return $(this).next();
////                        });
//////                            value=o.selectedText.append("...More");
//////                        alert("value11 :"+value)
////                        alert("o.selectedText  :"+o.selectedText)
////                        alert("map  :"+$checked.map(function() {return $(this).next().html();}))
//////                        alert("o.noneSelectedText :"+o.noneSelectedText)
////                    }
////          alert("value :"+value)
//      }
//      }
//value=value.replace('&amp;', '&')
//      this._setButtonValue(value);
// loadingtimetr=true
// valselecttr=false
//      return value;
//    },
//
//    // this exists as a separate method so that the developer
//    // can easily override it.
//    _setButtonValue: function(value) {
//      this.buttonlabel.text(value);
//    },
//
//    // binds events
//    _bindEvents: function() {
//      var self = this;
//      var button = this.button;
//
//      function clickHandler() {
//        self[ self._isOpen ? 'close' : 'open' ]();
//        return false;
//      }
//
//      // webkit doesn't like it when you click on the span :(
//      button
//        .find('span')
//        .bind('click.multiselectTrend', clickHandler);
//
//      // button events
//      button.bind({
//        click: clickHandler,
//        keypress: function(e) {
//          switch(e.which) {
//            case 27: // esc
//              case 38: // up
//              case 37: // left
//              self.close();
//            break;
//            case 39: // right
//              case 40: // down
//              self.open();
//            break;
//          }
//        },
//        mouseenter: function() {
//          if(!button.hasClass('ui-state-disabled')) {
//            $(this).addClass('ui-state-hover');
//          }
//        },
//        mouseleave: function() {
//          $(this).removeClass('ui-state-hover');
//        },
//        focus: function() {
//          if(!button.hasClass('ui-state-disabled')) {
//            $(this).addClass('ui-state-focus');
//          }
//        },
//        blur: function() {
//          $(this).removeClass('ui-state-focus');
//        }
//      });
//
//      // header links
//      this.header.delegate('a', 'click.multiselectTrend', function(e) {
//        // close link
//        if($(this).hasClass('ui-multiselect-close')) {
//          self.close();
//
//          // check all / uncheck all
//        } else {
//          self[$(this).hasClass('ui-multiselect-all') ? 'checkAll' : 'uncheckAll']();
//        }
//
//        e.preventDefault();
//      });
//
//      // optgroup label toggle support
//      this.menu.delegate('li.ui-multiselect-optgroup-label a', 'click.multiselectTrend', function(e) {
//        e.preventDefault();
//
//        var $this = $(this);
//        var $inputs = $this.parent().nextUntil('li.ui-multiselect-optgroup-label').find('input:visible:not(:disabled)');
//        var nodes = $inputs.get();
//        var label = $this.parent().text();
//
//        // trigger event and bail if the return is false
//        if(self._trigger('beforeoptgrouptoggle', e, {inputs:nodes, label:label}) === false) {
//          return;
//        }
//
//        // toggle inputs
//        self._toggleChecked(
//          $inputs.filter(':checked').length !== $inputs.length,
//          $inputs
//        );
//
//        self._trigger('optgrouptoggle', e, {
//          inputs: nodes,
//          label: label,
//          checked: nodes[0].checked
//        });
//      })
//      .delegate('label', 'mouseenter.multiselectTrend', function() {
//        if(!$(this).hasClass('ui-state-disabled')) {
//          self.labels.removeClass('ui-state-hover');
//          $(this).addClass('ui-state-hover').find('input').focus();
//        }
//      })
//      .delegate('label', 'keydown.multiselectTrend', function(e) {
//        e.preventDefault();
//
//        switch(e.which) {
//          case 9: // tab
//            case 27: // esc
//            self.close();
//          break;
//          case 38: // up
//            case 40: // down
//            case 37: // left
//            case 39: // right
//            self._traverse(e.which, this);
//          break;
//          case 13: // enter
//            $(this).find('input')[0].click();
//          break;
//        }
//      })
//      .delegate('input[type="checkbox"], input[type="radio"]', 'click.multiselectTrend', function(e) {
//        var $this = $(this);
//        var val = this.value;
//        var checked = this.checked;
//        var tags = self.element.find('option');
//
//        // bail if this input is disabled or the event is cancelled
//        if(this.disabled || self._trigger('click', e, {value: val, text: this.title, checked: checked}) === false) {
//          e.preventDefault();
//          return;
//        }
//
//        // make sure the input has focus. otherwise, the esc key
//        // won't close the menu after clicking an item.
//        $this.focus();
//
//        // toggle aria state
//        $this.attr('aria-selected', checked);
//
//        // change state on the original option tags
//        tags.each(function() {
//          if(this.value === val) {
//            this.selected = checked;
//          } else if(!self.options.multiple) {
//            this.selected = false;
//          }
//        });
//
//        // some additional single select-specific logic
//        if(!self.options.multiple) {
//          self.labels.removeClass('ui-state-active');
//          $this.closest('label').toggleClass('ui-state-active', checked);
//
//          // close menu
//          self.close();
//        }
//
//        // fire change on the select box
//        self.element.trigger("change");
//
//        // setTimeout is to fix multiselect issue #14 and #47. caused by jQuery issue #3827
//        // http://bugs.jquery.com/ticket/3827
//        setTimeout($.proxy(self.update, self), 10);
//      });
//
//      // close each widget when clicking on any other element/anywhere else on the page
//      $doc.bind('mousedown.' + this._namespaceID, function(event) {
//        var target = event.target;
//
//        if(self._isOpen
//            && target !== self.button[0]
//            && target !== self.menu[0]
//            && !$.contains(self.menu[0], target)
//            && !$.contains(self.button[0], target)
//          ) {
//          self.close();
//        }
//      });
//
//      // deal with form resets.  the problem here is that buttons aren't
//      // restored to their defaultValue prop on form reset, and the reset
//      // handler fires before the form is actually reset.  delaying it a bit
//      // gives the form inputs time to clear.
//      $(this.element[0].form).bind('reset.multiselectTrend', function() {
//        setTimeout($.proxy(self.refresh, self), 10);
//      });
//    },
//
//    // set button width
//    _setButtonWidth: function() {
//      var width = this.element.outerWidth();
//      var o = this.options;
//
//      if(/\d/.test(o.minWidth) && width < o.minWidth) {
//        width = o.minWidth;
//      }
//width=193;
//var size=parent.sizeoffiltersg;
//if(size>=6){
//    size=6;
//}
//var dvdvalue=size+".3";
//width=parseInt(((($(window).width()*.89)))/dvdvalue);
//      // set widths
//      this.button.outerWidth(width);
//    },
//
//    // set menu width
//    _setMenuWidth: function() {
//      var m = this.menu;
//      m.outerWidth(this.button.outerWidth());
//    },
//
//    // move up or down within the menu
//    _traverse: function(which, start) {
//      var $start = $(start);
//      var moveToLast = which === 38 || which === 37;
//
//      // select the first li that isn't an optgroup label / disabled
//      var $next = $start.parent()[moveToLast ? 'prevAll' : 'nextAll']('li:not(.ui-multiselect-disabled, .ui-multiselect-optgroup-label)').first();
//
//      // if at the first/last element
//      if(!$next.length) {
//        var $container = this.menu.find('ul').last();
//
//        // move to the first/last
//        this.menu.find('label')[ moveToLast ? 'last' : 'first' ]().trigger('mouseover');
//
//        // set scroll position
//        $container.scrollTop(moveToLast ? $container.height() : 0);
//
//      } else {
//        $next.find('label').trigger('mouseover');
//      }
//    },
//
//    // This is an internal function to toggle the checked property and
//    // other related attributes of a checkbox.
//    //
//    // The context of this function should be a checkbox; do not proxy it.
//    _toggleState: function(prop, flag) {
//      return function() {
//        if(!this.disabled) {
//          this[ prop ] = flag;
//        }
//
//        if(flag) {
//          this.setAttribute('aria-selected', true);
//        } else {
//          this.removeAttribute('aria-selected');
//        }
//      };
//    },
//
//    _toggleChecked: function(flag, group) {
//      var $inputs = (group && group.length) ?  group : this.inputs;
//      var self = this;
//
//      // toggle state on inputs
//      $inputs.each(this._toggleState('checked', flag));
//
//      // give the first input focus
//      $inputs.eq(0).focus();
//
//      // update button text
//      this.update();
//
//      // gather an array of the values that actually changed
//      var values = $inputs.map(function() {
//        return this.value;
//      }).get();
//
//      // toggle state on original option tags
//      this.element
//        .find('option')
//        .each(function() {
//          if(!this.disabled && $.inArray(this.value, values) > -1) {
//            self._toggleState('selected', flag).call(this);
//          }
//        });
//
//      // trigger the change event on the select
//      if($inputs.length) {
//        this.element.trigger("change");
//      }
//    },
//
//    _toggleDisabled: function(flag) {
//      this.button.attr({'disabled':flag, 'aria-disabled':flag})[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');
//
//      var inputs = this.menu.find('input');
//      var key = "ech-multiselectTrend-disabled";
//
//      if(flag) {
//        // remember which elements this widget disabled (not pre-disabled)
//        // elements, so that they can be restored if the widget is re-enabled.
//        inputs = inputs.filter(':enabled').data(key, true)
//      } else {
//        inputs = inputs.filter(function() {
//          return $.data(this, key) === true;
//        }).removeData(key);
//      }
//
//      inputs
//        .attr({'disabled':flag, 'arial-disabled':flag})
//        .parent()[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');
//
//      this.element.attr({
//        'disabled':flag,
//        'aria-disabled':flag
//      });
//    },
//
//    // open the menu
//    open: function(e) {
//      var self = this;
//      var button = this.button;
//      var menu = this.menu;
//      var speed = this.speed;
//      var o = this.options;
//      var args = [];
//
//      // bail if the multiselectopen event returns false, this widget is disabled, or is already open
//      if(this._trigger('beforeopen') === false || button.hasClass('ui-state-disabled') || this._isOpen) {
//        return;
//      }
//
//      var $container = menu.find('ul').last();
//      var effect = o.show;
//
//      // figure out opening effects/speeds
//      if($.isArray(o.show)) {
//        effect = o.show[0];
//        speed = o.show[1] || self.speed;
//      }
//
//      // if there's an effect, assume jQuery UI is in use
//      // build the arguments to pass to show()
//      if(effect) {
//        args = [ effect, speed ];
//      }
//
//      // set the scroll of the checkbox container
//      $container.scrollTop(0).height(o.height);
//
//      // positon
//      this.position();
//
//      // show the menu, maybe with a speed/effect combo
//      $.fn.show.apply(menu, args);
//
//      // select the first not disabled option
//      // triggering both mouseover and mouseover because 1.4.2+ has a bug where triggering mouseover
//      // will actually trigger mouseenter.  the mouseenter trigger is there for when it's eventually fixed
//      this.labels.filter(':not(.ui-state-disabled)').eq(0).trigger('mouseover').trigger('mouseenter').find('input').trigger('focus');
//
//      button.addClass('ui-state-active');
//      this._isOpen = true;
//      this._trigger('open');
//    },
//
//    // close the menu
//    close: function() {
//      if(this._trigger('beforeclose') === false) {
//        return;
//      }
//
//      var o = this.options;
//      var effect = o.hide;
//      var speed = this.speed;
//      var args = [];
//
//      // figure out opening effects/speeds
//      if($.isArray(o.hide)) {
//        effect = o.hide[0];
//        speed = o.hide[1] || this.speed;
//      }
//
//      if(effect) {
//        args = [ effect, speed ];
//      }
//
//      $.fn.hide.apply(this.menu, args);
//      this.button.removeClass('ui-state-active').trigger('blur').trigger('mouseleave');
//      this._isOpen = false;
//      this._trigger('close');
//    },
//
//    enable: function() {
//      this._toggleDisabled(false);
//    },
//
//    disable: function() {
//      this._toggleDisabled(true);
//    },
//
//    checkAll: function(e) {
//      this._toggleChecked(true);
//      this._trigger('checkAll');
//    },
//
//    uncheckAll: function() {
//      this._toggleChecked(false);
//      this._trigger('uncheckAll');
//    },
//
//    getChecked: function() {
//      return this.menu.find('input').filter(':checked');
//    },
//
//    destroy: function() {
//      // remove classes + data
//      $.Widget.prototype.destroy.call(this);
//
//      // unbind events
//      $doc.unbind(this._namespaceID);
//
//      this.button.remove();
//      this.menu.remove();
//      this.element.show();
//
//      return this;
//    },
//
//    isOpen: function() {
//      return this._isOpen;
//    },
//
//    widget: function() {
//      return this.menu;
//    },
//
//    getButton: function() {
//      return this.button;
//    },
//
//    position: function() {
//      var o = this.options;
//
//      // use the position utility if it exists and options are specifified
//      if($.ui.position && !$.isEmptyObject(o.position)) {
//        o.position.of = o.position.of || this.button;
//
//        this.menu
//          .show()
//          .position(o.position)
//          .hide();
//
//        // otherwise fallback to custom positioning
//      } else {
//        var pos = this.button.offset();
//
//        this.menu.css({
//          top: pos.top + this.button.outerHeight(),
//          left: pos.left
//        });
//      }
//    },
//
//    // react to option changes after initialization
//    _setOption: function(key, value) {
//      var menu = this.menu;
//
//      switch(key) {
//        case 'header':
//          menu.find('div.ui-multiselect-header')[value ? 'show' : 'hide']();
//          break;
//        case 'checkAllText':
//          menu.find('a.ui-multiselect-all span').eq(-1).text(value);
//          break;
//        case 'uncheckAllText':
//          menu.find('a.ui-multiselect-none span').eq(-1).text(value);
//          break;
//        case 'height':
//          menu.find('ul').last().height(parseInt(value, 10));
//          break;
//        case 'minWidth':
//          this.options[key] = parseInt(value, 10);
//          this._setButtonWidth();
//          this._setMenuWidth();
//          break;
//        case 'selectedText':
//        case 'selectedList':
//        case 'noneSelectedText':
//          this.options[key] = value; // these all needs to update immediately for the update() call
//          this.update();
//          break;
//        case 'classes':
//          menu.add(this.button).removeClass(this.options.classes).addClass(value);
//          break;
//        case 'multiple':
//          menu.toggleClass('ui-multiselect-single', !value);
//          this.options.multiple = value;
//          this.element[0].multiple = value;
//          this.refresh();
//          break;
//        case 'position':
//          this.position();
//      }
//
//      $.Widget.prototype._setOption.apply(this, arguments);
//    }
//
//  });


   var multiselectID = 0;
  var $doc = $(document);
 var selftb;
 var self11;
 var elementid;
 var oncliktb='false';
 var loadingtime=false
 var notinflag=false
var mapt = new Object();
var value1;
var value5;
var tbidadvance;
var totalSelectOptiontb=0;
$.widget("ech.multiselecttable", {

    // default options
    options: {
      header: true,
      height: 175,
      minWidth: 225,
      classes: '',
      checkAllText: 'Check all',
      uncheckAllText: 'Uncheck all',
      noneSelectedText: 'Select filters',
      selectedText: '# selected',
      selectedList: 0,
      show: null,
      hide: null,
      autoOpen: false,
      multiple: true,
      position: {},
      appendTo: "body"
    },

    _create: function() {
      var el = this.element.hide();
      var o = this.options;
loadingtime=true
      this.speed = $.fx.speeds._default; // default speed for effects
      this._isOpen = false; // assume no

      // create a unique namespace for events that the widget
      // factory cannot unbind automatically. Use eventNamespace if on
      // jQuery UI 1.9+, and otherwise fallback to a custom string.
      this._namespaceID = this.eventNamespace || ('multiselect' + multiselectID);

  var el1 = this.element;

    var $this = $(this);
var id = el1.attr('id') || multiselectID++; // unique ID for the label & option tags
 id=id.split("__")[1];
id=id.replace("table", "");
var namevalue=id.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
     elementid = el1.attr('name')
     var totalvalue=id+"__"+elementid+"__"+$this;
     var tbidbuttin='multiselectttable' + elementid; var lableclick='lableclick' + elementid;var arrowclick='arrowclick' + elementid
      var scrollid='tableui-helper-reset' + elementid
        tbidadvance='multiselectadvance' + elementid;
var tablecheckall='tableui-multiselect-all' + elementid
var tableuncheckall='tableui-multiselect-close' + elementid
  mapt[tbidbuttin] = this;
    var ctxPath=parent.document.getElementById("h").value;
      var button = (this.button = $('<button id="'+tbidbuttin+'" title="'+namevalue+'" value="'+totalvalue+'" style="width:200px;height:27px; color:white; background:white;"  type="button"><span style="float:right;margin-right:17px"><i id="'+arrowclick+'" class="fa fa-angle-down" style="color:#ccc;font-size:large"></i></span></button>'))
//      var button = (this.button = $('<button id="'+tbidbuttin+'" title="'+namevalue+'" value="'+totalvalue+'" style="width:200px; color:white; background:#1B3E70;"  type="button"><span class="ui-icon ui-icon-triangle-1-s"></span></button>'))
        .addClass('ui-multiselect ui-widget ui-state-default ui-corner-all')
        .addClass(o.classes)
        .attr({'aria-haspopup':true, 'tabIndex':el.attr('tabIndex')})
        .insertAfter(el),

        buttonlabel = (this.buttonlabel = $('<span id="'+lableclick+'" class="gFontFamily gFontSize12" style="color: #555;" />'))
          .html(o.noneSelectedText)
          .appendTo(button),

        menu = (this.menu = $('<div />'))
          .addClass('ui-multiselect-menu ui-widget ui-widget-content ui-corner-all')//changed by sandeep for drop down closing issue track id-->TA_R_41
          .addClass(o.classes)
          .appendTo($(o.appendTo)),

        header = (this.header = $('<div />'))
          .addClass('ui-widget-header ui-corner-all ui-multiselect-header ui-helper-clearfix')
          .appendTo(menu),

        headerLinkContainer = (this.headerLinkContainer = $('<ul />'))
          .addClass('ui-helper-reset')
          .html(function() {
            if(o.header === true) {
              return '<li id="'+tablecheckall+'" style="margin-top: 1em;width:40%;" ><a class="ui-multiselect-all" href="#" style="color: white;"><span class="ui-icon ui-icon-check class="gFontFamily gFontSize12""></span><span class="gFontFamily gFontSize12" style="color: black;"> Select All </span></a></li><li id="'+tableuncheckall+'" style="margin-top: 1em;width:50%;"><a class="ui-multiselect-none" href="#" style="color: white;"><span class="ui-icon ui-icon-closethick"></span><span class="gFontFamily gFontSize12" style="color: black;" >UnSelect All</span></a></li><li class="gFontFamily" id="'+tbidadvance+'"style="width: 100%;height: 30px;text-align: center;background-color: #8BC34A;"><span  style="display: block;padding: 5px;cursor: pointer;font-size: 12px;color: rgb(0, 0, 0);">Initialize Filters</span> </li>';
            } else if(typeof o.header === "string") {
              return '<li>' + o.header + '</li>';
            } else {
              return '';
            }
          })
          .append('<li class="ui-multiselect-close" style="margin-top: -2.3em;"><a href="#" class="ui-multiselect-close"><span class="ui-icon ui-icon-circle-close"></span></a></li>')
          .appendTo(header),

        checkboxContainer = (this.checkboxContainer = $('<ul id="'+scrollid+'" />'))
          .addClass('ui-multiselect-checkboxes ui-helper-reset')
          .appendTo(menu);
  //added by sruthi for advance filters
     $("#"+tbidadvance).click(function(){
   var idvalue= $("#"+tbidbuttin).val()
var id1=idvalue.split("__")[0];
var elname=id1.replace("1q1","");loadingtime=false
var elementid1=idvalue.split("__")[1];
 elementid1=elementid1;
self11=mapt[tbidbuttin];
 selftb = self11;
    setTimeout(parent.AdvanceFilters(elementid1,elname),2000);
    //oncliktb='true'

//         setTimeout($.proxy(self11.refresh, self11), 2000);
//    }


})
  //ended by sruthi
$("#"+scrollid).on('scroll',function(){loadingtime=false
     setTimeout(parent.onScrollDivtable(this),2000);
//      setTimeout($.proxy(self1.refresh, self1), 2000);
var  scHeight=(this.scrollHeight);
  var   scTop=(this.scrollTop);
  var   clHgt=(this.clientHeight);
        if(scTop==(scHeight-clHgt)){
 var flag= parent.$("#multigblrefresh").val();
   if(flag=="true"){

      setTimeout($.proxy(selftb.refresh, selftb), 10);
        }
        }
})
var oldobj=this;
$("#"+tbidbuttin).mouseover(function(){
    var idvalue= $("#"+tbidbuttin).val()
var id1=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
    var pos = button.offset();
    var left=pos.left
     var top=pos.top + button.outerHeight()
//    parent.showfilters(elementid,"table","event",left,top)
})
$("#"+tbidbuttin).mouseout(function(){
//   parent.hidefilters()
})


$("#"+tbidbuttin).click(function(){
   var idvalue= $("#"+tbidbuttin).val()
var id1=idvalue.split("__")[0];
id1="table"+id1;loadingtime=false
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
self11=mapt[tbidbuttin];
 selftb = self11;
    setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"table","null"),2000);
    oncliktb='true'

//         setTimeout($.proxy(self11.refresh, self11), 2000);
//    }


})
$("#"+lableclick).click(function(){
   var idvalue= $("#"+tbidbuttin).val()
var id1=idvalue.split("__")[0];
id1="table"+id1;loadingtime=false
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
self11=mapt[tbidbuttin];
 selftb = self11;
    setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"table","null"),2000);
    oncliktb='true'

//         setTimeout($.proxy(self11.refresh, self11), 2000);
//    }


})
$("#"+arrowclick).click(function(){
   var idvalue= $("#"+tbidbuttin).val()
var id1=idvalue.split("__")[0];
id1="table"+id1;loadingtime=false
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
self11=mapt[tbidbuttin];
 selftb = self11;
    setTimeout(parent.lovfiltersreptable(id1,elementid1,self11,"table","null"),2000);
    oncliktb='true'



})
$("#"+tableuncheckall).click(function(){
var idvalue= $("#"+tbidbuttin).val()
var name=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
 setTimeout(parent.unchektableall(elementid,"uncheckall"),2000);
})
$("#"+tablecheckall).click(function(){
var idvalue= $("#"+tbidbuttin).val()
var name=idvalue.split("__")[0];
var elementid1=idvalue.split("__")[1];
elementid=elementid1;
loadingtime=false
setTimeout(parent.unchektableall(elementid,"checkall"),2000);
})
        // perform event bindings
        this._bindEvents();

        // build menu
        this.refresh(true);

        // some addl. logic for single selects
        if(!o.multiple) {
          menu.addClass('ui-multiselect-single');
        }

        // bump unique ID
        multiselectID++;
    },

    _init: function() {
      if(this.options.header === false) {
        this.header.hide();
      }
      if(!this.options.multiple) {
        this.headerLinkContainer.find('.ui-multiselect-all, .ui-multiselect-none').hide();
      }
      if(this.options.autoOpen) {
        this.open();
      }
      if(this.element.is(':disabled')) {
        this.disable();
      }
    },

    refresh: function(init) {
      var el = this.element;
      var o = this.options;
      var menu = this.menu;
      var checkboxContainer = this.checkboxContainer;
      var optgroups = [];
         var self = this;
//      self1 = self;
      var html = "";
      var  notin = el.attr('title');
     if(notin=="true"){
           notinflag=true
     }
     var id = el.attr('id') || multiselectID++; // unique ID for the label & option tags
   id=id.split("__")[1];
      // unique ID for the label & option tags
//     var idbuttin='multiselectt' + elementid;
//   var idvalue= $("#"+idbuttin).val();
   if(oncliktb!=""&& oncliktb=='true'){
el= self11.element;
o = self11.options;
 menu = self11.menu;
 checkboxContainer = self11.checkboxContainer;
   self = self11;
   }
var flag= parent.$("#multigblrefresh").val();
if(flag=='intilialized'){
   loadingtime=true
    parent.$("#multigblrefresh").val("true");
}


      // build items
      el.find('option').each(function(i) {
        var $this = $(this);
        var parent = this.parentNode;
        var description = this.innerHTML;
        var title = "";
        var filterselect;
        var value = this.value;
           var index=value.split("_")[1];
           var paramvalue=value.split("_")[0];
         if(paramvalue.length>=1){
            paramvalue=paramvalue.replace(' ', '1q1');
            paramvalue=paramvalue.replace('-', '1q1');
                paramvalue=paramvalue.replace(' ', '1q1');
         }else{
             paramvalue=i;
         }
         paramvalue=paramvalue.replace('2q2', '_');
        if(value.split("_")[3]=='selecttrue'){
           filterselect = value.split("_")[3];
            value=value.replace('_selecttrue', '');
        }

        var inputID = 'ui-multiselect-' + (this.id || id + '-option-' + i);
        var isDisabled = this.disabled;
        var isSelected = this.selected;
        var labelClasses = [ 'ui-corner-all' ];
        var liClasses = (isDisabled ? 'ui-multiselect-disabled ' : ' ') + this.className;
        var optLabel;

        // is this an optgroup?
        if(parent.tagName === 'OPTGROUP') {
          optLabel = parent.getAttribute('label');

          // has this optgroup been added already?
          if($.inArray(optLabel, optgroups) === -1) {
            html += '<li class="ui-multiselect-optgroup-label ' + parent.className + '"><a href="#">' + optLabel + '</a></li>';
            optgroups.push(optLabel);
          }
        }

        if(isDisabled) {
          labelClasses.push('ui-state-disabled');
        }

        // browsers automatically select the first option
        // by default with single selects
        if(isSelected && !o.multiple) {
          labelClasses.push('ui-state-active');
        }

        html += '<li class="' + liClasses + '">';
var idval=(this.id.replace("table", "") || id.replace("table", ""))+"_"+index;
var nameval=(this.id.replace("table", "") || id.replace("table", ""))+"*,"+value.split("_")[2];
        // create the label
        html += '<label style="width:95%;" for="' + inputID + '"  class="' + labelClasses.join(' ') + '">';
//        html += '<input id="' + inputID + '" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" title="' + title + '"';
        html += '<input id="' + inputID + '" style="float:left;"  name="multiselect_' + id+ '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" onclick=parent.applyGlobalFilterinreports("'+idval+ '","' + nameval + '","'+inputID+'") title="' + title + '"';

        // pre-selected?
        if(isSelected) {
          html += ' checked="checked"';
          html += ' aria-selected="true"';
        }

        // disabled?
        if(isDisabled) {
          html += ' disabled="disabled"';
          html += ' aria-disabled="true"';
        }
if(filterselect=='selecttrue'){
    html += ' checked="checked"';
          html += ' aria-selected="true"';
}
        // add the title and close everything off
        html += ' /><span style="float:left;width:80%;" class="gFontFamily gFontSize12">' + description + '</span></label></li>';
        totalSelectOptiontb=i+1;
      });

      // insert into the DOM
      checkboxContainer.html(html);

      // cache some moar useful elements
      this.labels = menu.find('label');
      this.inputs = this.labels.children('input');

      // set widths
      this._setButtonWidth();
      this._setMenuWidth();

      // remember default value
      this.button[0].defaultValue = this.update();

      // broadcast refresh event; useful for widgets
      if(!init) {
        this._trigger('refresh');
      }
    },

    // updates the button text. call refresh() to rebuild
    update: function() {
      var o = this.options;
      var $inputs = this.inputs;
       var el = this.element;
       elementid = el.attr('name')
     
        var tbidbuttin='multiselectttable' + elementid
selftb=mapt[tbidbuttin];
      var $checked = $inputs.filter(':checked');
      var numChecked = $checked.length;
      var value;

   el.find('option').each(function(i) {
 totalSelectOptiontb=i+1;
           });
      var countt=0;
      if(numChecked === 0) {
        value = o.noneSelectedText;
        var iddd3=o.noneSelectedText.replace(' ', '1q1');
         iddd3=iddd3.replace(' ', '1q1')
         iddd3=iddd3.replace(' ', '1q1')
        iddd3=elementid+"__table"+iddd3;
        if(loadingtime){
                          
                           if(notinflag){
                               notinflag=false;
                               $("#multiselectttable"+$('#'+iddd3).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
//                             $("#multiselectttable"+$('#'+iddd3).attr('name')).css({'background': '#55B63D','color':'white'});
                             $("#multiselectttable"+$('#'+iddd3).attr('name')).css({'background': '#fff','color':'white'});
                           }
               }else{
        $("#multiselectttable"+$('#'+iddd3).attr('name')).css({'background': '#fff','color':'white'});
               }
          var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
//   value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
 value = o.selectedText.replace('of # selected', filName1+":All").replace('#', '');
      } else {
        if($.isFunction(o.selectedText)) {
          value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
         if(numChecked==totalSelectOptiontb){

                        if(loadingtime){
                            
                             value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
                        }else{
                            var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
                        }
                    }else{
           if(value.length>20 || value.length>20){
    value=value.substring(0,15)+".."
}
                    }
                     var iddd=o.noneSelectedText.replace(' ', '1q1')
            iddd=iddd.replace(' ', '1q1')
            iddd=iddd.replace(' ', '1q1')
              iddd=elementid+"__table"+iddd;
               if(numChecked==totalSelectOptiontb){
                      if(loadingtime){
                           
                            if(notinflag){
                               notinflag=false
                               $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
//                             $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': '#B36666','color':'white'});
                             $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                           }
               }else{loadingtime=true
   $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': '#fff','color':'white'});
               }
                    }else{
         $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                    }
        } else if(/\d/.test(o.selectedList) && o.selectedList > 0 && numChecked <= o.selectedList) {
          value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
          value1 = $checked.map(function() {return $(this).next().html();}).get();
          if(numChecked==totalSelectOptiontb){
value = o.selectedText.replace('of # selected',o.noneSelectedText+":All").replace('#', '');
//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );

                    }else{
           if(value.length>10 || value.length>10){
    value=value.substring(0,10)+".."
}
                    }
//          alert("ddd "+o.noneSelectedText)
          var iddd=o.noneSelectedText.replace(' ', '1q1')
            iddd=iddd.replace(' ', '1q1')
            iddd=iddd.replace(' ', '1q1')
              iddd=elementid+"__table"+iddd;
               if(numChecked==totalSelectOptiontb){
                    if(loadingtime){
                           
                             value = $checked.map(function() {return $(this).next().html();}).get().join(', ');
                                if(value.length>10 || value.length>10){
    value=value.substring(0,10)+".."
}
if(notinflag){
                               notinflag=false
                               $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
                              $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                           }
                    }else{loadingtime=true
                         var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
                         $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': '#fff','color':'white'});
 }


                    }else{
         $("#multiselectttable"+$('#'+iddd).attr('name')).css({'background': 'lightgray','color':'white'});
                    }
//          alert("value1 : "+$checked.map(function() {return $(this).next().html();}))
//          alert("value2 : "+$checked.map(function() {return $(this).next().html();}).get())
//          alert("value3 : "+$checked.toString())

        } else {
//            alert("attribute "+this.getAttribute('aria-selected'));
//            alert("attribute "+this.attr('aria-selected'));
//                    alert("$inputs.length "+$inputs.length)
//                    alert("$checked.get() "+$checked.get().toString())
//                    alert("o.noneSelectedText "+o.noneSelectedText)
//                    alert($('#'+o.noneSelectedText).attr('name'))
                    var iddd1=o.noneSelectedText.replace(' ', '1q1')
                       iddd1=iddd1.replace(' ', '1q1')
                       iddd1=iddd1.replace(' ', '1q1')
                      iddd1=elementid+"__table"+iddd1;
//                    alert("iddd1 "+iddd1)


//                    alert("o.selectedList "+o.selectedList)
//                    alert("o.noneSelectedText "+o.noneSelectedText)
//                    alert("numChecked "+numChecked)
//                    alert("$checked.html() "+$checked.html())

                    countt++;
//                    alert("cllllllllllaass"+$(this).hasClass('ui-multiselect-all'));
//                    alert("numChecked ooooo   "+numChecked)
//                    alert("idd   "+totalSelectOption);

                    if(numChecked==totalSelectOptiontb){
                         if(loadingtime){
                          
                            if(notinflag){
                               notinflag=false
                               $("#multiselectttable"+$('#'+iddd1).attr('name')).css({'background': '#76e1bd','color':'white'})
                           }else{
                          $("#multiselectttable"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});
                           }
                              var nvalues=$checked.map(function() {
                            return $(this).next().html();
                        }).get();

                               if(nvalues[0].length>10 || nvalues[1].length>10){
    value=nvalues[0].substring(0,10)+".."
}else{
     if(nvalues[0].length<10 || nvalues[1].length<10){
          value=nvalues[0].substring(0,5).toString().trim()+","+nvalues[1].substring(0,5).toString().trim()+", More.."
     }else{
                                value=nvalues[0]+", More.."
}
}
                    }else{loadingtime=true
  $("#multiselectttable"+$('#'+iddd1).attr('name')).css({'background': '#fff','color':'white'});
 var filName1= o.noneSelectedText;
  if(filName1.length>10){
                    filName1=filName1.substring(0, 10)+"..";
                }
//                        value = o.selectedText.replace('#', 'All').replace('of # selected',o.noneSelectedText );
                         value = o.selectedText.replace('of # selected',filName1+":All").replace('#', '');
                    }
                    }else{
                          $("#multiselectttable"+$('#'+iddd1).attr('name')).css({'background': 'lightgray','color':'white'});
//                        value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
//                            if(numChecked<3){
//                        value=$checked.map(function() {
//                            return $(this).next().html();
//                        }).get()+"...";
////                        value5=value1+"...";
//                            }else{

                                var nvalues=$checked.map(function() {
                            return $(this).next().html();
                        }).get();

                               if(nvalues[0].length>10 || nvalues[1].length>10){
    value=nvalues[0].substring(0,10)+".."
}else{
                                 if(nvalues[0].length<10 || nvalues[1].length<10){
          value=nvalues[0].substring(0,5).toString().trim()+","+nvalues[1].substring(0,5).toString().trim()+", More.."
     }else{
                                value=nvalues[0]+", More.."
}
}

//                        alert("nval "+nvalues[1])
//                                }
//                            }
//                            value=value5;
        }
//                    alert("cllllllllllaass  iddd   "+$(this).hasClass('ui-multiselect-all').id);
//                    edited by manik
//                             value = o.selectedText.replace('#', numChecked).replace('#', $inputs.length);
//alert("valu555 : "+$checked.map(function() {return $(this).next().html();}).get())
//                        value=$checked.map(function() {return $(this).next().html();}).get()+"...";
//                        value=value1+"..."+numChecked+" More";
//                        value=value1+"...";
//                             value = o.selectedText.replace('#', numChecked).replace('of #', ' ');
//                    if(countt==1){
//                        value = $checked.map(function() {
//                            return $(this).next();
//                        });
////                            value=o.selectedText.append("...More");
////                        alert("value11 :"+value)
//                        alert("o.selectedText  :"+o.selectedText)
//                        alert("map  :"+$checked.map(function() {return $(this).next().html();}))
////                        alert("o.noneSelectedText :"+o.noneSelectedText)
//                    }
//          alert("value :"+value)
      }
      }
value=value.replace('&amp;', '&').replace('&gt;', '>').replace('&lt;', '<')
      this._setButtonValue(value);
loadingtime=true
      return value;
    },

    // this exists as a separate method so that the developer
    // can easily override it.
    _setButtonValue: function(value) {
      this.buttonlabel.text(value);
    },

    // binds events
    _bindEvents: function() {
      var self = this;
      var button = this.button;

      function clickHandler() {
        self[ self._isOpen ? 'close' : 'open' ]();
        return false;
      }

      // webkit doesn't like it when you click on the span :(
      button
        .find('span')
        .bind('click.multiselecttable', clickHandler);

      // button events
      button.bind({
        click: clickHandler,
        keypress: function(e) {
          switch(e.which) {
            case 27: // esc
              case 38: // up
              case 37: // left
              self.close();
            break;
            case 39: // right
              case 40: // down
              self.open();
            break;
          }
        },
        mouseenter: function() {
          if(!button.hasClass('ui-state-disabled')) {
            $(this).addClass('ui-state-hover');
          }
        },
        mouseleave: function() {
          $(this).removeClass('ui-state-hover');
        },
        focus: function() {
          if(!button.hasClass('ui-state-disabled')) {
            $(this).addClass('ui-state-focus');
          }
        },
        blur: function() {
          $(this).removeClass('ui-state-focus');
        }
      });

      // header links
      this.header.delegate('a', 'click.multiselecttable', function(e) {
        // close link
        if($(this).hasClass('ui-multiselect-close')) {
          self.close();

          // check all / uncheck all
        } else {
          self[$(this).hasClass('ui-multiselect-all') ? 'checkAll' : 'uncheckAll']();
        }

        e.preventDefault();
      });

      // optgroup label toggle support
      this.menu.delegate('li.ui-multiselect-optgroup-label a', 'click.multiselecttable', function(e) {
        e.preventDefault();

        var $this = $(this);
        var $inputs = $this.parent().nextUntil('li.ui-multiselect-optgroup-label').find('input:visible:not(:disabled)');
        var nodes = $inputs.get();
        var label = $this.parent().text();

        // trigger event and bail if the return is false
        if(self._trigger('beforeoptgrouptoggle', e, {inputs:nodes, label:label}) === false) {
          return;
        }

        // toggle inputs
        self._toggleChecked(
          $inputs.filter(':checked').length !== $inputs.length,
          $inputs
        );

        self._trigger('optgrouptoggle', e, {
          inputs: nodes,
          label: label,
          checked: nodes[0].checked
        });
      })
      .delegate('label', 'mouseenter.multiselecttable', function() {
        if(!$(this).hasClass('ui-state-disabled')) {
          self.labels.removeClass('ui-state-hover');
          $(this).addClass('ui-state-hover').find('input').focus();
        }
      })
      .delegate('label', 'keydown.multiselecttable', function(e) {
        e.preventDefault();

        switch(e.which) {
          case 9: // tab
            case 27: // esc
            self.close();
          break;
          case 38: // up
            case 40: // down
            case 37: // left
            case 39: // right
            self._traverse(e.which, this);
          break;
          case 13: // enter
            $(this).find('input')[0].click();
          break;
        }
      })
      .delegate('input[type="checkbox"], input[type="radio"]', 'click.multiselecttable', function(e) {
        var $this = $(this);
        var val = this.value;
        var checked = this.checked;
        var tags = self.element.find('option');

        // bail if this input is disabled or the event is cancelled
        if(this.disabled || self._trigger('click', e, {value: val, text: this.title, checked: checked}) === false) {
          e.preventDefault();
          return;
        }

        // make sure the input has focus. otherwise, the esc key
        // won't close the menu after clicking an item.
        $this.focus();

        // toggle aria state
        $this.attr('aria-selected', checked);

        // change state on the original option tags
        tags.each(function() {
          if(this.value === val) {
            this.selected = checked;
          } else if(!self.options.multiple) {
            this.selected = false;
          }
        });

        // some additional single select-specific logic
        if(!self.options.multiple) {
          self.labels.removeClass('ui-state-active');
          $this.closest('label').toggleClass('ui-state-active', checked);

          // close menu
          self.close();
        }

        // fire change on the select box
        self.element.trigger("change");

        // setTimeout is to fix multiselect issue #14 and #47. caused by jQuery issue #3827
        // http://bugs.jquery.com/ticket/3827
        setTimeout($.proxy(self.update, self), 10);
      });

      // close each widget when clicking on any other element/anywhere else on the page
      $doc.bind('mousedown.' + this._namespaceID, function(event) {
        var target = event.target;

        if(self._isOpen
            && target !== self.button[0]
            && target !== self.menu[0]
            && !$.contains(self.menu[0], target)
            && !$.contains(self.button[0], target)
          ) {
          self.close();
        }
      });

      // deal with form resets.  the problem here is that buttons aren't
      // restored to their defaultValue prop on form reset, and the reset
      // handler fires before the form is actually reset.  delaying it a bit
      // gives the form inputs time to clear.
      $(this.element[0].form).bind('reset.multiselecttable', function() {
        setTimeout($.proxy(self.refresh, self), 10);
      });
    },

    // set button width
    _setButtonWidth: function() {
      var width = this.element.outerWidth();
      var o = this.options;

      if(/\d/.test(o.minWidth) && width < o.minWidth) {
        width = o.minWidth;
      }
width=193;
width=parseInt(((($(window).width()*.89)))/6.3);
      // set widths
      this.button.outerWidth(width);
    },

    // set menu width
    _setMenuWidth: function() {
      var m = this.menu;
      m.outerWidth(this.button.outerWidth());
    },

    // move up or down within the menu
    _traverse: function(which, start) {
      var $start = $(start);
      var moveToLast = which === 38 || which === 37;

      // select the first li that isn't an optgroup label / disabled
      var $next = $start.parent()[moveToLast ? 'prevAll' : 'nextAll']('li:not(.ui-multiselect-disabled, .ui-multiselect-optgroup-label)').first();

      // if at the first/last element
      if(!$next.length) {
        var $container = this.menu.find('ul').last();

        // move to the first/last
        this.menu.find('label')[ moveToLast ? 'last' : 'first' ]().trigger('mouseover');

        // set scroll position
        $container.scrollTop(moveToLast ? $container.height() : 0);

      } else {
        $next.find('label').trigger('mouseover');
      }
    },

    // This is an internal function to toggle the checked property and
    // other related attributes of a checkbox.
    //
    // The context of this function should be a checkbox; do not proxy it.
    _toggleState: function(prop, flag) {
      return function() {
        if(!this.disabled) {
          this[ prop ] = flag;
        }

        if(flag) {
          this.setAttribute('aria-selected', true);
        } else {
          this.removeAttribute('aria-selected');
        }
      };
    },

    _toggleChecked: function(flag, group) {
      var $inputs = (group && group.length) ?  group : this.inputs;
      var self = this;

      // toggle state on inputs
      $inputs.each(this._toggleState('checked', flag));

      // give the first input focus
      $inputs.eq(0).focus();

      // update button text
      this.update();

      // gather an array of the values that actually changed
      var values = $inputs.map(function() {
        return this.value;
      }).get();

      // toggle state on original option tags
      this.element
        .find('option')
        .each(function() {
          if(!this.disabled && $.inArray(this.value, values) > -1) {
            self._toggleState('selected', flag).call(this);
          }
        });

      // trigger the change event on the select
      if($inputs.length) {
        this.element.trigger("change");
      }
    },

    _toggleDisabled: function(flag) {
      this.button.attr({'disabled':flag, 'aria-disabled':flag})[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');

      var inputs = this.menu.find('input');
      var key = "ech-multiselecttable-disabled";

      if(flag) {
        // remember which elements this widget disabled (not pre-disabled)
        // elements, so that they can be restored if the widget is re-enabled.
        inputs = inputs.filter(':enabled').data(key, true)
      } else {
        inputs = inputs.filter(function() {
          return $.data(this, key) === true;
        }).removeData(key);
      }

      inputs
        .attr({'disabled':flag, 'arial-disabled':flag})
        .parent()[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');

      this.element.attr({
        'disabled':flag,
        'aria-disabled':flag
      });
    },

    // open the menu
    open: function(e) {
      var self = this;
      var button = this.button;
      var menu = this.menu;
      var speed = this.speed;
      var o = this.options;
      var args = [];

      // bail if the multiselectopen event returns false, this widget is disabled, or is already open
      if(this._trigger('beforeopen') === false || button.hasClass('ui-state-disabled') || this._isOpen) {
        return;
      }

      var $container = menu.find('ul').last();
      var effect = o.show;

      // figure out opening effects/speeds
      if($.isArray(o.show)) {
        effect = o.show[0];
        speed = o.show[1] || self.speed;
      }

      // if there's an effect, assume jQuery UI is in use
      // build the arguments to pass to show()
      if(effect) {
        args = [ effect, speed ];
      }

      // set the scroll of the checkbox container
      $container.scrollTop(0).height(o.height);

      // positon
      this.position();

      // show the menu, maybe with a speed/effect combo
      $.fn.show.apply(menu, args);

      // select the first not disabled option
      // triggering both mouseover and mouseover because 1.4.2+ has a bug where triggering mouseover
      // will actually trigger mouseenter.  the mouseenter trigger is there for when it's eventually fixed
      this.labels.filter(':not(.ui-state-disabled)').eq(0).trigger('mouseover').trigger('mouseenter').find('input').trigger('focus');

      button.addClass('ui-state-active');
      this._isOpen = true;
      this._trigger('open');
    },

    // close the menu
    close: function() {
      if(this._trigger('beforeclose') === false) {
        return;
      }

      var o = this.options;
      var effect = o.hide;
      var speed = this.speed;
      var args = [];

      // figure out opening effects/speeds
      if($.isArray(o.hide)) {
        effect = o.hide[0];
        speed = o.hide[1] || this.speed;
      }

      if(effect) {
        args = [ effect, speed ];
      }

      $.fn.hide.apply(this.menu, args);
      this.button.removeClass('ui-state-active').trigger('blur').trigger('mouseleave');
      this._isOpen = false;
      this._trigger('close');
    },

    enable: function() {
      this._toggleDisabled(false);
    },

    disable: function() {
      this._toggleDisabled(true);
    },

    checkAll: function(e) {
      this._toggleChecked(true);
      this._trigger('checkAll');
    },

    uncheckAll: function() {
      this._toggleChecked(false);
      this._trigger('uncheckAll');
    },

    getChecked: function() {
      return this.menu.find('input').filter(':checked');
    },

    destroy: function() {
      // remove classes + data
      $.Widget.prototype.destroy.call(this);

      // unbind events
      $doc.unbind(this._namespaceID);

      this.button.remove();
      this.menu.remove();
      this.element.show();

      return this;
    },

    isOpen: function() {
      return this._isOpen;
    },

    widget: function() {
      return this.menu;
    },

    getButton: function() {
      return this.button;
    },

    position: function() {
      var o = this.options;

      // use the position utility if it exists and options are specifified
      if($.ui.position && !$.isEmptyObject(o.position)) {
        o.position.of = o.position.of || this.button;

        this.menu
          .show()
          .position(o.position)
          .hide();

        // otherwise fallback to custom positioning
      } else {
        var pos = this.button.offset();

        this.menu.css({
          top: pos.top + this.button.outerHeight(),
          left: pos.left
        });
      }
    },

    // react to option changes after initialization
    _setOption: function(key, value) {
      var menu = this.menu;

      switch(key) {
        case 'header':
          menu.find('div.ui-multiselect-header')[value ? 'show' : 'hide']();
          break;
        case 'checkAllText':
          menu.find('a.ui-multiselect-all span').eq(-1).text(value);
          break;
        case 'uncheckAllText':
          menu.find('a.ui-multiselect-none span').eq(-1).text(value);
          break;
        case 'height':
          menu.find('ul').last().height(parseInt(value, 10));
          break;
        case 'minWidth':
          this.options[key] = parseInt(value, 10);
          this._setButtonWidth();
          this._setMenuWidth();
          break;
        case 'selectedText':
        case 'selectedList':
        case 'noneSelectedText':
          this.options[key] = value; // these all needs to update immediately for the update() call
          this.update();
          break;
        case 'classes':
          menu.add(this.button).removeClass(this.options.classes).addClass(value);
          break;
        case 'multiple':
          menu.toggleClass('ui-multiselect-single', !value);
          this.options.multiple = value;
          this.element[0].multiple = value;
          this.refresh();
          break;
        case 'position':
          this.position();
      }

      $.Widget.prototype._setOption.apply(this, arguments);
    }

  });
})(jQuery);
