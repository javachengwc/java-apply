/**
 * PageBar分页工具条
 */
;(function($){
	$.uiwidget = $.uiwidget || {};
	$.uiwidget.PageBar = function(target, cfg){
		$.extend(this, cfg);//本对象加载cfg属性
		this.target = target;//绑定的dom el
		this.init();//初始化
	};
	/**
	 * 类原型的相关属性和方法定义
	 */
	$.uiwidget.PageBar.prototype = {
		preText : "上一页",
		nextText : "下一页",
		displayText: "共 {0} 页，共 {1} 条",
		preEl : null,
		nextEl : null,
		pageNoEl : null,
		limitPageCount : 10,//分页自动省略，限制显示的页码数
		hideLastPage : false,//是否隐藏最后一页
		/**
		 * 初始化提交ajax请求获得总记录数
		 */
		init : function(cfg){
			var t = this;
			if(t.limitPageCount < 5){//暂时只支持限制页码最小为5
				t.limitPageCount = 5;
			}
			if(cfg){
				$.extend(this, cfg);
			}

            //初始渲染数据
			if (!t.url && t.totalCount!=null) {//url为空的情况下直接返回，不执行ajax
                //一般都是执行这一步来初始首次载入的第一页数据
				t.render({totalCount:t.totalCount});
				t.changePage(t);
				return;
			}
			t.params.pageNo = t.pageNo;
			t.params.pageSize = t.pageSize;
            //url不为空的情况下
			$.ajax({
			    url: t.url,
			    type: t.type || 'post',
			    dataType: 'json',
			    data: t.params,
			    success: function(json){
			        t.render(json);
					t.changePage(t);
			    }
			});
		},
		/**
		 * 渲染pagebar的html
		 * @param {Object} json
		 */
		render : function(json){
			var t = this;
			var totalCount = t.totalCount = json.totalCount;
			if(json.totalCount <= 0){
				t.target.empty();
				return;
			}
			var pageSize = t.pageSize;
			var pageNo = t.pageNo;
			var pageCount = parseInt(totalCount / pageSize);
			if (totalCount % pageSize != 0) {
				pageCount++;
			}
			t.pageCount = pageCount;
			
			//pagebar的html代码
			
			var html = '<ul class="page-bar">'
				+'<li><a class="page-bar-pre" href="javascript:void(0);">'+t.preText+'</a></li>';
			for(var i=1;i<=pageCount;i++){
				html += '<li><a id="pageBar_'+i+'" class="page-bar-no"  href="javascript:void(0);">'+i+'</a></li>';
			}
			html += '  <li><a class="page-bar-next" href="javascript:void(0);">'+t.nextText+'</a></li></ul>';
			//	+'  <span>'+t.getDisplayText(pageCount, totalCount)+'</span></div> ';
			//alert(html);
			$(t.target).html(html);
						
			t.target.show();
			//清空el，并重新绑定click事件
			t.preEl = null;
			t.nextEl = null;
			t.pageNoEl = null;
			t.getPreEl().click(function(){
				if(t.pageNo > 1){
					t.pageNo --;
					t.getCurrentEl().attr('className', 'page-bar-no');
					$('#pageBar_'+t.pageNo).attr('className', 'page-bar-current');
					t.handleLimitPageCount();
					t.changePage(t);
				}
				return false;
			});
			t.getNextEl().click(function(){
				if(t.pageNo < pageCount){
					t.pageNo ++;
					t.getCurrentEl().attr('className', 'page-bar-no');
					$('#pageBar_'+t.pageNo).attr('className', 'page-bar-current');
					t.handleLimitPageCount();
					t.changePage(t);
				}
				return false;
			});
			t.getPageNoEl().click(function(){
				t.pageNo = parseInt($(this).text());
				t.getCurrentEl().attr('className', 'page-bar-no');
				$('#pageBar_'+t.pageNo).attr('className', 'page-bar-current');
				t.handleLimitPageCount();
				t.changePage(t);
				return false;
			});
			$('#pageBar_'+t.pageNo).attr('className', 'page-bar-current');
			t.handleLimitPageCount();
		}
		/**
		 * 控制显示的页数，超出就省略
		 */
		,handleLimitPageCount : function(){
			var t = this;
			var pageCount = t.pageCount;
			if(t.limitPageCount){
				var omit = '<li class="page-bar-omit" style="font-weight: bold;">...</li>';
				t.getPageNoEl().parent().show();
				t.target.find('li.page-bar-omit').remove();
				if(pageCount > t.limitPageCount){
					if(t.pageNo < t.limitPageCount-1){
						for(var i=t.limitPageCount-1;i<pageCount-1;i++){
					//		alert('pageNo:'+$(t.getPageNoEl()[i]).text());
							$(t.getPageNoEl()[i]).parent().hide();
							
						}
						$(t.getPageNoEl()[t.limitPageCount-1]).parent().after(omit);	
						if(t.hideLastPage){
							t.getPageNoEl().last().parent().hide();
							$(t.getPageNoEl()[t.limitPageCount-1]).parent().show();
						}
					}else{
						//i向右，j向左，c次数，flag保证左右轮流
						var i=1,j=pageCount-2,c=0,flag = true;
						//flagLeft,flagRight标识是否隐藏，如果为true就出现省略
						var flagLeft = flagRight = false;
						while(c < (pageCount - t.limitPageCount)){
							if(flag){
								if(t.pageNo < (j+1)-1){
									$(t.getPageNoEl()[j]).parent().hide();
									flagRight = true;
									j--;
									c++;
								}
								flag = false;
							}else{
								if (t.pageNo > (i+1)+1) {
									$(t.getPageNoEl()[i]).parent().hide();
									flagLeft = true;
									i++;
									c++;
								}
								flag = true;
							}
							if (!(t.pageNo < (j+1)-1) && !(t.pageNo > (i+1)+1)){
								break;
							}
						}
						if(flagRight){
							t.getPageNoEl().last().parent().before(omit);
						}
						if(flagLeft){
							t.getPageNoEl().first().parent().after(omit);
						}
						if(t.hideLastPage){
							if(t.pageNo < pageCount-2){
								t.getPageNoEl().last().parent().hide();
								$(t.getPageNoEl()[j+1]).parent().show();
							}
						}
					}
				}
			}
		}
		,format : function(format){
        	var args = Array.prototype.slice.call(arguments, 1);
        	if(!format) throw "format is null";
        	return format.replace(/\{(\d+)\}/g, function(m, i){
        	    return args[i];
        	});
		}
		,getDisplayText : function(pageCount, totalCount){
			return this.format(this.displayText, 
				'<span class="bold">' + pageCount + '</span>',
				'<span class="bold">'+ totalCount +'</span>');
        },
		getLastPage : function(){
			var t = this;
			var lastPage = parseInt(t.totalCount / t.pageSize);
			if(t.totalCount % t.pageSize != 0){
				lastPage++;
			}
			return lastPage;
        },
		goTo : function(pageNo){
			var t = this;
			if(pageNo && $('#pageBar_'+pageNo).length > 0){
				t.pageNo = pageNo;
				$('#pageBar_'+t.pageNo).click();
			}
        }
		,getPreEl : function(){
			return this.preEl || (this.preEl = this.target.find('.page-bar-pre'));
		}
		,getNextEl : function(){
			return this.nextEl || (this.nextEl = this.target.find('.page-bar-next'));
		}
		,getPageNoEl : function(){
			return this.pageNoEl || (this.pageNoEl = this.target.find('.page-bar-no'));
		},
		getCurrentEl : function(){
			return this.target.find('.page-bar-current');
		}
	};
	
	$.fn.pageBar = function(cfg){
		return new $.uiwidget.PageBar(this, cfg);
	};
})(jQuery);