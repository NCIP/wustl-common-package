package edu.wustl.common.security.exceptions;

/**
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class SMException extends Exception
{

	/**
	 * serial version id.
	 */
	private static final long serialVersionUID = 1998965888442573900L;

	/**
	 * No argument constructor.
	 */
	public SMException()
	{
		super();
	}

	/**
	 * @param message
	 */
	public SMException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SMException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public SMException(Throwable cause)
	{
		super(cause);
	}
}
