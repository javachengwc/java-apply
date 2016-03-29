package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgColumnNotFoundException;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;
import com.rule.data.util.Type;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <hr>求和</hr>
 */
public class _O_CONCAT extends Function {
    public static final String NAME = _O_CONCAT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        final String serviceName = DataUtil.getServiceName(args[0]);
        final String colCal = DataUtil.getStringValue(args[1]);

        boolean needFilter = false;
        if (args.length > 2 && DataUtil.getType(args[2]) == Type.BOOLEAN) {
            needFilter = (Boolean) args[2];
        }

        String flag = ",";
        if (args.length > 3) {
            flag = DataUtil.getStringValue(args[3]);
            if (flag.length() == 0) {
                flag = ",";
            }
        }

        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        __Key key = new __Key(colCal, needFilter, flag);

        String result = (String) cache.get(key);
        if (result == null) {
            result = init(calInfo, serviceName, calInfo.getParam()
                    , colCal, needFilter, flag, NAME);
            cache.put(key, result);
        }

        return result;
    }

    class __Key {
        Object colCal, needFilter, flag;

        __Key(Object colCal, Object flag, Object needFilter) {
            this.colCal = colCal;
            this.flag = flag;
            this.needFilter = needFilter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCal != null ? !colCal.equals(key.colCal) : key.colCal != null)
                return false;
            if (flag != null ? !flag.equals(key.flag) : key.flag != null)
                return false;
            if (needFilter != null ? !needFilter.equals(key.needFilter) : key.needFilter != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCal != null ? colCal.hashCode() : 0;
            result = 31 * result + (needFilter != null ? needFilter.hashCode() : 0);
            result = 31 * result + (flag != null ? flag.hashCode() : 0);
            return result;
        }
    }

    static String init(CalInfo calInfo, String serviceName, Map<String, Object> currentParam
            , String colCal, boolean needFilter, String flag, String funcName) throws RengineException {
        final SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new RengineException(serviceName, "数据源未找到");
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, currentParam,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);


        final Object[][] value = d2Data.getData();
        int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(funcName, colCal);
        }

        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<String>();

        for (int i = 0; i < value.length; i++) {
            final Object colCalValue = value[i][colCalInt];
            if (colCalValue == null) {
                continue;
            }

            String tmp = DataUtil.getStringValue(colCalValue, DataUtil.getType(colCalValue));

            if (needFilter) {
                if (set.contains(tmp)) {
                    continue;
                }
                set.add(tmp);
            }
            sb.append(tmp).append(flag);
        }

        return sb.length() == 0 ? "" : sb.substring(0, sb.length() - flag.length());
    }
}