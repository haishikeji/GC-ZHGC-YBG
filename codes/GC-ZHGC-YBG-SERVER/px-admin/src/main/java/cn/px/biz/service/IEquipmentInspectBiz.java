package cn.px.biz.service;

import java.util.List;
import cn.px.domain.EquipmentInspect;
/**
 * 设备管理-设备巡检记录Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IEquipmentInspectBiz
{


    /**
     * 查询设备管理-设备巡检记录列表
     *
     * @param EquipmentInspect 设备管理-设备巡检记录
     * @return 设备管理-设备巡检记录集合
     */
    public List<EquipmentInspect> searchEquipmentInspectList(EquipmentInspect EquipmentInspect);

    /**
     * 保存设备管理-设备巡检记录
     *
     * @param EquipmentInspect 设备管理-设备巡检记录
     * @return 结果
     */
    public int saveEquipmentInspect(EquipmentInspect EquipmentInspect);


}
