package com.shop.order.service;

import com.icbc.model.IcbcForm;
import com.shop.order.api.model.IcbcPayVo;

public interface OrderIcbcService {

    //工行e支付支付
    public IcbcPayVo icbcPay(IcbcPayVo icbcPayVo);

    public String packTranData(IcbcPayVo icbcPayVo);

    public IcbcForm createFormData(String paramString, IcbcPayVo icbcPayVo);

    public IcbcForm createFormData( IcbcPayVo icbcPayVo);

    public String buildRequest(IcbcForm form);
}
