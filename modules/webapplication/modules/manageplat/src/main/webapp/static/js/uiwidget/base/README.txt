1.0.0	2010-03-12

1.日志记录：$.uiwidget.Log
$.debug("") 输出debug日志到页面底部
$.info("") 输出info日志到页面底部

2.JSON处理：$.uiwidget.JSON
$.uiwidget.JSON.encode 将对象转换成json字符串
$.uiwidget.JSON.decode 将json字符串转换成对象

3.简单计时器：$.uiwidget.StopWatch
var sw = $.uiwidget.StopWatch();
sw.start();
...do something
sw.stop();
alert(sw);//打印出do something所用时间 