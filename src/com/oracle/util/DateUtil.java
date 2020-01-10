package com.oracle.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author Administrator
 *
 */
public class DateUtil {

	public static final String DATE1 = "yyyy年MM月dd日 HH:mm:ss";

	public static String getDate(String patten) {

		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		return sdf.format(new Date());

	}

}
