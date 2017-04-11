package com.boot.service;

import com.boot.dao.mapper.UuMapper;
import com.boot.model.Uu;
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
