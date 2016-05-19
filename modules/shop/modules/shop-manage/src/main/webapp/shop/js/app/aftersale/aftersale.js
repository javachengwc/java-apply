
//初始化
$(function () {

    $("#applyTimeBegin").datebox();
    $("#applyTimeEnd").datebox();

    initQuery();

    var applyTimeBegin = $('#applyTimeBegin').datebox("getValue");
    var applyTimeEnd = $('#applyTimeEnd').datebox("getValue");

    $('#tbAftersale').datagrid({
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
        url:'/shop/aftersale/aftersale.do'
    });

});

function initQuery()
{
    $("#applyTimeBegin").datebox("setValue", intDate(-1));
    $("#applyTimeEnd").datebox("setValue", intDate(0));
}

//重载售后
function reloadAftersale(){
    $('#tbAftersale').datagrid('reload');
}

//查询
function search() {

    var id =$("#id").val();
    var fromSource =$("#fromSource").val();
    var orderId =$("#orderId").val();
    var productId =$("#productId").val();
    var userId =$("#userId").val();
    var userName =$("#userName").val();
    var shopId =$("#shopId").val();
    var shopName =$("#shopName").val();
    var applyTimeBegin = $('#applyTimeBegin').datebox("getValue");
    var applyTimeEnd = $('#applyTimeEnd').datebox("getValue");
    var state =$("#state").val();
    var type =$("#type").val();

    var data ={id:id,fromSource:fromSource,orderId:orderId,productId:productId,
        userId:userId,userName:userName,shopId:shopId,shopName:shopName,
        applyTimeBegin:applyTimeBegin,applyTimeEnd:applyTimeEnd,state:state,type:type};
    $('#tbAftersale').datagrid('reload',data);
}