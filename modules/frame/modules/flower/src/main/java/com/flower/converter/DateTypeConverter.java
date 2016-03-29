package com.flower.converter;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 对以下一些日期类型进行转换，包括以下一些类型之间的相互转换 <br/>
 * <li>java.util.Date</li>
 * <li>java.util.Calendar</li>
 * <li>java.sql.Date</li>
 * <li>java.sql.Time</li>
 * <li>java.sql.Timestamp</li>
 * <li>java.lang.Long</li>
 * <li>java.lang.String</li>
 * <br/> String表示日期对象的默认格式为yyyy-MM-dd HH:mm:ss, 也可在日期部分和时间部分任选其一
 *
 */
public class DateTypeConverter extends BaseSimpleTypeConverter {

	/**
	 * 用于保存String表示日期的格式
	 */
	private SimpleDateFormat dateFormat;

	private static final SimpleDateFormat defaultDateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final SimpleDateFormat defaultTimeFormat = new SimpleDateFormat(
			"HH:mm:ss");

	/**
	 * 获得日期表示的格式
	 *
	 * @return 获得日期格式
	 */
	public String getDateFormat() {
		return dateFormat.toPattern();
	}

	/**
	 * 设置一个日期保存的格式
	 *
	 * @param dateFormat 日期格式
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = new SimpleDateFormat(dateFormat);
	}

	/**
	 * 实现了ITypeConverter接口的方法，对日期对象进行相互转换
	 */
	protected Object doConvertValue(Object value, Class<?> toType, Object... params) {

		/**
		 * 不能够为null
		 */
		if (value == null) {
			return null;
		}

		/**
		 * 只对符合的类型进行操作
		 */
		if (value instanceof Date || value instanceof Calendar
				|| value instanceof String || value instanceof Long
				|| toType.getSuperclass() == Calendar.class
				|| toType == Calendar.class
				|| toType.getSuperclass() == Date.class || toType == Date.class
				|| toType == String.class || toType == long.class
				|| toType == Long.class) {
			return doConvert(value, toType);
		}

		return null;
	}

	private Object doConvert(Object value, Class<?> toType) {
		Object result = null;

		if ((toType == Date.class)) {
			result = utilDateValue(value);
		} else if (toType == java.sql.Date.class) {
			result = sqlDateValue(value);
		} else if (toType == Time.class) {
			result = sqlTimeValue(value);
		} else if (toType == Timestamp.class) {
			result = sqlTimestampValue(value);
		} else if (toType == Calendar.class) {
			result = utilCalendarValue(value);
		} else if (toType == GregorianCalendar.class) {
			result = utilCalendarValue(value);
		} else if (toType == String.class) {
			result = stringValue(value);
		} else if (toType == Long.class || toType == long.class) {
			result = longValue(value);
		}

		return result;
	}

	private Date utilDateValue(Object value) {
		if (value instanceof String) {
			String dataTimeValue = (String) value;
			try {
				if (dateFormat != null)
					return dateFormat.parse(dataTimeValue);

				if (dataTimeValue.contains("-") && dataTimeValue.contains(":"))
					return defaultDateTimeFormat.parse(dataTimeValue);
				else if (dataTimeValue.contains("-"))
					return defaultDateFormat.parse(dataTimeValue);
				else if (dataTimeValue.contains(":"))
					return defaultTimeFormat.parse(dataTimeValue);

			} catch (ParseException e) {
				logger.debug("Can not convert String value: " + value
						+ " to Date value", e);
			}
		} else if (value instanceof Long) {
			return new Date(((Long) value).longValue());
		} else if (value instanceof Calendar) {
			return ((Calendar) value).getTime();
		} else if (value instanceof Date) {
			return (Date) value;
		}

		return null;
	}

	private java.sql.Date sqlDateValue(Object value) {
		if (value instanceof String) {
			String dataTimeValue = (String) value;
			try {
				if (dateFormat != null)
					return new java.sql.Date(dateFormat.parse(dataTimeValue)
							.getTime());

				if (dataTimeValue.contains("-") && dataTimeValue.contains(":"))
					return new java.sql.Date(defaultDateTimeFormat.parse(
							dataTimeValue).getTime());
				else if (dataTimeValue.contains("-"))
					return new java.sql.Date(defaultDateFormat.parse(
							dataTimeValue).getTime());
				else if (dataTimeValue.contains(":"))
					return new java.sql.Date(defaultTimeFormat.parse(
							dataTimeValue).getTime());

			} catch (ParseException e) {
				logger.debug("Can not convert String value: " + value
						+ " to Date value", e);
			}
		} else if (value instanceof Long) {
			return new java.sql.Date(((Long) value).longValue());
		} else if (value instanceof Calendar) {
			return new java.sql.Date(((Calendar) value).getTimeInMillis());
		} else if (value instanceof Date) {
			return new java.sql.Date(((Date) value).getTime());
		}

		return null;
	}

