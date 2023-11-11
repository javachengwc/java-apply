package com.druid.model.pojo;

import java.util.Date;

public class EntityExt {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_ext.id
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_ext.entity_id
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    private Integer entityId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_ext.ext
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    private String ext;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_ext.crete_time
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    private Date creteTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_entity_ext.update_time
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_entity_ext.id
     *
     * @return the value of t_entity_ext.id
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_entity_ext.id
     *
     * @param id the value for t_entity_ext.id
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_entity_ext.entity_id
     *
     * @return the value of t_entity_ext.entity_id
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public Integer getEntityId() {
        return entityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_entity_ext.entity_id
     *
     * @param entityId the value for t_entity_ext.entity_id
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_entity_ext.ext
     *
     * @return the value of t_entity_ext.ext
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public String getExt() {
        return ext;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_entity_ext.ext
     *
     * @param ext the value for t_entity_ext.ext
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_entity_ext.crete_time
     *
     * @return the value of t_entity_ext.crete_time
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public Date getCreteTime() {
        return creteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_entity_ext.crete_time
     *
     * @param creteTime the value for t_entity_ext.crete_time
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public void setCreteTime(Date creteTime) {
        this.creteTime = creteTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_entity_ext.update_time
     *
     * @return the value of t_entity_ext.update_time
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_entity_ext.update_time
     *
     * @param updateTime the value for t_entity_ext.update_time
     *
     * @mbg.generated Thu Mar 16 10:11:33 CST 2023
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}