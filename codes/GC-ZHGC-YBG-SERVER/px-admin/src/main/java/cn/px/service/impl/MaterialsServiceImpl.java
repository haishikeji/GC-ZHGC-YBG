package cn.px.service.impl;

import java.util.*;

import cn.px.common.core.domain.AjaxResult;
import cn.px.common.exception.ServiceException;
import cn.px.common.utils.DateUtils;
import cn.px.common.utils.StringUtils;
import cn.px.common.utils.bean.BeanValidators;
import cn.px.domain.*;
import cn.px.service.*;
import cn.px.domain.*;
import cn.px.domain.enums.BaseDictDataEnum;
import cn.px.domain.enums.MaterialsTypeEnum;
import cn.px.domain.request.ProductListReqVo;
import cn.px.domain.vo.MaterialsExcel;
import cn.px.domain.vo.MaterialsVo;
import cn.px.mapper.InventoryMapper;
import cn.px.service.*;
import cn.px.system.service.ISysDictDataService;
import cn.px.utils.TeaBeiBeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.mapper.MaterialsMapper;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;

import static cn.px.common.utils.SecurityUtils.getTenantId;
import static cn.px.common.utils.SecurityUtils.getUsername;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.lang3.SystemUtils.getUserName;

/**
 * 物料信息Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class MaterialsServiceImpl implements IMaterialsService {

    private static final Logger log = LoggerFactory.getLogger(MaterialsServiceImpl.class);
    @Autowired
    private MaterialsMapper materialsMapper;

    @Autowired
    private IRoutingService routingService;

    @Autowired
    private IEnterService iEnterService;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private IMaterialsDetailService materialsDetailService;

    @Autowired
    private IGeneratorSeqService generatorSeqService;

    @Autowired
    protected Validator validator;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private IMaterialsTypeService materialsTypeService;

    /**
     * 查询物料信息
     *
     * @param id 物料信息主键
     * @return 物料信息
     */
    @Override
    public Materials selectMaterialsById(Long id) {
        return materialsMapper.selectMaterialsById(id);
    }

    /**
     * 查询物料信息列表
     *
     * @param materials 物料信息
     * @return 物料信息
     */
    @Override
    public List<Materials> selectMaterialsList(Materials materials) {
        return materialsMapper.selectMaterialsList(materials);
    }

    @Override
    public List<MaterialsVo> selectCargoPositionMaterialsList(Long positionId, String materialsName) {
        List<MaterialsVo> materialsVoList = new ArrayList<>();
        Enter enter = new Enter();
        enter.setPositionId(positionId);
        List<Enter> enterList = iEnterService.selectEnterList(enter);
        //根据物料编码去重
        enterList.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(Enter::getMaterialsNumber))), ArrayList::new));
        enterList.stream().forEach(i -> {
            Materials materials = new Materials();
            materials.setNumber(i.getMaterialsNumber());
            materials.setNumber(materialsName);
            List<Materials> materialsList = materialsMapper.selectMaterialsList(materials);
            if (!CollectionUtils.isEmpty(materialsList)) {
                MaterialsVo materialsVo = new MaterialsVo();
                BeanUtils.copyProperties(materialsList.get(0), materialsVo);

                Inventory inventory = new Inventory();
                inventory.setMaterialId(materialsVo.getId());
                List<Inventory> materialQuantityList = inventoryMapper.selectInventoryList(inventory);
                if (!CollectionUtils.isEmpty(materialQuantityList)) {
                    materialsVo.setQuantity(materialQuantityList.get(0).getQuantity());
                }
                materialsVoList.add(materialsVo);
            }
        });
        return materialsVoList;
    }

    /**
     * 新增物料信息
     *
     * @param materials 物料信息
     * @return 结果
     */
    @Override
    public int insertMaterials(Materials materials) {
        materials.setCreateTime(DateUtils.getNowDate());
        materials.setCreateBy(getUsername());
        materials.setStatus(BaseDictDataEnum.KEY_START.getKey());//默认启用
        materials.setTenantId(getTenantId());
        materials.setModel(materials.getlModel() + "*" + materials.getwModel() + "*" + materials.gethModel() + "*" + materials.gettModel());
        return materialsMapper.insertMaterials(materials);
    }
    @Override
    public AjaxResult saveMaterials(MaterialsVo materialsVo){
        int result = 0;

        try{

            List<Materials> matters = materialsMapper.selectMaterialsbyName(new Materials(materialsVo.getName(),materialsVo.getNumber(),getTenantId(),materialsVo.getCategoryId()));

            if (StringUtils.isNull(materialsVo.getNumber())) {
              //  throw new ServiceException("物料编码不能为空");
                return  AjaxResult.error("物料编码不能为空");
            }

            if(matters.size()>0){
             //   throw new ServiceException("该物料信息已存在");
                return  AjaxResult.error("该物料信息已存在");
            }

            Materials materials = TeaBeiBeanUtils.propertiesCopy(materialsVo,Materials.class); //setMaterialsNumber(materialsVo);

            result =  insertMaterials(materials);

            if (result > 0) {
                List<MaterialsDetail> list = materialsVo.getList();
                if (list.size() > 0) {
                    for (MaterialsDetail detail : list) {
                        detail.setMatterId(materials.getId());
                        result = materialsDetailService.insertMaterialsDetail(detail);
                    }
                }
            }
        }catch (Exception ex){
            log.info(ex.getMessage());
        }

        return AjaxResult.success(result);
    }

    private Materials setMaterialsNumber(MaterialsVo materialsVo){
        Materials materials = TeaBeiBeanUtils.propertiesCopy(materialsVo, Materials.class);

        String number = "";

        switch (materialsVo.getCategoryId().toString()){
            case "1":
                number = number+MaterialsTypeEnum.KEY_AM.getValue();
                break;
            case "2":
                number = number+MaterialsTypeEnum.KEY_SP.getValue();
                break;
            case "3":
                number = number+MaterialsTypeEnum.KEY_FG.getValue();
                break;
            case "4":
                number = number+MaterialsTypeEnum.KEY_RM.getValue();
                break;
            case "5":
                number = number+MaterialsTypeEnum.KEY_AS.getValue();
                break;
            case "6":
                number = number+number+MaterialsTypeEnum.KEY_EG.getValue();
                break;
        }

        number = number+materialsVo.getNumberMax()+materialsVo.getNumberMin();

        int count = materialsMapper.selectMaterialsList(new Materials(Integer.valueOf(materialsVo.getCategoryId().toString()),getTenantId(),number)).size();

        number = generatorSeqService.generatorSeqByCount(count,number);

        materials.setNumber(number);

        return materials;
    }

    /**
     * 修改物料信息
     *
     * @param materials 物料信息
     * @return 结果
     */
    @Override
    public int updateMaterials(Materials materials) {
        materials.setUpdateTime(DateUtils.getNowDate());
        materials.setModel(materials.getlModel() + "*" + materials.getwModel() + "*" + materials.gethModel() + "*" + materials.gettModel());
        materials.setUpdateBy(getUsername());
        materials.setTenantId(getTenantId());
        return materialsMapper.updateMaterials(materials);
    }

    @Override
    public int updateMaterials(MaterialsVo materialsVo,Materials materials) {

//        String numberMax = materials.getNumber().substring(2,4);
//        String numberMin = materials.getNumber().substring(4,6);
//
//        if(!materialsVo.getNumberMin().equals(numberMin) || !materialsVo.getNumberMax().equals(numberMax)){
//            materials = setMaterialsNumber(materialsVo);
//        }else{
//            materials = TeaBeiBeanUtils.propertiesCopy(materialsVo,Materials.class);
//        }
        if(materialsVo.getName().equals(materials.getName()) && materialsVo.getNumber().equals(materials.getNumber())){
                //如果传进来的物料名称+物料编码 等于根据物料id查询到数据，说明这两个字段没有任何修改
        }else {
            Materials materia = new Materials();
            materia.setNumber(materialsVo.getNumber());
            materia.setName(materialsVo.getName());
            materia.setTenantId(getTenantId());
            if(ObjectUtils.isNotEmpty(materialsMapper.selectMaterialsbyName(materia))){
                //如果不为空，说明已经有这个物料名称及编码
                throw new ServiceException("此物料名称+物料编码已经存在！");
            }
            // 物料名称或者物料编码 不相同 或者不存在 则查询对应工艺路线是否存在
            Routing routing = new Routing();
            routing.setMatterId(materials.getId());
            List<Routing> routings = routingService.selectRoutingbyName(routing);
            if(ObjectUtils.isNotEmpty(routings)){
                // 说明配了工艺路线，需要同步工艺路线名称
                Routing updRouting = routings.get(0);
                updRouting.setNumber(materialsVo.getNumber());
                updRouting.setName(materialsVo.getName());
                routingService.updateRouting(updRouting);
            }
        }
        materials = TeaBeiBeanUtils.propertiesCopy(materialsVo,Materials.class);
        materials.setUnitId(Integer.valueOf(materialsVo.getUnitId()));

        return updateMaterials(materials);
    }
    /**
     * 批量删除物料信息
     *
     * @param ids 需要删除的物料信息主键
     * @return 结果
     */
    @Override
    public int deleteMaterialsByIds(Long[] ids) {
        return materialsMapper.deleteMaterialsByIds(ids);
    }

    /**
     * 删除物料信息信息
     *
     * @param id 物料信息主键
     * @return 结果
     */
    @Override
    public int deleteMaterialsById(Long id) {
        return materialsMapper.deleteMaterialsById(id);
    }

    @Override
    public  List<Materials> selectMaterialsbyName(Materials matter){
        matter.setTenantId(getTenantId());
        return  materialsMapper.selectMaterialsbyName(matter);
    }

    @Override
    public  List<MaterialsVo> selectMaterialsbyBom(Materials matter){
        matter.setTenantId(getTenantId());
        return  materialsMapper.selectMaterialsbyBom(matter);
    }


    @Override
    public List<MaterialsVo> selectMaterialsVoList(MaterialsVo materialsVo){
        materialsVo.setTenantId(getTenantId());
        return materialsMapper.selectMaterialsVoList(materialsVo);
    }

    @Override
    public List<MaterialsVo> selectMaterialsVoListByCategoryIds(ProductListReqVo productListReqVo){
        return materialsMapper.selectMaterialsVoListByCategoryIds(productListReqVo);
    }
    @Override
    public String importMaterials(List<MaterialsExcel> materialsList, Boolean isUpdateSupport, String operName){
        if (StringUtils.isNull(materialsList) || materialsList.size() == 0) {
            throw new ServiceException("导入客户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (MaterialsExcel materialVo : materialsList) {
            try {
                // 验证是否存在这个物料信息
                List<Materials> matters = materialsMapper.selectMaterialsbyName(new Materials(materialVo.getName(),materialVo.getNumber(),getTenantId()));

                if (ObjectUtils.isEmpty(materialVo.getCategoryId())){
                    throw new ServiceException("物料分类不能为空");
                }
                if (ObjectUtils.isEmpty(materialVo.getName())){
                    throw new ServiceException("物料名称不能为空");
                }
                if (ObjectUtils.isEmpty(materialVo.getNumber())){
                    throw new ServiceException("物料编码不能为空");
                }

                if (ObjectUtils.isEmpty(materialVo.getUnitPrice())){
                    throw new ServiceException("物料价格不能为空");
                }

                if (ObjectUtils.isEmpty(materialVo.getUnitId())){
                    throw new ServiceException("物料单位不能为空");
                }

                Materials matter = TeaBeiBeanUtils.propertiesCopy(materialVo,Materials.class);

                BeanValidators.validateWithException(validator, materialVo);

                String categoryString = materialVo.getCategoryId();  //输入的是中文，转成字典value

                List<MaterialsType> materialsTypes = materialsTypeService.selectMaterialsTypeList(new MaterialsType(categoryString,getTenantId()));

                String unitString = materialVo.getUnitId();  //输入的是中文，转成字典value

                String unitId = sysDictDataService.selectDictValue("zcii_bas_unit_type",unitString);

                Integer categoryId = null ;

                if(ObjectUtils.isNotEmpty(materialsTypes)){
                    categoryId = materialsTypes.get(0).getMaterialsTypeValue();
                }

                if (matters.size()>0){
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、物料名称 " + materialVo.getName()+ "物料编码 "+materialVo.getNumber()+" 已存在");
                }else if(ObjectUtils.isEmpty(materialsTypes) || ObjectUtils.isEmpty(categoryId) ){
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、物料分类名称 " +categoryString + "不存在");
                }else if(ObjectUtils.isEmpty(unitId)) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、物料单位 " + unitString + "不存在");
                }else if(!isUpdateSupport) {
                    if(ObjectUtils.isNotEmpty(categoryId) && ObjectUtils.isNotEmpty(unitId)){
                        matter.setUnitId(Integer.valueOf(unitId));
                        matter.setCategoryId(Integer.valueOf(categoryId));
                        matter.setCreateBy(operName);
                        matter.setTenantId(getTenantId());
                        insertMaterials(matter);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、物料名称 " + matter.getName() + " 导入成功");
                    }
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, materialVo);
                    Materials mater =  TeaBeiBeanUtils.propertiesCopy(materialVo,Materials.class);
                    mater.setId(matters.get(0).getId());
                    mater.setUpdateBy(operName);
                    this.updateMaterials(mater);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、物料名称 " + materialVo.getName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、物料名称 " + materialVo.getName()+ "物料编码 "+materialVo.getNumber()+" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、物料名称 " + materialVo.getName() +"物料编码 "+materialVo.getNumber()+ " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}
