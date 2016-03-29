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
import com.rule.data.util.DebugUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ServiceHandler implements Handler {

    @Override
    public HttpResponse handleReq(String content) throws Throwable {
        ServiceReqInfo reqInfo = DataUtil.parse(content, ServiceReqInfo.class);
        Integer serviceID = reqInfo.getServiceID();
        final String format = reqInfo.getFormat();
        List<Integer> serviceIDList = reqInfo.getServiceIDList();
        final Map<String, Object> param = reqInfo.getParam();
        final Map<String, Map<String, Object>> paramList = reqInfo.getParamList();
        String serviceName = reqInfo.getServiceName();
        List<String> serviceNameList = reqInfo.getServiceNameList();
        boolean isDbLangZH = true;
        final String orderColumnName = reqInfo.getOrderColumnName();
        final String order = reqInfo.getOrder();

        if (reqInfo.getDbLang() != null) {
            isDbLangZH = reqInfo.getDbLang().trim().equalsIgnoreCase("zh");
        }

        boolean getByID = false;

        if (serviceName == null && (serviceNameList == null || serviceNameList.isEmpty())) {    // ID 转为Name
            getByID = true;
            if (serviceID != null) {            // 如果是ID单独调用
                serviceName = Services.id2Name(serviceID);
                if (serviceName == null) {
                    throw new RengineException(null, "数据源未找到, id=" + serviceID);
                }
            } else if (serviceIDList != null && !serviceIDList.isEmpty()) { // 如果是ID组合调用
                serviceNameList = new ArrayList<String>(serviceIDList.size());
                for (Integer id : serviceIDList) {
                    String name = Services.id2Name(id);
                    if (name != null) {
                        serviceNameList.add(name);
                    } else {
                        throw new RengineException(null, "数据源未找到, id=" + id);
                    }
                }
            }
        }

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        boolean needAll = "1".equals(reqInfo.getNeedAll());

        if (serviceNameList != null && !serviceNameList.isEmpty()) {    //组合调用
            response.setHeader(CONTENT_TYPE, "application/json");
            ServiceRespInfo respInfo = new ServiceRespInfo();

            Map<String, List<Map<String, String>>> rowsList = new HashMap<String, List<Map<String, String>>>();
            List<Map<String, String>> row;
            D2Data data;
            for (String _serviceName : serviceNameList) {
                final String mappingService = Services.getServiceMapping(_serviceName);
                SerService info;
                if (mappingService != null) {
                    info = Services.getService(mappingService);
                    if (info == null) {
                        throw new RengineException(_serviceName, "映射数据源未找到, " + mappingService);
                    }

                    if ("LS".equals(info.getState())) {
                        throw new RengineException(_serviceName, "映射数据源已失效, " + mappingService);
                    }
                } else {
                    info = Services.getService(_serviceName);

                    try {
                        DebugUtil.debugNull(_serviceName, content, "组合调用");  //llcheng 监控空调用
                    } catch (RengineException re) {
                        throw re;
                    } catch (Exception e) {
                        //
                    }

                    if (info == null) {
                        throw new RengineException(_serviceName, "数据源未找到");
                    }

                    if ("LS".equals(info.getState())) {
                        throw new RengineException(_serviceName, "数据源已失效");
                    }
                }


                if (getByID) {
                    if (paramList != null) {
                        data = DataService.getD2Data(info, paramList.get(String.valueOf(info.getServiceID())));
                    } else {
                        data = DataService.getD2Data(info, null);
                    }

                    row = data2Rows(data, info.getColumns(), needAll, isDbLangZH);

                    rowsList.put(String.valueOf(info.getServiceID()), row);
                } else {
                    if (paramList != null) {
                        data = DataService.getD2Data(info, paramList.get(info.getName()));
                    } else {
                        data = DataService.getD2Data(info, null);
                    }

                    row = data2Rows(data, info.getColumns(), needAll, isDbLangZH);
                    rowsList.put(info.getName(), row);
                }
            }

            respInfo.setRowsList(rowsList);
            response.setContent(ChannelBuffers.wrappedBuffer(JSON.toJSONString(respInfo).getBytes()));
        } else {
            final String mappingService = Services.getServiceMapping(serviceName);
            SerService info;
            if (mappingService != null) {
                info = Services.getService(mappingService);
                if (info == null) {
                    throw new RengineException(serviceName, "映射数据源未找到, " + mappingService);
                }

                if ("LS".equals(info.getState())) {
                    throw new RengineException(serviceName, "映射数据源已失效, " + mappingService);
                }
            } else {
                info = Services.getService(serviceName);

                try {
                    DebugUtil.debugNull(serviceName, content, null);//llcheng 监控空调用
                } catch (RengineException re) {
                    throw re;
                } catch (Exception e) {
                    //
                }

                if (info == null) {
                    throw new RengineException(serviceName, "数据源未找到");
                }

                if ("LS".equals(info.getState())) {
                    throw new RengineException(serviceName, "数据源已失效");
                }
            }

            D2Data data = DataService.getD2Data(info, param);

            if (format.equalsIgnoreCase("xml")) {
                response.setHeader(CONTENT_TYPE, "text/xml");

                String xml = data2Xml(data2Rows(data, info.getColumns(), needAll, isDbLangZH, orderColumnName, order));
                response.setContent(ChannelBuffers.wrappedBuffer(xml.getBytes()));
            } else if (format.equalsIgnoreCase("excel")) {
                response.setHeader(CONTENT_TYPE, "application/csv;charset=gbk");
                response.setHeader("Content-Disposition", "attachment;filename=\"" +
                        URLEncoder.encode(info.getName(), "utf8") + ".csv\"");

                ChannelBuffer buffer = data2Excel(data, info.getColumns(), needAll);
                response.setContent(buffer);
            } else if (format.equalsIgnoreCase("json")) {
                response.setHeader(CONTENT_TYPE, "application/json");
                ServiceRespInfo respInfo = new ServiceRespInfo();
                respInfo.setRows(data2Rows(data, info.getColumns(), needAll, isDbLangZH, orderColumnName, order));

                response.setContent(ChannelBuffers.wrappedBuffer(JSON.toJSONString(respInfo).getBytes()));
            } else {
                throw new RengineException(serviceName, "数据格式不支持, " + format);
            }
        }

        return response;
    }

    /**
     * 将d2data转为List<Map>格式, 可灵活转为csv和xml
     *
     * @param data
     * @param columnsNeed
     * @param needAll
     * @param isDbLangZH
     * @param orderColumnName
     * @param order
     * @return
     * @throws Exception
     */
    private List<Map<String, String>> data2Rows(D2Data data, List<SerColumn> columnsNeed
            , boolean needAll
            , boolean isDbLangZH
            , String orderColumnName
            , String order) throws Exception {
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

    private List<Map<String, String>> data2Rows(D2Data data, List<SerColumn> columnsNeed
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


    private ChannelBuffer data2Excel(D2Data data, List<SerColumn> columnsNeed, boolean needAll) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final int maxRow = data.getData().length;

        if (maxRow != 0) {
            int maxColumn = data.getData()[0].length;
            SerColumn[] columns = new SerColumn[maxColumn];

            for (SerColumn column : needAll ? data.getColumnList() : columnsNeed) {
                if ("1".equals(column.getIsTransfer())) {
                    columns[column.getColumnIntIndex()] = column;
                }
            }

            for (int i = 0; i < maxColumn; i++) {
                final SerColumn column = columns[i];
                if (column != null) {
                    writeString(column.getColumnIndex() + "-" + column.getColumnName(), baos);
                }
            }

            writeLine(baos);

            for (int i = 0; i < data.getData().length; i++) {
                final Object[] row = data.getData()[i];

                for (int j = 0; j < row.length; j++) {
                    if (columns[j] == null) {
                        continue;
                    }

                    final Object value = row[j];
                    writeString(DataUtil.getStringValue(value), baos);
                }
                writeLine(baos);
            }
        } else {
            writeString("无任何结果", baos);
            writeLine(baos);
        }

        baos.flush();
        return ChannelBuffers.wrappedBuffer(baos.toByteArray());
    }

    static byte[] buffd = ",".getBytes(Charset.forName("GBK"));
    static byte[] buffl = "\r\n".getBytes(Charset.forName("GBK"));

    private void writeString(String s, ByteArrayOutputStream baos) throws IOException {
        baos.write(("\"" + s.replace("\"", "\"\"") + "\"").getBytes(Charset.forName("GBK")));
        baos.write(buffd);
    }

    private void writeLine(ByteArrayOutputStream baos) throws IOException {
        baos.write(buffl);
    }


    private String data2Xml(List<Map<String, String>> rowsList) {
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("result");
        Element rowE;

        for (Map<String, String> rows : rowsList) {
            rowE = rootElement.addElement("row");

            for (Map.Entry<String, String> entry : rows.entrySet()) {
                rowE.addAttribute(entry.getKey(), entry.getValue());
            }
        }

        return document.asXML();
    }

}
