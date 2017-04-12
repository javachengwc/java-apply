<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>websocket</title>
    <script type="text/javascript" src="/js/jquery-1.8.0.js"></script>
</head>
<body>
    <div>
        <input id="text" type="text"/>&nbsp;&nbsp;&nbsp;<button onclick="send()">发送消息</button>&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;<button onclick="closeCnn()">关闭连接</button>
    </div>
    <div id="message"></div>
</body>
</html>

<script type="text/javascript">
    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/websocket");
    }
    else {
        alert('浏览器不支持websocket')
    }

    websocket.onerror = function () {
        show("websocket连接错误");
    };

    websocket.onopen = function () {
        show("websocket连接成功");
    }

    websocket.onmessage = function (event) {
        show(event.data);
    }

    websocket.onclose = function () {
        show("websocket连接关闭");
    }

    //将消息显示在网页上
    function show(info) {
        var oldHtml =$('#message').html();
        $('#message').html(oldHtml+info+'<br/>');
    }

    function send() {
        var message = $('#text').val();
        websocket.send(message);
    }

    function closeCnn() {
        websocket.close();
    }

    //监听窗口关闭事件，当窗口关闭时，关闭websocket连接，防止连接还没断开就关闭窗口
    window.onbeforeunload = function () {
        closeCnn();
    }
</script>