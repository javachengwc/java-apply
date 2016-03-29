
//初始化
$(function () {

    $('#tbApp').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        view: detailview,
        detailFormatter: noteFormat,
        toolbar:'#toolbar',
        method:'get',
        url:'/queryAppPage.do'
    });

});

//重载应用
function reloadApp(){
    $('#tbApp').datagrid('reload');
}

//查询
function search() {
    var name =$("#name").val();
    var nameCh =$("#nameCh").val();
    var data ={name:name,nameCh:nameCh};
    $('#tbApp').datagrid('reload',data);
}

//展示备注信息
function noteFormat(rowIndex, rowData)
{
    var html = '';
    html +='<div style="margin:3px 0px;">';
    html +='<span style="margin-right: 5px;">备注:</span>';
    html +='<span>' + rowData.note + '</span>';
    html += '</div>';
    return html;
}

//渲染操作按钮
function formatOpt(value, row, index) {

    var appId = row.id;
    var html='';
    html += '<a href="javascript:void(0);"  onclick="synApp('+index+')">同步</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="openConfigTab('+index+')">配置项</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="editApp('+index+')">编辑</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="delApp('+index+')">删除</a>';

    return html;
}

//打开增改应用栏
function openAppDlg()
{
    $('#app_form').form('clear');
    $('#app_add').dialog('open').dialog('setTitle', '新增应用');
}

//增改应用
function addOrUptApp()
{
    var options = {
        url: '/addOrUptApp.do',
        type: 'post',
        dataType: 'json',
        success: function (json) {
            if (json.result == 0) {
                $('#app_add').dialog('close');
                reloadApp();
            } else {
                tipMsg("错误", json.msg);
            }
        }
    };

    $("#app_form").ajaxSubmit(options);
}

//编辑应用
function editApp(index)
{
    var row = $('#tbApp').datagrid('getRows')[index];
    if (row) {
        $('#app_add').dialog('open').dialog('setTitle', '编辑应用');
        $('#app_form').form('load', row);
    } else {
        tipNoSelect();
    }
}

//删除应用
function delApp(index)
{
    var row = $('#tbApp').datagrid('getRows')[index];
    if (row) {
        $.messager.confirm('确认', '确认删除此应用吗?',
            function (r)
            {
                if (r)
                {
                    $.post('/delApp.do',{id:row.id},
                        function (json)
                        {
                            if (json.result == 0) {
                                reloadApp();
                            } else {
                                tipMsg("错误", json.msg);
                            }
                        },
                        'json');
                }
            });
    }
}

//打开应用的配置项tab
function openConfigTab(index)
{
    var row = $('#tbApp').datagrid('getRows')[index];
    if (row)
    {
        var appId =row.id;
        var appName = row.name;
        var title = "应用["+appName+"]";

        if (parent.window.document.getElementById("tabs") != null) {
            parent.window.createLinkTabPanel(title, '/appConfigView.do?appId=' + appId+'&appName='+appName);
        } else {
            top.window.createLinkTabPanel(title, '/appConfigView.do?appId=' + appId+'&appName='+appName);
        }
    }
}

//全量同步
function synAll()
{
    $.messager.confirm('确认', '确定所有应用配置同步到zookeeper吗?',
        function (r)
        {
            if (r)
            {
                $.ajax({
                    type: "get",
                    url: "/synAll.do",
                    dataType:'json',
                    success: function (json) {

                        if (json.result == 0) {
                            tipMsg("提示", "同步成功");
                        } else {
                            tipMsg("错误", json.msg);
                        }
                    }
                });
            }
        });
}

//同步app
function synApp( index)
{
    var row = $('#tbApp').datagrid('getRows')[index];
    if (row) {

        var appId =row.id;
        var appName = row.name;
        $.messager.confirm('确认', '确认应用'+appName+'配置同步到zookeeper吗?',
        function (r)
        {
            if (r)
            {
                $.ajax({
                    type: "get",
                    url: "/synApp.do",
                    data:{id:appId},
                    dataType:'json',
                    success: function (json) {

                        if (json.result == 0) {
                            tipMsg("提示", "同步成功");
                        } else {
                            tipMsg("错误", json.msg);
                        }
                    }
                });
            }
        });
    }

}