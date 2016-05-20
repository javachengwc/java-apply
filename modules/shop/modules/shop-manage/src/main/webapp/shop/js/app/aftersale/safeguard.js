
//初始化
$(function () {

    $("#applyTimeBegin").datebox();
    $("#applyTimeEnd").datebox();

    initQuery();

    var applyTimeBegin = $('#applyTimeBegin').datebox("getValue");
    var applyTimeEnd = $('#applyTimeEnd').datebox("getValue");

    $('#tbSafeguard').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        queryParams:{applyTimeBegin:applyTimeBegin,applyTimeEnd:applyTimeEnd},
        url:'/shop/aftersale/safeguardList.do'
    });

});

function initQuery()
{
    $("#applyTimeBegin").datebox("setValue", intDate(-1));
    $("#applyTimeEnd").datebox("setValue", intDate(0));
}

//重载维权
function reloadSafeguard(){
    $('#tbSafeguard').datagrid('reload');
}

//查询
function search() {

    var id =$("#id").val();
    var fromSource =$("#fromSource").val();
    var orderId =$("#orderId").val();
    var userId =$("#userId").val();
    var userName =$("#userName").val();
    var shopId =$("#shopId").val();
    var shopName =$("#shopName").val();
    var applyTimeBegin = $('#applyTimeBegin').datebox("getValue");
    var applyTimeEnd = $('#applyTimeEnd').datebox("getValue");
    var state =$("#state").val();
    var aftersaleId=$("#aftersaleId").val();
    var safeguardStarter=$("#safeguardStarter").val();

    var data ={id:id,fromSource:fromSource,orderId:orderId,aftersaleId:aftersaleId,
        userId:userId,userName:userName,shopId:shopId,shopName:shopName,
        applyTimeBegin:applyTimeBegin,applyTimeEnd:applyTimeEnd,state:state,safeguardStarter:safeguardStarter};
    $('#tbSafeguard').datagrid('reload',data);
}