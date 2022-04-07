/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.uiColor = '#AADC6E';
		config.language = 'zh-cn'; // 配置语言  
	    //config.uiColor = '#FFF'; // 背景颜色  
	    config.width = '800px'; // 宽度  
	    config.height = '300px'; // 高度  
	    config.skin = 'kama';//界面v2,kama,office2003  
	    config.toolbar = 'Full';// 工具栏风格Full,Basic  
	    config.filebrowserImageUploadUrl = 'noticeUploadFile.do?type=Images';//上传图片的保存路径
	    config.filebrowserFlashUploadUrl = 'noticeUploadFile.do?type=Flash';//上传flash的保存路径
	    config.toolbar =  
	        [  
	            ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],  
	            ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],  
	            ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],  
	            ['Link','Unlink','Anchor'],  
	            ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','TextColor','BGColor',  
	             'Maximize', 'ShowBlocks','-','Source','-','Undo','Redo'],  
	            '/',  
	            ['Styles','Format','Font','FontSize']  
	        ];  
	    config.contentsCss = ['../../../css/my_ckeditor_style.css', 
	                          '../../../js/ckeditor/contents.css'];
};
