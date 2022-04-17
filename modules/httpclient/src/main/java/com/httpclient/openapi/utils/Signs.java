package com.httpclient.openapi.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

public class Signs {

    public static String sign(String appSecret, Map<String, Object> paramMap) throws Exception {
        if (!MapUtils.isEmpty(paramMap) && !StringUtils.isEmpty(appSecret)) {
            List<Map.Entry<String, Object>> params = new ArrayList(paramMap.entrySet());
            Collections.sort(params, new Comparator<Map.Entry<String, Object>>() {
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return ((String)o1.getKey()).compareTo((String)o2.getKey());
                }
            });
            StringBuilder sb = new StringBuilder();
            sb.append(appSecret);
            Iterator var4 = params.iterator();

            while(true) {
                while(true) {
                    String key;
                    Object value;
                    do {
                        if (!var4.hasNext()) {
                            sb.append(appSecret);
                            String signature = Encoder.encodeByMD5(sb.toString()).toUpperCase();
                            return signature;
                        }

                        Map.Entry<String, Object> item = (Map.Entry)var4.next();
                        key = (String)item.getKey();
                        value = item.getValue();
                    } while(value == null);

                    if (value instanceof Object[]) {
                        Object[] values = (Object[])((Object[])value);
                        String str = "[";

                        for(int i = 0; i < values.length; ++i) {
                            if (i > 0) {
                                str = str + ",";
                            }

                            str = str + values[i];
                        }

                        str = str + "]";
                        sb.append(key).append(str);
                    } else {
                        sb.append(key).append(value);
                    }
                }
            }
        } else {
            return "";
        }
    }

}
