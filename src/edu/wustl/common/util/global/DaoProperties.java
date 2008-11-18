package edu.wustl.common.util.global;

import java.sql.Connection;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.common.dao.IConnectionManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;


public class DaoProperties
{

	
	
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ApplicationProperties.class);

	static ResourceBundle bundle;
	static
	{
		try
		{
			bundle = ResourceBundle.getBundle("Dao");
			logger.debug("File Loaded !!!");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage() + " " + e);
		}
	}
	
	public DaoProperties()
	{
		// TODO Auto-generated constructor stub
	}
	
	DaoProperties (IConnectionManager icon)
	{
		System.out.println(" DaoProperties :  >>>"+icon);
		//System.out.println("-----" +icon.getGenericInterfaces());
	}


	/**
	 * @param theKey key in a application property file
	 * @return the value of key.
	 */
	public static String getValue(String theKey)
	{
		String val = TextConstants.EMPTY_STRING;
		if (bundle == null)
		{
			logger.fatal("Resource bundle is null cannot return value for key " + theKey);
		}
		else
		{
			val = bundle.getString(theKey);
		}
		return val;
	}
	
	
	
	public static void main (String[] argv)
	{
		
//		String defaultDao = daoProperties.getValue("defaultDao");
		
		try
		{
	
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		
		
	}


	public void closeConnection() throws HibernateException
	{
		// TODO Auto-generated method stub
		
	}


	public void closeSession() throws HibernateException
	{
		// TODO Auto-generated method stub
		
	}


	public Session currentSession() throws HibernateException
	{
		// TODO Auto-generated method stub
		return null;
	}


	public Session getCleanSession() throws BizLogicException
	{
		// TODO Auto-generated method stub
		return null;
	}


	public Connection getConnection() throws HibernateException
	{
		// TODO Auto-generated method stub
		return null;
	}


	public Object loadCleanObj(Class objectClass, Long identifier) throws HibernateException
	{
		// TODO Auto-generated method stub
		return null;
	}
}