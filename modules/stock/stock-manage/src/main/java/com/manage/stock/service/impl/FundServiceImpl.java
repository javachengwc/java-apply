package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.FundMapper;
import com.manage.stock.domain.Fund;
import com.manage.stock.service.IFundService;

/**
 * 基金Service业务层处理
 * 
 * @author gener
 * @date 2025-06-18
 */
@Service
public class FundServiceImpl implements IFundService 
{
    @Autowired
    private FundMapper fundMapper;

    /**
     * 查询基金
     * 
     * @param id 基金主键
     * @return 基金
     */
    @Override
    public Fund selectFundById(Long id)
    {
        return fundMapper.selectFundById(id);
    }

    /**
     * 查询基金列表
     * 
     * @param fund 基金
     * @return 基金
     */
    @Override
    public List<Fund> selectFundList(Fund fund)
    {
        return fundMapper.selectFundList(fund);
    }

    /**
     * 新增基金
     * 
     * @param fund 基金
     * @return 结果
     */
    @Override
    public int insertFund(Fund fund)
    {
        fund.setCreateTime(DateUtils.getNowDate());
        return fundMapper.insertFund(fund);
    }

    /**
     * 修改基金
     * 
     * @param fund 基金
     * @return 结果
     */
    @Override
    public int updateFund(Fund fund)
    {
        return fundMapper.updateFund(fund);
    }

    /**
     * 批量删除基金
     * 
     * @param ids 需要删除的基金主键
     * @return 结果
     */
    @Override
    public int deleteFundByIds(Long[] ids)
    {
        return fundMapper.deleteFundByIds(ids);
    }

    /**
     * 删除基金信息
     * 
     * @param id 基金主键
     * @return 结果
     */
    @Override
    public int deleteFundById(Long id)
    {
        return fundMapper.deleteFundById(id);
    }
}
