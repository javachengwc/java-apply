$(function () {
    $("#serviceTable").datagrid({
        onSelect: function (index, dimen) {
            var dimenName =dimen.name;
            $("#serviceProvider").panel({title: '服务 [' + dimenName + '] 的信息'});
            $("#serviceProviderIframe").attr("src", '/djy/serve/provider.do?assignValue=' + dimenName+"&dimenType=service");

            $("#serviceConsumer").panel({title: '调用了服务 [' +dimenName + '] 的消费列表'});
            $("#serviceConsumerIframe").attr("src", '/djy/serve/consumer.do?assignValue=' + dimenName+"&dimenType=service");
        }
    }).datagrid('enableFilter');
});