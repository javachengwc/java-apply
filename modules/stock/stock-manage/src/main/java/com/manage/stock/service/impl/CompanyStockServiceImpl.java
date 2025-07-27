package com.manage.stock.service.impl;

import java.util.List;
import com.manage.common.utils.DateUtils;
import com.manage.stock.domain.Company;
import com.manage.stock.domain.vo.CompanyStockVo;
import com.manage.stock.service.ICompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manage.stock.mapper.CompanyStockMapper;
import com.manage.stock.domain.CompanyStock;
import com.manage.stock.service.ICompanyStockService;

/**
 * 公司股票Service业务层处理
 * 
 * @author gener
 * @date 2025-06-17
 */
@Service
public class CompanyStockServiceImpl implements ICompanyStockService 
{
    @Autowired
    private CompanyStockMapper companyStockMapper;

    @Autowired
    private ICompanyService companyService;

    /**
     * 查询公司股票
     * 
     * @param id 公司股票主键
     * @return 公司股票
     */
    @Override
    public CompanyStock selectCompanyStockById(Long id)
    {
        return companyStockMapper.selectCompanyStockById(id);
    }

    @Override
    public CompanyStockVo getDetail(Long id) {
        CompanyStock companyStock = this.selectCompanyStockById(id);
        Long companyId = companyStock.getCompanyId();
        Company company =companyService.selectCompanyById(companyId);
        CompanyStockVo companyStockVo = new CompanyStockVo();
        BeanUtils.copyProperties(companyStock,companyStockVo);
        companyStockVo.setCompanyIntroduce(company==null?"":company.getIntroduce());
        return companyStockVo;
    }

    /**
     * 查询公司股票列表
     * 
     * @param companyStock 公司股票
     * @return 公司股票
     */
    @Override
    public List<CompanyStock> selectCompanyStockList(CompanyStock companyStock)
    {
        return companyStockMapper.selectCompanyStockList(companyStock);
    }

    /**
     * 新增公司股票
     * 
     * @param companyStock 公司股票
     * @return 结果
     */
    @Override
    public int insertCompanyStock(CompanyStock companyStock)
    {
        companyStock.setCreateTime(DateUtils.getNowDate());
        return companyStockMapper.insertCompanyStock(companyStock);
    }

    /**
     * 修改公司股票
     * 
     * @param companyStock 公司股票
     * @return 结果
     */
    @Override
    public int updateCompanyStock(CompanyStock companyStock)
    {
        return companyStockMapper.updateCompanyStock(companyStock);
    }

    /**
     * 批量删除公司股票
     * 
     * @param ids 需要删除的公司股票主键
     * @return 结果
     */
    @Override
    public int deleteCompanyStockByIds(Long[] ids)
    {
        return companyStockMapper.deleteCompanyStockByIds(ids);
    }

    /**
     * 删除公司股票信息
     * 
     * @param id 公司股票主键
     * @return 结果
     */
    @Override
    public int deleteCompanyStockById(Long id)
    {
        return companyStockMapper.deleteCompanyStockById(id);
    }
}
