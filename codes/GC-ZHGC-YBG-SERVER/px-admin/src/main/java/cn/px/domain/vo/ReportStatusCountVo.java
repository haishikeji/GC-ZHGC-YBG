package cn.px.domain.vo;

import lombok.Data;

/**
 * 生产制造-订单、工单数量
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
public class ReportStatusCountVo {

    /**
     * 待派发
     */
    private  int dpfCount;


    /**
     * 执行中
     */
    private  int zxzCount;

    /**
     * 已完成
     */
    private  int ywcCount;

    /**
     * 待检测报工
     */
    private  int dbgCount;

    /**
     * 待报工审批
     */
//    private  int dspCount;



}
