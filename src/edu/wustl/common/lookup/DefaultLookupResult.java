/**
 * <p>Title: LookupResult Class>
 * <p>Description:	This contains the matching object with probablity match.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author vaishali_khandelwal
 */

package edu.wustl.common.lookup;

import java.io.Serializable;

public class DefaultLookupResult implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Object object;
	Double probablity;

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object object)
	{
		this.object = object;
	}

	public Double getProbablity()
	{
		return probablity;
	}

	public void setProbablity(Double probablity)
	{
		this.probablity = probablity;
	}

}