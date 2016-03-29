package com.rule.data.engine.functions.base;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.ExcelRange;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class CONCAT extends Function {

    public static final String NAME = CONCAT.class.getSimpleName();

    class __key {
        Object range, needFilter, flag;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __key)) return false;

            __key key = (__key) o;

            if (flag != null ? !flag.equals(key.flag) : key.flag != null)
                return false;
            if (needFilter != null ? !needFilter.equals(key.needFilter) : key.needFilter != null)
                return false;
            if (range != null ? !range.equals(key.range) : key.range != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = range != null ? range.hashCode() : 0;
            result = 31 * result + (needFilter != null ? needFilter.hashCode() : 0);
            result = 31 * result + (flag != null ? flag.hashCode() : 0);
            return result;
        }

        __key(Object flag, Object needFilter, Object range) {
            this.flag = flag;
            this.needFilter = needFilter;
            this.range = range;
        }
    }


    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] instanceof ExcelRange) {
            ExcelRange range = (ExcelRange) args[0];
            boolean needFilter = false;
            if (args.length > 1 && args[1] instanceof Boolean) {
                needFilter = (Boolean) args[1];
            }
            String flag = ",";
            if (args.length > 2) {
                flag = DataUtil.getStringValue(args[2]);
            }

            flag = flag.trim();
            if (flag.length() == 0) {
                flag = ",";
            }

            Map<Object, Object> cache = calInfo.getCache(NAME);
            __key key = new __key(flag, needFilter, range);
            String areaValue = (String) cache.get(key);

            if (areaValue == null) {
                StringBuilder areaBuilder = new StringBuilder();

                Set<String> set = new HashSet<String>();

                Iterator<Object> ite = range.getIterator();

                while (ite.hasNext()) {
                    Object value = ite.next();

                    if (value != null) {
                        String tmp = DataUtil.getStringValue(value);

                        if (needFilter) {
                            if (!set.contains(tmp)) {
                                set.add(tmp);
                                areaBuilder.append(tmp).append(flag);
                            }
                        } else {
                            areaBuilder.append(tmp).append(flag);
                        }
                    }
                }

                areaValue = areaBuilder.length() == 0 ? ""
                        : areaBuilder.substring(0, areaBuilder.length() - flag.length());

                cache.put(key, areaValue);
            }

            return areaValue;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }
}
