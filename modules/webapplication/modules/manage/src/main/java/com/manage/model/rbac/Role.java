package com.manage.model.rbac;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.manage.model.BaseEntity;
import com.manage.model.rbac.ResourcePermission.OperationFlag;



/**
 * 角色
 */
@Entity
@Table(name = "admin_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = -3522903399801L;
    
    private Integer id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 介绍
     */
    private String introduct;
    /**
     * 可访问资源
     */
    private Set<ResourcePermission> resourcePermissions;
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
	    return id;
    }

    public void setId(Integer id) {
	    this.id = id;
    }

    @Column(nullable = false, length = 30, unique = true)
    public String getName() {
	    return name;
    }

    public void setName(String name) {
	    this.name = name;
    }

    public String getIntroduct() {
	    return introduct;
    }

    public void setIntroduct(String introduct) {
	    this.introduct = introduct;
    }
    
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    public Set<ResourcePermission> getResourcePermissions() {
	    return resourcePermissions;
    }

    protected void setResourcePermissions(final Set<ResourcePermission> resourcePermissions) {
	    this.resourcePermissions = resourcePermissions;
    }
    
    public boolean hasPermission(String resource, OperationFlag needOp) {
	if (resourcePermissions == null || resourcePermissions.size() == 0) {
	    return false;
	}
	if (resource == null || "".equals(resource)) {
	    return false;
	}
	int index = resource.indexOf("$");
	if (index > 0) {
	    resource = resource.substring(0, index);
	}
	for (final ResourcePermission permission : resourcePermissions) {
	    if (resource.equals(permission.getResource().getValue())) {
		if (permission.getOperations().contains(needOp)) {
		    return true;
		}
	    }
	}
	return false;
    }

   /**
	 * 检查此帐号对指定资源有没有指定的执行权限
	 */
    public boolean hasPermission(Class<?> resource, OperationFlag needOp) {
	if (resource == null) {
	    return false;
	}
	return hasPermission(resource.getName(), needOp);
    }

    /**
	 * 检查此帐号对指定资源集合有没有指定的执行权限
	 * 
	 * @param resources
	 * @param needOp
	 * @return
	 */
    public boolean hasPermission(final Collection<?> resources, final OperationFlag needOp) {
	for (final Object object : resources) {
	    if (!hasPermission(object.getClass(), needOp)) {
		return false;
	    }
	}
	return true;
    }

    	/**
	 * 删除权限
	 * 
	 * @param resource
	 */
    public void removePermission(Resource resource) {
	ResourcePermission permission = getPermission(resource);
	removePermission(permission);
    }

    	/**
	 * 刪除权限
	 * 
	 * @param permission
	 */
    public void removePermission(ResourcePermission permission) {
	if (permission != null) {
	    permission.setOperations(EnumSet.noneOf(OperationFlag.class));
	    permission.setRole(null);
	    getResourcePermissions().remove(permission);
	}
    }

    /**
	 * 添加权限
	 * 
	 * @param resource
	 * @param operations
	 */
    public void addPermission(Resource resource, EnumSet<OperationFlag> operations) {
	ResourcePermission permission = new ResourcePermission();
	permission.setOperations(operations);
	permission.setRole(this);
	permission.setResource(resource);
	if (getResourcePermissions() == null) {
	    setResourcePermissions(new HashSet<ResourcePermission>());
	}
	getResourcePermissions().add(permission);
    }

    /**
	 * 修改权限，如果权限已经存在，修改之。不存在，创建之。存在但操作列表为空，删除之
	 * 
	 * @param resource
	 * @param operations
	 */
    public void changePermission(Resource resource, EnumSet<OperationFlag> operations) {
	ResourcePermission permission = getPermission(resource);
	if (permission == null && operations.size() == 0) {
	    return;
	}
	if (permission == null && operations.size() != 0) {
	    addPermission(resource, operations);
	} else if (permission != null && operations.size() == 0) {
	    removePermission(permission);
	} else {
	    permission.setOperations(operations);
	}
    }

    /**
	 * 获得指定资源的权限
	 * 
	 * @param resource
	 * @return
	 */
    public ResourcePermission getPermission(Resource resource) {
	if (getResourcePermissions() == null) {
	    return null;
	}
	for (Iterator<ResourcePermission> it = resourcePermissions.iterator(); it.hasNext();) {
	    ResourcePermission permission = it.next();
	    if (permission.getResource().equals(resource)) {
		return permission;
	    }
	}
	return null;
    }
    
    public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(!(obj instanceof Role))
			return false;
		Role aa=(Role)obj;
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
