package com.manage.stock.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manage.framework.aspectj.lang.annotation.Log;
import com.manage.framework.aspectj.lang.enums.BusinessType;
import com.manage.stock.domain.CompanyStock;
import com.manage.stock.service.ICompanyStockService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 公司股票Controller
 * 
 * @author gener
 * @date 2025-06-17
 */
@RestController
@RequestMapping("/stock/stock")
public class CompanyStockController extends BaseController
{
    @Autowired
    private ICompanyStockService companyStockService;

    /**
     * 查询公司股票列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:list')")
    @GetMapping("/list")
    public TableDataInfo list(CompanyStock companyStock)
    {
        startPage();
        List<CompanyStock> list = companyStockService.selectCompanyStockList(companyStock);
        return getDataTable(list);
    }

    /**
     * 导出公司股票列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:export')")
    @Log(title = "公司股票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CompanyStock companyStock)
    {
        List<CompanyStock> list = companyStockService.selectCompanyStockList(companyStock);
        ExcelUtil<CompanyStock> util = new ExcelUtil<CompanyStock>(CompanyStock.class);
        util.exportExcel(response, list, "公司股票数据");
    }

    /**
     * 获取公司股票详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(companyStockService.selectCompanyStockById(id));
    }

    /**
     * 获取公司股票详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:query')")
    @GetMapping(value = "/getDetail/{id}")
    public AjaxResult getDetail(@PathVariable("id") Long id)
    {
        return success(companyStockService.getDetail(id));
    }

    /**
     * 新增公司股票
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:add')")
    @Log(title = "公司股票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CompanyStock companyStock)
    {
        return toAjax(companyStockService.insertCompanyStock(companyStock));
    }

    /**
     * 修改公司股票
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:edit')")
    @Log(title = "公司股票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CompanyStock companyStock)
    {
        return toAjax(companyStockService.updateCompanyStock(companyStock));
    }

    /**
     * 删除公司股票
     */
    @PreAuthorize("@ss.hasPermi('stock:stock:remove')")
    @Log(title = "公司股票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(companyStockService.deleteCompanyStockByIds(ids));
    }
}
