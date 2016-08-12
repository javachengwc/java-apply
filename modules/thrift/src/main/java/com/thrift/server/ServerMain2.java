package com.thrift.server;

import com.thrift.finagle.trans.TransDealService;
import com.thrift.service.TransServiceImpl2;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.util.Duration;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
/**
 * 第二种启动thirft服务方式
 */
public class ServerMain2 {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain2.class);

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext-*.xml");
        applicationContext.start();
        TransServiceImpl2 transServiceImpl2 = applicationContext.getBean(TransServiceImpl2.class);
        TransDealService.Service service = new TransDealService.Service(transServiceImpl2, new TBinaryProtocol.Factory());
        ServerBuilder serverBuilder = ServerBuilder.get()
                .name("transService")
                .codec(ThriftServerFramedCodec.get())
                .requestTimeout(new Duration(30000 * Duration.NanosPerMillisecond()))//请求超时时间
                .bindTo(new InetSocketAddress("0.0.0.0", 9999));
        ServerBuilder.safeBuild(service, serverBuilder);
        logger.info("ServerMain2 thrift server start ");
        terminalSession();
        logger.info("ServerMain2 server  is quited!");
    }

    /**
     * 终端对话*
     */
    public static void terminalSession() {
        byte[] bytes = new byte[1024];
        while (true) {
            try {
                Thread.sleep(1000);
                System.in.read(bytes);
            } catch (Exception e) {
                logger.info("read the command error! please retry again!");
            }

            String instr = new String(bytes).trim();
            if ("quit".equalsIgnoreCase(instr)) {
                break;
            }
        }
    }
}
