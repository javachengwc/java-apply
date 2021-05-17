/**
* ajax 加载页面
*/
;(function($){
	$.uiwidget = $.uiwidget || {};
	$.uiwidget.Page = function(el, cfg){
		if(typeof cfg == 'string'){
			cfg = {url: cfg}; 
		}
		$.extend(this, cfg);
		this.el = el;
		if(!window.urlPaths){
			window.urlPaths = [];
		}
		window.urlPaths.push(this.url);
		this.load();
	};
	var rscript = /<script(.|\s)*?\/script>/gi;
	$.uiwidget.Page.prototype = {
		cache : false
		,load : function(){
			var t = this;
			
			if (!this.url || !this.el.length) {
				return this;
		    }
			var selector;
			var off = this.url.indexOf(" ");
			if ( off >= 0 ) {
				selector=  this.url.slice(off, this.url.length);
				this.url = this.url.slice(0, off);
			}
			 
			$.ajax({
				url: this.url,
				type: "GET",
				dataType: "html",
				data: this.params,
				cache : this.cache,
				success: function(data, status ) {
					// If successful, inject the HTML into all the matched elements
					t.el.html(selector ?$("<div />").append(data.replace(rscript, "")).find(selector) :data);
					if(t.afterLoad)
						t.afterLoad();
				}
			});
			return this;
		}
	};	
	$.fn.page = function(cfg){
		return new $.uiwidget.Page(this, cfg);
	};
})(jQuery);	
