package com.manage.model.rbac;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.manage.model.BaseEntity;
/**
 * 模块实体
 */
@Entity
@Table(name = "admin_module")
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Module extends BaseEntity{
	
	private static final long serialVersionUID = -1902563351974265643L;
	//id
	private Integer id;
	//名称
	private String name;
	//模块访问url
	private String url;
	//父模块
	private Module parent;
	
	@Transient
	private Integer parentid;
	//子模块
	private Set<Module> childs;
	//排序
	private Integer sort;
	
	public Module()
	{
		
	}
	public Module(boolean initParent)
	{
		if(initParent)
			parent=new Module();
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentid")
	public Module getParent() {
		return parent;
	}
	public void setParent(Module parent) {
		this.parent = parent;
		if(this.parentid!=null)
		    this.parentid=parent.getId();
	}
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Module> getChilds() {
		return childs;
	}
	public void setChilds(Set<Module> childs) {
		this.childs = childs;
	}
	public void addChild(Module module)
	{
		if(childs==null)
			childs=new HashSet<Module>();
		childs.add(module);
	}
	public void removeChild(Module module)
	{
		if(childs!=null && childs.contains(module))
			childs.remove(module);
	}
	
	@Transient
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	@Transient
	public String getParentName()
	{
		if(parent==null)
			return null;
		else
			return parent.getName();
	}
	
	
	public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(!(obj instanceof Module))
			return false;
		Module aa=(Module)obj;
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
