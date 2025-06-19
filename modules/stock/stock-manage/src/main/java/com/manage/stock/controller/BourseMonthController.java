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
import com.manage.stock.domain.BourseMonth;
import com.manage.stock.service.IBourseMonthService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 证券指数月数据Controller
 * 
 * @author gener
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/stock/boursemonth")
public class BourseMonthController extends BaseController
{
    @Autowired
    private IBourseMonthService bourseMonthService;

    /**
     * 查询证券指数月数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:boursemonth:list')")
    @GetMapping("/list")
    public TableDataInfo list(BourseMonth bourseMonth)
    {
        startPage();
        List<BourseMonth> list = bourseMonthService.selectBourseMonthList(bourseMonth);
        return getDataTable(list);
    }

    /**
     * 导出证券指数月数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:boursemonth:export')")
    @Log(title = "证券指数月数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BourseMonth bourseMonth)
    {
        List<BourseMonth> list = bourseMonthService.selectBourseMonthList(bourseMonth);
        ExcelUtil<BourseMonth> util = new ExcelUtil<BourseMonth>(BourseMonth.class);
        util.exportExcel(response, list, "证券指数月数据数据");
    }

    /**
     * 获取证券指数月数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:boursemonth:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bourseMonthService.selectBourseMonthById(id));
    }

    /**
     * 新增证券指数月数据
     */
    @PreAuthorize("@ss.hasPermi('stock:boursemonth:add')")
    @Log(title = "证券指数月数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BourseMonth bourseMonth)
    {
        return toAjax(bourseMonthService.insertBourseMonth(bourseMonth));
    }

    /**
     * 修改证券指数月数据
     */
    @PreAuthorize("@ss.hasPermi('stock:boursemonth:edit')")
    @Log(title = "证券指数月数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BourseMonth bourseMonth)
    {
        return toAjax(bourseMonthService.updateBourseMonth(bourseMonth));
    }
}
