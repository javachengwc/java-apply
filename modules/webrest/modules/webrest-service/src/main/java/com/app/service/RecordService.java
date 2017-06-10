package com.app.service;

import com.app.model.Record;

public interface RecordService {

    public Integer insertRecord(Record record);

    public Record getById(Integer id);
}
