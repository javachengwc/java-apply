package com.flower.util;

import java.lang.reflect.Type;

public class TypeVo {
	private Type nextType;

	private Class<?> cls;

	public Type getNextType() {
		return nextType;
	}

	public void setNextType(Type nextType) {
		this.nextType = nextType;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

}
