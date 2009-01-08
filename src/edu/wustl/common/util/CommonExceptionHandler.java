
package edu.wustl.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 *
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class CommonExceptionHandler extends ExceptionHandler
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(CommonExceptionHandler.class);
	/**
	 * Retrieve the error details from request and set it in session.
	 * @param exception Exception generic exception.
	 * @param exConfig ExceptionConfig configuration exception.
	 * @param mapping ActionMapping information about current page.
	 * @param formInstance ActionForm populated form.
	 * @param request HttpServletRequest information about request.
	 * @param response HttpServletResponse information about response.
	 * @throws ServletException servlet exception.
	 * @return the output of execute method.
	 */
	public ActionForward execute(Exception exception, ExceptionConfig exConfig, ActionMapping mapping,
			ActionForm formInstance, HttpServletRequest request, HttpServletResponse response)
			throws ServletException
	{
		logger.error(getErrorMsg(exception), exception);
		StringBuffer mess= new StringBuffer("Unhandled Exception occured in ").
				append(CommonServiceLocator.getInstance().getAppURL()).
				append(" : ").append(exception.getMessage());
		request.getSession().setAttribute(Constants.ERROR_DETAIL,mess);
		return super.execute(exception, exConfig, mapping, formInstance, request, response);
	}

	/**
	* @param exception the Exception.
	* @return the string of the error message.
	*/
	public String getErrorMsg(Exception exception)
	{
		StringBuffer msg=new StringBuffer();
		if (null==exception)
		{
			msg.append("Exception was NULL");
		}
		else
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter printWriter = new PrintWriter(baos, true);
			exception.printStackTrace(printWriter);
			msg.append("Unhandled Exception occured in caTISSUE Core \nMessage: ")
			.append(exception.getMessage())
			.append("\nStackTrace: ").append(baos.toString());
		}
		return msg.toString();
	}

}
