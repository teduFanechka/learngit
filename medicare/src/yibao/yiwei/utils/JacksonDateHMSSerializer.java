package yibao.yiwei.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * java日期对象经过Jackson库转换成JSON日期格式化自定义类
 * 
 * @author Administrator
 */
public class JacksonDateHMSSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator jg, SerializerProvider arg2) throws IOException, JsonProcessingException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fmDate = sf.format(date);
		jg.writeString(fmDate);
	}

}
