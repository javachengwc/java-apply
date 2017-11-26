package com.cache;

import com.cache.model.Achieve;
import com.cache.redis.service.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RankMain {

    private static Logger logger = LoggerFactory.getLogger(RankMain.class);

    public static void main(String args [])
    {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/ApplicationContext-context.xml",
                        "classpath:spring/ApplicationContext-redis.xml"
                });
        applicationContext.start();

        logger.info("RankMain started..............");
        RankService rankService = applicationContext.getBean(RankService.class);
        try {
            String course="chinese";
            for(int i=0;i<20;i++) {
                Achieve achieve =new  Achieve();
                achieve.setCourse(course);
                achieve.setStudent("who"+i);
                achieve.setScore(i*5);

                rankService.putAchieve(achieve);
            }
            Achieve bb =new  Achieve();
            bb.setCourse(course);
            bb.setStudent("bb");
            bb.setScore(55);
            System.out.println(rankService.addStudentScore(bb));

            rankService.queryCourseRank(course,12);

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            logger.info("RankMain exit.............");
            System.exit(0);
        }
    }
}

