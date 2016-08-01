$(function () {
    $("#appTable").datagrid({
        onSelect: function (index, app) {
            var appName =app.name;
            $("#appProvider").panel({title: '应用 [' + appName + '] 提供的服务'});
            $("#appProviderIframe").attr("src", '/djy/serve/provider.do?assignValue=' + appName+"&dimenType=application");

            $("#appConsumer").panel({title: '调用了应用 [' +appName + '] 提供的服务的消费列表'});
            $("#appConsumerIframe").attr("src", '/djy/serve/consumer.do?assignValue=' + appName+"&dimenType=application");
        }
    }).datagrid('enableFilter');
});