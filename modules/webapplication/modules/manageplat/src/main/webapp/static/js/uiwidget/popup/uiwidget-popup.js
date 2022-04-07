/**
 * Popup弹出窗口
 */
;(function($){
	$.uiwidget = $.uiwidget || {};
	$.uiwidget.Popup = function(target, cfg){
		$.extend(this, cfg);//本对象加载cfg属性
		this.target = target;//绑定的dom el
		this.init();//初始化
	};
	/**
	 * 类原型的相关属性和方法定义
	 */
	$.uiwidget.Popup.prototype = {
		dragEnable : false,//可以拖拽
		showMask : true,//有遮罩
		display : false,//判断初始化时是否显示
		/**
		 * 初始化
		 */
		init : function(){
			var t = this;
			if(t.dragEnable && $.uiwidget.Drag){
				t.target.drag();
			}
			t.target.css({
				position : 'absolute',
				zIndex:20002
			});
			$(window).bind('resize', function(){
				t.centerLayout();
			});
			$(window).bind('scroll', function(){
				t.centerLayout();
			});
		},
		/**
		 * 显示Popup
		 */
		show : function(){
			var t = this;
			if (t.showMask) {
				t.getMaskEl().show();
			}
			t.target.show();
			t.centerLayout();
		},
		/**
		 * 隐藏Popup
		 */
		hide : function(){
			var t = this;
			if (t.showMask) {
				t.getMaskEl().hide();
			}
			t.target.hide();
		},
		/**
		 * 居中定位
		 */
		centerLayout : function(){
			var t = this;
			var scrollLeft = (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
   			 var scrollTop = (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
			t.target.css({
				left : ($(window).width()-t.target.width())/2+scrollLeft,
				top : ($(window).height()-t.target.height())/2+scrollTop
			});
			if(t.target.css('display') == 'none') return;
			t.getMaskEl().height($(document).height());		
		},
		/**
		 * 渲染遮罩
		 */
		renderMask : function(){
			var maskContent = $('<div id="popupMask'+this.id+'" class="popup-mask"><iframe frameborder="0" style="width:100%;height:100%"></iframe></div>');
			$(document.body).append(maskContent);
		   	return maskContent;  
		},
		getMaskEl : function(){
			return this.maskEl || (this.maskEl = this.renderMask());
		}
	};
	
	$.fn.popup = function(cfg){
		return new $.uiwidget.Popup(this, cfg);
	};
	/**
	 * 弹出显示
	 * @param {Object} cfg
	 */
	$.uiwidget.popupShow = function(cfg){
		var t = $('#'+cfg.id);
		var p = $.uiwidget.popup = $.uiwidget.popup || {};
		if(!$(p).attr(cfg.id)){
			($(p).attr(cfg.id, new $.uiwidget.Popup(t, cfg)));
		}
		var obj = $(p).attr(cfg.id);
		obj.show();
		
		return obj;
	};
	/**
	 * 隐藏
	 * @param {Object} cfg
	 */
	$.uiwidget.popupHide = function(cfg){
		var obj = $($.uiwidget.popup).attr(cfg.id);
		if(obj){
			obj.hide();
			$($.uiwidget.popup).attr(cfg.id, null);
			$('#popupMask'+cfg.id).remove();
		}
	};
	
	$.uiwidget.showMsgbox = function(msg,time,callback,flag){
		var scrollLeft = (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
   		var scrollTop = (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
		var MsgBox = document.createElement("div");
		MsgBox.className = "popup-msgbox";	
		
		if(flag == 0)//失败
		{
			MsgBox.innerHTML = '<span class="fail">'+msg+'</span>';
		}
		else
		{
			MsgBox.innerHTML = '<span class="success">'+msg+'</span>';
		}
		document.body.appendChild(MsgBox);
		MsgBox.style.position = "absolute";
		MsgBox.style.zIndex = 999;
		MsgBox.style.display = "block";
		MsgBox.style.left = scrollLeft+(($(window).width()-$(MsgBox).width())/2)+'px';
		MsgBox.style.top = scrollTop+(($(window).height()-$(MsgBox).width())/2)+'px';
		window.setTimeout(function(){document.body.removeChild(MsgBox);if(callback){callback();}},time);
	};
})(jQuery);