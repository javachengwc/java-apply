$(function () {
    //加载菜单
    $.ajax({
        type: "get",
        url: "/djy/menuList.do",
        success: function (rt) {

            if(rt.result==null || rt.result!=0)
            {
                return;
            }
            var list =rt.list;
            $('#menuTree').tree({
                data:list,
                onClick: function (node) {
                    if(node.isParent==1)
                    {
                        $("#menuTree").tree('expand', node.target);
                        return;
                    }
                    createTabPanel(node.id,node.text,node.url);
                }
            });
        }
    });

});

function createTabPanel(nid,title,href) {

    var iframeName = "iframe_"+nid;
    var tab = $('#tabs').tabs('exists', title);
    if (tab) {
        //之前已打开过此面板，只需要把此面板当前显示即可
        $("#tabs").tabs('select', title);
        return;
    }
    if (href != null && href.trim() != '') {
        var content = '<iframe id="' + iframeName + '" name="' + iframeName + '" src="' + href + '"  width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>';
        $('#tabs').tabs('add', {
            title: title,
            closable: true,
            cache: false,
            content: content,
            tools: [
                {
                    iconCls: 'icon-reload',
                    handler: function () {
                        $("#" + iframeName).attr('src', href);
                    }
                }
            ]
        });
    } else {
        alert("菜单地址为空");
    }
}

function createLinkTabPanel(title, href) {

    var iframeName = "link_iframe_";
    var tab = $("#tabs").tabs('getTab', title);

    if (tab) {
        $("#tabs").tabs('select', title);
        iframeName += $("#tabs").tabs('getTabIndex', tab);
        $("#" + iframeName).attr('src', href);
        return;
    }

    iframeName += $("#tabs").tabs('tabs').length.toString();//相当于下标，第一个的下标为0
    var content = '<iframe id="' + iframeName + '" name="' + iframeName + '" src="' + href + '" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>';
    $('#tabs').tabs('add', {
        title: title,
        closable: true,
        cache: false,
        content: content
    });
}
