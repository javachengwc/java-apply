//验证上传文件类型
function validUploadFlieType(value,type){
	var rxAccept = new RegExp('\\.('+(type?type:'')+')$','gi');
   	return value.match(rxAccept);
}


//定义图片位置
function setImagePosition(t){
	t = $(t);
	if(t.width()/1.5 > t.parent().width()){
		t.css({left:-t.parent().width()/2});
	}
	if(t.height()/1.5 > t.parent().height() && t.width()/1.5 > t.parent().width()){
		t.css({top:-t.parent().height()/2});
	}
	
}

//四舍五入,当小数为0时不显示
 function roundNum(f, c){
   	var t = Math.pow(10, c);
   	var tmp = (f * t)+0.000000001;
   	var result = Math.round(tmp) / t;
   	return result;
   }
function getRoundNum(f,c,d){
	if(c!=0 && !c){
		c = 2;
	}
	if(!d)d=c;
	var result = roundNum(f,c);
    return appendDigit(result,d);
}
//追加小数位
function appendDigit(result,c){
	result = result.toString();
	if(result.indexOf('.')==-1){
		if(c>0){
			result = result +".";
			for(var i=0;i<c;i++){
				result = result + "0";
			}
		}
	}else {
		var rsLen = result.split('.')[1].length;
		if(rsLen<c){
			for(var i=0;i<c - rsLen;i++){
				result = result + "0";
			}
		}
	}

	return result;
}

/**
 *Desc:计算长度，每个汉字占两个长度，英文字符每个占一个长度
 *return:字符串长度(按字节计算)
 */
function strLength(str)
{
	var len = 0;
  	for(var i=0;i<str.length;i++){
	    if(str.charCodeAt(i)>255)
	        len+=2;
	    else
	        len++;
	  }
	return len;
}

function textOmitor(text, width,suffix,enWidth,chWidth){
	var e = 7,c = 12;
	var intCount = 0;
	var i = 0
	var flag =false;
	if(suffix)
		intCount +=10;
	if(enWidth) 
		e = enWidth;	
	if(chWidth)
		c = chWidth;
	for (; Math.min(i, text.length) != text.length; i++) {
		if (text.charCodeAt(i) > 255) {
			intCount += c;
		} else {
			intCount += e;
		}
		if (intCount > width) {
			flag = true;
			break;
		}
	}
	var result = text.substring(0, i);
	if(flag && suffix)
		result +="....";
	return result;
}


/** 
* 数组去重复 
*/  
function undulpicate(array){  
    for(var i=0;i<array.length;i++) {  
        for(var j=i+1;j<array.length;j++) {  
            //注意 ===  
            if(array[i]===array[j]) {  
                array.splice(j,1);  
                j--;  
            }  
        }  
    }  
    return array;  
}     


//转义<>字符
function arrowFilter(s){
    if(!s)return "";
    var html = "";
    var buffer = "";
    for (var i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        switch (c) {
        case '<':
            buffer += "&lt;";
            break;
        case '>':    
			buffer += "&gt;";
            break;
        case '&':
			buffer += "&amp;";
            break;
        case '"':
			buffer += "&quot;";
            break;
        case "'":
			buffer += "&#39;";
            break;
        default:
            buffer +=c;
        }
    }
    html = buffer.toString();
    return html;
	//return s.replaceAll('<', '&lt;').replaceAll('>', '&gt;').replaceAll('&', '&amp;').replaceAll('"', '&quot;').replaceAll("'", "\'");
}

function suitableImage(iW, iH, cW, cH, flag){
			if(iW <= cW && iH <= cH){
				return {w:iW,h:iH};
			}
            var w = iW / cW;
            var h = iH / cH;
            if (w > h) {
                var zoom = cW / iW;
                if (flag) {
                    w = iH * zoom;
                    h = cW
                }
                else {
                    w = cW;
                    h = iH * zoom
                }
                
            }
            else {
                var zoom = cH / iH;
                if (flag) {
                    w = cH;
                    h = iW * zoom
                }
                else {
                    w = iW * zoom;
                    h = cH
                }
            }
			return {w:w,h:h};
}

//格式化数字
function formatNumberByComma(num)
{
	if(isNaN(num))return num;
	var numArry = String(num).split(".");
    var ss=numArry[0];
    
    var strFormat="";
    while(ss.length>3)
    {
        strFormat=","+ss.substring(ss.length-3,ss.length)+strFormat;
        ss=ss.substring(0,ss.length-3);
    }
    if(ss.length>0)
    {
        strFormat=ss+strFormat;
        if(numArry[1]){
        	strFormat+="."+numArry[1];
        }
    }
    return strFormat;
}

function addFavorite(url, title) {

    url = url || window.location;

    title = title || document.title;
    try{
	    if (document.all)
		{
	    	window.external.addFavorite(url, title);
		}
		else if (window.sidebar)
		{
			window.sidebar.addPanel(title, url, "");
		}else{
			alert("您的浏览器不支持自动收藏，请按Ctrl + D 手动收藏");
		}
    }catch(ex){
    	alert("您的浏览器不支持自动收藏，请按Ctrl + D 手动收藏");
    }
}

function include(file)
{
    var files = typeof file == "string" ? [file] : file;
    for (var i = 0; i < files.length; i++)
    {
        //var name = files[i].replace(/^\s|\s$/g, "");
        //var att = name.split('.');
        //var ext = att[att.length - 1].toLowerCase();
        var isCSS = /\.css/.test(files[i]);
        if(isCSS){
		    var styleTag = document.createElement("link");
		    styleTag.setAttribute('type', 'text/css');
		    styleTag.setAttribute('rel', 'stylesheet');
		    styleTag.setAttribute('href', files[i]);
		    $("head")[0].appendChild(styleTag);
        }else{
        	var script = document.createElement('script');
        	script.type = "text/javascript";
        	script.language="javascript";
        	script.src = files[i];
        	$("head")[0].appendChild(script);
        }
    }
}