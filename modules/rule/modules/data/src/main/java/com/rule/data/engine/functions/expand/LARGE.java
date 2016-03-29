package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;

import java.util.*;

public final class LARGE extends Function {

    public static final String NAME = LARGE.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] instanceof ExcelRange) {
            ExcelRange range = (ExcelRange) args[0];
            int index = DataUtil.getNumberValue(args[1]).intValue();

            Map<Object, Object> cache = calInfo.getCache(NAME);
            List<Double> result = (List<Double>) cache.get(range);

            if (result == null) {
                Iterator<Object> ite = range.getIterator();
                result = new ArrayList<Double>();

                while (ite.hasNext()) {
                    Object tmp = ite.next();

                    if (tmp != null && canNumberOP(tmp)) {
                        result.add(DataUtil.getNumberValue(tmp).doubleValue());
                    }
                }
                Collections.sort(result);
                Collections.reverse(result);
                cache.put(range, result);
            }

            try {
                return result.get(index - 1);
            } catch (Exception e) {
                throw new CalculateException("LARGE位置" + index + "非法");
            }
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }
}
