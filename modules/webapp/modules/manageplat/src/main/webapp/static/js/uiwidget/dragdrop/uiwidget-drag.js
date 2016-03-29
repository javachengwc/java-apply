/**
* 为元素提供拖拉功能
*/
;(function($){
	$.uiwidget = $.uiwidget || {};
	$.uiwidget.Drag = function(target, cfg){
		$.extend(this, cfg);
		this.el = $(target);
		this.setup();
	};
	
	$.uiwidget.Drag.prototype = {
		setup : function(){
			this.handler = this.handler || this.el;
			this.el.css('position', 'absolute');
			this.el.css('z-index' , $.uiwidget.Drag.z_index++);			
			this.getHelper().css('cursor' , 'move');
			this.handler.bind('mousedown', this, this.onMouseDown);
		}
		
		,getHelper : function(){
			if (!this.helper) {
				$(document.body).append('<div id="dragHelper" unselectable=on style="background-color: transparent;position:absolute;display:none:cursor:move;border:2px dotted #bbb;mozUserSelect:none;z-index:10000000001;left:-1000px;top:-1000px;"></div>');
				this.helper = $('#dragHelper');
			}
			return this.helper;
		}
		,onMouseDown : function(e){
			var t = e.data;
			t.el.css('z-index' , $.uiwidget.Drag.z_index++);
			//$.debug('z_index ' + t.el.css('z-index'));
			t._x = e.pageX - t.el.attr("offsetLeft");
			t._y = e.pageY - t.el.attr("offsetTop");
			t.getHelper().height(t.el.height());
			t.getHelper().width(t.el.width());
			t.getHelper().css("left", t.el.css('left'));
			t.getHelper().css("top", t.el.css('top'));			
			$(document).bind("mousemove", t, t.onMouseMove);
			$(document).bind("mouseup", t, t.onMouseUp);
		}
		
		,onMouseMove : function(e){
			var t = e.data;
			t.el.hide();
			t.getHelper().show();
			t.getHelper().css("left", e.pageX - t._x + "px");
			t.getHelper().css("top", e.pageY - t._y + "px");
		}
	
		,onMouseUp : function(e){
			var t = e.data;
			t.el.css("left", t.getHelper().css('left'));
			t.el.css("top", t.getHelper().css("top"));
			t.el.show();
			t.getHelper().hide();			
			$(document).unbind("mousemove");
			$(document).unbind("mouseup");
		}
	};
	$.uiwidget.Drag.z_index = 1000000000;
	$.fn.drag = function(cfg){
		return new $.uiwidget.Drag(this, cfg);
	};
})(jQuery);	