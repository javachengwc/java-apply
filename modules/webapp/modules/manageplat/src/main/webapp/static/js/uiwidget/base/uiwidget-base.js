/**
* uiwidget基础工具库
* 1.日志记录：$.uiwidget.Log
* 2.JSON处理：$.uiwidget.JSON
* 3.简单计时器：$.uiwidget.StopWatch
*/
;(function($){
	$.uiwidget = {};
	//Log
	$.uiwidget.Log = {
		isInfoEnabled : true
		,isDebugEnabled : true
		/**
		记录info日志
		*/
		,info : function(message) {
			if(!$.uiwidget.Log.isInfoEnabled) return jQuery;
	        var logPanel = this.getLogPanel();
	        var aDate = new Date();
	        var time = [aDate.getFullYear(),'-',(aDate.getMonth()+1),'-',aDate.getDate(),' ',aDate.getHours(),':'+aDate.getMinutes(),':',aDate.getSeconds(),',',aDate.getMilliseconds()];
	        logPanel.prepend(['[INFO]',time.join(''),' ',message,'<br>'].join(''));
	        return jQuery;
		}
		/**
		记录debug日志
		*/
		,debug : function(message) {
			if(!$.uiwidget.Log.isDebugEnabled) return jQuery;
	        var logPanel = this.getLogPanel();
	        var aDate = new Date();
	        var time = [aDate.getFullYear(),'-',(aDate.getMonth()+1),'-',aDate.getDate(),' ',aDate.getHours(),':'+aDate.getMinutes(),':',aDate.getSeconds(),',',aDate.getMilliseconds()];
	        logPanel.prepend(['[DEBUG]',time.join(''),' ',message,'<br>'].join(''));
	        return jQuery;
		}
		/**
		清除日志
		*/
	    ,clear : function(name) {   
	        var logPanel = getLogPanel();
	        logPanel.empty();
	        return jQuery;
		}
		
		//private
		,getLogPanel : function(){
			var logPanel = $('.uiwidget-log');
			if(logPanel.length==0){
				logPanel = $('<div class="uiwidget-log"/>');
				$(document.body).append(logPanel);
			}
			return logPanel;
		}
		/**
		* 格式化字符串,如"显示 {0} - {1}条，共 {2} 条"
		* @param {String} 格式的字符串
     	* @param {String} {0}的值
     	* @param {String} {1}的值...如此累推
     	* @return {String} 格式化后的字符串
     	*/
		,format : function(format){
        	var args = Array.prototype.slice.call(arguments, 1);
        	return format.replace(/\{(\d+)\}/g, function(m, i){
        	    return args[i];
        	});
		}
	};
	$.extend($.uiwidget.Log);
	
	$.uiwidget.JSON = {
			useHasOwn : !!{}.hasOwnProperty
			

		    ,pad : function(n) {
		        return n < 10 ? "0" + n : n;
		    }

		    ,m : {
		        "\b": '\\b',
		        "\t": '\\t',
		        "\n": '\\n',
		        "\f": '\\f',
		        "\r": '\\r',
		        '"' : '\\"',
		        "\\": '\\\\'
		    }
		   
		    ,encodeString : function(s){
		        if (/["\\\x00-\x1f]/.test(s)) {
		            return '"' + s.replace(/([\x00-\x1f\\"])/g, function(a, b) {
		                var c = m[b];
		                if(c){
		                    return c;
		                }
		                c = b.charCodeAt();
		                return "\\u00" +
		                    Math.floor(c / 16).toString(16) +
		                    (c % 16).toString(16);
		            }) + '"';
		        }
		        return '"' + s + '"';
		    }

		    ,encodeArray : function(o){
		        var a = ["["], b, i, l = o.length, v;
		            for (i = 0; i < l; i += 1) {
		                v = o[i];
		                switch (typeof v) {
		                    case "undefined":
		                    case "function":
		                    case "unknown":
		                        break;
		                    default:
		                        if (b) {
		                            a.push(',');
		                        }
		                        a.push(v === null ? "null" : this.encode(v));
		                        b = true;
		                }
		            }
		            a.push("]");
		            return a.join("");
		    }

		    ,encodeDate : function(o){
		        return '"' + o.getFullYear() + "-" +
		                pad(o.getMonth() + 1) + "-" +
		                pad(o.getDate()) + " " +
		                pad(o.getHours()) + ":" +
		                pad(o.getMinutes()) + ":" +
		                pad(o.getSeconds()) + '"';
		    }

		    /**
		     * Encodes an Object, Array or other value
		     * @param {Mixed} o The variable to encode
		     * @return {String} The JSON string
		     */
		    ,encode : function(o){
		        if(typeof o == "undefined" || o === null){
		            return "null";
		        }else if($.isArray(o)){
		            return this.encodeArray(o);
		        }else if(Object.prototype.toString.apply(o) === '[object Date]'){
		            return this.encodeDate(o);
		        }else if(typeof o == "string"){
		            return this.encodeString(o);
		        }else if(typeof o == "number"){
		            return String(o);
		        }else if(typeof o == "boolean"){
		            return String(o);
		        }else {
		            var a = ["{"], b, i, v;
		            for (i in o) {
		                if(!this.useHasOwn || o.hasOwnProperty(i)) {
		                    v = o[i];
		                    switch (typeof v) {
		                    case "undefined":
		                    case "function":
		                    case "unknown":
		                        break;
		                    default:
		                        if(b){
		                            a.push(',');
		                        }
		                        a.push(this.encode(i), ":",
		                                v === null ? "null" : this.encode(v));
		                        b = true;
		                    }
		                }
		            }
		            a.push("}");
		            return a.join("");
		        }
		    }

		    /**
		     * Decodes (parses) a JSON string to an object. If the JSON is invalid, this function throws a SyntaxError unless the safe option is set.
		     * @param {String} json The JSON string
		     * @return {Object} The resulting object
		     */
		    ,decode :  function(json){
		        return eval("(" + json + ')');    
		    }
		};
	
	$.uiwidget.StopWatch = function(){
		
	};
		
	$.uiwidget.StopWatch.prototype = {
		version : 1	
		/**
		 * 计时器开始
		 * @return
		 */
		,start : function(){
			this.startTime = new Date().getTime();
		}
	
		/**
		 * 计时器停止
		 */
		,stop : function(){
			this.endTime = new Date().getTime();
		}
		
		/**
		 * 显示计时器时间
		 */
		,toString : function(){
			return "time used: " + (this.endTime - this.startTime) + " ms";
		}
	};
			
})(jQuery);
