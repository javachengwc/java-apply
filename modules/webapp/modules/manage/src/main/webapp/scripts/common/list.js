function showif()
{
	var hidese=document.getElementById("hideselect");
	hidese.style.display="block";
}

function hideif()
{
	var hidese=document.getElementById("hideselect");
	hidese.style.display="none";
}

function resetValue()
{
	var value = document.getElementById("checkInput").value;
	if(value ==deftip)
	{
		document.getElementById("checkInput").value="";
	}
}
function initValue(){
	var value = document.getElementById("checkInput").value;
	if(value =="")
	{
		document.getElementById("checkInput").value=deftip;
	}
}

function initWidth()
{
	var all=document.getElementById("mytest");
	$(all).css("overflow-y","auto");
    var ph=window.screen.width;//根据分辨率设置宽度
   
    if(ph != 1024)
	{
		all.style.width="100%";
		all.style.height="100%";
	}
}
function initOnload()
{
	initWidth();
	initValue();
}