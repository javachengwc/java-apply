package com.util;

import java.nio.charset.Charset;

public class CharsetUtil {

    public final static String GBK 			= "GBK";

    public final static String UTF8			= "UTF-8";

    public final static String ISO88591 	= "ISO-8859-1";

    public static final Charset UTF_8 = Charset.forName(UTF8);

    public static final Charset ISO_8859_1 = Charset.forName(ISO88591);
}
