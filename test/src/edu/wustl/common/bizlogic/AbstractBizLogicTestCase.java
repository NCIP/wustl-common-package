package edu.wustl.common.bizlogic;

import java.util.ArrayList;
import java.util.Collection;

import edu.wustl.common.CommonBaseTestCase;
import edu.wustl.common.actionForm.MyActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.MyDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.MyDAOImpl;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.DAOConstants;

/**
 * This class for juint test cases for methods in AbstractBizLogic.
 * @author ravi_kumar
 *
 */
public class AbstractBizLogicTestCase extends CommonBaseTestCase
{
	/**
	 * Generic Logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(AbstractBizLogicTestCase.class);

	/**
	 * Application name for DAO configuration.This must be same as in file ApplicationDAOProperties.xml.
	 */
	private static final String APP_NAME="commonpackagetest";
	/**
	 * DAO factory.
	 */
	private static final  IDAOFactory DAO_FACTORY = DAOConfigFactory.getInstance().getDAOFactory(APP_NAME);

	/**
	 *  Positive test case for public void delete(Object).
	 */
	public void testDelete()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			MyDomainObject ado = getAbstractDomainObject(1);
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			defaultBizLogic.delete(ado);
			assertTrue("Object deleted successfuly.", true);
		}
		catch (Exception exception)
		{
			fail("Not able to delete the object.");
			logger.fatal(exception.getMessage(),exception);
		}
	}
	/**
	 *  Negative test case for public void delete(Object).
	 */
	public void testFailDelete()
	{
		try
		{
			MyDAOImpl.isTestForFail=true;
			MyDomainObject ado = getAbstractDomainObject(1);
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			defaultBizLogic.delete(ado);
			fail("Negative test case:must throw BizLogicException.");
		}
		catch (BizLogicException exception)
		{
			assertTrue("Negative test case: Thrown BizLogicException during delete operation.", true);
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Positive test case for public final void insert(Object obj).
	 */
	public void testInsertWithSessionDataBean()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			MyDomainObject ado = getAbstractDomainObject(1);
			MyDefaultBizLogic defaultBizLogic = new MyDefaultBizLogic();
			defaultBizLogic.insert(ado);
			assertTrue("Object inserted successfuly.", true);
		}
		catch (BizLogicException exception)
		{
			fail("Not able to insert the object.");
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Negative test case for public final void insert(Object obj).
	 */
	public void testFailInsertWithSessionDataBean()
	{
		try
		{
			MyDAOImpl.isTestForFail=true;
			MyDomainObject ado = getAbstractDomainObject(1);
			MyDefaultBizLogic defaultBizLogic = new MyDefaultBizLogic();
			defaultBizLogic.insert(ado);
			fail("Negative test case:must throw BizLogicException.");
		}
		catch (BizLogicException exception)
		{
			assertTrue("Negative test case: Thrown BizLogicException during insert operation.", true);
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Positive test case for
	 *  public final void insert(Collection objCollection,
			SessionDataBean sessionDataBean, boolean isInsertOnly).
	 */
	public void testInsertCollection()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			Collection<AbstractDomainObject> adoList= new ArrayList<AbstractDomainObject>();
			adoList.add(getAbstractDomainObject(1));
			adoList.add(getAbstractDomainObject(2));
			MyDefaultBizLogic defaultBizLogic = new MyDefaultBizLogic();
			defaultBizLogic.insert(adoList,null,true);
			assertTrue("Object inserted successfuly.", true);
		}
		catch (BizLogicException exception)
		{
			fail("Not able to insert the object.");
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Negative test case for
	 *  public final void insert(Collection objCollection,
			SessionDataBean sessionDataBean, boolean isInsertOnly).
	 */
	public void testFailInsertCollection()
	{
		try
		{
			MyDAOImpl.isTestForFail=true;
			Collection<AbstractDomainObject> adoList= new ArrayList<AbstractDomainObject>();
			adoList.add(getAbstractDomainObject(1));
			adoList.add(getAbstractDomainObject(2));
			MyDefaultBizLogic defaultBizLogic = new MyDefaultBizLogic();
			defaultBizLogic.insert(adoList,null,true);
			fail("Negative test case:must throw BizLogicException.");
		}
		catch (BizLogicException exception)
		{
			assertTrue("Negative test case: Thrown BizLogicException during collection insert.", true);
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Negative test case for
	 *  public final void insert(Collection objCollection,
			SessionDataBean sessionDataBean, boolean isInsertOnly).
	 */
	public void testinsertMultipleWithoutAutorization()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			DAO myJdbcDao = DAO_FACTORY.getDAO();
			Collection<AbstractDomainObject> adoList= new ArrayList<AbstractDomainObject>();
			adoList.add(getAbstractDomainObject(1));
			adoList.add(getAbstractDomainObject(2));
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			defaultBizLogic.insertMultiple(adoList,myJdbcDao,null);
			fail("Negative test case:must throw BizLogicException.");
		}
		catch (BizLogicException exception)
		{
			assertTrue("Negative test case: Thrown BizLogicException during collection insert.", true);
			logger.fatal(exception.getMessage(),exception);
		}
		catch (DAOException exception)
		{
			fail("Negative test case:must throw BizLogicException.");
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Positive test case for
	 *  public final void update(Object currentObj).
	 */
	public void testUpdate()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			MyDefaultBizLogic defaultBizLogic = new MyDefaultBizLogic();
			defaultBizLogic.update(getAbstractDomainObject(1));
			assertTrue("Object updated successfuly.", true);
		}
		catch (BizLogicException exception)
		{
			fail("Not able to update the object.");
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Positive test case for
	 *  public final void update(Object currentObj).
	 */
	public void testUpdateWithoAutorization()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			defaultBizLogic.update(getAbstractDomainObject(1));
			fail("Negative test case:must throw BizLogicException.");
		}
		catch (BizLogicException exception)
		{
			assertTrue("Negative test case: Thrown BizLogicException during update.", true);
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Positive test case for
	 *  public final void setPrivilege(String privilegeName, Class objectType,
			Long[] objectIds, Long userId, SessionDataBean sessionDataBean, String roleId,
			boolean assignToUser, boolean assignOperation)
	 */
	public void testSetPrivilege()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			Long []objeIds= {Long.valueOf(1),Long.valueOf(2)};
			Long userId=Long.valueOf(1);
			SessionDataBean sessionDataBean=null;
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			defaultBizLogic.setPrivilege(1, "privilegeName", "".getClass(), objeIds,
					userId, sessionDataBean, "roleId", true, false);
			assertTrue("Object updated successfuly.", true);
		}
		catch (BizLogicException exception)
		{
			assertTrue("Negative test case: Thrown BizLogicException during update.", true);
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Negative test case for
	 *  public final void setPrivilege(String privilegeName, Class objectType,
			Long[] objectIds, Long userId, SessionDataBean sessionDataBean, String roleId,
			boolean assignToUser, boolean assignOperation)
	 */
	public void testFailSetPrivilege()
	{
		try
		{
			MyDAOImpl.isTestForFail=true;
			Long []objeIds= {Long.valueOf(1),Long.valueOf(2)};
			Long userId=Long.valueOf(1);
			SessionDataBean sessionDataBean=null;
			MyDefaultBizLogic defaultBizLogic = new MyDefaultBizLogic();
			defaultBizLogic.setPrivilege(1, "privilegeName", "".getClass(), objeIds,
					userId, sessionDataBean, "roleId", true, false);
			fail("Negative test case:must throw BizLogicException.");
		}
		catch (BizLogicException exception)
		{
			assertTrue("Negative test case: Thrown BizLogicException during SetPrivilege.", true);
			logger.fatal(exception.getMessage(),exception);
		}
	}

	/**
	 *  Positive test case for
	 *  public String formatException(Exception exception, Object obj, String operation).
	 */
	public void testFormatException()
	{
		MyDAOImpl.isTestForFail=false;
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		ErrorKey errorKey = ErrorKey.getErrorKey("db.operation.error");
		DAOException exception= new DAOException(errorKey,new Exception(""),"MyJDAOImpl.java :"+
				DAOConstants.EXECUTE_QUERY_ERROR);
		defaultBizLogic.formatException(exception,null,null);
		assertTrue("Got formated message.", true);
	}

	/**
	 *  Positive test case for
	 *  public String formatException(Exception exception, Object obj, String operation).
	 */
	public void testFormatExceptionWithNullException()
	{
		MyDAOImpl.isTestForFail=false;
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		defaultBizLogic.formatException(null,null,null);
		assertTrue("Got formated message.", true);
	}

	public void testPopulateUIBean()
	{
		try
		{
			MyDAOImpl.isTestForFail=false;
			MyDAOImpl.returnActivityStatusObj=true;
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			MyActionForm uiForm= new MyActionForm();
			defaultBizLogic.populateUIBean("className",Long.valueOf(1),uiForm);
			assertTrue("Populated UI Bean successfully.", true);
		}
		catch (BizLogicException exception)
		{
			fail("Not able to Populate UI Bean.");
			logger.fatal(exception.getMessage(),exception);
		}
	}
	/**
	 * @return AbstractDomainObject
	 */
	private MyDomainObject getAbstractDomainObject(int id)
	{
		MyDomainObject ado= new MyDomainObject();
		ado.setActivityStatus("Active");
		ado.setId(Long.valueOf(id));
		return ado;
	}
}
