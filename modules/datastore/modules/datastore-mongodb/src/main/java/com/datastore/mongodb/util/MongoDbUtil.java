package com.datastore.mongodb.util;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MongoDbUtil {

    //获取db
    public MongoDatabase getDb(MongoClient mongoClient,String dbName) {
        if(StringUtils.isBlank(dbName)) {
            return null;
        }
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database;
    }

    //删除db
    public void dropDb(MongoClient mongoClient,String dbName) {
        MongoDatabase db= getDb(mongoClient,dbName);
        if(db!=null) {
            db.drop();
        }
    }

    //获取collection
    public MongoCollection<Document> getCollection(MongoClient mongoClient,String dbName, String collectionName) {
        if(StringUtils.isBlank(dbName) ||StringUtils.isBlank(collectionName) ) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
        return collection;
    }

    //创建collection
    public void createCollection(MongoDatabase db,String collectionName) {
        db.createCollection(collectionName);
    }

    //删除collection
    public void dropCollection(MongoClient mongoClient,String dbName, String collectionName) {
        MongoCollection<Document> collection=getCollection(mongoClient,dbName,collectionName);
        if(collection!=null) {
            collection.drop();
        }
    }

    //获取db列表
    public List<String> getAllDbs(MongoClient mongoClient) {
        MongoIterable<String> dbs = mongoClient.listDatabaseNames();
        List<String> list = new ArrayList<String>();
        for (String per : dbs) {
            list.add(per);
        }
        return list;
    }

    //查询db下的所有表
    public List<String> getAllCollections(MongoClient mongoClient,String dbName) {
        MongoDatabase db = getDb(mongoClient, dbName);
        if(db==null) {
            return Collections.EMPTY_LIST;
        }
        MongoIterable<String> colls =db.listCollectionNames();
        List<String> list = new ArrayList<String>();
        for (String per : colls) {
            list.add(per);
        }
        return list;
    }

    //关闭Mongodb
    public void close(MongoClient mongoClient) {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    //根据id查询
    public Document getById(MongoCollection<Document> coll, String id) {
        ObjectId obj =new ObjectId(id);
        Document doc = coll.find(Filters.eq("_id", obj)).first();
        return doc;
    }

    //根据id删除
    public int deleteById(MongoCollection<Document> coll, String id) {
        int count = 0;
        ObjectId obj =new ObjectId(id);
        Bson filter = Filters.eq("_id", obj);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    //根据id更新
    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId obj =new ObjectId(id);
        Bson filter = Filters.eq("_id", obj);
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }

    public int getAllCount(MongoCollection<Document> coll) {
        int count = (int) coll.count();
        return count;
    }

    //条件查询
    public MongoCursor<Document> query(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    //分页查询
    public MongoCursor<Document> queryPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
        Bson orderBy = new BasicDBObject("_id", 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }
}
