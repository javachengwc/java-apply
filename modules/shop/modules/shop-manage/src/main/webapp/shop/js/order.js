
//初始化
$(function () {

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
        url:'/shop/order/orderList.do'
    });

});

//重载商品
function reloadOrder(){
    $('#tbOrder').datagrid('reload');
}

//查询
function search() {
    var name =$("#name").val();
    var productId =$("#productId").val();
    var sellerId =$("#sellerId").val();
    var data ={name:name,productId:productId,sellerId:sellerId};
    $('#tbOrder').datagrid('reload',data);
}