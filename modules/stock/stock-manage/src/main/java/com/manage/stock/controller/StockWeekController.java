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
import com.manage.stock.domain.StockWeek;
import com.manage.stock.service.IStockWeekService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 股票周数据Controller
 * 
 * @author gener
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/stock/stockweek")
public class StockWeekController extends BaseController
{
    @Autowired
    private IStockWeekService stockWeekService;

    /**
     * 查询股票周数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockweek:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockWeek stockWeek)
    {
        startPage();
        List<StockWeek> list = stockWeekService.selectStockWeekList(stockWeek);
        return getDataTable(list);
    }

    /**
     * 导出股票周数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockweek:export')")
    @Log(title = "股票周数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockWeek stockWeek)
    {
        List<StockWeek> list = stockWeekService.selectStockWeekList(stockWeek);
        ExcelUtil<StockWeek> util = new ExcelUtil<StockWeek>(StockWeek.class);
        util.exportExcel(response, list, "股票周数据数据");
    }

    /**
     * 获取股票周数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:stockweek:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockWeekService.selectStockWeekById(id));
    }

    /**
     * 新增股票周数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockweek:add')")
    @Log(title = "股票周数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockWeek stockWeek)
    {
        return toAjax(stockWeekService.insertStockWeek(stockWeek));
    }

    /**
     * 修改股票周数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockweek:edit')")
    @Log(title = "股票周数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockWeek stockWeek)
    {
        return toAjax(stockWeekService.updateStockWeek(stockWeek));
    }

}
