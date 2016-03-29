package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.*;

public final class AVERAGEBYGROUP extends Function {

    public static final String NAME = AVERAGEBYGROUP.class.getSimpleName();

    class __Key {
        Object by, cal;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (by != null ? !by.equals(key.by) : key.by != null) return false;
            if (cal != null ? !cal.equals(key.cal) : key.cal != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = by != null ? by.hashCode() : 0;
            result = 31 * result + (cal != null ? cal.hashCode() : 0);
            return result;
        }

        __Key(Object by, Object cal) {
            this.by = by;
            this.cal = cal;
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length != 3) {
            throw new ArgsCountException(NAME);
        }

        if (args[1] instanceof ExcelRange && args[2] instanceof ExcelRange) {
            String req = DataUtil.getStringValue(args[0]);
            ExcelRange by = (ExcelRange) args[1];
            ExcelRange cal = (ExcelRange) args[2];

            __Key key = new __Key(by, cal);
            Map<Object, Object> cache = calInfo.getCache(NAME);
            Map<String, Double> result = (Map<String, Double>) cache.get(key);

            if (result == null) {
                result = new HashMap<String, Double>();
                Map<String, List<Double>> datas = new HashMap<String, List<Double>>();

                Iterator<Object> byI = by.getIterator();
                Iterator<Object> calI = cal.getIterator();

                while (byI.hasNext() && calI.hasNext()) {
                    final Object byObj = byI.next();
                    final Object calObj = calI.next();

                    if (byObj == null || calObj == null) {
                        continue;
                    }

                    if (canNumberOP(calObj)) {
                        String _key = DataUtil.getStringValue(byObj);
                        double value = DataUtil.getNumberValue(calObj).doubleValue();

                        List<Double> dataList = datas.get(_key);
                        if (dataList != null) {
                            dataList.add(value);
                        } else {
                            dataList = new ArrayList<Double>();
                            dataList.add(value);
                            datas.put(_key, dataList);
                        }
                    }
                }


                for (Map.Entry<String, List<Double>> entry : datas.entrySet()) {
                    List<Double> dataList = entry.getValue();

                    double sum = NumberPool.DOUBLE_0;
                    for (int i = 0; i < dataList.size(); i++) {
                        sum += dataList.get(i);
                    }

                    result.put(entry.getKey(), sum / dataList.size());
                }

                cache.put(key, result);
            }

            Double ret = result.get(req);
            if (ret == null) {
                return NumberPool.DOUBLE_0;
            }

            return ret;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }
}
