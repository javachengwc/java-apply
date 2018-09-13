package com.mybatis.pseudocode.mybatisspring.mapper;


import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatisspring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;

//mapper接口类对应的springbean类
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T>
{
    //mapper接口类
    private Class<T> mapperInterface;

    private boolean addToConfig = true;

    public MapperFactoryBean()
    {
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    //MapperFactoryBean继承了SqlSessionDaoSupport，而SqlSessionDaoSupport又继承了DaoSupport类，DaoSupport类又实现了InitializingBean接口，
    //当MapperFactoryBean初始化完毕后会调用DaoSupport的afterPropertiesSet方法，在那方法中会调用checkDaoConfig方法，也就是此方法
    protected void checkDaoConfig()
    {
        super.checkDaoConfig();
        Configuration configuration = getSqlSession().getConfiguration();
        if ((this.addToConfig) && (!configuration.hasMapper(this.mapperInterface)))
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                this.logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                //ErrorContext.instance().reset();
            }
    }

    public T getObject() throws Exception
    {
        return getSqlSession().getMapper(this.mapperInterface);
    }

    public Class<T> getObjectType()
    {
        return this.mapperInterface;
    }

    public boolean isSingleton()
    {
        return true;
    }

    public void setMapperInterface(Class<T> mapperInterface)
    {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface()
    {
        return this.mapperInterface;
    }

    public void setAddToConfig(boolean addToConfig)
    {
        this.addToConfig = addToConfig;
    }

    public boolean isAddToConfig()
    {
        return this.addToConfig;
    }
}
