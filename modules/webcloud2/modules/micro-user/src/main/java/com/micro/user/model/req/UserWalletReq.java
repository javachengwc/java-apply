package com.micro.user.model.req;

import lombok.Data;

@Data
public class UserWalletReq {

    private Long userId;

    private Long changeAmount;

}
