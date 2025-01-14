package cn.px.biz.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.px.mapper.ServiceAftercareMapper;
import cn.px.domain.ServiceAftercare;
import cn.px.service.IServiceAftercareService;
import cn.px.biz.service.IServiceAftercareBiz;
/**
 * 售后服务-客户售后服务记录biz业务层处理
 *  多个服务操作可以放到这里 调用 mapper 或者 service都可以
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ServiceAftercareBizImpl implements IServiceAftercareBiz
{
    @Autowired
    private ServiceAftercareMapper ServiceAftercareMapper;
    @Autowired
    private IServiceAftercareService ServiceAftercareService;

    /**
     * 查询售后服务-客户售后服务记录列表
     *
     * @param ServiceAftercare 售后服务-客户售后服务记录
     * @return 售后服务-客户售后服务记录
     */
    @Override
    public List<ServiceAftercare> searchServiceAftercareList(ServiceAftercare ServiceAftercare)
    {
        //TODO 自己的代码

        return null;
    }

    /**
     * 新增售后服务-客户售后服务记录
     *
     * @param ServiceAftercare 售后服务-客户售后服务记录
     * @return 结果
     */
    @Transactional
    @Override
    public int saveServiceAftercare(ServiceAftercare ServiceAftercare)
    {
      //TODO 自己的代码

      return 0;
    }


}
