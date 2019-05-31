package com.micro.store.dao.ext;

import org.apache.ibatis.annotations.Param;

public interface GoodsDao {

    //减库存
    public Integer decreaseStock(@Param("goodsId") Long goodsId,@Param("count") Integer count);

    //减锁定的库存
    public Integer decLockStock(@Param("goodsId") Long goodsId,@Param("count") Integer count);

    //退还库存
    public Integer backStock(@Param("goodsId") Long goodsId,@Param("count") Integer count);
}
