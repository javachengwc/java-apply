function setFlash(url,w,h){
	var txt='<embed src='+url+' width='+w+' height='+h+'>';
	txt+='</embed>';
	document.write(txt);
}