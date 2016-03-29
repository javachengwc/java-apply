package com.manage.model.rbac;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.manage.model.BaseEntity;

/**
 * 资源实体
 */
@Entity
@Table(name = "admin_resource")
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Resource extends BaseEntity {

    private static final long serialVersionUID = 8896786223L;
    
    private Integer id;
    /**
     * 资源值 
     */
    private String value;
    /**
     * 名称
     */
    private String name;
    /**
     * 介绍
     */
    private String introduct;

    
    private Set<ResourcePermission> resourcePermissions;
    /**
     * 所属模块
     */
    private Module module;
    
    /**
     * 所属模块id
     */
    @Transient
    private Integer moduleid;
    /**
     * 相应的模块，（把模块当成资源）
     */
    private Module matchModule;
    
    public Resource()
    {
    	
    }
    public Resource(Module module)
    {
    	this.name=module.getName();
    	this.matchModule=module;
    }
    public Resource(boolean initModule)
    {
    	if(initModule)
    		module =new Module();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
        public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	    this.id = id;
    }
    
    public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "introduct")
    public String getIntroduct() {
	    return introduct;
    }

    public void setIntroduct(String introduct) {
	    this.introduct = introduct;
    }
    
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<ResourcePermission> getResourcePermissions() {
	    return resourcePermissions;
    }

    protected void setResourcePermissions(Set<ResourcePermission> resourcePermissions) {
	    this.resourcePermissions = resourcePermissions;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODULEID")
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
		this.moduleid=module.getId();
	}
	@OneToOne
	@JoinColumn(name = "matchmdid")
	public Module getMatchModule() {
		return matchModule;
	}

	public void setMatchModule(Module matchModule) {
		this.matchModule = matchModule;
	}

	
	@Transient
	public Integer getModuleid() {
		return moduleid;
	}
	
	public void setModuleid(Integer moduleid) {
		this.moduleid = moduleid;
	}
	public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(!(obj instanceof Resource))
			return false;
		Resource aa=(Resource)obj;
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
