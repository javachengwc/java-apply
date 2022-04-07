/**
* Editor组件
*/
;(function($){
	$.uiwidget = $.uiwidget || {};
	
	$.uiwidget.isIE = !!window.ActiveXObject;   
	$.uiwidget.isIE6 = $.uiwidget.isIE && !window.XMLHttpRequest;
	$.uiwidget.isIE8 = $.uiwidget.isIE && !!document.documentMode;   
	$.uiwidget.isIE7 = $.uiwidget.isIE && !$.uiwidget.isIE6 && !$.uiwidget.isIE8; 
	
	$.uiwidget.Editor = function(target, cfg){
		$.extend(this, cfg);
		this.target = $(target);
		this.init();
		this.render();
	};
	
	$.uiwidget.Editor.prototype = {
		//field 编辑的表单元素
		
		//onComplete 编辑结束完执行
		
		//onCancel 编辑取消后执行

		//beforeComplete 编辑结束前执行
		updateBindEl : true
				
		,init : function(){
			this.editing = false;
		}
		,render : function(){
			this.editElWrap = $('<div class="uiwidget-editor"></div>');
			this.target.append(this.editElWrap);
			this.editEl = $(this.field);
			this.editElWrap.append(this.editEl);
			var t = this;
			this.editEl.blur(function(e){t.handlerOnBlur(e);});
			this.addEnterEvent(this.editEl);
		}
		,startEdit : function(el, value, box){
			this.bindEl = $(el);
			this.startValue = value || this.bindEl.text();
			if(this.triggerHandler('beforeStartEdit', [this]) === false){
				return;
			}
			this.editEl.height(box && box.height ? box.height : this.bindEl.innerHeight());
			var ua = navigator.userAgent.toLowerCase();
			// var isIE6 = ua.indexOf("msie 6") > -1;
			// $.debug('box.width='+box.width);
			if($.uiwidget.isIE6){
				if(box && box.width){
					this.editEl.width(box.width);				
				}else{
					this.editEl.width(parseFloat(this.bindEl.css('width')));
				}
			}else{
				if(box && box.width){
					this.editEl.width(box.width);				
				}else{
					this.editEl.width(this.bindEl.innerWidth());
				}
			}
			// $.debug('this.editEl.width='+this.editEl.outerWidth());	
			var o = this.bindEl.position();
			this.editElWrap.css('top', box? o.top + box.top : o.top);
			this.editElWrap.css('left', box ? o.left + box.left : o.left);
			
			this.editEl.val(this.startValue);
			this.show();
			this.editEl.focus();
			this.editEl.select();
			this.editing = true;
			this.triggerHandler('startEdit', [this]);
		}
		,completeEdit : function(e){
			if(!this.editing) return;
			if(this.triggerHandler('beforeComplete', [this], e) === false)
				return false;
			this.hide();
			this.bindEl.show();
			if(this.updateBindEl)
				this.bindEl.text(this.editEl.val());
			this.triggerHandler('onComplete', [this], e);
			this.editing = false;
			this.editEl.val('');
			return true;	
		}
		,cancelEdit : function(){
			if(!this.editing) return;
			this.editing = false;
			this.hide();
			this.bindEl.show();
			this.triggerHandler('onCancel', [this]);	
			this.editEl.val('');
		}
		,show : function(){
			this.editElWrap.show();
		}
		,hide : function(){
			this.editElWrap.hide();
		}
		,handlerOnBlur : function(e){
			this.completeEdit();
			if(!this.completeEdit()){
				this.cancelEdit();
			}
		}
		,addEnterEvent : function(editEl){
			var t = this;
			editEl.keydown(function(e){
				e.stopPropagation();
				if(e.which == 13){//enter
					t.completeEdit(e);
				}else if(e.which == 27){//esc
					t.cancelEdit(e);
				}	
			});
		}
		/**
		编辑器当前值
		*/
		,getValue : function(){
			return this.editEl.val();
		}
		/**
		编辑器编辑开始时的值
		*/
		,getStartValue : function(){
			return this.startValue;
		}
		/**
		取得编辑器元素
		*/
		,getEditEl : function(){
			return this.editEl;
		}
		/**
		取得被编辑的元素
		*/
		,getBindEl : function(){
			return this.bindEl;
		}
		
		/*
		绑定事件
		*/
		,bind : function(arg0, arg1, arg2){
			arg0 = arg0.toLowerCase();
			if(typeof arg1 == 'object')
				return this.editEl.bind(arg0, arg1, arg2);
			else 
				return this.editEl.bind(arg0, arg1);
		}
		,trigger : function( event, data){
			return this.editEl.trigger(event, data);
		}
		,triggerHandler : function(type, data, srcEvent){
			type = type.toLowerCase();
			if(srcEvent){
				if( this.editEl[0] ){
					var event = srcEvent;
					event.type = type;
					//触发事件时实际的元素
					event.triggerTarget = event.target;
					event.stopPropagation();
					$.event.trigger( event, data, this.editEl[0], false);
					return event.result;
				}
			}else{
				return this.editEl.triggerHandler(type, data);
			}		
		}
	};
	
	/**
	使用编辑组件
	*/
	$.fn.editor = function(cfg){
		return new $.uiwidget.Editor(this, cfg);
	};
})(jQuery);	