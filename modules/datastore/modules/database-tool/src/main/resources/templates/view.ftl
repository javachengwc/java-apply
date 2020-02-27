<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="数据库表查询">
    <meta name="description" content="数据库表查询">
    <title>数据库表查询</title>
    <link rel="stylesheet" type="text/css" href="/static/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/static/easyui/themes/icon.css">
    <script type="text/javascript" src="/static/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script>

        function updateTableSqlInfo(node) {
            $('#sql').textbox('setValue', 'select * from ' + node.text);
        }
        function updateDatagrid(data) {
            var d = {};
            d.fields = data;
            d.rows = [];
            initDg(d);
        }
        function initDg(data) {
            var array = [];
            array.push(data.fields);
            $('#dg').datagrid({
                columns: array,
                data: data.rows.slice(0, 20),
                fitColumns: true,
                singleSelect: true,
                rownumbers: true,
                pagination: "true",
                pageSize: 20,
                pageList: [10, 20, 30, 40, 50],
            })
            var pager = $("#dg").datagrid("getPager");
            pager.pagination({
                total: data.rows.length,
                onSelectPage: function (pageNo, pageSize) {
                    var start = (pageNo - 1) * pageSize;
                    var end = start + pageSize;
                    $("#dg").datagrid("loadData", data.rows.slice(start, end));
                    pager.pagination('refresh', {
                        total: data.rows.length,
                        pageNumber: pageNo
                    });
                }
            });
        }

        $(function () {
            $.ajaxSetup({
                beforeSend: function () {
                    console.log("before send");
                    $('#loading_msg').show();
                },
                complete: function () {
                    console.log("complete");
                    $('#loading_msg').hide();
                }
            });
            $('#tt').tree({
                url: '/tables',
                lines: true,
                onDblClick: function (node) {
                    console.log(node.text);
                    $('#count_info').text('');
                    if (node.type === "DB") {
                        $('#sql').textbox('setValue', '');
                        updateDatagrid({});
                        return;
                    }
                    updateTableSqlInfo(node);
                    $.post('/getColumns', {tableName: node.text},
                            function (data) {
                                updateDatagrid(data);
                            }
                    )
                },
                onContextMenu: function (e, node) {
                    e.preventDefault();
                    $('#tt').tree('select', node.target);
                    $('#mm').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            });
            $('#btn').on('click', function () {
                console.log('query');
                $.post('/query', {sql: $('#sql').textbox('getValue')},
                        function (data) {
                            var info = "共" + data.count + "条记录";
                            $('#count_info').text(data.count > 10000 ? info + ",最多显示10000条记录" : info);
                            initDg(data);
                        }
                ).fail(function () {
                            $.messager.show({
                                title: '错误',
                                msg: '查询出错',
                                timeout: 5000,
                                showType: 'slide'
                            });
                        });
            });
        });
    </script>
</head>
<body>
<div id ="left" style="width:20%;float:left">
   <ul id="tt" class="easyui-tree" url="/tables" />
</div>
<div id="right" style="width:80%;float:right">
    <div>
        sql：
    </div>
    <div style="margin-top:10px;margin-bottom:10px;">
        <input id="sql" class="easyui-textbox" data-options="multiline:true" style="width:650px;height:100px">
        <a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
    </div>
    <div style="margin-top:10px;margin-bottom:10px;">
        结果显示：<span id="count_info"></span>
    <div>
    <div style="margin-top:10px;margin-bottom:10px;">
        <table id="dg"></table>
    </div>
</body>
</html>