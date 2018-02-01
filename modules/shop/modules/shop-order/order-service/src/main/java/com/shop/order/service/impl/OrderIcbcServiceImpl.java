package com.shop.order.service.impl;

import com.icbc.model.IcbcForm;
import com.shop.order.api.model.IcbcPayVo;
import com.shop.order.config.IcbcConfig;
import com.shop.order.service.OrderIcbcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderIcbcServiceImpl implements OrderIcbcService {

    private static Logger logger= LoggerFactory.getLogger(OrderIcbcServiceImpl.class);

    //工行e支付支付
    public IcbcPayVo icbcPay(IcbcPayVo icbcPayVo ) {
        logger.info("OrderIcbcServiceImpl icbcPay start,icbcPayVo={}",icbcPayVo);
        return  null;
    }

    public String packTranData(IcbcPayVo icbcPayVo) {
        return null;
    }

    public IcbcForm createFormData(String paramString, IcbcPayVo icbcPayVo) {
        return null;
    }

    public IcbcForm createFormData( IcbcPayVo icbcPayVo) {
        return null;
    }

    public String buildRequest(IcbcForm form) {
        try {
            String interfaceName=form.getInterfaceName();
            String merCert=form.getMerCert();
            String merSignMsg=form.getMerSignMsg();
            String tranData=form.getTranData();

            StringBuffer buf = new StringBuffer();
            buf.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><title>商户支付页面</title></head><body>");
            buf.append("<form id=\"icbcpaysubmit\" name=\"icbcpaysubmit\" action=\"" + IcbcConfig.gatewayUrl+ "\">");
            buf.append("<input type=\"hidden\" name=\"interfaceName\" value=\"" + interfaceName + "\"/>");
            buf.append("<input type=\"hidden\" name=\"interfaceVersion\" value=\"" + IcbcConfig.interfaceVersion + "\"/>");
            buf.append("<input type=\"hidden\" name=\"tranData\" value=\"" + tranData + "\"/>");
            buf.append("<input type=\"hidden\" name=\"merSignMsg\" value=\"" + merSignMsg + "\"/>");
            buf.append("<input type=\"hidden\" name=\"merCert\" value=\"" + merCert + "\"/>");
            buf.append("<input type=\"hidden\" name=\"HangSupportFlag\" value=\"" + IcbcConfig.hangSupportFlag + "\"/>");
            buf.append("<input type=\"hidden\" name=\"HangTimeInterval\" value=\"" + IcbcConfig.hangTimeInterval+ "\"/>");
            buf.append("<input type=\"submit\" value=\"提交\" style=\"display:none;\"></form>");
            buf.append("<script>document.forms['icbcpaysubmit'].submit();</script>");
            buf.append("</body></html>");
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
