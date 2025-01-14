package cn.px.service;

import java.util.List;
import cn.px.domain.ServiceAftercare;
import cn.px.domain.vo.ServiceAftercareVo;

/**
 * 售后服务-客户售后服务记录Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IServiceAftercareService
{
    /**
     * 查询售后服务-客户售后服务记录
     *
     * @param id 售后服务-客户售后服务记录主键
     * @return 售后服务-客户售后服务记录
     */
    public ServiceAftercare selectServiceAftercareById(Long id);

    public ServiceAftercareVo selectServiceAftercareVoById(Long id);


    /**
     * 查询售后服务-客户售后服务记录列表
     *
     * @param ServiceAftercare 售后服务-客户售后服务记录
     * @return 售后服务-客户售后服务记录集合
     */
    public List<ServiceAftercare> selectServiceAftercareList(ServiceAftercare ServiceAftercare);

    /**
     * 新增售后服务-客户售后服务记录
     *
     * @param ServiceAftercare 售后服务-客户售后服务记录
     * @return 结果
     */
    public int insertServiceAftercare(ServiceAftercare ServiceAftercare);

    /**
     * 修改售后服务-客户售后服务记录
     *
     * @param ServiceAftercare 售后服务-客户售后服务记录
     * @return 结果
     */
    public int updateServiceAftercare(ServiceAftercare ServiceAftercare);

    /**
     * 批量删除售后服务-客户售后服务记录
     *
     * @param ids 需要删除的售后服务-客户售后服务记录主键集合
     * @return 结果
     */
    public int deleteServiceAftercareByIds(Long[] ids);

    /**
     * 删除售后服务-客户售后服务记录信息
     *
     * @param id 售后服务-客户售后服务记录主键
     * @return 结果
     */
    public int deleteServiceAftercareById(Long id);
}
