/**
 * 弹窗相关
 */

//提示信息
function tipMsg(title,msg)
{
    $.messager.show({
        title: title,
        msg: msg,
        showType: 'fade',
        style: {
            right: '',
            bottom: ''
        }
    });
}

//提示没选中行
function tipNoSelect()
{
    tipMsg("提示","没有选中任意行");
}

