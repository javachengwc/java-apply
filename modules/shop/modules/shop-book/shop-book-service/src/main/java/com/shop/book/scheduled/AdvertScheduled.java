package com.shop.book.scheduled;

import com.shop.book.service.manager.AdvertManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AdvertScheduled {

    private static Logger logger = LoggerFactory.getLogger(AdvertScheduled.class);

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void reloadAdvert() {
        logger.info("AdvertScheduled reloadAdvert start,now={}",System.currentTimeMillis());
        try {
            AdvertManager.getInstance().init();
            logger.info("AdvertScheduled reloadAdvert end,now={}",System.currentTimeMillis());
        } catch (Throwable e) {
            logger.info("AdvertScheduled reloadAdvert error,",e);
        }
    }
}
