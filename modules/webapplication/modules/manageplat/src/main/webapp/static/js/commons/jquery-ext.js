/**
 * JQuery扩展，主要是处理ajax异常和错误
 */
;(function($){
	$.ajaxSetup({cache : false, traditional : true});
	var ajax = $.ajax;
	$.ajax = function(s){
		//是否等待此次请求完成再执行下次请求，防止重复提交
		if(s.eventTarget){
			if(typeof s.eventTarget == "string"){
				s.eventTarget = $('#'+s.eventTarget);
			}else if(!s.eventTarget.jquery){
				s.eventTarget = $(s.eventTarget);
			}

			if(s.eventTarget.data("_waitting")){
				return;
			}
			s.eventTarget.data("_waitting",true);
		}
		var tempXhr;
		
	    //扩展ajax提交之前的操作
		var oldBeforeSend = s.beforeSend;
		s.beforeSend = function(xhr, s){
			//设置是否显示遮罩效果
			if(s.showMask && top.commonShowMask){
				top.commonShowMask();
			}
		    var isSend = true;
		    tempXhr = xhr;
			try {
				xhr.setRequestHeader("Request-Type", "ajax");
			}catch(e){}
			if(oldBeforeSend){
				isSend = oldBeforeSend(xhr,s);
			}
			return isSend;
		}
		
		//扩展成功提示
		var oldSuccess = s.success;
		s.success = function(data,status,xhr){
			var exp = tempXhr.getResponseHeader("Json-Exception");
			if(exp=="jsExc"){
				var contentType = tempXhr.getResponseHeader("Content-Type");
				var dataOld = data;
			    if(s.dataType!="json")
					data = eval("("+data+")");
				if(!data){
					return;
				}
				//开发人员自定义异常处理
				if(s.exception){
					s.exception(dataOld);
				}
				if(s.errorPage){
					window.location.href = s.errorPage;
					return;
				}
				if(s.autoAlert == true){
					s.alert(data);
				}
			}else{
				if(s.autoAlert == true && s.dataType == "json" && data && data.result != 0 && data.msg){
					alert(data.msg);
					return;
				}
				//开发人员自定义成功处理
				if(oldSuccess){
					oldSuccess(data,status,xhr);
				}
			}
		};
		
		var oldComplete = s.complete;
		s.complete = function(xhr, textStatus){
			if (s.eventTarget) {
				s.eventTarget.data("_waitting",null);
			}
			if(oldComplete){
				oldComplete(xhr, textStatus);
			}
		};
		var oldError = s.error;
		s.error = function(XMLHttpRequest, textStatus, errorThrown){
			if(textStatus.status >= 500){
				if(s.errorPage){
					window.location.href = s.errorPage;
					return;
				}
			}
			if(oldError){
				oldError(XMLHttpRequest, textStatus, errorThrown);
			}
		};
		s.alert = function(data){
			// 统一处理异常
			if(data.type=="ARGUMENT"){
				if($.messagebox){
					$.messagebox({type:"error", msg:"参数错误:" + data.msg});
				}else{
					alert("参数错误:" + data.msg);
				}
			}else if(data.type=="NETWORK"){
				if($.messagebox){
					$.messagebox({type:"error", msg:"网络错误:" + data.msg});
				}else{
					alert("网络错误:" + data.msg);
				}
			}else if(data.type=="BUSINESS"){
				if($.messagebox){
					$.messagebox({type:"error", msg:"" + data.msg});
				}else{
					alert("" + data.msg);
				}
			}else{
				if($.messagebox){
					$.messagebox({type:"error", msg:"未知类型异常:" + data.msg});
				}else{
					alert("未知类型异常:" + data.msg);
				}
			}
		};
		ajax(s);
	}
})(jQuery);


