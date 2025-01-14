package cn.px.biz.service;

import java.util.List;
import cn.px.domain.WorkOrderDetail;
import cn.px.domain.vo.MasterialsInfoReportVo;

/**
 * 工单检测明细Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IWorkOrderDetailBiz
{


    /**
     * 查询工单检测明细列表
     *
     * @param workOrderDetail 工单检测明细
     * @return 工单检测明细集合
     */
    public List<WorkOrderDetail> searchWorkOrderDetailList(WorkOrderDetail workOrderDetail);

    /**
     * 保存工单检测明细
     *
     * @param workOrderDetail 工单检测明细
     * @return 结果
     */
    public int saveWorkOrderDetail(WorkOrderDetail workOrderDetail);


    public List<MasterialsInfoReportVo> getMasterialsInfoReportVoList(MasterialsInfoReportVo reportVo);
}
