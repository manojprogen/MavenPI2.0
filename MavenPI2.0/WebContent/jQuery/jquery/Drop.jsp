<%-- 
    Document   : Drop
    Created on : Aug 6, 2009, 1:09:00 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<!doctype html>

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html lang="en">
	<head>
		<title>jQuery UI Droppable - Simple photo manager</title>
		<link type="text/css" href="<%=request.getContextPath()%>/jQuery/themes/base/ui.all.css" rel="stylesheet" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/ui/ui.core.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/ui/ui.draggable.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/ui/ui.droppable.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/ui/ui.resizable.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/ui/ui.dialog.js"></script>
		<link type="text/css" href="<%=request.getContextPath()%>/jQuery/demos.css" rel="stylesheet" />
		<style type="text/css">
			#gallery { float: left; width: 65%; min-height: 12em; } * html #gallery { height: 12em; } /* IE6 */
			.gallery.custom-state-active { background: #eee; }
			.gallery li { float: left; width: 96px; padding: 0.4em; margin: 0 0.4em 0.4em 0; text-align: center; }
			.gallery li h5 { margin: 0 0 0.4em; cursor: move; }
			.gallery li a { float: right; }
			.gallery li a.ui-icon-zoomin { float: left; }
			.gallery li img { width: 100%; cursor: move; }

			#trash { float: right; width: 32%; min-height: 18em; padding: 1%;} * html #trash { height: 18em; } /* IE6 */
			#trash h4 { line-height: 16px; margin: 0 0 0.4em; }
			#trash h4 .ui-icon { float: left; }
			#trash .gallery h5 { display: none; }
		</style>
		<script type="text/javascript">
			$(function() {
				// there's the gallery and the trash
				var $gallery = $('#gallery'), $trash = $('#trash');

				// let the gallery items be draggable
				$('li',$gallery).draggable({
					cancel: 'a.ui-icon',// clicking an icon won't initiate dragging
					revert: 'invalid', // when not dropped, the item will revert back to its initial position
					containment: $('#demo-frame').length ? '#demo-frame' : 'document', // stick to demo-frame if present
					helper: 'clone',
					cursor: 'move'
				});

				// let the trash be droppable, accepting the gallery items
				$trash.droppable({
					accept: '#gallery > li',
					activeClass: 'ui-state-highlight',
					drop: function(ev, ui) {
						deleteImage(ui.draggable);
					}
				});

				// let the gallery be droppable as well, accepting items from the trash
				$gallery.droppable({
					accept: '#trash li',
					activeClass: 'custom-state-active',
					drop: function(ev, ui) {
						recycleImage(ui.draggable);
					}
				});

				// image deletion function
				var recycle_icon = '<a href="link/to/recycle/script/when/we/have/js/off" title="Recycle this image" class="ui-icon ui-icon-triangle-1-w ">Recycle image</a>';
				function deleteImage($item) {
					$item.fadeOut(function() {
						var $list = $('ul',$trash).length ? $('ul',$trash) : $('<ul class="gallery ui-helper-reset"/>').appendTo($trash);

						$item.find('a.ui-icon-triangle-1-e').remove();
						$item.append(recycle_icon).appendTo($list).fadeIn(function() {
							$item.animate({ width: '48px' }).find('img').animate({ height: '36px' });
						});
					});
				}

				// image recycle function
				var trash_icon = '<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon ui-icon-triangle-1-e ">Delete image</a>';
				function recycleImage($item) {
					$item.fadeOut(function() {
						$item.find('a.ui-icon-triangle-1-w ').remove();
						$item.css('width','96px').append(trash_icon).find('img').css('height','72px').end().appendTo($gallery).fadeIn();
					});
				}

				// image preview function, demonstrating the ui.dialog used as a modal window
				/*function viewLargerImage($link) {
					var src = $link.attr('href');
					var title = $link.siblings('img').attr('alt');
					var $modal = $('img[src$="'+src+'"]');

					if ($modal.length) {
						$modal.dialog('open')
					} else {
						var img = $('<img alt="'+title+'" width="384" height="288" style="display:none;padding: 8px;" />')
							.attr('src',src).appendTo('body');
						setTimeout(function() {
							img.dialog({
									title: title,
									width: 400,
									modal: true
								});
						}, 1);
					}
				}*/

				// resolve the icons behavior with event delegation
				$('ul.gallery > li').click(function(ev) {
					var $item = $(this);
					var $target = $(ev.target);

					if ($target.is('a.ui-icon-triangle-1-e')) {
						deleteImage($item);
					}  else if ($target.is('a.ui-icon-triangle-1-w ')) {
						recycleImage($item);
					}

					return false;
				});
			});
		</script>

	</head>
	<body>
		<div class="demo ui-widget ui-helper-clearfix">

			<ul id="gallery" class="gallery ui-helper-reset ui-helper-clearfix">
				<li class="ui-widget-content ui-corner-tr">
					<h5 class="ui-widget-header">High Tatras</h5>
                    <a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-triangle-1-e ">Delete image</a>

				</li>
				<li class="ui-widget-content ui-corner-tr">
					<h5 class="ui-widget-header">High Tatras 2</h5>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-triangle-1-e ">Delete image</a>
				</li>

				<li class="ui-widget-content ui-corner-tr">
					<h5 class="ui-widget-header">High Tatras 3</h5>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-triangle-1-e ">Delete image</a>
				</li>
				<li class="ui-widget-content ui-corner-tr">

					<h5 class="ui-widget-header">High Tatras 4</h5>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-triangle-1-e ">Delete image</a>
				</li>
			</ul>

			<div id="trash" class="ui-widget-content ui-state-default">

				<h4 class="ui-widget-header"><span class="ui-icon ui-icon-triangle-1-e ">Trash</span> Trash</h4>
            </div>

		</div><!-- End demo -->

		
	</body>
</html>
