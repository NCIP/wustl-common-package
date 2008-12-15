package edu.wustl.common.util.impexp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.DatabaseUtility;

/**
 * This class is for import/export meta data for Oracle.
 * @author ravi_kumar
 *
 */
public class OracleAutomateImpExp extends AbstractAutomateImpExp
{

	/**
	 * Index for CTL file name.
	 */
	private static final int  INDX_FOR_CTL_FILE_PATH=10;

	/**
	 * Index for oracle tns name.
	 */
	private static final int  INDX_FOR_TNS_NAME=11;

	/**
	 * oracle tns name.
	 */
	private String oracleTnsName;

	/**
	 * path of CTL file.
	 */
	private String filePathCTL;

	/**
	 * This method do some process before import/export operation.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	protected void preImpExp(String[] args)  throws ApplicationException
	{
		super.preImpExp(args);
		setOracleTnsName(args[INDX_FOR_TNS_NAME]);
		setFilePathCTL(args[INDX_FOR_CTL_FILE_PATH].replaceAll("\\\\", "//"));
	}

	/**
	 * Method to export meta data.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	public void executeExport(String[] args) throws ApplicationException
	{
		preImpExp(args);
		try
		{
			for (int i = 0; i < getSize(); i++)
			{
				String ctlFilePath = filePathCTL + getTableNamesList().get(i) + ".ctl";
				String csvFilePath = getFilePath() + getTableNamesList().get(i) + ".csv";
				createCTLFiles(csvFilePath, ctlFilePath,
						getTableNamesList().get(i));
			}
		}
		catch(Exception exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("impexp.oraexport.error");
			throw new ApplicationException(errorKey,exception,"OracleAutomateImpExp");
		}

	}
	/**
	 * Method to import meta data.
	 * @param args String array of parameters.
	 * @throws ApplicationException Application Exception
	 */
	public void executeImport(String[] args) throws ApplicationException
	{
		preImpExp(args);
		try
		{
		for (int i = 0; i < getSize(); i++)
		{
			String ctlFilePath = filePathCTL + getTableNamesList().get(i) + ".ctl";
			if (!new File(ctlFilePath).exists())
			{
				String csvFilePath = getFilePath()
					+ getTableNamesList().get(i) + ".csv";
				createCTLFiles(csvFilePath,ctlFilePath,
						getTableNamesList().get(i));
			}
			importDataOracle(ctlFilePath);
		}
		}
		catch(Exception exception)
		{
			ErrorKey errorKey=ErrorKey.getErrorKey("impexp.oraimport.error");
			throw new ApplicationException(errorKey,exception,"OracleAutomateImpExp");
		}

	}

	/**
	 * Method to get database connection.
	 * @return Oracle database connection
	 * @throws SQLException Generic SQL exception.
	 * @throws ClassNotFoundException throws this exception if Driver class not found in class path.
	 */
	public Connection getConnection() throws SQLException, ClassNotFoundException
	{
		Connection connection=null;
		DatabaseUtility dbUtil=getDbUtility();
		Class.forName(dbUtil.getDbDriver());
		String url = new StringBuffer("jdbc:oracle:thin:@").append(dbUtil.getDbServerNname())
						.append(":").append(dbUtil.getDbServerPortNumber())
						.append(":").append(dbUtil.getDbName()).toString();
		connection= DriverManager.getConnection(url, dbUtil.getDbUserName()
						,dbUtil.getDbPassword());
		return connection;
	}

	/**
	 * This method will insert the data to database.
	 * @param fileName File Name
	 * @throws Exception generic exception
	 */
	private void importDataOracle(String fileName) throws Exception
	{
		StringBuffer cmd = new StringBuffer("sqlldr ").append(getDbUtility().getDbUserName())
								.append('/').append(getDbUtility().getDbPassword())
								.append('@').append(oracleTnsName)
								.append(" control=").append(fileName);
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(cmd.toString());
		// any error message?
		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream());

