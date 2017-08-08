package com.datastore.mongodb.service;

import com.datastore.mongodb.model.Info;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

    private static Logger logger= LoggerFactory.getLogger(InfoService.class);

    @Autowired
    private DBCollection mongoDbCollection;

    private Gson gson = new Gson();

    public Boolean  saveInfo(Info info) {
        BasicDBObject obj = new BasicDBObject();
        obj.put("no", info.getNo());
        obj.put("name",info.getName());
        obj.put("data",info.getData());
        obj.put("cnt", info.getCnt());
        WriteResult result=mongoDbCollection.insert(obj);
        logger.info("InfoService saveInfo result ={}",result);
        return  true;
    }

    public Info queryByNo(String no) {
        DBObject obj = mongoDbCollection.findOne(new BasicDBObject("no", no));
        if(obj ==null ) {
            return null;
        }
        logger.info("InfoService queryByNo end,no={},obj={}",no,obj);
        Info info =gson.fromJson(obj.toString(),Info.class);
        return info;
    }

    public Boolean delInfo(String no) {
        DBObject cdnObj= new BasicDBObject("no", no);
        WriteResult result =mongoDbCollection.remove(cdnObj);
        logger.info("InfoService delInfo end ,no={},result={}",no,result);
        return true;
    }

    public Boolean uptInfo(Info info) {
        String no =info.getNo();
        DBObject queryObj = new BasicDBObject("no",no);
        BasicDBObject setObj = new BasicDBObject();
        if(info.getName()!=null) {
            setObj.put("name", info.getName());
        }
        if(info.getData()!=null) {
            setObj.put("data", info.getData());
        }
        if(info.getCnt()!=null) {
            setObj.put("cnt", info.getCnt());
        }
        BasicDBObject doc = new BasicDBObject();
        doc.put("$set", setObj);
        mongoDbCollection.update(queryObj,doc);
        return true;
    }
}
