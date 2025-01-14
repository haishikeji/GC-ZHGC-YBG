package cn.px.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.px.common.enums.ProductionOrderStatus;
import cn.px.common.enums.SalesOrderStatus;
import cn.px.common.exception.ServiceException;
import cn.px.common.utils.DateUtils;
import cn.px.domain.Customer;
import cn.px.domain.ProductionOrder;
import cn.px.domain.SalesOrder;
import cn.px.domain.SalesOrderDetail;
import cn.px.domain.vo.InventoryVo;
import cn.px.domain.vo.SalesOrderDetailVo;
import cn.px.domain.vo.SalesOrderListVo;
import cn.px.domain.vo.SalesOrderVo;
import cn.px.domain.*;
import cn.px.domain.enums.GeneratorEnum;
import cn.px.domain.vo.*;
import cn.px.mapper.CustomerMapper;
import cn.px.mapper.InventoryMapper;
import cn.px.service.IGeneratorSeqService;
import cn.px.service.IProductionOrderService;
import cn.px.service.ISalesOrderDetailService;
import cn.px.utils.TeaBeiBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.mapper.SalesOrderMapper;
import cn.px.service.ISalesOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import static cn.px.common.utils.SecurityUtils.getTenantId;
import static cn.px.common.utils.SecurityUtils.getUsername;

