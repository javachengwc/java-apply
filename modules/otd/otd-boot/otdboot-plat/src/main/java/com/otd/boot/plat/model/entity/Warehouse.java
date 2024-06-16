package com.otd.boot.plat.model.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Warehouse {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.id
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.warehouse_code
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String warehouseCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.warehouse_name
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String warehouseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.is_enable
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private Integer isEnable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.address
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String address;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.contact_person
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String contactPerson;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.contact_tel
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String contactTel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.contact_email
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String contactEmail;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.logistics_provider_no
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String logisticsProviderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.diy_capacity
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private Integer diyCapacity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.vkorgs
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String vkorgs;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.line_body_qps
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private Integer lineBodyQps;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.work_start_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String workStartTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.work_end_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String workEndTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.in_warehouse_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private BigDecimal inWarehouseTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.legal_person
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String legalPerson;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.create_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.create_by
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String createBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.update_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column warehouse.update_by
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    private String updateBy;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.id
     *
     * @return the value of warehouse.id
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.id
     *
     * @param id the value for warehouse.id
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.warehouse_code
     *
     * @return the value of warehouse.warehouse_code
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getWarehouseCode() {
        return warehouseCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.warehouse_code
     *
     * @param warehouseCode the value for warehouse.warehouse_code
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode == null ? null : warehouseCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.warehouse_name
     *
     * @return the value of warehouse.warehouse_name
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getWarehouseName() {
        return warehouseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.warehouse_name
     *
     * @param warehouseName the value for warehouse.warehouse_name
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName == null ? null : warehouseName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.is_enable
     *
     * @return the value of warehouse.is_enable
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public Integer getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.is_enable
     *
     * @param isEnable the value for warehouse.is_enable
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.address
     *
     * @return the value of warehouse.address
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.address
     *
     * @param address the value for warehouse.address
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.contact_person
     *
     * @return the value of warehouse.contact_person
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.contact_person
     *
     * @param contactPerson the value for warehouse.contact_person
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson == null ? null : contactPerson.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.contact_tel
     *
     * @return the value of warehouse.contact_tel
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getContactTel() {
        return contactTel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.contact_tel
     *
     * @param contactTel the value for warehouse.contact_tel
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setContactTel(String contactTel) {
        this.contactTel = contactTel == null ? null : contactTel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.contact_email
     *
     * @return the value of warehouse.contact_email
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.contact_email
     *
     * @param contactEmail the value for warehouse.contact_email
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.logistics_provider_no
     *
     * @return the value of warehouse.logistics_provider_no
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getLogisticsProviderNo() {
        return logisticsProviderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.logistics_provider_no
     *
     * @param logisticsProviderNo the value for warehouse.logistics_provider_no
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setLogisticsProviderNo(String logisticsProviderNo) {
        this.logisticsProviderNo = logisticsProviderNo == null ? null : logisticsProviderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.diy_capacity
     *
     * @return the value of warehouse.diy_capacity
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public Integer getDiyCapacity() {
        return diyCapacity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.diy_capacity
     *
     * @param diyCapacity the value for warehouse.diy_capacity
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setDiyCapacity(Integer diyCapacity) {
        this.diyCapacity = diyCapacity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.vkorgs
     *
     * @return the value of warehouse.vkorgs
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getVkorgs() {
        return vkorgs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.vkorgs
     *
     * @param vkorgs the value for warehouse.vkorgs
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setVkorgs(String vkorgs) {
        this.vkorgs = vkorgs == null ? null : vkorgs.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.line_body_qps
     *
     * @return the value of warehouse.line_body_qps
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public Integer getLineBodyQps() {
        return lineBodyQps;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.line_body_qps
     *
     * @param lineBodyQps the value for warehouse.line_body_qps
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setLineBodyQps(Integer lineBodyQps) {
        this.lineBodyQps = lineBodyQps;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.work_start_time
     *
     * @return the value of warehouse.work_start_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getWorkStartTime() {
        return workStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.work_start_time
     *
     * @param workStartTime the value for warehouse.work_start_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = workStartTime == null ? null : workStartTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.work_end_time
     *
     * @return the value of warehouse.work_end_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getWorkEndTime() {
        return workEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.work_end_time
     *
     * @param workEndTime the value for warehouse.work_end_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime == null ? null : workEndTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.in_warehouse_time
     *
     * @return the value of warehouse.in_warehouse_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public BigDecimal getInWarehouseTime() {
        return inWarehouseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.in_warehouse_time
     *
     * @param inWarehouseTime the value for warehouse.in_warehouse_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setInWarehouseTime(BigDecimal inWarehouseTime) {
        this.inWarehouseTime = inWarehouseTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.legal_person
     *
     * @return the value of warehouse.legal_person
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getLegalPerson() {
        return legalPerson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.legal_person
     *
     * @param legalPerson the value for warehouse.legal_person
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.create_time
     *
     * @return the value of warehouse.create_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.create_time
     *
     * @param createTime the value for warehouse.create_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.create_by
     *
     * @return the value of warehouse.create_by
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.create_by
     *
     * @param createBy the value for warehouse.create_by
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.update_time
     *
     * @return the value of warehouse.update_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.update_time
     *
     * @param updateTime the value for warehouse.update_time
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column warehouse.update_by
     *
     * @return the value of warehouse.update_by
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column warehouse.update_by
     *
     * @param updateBy the value for warehouse.update_by
     *
     * @mbggenerated Wed Feb 28 17:49:27 CST 2024
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }
}