package com.thrift.server;

import com.thrift.finagle.trans.TransDealService;
import com.thrift.util.SpringContextUtils;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * thirft服务端
 */
public class ServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext =new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext-*.xml");
        applicationContext.start();

        TransDealService.Iface face =SpringContextUtils.getBean("transServiceImpl",TransDealService.Iface.class);
        if(face==null)
        {
            System.out.println("-------face is null ");
        }else
        {
            face.transDeal(1);
        }

        //获取properties配置文件
        Properties serverConfig = (Properties) SpringContextUtils.getBean("thriftServerConf");

        int port = Integer.valueOf(serverConfig.getProperty("trans.server.port", "17001"));
        int idleTime = Integer.valueOf(serverConfig.getProperty("trans.server.idleTime", "30000"));
        int maxConcurrentRequests = Integer.valueOf(serverConfig.getProperty("trans.server.maxConcurrentRequests"));
        String name = serverConfig.getProperty("trans.server.name");
        System.out.println("---get prop-->port:"+port+",idelTime:"+idleTime+",name:"+name);
        LOGGER.info("serverName:{}", name);

        ServerBuilder serverBuilder = ServerBuilder.get()
                .name(name)
                .codec(ThriftServerFramedCodec.get())
                .hostConnectionMaxIdleTime(com.twitter.util.Duration.fromMilliseconds(idleTime))
                .maxConcurrentRequests(maxConcurrentRequests)
                .bindTo(new InetSocketAddress(port));

        FinagleServerHandler hander = new FinagleServerHandler(face,TransDealService.class.getName(),serverBuilder);
        hander.start();

        System.out.println("---------ransDealService started");
        LOGGER.info("Server is started at {}....", port);


        LOGGER.info("ServerMain server is running now! Press 'quit' to quit!");

        terminalSession();

        LOGGER.info("ServerMain server  is quited!");
    }

    /**终端对话**/
    public static void terminalSession()
    {
        byte[] bytes = new byte[1024];
        while (true) {
            try {
                Thread.sleep(1000);
                System.in.read(bytes);
            } catch (Exception e) {
                LOGGER.info("read the command error! please retry again!");
            }

            String instr = new String(bytes).trim();
            if ("quit".equalsIgnoreCase(instr)) {
                break;
            }
        }
    }

}