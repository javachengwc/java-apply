package com.flower.converter;

/**
 * 用于产生Converter的工厂
 */
public final class ConverterFactory {

	private ConverterFactory(){}

	/**
	 * 默认的转换器
	 */
	private static final ITypeConverter converter;

	static {
		ConverterManager convMgr = new ConverterManager();
		ITypeConverter priConv = new PriTypeConverter();
		ITypeConverter dateConv = new DateTypeConverter();
//		ITypeConverter lobConv = new LobTypeConverter();

		convMgr.getConverters().add(priConv);
		convMgr.getConverters().add(dateConv);
//		webxConv.getConverters().add(lobConv);

		converter = convMgr;
	}

	/**
	 * 获得一个系统默认的Converter
	 * @return 系统默认的Converter
	 */
	public static ITypeConverter getDefaultConverter() {
		return converter;
	}
}
