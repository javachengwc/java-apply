package com.dubbo.client;

import com.dubbo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboClient {

    public static Logger logger = LoggerFactory.getLogger(DubboClient.class);

    public static void main(String args[]) throws Exception{

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/ApplicationContext-context.xml",
                        "classpath:spring/ApplicationContext-dubbo-client-service.xml"
                });

        applicationContext.start();

        DemoService service =applicationContext.getBean("demoService",DemoService.class);

        System.out.println(service.sayHello());


        logger.info("dubbo client is running now! Press 'quit' to quit!");
        terminalSession();
        logger.info("dubbo client is quited!");
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