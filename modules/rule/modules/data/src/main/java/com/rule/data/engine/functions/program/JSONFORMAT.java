package com.rule.data.engine.functions.program;

import com.alibaba.fastjson.JSON;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.util.Type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class JSONFORMAT extends Function {

    public static final String NAME = JSONFORMAT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        if (args.length > 1) {
            Type type = DataUtil.getType(args[1]);
            final String key = JSON.toJSONString(DataUtil.getStringValue(args[0]));

            if (type == Type.RANGE) {
                List<String> lst = new ArrayList<String>();
                ExcelRange range = (ExcelRange) args[1];
                Iterator<Object> ite = range.getIterator();

                while (ite.hasNext()) {
                    Object tmp = ite.next();
                    lst.add(DataUtil.getStringValue(tmp));
                }

                return key + ":" + JSON.toJSONString(lst);
            }

            String str = DataUtil.getStringValue(args[1], type);
            return key + ":" + JSON.toJSONString(str);
        } else {
            Type type = DataUtil.getType(args[0]);

            if (type == Type.RANGE) {
                List<String> lst = new ArrayList<String>();
                ExcelRange range = (ExcelRange) args[0];
                Iterator<Object> ite = range.getIterator();

                while (ite.hasNext()) {
                    Object tmp = ite.next();
                    lst.add(DataUtil.getStringValue(tmp));
                }

                return JSON.toJSONString(lst);
            }

            String str = DataUtil.getStringValue(args[0], type);
            return JSON.toJSONString(str);
        }
    }
}
