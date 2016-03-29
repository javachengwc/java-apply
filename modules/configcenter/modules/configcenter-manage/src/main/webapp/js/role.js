
var selectRoleId;

//初始化
$(function () {

    $('#tbRole').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        url:'/rbac/queryRolePage.do'
    });

});

//重载角色
function reloadRole(){
    $('#tbRole').datagrid('reload');
}

//查询
function search() {
    var name =$("#name").val();
    var nameCh =$("#nameCh").val();
    var data ={name:name,nameCh:nameCh};
    $('#tbRole').datagrid('reload',data);
}

//渲染操作按钮
function formatOpt(value, row, index) {

    var roleId = row.id;
    var html='';
    html += '<a href="javascript:void(0);"  onclick="openSelectRs('+roleId+')">关联资源</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="editRole('+index+')">编辑</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="delRole('+index+')">删除</a>';

    return html;
}

//打开增改角色栏
function openRoleDlg()
{
    $('#role_form').form('clear');
    $('#role_add').dialog('open').dialog('setTitle', '新增角色');
}

//增改角色
function addOrUptRole()
{
    var options = {
        url: '/rbac/addOrUptRole.do',
        type: 'post',
        dataType: 'json',
        success: function (json) {
            if (json.result == 0) {
                $('#role_add').dialog('close');
                reloadRole();
            } else {
                tipMsg("错误", json.msg);
            }
        }
    };

    $("#role_form").ajaxSubmit(options);
}

//编辑角色
function editRole(index)
{
    var row = $('#tbRole').datagrid('getRows')[index];
    if (row) {
        $('#role_add').dialog('open').dialog('setTitle', '编辑角色');
        $('#role_form').form('load', row);
    } else {
        tipNoSelect();
    }
}

//删除角色
function delRole(index)
{
    var row = $('#tbRole').datagrid('getRows')[index];
    if (row) {
        $.messager.confirm('确认', '确认删除此角色吗?',
            function (r)
            {
                if (r)
                {
                    $.post('/rbac/delRole.do',{id:row.id},
                        function (json)
                        {
                            if (json.result == 0) {
                                reloadRole();
                            } else {
                                tipMsg("错误", json.msg);
                            }
                        },
                        'json');
                }
            });
    }
}

//打开选择资源对话框
function openSelectRs(roleId)
{
    selectRoleId=roleId;
    var _url='/rbac/selectRs.do?roleId=' + roleId+"&_time="+new Date().getTime();
    $('#select_rs_dlg').dialog('open');
    $('#select_rs_dlg').dialog('refresh', _url);
}

//关联资源
function relaRs() {

    var roleId = selectRoleId;
    var selectNode = $("#resource_tree").tree('getChecked', ['checked', 'indeterminate']);
    var resourceIds = new Array();
    for (var i = 0; i < selectNode.length; i++) {
        resourceIds[i] = selectNode[i].id;
    }
    $.post('/rbac/roleRelaResource.do',
        {
            roleId: roleId,
            resourceIds: resourceIds.toString()
        }
        , function (json) {
            if (json.result == 0) {
                $('#select_rs_dlg').dialog('close');
                tipMsg("成功", "操作成功");
            } else {
                tipMsg("错误", json.msg);
            }
        }, 'json');
}