import com.alibaba.fastjson.JSON;
import com.configcenter.client.ConfigClient;
import com.configcenter.template.BaseTemplate;
import com.configcenter.template.KafkaTemplate;
import com.configcenter.template.MysqlTemplate;
import com.configcenter.template.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) throws Exception{

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/ApplicationContext-context.xml"});

        ConfigClient configClient =applicationContext.getBean("configClient",ConfigClient.class);

        //String value = configClient.getConfigValue("task","port");
        //System.out.println("value:"+value);

        logger.info("app is running now! Press 'quit' to quit!");
        terminalSession(configClient);
        logger.info("app is quited!");
    }

    public static void terminalSession( ConfigClient configClient )
    {

        while (true) {

            byte[] bytes = new byte[1024];
            try {

                System.in.read(bytes);
            } catch (Exception e) {
                logger.info("read the command error! please retry again!");
            }

            String instr = new String(bytes).trim();
            if ("quit".equalsIgnoreCase(instr)) {
                break;
            }

            if("printData".equalsIgnoreCase(instr))
            {
                configClient.printData();
                continue;
            }



            //获取配置项的参数，app ,key ,format
            String ps [] = instr.split(" ");
            if(ps.length<=1)
            {
                continue;
            }
            String app = ps[0].trim();
            String configKey = ps[1].trim();
            String format="data";
            if(ps.length>=3)
            {
                format = ps[2].trim();
            }
            String value=null;
            if("base".equalsIgnoreCase(format))
            {
                BaseTemplate baseValue = configClient.getBaseConfig(app,configKey);
                value = baseValue==null?"":JSON.toJSONString(baseValue);
            }else
            if("kafka".equalsIgnoreCase(format))
            {
                KafkaTemplate kafkaValue = configClient.getKafkaConfig(app,configKey);
                value = kafkaValue==null?"":JSON.toJSONString(kafkaValue);
            }else
            if("mysql".equalsIgnoreCase(format))
            {
                MysqlTemplate mysqlValue = configClient.getMysqlConfig(app,configKey);
                value = mysqlValue==null?"":JSON.toJSONString(mysqlValue);

            }else
            if("redis".equalsIgnoreCase("format"))
            {
                RedisTemplate redisTemplate = configClient.getRedisConfig(app,configKey);
                value = redisTemplate==null?"":JSON.toJSONString(redisTemplate);

            }else
            {
                value = configClient.getConfigValue(app,configKey);
            }
            System.out.println("app="+app+",configKey="+configKey+"\r\n"+"value="+value);
        }
    }
}
