package com.manage.model.rbac;

import java.util.EnumSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.manage.model.BaseEntity;
import com.manage.model.IOneBitEnum;
import com.manage.model.LongEnumSetTransform;

/**
 * 管理员权限的实体
 */
@Entity
@Table(name = "admin_resource_permission")
public class ResourcePermission extends BaseEntity {

    private static final long serialVersionUID = 4548796849418736769L;

    public enum OperationFlag implements IOneBitEnum {
    	 LIST("列出内容", 0),UPDATE("修改", 1), CREATE("创建", 2), DELETE("删除", 3);
	private final String name;
	private final int position;
	private final long value;

	private OperationFlag(final String name, final int position) {
	    this.name = name;
	    this.position = position;
	    value = (long) Math.pow(2, position);
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
	    return position;
	}

	public long getValue() {
	    return value;
	}
    }
    private Integer id;
    
    private Role role;
    
    @Transient
    private Integer roleid;
    
    private Resource resource;
    
    @Transient
    private Integer resourceid;
    
    //创建空的 OperationFlag类型集合
    private EnumSet<OperationFlag> operations = EnumSet.noneOf(OperationFlag.class);

    @Transient
    private Long flag;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
	    return id;
    }

    public void setId(final Integer id) {
	this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    public Role getRole() {
	    return role;
    }

    public void setRole(Role role) {
	    this.role = role;
	    if(role!=null)
	    	this.roleid=role.getId();
    }
    @Transient
    public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	@Transient
	public Integer getResourceid() {
		return resourceid;
	}

	public void setResourceid(Integer resourceid) {
		this.resourceid = resourceid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resourceId")
    @Fetch(FetchMode.SELECT)
    public Resource getResource() {
	    return resource;
    }

    public void setResource(final Resource resource) {
	    this.resource = resource;
	    if(resource!=null)
	    	this.resourceid=resource.getId();
    }

    @Type(parameters = {@Parameter(name = "enumClass", value = "com.manage.model.rbac.ResourcePermission$OperationFlag")}, type = "com.manage.usertype.BitEnumSetUserType")
    @Column(name = "flag",nullable = false)
    public EnumSet<OperationFlag> getOperations() {
	    return operations;
    }

    public void setOperations(final EnumSet<OperationFlag> operation) {
	    this.operations = operation;
	    this.flag=LongEnumSetTransform.enumSet2Long(this.operations);
    }
    
    @Transient
    public Long getFlag() {
		return flag;
	}

	public void setFlag(Long flag) {
		this.flag = flag;
		this.operations=LongEnumSetTransform.long2EnumSet(OperationFlag.class, flag);
	}

	public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(!(obj instanceof ResourcePermission))
			return false;
		ResourcePermission aa=(ResourcePermission)obj;
		if(aa.getId()==null || this.getId()==null)
			return false;
		else
			return aa.getId().equals(this.getId());
	}
	
	public int hashCode () 
	{
		if (null == this.getId()) return super.hashCode();
		else {
			String hashStr = this.getClass().getName() + ":" + this.getId();
			return hashStr.hashCode();
		}
	}
}
