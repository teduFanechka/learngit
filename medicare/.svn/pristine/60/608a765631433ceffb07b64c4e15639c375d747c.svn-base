package yibao.yiwei.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 对象(含有java.util.Date)，集合等转换成json controller 中 toBeJson 调用
 *  后期可用jackson 替代完成 (JacksonDateSerializer)
 */
public class JsonDateValueProcessor implements JsonValueProcessor {

	private String format = "yyyy-MM-dd HH:mm:ss";

	public Object processArrayValue(Object value, JsonConfig config) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value, JsonConfig config) {
		return process(value);
	}

	private Object process(Object value) {

		if (value instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
			return sdf.format(value);
		}
		return value == null ? "" : value.toString();
	}
}