package com.micro.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.store.dao.mapper.GoodsMapper;
import com.micro.store.model.pojo.Goods;
import com.micro.store.service.GoodsService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
}
