package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.text.CollationKey;
import java.text.Collator;
import java.util.*;

public class RANKSTR extends Function {

    public static final String NAME = RANKSTR.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        if (args[1] instanceof ExcelRange) {
            final String reqKey = DataUtil.getStringValue(args[0]);
            final ExcelRange range = (ExcelRange) args[1];
            int order = 0;
            if (args.length > 2) {
                order = DataUtil.getNumberValue(args[2]).intValue();
            }
            boolean isPassEmpty = false;
            if (args.length > 3 && args[3] instanceof Boolean) {
                isPassEmpty = (Boolean) args[3];
            }

            Map<Object, Object> cache = calInfo.getCache(NAME);
            __Key key = new __Key(range, order, isPassEmpty);

            Map<String, Long> result = (Map<String, Long>) cache.get(key);

            if (result == null) {
                result = init(range, order, isPassEmpty);
                cache.put(key, result);
            }

            Long ret = result.get(reqKey);
            if (ret == null) {
                return NumberPool.LONG_0;
            }

            return ret;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }

    class __Key {
        Object colCal, order, isPassEmpty;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCal != null ? !colCal.equals(key.colCal) : key.colCal != null)
                return false;
            if (isPassEmpty != null ? !isPassEmpty.equals(key.isPassEmpty) : key.isPassEmpty != null)
                return false;
            if (order != null ? !order.equals(key.order) : key.order != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCal != null ? colCal.hashCode() : 0;
            result = 31 * result + (order != null ? order.hashCode() : 0);
            result = 31 * result + (isPassEmpty != null ? isPassEmpty.hashCode() : 0);
            return result;
        }

        __Key(Object colCal, Object order, Object isPassEmpty) {
            this.colCal = colCal;
            this.isPassEmpty = isPassEmpty;
            this.order = order;
        }
    }

    private Map<String, Long> init(ExcelRange range, int order, boolean isPassEmpty) throws RengineException, CalculateException {
        Collator collator = Collator.getInstance(Locale.CHINA);
        List<CollationKey> values = new ArrayList<CollationKey>();
        Map<String, Long> result = new HashMap<String, Long>();

        Iterator<Object> ite = range.getIterator();

        while (ite.hasNext()) {
            Object tmp = ite.next();

            String str = DataUtil.getStringValue(tmp);

            if (isPassEmpty
                    && str.length() == 0) {
                continue;
            }
            values.add(collator.getCollationKey(str));
        }

        final int size = values.size();

        if (size != 0) {
            Collections.sort(values);
        }

        long rank = NumberPool.LONG_1;
        if (order == 0) {
            for (int i = size - 1; i >= 0; i--) {
                if (i == size - 1) {
                    result.put(values.get(i).getSourceString(), rank);
                } else {
                    if (values.get(i).getSourceString().equals(values.get(i + 1).getSourceString())) {
                        result.put(values.get(i).getSourceString(), rank);
                    } else {
                        rank = size - i;
                        result.put(values.get(i).getSourceString(), rank);
                    }
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    result.put(values.get(i).getSourceString(), rank);
                } else {
                    if (values.get(i).getSourceString().equals(values.get(i - 1).getSourceString())) {
                        result.put(values.get(i).getSourceString(), rank);
                    } else {
                        rank = i + 1;
                        result.put(values.get(i).getSourceString(), rank);
                    }
                }
            }
        }

        return result;
    }

}
