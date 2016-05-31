package com.shopstat.model.vo.stat;

import com.util.StringUtil;
import com.util.date.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 统计类
 */
public class Stat implements Serializable {

    //统计实体类
    public Class statClazz;

    //统计对应的表
    public String table;

    //统计日期
    public Date statDate;

    //统计日期field名
    public String dateName;

    //统计日期列名
    public String dateColumn;

    //统计的维度列表
    public List<Dimen> dimenList= new LinkedList<Dimen>();

    //统计的指标列表
    public List<Norm> normList =new LinkedList<Norm>();

    public String querySql;

    public Class getStatClazz() {
        return statClazz;
    }

    public void setStatClazz(Class statClazz) {
        this.statClazz = statClazz;
        this.table=StringUtil.field2Col(StringUtil.initialLower(statClazz.getSimpleName()));
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
        this.dateColumn=StringUtil.field2Col(dateName);
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public List<Dimen> getDimenList() {
        return dimenList;
    }

    public void setDimenList(List<Dimen> dimenList) {
        this.dimenList = dimenList;
    }

    public List<Norm> getNormList() {
        return normList;
    }

    public void setNormList(List<Norm> normList) {
        this.normList = normList;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getNormQueryFragment()
    {
        StringBuffer normQueryBuf = new StringBuffer("");
        for(Norm norm:getNormList())
        {
            normQueryBuf.append(norm.getQueryFragment()).append(",");
        }
        String normQueryStr = normQueryBuf.toString();
        if(normQueryStr.endsWith(","))
        {
            normQueryStr =normQueryStr.substring(0,(normQueryStr.length()-1));
        }
        return normQueryStr;
    }

    public String getDateCdnFragMent()
    {
        StringBuffer buf = new StringBuffer("");
        buf.append(dateColumn).append("=").append("'").append(DateUtil.formatDate(statDate,DateUtil.FMT_YMD)).append("'");
        return buf.toString();
    }

    //自动拼接sql
    public String combinSql(Stat stat,Integer [] combin)
    {
        //后面如果有复杂sql的情况，将扩展支持sql自定义
        StringBuffer dynDimenQueryBuf=new StringBuffer("");
        StringBuffer dynDimenGroupBuf=new StringBuffer("");
        StringBuffer dimenCndBuf=new StringBuffer("");
        for(int i=0;i<stat.getDimenList().size();i++)
        {
            Dimen dimen = stat.getDimenList().get(i);
            int total= combin[i];
            String queryFlag = dimen.getQueryFlagMent(total);
            String groupFlag =dimen.getGroupFlagment(total);
            dynDimenQueryBuf.append(queryFlag).append(",");
            if(!StringUtils.isBlank(groupFlag))
            {
                dynDimenGroupBuf.append(groupFlag).append(",");
            }
            dimenCndBuf.append(" and ").append(dimen.getCndFlagMent());
        }
        String dynDimenQuery = dynDimenQueryBuf.toString();
        if(dynDimenQuery.endsWith(","))
        {
            dynDimenQuery =dynDimenQuery.substring(0,(dynDimenQuery.length()-1));
        }
        String dynDimenGroup =dynDimenGroupBuf.toString();
        if(dynDimenGroup.endsWith(","))
        {
            dynDimenGroup =dynDimenGroup.substring(0,(dynDimenGroup.length()-1));
        }
        String dimenCndStr = dimenCndBuf.toString();
        boolean hasGroup=true;
        if(StringUtils.isBlank(dynDimenGroup))
        {
            hasGroup=false;
        }

        String normQueryFrag =stat.getNormQueryFragment();

        StringBuffer sqlBuf = new StringBuffer("");
        sqlBuf.append("select ").append(stat.getDateColumn()).append(",");
        sqlBuf.append(dynDimenQuery).append(",");
        sqlBuf.append(normQueryFrag).append(" ");
        sqlBuf.append(" from ").append(table).append(" ");
        sqlBuf.append(" where ").append(stat.getDateCdnFragMent());
        sqlBuf.append(dimenCndStr);
        if(hasGroup) {
            sqlBuf.append(" group by ").append(dynDimenGroup);
        }
        String sql =sqlBuf.toString();
        return sql;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
