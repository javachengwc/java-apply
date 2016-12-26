<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>DB管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" type="text/css" href="/static/thirdpart/jquery-easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="/static/thirdpart/jquery-easyui/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <style>
        #query_div{text-align: left;padding:5px 0px 5px 5px;}
        #tbOrder{width:100%;}
    </style>

    <script type="text/javascript" src="/static/js/jquery-1.8.0.js"></script>
    <script type="text/javascript" src="/static/js/jquery.form.js"></script>
    <script type="text/javascript" src="/static/js/base/date-util.js"></script>
    <script type="text/javascript" src="/static/js/base/string-util.js"></script>
    <script type="text/javascript" src="/static/js/base/popups-util.js"></script>

    <script type="text/javascript" src="/static/thirdpart/jquery-easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/thirdpart/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/thirdpart/jquery-easyui/extension/datagrid-detailview.js"></script>

    <script type="text/javascript" src="/static/js/db.js"></script>

</head>
<body>
<div id="toolbar">
    <div id="query_div">
        <span>host:
            <input id="host" name="host" type="text" />
        </span>
        <span style="margin-left:5px;">数据库名:
            <input id="name" name="name" type="text" />
        </span>
        <span style="margin-left:5px;">账号:
            <input id="account" name="account" type="text" />
        </span>
        <input id="searchBtn" type="button" value="查询" onClick="search()" />
    </div>
</div>
<table id="tbDb">
    <thead>
    <tr>
        <th data-options="field:'id',align:'left'">ID</th>
        <th data-options="field:'ht',align:'left'" >Host</th>
        <th data-options="field:'pt',align:'left'" >端口</th>
        <th data-options="field:'name',align:'left'">数据库名</th>
        <th data-options="field:'account',align:'left'">账号</th>
        <th data-options="field:'pwd',align:'left'">密码</th>
        <th data-options="field:'createTime',align:'left'">创建时间</th>
        <th data-options="field:'updateTime',align:'left'">更新时间</th>
    </tr>
    </thead>
</table>

</body>
</html>