package com.flower.populator;

import com.flower.converter.ITypeConverter;

/**
 * 为方便注值器的实现而提供的一个通用基类
 * @author Zal
 *
 */
public abstract class BasePopulator extends BaseSimplePopulator {

	/**
	 * 用于保存一个类型转换器
	 */
	private ITypeConverter converter;

	/**
	 * 获得类型转换器
	 * @return 类型转换器
	 */
	public ITypeConverter getConverter() {
		return converter;
	}

	/**
	 * 设置类型转换器
	 * @param converter 类型转换器
	 */
	public void setConverter(ITypeConverter converter) {
		this.converter = converter;
	}

	/**
	 * 判断一个property是否在ignoreProperties中
	 * @param targetName 目标的名称
	 * @param ignoreProperties 忽略列表
	 * @return 是否需要继续处理
	 */
	protected boolean doProcess(String targetName, String[] ignoreProperties) {
		if (ignoreProperties != null) {
			for (String ignoreProperty : ignoreProperties) {
				if (ignoreProperty.equals(targetName)) {
					return false;
				}
			}
		}
		return true;
	}
}
