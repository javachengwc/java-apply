
//初始化
$(function () {

    $('#tbProduct').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        url:'/shop/product/productList.do'
    });

});

//重载商品
function reloadProduct(){
    $('#tbProduct').datagrid('reload');
}

//查询
function search() {
    var name =$("#name").val();
    var productId =$("#productId").val();
    var shopId =$("#shopId").val();
    var data ={name:name,productId:productId,shopId:shopId};
    $('#tbProduct').datagrid('reload',data);
}