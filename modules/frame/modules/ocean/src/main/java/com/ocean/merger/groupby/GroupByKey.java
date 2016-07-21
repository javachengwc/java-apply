package com.ocean.merger.groupby;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组结果集数据存储索引
 */
public class GroupByKey {

    private List<String> unionKey = new ArrayList<String>();

    public List<String> getUnionKey() {
        return unionKey;
    }

    public void setUnionKey(List<String> unionKey) {
        this.unionKey = unionKey;
    }

    public void append(String key) {
        unionKey.add(key);
    }

    @Override
    public int hashCode()
    {
        int code=0;
        int count = (unionKey==null)?0:unionKey.size();
        if(count==0)
        {
            return code;
        }

        for(String per:unionKey)
        {
            code+=per.hashCode();
        }
        code = code/count+count;
        return code;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj==null)
        {
            return false;
        }
        if(obj instanceof GroupByKey)
        {
            GroupByKey objKey =(GroupByKey)obj;
            int thCnt =  (unionKey==null)?0:unionKey.size();
            int objCnt =  (objKey.getUnionKey()==null)?0:objKey.getUnionKey().size();
            if(thCnt!=objCnt)
            {
                return false;
            }
            if(thCnt>0)
            {
                for(String per:unionKey)
                {
                    if(!objKey.getUnionKey().contains(per))
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
