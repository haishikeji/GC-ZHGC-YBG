package cn.px.service;

import java.util.List;
import java.util.Map;

import cn.px.domain.WorkReport;
import cn.px.domain.request.CountTypeReqVo;
import cn.px.domain.vo.*;
import cn.px.domain.vo.*;

/**
 * 生产制造-报工信息Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IWorkReportService {
    /**
     * 查询生产制造-报工信息
     *
     * @param id 生产制造-报工信息主键
     * @return 生产制造-报工信息
     */
    public WorkReport selectWorkReportById(Long id);

    /**
     * 查询生产制造-报工信息
     *
     * @param ids
     * @return
     */
    public List<WorkReportVo> selectWorkReportByIds(Long[] ids);

    /**
     * 查询生产制造-报工信息列表
     *
     * @param workReport 生产制造-报工信息
     * @return 生产制造-报工信息集合
     */
    public List<WorkReport> selectWorkReportList(WorkReport workReport);

    public List<WorkReport> selectWorkReportListByStatus(WorkReport workReport);

    public List<WorkReportListVo> selectWorkReportListByType(WorkReportVo workReportVo);

    /**
     * 查询生产制造-未入库报工信息列表
     *
     * @param workReport 生产制造-报工信息
     * @return 生产制造-报工信息集合
     */
    public List<WorkReport> selectNotEnterReportList(WorkReport workReport);


    /**
     * 新增生产制造-报工信息
     *
     * @param workReport 生产制造-报工信息
     * @return 结果
     */
    public int insertWorkReport(WorkReport workReport);

    /**
     * 修改生产制造-报工信息
     *
     * @param workReport 生产制造-报工信息
     * @return 结果
     */
    public int updateWorkReport(WorkReport workReport);

    /**
     * 修改报工信息与明细信息
     *
     * @param workReportVo
     * @return
     */
    public int editPeport(WorkReportVo workReportVo);

    /**
     * 批量修改
     * @param workReportVo
     * @return
     */
    public int editDetectionList(List<WorkReportListVo> workReportListVos);

    /**
     * 检测
     * @param workReportVo
     * @return
     */
    public int editDetection(WorkReportListVo workReport);

    /**
     * 批量删除生产制造-报工信息
     *
     * @param ids 需要删除的生产制造-报工信息主键集合
     * @return 结果
     */
    public int deleteWorkReportByIds(Long[] ids);

    /**
     * 删除生产制造-报工信息信息
     *
     * @param id 生产制造-报工信息主键
     * @return 结果
     */
    public int deleteWorkReportById(Long id);


    public WorkReportVo selectWorkReportInfoVo(Long id);

    /**
     * 获得产品数量 合格数 、 不合格数
     *
     * @return
     */
    public ReportMsterialsSumVo getProductionSumCount(CountTypeReqVo typeReqVo);

    public List<ProductionStatisticsVo> selectProductionStatisticsList(WorkReport report);

    public List<TaskQuantitySumVo> selectTaskQuantitySumByDate(CountTypeReqVo typeReqVo);

    public List<TaskQuantitySumVo> selectTaskQuantitySumByStatus();

    public Map<String, Object> selectCompletionQuantityByMonth(Long tenantId);

    public Map<String, Object> selectMaterialsSupplyMode(Long tenantId);

    public Map<String, Object> getCompletionQuantityTop(Long tenantId);

    public Map<String, Object> selectCompletionYieldachievementTop(Long tenantId,String unitCode);

    public List<Map<String, Object>> selectProductionUnitNamePie(Long tenantId);

    public List<Map<String, Object>> selectProductionLineOutputChartPie(Long tenantId);

    public List<Map<String, Object>> selectMaterialdefectChartPie();

    public List<Map<String, Object>> selectmaterialQualifiedpercentageChartPie(Long tenantId);

    public List<Map<String, Object>> selectMaterialTypeChartPie(Long tenantId);

    public List<ObjectCountVo> getReportCountByUserId(Long userId);

    public int getReportTaskCount(CountTypeReqVo typeReqVo);

}
