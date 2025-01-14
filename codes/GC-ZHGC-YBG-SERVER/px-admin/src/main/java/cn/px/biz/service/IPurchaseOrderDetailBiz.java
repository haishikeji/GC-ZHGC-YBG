package cn.px.biz.service;

import java.util.List;
import cn.px.domain.PurchaseOrderDetail;
/**
 * 生产制造-采购订单明细Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IPurchaseOrderDetailBiz
{


    /**
     * 查询生产制造-采购订单明细列表
     *
     * @param purchaseOrderDetail 生产制造-采购订单明细
     * @return 生产制造-采购订单明细集合
     */
    public List<PurchaseOrderDetail> searchPurchaseOrderDetailList(PurchaseOrderDetail purchaseOrderDetail);

    /**
     * 保存生产制造-采购订单明细
     *
     * @param purchaseOrderDetail 生产制造-采购订单明细
     * @return 结果
     */
    public int savePurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail);


}