		// any output?
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream());
		errorGobbler.start();
		outputGobbler.start();
		proc.waitFor();

	}

	/**
	 * This method will create control file for SQL loader.
	 * @param csvFileName CSV file name.
	 * @param ctlFileName ctl file name
	 * @param tableName table name
	 * @throws IOException Generic IO Exception
	 * @throws SQLException Generic SQL Exception.
	 * @throws ClassNotFoundException throws this exception if Driver class not found in class path.
	 */
	private void createCTLFiles(String csvFileName, String ctlFileName,
			String tableName) throws IOException,  SQLException, ClassNotFoundException
	{
		File file = new File(ctlFileName);
		if (file.exists())
		{
			file.delete();
		}
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(ctlFileName)));
		String value= new StringBuffer("LOAD DATA INFILE '")
			.append(csvFileName)
			.append("' \nBADFILE '/sample.bad'\nDISCARDFILE '/sample.dsc'\nAPPEND \nINTO TABLE ")
			.append(tableName)
			.append(" \nFIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"'\n").toString();

		String columnName = getColumnName(tableName);
		bufferedWriter.write(value + columnName);
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	/**
	 * This method will retrieve the column name list for a given table.
	 * @param tableName Table Name.
	 * @return column Name.
	 * @throws SQLException generic SQL exception.
	 * @throws ClassNotFoundException throws this exception if Driver class not found in class path.
	 */
	private String getColumnName(String tableName) throws SQLException, ClassNotFoundException
	{
		Statement stmt = null;
		ResultSet resultSet = null;
		try
		{
			Connection connection=getConnection();
			String query = "select * from " + tableName + " where 1=2";
			stmt = connection.createStatement();
			resultSet = stmt.executeQuery(query);
			StringBuffer columnNameList = getColumnNameList(resultSet);
			return columnNameList.toString();
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
			if (resultSet != null)
			{
				resultSet.close();
			}

		}
	}

	/**
	 * @param resultSet ResultSet object.
	 * @return list of column name
	 * @throws SQLException Generic SQL Exception.
	 */
	private StringBuffer getColumnNameList(ResultSet resultSet) throws SQLException
	{
		StringBuffer columnNameList = new StringBuffer();
		columnNameList.append('(');
		ResultSetMetaData rsMetaData = resultSet.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		for (int i = 1; i <= numberOfColumns; i++)
		{
			setColList(columnNameList, rsMetaData, i);
		}
		columnNameList.deleteCharAt(columnNameList.length()-1);
		columnNameList.append(')');
		return columnNameList;
	}

	/**
	 * @param columnNameList column Name List.
	 * @param rsMetaData rs Meta Data
	 * @param intVar int Variable
	 * @throws SQLException SQL Exception.
	 */
	private void setColList(StringBuffer columnNameList, ResultSetMetaData rsMetaData, int intVar)
			throws SQLException
	{
		columnNameList.append(rsMetaData.getColumnName(intVar));
		if (Types.DATE == rsMetaData.getColumnType(intVar)
				|| Types.TIMESTAMP == rsMetaData.getColumnType(intVar))
		{
			columnNameList.append(" DATE 'YYYY-MM-DD'");
		}
		if (!("HIDDEN".equals(rsMetaData.getColumnName(intVar)))
				&& !("FORMAT".equals(rsMetaData.getColumnName(intVar))))
		{
			columnNameList.append(" NULLIF ");
			columnNameList.append(rsMetaData.getColumnName(intVar));
			columnNameList.append("='\\\\N'");
		}
		columnNameList.append(',');
	}

	/**
	 * @return the oracleTnsName
	 */
	public String getOracleTnsName()
	{
		return oracleTnsName;
	}

	/**
	 * @param oracleTnsName the oracleTnsName to set
	 */
	public void setOracleTnsName(String oracleTnsName)
	{
		this.oracleTnsName = oracleTnsName;
	}

	/**
	 * @return the filePathCTL
	 */
	public String getFilePathCTL()
	{
		return filePathCTL;
	}

	/**
	 * @param filePathCTL the filePathCTL to set
	 */
	public void setFilePathCTL(String filePathCTL)
	{
		this.filePathCTL = filePathCTL;
	}


	/**
	 *This class is for output of any  message(or error message) during import.
	 *
	 */
	class StreamGobbler extends Thread
	{
		/**
		 * InputStream object.
		 */
		private InputStream inputStream;

		/**
		 * One argument constructor.
		 * @param inputStream InputStream object.
		 */
		StreamGobbler(InputStream inputStream)
		{
			super();
			this.inputStream = inputStream;
		}
	}

}
