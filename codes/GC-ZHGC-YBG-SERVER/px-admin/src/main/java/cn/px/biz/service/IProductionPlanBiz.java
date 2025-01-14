package cn.px.biz.service;

import java.util.List;
import cn.px.domain.ProductionPlan;
/**
 * 生产制造-生产计划Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IProductionPlanBiz
{


    /**
     * 查询生产制造-生产计划列表
     *
     * @param productionPlan 生产制造-生产计划
     * @return 生产制造-生产计划集合
     */
    public List<ProductionPlan> searchProductionPlanList(ProductionPlan productionPlan);

    public int distribute(ProductionPlan productionPlan);

    /**
     * 计划订单-工单制定
     * @param
     */
    public void saveWorkReport(ProductionPlan productionPlan);


}
