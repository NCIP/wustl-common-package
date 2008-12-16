package edu.wustl.common.util.global;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;


/**
 * This class is a common service locator. Different parameter like application url,
 * properties file directory etc. are set when this class loads. are set when this class loads.
 * @author ravi_kumar
 */
public final class CommonServiceLocator
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(CommonServiceLocator.class);

	/**
	 * object of class CommonServiceLocator.
	 */
	private static CommonServiceLocator commonServLocator= new CommonServiceLocator();
	/**
	 * Application Name.
	 */
	private static String appName;

	/**
	 * Application Home directory.
	 */
	private static String appHome;
	/**
	 * Directory path of properties file.
	 */
	private static String propDirPath;
	/**
	 * Application URL.
	 */
	private static String appURL;

	/**
	 * Date separator.
	 */
	private static String dateSeparator="-";

	/**
	 * Date separator.
	 */
	private static String dateSeparatorSlash="/";

	/**
	 * Minimum year.
	 * e.g 1900
	 */
	private static String minYear;
	/**
	 * Maximum year.
	 * e.g 9999
	 */
	private static String maxYear;

	/**
	 * Date pattern.
	 * e.g.MM-dd-yyyy
	 */
	private static String datePattern;

	/**
	 * Time pattern. e.g. HH:mm:ss
	 */
	private static String timePattern;
	/**
	 * Date time pattern.
	 * e.g. yyyy-MM-dd-HH24.mm.ss.SSS
	 */
	private static String timeStampPattern;
	/**
	 *No argument constructor.
	 *Here all the properties are set
	 */
	private CommonServiceLocator()
	{
		initProps();
	}

	/**
	 * This method return object of the class CommonServiceLocator.
	 * @return object of the class CommonServiceLocator.
	 */
	public static CommonServiceLocator getInstance()
	{
		return commonServLocator;
	}

	/**
	 * This method loads properties file.
	 */
	private void initProps()
	{
		InputStream stream = CommonServiceLocator.class.getClassLoader()
		.getResourceAsStream("ApplicationResources.properties");
	    try
		{
			Properties props= new Properties();
			props.load(stream);
			setAppName(props);
			setAppHome(props);
			setPropDirPath();
			setDateSeparator(props);
			setDatePattern(props);
			setTimePattern(props);
			setTimeStampPattern(props);
			setMinYear(props);
			setMaxYear(props);
			stream.close();
		}
		catch (IOException exception)
		{
			logger.fatal("Not able to load properties file",exception);
		}
	}
	/**
	 * @return the application name.
	 */
	public String getAppName()
	{
		return appName;
	}


	/**
	 * Set the application name.
	 * @param props Object of Properties
	 */
	private void setAppName(Properties props)
	{
		appName=props.getProperty("app.name");
	}

	/**
	 * @return the application home directory where application is running.
	 */
	public String getAppHome()
	{
		return appHome;
	}


	/**
	 * Set application home directory where application is running.
	 * @param props Object of Properties
	 */
	private void setAppHome(Properties props)
	{
		appHome=props.getProperty("app.home.dir");
	}


	/**
	 * @return the path of directory of property files.
	 */
	public String getPropDirPath()
	{
		return propDirPath;
	}


	/**
	 * Set path of directory of property files.
	 */
	private void setPropDirPath()
	{
		String path = System.getProperty("app.propertiesFile");
    	File propetiesDirPath = new File(path);
    	propDirPath = propetiesDirPath.getParent();
	}


	/**
	 * @return the Application URL.
	 */
	public String getAppURL()
	{
		return appURL;
	}


	/**
	 * This method Set Application URL.
	 * @param requestURL request URL.
	 */
	public void setAppURL(String requestURL)
	{
		if (appURL == null || TextConstants.EMPTY_STRING.equals(appURL.trim()))
		{
			String tempUrl=TextConstants.EMPTY_STRING;
			try
			{
				URL url = new URL(requestURL);
				tempUrl = url.getProtocol() + "://" + url.getAuthority() + url.getPath();
				appURL = tempUrl.substring(0, tempUrl.lastIndexOf('/'));
				logger.debug("Application URL set: " + appURL);
			}
			catch (MalformedURLException urlExp)
			{
				logger.error(urlExp.getMessage(), urlExp);
			}
		}
	}


	/**
	 * @return the dateSeparator
	 */
	public String getDateSeparator()
	{
		return dateSeparator;
	}

	/**
	 * @param props Object of Properties
	 */
	public void setDateSeparator(Properties props)
	{
		CommonServiceLocator.dateSeparator = props.getProperty("date.separator");
	}

	/**
	 * @return the dateSeparatorSlash
	 */
	public String getDateSeparatorSlash()
	{
		return dateSeparatorSlash;
	}

	/**
	 * @return the minYear
	 */
	public String getMinYear()
	{
		return minYear;
	}

	/**
	 * @param props Object of Properties.
	 */
	public void setMinYear(Properties props)
	{
		CommonServiceLocator.minYear = props.getProperty("min.year");
	}

	/**
	 * @return the maxYear
	 */
	public String getMaxYear()
	{
		return maxYear;
	}

	/**
	 * @param props Object of Properties.
	 */
	public void setMaxYear(Properties props)
	{
		CommonServiceLocator.maxYear = props.getProperty("max.year");
	}

	/**
	 * @return the datePattern
	 */
	public String getDatePattern()
	{
		return datePattern;
	}

	/**
	 * @param props Object of Properties
	 */
	public void setDatePattern(Properties props)
	{
		CommonServiceLocator.datePattern = props.getProperty("date.pattern");
	}

	/**
	 * @return the timePattern
	 */
	public String getTimePattern()
	{
		return timePattern;
	}

	/**
	 * @param props Object of Properties
	 */
	public void setTimePattern(Properties props)
	{
		CommonServiceLocator.timePattern = props.getProperty("time.pattern");
	}

	/**
	 * @return the timeStampPattern
	 */
	public String getTimeStampPattern()
	{
		return timeStampPattern;
	}

	/**
	 * @param props Object of Properties
	 */
	public void setTimeStampPattern(Properties props)
	{
		CommonServiceLocator.timeStampPattern = props.getProperty("timestamp.pattern");
	}
}