	private Time sqlTimeValue(Object value) {
		if (value instanceof String) {
			String dataTimeValue = (String) value;
			try {
				if (dateFormat != null)
					return new Time(dateFormat.parse(dataTimeValue).getTime());

				if (dataTimeValue.contains("-") && dataTimeValue.contains(":"))
					return new Time(defaultDateTimeFormat.parse(dataTimeValue)
							.getTime());
				else if (dataTimeValue.contains("-"))
					return new Time(defaultDateFormat.parse(
							dataTimeValue).getTime());
				else if (dataTimeValue.contains(":"))
					return new Time(defaultTimeFormat.parse(
							dataTimeValue).getTime());

			} catch (ParseException e) {
				logger.debug("Can not convert String value: " + value
						+ " to Date value", e);
			}
		} else if (value instanceof Long) {
			return new Time(((Long) value).longValue());
		} else if (value instanceof Calendar) {
			return new Time(((Calendar) value).getTimeInMillis());
		} else if (value instanceof Date) {
			return new Time(((Date) value).getTime());
		}

		return null;
	}

	private Timestamp sqlTimestampValue(Object value) {
		if (value instanceof String) {
			String dataTimeValue = (String) value;
			try {
				if (dateFormat != null)
					return new Timestamp(dateFormat.parse(dataTimeValue)
							.getTime());

				if (dataTimeValue.contains("-") && dataTimeValue.contains(":"))
					return new Timestamp(defaultDateTimeFormat.parse(
							dataTimeValue).getTime());
				else if (dataTimeValue.contains("-"))
					return new Timestamp(defaultDateFormat.parse(
							dataTimeValue).getTime());
				else if (dataTimeValue.contains(":"))
					return new Timestamp(defaultTimeFormat.parse(
							dataTimeValue).getTime());

			} catch (ParseException e) {
				logger.debug("Can not convert String value: " + value
						+ " to Date value", e);
			}
		} else if (value instanceof Long) {
			return new Timestamp(((Long) value).longValue());
		} else if (value instanceof Calendar) {
			return new Timestamp(((Calendar) value).getTimeInMillis());
		} else if (value instanceof Date) {
			return new Timestamp(((Date) value).getTime());
		}

		return null;
	}

	private Calendar utilCalendarValue(Object value) {
		if (value instanceof String) {

			String dataTimeValue = (String) value;
			try {
				if (dateFormat != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dateFormat.parse(dataTimeValue));
					return calendar;
				}

				if (dataTimeValue.contains("-") && dataTimeValue.contains(":")) {
					Calendar calendar = Calendar.getInstance();
					calendar
							.setTime(defaultDateTimeFormat.parse(dataTimeValue));
					return calendar;
				} else if (dataTimeValue.contains("-")) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(defaultDateFormat.parse(dataTimeValue));
					return calendar;
				} else if (dataTimeValue.contains(":")) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(defaultTimeFormat.parse(dataTimeValue));
					return calendar;
				}
			} catch (ParseException e) {
				logger.debug("Can not convert String value: " + value
						+ " to Date value", e);
			}

			try {
				Calendar calendar = Calendar.getInstance();
				if(dateFormat != null){
					calendar.setTime(dateFormat.parse((String) value));
					return calendar;
				}
			} catch (ParseException e) {
				logger.debug("Can not convert String value: " + value
						+ " to Date value", e);
			}
		} else if (value instanceof Long) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis((Long) value);
			return calendar;
		} else if (value instanceof Date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) value);
			return calendar;
		} else if (value instanceof Calendar) {
			return (Calendar) value;
		}

		return null;
	}

	private String stringValue(Object value) {
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Long) {
			if (dateFormat != null)
				return dateFormat.format(new Date(((Long) value).longValue()));
			else
				return defaultDateTimeFormat.format(new Date(((Long) value).longValue()));
		} else if (value instanceof Date) {
			if (dateFormat != null)
				return dateFormat.format((Date) value);
			else
				return defaultDateTimeFormat.format((Date) value);
		} else if (value instanceof Calendar) {
			if (dateFormat != null)
				return dateFormat.format(((Calendar) value).getTime());
			else
				return defaultDateTimeFormat.format(((Calendar) value).getTime());
		}

		return null;
	}

	private Long longValue(Object value) {
		if (value instanceof String) {
			String dataTimeValue = (String) value;
			try {
				if (dateFormat != null)
					return dateFormat.parse(dataTimeValue)
							.getTime();

				if (dataTimeValue.contains("-") && dataTimeValue.contains(":"))
					return defaultDateTimeFormat.parse(
							dataTimeValue).getTime();
				else if (dataTimeValue.contains("-"))
					return defaultDateFormat.parse(
							dataTimeValue).getTime();
				else if (dataTimeValue.contains(":"))
					return defaultTimeFormat.parse(
							dataTimeValue).getTime();

			} catch (ParseException e) {
				logger.debug("Can not convert String value: " + value
						+ " to Date value", e);
			}
		} else if (value instanceof Long) {
			return (Long) value;
		} else if (value instanceof Date) {
			return ((Date) value).getTime();
		} else if (value instanceof Calendar) {
			return ((Calendar) value).getTimeInMillis();
		}

		return null;
	}
}
