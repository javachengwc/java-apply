$(function () {
    $("#machineTable").datagrid({
        onSelect: function (index, dimen) {
            var dimenName =dimen.name;
            $("#machineProvider").panel({title: '主机 [' + dimenName + '] 提供的服务'});
            $("#machineProviderIframe").attr("src", '/mountain/serve/provider.do?assignValue=' + dimenName+"&dimenType=machine");

            $("#machineConsumer").panel({title: '调用了主机 [' +dimenName + '] 提供的服务的消费列表'});
            $("#machineConsumerIframe").attr("src", '/mountain/serve/consumer.do?assignValue=' + dimenName+"&dimenType=machine");
        }
    }).datagrid('enableFilter');
});