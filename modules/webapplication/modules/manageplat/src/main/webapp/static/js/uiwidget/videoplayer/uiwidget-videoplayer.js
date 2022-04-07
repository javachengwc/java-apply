 ;(function($){
 	$.uiwidget=$.uiwidget||{};//定义命名空间$.uiwidget
 	$.uiwidget.videoPlayer=function(target,cfg){
 		jQuery.extend(this,cfg);//this  指向命名空间
 		this.target=target;
 		this.render();
 		this.init();
 		this.buttonCtrol();
 		this.progressBarDrag();
 	}

 	$.uiwidget.videoPlayer.prototype={
 	    videoSrcStart:"../../../video/test1.mp4",
 	    videoSrcEnd:"../../../video/test1.mp4",
 	    videoSrcNext:"../../../video/test1.mp4",
 	    videoObject: null,
 	    videoWidth: "480px",
        videoHeight: "360px",
        videoCss: "jp-video-360p",
        videoSwfPath : '../../../swf/Jplayer.swf',
        dragable:true,


 		init: function(){
 			var t=this;
 			t.videoObject=$("#video-box").jPlayer({
				ready : function() {
	                $(this).jPlayer("setMedia", {
	                m4v : t.videoSrcStart
	                }).jPlayer("play");
                },
                play : function(e) {
                    $("#video-box").css({
                        cursor : "pointer"
                    });
                },
                pause : function(e) {
                    $("#video-box").css({
                        cursor : ''
                    });
                },
                ended : function(e) {
                        $(this).jPlayer("setMedia", {
                            m4v : t.videoSrcEnd
                        }).jPlayer('play');
                },
                size: {
                    width: t.videoWidth,
                    height: t.videoHeight,
                    cssClass: t.videoCss
                },
                supplied : "m4v",
                smoothPlayBar : true,
                keyEnabled : true,
                swfPath : t.videoSwfPath
 			});

			return t.videoObject;
 		},

 		/*渲染html*/
 		render:function(){
 			var t=this;
 			var s="<div id='jp_container_1' class='jp-video1'>"+
			    		"<div class='jp-type-single'>"+
						"<div class='video-box' id ='video-box'></div>"+
						"<div class='jp-gui' id='jp-gui2'>"+
							"<div class='jp-interface'>"+
								"<div class='jp-progress'>"+
								    "<div class='jp-seek-bar'>"+
				                    "<div class='jp-play-bar png'></div>"+
				                    "</div>"+
								"</div>"+
								"<span class='jp-current-time'></span><span class='jp-duration'></span>"+
								"<div id='video-bottom-con' class='video-bottom-con'>"+
				                "<!-- <a href='javascript:;' class='stop' title='停止'></a> -->"+
				                "<a href='javascript:;' class='play png' title='播放' style='display:none;'></a>"+
				                "<a href='javascript:;' class='pause png' title='暂停'></a>"+
				                "<a href='javascript:;' class='volume png' title='开启声音' style='display:none;'></a>"+
				                "<a href='javascript:;' class='mute png' title='静音'></a>"+
				                "</div>"+
				                "<div class='jp-volume-bar'>"+
									"<div class='jp-volume-bar-value'></div>"+
								"</div>"+
							"</div>"+
						"</div>"+
					"</div>";
			t.target.html(s);
 		},

 		//绑定控制键
 		buttonCtrol:function(){
 			var t=this;
 			var video=t.videoObject;
			var buttons = $('#video-bottom-con');
		    buttons.find('a.play').click(function(){
		        video.jPlayer('play');
		        buttons.find('a.pause').show();
		        $(this).hide();
		    });
		    buttons.find('a.stop').click(function(){
		        video.jPlayer('stop');
		        buttons.find('a.play').show();
		        buttons.find('a.pause').hide();
		    });
		    buttons.find('a.pause').click(function(){
		        video.jPlayer('pause');
		        buttons.find('a.play').show();
		        $(this).hide();
		    });
		    buttons.find('a.mute').click(function(){
		        video.jPlayer('mute');
		        buttons.find('a.volume').show();
		        $(this).hide();
		    });
		    buttons.find('a.volume').click(function(){
		        video.jPlayer('unmute');
		        buttons.find('a.mute').show();
		        $(this).hide();
		    });
		    $(".jp-volume-bar").click(function(){
		    	buttons.find('a.mute').show();
		    	buttons.find('a.volume').hide();
		    });
 		},


 		/*进度条拖拽*/
 		progressBarDrag:function(){
 			var t=this;
 			if(t.dragable==false){
 				return;
 			}
 			var bar=$(".jp-play-bar");
 			bar.mousedown(function(e){
 				if(e.which==1){
 					var offset=bar.offset();
 					var pageX=e.pageX;
 					console.log("offset.left:"+offset.left+"pageX:"+pageX);
 				}
 			});
 		},

 		/*暴露控制接口*/
 		stop:function(){
 			var t=this;
 			t.videoObject.jPlayer('stop');
 		},
 		play:function(){
 			var t=this;
 			t.videoObject.jPlayer('play');
 		},
 		pause:function(){
 			var t=this;
 			t.videoObject.jPlayer('pause');
 		},
 		mute:function(){
 			var t=this;
 			t.videoObject.jPlayer('mute');
 		},
 		unmute:function(){
 			var t=this;
 			t.videoObject.jPlayer('unmute');
 		},
		setVideoSrc:function(src){
 			var t=this;
 			var video=t.videoObject;
 			video.jPlayer("setMedia", {
                m4v : src
            }).jPlayer('play',0);
 		}
 	}

 	$.fn.videoplayer=function(cfg){
 		return new $.uiwidget.videoPlayer(this,cfg);
 	}
 })(jQuery);