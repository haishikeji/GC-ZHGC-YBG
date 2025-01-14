package cn.px.domain.vo;

import lombok.Data;

/**
 * 生产制造-订单、工单数量
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
public class ReportTaskCountVo {

    /**
     * 工单总数
     */
    private  int taskQuantity;

    /**
     * 生产中数量
     */
    private  int inProduction;

    /**
     * 未在生产中数量
     */
    private  int noProduction;

    /**
     * 完工数量
     */
    private  int completionQuantity;

    /**
     * 生产订单数
     */
    private  int orderCount;

    /**
     * 计划数量
     */
    private  int planSum;

}
