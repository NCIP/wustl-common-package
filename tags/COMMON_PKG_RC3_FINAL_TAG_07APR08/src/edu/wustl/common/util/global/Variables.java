/*
 * Created on Nov 30, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.wustl.common.util.global;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ajay_sharma
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * This class is specific to common files. It is used by common package.
 * */

public class Variables 
{
	public static String applicationName = new String();
	public static String applicationVersion = new String();
	
	/* To hold path of applicatio installation directory */
	public static String applicationHome = new String();
	
	public static String databaseName=new String();
	public static String propertiesDirPath = new String();
	
	public static String datePattern = new String();
	public static String timePattern = new String();
	public static String timeFormatFunction = new String();
	public static String dateFormatFunction = new String();
	public static String strTodateFunction = new String();
	public static String dateTostrFunction = new String();
	public static String catissueURL=new String();
	
	 public static Map<String,String> entityCPSqlMap = new HashMap<String, String>();
}