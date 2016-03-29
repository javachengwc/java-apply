package com.rule.data.engine.functions.time;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;

import java.util.Date;
import java.util.Map;

public final class NOW extends Function {

    public static final String NAME = NOW.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        final Map<Object, Object> cache = calInfo.getCache(NAME);
        Date now = (Date) cache.get(NAME);

        if (now == null) {
            now = new Date();
            cache.put(NAME, now);
        }

        return now;
    }
}
