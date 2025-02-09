package com.cloudali.nacos.test;

import com.alibaba.nacos.api.*;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.AbstractEventListener;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * nacos的java sdk生命周期从创建时开始，到调用shutdown()方法时结束，期间对应创建的线程池、连接等均会始终保留，即使连接断开，也会不断重试重新建立连接。
 * 因此在使用时需要注意应用中创建的nacos java sdk的实例个数，避免造成线程池和连接的泄漏，
 * 在更换nacos java sdk实例时，切记调用shutdown()方法，
 * 同时在应用中应尽量复用同一个nacos java sdk实例，避免频繁的初始化实例。
 */
public class NacosTest {

    public static void main(String [] args) throws Exception {

        String serverAddr = "localhost:8848";
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.setProperty(PropertyKeyConst.NAMESPACE, "${namespaceId}");
        //如果nacos开启了权限效验，需要传账号密码
        properties.put(PropertyKeyConst.USERNAME,"${username}");
        properties.put(PropertyKeyConst.PASSWORD,"${password}");

        //初始化配置中心的Nacos
        ConfigService configService = NacosFactory.createConfigService(properties);
        //ConfigService configService = NacosFactory.createConfigService(serverAddr);
        configService.getServerStatus();

        String dataId = "{dataId}";
        String group = "{group}";
        //发布配置
        boolean isPublishOk = configService.publishConfig(dataId, group, "content");
        //直接使用publishConfig进行配置发布时，可能存在不同进程间并发的配置覆盖问题，
        //可以使用带Compare-And-Swap（CAS）的发布配置API，来保证不会此类情形。
        //首次发布，casMd5传入null。
        String configValue ="content";
        isPublishOk = configService.publishConfigCas(dataId, group, configValue, null);
        System.out.println(isPublishOk);
        //old Md5正确，变成成功
        String oldMd5 = encodeMd5Hex(configValue);
        isPublishOk = configService.publishConfigCas(dataId, group, "newContent", oldMd5);
        System.out.println(isPublishOk);
        //删除配置
        //boolean isRemoveOk = configService.removeConfig(dataId, group);
        //获取配置
        String content = configService.getConfig(dataId, group, 5000);
        System.out.println(content);
        //带监听器的获取配置,在程序首次启动获取配置时自行注册的Listener用于以后配置更新
        content = configService.getConfigAndSignListener(dataId, group, 5000, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println("recieve :" + configInfo);
            }
            @Override
            public Executor getExecutor() {
                return null;
            }
        });
        System.out.println(content);

        //监听配置
        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println("recieve:" + configInfo);
            }
            @Override
            public Executor getExecutor() {
                return null;
            }
        });

        //删除监听
        //configService.removeListener(dataId, group, aListener);

        //初始化注册中心的Nacos
        NamingService namingService = NacosFactory.createNamingService(properties);
        namingService.getServerStatus();

        //注册实例
        //同一个nacos client实例，仅能向一个服务注册一个实例；若同一个nacos client实例多次向同一个服务注册实例，后注册的实例将会覆盖先注册的实例
        namingService.registerInstance("nacos.test.service", "DEFAULT_GROUP", "127.0.0.1", 8848);

        //注销实例
        namingService.deregisterInstance("nacos.test.service", "DEFAULT_GROUP", "127.0.0.1", 8848);

        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8848);
        instance.setClusterName("DEFAULT");
        //注册实例
        namingService.registerInstance("nacos.test.service", instance);
        //注销实例
        namingService.deregisterInstance("nacos.test.service", instance);

        List<Instance> instances = new ArrayList<>(2);
        instances.add(instance);
        //批量注册实例
        //namingService.batchRegisterInstance("nacos.test.service", "DEFAULT_GROUP", instances);
        //批量注销实例
        //namingService.batchDeregisterInstance("nacos.test.service", "DEFAULT_GROUP", instances);

        namingService.registerInstance("nacos.test.service", "DEFAULT_GROUP", instance);

        //获取实例列表
        System.out.println(namingService.getAllInstances("nacos.test.service", "DEFAULT_GROUP"));

        //获取健康或不健康实例列表 true--健康 false--不健康
        System.out.println(namingService.selectInstances("nacos.test.service", "DEFAULT_GROUP", true));

        //获取一个健康实例
        System.out.println(namingService.selectOneHealthyInstance("nacos.test.service", "DEFAULT_GROUP"));

        //监听服务下的实例列表变化
        EventListener serviceListener = event -> {
            if (event instanceof NamingEvent) {
                System.out.println(((NamingEvent) event).getServiceName());
                System.out.println(((NamingEvent) event).getInstances());
            }
        };
        namingService.subscribe("nacos.test.service", "DEFAULT_GROUP", serviceListener);
        //取消监听
        namingService.unsubscribe("nacos.test.service", "DEFAULT_GROUP",serviceListener);

        //nacos支持使用自定义线程池进行异步监听回调，只需要将EventListener更换为AbstractEventListener，
        //并实现Executor getExecutor()方法来返回自定义的线程池，nacos client将在服务发生变更时使用该线程池进行异步回调。
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        EventListener serEventListener = new AbstractEventListener() {
            @Override
            public void onEvent(Event event) {
                if (event instanceof NamingEvent) {
                    System.out.println(((NamingEvent) event).getServiceName());
                    System.out.println(((NamingEvent) event).getInstances());
                }
            }

            @Override
            public Executor getExecutor() {
                return executorService;
            }
        };
        namingService.subscribe("nacos.test.service", serEventListener);

        //nacos 从2.4.0版本你开始，支持监听服务变化的差值，即和之前相比，有哪些实例被新增，移除和修改，
        //只需要将EventListener更换为AbstractNamingChangeListener，实现onChange方法即可
        //同时为了防止差值的错误和异常，NamingChangeEvent仍然可以通过getInstances方法获取最终的服务实例列表
//        EventListener eveChgListener = new AbstractNamingChangeListener() {
//            @Override
//            public void onChange(NamingChangeEvent event) {
//                if (event.isAdded()) {
//                    System.out.println(event.getAddedInstances());
//                }
//                if (event.isRemoved()) {
//                    System.out.println(event.getRemovedInstances());
//                }
//                if (event.isModified()) {
//                    System.out.println(event.getModifiedInstances());
//                }
//            }
//
//            @Override
//            public Executor getExecutor() {
//                return executorService;
//            }
//        };
//        namingService.subscribe("nacos.test.service", serviceListener);

        //带选择器的监听服务，当服务列表发生变化时，会使用自定义的选择器进行过滤，当过滤后的数据仍然有变化时，才会进行回调通知。
        //NamingSelector selector = NamingSelectorFactory.newIpSelector("127.0.*");
        //namingService.subscribe("nacos.test.service", "DEFAULT_GROUP", selector, serviceListener);
        //取消带选择器的监听服务
        //namingService.unsubscribe("nacos.test.service", "DEFAULT_GROUP", selector, serviceListener);

        //分页获取当前客户端所在命名空间的服务列表
        ListView<String> list = namingService.getServicesOfServer(1, 10,"DEFAULT_GROUP");
        //获取当前客户端订阅监听的所有服务列表
        System.out.println(namingService.getSubscribeServices());

        //让主线程不退出，因为订阅配置是守护线程，主线程退出守护线程就会退出。
        //正式代码中无需此代码
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String encodeMd5Hex(String data) throws Exception {
        //直接使用new String(encodedByte,"UTF-8")不行
        return new String(DigestUtils.md5Hex(data.getBytes("UTF-8")));
    }
}
