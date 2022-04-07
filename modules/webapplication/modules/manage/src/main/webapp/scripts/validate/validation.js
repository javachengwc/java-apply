/****************************************************
*                         验证控件    翁灿金  2011/11/03    add
****************************************************/
var Validation = {
	jsyz : true,     //验证类型  及时验证 和提交验证
	selectXxkEle : null,
	divElemensArr : [],                      //div对象集合
	elementsArr : [], 
	errorMessage : '',    //提示信息
	errorElement : [],    //错误对象数组  
	rediosElement : [],
	checkboxsElement : [],
	
	showMustFlag :true, //是否显示必填项的  *  符号
	                                                
	validateElement : function(element){//单个元素的验证
		Validation.errorMessage = '';
		var val = element.value;
		var className = Validation.evalString(element.className);
		if(className.cls=='true'){
			val = element.realvalue;
		}
		var type = element.type;
		if(type =='text'||type=='textarea'){
			if(className.must=='true'){//必须输入
				if(val==''){
					if(className.cls=='true'){
						Validation.errorMessage = '* 请选择'+element.title;
						return false;
					}else{
						Validation.errorMessage = '* '+element.title+'必填';
						return false;
					}
				}
					return Validation.checkItem(element);
			}else{
				if(val!=''){
					return Validation.checkItem(element);
				}
			}
		}
		if(type == 'select-one'){
			if(className.must == 'true'){
				if(!Validation.validateSelectIsSelect(element)){
					Validation.errorMessage = '* 请选择'+element.title;
					return false;
				}
			}
		}
		if(type == 'select-multiple'){
			if(className.must == 'true'){
				var selectArr = [];
				for(var i=0;i<element.options.length;i++){
					if(element.options[i].selected){
						selectArr.push(i);
					}
				}
				if(selectArr.length==0){
					Validation.errorMessage = '* 请选择'+element.title;
					return false;
				}
				if(typeof(className.minitem!='undefined')){
					if(selectArr.length<className.minitem){
						Validation.errorMessage = '* 请选择 '+className.minitem +' 项';
						return false;
					}
				}
			}
		}
		if(type=='radio'){
			if(className.must =='true'){
				if(!Validation.validateCheckRadioIsSelect(element)){
					Validation.errorMessage = '* 请选择'+element.title;
					return false;
				}
			}
		}
		if(type == 'checkbox'){
			if(element.name!=null&&element.name!=''){
				var checkboxs = document.getElementsByName(element.name);
				if(className.must == 'true'){
					var checkArr = [];
					for(var i=0;i<checkboxs.length;i++){
						if(checkboxs[i].checked){
							checkArr.push(checkboxs[i]);
						}
					}
					var count = checkArr.length;
					if(count==0){
						Validation.errorMessage = '* 请选择'+element.title;
						return false;
					}
					if(className.minlen!=null){
						if(count<className.minlen){
							Validation.errorMessage = '* '+element.title+'必须选择'+className.minlen+'项';
							return false;
						}
					}
				}else{
					var checkArr = [];
					for(var i=0;i<checkboxs.length;i++){
						if(checkboxs[i].checked){
							checkArr.push(checkboxs[i]);
						}
					}
					var count = checkArr.length;
					if(count!=0&&className.minlen!=null){
						if(count<className.minlen){
							Validation.errorMessage = '* '+element.title+'必须选择'+className.minlen+'项';
							return false;
						}
					}
				}
			}
			
		}
		if(type=='file'){
			var accurateFile = false;
			var url = element.value;
			var fileType = url.substr(url.lastIndexOf('.')+1,url.length);
			if(fileType=='exe'){
				Validation.errorMessage = '* 不能上传 .exe 类型文件!';
				return false;
			}
			var accept = element.getAttribute('accept');
			if(className.must =='true'){
				if(url == ''){
					Validation.errorMessage = '* '+element.title+'必须上传';
					return false;
				}	
			}
			if(url!=''){
				if(accept!=null){
					var accepts = accept.split('|');
					for(var i=0;i<accepts.length;i++){
						if(fileType==accepts[i]){
							accurateFile = true;//文件类型验证通过
							break;
						}
					}
				}
				if(accepts[0]==''){
					accurateFile = true;
				}
				if(!accurateFile){
					Validation.errorMessage = '* 文件类型必须为:'+element.getAttribute('accept');
					return false;
				}
			}
		}
		return true;
	},
	checkItem : function(element){
		var className = Validation.evalString(element.className);
		var val = element.value;
		var rules = Validation.allPrpos(ValidationRules);
		for(var i=0;i<rules.length;i++){
			if(rules[i]==className.type){
				var regex = ValidationRules[rules[i]].regex; 
				if(!regex.test(val)){//自定义验证规则匹配
					Validation.errorMessage = ValidationRules[rules[i]].alertText;
					element.value = '';
					return false;
				}
			}
		}
		if(typeof(className.minlen!='undefined')||typeof(className.maxlen)!='undefined'){
			if(val.length<className.minlen-0){
				Validation.errorMessage = '* 最小长度为:'+className.minlen;	
				return false;
			}
			if(val.length>className.maxlen-0){
				Validation.errorMessage = '* 最大长度为:'+className.maxlen;
				return false;
			}
		}
		return true;
	},
	 getElementsByClassName: function(clsName,htmltag){ 
		var arr = new Array(); 
		var elems = document.getElementsByTagName(htmltag);
		for ( var cls, i = 0; ( elem = elems[i] ); i++ ){
			if ( elem.className == clsName ){ //只能取到className完全相等的情况，未考虑有多个class的情况  多个可以用  if ( elem.className.indexOf(clsName)>=0 )
			//if ( elem.className.indexOf(clsName)>=0 ){
				arr[arr.length] = elem;
			}
		}
		return arr;
	},
	getElementByMessageId: function(attName){
		var elems = document.getElementsByTagName('*');
		for ( var cls, i = 0; ( elem = elems[i] ); i++ ){
			if ( elem.getAttribute('messageId') == attName ){
				return elem;
			}
		}
		/*
		elems = document.getElementsByTagName('textarea');
		for ( var cls, i = 0; ( elem = elems[i] ); i++ ){
			if ( elem.getAttribute('messageId') == attName ){
				return elem;
			}
		}
		*/
	},
	evalString : function(className){   //转换className  为obj
		if(className.indexOf(' ')>0){
			className = className.substr(0,className.indexOf(' '));//截取  空格前的字符串 也就是说 验证规则必须写在空格前 
		}
		var strArr = className.split(',');
			var evalStr = "({";
			for(var i=0;i<strArr.length;i++){
				var item = strArr[i].trim();
				evalStr += item.substr(0,item.indexOf(':')+1).trim()+"'"+item.substr(item.indexOf(':')+1).trim()+"'" +",";
			}
			evalStr = evalStr.trim().substr(0,evalStr.length-1);
			evalStr+="})";
			if(evalStr.indexOf(':')>0){
				return eval(evalStr);
			}else{
				return eval("({must:false})");
			}
	},
	allPrpos : function (obj) {  //转换
		var arr = [];
	    var props = ""; 
	    for(var p in obj){  
	        if(typeof(obj[p])=="function"){  
	            obj[p](); 
	        }else{  
	            // p 为属性名称，obj[p]为对应属性的值 
	            props+= p + "=" + obj[p] + "\t"; 
	            if(typeof(p)!='undefined'){
	            	arr.push(p);
	            }
	        }  
	    }  
	    return arr;
	},
	setBorderColor : function(element,color){                                 //设置边框颜色
		if(element!=null){
				element.style.border = "1px solid "+color;
		}
		
	},
	radioNameInArr : function (radiosArr,radioName){//判断radio的name 在数组中是否存在
		for(var i=0;i<radiosArr.length;i++){
		
			if(radioName==radiosArr[i].name){
				return true;
			}
		}
		return false;
	},
	validateSelectIsSelect : function(element){      //判断选择框是否选择
		return element.selectedIndex == 0 ? false:true;
	},
	brower  :  function() { //判断浏览器
		var ua = navigator.userAgent.toLowerCase();
		var os = new Object();
		os.isFirefox = ua.indexOf ('gecko') != -1;
		os.isOpera = ua.indexOf ('opera') != -1;
		os.isIE = !os.isOpera && ua.indexOf ('msie') != -1;
		os.isIE7 = os.isIE && ua.indexOf ('7.0') != -1;
		return os;
	},
	validateCheckRadioIsSelect : function(element){//判断单选框是否选择
		var isSelect = false;
		var radioElement = document.getElementsByName(element.name);
		for(var i=0;i<radioElement.length;i++){
			if(radioElement[i].checked){
				isSelect = true;
				break;
			}
		}
		return isSelect;
	},
	elementIsSelText : function(element){//判断元素是否是年选择框
		return Validation.evalString(element.className).sel=='true' ? true : false;
	},
	elementIsXxk : function(element){	//判断元素是否是选项卡
		return Validation.evalString(element.className).xxk=='true' ? true : false;
	},
	elementIsClsText : function(element){
		return Validation.evalString(element.className).cls=='true' ? true : false;
	},
	elementMaxlen : function(element){ //textarea 是否有设置最大输入  及时提示
		return Validation.evalString(element.className).maxlen!=null ? true : false;
	},
	elementIsZsh : function(element){//判断元素是否增删行的数据
		return Validation.evalString(element.className).zsh=='true' ? true : false;
	},
	elementIsDate : function(element){//是否是日历控件
	 	return Validation.evalString(element.className).type=='date' ? true : false;
	},
	setMustElementStyle : function(element){                                  //如果元素是必填项、必选项  设置边框样式
		var className = Validation.evalString(element.className);
		if(className.must=='true'){
			if(element.type=='text'||element.type=='textarea'||element.type=='file'){
				//Validation.setBorderColor(element,'#2979EF');                 //必填项颜色
				Validation.setBorderColor(element,'#A6FFFF');                 //必填项颜色
				//Validation.addMustElementFlag(element);
			}
			if(element.type=='select-one'){
				//Validation.addMustElementFlag(element);
			}
		}
	},
	addMustElementFlag : function(element){//为必填项添加 "*" 标记
		if(Validation.showMustFlag){
			var span  = document.createElement("span");
			span.innerHTML = "<b>*</b>";
			span.style.color = "red";
			element.parentNode.appendChild(span);
		}
	},
	appendElement : function(tagName, Attribute, strHtml, refNode) {         // 生成元素到refNode  可以remove
		var cEle = document.createElement(tagName);
		// 属性值
		for (var i in Attribute){
			cEle.setAttribute(i, Attribute[i]);
		}
		cEle.innerHTML = strHtml;
		refNode.appendChild(cEle);
		return cEle;
	},
	getUtterlyPoint : function(element){                                            // 获取元素绝对坐标
		var x = element.offsetLeft;
		var y = element.offsetTop;
		var parent = element.offsetParent;
		while (parent != null){
			x += parent.offsetLeft;
			y += parent.offsetTop;
			parent = parent.offsetParent;
		}
		
		//var x = element.getBoundingClientRect().left+document.documentElement.scrollLeft;
		//var y = element.getBoundingClientRect().top+document.documentElement.scrollTop;
		return {x: x, y: y};
	},
	getUomparativelyPoint : function(element){                                            // 获取元素相对坐标
		if(typeof(element)!='undefined'){
			var x = element.getBoundingClientRect().left+document.documentElement.scrollLeft;
			var y = element.getBoundingClientRect().top+document.documentElement.scrollTop;
			return {x: x, y: y};
		}else{
			return {x: 0, y: 0};
		}
		
	},
	addListener : function(element,e,ch){//添加事件
		var fn = null;
			if(e=='blur'){
				fn = function() {Validation.elementBlur(element)};	
			}
			if(e=='focus'){
				fn = function() {Validation.elementFocus(element)};	
			}
			if(e=='change'){
				fn = function() {Validation.elementChange(element)};	
			}
			if(e=='click'){
				fn = function(){Validation.elementClick(element)};
			}
			if(e=='propertychange'||e=='input'){
				fn = function(){Validation.elementPropertychange(element)};
			}
	        if(element.addEventListener){  
	             element.addEventListener(e,fn,false);  
	        } else {  //IE
	             element.attachEvent("on" + e,fn);  
	        } 
	},
	initDivElements : function(){                                             //初始化div 中的元素
		var divElement = null;
		var inputElements = [];
		var textareaElements = [];
		var selectElements = [];
		var checkboxElements = [];
		for(var i=0;i<Validation.divElemensArr.length;i++){
			var  elements = new Elements();
			divElement = Validation.divElemensArr[i];                         //得到div对象
			inputElements = divElement.getElementsByTagName('input');
			for(var j=0;j<inputElements.length;j++){
				if(!Validation.elementIsZsh(inputElements[j])){
					if(inputElements[j].type=='text'){
						elements.inputs.push(inputElements[j]);
					}
					if(inputElements[j].type=='radio'){
						if(!Validation.radioNameInArr(elements.radios,inputElements[j].name)){
							elements.radios.push(inputElements[j]);
						}
					}
					if(inputElements[j].type=='checkbox'){
						if(!Validation.radioNameInArr(elements.checkboxs,inputElements[j].name)){
							elements.checkboxs.push(inputElements[j]);
						}
					}	
					if(inputElements[j].type=='file'){
						elements.files.push(inputElements[j]);
					}	
					Validation.setMustElementStyle(inputElements[j]);			
				}
			}
			textareaElements = divElement.getElementsByTagName('textarea');
			for(var j=0;j<textareaElements.length;j++){
				if(!Validation.elementIsZsh(textareaElements[j])){
					elements.textareas.push(textareaElements[j]);
				}
				Validation.setMustElementStyle(textareaElements[j]);		
			}
			selectElements = divElement.getElementsByTagName('select');
			for(var j=0;j<selectElements.length;j++){
				if(!Validation.elementIsZsh(selectElements[j])){
					elements.selects.push(selectElements[j]);
					Validation.setMustElementStyle(selectElements[j]);
				}
				
			}
			elements.id = Validation.divElemensArr[i].id;
			Validation.elementsArr.push(elements);
		}
		
		//注册事件
		for(var i=0;i<Validation.elementsArr.length;i++){
				for(var j=0;j<Validation.elementsArr[i].inputs.length;j++){                      //input
					Validation.addListener(Validation.elementsArr[i].inputs[j],"focus",Validation.elementFocus);
					if(Validation.elementIsSelText(Validation.elementsArr[i].inputs[j]) || 
					   Validation.elementIsClsText(Validation.elementsArr[i].inputs[j])){ 
						Validation.addListener(Validation.elementsArr[i].inputs[j],"change",Validation.elementChange);
					}
					if(!(Validation.elementIsDate(Validation.elementsArr[i].inputs[j]))){//不是日历控件
						Validation.addListener(Validation.elementsArr[i].inputs[j],"blur",Validation.elementBlur);
					}
				}
				for(var j=0;j<Validation.elementsArr[i].radios.length;j++){                      //radio
						var radios = document.getElementsByName(Validation.elementsArr[i].radios[j].name);
						for(var k=0;k<radios.length;k++){  
							Validation.addListener(radios[k],"click",Validation.elementClick);		
						}
				}
				for(var j=0;j<Validation.elementsArr[i].checkboxs.length;j++){                      //checkbox
					if(Validation.elementsArr[i].checkboxs[j].name!=null&&Validation.elementsArr[i].checkboxs[j].name!=''){
						var checkboxs = document.getElementsByName(Validation.elementsArr[i].checkboxs[j].name);
						for(var k=0;k<checkboxs.length;k++){  
							Validation.addListener(checkboxs[k],"click",Validation.elementClick);		
						}
					}
						
				}
				for(var j=0;j<Validation.elementsArr[i].files.length;j++){                      //file
					Validation.addListener(Validation.elementsArr[i].files[j],"change",Validation.elementChange);//
				}
				for(var j=0;j<Validation.elementsArr[i].selects.length;j++){
						Validation.addListener(Validation.elementsArr[i].selects[j],"focus",Validation.elementFocus);
						Validation.addListener(Validation.elementsArr[i].selects[j],"blur",Validation.elementBlur);
				}
				for(var j=0;j<Validation.elementsArr[i].textareas.length;j++){                  //textarea
					Validation.addListener(Validation.elementsArr[i].textareas[j],"focus",Validation.elementFocus);
					Validation.addListener(Validation.elementsArr[i].textareas[j],"blur",Validation.elementBlur);
					if(Validation.elementMaxlen(Validation.elementsArr[i].textareas[j])){
						var brower = Validation.brower(); 
						if(brower.isIE){
							Validation.addListener(Validation.elementsArr[i].textareas[j],"propertychange",Validation.elementPropertychange);
						}else{
							Validation.addListener(Validation.elementsArr[i].textareas[j],"input",Validation.elementPropertychange);
						}
							
					}
				}
		}
	},
	elementFocus : function(element){//元素获得光标事件
		Validation.setBorderColor(element,'#4AD7F7');           //改变元素边框颜色
		if(element.type=='textarea'){
			if(Validation.elementMaxlen(element)){
				Validation.createTextareaMessage(element);	    
			}
		}
		Validation.removeMessageDiv(element);
	},
	elementPropertychange : function (element){  //textarea  及时提示
		Validation.removeMessageDiv(element);
		var point = Validation.getUtterlyPoint(element);
		var maxlen = Validation.evalString(element.className).maxlen;
		var total = 0; //当前输入
		var spare = 0; //剩余输入;   
		var stat_total_span = document.getElementById(element.name+point.y+"stat_total");
		var stat_left_span =  document.getElementById(element.name+point.y+"stat_left");
		if(stat_total_span!=null&&stat_left_span!=null){
			if(element.value.substr(maxlen-1, 2) == "\r\n"){  //防止最后一个字符为  \r \n
				element.value = element.value.substr(0, maxlen-1);
			}
			total =  element.value.trim().length;
			
			spare = maxlen - element.value.trim().length;
			if(total<=maxlen){
				stat_total_span.innerHTML = total;
				stat_left_span.innerHTML = spare;
			}else{
				element.value = element.value.substr(0,maxlen);
				Validation.setBorderColor(element,'red'); 
				Validation.errorMessage = "最多可输入为 "+maxlen+" 个字";
				Validation.createMessageDiv(element);
				Validation.errorMessage = "";
			}
		}
	},
	elementBlur : function(element){ //文本框失去焦点事件
		try{
			if(element.type=='text'||element.type=='textarea'){
					if(element.value!=null){//解决输入空格的问题
						if(element.value.trim().length==0){
							element.value = '';
						}
					}
					if(!Validation.validateElement(element)){//有错误
						Validation.setBorderColor(element,'red'); 
						Validation.removeMessageDiv(element);
						Validation.createMessageDiv(element);
					}else{
						Validation.setBorderColor(element,'#396DA5'); 
						Validation.removeMessageDiv(element);
					}
					
					if(element.type=='textarea'){
						Validation.removeTextareaMessage(element);
					}
				
			}
			if(element.type=='select-one'||element.type=='select-multiple'){
				if(!Validation.validateElement(element)){//有错误
						Validation.removeMessageDiv(element);
						Validation.createMessageDiv(element);
					}else{
						Validation.removeMessageDiv(element);
					}
			}
		}catch(e){}
	},
	
	elementChange :function(element){ //文本改变事件
		if(element.type=='file'){
			if(!Validation.validateElement(element)){//有错误
					Validation.setBorderColor(element,'red'); 
					Validation.removeMessageDiv(element);
					Validation.createMessageDiv(element);
			}else{
				Validation.setBorderColor(element,'#396DA5'); 
				Validation.removeMessageDiv(element);
			}
		}
		if(Validation.elementIsSelText(element)){//sel blur
			if(!Validation.validateElement(element)){//有错误
					Validation.setBorderColor(element,'red'); 
					Validation.removeMessageDiv(element);
					Validation.createMessageDiv(element);
			}else{
				Validation.setBorderColor(element,'#396DA5'); 
				Validation.removeMessageDiv(element);
			}	
		}
	}, 
	validateMuSelect : function(element){  //下拉多选控件的验证
		if(!Validation.validateElement(element)){
				Validation.setBorderColor(element,'red'); 
				Validation.removeMessageDiv(element);
				Validation.createMessageDiv(element);
			}else{
				Validation.setBorderColor(element,'#396DA5'); 
				Validation.removeMessageDiv(element);
			}
	},
	elementKeyUp : function (element){
		//if(Validation.elementIsClsText(element)){//cls  blur
			if(!Validation.validateElement(element)){
				Validation.setBorderColor(element,'red'); 
				Validation.removeMessageDiv(element);
				Validation.createMessageDiv(element);
			}else{
				Validation.setBorderColor(element,'#396DA5'); 
				Validation.removeMessageDiv(element);
			}
		//}
	},
	elementClick : function(element){
		if(element!=null){
			if(element.className=='popHint'){
				document.body.removeChild(element);	
				//Validation.removeMessageDiv(element);
			}
			if(element.type=='radio'){
				if(element.name!=null){
					var radios = document.getElementsByName(element.name);
					for(var i=0;i<radios.length;i++){
						Validation.setBorderColor(radios[i],'#FFFFFF'); //设置边框不可见
					}
					Validation.removeMessageDiv(radios[0]);
				}
			}
			if(element.type=='checkbox'){
				if(!Validation.validateElement(element)){//有错误
					Validation.setBorderColor(element,'red'); 
					Validation.removeMessageDiv(element);
					Validation.createMessageDiv(element);
				}else{
					if(element.name!=null){
						var checkboxs = document.getElementsByName(element.name);
						for(var i=0;i<checkboxs.length;i++){
							Validation.setBorderColor(checkboxs[i],'#FFFFFF'); //设置边框不可见
						}
						Validation.removeMessageDiv(checkboxs[0]);
					}
					
				}
			}
			else{
				Validation.removeAllMessageDiv();
			}
		}
	},
	removeAllMessageDiv : function (){ //移除所有提示框
		var divs = Validation.getElementsByClassName('popHint','div');
		if(divs.length>0){
			for(var i=0;i<divs.length;i++){
				document.body.removeChild(divs[i]);
			}
		}
		var iframes = Validation.getElementsByClassName('ipopHint','iframe');
		if(iframes.length>0){
			for(var i=0;i<iframes.length;i++){
				document.body.removeChild(iframes[i]);
			}
		}
	},
	removeMessageDiv : function(element){//移除单个提示框
		var messageDivId = element.getAttribute('messageId');
		var messageDiv = document.getElementById(messageDivId);
		if(messageDiv!=null){
			document.body.removeChild(messageDiv);	
		}
		
		
		var iFmessageDiv = document.getElementById('i'+messageDivId);
		if(iFmessageDiv!=null){
			//iFmessageDiv.style.display = 'none';
			document.body.removeChild(iFmessageDiv);	
		}
		
	},
	removeTextareaMessage : function(element){//隐藏textarea及时提示
		var msid = element.getAttribute("msid");
		if(msid!=null){
			var grayDiv = document.getElementById(msid);
			if(grayDiv!=null){
				//element.parentNode.removeChild(grayDiv);
				grayDiv.style.display = 'none';
			}
			
		}
	},
	createTextareaMessage : function(element){//textarea 消息提示DIV
	 var msid = element.getAttribute("msid");
	 if(msid==null){
	 	var point = Validation.getUtterlyPoint(element);//getUomparativelyPoint(element);
	 	
		var grayDiv = document.createElement('div');
			grayDiv.setAttribute("className","gray");
			
		var span1 = document.createElement('span');
			span1.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;最多可输入";	
			grayDiv.appendChild(span1);	
			
		var span2 = document.createElement('span');
			span2.setAttribute("className","b light");
			span2.setAttribute("id",element.name+point.y+"stat_max");  //最多输入
		var maxlen = Validation.evalString(element.className).maxlen;
			span2.innerHTML = maxlen;
			grayDiv.appendChild(span2);	
			
		var span3 = document.createElement('span');
			span3.innerHTML = ", 当前输入";	
			grayDiv.appendChild(span3);	
			
		var span4 = document.createElement('span');
			span4.setAttribute("className","b light");  
			span4.setAttribute("id",element.name+point.y+"stat_total");//当前输入
			span4.innerHTML = "0";
			grayDiv.appendChild(span4);	
			
		var span5 = document.createElement('span');
			span5.innerHTML = ", 还可以输入";	
			grayDiv.appendChild(span5);	
							
		var span6 = document.createElement('span');
			span6.setAttribute("className","b light");
			span6.setAttribute("id",element.name+point.y+"stat_left");//剩余输入	
			span6.innerHTML = maxlen;
			grayDiv.appendChild(span6);	
		
		
		var divId =  "msid"+element.name+point.x+point.y;
			grayDiv.setAttribute("id",divId);
			element.setAttribute("msid",divId);  //设置临时属性 textarea提示框的ID
			grayDiv.style.display = 'block';
			element.parentNode.appendChild(grayDiv);
	 }else{
	 	var grayDiv  = document.getElementById(msid);
	 	if(grayDiv!=null){
	 		grayDiv.style.display = 'block';
	 	}
	 }
	},
	showMessageDiv : function(element,message){ //自定义提示  关闭提示 使用 Validation.removeMessageDiv
		Validation.errorMessage = message;
		Validation.createMessageDiv(element);
		Validation.setBorderColor(element,'red');
		Validation.errorMessage = '';
	},
	createMessageDiv : function(element){//创建一个消息提示框
		if(Validation.errorMessage!=''){
			var point = null;
			if(document.getElementById('divData')==null){
				point =  Validation.getUtterlyPoint(element);//得到绝对位置
			}else{
				point =Validation.getUomparativelyPoint(element);//得到相对位置
			}
			
		//	var eleHeight = element.offsetHeight;//得到元素的高度
		//	var screenH = window.screen.height; //得到浏览器高度
		
			var x = point.x+10;
			var y = point.y - 26;
			if(Validation.elementIsXxk(element)){
				var pointId = '';	
				if(Validation.selectXxkEle==null){//选择的元素
					var className = Validation.evalString(element.className);
					pointId = className.pointid;
					var tempEle = document.getElementById(pointId);
					if(Validation.jsyz==false){
						point = Validation.getUomparativelyPoint(tempEle);
						y = point.y-26;
					}
				}else{
					pointId = Validation.selectXxkEle.id;
					var tempEle = document.getElementById(pointId);
					if(Validation.jsyz==false){
						point = Validation.getUomparativelyPoint(tempEle);
						y = point.y;
					}
				}
				
			}
			var customPoint = element.getAttribute('point');//得到自定义显示位置
			if(customPoint!=null){
				customPoint = Validation.evalString(customPoint);
				if(typeof(customPoint.x)!='undefined'){
					x =  customPoint.x;
				}
				if(typeof(customPoint.y)!='undefined'){
					y =  y - customPoint.y ;
				}
			}
			
			var divId =  element.name+x+y;
			element.setAttribute("messageId",divId);			//设置一个临时属性 用于得到messageId
			if(element.type!='radio'){
				$('body').append('<div id="'+divId+'" class="popHint"><div class="popHeader"><div  class="popLeft"></div><div class="popHintText" align="left"></div><div class="popRight"></div></div><div class="popAngle"><span></span></div></div>');
			}else{
				$('body').append('<div id="'+divId+'" class="popHint"><div class="popHeader"><div  class="popLeft"></div><div class="popHintText" align="left"></div><div class="popRight"></div></div><div class="popAngle"></div></div>');
			}

			$('#'+divId).css({left:x+'px',top:y+'px',zIndex:'1001'});
			$('#'+divId).find('.popHintText').html(Validation.errorMessage);
            $('#'+divId).fadeIn("fast");
            
//			var iframe = document.createElement('iframe');  //解决ie6 环境下不能遮挡select的问题  为提示框添加iframe  
//			iframe.setAttribute('scrolling','no');
//			iframe.setAttribute('id','i'+divId);
//			iframe.setAttribute('className','ipopHint');
//			iframe.setAttribute('frameborder','0');
//			
//		//	iframe.setAttribute('visibility','inherit');
//		//	iframe.setAttribute('filter','progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)');
//		//	iframe.setAttribute('allowTransparency','true');
//			
//			iframe.style.position = 'absolute';
//			
//			//var div = document.getElementById(divId);
//			var length = Validation.errorMessage.length;
//			//根据字符串的长度设置iframe的宽度 
//			//暂时不能得到MessageDiv 的宽度 先这样判断  如果最后一个字符为数字，则减去21px的宽度 
//			if(isNaN(Validation.errorMessage.substr(length-1,length))){
//				iframe.style.width = length * 12 -3  +'px';
//			}else{
//				iframe.style.width = length * 12 -21 +'px';
//			}
//			iframe.style.height = '24px';
//			iframe.style.left = x+'px';
//			iframe.style.top = y+'px';
//			iframe.style.zIndex = '1000';
//			document.body.appendChild(iframe);
			Validation.addListener($('#'+divId).get(0),"click",Validation.elementClick);	//为DIV添加点击关闭事件
		}
	},
	scroll : function(scr){			//DIV滚动条滚动事件
		var errorEle  = null;
		var point = null;
		var messageDivs = Validation.getElementsByClassName('popHint','div');
		var messageDiv  = null;
		var element = null;
		var divData = document.getElementById("divData");
		var className = null;
		var pointId = null;
		var tempEle = null;
		var x = 0;
		var y = 0;
		for(var i=0;i<messageDivs.length;i++){
			messageDiv = messageDivs[i];
			if(messageDiv!=null){
				 element = Validation.getElementByMessageId(messageDiv.id);
				 iframeElement = document.getElementById('i'+messageDiv.id);  //iframe  
				 x = 0;
				 y = 0;
				 if(!Validation.elementIsXxk(element)){  //如果不是选项卡
				 	point = Validation.getUomparativelyPoint(elem);
					x = point.x - 10;
					y = point.y - 26;
				 }else{
				 	if(Validation.selectXxkEle==null){
				 		className = Validation.evalString(element.className);
						pointId = className.pointid;
						tempEle = document.getElementById(pointId);
						point = Validation.getUomparativelyPoint(tempEle);
						y = point.y-26;
				 	}else{
				 		pointId = Validation.selectXxkEle.id;
						tempEle = document.getElementById(pointId);
						point = Validation.getUomparativelyPoint(tempEle);
						y = point.y;
				 	}
				 }
				 var customPoint = element.getAttribute('point');//得到自定义显示位置
						if(customPoint!=null){
							customPoint = Validation.evalString(customPoint);
							if(typeof(customPoint.y)!='undefined'){
								y =  y - customPoint.y ;
							}
						}
				 messageDiv.style.top = y +"px";     //提示div
				 iframeElement.style.top = y+"px";   //iframe  
			}
		}
		
		//var mushowSelect = Validation.getElementsByClassName('seleObj','div');
		//var mushowtext = Validation.getElementsByClassName('textObj','input');
		//if(mushowSelect.length==mushowtext.length){
			var temp_bldw = document.getElementById('bldw');//案源受理页面里的  待完善
			if(temp_bldw!=null){
					point = Validation.getUomparativelyPoint(temp_bldw);
					var tempAttr = temp_bldw.getAttribute('tempAttr')
					var objDiv = document.getElementById(tempAttr);
					if(objDiv!=null){
						objDiv.style.top = point.y-0+temp_bldw.offsetHeight+'px';
					}
					
			}
			
			var temp_nextperson = document.getElementById('nextperson');//下一处理人
			if(temp_nextperson!=null){
					point = Validation.getUomparativelyPoint(temp_nextperson);
					var tempAttr = temp_nextperson.getAttribute('tempAttr');
					var objDiv = document.getElementById(tempAttr);
					if(objDiv!=null){
						objDiv.style.top = point.y-0+temp_nextperson.offsetHeight+'px';
					}
					
			}
			
		//}
		messageDivs = null;
		/*
		for(var i=0;i<Validation.errorElement.length;i++){
				 errorEle = Validation.errorElement[i];
				 messageDiv = document.getElementById(errorEle.element.getAttribute('messageId'));
				if(messageDiv!=null){
					point = Validation.getUomparativelyPoint(errorEle.element);
					var customPoint = errorEle.element.getAttribute('point');//得到自定义显示位置
					if(customPoint!=null){
						customPoint = Validation.evalString(customPoint);
						if(typeof(customPoint.x)!='undefined'){
							point.x =  customPoint.x;
						}
					}
					messageDiv.style.left = point.x+10 +"px";
					messageDiv.style.top = point.y-26 +"px";
				}
		}
		*/
	},
	validateElements : function(){  //提交时验证
		Validation.errorElement.length = 0;
		Validation.rediosElement.length = 0;
		Validation.checkboxsElement.length = 0;
		var divElement = null;
		var inputElements = [0];
		var textareaElements = [0];
		var selectElements = [0];
		var element = null;
		var  errorEle = null;  //错误信息
		for(var i=0;i<Validation.divElemensArr.length;i++){
			divElement = Validation.divElemensArr[i];//得到div对象
			inputElements = divElement.getElementsByTagName('input');
			for(var j=0;j<inputElements.length;j++){
				if(inputElements[j].type=='radio'){
					if(!Validation.radioNameInArr(Validation.rediosElement,inputElements[j].name)){
						if(!Validation.validateElement(inputElements[j])){
							element = inputElements[j];
							var radios = document.getElementsByName(element.name);
							for(var k=0;k<radios.length;k++){//如果是radio   遍历所有，设置边框为红色 
								Validation.setBorderColor(radios[k],'red');
							}
							errorEle = new ErrorElement();
							errorEle.element = element;
							errorEle.message = Validation.errorMessage;
							Validation.errorElement.push(errorEle);
							Validation.rediosElement.push(inputElements[j]);
						}
					}
				}
				if(inputElements[j].type=='checkbox'){
					if(!Validation.radioNameInArr(Validation.checkboxsElement,inputElements[j].name)){
						if(!Validation.validateElement(inputElements[j])){
							element = inputElements[j];
							var checkboxs = document.getElementsByName(element.name);
							for(var k=0;k<checkboxs.length;k++){//如果是chekcbox   遍历所有，设置边框为红色 
								Validation.setBorderColor(checkboxs[k],'red');
							}
							errorEle = new ErrorElement();
							errorEle.element = element;
							errorEle.message = Validation.errorMessage;
							Validation.errorElement.push(errorEle);
							Validation.checkboxsElement.push(inputElements[j]);
						}
					}
				}
				else{
					if(!Validation.validateElement(inputElements[j])){
						element = inputElements[j];
						Validation.setBorderColor(element,'red');
						errorEle = new ErrorElement();
						errorEle.element = element;
						errorEle.message = Validation.errorMessage;
						Validation.errorElement.push(errorEle);
					}
					
				}
			}
			textareaElements = divElement.getElementsByTagName('textarea');
		
			for(var j=0;j<textareaElements.length;j++){
				if(!Validation.validateElement(textareaElements[j])){
					element = textareaElements[j];
					Validation.setBorderColor(element,'red');
					errorEle = new ErrorElement();
					errorEle.element = element;
					errorEle.message = Validation.errorMessage;
					Validation.errorElement.push(errorEle);
				}
			}
			
			selectElements = divElement.getElementsByTagName('select');
			
			for(var j=0;j<selectElements.length;j++){
				if(!Validation.validateElement(selectElements[j])){
					element = selectElements[j];
					errorEle = new ErrorElement();
					errorEle.element = element;
					errorEle.message = Validation.errorMessage;
					Validation.errorElement.push(errorEle);
				}
			}
		}
		if(Validation.errorElement.length>0){
			for(var i=0;i<Validation.errorElement.length;i++){
				errorEle = Validation.errorElement[i];
				Validation.errorMessage = errorEle.message;
				Validation.removeMessageDiv(errorEle.element);
				Validation.createMessageDiv(errorEle.element);
			}
			return false;
		}else{
			return true;
		}
	},
	validate : function(divNamesArr){
	//	Load.show();  //显示遮盖层 及Loading
		Validation.jsyz = false; //及时验证 = false
		var divData = document.getElementById("divData");
		if(divData!=null){
			//document.all.divData.scrollTop =0;
		}
		Validation.removeAllMessageDiv();
		Validation.divElemensArr.length = 0;
			for(var i=0;i<divNamesArr.length;i++){
				Validation.divElemensArr.push(document.getElementById(divNamesArr[i]));
			}
		var allowCommit = Validation.validateElements();
		var divData = document.getElementById("divData");
		if(divData!=null){
			var scrollTop = 0;
			var errorEle = null;
			for(var i=0;i<Validation.errorElement.length;i++){
				 errorEle = Validation.errorElement[i];
				 break;
			}
			if(errorEle!=null){//有错误 设置浏览器滚动条位置
				var point = Validation.getUomparativelyPoint(errorEle.element);
				if(point!=null){
					if((point.y-0)<=0){
						document.all.divData.scrollTop = 0;
					}else{
						document.all.divData.scrollTop = point.y;
					}
				}
				
			}
			//var screenH = window.screen.height; //得到浏览器高度
			//document.all.divData.scrollTop = min;//scrollTop;
			//	serrorEleTop.length = 0;
		}
		Validation.jsyz = true; 
		if(!allowCommit){
	//		Load.close(); //验证不通过  去除遮盖层和Loading
		}
		return allowCommit;
	},
	init : function(divNamesArr){//初始化
		try{
			for(var i=0;i<divNamesArr.length;i++){
				Validation.divElemensArr.push(document.getElementById(divNamesArr[i]));
			}
			Validation.initDivElements();
		}catch(e){}
	}
	
}

 document.ondblclick = function(e){//双击事件 移除所有提示框
 	Validation.removeAllMessageDiv();
 }

