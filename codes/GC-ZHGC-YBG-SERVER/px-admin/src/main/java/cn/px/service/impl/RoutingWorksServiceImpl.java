package cn.px.service.impl;

import java.util.Date;
import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.mapper.RoutingWorksMapper;
import cn.px.domain.RoutingWorks;
import cn.px.service.IRoutingWorksService;

import static cn.px.common.utils.SecurityUtils.getTenantId;
import static cn.px.common.utils.SecurityUtils.getUsername;

/**
 * 基础数据-工艺路线工序明细Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class RoutingWorksServiceImpl implements IRoutingWorksService
{
    @Autowired
    private RoutingWorksMapper routingWorksMapper;

    /**
     * 查询基础数据-工艺路线工序明细
     *
     * @param id 基础数据-工艺路线工序明细主键
     * @return 基础数据-工艺路线工序明细
     */
    @Override
    public RoutingWorks selectRoutingWorksById(Long id)
    {
        return routingWorksMapper.selectRoutingWorksById(id);
    }

    /**
     * 查询基础数据-工艺路线工序明细列表
     *
     * @param routingWorks 基础数据-工艺路线工序明细
     * @return 基础数据-工艺路线工序明细
     */
    @Override
    public List<RoutingWorks> selectRoutingWorksList(RoutingWorks routingWorks)
    {
        routingWorks.setTenantId(getTenantId());
        return routingWorksMapper.selectRoutingWorksList(routingWorks);
    }
    @Override
    public List<RoutingWorks> selectRoutingWorksByRoutingId(Long routingId){
        return routingWorksMapper.selectRoutingWorksByRoutingId(routingId);
    }

    /**
     * 新增基础数据-工艺路线工序明细
     *
     * @param routingWorks 基础数据-工艺路线工序明细
     * @return 结果
     */
    @Override
    public int insertRoutingWorks(RoutingWorks routingWorks)
    {
        routingWorks.setCreateBy(getUsername());
        routingWorks.setCreateTime(new Date());
        routingWorks.setTenantId(getTenantId());
        return routingWorksMapper.insertRoutingWorks(routingWorks);
    }

    /**
     * 修改基础数据-工艺路线工序明细
     *
     * @param routingWorks 基础数据-工艺路线工序明细
     * @return 结果
     */
    @Override
    public int updateRoutingWorks(RoutingWorks routingWorks)
    {
        routingWorks.setUpdateBy(getUsername());
        routingWorks.setUpdateTime(new Date());
        routingWorks.setTenantId(getTenantId());
        return routingWorksMapper.updateRoutingWorks(routingWorks);
    }

    /**
     * 批量删除基础数据-工艺路线工序明细
     *
     * @param ids 需要删除的基础数据-工艺路线工序明细主键
     * @return 结果
     */
    @Override
    public int deleteRoutingWorksByIds(Long[] ids)
    {
        return routingWorksMapper.deleteRoutingWorksByIds(ids);
    }

    /**
     * 删除基础数据-工艺路线工序明细信息
     *
     * @param id 基础数据-工艺路线工序明细主键
     * @return 结果
     */
    @Override
    public int deleteRoutingWorksById(Long id)
    {
        return routingWorksMapper.deleteRoutingWorksById(id);
    }

}
