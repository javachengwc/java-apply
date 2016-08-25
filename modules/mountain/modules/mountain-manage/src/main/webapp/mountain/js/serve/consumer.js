
$(function () {

    var dimenType =$.url.param("dimenType");
    var assignValue =$.url.param("assignValue");
    $("#dimenType").val(dimenType);
    $("#assignValue").val(assignValue);

    //加载数据
    $('#tbConsumer').datagrid({
        pageSize:20,
        fit:true,   //自动大小,有固定表头作用
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbarConsumer',
        method:'get',
        defaultFilterType:'text',
        checkOnSelect:false,
        selectOnCheck:false,
        queryParams:{dimenType:dimenType,assignValue:assignValue},
        url:'/mountain/serve/queryConsumerPage.do',
        view: detailview,
        detailFormatter: function (rowIndex, rowData) {
            var info = '<table style="border: 0;font-size: 12;line-height:150%">';
            info += '<tr><td width="100px" style="border:0">消费地址:</td><td style="border:0">'+rowData.url+ '</td></tr>';
            info += '<tr><td style="border:0">进程号:<td style="border:0">' + rowData.pid + '</td></tr></table>';
            return info;
        }
    });
});

function reloadConsumer()
{
    $('#tbConsumer').datagrid('reload');
}

function search() {

    var dimenType =$("#dimenType").val();
    var assignValue =$("#assignValue").val();
    var queryValue =$("#queryValue").val();
    var state= $("input[name='state']:checked").val();

    var data ={dimenType:dimenType,assignValue:assignValue,queryValue:queryValue,state:state};

    $('#tbConsumer').datagrid('reload',data);
}

function formatStatus(value, row, index) {
    if (value == true) {
        return "启用";
    } else {
        return '<span style="color:red;">屏蔽</span>';
    }
}

function formatButton(value, row, index) {
    var str = "";
    if (row.useable == true) {
        str += '<a href="javascript:void(0);"  onclick="singleDo(' + row.id + ',2)">屏蔽</a>';
    } else {
        str += '<a href="javascript:void(0);"  onclick="singleDo(' + row.id + ',1)">启用</a>';
    }
    return str;
}

function formatInvokeStat(value,row,index)
{
    var str = "";
    str += (row.invokeSuccessCnt == null) ? 0 : row.invokeSuccessCnt;
    str += "/";
    var failCnt=(row.invokeFailCnt == null) ? 0 : row.invokeFailCnt;
    if (failCnt > 0) {
        str += '<span style="color:red;">' +failCnt+ '</span>';
    } else {
        str += failCnt;
    }

    str += "<br>";
    str += (row.invokeSucTotalCnt == null) ?0: row.invokeSucTotalCnt;
    str += "/";
    var failTotalCnt=(row.invokeFailTotalCnt == null) ? 0 : row.invokeFailTotalCnt;
    if (failTotalCnt > 0) {
        str += '<span style="color:red;">' +failTotalCnt+ '</span>';
    } else {
        str += failTotalCnt;
    }
    return str;
}

//批量操作
function batchDo(optFlag) {
    var strIds = getCheckedIds();
    if (!strIds) {
        return;
    }
    var mth=getUrlByOpt(optFlag,2);
    postUrl(mth,{ids: strIds});
}

//单条操作
function singleDo(id, optFlag) {
    var mth=getUrlByOpt(optFlag,1);
    postUrl(mth, {id: id});
}

function getUrlByOpt(optFlag,batchOrSingle)
{

}

function postUrl(url, data) {
    $.post(
        url,
        data,
        function (json) {
            if (json.result == 0) {
                reloadConsumer();
            } else {
                tipMsg("错误", json.msg);
            }
        },
        'json'
    );
}

function getCheckedIds() {

    var rows = $("#tbConsumer").datagrid("getChecked");
    if (rows == null || rows.length == 0) {
        tipNoSelect()
        return;
    }
    var ids = new Array(rows.length);
    for (var i = 0; i < rows.length; i++) {
        ids[i] = rows[i].id;
    }
    return ids.join();
}

function exportData() {

    var dimenType =$("#dimenType").val();
    var assignValue =$("#assignValue").val();
    var queryValue =$("#queryValue").val();
    var state= $("input[name='state']:checked").val();

    window.location.href = "/mountain/serve/exportConsumer.do?dimenType="+dimenType+ "&assignValue=" + assignValue+"&queryValue=" + queryValue+ "&state=" + state;

}