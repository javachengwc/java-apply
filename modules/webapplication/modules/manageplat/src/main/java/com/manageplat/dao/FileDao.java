package com.manageplat.dao;

import com.manageplat.model.pojo.FileRecord;

import java.util.List;

public interface FileDao {

    public FileRecord addFile(FileRecord fileRecord);

    public FileRecord get(Integer id);

    public void delete(Integer id);

    public List<FileRecord> queryPage(Integer type,int start,int pageSize);

    public int count(Integer type);



}
