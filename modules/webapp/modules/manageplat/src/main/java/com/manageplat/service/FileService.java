package com.manageplat.service;

import com.manageplat.model.pojo.FileRecord;

import java.util.List;

public interface FileService {

    public FileRecord addFile(FileRecord fileRecord);

    public FileRecord get(Integer id);

    public void delete(int id);

    public List<FileRecord> queryPage(Integer type,int start,int pageSize);

    public int count(Integer type);
}
