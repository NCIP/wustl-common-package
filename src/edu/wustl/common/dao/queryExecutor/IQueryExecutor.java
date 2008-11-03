package edu.wustl.common.dao.queryExecutor;

import java.sql.Connection;
import java.util.Map;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.dbmanager.DAOException;


public interface IQueryExecutor 
{
	
	/**
	 * Constants required for forming/changing SQL.
	 */
	public static final String SELECT_CLAUSE = "SELECT";
	public static final String FROM_CLAUSE = "FROM";
	public IQueryExecutor getInstance();
	public PagenatedResultData getQueryResultList(String query,
			Connection connection, SessionDataBean sessionDataBean,
			boolean isSecureExecute, boolean hasConditionOnIdentifiedField,
			Map queryResultObjectDataMap, int startIndex, int noOfRecords) throws DAOException;
}
