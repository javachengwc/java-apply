function picTab(){
var focus_width=250;
var focus_height=160;
var text_height=20;
var swf_height = focus_height+text_height;
var pics = '';
var links = '';
var texts = '';
				function addTxt(url, img, title){
									if(pics != ''){
										pics = "|" + pics;
										links = "|" + links;
										texts = "|" + texts;
										}
									pics = escape(img) + pics;
									links = escape(url) + links;
									texts = title + texts;
								   }								
addTxt('http://www.a.com', 'img/newsa.jpg', '共建和谐社会');	
addTxt('http://www.b.com', 'img/newsb.jpg', '财富新闻消息');		
addTxt('http://www.c.com', 'img/newsc.jpg', '网络营销新闻');	
var txt='<embed src="swf/pic.swf" wmode="transparent" FlashVars="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'" menu="false" quality="high" width="'+ focus_width +'" height="'+ swf_height +'" allowScriptAccess="sameDomain" type="application/x-shockwave-flash"/>';
document.write(txt);
}