package com.manageplat.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * mybatis一级缓存只能在原生mybatis操作中使用。
 * 如果集成了spring,每次查询使用的session,是spring通过下面机制生成的一个新的session实例，
 * 无法使用mybatis的一级缓存,也就是说mybatis的一级缓存在spring中是没有作用的。
 * SqlSessionDaoSupport内部sqlSession的实现是使用动态代理实现的,这个动态代理sqlSessionProxy使用一个模板方法封装了select()等操作,
 * 每一次select()查询都会自动先执行openSession(),执行完后调用close()方法。
 */
public class MyBatisPrg {

    public static void main(String[] args) throws Exception {
        String config = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(config);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = factory.openSession();
        System.out.println((Object)session.selectOne("selectUu", 1));
        //同一个session的相同sql查询,将会使用一级缓存
        System.out.println((Object)session.selectOne("selectUu", 1));
        //参数改变,需要重新查询
        System.out.println((Object)session.selectOne("selectUu", 2));
        //清空缓存后需要重新查询
        session.clearCache();
        System.out.println((Object)session.selectOne("selectUu", 1));
        //session close
        session.close();
        session = factory.openSession();
        System.out.println((Object)session.selectOne("selectUu", 1));
        session.close();
    }
}
