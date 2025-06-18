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
import com.manage.stock.domain.StockDay;
import com.manage.stock.service.IStockDayService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 股票天数据Controller
 * 
 * @author gener
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/stock/stockday")
public class StockDayController extends BaseController
{
    @Autowired
    private IStockDayService stockDayService;

    /**
     * 查询股票天数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockday:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockDay stockDay)
    {
        startPage();
        List<StockDay> list = stockDayService.selectStockDayList(stockDay);
        return getDataTable(list);
    }

    /**
     * 导出股票天数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockday:export')")
    @Log(title = "股票天数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockDay stockDay)
    {
        List<StockDay> list = stockDayService.selectStockDayList(stockDay);
        ExcelUtil<StockDay> util = new ExcelUtil<StockDay>(StockDay.class);
        util.exportExcel(response, list, "股票天数据数据");
    }

    /**
     * 获取股票天数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:stockday:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockDayService.selectStockDayById(id));
    }

    /**
     * 新增股票天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockday:add')")
    @Log(title = "股票天数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockDay stockDay)
    {
        return toAjax(stockDayService.insertStockDay(stockDay));
    }

    /**
     * 修改股票天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockday:edit')")
    @Log(title = "股票天数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockDay stockDay)
    {
        return toAjax(stockDayService.updateStockDay(stockDay));
    }

    /**
     * 删除股票天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockday:remove')")
    @Log(title = "股票天数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockDayService.deleteStockDayByIds(ids));
    }
}
