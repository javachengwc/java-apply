//初始化
$(function () {

    $('#tbDb').datagrid({
        pageSize:20,
        rownumbers:true,
        fitColumns:true,
        collapsible:true,
        singleSelect:true,
        showFooter:true,
        pagination:true,
        toolbar:'#toolbar',
        method:'get',
        url:'/db/dbList.do'
    });

});

//重载
function reloadDb(){
    $('#tbDb').datagrid('reload');
}

//查询
function search() {

    var host =$("#host").val();
    var name =$("#name").val();
    var account =$("#account").val();

    var data ={host:host,name:name,account:account};
    $('#tbDb').datagrid('reload',data);
}