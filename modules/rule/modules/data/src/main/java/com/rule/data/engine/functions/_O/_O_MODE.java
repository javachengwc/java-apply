package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.RengineException;
import com.rule.data.exception.ServiceNotFoundException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.StringPool;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;
import com.rule.data.util.LogUtil;

import java.util.*;


public class _O_MODE extends Function {
    public static final String NAME = _O_MODE.class.getSimpleName();

    class _Key {
        List<String> columnNameList;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof _Key)) return false;

            _Key key = (_Key) o;

            if (columnNameList != null ? !columnNameList.equals(key.columnNameList) : key.columnNameList != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return columnNameList != null ? columnNameList.hashCode() : 0;
        }

        public _Key(List<String> columnNameList) {
            this.columnNameList = columnNameList;
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {

        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }
        final String serviceName = DataUtil.getServiceName(args[0]);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        List<String> alist = new ArrayList<String>();

        for (int i = 1; i < args.length; i++) {
            alist.add(DataUtil.getStringValue(args[i]));
        }

        _Key Key = new _Key(alist);
        String result = (String) cache.get(Key);
        if (result == null) {
            result = init(calInfo, serviceName, calInfo.getParam()
                    , alist, NAME);
            cache.put(Key, result);
        }

        return result;


    }

    static String init(CalInfo calInfo, String serviceName, Map<String, Object> current
            , List<String> columnNameList, String funcName) throws RengineException, CalculateException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }
        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, current,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);
        final Object[][] value = d2Data.getData();
        List<Double> numList = new ArrayList<Double>();
        for (int i = 0; i < columnNameList.size(); i++) {
            for (int j = 0; j < value.length; j++) {
                int colCalInt = DataUtil.getColumnIntIndex(columnNameList.get(i), d2Data.getColumnList());
                if (colCalInt == -1) {
                    // throw new ArgColumnNotFound(NAME, columnNameList.get(i));
                    if (columnNameList.get(i) != null && canNumberOP(columnNameList.get(i))) {  //如果输入的不是列名称，则判断如果是数字，则加入列表，否则直接略过
                        numList.add(DataUtil.getNumberValue(columnNameList.get(i)).doubleValue());
                        break;
                    }
                } else {
                    final Object colCalValue = value[j][colCalInt];
                    if (colCalValue == null) {
                        //
                    } else {
                        numList.add(DataUtil.getNumberValue(colCalValue).doubleValue());
                    }
                }

            }

        }

        return mode(numList);


    }

    public static String mode(List<Double> numList) throws RengineException, CalculateException {
        if (numList.size() == 0) {
            return StringPool.EMPTY;
        } else {
            Collections.sort(numList);
            Integer count = 1; //记录出现次数
            Integer longest = 0;  //重数
            double mode = 0;
            int temp = 0;
            for (int i = 0; i < numList.size() - 1; i++) {
                if (numList.get(i).doubleValue() == numList.get(i + 1).doubleValue()) {
                    count++;
                } else {
                    count = 1;//如果不等于，就换到了下一个数，那么计算下一个数的次数时，count的值应该重新符值为1
                    continue;
                }
                if (count > longest) {
                    mode = numList.get(i);
                    longest = count;
                    temp = i;
                }
            }
            Double MODE = numList.get(temp);

            LogUtil.debug("出现次数" + longest);//打印出这个数出现的次数已判断是否正确
            if (longest == 0 || longest == 1) {
                // cache.put(key, StringPool.EMPTY);
                return StringPool.EMPTY;

            } else {
                LogUtil.debug("众数是" + MODE);
                LogUtil.debug("出现次数" + longest);//打印出这个数出现的次数已判断是否正确
                // cache.put(key, MODE);
                return DataUtil.getStringValue(MODE);
            }
            // return cache.get(key);

        }
    }
}
