package cn.px.biz.service;

import java.util.List;
import cn.px.common.core.domain.entity.Tenant;
/**
 * 租户Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface ITenantBiz
{


    /**
     * 查询租户列表
     *
     * @param tenant 租户
     * @return 租户集合
     */
    public List<Tenant> searchTenantList(Tenant tenant);

    /**
     * 保存租户
     *
     * @param tenant 租户
     * @return 结果
     */
    public int saveTenant(Tenant tenant);


}
