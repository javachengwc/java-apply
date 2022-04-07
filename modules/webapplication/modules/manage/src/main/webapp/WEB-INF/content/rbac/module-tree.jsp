<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<html>
	<head>
		<title>模块目录</title>
		<style>
		    body{ margin: 0px; width: 100%;overflow:scroll;}
		    ul.ztree {margin-top: 0px;overflow-y:scroll;overflow-x:auto;width:100%; height:100%;border:#d1d1d1 1px solid;}
		</style>
		<link rel="stylesheet" href="${ctx}/styles/zTreeStyle/zTreeStyle.css" type="text/css">
	    <SCRIPT type=text/javascript src="${ctx}/scripts/jquery.js"></SCRIPT>
	    <script type="text/javascript" src="${ctx}/scripts/jquery.ztree.core-3.1.js"></script>
		
		<script LANGUAGE="JavaScript">
		var zTree;
		var reAsNode;
		var setting = {
		
		    view: {
				dblClickExpand: true,
				showLine: true,
				selectedMulti: false,
				expandSpeed: ($.browser.msie && parseInt($.browser.version)<=6)?"":"fast"
			},
			async: {
				enable: true,
				type: "post",
				dataType: "text",
				url:  "${ctx}/rbac/module!child.action", 
				autoParam: [ "level", "id" ],
				dataFilter: null
			},
			callback : {
				onClick : function(event, treeId, treeNode) {
				    if(treeNode.id>=0)
				    {
					    reAsNode = treeNode;
					    var url = "${ctx}/rbac/module!hierarchyPage.action?parentid=" + treeNode.id;
						if(treeNode.level>=2)
						    url="${ctx}/rbac/resource!hierarchyPage.action?moduleid="+treeNode.id;
						    
						window.parent.document.getElementById("right").src=url;
					}
				},
				expand : function(event, treeId, treeNode) {
				   
				},
				onAsyncSuccess:zTreeOnAsyncSuccess,
				collapse : function(event, treeId, treeNode) {
				}
			}
		};
		
		function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
		    //初次加载
		    if(treeNode==null || treeNode==undefined)
		    {
		       reAsNode=zTree.getNodes()[0];
		       refresh(zTree.getNodes()); 
		    }else
		    {
		        if(treeNode!=null && treeNode.isParent && (treeNode.nodes==null ||treeNode.nodes.length==0 ) && treeNode.parentNode!=null )
		        {
		            zTree.reAsyncChildNodes(treeNode.parentNode, "refresh");
		        }
		    }
	    }
	    function reAsyn() {
			if(reAsNode!=null && !reAsNode.isParent)
		         reAsNode.isParent=true;
			zTree.reAsyncChildNodes(reAsNode, "refresh");
		}
		
		function refresh(objs) {
		    for(var i=0;i<objs.length;i++)
		    {
		        zTree.reAsyncChildNodes(objs[i], "refresh");
			}
		}
		function refreshTree() {
			zTree = $.fn.zTree.init($("#tree"), setting);
		}
		$(document).ready(function() {
			refreshTree();
		});
		</script>
	</head>
	
	<body>
	     <ul id="tree" class="ztree" ></ul>
	</body>
</html>