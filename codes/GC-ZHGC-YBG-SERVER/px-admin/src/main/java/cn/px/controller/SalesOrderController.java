package cn.px.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.px.domain.vo.SalesOrderDetailVo;
import cn.px.domain.vo.SalesOrderListVo;
import cn.px.domain.vo.SalesOrderVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.px.common.annotation.Log;
import cn.px.common.core.controller.BaseController;
import cn.px.common.core.domain.AjaxResult;
import cn.px.common.enums.BusinessType;
import cn.px.domain.SalesOrder;
import cn.px.service.ISalesOrderService;
import cn.px.common.utils.poi.ExcelUtil;
import cn.px.common.core.page.TableDataInfo;

/**
 * 生产制造-销售订单Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/fabricate/salesorder")
public class SalesOrderController extends BaseController {
    @Autowired
    private ISalesOrderService salesOrderService;

    /**
     * 查询生产制造-销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('fabricate:salesorder:list')")
    @GetMapping("/list")
    public TableDataInfo list(SalesOrder salesOrder) {
        startPage();
        List<SalesOrderListVo> list = salesOrderService.selectSalesOrderListVo(salesOrder);
        return getDataTable(list);
    }

    /**
     * 查询生产制造-销售订单详细列表
     */
    @GetMapping("/listDetail")
    public TableDataInfo selectSalesTypeDetailList(SalesOrder salesOrder) {
        List<SalesOrderDetailVo> list = salesOrderService.selectSalesTypeDetailList(salesOrder);
        return getDataTable(list);
    }

    /**
     * 导出生产制造-销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('fabricate:salesorder:export')")
    @Log(title = "生产制造-销售订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SalesOrder salesOrder) {
        List<SalesOrder> list = salesOrderService.selectSalesOrderList(salesOrder);
        ExcelUtil<SalesOrder> util = new ExcelUtil<SalesOrder>(SalesOrder.class);
        util.exportExcel(response, list, "生产制造-销售订单数据");
    }

    /**
     * 获取生产制造-销售订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('fabricate:salesorder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(salesOrderService.selectSalesOrderVoById(id));
    }

    /**
     * 获取生产制造-销售订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('fabricate:salesorder:query')")
    @GetMapping(value = "selectSalesOrderByNumber/{number}")
    public AjaxResult selectSalesOrderByNumber(@PathVariable("number") String number) {
        return AjaxResult.success(salesOrderService.selectSalesOrderByNumber(number,getTenantId()));
    }

    /**
     * 新增生产制造-销售订单
     */
    @PreAuthorize("@ss.hasPermi('fabricate:salesorder:add')")
    @Log(title = "生产制造-销售订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SalesOrderVo salesOrder) {
        return AjaxResult.success(salesOrderService.saveSalesOrder(salesOrder));
    }

    /**
     * 修改生产制造-销售订单
     */
    @PreAuthorize("@ss.hasPermi('fabricate:salesorder:edit')")
    @Log(title = "生产制造-销售订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SalesOrderVo salesOrderVo) {
        return AjaxResult.success(salesOrderService.updateSalesOrderVo(salesOrderVo));
    }

    /**
     * 删除生产制造-销售订单
     */
    @PreAuthorize("@ss.hasPermi('fabricate:salesorder:remove')")
    @Log(title = "生产制造-销售订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(salesOrderService.deleteSalesOrderByIds(ids));
    }


    /**
     * 下发生产制造-销售订单
     */
    @Log(title = "下发生产制造-销售订单", businessType = BusinessType.UPDATE)
    @PutMapping("/sendOrder")
    public AjaxResult sendOrder(@RequestBody Long id) {
        return toAjax(salesOrderService.sendOrder(id));
    }


    /**
     * 撤回生产制造-销售订单
     */
    @Log(title = "撤回生产制造-销售订单", businessType = BusinessType.UPDATE)
    @PutMapping("/withdrawOder")
    public AjaxResult withdrawOder(@RequestBody Long id) {
        return toAjax(salesOrderService.withdrawOder(id));
    }
}
