/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.bizlogic;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import oracle.sql.CLOB;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.query.Client;
import edu.wustl.common.query.DataElement;
import edu.wustl.common.query.Operator;
import edu.wustl.common.query.Query;
import edu.wustl.common.query.Relation;
import edu.wustl.common.query.RelationCondition;
import edu.wustl.common.util.dbmanager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class QueryBizLogic extends DefaultBizLogic
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(QueryBizLogic.class);
	/**
	 * specify ALIAS_NAME_TABLE_NAME_MAP_QUERY.
	 */
	private static final String ALIAS_NAME_TABLE_NAME_MAP_QUERY = "select ALIAS_NAME ,TABLE_NAME from "
			+ "CATISSUE_QUERY_TABLE_DATA";

	/**
	 * specify ALIAS_NAME_PRIVILEGE_TYPE_MAP_QUERY.
	 */
	private static final String ALIAS_NAME_PRIVILEGE_TYPE_MAP_QUERY = "select ALIAS_NAME ,PRIVILEGE_ID from "
			+ "CATISSUE_QUERY_TABLE_DATA";

	/**
	 * specify GET_RELATION_DATA query.
	 */
	private static final String GET_RELATION_DATA = "select FIRST_TABLE_ID, SECOND_TABLE_ID,"
			+ "FIRST_TABLE_JOIN_COLUMN, SECOND_TABLE_JOIN_COLUMN "
			+ "from CATISSUE_RELATED_TABLES_MAP";

	/**
	 * specify GET_TABLE_ALIAS query.
	 */
	private static final String GET_TABLE_ALIAS = "select ALIAS_NAME from CATISSUE_QUERY_TABLE_DATA "
			+ "where TABLE_ID=";

	/**
	 * specify GET_RELATED_TABLE_ALIAS_PART1 query.
	 */
	private static final String GET_RELATED_TABLE_ALIAS_PART1 = "SELECT table2.alias_name "
			+ " from catissue_table_relation relation, CATISSUE_QUERY_TABLE_DATA table1, "
			+ " CATISSUE_QUERY_TABLE_DATA table2 "
			+ " where relation.parent_table_id = table1.table_id "
			+ " and relation.child_table_id = table2.table_id " + " and table1.alias_name = ";

	/**
	 * specify GET_RELATED_TABLE_ALIAS_PART2 query part2.
	 */
	private static final String GET_RELATED_TABLE_ALIAS_PART2 = " and exists "
			+ "(select * from catissue_search_display_data displayData "
			+ " where relation.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID)";

	/**
	 * This method gets Query Object Name TableName Map.
	 * @return queryObjectNameTableNameMap.
	 */
	public static HashMap getQueryObjectNameTableNameMap()
	{
		List list = null;
		HashMap queryObjectNameTableNameMap = new HashMap();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(ALIAS_NAME_TABLE_NAME_MAP_QUERY, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				queryObjectNameTableNameMap.put(row.get(0), row.get(1));
			}

		}
		catch (DAOException daoExp)
		{
			logger.debug("Could not obtain table object relation. Exception: "
					+ daoExp.getMessage(), daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			logger.debug("Could not obtain table object relation. Exception:"
					+ classExp.getMessage(), classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
			}
		}
		return queryObjectNameTableNameMap;
	}

	/**
	 * This returns the map containing table alias as key
	 * and type of privilege on that table as value.
	 * @return returns the map.
	 */
	public static HashMap getPivilegeTypeMap()
	{
		List list = null;
		HashMap pivilegeTypeMap = new HashMap();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(ALIAS_NAME_PRIVILEGE_TYPE_MAP_QUERY, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				pivilegeTypeMap.put(row.get(0), row.get(1));

			}

		}
		catch (DAOException daoExp)
		{
			logger.debug("Could not obtain table privilege map. Exception:" + daoExp.getMessage(),
					daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			logger.debug(
					"Could not obtain table privilege map. Exception:" + classExp.getMessage(),
					classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
			}
		}
		return pivilegeTypeMap;
	}

	/**
	 * This method gets relation data.
	 * @return relationConditionsForRelatedTables.
	 */
	public static HashMap getRelationData()
	{
		List list = null;
		JDBCDAO dao = null;
		String tableAlias1;
		String columnName1;
		String tableAlias2;
		String columnName2;
		List columnDataList;
		List row;
		String parentTableColumnID;
		String childTableColumnID;
		HashMap relationConditionsForRelatedTables = new HashMap();
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(GET_RELATION_DATA, null, false, null);

			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				row = (List) iterator.next();
				parentTableColumnID = (String) row.get(0);
				childTableColumnID = (String) row.get(1);
				columnName1 = (String) row.get(2);
				columnName2 = (String) row.get(3);
				if (Integer.parseInt(parentTableColumnID) == 0
						|| Integer.parseInt(childTableColumnID) == 0)
				{
					continue;
				}

				columnDataList = dao.executeQuery(GET_TABLE_ALIAS + parentTableColumnID, null,
						false, null);
				if (columnDataList.size() <= 0)
				{
					continue;
				}
				row = (List) columnDataList.get(0);
				if (row.size() <= 0)
				{
					continue;
				}
				tableAlias1 = (String) row.get(0);

				columnDataList = dao.executeQuery(GET_TABLE_ALIAS + childTableColumnID, null,
						false, null);
				if (columnDataList.size() <= 0)
				{
					continue;
				}
				row = (List) columnDataList.get(0);
				if (row.size() <= 0)
				{
					continue;
				}
				tableAlias2 = (String) row.get(0);

				relationConditionsForRelatedTables.put(new Relation(tableAlias1, tableAlias2),
						new RelationCondition(new DataElement(tableAlias1, columnName1),
								new Operator(Operator.EQUAL), new DataElement(tableAlias2,
										columnName2)));
			}

		}
		catch (DAOException daoExp)
		{
			logger.debug(
					"Could not obtain table object relation. Exception:" + daoExp.getMessage(),
					daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			logger.debug("Could not obtain table object relation. Exception:"
					+ classExp.getMessage(), classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
			}
		}
		return relationConditionsForRelatedTables;
	}

	/**
	 * initialized Query Data.
	 */
	public static void initializeQueryData()
	{

		Client.objectTableNames = QueryBizLogic.getQueryObjectNameTableNameMap();
		Client.relationConditionsForRelatedTables = QueryBizLogic.getRelationData();
		Client.privilegeTypeMap = QueryBizLogic.getPivilegeTypeMap();
		List identifiedData = new ArrayList();

		//For Participant
		//identifiedData = new Vector();
		identifiedData.add("FIRST_NAME");
		identifiedData.add("LAST_NAME");
		identifiedData.add("MIDDLE_NAME");
		identifiedData.add("BIRTH_DATE");
		identifiedData.add("SOCIAL_SECURITY_NUMBER");
		Client.identifiedDataMap.put(Query.PARTICIPANT, identifiedData);

		//For CollectionProtocolRegistration
		identifiedData = new Vector();
		identifiedData.add("REGISTRATION_DATE");
		Client.identifiedDataMap.put(Query.COLLECTION_PROTOCOL_REGISTRATION, identifiedData);

		//For CollectionProtocolRegistration
		identifiedData = new Vector();
		identifiedData.add("MEDICAL_RECORD_NUMBER");
		Client.identifiedDataMap.put(Query.PARTICIPANT_MEDICAL_IDENTIFIER, identifiedData);

		//For CollectionProtocolRegistration
		identifiedData = new Vector();
		identifiedData.add("SURGICAL_PATHOLOGICAL_NUMBER");
		Client.identifiedDataMap.put(Query.CLINICAL_REPORT, identifiedData);

	}

	/**
	 * Returns the aliasName of the table from the table id.
	 * @param columnName column Name
	 * @param columnValue column Value
	 * @return Returns the aliasName.
	 * @throws DAOException generic DAOException.
	 */
	public String getAliasName(String columnName, Object columnValue) throws DAOException
	{
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String[] selectColumnNames = {Constants.TABLE_ALIAS_NAME_COLUMN};
		String[] whereColumnNames = {columnName};
		String[] whereColumnConditions = {"="};
		Object[] whereColumnValues = {columnValue};
		List list = jdbcDAO.retrieve(Constants.TABLE_DATA_TABLE_NAME, selectColumnNames,
				whereColumnNames, whereColumnConditions, whereColumnValues, null);
		jdbcDAO.closeSession();

		String aliasName = null;
		if (!list.isEmpty())
		{
			List row = (List) list.get(0);
			aliasName = (String) row.get(0);
		}

		return aliasName;
	}

	/**
	 * Bug#3549
	 * Patch 1_1
	 * Description: modified query to order the result  by ATTRIBUTE_ORDER column.
	 */
	/**
	 * Sets column names depending on the table name selected for that condition.
	 * @param value table name.
	 * @return Column Names.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception
	 */
	public List getColumnNames(String value) throws DAOException, ClassNotFoundException
	{
		String sql = " SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.ATTRIBUTE_TYPE,"
				+ " temp.TABLES_IN_PATH, temp.DISPLAY_NAME,temp.ATTRIBUTE_ORDER "
				+ " from CATISSUE_QUERY_TABLE_DATA tableData2 join "
				+ " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID,"
				+ " columnData.ATTRIBUTE_TYPE, "
				+ " displayData.DISPLAY_NAME, displayData.ATTRIBUTE_ORDER ,"
				+ " relationData.TABLES_IN_PATH "
				+ " FROM CATISSUE_INTERFACE_COLUMN_DATA columnData, "
				+ " CATISSUE_TABLE_RELATION relationData, "
				+ " CATISSUE_QUERY_TABLE_DATA tableData, "
				+ " CATISSUE_SEARCH_DISPLAY_DATA displayData "
				+ " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "
				+ " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
				+ " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
				+ " columnData.IDENTIFIER = displayData.COL_ID and " + " tableData.ALIAS_NAME = '"
				+ value + "') temp "
				+ " on temp.TABLE_ID = tableData2.TABLE_ID ORDER BY temp.ATTRIBUTE_ORDER";

		logger.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		List columnNameValueBeanList = new ArrayList();

		Iterator iterator = list.iterator();
		int j = 0;
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			String columnValue = (String) rowList.get(j++) + "." + (String) rowList.get(j++) + "."
					+ (String) rowList.get(j++);
			String tablesInPath = (String) rowList.get(j++);
			if ((tablesInPath != null) && (!"".equals(tablesInPath)))
			{
				columnValue = columnValue + "." + tablesInPath;
			}
			String columnName = (String) rowList.get(j++);
			NameValueBean nameValueBean = new NameValueBean();
			nameValueBean.setName(columnName);
			nameValueBean.setValue(columnValue);
			columnNameValueBeanList.add(nameValueBean);
			j = 0;
		}

		return columnNameValueBeanList;
	}

	/**
	 * Sets the next table names depending on the table in the previous row.
	 * @param prevValue previous table name.
	 * @return next table names.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception
	 */
	public Set getNextTableNames(String prevValue) throws DAOException, ClassNotFoundException
	{
		Set objectList = new TreeSet();
		String sql = " (select temp.ALIAS_NAME, temp.DISPLAY_NAME " + " from "
				+ " (select relationData.FIRST_TABLE_ID, tableData.ALIAS_NAME,"
				+ " tableData.DISPLAY_NAME " + " from CATISSUE_QUERY_TABLE_DATA tableData join "
				+ " CATISSUE_RELATED_TABLES_MAP relationData "
				+ " on tableData.TABLE_ID = relationData.SECOND_TABLE_ID)"
				+ " temp join CATISSUE_QUERY_TABLE_DATA tableData2 "
				+ " on temp.FIRST_TABLE_ID = tableData2.TABLE_ID "
				+ " where tableData2.ALIAS_NAME = '" + prevValue + "') " + " union "
				+ " (select temp1.ALIAS_NAME, temp1.DISPLAY_NAME " + " from "
				+ " (select relationData1.SECOND_TABLE_ID, tableData4.ALIAS_NAME,"
				+ " tableData4.DISPLAY_NAME " + " from CATISSUE_QUERY_TABLE_DATA tableData4 join "
				+ " CATISSUE_RELATED_TABLES_MAP relationData1 "
				+ " on tableData4.TABLE_ID = relationData1.FIRST_TABLE_ID)"
				+ " temp1 join CATISSUE_QUERY_TABLE_DATA tableData3 "
				+ " on temp1.SECOND_TABLE_ID = tableData3.TABLE_ID "
				+ " where tableData3.ALIAS_NAME = '" + prevValue + "')";

		logger.debug("TABLE SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		//Adding NameValueBean of select option.
		NameValueBean nameValueBean = new NameValueBean();
		nameValueBean.setName(Constants.SELECT_OPTION);
		nameValueBean.setValue("-1");
		objectList.add(nameValueBean);

		//Adding the NameValueBean of previous selected object.
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);

		sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='" + prevValue
				+ "'";
		List prevValueDisplayNameList = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!prevValueDisplayNameList.isEmpty())
		{
			List rowList = (List) prevValueDisplayNameList.get(0);
			nameValueBean = new NameValueBean();
			nameValueBean.setName((String) rowList.get(0));
			nameValueBean.setValue(prevValue);
			objectList.add(nameValueBean);
		}

		List checkList = getCheckList();
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			int j = 0;
			List rowList = (List) iterator.next();
			if (checkForTable(rowList, checkList))
			{
				nameValueBean = new NameValueBean();
				nameValueBean.setValue((String) rowList.get(j++));
				nameValueBean.setName((String) rowList.get(j));
				objectList.add(nameValueBean);
			}
		}

		return objectList;
	}

	/**
	 * gets Check List.
	 * @return Check List.
	 * @throws DAOException generic DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	private List getCheckList() throws DAOException, ClassNotFoundException
	{
		String sql = " select TABLE_A.ALIAS_NAME, TABLE_A.DISPLAY_NAME "
				+ " from catissue_table_relation TABLE_R, "
				+ " CATISSUE_QUERY_TABLE_DATA TABLE_A, " + " CATISSUE_QUERY_TABLE_DATA TABLE_B "
				+ " where TABLE_R.PARENT_TABLE_ID = TABLE_A.TABLE_ID and "
				+ " TABLE_R.CHILD_TABLE_ID = TABLE_B.TABLE_ID ";

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List checkList = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		return checkList;
	}

	/**
	 * check For Table.
	 * @param rowList row List.
	 * @param checkList check List.
	 * @return is Table Exists.
	 */
	private boolean checkForTable(List rowList, List checkList)
	{
		String aliasName = (String) rowList.get(0), displayName = (String) rowList.get(1);
		Iterator iterator = checkList.iterator();
		boolean isTableExists = false;

		while (iterator.hasNext())
		{
			List row = (List) iterator.next();
			if (aliasName.equals((String) row.get(0)) && displayName.equals((String) row.get(1)))
			{
				isTableExists = true;
			}
		}

		return isTableExists;
	}

	/**
	 * Returns the display name of the aliasName passed.
	 * @param aliasName aliasName.
	 * @return Returns the display name.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public String getDisplayName(String aliasName) throws DAOException, ClassNotFoundException
	{
		String prevValueDisplayName = null;

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='"
				+ aliasName + "'";
		List list = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!list.isEmpty())
		{
			List rowList = (List) list.get(0);
			prevValueDisplayName = (String) rowList.get(0);
		}
		return prevValueDisplayName;
	}

	/**
	 * gets Display Name by Table Name.
	 * @param tableName tableName
	 * @return prevValueDisplayName.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public String getDisplayNamebyTableName(String tableName) throws DAOException,
			ClassNotFoundException
	{
		String prevValueDisplayName = null;

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where TABLE_NAME='"
				+ tableName + "'";
		List list = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!list.isEmpty())
		{
			List rowList = (List) list.get(0);
			prevValueDisplayName = (String) rowList.get(0);
		}
		return prevValueDisplayName;
	}

	/**
	 * Returns all the tables in the simple query interface.
	 * @param aliasName alias Name.
	 * @param forQI for QI.
	 * @return Returns all the tables in the simple query interface.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public Set getAllTableNames(String aliasName, int forQI) throws DAOException,
			ClassNotFoundException
	{
		String[] selectColumnNames = {Constants.TABLE_DISPLAY_NAME_COLUMN,
				Constants.TABLE_ALIAS_NAME_COLUMN};
		String[] whereColumnNames = {Constants.TABLE_FOR_SQI_COLUMN};
		String[] whereColumnConditions = {"="};
		String[] whereColumnValues = {String.valueOf(forQI)};

		if ((aliasName != null) && (!"".equals(aliasName)))
		{
			whereColumnNames = new String[2];
			whereColumnNames[0] = Constants.TABLE_FOR_SQI_COLUMN;
			whereColumnNames[1] = Constants.TABLE_ALIAS_NAME_COLUMN;
			whereColumnConditions = new String[2];
			whereColumnConditions[0] = "=";
			whereColumnConditions[1] = "=";
			whereColumnValues = new String[2];
			whereColumnValues[0] = String.valueOf(forQI);
			String alias = "'" + aliasName + "'";
			whereColumnValues[1] = alias;
		}

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		List tableList = jdbcDAO.retrieve(Constants.TABLE_DATA_TABLE_NAME, selectColumnNames,
				whereColumnNames, whereColumnConditions, whereColumnValues, null);
		jdbcDAO.closeSession();

		Set objectNameValueBeanList = new TreeSet();
		if (aliasName == null || "".equals(aliasName))
		{
			NameValueBean nameValueBean = new NameValueBean();
			nameValueBean.setValue("-1");
			nameValueBean.setName(Constants.SELECT_OPTION);
			objectNameValueBeanList.add(nameValueBean);
		}

		Iterator objIterator = tableList.iterator();
		while (objIterator.hasNext())
		{
			List row = (List) objIterator.next();
			NameValueBean nameValueBean = new NameValueBean();
			nameValueBean.setName((String) row.get(0));
			nameValueBean.setValue((String) row.get(1));
			objectNameValueBeanList.add(nameValueBean);
		}

		return objectNameValueBeanList;
	}

	/**
	 * Returns all the Main Objects Of Query.
	 * @return Main Objects Of Query
	 * @throws DAOException generic DAOException.
	 */
	public static List getMainObjectsOfQuery() throws DAOException
	{
		String sql = " select alias_name from CATISSUE_QUERY_TABLE_DATA where FOR_SQI=1";

		List list = null;
		List mainObjects = new ArrayList();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(sql, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				mainObjects.add(row.get(0));
				logger.info("Main Objects:" + row.get(0));
			}

		}
		catch (DAOException daoExp)
		{
			logger.debug("Could not obtain main objects. Exception:" + daoExp.getMessage(), daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			logger.debug("Could not obtain main objects. Exception:" + classExp.getMessage(),
					classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
			}
		}
		return mainObjects;
	}

	/**
	 * This method returns all tables that are related to
	 * the aliasname passed as parameter.
	 * @author aarti_sharma
	 * @param aliasName aliasName.
	 * @return related Table Aliases.
	 * @throws DAOException generic DAOException.
	 */
	public static List getRelatedTableAliases(String aliasName) throws DAOException
	{
		List list = null;
		List relatedTableAliases = new ArrayList();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(GET_RELATED_TABLE_ALIAS_PART1 + "'" + aliasName + "'"
					+ GET_RELATED_TABLE_ALIAS_PART2, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				relatedTableAliases.add(row.get(0));
				logger.info("aliasName:" + aliasName + " Related Table: " + row.get(0));
			}

		}
		catch (DAOException daoExp)
		{
			logger.debug("Could not obtain related tables. Exception:" + daoExp.getMessage(),
					daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			logger.debug("Could not obtain related tables. Exception:" + classExp.getMessage(),
					classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				logger.debug(e.getMessage(), e);
			}
		}
		return relatedTableAliases;
	}

	/**
	 * Returns the tables in path depending on the parent table Id and child table Id.
	 * @param parentTableId parent Table Id.
	 * @param childTableId child Table Id.
	 * @return Returns the tables in path.
	 * @throws DAOException generic DAOException
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public Set setTablesInPath(Long parentTableId, Long childTableId) throws DAOException,
			ClassNotFoundException
	{
		String sql = " SELECT TABLES_IN_PATH FROM CATISSUE_TABLE_RELATION WHERE "
				+ " PARENT_TABLE_ID = '" + parentTableId + "' and " + " CHILD_TABLE_ID = '"
				+ childTableId + "'";

		logger.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		Set tablePathSet = new HashSet();
		QueryBizLogic bizLogic = new QueryBizLogic();
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			String tablePath = (String) rowList.get(0);
			logger.debug("tableinpath with ids as from database:" + tablePath);
			StringTokenizer pathIdToken = new StringTokenizer(tablePath, ":");
			logger.debug("no. of tables in path:" + pathIdToken.countTokens());
			while (pathIdToken.hasMoreTokens())
			{
				Long tableIdinPath = Long.valueOf(pathIdToken.nextToken());
				String tableName = bizLogic.getAliasName(Constants.TABLE_ID_COLUMN, tableIdinPath);
				logger.debug("table in path:" + tableName);
				tablePathSet.add(tableName);
			}
		}
		return tablePathSet;
	}

	/**
	 * Returns the attribute type for the given column name and table alias name.
	 * @param columnName Column Name.
	 * @param aliasName aliasName.
	 * @return Returns the attribute type.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public String getAttributeType(String columnName, String aliasName) throws DAOException,
			ClassNotFoundException
	{
		String sql = " select columnData.ATTRIBUTE_TYPE from "
				+ " CATISSUE_INTERFACE_COLUMN_DATA columnData, "
				+ " CATISSUE_QUERY_TABLE_DATA tableData "
				+ " where  columnData.TABLE_ID = tableData.TABLE_ID and "
				+ "  columnData.COLUMN_NAME = '" + columnName + "' and tableData.ALIAS_NAME = '"
				+ aliasName + "' ";
		logger.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();
		String atrributeType = "";
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			atrributeType = (String) rowList.get(0);

		}
		return atrributeType;
	}

	/**
	 * Returns the table Id of the table given the table alias name.
	 * @param aliasName aliasName
	 * @return Returns the table Id
	 * @throws DAOException generic DAOException.
	 */
	public String getTableIdFromAliasName(String aliasName) throws DAOException
	{

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String[] selectColumnNames = {"TABLE_ID"};
		String[] whereColumnNames = {"ALIAS_NAME"};
		String[] whereColumnConditions = {"="};
		String[] whereColumnValues = {"'" + aliasName + "'"};
		List list = jdbcDAO.retrieve("CATISSUE_QUERY_TABLE_DATA", selectColumnNames,
				whereColumnNames, whereColumnConditions, whereColumnValues, null);
		jdbcDAO.closeSession();
		logger.debug("List of Ids size: " + list.size() + " list " + list);
		String tableIdString = "";
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			logger.debug("RowList of Ids size: " + rowList.size() + " Rowlist " + rowList);
			tableIdString = (String) rowList.get(0);
		}
		return tableIdString;
	}

	/**
	 * To get the List of NameValueBean objects of columns corresponding to the table aliasName.
	 * @param aliasName The String representing aliasName of the table.
	 * @return The List of NameValueBean objects, representing Columns corresponding to a table aliasName.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public List setColumnNames(String aliasName) throws DAOException, ClassNotFoundException
	{
		return getColumnNames(aliasName, false);
	}

	/**
	 * To get the List of NameValueBean objects of columns corresponding to the table aliasName.
	 * @param aliasName The String representing aliasName of the table.
	 * @param defaultViewAttributesOnly The boolean value,
	 * which will decide the list of columns in returned list.
	 * If true, it will return only default view attributes corresponding to a table aliasName,
	 * else return all attributes.
	 * @return The List of NameValueBean objects, representing Columns corresponding to a table aliasName.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public List getColumnNames(String aliasName, boolean defaultViewAttributesOnly)
			throws DAOException, ClassNotFoundException
	{
		String sql = getQueryFor(aliasName, defaultViewAttributesOnly);
		return getList(aliasName, sql);
	}

	/**
	 * @param aliasName The String representing alias name of the table.
	 * @param sql The Sql Script to get List of records.
	 * @return The List of NameValueBean objects, obtained by specified sql.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	private List getList(String aliasName, String sql) throws DAOException, ClassNotFoundException
	{
		logger.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();
		String tableName = "";
		String tableDisplayName = "";
		String columnName = "";
		String columnDisplayName = "";
		List columnList = new ArrayList();
		Iterator iterator = list.iterator();
		int j = 0, k = 0;
		while (iterator.hasNext())
		{

			List rowList = (List) iterator.next();
			tableName = (String) rowList.get(j++);
			tableDisplayName = getDisplayName(aliasName);
			columnName = (String) rowList.get(j++);
			columnDisplayName = (String) rowList.get(j++);

			//Name ValueBean Value in the for of tableAlias.columnName.columnDisplayName.tablesInPath.
			String columnValue = tableName + "." + columnName + "." + columnDisplayName + " : "
					+ tableDisplayName;
			String tablesInPath = (String) rowList.get(j++);

			if ((tablesInPath != null) && (!"".equals(tablesInPath)))
			{
				columnValue = columnValue + "." + tablesInPath;
			}

			NameValueBean columns = new NameValueBean(columnDisplayName, columnValue);
			columnList.add(columns);
			j = 0;
			k++;
		}
		return columnList;
	}

	/**
	 * Bug#3549
	 * Patch 1_2
	 * Description:modified query to order the result  by ATTRIBUTE_ORDER column.
	 */
	/**
	 * To get The SQL Query for fetching column names of given aliasName.
	 * @param aliasName The Table alias name.
	 * @param defaultViewAttributesOnly true if user wants only Default view attributes,
	 * else query created will return all column names for given aliasName.
	 * @return The sql query.
	 */
	private String getQueryFor(String aliasName, boolean defaultViewAttributesOnly)
	{
		String sql = " SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME,  temp.DISPLAY_NAME, temp.TABLES_IN_PATH  "
				+ " from CATISSUE_QUERY_TABLE_DATA tableData2 join "
				+ " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, columnData.ATTRIBUTE_TYPE, "
				+ " displayData.DISPLAY_NAME,displayData.ATTRIBUTE_ORDER , relationData.TABLES_IN_PATH "
				+ " FROM CATISSUE_INTERFACE_COLUMN_DATA columnData, "
				+ " CATISSUE_TABLE_RELATION relationData, "
				+ " CATISSUE_QUERY_TABLE_DATA tableData, "
				+ " CATISSUE_SEARCH_DISPLAY_DATA displayData "
				+ " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "
				+ " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
				+ " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
				+ " columnData.IDENTIFIER = displayData.COL_ID and ";

		String sqlConditionForDefaultView = " displayData.DEFAULT_VIEW_ATTRIBUTE = 1 and ";

		String sql1 = " tableData.ALIAS_NAME = '" + aliasName + "') temp "
				+ " on temp.TABLE_ID = tableData2.TABLE_ID ORDER BY temp.ATTRIBUTE_ORDER";

		if (defaultViewAttributesOnly)
		{
			sql = sql + sqlConditionForDefaultView + sql1;
		}
		else
		{
			sql = sql + sql1;
		}

		return sql;
	}

	// For Summary Report Page
	/**
	 * Returns the count of speciman of the type passed.
	 * @param specimanType String of Specimen Type, JDDCDAO object.
	 * @param jdbcDAO JDBCDAO object.
	 * @return String
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public String getSpecimenTypeCount(String specimanType, JDBCDAO jdbcDAO) throws DAOException,
			ClassNotFoundException
	{
		String prevValueDisplayName = "0";
		String sql = "select count(*) from CATISSUE_SPECIMEN specimen "
			    + "join catissue_abstract_specimen absspec "
				+ " on specimen.identifier=absspec.identifier where absspec.SPECIMEN_CLASS = '"
				+ specimanType + "' and specimen.COLLECTION_STATUS = 'Collected'";
		prevValueDisplayName = getPrevValueDisplayName(jdbcDAO, sql);
		return prevValueDisplayName;
	}

	/**
	 * Returns Map which has all the details of Summary Page.
	 * @return Map
	 * @throws DAOException generic DAOException.
	 */

	public Map<String, Object> getTotalSummaryDetails() throws DAOException
	{
		JDBCDAO jdbcDAO = null;
		Map<String, Object> summaryDataMap = null;
		try
		{
			// Database connection established
			jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDAO.openSession(null);

			summaryDataMap = new HashMap<String, Object>();
			summaryDataMap.put("TotalSpecimenCount", getTotalSpecimenCount(jdbcDAO));
			summaryDataMap.put("TissueCount", getSpecimenTypeCount(Constants.TISSUE, jdbcDAO));
			summaryDataMap.put("CellCount", getSpecimenTypeCount(Constants.CELL, jdbcDAO));
			summaryDataMap.put("MoleculeCount", getSpecimenTypeCount(Constants.MOLECULE, jdbcDAO));
			summaryDataMap.put("FluidCount", getSpecimenTypeCount(Constants.FLUID, jdbcDAO));
			summaryDataMap.put("TissueTypeDetails", getSpecimenTypeDetailsCount(Constants.TISSUE,
					jdbcDAO));
			summaryDataMap.put("CellTypeDetails", getSpecimenTypeDetailsCount(Constants.CELL,
					jdbcDAO));
			summaryDataMap.put("MoleculeTypeDetails", getSpecimenTypeDetailsCount(
					Constants.MOLECULE, jdbcDAO));
			summaryDataMap.put("FluidTypeDetails", getSpecimenTypeDetailsCount(Constants.FLUID,
					jdbcDAO));
			summaryDataMap
					.put("TissueQuantity", getSpecimenTypeQuantity(Constants.TISSUE, jdbcDAO));
			summaryDataMap.put("CellQuantity", getSpecimenTypeQuantity(Constants.CELL, jdbcDAO));
			summaryDataMap.put("MoleculeQuantity", getSpecimenTypeQuantity(Constants.MOLECULE,
					jdbcDAO));
			summaryDataMap.put("FluidQuantity", getSpecimenTypeQuantity(Constants.FLUID, jdbcDAO));
		}
		catch (ClassNotFoundException exception)
		{
			logger.error(exception.getMessage(), exception);
		}
		catch (DAOException exception)
		{
			logger.error(exception.getMessage(), exception);
		}
		finally
		{
			jdbcDAO.closeSession();
		}
		return summaryDataMap;
	}

	/**
	 * Returns the count of speciman of the type passed.
	 * @param specimanType speciman Type.
	 * @param jdbcDAO JDBCDAO object.
	 * @return Returns the count of speciman of the type passed.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	private String getSpecimenTypeQuantity(String specimanType, JDBCDAO jdbcDAO)
			throws DAOException, ClassNotFoundException
	{
		String prevValueDisplayName = "0";
		String sql = "select sum(AVAILABLE_QUANTITY) from CATISSUE_SPECIMEN specimen join"
				+ " catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier"
				+ " where absspec.SPECIMEN_CLASS='" + specimanType + "'";
		prevValueDisplayName = getPrevValueDisplayName(jdbcDAO, sql);
		return prevValueDisplayName;
	}

	/**
	 * Returns the Specimen Sub-Type and its available Quantity.
	 * @param specimenType Class whose details are to be retrieved.
	 * @param jdbcDAO JDBCDAO object.
	 * @return Vector of type and count name value bean
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	private List getSpecimenTypeDetailsCount(String specimenType, JDBCDAO jdbcDAO)
			throws DAOException, ClassNotFoundException
	{
		String sql = "select absspec.SPECIMEN_TYPE,COUNT(*) from CATISSUE_SPECIMEN specimen "
				+ "join catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier "
				+ "and specimen.COLLECTION_STATUS='Collected' " + " and absspec.SPECIMEN_CLASS = '"
				+ specimenType + "'group by absspec.SPECIMEN_TYPE "
				+ " order by absspec.SPECIMEN_TYPE";
		List nameValuePairs = null;
		try
		{
			List list = jdbcDAO.executeQuery(sql, null, false, null);
			nameValuePairs = new ArrayList();
			if (!list.isEmpty())
			{
				// Creating name value beans.
				for (int i = 0; i < list.size(); i++)
				{
					List detailList = (List) list.get(i);
					NameValueBean nameValueBean = new NameValueBean();
					nameValueBean.setName((String) detailList.get(0));
					nameValueBean.setValue((String) detailList.get(1));
					logger.debug(i + " : " + nameValueBean.toString());
					nameValuePairs.add(nameValueBean);
				}
			}
		}
		catch (DAOException exception)
		{
			logger.error(exception.getMessage(),exception);
		}
		return nameValuePairs;
	}

	/***
	 * Returns the Total Specimen Count of caTissue.
	 * @param jdbcDAO JDBCDAO object.
	 * @return String
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	private String getTotalSpecimenCount(JDBCDAO jdbcDAO) throws DAOException,
			ClassNotFoundException
	{
		String prevValueDisplayName = "0";
		String sql = "select count(*) from CATISSUE_SPECIMEN specimen join "
				+ "catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier "
				+ "where specimen.COLLECTION_STATUS='Collected'";

		prevValueDisplayName = getPrevValueDisplayName(jdbcDAO, sql);
		return prevValueDisplayName;
	}

	/**
	 * inserts Query For MySQL.
	 * @param sqlQuery sql Query.
	 * @param sessionData session Data.
	 * @param jdbcDAO JDBCDAO object.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public void insertQueryForMySQL(String sqlQuery, SessionDataBean sessionData, JDBCDAO jdbcDAO)
			throws DAOException, ClassNotFoundException
	{
		String sqlQuery1 = sqlQuery.replaceAll("'", "''");
		long number = 1;

		SimpleDateFormat fSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStamp = fSDateFormat.format(new Date());

		String ipAddr = sessionData.getIpAddress();

		String userId = sessionData.getUserId().toString();
		String comments = "QueryLog";

		String sqlForAudiEvent = "insert into catissue_audit_event(IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) values ('"
				+ ipAddr + "','" + timeStamp + "','" + userId + "','" + comments + "')";
		jdbcDAO.executeUpdate(sqlForAudiEvent);

		String sql = "select max(identifier) from catissue_audit_event where USER_ID='" + userId
				+ "'";

		number = getQueryNumber(jdbcDAO, sql);

		String sqlForQueryLog = "insert into catissue_audit_event_query_log(QUERY_DETAILS,AUDIT_EVENT_ID) values ('"
				+ sqlQuery1 + "','" + number + "')";
		jdbcDAO.executeUpdate(sqlForQueryLog);

	}

	/**
	 * inserts Query For Oracle.
	 * @param sqlQuery sql Query.
	 * @param sessionData session Data
	 * @param jdbcDAO JDBCDAO object.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void insertQueryForOracle(String sqlQuery, SessionDataBean sessionData, JDBCDAO jdbcDAO)
			throws DAOException, ClassNotFoundException, SQLException, IOException
	{
		String sqlQuery1 = sqlQuery.replaceAll("'", "''");
		long no = 1;
		SimpleDateFormat fSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStamp = fSDateFormat.format(new Date());

		String ipAddr = sessionData.getIpAddress();

		String userId = sessionData.getUserId().toString();
		String comments = "QueryLog";

		String sql = "select CATISSUE_AUDIT_EVENT_PARAM_SEQ.nextVal from dual";

		List list = jdbcDAO.executeQuery(sql, null, false, null);

		if (!list.isEmpty())
		{
			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				if (!"".equals(str))
				{
					no = Long.parseLong(str);

				}
			}
		}
		String sqlForAudiEvent = "insert into catissue_audit_event(IDENTIFIER,IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) values ('"
				+ no
				+ "','"
				+ ipAddr
				+ "',to_date('"
				+ timeStamp
				+ "','yyyy-mm-dd HH24:MI:SS'),'"
				+ userId + "','" + comments + "')";
		Logger.out.info("sqlForAuditLog:" + sqlForAudiEvent);
		jdbcDAO.executeUpdate(sqlForAudiEvent);

		long queryNo = 1;
		sql = "select CATISSUE_AUDIT_EVENT_QUERY_SEQ.nextVal from dual";
		queryNo = getQueryNumber(jdbcDAO, sql);

		String sqlForQueryLog = "insert into catissue_audit_event_query_log(IDENTIFIER,QUERY_DETAILS,AUDIT_EVENT_ID) "
				+ "values (" + queryNo + ",EMPTY_CLOB(),'" + no + "')";
		jdbcDAO.executeUpdate(sqlForQueryLog);
		String sql1 = "select QUERY_DETAILS from catissue_audit_event_query_log where IDENTIFIER="
				+ queryNo + " for update";
		list = jdbcDAO.executeQuery(sql1, null, false, null);

		CLOB clob = null;

		if (!list.isEmpty())
		{

			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				clob = (CLOB) columnList.get(0);
			}
		}
		//		get output stream from the CLOB object
		OutputStream os = clob.getAsciiOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);

		//	use that output stream to write character data to the Oracle data store
		osw.write(sqlQuery1.toCharArray());
		//write data and commit
		osw.flush();
		osw.close();
		os.close();
		jdbcDAO.commit();
		logger.info("sqlForQueryLog:" + sqlForQueryLog);

	}

	/**
	 * Inserts Query.
	 * @param sqlQuery sql Query.
	 * @param sessionData session Data.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public void insertQuery(String sqlQuery, SessionDataBean sessionData) throws DAOException,
			ClassNotFoundException
	{

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{

			jdbcDAO.openSession(null);
			if (Variables.databaseName.equals(Constants.MYSQL_DATABASE))
			{
				insertQueryForMySQL(sqlQuery, sessionData, jdbcDAO);

			}
			else
			{

				insertQueryForOracle(sqlQuery, sessionData, jdbcDAO);
			}
		}
		catch (IOException e)
		{
			throw new DAOException(e.getMessage());
		}
		catch (SQLException e)
		{
			throw new DAOException(e.getMessage());
		}
		catch (DAOException e)
		{
			throw (e);
		}
		finally
		{
			jdbcDAO.closeSession();
		}
	}

	/**
	 * Method to execute the given SQL to get the query result.
	 * @param sessionDataBean reference to SessionDataBean object
	 * @param querySessionData query Session Data.
	 * @param startIndex The Starting index of the result set.
	 * @return The reference to PagenatedResultData, which contains the Query result information.
	 * @throws DAOException generic DAOException.
	 */
	public PagenatedResultData execute(SessionDataBean sessionDataBean,
			QuerySessionData querySessionData, int startIndex) throws DAOException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			dao.openSession(null);
			edu.wustl.common.dao.queryExecutor.PagenatedResultData pagenatedResultData = dao
					.executeQuery(querySessionData.getSql(), sessionDataBean, querySessionData
							.isSecureExecute(), querySessionData.isHasConditionOnIdentifiedField(),
							querySessionData.getQueryResultObjectDataMap(), startIndex,
							querySessionData.getRecordsPerPage());

			return pagenatedResultData;
		}
		catch (DAOException daoExp)
		{
			throw new DAOException(daoExp.getMessage(), daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			throw new DAOException(classExp.getMessage(), classExp);
		}
		finally
		{
			dao.closeSession();
		}
	}

	/**
	 * Executes SQL query.
	 * @param sql sql query.
	 * @return list.
	 * @throws DAOException generic DAOException.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	public static List executeSQL(String sql) throws DAOException, ClassNotFoundException
	{
		logger.debug("SQL to get cardinality between 2 entities... " + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();
		return list;
	}

	/**
	 * Gets Query Number of given sql.
	 * @param jdbcDAO JDBCDAO object.
	 * @param sql sql query.
	 * @return queryNo.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 * @throws DAOException generic DAOException.
	 */
	private Long getQueryNumber(JDBCDAO jdbcDAO, String sql) throws ClassNotFoundException,
			DAOException
	{
		List list = jdbcDAO.executeQuery(sql, null, false, null);
		long queryNo = 1;
		if (!list.isEmpty())
		{

			List columnList = (List) list.get(0);
			if (!columnList.isEmpty())
			{
				String str = (String) columnList.get(0);
				if (!"".equals(str))
				{
					queryNo = Long.parseLong(str);

				}
			}
		}
		return queryNo;
	}

	/**
	 * Gets Previous Value Display Name.
	 * @param jdbcDAO JDBCDAO object.
	 * @param sql sql query.
	 * @return Previous Value Display Name.
	 * @throws ClassNotFoundException Class Not Found Exception.
	 */
	private String getPrevValueDisplayName(JDBCDAO jdbcDAO, String sql)
			throws ClassNotFoundException
	{
		String prevValueDisplayName = "0";
		try
		{
			List list = jdbcDAO.executeQuery(sql, null, false, null);

			if (!list.isEmpty())
			{
				List rowList = (List) list.get(0);
				prevValueDisplayName = (String) rowList.get(0);
			}
		}
		catch (DAOException exception)
		{
			logger.error(exception.getMessage(), exception);
		}
		return prevValueDisplayName;
	}
}