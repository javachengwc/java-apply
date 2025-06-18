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
import com.manage.stock.domain.Fund;
import com.manage.stock.service.IFundService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 基金Controller
 * 
 * @author gener
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/stock/fund")
public class FundController extends BaseController
{
    @Autowired
    private IFundService fundService;

    /**
     * 查询基金列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fund:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fund fund)
    {
        startPage();
        List<Fund> list = fundService.selectFundList(fund);
        return getDataTable(list);
    }

    /**
     * 导出基金列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fund:export')")
    @Log(title = "基金", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fund fund)
    {
        List<Fund> list = fundService.selectFundList(fund);
        ExcelUtil<Fund> util = new ExcelUtil<Fund>(Fund.class);
        util.exportExcel(response, list, "基金数据");
    }

    /**
     * 获取基金详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:fund:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fundService.selectFundById(id));
    }

    /**
     * 新增基金
     */
    @PreAuthorize("@ss.hasPermi('stock:fund:add')")
    @Log(title = "基金", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fund fund)
    {
        return toAjax(fundService.insertFund(fund));
    }

    /**
     * 修改基金
     */
    @PreAuthorize("@ss.hasPermi('stock:fund:edit')")
    @Log(title = "基金", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fund fund)
    {
        return toAjax(fundService.updateFund(fund));
    }

    /**
     * 删除基金
     */
    @PreAuthorize("@ss.hasPermi('stock:fund:remove')")
    @Log(title = "基金", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fundService.deleteFundByIds(ids));
    }
}
