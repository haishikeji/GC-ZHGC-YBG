package cn.px.service;

import java.util.List;

import cn.px.domain.Routing;
import cn.px.domain.vo.RoutingVo;
import cn.px.domain.vo.RoutingWorkVo;

/**
 * 基础数据-工艺路线Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IRoutingService
{
    /**
     * 查询基础数据-工艺路线
     *
     * @param id 基础数据-工艺路线主键
     * @return 基础数据-工艺路线
     */
    public Routing selectRoutingById(Long id);

    public RoutingVo selectRoutingVoById(Long id);

    /**
     * 查询基础数据-工艺路线列表
     *
     * @param routing 基础数据-工艺路线
     * @return 基础数据-工艺路线集合
     */
    public List<Routing> selectRoutingList(Routing routing);

    /**
     * 工艺路线VO集合
     * @param routing
     * @return
     */
    public List<RoutingVo> selectRoutingListVo(Routing routing);

    /**
     * 工艺路线工序集合
     * @param routing
     * @return
     */
    public List<RoutingWorkVo> selectRoutingWorkListPageVo(RoutingWorkVo routing);

    /**
     * 新增基础数据-工艺路线
     *
     * @param routing 基础数据-工艺路线
     * @return 结果
     */
    public int insertRouting(Routing routing);

    public int saveRouting(RoutingVo routingVo);
    /**
     * 复制基础数据-工艺路线
     *
     * @param routing 基础数据-工艺路线
     * @return 结果
     */
    public int copyRouting(RoutingVo routingVo);

    /**
     * 修改基础数据-工艺路线
     *
     * @param routing 基础数据-工艺路线
     * @return 结果
     */
    public int updateRouting(Routing routing);

    /**
     * 批量删除基础数据-工艺路线
     *
     * @param ids 需要删除的基础数据-工艺路线主键集合
     * @return 结果
     */
    public int deleteRoutingByIds(Long[] ids);

    /**
     * 删除基础数据-工艺路线信息
     *
     * @param id 基础数据-工艺路线主键
     * @return 结果
     */
    public int deleteRoutingById(Long id);

    public List<Routing> selectRoutingbyName(Routing reqRouting);


}
