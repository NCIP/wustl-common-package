
package edu.wustl.labelSQLApp.bizlogic;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import java.sql.*;
import edu.wustl.labelSQLApp.domain.LabelSQLAssociation;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.wustl.common.beans.NameValueBean;

import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.util.logger.Logger;

public class CommonBizlogic
{

	/**
	 * This method executes the HQL in LabelSQL_HQL.hbm.xml
	 * @param queryName
	 * @param values
	 * @return
	 * @throws HibernateException
	 */
	public static List<?> executeHQL(String queryName, List<Object> values)
			throws HibernateException
	{
		Session session = HibernateUtil.newSession();

		List<?> records = null;
		try
		{
			Query q = session.getNamedQuery(queryName);
			if (values != null)
			{
				for (int counter = 0; counter < values.size(); counter++)
				{
					Object value = values.get(counter);
					String objectType = value.getClass().getSimpleName();

					if ("Long".equals(objectType))
					{
						//sets the value for parameter of Long type
						q.setLong(counter, Long.parseLong(value.toString()));
					}
					else if ("String".equals(objectType))
					{
						//sets the value for parameter of String type
						q.setString(counter, (String)value);
					}
					else
					{
						q.setEntity(counter, value);
					}
				}
			}
			records = q.list();
		}
		finally
		{
			session.close();
		}

		return records;

	}

	/**
	 * Retrieves the label for dashboard item can be predefined label or display name
	 * @param labelSQLAssocId
	 * @return
	 * @throws Exception
	 */
	public String getLabelByLabelSQLAssocId(Long labelSQLAssocId) throws Exception
	{

		List<Object> values = new ArrayList<Object>();
		values.add(labelSQLAssocId);
		values.add(labelSQLAssocId);

		List<?> result = executeHQL("getLabelByLabelSQLAssocId", values);

		if (result.size() != 0)
		{
			return (String) result.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the query associated with label id
	 * @param labelSQLId
	 * @return
	 * @throws Exception
	 */
	public List<String> getQueryByLabelSQLAssocId(Long labelSQLId) throws Exception
	{
		List<Object> values = new ArrayList<Object>();
		values.add(labelSQLId);

		List<?> result = executeHQL("getQueryByLabelSQLAssocId", values);

		return (List<String>) result;
	}

	/**
	 * Returns map with the label/display name and query count
	 * @param CPId
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Integer> getLabelQueryResultMapByCPId(Long CPId) throws Exception
	{
		LinkedHashMap<String, Integer> labelQueryResultMap = new LinkedHashMap<String, Integer>();
		LabelSQLAssociationBizlogic labelSQLAssociationBizlogic = new LabelSQLAssociationBizlogic();

		/*Retrieve all the LabelSQL association associated with the CP/CS*/
		List<LabelSQLAssociation> labelSQLAssociations = labelSQLAssociationBizlogic
				.getLabelSQLAssocCollectionByCPId(CPId);

		for (LabelSQLAssociation labelSQLAssociation : labelSQLAssociations)
		{

			Long labelAssocId = labelSQLAssociation.getId();

			/*insert label and query count in map*/
			String label = getLabelByLabelSQLAssocId(labelAssocId);
			int result = executeQuery((getQueryByLabelSQLAssocId(labelAssocId)).get(0), CPId);

			if (!"".equals(label) && label != null)
			{
				labelQueryResultMap.put(label, result);
			}

		}

		return labelQueryResultMap;
	}

	/**
	 * Returns String with labelSQLAsociation and query result
	 * @param labelSQLAssocID
	 * @return
	 * @throws Exception
	 */
	public String getQueryResultByLabelSQLAssocId(Long labelSQLAssocID) throws Exception
	{
		/*Retrieve all the LabelSQL association associated with the CP/CS*/
		LabelSQLAssociation labelSQLAssociation = new LabelSQLAssociationBizlogic()
				.getLabelSQLAssocById(labelSQLAssocID);

		int result = executeQuery((getQueryByLabelSQLAssocId(labelSQLAssociation.getId())).get(0),
				labelSQLAssociation.getLabelSQLCollectionProtocol());//retrieve the query from labelSQLAssocID and execute it
		String resultString = labelSQLAssocID.toString() + "," + result;
		return resultString;
	}

	/**
	* Executes the stored SQL to give the count
	* @param sql
	* @param CPId
	* @return
	* @throws Exception
	*/
	public int executeQuery(String sql, Long CPId) throws Exception
	{
		Session session = HibernateUtil.newSession();
		if(sql.endsWith(";")){
			sql = sql.substring(0, sql.length()-1);
		}
		PreparedStatement stmt = session.connection().prepareStatement(sql);
		ResultSet rs = null;
		try
		{
			stmt.setLong(1, CPId);
			rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(1);
		}
		catch (Exception e)
		{
			Logger.out.error("Error executing query -> " + sql);
			e.printStackTrace();
			return -1;
		}
		finally
		{
			session.close();

		}
	}

}