/**
 * 生产制造-销售订单Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class SalesOrderServiceImpl implements ISalesOrderService {
    @Autowired
    private SalesOrderMapper salesOrderMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private IGeneratorSeqService generatorSeqService;

    @Autowired
    private IProductionOrderService productionOrderService;

    @Autowired
    private ISalesOrderDetailService salesOrderDetailService;

    /**
     * 查询生产制造-销售订单
     *
     * @param id 生产制造-销售订单主键
     * @return 生产制造-销售订单
     */
    @Override
    public SalesOrderVo selectSalesOrderVoById(Long id) {
        SalesOrder salesOrder = salesOrderMapper.selectSalesOrderById(id);
        if (!Objects.isNull(salesOrder)) {
            SalesOrderVo salesOrderVo = new SalesOrderVo();
            BeanUtils.copyProperties(salesOrder, salesOrderVo);
            SalesOrderDetail salesOrderDetail = new SalesOrderDetail();
            salesOrderDetail.setSalesOrderId(salesOrder.getId());
            salesOrderVo.setList(salesOrderDetailService.selectSalesOrderDetailList(salesOrderDetail));
            Customer customer = customerMapper.selectCustomerById(salesOrderVo.getCustomerId());
            salesOrderVo.setCustomerName(customer.getCustName());
            salesOrderVo.setCustomerNumber(customer.getCustomer());
            return salesOrderVo;
        } else throw new ServiceException("未找到该销售数据请确认。");

    }
    @Override
    public SalesOrder selectSalesOrderById(Long id){
        return salesOrderMapper.selectSalesOrderById(id);
    }

    @Override
    public  List<SalesOrderDetailVo> selectSalesTypeDetailList(SalesOrder salesOrder){
        salesOrder.setTenantId(getTenantId());
        List<SalesOrderDetailVo> salesOrderDetails = salesOrderMapper.selectSalesTypeDetailList(salesOrder);
        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setTenantId(getTenantId());
        for (SalesOrderDetailVo salesOrderDeta: salesOrderDetails) {
            inventoryVo.setMaterialId(salesOrderDeta.getMaterialsId());
            List<InventoryVo> inventoryVoList =  inventoryMapper.selectInventoryVoList(inventoryVo);
            if (ObjectUtils.isNotEmpty(inventoryVoList)) {
                salesOrderDeta.setStashId(inventoryVoList.get(0).getStashId());
                salesOrderDeta.setStashName(inventoryVoList.get(0).getStashName());
                salesOrderDeta.setPositionId(inventoryVoList.get(0).getCargoPositionId());
                salesOrderDeta.setPositionName(inventoryVoList.get(0).getCargoPositionName());
            }

        }
        return salesOrderDetails;
    }

    /**
     * 根据销售订单编号查询生产制造-销售订单
     */

    @Override
    public SalesOrderVo selectSalesOrderByNumber(String number,Long tenantId) {
        //通过列表接口查询销售订单以及详情数据
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setNumber(number);
        salesOrder.setTenantId(tenantId);
        salesOrder.setStatus(SalesOrderStatus.SALES_STATUS_COMPLETE.getCode());//只查询销售订单状态为已完成的订单
        List<SalesOrder> salesOrderList = salesOrderMapper.selectSalesOrderList(salesOrder);
        if (!CollectionUtils.isEmpty(salesOrderList)) {
            SalesOrder order = salesOrderList.get(0);
            SalesOrderVo orderVo = new SalesOrderVo();
            BeanUtils.copyProperties(order, orderVo);
            SalesOrderDetail salesOrderDetail = new SalesOrderDetail();
            salesOrderDetail.setSalesOrderId(order.getId());
            orderVo.setList(salesOrderDetailService.selectSalesOrderDetailList(salesOrderDetail));
            Customer customer = customerMapper.selectCustomerById(order.getCustomerId());
            orderVo.setCustomerName(customer.getCustName());
            orderVo.setCustomerNumber(customer.getCustomer());
            return orderVo;
        }
        return null;
    }


    @Override
    public SalesOrderVo selectSalesOrderVoByNumber(String number,Long tenantId) {
        //通过列表接口查询销售订单以及详情数据
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setNumber(number);
        salesOrder.setTenantId(tenantId);
        salesOrder.setStatus(SalesOrderStatus.SALES_STATUS_COMPLETE.getCode());//只查询销售订单状态为已完成的订单
        salesOrder.setType(0L);
        List<SalesOrderListVo> salesOrderList = this.selectSalesOrderListVo(salesOrder);
        if (!CollectionUtils.isEmpty(salesOrderList)) {
            SalesOrderListVo order = salesOrderList.get(0);
            SalesOrderVo orderVo = new SalesOrderVo();
            BeanUtils.copyProperties(order, orderVo);
            SalesOrderDetail salesOrderDetail = new SalesOrderDetail();
            salesOrderDetail.setSalesOrderId(order.getId());
            orderVo.setList(salesOrderDetailService.selectSalesOrderDetailList(salesOrderDetail));
            Customer customer = customerMapper.selectCustomerById(order.getCustomerId());
            orderVo.setCustomerName(customer.getCustName());
            orderVo.setCustomerNumber(customer.getCustomer());
            return orderVo;
        }
        return null;
    }

    /**
     * 查询生产制造-销售订单列表
     *
     * @param salesOrder 生产制造-销售订单
     * @return 生产制造-销售订单
     */
    @Override
    public List<SalesOrder> selectSalesOrderList(SalesOrder salesOrder) {
        salesOrder.setTenantId(getTenantId());
        return salesOrderMapper.selectSalesOrderList(salesOrder);
    }

    @Override
    public List<SalesOrderListVo> selectSalesOrderListVo(SalesOrder salesOrder) {
        salesOrder.setTenantId(getTenantId());
        List<SalesOrderListVo> salesOrderList = salesOrderMapper.selectExtSalesOrderList(salesOrder);
        return salesOrderList;
    }

    /**
     * 新增生产制造-销售订单
     *
     * @param salesOrder 生产制造-销售订单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSalesOrder(SalesOrder salesOrder) {
        salesOrder.setCreateTime(DateUtils.getNowDate());
        salesOrder.setCreateBy(getUsername());
        salesOrder.setTenantId(getTenantId());
        return salesOrderMapper.insertSalesOrder(salesOrder);
    }

    @Override
    @Transactional
    public int saveSalesOrder(SalesOrderVo salesOrderVo) {
        SalesOrder salesOrder = TeaBeiBeanUtils.propertiesCopy(salesOrderVo, SalesOrder.class);
        salesOrder.setNumber(generatorSeqService.generatorSeqUtilByDate(GeneratorEnum.KEY_SO.getValue(), salesOrder.getTenantId()));
        salesOrder.setStatus(SalesOrderStatus.SALES_STATUS_NOT.getCode());
        salesOrder.setSchedule(new BigDecimal(20));
        salesOrder.setContractNumber(generatorSeqService.generatorSeqUtilByDate(GeneratorEnum.KEY_HT.getValue(), salesOrder.getTenantId()));
        insertSalesOrder(salesOrder);

        if (CollectionUtils.isEmpty(salesOrderVo.getList())) {
            throw new ServiceException("销售订单产品信息为空，请补充产品信息！");
        }
        salesOrderVo.getList().stream().forEach(i -> {
            i.setSalesOrderId(salesOrder.getId());
            if (ObjectUtils.isEmpty(i.getCount())||i.getCount() < 1) {
                throw new ServiceException("销售订单产品数量不能小于1");
            }
            if (i.getPrice().doubleValue() < 0) {
                throw new ServiceException("销售订单产品价格不能小于0");
            }
            salesOrderDetailService.insertSalesOrderDetail(i);
        });
        return 1;
    }

    /**
     * 修改生产制造-销售订单
     *
     * @param salesOrder 生产制造-销售订单
     * @return 结果
     */
    @Override
    public int updateSalesOrder(SalesOrder salesOrder) {
        salesOrder.setUpdateBy(getUsername());
        salesOrder.setUpdateTime(DateUtils.getNowDate());
        salesOrder.setTenantId(getTenantId());
        return salesOrderMapper.updateSalesOrder(salesOrder);
    }

    @Override
    @Transactional
    public int updateSalesOrderVo(SalesOrderVo salesOrderVo) {
        SalesOrder salesOrder = salesOrderMapper.selectSalesOrderById(salesOrderVo.getId());

        if (ObjectUtils.isEmpty(salesOrder)) {
            throw new ServiceException("该销售订单不存在，ID:" + salesOrderVo.getId());
        }

        salesOrder = TeaBeiBeanUtils.propertiesCopy(salesOrderVo,SalesOrder.class);

        if (!Objects.isNull(salesOrderVo) && !CollectionUtils.isEmpty(salesOrderVo.getList())) {
            SalesOrderDetail salesOrderDetail = new SalesOrderDetail();
            salesOrderDetail.setSalesOrderId(salesOrderVo.getId());
            List<SalesOrderDetail> salesOrderDetailVoList = salesOrderDetailService.selectSalesOrderDetailList(salesOrderDetail);

            List<Long> ids = salesOrderDetailVoList.stream().map(o -> o.getId()).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(ids)) {
                salesOrderDetailService.deleteSalesOrderDetailByIds(ids.toArray(new Long[ids.size()]));
            }
            salesOrderVo.getList().stream().forEach(i -> {
                i.setSalesOrderId(salesOrderVo.getId());
                if (ObjectUtils.isEmpty(i) || i.getCount() < 1) {
                    throw new ServiceException("销售订单产品数量不能小于1");
                }
                if (i.getPrice().doubleValue() < 0) {
                    throw new ServiceException("销售订单产品价格不能小于0");
                }
                salesOrderDetailService.insertSalesOrderDetail(i);
            });
        }

        return updateSalesOrder(salesOrder);
    }

    /**
     * 下发销售订单
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int sendOrder(Long id) {
        SalesOrderVo salesOrderVo = this.selectSalesOrderVoById(id);
        //修改销售订单状态位已下发
        if (salesOrderMapper.selectSalesOrderById(id).getStatus() == SalesOrderStatus.SALES_STATUS_NOT.getCode()) {
            SalesOrder salesOrder = new SalesOrder();
            salesOrder.setStatus(SalesOrderStatus.SALES_STATUS_HAS.getCode());
            salesOrder.setId(id);
            salesOrder.setSchedule(new BigDecimal(40));
            salesOrderMapper.updateSalesOrder(salesOrder);
        } else {
            throw new ServiceException("该销售订单状态为已下发，无法对同一订单进行重复下发！");
        }

        //根据销售订单明细下发生产订单
        if (!Objects.isNull(salesOrderVo) && !CollectionUtils.isEmpty(salesOrderVo.getList())) {
            salesOrderVo.getList().forEach(i -> {
                ProductionOrder productionOrder = new ProductionOrder();
                productionOrder.setNumber(generatorSeqService.generatorSeqUtilByDate(GeneratorEnum.KEY_SCD.getValue(), productionOrder.getTenantId()));
                productionOrder.setUnitId(i.getUnitId());
                productionOrder.setCount(i.getCount());
                productionOrder.setCustomerName(salesOrderVo.getCustomerName());
                productionOrder.setCustomerNumber(salesOrderVo.getCustomerNumber());
                productionOrder.setDate(salesOrderVo.getDeliveryDate());
                productionOrder.setMaterialsId(i.getMaterialsId());
                productionOrder.setMaterialsName(i.getMaterialsName());
                productionOrder.setMaterialsNumber(i.getMaterialsNumber());
                productionOrder.setSalesOrderId(salesOrderVo.getId());
                productionOrder.setSchedule(new BigDecimal(30));
                productionOrder.setStatus(ProductionOrderStatus.PLAN_STATUS_NOT_FORMULATE.getCode());
                productionOrderService.insertProductionOrder(productionOrder);
            });
        } else throw new ServiceException("系统中不存在该条数据请刷新页面后重试！");
        return 1;
    }

    /**
     * 撤回生产制造-生产订单
     */
    @Override
    @Transactional
    public int withdrawOder(Long id) {

        SalesOrder salesOrder = salesOrderMapper.selectSalesOrderById(id);

        if(ObjectUtils.isEmpty(salesOrder)){
            throw new ServiceException("该销售订单不存在，暂无法进行销售订单撤回！");
        }

        //修改销售订单状态位未下发
        if (salesOrder.getStatus() == SalesOrderStatus.SALES_STATUS_HAS.getCode()) {
            salesOrder.setStatus(SalesOrderStatus.SALES_STATUS_NOT.getCode());
            salesOrder.setSchedule(new BigDecimal(20));
            updateSalesOrder(salesOrder);
        } else{
            throw new ServiceException("该销售订单状态非已下发，暂无法进行销售订单撤回！");
        }

        ProductionOrder productionOrder = new ProductionOrder();
        productionOrder.setSalesOrderId(id);
        List<ProductionOrder> productionOrderList = productionOrderService.selectProductionOrderList(productionOrder);
        if (!CollectionUtils.isEmpty(productionOrderList)) {
            productionOrderList.stream().forEach(i -> {
                if (i.getStatus() != ProductionOrderStatus.PLAN_STATUS_NOT_FORMULATE.getCode()) {
                    throw new ServiceException("该销售订单已制定生产计划，不允许撤回");
                }
                productionOrderService.deleteProductionOrderById(i.getId());
            });
        }
        return 1;
    }

    /**
     * 批量删除生产制造-销售订单
     *
     * @param ids 需要删除的生产制造-销售订单主键
     * @return 结果
     */
    @Override
    public int deleteSalesOrderByIds(Long[] ids) {
        return salesOrderMapper.deleteSalesOrderByIds(ids);
    }

    /**
     * 删除生产制造-销售订单信息
     *
     * @param id 生产制造-销售订单主键
     * @return 结果
     */
    @Override
    public int deleteSalesOrderById(Long id) {
        return salesOrderMapper.deleteSalesOrderById(id);
    }
}
