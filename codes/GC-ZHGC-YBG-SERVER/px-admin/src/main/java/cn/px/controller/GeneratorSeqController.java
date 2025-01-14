package cn.px.controller;

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
import cn.px.common.annotation.Log;
import cn.px.common.core.controller.BaseController;
import cn.px.common.core.domain.AjaxResult;
import cn.px.common.enums.BusinessType;
import cn.px.domain.GeneratorSeq;
import cn.px.service.IGeneratorSeqService;
import cn.px.common.utils.poi.ExcelUtil;
import cn.px.common.core.page.TableDataInfo;

/**
 * 流水号生成器Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/basic/service")
public class GeneratorSeqController extends BaseController
{
    @Autowired
    private IGeneratorSeqService generatorSeqService;

    /**
     * 查询流水号生成器列表
     */
    @PreAuthorize("@ss.hasPermi('basic:service:list')")
    @GetMapping("/list")
    public TableDataInfo list(GeneratorSeq generatorSeq)
    {
        startPage();
        List<GeneratorSeq> list = generatorSeqService.selectGeneratorSeqList(generatorSeq);
        return getDataTable(list);
    }

    /**
     * 导出流水号生成器列表
     */
    @PreAuthorize("@ss.hasPermi('basic:service:export')")
    @Log(title = "流水号生成器", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GeneratorSeq generatorSeq)
    {
        List<GeneratorSeq> list = generatorSeqService.selectGeneratorSeqList(generatorSeq);
        ExcelUtil<GeneratorSeq> util = new ExcelUtil<GeneratorSeq>(GeneratorSeq.class);
        util.exportExcel(response, list, "流水号生成器数据");
    }

    /**
     * 获取流水号生成器详细信息
     */
    @PreAuthorize("@ss.hasPermi('basic:service:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(generatorSeqService.selectGeneratorSeqById(id));
    }

    /**
     * 新增流水号生成器
     */
    @PreAuthorize("@ss.hasPermi('basic:service:add')")
    @Log(title = "流水号生成器", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GeneratorSeq generatorSeq)
    {
        return toAjax(generatorSeqService.insertGeneratorSeq(generatorSeq));
    }

    /**
     * 修改流水号生成器
     */
    @PreAuthorize("@ss.hasPermi('basic:service:edit')")
    @Log(title = "流水号生成器", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GeneratorSeq generatorSeq)
    {
        return toAjax(generatorSeqService.updateGeneratorSeq(generatorSeq));
    }

    /**
     * 删除流水号生成器
     */
    @PreAuthorize("@ss.hasPermi('basic:service:remove')")
    @Log(title = "流水号生成器", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(generatorSeqService.deleteGeneratorSeqByIds(ids));
    }
}
