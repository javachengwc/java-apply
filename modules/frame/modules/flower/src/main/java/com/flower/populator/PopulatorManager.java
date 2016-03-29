package com.flower.populator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现了IPopulator接口的注值器，本实现自己不做注值，而是调用其它的已经向它注册了的其它的注值器进行工作。
 * 本注值器可以作为总注值器来控制调用其它注值器。
 *
 */
public class PopulatorManager extends BaseSimplePopulator {

	/**
	 * 用于存储其它注值器了List
	 */
	private List<IPopulator> populators = new ArrayList<IPopulator>();

	/**
	 * 用于记录对象转换时所使用的注值器
	 */
	private Map<String, IPopulator> recorder = new HashMap<String, IPopulator>();

	/**
	 * 调用其它已注册的注值器进行注值
	 */
	protected boolean doPopulate(Object source, Object target,
			Map<String, String> propertiesMapping, String[] ignoreProperties, Object... params) {

		if (populators == null) {
			return false;
		}

		if (source instanceof List && target instanceof List) {
			boolean listResult = false;
			for (int i = 0; i < Math.min(((List) source).size(),
					((List) target).size()); i++) {

				if (populate(((List) source).get(i), ((List) target).get(i),
						propertiesMapping, ignoreProperties, params))
					listResult = true;
			}
			return listResult;
		}

		String recordKey = source.getClass().getName() + " --> "
				+ target.getClass().getName();
		if (recorder.containsKey(recordKey)) {
			return recorder.get(recordKey).populate(source, target,
					propertiesMapping, ignoreProperties, params);
		}

		/**
		 * 循环调用已注册的其它注值器，使用第一个有能力进行注值的注值器进行注值。
		 */
		for (IPopulator populator : populators) {

			try {
				synchronized (recorder) {
					if (recorder.containsKey(recordKey)) {
						return recorder.get(recordKey).populate(source, target,
								propertiesMapping, ignoreProperties, params);
					}

					if (populator.populate(source, target, propertiesMapping,
							ignoreProperties, params)) {
						recorder.put(recordKey, populator);
						return true;
					}
				}
			} catch (RuntimeException re) {
				logger.debug("Populator " + populator
						+ " failed on populating Source: " + source
						+ " to Target: " + target, re);
			}
		}
		return false;
	}

	/**
	 * 获得用于存储注值器的List
	 *
	 * @return 用于存储注值器的List
	 */
	public List<IPopulator> getPopulators() {
		return populators;
	}

	/**
	 * 设置一个用于存储注值器的List
	 *
	 * @param populators
	 *            用于存储注值器的List
	 */
	public void setPopulators(List<IPopulator> populators) {
		this.populators = populators;
	}
}
