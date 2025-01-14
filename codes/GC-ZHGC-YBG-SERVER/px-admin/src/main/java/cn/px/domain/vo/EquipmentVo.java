package cn.px.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 生产制造-设备台账对象 zcii_fabricate_equipment
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Data
public class EquipmentVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 设备状态 */
    private Long deviceStatus;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String deviceStatusName;

    /** 买入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "买入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deviceDate;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String deviceModel;

    /** 安装位置 */
    @Excel(name = "安装位置")
    private String devicePosition;

    /** 状态 */
    @Excel(name = "状态")
    private String statusName;

    /** 状态 */
    private Long status;

    /** 责任人 */
    @Excel(name = "责任人")
    private String responsiblePerson;

    /** 责任人id */
    private String responsibleId;

    /** 租户ID */
    private Long tenantId;

}
