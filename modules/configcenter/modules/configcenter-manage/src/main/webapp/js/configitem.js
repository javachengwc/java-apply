
//重载配置项
function reloadConfigItem(){
    $('#tbConfig').datagrid('reload');
}

//查询
function search() {
    var name =$("#name").val();
    var key =$("#key").val();
    var parentId=$("#parentId").val();
    var data ={name:name,key:key,parentId:parentId};
    $('#tbConfig').datagrid('reload',data);
}

//格式化时间
function formatDateTime(value, row, index) {
    if(value==null||value==''){
        return "";
    }
    return new Date(value).format('yyyy-MM-dd hh:mm:ss');
}

//渲染操作按钮
function formatOpt(value, row, index) {

    var html='';
    html += '<a href="javascript:void(0);"  onclick="queryConfigItemOnZK('+index+')">查看ZK</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="editConfigItem('+index+')">编辑</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="delConfigItem('+index+')">删除</a>';

    return html;
}

//打开增改配置项栏
function openConfigItemDlg()
{
    $('#config_form').form('clear');

    var appName = $("#name").val();

    $('#appId').textbox('readonly', false);
    $('#appId').textbox('setValue',appId);
    $('#appId').textbox('setText',appName);
    $('#appId').textbox('readonly', true);

    $('#config_add').dialog('open').dialog('setTitle', '新增配置项');
}

//增改配置项
function addOrUptConfigItem()
{
    var options = {
        url: '/addOrUptConfigItem.do',
        type: 'post',
        dataType: 'json',
        success: function (json) {
            if (json.result == 0) {
                $('#config_add').dialog('close');
                reloadConfigItem();
            } else {
                tipMsg("错误", json.msg);
            }
        }
    };

    $("#config_form").ajaxSubmit(options);
}

//编辑配置项
function editConfigItem(index)
{
    var row = $('#tbConfig').datagrid('getRows')[index];
    if (row) {

        var appId = row.appId;
        var appName =row.appName;

        $('#config_add').dialog('open').dialog('setTitle', '编辑配置项');
        $('#config_form').form('load', row);

        $('#appId').textbox('readonly', false);
        $('#appId').textbox('setValue',appId);
        $('#appId').textbox('setText',appName);
        $('#appId').textbox('readonly', true);

    } else {
        tipNoSelect();
    }
}

//删除配置项
function delConfigItem(index)
{
    var row = $('#tbConfig').datagrid('getRows')[index];
    if (row) {
        $.messager.confirm('确认', '确认删除此配置项吗?',
            function (r)
            {
                if (r)
                {
                    $.post('/delConfigItem.do',{id:row.id},
                        function (json)
                        {
                            if (json.result == 0) {
                                reloadConfigItem();
                            } else {
                                tipMsg("错误", json.msg);
                            }
                        },
                        'json');
                }
            });
    }
}

//在zookeeper上查看配置项值
function queryConfigItemOnZK(index)
{
    var row = $('#tbConfig').datagrid('getRows')[index];
    if (row) {

        var configItemId =row.id;

        $.ajax({
            type: "get",
            url: "/queryConfigItemOnZooKeeper.do",
            data:{id:configItemId},
            dataType:'json',
            success: function (json) {

                if (json.result == 0) {
                    $('#zk_value').html("");
                    $('#zk_value').html(json.msg);
                    $('#zk_dlg').dialog('open').dialog('setTitle', 'ZK值');
                } else {
                    tipMsg("错误", json.msg);
                }
            }
        });
    }
}

//同步app
function synApp2()
{
    var appName = $("#name").val();
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