package com.rule.data.service.preview;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rule.data.exception.RengineException;
import com.rule.data.handler.Handler;
import com.rule.data.model.SerColumn;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.model.vo.InvokeNode;
import com.rule.data.model.vo.OrderRow;
import com.rule.data.service.core.Cache4BaseService;
import com.rule.data.service.core.DataService;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.ProcessInfos;
import com.rule.data.service.core.Services;
import com.rule.data.util.LogUtil;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class PreviewHandler implements Handler {

    @Override
    public HttpResponse handleReq(String content) throws Throwable {
        PreviewReqInfo reqInfo = DataUtil.parse(content, PreviewReqInfo.class);
        final Integer serviceID = reqInfo.getServiceID();
        final SerService info;
        final Map<String, Object> param = reqInfo.getParam();
        final Integer previewCount = reqInfo.getPreviewCount();

        if (reqInfo.getPreviewType() == 1) {
            info = Services.getService(Services.id2Name(serviceID));
        } else {
            info = reqInfo.getInfo();

            if (info.getBaseServiceID() != null) {
                String columnIndex = "A";

                SerService basePo = Services.getService(Services.id2Name(info.getBaseServiceID()));
                if (basePo == null) {
                    throw new
                            RengineException("基础数据源没找到, id=" + info.getBaseServiceID()
                            , info.getName());
                }

                for (SerColumn po : basePo.getColumns()) {
                    if (DataUtil.countIndex(columnIndex) < DataUtil.countIndex(po.getColumnIndex())) {
                        columnIndex = po.getColumnIndex();
                    }
                }

                info.setType("advance");
                for (SerColumn po : info.getColumns()) {
                    columnIndex = DataUtil.getNextColumnIndex(columnIndex);
                    po.setColumnIndex(columnIndex);
                    po.setColumnName("预览");
                }
            } else {
                info.setType("base");
                String columnIndex = "A";
                for (SerColumn po : info.getColumns()) {
                    po.setColumnIndex(columnIndex);
                    columnIndex = DataUtil.getNextColumnIndex(columnIndex);
                }
            }
        }

        if (info == null) {
            throw new RengineException(null, "数据源未找到, id=" + serviceID);
        }


        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        response.setHeader(CONTENT_TYPE, "application/json");

        PreviewRespInfo respInfo = new PreviewRespInfo();
        respInfo.setRows(data2Rows(DataService.getD2Data(info, param), previewCount
                , reqInfo.getOrderColumnName(), reqInfo.getOrder()));
        respInfo.setProcessInfo(convert(ProcessInfos.getInvokeNode()));

        if ("base".equals(info.getType())) {
            // 基础数据源执行计划
            String serviceName = info.getName();
            String sql = info.getSql();
            String dbID = info.getDbID();
            try{
               List<LinkedHashMap<String, Object>> explains = Cache4BaseService.explain(serviceName, sql, dbID, param);
               if(explains!=null){
                   if(explains.size()!=0){
                       respInfo.setExplains(explains);
                   }
               }
            }catch (Exception e){
                LogUtil.info("执行计划:"+serviceName+"失败,@"+e.getMessage());
            }
        }

        response.setContent(ChannelBuffers.wrappedBuffer(JSON.toJSONString(
                respInfo, SerializerFeature.DisableCircularReferenceDetect).getBytes()));

        return response;
    }

    private ProcessInfo convert(InvokeNode root) {
        ProcessInfo processInfo = new ProcessInfo();

        if (root != null) {
            bfs(root, processInfo.getParamRequired()
                    , processInfo.getParamROptional()
                    , processInfo.getServiceNames());
            processInfo.setRoot(root);
        }

        return processInfo;
    }

    static final int TREE_NODE_MAX = 100;

    private void bfs(InvokeNode root
            , Set<String> paramRequired
            , Set<String> paramROptional, Set<String> serviceNames) {
        if (root == null) {
            return;
        }

        LinkedList<InvokeNode> queue = new LinkedList<InvokeNode>();
        ArrayList<InvokeNode> lst = new ArrayList<InvokeNode>();
        queue.offer(root);
        int cnt = 0;

        while (!queue.isEmpty()) {
            cnt++;
            InvokeNode cur = queue.poll();

            if (cnt <= TREE_NODE_MAX) {
                lst.add(cur);
            }

            paramRequired.addAll(getParamRequired(cur.getNodeService().getSql()));
            paramROptional.addAll(getParamOptional(cur.getNodeService().getSql()));
            serviceNames.add(cur.getNodeService().getName());

            for (InvokeNode child : cur.getChilds()) {
                queue.offer(child);
            }
        }

        root.setNodeSum(cnt);

        if (cnt > TREE_NODE_MAX) {
            for (int i = 0; i < lst.size(); i++) {
                Iterator<InvokeNode> ite = lst.get(i).getChilds().iterator();

                for (; ite.hasNext(); ) {
                    InvokeNode cur = ite.next();

                    if (!lst.contains(cur)) {
                        ite.remove();
                    }
                }
            }
        }
    }

    private Collection<? extends String> getParamRequired(String sql) {
        Set<String> set = new HashSet<String>();

        if (sql == null) {
            return set;
        }

        Pattern p = Pattern.compile("#[\\u4e00-\\u9fa5a-zA-Z0-9_-]+#");
        Matcher m = p.matcher(sql);

        while (m.find()) {
            String str = m.group();
            str = str.substring(1, str.length() - 1);
            if (!set.contains(str)) {
                set.add(str);
            }
        }

        return set;
    }

    private Collection<? extends String> getParamOptional(String sql) {
        Set<String> set = new HashSet<String>();
        if (sql == null) {
            return set;
        }
        Pattern pa = Pattern.compile("\\$[a-zA-Z]+[a-zA-Z0-9_-]*");
        Matcher ma = pa.matcher(sql);
        while (ma.find()) {
            String str = ma.group();
            str = str.substring(1, str.length());
            if (!set.contains(str)) {
                set.add(str);
            }
        }


        return set;
    }


    private List<Map<String, String>> data2Rows(D2Data data, Integer previewCount
            , String orderColumnName, String order) throws Exception {
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>(data.getData().length);
        if (data.getData().length == 0) {
            return rows;
        }

        Map<String, String> rowData;
        SerColumn[] columns = new SerColumn[data.getData()[0].length];
        int orderColumnInt = -1;

        for (SerColumn column : data.getColumnList()) {
            columns[column.getColumnIntIndex()] = column;

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
                    if (columns[j] != null) { // 需要该列
                        rowData.put(columns[j].getColumnIndex()
                                + "-"
                                + columns[j].getColumnName(), DataUtil.getStringValue(row[j]));
                    }
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
            for (int i = 0; i < data.getData().length && i < previewCount; i++) {
                final Object[] row = data.getData()[i];
                rowData = new LinkedHashMap<String, String>();

                for (int j = 0; j < row.length; j++) {
                    if (columns[j] != null) { // 需要该列
                        rowData.put(columns[j].getColumnIndex()
                                + "-"
                                + columns[j].getColumnName(), DataUtil.getStringValue(row[j]));
                    }
                }

                rows.add(rowData);
            }
        }

        return rows;
    }
}
