package cn.px.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.px.common.enums.OrderStatus;
import cn.px.common.enums.ProductionOrderStatus;
import cn.px.common.enums.SalesOrderStatus;
import cn.px.common.exception.ServiceException;
import cn.px.common.utils.DateUtils;
import cn.px.common.utils.SecurityUtils;
import cn.px.domain.ProductionPlan;
import cn.px.domain.SalesOrder;
import cn.px.domain.enums.GeneratorEnum;
import cn.px.domain.request.CountTypeReqVo;
import cn.px.domain.vo.ProductionOrderVo;
import cn.px.mapper.ProductionPlanMapper;
import cn.px.mapper.SalesOrderMapper;
import cn.px.service.IGeneratorSeqService;
import cn.px.utils.TeaBeiBeanUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.mapper.ProductionOrderMapper;
import cn.px.domain.ProductionOrder;
import cn.px.service.IProductionOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static cn.px.common.utils.SecurityUtils.getTenantId;
import static cn.px.common.utils.SecurityUtils.getUsername;

/**
 * 生产制造--生产订单Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ProductionOrderServiceImpl implements IProductionOrderService {
    @Autowired
    private ProductionOrderMapper productionOrderMapper;

    @Autowired
    private ProductionPlanMapper productionPlanMapper;

    @Autowired
    private SalesOrderMapper salesOrderMapper;

    @Autowired
    private IGeneratorSeqService generatorSeqService;

    /**
     * 查询生产制造--生产订单
     *
     * @param id 生产制造--生产订单主键
     * @return 生产制造--生产订单
     */
    @Override
    public ProductionOrder selectProductionOrderById(Long id) {
        return productionOrderMapper.selectProductionOrderById(id);
    }

    /**
     * 查询生产制造--生产订单
     *
     * @param id 生产制造--生产订单主键  合集
     * @return 生产制造--生产订单
     */
    @Override
    public ProductionOrderVo selectProductionOrderById(List<String> id) {

        ProductionOrderVo productionOrderVo = new ProductionOrderVo();
        ProductionOrder productionOrder = new ProductionOrder();
        Double count = 0D;
        List<ProductionOrder> numbers = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(id)) {
          //  String[] numbersStrArr = id.split(",");

        for (String str : id) {
            productionOrder = productionOrderMapper.selectProductionOrderById(Long.parseLong(str));
            count+=productionOrder.getCount();
            numbers.add(productionOrder);
        }
    }
        productionOrderVo.setCount(count);
        productionOrderVo.setMaterialsName(productionOrder.getMaterialsName());
        productionOrderVo.setMaterialsId(productionOrder.getMaterialsId());
        productionOrderVo.setMaterialsNumber(productionOrder.getMaterialsNumber());
        productionOrderVo.setUnitId((long)productionOrder.getUnitId());
        productionOrderVo.setProductionList(numbers);
        return productionOrderVo;
    }

    @Override
    public ProductionOrderVo selectmergeById(Long id) {

        ProductionOrderVo productionOrderVo = new ProductionOrderVo();
        ProductionOrder productionOrder = productionOrderMapper.selectProductionOrderById(id);
        List<ProductionOrder> numbers = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(productionOrder)) {
            String[] numbersStrArr = productionOrder.getMergeNumber().split(",");

            for (String str : numbersStrArr) {
                productionOrder = productionOrderMapper.selectProductionOrderById(Long.parseLong(str));
                numbers.add(productionOrder);
            }
        }
        productionOrderVo.setStatus(3);
        productionOrderVo.setProductionList(numbers);
        return productionOrderVo;
    }

    /**
     * 查询生产制造--生产订单列表
     *
     * @param productionOrder 生产制造--生产订单
     * @return 生产制造--生产订单
     */
    @Override
    public List<ProductionOrderVo> selectExtProductionOrderList(ProductionOrderVo productionOrder) {
        productionOrder.setTenantId(getTenantId());
        productionOrder.setMerge(1);
        List<ProductionOrderVo> productionOrderList = productionOrderMapper
                .selectExtProductionOrderList(productionOrder);
        for(ProductionOrderVo plan :productionOrderList){
            if(plan.getMerge() == 2){
                String[] numbersStrArr = plan.getMergeNumber().split(",");
                List<ProductionOrderVo> proOrder = new ArrayList<ProductionOrderVo>();
                for(String orderId : numbersStrArr){
                    proOrder.add(productionOrderMapper.selectExtProductionOrderById(Long.parseLong(orderId)));
                }
                plan.setSalesOrderNumber("已合并");
                plan.setChildren(proOrder);
            }
        }
        return productionOrderList;
    }

    @Override
    public List<ProductionOrder> selectProductionOrderList(ProductionOrder productionOrder) {
        productionOrder.setTenantId(getTenantId());
        return productionOrderMapper.selectProductionOrderList(productionOrder);
    }

    /**
     * 新增生产制造--生产订单
     *
     * @param productionOrder 生产制造--生产订单
     * @return 结果
     */
    @Override
    public int insertProductionOrder(ProductionOrder productionOrder) {
        productionOrder.setCreateTime(DateUtils.getNowDate());
        productionOrder.setCreateBy(SecurityUtils.getUsername());
        productionOrder.setTenantId(getTenantId());
        return productionOrderMapper.insertProductionOrder(productionOrder);
    }

    /**
     * 修改生产制造--生产订单
     *
     * @param productionOrder 生产制造--生产订单
     * @return 结果
     */
    @Override
    public int updateProductionOrder(ProductionOrder productionOrder) {
        productionOrder.setUpdateTime(DateUtils.getNowDate());
        productionOrder.setUpdateBy(getUsername());
        productionOrder.setTenantId(getTenantId());
        return productionOrderMapper.updateProductionOrder(productionOrder);
    }

    /**
     * 批量删除生产制造--生产订单
     *
     * @param ids 需要删除的生产制造--生产订单主键
     * @return 结果
     */
    @Override
    public int deleteProductionOrderByIds(Long[] ids) {
        return productionOrderMapper.deleteProductionOrderByIds(ids);
    }

    /**
     * 删除生产制造--生产订单信息
     *
     * @param id 生产制造--生产订单主键
     * @return 结果
     */
    @Override
    public int deleteProductionOrderById(Long id) {
        return productionOrderMapper.deleteProductionOrderById(id);
    }

    /**
     * 合并指定计划
     * @param productionOrderVo
     * @return
     */
    @Override
    public int insterMergePlanData(ProductionOrderVo productionOrderVo) {
        int result = 0;
        ProductionOrder productionOrder = new ProductionOrder();
        Double count = productionOrderVo.getCount();
        if (ObjectUtils.isNotEmpty(productionOrderVo.getList())) {

            //生产计划产出产品同一数量不能大于生产订单数量
            for (ProductionPlan plan : productionOrderVo.getList()) {
                List<ProductionPlan> plans = productionOrderVo.getList().stream().filter(item -> item.getMatterId().equals(plan.getMatterId())).collect(Collectors.toList());
                if (plans.size() > 0) {
                    int quantity = plans.stream().mapToInt(ProductionPlan::getProductionQuantity).sum();
                    if (quantity != count) {
                        throw new ServiceException("该合并生产订单，总生产计划数量：" + quantity + "，不等于生产订单数：" + count);
                    }
                }
            }
        }
        if (ObjectUtils.isNotEmpty(productionOrderVo.getMergeNumber())) {
            String orderId = productionOrderVo.getMergeNumber();
            String[] numbersStrArr = orderId.split(",");
            for (String plan : numbersStrArr) {
                productionOrder.setId(Long.parseLong(plan));
                productionOrder.setMerge(1);
                productionOrder.setSchedule(new BigDecimal(100));
                productionOrder.setStatus(ProductionOrderStatus.PLAN_STATUS_SEND.getCode());
                productionOrderMapper.updateProductionOrder(productionOrder);
                ProductionOrder salesOrderId = productionOrderMapper.selectProductionOrderById(productionOrder.getId());
                SalesOrder salesOrder = salesOrderMapper.selectSalesOrderById(salesOrderId.getSalesOrderId());
                if (ObjectUtils.isNotEmpty(salesOrder)) {
                    salesOrder.setSchedule(new BigDecimal(50));
                    salesOrder.setStatus(SalesOrderStatus.SALES_STATUS_PRODUCTION.getCode());
                    salesOrderMapper.updateSalesOrder(salesOrder);
                }

            }
            productionOrder = new ProductionOrder();
            productionOrder = TeaBeiBeanUtils.propertiesCopy(productionOrderVo, ProductionOrder.class);
            productionOrder.setNumber(generatorSeqService.generatorSeqUtilByDate(GeneratorEnum.KEY_SCD.getValue(), productionOrder.getTenantId()));
            productionOrder.setSalesOrderId((long) -1);
            productionOrder.setSchedule(new BigDecimal(100));
            productionOrder.setStatus(ProductionOrderStatus.PLAN_STATUS_SEND.getCode());
            productionOrder.setMerge(2);
            productionOrder.setCount(count);
            productionOrder.setCreateTime(DateUtils.getNowDate());
            productionOrder.setCreateBy(SecurityUtils.getUsername());
            productionOrder.setTenantId(getTenantId());
            productionOrder.setMaterialsId(productionOrderVo.getList().get(0).getMatterId());
            productionOrder.setMaterialsName(productionOrderVo.getList().get(0).getMatterName());
            productionOrder.setMaterialsNumber(productionOrderVo.getList().get(0).getMatterNumber());
            productionOrderMapper.insertProductionOrder(productionOrder);
            ProductionOrder productionOrderNumber = productionOrderMapper.selectProductionOrderByName(productionOrder.getNumber());

                // 添加数据uniLogin
                for (ProductionPlan productionPlan : productionOrderVo.getList()) {
                    String number = generatorSeqService.generatorSeqUtilByDate(GeneratorEnum.KEY_JH.getValue(), productionOrderNumber.getTenantId());
                    productionPlan.setNumber(number);
                    productionPlan.setStatus(OrderStatus.ORDER_STATUS_NOT_FORMULATE.getCode());
                    productionPlan.setProductOrderId(productionOrderNumber.getId());
                    productionPlan.setProductOrderNumber(productionOrderNumber.getNumber());
                    productionPlan.setCreateTime(DateUtils.getNowDate());
                    productionPlan.setCreateBy(SecurityUtils.getUsername());
                    productionPlan.setSchedule(new BigDecimal("0"));
                    productionPlan.setTenantId(getTenantId());
                    result = productionPlanMapper.insertProductionPlan(productionPlan);
                }


            } else {
                throw new ServiceException("该生产订单计划信息为空");
            }



        return result;
    }

    /**
     * 指定计划
     * @param productionOrderVo
     * @return
     */
    @Override
    public int insterPlanData(ProductionOrderVo productionOrderVo) {
        int result = 0;
        ProductionOrder productionOrder = productionOrderMapper.selectProductionOrderById(productionOrderVo.getId());
        if(ObjectUtils.isEmpty(productionOrder)){
            throw new ServiceException("该生产订单不存在,Id:"+productionOrderVo.getProductOrderId());
        }
        if (ObjectUtils.isNotEmpty(productionOrderVo.getList())) {
            Double count = productionOrder.getCount();
            //生产计划产出产品同一数量不能大于生产订单数量
            for(ProductionPlan plan : productionOrderVo.getList()){
                List<ProductionPlan> plans = productionOrderVo.getList().stream().filter(item->item.getMatterId().equals(plan.getMatterId())).collect(Collectors.toList());
                if(plans.size()>0){
                    int quantity = plans.stream().mapToInt(ProductionPlan::getProductionQuantity).sum();
                    if(quantity>count){
                        throw new ServiceException("该生产订单：工序编码{"+productionOrder.getNumber()+"}，总生产计划数量："+quantity+"，大于生产订单数：" + count);
                    }
                }
            }

            if (productionOrderVo.getStatus() == ProductionOrderStatus.PLAN_STATUS_NOT_FORMULATE.getCode()) {
                // 添加数据
                for(ProductionPlan productionPlan :productionOrderVo.getList()){
                    String number = generatorSeqService.generatorSeqUtilByDate(GeneratorEnum.KEY_JH.getValue(), productionOrder.getTenantId());
                    productionPlan.setNumber(number);
                    productionPlan.setStatus(OrderStatus.ORDER_STATUS_NOT_FORMULATE.getCode());
                    productionPlan.setProductOrderId(productionOrderVo.getId());
                    productionPlan.setProductOrderNumber(productionOrderVo.getNumber());
                    productionPlan.setCreateTime(DateUtils.getNowDate());
                    productionPlan.setCreateBy(SecurityUtils.getUsername());
                    productionPlan.setSchedule(new BigDecimal("0"));
                    productionPlan.setTenantId(getTenantId());
                    result = productionPlanMapper.insertProductionPlan(productionPlan);
                }
            } else {
                throw new ServiceException("该状态不可进行计划制定");
            }
        }else{
            throw new ServiceException("该生产订单计划信息为空");
        }

        productionOrder.setStatus(ProductionOrderStatus.PLAN_STATUS_AWAIT.getCode());
        productionOrder.setSchedule(new BigDecimal(60));
        result = updateProductionOrder(productionOrder);
        return result;
    }

    /**
     * 修改生产订单 - 未下达的生产订单
     * @param productionOrderVo
     * @return
     */
    @Override
    public int updatePlanData(ProductionOrderVo productionOrderVo) {
        int result = 0;
        if (!Objects.isNull(productionOrderVo)) {
            ProductionOrder productionOrderResult = productionOrderMapper
                    .selectProductionOrderById(productionOrderVo.getId());
            // 校验计划数量是否超出需求数量
            if (ObjectUtils.isNotEmpty(productionOrderResult)) {

                //生产计划产出产品同一数量不能大于生产订单数量
                for(ProductionPlan plan : productionOrderVo.getList()){
                    List<ProductionPlan> plans = productionOrderVo.getList().stream().filter(item->item.getMatterId().equals(plan.getMatterId())).collect(Collectors.toList());
                    if(plans.size()>0){
                        int quantity = plans.stream().mapToInt(ProductionPlan::getProductionQuantity).sum();
                        if(quantity>productionOrderResult.getCount()){
                            throw new ServiceException("该生产订单：工序编码{"+productionOrderResult.getNumber()+"}，总生产计划数量："+quantity+"，大于生产订单数：" + productionOrderResult.getCount());
                        }
                    }
                }
            }else{
                throw new ServiceException("生产订单不存在");
            }

            if (productionOrderResult.getStatus() == ProductionOrderStatus.PLAN_STATUS_AWAIT.getCode()) {

                if(CollectionUtils.isEmpty(productionOrderVo.getList())) {
                    throw new ServiceException("生产订单号:"+productionOrderResult.getNumber()+"，计划信息为空，不能下达！");
                }else{
                    List<ProductionPlan> productionPlans = productionPlanMapper.selectProductionPlanList(new ProductionPlan(productionOrderVo.getId()));
                    if (ObjectUtils.isNotEmpty(productionPlans)) {
                        // 删除数据
                        List<Long> ids = productionPlans.stream().map(o -> o.getId()).collect(Collectors.toList());
                        productionPlanMapper.deleteProductionPlanByIds(ids.toArray(new Long[ids.size()]));
                    }
                    // 添加数据
                    for(ProductionPlan productionPlan :productionOrderVo.getList()){
                        String number = generatorSeqService.generatorSeqUtilByDate(GeneratorEnum.KEY_JH.getValue(), productionOrderResult.getTenantId());
                        productionPlan.setNumber(number);
                        productionPlan.setStatus(OrderStatus.ORDER_STATUS_NOT_FORMULATE.getCode());
                        productionPlan.setProductOrderId(productionOrderVo.getId());
                        productionPlan.setProductOrderNumber(productionOrderVo.getNumber());
                        productionPlan.setCreateTime(DateUtils.getNowDate());
                        productionPlan.setCreateBy(SecurityUtils.getUsername());
                        productionPlan.setSchedule(new BigDecimal("0"));
                        productionPlan.setTenantId(getTenantId());
                        result = productionPlanMapper.insertProductionPlan(productionPlan);
                    }
                }

            } else {
                throw new ServiceException("生产订单号:{"+productionOrderResult.getNumber()+"},状态为："+productionOrderResult.getStatus()+",不能下达计划!");
            }

        }
        return result;
    }

    /**
     * 生产订单下达
     * @param productionOrderVo
     * @return
     */
    @Override
    @Transactional
    public int sendPlanData(ProductionOrderVo productionOrderVo) {
            ProductionOrder productionOrderResult = productionOrderMapper
                    .selectProductionOrderById(productionOrderVo.getId());
        if (ObjectUtils.isNotEmpty(productionOrderResult)) {
            //待下达
            if (productionOrderResult.getStatus() == ProductionOrderStatus.PLAN_STATUS_SEND.getCode()) {
                throw new ServiceException("生产订单号:{"+productionOrderResult.getNumber()+"},状态为："+productionOrderResult.getStatus()+",不能下达生产订单!");
            }
            // 校验计划数量是否超出需求数量
            if (ObjectUtils.isNotEmpty(productionOrderVo.getList())) {

                for(ProductionPlan plan : productionOrderVo.getList()){
                    ProductionPlan productionPlan = productionPlanMapper.selectProductionPlanById(plan.getId());
                    if(ObjectUtils.isNotEmpty(productionPlan)){
                        productionPlan.setStartTime(plan.getStartTime());
                        productionPlan.setEndTime(plan.getEndTime());
                        productionPlan.setProductionQuantity(plan.getProductionQuantity());
                        productionPlanMapper.updateProductionPlan(productionPlan);
                    }
                }

                //计划下达，查询该生产订单计划，不能依赖客户端数据
                List<ProductionPlan> planList = productionPlanMapper.selectProductionPlanList(new ProductionPlan(productionOrderResult.getId()));

                //生产计划产出产品同一数量不能大于生产订单数量
                for(ProductionPlan plan : planList){
                    List<ProductionPlan> plans = productionPlanMapper.selectProductionPlanList(plan).stream().filter(item->item.getMatterId().equals(plan.getMatterId())).collect(Collectors.toList());
                    if(plans.size()>0){
                        //生产计划产出产品同一数量不能大于生产订单数量
                        int quantity = plans.stream().mapToInt(ProductionPlan::getProductionQuantity).sum();
                        if(quantity>productionOrderResult.getCount()){
                            throw new ServiceException("该生产订单：工序编码{"+productionOrderResult.getNumber()+"}，总生产计划数量："+quantity+"，大于生产订单数：" + productionOrderResult.getCount());
                        }
                    }

                }
            }else {
                throw new ServiceException("生产订单号:"+productionOrderResult.getNumber()+"，计划信息为空，不能下达！");
            }

            //该销售订单的生产订单数量
            List<ProductionOrder> list_old = productionOrderMapper.selectProductionOrderList(new ProductionOrder(productionOrderResult.getSalesOrderId()));

            //该销售订单的生产订单已下达数量
            List<ProductionOrder> list_new = productionOrderMapper.selectProductionOrderList(new ProductionOrder(ProductionOrderStatus.PLAN_STATUS_SEND.getCode(),productionOrderResult.getSalesOrderId()));

            //销售订单的生产订单全部下达，更新销售订单进度
            if(list_old.size() == list_new.size()){
                SalesOrder salesOrder = salesOrderMapper.selectSalesOrderById(productionOrderResult.getSalesOrderId());
                if(ObjectUtils.isNotEmpty(salesOrder)){
                    salesOrder.setSchedule(new BigDecimal(50));
                    salesOrder.setStatus(SalesOrderStatus.SALES_STATUS_PRODUCTION.getCode());
                    salesOrderMapper.updateSalesOrder(salesOrder);
                }else{
                    throw new ServiceException("生产订单号:{"+productionOrderResult.getNumber()+"},的销售订单【"+productionOrderResult.getSalesOrderId()+"】不存在,不能下达生产订单!");
                }
            }

        }else{
            throw new ServiceException("该生产订单不存在，ID:"+productionOrderVo.getId());
        }

        // 状态为已下达
        productionOrderResult.setStatus(ProductionOrderStatus.PLAN_STATUS_SEND.getCode());
        productionOrderResult.setSchedule(new BigDecimal(100));

        return  updateProductionOrder(productionOrderResult);
    }

    @Override
    public int count(CountTypeReqVo typeReqVo) {
        return productionOrderMapper.count(typeReqVo);
    }
    @Override
    public ProductionOrder selectProductionOrderByName(String number){
        return productionOrderMapper.selectProductionOrderByName(number);
    }
}
