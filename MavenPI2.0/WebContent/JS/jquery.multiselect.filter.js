/* jshint forin:true, noarg:true, noempty:true, eqeqeq:true, boss:true, undef:true, curly:true, browser:true, jquery:true */
/*
 * jQuery MultiSelect UI Widget Filtering Plugin 1.5pre
 * Copyright (c) 2012 Eric Hynds
 *
 * http://www.erichynds.com/jquery/jquery-ui-multiselect-widget/
 *
 * Depends:
 *   - jQuery UI MultiSelect widget
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 */
(function($) {
  var rEscape = /[\-\[\]{}()*+?.,\\\^$|#\s]/g;

  $.widget('ech.multiselectfilter', {

    options: {
      label: 'Filter:',
      width: null, /* override default width set in css file (px). null will inherit */
      placeholder: 'Enter keywords',
      autoReset: false
    },

    _create: function() {
      var opts = this.options;
      var elem = $(this.element);
var elementid = elem.attr('name')
 var idbutting='multitypeg' + elementid
      // get the multiselect instance
      var instance = (this.instance = (elem.data('echMultiselect') || elem.data("multiselect") || elem.data("ech-multiselect")));

      // store header; add filter class so the close/check all/uncheck all links can be positioned correctly
      var header = (this.header = instance.menu.find('.ui-multiselect-header').addClass('ui-multiselect-hasfilter'));

      // wrapper elem
      var wrapper = (this.wrapper = $('<div class="ui-multiselect-filter">' + (opts.label.length ? opts.label : '') + '<input id="'+idbutting+'"    placeholder="'+opts.placeholder+'" type="text"' + (/\d/.test(opts.width) ? 'style="width:'+opts.width+'px"' : '') + ' /></div>').prependTo(this.header));

      // reference to the actual inputs
      this.inputs = instance.menu.find('input[type="checkbox"], input[type="radio"]');

      // build the input box
      this.input = wrapper.find('input').bind({
        keydown: function(e) {
          // prevent the enter key from submitting the form / closing the widget
          if(e.which === 13) {
            e.preventDefault();
          }
        },
        keyup: $.proxy(this._handler, this),
        click: $.proxy(this._handler, this)
      });
      //changed by sandeep for filter hanging issue track id-->
$("#"+idbutting).keyup(function(event){ 
    if(event.keyCode == 13){
        $("#"+idbutting).click();
        parent.resetmorefilters(idbutting)
    }
});
      // cache input values for searching
      this.updateCache();

      // rewrite internal _toggleChecked fn so that when checkAll/uncheckAll is fired,
      // only the currently filtered elements are checked
      instance._toggleChecked = function(flag, group) {
        var $inputs = (group && group.length) ?  group : this.labels.find('input');
        var _self = this;

        // do not include hidden elems if the menu isn't open.
        var selector = instance._isOpen ?  ':disabled, :hidden' : ':disabled';

        $inputs = $inputs
          .not(selector)
          .each(this._toggleState('checked', flag));

        // update text
        this.update();

        // gather an array of the values that actually changed
        var values = $inputs.map(function() {
          return this.value;
        }).get();

        // select option tags
        this.element.find('option').filter(function() {
          if(!this.disabled && $.inArray(this.value, values) > -1) {
            _self._toggleState('selected', flag).call(this);
          }
        });

        // trigger the change event on the select
        if($inputs.length) {
          this.element.trigger('change');
        }
      };

      // rebuild cache when multiselect is updated
      var doc = $(document).bind('multiselectrefresh', $.proxy(function() {
        this.updateCache();
        this._handler();
      }, this));

      // automatically reset the widget on close?
      if(this.options.autoReset) {
        doc.bind('multiselectclose', $.proxy(this._reset, this));
      }
    },

    // thx for the logic here ben alman
    _handler: function(e) {
      var term = $.trim(this.input[0].value.toLowerCase()),

      // speed up lookups
      rows = this.rows, inputs = this.inputs, cache = this.cache;

      if(!term) {
        //rows.show();
      } else {
       // rows.hide();

        var regex = new RegExp(term.replace(rEscape, "\\$&"), 'gi');

        this._trigger("filter", e, $.map(cache, function(v, i) {
          if(v.search(regex) !== -1) {
           // rows.eq(i).show();
            return inputs.get(i);
          }

          return null;
        }));
         //resetAjax(term);
      }

      // show/hide optgroups
      this.instance.menu.find(".ui-multiselect-optgroup-label").each(function() {
        var $this = $(this);
        var isVisible = $this.nextUntil('.ui-multiselect-optgroup-label').filter(function() {
          return $.css(this, "display") !== 'none';
        }).length;

        $this[isVisible ? 'show' : 'hide']();
      });
    },

    _reset: function() {
      this.input.val('').trigger('keyup');
    },

    updateCache: function() {
      // each list item
      this.rows = this.instance.menu.find(".ui-multiselect-checkboxes li:not(.ui-multiselect-optgroup-label)");

      // cache
      this.cache = this.element.children().map(function() {
        var elem = $(this);

        // account for optgroups
        if(this.tagName.toLowerCase() === "optgroup") {
          elem = elem.children();
        }

        return elem.map(function() {
          return this.innerHTML.toLowerCase();
        }).get();
      }).get();
    },

    widget: function() {
      return this.wrapper;
    },

    destroy: function() {
      $.Widget.prototype.destroy.call(this);
      this.input.val('').trigger("keyup");
      this.wrapper.remove();
    }
  });
 $.widget('ech.multiselectfilterTrend', {

    options: {
      label: 'Filter:',
      width: null, /* override default width set in css file (px). null will inherit */
      placeholder: 'Enter keywords',
      autoReset: false
    },

    _create: function() {
      var opts = this.options;
      var elem = $(this.element);
var elementid = elem.attr('name')
 var idbuttin='multitypet' + elementid
      // get the multiselect instance
      var instance = (this.instance = (elem.data('echMultiselectTrend') || elem.data("multiselectTrend") || elem.data("ech-multiselectTrend")));

      // store header; add filter class so the close/check all/uncheck all links can be positioned correctly
      var header = (this.header = instance.menu.find('.ui-multiselect-header').addClass('ui-multiselect-hasfilter'));

      // wrapper elem
      var wrapper = (this.wrapper = $('<div class="ui-multiselect-filter">' + (opts.label.length ? opts.label : '') + '<input id="'+idbuttin+'"  placeholder="'+opts.placeholder+'" type="search"' + (/\d/.test(opts.width) ? 'style="width:'+opts.width+'px"' : '') + ' /></div>').prependTo(this.header));

      // reference to the actual inputs
      this.inputs = instance.menu.find('input[type="checkbox"], input[type="radio"]');

      // build the input box
      this.input = wrapper.find('input').bind({
        keydown: function(e) {
          // prevent the enter key from submitting the form / closing the widget
          if(e.which === 13) {
            e.preventDefault();
          }
        },
        keyup: $.proxy(this._handler, this),
        click: $.proxy(this._handler, this)
      });
$("#"+idbuttin).keyup(function(event){
    if(event.keyCode == 13){
        $("#"+idbuttin).click();
        parent.resetmorefilters(idbuttin)
    }
});
      // cache input values for searching
      this.updateCache();

      // rewrite internal _toggleChecked fn so that when checkAll/uncheckAll is fired,
      // only the currently filtered elements are checked
      instance._toggleChecked = function(flag, group) {
        var $inputs = (group && group.length) ?  group : this.labels.find('input');
        var _self = this;

        // do not include hidden elems if the menu isn't open.
        var selector = instance._isOpen ?  ':disabled, :hidden' : ':disabled';

        $inputs = $inputs
          .not(selector)
          .each(this._toggleState('checked', flag));

        // update text
        this.update();

        // gather an array of the values that actually changed
        var values = $inputs.map(function() {
          return this.value;
        }).get();

        // select option tags
        this.element.find('option').filter(function() {
          if(!this.disabled && $.inArray(this.value, values) > -1) {
            _self._toggleState('selected', flag).call(this);
          }
        });

        // trigger the change event on the select
        if($inputs.length) {
          this.element.trigger('change');
        }
      };

      // rebuild cache when multiselect is updated
      var doc = $(document).bind('multiselectrefresh', $.proxy(function() {
        this.updateCache();
        this._handler();
      }, this));

      // automatically reset the widget on close?
      if(this.options.autoReset) {
        doc.bind('multiselectclose', $.proxy(this._reset, this));
      }
    },

    // thx for the logic here ben alman
    _handler: function(e) {
      var term = $.trim(this.input[0].value.toLowerCase()),

      // speed up lookups
      rows = this.rows, inputs = this.inputs, cache = this.cache;

      if(!term) {
        rows.show();
            var flag= parent.$("#searchtype").val();
   if(flag=="trend"){
        //resetAjax(term);
   }
      } else {
        rows.hide();

        var regex = new RegExp(term.replace(rEscape, "\\$&"), 'gi');

        this._trigger("filter", e, $.map(cache, function(v, i) {
          if(v.search(regex) !== -1) {
            rows.eq(i).show();
            return inputs.get(i);
          }

          return null;
        }));
          var flag= parent.$("#searchtype").val();
   if(flag=="trend"){
//       parent.$("#multigblrefresh").val("reset")
         //resetAjax(term);
//        parent.$("#multigblrefresh").val("")
      }
      }

      // show/hide optgroups
      this.instance.menu.find(".ui-multiselect-optgroup-label").each(function() {
        var $this = $(this);
        var isVisible = $this.nextUntil('.ui-multiselect-optgroup-label').filter(function() {
          return $.css(this, "display") !== 'none';
        }).length;

        $this[isVisible ? 'show' : 'hide']();
      });
    },

    _reset: function() {
      this.input.val('').trigger('keyup');
    },

    updateCache: function() {
      // each list item
      this.rows = this.instance.menu.find(".ui-multiselect-checkboxes li:not(.ui-multiselect-optgroup-label)");

      // cache
      this.cache = this.element.children().map(function() {
        var elem = $(this);

        // account for optgroups
        if(this.tagName.toLowerCase() === "optgroup") {
          elem = elem.children();
        }

        return elem.map(function() {
          return this.innerHTML.toLowerCase();
        }).get();
      }).get();
    },

    widget: function() {
      return this.wrapper;
    },

    destroy: function() {
      $.Widget.prototype.destroy.call(this);
      this.input.val('').trigger("keyup");
      this.wrapper.remove();
    }
  });
  $.widget('ech.multiselectfilter1', {

    options: {
      label: 'Filter:',
      width: null, /* override default width set in css file (px). null will inherit */
      placeholder: 'Enter keywords',
      autoReset: false
    },

    _create: function() {
      var opts = this.options;
      var elem = $(this.element);
var elementid = elem.attr('name')
 var idbuttin='multitypea' + elementid
      // get the multiselect instance
      var instance = (this.instance = (elem.data('echMultiselect1') || elem.data("multiselect1") || elem.data("ech-multiselect1")));

      // store header; add filter class so the close/check all/uncheck all links can be positioned correctly
      var header = (this.header = instance.menu.find('.ui-multiselect-header').addClass('ui-multiselect-hasfilter'));

      // wrapper elem
      var wrapper = (this.wrapper = $('<div class="ui-multiselect-filter">' + (opts.label.length ? opts.label : '') + '<input id="'+idbuttin+'" placeholder="'+opts.placeholder+'" type="search"' + (/\d/.test(opts.width) ? 'style="width:'+opts.width+'px"' : '') + ' /></div>').prependTo(this.header));

      // reference to the actual inputs
      this.inputs = instance.menu.find('input[type="checkbox"], input[type="radio"]');

      // build the input box
      this.input = wrapper.find('input').bind({
        keydown: function(e) {
          // prevent the enter key from submitting the form / closing the widget
          if(e.which === 13) {
            e.preventDefault();
          }
        },
        keyup: $.proxy(this._handler, this),
        click: $.proxy(this._handler, this)
      });
$("#"+idbuttin).keyup(function(event){
    if(event.keyCode == 13){
        $("#"+idbuttin).click();
        parent.resetmorefilters(idbuttin)
    }
});
      // cache input values for searching
      this.updateCache();

      // rewrite internal _toggleChecked fn so that when checkAll/uncheckAll is fired,
      // only the currently filtered elements are checked
      instance._toggleChecked = function(flag, group) {
        var $inputs = (group && group.length) ?  group : this.labels.find('input');
        var _self = this;

        // do not include hidden elems if the menu isn't open.
        var selector = instance._isOpen ?  ':disabled, :hidden' : ':disabled';

        $inputs = $inputs
          .not(selector)
          .each(this._toggleState('checked', flag));

        // update text
        this.update();

        // gather an array of the values that actually changed
        var values = $inputs.map(function() {
          return this.value;
        }).get();

        // select option tags
        this.element.find('option').filter(function() {
          if(!this.disabled && $.inArray(this.value, values) > -1) {
            _self._toggleState('selected', flag).call(this);
          }
        });

        // trigger the change event on the select
        if($inputs.length) {
          this.element.trigger('change');
        }
      };

      // rebuild cache when multiselect is updated
      var doc = $(document).bind('multiselectrefresh', $.proxy(function() {
        this.updateCache();
        this._handler();
      }, this));

      // automatically reset the widget on close?
      if(this.options.autoReset) {
        doc.bind('multiselectclose', $.proxy(this._reset, this));
      }
    },

    // thx for the logic here ben alman
    _handler: function(e) {
      var term = $.trim(this.input[0].value.toLowerCase()),

      // speed up lookups
      rows = this.rows, inputs = this.inputs, cache = this.cache;

      if(!term) {
        rows.show();
          var flag= parent.$("#searchtype").val();
   if(flag=="advance"){
       // resetAjax(term);
   }
      } else {
        rows.hide();

        var regex = new RegExp(term.replace(rEscape, "\\$&"), 'gi');

        this._trigger("filter", e, $.map(cache, function(v, i) {
          if(v.search(regex) !== -1) {
            rows.eq(i).show();
            return inputs.get(i);
          }

          return null;
        }));
          var flag= parent.$("#searchtype").val();
   if(flag=="advance"){
        // resetAjax(term);
      }
      }

      // show/hide optgroups
      this.instance.menu.find(".ui-multiselect-optgroup-label").each(function() {
        var $this = $(this);
        var isVisible = $this.nextUntil('.ui-multiselect-optgroup-label').filter(function() {
          return $.css(this, "display") !== 'none';
        }).length;

        $this[isVisible ? 'show' : 'hide']();
      });
    },

    _reset: function() {
      this.input.val('').trigger('keyup');
    },

    updateCache: function() {
      // each list item
      this.rows = this.instance.menu.find(".ui-multiselect-checkboxes li:not(.ui-multiselect-optgroup-label)");

      // cache
      this.cache = this.element.children().map(function() {
        var elem = $(this);

        // account for optgroups
        if(this.tagName.toLowerCase() === "optgroup") {
          elem = elem.children();
        }

        return elem.map(function() {
          return this.innerHTML.toLowerCase();
        }).get();
      }).get();
    },

    widget: function() {
      return this.wrapper;
    },

    destroy: function() {
      $.Widget.prototype.destroy.call(this);
      this.input.val('').trigger("keyup");
      this.wrapper.remove();
    }
  });
    $.widget('ech.multiselectfiltertable', {

    options: {
      label: 'Filter:',
      width: null, /* override default width set in css file (px). null will inherit */
      placeholder: 'Enter keywords',
      autoReset: false
    },

    _create: function() {
      var opts = this.options;
      var elem = $(this.element);
var elementid = elem.attr('name')
 var idbuttin='multitypetb' + elementid
      // get the multiselect instance
      var instance = (this.instance = (elem.data('echMultiselecttable') || elem.data("multiselecttable") || elem.data("ech-multiselecttable")));

      // store header; add filter class so the close/check all/uncheck all links can be positioned correctly
      var header = (this.header = instance.menu.find('.ui-multiselect-header').addClass('ui-multiselect-hasfilter'));

      // wrapper elem
      var wrapper = (this.wrapper = $('<div style="width:85%;" class="ui-multiselect-filter">' + (opts.label.length ? opts.label : '') + '<input id="'+idbuttin+'"   placeholder="'+opts.placeholder+'" style="width:70%;" type="text"' + (/\d/.test(opts.width) ? 'style="width:'+opts.width+'px"' : '') + ' /></div><div style="width:15%;float:right; margin-top: -18px;" title="Advanced Filters" ><i class="fa fa-floppy-o" onclick=parent.openadvancefilter() ></i></div>').prependTo(this.header));

      // reference to the actual inputs
      this.inputs = instance.menu.find('input[type="checkbox"], input[type="radio"]');

      // build the input box
      this.input = wrapper.find('input').bind({
        keydown: function(e) {
          // prevent the enter key from submitting the form / closing the widget
          if(e.which === 13) {
            e.preventDefault();
          }
        },
        keyup: $.proxy(this._handler, this),
        click: $.proxy(this._handler, this)
      });
$("#"+idbuttin).keyup(function(event){
    if(event.keyCode == 13){
        $("#"+idbuttin).click();
        parent.resetmorefilters(idbuttin)
    }
});
      // cache input values for searching
      this.updateCache();

      // rewrite internal _toggleChecked fn so that when checkAll/uncheckAll is fired,
      // only the currently filtered elements are checked
      instance._toggleChecked = function(flag, group) {
        var $inputs = (group && group.length) ?  group : this.labels.find('input');
        var _self = this;

        // do not include hidden elems if the menu isn't open.
        var selector = instance._isOpen ?  ':disabled, :hidden' : ':disabled';

        $inputs = $inputs
          .not(selector)
          .each(this._toggleState('checked', flag));

        // update text
        this.update();

        // gather an array of the values that actually changed
        var values = $inputs.map(function() {
          return this.value;
        }).get();

        // select option tags
        this.element.find('option').filter(function() {
          if(!this.disabled && $.inArray(this.value, values) > -1) {
            _self._toggleState('selected', flag).call(this);
          }
        });

        // trigger the change event on the select
        if($inputs.length) {
          this.element.trigger('change');
        }
      };

      // rebuild cache when multiselect is updated
      var doc = $(document).bind('multiselectrefresh', $.proxy(function() {
        this.updateCache();
        this._handler();
      }, this));

      // automatically reset the widget on close?
      if(this.options.autoReset) {
        doc.bind('multiselectclose', $.proxy(this._reset, this));
      }
    },

    // thx for the logic here ben alman
    _handler: function(e) {
      var term = $.trim(this.input[0].value.toLowerCase()),

      // speed up lookups
      rows = this.rows, inputs = this.inputs, cache = this.cache;

      if(!term) {
        //rows.show();
         var flag= parent.$("#searchtype").val();
   if(flag=="table"){
        //resetAjax(term);
   }
      } else {
        //rows.hide();

        var regex = new RegExp(term.replace(rEscape, "\\$&"), 'gi');

        this._trigger("filter", e, $.map(cache, function(v, i) {
          if(v.search(regex) !== -1) {
           // rows.eq(i).show();
            return inputs.get(i);
          }

          return null;
        }));
        var flag= parent.$("#searchtype").val();
   if(flag=="table"){
        //resetAjax(term);
      }
      }

      // show/hide optgroups
      this.instance.menu.find(".ui-multiselect-optgroup-label").each(function() {
        var $this = $(this);
        var isVisible = $this.nextUntil('.ui-multiselect-optgroup-label').filter(function() {
          return $.css(this, "display") !== 'none';
        }).length;

        $this[isVisible ? 'show' : 'hide']();
      });
    },

    _reset: function() {
      this.input.val('').trigger('keyup');
    },

    updateCache: function() {
      // each list item
      this.rows = this.instance.menu.find(".ui-multiselect-checkboxes li:not(.ui-multiselect-optgroup-label)");

      // cache
      this.cache = this.element.children().map(function() {
        var elem = $(this);

        // account for optgroups
        if(this.tagName.toLowerCase() === "optgroup") {
          elem = elem.children();
        }

        return elem.map(function() {
          return this.innerHTML.toLowerCase();
        }).get();
      }).get();
    },

    widget: function() {
      return this.wrapper;
    },

    destroy: function() {
      $.Widget.prototype.destroy.call(this);
      this.input.val('').trigger("keyup");
      this.wrapper.remove();
    }
  });

})(jQuery);
