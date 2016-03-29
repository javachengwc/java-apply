package com.rule.data.model.vo;

import com.rule.data.exception.RengineException;
import com.rule.data.model.SerColumn;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;
import java.util.Map;

/**
 * 查询结果数据集类
 * data 中第一维度是行，第二维度是列
 */
public class D2Data {

    //列列表
    private final List<SerColumn> columnList;

    //表数据 行,列
    private Object[][] data = new Object[0][];

    //数据的创建时间 秒
    public long createtime = 0;

    //查询耗时 毫秒
    public long latency = Long.MAX_VALUE;

    //查询条件
    public Map<String, Object> parameter;

    //服务名
    public String serviceName;

    //服务得更新时间戳
    public long timestamp = 0;

    public boolean isInReload = false;

    //最后命中时间  就是最后获取到此数据的时间 毫秒
    public long lastAcTime = 0L;

    public void setProcessInfo(long createtime, long latency, Map<String, Object> parameter , String serviceName, long timestamp, long lastAcTime) {
        this.createtime = createtime;
        this.latency = latency;
        this.parameter = parameter;
        this.serviceName = serviceName;
        this.timestamp = timestamp;
        this.lastAcTime = lastAcTime;
    }

    public D2Data(List<SerColumn> columnList) {
        this.columnList = columnList;
    }

    public List<SerColumn> getColumnList() {
        return columnList;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    /**
     * 获取单元格值
     * @param name 列名称
     * @param row  行数
     * @return
     */
    public Object getValue(String name, int row) throws RengineException {

        if (row < data.length) {
            for (int i = 0; i < columnList.size(); i++) {

                final SerColumn column = columnList.get(i);

                if (column.getColumnName().equals(name)) {

                    return data[row][column.getColumnIntIndex()];
                }
            }
        }

        return null;
    }

    public Object getValue(int column, int row) {
        try {
            return data[row][column];
        } catch (Exception e) {
            //
        }

        return null;
    }

    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
