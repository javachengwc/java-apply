$(function () {

    var username =$.cookie('username');
    if(username==null)
    {
        username="";
    }
    username =decodeURIComponent(username).replace(/\"/g, "");
    $("#userinfo").text(username);

    //加载菜单
    $.ajax({
        type: "get",
        url: "/menuList.do",
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

//退出登陆
function logout() {
    $.messager.confirm('确认', '你确定要退出系统吗?', function (r) {
        if (r) {
            //同步调用
            $.ajax({
                type: "get",
                url:'/logout.do',
                async:false
            });
            var index = window.location.href.lastIndexOf('/');
            var url = window.location.href;
            url = url.substr(0, index);
            window.location.href = url;
        }
    });
}

//重置密码
function resetPwd() {
    $('#resetPwdForm').form('clear');
}

//修改密码
function changePwd() {

    var pwdOld= $("#pwdOld").val();
    var pwdNew = $("#pwdNew").val();
    var pwdConfirm = $("#pwdConfirm").val();

    if(pwdOld==null || pwdOld.trim()=="" || pwdNew==null || pwdNew.trim()=="" )
    {
        tipMsg("提示","新旧密码不能为空");
        return ;
    }

    if (pwdNew != pwdConfirm ) {
        tipMsg("提示","新密码与确认密码不一致");
        return;
    }

    if(pwdOld== pwdNew)
    {
        tipMsg("提示","新旧密码不能一样");
        return;
    }

    $.messager.confirm('确认', '确认修改密码吗?',
        function (r)
        {
            if (r)
            {
                var options = {
                    url: '/changePwd.do',
                    type: 'post',
                    dataType: 'json',
                    success: function (json) {
                        if (json.result == 0) {
                            $('#pwdDlg').dialog('close');
                            tipMsg("提示","操作成功");
                        } else {
                            tipMsg("错误", json.msg);
                        }
                    }
                };

                $("#resetPwdForm").ajaxSubmit(options);

            }
        });

}