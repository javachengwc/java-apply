
var selectUserId;

//初始化
$(function () {

    $('#tbUser').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        url:'/rbac/queryUserPage.do'
    });

});

//重载用户
function reloadUser(){
    $('#tbUser').datagrid('reload');
}

//查询
function search() {
    var name =$("#name").val();
    var nameCh =$("#nameCh").val();
    var data ={name:name,nameCh:nameCh};
    $('#tbUser').datagrid('reload',data);
}

//渲染操作按钮
function formatOpt(value, row, index) {

    var userId = row.id;
    var useable = row.useable;
    var html='';
    if(1==useable)
    {
        html += '<a href="javascript:void(0);"  onclick="uptUserUseable('+userId+',0)">禁用</a>';
    }else
    {
        html += '<a href="javascript:void(0);"  onclick="uptUserUseable('+userId+',1)">启用</a>';
    }
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="openAuthUser('+userId+')">授权</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="editUser('+index+')">编辑</a>';
    html += ' | ';
    html += '<a href="javascript:void(0);"  onclick="delUser('+index+')">删除</a>';

    return html;
}

//渲染状态
function formatUseable(value,row,index)
{
    var defVal="禁用";
    if(1==value)
    {
        return "可用";
    }
    return defVal;
}

//打开增改用户栏
function openUserDlg()
{
    $('#user_form').form('clear');
    $('#user_add').dialog('open').dialog('setTitle', '新增用户');
}

//增改用户
function addOrUptUser()
{
    var options = {
        url: '/rbac/addOrUptUser.do',
        type: 'post',
        dataType: 'json',
        success: function (json) {
            if (json.result == 0) {
                $('#user_add').dialog('close');
                reloadUser();
            } else {
                tipMsg("错误", json.msg);
            }
        }
    };

    $("#user_form").ajaxSubmit(options);
}

//编辑用户
function editUser(index)
{
    var row = $('#tbUser').datagrid('getRows')[index];
    if (row) {
        $('#user_add').dialog('open').dialog('setTitle', '编辑用户');
        $('#user_form').form('load', row);
    } else {
        tipNoSelect();
    }
}

//禁用或启用用户
function uptUserUseable(id,flag)
{
    var msg='确认禁用此用户吗?';
    if(flag==1)
    {
        msg='确认启用此用户吗?'
    }

    $.messager.confirm('确认', msg,
    function (r)
    {
        if (r)
        {
            $.post('/rbac/useableOrNoUser.do',{id:id,flag:flag},
                function (json)
                {
                    if (json.result == 0) {
                        reloadUser();
                    } else {
                        tipMsg("错误", json.msg);
                    }
                },
                'json');
        }
    });
}

//删除用户
function delUser(index)
{
    var row = $('#tbUser').datagrid('getRows')[index];
    if (row) {
        $.messager.confirm('确认', '确认删除此用户吗?',
        function (r)
        {
            if (r)
            {
                $.post('/rbac/delUser.do',{id:row.id},
                    function (json)
                    {
                        if (json.result == 0) {
                            reloadUser();
                        } else {
                            tipMsg("错误", json.msg);
                        }
                    },
                    'json');
            }
        });
    }
}

//打开授权对话框
function openAuthUser(userId)
{
    selectUserId=userId;
    var _url='/rbac/selectRole.do?userId=' + userId;
    $('#select_role_dlg').dialog('open');
    $('#select_role_dlg').dialog('refresh', _url);
}

//授权
function authUser()
{
    var userId=$("#userId").val();
    var roleIds = new Array();
    $('input:checkbox[name=selectRole]:checked').each(function (i) {
        roleIds[i] = $(this).val();
    });
    $.post('/rbac/authUser.do',
        {userId: userId, roleIds: roleIds.toString()},
        function (json) {
            if (json.result == 0) {
                $('#select_role_dlg').dialog('close');
                tipMsg("成功", "操作成功");
            } else {
                tipMsg("错误", json.msg);
            }
        }, 'json');
}