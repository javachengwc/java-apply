package com.rule.data.engine.functions.program;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.StringPool;
import java.util.concurrent.ConcurrentHashMap;

public final class MD5 extends Function {

    public static final String NAME = MD5.class.getSimpleName();
    private ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<String, String>();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        String source = StringPool.EMPTY;

        if (args.length > 0) {
            source = DataUtil.getStringValue(args[0]);
        }

        String result = cache.get(source);
        //待处理
//        if (result == null) {
//            result = EnDecryptUtil.digest(source, false);
//            cache.put(source, result);
//        }

        return result;
    }
}
