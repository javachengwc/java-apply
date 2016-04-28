
//初始化
$(function () {

    $("#beginTimeBegin").datebox();
    $("#beginTimeEnd").datebox();
    $("#endTimeBegin").datebox();
    $("#endTimeEnd").datebox();
    $("#createTimeBegin").datebox();
    $("#createTimeEnd").datebox();

    initQuery();

    var createTimeBegin = $('#createTimeBegin').datebox("getValue");
    var createTimeEnd = $('#createTimeEnd').datebox("getValue");

    $('#tbShopDiscount').datagrid({
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
        url:'/shop/shop/shopDiscountList.do'
    });

});

function initQuery()
{
    $("#createTimeBegin").datebox("setValue", intDate(-1));
    $("#createTimeEnd").datebox("setValue", intDate(0));
}

//重载数据
function reloadShopDiscount(){
    $('#tbShopDiscount').datagrid('reload');
}

//查询
function search() {

    var createTimeBegin = $('#createTimeBegin').datebox("getValue");
    var createTimeEnd = $('#createTimeEnd').datebox("getValue");
    var beginTimeBegin = $('#beginTimeBegin').datebox("getValue");
    var beginTimeEnd = $('#beginTimeEnd').datebox("getValue");
    var endTimeBegin = $('#endTimeBegin').datebox("getValue");
    var endTimeEnd = $('#endTimeEnd').datebox("getValue");

    var shopId =$("#shopId").val();
    var discountName =$("#discountName").val();
    var discountState =$("#discountState").val();
    var discountType =$("#discountType").val();
    var discountRange =$("#discountRange").val();

    var data ={shopId:shopId,discountName:discountName,discountState:discountState,discountType:discountType,discountRange:discountRange,
        createTimeBegin:createTimeBegin,createTimeEnd:createTimeEnd,beginTimeBegin:beginTimeBegin,beginTimeEnd:beginTimeEnd,
        endTimeBegin:endTimeBegin,endTimeEnd:endTimeEnd};

    $('#tbShopDiscount').datagrid('reload',data);
}