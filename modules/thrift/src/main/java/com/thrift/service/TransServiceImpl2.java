package com.thrift.service;

import com.thrift.finagle.trans.Result;
import com.thrift.finagle.trans.StringResult;
import com.thrift.finagle.trans.TransDealService;
import com.twitter.util.Future;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 具体的thrift服务类的实现类
 */
@Service
public class TransServiceImpl2  implements TransDealService.ServiceIface {

    public Future<StringResult> transDeal(int dealId)
    {
        System.out.println("-----TransServiceImpl transDeal invoked,dealId="+dealId);
        StringResult result =new  StringResult();
        result.setValue(""+dealId);
        result.setExtend("ext");
        System.out.println("-----TransServiceImpl transDeal rt="+result);
        return Future.value(result);
    }

    public Future<Result> batchTransDeal(List<Integer> dealIdList)
    {
        System.out.println("-----TransServiceImpl batchTransDeal invoked,dealIdList="+dealIdList);
        int size = (dealIdList==null)?0:dealIdList.size();
        Result result = new Result();
        result.setCode(size);
        System.out.println("-----TransServiceImpl batchTransDeal rt="+result);
        return Future.value(result);
    }
}