package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.StringPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public final class CONCATBYGROUP extends Function {

    public static final String NAME = CONCATBYGROUP.class.getSimpleName();

    class __Key {
        Object by, cal, needFilter, flag;

        __Key(Object by, Object cal, Object needFilter, Object flag) {
            this.by = by;
            this.cal = cal;
            this.needFilter = needFilter;
            this.flag = flag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (by != null ? !by.equals(key.by) : key.by != null) return false;
            if (cal != null ? !cal.equals(key.cal) : key.cal != null)
                return false;
            if (flag != null ? !flag.equals(key.flag) : key.flag != null)
                return false;
            if (needFilter != null ? !needFilter.equals(key.needFilter) : key.needFilter != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = by != null ? by.hashCode() : 0;
            result = 31 * result + (cal != null ? cal.hashCode() : 0);
            result = 31 * result + (needFilter != null ? needFilter.hashCode() : 0);
            result = 31 * result + (flag != null ? flag.hashCode() : 0);
            return result;
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        if (args[1] instanceof ExcelRange && args[2] instanceof ExcelRange) {
            String req = DataUtil.getStringValue(args[0]);
            ExcelRange by = (ExcelRange) args[1];
            ExcelRange cal = (ExcelRange) args[2];

            boolean needFilter = false;
            if (args.length > 3 && args[3] instanceof Boolean) {
                needFilter = (Boolean) args[3];
            }
            String flag = ",";
            if (args.length > 4) {
                flag = DataUtil.getStringValue(args[4]);
            }

            flag = flag.trim();
            if (flag.length() == 0) {
                flag = ",";
            }


            __Key key = new __Key(by, cal, needFilter, flag);
            Map<Object, Object> cache = calInfo.getCache(NAME);
            Map<String, String> result = (Map<String, String>) cache.get(key);
            Map<String, StringBuilder> strResult = new HashMap<String, StringBuilder>();
            Map<String, HashSet<String>> setResult = new HashMap<String, HashSet<String>>();

            if (result == null) {
                result = new HashMap<String, String>();

                Iterator<Object> byI = by.getIterator();
                Iterator<Object> calI = cal.getIterator();

                while (byI.hasNext() && calI.hasNext()) {
                    final Object byObj = byI.next();
                    final Object calObj = calI.next();

                    if (byObj == null || calObj == null) {
                        continue;
                    }

                    String _key = DataUtil.getStringValue(byObj);
                    String _value = DataUtil.getStringValue(calObj);

                    HashSet<String> childs = setResult.get(_key);
                    StringBuilder sb = strResult.get(_key);

                    if (childs == null) {
                        childs = new HashSet<String>();
                        setResult.put(_key, childs);
                    }

                    if (sb == null) {
                        sb = new StringBuilder();
                        strResult.put(_key, sb);
                    }

                    if (needFilter) {
                        if (!childs.contains(_value)) {
                            childs.add(_value);
                            sb.append(_value).append(flag);
                        }
                    } else {
                        sb.append(_value).append(flag);
                    }
                }

                for (Map.Entry<String, StringBuilder> entry : strResult.entrySet()) {
                    StringBuilder sb = entry.getValue();
                    final String str = sb.length() == 0 ? ""
                            : sb.substring(0, sb.length() - flag.length());

                    result.put(entry.getKey(), str);
                }

                cache.put(key, result);
            }

            String ret = result.get(req);
            if (ret == null) {
                return StringPool.EMPTY;
            }

            return ret;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }
}
