package cn.px.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.px.common.exception.ServiceException;
import cn.px.domain.Materials;
import cn.px.service.IMaterialsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import cn.px.domain.MaterialsType;
import cn.px.service.IMaterialsTypeService;
import cn.px.common.utils.poi.ExcelUtil;
import cn.px.common.core.page.TableDataInfo;

/**
 * 物料类型数据Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/basic/materialstype")
public class MaterialsTypeController extends BaseController
{
    @Autowired
    private IMaterialsTypeService materialsTypeService;
    @Autowired
    private IMaterialsService materialsService;
    /**
     * 查询物料类型数据列表
     */
    @PreAuthorize("@ss.hasPermi('basic:materialstype:list')")
    @GetMapping("/listType")
    public List<MaterialsType> listType(MaterialsType materialsType)
    {
        List<MaterialsType> list = materialsTypeService.selectMaterialsTypeListVo(materialsType);
        return list;
    }

    /**
     * 查询物料类型数据列表
     */
    @PreAuthorize("@ss.hasPermi('basic:materialstype:list')")
    @GetMapping("/list")
    public TableDataInfo list(MaterialsType materialsType)
    {
        startPage();
        List<MaterialsType> list = materialsTypeService.selectMaterialsTypeList(materialsType);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('basic:materialstype:list')")
    @GetMapping("/listVo")
    public TableDataInfo listVo(MaterialsType materialsType)
    {
        startPage();
        List<MaterialsType> list = materialsTypeService.selectMaterialsTypeListVo(materialsType);
        return getDataTable(list);
    }

    /**
     * 导出物料类型数据列表
     */
    @PreAuthorize("@ss.hasPermi('basic:materialstype:export')")
    @Log(title = "物料类型数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialsType materialsType)
    {
        List<MaterialsType> list = materialsTypeService.selectMaterialsTypeList(materialsType);
        ExcelUtil<MaterialsType> util = new ExcelUtil<MaterialsType>(MaterialsType.class);
        util.exportExcel(response, list, "物料类型数据数据");
    }

    /**
     * 获取物料类型数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('basic:materialstype:query')")
    @GetMapping(value = "/{materialsTypeId}")
    public AjaxResult getInfo(@PathVariable("materialsTypeId") Long materialsTypeId)
    {
        return AjaxResult.success(materialsTypeService.selectMaterialsTypeByMaterialsTypeId(materialsTypeId));
    }

    /**
     * 新增物料类型数据
     */
    @PreAuthorize("@ss.hasPermi('basic:materialstype:add')")
    @Log(title = "物料类型数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialsType materialsType)
    {
        List<MaterialsType> materials_value = materialsTypeService.selectMaterialsTypeListVo(new MaterialsType(materialsType.getMaterialsTypeValue(),getTenantId()));
        if(materials_value.size()>0){
            throw new ServiceException("新增物料类型["+materialsType.getMaterialsTypeLabel()+"],与["+materials_value.get(0).getMaterialsTypeLabel()+"],类型值不能相同！");
        }

        List<MaterialsType> materialsList_name = materialsTypeService.selectMaterialsTypeListVo(new MaterialsType(materialsType.getMaterialsTypeLabel(),getTenantId()));
        if(materialsList_name.size()>0){
            throw new ServiceException("新增物料类型["+materialsType.getMaterialsTypeLabel()+"],与["+materialsList_name.get(0).getMaterialsTypeLabel()+"],类型名称不能相同！");
        }

        return toAjax(materialsTypeService.insertMaterialsType(materialsType));
    }

    /**
     * 修改物料类型数据
     */
    @PreAuthorize("@ss.hasPermi('basic:materialstype:edit')")
    @Log(title = "物料类型数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialsType materialsType)
    {
        return toAjax(materialsTypeService.updateMaterialsType(materialsType));
    }

    /**
     * 删除物料类型数据
     */
    @PreAuthorize("@ss.hasPermi('basic:materialstype:remove')")
    @Log(title = "物料类型数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{materialsTypeIds}")
    @Transactional
    public AjaxResult remove(@PathVariable Long[] materialsTypeIds)
    {
        for(Long id : materialsTypeIds){
            MaterialsType materialsType = materialsTypeService.selectMaterialsTypeByMaterialsTypeId(id);
            if(ObjectUtils.isNotEmpty(materialsType)){
                List<Materials> materials = materialsService.selectMaterialsList(new Materials(Integer.valueOf(materialsType.getMaterialsTypeValue()),getTenantId()));
                if(materials.size()>0){
                    throw new ServiceException("物料信息包含该物料类型["+materialsType.getMaterialsTypeLabel()+"],不能删除！");
                }
            }
        }

        return toAjax(materialsTypeService.deleteMaterialsTypeByMaterialsTypeIds(materialsTypeIds));
    }
}
