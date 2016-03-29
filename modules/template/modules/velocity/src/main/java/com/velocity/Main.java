package com.velocity;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String args []) throws Exception
    {
        String querySql="select * from user_daily_stat where 1=1 #if($partitiontime && $partitiontime!='') and partitiontime='${partitiontime}' #end #if($user_id && $user_id!='') and user_id=$user_id #end";
        Map<String, Object> param=new HashMap<String, Object>();
        //param.put("partitiontime","哈哈");
        param.put("user_id",0);

        String rt =SqlAssembler.renderSql(querySql,param);

        System.out.println(rt);
    }
}
