package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgColumnNotFoundException;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.RengineException;
import com.rule.data.exception.ServiceNotFoundException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.StringPool;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;
import com.rule.data.util.Type;

import java.util.*;

public class _O_VLOOKUP extends Function {
    public static final String NAME = _O_VLOOKUP.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 4) {
            throw new ArgsCountException(NAME);
        }

        boolean rangLookup = false; // 精确
        if (args.length > 4) {
            if (args[4] instanceof Boolean) {
                rangLookup = (Boolean) args[4];
            }
        }

        final String reqKey = DataUtil.getStringValue(args[0]);
        final String serviceName = DataUtil.getServiceName(args[1]);
        final String colBy = DataUtil.getStringValue(args[2]);
        final String colCal = DataUtil.getStringValue(args[3]);

        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        __Key key = new __Key(colBy, colCal, rangLookup);

        Object result = cache.get(key);

        if (result == null) {
            if (!rangLookup) {
                result = init(calInfo, serviceName, calInfo.getParam(), colBy, colCal, NAME);
                cache.put(key, result);
            } else {
                result = init1(calInfo, serviceName, calInfo.getParam(), colBy, colCal, NAME);
                cache.put(key, result);
            }
        }

        Object ret = null;

        if (!rangLookup) {
            ret = ((Map<String, Object>) result).get(reqKey);
        } else {
            Row tmp = new Row();
            if (args[0] instanceof Number) {
                tmp.key = ((Number) args[0]).doubleValue();
            } else {
                tmp.key = reqKey;
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

        if(ret == null){
            return StringPool.EMPTY;
        }

        return ret;
    }

    static class __Key {
        Object colBy, colCal, rangeLookup;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colBy != null ? !colBy.equals(key.colBy) : key.colBy != null)
                return false;
            if (colCal != null ? !colCal.equals(key.colCal) : key.colCal != null)
                return false;
            if (rangeLookup != null ? !rangeLookup.equals(key.rangeLookup) : key.rangeLookup != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colBy != null ? colBy.hashCode() : 0;
            result = 31 * result + (colCal != null ? colCal.hashCode() : 0);
            result = 31 * result + (rangeLookup != null ? rangeLookup.hashCode() : 0);
            return result;
        }

        __Key(Object colBy, Object colCal, Object rangeLookup) {
            this.colBy = colBy;
            this.colCal = colCal;
            this.rangeLookup = rangeLookup;
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


    static List<Row> init1(CalInfo calInfo, String serviceName, Map<String, Object> current
            , String colBy, String colCal, String funcName)
            throws RengineException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, current,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);

        List<Row> result = new ArrayList<Row>();
        final Object[][] value = d2Data.getData();
        int colByInt = DataUtil.getColumnIntIndex(colBy, d2Data.getColumnList());
        int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colByInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colBy);
        }
        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colCal);
        }

        for (int i = 0; i < value.length; i++) {
            final Object colByValue = value[i][colByInt];
            final Object colCalValue = value[i][colCalInt];
            if (colByValue == null || colCalValue == null) {
                continue;
            }

            Row row = new Row();

            Type type = DataUtil.getType(colByValue);
            switch (type) {
                case LONG:
                case DOUBLE:
                    row.key = ((Number) colByValue).doubleValue();
                    break;
                default:
                    row.key = DataUtil.getStringValue(colByValue, type);
            }

            row.value = colCalValue;
            result.add(row);
        }

        Collections.sort(result);
        return result;
    }

    static Map<String, Object> init(CalInfo calInfo, String serviceName, Map<String, Object> current
            , String colBy, String colCal, String funcName)
            throws RengineException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, current,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);

        Map<String, Object> result = new HashMap<String, Object>();
        final Object[][] value = d2Data.getData();
        int colByInt = DataUtil.getColumnIntIndex(colBy, d2Data.getColumnList());
        int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colByInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colBy);
        }
        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colCal);
        }

        for (int i = 0; i < value.length; i++) {
            final Object colByValue = value[i][colByInt];
            final Object colCalValue = value[i][colCalInt];
            if (colByValue == null || colCalValue == null) {
                continue;
            }

            String key = DataUtil.getStringValue(colByValue);
            result.put(key, colCalValue);
        }

        return result;
    }
}
