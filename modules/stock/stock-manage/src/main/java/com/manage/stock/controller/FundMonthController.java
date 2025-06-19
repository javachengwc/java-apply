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
import com.manage.stock.domain.FundMonth;
import com.manage.stock.service.IFundMonthService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 基金月数据Controller
 * 
 * @author gener
 * @date 2025-06-19
 */
@RestController
@RequestMapping("/stock/fundmonth")
public class FundMonthController extends BaseController
{
    @Autowired
    private IFundMonthService fundMonthService;

    /**
     * 查询基金月数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fundmonth:list')")
    @GetMapping("/list")
    public TableDataInfo list(FundMonth fundMonth)
    {
        startPage();
        List<FundMonth> list = fundMonthService.selectFundMonthList(fundMonth);
        return getDataTable(list);
    }

    /**
     * 导出基金月数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fundmonth:export')")
    @Log(title = "基金月数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FundMonth fundMonth)
    {
        List<FundMonth> list = fundMonthService.selectFundMonthList(fundMonth);
        ExcelUtil<FundMonth> util = new ExcelUtil<FundMonth>(FundMonth.class);
        util.exportExcel(response, list, "基金月数据数据");
    }

    /**
     * 获取基金月数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:fundmonth:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fundMonthService.selectFundMonthById(id));
    }

    /**
     * 新增基金月数据
     */
    @PreAuthorize("@ss.hasPermi('stock:fundmonth:add')")
    @Log(title = "基金月数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FundMonth fundMonth)
    {
        return toAjax(fundMonthService.insertFundMonth(fundMonth));
    }

    /**
     * 修改基金月数据
     */
    @PreAuthorize("@ss.hasPermi('stock:fundmonth:edit')")
    @Log(title = "基金月数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FundMonth fundMonth)
    {
        return toAjax(fundMonthService.updateFundMonth(fundMonth));
    }

}
