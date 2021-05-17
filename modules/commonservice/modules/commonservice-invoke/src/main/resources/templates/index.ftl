<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <title>简单后台</title>
  <link rel="stylesheet" type="text/css" href="static/css/style.css" />
  <link rel="stylesheet" type="text/css" href="static/css/uiwidget/popup/uiwidget-popup.css"/>
  <link rel="stylesheet" type="text/css" href="static/css/uiwidget/previewer/uiwidget-previewer.css" />
  <link rel="stylesheet" type="text/css" href="static/css/uiwidget/messagebox/uiwidget-messagebox.css"/>
  <link rel="stylesheet" type="text/css" href="static/css/uiwidget/pagebar/uiwidget-pagebar.css"/>
  <link rel="stylesheet" type="text/css" href="static/css/validate.css" />
  <link rel="stylesheet" type="text/css" href="static/js/jquery-easyui-1.3.6/themes/gray/easyui.css"/>

  <script type="text/javascript" src="static/js/jquery-1.8.0.js"></script>
  <script type="text/javascript" src="static/js/jquery.form.js"></script>
  <script type="text/javascript" src="static/js/jquery.url.min.js"></script>
  <script type="text/javascript" src="static/js/jquery.cookie.min.js"></script>
  <script type="text/javascript" src="static/js/json.js"></script>

  <script type="text/javascript" src="static/js/commons/common-util.js"></script>
  <script type="text/javascript" src="static/js/commons/date-util.js"></script>
  <script type="text/javascript" src="static/js/commons/string-util.js"></script>
  <script type="text/javascript" src="static/js/commons/jquery-ext.js"></script>

  <script type="text/javascript" src="static/js/jquery-easyui-1.3.6/jquery.easyui.min.js"></script>
  <script type="text/javascript" src="static/js/jquery-easyui-1.3.6/locale/easyui-lang-zh_CN.js"></script>

  <script type="text/javascript" src="static/js/uiwidget/popup/uiwidget-popup.js"></script>
  <script type="text/javascript" src="static/js/uiwidget/previewer/uiwidget-previewer.js"></script>
  <script type="text/javascript" src="static/js/uiwidget/messagebox/uiwidget-messagebox.js"></script>
  <script type="text/javascript" src="static/js/uiwidget/pagebar/uiwidget-pagebar.js"></script>
  <script type="text/javascript" src="static/js/uiwidget/page/uiwidget-page.js"></script>

  <script type="text/javascript" src="static/js/validation.js"></script>
  <script type="text/javascript" src="static/js/layer/layer.js"></script>
  <script type="text/javascript" src="static/js/My97DatePicker/WdatePicker.js"></script>

  <script type="text/javascript">

  var accessComefromMenu=true;

  $(document).ready(function(){
      loadMenu();
  });

  function loadMenu()
  {
      $.get("/menu/getMenu",null,function(resp){
          var menus = resp.data;
          var menuHTML ="";
          if(menus.length<=0) {
            return;
          }
          for (var i = 0; i <menus.length; i++) {
              var  menu = menus[i];
              menuHTML+='<ul class="collapse">';
              menuHTML+='<span>'+parentMenu.text+'</span>';
              for (var j = 0; j < n.length; j++) {
                  var childMenu = parentMenu[n[j]];
                  var targetFlag="";
                  if(parentMenu.target){
                      targetFlag='target="'+parentMenu.target+'"';
                  }
                  menuHTML+='<li><a href="javascript:void(0);" url="'+childMenu.url+'" '+targetFlag+'>>>'+childMenu.name+'</a></li>';
              }
              menuCount++;
              menuHTML+='</ul>';
          }

          $('#left').append(menuHTML);

          $('#left ul').click(function(){

              if($(this).attr('class') == 'expand'){
                  $(this).attr('class', 'collapse');
                  $(this).find('li').hide();
              }else if($(this).attr('class') == 'collapse'){
                  $(this).attr('class', 'expand');
                  $(this).find('li').show();
              }
          });
          $('#left li').click(function(e){
              e.stopPropagation();
              e.preventDefault();
              var a = $(this).find('a');
              a.blur();
              var url = a.attr('url');
              if(a.attr("target")){
                  window.open(url);
                  return false;
              }
              $('#left li').removeClass('here');
              $(this).attr('class', 'here');

              $(document).unbind('keyup');

              accessComefromMenu=true;
              Validation.removeAllMessageDiv();//去除提示信息

              $('#right').page(url);

              $('#naviLeft').text('当前位置：'+$(this).parent().find('span').text() + $(this).text());
          });


          $('#left li').hide();
          $("#left").show();

          $($('#left ul')[0]).click();
          $($('#left li')[0]).click();

      },"json");
  }
  </script>

</head>
<body>

<div class="warp">
	<div class="top"><h2>简单后台</h2></div>
	<div class="navi">
		<div id="naviLeft" class="navi-left"> </div>
		<div class="navi-right"><span id="userName"></span> 欢迎</div>
	</div>
	<div id="main" class="main">
		<div class="left" id="left" style="display:none">

		</div>
		<div id="right" class="right">

		</div>
		<div style="clear:both;width:100%;height:0;overflow:hidden;"></div>
	</div>
</div>

</body>
</html>