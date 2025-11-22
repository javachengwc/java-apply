package com.boot3.service;

import com.boot3.dao.mapper.UuMapper;
import com.boot3.model.Uu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UuService {

    @Autowired
    private UuMapper uuMapper;

    public Uu queryUu()
    {
        return uuMapper.queryUu();
    }
}
