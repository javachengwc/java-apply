package com.rule.data.engine.functions._O;

import com.alibaba.fastjson.JSON;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.RengineException;
import com.rule.data.exception.ServiceNotFoundException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.StringPool;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerColumn;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;
import com.rule.data.util.LogUtil;

import java.util.*;

public class _O_JSONTABLEBYPARA extends Function {
    public static final String NAME = _O_JSONTABLEBYPARA.class.getSimpleName();

    //  通用整表JSON返回函数
    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        // args : 数据源名称 - needAll(是否返回基础数据表)- dbLang - 是否去重 -returnType- 是否继承参数 - 参数值 - 参数名
        if (args.length < 5) {
            throw new ArgsCountException(NAME);
        }
        String serviceName = DataUtil.getServiceName(args[0]);
        String needAll = DataUtil.getStringValue(args[1]);
        boolean needAllBoolean = true;
        if ("1".equals(needAll)) {
            needAllBoolean = true;
        } else if ("0".equals(needAll)) {
            needAllBoolean = false;
        } else {
            throw new RengineException(calInfo.getServiceName(), NAME + "未识别是否返回基础数据");
        }
        String dbLang = DataUtil.getStringValue(args[2]);
        boolean isDbLangZH = true;
        isDbLangZH = dbLang.trim().equalsIgnoreCase("zh");
        boolean needFilter;
        if (args[3] instanceof Boolean) {
            needFilter = (Boolean) args[3];
        } else {
            throw new RengineException(calInfo.getServiceName(), NAME + "未识别是否去重");
        }

        String returnType = DataUtil.getStringValue(args[4]);
        boolean returnTypeBoolean = true;
        if ("0".equals(returnType)) {
            // 按规则引擎返回格式 String-String 形式返回 二维数据 json格式
            returnTypeBoolean = true;
        } else if ("1".equals(returnType)) {
            // 按String-List形式返回 减少传输数据冗余 json格式
            returnTypeBoolean = false;
        } else {
            throw new RengineException(calInfo.getServiceName(), NAME + "未识别数据返回形式");
        }

        Map<String, Object> currentParam = calInfo.getParam();

        if (args.length > 5) {
            boolean isByParam = false;
            if (args[5] instanceof Boolean) {
                isByParam = (Boolean) args[5];
            } else {
                throw new RengineException(calInfo.getServiceName(), NAME + "未识别是否继承参数");
            }
            currentParam = getParam(args, 6, calInfo.getParam(), isByParam);
        }

        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, currentParam, NAME);
        __key key = new __key(serviceName, needAll, dbLang, needFilter, returnType);
        String result = (String) cache.get(key);
        if (result == null) {
            // long time5 = System.currentTimeMillis();
            result = init(calInfo, serviceName, currentParam, needAllBoolean, isDbLangZH, needFilter, returnTypeBoolean, NAME);
            // long time6 = System.currentTimeMillis();
            // LogUtil.info("函数耗时:"+(time6-time5));
            cache.put(key, result);
        }
        return result;
    }

    static String init(CalInfo calInfo, String serviceName, Map<String, Object> currentParam
            , boolean needAllBoolean, boolean isDbLangZH, boolean needFilter, boolean returnTypeBoolean, String funcName) throws RengineException, CalculateException {

        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }
        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, currentParam,
                        calInfo.getCallLayer(), calInfo.getServicePo(), calInfo.getParam(), NAME);
        try {
            List<Map<String, String>> row = data2Rows(d2Data, servicePo.getColumns(), needAllBoolean, isDbLangZH);
            if (row == null || row.size() == 0) {
                return "{}";
            }

            if (needFilter) {
                Set<Map<String, String>> set = new LinkedHashSet<Map<String, String>>();
                set.addAll(row);
                if (row.size() != set.size()) {
                    row.clear();
                    row.addAll(set);
                }
            }

            if (returnTypeBoolean) {
                return JSON.toJSONString(row);

            } else {
                //   long time3 = System.currentTimeMillis();
                Map<String, List<String>> rowByList = new LinkedHashMap<String, List<String>>();
                Set<String> columnNameSet = row.get(0).keySet();
                Iterator<String> iterator = columnNameSet.iterator();
                while (iterator.hasNext()) {
                    Object columnName = iterator.next();
                    List<String> datas = new ArrayList<String>();
                    for (int i = 0; i < row.size(); i++) {
                        datas.add(row.get(i).get(columnName));
                    }
                    rowByList.put(String.valueOf(columnName), datas);
                }
                //  long time4 = System.currentTimeMillis();
                //  LogUtil.info("data2List耗时:"+(time4-time3));
                return JSON.toJSONString(rowByList);
            }
        } catch (Exception e) {
            LogUtil.error("", e);
            return StringPool.EMPTY;
        }
    }


    class __key {
        Object serviceName, needAll, dbLang, needFilter, returnType;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __key)) return false;

            __key key = (__key) o;

            if (dbLang != null ? !dbLang.equals(key.dbLang) : key.dbLang != null)
                return false;
            if (needAll != null ? !needAll.equals(key.needAll) : key.needAll != null)
                return false;
            if (needFilter != null ? !needFilter.equals(key.needFilter) : key.needFilter != null)
                return false;
            if (returnType != null ? !returnType.equals(key.returnType) : key.returnType != null)
                return false;
            if (serviceName != null ? !serviceName.equals(key.serviceName) : key.serviceName != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = serviceName != null ? serviceName.hashCode() : 0;
            result = 31 * result + (needAll != null ? needAll.hashCode() : 0);
            result = 31 * result + (dbLang != null ? dbLang.hashCode() : 0);
            result = 31 * result + (needFilter != null ? needFilter.hashCode() : 0);
            result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
            return result;
        }

        public __key(Object serviceName, Object needAll, Object dbLang, Object needFilter, Object returnType) {
            this.serviceName = serviceName;
            this.needAll = needAll;
            this.dbLang = dbLang;
            this.needFilter = needFilter;
            this.returnType = returnType;
        }
    }

    private static List<Map<String, String>> data2Rows(D2Data data, List<SerColumn> columnsNeed
            , boolean needAll
            , boolean isDbLangZH) throws Exception {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>(data.getData().length);
        if (data.getData().length == 0) {
            return rows;
        }

        Map<String, String> rowData;

        SerColumn[] columns = new SerColumn[data.getData()[0].length];

        for (SerColumn column : needAll ? data.getColumnList() : columnsNeed) {
            if ("1".equals(column.getIsTransfer())) {
                columns[column.getColumnIntIndex()] = column;
            }
        }

        for (int i = 0; i < data.getData().length; i++) {
            final Object[] row = data.getData()[i];
            rowData = new LinkedHashMap<String, String>();

            for (int j = 0; j < row.length; j++) {
                if (columns[j] == null) {
                    continue;
                }

                String key = columns[j].getColumnName();
                if (!isDbLangZH) {
                    final String sqlColumnName = columns[j].getSqlColumnName();
                    if (sqlColumnName != null && sqlColumnName.trim().length() != 0) {
                        key = sqlColumnName;
                    }
                }

                rowData.put(key, DataUtil.getStringValue(row[j]));
            }

            rows.add(rowData);
        }
        return rows;
    }
}


