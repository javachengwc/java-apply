package com.app.apl;

import com.app.api.rest.RestResource;
import com.app.annotation.RestService;
import com.app.model.Record;
import com.app.model.vo.RspData;
import com.app.service.RecordService;
import com.app.service.ServiceFactory;
import com.util.AppUtil;
import com.util.base.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Controller
@RestService
public class RestResourceImpl implements RestResource {

    private static Logger logger = LoggerFactory.getLogger(RestResourceImpl.class);

    @Autowired
    private RecordService recordService;

    public RecordService getRecordService() {
        return ServiceFactory.getRecordService();
    }

    public RspData<String> def()
    {
        logger.info("RestResource def invoked..........");
        return new RspData<String>(0,"success","");
    }

    public RspData<Map<String,Object>> status()
    {
        logger.info("RestResource status invoked..........");
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("os",AppUtil.getOsName());
        map.put("ip",AppUtil.getLocalHost());
        map.put("cpu",AppUtil.getCpuCoreNumber());
        map.put("pid",AppUtil.getPid());
        return new RspData<Map<String,Object>>(0,"success",map);
    }

    public  RspData<Integer>  genRecord(Record record)
    {
        int rt =getRecordService().insertRecord(record);
        return new RspData<Integer>(0,"success",rt);
    }

    public  Record  getRecord(String recordId)
    {
        if(NumberUtil.isNumeric(recordId)) {
            return getRecordService().getById(Integer.parseInt(recordId));
        }
        return null;
    }

    public  Record  queryRecord( String recordId)
    {
        if(NumberUtil.isNumeric(recordId)) {
            return getRecordService().getById(Integer.parseInt(recordId));
        }
        return null;
    }

    public  Record  queryRecordByRecord(Record record)
    {
        logger.info("queryRecordByRecord invoked..........");
        Integer recordId =record.getId();
        if(recordId!=null)
        {
            return getRecordService().getById(recordId);
        }
        return null;
    }
}
