package com.dubbo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class DubboServer {

    public static Logger logger = LoggerFactory.getLogger(DubboServer.class);

    public static void main(String args[]) throws Exception{

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/ApplicationContext-context.xml",
                        "classpath:spring/ApplicationContext-service.xml",
                        "classpath:spring/ApplicationContext-dubbo-service.xml"
                });
        applicationContext.start();

        //System.out.println("dubbo server is running");

        logger.info("dubbo server is running now! Press 'quit' to quit!");
        terminalSession();
        logger.info("dubbo server is quited!");
    }

    public static void terminalSession()
    {
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

