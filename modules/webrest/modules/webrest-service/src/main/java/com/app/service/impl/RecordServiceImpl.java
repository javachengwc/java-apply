package com.app.service.impl;

import com.app.model.Record;
import com.app.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

    private static Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);

    public Integer insertRecord(Record record)
    {
        return 1;
    }

    public Record getById(Integer id)
    {

        logger.info("RecordService getById invoked,id="+id);
        Record record = new Record();
        record.setId(id);
        record.setInfo("info");
        record.setUid("uid");
        return  record;
    }
}
