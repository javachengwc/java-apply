package com.manage.stock.mapper;

import java.util.List;
import com.manage.stock.domain.CompanyStock;

/**
 * 公司股票Mapper接口
 * 
 * @author gener
 * @date 2025-06-17
 */
public interface CompanyStockMapper 
{
    /**
     * 查询公司股票
     * 
     * @param id 公司股票主键
     * @return 公司股票
     */
    public CompanyStock selectCompanyStockById(Long id);

    /**
     * 查询公司股票列表
     * 
     * @param companyStock 公司股票
     * @return 公司股票集合
     */
    public List<CompanyStock> selectCompanyStockList(CompanyStock companyStock);

    /**
     * 新增公司股票
     * 
     * @param companyStock 公司股票
     * @return 结果
     */
    public int insertCompanyStock(CompanyStock companyStock);

    /**
     * 修改公司股票
     * 
     * @param companyStock 公司股票
     * @return 结果
     */
    public int updateCompanyStock(CompanyStock companyStock);

    /**
     * 删除公司股票
     * 
     * @param id 公司股票主键
     * @return 结果
     */
    public int deleteCompanyStockById(Long id);

    /**
     * 批量删除公司股票
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCompanyStockByIds(Long[] ids);
}
