package com.manage.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * 转换成各种对象的抽象UserType
 */
public abstract class ObjectUserType<T> implements UserType {
	private static final int[] SQL_TYPES = new int[] { Hibernate.STRING.sqlType() };

	public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
		return cached;
	}

	public Object deepCopy(final Object value) throws HibernateException {
		return value;
	}

	public Serializable disassemble(final Object value) throws HibernateException {
		return (Serializable) value;
	}

	public boolean equals(final Object x, final Object y) throws HibernateException {
		if (x == null) {
			return false;
		}
		return x.equals(y);
	}

	public int hashCode(final Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean isMutable() {
		return true;
	}

	public abstract T nullSafeGet(final ResultSet rs, final String[] names, final Object owner)
			throws HibernateException, SQLException;

	public abstract void nullSafeSet(final PreparedStatement st, final Object value, final int index)
			throws HibernateException, SQLException;

	public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
		return original;
	}

	public Class<?> returnedClass() {
		return Object.class;
	}

	public int[] sqlTypes() {
		return SQL_TYPES;
	}
}
