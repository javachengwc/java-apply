package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class REG_MATCH extends Function {
    public static final String NAME = REG_MATCH.class.getSimpleName();

    class __key {
        Object reg, source;

        __key(Object reg, Object source) {
            this.reg = reg;
            this.source = source;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __key)) return false;

            __key key = (__key) o;

            if (reg != null ? !reg.equals(key.reg) : key.reg != null)
                return false;
            if (source != null ? !source.equals(key.source) : key.source != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = reg != null ? reg.hashCode() : 0;
            result = 31 * result + (source != null ? source.hashCode() : 0);
            return result;
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2) {
            throw new RengineException(calInfo.getServiceName(), NAME + "参数个数不匹配");
        }

        __key key = new __key(args[0], args[1]);
        final Map<Object, Object> cache = calInfo.getCache(NAME);

        Boolean result = (Boolean) cache.get(key);

        if (result == null) {
            String reg = DataUtil.getStringValue(args[0]);
            String source = DataUtil.getStringValue(args[1]);

            try {
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(source);
                result = m.matches();
            } catch (Exception e) {
                result = false;
            }

            cache.put(key, result);
        }

        return result;
    }
}
