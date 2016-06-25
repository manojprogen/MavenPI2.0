<%-- 
    Document   : Test
    Created on : Aug 10, 2009, 2:00:30 PM
    Author     : Administrator
--%>

<html>
<head>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Draggables and droppables demo - Interface plugin for jQuery</title>
		<script type="text/javascript" src="javascript/treeview/jquery.js"></script>
		<script type="text/javascript" src="javascript/interface.js"></script>
<style type="text/css" media="all">

body
{
	background: #fff;
	height: 100%;
	font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: 11px;
	min-height: 600px;
}
.myTree,
.myTree ul
{
	list-style: none;
	padding-left: 22px;
}
.expandImage
{
	margin-right: 4px;
}
.folderImage
{
}
.textHolder
{
	height: 16px;
	line-height: 16px;
	padding-left: 6px;
}
span.dropOver
{
	background-color: #00c;
	color: #fff;
	height: 16px;
	line-height: 16px;
	padding-left: 6px;
}
.treeItem
{
	list-style: none;
}
</style>
</head>
<body>

<ul class="myTree">
	<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Folder 1</span>
		<ul>
			<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Subfolder 1 1</span></li>
			<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Subfolder 1 2</span>
				<ul style="display: none;">
					<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Subfolder 1 2 1</span></li>

					<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Subfolder 1 2 2</span></li>
					<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Subfolder 1 2 3</span></li>
				</ul>
			</li>
		</ul>
	</li>
	<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Folder 2</span>

		<ul>
			<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Subfolder 2 1</span></li>
			<li class="treeItem"><img src="images/treeViewImages/folder-closed.gif" class="folderImage" /><span class="textHolder">Subfolder 2 2</span></li>
		</ul>
	</li>
</ul>

<script type="text/javascript">
$(document).ready(
	function()
	{
		tree = $('#myTree');
		$('li', tree.get(0)).each(
			function()
			{
				subbranch = $('ul', this);
				if (subbranch.size() > 0) {
					if (subbranch.eq(0).css('display') == 'none') {
						$(this).prepend('<img src="images/treeViewImages/plus.gif" width="16" height="16" class="expandImage" />');
					} else {
						$(this).prepend('<img src="images/treeViewImages/minus.gif" width="16" height="16" class="expandImage" />');
					}
				} else {
					$(this).prepend('<img src="images/treeViewImages/key.png" width="16" height="16" class="expandImage" />');
				}
			}
		);
		$('img.expandImage', tree.get(0)).click(
			function()
			{
				if (this.src.indexOf('spacer') == -1) {
					subbranch = $('ul', this.parentNode).eq(0);
					if (subbranch.css('display') == 'none') {
						subbranch.show();
						this.src = 'images/bullet_toggle_minus.png';
					} else {
						subbranch.hide();
						this.src = 'images/treeViewImages/plus.gif';
					}
				}
			}
		);
		$('span.textHolder').Droppable(
			{
				accept			: 'treeItem',
				hoverclass		: 'dropOver',
				activeclass		: 'fakeClass',
				tollerance		: 'pointer',
				onhover			: function(dragged)
				{
					if (!this.expanded) {
						subbranches = $('ul', this.parentNode);
						if (subbranches.size() > 0) {
							subbranch = subbranches.eq(0);
							this.expanded = true;
							if (subbranch.css('display') == 'none') {
								var targetBranch = subbranch.get(0);
								this.expanderTime = window.setTimeout(
									function()
									{
										$(targetBranch).show();
										$('img.expandImage', targetBranch.parentNode).eq(0).attr('src', 'images/bullet_toggle_minus.png');
										$.recallDroppables();
									},
									500
								);
							}
						}
					}
				},
				onout			: function()
				{
					if (this.expanderTime){
						window.clearTimeout(this.expanderTime);
						this.expanded = false;
					}
				},
				ondrop			: function(dropped)
				{
					if(this.parentNode == dropped)
						return;
					if (this.expanderTime){
						window.clearTimeout(this.expanderTime);
						this.expanded = false;
					}
					subbranch = $('ul', this.parentNode);
					if (subbranch.size() == 0) {
						$(this).after('<ul></ul>');
						subbranch = $('ul', this.parentNode);
					}
					oldParent = dropped.parentNode;
					subbranch.eq(0).append(dropped);
					oldBranches = $('li', oldParent);
					if (oldBranches.size() == 0) {
						$('img.expandImage', oldParent.parentNode).src('images/spacer.gif');
						$(oldParent).remove();
					}
					expander = $('img.expandImage', this.parentNode);
					if (expander.get(0).src.indexOf('spacer') > -1)
						expander.get(0).src = 'images/bullet_toggle_minus.png';
				}
			}
		);
		$('li.treeItem').Draggable(
			{
				revert		: true,
				autoSize		: true,
				ghosting			: true/*,
				onStop		: function()
				{
					$('span.textHolder').each(
						function()
						{
							this.expanded = false;
						}
					);
				}*/
			}
		);
	}
);
</script>
		<script language="JavaScript" type="text/javascript">var client_id = 1;</script>

		<script language="JavaScript" src="http://stats.byspirit.ro/track.js" type="text/javascript"></script>
		<noscript>
		<p><img alt="" src="http://stats.byspirit.ro/image.php?client_id=1" width="1" height="1" /></p>
		</noscript>
</body>
</html>