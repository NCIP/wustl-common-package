
package edu.wustl.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the action class for sending file to client end.
 * @author Poornima Govindrao
 *
 */
public class SendFile
{

	/**
	 * logger Logger Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SendFile.class);

	public static void sendFileToClient(HttpServletResponse response, String filePath,
			String fileName, String contentType)
	{
		if (filePath != null && (filePath.length()!=0))
		{
			File file = new File(filePath);
			if (file.exists())
			{
				response.setContentType(contentType);
				response.setHeader("Content-Disposition", "attachment;filename=\""
						+ fileName+ "\";");
				response.setContentLength((int) file.length());
				BufferedInputStream bis=null;
				try
				{
					OutputStream opstream = response.getOutputStream();
					bis = new BufferedInputStream(new FileInputStream(file));
					int count;
					byte[] buf = new byte[Constants.FOUR_KILO_BYTES];
					while ((count = bis.read(buf)) > -1)
					{
						opstream.write(buf, 0, count);
					}
					opstream.flush();
					bis.close();
					file.delete();
				}
				catch (FileNotFoundException ex)
				{
					logger.error("Exception in method sendFileToClient:"+ex.getMessage(), ex);
				}
				catch (IOException ex)
				{
					logger.error("Exception in method sendFileToClient:"+ex.getMessage(), ex);
				}
			}
			else
			{
				logger.error("Sorry Cannot Download as fileName is null");
			}
		}
	}
}