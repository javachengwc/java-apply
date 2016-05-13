
//初始化
$(function () {

    $("#payTimeBegin").datebox();
    $("#payTimeEnd").datebox();
    $("#deliverTimeBegin").datebox();
    $("#deliverTimeEnd").datebox();
    $("#createTimeBegin").datebox();
    $("#createTimeEnd").datebox();

    initQuery();

    var payTimeBegin = $('#payTimeBegin').datebox("getValue");
    var payTimeEnd = $('#payTimeEnd').datebox("getValue");

    $('#tbOrder').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        queryParams:{payTimeBegin:payTimeBegin,payTimeEnd:payTimeEnd},
        url:'/shop/order/orderList.do'
    });

});

function initQuery()
{
    $("#payTimeBegin").datebox("setValue", intDate(-1));
    $("#payTimeEnd").datebox("setValue", intDate(0));
}

//重载商品
function reloadOrder(){
    $('#tbOrder').datagrid('reload');
}

//查询
function search() {

    var orderId =$("#orderId").val();
    var fromSource =$("#fromSource").val();
    var orderState =$("#orderState").val();
    var isOverbuy =$("#isOverbuy").val();
    var isCancel =$("#isCancel").val();
    var userId =$("#userId").val();
    var userName =$("#userName").val();
    var shopId =$("#shopId").val();
    var shopName =$("#shopName").val();
    var payTimeBegin = $('#payTimeBegin').datebox("getValue");
    var payTimeEnd = $('#payTimeEnd').datebox("getValue");
    var deliverTimeBegin = $('#deliverTimeBegin').datebox("getValue");
    var deliverTimeEnd = $('#deliverTimeEnd').datebox("getValue");
    var createTimeBegin = $('#createTimeBegin').datebox("getValue");
    var createTimeEnd = $('#createTimeEnd').datebox("getValue");

    var data ={orderId:orderId,fromSource:fromSource,orderState:orderState,isOverbuy:isOverbuy,isCancel:isCancel,
        userId:userId,userName:userName,shopId:shopId,shopName:shopName,payTimeBegin:payTimeBegin,
        payTimeEnd:payTimeEnd,deliverTimeBegin:deliverTimeBegin,deliverTimeEnd:deliverTimeEnd,createTimeBegin:createTimeBegin,createTimeEnd:createTimeEnd};
    $('#tbOrder').datagrid('reload',data);
}