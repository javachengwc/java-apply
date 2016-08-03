var editor = '';
var publicUuid ='';
var publicPath = '';
$(document).ready(function(){
    editor = CKEDITOR.appendTo( 'editor', {}, '' );
    showNewNotice();
    });

//显示发布公告的页面
function showNewNotice(){
    $(".manageTips").empty();
    $(".tips").empty();
    $(".address").empty();

    var operatorName = $('div.navi').find('span#userName').text();

    $(".noticeName").attr('value' ,'');
    $(".operator").attr('value' ,operatorName);
    $(".noticeSide").show();
    $(".listSide").hide();
    $(".manageSide").hide();
    $(".inputContent").show();

    //按钮
    $(".submit").show();
    $("#updateManage").hide();
    $(".updateSingle").hide();

    editor.setData();
    }

//编辑公告列表的响应函数  类型： 运营规则>>直播间 、运营规则>>星级公会、  违规处罚 、应用发放、节日活动
function beginEdit(obj){
    $.ajax({
        url : 'loadManageHtmlFile.action',
        type : 'get',
        data : {'fileName' : obj},
        dataType : 'json',
        success : function(json){
            if (json.result == 0) {
                $(".tips").empty();
                $(".address").empty();

                $(".noticeSide").show();
                $(".listSide").hide();
                $(".manageSide").hide();
                editor.setData(json.document);
                $(".inputContent").hide();

                var buttonValue = '';
                var buttonClass ='';

                //按钮
                $(".submit").hide();
                if(obj == 'liveRoomList' ){
                    buttonValue = '更新运营规则>>直播间';
                    buttonClass = 'updateLiveRoom';
                }
                else if(obj == 'starChannelList' ){
                    buttonValue = '更新运营规则>>星级公会';
                    buttonClass = 'updateStarChannel';
                }
                else if(obj == 'punishList'){
                    buttonValue = '更新违规处罚';
                    buttonClass = 'upadatePunishment';
                }else if(obj == 'appList'){
                    buttonValue = '更新应用发放';
                    buttonClass = 'updateAPP';
                }else if(obj == 'functionList'){
                    buttonValue = '更新节日活动';
                    buttonClass = 'updateFunction';
                }else{
                    return;
                }

                $("#updateManage").attr('value' , buttonValue);
                $("#updateManage").attr('class' , buttonClass);

                $("#updateManage").show();
                $(".updateSingle").hide();
            }else{
                if(json.msg ==1){
                    $(".tips").html('<font size="3" color="red"><b>错误：后台公告管理的Html文件不存在</b></font>');
                }else{
                    $(".tips").html('<font size="3" color="red"><b>错误：服务器读写错误</b></font>');
                }
            }
        }
    });
    }


//提交按钮的响应函数
function clickOn(flag,obj){
    var url = '';
    var b ='';
    var className = $("#updateManage").attr('class');
    var text = editor.document.getBody().getText();
    var noticeName = $(".noticeName").val();
    var operator = $(".operator").val();
    if(flag == 1){
    url = 'addNewNotice.action';
    b = !(text == '' || operator =='' || noticeName =='') ;
    }else if(flag ==2){
    url = 'updateManageHtmlFile.action';
    b = (text != '');
    }else{
    url = 'updateSingleNotice.action';
    b = !(text == '' || operator =='' || noticeName =='') ;
    }
    $(".tips").empty();
    $(".address").empty();
    var data = editor.getData();
    if(b){
    $.ajax({
    url: url,
    type: 'post',
    dataType: 'json',
    data: {
    'data': data ,
    'operator' : operator,
    'noticeName' : noticeName,
    'path' : publicPath,
    'uuid' : publicUuid,
    'className':className
    },
    success: function(json){
    if(json.result == 0 && json.url != ''){
    $(".tips").html('<font size="3" color="blue"><b>发布公告成功！<b></font>');
    if(flag != 2)$(".address").html('<b>新公告的URL为      :    '+ '<br/><br/>' + json.url + '</b>');
    editor.setData();
    $(".noticeName").attr('value' , '');
    //$(".operator").attr('value' , '');
    }else{
    if(json.msg ==1){
    $(".tips").html('<font size="3" color="red"><b>错误：用户输入为空</b></font>')
    }else if(json.msg == 2){
    $(".tips").html('<font size="3" color="red"><b>错误：服务器读写错误</b></font>')
    }else{
    $(".tips").html('<font size="3" color="red"><b>错误：后台公告管理的Html文件不存在</b></font>')
    }
    }
    }
    });
    }else{
    $(".tips").html('<font size="3" color="red"><b>内容不能为空，请重新输入！</b></font>')
    }
    }

