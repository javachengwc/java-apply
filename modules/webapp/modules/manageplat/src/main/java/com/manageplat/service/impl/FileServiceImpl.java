package com.manageplat.service.impl;

import com.manageplat.dao.FileDao;
import com.manageplat.model.pojo.FileRecord;
import com.manageplat.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao fileDao;

    public FileRecord addFile(FileRecord fileRecord)
    {
        return fileDao.addFile(fileRecord);
    }

    public FileRecord get(Integer id)
    {
        return fileDao.get(id);
    }

    public void delete(int id)
    {
        fileDao.delete(id);
    }

    public int count(Integer type)
    {
        return fileDao.count(type);
    }

    public List<FileRecord> queryPage(Integer type,int start,int pageSize)
    {
        return fileDao.queryPage(type,start,pageSize);
    }
}
