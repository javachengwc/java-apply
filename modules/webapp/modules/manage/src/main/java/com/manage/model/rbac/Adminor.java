package com.manage.model.rbac;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.manage.model.BaseEntity;
import com.manage.model.rbac.ResourcePermission.OperationFlag;


/**
 * 账号
 */
@Entity
@Table(name = "admin_adminor")
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Adminor extends BaseEntity{

    private static final long serialVersionUID = 88321237L;
    
    public enum AccountType {
	    General, Super;
    }
    
    private Integer id;
    /**
     * 账号名
     */
    private String name;
    /**
     * 账号密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户类型
     */
    private AccountType type = AccountType.General;
    
    @Transient
    private Integer typevalue=0; 
    /**
     * 拥有的角色
     */
    private Set<Role> roles;
    /**
     * 最后一次登录时间
     */
    private Timestamp lastLoginTime;
    /**
     * 登录时间
     */
    private Timestamp loginTime;
    
    /**
     * 创建时间
     */
    private Timestamp createTime;
    
    public Adminor()
    {
    	
    }
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

    @Column(nullable = false, length = 30)
    public String getPassword() {
	return password;
    }

    public void setPassword( String password) {
	this.password = password;
    }

    @Column(nullable = false, length = 30)
    public String getNickname() {
	return nickname;
    }

    public void setNickname(String nickname) {
	this.nickname = nickname;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ADMIN_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    @Cascade(CascadeType.SAVE_UPDATE)
    public Set<Role> getRoles() {
	return roles;
    }

    public void setRoles(Set<Role> roles) {
	this.roles = roles;
    }

	@Enumerated(EnumType.ORDINAL)
    public AccountType getType() {
	return type;
    }

    public void setType(AccountType type) {
	this.type = type;
	this.typevalue=type.ordinal();
    }
    
    @Transient
    public boolean isSuper()
    {
    	if(this.type==AccountType.Super)
    		return true;
    	return false;
    }
    
    @Transient
    @Column(name = "type")
    public Integer getTypevalue() {
		return typevalue;
	}
	public void setTypevalue(Integer typevalue) {
		this.typevalue = typevalue;
		for(AccountType t:AccountType.values())
		{
			if(t.ordinal()==typevalue.intValue())
			{
				this.type=t;
				break;
			}
		}
	}

	@Column(name = "LAST_LOGIN_TIME")
    public Timestamp getLastLoginTime() {
	return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
	this.lastLoginTime = lastLoginTime;
    }
    
    @Column(name = "LOGIN_TIME")
    public Timestamp getLoginTime() {
	return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
	this.loginTime = loginTime;
    }
    
    @Column(name = "CREATE_TIME")
    public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Transient
    public String getCreateTimeStr()
    {
    	DateFormat format=new SimpleDateFormat(DATE_TIME_FORMAT);
		if(createTime!=null)
		{
			Date date =new Date(createTime.getTime());
		    return format.format(date);
		}
		return null;
    }
	
	@Transient
    public String getLoginTimeStr()
    {
    	DateFormat format=new SimpleDateFormat(DATE_TIME_FORMAT);
    	
		if(loginTime!=null)
		{
			Date date =new Date(loginTime.getTime());
		    return format.format(date);
		}
		return null;
    }
	
    public String getLastLoginTimeStr()
    {
        DateFormat format=new SimpleDateFormat(DATE_TIME_FORMAT);
    	
		if(lastLoginTime!=null)
		{
			Date date =new Date(lastLoginTime.getTime());
		    return format.format(date);
		}
		return null;
    }
    
    public void addRole(Role role) {
		if (role == null) {
		    return;
		}
		if (roles == null) {
		    roles=new LinkedHashSet<Role>();
		}
		roles.add(role);
	}
	
    public void removeRole(Role role) {
		if (role == null) {
		    return;
		}
		roles.remove(role);
    }
    
    public boolean hasPermission(String resource, OperationFlag needOp) {
	if (AccountType.Super == type) {
	    return true;
	}
	if (getRoles() == null) {
	    return false;
	}
	for (Role role : getRoles()) {
	    boolean has = role.hasPermission(resource, needOp);
	    if (has) {
		return true;
	    }
	}
	return false;
    }

    public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		if(!(obj instanceof Adminor))
			return false;
		Adminor aa=(Adminor)obj;
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