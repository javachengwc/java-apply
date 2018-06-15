package com.es;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.get.GetField;

import com.google.gson.Gson;

public class ESCommon {

    //es的客户端实例
    Client client=null;
    {
        //连接单台机器，注意ip和端口号，不能写错
        client=new TransportClient().
                addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

    }

    public static void main(String[] args)throws Exception {
        ESCommon es=new ESCommon();
        //es.updatedoc();
        //es.getone();
        //es.deleteOne();
        es.indexOne();
    }

    /**delete one data**/
    public void deleteOne(){
        try{
            DeleteResponse de=client.prepareDelete("database", "table", "2").execute().actionGet();
            if(!de.isFound()){
                System.out.println("词条数据不存在！");
            }
            System.out.println("删除成功！");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**index one data**/
    public void updatedoc()throws Exception{
        UpdateRequest ur=new UpdateRequest();
        ur.index("database");
        ur.type("table");
        ur.id("1");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user","更新的用户");
        data.put("message","我也要更新了呀");
        ur.doc(data);
        client.update(ur);
        System.out.println("更新成功！");
    }

    /**get one data**/
    public void getone()throws Exception{
        GetResponse response = client.prepareGet("database", "table", "22")
                .execute()
                .actionGet();
        if(!response.isExists()){
            System.out.println("数据不存在！ ");
            return;
        }
        Map<String, Object> source = response.getSource();
        for(Entry<String, Object> eo:source.entrySet()){
            System.out.println(eo.getKey()+"  "+eo.getValue());
        }
        Map<String, GetField> fields = response.getFields();
        if(fields!=null){
            for(Entry<String, GetField> s:fields.entrySet()){
                System.out.println(s.getKey());
            }

        }else{
            System.out.println("fields is null;");
        }
        client.close();
    }

    /**index one data**/
    public void indexOne()throws Exception{

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user","kimchy");
        data.put("postDate",new Date());
        data.put("message","trying out Elasticsearch");
        Gson g=new Gson();
        String json=g.toJson(data);
        //得到一个json串
        IndexResponse ir=client.prepareIndex("database", "table", "23").setSource(json).execute().actionGet()               ;

        String index_name=ir.getIndex();
        String index_type=ir.getType();
        String docid=ir.getId();
        long version=ir.getVersion();

        System.out.println("索引名： "+index_name+"  ");
        System.out.println("索引类型： "+index_type+"  ");
        System.out.println("docid： "+docid+"  ");
        System.out.println("版本号： "+version+"  ");

        client.close();
        System.out.println("连接成功！");

    }

}