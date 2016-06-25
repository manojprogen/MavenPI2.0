<!doctype html>
<html lang="en">
	<head>
		<title>jQuery UI Droppable - Simple photo manager</title>
		<link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.draggable.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.droppable.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.resizable.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.dialog.js"></script>
		<link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />

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
					//revert: 'invalid', // when not dropped, the item will revert back to its initial position
					//containment: $('#demo-frame').length ? '#demo-frame' : 'document', // stick to demo-frame if present
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
				var recycle_icon = '<a href="link/to/recycle/script/when/we/have/js/off" title="Recycle this image" class="ui-icon ui-icon-refresh">Recycle image</a>';
				function deleteImage($item) {
					$item.fadeOut(function() {
						var $list = $('ul',$trash).length ? $('ul',$trash) : $('<ul class="gallery ui-helper-reset"/>').appendTo($trash);

						$item.find('a.ui-icon-trash').remove();
						$item.append(recycle_icon).appendTo($list).fadeIn(function() {
							$item.animate({ width: '48px' }).find('img').animate({ height: '36px' });
						});
					});
				}

				// image recycle function
				var trash_icon = '<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>';
				function recycleImage($item) {
					$item.fadeOut(function() {
						$item.find('a.ui-icon-refresh').remove();
						$item.css('width','96px').append(trash_icon).find('img').css('height','72px').end().appendTo($gallery).fadeIn();
					});
				}

				// image preview function, demonstrating the ui.dialog used as a modal window
				function viewLargerImage($link) {
					var src = $link.attr('href');
					var title = $link.siblings('img').attr('alt');
					var $modal = $('img[src$="'+src+'"]');

					if ($modal.length) {
						$modal.dialog('open')
					} else {
						var img = $('<img alt="'+title+'"  style="display:none;padding: 8px;" />')
							.attr('src',src).appendTo('body');
						setTimeout(function() {
							img.dialog({
									title: title,
									width: 400,
									modal: true
								});
						}, 1);
					}
				}

				// resolve the icons behavior with event delegation
				$('ul.gallery > li').click(function(ev) {
					var $item = $(this);
					var $target = $(ev.target);

					if ($target.is('a.ui-icon-trash')) {
						deleteImage($item);
					} else if ($target.is('a.ui-icon-zoomin')) {
						viewLargerImage($target);
					} else if ($target.is('a.ui-icon-refresh')) {
						recycleImage($item);
					}

					return false;
				});
			});
		</script>
	</head>
	<body>
		<div class="demo ui-widget ui-helper-clearfix">
            <h4 class="ui-widget-header"> Gallery</h4>

			<ul id="gallery" class="gallery ui-helper-reset ui-helper-clearfix">

				 <li  class="ui-widget-content ui-corner-tr" title="Pie">
					<h5 class="ui-widget-header">Pie</h5>
					<img src="images/Pie_min.gif" alt="Pie" width="96" height="72" />
					<a href="images/Pie.gif" title="View larger image" class="ui-icon ui-icon-zoomin">View larger</a>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>
				</li>
				<li class="ui-widget-content ui-corner-tr" title="Pie 3D">
					<h5 class="ui-widget-header">Pie 3D</h5>
					<img src="images/Pie3D_min.gif" alt="Pie 3D" width="96" height="72" />
					<a href="images/Pie3D.gif" title="View larger image" class="ui-icon ui-icon-zoomin">View larger</a>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>
				</li>
				<li class="ui-widget-content ui-corner-tr" title="Ring">
					<h5 class="ui-widget-header">Ring</h5>
					<img src="images/Ring_min.gif" alt="Ring" width="96" height="72" />
					<a href="images/Ring.gif" title="View larger image" class="ui-icon ui-icon-zoomin">View larger</a>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>
				</li>
				<li class="ui-widget-content ui-corner-tr" title="Bar 3D">
					<h5 class="ui-widget-header">Bar 3D</h5>
					<img src="images/Bar3D_min.gif" alt="Bar 3D" width="96" height="72" />
					<a href="images/Bar3D.gif" title="View larger image" class="ui-icon ui-icon-zoomin">View larger</a>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>
				</li>
                    <li class="ui-widget-content ui-corner-tr" title="Bar">
					<h5 class="ui-widget-header">Bar</h5>
					<img src="images/Bar_min.gif" alt="Bar" width="96" height="72" />
					<a href="images/Bar.gif" title="View larger image" class="ui-icon ui-icon-zoomin">View larger</a>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>
				</li>
				<li class="ui-widget-content ui-corner-tr" title="Dual Axis">
					<h5 class="ui-widget-header">Dual Axis</h5>
					<img src="images/Dual Axis_min.gif" alt="Dual Axis" width="96" height="72" />
					<a href="images/Dual Axis.gif" title="View larger image" class="ui-icon ui-icon-zoomin">View larger</a>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>
				</li>
				
				<li class="ui-widget-content ui-corner-tr" title="Line">
					<h5 class="ui-widget-header">Line</h5>
					<img src="images/Line_min.gif" alt="Line" width="96" height="72" />
					<a href="images/Line.gif" title="View larger image" class="ui-icon ui-icon-zoomin">View larger</a>
					<a href="link/to/trash/script/when/we/have/js/off" title="Delete this image" class="ui-icon ui-icon-trash">Delete image</a>
				</li>
			</ul>

			<div id="trash" class="ui-widget-content ui-state-default">
				<h4 class="ui-widget-header"> </h4>
			</div>

		</div><!-- End demo -->

		
	</body>
</html>
