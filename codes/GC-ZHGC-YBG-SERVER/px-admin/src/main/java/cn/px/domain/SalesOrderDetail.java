package cn.px.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;

/**
 * 生产制造-销售订单明细对象 zcii_fabricate_sales_order_detail
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class SalesOrderDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 销售订单id */
    @Excel(name = "销售订单id")
    private Long salesOrderId;

    /** 产品编号 */
    @Excel(name = "产品编号")
    private String materialsNumber;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String materialsName;

    /** 产品od */
    @Excel(name = "产品od")
    private Long materialsId;

    /** 单位 */
    @Excel(name = "单位")
    private Integer unitId;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 需求数量 */
    @Excel(name = "需求数量")
    private Double count;

    /** 最近入库数量 */
    private Double stockQuantity;

    /** 剩余可入数量 */
    private Double available;


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
    public void setSalesOrderId(Long salesOrderId)
    {
        this.salesOrderId = salesOrderId;
    }

    public Long getSalesOrderId()
    {
        return salesOrderId;
    }
    public void setMaterialsNumber(String materialsNumber)
    {
        this.materialsNumber = materialsNumber;
    }

    public String getMaterialsNumber()
    {
        return materialsNumber;
    }
    public void setMaterialsName(String materialsName)
    {
        this.materialsName = materialsName;
    }

    public Double getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Double stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getMaterialsName()
    {
        return materialsName;
    }
    public void setMaterialsId(Long materialsId)
    {
        this.materialsId = materialsId;
    }

    public Long getMaterialsId()
    {
        return materialsId;
    }
    public void setUnitId(Integer unitId)
    {
        this.unitId = unitId;
    }

    public Integer getUnitId()
    {
        return unitId;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setCount(Double count)
    {
        this.count = count;
    }

    public Double getCount()
    {
        return count;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
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
                .append("salesOrderId", getSalesOrderId())
                .append("materialsNumber", getMaterialsNumber())
                .append("materialsName", getMaterialsName())
                .append("materialsId", getMaterialsId())
                .append("unitId", getUnitId())
                .append("price", getPrice())
                .append("count", getCount())
                .append("available",getAvailable())
                .append("stockQuantity", getStockQuantity())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .toString();
    }
}
