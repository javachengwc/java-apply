
var checkElement;

function checkAll() { 
	var check = document.getElementsByTagName("input");
	var index1 = 0;
	var checkArr = new Array();
	for(var m = 0;m<check.length;m++){
		if(check[m].type == "checkbox"){
			checkArr[index1] = check[m];
			index1++;
		}
	}
	if(checkArr[0].checked== true){
		for(var i = 1;i<checkArr.length;i++){
			checkArr[i].checked = true;
		}
 	}else{
  		for(var i = 1;i<checkArr.length;i++){
			checkArr[i].checked = false;
		}
    }	
}
function getCheckCount()
{
	var checkEle;
	var checkCount=0;
	var check = document.getElementsByTagName("input");
	var index1 = 0;
	var checkArr = new Array();
	for(var m = 0;m<check.length;m++){
		if(check[m].type == "checkbox"){
			checkArr[index1] = check[m];
			index1++;
		}
	}
	for(var i = 1;i<checkArr.length;i++){
		if(checkArr[i].checked == true)
		{
		   checkEle=checkArr[i];
		   checkCount+=1;
		}
	}
	if(checkCount==1)
		checkElement=checkEle;
	return checkCount;
}