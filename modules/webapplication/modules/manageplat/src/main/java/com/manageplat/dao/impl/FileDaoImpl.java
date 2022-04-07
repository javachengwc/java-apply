package com.manageplat.dao.impl;

import com.manageplat.dao.FileDao;
import com.manageplat.model.mapper.FileRecordMapper;
import com.manageplat.model.pojo.FileRecord;
import com.manageplat.model.pojo.FileRecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FileDaoImpl implements FileDao {

    @Autowired
    private FileRecordMapper mapper;

    public FileRecord addFile(FileRecord fileRecord) {
        mapper.insert(fileRecord);
        return fileRecord;
    }

    public FileRecord get(Integer id)
    {
        return mapper.selectByPrimaryKey(id);
    }

    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    public List<FileRecord> queryPage(Integer type, int start, int pageSize)
    {

        FileRecordExample example = new FileRecordExample();
        if(type!=null)
        {
            FileRecordExample.Criteria criteria = example.createCriteria();
            criteria.andTypeEqualTo(type);
        }
        example.setOrderByClause(" id desc limit "+start+","+pageSize+" ");
        return mapper.selectByExample(example);

    }

    public int count(Integer type)
    {
        FileRecordExample example = new FileRecordExample();
        if(type!=null)
        {
            FileRecordExample.Criteria criteria = example.createCriteria();
            criteria.andTypeEqualTo(type);
        }
        return mapper.countByExample(example);
    }

}
