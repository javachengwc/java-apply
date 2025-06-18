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
import com.manage.stock.domain.StockMonth;
import com.manage.stock.service.IStockMonthService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 股票月数据Controller
 * 
 * @author gener
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/stock/stockmonth")
public class StockMonthController extends BaseController
{
    @Autowired
    private IStockMonthService stockMonthService;

    /**
     * 查询股票月数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockmonth:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockMonth stockMonth)
    {
        startPage();
        List<StockMonth> list = stockMonthService.selectStockMonthList(stockMonth);
        return getDataTable(list);
    }

    /**
     * 导出股票月数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:stockmonth:export')")
    @Log(title = "股票月数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockMonth stockMonth)
    {
        List<StockMonth> list = stockMonthService.selectStockMonthList(stockMonth);
        ExcelUtil<StockMonth> util = new ExcelUtil<StockMonth>(StockMonth.class);
        util.exportExcel(response, list, "股票月数据数据");
    }

    /**
     * 获取股票月数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:stockmonth:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockMonthService.selectStockMonthById(id));
    }

    /**
     * 新增股票月数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockmonth:add')")
    @Log(title = "股票月数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockMonth stockMonth)
    {
        return toAjax(stockMonthService.insertStockMonth(stockMonth));
    }

    /**
     * 修改股票月数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockmonth:edit')")
    @Log(title = "股票月数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockMonth stockMonth)
    {
        return toAjax(stockMonthService.updateStockMonth(stockMonth));
    }

    /**
     * 删除股票月数据
     */
    @PreAuthorize("@ss.hasPermi('stock:stockmonth:remove')")
    @Log(title = "股票月数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockMonthService.deleteStockMonthByIds(ids));
    }
}
