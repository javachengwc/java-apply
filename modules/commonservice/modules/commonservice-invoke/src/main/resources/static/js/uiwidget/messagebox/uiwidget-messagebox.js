/**
* Messagebox提示窗口
*/
;(function($){
	$.uiwidget = $.uiwidget || {};
	/**
	* Messagebox
	*/
	$.uiwidget.Messagebox = function(target, cfg){
		$.extend(this, cfg);
		this.messagebox = $(target);
		this.init();
		this.render();
	};
	
	
	
	$.uiwidget.Messagebox.prototype = {
		OK_TEXT : '确 定'
		,CANCEL_TEXT : '取 消'
    ,CLOSE_TEXT : '关 闭'
		,ASK_TITLE : '提示'
		,ALERT_TITLE : '警告'
    ,SUCCESS_TITLE : '成功'
    ,ERROR_TITLE : '失败'
	,WIDTH:426
	,init : function(){
			var SELF=this;
			$(window).bind('resize', function(){
				SELF.reLayout();
			});
			$(window).bind('scroll', function(){
				SELF.reLayout();

			});
			
		}
		,render : function(){
		
		}
		
		,renderWindow : function(){
		   var windowContent = $(['<div class="pop_box" onselectstart="return false;" style="-moz-user-select: none;width:'+WIDTH+'px;"><div class="pop_title"><div class="pop_title_left"><div class="pop_title_right">',
					   '<div class="pop_title_midle"></div><span class="pop_close" title="关闭" ></span>',
					   '</div></div></div>',
					   '<div class="pop_content" style="width:'+(WIDTH-2)+'px;">',
						 '<div class="msg-window-ico-prompt"></div><div class="msg-window-msg-content"><table cellpadding="0" cellspacing="0"><tr><td></td></tr></table>',
						 '</div>',
						 '<div class="msg-window-button" >',
			       '</div> ',
			       '</div>',
			       '</div>'].join(''));	
			  $(document.body).append(windowContent);
			  this.titleEl = windowContent.find('div.pop_title_midle');
			  this.msgEl = windowContent.find('div.pop_content td');
			  this.iconEl = windowContent.find('div.pop_content>div')[0];
			  this.buttonEl = windowContent.find('div.msg-window-button');
			  
        //只有title栏可以拖拽
			  return windowContent;
		}
		,renderMask : function(){
			 var maskContent = $('<div class="mask"><iframe frameborder="0" style="width:100%;height:100%"></iframe></div>');
			 $(document.body).append(maskContent);		 
		   return maskContent;  
		} 
		,message : function(args){
			//$.debug(args.msg);
			if(args.width){
				WIDTH = args.width;
			}else{
				WIDTH = 426;	
			}
			this.show();
			if(args.type)
				this[args.type](args);
			else
				this.customCommand(args);
		}		
		//自定义提示
		,customCommand : function(args){			
			if(args.title)
			  this.setTitle(args.title);
				
			this.setMsg(args.msg);
						
			if(args.iconCls)
			  this.setIconCls(args.iconCls);
			  
			this.reSetWidth(WIDTH);
						  
			//var mw = $('div.pop_box');
			var mw = this.getWindow();
			//设置为居中
			//mw.css('left', (document.body.clientWidth-mw[0].clientWidth)/2+document.body.scrollLeft);			
	       // mw.css('top',($(window).height()-mw[0].clientHeight)/2+document.body.scrollTop);
		   var scrollLeft = (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
   		   var scrollTop = (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
		   mw.css('left', ($(window).width()-mw.eq(0).width())/2+scrollLeft);			
	       mw.css('top',($(window).height()-mw.eq(0).height())/2+scrollTop);
		   mw.css('z-index',20003);
		
	    //先清空原先按钮,再生成新按钮			
	    if(args.buttons)
	    	 this.setButton(args.buttons);
	    
	    var t = this;
	    //绑定按钮事件
	    this.windowEl.find("div.btn-order").each(function(i){
	       $(this).bind("click", function(){
	    	     if(args.fn)
	    	        args.fn({index:i},args.msg);
	    	     t.hide();
         });
         /*
         $(this).find("li.middle-normal").mouseover(function(){
	    	    $(this).prev().addClass('left-over');
						$(this).addClass('middle-over');
						$(this).next().addClass('right-over');
					}).mouseout(function(){
						$(this).prev().removeClass('left-over');
						$(this).removeClass('middle-over');
						$(this).next().removeClass('right-over');
					});*/
					$(this).find("li.middle-normal").hover(function(){
	    	    $(this).prev().addClass('left-over');
						$(this).addClass('middle-over');
						$(this).next().addClass('right-over');
					},function(){
						$(this).prev().removeClass('left-over');
						$(this).removeClass('middle-over');
						$(this).next().removeClass('right-over');
					});
      });
      
      //绑定'×'的事件
      this.windowEl.find("span.pop_close").unbind("click");
/*      this.windowEl.find("span.pop_close").hover(function(){	
      	    $(this).removeClass('pop_close');   	    
						$(this).addClass('btn-msg-window-close-over');						
					},function(){
						$(this).removeClass('btn-msg-window-close-over');   	    
						$(this).addClass('pop_close');	
					});*/
      this.windowEl.find("span.pop_close").bind("click", function(){
     	  if(args.fn)
	    	     args.fn({index:-1},args.msg);
          t.hide();
      }); 
		}
		/**
		提示/询问,确定/取消按钮
		*/
		,ask : function(args){
			//设置默认值			
			this.setTitle(this.ASK_TITLE);			
			this.setIconCls('msg-window-ico-prompt');			
	    this.setButton([{text:this.OK_TEXT,iconCls:'ico-btn-order-ok'}, {text:this.CANCEL_TEXT,iconCls:'ico-btn-order-close'}]);
	    
      //调用自定义信息
      this.customCommand(args);
		}
		/**
		警告,确定/取消按钮
		String msg 提供信息
		[String title] 标题
		[Function fn] 确定后回调函数
		*/
		,alert : function(args){
			//设置默认值			
			this.setTitle(this.ALERT_TITLE);
			this.setIconCls('msg-window-ico-warning');			
	    //	this.setButton([{text:this.OK_TEXT,iconCls:'ico-btn-order-ok'}, {text:this.CANCEL_TEXT,iconCls:'ico-btn-order-close'}]);
			this.setButton([{text:this.OK_TEXT,iconCls:'ico-btn-order-ok'}]);
	    
	    //调用自定义信息
      		this.customCommand(args);
		}
		/**
		成功,关闭按钮
		*/
		,success : function(args){
			//设置默认值
			this.setTitle(this.SUCCESS_TITLE);			
			this.setIconCls('msg-window-ico-success');			
	    this.setButton([{text:this.OK_TEXT,iconCls:'ico-btn-order-ok'}]);
	    
	    //调用自定义信息
      this.customCommand(args);
		}
		/**
		失败/错误/异常,关闭按钮
		*/
		,error : function(args){
			//设置默认值
			this.setTitle(this.ERROR_TITLE);			
			this.setIconCls('msg-window-ico-error');			
	    this.setButton([{text:this.OK_TEXT,iconCls:'ico-btn-order-ok'}]);
	    
	    //调用自定义信息
      this.customCommand(args);
		}
		//弹出窗
		,getWindow : function(){
			return this.windowEl || (this.windowEl = this.renderWindow());
		}
		//遮罩
		,getMask : function(){
			return this.maskEl || (this.maskEl = this.renderMask());
		}
		//标题
		,setTitle : function(title){
			this.titleEl.html(title);
		}
		//设置信息
		,setMsg : function(text){
			this.msgEl.html(text);
		}
		//设置图标css
		,setIconCls : function(css){
			this.iconEl.className = css;			
		}
		,reSetWidth : function(w){
			var currentObj = this.getWindow();
			currentObj.find("div.pop_content").css("width",w-2);
			currentObj.css("width",w);
		}
		//设置按钮
		,setButton : function(buttons){
			this.buttonEl.empty();
			
			for(var i=0;i<buttons.length;i++)
			  if(buttons[i].iconCls)
				 this.buttonEl.append(['<div class="btn-order"><input type="button" class="pop_btn ',buttons[i].iconCls,'" id="delete_ajax" value="',buttons[i].text,'"></div>'].join(''));
		    else
		    	this.buttonEl.append(['<div class="btn-order"><ul><li class="left-normal"></li><li class="middle-normal">',buttons[i].text,'</li><li class="right-normal"></li></ul></div>'].join(''));
		}		
		,show : function(){
			this.getWindow().show();
			this.getMask();
			//ie
			var newHeight = document.body.scrollHeight;			
			if(document.body.scrollHeight <= document.body.clientHeight){
			   newHeight = document.body.clientHeight;
			}
			//firefox
			if($.browser.mozilla) 
			   newHeight = $(document).height();
										
			$("div.mask").height(newHeight).show();
		}
		,hide : function(){
			this.getWindow().hide();
			this.getMask().hide();
		}
		,reLayout:function(){
				//var mw = $('div.pop_box');
				var mw = this.getWindow();

			   var scrollLeft = (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
			   var scrollTop = (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
			   mw.css('left', ($(window).width()-mw.eq(0).width())/2+scrollLeft);			
			   mw.css('top',($(window).height()-mw.eq(0).height())/2+scrollTop);
		}
	};
	
	$.messagebox = function(cfg){
		$.uiwidget.Messagebox.instance = $.uiwidget.Messagebox.instance || new $.uiwidget.Messagebox(document.body, {});
		return $.uiwidget.Messagebox.instance.message(cfg);
	};
})(jQuery);	