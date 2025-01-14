package cn.px.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
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
import cn.px.domain.Pack;
import cn.px.service.IPackService;
import cn.px.common.utils.poi.ExcelUtil;
import cn.px.common.core.page.TableDataInfo;

/**
 * 基础数据-包装Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/basic/pack")
public class PackController extends BaseController {
    @Autowired
    private IPackService packService;


    /**
     * 查询基础数据-包装列表
     */
    @PreAuthorize("@ss.hasPermi('basic:pack:list')")
    @GetMapping("/list")
    public TableDataInfo list(Pack pack) {
        startPage();
        List<Pack> list = packService.selectPackList(pack);
        return getDataTable(list);
    }

    /**
     * 导出基础数据-包装列表
     */
    @PreAuthorize("@ss.hasPermi('basic:pack:export')")
    @Log(title = "基础数据-包装", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Pack pack) {
        List<Pack> list = packService.selectPackList(pack);
        ExcelUtil<Pack> util = new ExcelUtil<Pack>(Pack.class);
        util.exportExcel(response, list, "基础数据-包装数据");
    }

    /**
     * 获取基础数据-包装详细信息
     */
    @PreAuthorize("@ss.hasPermi('basic:pack:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(packService.selectPackById(id));
    }

    /**
     * 新增基础数据-包装
     */
    @PreAuthorize("@ss.hasPermi('basic:pack:add')")
    @Log(title = "基础数据-包装", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Pack pack) {
        return toAjax(packService.insertPack(pack));
    }

    /**
     * 修改基础数据-包装
     */
    @PreAuthorize("@ss.hasPermi('basic:pack:edit')")
    @Log(title = "基础数据-包装", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Pack pack) {
        if(ObjectUtils.isEmpty(packService.selectPackById(pack.getId()))){
            return AjaxResult.error("该包装信息不存在");
        }else{
            return toAjax(packService.updatePack(pack));
        }
    }

    /**
     * 删除基础数据-包装
     */
    @PreAuthorize("@ss.hasPermi('basic:pack:remove')")
    @Log(title = "基础数据-包装", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(packService.deletePackByIds(ids));
    }

    /**
     * 验证名称是否存在
     *
     * @param name
     * @return
     */
    @ApiOperation("验证名称是否存在")
    @PostMapping("/exist")
    public AjaxResult ExistName(@RequestBody String name) {
        Pack pack = new Pack();
        pack.setName(name);
        pack.setTenantId(getTenantId());
        List<Pack> packs = packService.selectPackbyName(pack);
        return AjaxResult.success(packs);
    }
}


