/**
 * previewer图片预览组件
 */
;(function($){
	$.uiwidget = $.uiwidget || {};
	$.uiwidget.PreviewerFix = function(target, cfg){
		$.extend(this, cfg);
		this.target = target;
		this.init();
//		this.render();
	};
	
	$.uiwidget.PreviewerFix.prototype = {
		drag : false,//拖动标志
		tX : 0, //移动中的clone图片位置
		tY : 0,
		newzoom : 10,//图片缩放的比例
		iW : null,//图片原始宽
		iH : null,//图片原始高
		cW : null,//容器宽
		cH : null,//容器高
		//拖动范围控制
		
		//图片往下或右方向移动时相对于容器的top和left的最大范围
		bTop : 100,
		bLeft : 100,
		//图片往上或左方向移动时相对于容器的top和left的最大范围
		sTop : 0,
		sLeft : 0,
		
		curW : 0,//图片当前宽
		curH : 0,// 图片当前高
		dragX : 0,//拖动点相到图片左边的距离
		dragY : 0,//拖动点相到图片上边的距离

		oB : null,//保存按过的按钮对象
		suitSize : '适应大小',
		fullSize : '实际大小',
		init : function(){
//			$.debug(this.target[0].tagName);
			this.target.css({
				position : 'relative',
				overflow : 'hidden'
			});
			
			
			//初始化大小调整按钮
			$('div.previewer-zoom').remove();
			this.target.append('<div id="5" class="previewer-zoom" style="left:12px;">50%</div>');
			$('#5').bind('click', this, this.resizeImage);
			this.target.append('<div id="10" class="previewer-zoom" style="left:50px;">'+this.fullSize+'</div>');
			$('#10').bind('click', this, this.resizeImage);
			this.target.append('<div id="15" class="previewer-zoom" style="left:112px;">150%</div>');
			$('#15').bind('click', this, this.resizeImage);
			this.target.append('<div id="0" class="previewer-zoom" style="left:158px;">'+this.suitSize+'</div>');
			$('#0').bind('click', this, this.suitableImage);
			oB = $('#0').css('backgroundColor', '#feec8c');


			$('#img').remove();
			this.target.append('<img id="img" class="previewer-image" style="position:absolute;display:none" />');	
			
			this.img = $('#img');
			this.img.bind('load', this, this.render);//图片加载完执行
			this.img.attr('src',this.src);
			
			this.img.bind('mousedown', this, this.onMouseDown);
			$(document).bind('mousemove', this, this.onMouseMove);
			$(document).bind('mouseleave', this, this.onMouseUp);
			$(document).bind('mouseup', this, this.onMouseUp);
			this.target.bind('mousewheel', this, this.onMouseScroll);
			this.target.bind('DOMMouseScroll', this, this.onMouseScroll);
		},
		render : function(e){	
			var d = e.data;
			d.iW = d.img.width();
			d.iH = d.img.height();
			if(d.cW == null){
				d.cW = d.target.width();
			}else{
				d.target.width(d.cW);
			}
			if(d.cH == null){
				d.cH = d.target.height();
			}else{
				d.target.height(d.cH);
			}
	        
			d.img.unbind('load');
			//为了防止图片第一次加载完成后马上执行图片缩放事件无效而做得处理
			setTimeout("$('#0').click();", 1);

		},
		onMouseDown : function(e){
			var d = e.data;
	    	if($.browser.msie){ 
				//将鼠标事件捕获到当前文档的指定的对象。这个对象会为当前应用程序或整个系统接收所有鼠标事件。
				//setCapture捕获以下鼠标事件：onmousedown、onmouseup、onmousemove、onclick、ondblclick、 
				//onmouseover和onmouseout。
	            this.setCapture();
			}
	        $(this).css({
	            cursor: 'pointer'
	        });
	        $(document.body).css({
	            cursor: 'pointer'
	        });
	        $('#imgClone').remove();
	        $(this).clone(true).insertAfter(this).attr('id', 'imgClone').show();
	        $(this).hide();
	        d.dragX = ($(window).scrollLeft() + e.clientX) - (parseInt($(this).css("left")));
	        d.dragY = ($(window).scrollTop() + e.clientY) - (parseInt($(this).css("top")));
	        d.drag = true;

			//$(document).bind('mousemove', d, d.onMouseMove);
			//$(document).bind('mouseleave', d, d.onMouseUp);
			//$(document).bind('mouseup', d, d.onMouseUp);
	    },
		onMouseMove : function(e){
	    	var d = e.data;
	        if (d.drag) {
	        	d.tX = e.pageX - d.dragX;//拖动时得到相对容器的left
	        	d.tY = e.pageY - d.dragY;//拖动时得到相对容器的top
	            if(d.tY < d.sTop){//向上拖动时,top小于top的最大范围(最小值),取它的最小值
	            	d.tY = d.sTop;
	    		}
	            if(d.tX < d.sLeft){//向左拖动时,left小于left的最大范围(最小值),取它的最小值
	            	d.tX = d.sLeft;
	    		}
	            if(d.tY > d.bTop){//向下拖动时,top小于top的最大范围(最大值),取它的最大值
	            	d.tY = d.bTop;
	    		}

	            if(d.tX > d.bLeft){//向右拖动时,left小于left的最大范围(最大值),取它的最大值
					d.tX = d.bLeft;
	    		}
	            $('#imgClone').css({
	                left: d.tX,
	                top: d.tY
	            });
	        }
	    },
	    onMouseUp : function(e){
	    	var d = e.data;
	    	var img = d.img;
	    	if ($.browser.msie){ 
				//释放鼠标监控.
	    		img[0].releaseCapture();
			}
	    	img.css({
	            cursor: 'default'
	        });
	        $(document.body).css({
	            cursor: 'default'
	        });
	    	img.css({
	            left: d.tX,
	            top: d.tY
	        });
	        $('#imgClone').remove();
	        img.show();
	        d.drag = false;

			//$(document).unbind('mousemove');
			//$(document).unbind('mouseleave');
			//$(document).unbind('mouseup');
	    },
	    onMouseScroll : function(e){			
	    	var d = e.data;
			if (e.detail >= 3) {
				d.newzoom++;
			} else if (e.detail <= -3) {
				d.newzoom--;
			}
			if (e.wheelDelta >= 120) {
				d.newzoom--;
			} else if (e.wheelDelta <= -120) {
				d.newzoom++;
			}
	    	if (d.newzoom<2)  d.newzoom=2;   
	    	if (d.newzoom>50)  d.newzoom=50;
	    	$(d.img).css({
	    		width : d.iW * d.newzoom / 10,
	        	height : d.iH * d.newzoom / 10
	        });
	    	d.calculateCurSize();
			return false;
	    },
	    alignCenter : function(){
	    	this.tX = (this.cW - this.img.width()) / 2;
	    	this.tY = 0;
	    	this.img.css({
	            top: this.tY,
	            left: this.tX
	        });
			
	    },
	    /**
	     * 计算容器大小 
	     */
	    calculateCurSize : function(){
	    	this.curW = this.img.width();
	    	this.curH = this.img.height();
	    	this.sTop = this.cH - this.curH;
	    	this.sLeft = this.cW - this.curW;
	    	this.sTop = this.sTop>0 ? 0 : this.sTop;
	    	this.sLeft = this.sLeft>0 ? 0 : this.sLeft;
	    	this.bTop = this.cH - this.curH;
	    	this.bLeft = this.cW - this.curW;
	    	this.bTop = this.bTop<0? 0 : this.bTop;
	    	this.bLeft = this.bLeft<0? 0 : this.bLeft;
			
	    },
	    resizeImage : function(e){
	    	var d = e.data;
	    	d.newzoom = this.id;
	    	d.img.css({
	    		width : d.iW * d.newzoom / 10,
	        	height : d.iH * d.newzoom / 10
	        });
	    	d.calculateCurSize();
	    	d.alignCenter();
	    	oB.css('backgroundColor', '');
	  		$(this).css('backgroundColor', '#feec8c');
	  		oB = $(this);
	    },
	    suitableImage : function(e){
	    	var d = e.data;	
			//var w = d.iW - d.cW;
	        //var h = d.iH - d.cH;
			var w = d.iW/d.cW;
	        var h = d.iH/d.cH;
	  		if(w > h){//当宽度
	  			var zoom = d.cW / d.iW;
	  			d.img.css({
	  				width : d.cW,
	  				height : d.iH * zoom
		        });
	  	  	}else{
	  	  		var zoom = d.cH / d.iH;
	  	  		d.img.css({
	  	  			width : d.iW * zoom,
					height : d.cH
		        });
	  	  	}
			d.newzoom = (d.img.width() / d.iW) * 10;
	  		d.calculateCurSize();
	  		d.alignCenter();
	  		oB.css('backgroundColor', '');
	  		$(this).css('backgroundColor', '#feec8c');
	  		oB = $(this);
			d.img.show();
	    },
	    changeImage : function(src){//多张图片切换
	   		this.target.previewer({src : src});
	    }
	};
	
	$.fn.previewerFix = function(cfg){
		return new $.uiwidget.PreviewerFix(this, cfg);
	};
})(jQuery);