//显示公告管理页面
function showManageNotice(){
    $(".manageSide").show();
    $(".tips").empty();
    $(".manageTips").empty();
    $(".noticeSide").hide();
    $(".listSide").hide();
    $(".inputContent").show();

    $.ajax({
    url: "getNoticeList.action",
    type: 'get',
    dataType: 'json',
    success: function(json){
    $('.tbody').empty();// 清空 列表
    var html = '';
    $.each(json, function(i, n){
    var single = json[i];

    var noticeName = single['noticeName'];
    var time = single.time;
    var operator = single.operator;
    var url = single.url;
    html += '<tr height="50">';
    html +='<td  class="noticeName" width="25%">'+ noticeName+ '</td>';
    html +='<td class="url" width="30%">'+ url +'</td>';
    html +='<td class="time" width="10%">'+ time +'</td>';
    html +='<td class="operator" width="10%">'+ operator +'</td>';
    html +='<td width="10%">'+ '<a onclick="loadSingleNotice(this)" href="javascript:void(0)"><font color="blue">编辑</font></a>&nbsp;&nbsp;&nbsp;<a onclick="deleteNotice(this)"  href="javascript:void(0)"><font color="blue">删除</font></a>' + '</td>'
    html +='<td  class="uuid" style="display: none;">'+ i+ '</td>';
    html += '</tr>';
    });

    $(".tbody").append(html);
    }
    });
    }

//加载某个公告到Ckeditor
function loadSingleNotice(obj){
    var path = $(obj).parent().parent().find("td.url").text().trim();
    var noticeName = $(obj).parent().parent().find("td.noticeName").text().trim();
    var operator = $(obj).parent().parent().find("td.operator").text().trim();
    var uuid = $(obj).parent().parent().find("td.uuid").text().trim();

    $.ajax({
    url: "loadSingleNotice.action",
    type: 'post',
    data :{'path' : path},
    dataType: 'json',
    success: function(json){
    if (json.result == 0) {
    $(".tips").empty();
    $(".address").empty();
    $(".noticeSide").show();
    $(".listSide").hide();
    $(".manageSide").hide();
    editor.setData(json.document);
    $(".noticeName").attr("value",noticeName);
    $(".operator").attr("value",operator);

    //按钮
    $("#updateManage").hide();
    $(".updateSingle").show();
    $(".submit").hide();

    publicPath = path;
    publicUuid = uuid;
    }else{
    if(json.msg ==1){
    $(".tips").html('<font size="3" color="red"><b>错误：服务器错误</b></font>')
    }else{
    $(".tips").html('<font size="3" color="red"><b>错误：读取文件失败</b></font>')
    }
    }
    }
    });
    }

//删除某个公告
function deleteNotice(obj){
    var path = $(obj).parent().parent().find("td.url").text().trim();
    var uuid = $(obj).parent().parent().find("td.uuid").text().trim();

    $.ajax({
    url: "deleteNotice.action",
    type: 'post',
    data :{'uuid' : uuid ,
    'path' : path
    },
    dataType: 'json',
    success: function(json){
    if(json.result == 0){
    $(".manageTips").html('<font size="3" color="blue"><b>删除成功</b></font>');
    showManageNotice();
    }else{
    $(".manageTips").html('<font size="3" color="red"><b>错误：服务器错误</b></font>');
    }
    }
    });
    }