package com.rule.data.model.vo;

import com.rule.data.engine.excel.NumberPool;
import com.rule.data.util.DataUtil;

import java.util.Map;

public class OrderRow implements Comparable<OrderRow> {

    public final Map<String, String> row;

    public final Object id;

    public OrderRow(Object id, Map<String, String> row) {

        this.id = id;

        this.row = row;
    }

    @Override
    public int compareTo(OrderRow o) {
        try {

            long cmp = DataUtil.compareO(id, o.id);

            return cmp > NumberPool.LONG_0 ? 1 : (cmp == NumberPool.LONG_0 ? 0 : -1);

        } catch (Exception e) {
            //
        }

        return 0;
    }
}