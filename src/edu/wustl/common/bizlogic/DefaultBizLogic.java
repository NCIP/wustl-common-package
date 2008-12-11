/**
 * <p>Title: DefaultBizLogic Class>
 * <p>Description:	DefaultBizLogic is a class which contains the default
 * implementations required for all the biz logic classes.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */

package edu.wustl.common.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.AuditEventDetails;
import edu.wustl.common.domain.AuditEventLog;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.condition.INClause;
import edu.wustl.dao.connectionmanager.IConnectionManager;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * DefaultBizLogic is a class which contains the default
 * implementations required for all the biz logic classes.
 * @author kapil_kaveeshwar
 */
public class DefaultBizLogic extends AbstractBizLogic
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(DefaultBizLogic.class);

	/**
	 * This method gets called before insert method.
	 * Any logic before inserting into database can be included here.
	 * @param obj The object to be inserted.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	protected void preInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{

	}

	/**
	 *
	 * @param currentObj current Object.
	 * @throws BizLogicException BizLogic Exception
	 */
	public void createProtectionElement(Object currentObj) throws BizLogicException
	{

	}

	/**
	 * Inserts an object into the database.
	 * @param obj The object to be inserted.
	 * @param dao The dao object.
	 * @param sessionDataBean session specific Data
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected void insert(Object obj, DAO dao, SessionDataBean sessionDataBean)	throws BizLogicException
	{
		try
		{
			dao.insert(obj, sessionDataBean, true, true);
		}
		catch (DAOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.insert.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
	}

	/**
	 * This method gets called after insert method.
	 * Any logic after inserting object in database can be included here.
	 * @param obj The inserted object.
	 * @param dao the dao object
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	protected void postInsert(Object obj, DAO dao, SessionDataBean sessionDataBean)
			throws BizLogicException
	{

	}

	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @param dao The dao object.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected void delete(Object obj, DAO dao) throws BizLogicException
	{
		try
		{
			dao.delete(obj);
		}
		catch (DAOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.delete.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
	}

	/**
	 * This method gets called before update method.
	 * Any logic before updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	protected void preUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{

	}

	/**
	 * Updates an objects into the database.
	 * @param dao The dao object.
	 * @param obj The object to be updated into the database.
	 * @param oldObj old Object.
	 * @param sessionDataBean session specific Data
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected void update(DAO dao, Object obj, Object oldObj, SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			dao.update(obj);
			dao.audit(obj, oldObj, sessionDataBean, true);
		}
		catch (DAOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.update.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
	}

	/**
	 * This method gets called after update method.
	 * Any logic after updating into database can be included here.
	 * @param dao the dao object
	 * @param currentObj The object to be updated.
	 * @param oldObj The old object.
	 * @param sessionDataBean session specific data
	 * @throws BizLogicException Generic BizLogic Exception
	 * */
	protected void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{

	}

	/**
	 * This method validate the object.
	 * @param obj object.
	 * @param dao The dao object
	 * @param operation operation
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return true
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		return true;
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param sourceObjectName	source object name
	 * @param selectColumnName	An array of field names to be selected
	 * @param whereColumnName column name used in where clause
	 * @param whereColumnCondition conditions used in where clause.
	 * @param whereColumnValue column values used in where clause.
	 * @param joinCondition join condition used in  where clause.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return list retrieved objects list
	 * @deprecated This method has been deprecated with new DAO implementation.
	 *  instead of this method retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause)
			method can be used.
	 */
	public List<Object> retrieve(String sourceObjectName, String[] selectColumnName,
			String[] whereColumnName, String[] whereColumnCondition, Object[] whereColumnValue,
			String joinCondition) throws BizLogicException
	{
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		List<Object> list = null;
		DAO dao = null;
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
			queryWhereClause.getWhereCondition(whereColumnName, whereColumnCondition,
					whereColumnValue,joinCondition);
			list = dao.retrieve(sourceObjectName, selectColumnName,queryWhereClause);
		}
		catch (DAOException daoExp)
		{
			logger.error("not able to retrieve.", daoExp);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
			throw new BizLogicException(errorKey,daoExp, "DefaultBizLogic");
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException exception)
			{
				logger.error("not able close the session.", exception);
				ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
				throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
			}
		}

		return list;
	}
	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param sourceObjectName :source object name
	 * @param selectColumnName :An array of field names to be selected
	 * @param queryWhereClause :object of QueryWhereClause.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return list :retrieved objects list
	 */
	public List<Object> retrieve(String sourceObjectName, String[] selectColumnName,
			QueryWhereClause queryWhereClause) throws BizLogicException
	{
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		DAO dao=null;
		List<Object> list = null;
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			list = dao.retrieve(sourceObjectName, selectColumnName,queryWhereClause);
		}
		catch (DAOException daoExp)
		{
			logger.error("not able to retrieve.", daoExp);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
			throw new BizLogicException(errorKey,daoExp, "DefaultBizLogic");
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException exception)
			{
				logger.error("not able close the session.", exception);
				ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
				throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
			}
		}

		return list;
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param sourceObjectName source Object Name
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparison condition for the field values.
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return List
	 */
	public List retrieve(String sourceObjectName, String[] whereColumnName,
			String[] whereColumnCondition, Object[] whereColumnValue, String joinCondition)
			throws BizLogicException
	{
		return retrieve(sourceObjectName, null, whereColumnName, whereColumnCondition,
				whereColumnValue, joinCondition);
	}

	/**
	 * Retrieves the records for class name in sourceObjectName according to field values passed.
	 * @param className class Name
	 * @param colName Contains the field name.
	 * @param colValue Contains the field value.
	 * @return records
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List retrieve(String className, String colName, Object colValue) throws BizLogicException
	{
		String[] colNames = {colName};
		String[] colConditions = {"="};
		Object[] colValues = {colValue};

		return retrieve(className, colNames, colConditions, colValues, Constants.AND_JOIN_CONDITION);
	}

	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName Contains the class name whose records are to be retrieved.
	 * @return records
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List retrieve(String sourceObjectName) throws BizLogicException
	{
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		DAO dao=null;
		List<Object> list = null;
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			list = dao.retrieve(sourceObjectName);
		}
		catch (DAOException daoExp)
		{
			logger.error("not able to retrieve.", daoExp);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
			throw new BizLogicException(errorKey,daoExp, "DefaultBizLogic");
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException exception)
			{
				logger.error("not able close the session.", exception);
				ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
				throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
			}
		}

		return list;
	}

	/**
	 * Retrieves all the records for class name in sourceObjectName.
	 * @param sourceObjectName Contains the class name whose records are to be retrieved.
	 * @param selectColumnName An array of the fields that should be selected
	 * @return records
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List retrieve(String sourceObjectName, String[] selectColumnName) throws BizLogicException
	{
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		DAO dao=null;
		List<Object> list = null;
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			list = dao.retrieve(sourceObjectName, selectColumnName);
		}
		catch (DAOException daoExp)
		{
			logger.error("not able to retrieve.", daoExp);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
			throw new BizLogicException(errorKey,daoExp, "DefaultBizLogic");
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException exception)
			{
				logger.error("not able close the session.", exception);
				ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
				throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
			}
		}

		return list;
	}

	/**
	 * Retrieve object.
	 * @param sourceObjectName Contains the class name whose object is to be retrieved.
	 * @param identifier Contains the id whose object is to be retrieved.
	 * @return object.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public Object retrieve(String sourceObjectName, Long identifier) throws BizLogicException
	{

		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		DAO dao=null;
		Object object = null;
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			object = dao.retrieve(sourceObjectName, identifier);
		}
		catch (DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
			throw new BizLogicException(errorKey,daoExp, "DefaultBizLogic");
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException exception)
			{
				ErrorKey errorKey=ErrorKey.getErrorKey("biz.ret.error");
				throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
			}
		}

		return object;

	}

	/**
	 * This method gets list.
	 * @param sourceObjectName source Object Name
	 * @param displayNameFields display Name Fields
	 * @param valueField value Field
	 * @param isToExcludeDisabled is To Exclude Disabled
	 * @return List
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List getList(String sourceObjectName, String[] displayNameFields, String valueField,
			boolean isToExcludeDisabled) throws BizLogicException
	{
		String[] whereColumnName = null;
		String[] whereColumnCondition = null;
		Object[] whereColumnValue = null;
		String joinCondition = null;
		String separatorBetweenFields = ", ";

		if (isToExcludeDisabled)
		{
			whereColumnName = new String[]{"activityStatus"};
			whereColumnCondition = new String[]{"!="};
			whereColumnValue = new String[]{Constants.ACTIVITY_STATUS_DISABLED};
		}

		return getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition,
				separatorBetweenFields);
			}

	/**
	 * Returns collection of name value pairs.
	 * @param sourceObjectName source Object Name
	 * @param displayNameFields display Name Fields
	 * @param valueField value Field
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparison condition for the field values.
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @param separatorBetweenFields separator Between Fields
	 * @param isToExcludeDisabled is To Exclude Disabled
	 * @return Returns collection of name value pairs
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List getList(String sourceObjectName, String[] displayNameFields, String valueField,
			String[] whereColumnName, String[] whereColumnCondition, Object[] whereColumnValue,
			String joinCondition, String separatorBetweenFields, boolean isToExcludeDisabled)
			throws BizLogicException
	{
		String[] whereColName = {""};
		String[] whereColCondition = {""};
		Object[] whereColValue = {""};

		if (isToExcludeDisabled)
		{
			whereColName = (String[]) Utility.addElement(whereColumnName, "activityStatus");
			whereColCondition = (String[]) Utility.addElement(whereColumnCondition, "!=");
			whereColValue = Utility
					.addElement(whereColumnValue, Constants.ACTIVITY_STATUS_DISABLED);
		}

		return getList(sourceObjectName, displayNameFields, valueField, whereColName,
				whereColCondition, whereColValue, joinCondition, separatorBetweenFields);
	}

	/**
	 * Sorting of ID columns.
	 * @param sourceObjectName source Object Name
	 * @param displayNameFields display Name Fields
	 * @param valueField value Field
	 * @param whereColumnName An array of field names.
	 * @param whereColumnCondition The comparison condition for the field values.
	 * @param whereColumnValue An array of field values.
	 * @param joinCondition The join condition.
	 * @param separatorBetweenFields separator Between Fields
	 * @return nameValuePairs.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	private List getList(String sourceObjectName, String[] displayNameFields, String valueField,
			String[] whereColumnName, String[] whereColumnCondition, Object[] whereColumnValue,
			String joinCondition, String separatorBetweenFields) throws BizLogicException
	{
		String[] selectColumnName = new String[displayNameFields.length + 1];
		System.arraycopy(displayNameFields,0,selectColumnName,0,displayNameFields.length);
		selectColumnName[displayNameFields.length] = valueField;
		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		try
		{
			queryWhereClause.getWhereCondition(whereColumnName, whereColumnCondition,
					whereColumnValue,joinCondition);
		}
		catch (DAOException exception)
		{
			logger.error("Not able to get list.", exception);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.getlist.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
		List results = retrieve(sourceObjectName, selectColumnName, queryWhereClause);

		/**
		 * For each row in the result a vector will be created.Vector will contain all the columns
		 * other than the value column(last column).
		 * If there is only one column in the result it will be set as the Name for the NameValueBean.
		 * When more than one columns are present, a string representation will be set.
		 */
		List<NameValueBean> nameValuePairs = new ArrayList<NameValueBean>();
		nameValuePairs.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		getNameValueList(separatorBetweenFields, nameValuePairs, results);
		Collections.sort(nameValuePairs);
		return nameValuePairs;
	}

	/**
	 * @param separatorBetweenFields separator Between Fields
	 * @param nameValuePairs list to add attributes.
	 * @param results results from database.
	 */
	private void getNameValueList(String separatorBetweenFields, List<NameValueBean> nameValuePairs,
				List<Object> results)
	{
		NameValueBean nameValueBean;
		Object[] columnArray;
		if (results != null)
		{
			for (int i = 0; i < results.size(); i++)
			{
				columnArray = (Object[]) results.get(i);
				Object tmpObj = null;
				List tmpBuffer = new ArrayList();
				StringBuffer nameBuff = new StringBuffer();
				if (columnArray != null)
				{
					getTempBuffer(columnArray, tmpBuffer);
					tmpObj = getName(separatorBetweenFields, tmpBuffer, nameBuff);
					nameValueBean = new NameValueBean();
					nameValueBean.setName(tmpObj);
					nameValueBean.setValue(columnArray[columnArray.length - 1].toString());
					nameValuePairs.add(nameValueBean);
				}
			}
		}
	}

	/**
	 * @param separatorBetweenFields separator Between Fields
	 * @param tmpBuffer buffer
	 * @param nameBuff buffer
	 * @return name for list
	 */
	private Object getName(String separatorBetweenFields, List tmpBuffer, StringBuffer nameBuff)
	{
		Object tmpObj;
		//create name data
		if (tmpBuffer.size() == 1)//	only one column
		{
			tmpObj = tmpBuffer.get(0);
		}
		else
		// multiple columns
		{
			for (int j = 0; j < tmpBuffer.size(); j++)
			{
				nameBuff.append(tmpBuffer.get(j).toString());
				if (j < tmpBuffer.size() - 1)
				{
					nameBuff.append(separatorBetweenFields);
				}
			}

			tmpObj=nameBuff.toString();
		}
		return tmpObj;
	}

	/**
	 * @param columnArray column array
	 * @param tmpBuffer buffer
	 */
	private void getTempBuffer(Object[] columnArray, List tmpBuffer)
	{
		for (int j = 0; j < columnArray.length - 1; j++)
		{
			if (columnArray[j] != null && (!columnArray[j].toString().equals("")))
			{
				tmpBuffer.add(columnArray[j]);
			}
		}
	}

	/**
	 * This method disable Objects.
	 * @param dao The dao object.
	 * @param sourceClass source Class
	 * @param classIdentifier class Identifier
	 * @param tablename Contains the field table name
	 * @param colName Contains the field name
	 * @param objIDArr object ID Array
	 * @return listOfSubElement
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected List disableObjects(DAO dao, Class sourceClass, String classIdentifier,
			String tablename, String colName, Long[] objIDArr) throws BizLogicException
	{
		try
		{
			dao.disableRelatedObjects(tablename, colName, objIDArr);
		}
		catch (DAOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.disableobj.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
		List listOfSubElement = getRelatedObjects(dao, sourceClass, classIdentifier, objIDArr);
		auditDisabledObjects(dao, tablename, listOfSubElement);
		return listOfSubElement;
	}

	/**
	 * This method disable Objects.
	 * @param dao The dao object.
	 * @param tablename Contains the field table name
	 * @param sourceClass source Class
	 * @param classIdentifier class Identifier
	 * @param objIDArr object ID Array
	 * @return listOfSubElement
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected List disableObjects(DAO dao, String tablename, Class sourceClass,
			String classIdentifier, Long[] objIDArr) throws BizLogicException
	{
		List listOfSubElement = getRelatedObjects(dao, sourceClass, classIdentifier, objIDArr);
		disableAndAuditObjects(dao, sourceClass.getName(), tablename, listOfSubElement);
		return listOfSubElement;
	}

	/**
	 * This metod disabled And Audit Objects.
	 * @param dao The dao object.
	 * @param sourceClass source Class
	 * @param tablename Contains the field table name
	 * @param listOfSubElement list Of SubElement
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected void disableAndAuditObjects(DAO dao, String sourceClass, String tablename,
			List listOfSubElement) throws BizLogicException
	{
		Iterator iterator = listOfSubElement.iterator();
		Collection auditEventLogsCollection = new HashSet();

		try
		{
			while (iterator.hasNext())
			{
				Long objectId = (Long) iterator.next();
				IActivityStatus object = (IActivityStatus) dao.retrieve(sourceClass, objectId);
				object.setActivityStatus(Constants.ACTIVITY_STATUS_DISABLED);
				dao.update(object);
				addAuditEventstoColl(tablename, auditEventLogsCollection, objectId);
			}

			HibernateDAO hibDAO = (HibernateDAO) dao;
			hibDAO.addAuditEventLogs(auditEventLogsCollection);
		}
		catch (DAOException daoEx)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.disableaudit.error");
			throw new BizLogicException(errorKey,daoEx, "DefaultBizLogicLogic");
		}
	}

	/**
	 * This method add Audit Events to Collection.
	 * @param tablename Contains the field table name
	 * @param auditEventLogsCollection audit Event Logs Collection
	 * @param objectId object Id
	 */
	private void addAuditEventstoColl(String tablename, Collection auditEventLogsCollection,
			Long objectId)
	{
		AuditEventLog auditEventLog = new AuditEventLog();
		auditEventLog.setObjectIdentifier(objectId);
		auditEventLog.setObjectName(tablename);
		auditEventLog.setEventType(Constants.UPDATE_OPERATION);

		Collection auditEventDetailsCollection = new HashSet();
		AuditEventDetails auditEventDetails = new AuditEventDetails();
		auditEventDetails.setElementName(Constants.ACTIVITY_STATUS_COLUMN);
		auditEventDetails.setCurrentValue(Constants.ACTIVITY_STATUS_DISABLED);

		auditEventDetailsCollection.add(auditEventDetails);

		auditEventLog.setAuditEventDetailsCollcetion(auditEventDetailsCollection);
		auditEventLogsCollection.add(auditEventLog);
	}

	/**
	 * @param dao The dao object.
	 * @param tablename Contains the field table name
	 * @param listOfSubElement list Of SubElement
	 */
	protected void auditDisabledObjects(DAO dao, String tablename, List listOfSubElement)
	{
		Iterator iterator = listOfSubElement.iterator();
		Collection auditEventLogsCollection = new HashSet();

		while (iterator.hasNext())
		{
			Long objectIdentifier = (Long) iterator.next();
			addAuditEventstoColl(tablename, auditEventLogsCollection, objectIdentifier);
		}
		HibernateDAO hibDAO = (HibernateDAO) dao;
		hibDAO.addAuditEventLogs(auditEventLogsCollection);
	}

	/**
	 * This method gets related objects.
	 * @param dao The dao object.
	 * @param sourceClass source Class
	 * @param classIdentifier class Identifier
	 * @param objIDArr object ID Array.
	 * @return list of related objects.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List getRelatedObjects(DAO dao, Class sourceClass, String classIdentifier,
			Long[] objIDArr) throws BizLogicException
	{
		String sourceObjectName = sourceClass.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};

		String whereColumnName = classIdentifier + "." + Constants.SYSTEM_IDENTIFIER;
		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new INClause(whereColumnName,objIDArr,sourceObjectName));

		List list=null;
		try
		{
			list = dao.retrieve(sourceObjectName, selectColumnName,queryWhereClause);
		}
		catch (DAOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.getrelatedobj.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
		list = Utility.removeNull(list);
		logger.debug(sourceClass.getName() + " Related objects to "
				+ edu.wustl.common.util.Utility.getArrayString(objIDArr) + " are " + list);
		return list;
	}

	/**
	 * Overloaded to let selectColumnName and whereColumnName also be
	 * parameters to method and are not hardcoded.
	 * @param dao The dao object.
	 * @param sourceClass source Class
	 * @param selectColumnName An array of field names.
	 * @param queryWhereClause object of QueryWhereClause
	 * @return list of related objects.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List getRelatedObjects(DAO dao, Class sourceClass, String[] selectColumnName,
			QueryWhereClause queryWhereClause) throws BizLogicException
	{
		String sourceObjectName = sourceClass.getName();
		String[] whereColumnCondition = {"in"};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		List<Object> list=null;
		try
		{
			list = dao.retrieve(sourceObjectName, selectColumnName,queryWhereClause);
		}
		catch (DAOException e)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.getrelatedobj.error");
			throw new BizLogicException(errorKey,null, "DefaultBizLogic");
		}
		list = Utility.removeNull(list);
		return list;
	}

	/**
	 * This method gets related objects.
	 * @param dao The dao object.
	 * @param sourceClass source Class
	 * @param queryWhereClause object of QueryWhereClause
	 * @return list of related objects.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public List getRelatedObjects(DAO dao, Class sourceClass,QueryWhereClause queryWhereClause)
							throws BizLogicException
	{
		String sourceObjectName = sourceClass.getName();
		String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		List<Object> list=null;
		try
		{
			list = dao.retrieve(sourceObjectName, selectColumnName,queryWhereClause);
		}
		catch (DAOException e)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.getrelatedobj.error");
			throw new BizLogicException(errorKey,null, "DefaultBizLogic");
		}
		list = Utility.removeNull(list);
		return list;
	}



	/**
	 * This method gets Corresponding Old Object.
	 * @param objectCollection object Collection
	 * @param identifier id.
	 * @return Object.
	 */
	protected Object getCorrespondingOldObject(Collection objectCollection, Long identifier)
	{
		Iterator iterator = objectCollection.iterator();
		AbstractDomainObject abstractDomainObject = null;
		while (iterator.hasNext())
		{
			AbstractDomainObject abstractDomainObj = (AbstractDomainObject) iterator.next();

			if (identifier != null && identifier.equals(abstractDomainObj.getId()))
			{
				abstractDomainObject = abstractDomainObj;
				break;
			}
		}
		return abstractDomainObject;
	}

	/**
	 *  Method to check the ActivityStatus of the given identifier.
	 * @param dao The dao object.
	 * @param ado object of IActivityStatus
	 * @param errorName Display Name of the Element
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected void checkStatus(DAO dao, IActivityStatus ado, String errorName) throws BizLogicException
	{
		if (ado != null)
		{
			Long identifier = ((AbstractDomainObject) ado).getId();
			if (identifier != null)
			{
				String className = ado.getClass().getName();
				String activityStatus = ado.getActivityStatus();
				if (activityStatus == null)
				{
					activityStatus = getActivityStatus(dao, className, identifier);
				}
				if (activityStatus.equals(Constants.ACTIVITY_STATUS_CLOSED))
				{
					ErrorKey errorKey=ErrorKey.getErrorKey("biz.checkstatus.error");
					throw new BizLogicException(errorKey,null, "DefaultBizLogic");
				}
			}
		}
	}

	/**
	 * This method gets Activity Status.
	 * @param dao The dao object.
	 * @param sourceObjectName source Object Name
	 * @param indetifier indetifier
	 * @return activityStatus
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public String getActivityStatus(DAO dao, String sourceObjectName, Long indetifier)
			throws BizLogicException
	{
		String[] selectColumnName = {Constants.ACTIVITY_STATUS};
		QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(Constants.SYSTEM_IDENTIFIER,indetifier.toString(),sourceObjectName));

		List<Object> list;
		try
		{
			list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
		}
		catch (DAOException daoEx)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.activitystatus.error");
			throw new BizLogicException(errorKey,daoEx, "DefaultBizLogic");
		}
		String activityStatus = "";
		if (!list.isEmpty())
		{
			Object obj = list.get(0);
			logger.debug("obj Class " + obj.getClass());
			//Object[] objArr = (String)obj
			activityStatus = (String) obj;
		}
		return activityStatus;
	}

	/**
	 * This method insert object.
	 * @param obj object to be inserted.
	 * @param dao The dao object.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected void insert(Object obj, DAO dao) throws BizLogicException
	{
		try
		{
			dao.insert(obj, null, false, false);
		}
		catch (DAOException daoEx)
		{
			logger.debug("Exception during insert operation", daoEx);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.insert.error");
			throw new BizLogicException(errorKey,daoEx, "DefaultBizLogic");
		}
	}

	/**
	 * This method updates object in database.
	 * @param dao The dao object.
	 * @param obj object to be updated.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	protected void update(DAO dao, Object obj) throws BizLogicException
	{
		try
		{
			dao.update(obj);
		}
		catch (DAOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.update.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic-User not authorized");
		}
	}

	/**
	 * This method retrieve Attribute.
	 * @param objClass objClass
	 * @param identifier identifier
	 * @param attributeName attribute Name
	 * @return attribute.
	 * @throws BizLogicException Generic BizLogic Exception
	 */
	public Object retrieveAttribute(Class objClass, Long identifier, String attributeName)
			throws BizLogicException
	{
		String columnName=Constants.SYSTEM_IDENTIFIER;
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		DAO dao=null;
		Object attribute = null;
		try
		{
			dao = daofactory.getDAO();
			dao.openSession(null);
			attribute = dao.retrieveAttribute(objClass, identifier, attributeName,columnName);
		}
		catch (DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.retattr.error");
			throw new BizLogicException(errorKey,daoExp, "DefaultBizLogic");
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException exception)
			{
				logger.error("Not able to close session.", exception);
				ErrorKey errorKey=ErrorKey.getErrorKey("biz.retattr.error");
				throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
			}
		}
		return attribute;
	}

	/**
	 * To retrieve the attribute value for the given source object name & Id.
	 * @param sourceObjectName Source object in the Database.
	 * @param identifier Id of the object.
	 * @param attributeName attribute name to be retrieved.
	 * @return The Attribute value corresponding to the SourceObjectName & id.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @see edu.wustl.common.bizlogic.IBizLogic
	 * #retrieveAttribute(java.lang.String, java.lang.Long, java.lang.String)
	 */
	public Object retrieveAttribute(String sourceObjectName, Long identifier, String attributeName)
			throws BizLogicException
	{
		Object attribute = null;
		try
		{

			Class clazz=Class.forName(sourceObjectName);
			attribute=retrieveAttribute(clazz,identifier,attributeName);
		}
		catch (ClassNotFoundException exception)
		{
			logger.error("Not able to find class:"+sourceObjectName, exception);
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.retattr.error");
			throw new BizLogicException(errorKey,exception,
					"DefaultBizLogic-Not able to find class:"+sourceObjectName);
		}
		return attribute;
	}

	/**
	 * This method gets called before populateUIBean method.
	 * Any logic before updating uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 */
	protected void prePopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)
			throws BizLogicException
	{

	}

	/**
	 * This method gets called after populateUIBean method.
	 * Any logic after populating  object uiForm can be included here.
	 * @param domainObj object of type AbstractDomainObject
	 * @param uiForm object of the class which implements IValueObject
	 * @throws BizLogicException BizLogic Exception
	 */
	protected void postPopulateUIBean(AbstractDomainObject domainObj, IValueObject uiForm)
			throws BizLogicException
	{

	}

	/**
	 *@param objCollection object collection.
	 *@param dao The dao object.
	 *@param sessionDataBean session specific Data
	 *@throws BizLogicException Generic BizLogic Exception
	 */
	@Override
	protected void postInsert(Collection<AbstractDomainObject> objCollection, DAO dao,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Gets privilege name depending upon operation performed.
	 * @param domainObject Object on which authorization is reqd.
	 * @return Privilege Name
	 */
	protected String getPrivilegeName(Object domainObject)
	{
		String privilegeName = Variables.privilegeDetailsMap.get(getPrivilegeKey(domainObject));
		return privilegeName;
	}

	/**
	 * @param domainObject Object on which authorization is required.
	 * @return Privilege Key
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return null;
	}

	/**
	 * @see edu.wustl.common.bizlogic.IBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 * @param dao The dao object.
	 * @param domainObject Object on which authorization is required.
	 * @return allow Operation.
	 */
	public String getObjectId(DAO dao, Object domainObject)
	{
		return Constants.ALLOW_OPERATION;
	}

	/**
	 * Executes the HQL query.
	 * @param query HQL query to execute.
	 * @throws BizLogicException Generic BizLogic Exception
	 * @return returner
	 */
	public List executeQuery(String query) throws BizLogicException
	{
		List returner = null;
		IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory();
		DAO dao=null;
		IConnectionManager connectionManager = dao.getConnectionManager();
		Session session = null;
		try
		{
			dao = daofactory.getDAO();
			session = connectionManager.getCleanSession();
			Query hibernateQuery = session.createQuery(query);
			returner = hibernateQuery.list();
		}
		catch (HibernateException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.exequery.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
		catch (DAOException exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("biz.exequery.error");
			throw new BizLogicException(errorKey,exception, "DefaultBizLogic");
		}
		finally
		{
			session.close();
		}
		return returner;
	}

	/**
	 * Read Denied To be Checked.
	 * @return false
	 */
	@Override
	public boolean isReadDeniedTobeChecked()
	{
		return false;
	}

	/**
	 * This method gets Read Denied Privilege Name.
	 * @return Privilege Name
	 */
	@Override
	public String getReadDeniedPrivilegeName()
	{
		return null;
	}

	@Override
	protected void setPrivilege(DAO dao, String privilegeName,
			Class objectType, Long[] objectIds, Long userId, String roleId,
			boolean assignToUser, boolean assignOperation)
			throws BizLogicException 
	{
		// TODO Auto-generated method stub
	}

}