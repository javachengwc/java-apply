package com.rule.data.service.core;

import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.ExcelEngineTool;
import com.rule.data.exception.RengineException;
import com.rule.data.model.SerColumn;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询高级数据服务类
 */
public final class Cache4AdvanceService {

    /**
     * 高级数据源的计算, 优先计算基本数据源, 然后在计算高级数据源
     *
     * @param servicePo
     * @param param
     * @param callLayer
     * @return
     * @throws com.rule.data.exception.RengineException
     */
    static D2Data getD2Data(SerService servicePo, Map<String, Object> param, int callLayer) throws RengineException {
        if (callLayer > 30) {
            throw new RengineException(servicePo.getName(), "数据源嵌套调用层次不得深于30");
        }

        if (servicePo.getColumns().size() == 0) {
            throw new RengineException(servicePo.getName(), "列数目为0, ");
        }

        final String serviceName = servicePo.getName();
        final Integer baseServiceID = servicePo.getBaseServiceID();
        final String baseServicename = Services.id2Name(baseServiceID);
        final SerService baseServicePo = Services.getService(baseServicename);
        if (baseServicename == null || baseServicePo == null) {
            throw new RengineException(serviceName, "基本数据源ID: " + baseServiceID + " 未找到");
        }

        CalInfo calInfo = new CalInfo(callLayer, param, servicePo);
        try {
            final D2Data baseD2Data =
                    Cache4D2Data.getD2Data(baseServicePo, param, callLayer, servicePo, param, "父级调用");
            final D2Data d2Data = cal(baseD2Data, calInfo, servicePo);
            calInfo = null;
            if (LogUtil.isDebugEnabled()) {
                LogUtil.debug("rows " + d2Data.getData().length + ", columns: " + d2Data.getColumnList().size());
            }
            return d2Data;
        } catch (RengineException e) {
            e.addInvoke(calInfo);
            throw e;
        }
    }

    /**
     * 创建新的d2data, 配置新的二维数据和列信息
     * 计算完后, 返回
     *
     * @param baseD2Data
     * @param calInfo
     * @param servicePo
     * @return
     * @throws com.rule.data.exception.RengineException
     */
    private static D2Data cal(D2Data baseD2Data, CalInfo calInfo, SerService servicePo)
            throws RengineException {
        Object[][] basedata = baseD2Data.getData();
        Object[][] data = basedata;
        final int maxRow = basedata.length;

        final List<SerColumn> columnList = new ArrayList<SerColumn>(
                baseD2Data.getColumnList().size() + servicePo.getColumns().size());
        final D2Data d2Data = new D2Data(columnList);
        d2Data.setData(data);

        for (SerColumn column : baseD2Data.getColumnList()) {
            columnList.add(column);
        }
        for (SerColumn column : servicePo.getColumns()) {
            columnList.add(column);
        }

        if (maxRow != 0) {  // 有数据，进行COPY和扩容
            int maxColumnIndex = -1;
            for (SerColumn column : columnList) {
                final int index;
                index = column.getColumnIntIndex();
                maxColumnIndex = index > maxColumnIndex ? index : maxColumnIndex;
            }

            int needColumnSize = maxColumnIndex + 1; // 实际需要的列数目

            data = new Object[maxRow][];
            Object[] rowData;

            for (int i = 0; i < maxRow; i++) {
                rowData = new Object[needColumnSize];
                System.arraycopy(basedata[i], 0, rowData, 0, basedata[i].length); // 复制原有数据, 原有数据可能被多个高级数据源使用
                data[i] = rowData;
            }

            d2Data.setData(data);

            calInfo.setCurD2data(d2Data);
            calInfo.setMaxRow(maxRow);

            ExcelEngineTool.process(d2Data, calInfo);
        }

        return d2Data;
    }

}
