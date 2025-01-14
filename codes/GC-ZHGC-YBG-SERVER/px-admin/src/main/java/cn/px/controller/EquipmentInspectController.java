package cn.px.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.px.domain.vo.EquipmentInspectVo;
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
import cn.px.common.annotation.Log;
import cn.px.common.core.controller.BaseController;
import cn.px.common.core.domain.AjaxResult;
import cn.px.common.enums.BusinessType;
import cn.px.domain.EquipmentInspect;
import cn.px.service.IEquipmentInspectService;
import cn.px.common.utils.poi.ExcelUtil;
import cn.px.common.core.page.TableDataInfo;

/**
 * 设备管理-设备巡检记录Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/reportwork/inspect")
public class EquipmentInspectController extends BaseController
{
    @Autowired
    private IEquipmentInspectService EquipmentInspectService;

    /**
     * 查询设备管理-设备巡检记录列表
     */
    @PreAuthorize("@ss.hasPermi('basic:inspect:list')")
    @GetMapping("/list")
    public TableDataInfo list(EquipmentInspect EquipmentInspect)
    {
        startPage();
        List<EquipmentInspect> list = EquipmentInspectService.selectEquipmentInspectList(EquipmentInspect);
        return getDataTable(list);
    }

    /**
     * 导出设备管理-设备巡检记录列表
     */
    @PreAuthorize("@ss.hasPermi('basic:inspect:export')")
    @Log(title = "设备管理-设备巡检记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EquipmentInspect EquipmentInspect)
    {
        List<EquipmentInspectVo> list = EquipmentInspectService.selectEquipmentInspectExportList(EquipmentInspect);
        ExcelUtil<EquipmentInspectVo> util = new ExcelUtil<EquipmentInspectVo>(EquipmentInspectVo.class);
        util.exportExcel(response, list, "设备管理-设备巡检记录数据");
    }

    /**
     * 获取设备管理-设备巡检记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('basic:inspect:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(EquipmentInspectService.selectEquipmentInspectById(id));
    }

    /**
     * 新增设备管理-设备巡检记录
     */
    @PreAuthorize("@ss.hasPermi('basic:inspect:add')")
    @Log(title = "设备管理-设备巡检记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EquipmentInspect EquipmentInspect)
    {
        return toAjax(EquipmentInspectService.insertEquipmentInspect(EquipmentInspect));
    }

    /**
     * 修改设备管理-设备巡检记录
     */
    @PreAuthorize("@ss.hasPermi('basic:inspect:edit')")
    @Log(title = "设备管理-设备巡检记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EquipmentInspect EquipmentInspect)
    {
        return toAjax(EquipmentInspectService.updateEquipmentInspect(EquipmentInspect));
    }

    /**
     * 删除设备管理-设备巡检记录
     */
    @PreAuthorize("@ss.hasPermi('basic:inspect:remove')")
    @Log(title = "设备管理-设备巡检记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(EquipmentInspectService.deleteEquipmentInspectByIds(ids));
    }
}
