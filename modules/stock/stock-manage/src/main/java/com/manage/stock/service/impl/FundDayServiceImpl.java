package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.FundDayMapper;
import com.manage.stock.domain.FundDay;
import com.manage.stock.service.IFundDayService;

/**
 * 基金天数据Service业务层处理
 * 
 * @author gener
 * @date 2025-06-19
 */
@Service
public class FundDayServiceImpl implements IFundDayService 
{
    @Autowired
    private FundDayMapper fundDayMapper;

    /**
     * 查询基金天数据
     * 
     * @param id 基金天数据主键
     * @return 基金天数据
     */
    @Override
    public FundDay selectFundDayById(Long id)
    {
        return fundDayMapper.selectFundDayById(id);
    }

    /**
     * 查询基金天数据列表
     * 
     * @param fundDay 基金天数据
     * @return 基金天数据
     */
    @Override
    public List<FundDay> selectFundDayList(FundDay fundDay)
    {
        return fundDayMapper.selectFundDayList(fundDay);
    }

    /**
     * 新增基金天数据
     * 
     * @param fundDay 基金天数据
     * @return 结果
     */
    @Override
    public int insertFundDay(FundDay fundDay)
    {
        fundDay.setCreateTime(DateUtils.getNowDate());
        return fundDayMapper.insertFundDay(fundDay);
    }

    /**
     * 修改基金天数据
     * 
     * @param fundDay 基金天数据
     * @return 结果
     */
    @Override
    public int updateFundDay(FundDay fundDay)
    {
        return fundDayMapper.updateFundDay(fundDay);
    }

    /**
     * 批量删除基金天数据
     * 
     * @param ids 需要删除的基金天数据主键
     * @return 结果
     */
    @Override
    public int deleteFundDayByIds(Long[] ids)
    {
        return fundDayMapper.deleteFundDayByIds(ids);
    }

    /**
     * 删除基金天数据信息
     * 
     * @param id 基金天数据主键
     * @return 结果
     */
    @Override
    public int deleteFundDayById(Long id)
    {
        return fundDayMapper.deleteFundDayById(id);
    }
}
