(function() {
  var out$ = typeof exports != 'undefined' && exports || this;

  var doctype = '<?xml version="1.0" standalone="no"?><!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">';

  function isExternal(url) {
    return url && url.lastIndexOf('http',0) == 0 && url.lastIndexOf(window.location.host) == -1;
  }

  function inlineImages(el, callback) {
    var images = el.querySelectorAll('image');
    var left = images.length;
    if (left == 0) {
      callback();
    }
    for (var i = 0; i < images.length; i++) {
      (function(image) {
        var href = image.getAttribute('xlink:href');
        if (href) {
          if (isExternal(href.value)) {
            console.warn("Cannot render embedded images linking to external hosts: "+href.value);
            return;
          }
        }
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');
        var img = new Image();
        href = href || image.getAttribute('href');
        img.src = href;
        img.onload = function() {
          canvas.width = img.width;
          canvas.height = img.height;
          ctx.drawImage(img, 0, 0);
          image.setAttribute('xlink:href', canvas.toDataURL('image/png'));
          left--;
          if (left == 0) {
            callback();
          }
        }
        img.onerror = function() {
          console.log("Could not load "+href);
          left--;
          if (left == 0) {
            callback();
          }
        }
      })(images[i]);
    }
  }

  function styles(el, selectorRemap) {
    var css = "";
    var sheets = document.styleSheets;
    for (var i = 0; i < sheets.length; i++) {
      if (isExternal(sheets[i].href)) {
        console.warn("Cannot include styles from other hosts: "+sheets[i].href);
        continue;
      }
      var rules = sheets[i].cssRules;
      if (rules != null) {
        for (var j = 0; j < rules.length; j++) {
          var rule = rules[j];
          if (typeof(rule.style) != "undefined") {
            var match = null;
            try {
              match = el.querySelector(rule.selectorText);
            } catch(err) {
              console.warn('Invalid CSS selector "' + rule.selectorText + '"', err);
            }
            if (match) {
              var selector = selectorRemap ? selectorRemap(rule.selectorText) : rule.selectorText;
              css += selector + " { " + rule.style.cssText + " }\n";
            } else if(rule.cssText.match(/^@font-face/)) {
              css += rule.cssText + '\n';
            }
          }
        }
      }
    }
    return css;
  }

  out$.svgAsDataUri = function(el, options, cb,chartType) {
    options = options || {};
    options.scale = options.scale || 1;
    var xmlns = "http://www.w3.org/2000/xmlns/";

    inlineImages(el, function() {
      var outer = document.createElement("div");
      var clone = el.cloneNode(true);
      var width, height;
      if(el.tagName == 'svg') {
          
        var box = el.getBoundingClientRect();
        if(chartType==='Trend-Combo'){
            alert("Y");
            width = box.width+200;
        }
        else{
            width = box.width+100;
        }
        height = box.height*1.21;
      } else {
        var box = el.getBBox();
        width = box.x + box.width;
        height = box.y + box.height;
        clone.setAttribute('transform', clone.getAttribute('transform').replace(/translate\(.*?\)/, ''));

        var svg = document.createElementNS('http://www.w3.org/2000/svg','svg')
        svg.appendChild(clone)
        clone = svg;
      }

      clone.setAttribute("version", "1.1");
      clone.setAttributeNS(xmlns, "xmlns", "http://www.w3.org/2000/svg");
      clone.setAttributeNS(xmlns, "xmlns:xlink", "http://www.w3.org/1999/xlink");
      clone.setAttribute("width", width * options.scale);
      clone.setAttribute("height", height * options.scale);
      clone.setAttribute("viewBox", "0 0 " + width + " " + height);
      outer.appendChild(clone);

      var css = styles(el, options.selectorRemap);
      var s = document.createElement('style');
      s.setAttribute('type', 'text/css');
      s.innerHTML = "<![CDATA[\n" + css + "\n]]>";
      var defs = document.createElement('defs');
      defs.appendChild(s);
      clone.insertBefore(defs, clone.firstChild);

      var svg = doctype + outer.innerHTML;
      var uri = 'data:image/svg+xml;base64,' + window.btoa(unescape(encodeURIComponent(svg)));
      if (cb) {
        cb(uri);
      }
    });
  }

  out$.saveSvgAsPng = function(el, name, options,chartId,chartType) {
//      alert(chartId);
    options = options || {};
    out$.svgAsDataUri(el, options, function(uri) {
      var image = new Image();
      image.src = uri;
      image.onload = function() {
        var canvas = parent.document.createElement('canvas');
        canvas.width = image.width+50;
        canvas.height = image.height+50;
        var context = canvas.getContext('2d');
        context.fillStyle = "#FFFFFF";
        context.fillRect(0,0,canvas.width ,canvas.height);
        context.drawImage(image, 50, 50);
        context.fillStyle = "black";
        context.font = "bold 23px Courier New";
        context.fillText(chartId, 50, 50);
        var a = parent.document.createElement('a');
        a.download = name;
        a.href = canvas.toDataURL('image/png');
        parent.document.body.appendChild(a);
        a.addEventListener("click", function(e) {
          a.parentNode.removeChild(a);
        });
        a.click();
      }
    },chartType);
  }
  out$.saveSvgAsPDF = function(el, name, options,chartName,isName,chartId,w,h,pdf,callback) {
//      alert(chartId);
    pdf1=pdf;
    options = options || {};
    out$.svgAsDataUri(el, options, function(uri) {
      var image = new Image();
      image.src = uri;
      image.onload = function() {
        var canvas = parent.document.createElement('canvas');
        canvas.width = image.width;
        canvas.height = image.height;
//        alert("@@@"+canvas.width+":"+canvas.height);
        var context = canvas.getContext('2d');
        context.fillStyle = "black";
        context.font = "30px Arial";
//        if(isName){
            context.fillText(chartName, (canvas.width-(20*chartName.toString().length)), 30);
//        }
        context.drawImage(image, 0, 0);
        context.globalCompositeOperation = "destination-over";
        context.fillStyle = "#FFFFFF";
        context.fillRect(0,0,canvas.width,canvas.height);
//        context.globalCompositeOperation = "source-over";
//        context.lineWidth = 2.5;
//        context.strokeStyle="#000000";
//        alert("AAAA"+canvas.width+":"+canvas.height+":"+h*2.2265-5);
//        context.strokeRect(0, 0, w*3.779527559, h*3.779527559);
        var data = canvas.toDataURL('image/jpeg');
        
        callback(data);
      }
    });
  }
})();