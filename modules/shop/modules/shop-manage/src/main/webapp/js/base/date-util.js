/**
 * 日期工具
 */
/**
 * 注册日期格式化
 * 例如:new Date().format("yyyy-MM-dd")
 */
Date.prototype.format = function(format) //author: meizz
{
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) 
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) 
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    return format;
}

//传日期字符串格式化日期
function formatDate(date, fmt){
    if (!date) 
        return "";
    fmt = fmt || "yyyy-MM-dd";
    var d = new Date(Date.parse(date.replace(/-/g, "/")));
    return d.format(fmt);
}

//得到当天的日期
function getDoday(fmt){
    fmt = fmt || "yyyy-MM-dd";
    return new Date().format(fmt);
}

//本周开始日期
function getBeginDateOfWeek(fmt){
    var myDate = new Date();
    var week = myDate.getDay();
    if (week == 0) 
        week = 7;
    var daybegin = (-1) * week + 1;
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    day = day + daybegin;
    if (month < 10) 
        month = "0" + month;
    if (day < 10) 
        day = "0" + day;
    var beginWeekDate = year + "-" + month + "-" + day;
    return beginWeekDate
}

//本周结日期
function getEndDateOfWeek(fmt){
    var myDate = new Date();
    var week = myDate.getDay();
    if (week == 0) 
        week = 7;
    var dayend = 7 - week;
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    day = day + dayend;
    if (month < 10) 
        month = "0" + month;
    if (day < 10) 
        day = "0" + day;
    var endWeekDate = year + "-" + month + "-" + day;
    return endWeekDate
}

//本月开始日期
function getBeginDateOfMonth(fmt){
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    if (month < 10) 
        month = "0" + month;
    return year + "-" + month + "-" + "01";
}

//本月结日期
function getEndDateOfMonth(fmt,d){
    var myDate = d||new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    if (month < 10) 
        month = "0" + month;
    //取最后一天日期
    var new_date = new Date(year, month, 1);
    var day = (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate();
    return year + "-" + month + "-" + day;
}

function compareDate(date1, date2){ //date1是否大于date2
    var dateArry1 = date1.split("-");
    var dateArry2 = date2.split("-");
    
    var year1 = dateArry1[0];
    var month1 = dateArry1[1];
    var day1 = dateArry1[2];
    
    var year2 = dateArry2[0];
    var month2 = dateArry2[1];
    var day2 = dateArry2[2];
    
    if (parseFloat(year1) > parseFloat(year2)) {
        return true;
    }
    else if (parseFloat(year1) < parseFloat(year2)) {
        return false;
    }
    else if (parseFloat(month1) > parseFloat(month2)) {
        return true;
    }
    else if (parseFloat(month1) < parseFloat(month2)) {
        return false;
    }
    else  if (parseFloat(day1) > parseFloat(day2)) {
        return true;
    }
    
    return false;
}
function diffDays(date1,date2){
	var d1 = new Date(Date.parse(date1.replace(/-/g, "/")));
	var d2 = new Date(Date.parse(date2.replace(/-/g, "/")));
	var days = Math.abs(d1.getTime() - d2.getTime())/(1000 * 60 * 60 * 24);
	return Math.ceil(days);
}

/**
 * 判断两个时间的间隔,是否大于指定的间隔
 * @param time1 yyyy-mm-dd HH:mm:ss
 * @param time2 yyyy-mm-dd HH:mm:ss
 * @param intervalSeconds 时间间隔,单位：秒
 * 
 * @return time2 - time1 > intervalSeconds 返回true; 否则,返回false;
 */
function countInterval(time1,time2,intervalSeconds){
	var y1=  time1.substr(0,4);
    var y2=  time2.substr(0,4);

    var m1 = time1.substr(6,2);
    var m2= time2.substr(6,2);

    var d1= time1.substr(9,2);
    var d2= time2.substr(9,2);
	
    var temp1 = y1+"/"+m1+"/"+d1 + time1.substring(10);
    var temp2 = y2+"/"+m2+"/"+d2 + time2.substring(10);
	var timeVal1 = new Date(temp1).getTime();
	var timeVal2 = new Date(temp2).getTime();
	var interval = (timeVal2 - timeVal1);
	if( interval > intervalSeconds*1000 ){
		return true;
	}else{
		return false;
	}
}

//得到当天的日期 yyyy-mm-dd hh:MM:ss
function getCurrentTime(){
	 var now = new Date();
     var year = now.getFullYear();       //年
     var month = now.getMonth() + 1;     //月
     var day = now.getDate();            //日
    
     var hh = now.getHours();            //时
     var mm = now.getMinutes();          //分
     var ss = now.getSeconds();          //秒
    
     var clock = year + "-";
    
     if(month < 10)
         clock += "0";
    
     clock += month + "-";
    
     if(day < 10)
         clock += "0";
        
     clock += day + " ";
    
     if(hh < 10)
         clock += "0";
        
     clock += hh + ":";
     if (mm < 10) clock += '0'; 
     clock += mm + ":"; 
     
     if (ss < 10) clock += '0'; 
     clock += ss; 
     return(clock); 
}

function addDay(date,addDay){ 
	var arr = formatDate(date).split("-");  
	var newdate = new Date(Number(arr[0]),Number(arr[1])-1,Number(arr[2])+addDay);  
	var result = newdate.getFullYear()+"-"+(newdate.getMonth()+1)+"-" + newdate.getDate();
	return formatDate(result);
}  

function addYear(date,addYear){ 
	var arr = formatDate(date).split("-");  
	var newdate = new Date(Number(arr[0])+addYear,Number(arr[1])-1,Number(arr[2]));  
	var result = newdate.getFullYear()+"-"+(newdate.getMonth()+1)+"-" + newdate.getDate();
	return formatDate(result);
} 

function showdate(n)  
{  
	var uom = new Date();
	uom.setDate(uom.getDate()+n);  
	uom = uom.getFullYear() + "-" +   (uom.getMonth()+1) + "-" + uom.getDate();  
	return uom;  
} 