package cn.px.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;

/**
 * 客户信息对象 zcii_basic_customer
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class Customer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    public Customer(){}
    public Customer(String custName,Long tenantId) {
        this.custName = custName;
        this.tenantId = tenantId;
    }

    /** 唯一标识 */
    private Long id;

    /** 客户编号 */
    @Excel(name = "客户编号")
    private String customer;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String custName;

    /** 公司类型 */
    @Excel(name = "公司类型")
    private String firmType;

    /** 所属行业 */
    @Excel(name = "所属行业")
    private String industry;

    /** 法人代表 */
    @Excel(name = "法人代表")
    private String represent;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String tel;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contact;

    /** 状态 */
    //@Excel(name = "状态")
    private Integer myState;

    /** 注册地址 */
    @Excel(name = "注册地址")
    private String enrollAddr;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区 */
    @Excel(name = "区")
    private String area;

    /** 公司简介 */
    @Excel(name = "简介")
    private String remark;

    /** 租户编号 */
    //@Excel(name = "租户编号")
    private Long tenantId;

    /** 是否展示 */
    //@Excel(name = "是否展示")
    private Integer display;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCustomer(String customer)
    {
        this.customer = customer;
    }

    public String getCustomer()
    {
        return customer;
    }
    public void setCustName(String custName)
    {
        this.custName = custName;
    }

    public String getCustName()
    {
        return custName;
    }
    public void setFirmType(String firmType)
    {
        this.firmType = firmType;
    }

    public String getFirmType()
    {
        return firmType;
    }
    public void setIndustry(String industry)
    {
        this.industry = industry;
    }

    public String getIndustry()
    {
        return industry;
    }
    public void setRepresent(String represent)
    {
        this.represent = represent;
    }

    public String getRepresent()
    {
        return represent;
    }
    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getTel()
    {
        return tel;
    }
    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getContact()
    {
        return contact;
    }
    public void setMyState(Integer myState)
    {
        this.myState = myState;
    }

    public Integer getMyState()
    {
        return myState;
    }
    public void setEnrollAddr(String enrollAddr)
    {
        this.enrollAddr = enrollAddr;
    }

    public String getEnrollAddr()
    {
        return enrollAddr;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getProvince()
    {
        return province;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }
    public void setArea(String area)
    {
        this.area = area;
    }

    public String getArea()
    {
        return area;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }
    public void setDisplay(Integer display)
    {
        this.display = display;
    }

    public Integer getDisplay()
    {
        return display;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("customer", getCustomer())
                .append("custName", getCustName())
                .append("firmType", getFirmType())
                .append("industry", getIndustry())
                .append("represent", getRepresent())
                .append("tel", getTel())
                .append("contact", getContact())
                .append("myState", getMyState())
                .append("enrollAddr", getEnrollAddr())
                .append("address", getAddress())
                .append("province", getProvince())
                .append("city", getCity())
                .append("area", getArea())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("display", getDisplay())
                .toString();
    }
}
