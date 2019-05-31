package com.micro.user.dao.ext;

import org.apache.ibatis.annotations.Param;

public interface UserWalletDao {

    public Integer  decreaseAmount(@Param("userId") Long userId,@Param("amount") Long amount);

    public Integer  decLockAmount(@Param("userId") Long userId,@Param("amount") Long amount);

    public Integer  backAmount(@Param("userId") Long userId,@Param("amount") Long amount);
}
