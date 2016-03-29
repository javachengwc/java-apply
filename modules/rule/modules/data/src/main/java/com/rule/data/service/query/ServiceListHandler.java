package com.rule.data.service.query;

import com.alibaba.fastjson.JSON;
import com.rule.data.exception.RengineException;
import com.rule.data.handler.Handler;
import com.rule.data.model.SerColumn;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.model.vo.OrderRow;
import com.rule.data.service.core.DataService;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.util.*;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ServiceListHandler implements Handler {

    @Override
    public HttpResponse handleReq(String content) throws Throwable {
        ServiceListReqInfo reqInfo = DataUtil.parse(content, ServiceListReqInfo.class);
        final String format = reqInfo.getFormat();

        if (!format.equalsIgnoreCase("json") && !format.equalsIgnoreCase("xml")) {
            throw new RengineException(null, "组合调用支持json和xml格式, " + format);
        }

        if (reqInfo.getServiceInfos() == null || reqInfo.getServiceInfos().size() == 0) {
            throw new RengineException(null, "数据源信息列表无数据");
        }

        if (reqInfo.getParams() == null || reqInfo.getParams().size() == 0) {
            throw new RengineException(null, "参数信息列表无数据");
        }

        if (reqInfo.getParams().size() != reqInfo.getServiceInfos().size()) {
            throw new RengineException(null, "数据源信息列表与参数信息列表长度不一致, "
                    + reqInfo.getServiceInfos().size() + ", " + reqInfo.getParams().size());
        }

        List<List<Map<String, String>>> rowsList = new ArrayList<List<Map<String, String>>>(
                reqInfo.getServiceInfos().size());


        for (int i = 0; i < reqInfo.getServiceInfos().size(); i++) {
            final ServiceInfo info = reqInfo.getServiceInfos().get(i);
            final Map<String, Object> param = reqInfo.getParams().get(i);
            final String dbLang = info.getDbLang();
            final String serviceName = info.getServiceName();
            final boolean needAll = "1".equals(info.getNeedAll());

            final String mappingService = Services.getServiceMapping(serviceName);
            SerService servicePo;
            if (mappingService != null) {
                servicePo = Services.getService(mappingService);
                if (servicePo == null) {
                    throw new RengineException(serviceName, "映射数据源未找到, " + mappingService);
                }

                if ("LS".equals(servicePo.getState())) {
                    throw new RengineException(serviceName, "映射数据源已失效, " + mappingService);
                }
            } else {
                servicePo = Services.getService(serviceName);
                if (servicePo == null) {
                    throw new RengineException(serviceName, "数据源未找到");
                }

                if ("LS".equals(servicePo.getState())) {
                    throw new RengineException(serviceName, "数据源已失效");
                }
            }

            final D2Data data = DataService.getD2Data(servicePo, param);
            rowsList.add(data2Rows(data, servicePo.getColumns(), needAll, dbLang
                    , info.getOrderColumnName(), info.getOrder()));
        }

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        ServiceListRespInfo respInfo = new ServiceListRespInfo();
        respInfo.setRowsList(rowsList);

        if (format.equalsIgnoreCase("json")) {
            response.setHeader(CONTENT_TYPE, "application/json");
            response.setContent(ChannelBuffers.wrappedBuffer(JSON.toJSONString(respInfo).getBytes()));
        } else {
            response.setHeader(CONTENT_TYPE, "text/xml");
            response.setContent(ChannelBuffers.wrappedBuffer(rows2Xml(rowsList).getBytes()));
        }

        return response;
    }


    private List<Map<String, String>> data2Rows(D2Data data, List<SerColumn> columnsNeed
            , boolean needAll, String dbLang, String orderColumnName, String order) throws Exception {
        final boolean isDbLangZH = "zh".equalsIgnoreCase(dbLang);
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>(data.getData().length);
        if (data.getData().length == 0) {
            return rows;
        }

        Map<String, String> rowData;
        SerColumn[] columns = new SerColumn[data.getData()[0].length];
        int orderColumnInt = -1;

        for (SerColumn column : needAll ? data.getColumnList() : columnsNeed) {
            if ("1".equals(column.getIsTransfer())) {
                columns[column.getColumnIntIndex()] = column;
            }

            if (orderColumnInt == -1
                    && orderColumnName != null
                    && orderColumnName.equals(column.getColumnName())) {
                orderColumnInt = column.getColumnIntIndex();
            }
        }

        if (orderColumnInt != -1) {
            List<OrderRow> orderRows = new ArrayList<OrderRow>(data.getData().length);

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

                orderRows.add(new OrderRow(row[orderColumnInt], rowData));
            }

            Collections.sort(orderRows);

            if ("desc".equalsIgnoreCase(order)) {
                for (int i = orderRows.size() - 1; i >= 0; i--) {
                    rows.add(orderRows.get(i).row);
                }
            } else {
                for (int i = 0, size = orderRows.size(); i < size; i++) {
                    rows.add(orderRows.get(i).row);
                }
            }
        } else {
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
        }

        return rows;
    }

    private String rows2Xml(List<List<Map<String, String>>> rowsList) {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("result");

        Element rowsE;
        Element rowE;

        for (List<Map<String, String>> rows : rowsList) {
            rowsE = rootElement.addElement("rows");

            for (Map<String, String> row : rows) {
                rowE = rowsE.addElement("row");

                for (Map.Entry<String, String> entry : row.entrySet()) {
                    rowE.addAttribute(entry.getKey(), entry.getValue());
                }
            }
        }

        return document.asXML();
    }
}
