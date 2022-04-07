function trim(str)
{
    return str.replace(/&nbsp;/g,"").replace(/(^\s*)|(\s*$)/g,"");
}
function isBlank(str)
{
	if(str==null || str==undefined || str=="")
		return true;
	if(trim(str)=="")
	{
		return true;
	}
	return false;
}
function isNumber(str)
{
    var i;
    if(str.length==0)
       return false;
    for(i=0;i<str.length;i++)
    {
       if(str.charAt(i)<"0" || str.charAt(i)>"9")
             return false;
    }
    return true;
}
 function onclose()
 {
     window.close();
 }
 function checknum(ele,title)
 {
     if(ele.val()==null ||ele.val()=="" )
    {
       alert(title+"不能为空!");
       return false;
    }
     if(!isNumber(ele.val()))
     {
         alert(title+"必须是数字!");
         return false;
     }
     if(ele.val()<0)
     {
         alert(title+"必须大于等于0!");
         return false;
     }
     return true;
 }