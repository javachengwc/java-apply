
//初始化
$(function () {

    $("#statDateBegin").datebox();
    $("#statDateEnd").datebox();

    initQuery();

    var statDateBegin = $('#statDateBegin').datebox("getValue");
    var statDateEnd = $('#statDateEnd').datebox("getValue");

    $('#tbSafeguardStat').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        onLoadSuccess:showWeekDay,
        queryParams:{statDateBegin:statDateBegin,statDateEnd:statDateEnd},
        url:'/shopstat/aftersale/safeguardStatList.do'
    });

});

function initQuery()
{
    $("#statDateBegin").datebox("setValue", intDate(-7));
    $("#statDateEnd").datebox("setValue", intDate(-1));
}

//重载维权统计
function reloadSafeguardStat(){
    $('#tbSafeguardStat').datagrid('reload');
}

//查询
function search() {

    var statDateBegin = $('#statDateBegin').datebox("getValue");
    var statDateEnd = $('#statDateEnd').datebox("getValue");
    var fromSource =$("#fromSource").val();
    var ext1 =$("#ext1").val();

    var tagId=$("#tagId").val();
    var subId=$("#subId").val();
    var thirdId=$("#thirdId").val();

    var data ={statDateBegin:statDateBegin,statDateEnd:statDateEnd,fromSource:fromSource,ext1:ext1,
        tagId:tagId,subId:subId,thirdId:thirdId};
    $('#tbSafeguardStat').datagrid('reload',data);
}

function showWeekDay()
{
    var rows = $("#tbSafeguardStat").datagrid("getRows");
    for (var i = 0; i < rows.length; i++) {
        var stat = rows[i]['statDate'];
        rows[i]['weekDay'] = formatWeekDay(stat);
        $("#tbSafeguardStat").datagrid("updateRow", { index: i});
    }
}

