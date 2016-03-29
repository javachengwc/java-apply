//编辑的资源id
var editRsId;

//初始加载treegrid
$(function () {

    $('#tbResource').treegrid({
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        showFooter:true,
        toolbar:'#toolbar',
        iconCls: 'icon-ok',
        idField: 'id',
        treeField: 'name',
        animate: true,
        method:'get',
        url:'/rbac/queryTreeGridResource.do'
    });

});

//重载资源
function reloadRs(){
    $('#tbResource').treegrid('reload', {});
}

//打开新增资源栏
function openRsDlg()
{
    var row =  $('#tbResource').treegrid('getSelected');
    var parentId=0;
    var parentName="无";
    if(row && row.id>0)
    {
        parentId=row.id;
        parentName= row.name
    }
    $('#rs_form').form('clear');

    $('#rsParentId').textbox('readonly', false);
    $('#rsParentId').textbox('setValue',parentId);
    $('#rsParentId').textbox('setText',parentName);
    $('#rsParentId').textbox('readonly', true);

    $('#rs_add').dialog('open').dialog('setTitle', '新增资源');
}

//增加资源
function addRs()
{
    var options = {
        url: '/rbac/addResource.do',
        type: 'post',
        dataType: 'json',
        success: function (json) {
            if (json.result == 0) {
                $('#rs_add').dialog('close');
                reloadRs();
            } else {
                tipMsg("错误", json.msg);
            }
        }
    };

    $("#rs_form").ajaxSubmit(options);

}

//编辑资源
function editRs()
{
    var row = $('#tbResource').treegrid('getSelected');
    if(!row){
        tipNoSelectRs();
        return ;
    }
    if (row && row.id != undefined && row.id > 0){
        editRsId=row.id;
        $('#tbResource').treegrid('beginEdit', editRsId);
    }
}

//取消编辑资源
function cancelRs()
{
    if (editRsId != undefined){
        $('#tbResource').treegrid('cancelEdit', editRsId);
        editRsId = undefined;
    }
}

//修改资源
function uptRs()
{
    if(editRsId == undefined || editRsId== null ){
        tipMsg("提示","无修改的资源");
        return;
    }else
    {
        $('#tbResource').treegrid('endEdit', editRsId);
        $('#tbResource').treegrid('selectRow',editRsId);
        var row = $('#tbResource').treegrid('getSelected');
        editRsId = undefined;

        $.post('/rbac/uptResource.do',
            {
                id:row.id,
                name:row.name,
                path:row.path,
                isShow:row.isShow,
                isMenu:row.isMenu,
                parentId:row._parentId,
                tag:row.tag
            }, function (data) {
                if (data.result ==0) {
                    reloadRs();
                }else {
                    tipMsg("错误", data.msg);
                }
            }, 'json');
    }
}

//删除资源
function delRs()
{
    var row = $('#tbResource').treegrid('getSelected');

    if(!row){
        tipNoSelectRs();
        return ;
    }

    if(row.children && row.children.length>0){
        tipMsg("错误","此节点还有子节点, 不能删除");
        return;
    }

    $.messager.confirm('确认', '确定删除此资源吗?', function (r) {
        if (r) {
            $.post('/rbac/delResource.do',{id:row.id},function (data) {
                if (data.result == 0) {
                    reloadRs();
                } else
                {
                    tipMsg("错误", data.msg);
                }
            }, 'json');
        }
    });
}

//格式化数据
function formatIsMenu(value, row, index) {

    var defVal="否";
    if(1==value)
    {
        return "是";
    }
    return defVal;
}


function formatIsShow(value, row, index) {

    var defVal="不显示";
    if(1==value)
    {
        return "显示";
    }
    return defVal;
}

//提示没选中
function tipNoSelectRs()
{
    tipMsg("提示","没有选中任意资源");
}