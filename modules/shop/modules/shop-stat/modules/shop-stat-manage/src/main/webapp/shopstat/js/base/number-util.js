//数字转成带千位符的
//ep:transKilobitData(1234.56,2)=1,234.56
function transKilobitData(num, precision, separator) {
    var parts;
    // 判断是否为数字
    if (!isNaN(parseFloat(num)) && isFinite(num)) {
        num = Number(num);
        // 处理小数点位数
        num = (typeof precision !== 'undefined' ? num.toFixed(precision) : num).toString();
        //分离数字的小数部分和整数部分
        parts = num.split('.');
        // 整数部分加[separator]分隔, 借用正则表达式
        parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1' + (separator || ','));
        if(parts.length>1)
        {
            parts[1] = parts[1].toString().replace(/[0]+/g, '');
            if( parts[1]==null || parts[1].toString().length<=0)
            {
                return parts[0].toString();
            }
        }
        return parts.join(".");
    }
    return num;
}