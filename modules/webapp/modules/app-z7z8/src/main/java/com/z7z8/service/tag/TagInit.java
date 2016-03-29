package com.z7z8.service.tag;

import com.webapp.init.Init;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TagInit  implements Init {

	private static Logger logger = LoggerFactory.getLogger(TagInit.class);
	
	public void init()
	{
		logger.error("TagInit start,");
		TagManager.getInstance().init();
		logger.error("TagInit end,");
	}
}