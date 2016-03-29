package com.thrift.service;

import com.thrift.finagle.trans.Result;
import com.thrift.finagle.trans.StringResult;
import com.thrift.finagle.trans.TransDealService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 具体的thrift服务类的实现类
 */
@Service
public class TransServiceImpl implements TransDealService.Iface,InitializingBean {

    public StringResult transDeal(int dealId) throws TException
    {
        System.out.println("-----TransServiceImpl transDeal invoked,dealId="+dealId);
        StringResult result =new  StringResult();
        result.setValue(""+dealId);
        result.setExtend("ext");
        System.out.println("-----TransServiceImpl transDeal rt="+result);
        return result;
    }

    public Result batchTransDeal(List<Integer> dealIdList) throws TException
    {
        System.out.println("-----TransServiceImpl batchTransDeal invoked,dealIdList="+dealIdList);
        int size = (dealIdList==null)?0:dealIdList.size();
        Result result = new Result();
        result.setCode(size);
        System.out.println("-----TransServiceImpl batchTransDeal rt="+result);
        return result;
    }

    public void afterPropertiesSet() throws Exception {

    }
}