String.prototype.trim = function()
{
    // 用正则表达式将前后空格用空字符串替代。
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
var Elements = function(){
		this.id = '';
		this.inputs = [];
	    this.textareas=[];
	    this.radios = [];
	    this.selects= [];
	    this.checkboxs = [];
	    this.files = [];
}
var ErrorElement = function(){
	this.element = null;
	this.message = '';
}

//自定义验证规则 可添加
var ValidationRules = {
	"int":{
        "regex":/^[0-9\ ]+$/,
        "alertText":"* 请输入正整数"
    },
	"number":{
		"regex":/^\-{0,1}\d*(\.\d*)?$/,///^[0-9-()|.]+$/,
		"alertText":"* 请输入数字"  
	},
	"z-number":{
		"regex":/^\d*(\.\d*)?$/,///^[0-9-()|.]+$/,
		"alertText":"* 请输入正数"  
	},
	"date":{
        "regex":/^\d{4}(\-|\/|\.)\d{1,2}\1\d{1,2}$/,
        "alertText":"* 日期格式为:YYYY-MM-DD"
    },
    "year":{
        "regex":/^[0-9]{4}$/,
        "alertText":"* 日期格式为:YYYY"
    },
    "postalcode":{
         "regex":/^[0-9]{6}$/,
         "alertText":"* 邮编格式不正确"
    },
    "double":{
         "regex":/^\-{0,1}\d+(\.\d{1,2})?$/,
         "alertText":"* 请输入有效的金额数字"
    },
    "email":{
    	"regex":/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/,
        "alertText":"* 请输入有效邮件地址"
    },
    "IDCard":{
    	"regex":/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/,
        "alertText":"* 请输入有效的身份证号码"
    },
    "mobile":{
    	"regex":/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/,
        "alertText":"* 请输入有效手机号码"
    },
    "mail":{
    	"regex":/^([a-zA-Z0-9_-])+([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/,
        "alertText":"* 请输入有效邮件地址"
    }
   
}
