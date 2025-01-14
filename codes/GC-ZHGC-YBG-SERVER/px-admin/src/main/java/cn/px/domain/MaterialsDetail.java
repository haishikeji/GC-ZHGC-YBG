package cn.px.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;

/**
 * 基础数据-物料包装明细对象 zcii_basic_materials_detail
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class MaterialsDetail extends BaseEntity
{

    public MaterialsDetail() {
    }
    public MaterialsDetail(Long packId,Long tenantId) {
        this.packId = packId;
        this.tenantId=tenantId;
    }

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 主表Id */
    @Excel(name = "主表Id")
    private Long matterId;

    /** 包装Id */
    @Excel(name = "包装Id")
    private Long packId;

    /** 包装名称 */
    @Excel(name = "包装名称")
    private String name;

    /** 包装编码 */
    @Excel(name = "包装编码")
    private String number;

    /** 包装类型 */
    @Excel(name = "包装类型")
    private Integer packageTypeId;

    /** 装载数量 */
    @Excel(name = "装载数量")
    private Double packageNumber;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 租户ID */
    @Excel(name = "租户ID")
    private Long tenantId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMatterId(Long matterId)
    {
        this.matterId = matterId;
    }

    public Long getMatterId()
    {
        return matterId;
    }
    public void setPackId(Long packId)
    {
        this.packId = packId;
    }

    public Long getPackId()
    {
        return packId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getNumber()
    {
        return number;
    }
    public void setPackageTypeId(Integer packageTypeId)
    {
        this.packageTypeId = packageTypeId;
    }

    public Integer getPackageTypeId()
    {
        return packageTypeId;
    }
    public void setPackageNumber(Double packageNumber)
    {
        this.packageNumber = packageNumber;
    }

    public Double getPackageNumber()
    {
        return packageNumber;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("matterId", getMatterId())
                .append("packId", getPackId())
                .append("name", getName())
                .append("number", getNumber())
                .append("packageTypeId", getPackageTypeId())
                .append("packageNumber", getPackageNumber())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .toString();
    }
}
