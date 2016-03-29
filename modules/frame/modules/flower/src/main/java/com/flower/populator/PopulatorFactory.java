package com.flower.populator;

import java.util.Map;

import com.flower.converter.ConverterFactory;

/**
 * 用于产生Populator的工厂
 */
public final class PopulatorFactory {

	private PopulatorFactory() {
	}

	/**
	 * 默认的Populator
	 */
	private static final IPopulator populator;

	static {
		PopulatorManager webxPop = new PopulatorManager();

		BasePopulator requestPop = new ServletRequestBeanPopulator();
		requestPop.setConverter(ConverterFactory.getDefaultConverter());

		BasePopulator resultPop = new ResultSetBeanPopulator();
		resultPop.setConverter(ConverterFactory.getDefaultConverter());

		BasePopulator mapPop = new BeanMapPopulator();
		mapPop.setConverter(ConverterFactory.getDefaultConverter());

		BasePopulator beanPop = new BeanPopulator();
		beanPop.setConverter(ConverterFactory.getDefaultConverter());

		webxPop.getPopulators().add(beanPop);
		webxPop.getPopulators().add(mapPop);
		webxPop.getPopulators().add(resultPop);
		webxPop.getPopulators().add(requestPop);

		populator = webxPop;
	}

	/**
	 * 获得一个系统默认的Populator
	 * 
	 * @return 系统默认的Populator
	 */
	public static IPopulator getDefaultPopulator() {
		return populator;
	}

	public static void populate(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties,
			Object... params) {
		PopulatorFactory.getDefaultPopulator().populate(source, target,
				propertiesMapping, ignoreProperties, params);
	}

	public static void populate(Object source, Object target) {
		populate(source, target, null, null);
	}

}
