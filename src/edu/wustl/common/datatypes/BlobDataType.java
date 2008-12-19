/**
 *
 */
package edu.wustl.common.datatypes;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;

import org.apache.struts.action.ActionErrors;
import org.hibernate.Hibernate;


/**
 * @author prashant_bandal
 *
 */
public class BlobDataType implements IDBDataType
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.datatypes.IDBDataType
	 * #validate(java.lang.String,
	 * org.apache.struts.action.ActionErrors)
	 */
	/**
	 * This method validate entered values.
	 * @param enteredValue entered Value.
	 * @param errors errors.
	 * @return conditionError boolean value.
	 */
	public boolean validate(String enteredValue, ActionErrors errors)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * get Object Value.
	 * @param str string value
	 * @return Object.
	 * @throws ParseException Parse Exception
	 * @throws IOException IOException
	 */
	public Object getObjectValue(String str)throws ParseException, IOException
	{
		File file = new File(str);
		DataInputStream dis = new DataInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		byte[] buff = new byte[(int) file.length()];
		dis.readFully(buff);
		dis.close();
		return Hibernate.createBlob(buff);
	}

}