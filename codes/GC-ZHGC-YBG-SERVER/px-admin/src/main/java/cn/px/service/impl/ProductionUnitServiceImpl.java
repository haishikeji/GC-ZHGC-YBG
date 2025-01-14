package cn.px.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import cn.px.common.utils.DateUtils;
import cn.px.common.utils.StringUtils;
import cn.px.domain.UnitTreeSelect;
import cn.px.domain.enums.GeneratorEnum;
import cn.px.domain.vo.ProductionUnitVo;
import cn.px.service.IGeneratorSeqService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.mapper.ProductionUnitMapper;
import cn.px.domain.ProductionUnit;
import cn.px.service.IProductionUnitService;

import static cn.px.common.utils.SecurityUtils.getTenantId;
import static cn.px.common.utils.SecurityUtils.getUsername;

/**
 * 生产单元Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ProductionUnitServiceImpl implements IProductionUnitService {
    @Autowired
    private ProductionUnitMapper productionUnitMapper;
    @Autowired
    private IGeneratorSeqService generatorSeqService;

    /**
     * 查询生产单元
     *
     * @param id 生产单元主键
     * @return 生产单元
     */
    @Override
    public ProductionUnitVo selectProductionUnitVoById(Long id) {
        return productionUnitMapper.selectProductionUnitVoById(id);
    }

    @Override
    public ProductionUnit selectProductionUnitById(Long id) {
        return productionUnitMapper.selectProductionUnitById(id);
    }

    /**
     * 查询生产单元列表
     *
     * @param productionUnit 生产单元
     * @return 生产单元
     */
    @Override
    public List<ProductionUnit> selectProductionUnitList(ProductionUnit productionUnit) {
        productionUnit.setTenantId(getTenantId());
        return productionUnitMapper.selectProductionUnitList(productionUnit);
    }
    @Override
    public List<ProductionUnitVo> selectRoutingUnitVoList(ProductionUnit productionUnit){
        productionUnit.setTenantId(getTenantId());
        return productionUnitMapper.selectRoutingUnitVoList(productionUnit);
    }

    /**
     * 查询生产单元列表
     *
     * @param productionUnit 生产单元
     * @return 生产单元
     */
    @Override
    public List<ProductionUnitVo> selectUnitVoList(ProductionUnitVo productionUnit) {
        productionUnit.setTenantId(getTenantId());
        List<ProductionUnitVo> list = productionUnitMapper.selectUnitVoList(productionUnit);
        return list;

    }

    @Override
    public List<ProductionUnitVo> selectUnitVoListByTree(ProductionUnit productionUnit){
        productionUnit.setTenantId(getTenantId());
        List<ProductionUnitVo> list = productionUnitMapper.selectUnitVoListByTree(productionUnit);
        if(ObjectUtils.isNotEmpty(productionUnit.getParentId()) && productionUnit.getParentId()>0 && ObjectUtils.isNotEmpty(list)){
            //查询该节点下面的数据
            for(ProductionUnitVo unitVo :list){
                List<ProductionUnitVo> returnList = new ArrayList<ProductionUnitVo>();
                return selectUnitVoListByParentId(list,unitVo,returnList);
            }

        }
        return list;
    }

    /**
     * 递归列表
     * @return
     */
    public List<ProductionUnitVo> selectUnitVoListByParentId(List<ProductionUnitVo> list,ProductionUnitVo productionUnit,List<ProductionUnitVo> returnList){
        List<ProductionUnitVo> unitVoList = list.stream().filter(p-> p.getParentId() == productionUnit.getParentId()).collect(Collectors.toList());
        if(ObjectUtils.isNotEmpty(unitVoList)&& unitVoList.size()>0){
            for (ProductionUnitVo unitVo : unitVoList) {
                returnList.add(unitVo);
                productionUnit.setParentId(unitVo.getId());
                List<ProductionUnitVo> voList = productionUnitMapper.selectUnitVoList(productionUnit);
                if(ObjectUtils.isNotEmpty(voList)){
                    selectUnitVoListByParentId(voList,productionUnit,returnList);
                }
            }
        }

        if(ObjectUtils.isEmpty(returnList)){
            returnList = unitVoList;
        }

        return  returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param
     * @return 下拉树结构列表
     */
    @Override
    public List<UnitTreeSelect> buildUnitTreeSelect(List<ProductionUnitVo> productUnits) {
        List<ProductionUnitVo> productionUnitVosTrees = buildUnitTree(productUnits);
        return productionUnitVosTrees.stream().map(UnitTreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 新增生产单元
     *
     * @param productionUnit 生产单元
     * @return 结果
     */
    @Override
    public int insertProductionUnit(ProductionUnit productionUnit) {
        productionUnit.setTenantId(getTenantId());
        productionUnit.setCreateTime(DateUtils.getNowDate());
        productionUnit.setCreateBy(getUsername());
        productionUnit.setUnitCode(generatorSeqService.generatorSeqUtil(GeneratorEnum.KEY_UNIT.getValue(),getTenantId()));
        ProductionUnit sup = productionUnitMapper.selectProductionUnitById(productionUnit.getParentId());
        if (sup != null && !sup.getUnitName().isEmpty()) {
            productionUnit.setSuperiorUnit(sup.getUnitName());
        }
        return productionUnitMapper.insertProductionUnit(productionUnit);
    }

    /**
     * 修改生产单元
     *
     * @param productionUnit 生产单元
     * @return 结果
     */
    @Override
    public int updateProductionUnit(ProductionUnit productionUnit) {
        productionUnit.setUpdateTime(DateUtils.getNowDate());
        productionUnit.setUpdateBy(getUsername());
        productionUnit.setTenantId(getTenantId());
        if(null != productionUnit.getParentId() && productionUnit.getParentId() != 0){
            ProductionUnit supName = productionUnitMapper.selectProductionUnitById(productionUnit.getParentId());
            if (!supName.getUnitName().isEmpty())
                productionUnit.setSuperiorUnit(supName.getUnitName());
        }else if(productionUnit.getParentId() == 0){
            productionUnit.setSuperiorUnit("");
        }

        return productionUnitMapper.updateProductionUnit(productionUnit);
    }

    /**
     * 批量删除生产单元
     *
     * @param ids 需要删除的生产单元主键
     * @return 结果
     */
    @Override
    public int deleteProductionUnitByIds(Long[] ids) {
        return productionUnitMapper.deleteProductionUnitByIds(ids);
    }

    /**
     * 删除生产单元信息
     *
     * @param id 生产单元主键
     * @return 结果
     */
    @Override
    public int deleteProductionUnitById(Long id) {
        return productionUnitMapper.deleteProductionUnitById(id);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param  productUnits 生产单元
     * @return 树结构列表
     */
    @Override
    public List<ProductionUnitVo> buildUnitTree(List<ProductionUnitVo> productUnits) {
        List<ProductionUnitVo> returnList = new ArrayList<ProductionUnitVo>();
        List<Long> tempList = new ArrayList<Long>();

        for (ProductionUnitVo productUnit : productUnits) {
            tempList.add(productUnit.getId());
        }
        for (ProductionUnitVo productionUnit : productUnits) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(productionUnit.getParentId())) {
                recursionFn(productUnits, productionUnit);
                returnList.add(productionUnit);
            }
        }

        if(returnList.isEmpty()){
            returnList = productUnits;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<ProductionUnitVo> list, ProductionUnitVo t) {
        // 得到子节点列表
        List<ProductionUnitVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (ProductionUnitVo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<ProductionUnitVo> getChildList(List<ProductionUnitVo> list, ProductionUnitVo t) {
        List<ProductionUnitVo> tlist = new ArrayList<ProductionUnitVo>();
        Iterator<ProductionUnitVo> it = list.iterator();
        while (it.hasNext()) {
            ProductionUnitVo n = (ProductionUnitVo) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<ProductionUnitVo> list, ProductionUnitVo t) {
        return getChildList(list, t).size() > 0;
    }

    public List<ProductionUnitVo> selectProductUnitParent(Long id) {
        return productionUnitMapper.selectProductUnitParent(id);
    }
}
