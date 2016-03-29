package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class IN extends Function {

    public static final String NAME = IN.class.getSimpleName();

    class __Key {
        Object str, flag;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (flag != null ? !flag.equals(key.flag) : key.flag != null)
                return false;
            if (str != null ? !str.equals(key.str) : key.str != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = str != null ? str.hashCode() : 0;
            result = 31 * result + (flag != null ? flag.hashCode() : 0);
            return result;
        }

        __Key(Object flag, Object str) {
            this.flag = flag;
            this.str = str;
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        final String req1 = DataUtil.getStringValue(args[0]);
        final String req2 = DataUtil.getStringValue(args[1]);
        final String req3 = DataUtil.getStringValue(args[2]);
        final String reqSplitStr = (args.length > 3 ? DataUtil.getStringValue(args[3]) : null);

        Map<Object, Object> cache = calInfo.getCache(NAME);
        __Key key = new __Key(req3, req2);
        Set<String> set = (Set<String>) cache.get(key);

        if (set == null) {
            String[] arr = req2.split(req3);
            set = new HashSet<String>();
            for (String ele : arr) {
                set.add(ele);
            }
            cache.put(key, set);
        }

        if (reqSplitStr != null) {
            __Key reqKey = new __Key(reqSplitStr, req1);
            Set<String> setReq = (Set<String>) cache.get(reqKey);

            if (setReq == null) {
                String[] arr = req1.split(reqSplitStr);
                setReq = new HashSet<String>();

                for (String ele : arr) {
                    setReq.add(ele);
                }

                cache.put(reqKey, setReq);
            }

            for (String ele : setReq) {
                if (!set.contains(ele)) {
                    return false;
                }
            }

            return true;
        } else {
            return set.contains(req1);
        }
    }
}
