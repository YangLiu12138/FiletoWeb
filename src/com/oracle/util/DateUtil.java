package com.oracle.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ʱ�乤����
 * 
 * @author Administrator
 *
 */
public class DateUtil {

	public static final String DATE1 = "yyyy��MM��dd�� HH:mm:ss";

	public static String getDate(String patten) {

		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		return sdf.format(new Date());

	}

}
