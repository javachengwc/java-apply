package com.manage.usertype;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;

import com.manage.model.LongEnumSetTransform;

/**
 * OneBitEnum枚举集合的映射
 */
public class BitEnumSetUserType extends ObjectUserType<EnumSet<?>> implements ParameterizedType {
	private static final int[] SQL_TYPES = new int[] { Hibernate.LONG.sqlType() };

	@SuppressWarnings("unchecked")
	private Class<? extends Enum> enumType;

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		if (value == null) {
			return null;
		}
		EnumSet<?> es = (EnumSet<?>) value;
		return es.clone();
	}

	@Override
	@SuppressWarnings("unchecked")
	public EnumSet nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
			SQLException {
		long value = (Long) Hibernate.LONG.get(rs, names[0]);
		return LongEnumSetTransform.long2EnumSet(enumType, value);
	}

	@Override
	public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
			SQLException {
		Long longValue = LongEnumSetTransform.enumSet2Long((EnumSet<?>) value);
		if (longValue == null) {
			st.setNull(index, SQL_TYPES[0]);
		} else {
			Hibernate.LONG.set(st, longValue, index);
		}
	}

	@Override
	public Class<?> returnedClass() {
		return EnumSet.class;
	}

	public void setParameterValues(final Properties parameters) {
		final String enumClassName = parameters.getProperty("enumClass");
		try {
			this.enumType = Class.forName(enumClassName).asSubclass(Enum.class);
		} catch (final ClassNotFoundException cfne) {
			throw new HibernateException("Enum class not found", cfne);
		}
	}

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}
}