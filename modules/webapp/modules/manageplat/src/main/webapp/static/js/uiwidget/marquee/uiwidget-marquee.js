(function($) {
	$.fn.extend({
		marquee_setTimeout:function (callback,timeout,param){
		    var args = Array.prototype.slice.call(arguments,2);
		    var _cb = function()
		    {
		        callback.apply(null,args);
		    }  
		   $(this).marquee.stime = setTimeout(_cb,timeout);
		},
		marquee_Resest:function(obj,msg){
			var str = String($(obj).css('left'));
			var ml = Number(str.split('px')[0]);

			$(obj).css('left',(ml-2)+'px');
			var tx = ml*-1 - $(obj).width() + $(obj).parent().width();
			if(0 <= tx){
				var wl = ($(obj).width() - 75)/2;
				$(obj).css('left',$(obj).parent().width() - wl - tx + 'px');
			}
			$(this).marquee_setTimeout($(this).marquee_Resest,60,obj,msg);
		},
		marquee: function(options) {
			if($(this).parent().attr('css') != "marquee_css"){
				$(this).wrap('<p class="marquee_css" style="text-overflow: ellipsis;-o-text-overflow: ellipsis;white-space: nowrap;word-break: break-all;position: relative;text-indent: 0;overflow:hidden;width:'+options.width+'px;height:'+options.height+'px;"></p>');
			}else{
				return;
			}
			
			clearTimeout($(this).marquee.stime);
			$(this).marquee.defaults = $.extend($.fn.marquee.defaults, options); //这里用了$.extend方法，扩展一个对象 


			$(this).css('left','0px');
			$(this).css('position','absolute');
			$(this).css('top','0px');

			var msg= $(this).html();	
			if(!$(this).attr('msgs')){
				$(this).attr('msgs',msg);
			}

			$(this).mouseleave(function(){
				$(this).marqueeStop();
			});
			$(this).mouseenter(function(){
				$(this).marqueeStart();
			});
		},
		marqueeStart: function(options) {
			if(options){
				$(this).marquee(options);
			}

			if($(this).width() < ($(this).marquee.defaults.width+4)){
				return;
			}
			$(this).html($(this).attr('msgs') + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + $(this).attr('msgs'));
			$(this).marquee_Resest(this,$(this).attr('msgs'));
		},
		
		marqueeStop: function() {
			clearTimeout($(this).marquee.stime);
			$(this).html($(this).attr('msgs'));
			$(this).css('left','0px');
		}
	});
    $.fn.marquee.stime = 0;
	$.fn.marquee.defaults = {
		width:100,
		height:20
    }
})(jQuery);