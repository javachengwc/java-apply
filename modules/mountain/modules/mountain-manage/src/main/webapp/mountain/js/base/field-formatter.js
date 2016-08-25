var weekDays=["星期天","星期一","星期二","星期三","星期四","星期五","星期六"];

function formatDate(value, row, index) {
    if(value==null ||value=="")
    {
        return "";
    }
    return new Date(value).format('yyyy-MM-dd');
}

function formatDateTime(value, row, index)
{
    if(value==null ||value=="")
    {
        return "";
    }
    return new Date(value).format('yyyy-MM-dd hh:mm:ss');
}

//星期
function formatWeekDay(value, row, index) {
    if(value==null ||value=="")
    {
        return "";
    }
    var date = new Date(value);
    if(0<=date.getDay() && date.getDay()<=6)
    {
        return weekDays[date.getDay()];
    }
    return value;
}