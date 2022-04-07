package com.manageplat.service.tongyong;

import com.manageplat.dao.tongyong.TyQueryDao;
import com.manageplat.exception.TyException;
import com.manageplat.model.pojo.TyEntity;
import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.pojo.TyEntityItem;
import com.manageplat.model.vo.tongyong.TyResultVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单查询
 */
@Service
public class SingleQueryService {

    public static Logger logger = LoggerFactory.getLogger(SingleQueryService.class);

    @Autowired
    private EntityItemService entityItemService;

    @Autowired
    private EntityCdnService entityCdnService;

    @Autowired
    private TyQueryDao tyQueryDao;

    public TyResultVo queryPage(TyEntity entity,Map<String,Object> map) throws Exception
    {

        //查询sql
        String queryStr= "";

        String table =entity.getTableName();

        StringBuffer buf = new StringBuffer();
        buf.append("select * from ").append(table).append(" ");

        List<TyEntityCdn> cdnList= entityCdnService.queryEntityCdn(entity.getId());
        boolean hasWhere=false;
        if(cdnList!=null && cdnList.size()>0)
        {
            //目前只支持简单的等于
            for(int i=0;i<cdnList.size();i++)
            {
                TyEntityCdn cdn=cdnList.get(i);

                String colName = cdn.getCdnCol();
                Object value =map.get(colName);

                if(value==null || StringUtils.isBlank(value.toString()))
                {
                    //没值 就不组装到查询条件中
                    continue;
                }

                String val = value.toString();
                if(cdn.getCdnType()!=null && cdn.getCdnType()==1)
                {
                    val="'"+val+"'";
                }

                if(!hasWhere)
                {
                    buf.append(" where ");
                }else
                {
                    buf.append(" and ");
                }

                buf.append(colName).append("=").append(val);
            }
        }

        buf.append(" limit "+map.get("start")+","+map.get("pageSize"));

        queryStr=buf.toString();

        String tmp = queryStr.substring(0,queryStr.indexOf("limit"));

        String countStr = "";

        Pattern ptn2 = Pattern.compile(TyQueryService.queryPreStr);
        Matcher matcher2 =ptn2.matcher(tmp);
        if(matcher2.find())
        {
            countStr = tmp.replace(matcher2.group(2)," count(1) ");

        }else
        {
            logger.error("SingleQueryService queryPage 解析计算总数sql异常,tmp="+tmp);
            throw new TyException("解析计算总数sql异常");
        }

        List<Map<String,Object>> list =tyQueryDao.queryList(queryStr);
        int count = tyQueryDao.count(countStr);

        List<TyEntityItem> itemList= entityItemService.queryEntityItem(entity.getId());

        TyResultVo result = new TyResultVo();
        result.setEntity(entity);
        result.setEntityCdn(cdnList);
        result.setEntityItem(itemList);
        result.setList(list);
        result.setCount(count);
        result.setParamMap(map);

        return result;
    }
}
