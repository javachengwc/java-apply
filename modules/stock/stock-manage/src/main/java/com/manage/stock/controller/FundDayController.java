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
import com.manage.stock.domain.FundDay;
import com.manage.stock.service.IFundDayService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 基金天数据Controller
 * 
 * @author gener
 * @date 2025-06-19
 */
@RestController
@RequestMapping("/stock/fundday")
public class FundDayController extends BaseController
{
    @Autowired
    private IFundDayService fundDayService;

    /**
     * 查询基金天数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fundday:list')")
    @GetMapping("/list")
    public TableDataInfo list(FundDay fundDay)
    {
        startPage();
        List<FundDay> list = fundDayService.selectFundDayList(fundDay);
        return getDataTable(list);
    }

    /**
     * 导出基金天数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:fundday:export')")
    @Log(title = "基金天数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FundDay fundDay)
    {
        List<FundDay> list = fundDayService.selectFundDayList(fundDay);
        ExcelUtil<FundDay> util = new ExcelUtil<FundDay>(FundDay.class);
        util.exportExcel(response, list, "基金天数据数据");
    }

    /**
     * 获取基金天数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:fundday:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fundDayService.selectFundDayById(id));
    }

    /**
     * 新增基金天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:fundday:add')")
    @Log(title = "基金天数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FundDay fundDay)
    {
        return toAjax(fundDayService.insertFundDay(fundDay));
    }

    /**
     * 修改基金天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:fundday:edit')")
    @Log(title = "基金天数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FundDay fundDay)
    {
        return toAjax(fundDayService.updateFundDay(fundDay));
    }

}
