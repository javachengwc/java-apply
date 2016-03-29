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

public class RANKBYGROUP extends Function {

    public static final String NAME = RANKBYGROUP.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 4) {
            throw new ArgsCountException(NAME);
        }

        final String groupKey = DataUtil.getStringValue(args[0]);
        final String orderKey = DataUtil.getStringValue(args[1]);

        if (!(args[2] instanceof ExcelRange) || !(args[3] instanceof ExcelRange)) {
            throw new RengineException(calInfo.getServiceName(), "输入不是数列");
        }

        final ExcelRange colBy = (ExcelRange) args[2];
        final ExcelRange colOrder = (ExcelRange) args[3];

        int order = 0;
        if (args.length > 4) {
            order = DataUtil.getNumberValue(args[4]).intValue();
        }
        boolean isPassZero = false;
        if (args.length > 5 && args[5] instanceof Boolean) {
            isPassZero = (Boolean) args[5];
        }

        Map<Object, Object> cache = calInfo.getCache(NAME);
        __Key key = new __Key(colBy, colOrder, order, isPassZero);
        Map<String, Map<String, Long>> result = (Map<String, Map<String, Long>>) cache.get(key);

        if (result == null) {
            result = new HashMap<String, Map<String, Long>>();

            Map<String, List<Double>> tmpOrder = new HashMap<String, List<Double>>();

            Iterator<Object> iteOrder = colOrder.getIterator();
            Iterator<Object> iteBy = colBy.getIterator();

            while (true) {
                if (iteOrder.hasNext() && iteBy.hasNext()) {
                    Object orderT = iteOrder.next();
                    String byT = DataUtil.getStringValue(iteBy.next());

                    if (canNumberOP(orderT)) {
                        double d = DataUtil.getNumberValue(orderT).doubleValue();

                        if (isPassZero
                                && DataUtil.compare(d, NumberPool.DOUBLE_0) == 0) {
                            continue;
                        }

                        List<Double> lst = tmpOrder.get(byT);
                        if (lst == null) {
                            lst = new ArrayList<Double>();
                            tmpOrder.put(byT, lst);
                        }

                        lst.add(d);
                    }
                } else {
                    break;
                }
            }

            for (Map.Entry<String, List<Double>> entry : tmpOrder.entrySet()) {
                Map<String, Long> orderInfo = new HashMap<String, Long>();
                final List<Double> numbers = entry.getValue();
                Collections.sort(numbers);

                final int size = numbers.size();

                if (size != 0) {
                    Collections.sort(numbers);
                }

                long rank = NumberPool.LONG_1;
                if (order == 0) {
                    for (int i = size - 1; i >= 0; i--) {
                        if (i == size - 1) {
                            orderInfo.put(DataUtil.getStringValue(numbers.get(i)), rank);
                        } else {
                            if (numbers.get(i).equals(numbers.get(i + 1))) {
                                orderInfo.put(DataUtil.getStringValue(numbers.get(i)), rank);
                            } else {
                                rank = size - i;
                                orderInfo.put(DataUtil.getStringValue(numbers.get(i)), rank);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        if (i == 0) {
                            orderInfo.put(DataUtil.getStringValue(numbers.get(i)), rank);
                        } else {
                            if (numbers.get(i).equals(numbers.get(i - 1))) {
                                orderInfo.put(DataUtil.getStringValue(numbers.get(i)), rank);
                            } else {
                                rank = i + 1;
                                orderInfo.put(DataUtil.getStringValue(numbers.get(i)), rank);
                            }
                        }
                    }
                }

                result.put(entry.getKey(), orderInfo);
            }

            cache.put(key, result);
        }

        Map<String, Long> ret1 = result.get(groupKey);

        if (ret1 != null) {
            Long ret = ret1.get(orderKey);

            if (ret != null) {
                return ret;
            }
        }

        return NumberPool.LONG_0;
    }

    class __Key {
        ExcelRange colBy, colOrder;
        int order;
        boolean passZero;

        __Key(ExcelRange colBy, ExcelRange colOrder, int order, boolean passZero) {
            this.colBy = colBy;
            this.colOrder = colOrder;
            this.order = order;
            this.passZero = passZero;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (order != key.order) return false;
            if (passZero != key.passZero) return false;
            if (colBy != null ? !colBy.equals(key.colBy) : key.colBy != null)
                return false;
            if (colOrder != null ? !colOrder.equals(key.colOrder) : key.colOrder != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colBy != null ? colBy.hashCode() : 0;
            result = 31 * result + (colOrder != null ? colOrder.hashCode() : 0);
            result = 31 * result + order;
            result = 31 * result + (passZero ? 1 : 0);
            return result;
        }
    }
}