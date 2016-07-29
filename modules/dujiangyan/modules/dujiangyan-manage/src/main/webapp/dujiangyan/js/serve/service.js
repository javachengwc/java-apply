$(function () {
    $("#serviceTable").datagrid({
        onSelect: function (index, dimen) {
            var dimenName =dimen.name;
            $("#serviceProvider").panel({title: '服务 [' + dimenName + '] 的信息'});
            $("#serviceProviderIframe").attr("src", '/djy/serve/provider.do?assignApp=' + dimenName);

            $("#serviceConsumer").panel({title: '调用了服务 [' +dimenName + '] 的消费列表'});
            $("#serviceConsumerIframe").attr("src", '/djy/serve/consumer.do?assignApp=' + dimenName);
        }
    }).datagrid('enableFilter');
});