$(function () {
    $("#machineTable").datagrid({
        onSelect: function (index, dimen) {
            var dimenName =dimen.name;
            $("#machineProvider").panel({title: '主机 [' + dimenName + '] 提供的服务'});
            $("#machineProviderIframe").attr("src", '/djy/serve/provider.do?assignApp=' + dimenName);

            $("#machineConsumer").panel({title: '调用了主机 [' +dimenName + '] 提供的服务的消费列表'});
            $("#machineConsumerIframe").attr("src", '/djy/serve/consumer.do?assignApp=' + dimenName);
        }
    }).datagrid('enableFilter');
});