package com.micro.order.model.third;

import lombok.Data;

@Data
public class UserWalletReq {

    private Long userId;

    private Long changeAmount;

    public UserWalletReq() {

    }

    public UserWalletReq(Long userId,Long changeAmount) {
        this.userId=userId;
        this.changeAmount=changeAmount;
    }
}
