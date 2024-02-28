package com.util.regex;

public interface CommonRegex {
	
	String URL    = "((([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*)|(http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*)";
	String SCRIPT = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
	String STYLE  = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
	String HTML   = "<[^>]+>";
	
	String TELE_PHONE = "(^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
	String CELL_PHONE = "^1\\d{10}$";
	
	String BANK_ACCOUNT = "\\d{19}";
	
	String QQ = "^\\d{5,10}$";
	String EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	
	String IP = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	String STATUS_CODE = "";
	String REQUEST_METHOD = "GET|POST";

    String REG_EXP_OF_IP = "([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    String REG_EXP_OF_CONTAINS_IP = "((?s).*?)([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}((?s).*?)";
    //数组和字母
    String REG_DC="^[a-zA-Z\\d]+$";
    //分割符
	String CUTOFF= "[\\|\\s;,.\r\n]";

}
