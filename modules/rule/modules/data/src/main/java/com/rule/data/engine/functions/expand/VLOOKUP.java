package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.StringPool;
import com.rule.data.util.Type;

import java.util.*;

public class VLOOKUP extends Function {
    public static final String NAME = VLOOKUP.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }


        if (args[1] instanceof ExcelRange) {
            boolean rangLookup = false; // 精确
            if (args.length > 3) {
                if (args[3] instanceof Boolean) {
                    rangLookup = (Boolean) args[3];
                }
            }

            final String reqKey = DataUtil.getStringValue(args[0]);
            final ExcelRange range = (ExcelRange) args[1];
            int colRet = DataUtil.getNumberValue(args[2]).intValue() - 1;

            Map<Object, Object> cache = calInfo.getCache(NAME);
            __Key key = new __Key(range, colRet, rangLookup);

            Object result = cache.get(key);

            if (result == null) {
                if (!rangLookup) {
                    result = init(range, colRet);
                    cache.put(key, result);
                } else {
                    result = init1(range, colRet);
                    cache.put(key, result);
                }
            }

            Object ret = null;

            if (!rangLookup) {
                ret = ((Map<String, Object>) result).get(reqKey);
            } else {
                Row tmp = new Row();
                Type type = DataUtil.getType(args[0]);

                switch (type) {
                    case LONG:
                    case DOUBLE:
                        tmp.key = ((Number) args[0]).doubleValue();
                        break;
                    default:
                        tmp.key = DataUtil.getStringValue(args[0], type);
                }

                List<Row> lstResult = ((List<Row>) result);
                for (int i = 0; i < lstResult.size(); i++) {
                    if (tmp.compareTo(lstResult.get(i)) < 0) {
                        if (i > 0) {
                            ret = lstResult.get(i - 1).value;
                        }
                        break;
                    } else if (tmp.compareTo(lstResult.get(i)) == 0) {
                        ret = lstResult.get(i).value;
                        break;
                    }

                    if (i == lstResult.size() - 1) {
                        ret = lstResult.get(i).value;
                        break;
                    }
                }
            }

            if (ret == null) {
                return StringPool.EMPTY;
            }

            return ret;
        }


        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }

    static class __Key {
        Object range, colRet, rangeLookup;

        __Key(Object range, Object colRet, Object rangeLookup) {
            this.colRet = colRet;
            this.range = range;
            this.rangeLookup = rangeLookup;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colRet != null ? !colRet.equals(key.colRet) : key.colRet != null)
                return false;
            if (range != null ? !range.equals(key.range) : key.range != null)
                return false;
            if (rangeLookup != null ? !rangeLookup.equals(key.rangeLookup) : key.rangeLookup != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = range != null ? range.hashCode() : 0;
            result = 31 * result + (colRet != null ? colRet.hashCode() : 0);
            result = 31 * result + (rangeLookup != null ? rangeLookup.hashCode() : 0);
            return result;
        }
    }

    static class Row implements Comparable<Row> {
        Object key;
        Object value;

        @Override
        public int compareTo(Row o) {
            if (key instanceof String && o.key instanceof String) {
                return ((String) key).compareTo((String) o.key);
            } else if (key instanceof Double && o.key instanceof Double) {
                return DataUtil.compare((Double) key, (Double) o.key);
            } else {
                return -1;
            }
        }
    }


    static List<Row> init1(ExcelRange range, int colRet)
            throws RengineException {
        List<Row> result = new ArrayList<Row>();

        ExcelRange rangeKey
                = new ExcelRange(range.getX1(), range.getY1(), range.getX1(), range.getY2(), range.getData());
        ExcelRange rangeValue = new ExcelRange(range.getX1() + colRet, range.getY1(), range.getX1() + colRet, range.getY2(), range.getData());

        Iterator<Object> keyIte = rangeKey.getIterator();
        Iterator<Object> valueIte = rangeValue.getIterator();

        while (keyIte.hasNext() && valueIte.hasNext()) {
            Row row = new Row();
            final Object key = keyIte.next();

            Type type = DataUtil.getType(key);
            switch (type) {
                case LONG:
                case DOUBLE:
                    row.key = ((Number) key).doubleValue();
                    break;
                default:
                    row.key = DataUtil.getStringValue(key, type);
            }

            row.value = valueIte.next();
            result.add(row);
        }

        Collections.sort(result);
        return result;
    }

    static Map<String, Object> init(ExcelRange range, int colRet)
            throws RengineException {
        Map<String, Object> result = new HashMap<String, Object>();

        ExcelRange rangeKey
                = new ExcelRange(range.getX1(), range.getY1(), range.getX1(), range.getY2(), range.getData());
        ExcelRange rangeValue = new ExcelRange(range.getX1() + colRet, range.getY1(), range.getX1() + colRet, range.getY2(), range.getData());

        Iterator<Object> keyIte = rangeKey.getIterator();
        Iterator<Object> valueIte = rangeValue.getIterator();

        while (keyIte.hasNext() && valueIte.hasNext()) {
            result.put(DataUtil.getStringValue(keyIte.next()), valueIte.next());
        }

        return result;
    }
}
