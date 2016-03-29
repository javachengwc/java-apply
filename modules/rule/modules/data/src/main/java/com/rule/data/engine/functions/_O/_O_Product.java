package com.rule.data.engine.functions._O;

import com.rule.data.exception.*;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;

import java.util.Map;

/**
 * 跨表求积
 */
public class _O_Product extends Function{
    public static final String NAME = _O_Product.class.getSimpleName();
    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if(args.length<2){
            throw new ArgsCountException(NAME);
        }
        final String serviceName = DataUtil.getServiceName(args[0]); //数据源名称
        final String colCal = DataUtil.getStringValue(args[1]);      //计算列
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        Double result = (Double)cache.get(colCal);

        if(result==null){
            result = init(calInfo,serviceName,calInfo.getParam(),colCal,NAME);
            cache.put(colCal,result);
            return result;
        }else{
            return result;
        }

    }
    public static Double init(CalInfo calInfo, String serviceName, Map<String, Object> current
            , String colCal, String funcName)throws RengineException,CalculateException{
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }
        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, current,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), funcName);

        final Object[][] value = d2Data.getData();
        int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colCal);
        }

        Double product = NumberPool.DOUBLE_1;
        for(int i=0;i<value.length;i++){
            final Object colCalValue = value[i][colCalInt];
            if (canNumberOP(colCalValue)) {     //能进行数值运算
                product *= DataUtil.getNumberValue(colCalValue).doubleValue();
            }
        }
        return product;
    }

}
