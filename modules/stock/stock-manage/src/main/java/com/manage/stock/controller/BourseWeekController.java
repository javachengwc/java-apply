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
import com.manage.stock.domain.BourseWeek;
import com.manage.stock.service.IBourseWeekService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 证券指数周数据Controller
 * 
 * @author gener
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/stock/bourseweek")
public class BourseWeekController extends BaseController
{
    @Autowired
    private IBourseWeekService bourseWeekService;

    /**
     * 查询证券指数周数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseweek:list')")
    @GetMapping("/list")
    public TableDataInfo list(BourseWeek bourseWeek)
    {
        startPage();
        List<BourseWeek> list = bourseWeekService.selectBourseWeekList(bourseWeek);
        return getDataTable(list);
    }

    /**
     * 导出证券指数周数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseweek:export')")
    @Log(title = "证券指数周数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BourseWeek bourseWeek)
    {
        List<BourseWeek> list = bourseWeekService.selectBourseWeekList(bourseWeek);
        ExcelUtil<BourseWeek> util = new ExcelUtil<BourseWeek>(BourseWeek.class);
        util.exportExcel(response, list, "证券指数周数据数据");
    }

    /**
     * 获取证券指数周数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseweek:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bourseWeekService.selectBourseWeekById(id));
    }

    /**
     * 新增证券指数周数据
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseweek:add')")
    @Log(title = "证券指数周数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BourseWeek bourseWeek)
    {
        return toAjax(bourseWeekService.insertBourseWeek(bourseWeek));
    }

    /**
     * 修改证券指数周数据
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseweek:edit')")
    @Log(title = "证券指数周数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BourseWeek bourseWeek)
    {
        return toAjax(bourseWeekService.updateBourseWeek(bourseWeek));
    }

}
