<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理平台</title>

<link href="styles/index_css.css"  type="text/css" rel="stylesheet"/>
<link href="styles/TabPanel.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="scripts/jquery.js"></script>
<script type="text/javascript" src="scripts/Fader.js"></script>
<script type="text/javascript" src="scripts/TabPanel.js"></script>
<script type="text/javascript" src="scripts/Math.uuid.js"></script>

<style type="text/css"  >
#accountcheck:hover{background:url(images/demo_lf_menu_bj03.gif) no-repeat;}
#logcheck:hover{background:url(images/demo_lf_menu_bj03.gif) no-repeat;}
.bshow{background:#fff;cursor: pointer;}
/*展开左边菜单按钮样式*/
.cp_hidden_gb {height: 41px;left: 2px;float:left;margin-top:220px;top: 50%;width: 9px;}
</style>

<script type="text/javascript"  >
var tabpanel;  
$(document).ready(function(){
    //初始化面板  
    tabpanel = new TabPanel({  
        renderTo:'tab',  
        height:'452px', 
        width:'80%',
        //border:'none', 
        active : 0,
        maxLength : 10,  
        items : []
    });
      
	//定义添加一个tab内容方法
	function add(id,title,html)
	{
	    tabpanel.addTab({id:id,
	    title:title,
	    html:html,
	    closable: true,
	    disabled:false});
    };
  
   //点击叶子节点菜单，显示tab内容
  $('.menuli').click(function(){	
	   var mid=$(this).attr("mid");
	   var title=$(this).text();
	   var u=$(this).attr("url");
	   var html='<iframe src="${ctx}'+u+'" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
	   var cout=tabpanel.getTabsCount();
	   if(cout>=tabpanel.maxLength)
	   {
	       alert("提示:打开的Tab面板过多,请先关闭部分!");
	   }
	   add(mid,title,html);
  	   tabpanel.show(mid);   
  }); 
 
  //默认展开菜单按钮隐藏
  $('#hshowleft').hide();
  //默认第一个子菜单显示，其他隐藏
  var firstmenu= $(".demo_lf_menu").find(".menu01").first();
  firstmenu.siblings().find("ul").hide();
  
  //初始化第一个子tab面板
  $('.demo_lf_menu .menu01 ul li').first().trigger("click");
  
  //隐藏左边菜单
  $('#showleft').click(function (){ 
     $('#left').hide(500);
     $('#hshowleft').show(500);
  });
  //展示左边菜单
  $('#hshowleft').click(function (){
     $('#left').show(500);
     $('#hshowleft').hide(500);
  });
  //点击一个子菜单
  $('.demo_lf_menu .menu01 p').click(function (){
     if($(this).parent().find("ul").css('display')=='none')
     {
         $(this).parent().find("ul").show(500);
         $(this).parent().siblings().find("ul").hide(500);
     }
     else
     {
         $(this).parent().find("ul").hide(500);
     }
  });
  
}); 
</script>

</head>
<body>
	<div class="cp_main">
		<div class="cp_hidden_gb" id="hshowleft" ><img src="images/demo_main_bj11.gif"  /></div>
		<!-- 左侧菜单 -->
		<div class="cp_left" id="left">
		    <h2 class="bt">管理目录</h2>
			<div class="demo_lf_menu">
			    <s:iterator value="modules" var="m" id="mi">
			        <s:if test="#mi.parent==null">
			            <div class="menu01">
							<p class="bt02" ><a href="#"><b id="td"></b>${mi.name}</a></p>
							<ul class="menu01_nr">
							    <s:iterator value="modules" id="mii"> 
							        <s:if test="#mii.parent!=null && #mii.parent.id==#mi.id">
								    <li class="menuli" onmouseover="this.className='bshow'" onmouseout="this.className=''" mid="${mii.id}" url="${mii.url}">${mii.name}</li>
								    </s:if>
								</s:iterator>
								
							</ul>
						</div>
			        </s:if>
			    </s:iterator>
	        </div>
	        <!-- 菜单隐藏按钮 -->
		    <div class="cp_left_gb" id="showleft" ><img src="images/demo_main_bj10.gif"  />					
		    </div>						
	    </div>
	    <!--右侧Tab内容展示 -->
	    <div id="tab" class="demo_rt" ></div>																													
	</div>	
</body>
</html>
