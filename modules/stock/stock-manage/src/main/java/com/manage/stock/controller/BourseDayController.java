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
import com.manage.stock.domain.BourseDay;
import com.manage.stock.service.IBourseDayService;
import com.manage.framework.web.controller.BaseController;
import com.manage.framework.web.domain.AjaxResult;
import com.manage.common.utils.poi.ExcelUtil;
import com.manage.framework.web.page.TableDataInfo;

/**
 * 证券指数天数据Controller
 * 
 * @author gener
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/stock/bourseday")
public class BourseDayController extends BaseController
{
    @Autowired
    private IBourseDayService bourseDayService;

    /**
     * 查询证券指数天数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseday:list')")
    @GetMapping("/list")
    public TableDataInfo list(BourseDay bourseDay)
    {
        startPage();
        List<BourseDay> list = bourseDayService.selectBourseDayList(bourseDay);
        return getDataTable(list);
    }

    /**
     * 导出证券指数天数据列表
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseday:export')")
    @Log(title = "证券指数天数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BourseDay bourseDay)
    {
        List<BourseDay> list = bourseDayService.selectBourseDayList(bourseDay);
        ExcelUtil<BourseDay> util = new ExcelUtil<BourseDay>(BourseDay.class);
        util.exportExcel(response, list, "证券指数天数据数据");
    }

    /**
     * 获取证券指数天数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseday:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bourseDayService.selectBourseDayById(id));
    }

    /**
     * 新增证券指数天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseday:add')")
    @Log(title = "证券指数天数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BourseDay bourseDay)
    {
        return toAjax(bourseDayService.insertBourseDay(bourseDay));
    }

    /**
     * 修改证券指数天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseday:edit')")
    @Log(title = "证券指数天数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BourseDay bourseDay)
    {
        return toAjax(bourseDayService.updateBourseDay(bourseDay));
    }

    /**
     * 删除证券指数天数据
     */
    @PreAuthorize("@ss.hasPermi('stock:bourseday:remove')")
    @Log(title = "证券指数天数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bourseDayService.deleteBourseDayByIds(ids));
    }
}
