package com.manageplat.service.tongyong;

import com.manageplat.assemb.SqlAssembler;
import com.manageplat.dao.tongyong.TyQueryDao;
import com.manageplat.exception.TyException;
import com.manageplat.model.pojo.TyEntity;
import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.pojo.TyEntityItem;
import com.manageplat.model.vo.tongyong.TyResultVo;
import com.util.NumberUtil;
import com.util.StringUtil;
import com.util.page.Page;
import com.util.page.PageQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.enums.StringCodedLabeledEnum;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用查询服务类
 */
@Service
public class TyQueryService {

    private static Logger logger = LoggerFactory.getLogger(TyQueryService.class);

    //limit ?,? 或LIMIT ?,? 结尾的正则表达式
    public static String limitStr="(limit|LIMIT)\\s+(\\?)\\s*,\\s*(\\?)\\s*$";

    //查询开头select ...from部分的正则表达式
    public static String queryPreStr="^(select|SELECT)(.*?)(from|FROM)";

    @Autowired
    private EntityItemService entityItemService;

    @Autowired
    private EntityCdnService entityCdnService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private TyQueryDao tyQueryDao;

    @Autowired
    private SingleQueryService singleQueryService;

    public TyResultVo queryPage(int  entityId,Map<String,Object> map) throws Exception
    {
        TyEntity entity = entityService.getById(entityId);

        //查询sql
        String queryStr= entity.getQuerySql();

        if(StringUtils.isBlank(queryStr))
        {
             //简单查询
            return singleQueryService.queryPage(entity,map);
        }

        //sql参数设置
        String realQueryStr= SqlAssembler.renderSql(queryStr, map);

        int limitIndex= realQueryStr.lastIndexOf("limit");
        if(limitIndex<0)
        {
            limitIndex = realQueryStr.lastIndexOf("LIMIT");
        }
        if(limitIndex<0)
        {
            realQueryStr=realQueryStr+" limit "+map.get("start")+","+map.get("pageSize");
            limitIndex= realQueryStr.lastIndexOf("limit");
        }

        //计算总数语句
        String tmp = realQueryStr.substring(0,limitIndex);

        String realCountSql="";
        Pattern ptn2 = Pattern.compile(queryPreStr);
        Matcher matcher2 =ptn2.matcher(tmp);
        if(matcher2.find())
        {
            realCountSql = tmp.replace(matcher2.group(2)," count(1) ");

        }else
        {
            logger.error("TyQueryService queryPage 解析计算总数sql异常,tmp="+tmp);
            throw new TyException("解析计算总数sql异常");
        }

        List<Map<String,Object>> list =tyQueryDao.queryList(realQueryStr);
        int count = tyQueryDao.count(realCountSql);

        List<TyEntityCdn>  cdnList= entityCdnService.queryEntityCdn(entityId);
        List<TyEntityItem> itemList= entityItemService.queryEntityItem(entityId);

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
