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
import com.manage.stock.domain.FundWeek;
import com.manage.stock.service.IFundWeekService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 基金周数据Controller
 * 
 * @author gener
 * @date 2025-06-19
 */
@RestController
@RequestMapping("/stock/fundweek")
public class FundWeekController extends BaseController
{
    @Autowired
    private IFundWeekService fundWeekService;

    /**
     * 查询基金周数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fundweek:list')")
    @GetMapping("/list")
    public TableDataInfo list(FundWeek fundWeek)
    {
        startPage();
        List<FundWeek> list = fundWeekService.selectFundWeekList(fundWeek);
        return getDataTable(list);
    }

    /**
     * 导出基金周数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fundweek:export')")
    @Log(title = "基金周数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FundWeek fundWeek)
    {
        List<FundWeek> list = fundWeekService.selectFundWeekList(fundWeek);
        ExcelUtil<FundWeek> util = new ExcelUtil<FundWeek>(FundWeek.class);
        util.exportExcel(response, list, "基金周数据数据");
    }

    /**
     * 获取基金周数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:fundweek:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fundWeekService.selectFundWeekById(id));
    }

    /**
     * 新增基金周数据
     */
    @PreAuthorize("@ss.hasPermi('stock:fundweek:add')")
    @Log(title = "基金周数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FundWeek fundWeek)
    {
        return toAjax(fundWeekService.insertFundWeek(fundWeek));
    }

    /**
     * 修改基金周数据
     */
    @PreAuthorize("@ss.hasPermi('stock:fundweek:edit')")
    @Log(title = "基金周数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FundWeek fundWeek)
    {
        return toAjax(fundWeekService.updateFundWeek(fundWeek));
    }

}
