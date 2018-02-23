
//初始化
$(function () {

    $("#payTimeBegin").datebox();
    $("#payTimeEnd").datebox();
    $("#deliverTimeBegin").datebox();
    $("#deliverTimeEnd").datebox();
    $("#createTimeBegin").datebox();
    $("#createTimeEnd").datebox();

    initQuery();

    var createTimeBegin = $('#createTimeBegin').datebox("getValue");
    var createTimeEnd = $('#createTimeEnd').datebox("getValue");

    $('#tbShopOrder').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        queryParams:{createTimeBegin:createTimeBegin,createTimeEnd:createTimeEnd},
        url:'/shop/order/queryOrderPage.do'
    });

});

function initQuery()
{
    $("#createTimeBegin").datebox("setValue", intDate(-1));
    $("#createTimeEnd").datebox("setValue", intDate(0));
}

//重载订单
function reloadShopOrder(){
    $('#tbShopOrder').datagrid('reload');
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
    $('#tbShopOrder').datagrid('reload',data);
}