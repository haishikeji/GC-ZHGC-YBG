package cn.px.system.service;

import java.util.List;
import java.util.Map;

import cn.px.common.core.domain.entity.SysWorkflow;

/**
 * 业务审批流程Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface ISysWorkflowService
{
    /**
     * 查询业务审批流程
     *
     * @param id 业务审批流程主键
     * @return 业务审批流程
     */
    public SysWorkflow selectSysWorkflowById(Long id);

    /**
     * 查询业务审批流程列表
     *
     * @param sysWorkflow 业务审批流程
     * @return 业务审批流程集合
     */
    public List<SysWorkflow> selectSysWorkflowList(SysWorkflow sysWorkflow);

    /**
     * 新增业务审批流程
     *
     * @param sysWorkflow 业务审批流程
     * @return 结果
     */
    public int insertSysWorkflow(SysWorkflow sysWorkflow);

    /**
     * 修改业务审批流程
     *
     * @param sysWorkflow 业务审批流程
     * @return 结果
     */
    public int updateSysWorkflow(SysWorkflow sysWorkflow);

    /**
     * 批量删除业务审批流程
     *
     * @param ids 需要删除的业务审批流程主键集合
     * @return 结果
     */
    public int deleteSysWorkflowByIds(Long[] ids);

    /**
     * 删除业务审批流程信息
     *
     * @param id 业务审批流程主键
     * @return 结果
     */
    public int deleteSysWorkflowById(Long id);

    /**
     * 创建审批流程
     * @param processKey
     * @param businessId
     * @param condition
     * @return
     */
    Long createWorkflow(String processKey, Long businessId, Map<String,Boolean> condition);

    void createWorkflow(List<SysWorkflow> workflowList);

    List<SysWorkflow> selectBusinessWorkflowList(Long id, Long spiId);

    /**
     * 完成
     * @param sysWorkflow
     * @return
     */
    int complete(SysWorkflow sysWorkflow);

    /**
     * 修改业务表单状态
     * @param spiId
     * @param businessId
     * @param state
     */
    void updateBusinessState(Long spiId,Long businessId,Integer state);
}
