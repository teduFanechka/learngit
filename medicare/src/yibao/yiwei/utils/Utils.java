package yibao.yiwei.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.hygj.security.MD5;

public class Utils {
	
	static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 获得MD5加密字符串
	 * @param str
	 * @return
	 */
	public static String getMD5(String str){
		try{
			MD5 md5 = new MD5();
			return md5.getMD5ofStr(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public static String getNowTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		// System.out.println(dateString);
		return dateString;
	}

	/**
	 * 获取当前日期 返回时间类型yyyyMMdd
	 * 
	 * @return String
	 */
	public static String getNowDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		String strDate = simpledateformat.format(calendar.getTime());
		// System.out.println("strDate:::::::::::::::::::::::"+strDate);
		return strDate;
	}

	public static String getCurrentTimeAsNumber() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		smdate = sf.parse(sf.format(smdate));
		bdate = sf.parse(sf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 *字符串格式的日期计算
	 */
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(sf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获取(-1昨天)/(1明天)日期以此类推
	 * 
	 * @param num
	 * @return date
	 * @throws ParseException
	 */
	@SuppressWarnings("static-access")
	public static Date getDate(int num)   {
		Date date = new Date();// 取时间
		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, num);// num:-1昨天
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			String dateString = sf.format(date);
			date = sf.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取(-1昨天)/(1明天)日期以此类推
	 * 
	 * @param num
	 * @return date
	 */
	@SuppressWarnings("static-access")
	public static String getStrDate(int num) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, num);// num:-1昨天
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		String dateString = sf.format(date);
		return dateString;
		
	}

	/**
	 * 判断上传目录是否有文件,批量解析存储,操作完的文件进行移动操作
	 */
	public void zhushi() {

		String filePath = "D:/java_upload/目录/";
		File allFile = new File(filePath);
		String[] fileList = allFile.list();
		for (int i = 0; i < fileList.length; i++) {
			File readFile = new File(filePath + "\\" + fileList[i]); // 
			readFile.getName();
			// 20151117155744药品目录.txt
			@SuppressWarnings("unused")
			String path = readFile.getPath();//
			// D:\java_upload\目录\20151117155744药品目录.txt //
			// drugcatalogManager.addMedicineList(path);// 添加入数据库
			readFile.renameTo(new File("D:/java_upload/移除目录/"
					+ readFile.getName()));// 添加成功后移动到移除目录

		}

	}

	/**
	 * 判断一个字符串是否包含 string[]数组中的元素
	 * TODO 未实现
	 */
	public boolean isContains() {
		final String[] IGNORE_URI = { "/login", "/toLogin", "service/" };
		boolean flag = false;
		// String url = request.getRequestURL().toString();//完整请求地址
		String url = "http://localhost:8080/toHospital";
		for (String s : IGNORE_URI) {
			if (url.contains(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 根据Date 获取String
	 * 
	 * @param date
	 * @return String yyyy-MM-dd
	 */
	public static String getDate(Date date) {
		return sf.format(date);
	}
	/**
	 * 根据String 日期 返回Date
	 * @param date
	 * @return Date
	 * @throws ParseException 
	 * @throws ParseException
	 */
	public static Date getToDate(String date) throws ParseException {
		return sf.parse(date);
	}
	/**
	 * 
	 * @param list
	 * @param total
	 * @param response
	 * @throws IOException
	 */
	public static void toBeJson(List<?> list, int total, HttpServletResponse response) throws IOException {
		// 调用工具类 list(含有java.util.Date)转换成json
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		JSONObject json = new JSONObject();// new一个JSONObject
		json.accumulate("total", total);// total代表一共有多少数据
		json.accumulate("rows", list, jsonConfig);// 增加一个经过转换的list
		response.setContentType("application/json;charset=UTF-8");// 指定为utf-8
		response.getWriter().write(json.toString());// 转化为JSOn格式

	}
}
