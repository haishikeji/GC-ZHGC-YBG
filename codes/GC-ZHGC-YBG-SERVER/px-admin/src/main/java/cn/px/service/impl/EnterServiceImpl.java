package cn.px.service.impl;

import java.math.BigDecimal;
import java.util.*;

import cn.px.domain.*;
import cn.px.common.exception.ServiceException;
import cn.px.common.utils.DateUtils;
import cn.px.common.utils.SecurityUtils;
import cn.px.domain.*;
import cn.px.domain.enums.GeneratorEnum;
import cn.px.domain.request.CountTypeReqVo;
import cn.px.domain.vo.EnterVo;
import cn.px.domain.vo.OutMaterialsVo;
import cn.px.mapper.SalesOrderDetailMapper;
import cn.px.mapper.StockMapper;
import cn.px.service.IInventoryService;
import cn.px.service.IMaterialsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.mapper.EnterMapper;
import cn.px.service.IEnterService;
import org.springframework.util.CollectionUtils;

import static cn.px.common.utils.SecurityUtils.getTenantId;
import static cn.px.common.utils.SecurityUtils.getUserId;

/**
 * 仓储管理-入库Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class EnterServiceImpl implements IEnterService {
    @Autowired
    private EnterMapper enterMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SalesOrderDetailMapper SalesOrderDetailMapper;

    @Autowired
    private IInventoryService inventoryService;

    @Autowired
    private IMaterialsService materialsService;

    /**
     * 查询仓储管理-入库
     *
     * @param id 仓储管理-入库主键
     * @return 仓储管理-入库
     */
    @Override
    public Enter selectEnterById(Long id) {
        return enterMapper.selectEnterById(id);
    }

    /**
     * 查询仓储管理-入库列表
     *
     * @param enter 仓储管理-入库
     * @return 仓储管理-入库
     */
    @Override
    public List<Enter> selectEnterList(Enter enter) {
        enter.setTenantId(getTenantId());
        return enterMapper.selectEnterList(enter);
    }

    public List<EnterVo> selectEnterVoList(EnterVo enter){
        enter.setTenantId(getTenantId());
        return enterMapper.selectEnterVoList(enter);
    }

    /**
     * 新增仓储管理-入库
     *
     * @param enter 仓储管理-入库
     * @return 结果
     */
    @Override
    public int insertEnter(Enter enter) {
        enter.setCreateTime(DateUtils.getNowDate());
        enter.setCreateBy(SecurityUtils.getUserId().toString());
        enter.setDate(DateUtils.getNowDate());
        enter.setTenantId(getTenantId());
        return enterMapper.insertEnter(enter);
    }

    @Override
    public  int  saveEnterByOrderNumber(Enter enter){

        Materials materials = materialsService.selectMaterialsById(enter.getMaterialsId());
        if(ObjectUtils.isNotEmpty(materials)){
            enter.setObjectType(materials.getCategoryId());
        }else{
            throw new ServiceException("该入库物料不存在");
        }

        //查询该入库物料的库存信息
        Inventory materialQuantity = new Inventory();
        materialQuantity.setMaterialId(enter.getMaterialsId());
        materialQuantity.setCargoPositionId(enter.getPositionId());
        materialQuantity.setStashId(enter.getStashId());

        //入库，只会一次入一个仓库
        Inventory inventory = inventoryService.selectInventoryByEnter(materialQuantity);

        if(ObjectUtils.isNotEmpty(inventory)){
            inventory.setQuantity(inventory.getQuantity() + enter.getCount());
            inventory.setInventoryObj(enter.getObjectType());
            inventoryService.updateInventory(inventory);
        }else{
            inventory = new Inventory();
            inventory.setMaterialId(enter.getMaterialsId());
            inventory.setStashId(enter.getStashId());
            inventory.setCargoPositionId(enter.getPositionId());
            inventory.setQuantity(enter.getCount());
            inventory.setInventoryObj(enter.getObjectType());
            //首页统计的库存数量是取的updateTime时间，新增也需赋值
            inventory.setUpdateTime(DateUtils.getNowDate());
            inventory.setUpdateBy(SecurityUtils.getUserId().toString());
            inventoryService.insertInventory(inventory);
        }



        if (ObjectUtils.isNotEmpty(enter.getOrderNumber())) {
            //入库批次 code + 报工单编号mmdd+number
            enter.setBatchNo(GeneratorEnum.KEY_ENTER.getKey() + enter.getOrderNumber().substring(4, enter.getOrderNumber().length()));
        }

        return insertEnter(enter);
    }


    @Override
    public  int  saveSalesByOrderNumber(Enter enter, OutMaterialsVo out){

        Materials materials = materialsService.selectMaterialsById(enter.getMaterialsId());
        if(ObjectUtils.isNotEmpty(materials)){
            enter.setObjectType(materials.getCategoryId());
        }else{
            throw new ServiceException("该入库入库物料不存在");
        }

        //查询该入库物料的库存信息
        Inventory materialQuantity = new Inventory();
        materialQuantity.setMaterialId(enter.getMaterialsId());
        materialQuantity.setCargoPositionId(enter.getPositionId());
        materialQuantity.setStashId(enter.getStashId());

        //入库，只会一次入一个仓库
        Inventory inventory = inventoryService.selectInventoryByEnter(materialQuantity);

        if(ObjectUtils.isNotEmpty(inventory)){
            inventory.setQuantity(inventory.getQuantity() + enter.getCount());
            inventory.setInventoryObj(enter.getObjectType());
            inventoryService.updateInventory(inventory);
        }else{
            inventory = new Inventory();
            inventory.setMaterialId(enter.getMaterialsId());
            inventory.setStashId(enter.getStashId());
            inventory.setCargoPositionId(enter.getPositionId());
            inventory.setQuantity(enter.getCount());
            inventory.setInventoryObj(enter.getObjectType());
            //首页统计的库存数量是取的updateTime时间，新增也需赋值
            inventory.setUpdateTime(DateUtils.getNowDate());
            inventory.setUpdateBy(SecurityUtils.getUserId().toString());
            inventoryService.insertInventory(inventory);
        }
        if (ObjectUtils.isNotEmpty(enter.getOrderNumber())) {
            //入库批次 code + 报工单编号mmdd+number
            enter.setBatchNo(GeneratorEnum.KEY_ENTER.getKey() + enter.getOrderNumber().substring(4, enter.getOrderNumber().length()));
        }
        // 修改销售订单明细入库信息
        SalesOrderDetail salesOrder  = new SalesOrderDetail();
        salesOrder.setId(out.getId());
        salesOrder.setStockQuantity(out.getAvailable()+out.getStockQuantity());
        SalesOrderDetailMapper.updateSalesOrderDetail(salesOrder);
        return insertEnter(enter);
    }

    @Override
    public  int  saveReturnByOrderNumber(Enter enter){

        Materials materials = materialsService.selectMaterialsById(enter.getMaterialsId());
        if(ObjectUtils.isNotEmpty(materials)){
            enter.setObjectType(materials.getCategoryId());
        }else{
            throw new ServiceException("该退货入库物料不存在");
        }

        //查询该入库物料的库存信息
        Inventory materialQuantity = new Inventory();
        materialQuantity.setMaterialId(enter.getMaterialsId());
        materialQuantity.setCargoPositionId(enter.getPositionId());
        materialQuantity.setStashId(enter.getStashId());

        //入库，只会一次入一个仓库
        Inventory inventory = inventoryService.selectInventoryByEnter(materialQuantity);

        if(ObjectUtils.isNotEmpty(inventory)){
            inventory.setQuantity(inventory.getQuantity() + enter.getCount());
            inventory.setInventoryObj(enter.getObjectType());
            inventoryService.updateInventory(inventory);
        }else{
            inventory = new Inventory();
            inventory.setMaterialId(enter.getMaterialsId());
            inventory.setStashId(enter.getStashId());
            inventory.setCargoPositionId(enter.getPositionId());
            inventory.setQuantity(enter.getCount());
            inventory.setInventoryObj(enter.getObjectType());
            //首页统计的库存数量是取的updateTime时间，新增也需赋值
            inventory.setUpdateTime(DateUtils.getNowDate());
            inventory.setUpdateBy(SecurityUtils.getUserId().toString());
            inventoryService.insertInventory(inventory);
        }
        if (ObjectUtils.isNotEmpty(enter.getOrderNumber())) {
            //入库批次 code + 报工单编号mmdd+number
            enter.setBatchNo(GeneratorEnum.KEY_ENTER.getKey() + enter.getOrderNumber().substring(4, enter.getOrderNumber().length()));
        }
        //出库订单未修改
          Stock stock= stockMapper.selectStockById(enter.getId());
           stock.setStockQuantity(stock.getStockQuantity()+ enter.getCount());
           stockMapper.updateStock(stock);
        return insertEnter(enter);
    }
    /**
     * 修改仓储管理-入库
     *
     * @param enter 仓储管理-入库
     * @return 结果
     */
    @Override
    public int updateEnter(Enter enter) {
        enter.setUpdateTime(DateUtils.getNowDate());
        enter.setUpdateBy(getUserId().toString());
        return enterMapper.updateEnter(enter);
    }

    /**
     * 批量删除仓储管理-入库
     *
     * @param ids 需要删除的仓储管理-入库主键
     * @return 结果
     */
    @Override
    public int deleteEnterByIds(Long[] ids) {
        return enterMapper.deleteEnterByIds(ids);
    }

    /**
     * 删除仓储管理-入库信息
     *
     * @param id 仓储管理-入库主键
     * @return 结果
     */
    @Override
    public int deleteEnterById(Long id) {
        return enterMapper.deleteEnterById(id);
    }

    @Override
    public int selectStorageEnterCount(CountTypeReqVo typeReqVo) {
        return enterMapper.selectStorageEnterCount(typeReqVo);
    }

    @Override
    public List<Map<String, BigDecimal>> getStorageTypeProportion(Long tenantId) {
        return enterMapper.getStorageTypeProportion(tenantId);
    }

    /**
     * 月度入库趋势
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> selectEnterCountTopTrendList(Long tenantId) {
        //最终返回结果
        List<Map<String, Object>> list = new ArrayList<>();
        //区分月份数据
        Map<String, Map<String, Double>> map = new LinkedHashMap<>();

        List<Map<String, Object>> mapList = enterMapper.selectEnterCountTopTrendList(tenantId);

        if (!CollectionUtils.isEmpty(mapList)) {

            mapList.stream().forEach(i -> {
                if (!Objects.isNull(map.get(i.get("name")))) {
                    map.get(i.get("name")).put(i.get("MONTH").toString(), Objects.isNull(i.get("value"))?null:Double.parseDouble(i.get("value").toString()));
                } else {
                    if(i.get("name")!=null){
                        Map<String, Double> map1 = new LinkedHashMap<>();
                        map1.put(i.get("MONTH").toString(), Double.parseDouble(i.get("value").toString()));
                        map.put(i.get("name").toString(), map1);
                    }
                }
            });

            Iterator<Map.Entry<String, Map<String, Double>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map<String, Object> objectMap = new LinkedHashMap<>();
                Map.Entry<String, Map<String, Double>> entry = iterator.next();
                objectMap.put("name", entry.getKey());
                objectMap.put("type", "line");
                objectMap.put("stack", "Total");
                Iterator<Map.Entry<String, Double>> iterator1 = entry.getValue().entrySet().iterator();
                List<Double> longList = new ArrayList<>();
                while (iterator1.hasNext()) {
                    Map.Entry<String, Double> entry1 = iterator1.next();
                    if (Double.parseDouble(entry1.getKey()) > 0) {
                        if (CollectionUtils.isEmpty(longList)) {
                            for (int i = 0; i < Long.parseLong(entry1.getKey()) - 1; i++) {
                                longList.add(0.0);
                            }
                        }
                        longList.add(entry1.getValue());
                    }
                }
                if (CollectionUtils.isEmpty(longList)){
                    objectMap.put("data", new ArrayList<>(entry.getValue().values()));
                }else {
                    objectMap.put("data", longList);
                }
                list.add(objectMap);
            }
        }
        return list;
    }

    @Override
    public List<Map<String, BigDecimal>> getEnterTypeProportion() {
        return enterMapper.getEnterTypeProportion();
    }
    @Override
    public List<Enter> enterTop20list(Long tenantId){
        return enterMapper.enterTop20list(tenantId);
    }


}
