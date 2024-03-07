package com.otd.boot.plat.service.impl;

import com.otd.boot.plat.dao.mapper.StorageMapper;
import com.otd.boot.plat.model.entity.Storage;
import com.otd.boot.plat.model.entity.StorageExample;
import com.otd.boot.plat.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageMapper storageMapper;

    @Override
    public Storage queryByStorageCode(String storageCode) {
        StorageExample example = new StorageExample();
        example.createCriteria().andStorageCodeEqualTo(storageCode);
        List<Storage> list = storageMapper.selectByExample(example);
        if(list!=null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
