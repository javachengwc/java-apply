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
import com.manage.stock.domain.StockYear;
import com.manage.stock.service.IStockYearService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 股票年数据Controller
 * 
 * @author gener
 * @date 2025-06-17
 */
@RestController
@RequestMapping("/stock/stockyear")
public class StockYearController extends BaseController
{
    @Autowired
    private IStockYearService stockYearService;

    /**
     * 查询股票年数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockyear:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockYear stockYear)
    {
        startPage();
        List<StockYear> list = stockYearService.selectStockYearList(stockYear);
        return getDataTable(list);
    }

    /**
     * 导出股票年数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockyear:export')")
    @Log(title = "股票年数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockYear stockYear)
    {
        List<StockYear> list = stockYearService.selectStockYearList(stockYear);
        ExcelUtil<StockYear> util = new ExcelUtil<StockYear>(StockYear.class);
        util.exportExcel(response, list, "股票年数据数据");
    }

    /**
     * 获取股票年数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:stockyear:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockYearService.selectStockYearById(id));
    }

    /**
     * 新增股票年数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockyear:add')")
    @Log(title = "股票年数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockYear stockYear)
    {
        return toAjax(stockYearService.insertStockYear(stockYear));
    }

    /**
     * 修改股票年数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockyear:edit')")
    @Log(title = "股票年数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockYear stockYear)
    {
        return toAjax(stockYearService.updateStockYear(stockYear));
    }

    /**
     * 删除股票年数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockyear:remove')")
    @Log(title = "股票年数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockYearService.deleteStockYearByIds(ids));
    }
}
