package com.shop.server.service.impl;

import com.shop.server.dao.mapper.TbUuMapper;
import com.shop.server.dao.mapper.UuMapper;
import com.shop.server.model.Uu;
import com.shop.server.model.pojo.TbUu;
import com.shop.server.model.pojo.TbUuExample;
import com.shop.server.service.UuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UuServiceImpl  implements UuService {

    @Autowired
    private UuMapper uuMapper;

    @Autowired
    private TbUuMapper tbUuMapper;

    public Uu queryUu()
    {
        return uuMapper.queryUu();
    }

    public Uu queryTbUu()
    {
        TbUuExample example = new TbUuExample();
        List<TbUu> list =tbUuMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            TbUu tbUu = list.get(0);
            Uu uu = new Uu();
            uu.setId(tbUu.getId());
            uu.setUsername(tbUu.getUsername());
            return uu;
        }
        return null;
    }
}
